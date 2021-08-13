package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import java.util.EnumMap;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.server.level.ChunkHolder;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.math.Transformation;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.util.FrameTimer;
import net.minecraft.Util;
import net.minecraft.world.level.material.FluidState;
import java.util.Iterator;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.blaze3d.platform.GlUtil;
import net.minecraft.world.level.chunk.ChunkStatus;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import net.minecraft.client.renderer.PostChain;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.lighting.LevelLightEngine;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LightLayer;
import net.minecraft.util.Mth;
import java.util.Locale;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.SharedConstants;
import java.util.List;
import com.google.common.base.Strings;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.chunk.LevelChunk;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;
import net.minecraft.client.gui.GuiComponent;

public class DebugScreenOverlay extends GuiComponent {
    private static final Map<Heightmap.Types, String> HEIGHTMAP_NAMES;
    private final Minecraft minecraft;
    private final Font font;
    private HitResult block;
    private HitResult liquid;
    @Nullable
    private ChunkPos lastPos;
    @Nullable
    private LevelChunk clientChunk;
    @Nullable
    private CompletableFuture<LevelChunk> serverChunk;
    
    public DebugScreenOverlay(final Minecraft djw) {
        this.minecraft = djw;
        this.font = djw.font;
    }
    
    public void clearChunkCache() {
        this.serverChunk = null;
        this.clientChunk = null;
    }
    
    public void render(final PoseStack dfj) {
        this.minecraft.getProfiler().push("debug");
        RenderSystem.pushMatrix();
        final Entity apx3 = this.minecraft.getCameraEntity();
        this.block = apx3.pick(20.0, 0.0f, false);
        this.liquid = apx3.pick(20.0, 0.0f, true);
        this.drawGameInformation(dfj);
        this.drawSystemInformation(dfj);
        RenderSystem.popMatrix();
        if (this.minecraft.options.renderFpsChart) {
            final int integer4 = this.minecraft.getWindow().getGuiScaledWidth();
            this.drawChart(dfj, this.minecraft.getFrameTimer(), 0, integer4 / 2, true);
            final IntegratedServer emy5 = this.minecraft.getSingleplayerServer();
            if (emy5 != null) {
                this.drawChart(dfj, emy5.getFrameTimer(), integer4 - Math.min(integer4 / 2, 240), integer4 / 2, false);
            }
        }
        this.minecraft.getProfiler().pop();
    }
    
    protected void drawGameInformation(final PoseStack dfj) {
        final List<String> list3 = this.getGameInformation();
        list3.add("");
        final boolean boolean4 = this.minecraft.getSingleplayerServer() != null;
        list3.add(new StringBuilder().append("Debug: Pie [shift]: ").append(this.minecraft.options.renderDebugCharts ? "visible" : "hidden").append(boolean4 ? " FPS + TPS" : " FPS").append(" [alt]: ").append(this.minecraft.options.renderFpsChart ? "visible" : "hidden").toString());
        list3.add("For help: press F3 + Q");
        for (int integer5 = 0; integer5 < list3.size(); ++integer5) {
            final String string6 = (String)list3.get(integer5);
            if (!Strings.isNullOrEmpty(string6)) {
                this.font.getClass();
                final int integer6 = 9;
                final int integer7 = this.font.width(string6);
                final int integer8 = 2;
                final int integer9 = 2 + integer6 * integer5;
                GuiComponent.fill(dfj, 1, integer9 - 1, 2 + integer7 + 1, integer9 + integer6 - 1, -1873784752);
                this.font.draw(dfj, string6, 2.0f, (float)integer9, 14737632);
            }
        }
    }
    
    protected void drawSystemInformation(final PoseStack dfj) {
        final List<String> list3 = this.getSystemInformation();
        for (int integer4 = 0; integer4 < list3.size(); ++integer4) {
            final String string5 = (String)list3.get(integer4);
            if (!Strings.isNullOrEmpty(string5)) {
                this.font.getClass();
                final int integer5 = 9;
                final int integer6 = this.font.width(string5);
                final int integer7 = this.minecraft.getWindow().getGuiScaledWidth() - 2 - integer6;
                final int integer8 = 2 + integer5 * integer4;
                GuiComponent.fill(dfj, integer7 - 1, integer8 - 1, integer7 + integer6 + 1, integer8 + integer5 - 1, -1873784752);
                this.font.draw(dfj, string5, (float)integer7, (float)integer8, 14737632);
            }
        }
    }
    
    protected List<String> getGameInformation() {
        final IntegratedServer emy3 = this.minecraft.getSingleplayerServer();
        final Connection nd4 = this.minecraft.getConnection().getConnection();
        final float float5 = nd4.getAverageSentPackets();
        final float float6 = nd4.getAverageReceivedPackets();
        String string2;
        if (emy3 != null) {
            string2 = String.format("Integrated server @ %.0f ms ticks, %.0f tx, %.0f rx", new Object[] { emy3.getAverageTickTime(), float5, float6 });
        }
        else {
            string2 = String.format("\"%s\" server, %.0f tx, %.0f rx", new Object[] { this.minecraft.player.getServerBrand(), float5, float6 });
        }
        final BlockPos fx7 = this.minecraft.getCameraEntity().blockPosition();
        if (this.minecraft.showOnlyReducedInfo()) {
            return (List<String>)Lists.newArrayList((Object[])new String[] { "Minecraft " + SharedConstants.getCurrentVersion().getName() + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.minecraft.fpsString, string2, this.minecraft.levelRenderer.getChunkStatistics(), this.minecraft.levelRenderer.getEntityStatistics(), "P: " + this.minecraft.particleEngine.countParticles() + ". T: " + this.minecraft.level.getEntityCount(), this.minecraft.level.gatherChunkSourceStats(), "", String.format("Chunk-relative: %d %d %d", new Object[] { fx7.getX() & 0xF, fx7.getY() & 0xF, fx7.getZ() & 0xF }) });
        }
        final Entity apx8 = this.minecraft.getCameraEntity();
        final Direction gc9 = apx8.getDirection();
        String string3 = null;
        switch (gc9) {
            case NORTH: {
                string3 = "Towards negative Z";
                break;
            }
            case SOUTH: {
                string3 = "Towards positive Z";
                break;
            }
            case WEST: {
                string3 = "Towards negative X";
                break;
            }
            case EAST: {
                string3 = "Towards positive X";
                break;
            }
            default: {
                string3 = "Invalid";
                break;
            }
        }
        final ChunkPos bra11 = new ChunkPos(fx7);
        if (!Objects.equals(this.lastPos, bra11)) {
            this.lastPos = bra11;
            this.clearChunkCache();
        }
        final Level bru12 = this.getLevel();
        final LongSet longSet13 = (bru12 instanceof ServerLevel) ? ((ServerLevel)bru12).getForcedChunks() : LongSets.EMPTY_SET;
        final List<String> list14 = (List<String>)Lists.newArrayList((Object[])new String[] { "Minecraft " + SharedConstants.getCurrentVersion().getName() + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : ("/" + this.minecraft.getVersionType())) + ")", this.minecraft.fpsString, string2, this.minecraft.levelRenderer.getChunkStatistics(), this.minecraft.levelRenderer.getEntityStatistics(), "P: " + this.minecraft.particleEngine.countParticles() + ". T: " + this.minecraft.level.getEntityCount(), this.minecraft.level.gatherChunkSourceStats() });
        final String string4 = this.getServerChunkStats();
        if (string4 != null) {
            list14.add(string4);
        }
        list14.add(new StringBuilder().append((Object)this.minecraft.level.dimension().location()).append(" FC: ").append(longSet13.size()).toString());
        list14.add("");
        list14.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", new Object[] { this.minecraft.getCameraEntity().getX(), this.minecraft.getCameraEntity().getY(), this.minecraft.getCameraEntity().getZ() }));
        list14.add(String.format("Block: %d %d %d", new Object[] { fx7.getX(), fx7.getY(), fx7.getZ() }));
        list14.add(String.format("Chunk: %d %d %d in %d %d %d", new Object[] { fx7.getX() & 0xF, fx7.getY() & 0xF, fx7.getZ() & 0xF, fx7.getX() >> 4, fx7.getY() >> 4, fx7.getZ() >> 4 }));
        list14.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", new Object[] { gc9, string3, Mth.wrapDegrees(apx8.yRot), Mth.wrapDegrees(apx8.xRot) }));
        if (this.minecraft.level != null) {
            if (this.minecraft.level.hasChunkAt(fx7)) {
                final LevelChunk cge16 = this.getClientChunk();
                if (cge16.isEmpty()) {
                    list14.add("Waiting for chunk...");
                }
                else {
                    final int integer17 = this.minecraft.level.getChunkSource().getLightEngine().getRawBrightness(fx7, 0);
                    final int integer18 = this.minecraft.level.getBrightness(LightLayer.SKY, fx7);
                    final int integer19 = this.minecraft.level.getBrightness(LightLayer.BLOCK, fx7);
                    list14.add(new StringBuilder().append("Client Light: ").append(integer17).append(" (").append(integer18).append(" sky, ").append(integer19).append(" block)").toString());
                    final LevelChunk cge17 = this.getServerChunk();
                    if (cge17 != null) {
                        final LevelLightEngine cul21 = bru12.getChunkSource().getLightEngine();
                        list14.add(new StringBuilder().append("Server Light: (").append(cul21.getLayerListener(LightLayer.SKY).getLightValue(fx7)).append(" sky, ").append(cul21.getLayerListener(LightLayer.BLOCK).getLightValue(fx7)).append(" block)").toString());
                    }
                    else {
                        list14.add("Server Light: (?? sky, ?? block)");
                    }
                    final StringBuilder stringBuilder21 = new StringBuilder("CH");
                    for (final Heightmap.Types a25 : Heightmap.Types.values()) {
                        if (a25.sendToClient()) {
                            stringBuilder21.append(" ").append((String)DebugScreenOverlay.HEIGHTMAP_NAMES.get(a25)).append(": ").append(cge16.getHeight(a25, fx7.getX(), fx7.getZ()));
                        }
                    }
                    list14.add(stringBuilder21.toString());
                    stringBuilder21.setLength(0);
                    stringBuilder21.append("SH");
                    for (final Heightmap.Types a25 : Heightmap.Types.values()) {
                        if (a25.keepAfterWorldgen()) {
                            stringBuilder21.append(" ").append((String)DebugScreenOverlay.HEIGHTMAP_NAMES.get(a25)).append(": ");
                            if (cge17 != null) {
                                stringBuilder21.append(cge17.getHeight(a25, fx7.getX(), fx7.getZ()));
                            }
                            else {
                                stringBuilder21.append("??");
                            }
                        }
                    }
                    list14.add(stringBuilder21.toString());
                    if (fx7.getY() >= 0 && fx7.getY() < 256) {
                        list14.add(new StringBuilder().append("Biome: ").append((Object)this.minecraft.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(this.minecraft.level.getBiome(fx7))).toString());
                        long long22 = 0L;
                        float float7 = 0.0f;
                        if (cge17 != null) {
                            float7 = bru12.getMoonBrightness();
                            long22 = cge17.getInhabitedTime();
                        }
                        final DifficultyInstance aop25 = new DifficultyInstance(bru12.getDifficulty(), bru12.getDayTime(), long22, float7);
                        list14.add(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", new Object[] { aop25.getEffectiveDifficulty(), aop25.getSpecialMultiplier(), this.minecraft.level.getDayTime() / 24000L }));
                    }
                }
            }
            else {
                list14.add("Outside of world...");
            }
        }
        else {
            list14.add("Outside of world...");
        }
        final ServerLevel aag16 = this.getServerLevel();
        if (aag16 != null) {
            final NaturalSpawner.SpawnState d17 = aag16.getChunkSource().getLastSpawnState();
            if (d17 != null) {
                final Object2IntMap<MobCategory> object2IntMap18 = d17.getMobCategoryCounts();
                final int integer19 = d17.getSpawnableChunkCount();
                list14.add(new StringBuilder().append("SC: ").append(integer19).append(", ").append((String)Stream.of((Object[])MobCategory.values()).map(aql -> new StringBuilder().append(Character.toUpperCase(aql.getName().charAt(0))).append(": ").append(object2IntMap18.getInt((Object)aql)).toString()).collect(Collectors.joining(", "))).toString());
            }
            else {
                list14.add("SC: N/A");
            }
        }
        final PostChain eab17 = this.minecraft.gameRenderer.currentEffect();
        if (eab17 != null) {
            list14.add(("Shader: " + eab17.getName()));
        }
        list14.add((this.minecraft.getSoundManager().getDebugString() + String.format(" (Mood %d%%)", new Object[] { Math.round(this.minecraft.player.getCurrentMood() * 100.0f) })));
        return list14;
    }
    
    @Nullable
    private ServerLevel getServerLevel() {
        final IntegratedServer emy2 = this.minecraft.getSingleplayerServer();
        if (emy2 != null) {
            return emy2.getLevel(this.minecraft.level.dimension());
        }
        return null;
    }
    
    @Nullable
    private String getServerChunkStats() {
        final ServerLevel aag2 = this.getServerLevel();
        if (aag2 != null) {
            return aag2.gatherChunkSourceStats();
        }
        return null;
    }
    
    private Level getLevel() {
        return (Level)DataFixUtils.orElse(Optional.ofNullable(this.minecraft.getSingleplayerServer()).flatMap(emy -> Optional.ofNullable(emy.getLevel(this.minecraft.level.dimension()))), this.minecraft.level);
    }
    
    @Nullable
    private LevelChunk getServerChunk() {
        if (this.serverChunk == null) {
            final ServerLevel aag2 = this.getServerLevel();
            if (aag2 != null) {
                this.serverChunk = (CompletableFuture<LevelChunk>)aag2.getChunkSource().getChunkFuture(this.lastPos.x, this.lastPos.z, ChunkStatus.FULL, false).thenApply(either -> (LevelChunk)either.map(cft -> (LevelChunk)cft, a -> null));
            }
            if (this.serverChunk == null) {
                this.serverChunk = (CompletableFuture<LevelChunk>)CompletableFuture.completedFuture(this.getClientChunk());
            }
        }
        return (LevelChunk)this.serverChunk.getNow(null);
    }
    
    private LevelChunk getClientChunk() {
        if (this.clientChunk == null) {
            this.clientChunk = this.minecraft.level.getChunk(this.lastPos.x, this.lastPos.z);
        }
        return this.clientChunk;
    }
    
    protected List<String> getSystemInformation() {
        final long long2 = Runtime.getRuntime().maxMemory();
        final long long3 = Runtime.getRuntime().totalMemory();
        final long long4 = Runtime.getRuntime().freeMemory();
        final long long5 = long3 - long4;
        final List<String> list10 = (List<String>)Lists.newArrayList((Object[])new String[] { String.format("Java: %s %dbit", new Object[] { System.getProperty("java.version"), this.minecraft.is64Bit() ? 64 : 32 }), String.format("Mem: % 2d%% %03d/%03dMB", new Object[] { long5 * 100L / long2, bytesToMegabytes(long5), bytesToMegabytes(long2) }), String.format("Allocated: % 2d%% %03dMB", new Object[] { long3 * 100L / long2, bytesToMegabytes(long3) }), "", String.format("CPU: %s", new Object[] { GlUtil.getCpuInfo() }), "", String.format("Display: %dx%d (%s)", new Object[] { Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight(), GlUtil.getVendor() }), GlUtil.getRenderer(), GlUtil.getOpenGLVersion() });
        if (this.minecraft.showOnlyReducedInfo()) {
            return list10;
        }
        if (this.block.getType() == HitResult.Type.BLOCK) {
            final BlockPos fx11 = ((BlockHitResult)this.block).getBlockPos();
            final BlockState cee12 = this.minecraft.level.getBlockState(fx11);
            list10.add("");
            list10.add(new StringBuilder().append((Object)ChatFormatting.UNDERLINE).append("Targeted Block: ").append(fx11.getX()).append(", ").append(fx11.getY()).append(", ").append(fx11.getZ()).toString());
            list10.add(String.valueOf((Object)Registry.BLOCK.getKey(cee12.getBlock())));
            for (final Map.Entry<Property<?>, Comparable<?>> entry14 : cee12.getValues().entrySet()) {
                list10.add(this.getPropertyValueString(entry14));
            }
            for (final ResourceLocation vk14 : this.minecraft.getConnection().getTags().getBlocks().getMatchingTags(cee12.getBlock())) {
                list10.add(new StringBuilder().append("#").append((Object)vk14).toString());
            }
        }
        if (this.liquid.getType() == HitResult.Type.BLOCK) {
            final BlockPos fx11 = ((BlockHitResult)this.liquid).getBlockPos();
            final FluidState cuu12 = this.minecraft.level.getFluidState(fx11);
            list10.add("");
            list10.add(new StringBuilder().append((Object)ChatFormatting.UNDERLINE).append("Targeted Fluid: ").append(fx11.getX()).append(", ").append(fx11.getY()).append(", ").append(fx11.getZ()).toString());
            list10.add(String.valueOf((Object)Registry.FLUID.getKey(cuu12.getType())));
            for (final Map.Entry<Property<?>, Comparable<?>> entry14 : cuu12.getValues().entrySet()) {
                list10.add(this.getPropertyValueString(entry14));
            }
            for (final ResourceLocation vk14 : this.minecraft.getConnection().getTags().getFluids().getMatchingTags(cuu12.getType())) {
                list10.add(new StringBuilder().append("#").append((Object)vk14).toString());
            }
        }
        final Entity apx11 = this.minecraft.crosshairPickEntity;
        if (apx11 != null) {
            list10.add("");
            list10.add(new StringBuilder().append((Object)ChatFormatting.UNDERLINE).append("Targeted Entity").toString());
            list10.add(String.valueOf((Object)Registry.ENTITY_TYPE.getKey(apx11.getType())));
        }
        return list10;
    }
    
    private String getPropertyValueString(final Map.Entry<Property<?>, Comparable<?>> entry) {
        final Property<?> cfg3 = entry.getKey();
        final Comparable<?> comparable4 = entry.getValue();
        String string5 = Util.getPropertyName(cfg3, comparable4);
        if (Boolean.TRUE.equals(comparable4)) {
            string5 = ChatFormatting.GREEN + string5;
        }
        else if (Boolean.FALSE.equals(comparable4)) {
            string5 = ChatFormatting.RED + string5;
        }
        return cfg3.getName() + ": " + string5;
    }
    
    private void drawChart(final PoseStack dfj, final FrameTimer aez, final int integer3, final int integer4, final boolean boolean5) {
        RenderSystem.disableDepthTest();
        final int integer5 = aez.getLogStart();
        final int integer6 = aez.getLogEnd();
        final long[] arr9 = aez.getLog();
        int integer7 = integer5;
        int integer8 = integer3;
        final int integer9 = Math.max(0, arr9.length - integer4);
        final int integer10 = arr9.length - integer9;
        integer7 = aez.wrapIndex(integer7 + integer9);
        long long14 = 0L;
        int integer11 = Integer.MAX_VALUE;
        int integer12 = Integer.MIN_VALUE;
        for (int integer13 = 0; integer13 < integer10; ++integer13) {
            final int integer14 = (int)(arr9[aez.wrapIndex(integer7 + integer13)] / 1000000L);
            integer11 = Math.min(integer11, integer14);
            integer12 = Math.max(integer12, integer14);
            long14 += integer14;
        }
        int integer13 = this.minecraft.getWindow().getGuiScaledHeight();
        GuiComponent.fill(dfj, integer3, integer13 - 60, integer3 + integer10, integer13, -1873784752);
        final BufferBuilder dfe19 = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        dfe19.begin(7, DefaultVertexFormat.POSITION_COLOR);
        final Matrix4f b20 = Transformation.identity().getMatrix();
        while (integer7 != integer6) {
            final int integer15 = aez.scaleSampleTo(arr9[integer7], boolean5 ? 30 : 60, boolean5 ? 60 : 20);
            final int integer16 = boolean5 ? 100 : 60;
            final int integer17 = this.getSampleColor(Mth.clamp(integer15, 0, integer16), 0, integer16 / 2, integer16);
            final int integer18 = integer17 >> 24 & 0xFF;
            final int integer19 = integer17 >> 16 & 0xFF;
            final int integer20 = integer17 >> 8 & 0xFF;
            final int integer21 = integer17 & 0xFF;
            dfe19.vertex(b20, (float)(integer8 + 1), (float)integer13, 0.0f).color(integer19, integer20, integer21, integer18).endVertex();
            dfe19.vertex(b20, (float)(integer8 + 1), (float)(integer13 - integer15 + 1), 0.0f).color(integer19, integer20, integer21, integer18).endVertex();
            dfe19.vertex(b20, (float)integer8, (float)(integer13 - integer15 + 1), 0.0f).color(integer19, integer20, integer21, integer18).endVertex();
            dfe19.vertex(b20, (float)integer8, (float)integer13, 0.0f).color(integer19, integer20, integer21, integer18).endVertex();
            ++integer8;
            integer7 = aez.wrapIndex(integer7 + 1);
        }
        dfe19.end();
        BufferUploader.end(dfe19);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        if (boolean5) {
            GuiComponent.fill(dfj, integer3 + 1, integer13 - 30 + 1, integer3 + 14, integer13 - 30 + 10, -1873784752);
            this.font.draw(dfj, "60 FPS", (float)(integer3 + 2), (float)(integer13 - 30 + 2), 14737632);
            this.hLine(dfj, integer3, integer3 + integer10 - 1, integer13 - 30, -1);
            GuiComponent.fill(dfj, integer3 + 1, integer13 - 60 + 1, integer3 + 14, integer13 - 60 + 10, -1873784752);
            this.font.draw(dfj, "30 FPS", (float)(integer3 + 2), (float)(integer13 - 60 + 2), 14737632);
            this.hLine(dfj, integer3, integer3 + integer10 - 1, integer13 - 60, -1);
        }
        else {
            GuiComponent.fill(dfj, integer3 + 1, integer13 - 60 + 1, integer3 + 14, integer13 - 60 + 10, -1873784752);
            this.font.draw(dfj, "20 TPS", (float)(integer3 + 2), (float)(integer13 - 60 + 2), 14737632);
            this.hLine(dfj, integer3, integer3 + integer10 - 1, integer13 - 60, -1);
        }
        this.hLine(dfj, integer3, integer3 + integer10 - 1, integer13 - 1, -1);
        this.vLine(dfj, integer3, integer13 - 60, integer13, -1);
        this.vLine(dfj, integer3 + integer10 - 1, integer13 - 60, integer13, -1);
        if (boolean5 && this.minecraft.options.framerateLimit > 0 && this.minecraft.options.framerateLimit <= 250) {
            this.hLine(dfj, integer3, integer3 + integer10 - 1, integer13 - 1 - (int)(1800.0 / this.minecraft.options.framerateLimit), -16711681);
        }
        final String string21 = new StringBuilder().append(integer11).append(" ms min").toString();
        final String string22 = new StringBuilder().append(long14 / integer10).append(" ms avg").toString();
        final String string23 = new StringBuilder().append(integer12).append(" ms max").toString();
        final Font font = this.font;
        final String string24 = string21;
        final float float3 = (float)(integer3 + 2);
        final int n = integer13 - 60;
        this.font.getClass();
        font.drawShadow(dfj, string24, float3, (float)(n - 9), 14737632);
        final Font font2 = this.font;
        final String string25 = string22;
        final float float4 = (float)(integer3 + integer10 / 2 - this.font.width(string22) / 2);
        final int n2 = integer13 - 60;
        this.font.getClass();
        font2.drawShadow(dfj, string25, float4, (float)(n2 - 9), 14737632);
        final Font font3 = this.font;
        final String string26 = string23;
        final float float5 = (float)(integer3 + integer10 - this.font.width(string23));
        final int n3 = integer13 - 60;
        this.font.getClass();
        font3.drawShadow(dfj, string26, float5, (float)(n3 - 9), 14737632);
        RenderSystem.enableDepthTest();
    }
    
    private int getSampleColor(final int integer1, final int integer2, final int integer3, final int integer4) {
        if (integer1 < integer3) {
            return this.colorLerp(-16711936, -256, integer1 / (float)integer3);
        }
        return this.colorLerp(-256, -65536, (integer1 - integer3) / (float)(integer4 - integer3));
    }
    
    private int colorLerp(final int integer1, final int integer2, final float float3) {
        final int integer3 = integer1 >> 24 & 0xFF;
        final int integer4 = integer1 >> 16 & 0xFF;
        final int integer5 = integer1 >> 8 & 0xFF;
        final int integer6 = integer1 & 0xFF;
        final int integer7 = integer2 >> 24 & 0xFF;
        final int integer8 = integer2 >> 16 & 0xFF;
        final int integer9 = integer2 >> 8 & 0xFF;
        final int integer10 = integer2 & 0xFF;
        final int integer11 = Mth.clamp((int)Mth.lerp(float3, (float)integer3, (float)integer7), 0, 255);
        final int integer12 = Mth.clamp((int)Mth.lerp(float3, (float)integer4, (float)integer8), 0, 255);
        final int integer13 = Mth.clamp((int)Mth.lerp(float3, (float)integer5, (float)integer9), 0, 255);
        final int integer14 = Mth.clamp((int)Mth.lerp(float3, (float)integer6, (float)integer10), 0, 255);
        return integer11 << 24 | integer12 << 16 | integer13 << 8 | integer14;
    }
    
    private static long bytesToMegabytes(final long long1) {
        return long1 / 1024L / 1024L;
    }
    
    static {
        HEIGHTMAP_NAMES = Util.<Map>make((Map)new EnumMap((Class)Heightmap.Types.class), (java.util.function.Consumer<Map>)(enumMap -> {
            enumMap.put((Enum)Heightmap.Types.WORLD_SURFACE_WG, "SW");
            enumMap.put((Enum)Heightmap.Types.WORLD_SURFACE, "S");
            enumMap.put((Enum)Heightmap.Types.OCEAN_FLOOR_WG, "OW");
            enumMap.put((Enum)Heightmap.Types.OCEAN_FLOOR, "O");
            enumMap.put((Enum)Heightmap.Types.MOTION_BLOCKING, "M");
            enumMap.put((Enum)Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, "ML");
        }));
    }
}
