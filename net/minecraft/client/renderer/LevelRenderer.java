package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.particle.Particle;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.core.Registry;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.item.RecordItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.border.WorldBorder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.math.Vector3f;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Iterator;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Option;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import java.util.List;
import java.util.Queue;
import net.minecraft.core.Vec3i;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.entity.Entity;
import net.minecraft.Util;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.network.chat.Component;
import net.minecraft.CrashReport;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.network.chat.TextComponent;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockAndTintGetter;
import java.util.Random;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.Biome;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.util.Mth;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.google.common.collect.Sets;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.CloudStatus;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import java.util.Map;
import java.util.SortedSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.server.level.BlockDestructionProgress;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.world.level.block.entity.BlockEntity;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class LevelRenderer implements ResourceManagerReloadListener, AutoCloseable {
    private static final Logger LOGGER;
    private static final ResourceLocation MOON_LOCATION;
    private static final ResourceLocation SUN_LOCATION;
    private static final ResourceLocation CLOUDS_LOCATION;
    private static final ResourceLocation END_SKY_LOCATION;
    private static final ResourceLocation FORCEFIELD_LOCATION;
    private static final ResourceLocation RAIN_LOCATION;
    private static final ResourceLocation SNOW_LOCATION;
    public static final Direction[] DIRECTIONS;
    private final Minecraft minecraft;
    private final TextureManager textureManager;
    private final EntityRenderDispatcher entityRenderDispatcher;
    private final RenderBuffers renderBuffers;
    private ClientLevel level;
    private Set<ChunkRenderDispatcher.RenderChunk> chunksToCompile;
    private final ObjectList<RenderChunkInfo> renderChunks;
    private final Set<BlockEntity> globalBlockEntities;
    private ViewArea viewArea;
    private final VertexFormat skyFormat;
    @Nullable
    private VertexBuffer starBuffer;
    @Nullable
    private VertexBuffer skyBuffer;
    @Nullable
    private VertexBuffer darkBuffer;
    private boolean generateClouds;
    @Nullable
    private VertexBuffer cloudBuffer;
    private final RunningTrimmedMean frameTimes;
    private int ticks;
    private final Int2ObjectMap<BlockDestructionProgress> destroyingBlocks;
    private final Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;
    private final Map<BlockPos, SoundInstance> playingRecords;
    @Nullable
    private RenderTarget entityTarget;
    @Nullable
    private PostChain entityEffect;
    @Nullable
    private RenderTarget translucentTarget;
    @Nullable
    private RenderTarget itemEntityTarget;
    @Nullable
    private RenderTarget particlesTarget;
    @Nullable
    private RenderTarget weatherTarget;
    @Nullable
    private RenderTarget cloudsTarget;
    @Nullable
    private PostChain transparencyChain;
    private double lastCameraX;
    private double lastCameraY;
    private double lastCameraZ;
    private int lastCameraChunkX;
    private int lastCameraChunkY;
    private int lastCameraChunkZ;
    private double prevCamX;
    private double prevCamY;
    private double prevCamZ;
    private double prevCamRotX;
    private double prevCamRotY;
    private int prevCloudX;
    private int prevCloudY;
    private int prevCloudZ;
    private Vec3 prevCloudColor;
    private CloudStatus prevCloudsType;
    private ChunkRenderDispatcher chunkRenderDispatcher;
    private final VertexFormat format;
    private int lastViewDistance;
    private int renderedEntities;
    private int culledEntities;
    private boolean captureFrustum;
    @Nullable
    private Frustum capturedFrustum;
    private final Vector4f[] frustumPoints;
    private final Vector3d frustumPos;
    private double xTransparentOld;
    private double yTransparentOld;
    private double zTransparentOld;
    private boolean needsUpdate;
    private int frameId;
    private int rainSoundTime;
    private final float[] rainSizeX;
    private final float[] rainSizeZ;
    
    public LevelRenderer(final Minecraft djw, final RenderBuffers eae) {
        this.chunksToCompile = (Set<ChunkRenderDispatcher.RenderChunk>)Sets.newLinkedHashSet();
        this.renderChunks = (ObjectList<RenderChunkInfo>)new ObjectArrayList(69696);
        this.globalBlockEntities = (Set<BlockEntity>)Sets.newHashSet();
        this.skyFormat = DefaultVertexFormat.POSITION;
        this.generateClouds = true;
        this.frameTimes = new RunningTrimmedMean(100);
        this.destroyingBlocks = (Int2ObjectMap<BlockDestructionProgress>)new Int2ObjectOpenHashMap();
        this.destructionProgress = (Long2ObjectMap<SortedSet<BlockDestructionProgress>>)new Long2ObjectOpenHashMap();
        this.playingRecords = (Map<BlockPos, SoundInstance>)Maps.newHashMap();
        this.lastCameraX = Double.MIN_VALUE;
        this.lastCameraY = Double.MIN_VALUE;
        this.lastCameraZ = Double.MIN_VALUE;
        this.lastCameraChunkX = Integer.MIN_VALUE;
        this.lastCameraChunkY = Integer.MIN_VALUE;
        this.lastCameraChunkZ = Integer.MIN_VALUE;
        this.prevCamX = Double.MIN_VALUE;
        this.prevCamY = Double.MIN_VALUE;
        this.prevCamZ = Double.MIN_VALUE;
        this.prevCamRotX = Double.MIN_VALUE;
        this.prevCamRotY = Double.MIN_VALUE;
        this.prevCloudX = Integer.MIN_VALUE;
        this.prevCloudY = Integer.MIN_VALUE;
        this.prevCloudZ = Integer.MIN_VALUE;
        this.prevCloudColor = Vec3.ZERO;
        this.format = DefaultVertexFormat.BLOCK;
        this.lastViewDistance = -1;
        this.frustumPoints = new Vector4f[8];
        this.frustumPos = new Vector3d(0.0, 0.0, 0.0);
        this.needsUpdate = true;
        this.rainSizeX = new float[1024];
        this.rainSizeZ = new float[1024];
        this.minecraft = djw;
        this.entityRenderDispatcher = djw.getEntityRenderDispatcher();
        this.renderBuffers = eae;
        this.textureManager = djw.getTextureManager();
        for (int integer4 = 0; integer4 < 32; ++integer4) {
            for (int integer5 = 0; integer5 < 32; ++integer5) {
                final float float6 = (float)(integer5 - 16);
                final float float7 = (float)(integer4 - 16);
                final float float8 = Mth.sqrt(float6 * float6 + float7 * float7);
                this.rainSizeX[integer4 << 5 | integer5] = -float7 / float8;
                this.rainSizeZ[integer4 << 5 | integer5] = float6 / float8;
            }
        }
        this.createStars();
        this.createLightSky();
        this.createDarkSky();
    }
    
    private void renderSnowAndRain(final LightTexture dzx, final float float2, final double double3, final double double4, final double double5) {
        final float float3 = this.minecraft.level.getRainLevel(float2);
        if (float3 <= 0.0f) {
            return;
        }
        dzx.turnOnLightLayer();
        final Level bru11 = this.minecraft.level;
        final int integer12 = Mth.floor(double3);
        final int integer13 = Mth.floor(double4);
        final int integer14 = Mth.floor(double5);
        final Tesselator dfl15 = Tesselator.getInstance();
        final BufferBuilder dfe16 = dfl15.getBuilder();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();
        RenderSystem.normal3f(0.0f, 1.0f, 0.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableDepthTest();
        int integer15 = 5;
        if (Minecraft.useFancyGraphics()) {
            integer15 = 10;
        }
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        int integer16 = -1;
        final float float4 = this.ticks + float2;
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final BlockPos.MutableBlockPos a20 = new BlockPos.MutableBlockPos();
        for (int integer17 = integer14 - integer15; integer17 <= integer14 + integer15; ++integer17) {
            for (int integer18 = integer12 - integer15; integer18 <= integer12 + integer15; ++integer18) {
                final int integer19 = (integer17 - integer14 + 16) * 32 + integer18 - integer12 + 16;
                final double double6 = this.rainSizeX[integer19] * 0.5;
                final double double7 = this.rainSizeZ[integer19] * 0.5;
                a20.set(integer18, 0, integer17);
                final Biome bss28 = bru11.getBiome(a20);
                if (bss28.getPrecipitation() != Biome.Precipitation.NONE) {
                    final int integer20 = bru11.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, a20).getY();
                    int integer21 = integer13 - integer15;
                    int integer22 = integer13 + integer15;
                    if (integer21 < integer20) {
                        integer21 = integer20;
                    }
                    if (integer22 < integer20) {
                        integer22 = integer20;
                    }
                    int integer23 = integer20;
                    if (integer23 < integer13) {
                        integer23 = integer13;
                    }
                    if (integer21 != integer22) {
                        final Random random33 = new Random((long)(integer18 * integer18 * 3121 + integer18 * 45238971 ^ integer17 * integer17 * 418711 + integer17 * 13761));
                        a20.set(integer18, integer21, integer17);
                        final float float5 = bss28.getTemperature(a20);
                        if (float5 >= 0.15f) {
                            if (integer16 != 0) {
                                if (integer16 >= 0) {
                                    dfl15.end();
                                }
                                integer16 = 0;
                                this.minecraft.getTextureManager().bind(LevelRenderer.RAIN_LOCATION);
                                dfe16.begin(7, DefaultVertexFormat.PARTICLE);
                            }
                            final int integer24 = this.ticks + integer18 * integer18 * 3121 + integer18 * 45238971 + integer17 * integer17 * 418711 + integer17 * 13761 & 0x1F;
                            final float float6 = -(integer24 + float2) / 32.0f * (3.0f + random33.nextFloat());
                            final double double8 = integer18 + 0.5f - double3;
                            final double double9 = integer17 + 0.5f - double5;
                            final float float7 = Mth.sqrt(double8 * double8 + double9 * double9) / integer15;
                            final float float8 = ((1.0f - float7 * float7) * 0.5f + 0.5f) * float3;
                            a20.set(integer18, integer23, integer17);
                            final int integer25 = getLightColor(bru11, a20);
                            dfe16.vertex(integer18 - double3 - double6 + 0.5, integer22 - double4, integer17 - double5 - double7 + 0.5).uv(0.0f, integer21 * 0.25f + float6).color(1.0f, 1.0f, 1.0f, float8).uv2(integer25).endVertex();
                            dfe16.vertex(integer18 - double3 + double6 + 0.5, integer22 - double4, integer17 - double5 + double7 + 0.5).uv(1.0f, integer21 * 0.25f + float6).color(1.0f, 1.0f, 1.0f, float8).uv2(integer25).endVertex();
                            dfe16.vertex(integer18 - double3 + double6 + 0.5, integer21 - double4, integer17 - double5 + double7 + 0.5).uv(1.0f, integer22 * 0.25f + float6).color(1.0f, 1.0f, 1.0f, float8).uv2(integer25).endVertex();
                            dfe16.vertex(integer18 - double3 - double6 + 0.5, integer21 - double4, integer17 - double5 - double7 + 0.5).uv(0.0f, integer22 * 0.25f + float6).color(1.0f, 1.0f, 1.0f, float8).uv2(integer25).endVertex();
                        }
                        else {
                            if (integer16 != 1) {
                                if (integer16 >= 0) {
                                    dfl15.end();
                                }
                                integer16 = 1;
                                this.minecraft.getTextureManager().bind(LevelRenderer.SNOW_LOCATION);
                                dfe16.begin(7, DefaultVertexFormat.PARTICLE);
                            }
                            final float float9 = -((this.ticks & 0x1FF) + float2) / 512.0f;
                            final float float6 = (float)(random33.nextDouble() + float4 * 0.01 * (float)random33.nextGaussian());
                            final float float10 = (float)(random33.nextDouble() + float4 * (float)random33.nextGaussian() * 0.001);
                            final double double10 = integer18 + 0.5f - double3;
                            final double double11 = integer17 + 0.5f - double5;
                            final float float8 = Mth.sqrt(double10 * double10 + double11 * double11) / integer15;
                            final float float11 = ((1.0f - float8 * float8) * 0.3f + 0.5f) * float3;
                            a20.set(integer18, integer23, integer17);
                            final int integer26 = getLightColor(bru11, a20);
                            final int integer27 = integer26 >> 16 & 0xFFFF;
                            final int integer28 = (integer26 & 0xFFFF) * 3;
                            final int integer29 = (integer27 * 3 + 240) / 4;
                            final int integer30 = (integer28 * 3 + 240) / 4;
                            dfe16.vertex(integer18 - double3 - double6 + 0.5, integer22 - double4, integer17 - double5 - double7 + 0.5).uv(0.0f + float6, integer21 * 0.25f + float9 + float10).color(1.0f, 1.0f, 1.0f, float11).uv2(integer30, integer29).endVertex();
                            dfe16.vertex(integer18 - double3 + double6 + 0.5, integer22 - double4, integer17 - double5 + double7 + 0.5).uv(1.0f + float6, integer21 * 0.25f + float9 + float10).color(1.0f, 1.0f, 1.0f, float11).uv2(integer30, integer29).endVertex();
                            dfe16.vertex(integer18 - double3 + double6 + 0.5, integer21 - double4, integer17 - double5 + double7 + 0.5).uv(1.0f + float6, integer22 * 0.25f + float9 + float10).color(1.0f, 1.0f, 1.0f, float11).uv2(integer30, integer29).endVertex();
                            dfe16.vertex(integer18 - double3 - double6 + 0.5, integer21 - double4, integer17 - double5 - double7 + 0.5).uv(0.0f + float6, integer22 * 0.25f + float9 + float10).color(1.0f, 1.0f, 1.0f, float11).uv2(integer30, integer29).endVertex();
                        }
                    }
                }
            }
        }
        if (integer16 >= 0) {
            dfl15.end();
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.disableAlphaTest();
        dzx.turnOffLightLayer();
    }
    
    public void tickRain(final Camera djh) {
        final float float3 = this.minecraft.level.getRainLevel(1.0f) / (Minecraft.useFancyGraphics() ? 1.0f : 2.0f);
        if (float3 <= 0.0f) {
            return;
        }
        final Random random4 = new Random(this.ticks * 312987231L);
        final LevelReader brw5 = this.minecraft.level;
        final BlockPos fx6 = new BlockPos(djh.getPosition());
        BlockPos fx7 = null;
        for (int integer8 = (int)(100.0f * float3 * float3) / ((this.minecraft.options.particles == ParticleStatus.DECREASED) ? 2 : 1), integer9 = 0; integer9 < integer8; ++integer9) {
            final int integer10 = random4.nextInt(21) - 10;
            final int integer11 = random4.nextInt(21) - 10;
            final BlockPos fx8 = brw5.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, fx6.offset(integer10, 0, integer11)).below();
            final Biome bss13 = brw5.getBiome(fx8);
            if (fx8.getY() > 0 && fx8.getY() <= fx6.getY() + 10 && fx8.getY() >= fx6.getY() - 10 && bss13.getPrecipitation() == Biome.Precipitation.RAIN && bss13.getTemperature(fx8) >= 0.15f) {
                fx7 = fx8;
                if (this.minecraft.options.particles == ParticleStatus.MINIMAL) {
                    break;
                }
                final double double14 = random4.nextDouble();
                final double double15 = random4.nextDouble();
                final BlockState cee18 = brw5.getBlockState(fx7);
                final FluidState cuu19 = brw5.getFluidState(fx7);
                final VoxelShape dde20 = cee18.getCollisionShape(brw5, fx7);
                final double double16 = dde20.max(Direction.Axis.Y, double14, double15);
                final double double17 = cuu19.getHeight(brw5, fx7);
                final double double18 = Math.max(double16, double17);
                final ParticleOptions hf27 = (cuu19.is(FluidTags.LAVA) || cee18.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(cee18)) ? ParticleTypes.SMOKE : ParticleTypes.RAIN;
                this.minecraft.level.addParticle(hf27, fx7.getX() + double14, fx7.getY() + double18, fx7.getZ() + double15, 0.0, 0.0, 0.0);
            }
        }
        if (fx7 != null && random4.nextInt(3) < this.rainSoundTime++) {
            this.rainSoundTime = 0;
            if (fx7.getY() > fx6.getY() + 1 && brw5.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, fx6).getY() > Mth.floor((float)fx6.getY())) {
                this.minecraft.level.playLocalSound(fx7, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1f, 0.5f, false);
            }
            else {
                this.minecraft.level.playLocalSound(fx7, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2f, 1.0f, false);
            }
        }
    }
    
    public void close() {
        if (this.entityEffect != null) {
            this.entityEffect.close();
        }
        if (this.transparencyChain != null) {
            this.transparencyChain.close();
        }
    }
    
    public void onResourceManagerReload(final ResourceManager acf) {
        this.textureManager.bind(LevelRenderer.FORCEFIELD_LOCATION);
        RenderSystem.texParameter(3553, 10242, 10497);
        RenderSystem.texParameter(3553, 10243, 10497);
        RenderSystem.bindTexture(0);
        this.initOutline();
        if (Minecraft.useShaderTransparency()) {
            this.initTransparency();
        }
    }
    
    public void initOutline() {
        if (this.entityEffect != null) {
            this.entityEffect.close();
        }
        final ResourceLocation vk2 = new ResourceLocation("shaders/post/entity_outline.json");
        try {
            (this.entityEffect = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), vk2)).resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            this.entityTarget = this.entityEffect.getTempTarget("final");
        }
        catch (IOException iOException3) {
            LevelRenderer.LOGGER.warn("Failed to load shader: {}", vk2, iOException3);
            this.entityEffect = null;
            this.entityTarget = null;
        }
        catch (JsonSyntaxException jsonSyntaxException3) {
            LevelRenderer.LOGGER.warn("Failed to parse shader: {}", vk2, jsonSyntaxException3);
            this.entityEffect = null;
            this.entityTarget = null;
        }
    }
    
    private void initTransparency() {
        this.deinitTransparency();
        final ResourceLocation vk2 = new ResourceLocation("shaders/post/transparency.json");
        try {
            final PostChain eab3 = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), vk2);
            eab3.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            final RenderTarget ded4 = eab3.getTempTarget("translucent");
            final RenderTarget ded5 = eab3.getTempTarget("itemEntity");
            final RenderTarget ded6 = eab3.getTempTarget("particles");
            final RenderTarget ded7 = eab3.getTempTarget("weather");
            final RenderTarget ded8 = eab3.getTempTarget("clouds");
            this.transparencyChain = eab3;
            this.translucentTarget = ded4;
            this.itemEntityTarget = ded5;
            this.particlesTarget = ded6;
            this.weatherTarget = ded7;
            this.cloudsTarget = ded8;
        }
        catch (Exception exception3) {
            final String string4 = (exception3 instanceof JsonSyntaxException) ? "parse" : "load";
            final String string5 = "Failed to " + string4 + " shader: " + vk2;
            final TransparencyShaderException b6 = new TransparencyShaderException(string5, (Throwable)exception3);
            if (this.minecraft.getResourcePackRepository().getSelectedIds().size() > 1) {
                Component nr7;
                try {
                    nr7 = new TextComponent(this.minecraft.getResourceManager().getResource(vk2).getSourceName());
                }
                catch (IOException iOException8) {
                    nr7 = null;
                }
                this.minecraft.options.graphicsMode = GraphicsStatus.FANCY;
                this.minecraft.clearResourcePacksOnError((Throwable)b6, nr7);
            }
            else {
                final CrashReport l7 = this.minecraft.fillReport(new CrashReport(string5, (Throwable)b6));
                this.minecraft.options.graphicsMode = GraphicsStatus.FANCY;
                this.minecraft.options.save();
                LevelRenderer.LOGGER.fatal(string5, (Throwable)b6);
                this.minecraft.emergencySave();
                Minecraft.crash(l7);
            }
        }
    }
    
    private void deinitTransparency() {
        if (this.transparencyChain != null) {
            this.transparencyChain.close();
            this.translucentTarget.destroyBuffers();
            this.itemEntityTarget.destroyBuffers();
            this.particlesTarget.destroyBuffers();
            this.weatherTarget.destroyBuffers();
            this.cloudsTarget.destroyBuffers();
            this.transparencyChain = null;
            this.translucentTarget = null;
            this.itemEntityTarget = null;
            this.particlesTarget = null;
            this.weatherTarget = null;
            this.cloudsTarget = null;
        }
    }
    
    public void doEntityOutline() {
        if (this.shouldShowEntityOutlines()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            this.entityTarget.blitToScreen(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), false);
            RenderSystem.disableBlend();
        }
    }
    
    protected boolean shouldShowEntityOutlines() {
        return this.entityTarget != null && this.entityEffect != null && this.minecraft.player != null;
    }
    
    private void createDarkSky() {
        final Tesselator dfl2 = Tesselator.getInstance();
        final BufferBuilder dfe3 = dfl2.getBuilder();
        if (this.darkBuffer != null) {
            this.darkBuffer.close();
        }
        this.darkBuffer = new VertexBuffer(this.skyFormat);
        this.drawSkyHemisphere(dfe3, -16.0f, true);
        dfe3.end();
        this.darkBuffer.upload(dfe3);
    }
    
    private void createLightSky() {
        final Tesselator dfl2 = Tesselator.getInstance();
        final BufferBuilder dfe3 = dfl2.getBuilder();
        if (this.skyBuffer != null) {
            this.skyBuffer.close();
        }
        this.skyBuffer = new VertexBuffer(this.skyFormat);
        this.drawSkyHemisphere(dfe3, 16.0f, false);
        dfe3.end();
        this.skyBuffer.upload(dfe3);
    }
    
    private void drawSkyHemisphere(final BufferBuilder dfe, final float float2, final boolean boolean3) {
        final int integer5 = 64;
        final int integer6 = 6;
        dfe.begin(7, DefaultVertexFormat.POSITION);
        for (int integer7 = -384; integer7 <= 384; integer7 += 64) {
            for (int integer8 = -384; integer8 <= 384; integer8 += 64) {
                float float3 = (float)integer7;
                float float4 = (float)(integer7 + 64);
                if (boolean3) {
                    float4 = (float)integer7;
                    float3 = (float)(integer7 + 64);
                }
                dfe.vertex(float3, float2, integer8).endVertex();
                dfe.vertex(float4, float2, integer8).endVertex();
                dfe.vertex(float4, float2, integer8 + 64).endVertex();
                dfe.vertex(float3, float2, integer8 + 64).endVertex();
            }
        }
    }
    
    private void createStars() {
        final Tesselator dfl2 = Tesselator.getInstance();
        final BufferBuilder dfe3 = dfl2.getBuilder();
        if (this.starBuffer != null) {
            this.starBuffer.close();
        }
        this.starBuffer = new VertexBuffer(this.skyFormat);
        this.drawStars(dfe3);
        dfe3.end();
        this.starBuffer.upload(dfe3);
    }
    
    private void drawStars(final BufferBuilder dfe) {
        final Random random3 = new Random(10842L);
        dfe.begin(7, DefaultVertexFormat.POSITION);
        for (int integer4 = 0; integer4 < 1500; ++integer4) {
            double double5 = random3.nextFloat() * 2.0f - 1.0f;
            double double6 = random3.nextFloat() * 2.0f - 1.0f;
            double double7 = random3.nextFloat() * 2.0f - 1.0f;
            final double double8 = 0.15f + random3.nextFloat() * 0.1f;
            double double9 = double5 * double5 + double6 * double6 + double7 * double7;
            if (double9 < 1.0 && double9 > 0.01) {
                double9 = 1.0 / Math.sqrt(double9);
                double5 *= double9;
                double6 *= double9;
                double7 *= double9;
                final double double10 = double5 * 100.0;
                final double double11 = double6 * 100.0;
                final double double12 = double7 * 100.0;
                final double double13 = Math.atan2(double5, double7);
                final double double14 = Math.sin(double13);
                final double double15 = Math.cos(double13);
                final double double16 = Math.atan2(Math.sqrt(double5 * double5 + double7 * double7), double6);
                final double double17 = Math.sin(double16);
                final double double18 = Math.cos(double16);
                final double double19 = random3.nextDouble() * 3.141592653589793 * 2.0;
                final double double20 = Math.sin(double19);
                final double double21 = Math.cos(double19);
                for (int integer5 = 0; integer5 < 4; ++integer5) {
                    final double double22 = 0.0;
                    final double double23 = ((integer5 & 0x2) - 1) * double8;
                    final double double24 = ((integer5 + 1 & 0x2) - 1) * double8;
                    final double double25 = 0.0;
                    final double double26 = double23 * double21 - double24 * double20;
                    final double double28;
                    final double double27 = double28 = double24 * double21 + double23 * double20;
                    final double double29 = double26 * double17 + 0.0 * double18;
                    final double double30 = 0.0 * double17 - double26 * double18;
                    final double double31 = double30 * double14 - double28 * double15;
                    final double double32 = double29;
                    final double double33 = double28 * double14 + double30 * double15;
                    dfe.vertex(double10 + double31, double11 + double32, double12 + double33).endVertex();
                }
            }
        }
    }
    
    public void setLevel(@Nullable final ClientLevel dwl) {
        this.lastCameraX = Double.MIN_VALUE;
        this.lastCameraY = Double.MIN_VALUE;
        this.lastCameraZ = Double.MIN_VALUE;
        this.lastCameraChunkX = Integer.MIN_VALUE;
        this.lastCameraChunkY = Integer.MIN_VALUE;
        this.lastCameraChunkZ = Integer.MIN_VALUE;
        this.entityRenderDispatcher.setLevel(dwl);
        this.level = dwl;
        if (dwl != null) {
            this.allChanged();
        }
        else {
            this.chunksToCompile.clear();
            this.renderChunks.clear();
            if (this.viewArea != null) {
                this.viewArea.releaseAllBuffers();
                this.viewArea = null;
            }
            if (this.chunkRenderDispatcher != null) {
                this.chunkRenderDispatcher.dispose();
            }
            this.chunkRenderDispatcher = null;
            this.globalBlockEntities.clear();
        }
    }
    
    public void allChanged() {
        if (this.level == null) {
            return;
        }
        if (Minecraft.useShaderTransparency()) {
            this.initTransparency();
        }
        else {
            this.deinitTransparency();
        }
        this.level.clearTintCaches();
        if (this.chunkRenderDispatcher == null) {
            this.chunkRenderDispatcher = new ChunkRenderDispatcher(this.level, this, Util.backgroundExecutor(), this.minecraft.is64Bit(), this.renderBuffers.fixedBufferPack());
        }
        else {
            this.chunkRenderDispatcher.setLevel(this.level);
        }
        this.needsUpdate = true;
        this.generateClouds = true;
        ItemBlockRenderTypes.setFancy(Minecraft.useFancyGraphics());
        this.lastViewDistance = this.minecraft.options.renderDistance;
        if (this.viewArea != null) {
            this.viewArea.releaseAllBuffers();
        }
        this.resetChunksToCompile();
        synchronized (this.globalBlockEntities) {
            this.globalBlockEntities.clear();
        }
        this.viewArea = new ViewArea(this.chunkRenderDispatcher, this.level, this.minecraft.options.renderDistance, this);
        if (this.level != null) {
            final Entity apx2 = this.minecraft.getCameraEntity();
            if (apx2 != null) {
                this.viewArea.repositionCamera(apx2.getX(), apx2.getZ());
            }
        }
    }
    
    protected void resetChunksToCompile() {
        this.chunksToCompile.clear();
        this.chunkRenderDispatcher.blockUntilClear();
    }
    
    public void resize(final int integer1, final int integer2) {
        this.needsUpdate();
        if (this.entityEffect != null) {
            this.entityEffect.resize(integer1, integer2);
        }
        if (this.transparencyChain != null) {
            this.transparencyChain.resize(integer1, integer2);
        }
    }
    
    public String getChunkStatistics() {
        final int integer2 = this.viewArea.chunks.length;
        final int integer3 = this.countRenderedChunks();
        return String.format("C: %d/%d %sD: %d, %s", new Object[] { integer3, integer2, this.minecraft.smartCull ? "(s) " : "", this.lastViewDistance, (this.chunkRenderDispatcher == null) ? "null" : this.chunkRenderDispatcher.getStats() });
    }
    
    protected int countRenderedChunks() {
        int integer2 = 0;
        for (final RenderChunkInfo a4 : this.renderChunks) {
            if (!a4.chunk.getCompiledChunk().hasNoRenderableLayers()) {
                ++integer2;
            }
        }
        return integer2;
    }
    
    public String getEntityStatistics() {
        return new StringBuilder().append("E: ").append(this.renderedEntities).append("/").append(this.level.getEntityCount()).append(", B: ").append(this.culledEntities).toString();
    }
    
    private void setupRender(final Camera djh, final Frustum ecr, final boolean boolean3, final int integer, final boolean boolean5) {
        final Vec3 dck7 = djh.getPosition();
        if (this.minecraft.options.renderDistance != this.lastViewDistance) {
            this.allChanged();
        }
        this.level.getProfiler().push("camera");
        final double double8 = this.minecraft.player.getX() - this.lastCameraX;
        final double double9 = this.minecraft.player.getY() - this.lastCameraY;
        final double double10 = this.minecraft.player.getZ() - this.lastCameraZ;
        if (this.lastCameraChunkX != this.minecraft.player.xChunk || this.lastCameraChunkY != this.minecraft.player.yChunk || this.lastCameraChunkZ != this.minecraft.player.zChunk || double8 * double8 + double9 * double9 + double10 * double10 > 16.0) {
            this.lastCameraX = this.minecraft.player.getX();
            this.lastCameraY = this.minecraft.player.getY();
            this.lastCameraZ = this.minecraft.player.getZ();
            this.lastCameraChunkX = this.minecraft.player.xChunk;
            this.lastCameraChunkY = this.minecraft.player.yChunk;
            this.lastCameraChunkZ = this.minecraft.player.zChunk;
            this.viewArea.repositionCamera(this.minecraft.player.getX(), this.minecraft.player.getZ());
        }
        this.chunkRenderDispatcher.setCamera(dck7);
        this.level.getProfiler().popPush("cull");
        this.minecraft.getProfiler().popPush("culling");
        final BlockPos fx14 = djh.getBlockPosition();
        final ChunkRenderDispatcher.RenderChunk c15 = this.viewArea.getRenderChunkAt(fx14);
        final int integer2 = 16;
        final BlockPos fx15 = new BlockPos(Mth.floor(dck7.x / 16.0) * 16, Mth.floor(dck7.y / 16.0) * 16, Mth.floor(dck7.z / 16.0) * 16);
        final float float18 = djh.getXRot();
        final float float19 = djh.getYRot();
        this.needsUpdate = (this.needsUpdate || !this.chunksToCompile.isEmpty() || dck7.x != this.prevCamX || dck7.y != this.prevCamY || dck7.z != this.prevCamZ || float18 != this.prevCamRotX || float19 != this.prevCamRotY);
        this.prevCamX = dck7.x;
        this.prevCamY = dck7.y;
        this.prevCamZ = dck7.z;
        this.prevCamRotX = float18;
        this.prevCamRotY = float19;
        this.minecraft.getProfiler().popPush("update");
        if (!boolean3 && this.needsUpdate) {
            this.needsUpdate = false;
            this.renderChunks.clear();
            final Queue<RenderChunkInfo> queue20 = (Queue<RenderChunkInfo>)Queues.newArrayDeque();
            Entity.setViewScale(Mth.clamp(this.minecraft.options.renderDistance / 8.0, 1.0, 2.5) * this.minecraft.options.entityDistanceScaling);
            boolean boolean6 = this.minecraft.smartCull;
            if (c15 == null) {
                final int integer3 = (fx14.getY() > 0) ? 248 : 8;
                final int integer4 = Mth.floor(dck7.x / 16.0) * 16;
                final int integer5 = Mth.floor(dck7.z / 16.0) * 16;
                final List<RenderChunkInfo> list25 = (List<RenderChunkInfo>)Lists.newArrayList();
                for (int integer6 = -this.lastViewDistance; integer6 <= this.lastViewDistance; ++integer6) {
                    for (int integer7 = -this.lastViewDistance; integer7 <= this.lastViewDistance; ++integer7) {
                        final ChunkRenderDispatcher.RenderChunk c16 = this.viewArea.getRenderChunkAt(new BlockPos(integer4 + (integer6 << 4) + 8, integer3, integer5 + (integer7 << 4) + 8));
                        if (c16 != null && ecr.isVisible(c16.bb)) {
                            c16.setFrame(integer);
                            list25.add(new RenderChunkInfo(c16, (Direction)null, 0));
                        }
                    }
                }
                list25.sort(Comparator.comparingDouble(a -> fx14.distSqr(a.chunk.getOrigin().offset(8, 8, 8))));
                queue20.addAll((Collection)list25);
            }
            else {
                if (boolean5 && this.level.getBlockState(fx14).isSolidRender(this.level, fx14)) {
                    boolean6 = false;
                }
                c15.setFrame(integer);
                queue20.add(new RenderChunkInfo(c15, (Direction)null, 0));
            }
            this.minecraft.getProfiler().push("iteration");
            while (!queue20.isEmpty()) {
                final RenderChunkInfo a22 = (RenderChunkInfo)queue20.poll();
                final ChunkRenderDispatcher.RenderChunk c17 = a22.chunk;
                final Direction gc24 = a22.sourceDirection;
                this.renderChunks.add(a22);
                for (final Direction gc25 : LevelRenderer.DIRECTIONS) {
                    final ChunkRenderDispatcher.RenderChunk c18 = this.getRelativeFrom(fx15, c17, gc25);
                    if (!boolean6 || !a22.hasDirection(gc25.getOpposite())) {
                        if (!boolean6 || gc24 == null || c17.getCompiledChunk().facesCanSeeEachother(gc24.getOpposite(), gc25)) {
                            if (c18 != null) {
                                if (c18.hasAllNeighbors()) {
                                    if (c18.setFrame(integer)) {
                                        if (ecr.isVisible(c18.bb)) {
                                            final RenderChunkInfo a23 = new RenderChunkInfo(c18, gc25, a22.step + 1);
                                            a23.setDirections(a22.directions, gc25);
                                            queue20.add(a23);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.minecraft.getProfiler().pop();
        }
        this.minecraft.getProfiler().popPush("rebuildNear");
        final Set<ChunkRenderDispatcher.RenderChunk> set20 = this.chunksToCompile;
        this.chunksToCompile = (Set<ChunkRenderDispatcher.RenderChunk>)Sets.newLinkedHashSet();
        final ObjectListIterator iterator = this.renderChunks.iterator();
        while (((Iterator)iterator).hasNext()) {
            final RenderChunkInfo a22 = (RenderChunkInfo)((Iterator)iterator).next();
            final ChunkRenderDispatcher.RenderChunk c17 = a22.chunk;
            if (c17.isDirty() || set20.contains(c17)) {
                this.needsUpdate = true;
                final BlockPos fx16 = c17.getOrigin().offset(8, 8, 8);
                final boolean boolean7 = fx16.distSqr(fx14) < 768.0;
                if (c17.isDirtyFromPlayer() || boolean7) {
                    this.minecraft.getProfiler().push("build near");
                    this.chunkRenderDispatcher.rebuildChunkSync(c17);
                    c17.setNotDirty();
                    this.minecraft.getProfiler().pop();
                }
                else {
                    this.chunksToCompile.add(c17);
                }
            }
        }
        this.chunksToCompile.addAll((Collection)set20);
        this.minecraft.getProfiler().pop();
    }
    
    @Nullable
    private ChunkRenderDispatcher.RenderChunk getRelativeFrom(final BlockPos fx, final ChunkRenderDispatcher.RenderChunk c, final Direction gc) {
        final BlockPos fx2 = c.getRelativeOrigin(gc);
        if (Mth.abs(fx.getX() - fx2.getX()) > this.lastViewDistance * 16) {
            return null;
        }
        if (fx2.getY() < 0 || fx2.getY() >= 256) {
            return null;
        }
        if (Mth.abs(fx.getZ() - fx2.getZ()) > this.lastViewDistance * 16) {
            return null;
        }
        return this.viewArea.getRenderChunkAt(fx2);
    }
    
    private void captureFrustum(final Matrix4f b1, final Matrix4f b2, final double double3, final double double4, final double double5, final Frustum ecr) {
        this.capturedFrustum = ecr;
        final Matrix4f b3 = b2.copy();
        b3.multiply(b1);
        b3.invert();
        this.frustumPos.x = double3;
        this.frustumPos.y = double4;
        this.frustumPos.z = double5;
        this.frustumPoints[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.frustumPoints[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.frustumPoints[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.frustumPoints[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.frustumPoints[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.frustumPoints[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.frustumPoints[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.frustumPoints[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        for (int integer12 = 0; integer12 < 8; ++integer12) {
            this.frustumPoints[integer12].transform(b3);
            this.frustumPoints[integer12].perspectiveDivide();
        }
    }
    
    public void renderLevel(final PoseStack dfj, final float float2, final long long3, final boolean boolean4, final Camera djh, final GameRenderer dzr, final LightTexture dzx, final Matrix4f b) {
        BlockEntityRenderDispatcher.instance.prepare(this.level, this.minecraft.getTextureManager(), this.minecraft.font, djh, this.minecraft.hitResult);
        this.entityRenderDispatcher.prepare(this.level, djh, this.minecraft.crosshairPickEntity);
        final ProfilerFiller ant11 = this.level.getProfiler();
        ant11.popPush("light_updates");
        this.minecraft.level.getChunkSource().getLightEngine().runUpdates(Integer.MAX_VALUE, true, true);
        final Vec3 dck12 = djh.getPosition();
        final double double13 = dck12.x();
        final double double14 = dck12.y();
        final double double15 = dck12.z();
        final Matrix4f b2 = dfj.last().pose();
        ant11.popPush("culling");
        final boolean boolean5 = this.capturedFrustum != null;
        Frustum ecr21;
        if (boolean5) {
            ecr21 = this.capturedFrustum;
            ecr21.prepare(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z);
        }
        else {
            ecr21 = new Frustum(b2, b);
            ecr21.prepare(double13, double14, double15);
        }
        this.minecraft.getProfiler().popPush("captureFrustum");
        if (this.captureFrustum) {
            this.captureFrustum(b2, b, dck12.x, dck12.y, dck12.z, boolean5 ? new Frustum(b2, b) : ecr21);
            this.captureFrustum = false;
        }
        ant11.popPush("clear");
        FogRenderer.setupColor(djh, float2, this.minecraft.level, this.minecraft.options.renderDistance, dzr.getDarkenWorldAmount(float2));
        RenderSystem.clear(16640, Minecraft.ON_OSX);
        final float float3 = dzr.getRenderDistance();
        final boolean boolean6 = this.minecraft.level.effects().isFoggyAt(Mth.floor(double13), Mth.floor(double14)) || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        if (this.minecraft.options.renderDistance >= 4) {
            FogRenderer.setupFog(djh, FogRenderer.FogMode.FOG_SKY, float3, boolean6);
            ant11.popPush("sky");
            this.renderSky(dfj, float2);
        }
        ant11.popPush("fog");
        FogRenderer.setupFog(djh, FogRenderer.FogMode.FOG_TERRAIN, Math.max(float3 - 16.0f, 32.0f), boolean6);
        ant11.popPush("terrain_setup");
        this.setupRender(djh, ecr21, boolean5, this.frameId++, this.minecraft.player.isSpectator());
        ant11.popPush("updatechunks");
        final int integer24 = 30;
        final int integer25 = this.minecraft.options.framerateLimit;
        final long long4 = 33333333L;
        long long5;
        if (integer25 == Option.FRAMERATE_LIMIT.getMaxValue()) {
            long5 = 0L;
        }
        else {
            long5 = 1000000000 / integer25;
        }
        final long long6 = Util.getNanos() - long3;
        final long long7 = this.frameTimes.registerValueAndGetMean(long6);
        final long long8 = long7 * 3L / 2L;
        final long long9 = Mth.clamp(long8, long5, 33333333L);
        this.compileChunksUntil(long3 + long9);
        ant11.popPush("terrain");
        this.renderChunkLayer(RenderType.solid(), dfj, double13, double14, double15);
        this.renderChunkLayer(RenderType.cutoutMipped(), dfj, double13, double14, double15);
        this.renderChunkLayer(RenderType.cutout(), dfj, double13, double14, double15);
        if (this.level.effects().constantAmbientLight()) {
            Lighting.setupNetherLevel(dfj.last().pose());
        }
        else {
            Lighting.setupLevel(dfj.last().pose());
        }
        ant11.popPush("entities");
        this.renderedEntities = 0;
        this.culledEntities = 0;
        if (this.itemEntityTarget != null) {
            this.itemEntityTarget.clear(Minecraft.ON_OSX);
            this.itemEntityTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        if (this.weatherTarget != null) {
            this.weatherTarget.clear(Minecraft.ON_OSX);
        }
        if (this.shouldShowEntityOutlines()) {
            this.entityTarget.clear(Minecraft.ON_OSX);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        boolean boolean7 = false;
        final MultiBufferSource.BufferSource a39 = this.renderBuffers.bufferSource();
        for (final Entity apx41 : this.level.entitiesForRendering()) {
            if (!this.entityRenderDispatcher.<Entity>shouldRender(apx41, ecr21, double13, double14, double15) && !apx41.hasIndirectPassenger(this.minecraft.player)) {
                continue;
            }
            if (apx41 == djh.getEntity() && !djh.isDetached()) {
                if (!(djh.getEntity() instanceof LivingEntity)) {
                    continue;
                }
                if (!((LivingEntity)djh.getEntity()).isSleeping()) {
                    continue;
                }
            }
            if (apx41 instanceof LocalPlayer && djh.getEntity() != apx41) {
                continue;
            }
            ++this.renderedEntities;
            if (apx41.tickCount == 0) {
                apx41.xOld = apx41.getX();
                apx41.yOld = apx41.getY();
                apx41.zOld = apx41.getZ();
            }
            MultiBufferSource dzy42;
            if (this.shouldShowEntityOutlines() && this.minecraft.shouldEntityAppearGlowing(apx41)) {
                boolean7 = true;
                final OutlineBufferSource dzz43 = (OutlineBufferSource)(dzy42 = this.renderBuffers.outlineBufferSource());
                final int integer26 = apx41.getTeamColor();
                final int integer27 = 255;
                final int integer28 = integer26 >> 16 & 0xFF;
                final int integer29 = integer26 >> 8 & 0xFF;
                final int integer30 = integer26 & 0xFF;
                dzz43.setColor(integer28, integer29, integer30, 255);
            }
            else {
                dzy42 = a39;
            }
            this.renderEntity(apx41, double13, double14, double15, float2, dfj, dzy42);
        }
        this.checkPoseStack(dfj);
        a39.endBatch(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
        a39.endBatch(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
        a39.endBatch(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
        a39.endBatch(RenderType.entitySmoothCutout(TextureAtlas.LOCATION_BLOCKS));
        ant11.popPush("blockentities");
        for (final RenderChunkInfo a40 : this.renderChunks) {
            final List<BlockEntity> list42 = a40.chunk.getCompiledChunk().getRenderableBlockEntities();
            if (list42.isEmpty()) {
                continue;
            }
            for (final BlockEntity ccg44 : list42) {
                final BlockPos fx45 = ccg44.getBlockPos();
                MultiBufferSource dzy43 = a39;
                dfj.pushPose();
                dfj.translate(fx45.getX() - double13, fx45.getY() - double14, fx45.getZ() - double15);
                final SortedSet<BlockDestructionProgress> sortedSet47 = (SortedSet<BlockDestructionProgress>)this.destructionProgress.get(fx45.asLong());
                if (sortedSet47 != null && !sortedSet47.isEmpty()) {
                    final int integer30 = ((BlockDestructionProgress)sortedSet47.last()).getProgress();
                    if (integer30 >= 0) {
                        final PoseStack.Pose a41 = dfj.last();
                        final VertexConsumer dfn50 = new SheetedDecalTextureGenerator(this.renderBuffers.crumblingBufferSource().getBuffer((RenderType)ModelBakery.DESTROY_TYPES.get(integer30)), a41.pose(), a41.normal());
                        final MultiBufferSource.BufferSource bufferSource;
                        final VertexConsumer dfn51;
                        final VertexConsumer dfn54;
                        dzy43 = (eag -> {
                            dfn51 = bufferSource.getBuffer(eag);
                            if (eag.affectsCrumbling()) {
                                return VertexMultiConsumer.create(dfn54, dfn51);
                            }
                            else {
                                return dfn51;
                            }
                        });
                    }
                }
                BlockEntityRenderDispatcher.instance.<BlockEntity>render(ccg44, float2, dfj, dzy43);
                dfj.popPose();
            }
        }
        synchronized (this.globalBlockEntities) {
            for (final BlockEntity ccg45 : this.globalBlockEntities) {
                final BlockPos fx46 = ccg45.getBlockPos();
                dfj.pushPose();
                dfj.translate(fx46.getX() - double13, fx46.getY() - double14, fx46.getZ() - double15);
                BlockEntityRenderDispatcher.instance.<BlockEntity>render(ccg45, float2, dfj, a39);
                dfj.popPose();
            }
        }
        this.checkPoseStack(dfj);
        a39.endBatch(RenderType.solid());
        a39.endBatch(Sheets.solidBlockSheet());
        a39.endBatch(Sheets.cutoutBlockSheet());
        a39.endBatch(Sheets.bedSheet());
        a39.endBatch(Sheets.shulkerBoxSheet());
        a39.endBatch(Sheets.signSheet());
        a39.endBatch(Sheets.chestSheet());
        this.renderBuffers.outlineBufferSource().endOutlineBatch();
        if (boolean7) {
            this.entityEffect.process(float2);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        ant11.popPush("destroyProgress");
        for (final Long2ObjectMap.Entry<SortedSet<BlockDestructionProgress>> entry41 : this.destructionProgress.long2ObjectEntrySet()) {
            final BlockPos fx47 = BlockPos.of(entry41.getLongKey());
            final double double16 = fx47.getX() - double13;
            final double double17 = fx47.getY() - double14;
            final double double18 = fx47.getZ() - double15;
            if (double16 * double16 + double17 * double17 + double18 * double18 > 1024.0) {
                continue;
            }
            final SortedSet<BlockDestructionProgress> sortedSet48 = (SortedSet<BlockDestructionProgress>)entry41.getValue();
            if (sortedSet48 == null) {
                continue;
            }
            if (sortedSet48.isEmpty()) {
                continue;
            }
            final int integer31 = ((BlockDestructionProgress)sortedSet48.last()).getProgress();
            dfj.pushPose();
            dfj.translate(fx47.getX() - double13, fx47.getY() - double14, fx47.getZ() - double15);
            final PoseStack.Pose a42 = dfj.last();
            final VertexConsumer dfn52 = new SheetedDecalTextureGenerator(this.renderBuffers.crumblingBufferSource().getBuffer((RenderType)ModelBakery.DESTROY_TYPES.get(integer31)), a42.pose(), a42.normal());
            this.minecraft.getBlockRenderer().renderBreakingTexture(this.level.getBlockState(fx47), fx47, this.level, dfj, dfn52);
            dfj.popPose();
        }
        this.checkPoseStack(dfj);
        final HitResult dci40 = this.minecraft.hitResult;
        if (boolean4 && dci40 != null && dci40.getType() == HitResult.Type.BLOCK) {
            ant11.popPush("outline");
            final BlockPos fx48 = ((BlockHitResult)dci40).getBlockPos();
            final BlockState cee42 = this.level.getBlockState(fx48);
            if (!cee42.isAir() && this.level.getWorldBorder().isWithinBounds(fx48)) {
                final VertexConsumer dfn53 = a39.getBuffer(RenderType.lines());
                this.renderHitOutline(dfj, dfn53, djh.getEntity(), double13, double14, double15, fx48, cee42);
            }
        }
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(dfj.last().pose());
        this.minecraft.debugRenderer.render(dfj, a39, double13, double14, double15);
        RenderSystem.popMatrix();
        a39.endBatch(Sheets.translucentCullBlockSheet());
        a39.endBatch(Sheets.bannerSheet());
        a39.endBatch(Sheets.shieldSheet());
        a39.endBatch(RenderType.armorGlint());
        a39.endBatch(RenderType.armorEntityGlint());
        a39.endBatch(RenderType.glint());
        a39.endBatch(RenderType.glintDirect());
        a39.endBatch(RenderType.glintTranslucent());
        a39.endBatch(RenderType.entityGlint());
        a39.endBatch(RenderType.entityGlintDirect());
        a39.endBatch(RenderType.waterMask());
        this.renderBuffers.crumblingBufferSource().endBatch();
        if (this.transparencyChain != null) {
            a39.endBatch(RenderType.lines());
            a39.endBatch();
            this.translucentTarget.clear(Minecraft.ON_OSX);
            this.translucentTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            ant11.popPush("translucent");
            this.renderChunkLayer(RenderType.translucent(), dfj, double13, double14, double15);
            ant11.popPush("string");
            this.renderChunkLayer(RenderType.tripwire(), dfj, double13, double14, double15);
            this.particlesTarget.clear(Minecraft.ON_OSX);
            this.particlesTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            RenderStateShard.PARTICLES_TARGET.setupRenderState();
            ant11.popPush("particles");
            this.minecraft.particleEngine.render(dfj, a39, dzx, djh, float2);
            RenderStateShard.PARTICLES_TARGET.clearRenderState();
        }
        else {
            ant11.popPush("translucent");
            this.renderChunkLayer(RenderType.translucent(), dfj, double13, double14, double15);
            a39.endBatch(RenderType.lines());
            a39.endBatch();
            ant11.popPush("string");
            this.renderChunkLayer(RenderType.tripwire(), dfj, double13, double14, double15);
            ant11.popPush("particles");
            this.minecraft.particleEngine.render(dfj, a39, dzx, djh, float2);
        }
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(dfj.last().pose());
        if (this.minecraft.options.getCloudsType() != CloudStatus.OFF) {
            if (this.transparencyChain != null) {
                this.cloudsTarget.clear(Minecraft.ON_OSX);
                RenderStateShard.CLOUDS_TARGET.setupRenderState();
                ant11.popPush("clouds");
                this.renderClouds(dfj, float2, double13, double14, double15);
                RenderStateShard.CLOUDS_TARGET.clearRenderState();
            }
            else {
                ant11.popPush("clouds");
                this.renderClouds(dfj, float2, double13, double14, double15);
            }
        }
        if (this.transparencyChain != null) {
            RenderStateShard.WEATHER_TARGET.setupRenderState();
            ant11.popPush("weather");
            this.renderSnowAndRain(dzx, float2, double13, double14, double15);
            this.renderWorldBounds(djh);
            RenderStateShard.WEATHER_TARGET.clearRenderState();
            this.transparencyChain.process(float2);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        else {
            RenderSystem.depthMask(false);
            ant11.popPush("weather");
            this.renderSnowAndRain(dzx, float2, double13, double14, double15);
            this.renderWorldBounds(djh);
            RenderSystem.depthMask(true);
        }
        this.renderDebug(djh);
        RenderSystem.shadeModel(7424);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        FogRenderer.setupNoFog();
    }
    
    private void checkPoseStack(final PoseStack dfj) {
        if (!dfj.clear()) {
            throw new IllegalStateException("Pose stack not empty");
        }
    }
    
    private void renderEntity(final Entity apx, final double double2, final double double3, final double double4, final float float5, final PoseStack dfj, final MultiBufferSource dzy) {
        final double double5 = Mth.lerp(float5, apx.xOld, apx.getX());
        final double double6 = Mth.lerp(float5, apx.yOld, apx.getY());
        final double double7 = Mth.lerp(float5, apx.zOld, apx.getZ());
        final float float6 = Mth.lerp(float5, apx.yRotO, apx.yRot);
        this.entityRenderDispatcher.<Entity>render(apx, double5 - double2, double6 - double3, double7 - double4, float6, float5, dfj, dzy, this.entityRenderDispatcher.<Entity>getPackedLightCoords(apx, float5));
    }
    
    private void renderChunkLayer(final RenderType eag, final PoseStack dfj, final double double3, final double double4, final double double5) {
        eag.setupRenderState();
        if (eag == RenderType.translucent()) {
            this.minecraft.getProfiler().push("translucent_sort");
            final double double6 = double3 - this.xTransparentOld;
            final double double7 = double4 - this.yTransparentOld;
            final double double8 = double5 - this.zTransparentOld;
            if (double6 * double6 + double7 * double7 + double8 * double8 > 1.0) {
                this.xTransparentOld = double3;
                this.yTransparentOld = double4;
                this.zTransparentOld = double5;
                int integer16 = 0;
                for (final RenderChunkInfo a18 : this.renderChunks) {
                    if (integer16 < 15 && a18.chunk.resortTransparency(eag, this.chunkRenderDispatcher)) {
                        ++integer16;
                    }
                }
            }
            this.minecraft.getProfiler().pop();
        }
        this.minecraft.getProfiler().push("filterempty");
        this.minecraft.getProfiler().popPush((Supplier<String>)(() -> new StringBuilder().append("render_").append(eag).toString()));
        final boolean boolean10 = eag != RenderType.translucent();
        final ObjectListIterator<RenderChunkInfo> objectListIterator11 = (ObjectListIterator<RenderChunkInfo>)this.renderChunks.listIterator(boolean10 ? 0 : this.renderChunks.size());
        while (true) {
            if (boolean10) {
                if (!objectListIterator11.hasNext()) {
                    break;
                }
            }
            else if (!objectListIterator11.hasPrevious()) {
                break;
            }
            final RenderChunkInfo a19 = (RenderChunkInfo)(boolean10 ? objectListIterator11.next() : ((RenderChunkInfo)objectListIterator11.previous()));
            final ChunkRenderDispatcher.RenderChunk c13 = a19.chunk;
            if (c13.getCompiledChunk().isEmpty(eag)) {
                continue;
            }
            final VertexBuffer dfm14 = c13.getBuffer(eag);
            dfj.pushPose();
            final BlockPos fx15 = c13.getOrigin();
            dfj.translate(fx15.getX() - double3, fx15.getY() - double4, fx15.getZ() - double5);
            dfm14.bind();
            this.format.setupBufferState(0L);
            dfm14.draw(dfj.last().pose(), 7);
            dfj.popPose();
        }
        VertexBuffer.unbind();
        RenderSystem.clearCurrentColor();
        this.format.clearBufferState();
        this.minecraft.getProfiler().pop();
        eag.clearRenderState();
    }
    
    private void renderDebug(final Camera djh) {
        final Tesselator dfl3 = Tesselator.getInstance();
        final BufferBuilder dfe4 = dfl3.getBuilder();
        if (this.minecraft.chunkPath || this.minecraft.chunkVisibility) {
            final double double5 = djh.getPosition().x();
            final double double6 = djh.getPosition().y();
            final double double7 = djh.getPosition().z();
            RenderSystem.depthMask(true);
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableTexture();
            for (final RenderChunkInfo a12 : this.renderChunks) {
                final ChunkRenderDispatcher.RenderChunk c13 = a12.chunk;
                RenderSystem.pushMatrix();
                final BlockPos fx14 = c13.getOrigin();
                RenderSystem.translated(fx14.getX() - double5, fx14.getY() - double6, fx14.getZ() - double7);
                if (this.minecraft.chunkPath) {
                    dfe4.begin(1, DefaultVertexFormat.POSITION_COLOR);
                    RenderSystem.lineWidth(10.0f);
                    final int integer15 = (a12.step == 0) ? 0 : Mth.hsvToRgb(a12.step / 50.0f, 0.9f, 0.9f);
                    final int integer16 = integer15 >> 16 & 0xFF;
                    final int integer17 = integer15 >> 8 & 0xFF;
                    final int integer18 = integer15 & 0xFF;
                    final Direction gc19 = a12.sourceDirection;
                    if (gc19 != null) {
                        dfe4.vertex(8.0, 8.0, 8.0).color(integer16, integer17, integer18, 255).endVertex();
                        dfe4.vertex(8 - 16 * gc19.getStepX(), 8 - 16 * gc19.getStepY(), 8 - 16 * gc19.getStepZ()).color(integer16, integer17, integer18, 255).endVertex();
                    }
                    dfl3.end();
                    RenderSystem.lineWidth(1.0f);
                }
                if (this.minecraft.chunkVisibility && !c13.getCompiledChunk().hasNoRenderableLayers()) {
                    dfe4.begin(1, DefaultVertexFormat.POSITION_COLOR);
                    RenderSystem.lineWidth(10.0f);
                    int integer15 = 0;
                    for (final Direction gc19 : LevelRenderer.DIRECTIONS) {
                        for (final Direction gc20 : LevelRenderer.DIRECTIONS) {
                            final boolean boolean24 = c13.getCompiledChunk().facesCanSeeEachother(gc19, gc20);
                            if (!boolean24) {
                                ++integer15;
                                dfe4.vertex(8 + 8 * gc19.getStepX(), 8 + 8 * gc19.getStepY(), 8 + 8 * gc19.getStepZ()).color(1, 0, 0, 1).endVertex();
                                dfe4.vertex(8 + 8 * gc20.getStepX(), 8 + 8 * gc20.getStepY(), 8 + 8 * gc20.getStepZ()).color(1, 0, 0, 1).endVertex();
                            }
                        }
                    }
                    dfl3.end();
                    RenderSystem.lineWidth(1.0f);
                    if (integer15 > 0) {
                        dfe4.begin(7, DefaultVertexFormat.POSITION_COLOR);
                        final float float16 = 0.5f;
                        final float float17 = 0.2f;
                        dfe4.vertex(0.5, 15.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 15.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 15.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 15.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 0.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 0.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 0.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 0.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 15.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 15.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 0.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 0.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 0.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 0.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 15.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 15.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 0.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 0.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 15.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 15.5, 0.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 15.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 15.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(15.5, 0.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfe4.vertex(0.5, 0.5, 15.5).color(0.9f, 0.9f, 0.0f, 0.2f).endVertex();
                        dfl3.end();
                    }
                }
                RenderSystem.popMatrix();
            }
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableTexture();
        }
        if (this.capturedFrustum != null) {
            RenderSystem.disableCull();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.lineWidth(10.0f);
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)(this.frustumPos.x - djh.getPosition().x), (float)(this.frustumPos.y - djh.getPosition().y), (float)(this.frustumPos.z - djh.getPosition().z));
            RenderSystem.depthMask(true);
            dfe4.begin(7, DefaultVertexFormat.POSITION_COLOR);
            this.addFrustumQuad(dfe4, 0, 1, 2, 3, 0, 1, 1);
            this.addFrustumQuad(dfe4, 4, 5, 6, 7, 1, 0, 0);
            this.addFrustumQuad(dfe4, 0, 1, 5, 4, 1, 1, 0);
            this.addFrustumQuad(dfe4, 2, 3, 7, 6, 0, 0, 1);
            this.addFrustumQuad(dfe4, 0, 4, 7, 3, 0, 1, 0);
            this.addFrustumQuad(dfe4, 1, 5, 6, 2, 1, 0, 1);
            dfl3.end();
            RenderSystem.depthMask(false);
            dfe4.begin(1, DefaultVertexFormat.POSITION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.addFrustumVertex(dfe4, 0);
            this.addFrustumVertex(dfe4, 1);
            this.addFrustumVertex(dfe4, 1);
            this.addFrustumVertex(dfe4, 2);
            this.addFrustumVertex(dfe4, 2);
            this.addFrustumVertex(dfe4, 3);
            this.addFrustumVertex(dfe4, 3);
            this.addFrustumVertex(dfe4, 0);
            this.addFrustumVertex(dfe4, 4);
            this.addFrustumVertex(dfe4, 5);
            this.addFrustumVertex(dfe4, 5);
            this.addFrustumVertex(dfe4, 6);
            this.addFrustumVertex(dfe4, 6);
            this.addFrustumVertex(dfe4, 7);
            this.addFrustumVertex(dfe4, 7);
            this.addFrustumVertex(dfe4, 4);
            this.addFrustumVertex(dfe4, 0);
            this.addFrustumVertex(dfe4, 4);
            this.addFrustumVertex(dfe4, 1);
            this.addFrustumVertex(dfe4, 5);
            this.addFrustumVertex(dfe4, 2);
            this.addFrustumVertex(dfe4, 6);
            this.addFrustumVertex(dfe4, 3);
            this.addFrustumVertex(dfe4, 7);
            dfl3.end();
            RenderSystem.popMatrix();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableTexture();
            RenderSystem.lineWidth(1.0f);
        }
    }
    
    private void addFrustumVertex(final VertexConsumer dfn, final int integer) {
        dfn.vertex(this.frustumPoints[integer].x(), this.frustumPoints[integer].y(), this.frustumPoints[integer].z()).endVertex();
    }
    
    private void addFrustumQuad(final VertexConsumer dfn, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
        final float float10 = 0.25f;
        dfn.vertex(this.frustumPoints[integer2].x(), this.frustumPoints[integer2].y(), this.frustumPoints[integer2].z()).color((float)integer6, (float)integer7, (float)integer8, 0.25f).endVertex();
        dfn.vertex(this.frustumPoints[integer3].x(), this.frustumPoints[integer3].y(), this.frustumPoints[integer3].z()).color((float)integer6, (float)integer7, (float)integer8, 0.25f).endVertex();
        dfn.vertex(this.frustumPoints[integer4].x(), this.frustumPoints[integer4].y(), this.frustumPoints[integer4].z()).color((float)integer6, (float)integer7, (float)integer8, 0.25f).endVertex();
        dfn.vertex(this.frustumPoints[integer5].x(), this.frustumPoints[integer5].y(), this.frustumPoints[integer5].z()).color((float)integer6, (float)integer7, (float)integer8, 0.25f).endVertex();
    }
    
    public void tick() {
        ++this.ticks;
        if (this.ticks % 20 != 0) {
            return;
        }
        final Iterator<BlockDestructionProgress> iterator2 = (Iterator<BlockDestructionProgress>)this.destroyingBlocks.values().iterator();
        while (iterator2.hasNext()) {
            final BlockDestructionProgress zq3 = (BlockDestructionProgress)iterator2.next();
            final int integer4 = zq3.getUpdatedRenderTick();
            if (this.ticks - integer4 > 400) {
                iterator2.remove();
                this.removeProgress(zq3);
            }
        }
    }
    
    private void removeProgress(final BlockDestructionProgress zq) {
        final long long3 = zq.getPos().asLong();
        final Set<BlockDestructionProgress> set5 = (Set<BlockDestructionProgress>)this.destructionProgress.get(long3);
        set5.remove(zq);
        if (set5.isEmpty()) {
            this.destructionProgress.remove(long3);
        }
    }
    
    private void renderEndSky(final PoseStack dfj) {
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        this.textureManager.bind(LevelRenderer.END_SKY_LOCATION);
        final Tesselator dfl3 = Tesselator.getInstance();
        final BufferBuilder dfe4 = dfl3.getBuilder();
        for (int integer5 = 0; integer5 < 6; ++integer5) {
            dfj.pushPose();
            if (integer5 == 1) {
                dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            }
            if (integer5 == 2) {
                dfj.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
            }
            if (integer5 == 3) {
                dfj.mulPose(Vector3f.XP.rotationDegrees(180.0f));
            }
            if (integer5 == 4) {
                dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
            }
            if (integer5 == 5) {
                dfj.mulPose(Vector3f.ZP.rotationDegrees(-90.0f));
            }
            final Matrix4f b6 = dfj.last().pose();
            dfe4.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
            dfe4.vertex(b6, -100.0f, -100.0f, -100.0f).uv(0.0f, 0.0f).color(40, 40, 40, 255).endVertex();
            dfe4.vertex(b6, -100.0f, -100.0f, 100.0f).uv(0.0f, 16.0f).color(40, 40, 40, 255).endVertex();
            dfe4.vertex(b6, 100.0f, -100.0f, 100.0f).uv(16.0f, 16.0f).color(40, 40, 40, 255).endVertex();
            dfe4.vertex(b6, 100.0f, -100.0f, -100.0f).uv(16.0f, 0.0f).color(40, 40, 40, 255).endVertex();
            dfl3.end();
            dfj.popPose();
        }
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
    
    public void renderSky(final PoseStack dfj, final float float2) {
        if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.END) {
            this.renderEndSky(dfj);
            return;
        }
        if (this.minecraft.level.effects().skyType() != DimensionSpecialEffects.SkyType.NORMAL) {
            return;
        }
        RenderSystem.disableTexture();
        final Vec3 dck4 = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getBlockPosition(), float2);
        final float float3 = (float)dck4.x;
        final float float4 = (float)dck4.y;
        final float float5 = (float)dck4.z;
        FogRenderer.levelFogColor();
        final BufferBuilder dfe8 = Tesselator.getInstance().getBuilder();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(float3, float4, float5);
        this.skyBuffer.bind();
        this.skyFormat.setupBufferState(0L);
        this.skyBuffer.draw(dfj.last().pose(), 7);
        VertexBuffer.unbind();
        this.skyFormat.clearBufferState();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final float[] arr9 = this.level.effects().getSunriseColor(this.level.getTimeOfDay(float2), float2);
        if (arr9 != null) {
            RenderSystem.disableTexture();
            RenderSystem.shadeModel(7425);
            dfj.pushPose();
            dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            final float float6 = (Mth.sin(this.level.getSunAngle(float2)) < 0.0f) ? 180.0f : 0.0f;
            dfj.mulPose(Vector3f.ZP.rotationDegrees(float6));
            dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
            final float float7 = arr9[0];
            final float float8 = arr9[1];
            final float float9 = arr9[2];
            final Matrix4f b14 = dfj.last().pose();
            dfe8.begin(6, DefaultVertexFormat.POSITION_COLOR);
            dfe8.vertex(b14, 0.0f, 100.0f, 0.0f).color(float7, float8, float9, arr9[3]).endVertex();
            final int integer15 = 16;
            for (int integer16 = 0; integer16 <= 16; ++integer16) {
                final float float10 = integer16 * 6.2831855f / 16.0f;
                final float float11 = Mth.sin(float10);
                final float float12 = Mth.cos(float10);
                dfe8.vertex(b14, float11 * 120.0f, float12 * 120.0f, -float12 * 40.0f * arr9[3]).color(arr9[0], arr9[1], arr9[2], 0.0f).endVertex();
            }
            dfe8.end();
            BufferUploader.end(dfe8);
            dfj.popPose();
            RenderSystem.shadeModel(7424);
        }
        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        dfj.pushPose();
        final float float6 = 1.0f - this.level.getRainLevel(float2);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, float6);
        dfj.mulPose(Vector3f.YP.rotationDegrees(-90.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(this.level.getTimeOfDay(float2) * 360.0f));
        final Matrix4f b15 = dfj.last().pose();
        float float8 = 30.0f;
        this.textureManager.bind(LevelRenderer.SUN_LOCATION);
        dfe8.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe8.vertex(b15, -float8, 100.0f, -float8).uv(0.0f, 0.0f).endVertex();
        dfe8.vertex(b15, float8, 100.0f, -float8).uv(1.0f, 0.0f).endVertex();
        dfe8.vertex(b15, float8, 100.0f, float8).uv(1.0f, 1.0f).endVertex();
        dfe8.vertex(b15, -float8, 100.0f, float8).uv(0.0f, 1.0f).endVertex();
        dfe8.end();
        BufferUploader.end(dfe8);
        float8 = 20.0f;
        this.textureManager.bind(LevelRenderer.MOON_LOCATION);
        final int integer17 = this.level.getMoonPhase();
        final int integer18 = integer17 % 4;
        final int integer15 = integer17 / 4 % 2;
        final float float13 = (integer18 + 0) / 4.0f;
        final float float10 = (integer15 + 0) / 2.0f;
        final float float11 = (integer18 + 1) / 4.0f;
        final float float12 = (integer15 + 1) / 2.0f;
        dfe8.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe8.vertex(b15, -float8, -100.0f, float8).uv(float11, float12).endVertex();
        dfe8.vertex(b15, float8, -100.0f, float8).uv(float13, float12).endVertex();
        dfe8.vertex(b15, float8, -100.0f, -float8).uv(float13, float10).endVertex();
        dfe8.vertex(b15, -float8, -100.0f, -float8).uv(float11, float10).endVertex();
        dfe8.end();
        BufferUploader.end(dfe8);
        RenderSystem.disableTexture();
        final float float14 = this.level.getStarBrightness(float2) * float6;
        if (float14 > 0.0f) {
            RenderSystem.color4f(float14, float14, float14, float14);
            this.starBuffer.bind();
            this.skyFormat.setupBufferState(0L);
            this.starBuffer.draw(dfj.last().pose(), 7);
            VertexBuffer.unbind();
            this.skyFormat.clearBufferState();
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        dfj.popPose();
        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0f, 0.0f, 0.0f);
        final double double10 = this.minecraft.player.getEyePosition(float2).y - this.level.getLevelData().getHorizonHeight();
        if (double10 < 0.0) {
            dfj.pushPose();
            dfj.translate(0.0, 12.0, 0.0);
            this.darkBuffer.bind();
            this.skyFormat.setupBufferState(0L);
            this.darkBuffer.draw(dfj.last().pose(), 7);
            VertexBuffer.unbind();
            this.skyFormat.clearBufferState();
            dfj.popPose();
        }
        if (this.level.effects().hasGround()) {
            RenderSystem.color3f(float3 * 0.2f + 0.04f, float4 * 0.2f + 0.04f, float5 * 0.6f + 0.1f);
        }
        else {
            RenderSystem.color3f(float3, float4, float5);
        }
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
    }
    
    public void renderClouds(final PoseStack dfj, final float float2, final double double3, final double double4, final double double5) {
        final float float3 = this.level.effects().getCloudHeight();
        if (Float.isNaN(float3)) {
            return;
        }
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableFog();
        RenderSystem.depthMask(true);
        final float float4 = 12.0f;
        final float float5 = 4.0f;
        final double double6 = 2.0E-4;
        final double double7 = (this.ticks + float2) * 0.03f;
        double double8 = (double3 + double7) / 12.0;
        final double double9 = float3 - (float)double4 + 0.33f;
        double double10 = double5 / 12.0 + 0.33000001311302185;
        double8 -= Mth.floor(double8 / 2048.0) * 2048;
        double10 -= Mth.floor(double10 / 2048.0) * 2048;
        final float float6 = (float)(double8 - Mth.floor(double8));
        final float float7 = (float)(double9 / 4.0 - Mth.floor(double9 / 4.0)) * 4.0f;
        final float float8 = (float)(double10 - Mth.floor(double10));
        final Vec3 dck26 = this.level.getCloudColor(float2);
        final int integer27 = (int)Math.floor(double8);
        final int integer28 = (int)Math.floor(double9 / 4.0);
        final int integer29 = (int)Math.floor(double10);
        if (integer27 != this.prevCloudX || integer28 != this.prevCloudY || integer29 != this.prevCloudZ || this.minecraft.options.getCloudsType() != this.prevCloudsType || this.prevCloudColor.distanceToSqr(dck26) > 2.0E-4) {
            this.prevCloudX = integer27;
            this.prevCloudY = integer28;
            this.prevCloudZ = integer29;
            this.prevCloudColor = dck26;
            this.prevCloudsType = this.minecraft.options.getCloudsType();
            this.generateClouds = true;
        }
        if (this.generateClouds) {
            this.generateClouds = false;
            final BufferBuilder dfe30 = Tesselator.getInstance().getBuilder();
            if (this.cloudBuffer != null) {
                this.cloudBuffer.close();
            }
            this.cloudBuffer = new VertexBuffer(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
            this.buildClouds(dfe30, double8, double9, double10, dck26);
            dfe30.end();
            this.cloudBuffer.upload(dfe30);
        }
        this.textureManager.bind(LevelRenderer.CLOUDS_LOCATION);
        dfj.pushPose();
        dfj.scale(12.0f, 1.0f, 12.0f);
        dfj.translate(-float6, float7, -float8);
        if (this.cloudBuffer != null) {
            this.cloudBuffer.bind();
            DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.setupBufferState(0L);
            int integer31;
            for (int integer30 = integer31 = ((this.prevCloudsType != CloudStatus.FANCY) ? 1 : 0); integer31 < 2; ++integer31) {
                if (integer31 == 0) {
                    RenderSystem.colorMask(false, false, false, false);
                }
                else {
                    RenderSystem.colorMask(true, true, true, true);
                }
                this.cloudBuffer.draw(dfj.last().pose(), 7);
            }
            VertexBuffer.unbind();
            DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.clearBufferState();
        }
        dfj.popPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableAlphaTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableFog();
    }
    
    private void buildClouds(final BufferBuilder dfe, final double double2, final double double3, final double double4, final Vec3 dck) {
        final float float10 = 4.0f;
        final float float11 = 0.00390625f;
        final int integer12 = 8;
        final int integer13 = 4;
        final float float12 = 9.765625E-4f;
        final float float13 = Mth.floor(double2) * 0.00390625f;
        final float float14 = Mth.floor(double4) * 0.00390625f;
        final float float15 = (float)dck.x;
        final float float16 = (float)dck.y;
        final float float17 = (float)dck.z;
        final float float18 = float15 * 0.9f;
        final float float19 = float16 * 0.9f;
        final float float20 = float17 * 0.9f;
        final float float21 = float15 * 0.7f;
        final float float22 = float16 * 0.7f;
        final float float23 = float17 * 0.7f;
        final float float24 = float15 * 0.8f;
        final float float25 = float16 * 0.8f;
        final float float26 = float17 * 0.8f;
        dfe.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
        final float float27 = (float)Math.floor(double3 / 4.0) * 4.0f;
        if (this.prevCloudsType == CloudStatus.FANCY) {
            for (int integer14 = -3; integer14 <= 4; ++integer14) {
                for (int integer15 = -3; integer15 <= 4; ++integer15) {
                    final float float28 = (float)(integer14 * 8);
                    final float float29 = (float)(integer15 * 8);
                    if (float27 > -5.0f) {
                        dfe.vertex(float28 + 0.0f, float27 + 0.0f, float29 + 8.0f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float21, float22, float23, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        dfe.vertex(float28 + 8.0f, float27 + 0.0f, float29 + 8.0f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float21, float22, float23, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        dfe.vertex(float28 + 8.0f, float27 + 0.0f, float29 + 0.0f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float21, float22, float23, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        dfe.vertex(float28 + 0.0f, float27 + 0.0f, float29 + 0.0f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float21, float22, float23, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    }
                    if (float27 <= 5.0f) {
                        dfe.vertex(float28 + 0.0f, float27 + 4.0f - 9.765625E-4f, float29 + 8.0f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        dfe.vertex(float28 + 8.0f, float27 + 4.0f - 9.765625E-4f, float29 + 8.0f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        dfe.vertex(float28 + 8.0f, float27 + 4.0f - 9.765625E-4f, float29 + 0.0f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        dfe.vertex(float28 + 0.0f, float27 + 4.0f - 9.765625E-4f, float29 + 0.0f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }
                    if (integer14 > -1) {
                        for (int integer16 = 0; integer16 < 8; ++integer16) {
                            dfe.vertex(float28 + integer16 + 0.0f, float27 + 0.0f, float29 + 8.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            dfe.vertex(float28 + integer16 + 0.0f, float27 + 4.0f, float29 + 8.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            dfe.vertex(float28 + integer16 + 0.0f, float27 + 4.0f, float29 + 0.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            dfe.vertex(float28 + integer16 + 0.0f, float27 + 0.0f, float29 + 0.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (integer14 <= 1) {
                        for (int integer16 = 0; integer16 < 8; ++integer16) {
                            dfe.vertex(float28 + integer16 + 1.0f - 9.765625E-4f, float27 + 0.0f, float29 + 8.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            dfe.vertex(float28 + integer16 + 1.0f - 9.765625E-4f, float27 + 4.0f, float29 + 8.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 8.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            dfe.vertex(float28 + integer16 + 1.0f - 9.765625E-4f, float27 + 4.0f, float29 + 0.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            dfe.vertex(float28 + integer16 + 1.0f - 9.765625E-4f, float27 + 0.0f, float29 + 0.0f).uv((float28 + integer16 + 0.5f) * 0.00390625f + float13, (float29 + 0.0f) * 0.00390625f + float14).color(float18, float19, float20, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (integer15 > -1) {
                        for (int integer16 = 0; integer16 < 8; ++integer16) {
                            dfe.vertex(float28 + 0.0f, float27 + 4.0f, float29 + integer16 + 0.0f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            dfe.vertex(float28 + 8.0f, float27 + 4.0f, float29 + integer16 + 0.0f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            dfe.vertex(float28 + 8.0f, float27 + 0.0f, float29 + integer16 + 0.0f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            dfe.vertex(float28 + 0.0f, float27 + 0.0f, float29 + integer16 + 0.0f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                        }
                    }
                    if (integer15 <= 1) {
                        for (int integer16 = 0; integer16 < 8; ++integer16) {
                            dfe.vertex(float28 + 0.0f, float27 + 4.0f, float29 + integer16 + 1.0f - 9.765625E-4f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            dfe.vertex(float28 + 8.0f, float27 + 4.0f, float29 + integer16 + 1.0f - 9.765625E-4f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            dfe.vertex(float28 + 8.0f, float27 + 0.0f, float29 + integer16 + 1.0f - 9.765625E-4f).uv((float28 + 8.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            dfe.vertex(float28 + 0.0f, float27 + 0.0f, float29 + integer16 + 1.0f - 9.765625E-4f).uv((float28 + 0.0f) * 0.00390625f + float13, (float29 + integer16 + 0.5f) * 0.00390625f + float14).color(float24, float25, float26, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                        }
                    }
                }
            }
        }
        else {
            final int integer14 = 1;
            final int integer15 = 32;
            for (int integer17 = -32; integer17 < 32; integer17 += 32) {
                for (int integer18 = -32; integer18 < 32; integer18 += 32) {
                    dfe.vertex(integer17 + 0, float27, integer18 + 32).uv((integer17 + 0) * 0.00390625f + float13, (integer18 + 32) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    dfe.vertex(integer17 + 32, float27, integer18 + 32).uv((integer17 + 32) * 0.00390625f + float13, (integer18 + 32) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    dfe.vertex(integer17 + 32, float27, integer18 + 0).uv((integer17 + 32) * 0.00390625f + float13, (integer18 + 0) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    dfe.vertex(integer17 + 0, float27, integer18 + 0).uv((integer17 + 0) * 0.00390625f + float13, (integer18 + 0) * 0.00390625f + float14).color(float15, float16, float17, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                }
            }
        }
    }
    
    private void compileChunksUntil(final long long1) {
        this.needsUpdate |= this.chunkRenderDispatcher.uploadAllPendingUploads();
        final long long2 = Util.getNanos();
        int integer6 = 0;
        if (!this.chunksToCompile.isEmpty()) {
            final Iterator<ChunkRenderDispatcher.RenderChunk> iterator7 = (Iterator<ChunkRenderDispatcher.RenderChunk>)this.chunksToCompile.iterator();
            while (iterator7.hasNext()) {
                final ChunkRenderDispatcher.RenderChunk c8 = (ChunkRenderDispatcher.RenderChunk)iterator7.next();
                if (c8.isDirtyFromPlayer()) {
                    this.chunkRenderDispatcher.rebuildChunkSync(c8);
                }
                else {
                    c8.rebuildChunkAsync(this.chunkRenderDispatcher);
                }
                c8.setNotDirty();
                iterator7.remove();
                ++integer6;
                final long long3 = Util.getNanos();
                final long long4 = long3 - long2;
                final long long5 = long4 / integer6;
                final long long6 = long1 - long3;
                if (long6 < long5) {
                    break;
                }
            }
        }
    }
    
    private void renderWorldBounds(final Camera djh) {
        final BufferBuilder dfe3 = Tesselator.getInstance().getBuilder();
        final WorldBorder cfr4 = this.level.getWorldBorder();
        final double double5 = this.minecraft.options.renderDistance * 16;
        if (djh.getPosition().x < cfr4.getMaxX() - double5 && djh.getPosition().x > cfr4.getMinX() + double5 && djh.getPosition().z < cfr4.getMaxZ() - double5 && djh.getPosition().z > cfr4.getMinZ() + double5) {
            return;
        }
        double double6 = 1.0 - cfr4.getDistanceToBorder(djh.getPosition().x, djh.getPosition().z) / double5;
        double6 = Math.pow(double6, 4.0);
        final double double7 = djh.getPosition().x;
        final double double8 = djh.getPosition().y;
        final double double9 = djh.getPosition().z;
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.textureManager.bind(LevelRenderer.FORCEFIELD_LOCATION);
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        RenderSystem.pushMatrix();
        final int integer15 = cfr4.getStatus().getColor();
        final float float16 = (integer15 >> 16 & 0xFF) / 255.0f;
        final float float17 = (integer15 >> 8 & 0xFF) / 255.0f;
        final float float18 = (integer15 & 0xFF) / 255.0f;
        RenderSystem.color4f(float16, float17, float18, (float)double6);
        RenderSystem.polygonOffset(-3.0f, -3.0f);
        RenderSystem.enablePolygonOffset();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();
        final float float19 = Util.getMillis() % 3000L / 3000.0f;
        final float float20 = 0.0f;
        final float float21 = 0.0f;
        final float float22 = 128.0f;
        dfe3.begin(7, DefaultVertexFormat.POSITION_TEX);
        double double10 = Math.max((double)Mth.floor(double9 - double5), cfr4.getMinZ());
        double double11 = Math.min((double)Mth.ceil(double9 + double5), cfr4.getMaxZ());
        if (double7 > cfr4.getMaxX() - double5) {
            float float23 = 0.0f;
            for (double double12 = double10; double12 < double11; ++double12, float23 += 0.5f) {
                final double double13 = Math.min(1.0, double11 - double12);
                final float float24 = (float)double13 * 0.5f;
                this.vertex(dfe3, double7, double8, double9, cfr4.getMaxX(), 256, double12, float19 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, cfr4.getMaxX(), 256, double12 + double13, float19 + float24 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, cfr4.getMaxX(), 0, double12 + double13, float19 + float24 + float23, float19 + 128.0f);
                this.vertex(dfe3, double7, double8, double9, cfr4.getMaxX(), 0, double12, float19 + float23, float19 + 128.0f);
            }
        }
        if (double7 < cfr4.getMinX() + double5) {
            float float23 = 0.0f;
            for (double double12 = double10; double12 < double11; ++double12, float23 += 0.5f) {
                final double double13 = Math.min(1.0, double11 - double12);
                final float float24 = (float)double13 * 0.5f;
                this.vertex(dfe3, double7, double8, double9, cfr4.getMinX(), 256, double12, float19 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, cfr4.getMinX(), 256, double12 + double13, float19 + float24 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, cfr4.getMinX(), 0, double12 + double13, float19 + float24 + float23, float19 + 128.0f);
                this.vertex(dfe3, double7, double8, double9, cfr4.getMinX(), 0, double12, float19 + float23, float19 + 128.0f);
            }
        }
        double10 = Math.max((double)Mth.floor(double7 - double5), cfr4.getMinX());
        double11 = Math.min((double)Mth.ceil(double7 + double5), cfr4.getMaxX());
        if (double9 > cfr4.getMaxZ() - double5) {
            float float23 = 0.0f;
            for (double double12 = double10; double12 < double11; ++double12, float23 += 0.5f) {
                final double double13 = Math.min(1.0, double11 - double12);
                final float float24 = (float)double13 * 0.5f;
                this.vertex(dfe3, double7, double8, double9, double12, 256, cfr4.getMaxZ(), float19 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, double12 + double13, 256, cfr4.getMaxZ(), float19 + float24 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, double12 + double13, 0, cfr4.getMaxZ(), float19 + float24 + float23, float19 + 128.0f);
                this.vertex(dfe3, double7, double8, double9, double12, 0, cfr4.getMaxZ(), float19 + float23, float19 + 128.0f);
            }
        }
        if (double9 < cfr4.getMinZ() + double5) {
            float float23 = 0.0f;
            for (double double12 = double10; double12 < double11; ++double12, float23 += 0.5f) {
                final double double13 = Math.min(1.0, double11 - double12);
                final float float24 = (float)double13 * 0.5f;
                this.vertex(dfe3, double7, double8, double9, double12, 256, cfr4.getMinZ(), float19 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, double12 + double13, 256, cfr4.getMinZ(), float19 + float24 + float23, float19 + 0.0f);
                this.vertex(dfe3, double7, double8, double9, double12 + double13, 0, cfr4.getMinZ(), float19 + float24 + float23, float19 + 128.0f);
                this.vertex(dfe3, double7, double8, double9, double12, 0, cfr4.getMinZ(), float19 + float23, float19 + 128.0f);
            }
        }
        dfe3.end();
        BufferUploader.end(dfe3);
        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.polygonOffset(0.0f, 0.0f);
        RenderSystem.disablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        RenderSystem.depthMask(true);
    }
    
    private void vertex(final BufferBuilder dfe, final double double2, final double double3, final double double4, final double double5, final int integer, final double double7, final float float8, final float float9) {
        dfe.vertex(double5 - double2, integer - double3, double7 - double4).uv(float8, float9).endVertex();
    }
    
    private void renderHitOutline(final PoseStack dfj, final VertexConsumer dfn, final Entity apx, final double double4, final double double5, final double double6, final BlockPos fx, final BlockState cee) {
        renderShape(dfj, dfn, cee.getShape(this.level, fx, CollisionContext.of(apx)), fx.getX() - double4, fx.getY() - double5, fx.getZ() - double6, 0.0f, 0.0f, 0.0f, 0.4f);
    }
    
    public static void renderVoxelShape(final PoseStack dfj, final VertexConsumer dfn, final VoxelShape dde, final double double4, final double double5, final double double6, final float float7, final float float8, final float float9, final float float10) {
        final List<AABB> list14 = dde.toAabbs();
        final int integer15 = Mth.ceil(list14.size() / 3.0);
        for (int integer16 = 0; integer16 < list14.size(); ++integer16) {
            final AABB dcf17 = (AABB)list14.get(integer16);
            final float float11 = (integer16 % (float)integer15 + 1.0f) / integer15;
            final float float12 = (float)(integer16 / integer15);
            final float float13 = float11 * ((float12 == 0.0f) ? 1 : 0);
            final float float14 = float11 * ((float12 == 1.0f) ? 1 : 0);
            final float float15 = float11 * ((float12 == 2.0f) ? 1 : 0);
            renderShape(dfj, dfn, Shapes.create(dcf17.move(0.0, 0.0, 0.0)), double4, double5, double6, float13, float14, float15, 1.0f);
        }
    }
    
    private static void renderShape(final PoseStack dfj, final VertexConsumer dfn, final VoxelShape dde, final double double4, final double double5, final double double6, final float float7, final float float8, final float float9, final float float10) {
        final Matrix4f b14 = dfj.last().pose();
        final Matrix4f matrix4f;
        dde.forAllEdges((double10, double11, double12, double13, double14, double15) -> {
            dfn.vertex(matrix4f, (float)(double10 + double4), (float)(double11 + double5), (float)(double12 + double6)).color(float7, float8, float9, float10).endVertex();
            dfn.vertex(matrix4f, (float)(double13 + double4), (float)(double14 + double5), (float)(double15 + double6)).color(float7, float8, float9, float10).endVertex();
        });
    }
    
    public static void renderLineBox(final PoseStack dfj, final VertexConsumer dfn, final AABB dcf, final float float4, final float float5, final float float6, final float float7) {
        renderLineBox(dfj, dfn, dcf.minX, dcf.minY, dcf.minZ, dcf.maxX, dcf.maxY, dcf.maxZ, float4, float5, float6, float7, float4, float5, float6);
    }
    
    public static void renderLineBox(final PoseStack dfj, final VertexConsumer dfn, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8, final float float9, final float float10, final float float11, final float float12) {
        renderLineBox(dfj, dfn, double3, double4, double5, double6, double7, double8, float9, float10, float11, float12, float9, float10, float11);
    }
    
    public static void renderLineBox(final PoseStack dfj, final VertexConsumer dfn, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8, final float float9, final float float10, final float float11, final float float12, final float float13, final float float14, final float float15) {
        final Matrix4f b22 = dfj.last().pose();
        final float float16 = (float)double3;
        final float float17 = (float)double4;
        final float float18 = (float)double5;
        final float float19 = (float)double6;
        final float float20 = (float)double7;
        final float float21 = (float)double8;
        dfn.vertex(b22, float16, float17, float18).color(float9, float14, float15, float12).endVertex();
        dfn.vertex(b22, float19, float17, float18).color(float9, float14, float15, float12).endVertex();
        dfn.vertex(b22, float16, float17, float18).color(float13, float10, float15, float12).endVertex();
        dfn.vertex(b22, float16, float20, float18).color(float13, float10, float15, float12).endVertex();
        dfn.vertex(b22, float16, float17, float18).color(float13, float14, float11, float12).endVertex();
        dfn.vertex(b22, float16, float17, float21).color(float13, float14, float11, float12).endVertex();
        dfn.vertex(b22, float19, float17, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float20, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float20, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float20, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float20, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float20, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float20, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float17, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float17, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float17, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float17, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float17, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float16, float20, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float20, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float17, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float20, float21).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float20, float18).color(float9, float10, float11, float12).endVertex();
        dfn.vertex(b22, float19, float20, float21).color(float9, float10, float11, float12).endVertex();
    }
    
    public static void addChainedFilledBoxVertices(final BufferBuilder dfe, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final float float8, final float float9, final float float10, final float float11) {
        dfe.vertex(double2, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double3, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double2, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double4).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double7).color(float8, float9, float10, float11).endVertex();
        dfe.vertex(double5, double6, double7).color(float8, float9, float10, float11).endVertex();
    }
    
    public void blockChanged(final BlockGetter bqz, final BlockPos fx, final BlockState cee3, final BlockState cee4, final int integer) {
        this.setBlockDirty(fx, (integer & 0x8) != 0x0);
    }
    
    private void setBlockDirty(final BlockPos fx, final boolean boolean2) {
        for (int integer4 = fx.getZ() - 1; integer4 <= fx.getZ() + 1; ++integer4) {
            for (int integer5 = fx.getX() - 1; integer5 <= fx.getX() + 1; ++integer5) {
                for (int integer6 = fx.getY() - 1; integer6 <= fx.getY() + 1; ++integer6) {
                    this.setSectionDirty(integer5 >> 4, integer6 >> 4, integer4 >> 4, boolean2);
                }
            }
        }
    }
    
    public void setBlocksDirty(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        for (int integer7 = integer3 - 1; integer7 <= integer6 + 1; ++integer7) {
            for (int integer8 = integer1 - 1; integer8 <= integer4 + 1; ++integer8) {
                for (int integer9 = integer2 - 1; integer9 <= integer5 + 1; ++integer9) {
                    this.setSectionDirty(integer8 >> 4, integer9 >> 4, integer7 >> 4);
                }
            }
        }
    }
    
    public void setBlockDirty(final BlockPos fx, final BlockState cee2, final BlockState cee3) {
        if (this.minecraft.getModelManager().requiresRender(cee2, cee3)) {
            this.setBlocksDirty(fx.getX(), fx.getY(), fx.getZ(), fx.getX(), fx.getY(), fx.getZ());
        }
    }
    
    public void setSectionDirtyWithNeighbors(final int integer1, final int integer2, final int integer3) {
        for (int integer4 = integer3 - 1; integer4 <= integer3 + 1; ++integer4) {
            for (int integer5 = integer1 - 1; integer5 <= integer1 + 1; ++integer5) {
                for (int integer6 = integer2 - 1; integer6 <= integer2 + 1; ++integer6) {
                    this.setSectionDirty(integer5, integer6, integer4);
                }
            }
        }
    }
    
    public void setSectionDirty(final int integer1, final int integer2, final int integer3) {
        this.setSectionDirty(integer1, integer2, integer3, false);
    }
    
    private void setSectionDirty(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
        this.viewArea.setDirty(integer1, integer2, integer3, boolean4);
    }
    
    public void playStreamingMusic(@Nullable final SoundEvent adn, final BlockPos fx) {
        SoundInstance eml4 = (SoundInstance)this.playingRecords.get(fx);
        if (eml4 != null) {
            this.minecraft.getSoundManager().stop(eml4);
            this.playingRecords.remove(fx);
        }
        if (adn != null) {
            final RecordItem bmn5 = RecordItem.getBySound(adn);
            if (bmn5 != null) {
                this.minecraft.gui.setNowPlaying(bmn5.getDisplayName());
            }
            eml4 = SimpleSoundInstance.forRecord(adn, fx.getX(), fx.getY(), fx.getZ());
            this.playingRecords.put(fx, eml4);
            this.minecraft.getSoundManager().play(eml4);
        }
        this.notifyNearbyEntities(this.level, fx, adn != null);
    }
    
    private void notifyNearbyEntities(final Level bru, final BlockPos fx, final boolean boolean3) {
        final List<LivingEntity> list5 = bru.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, new AABB(fx).inflate(3.0));
        for (final LivingEntity aqj7 : list5) {
            aqj7.setRecordPlayingNearby(fx, boolean3);
        }
    }
    
    public void addParticle(final ParticleOptions hf, final boolean boolean2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
        this.addParticle(hf, boolean2, false, double3, double4, double5, double6, double7, double8);
    }
    
    public void addParticle(final ParticleOptions hf, final boolean boolean2, final boolean boolean3, final double double4, final double double5, final double double6, final double double7, final double double8, final double double9) {
        try {
            this.addParticleInternal(hf, boolean2, boolean3, double4, double5, double6, double7, double8, double9);
        }
        catch (Throwable throwable17) {
            final CrashReport l18 = CrashReport.forThrowable(throwable17, "Exception while adding particle");
            final CrashReportCategory m19 = l18.addCategory("Particle being added");
            m19.setDetail("ID", Registry.PARTICLE_TYPE.getKey(hf.getType()));
            m19.setDetail("Parameters", hf.writeToString());
            m19.setDetail("Position", (CrashReportDetail<String>)(() -> CrashReportCategory.formatLocation(double4, double5, double6)));
            throw new ReportedException(l18);
        }
    }
    
    private <T extends ParticleOptions> void addParticle(final T hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        this.addParticle(hf, hf.getType().getOverrideLimiter(), double2, double3, double4, double5, double6, double7);
    }
    
    @Nullable
    private Particle addParticleInternal(final ParticleOptions hf, final boolean boolean2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
        return this.addParticleInternal(hf, boolean2, false, double3, double4, double5, double6, double7, double8);
    }
    
    @Nullable
    private Particle addParticleInternal(final ParticleOptions hf, final boolean boolean2, final boolean boolean3, final double double4, final double double5, final double double6, final double double7, final double double8, final double double9) {
        final Camera djh17 = this.minecraft.gameRenderer.getMainCamera();
        if (this.minecraft == null || !djh17.isInitialized() || this.minecraft.particleEngine == null) {
            return null;
        }
        final ParticleStatus dkb18 = this.calculateParticleLevel(boolean3);
        if (boolean2) {
            return this.minecraft.particleEngine.createParticle(hf, double4, double5, double6, double7, double8, double9);
        }
        if (djh17.getPosition().distanceToSqr(double4, double5, double6) > 1024.0) {
            return null;
        }
        if (dkb18 == ParticleStatus.MINIMAL) {
            return null;
        }
        return this.minecraft.particleEngine.createParticle(hf, double4, double5, double6, double7, double8, double9);
    }
    
    private ParticleStatus calculateParticleLevel(final boolean boolean1) {
        ParticleStatus dkb3 = this.minecraft.options.particles;
        if (boolean1 && dkb3 == ParticleStatus.MINIMAL && this.level.random.nextInt(10) == 0) {
            dkb3 = ParticleStatus.DECREASED;
        }
        if (dkb3 == ParticleStatus.DECREASED && this.level.random.nextInt(3) == 0) {
            dkb3 = ParticleStatus.MINIMAL;
        }
        return dkb3;
    }
    
    public void clear() {
    }
    
    public void globalLevelEvent(final int integer1, final BlockPos fx, final int integer3) {
        switch (integer1) {
            case 1023:
            case 1028:
            case 1038: {
                final Camera djh5 = this.minecraft.gameRenderer.getMainCamera();
                if (!djh5.isInitialized()) {
                    break;
                }
                final double double6 = fx.getX() - djh5.getPosition().x;
                final double double7 = fx.getY() - djh5.getPosition().y;
                final double double8 = fx.getZ() - djh5.getPosition().z;
                final double double9 = Math.sqrt(double6 * double6 + double7 * double7 + double8 * double8);
                double double10 = djh5.getPosition().x;
                double double11 = djh5.getPosition().y;
                double double12 = djh5.getPosition().z;
                if (double9 > 0.0) {
                    double10 += double6 / double9 * 2.0;
                    double11 += double7 / double9 * 2.0;
                    double12 += double8 / double9 * 2.0;
                }
                if (integer1 == 1023) {
                    this.level.playLocalSound(double10, double11, double12, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                if (integer1 == 1038) {
                    this.level.playLocalSound(double10, double11, double12, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                this.level.playLocalSound(double10, double11, double12, SoundEvents.ENDER_DRAGON_DEATH, SoundSource.HOSTILE, 5.0f, 1.0f, false);
                break;
            }
        }
    }
    
    public void levelEvent(final Player bft, final int integer2, final BlockPos fx, final int integer4) {
        final Random random6 = this.level.random;
        switch (integer2) {
            case 1035: {
                this.level.playLocalSound(fx, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1033: {
                this.level.playLocalSound(fx, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1034: {
                this.level.playLocalSound(fx, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1032: {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRAVEL, random6.nextFloat() * 0.4f + 0.8f, 0.25f));
                break;
            }
            case 1001: {
                this.level.playLocalSound(fx, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0f, 1.2f, false);
                break;
            }
            case 1000: {
                this.level.playLocalSound(fx, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1003: {
                this.level.playLocalSound(fx, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0f, 1.2f, false);
                break;
            }
            case 1004: {
                this.level.playLocalSound(fx, SoundEvents.FIREWORK_ROCKET_SHOOT, SoundSource.NEUTRAL, 1.0f, 1.2f, false);
                break;
            }
            case 1002: {
                this.level.playLocalSound(fx, SoundEvents.DISPENSER_LAUNCH, SoundSource.BLOCKS, 1.0f, 1.2f, false);
                break;
            }
            case 2000: {
                final Direction gc7 = Direction.from3DDataValue(integer4);
                final int integer5 = gc7.getStepX();
                final int integer6 = gc7.getStepY();
                final int integer7 = gc7.getStepZ();
                final double double11 = fx.getX() + integer5 * 0.6 + 0.5;
                final double double12 = fx.getY() + integer6 * 0.6 + 0.5;
                final double double13 = fx.getZ() + integer7 * 0.6 + 0.5;
                for (int integer8 = 0; integer8 < 10; ++integer8) {
                    final double double14 = random6.nextDouble() * 0.2 + 0.01;
                    final double double15 = double11 + integer5 * 0.01 + (random6.nextDouble() - 0.5) * integer7 * 0.5;
                    final double double16 = double12 + integer6 * 0.01 + (random6.nextDouble() - 0.5) * integer6 * 0.5;
                    final double double17 = double13 + integer7 * 0.01 + (random6.nextDouble() - 0.5) * integer5 * 0.5;
                    final double double18 = integer5 * double14 + random6.nextGaussian() * 0.01;
                    final double double19 = integer6 * double14 + random6.nextGaussian() * 0.01;
                    final double double20 = integer7 * double14 + random6.nextGaussian() * 0.01;
                    this.<SimpleParticleType>addParticle(ParticleTypes.SMOKE, double15, double16, double17, double18, double19, double20);
                }
                break;
            }
            case 2003: {
                final double double21 = fx.getX() + 0.5;
                final double double22 = fx.getY();
                final double double11 = fx.getZ() + 0.5;
                for (int integer9 = 0; integer9 < 8; ++integer9) {
                    this.<ItemParticleOption>addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ENDER_EYE)), double21, double22, double11, random6.nextGaussian() * 0.15, random6.nextDouble() * 0.2, random6.nextGaussian() * 0.15);
                }
                for (double double12 = 0.0; double12 < 6.283185307179586; double12 += 0.15707963267948966) {
                    this.<SimpleParticleType>addParticle(ParticleTypes.PORTAL, double21 + Math.cos(double12) * 5.0, double22 - 0.4, double11 + Math.sin(double12) * 5.0, Math.cos(double12) * -5.0, 0.0, Math.sin(double12) * -5.0);
                    this.<SimpleParticleType>addParticle(ParticleTypes.PORTAL, double21 + Math.cos(double12) * 5.0, double22 - 0.4, double11 + Math.sin(double12) * 5.0, Math.cos(double12) * -7.0, 0.0, Math.sin(double12) * -7.0);
                }
                break;
            }
            case 2002:
            case 2007: {
                final Vec3 dck7 = Vec3.atBottomCenterOf(fx);
                for (int integer5 = 0; integer5 < 8; ++integer5) {
                    this.<ItemParticleOption>addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)), dck7.x, dck7.y, dck7.z, random6.nextGaussian() * 0.15, random6.nextDouble() * 0.2, random6.nextGaussian() * 0.15);
                }
                final float float8 = (integer4 >> 16 & 0xFF) / 255.0f;
                final float float9 = (integer4 >> 8 & 0xFF) / 255.0f;
                final float float10 = (integer4 >> 0 & 0xFF) / 255.0f;
                final ParticleOptions hf11 = (integer2 == 2007) ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT;
                for (int integer10 = 0; integer10 < 100; ++integer10) {
                    final double double12 = random6.nextDouble() * 4.0;
                    final double double13 = random6.nextDouble() * 3.141592653589793 * 2.0;
                    final double double23 = Math.cos(double13) * double12;
                    final double double24 = 0.01 + random6.nextDouble() * 0.5;
                    final double double25 = Math.sin(double13) * double12;
                    final Particle dxy23 = this.addParticleInternal(hf11, hf11.getType().getOverrideLimiter(), dck7.x + double23 * 0.1, dck7.y + 0.3, dck7.z + double25 * 0.1, double23, double24, double25);
                    if (dxy23 != null) {
                        final float float11 = 0.75f + random6.nextFloat() * 0.25f;
                        dxy23.setColor(float8 * float11, float9 * float11, float10 * float11);
                        dxy23.setPower((float)double12);
                    }
                }
                this.level.playLocalSound(fx, SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2001: {
                final BlockState cee7 = Block.stateById(integer4);
                if (!cee7.isAir()) {
                    final SoundType cab8 = cee7.getSoundType();
                    this.level.playLocalSound(fx, cab8.getBreakSound(), SoundSource.BLOCKS, (cab8.getVolume() + 1.0f) / 2.0f, cab8.getPitch() * 0.8f, false);
                }
                this.minecraft.particleEngine.destroy(fx, cee7);
                break;
            }
            case 2004: {
                for (int integer5 = 0; integer5 < 20; ++integer5) {
                    final double double22 = fx.getX() + 0.5 + (random6.nextDouble() - 0.5) * 2.0;
                    final double double11 = fx.getY() + 0.5 + (random6.nextDouble() - 0.5) * 2.0;
                    final double double12 = fx.getZ() + 0.5 + (random6.nextDouble() - 0.5) * 2.0;
                    this.level.addParticle(ParticleTypes.SMOKE, double22, double11, double12, 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.FLAME, double22, double11, double12, 0.0, 0.0, 0.0);
                }
                break;
            }
            case 2005: {
                BoneMealItem.addGrowthParticles(this.level, fx, integer4);
                break;
            }
            case 2008: {
                this.level.addParticle(ParticleTypes.EXPLOSION, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 0.0, 0.0, 0.0);
                break;
            }
            case 1500: {
                ComposterBlock.handleFill(this.level, fx, integer4 > 0);
                break;
            }
            case 1501: {
                this.level.playLocalSound(fx, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (random6.nextFloat() - random6.nextFloat()) * 0.8f, false);
                for (int integer5 = 0; integer5 < 8; ++integer5) {
                    this.level.addParticle(ParticleTypes.LARGE_SMOKE, fx.getX() + random6.nextDouble(), fx.getY() + 1.2, fx.getZ() + random6.nextDouble(), 0.0, 0.0, 0.0);
                }
                break;
            }
            case 1502: {
                this.level.playLocalSound(fx, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.5f, 2.6f + (random6.nextFloat() - random6.nextFloat()) * 0.8f, false);
                for (int integer5 = 0; integer5 < 5; ++integer5) {
                    final double double22 = fx.getX() + random6.nextDouble() * 0.6 + 0.2;
                    final double double11 = fx.getY() + random6.nextDouble() * 0.6 + 0.2;
                    final double double12 = fx.getZ() + random6.nextDouble() * 0.6 + 0.2;
                    this.level.addParticle(ParticleTypes.SMOKE, double22, double11, double12, 0.0, 0.0, 0.0);
                }
                break;
            }
            case 1503: {
                this.level.playLocalSound(fx, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                for (int integer5 = 0; integer5 < 16; ++integer5) {
                    final double double22 = fx.getX() + (5.0 + random6.nextDouble() * 6.0) / 16.0;
                    final double double11 = fx.getY() + 0.8125;
                    final double double12 = fx.getZ() + (5.0 + random6.nextDouble() * 6.0) / 16.0;
                    this.level.addParticle(ParticleTypes.SMOKE, double22, double11, double12, 0.0, 0.0, 0.0);
                }
                break;
            }
            case 2006: {
                for (int integer5 = 0; integer5 < 200; ++integer5) {
                    final float float9 = random6.nextFloat() * 4.0f;
                    final float float10 = random6.nextFloat() * 6.2831855f;
                    final double double11 = Mth.cos(float10) * float9;
                    final double double12 = 0.01 + random6.nextDouble() * 0.5;
                    final double double13 = Mth.sin(float10) * float9;
                    final Particle dxy24 = this.addParticleInternal(ParticleTypes.DRAGON_BREATH, false, fx.getX() + double11 * 0.1, fx.getY() + 0.3, fx.getZ() + double13 * 0.1, double11, double12, double13);
                    if (dxy24 != null) {
                        dxy24.setPower(float9);
                    }
                }
                if (integer4 == 1) {
                    this.level.playLocalSound(fx, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                    break;
                }
                break;
            }
            case 2009: {
                for (int integer5 = 0; integer5 < 8; ++integer5) {
                    this.level.addParticle(ParticleTypes.CLOUD, fx.getX() + random6.nextDouble(), fx.getY() + 1.2, fx.getZ() + random6.nextDouble(), 0.0, 0.0, 0.0);
                }
                break;
            }
            case 1012: {
                this.level.playLocalSound(fx, SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1036: {
                this.level.playLocalSound(fx, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1013: {
                this.level.playLocalSound(fx, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1014: {
                this.level.playLocalSound(fx, SoundEvents.FENCE_GATE_CLOSE, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1011: {
                this.level.playLocalSound(fx, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1006: {
                this.level.playLocalSound(fx, SoundEvents.WOODEN_DOOR_OPEN, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1007: {
                this.level.playLocalSound(fx, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1037: {
                this.level.playLocalSound(fx, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1008: {
                this.level.playLocalSound(fx, SoundEvents.FENCE_GATE_OPEN, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1005: {
                this.level.playLocalSound(fx, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1009: {
                this.level.playLocalSound(fx, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (random6.nextFloat() - random6.nextFloat()) * 0.8f, false);
                break;
            }
            case 1029: {
                this.level.playLocalSound(fx, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1030: {
                this.level.playLocalSound(fx, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, random6.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1044: {
                this.level.playLocalSound(fx, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1.0f, this.level.random.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1031: {
                this.level.playLocalSound(fx, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3f, this.level.random.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1039: {
                this.level.playLocalSound(fx, SoundEvents.PHANTOM_BITE, SoundSource.HOSTILE, 0.3f, this.level.random.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1010: {
                if (Item.byId(integer4) instanceof RecordItem) {
                    this.playStreamingMusic(((RecordItem)Item.byId(integer4)).getSound(), fx);
                    break;
                }
                this.playStreamingMusic(null, fx);
                break;
            }
            case 1015: {
                this.level.playLocalSound(fx, SoundEvents.GHAST_WARN, SoundSource.HOSTILE, 10.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1017: {
                this.level.playLocalSound(fx, SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 10.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1016: {
                this.level.playLocalSound(fx, SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 10.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1019: {
                this.level.playLocalSound(fx, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1022: {
                this.level.playLocalSound(fx, SoundEvents.WITHER_BREAK_BLOCK, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1021: {
                this.level.playLocalSound(fx, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1020: {
                this.level.playLocalSound(fx, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1018: {
                this.level.playLocalSound(fx, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1024: {
                this.level.playLocalSound(fx, SoundEvents.WITHER_SHOOT, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1026: {
                this.level.playLocalSound(fx, SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1027: {
                this.level.playLocalSound(fx, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1040: {
                this.level.playLocalSound(fx, SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.NEUTRAL, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1041: {
                this.level.playLocalSound(fx, SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, SoundSource.NEUTRAL, 2.0f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1025: {
                this.level.playLocalSound(fx, SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 0.05f, (random6.nextFloat() - random6.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1042: {
                this.level.playLocalSound(fx, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, this.level.random.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1043: {
                this.level.playLocalSound(fx, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0f, this.level.random.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 3000: {
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, true, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 0.0, 0.0, 0.0);
                this.level.playLocalSound(fx, SoundEvents.END_GATEWAY_SPAWN, SoundSource.BLOCKS, 10.0f, (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f, false);
                break;
            }
            case 3001: {
                this.level.playLocalSound(fx, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 64.0f, 0.8f + this.level.random.nextFloat() * 0.3f, false);
                break;
            }
        }
    }
    
    public void destroyBlockProgress(final int integer1, final BlockPos fx, final int integer3) {
        if (integer3 < 0 || integer3 >= 10) {
            final BlockDestructionProgress zq5 = (BlockDestructionProgress)this.destroyingBlocks.remove(integer1);
            if (zq5 != null) {
                this.removeProgress(zq5);
            }
        }
        else {
            BlockDestructionProgress zq5 = (BlockDestructionProgress)this.destroyingBlocks.get(integer1);
            if (zq5 != null) {
                this.removeProgress(zq5);
            }
            if (zq5 == null || zq5.getPos().getX() != fx.getX() || zq5.getPos().getY() != fx.getY() || zq5.getPos().getZ() != fx.getZ()) {
                zq5 = new BlockDestructionProgress(integer1, fx);
                this.destroyingBlocks.put(integer1, zq5);
            }
            zq5.setProgress(integer3);
            zq5.updateTick(this.ticks);
            ((SortedSet)this.destructionProgress.computeIfAbsent(zq5.getPos().asLong(), long1 -> Sets.newTreeSet())).add(zq5);
        }
    }
    
    public boolean hasRenderedAllChunks() {
        return this.chunksToCompile.isEmpty() && this.chunkRenderDispatcher.isQueueEmpty();
    }
    
    public void needsUpdate() {
        this.needsUpdate = true;
        this.generateClouds = true;
    }
    
    public void updateGlobalBlockEntities(final Collection<BlockEntity> collection1, final Collection<BlockEntity> collection2) {
        synchronized (this.globalBlockEntities) {
            this.globalBlockEntities.removeAll((Collection)collection1);
            this.globalBlockEntities.addAll((Collection)collection2);
        }
    }
    
    public static int getLightColor(final BlockAndTintGetter bqx, final BlockPos fx) {
        return getLightColor(bqx, bqx.getBlockState(fx), fx);
    }
    
    public static int getLightColor(final BlockAndTintGetter bqx, final BlockState cee, final BlockPos fx) {
        if (cee.emissiveRendering(bqx, fx)) {
            return 15728880;
        }
        final int integer4 = bqx.getBrightness(LightLayer.SKY, fx);
        int integer5 = bqx.getBrightness(LightLayer.BLOCK, fx);
        final int integer6 = cee.getLightEmission();
        if (integer5 < integer6) {
            integer5 = integer6;
        }
        return integer4 << 20 | integer5 << 4;
    }
    
    @Nullable
    public RenderTarget entityTarget() {
        return this.entityTarget;
    }
    
    @Nullable
    public RenderTarget getTranslucentTarget() {
        return this.translucentTarget;
    }
    
    @Nullable
    public RenderTarget getItemEntityTarget() {
        return this.itemEntityTarget;
    }
    
    @Nullable
    public RenderTarget getParticlesTarget() {
        return this.particlesTarget;
    }
    
    @Nullable
    public RenderTarget getWeatherTarget() {
        return this.weatherTarget;
    }
    
    @Nullable
    public RenderTarget getCloudsTarget() {
        return this.cloudsTarget;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");
        SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
        CLOUDS_LOCATION = new ResourceLocation("textures/environment/clouds.png");
        END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
        FORCEFIELD_LOCATION = new ResourceLocation("textures/misc/forcefield.png");
        RAIN_LOCATION = new ResourceLocation("textures/environment/rain.png");
        SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");
        DIRECTIONS = Direction.values();
    }
    
    class RenderChunkInfo {
        private final ChunkRenderDispatcher.RenderChunk chunk;
        private final Direction sourceDirection;
        private byte directions;
        private final int step;
        
        private RenderChunkInfo(final ChunkRenderDispatcher.RenderChunk c, @Nullable final Direction gc, final int integer) {
            this.chunk = c;
            this.sourceDirection = gc;
            this.step = integer;
        }
        
        public void setDirections(final byte byte1, final Direction gc) {
            this.directions |= (byte)(byte1 | 1 << gc.ordinal());
        }
        
        public boolean hasDirection(final Direction gc) {
            return (this.directions & 1 << gc.ordinal()) > 0;
        }
    }
    
    public static class TransparencyShaderException extends RuntimeException {
        public TransparencyShaderException(final String string, final Throwable throwable) {
            super(string, throwable);
        }
    }
}
