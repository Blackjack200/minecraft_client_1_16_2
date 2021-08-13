package net.minecraft.client.renderer.texture;

import net.minecraft.world.inventory.InventoryMenu;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import com.mojang.blaze3d.platform.PngInfo;
import javax.annotation.Nullable;
import net.minecraft.server.packs.resources.Resource;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.util.Mth;
import java.util.stream.Collectors;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.stream.Stream;
import net.minecraft.CrashReportCategory;
import java.util.Iterator;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import com.mojang.blaze3d.platform.TextureUtil;
import java.util.Collection;
import java.io.IOException;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.Set;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureAtlas extends AbstractTexture implements Tickable {
    private static final Logger LOGGER;
    @Deprecated
    public static final ResourceLocation LOCATION_BLOCKS;
    @Deprecated
    public static final ResourceLocation LOCATION_PARTICLES;
    private final List<TextureAtlasSprite> animatedTextures;
    private final Set<ResourceLocation> sprites;
    private final Map<ResourceLocation, TextureAtlasSprite> texturesByName;
    private final ResourceLocation location;
    private final int maxSupportedTextureSize;
    
    public TextureAtlas(final ResourceLocation vk) {
        this.animatedTextures = (List<TextureAtlasSprite>)Lists.newArrayList();
        this.sprites = (Set<ResourceLocation>)Sets.newHashSet();
        this.texturesByName = (Map<ResourceLocation, TextureAtlasSprite>)Maps.newHashMap();
        this.location = vk;
        this.maxSupportedTextureSize = RenderSystem.maxSupportedTextureSize();
    }
    
    @Override
    public void load(final ResourceManager acf) throws IOException {
    }
    
    public void reload(final Preparations a) {
        this.sprites.clear();
        this.sprites.addAll((Collection)a.sprites);
        TextureAtlas.LOGGER.info("Created: {}x{}x{} {}-atlas", a.width, a.height, a.mipLevel, this.location);
        TextureUtil.prepareImage(this.getId(), a.mipLevel, a.width, a.height);
        this.clearTextureData();
        for (final TextureAtlasSprite eju4 : a.regions) {
            this.texturesByName.put(eju4.getName(), eju4);
            try {
                eju4.uploadFirstFrame();
            }
            catch (Throwable throwable5) {
                final CrashReport l6 = CrashReport.forThrowable(throwable5, "Stitching texture atlas");
                final CrashReportCategory m7 = l6.addCategory("Texture being stitched together");
                m7.setDetail("Atlas path", this.location);
                m7.setDetail("Sprite", eju4);
                throw new ReportedException(l6);
            }
            if (eju4.isAnimation()) {
                this.animatedTextures.add(eju4);
            }
        }
    }
    
    public Preparations prepareToStitch(final ResourceManager acf, final Stream<ResourceLocation> stream, final ProfilerFiller ant, final int integer) {
        ant.push("preparing");
        final Set<ResourceLocation> set6 = (Set<ResourceLocation>)stream.peek(vk -> {
            if (vk == null) {
                throw new IllegalArgumentException("Location cannot be null!");
            }
        }).collect(Collectors.toSet());
        final int integer2 = this.maxSupportedTextureSize;
        final Stitcher ejr8 = new Stitcher(integer2, integer2, integer);
        int integer3 = Integer.MAX_VALUE;
        int integer4 = 1 << integer;
        ant.popPush("extracting_frames");
        for (final TextureAtlasSprite.Info a12 : this.getBasicSpriteInfos(acf, set6)) {
            integer3 = Math.min(integer3, Math.min(a12.width(), a12.height()));
            final int integer5 = Math.min(Integer.lowestOneBit(a12.width()), Integer.lowestOneBit(a12.height()));
            if (integer5 < integer4) {
                TextureAtlas.LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", a12.name(), a12.width(), a12.height(), Mth.log2(integer4), Mth.log2(integer5));
                integer4 = integer5;
            }
            ejr8.registerSprite(a12);
        }
        final int integer6 = Math.min(integer3, integer4);
        final int integer7 = Mth.log2(integer6);
        int integer5;
        if (integer7 < integer) {
            TextureAtlas.LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.location, integer, integer7, integer6);
            integer5 = integer7;
        }
        else {
            integer5 = integer;
        }
        ant.popPush("register");
        ejr8.registerSprite(MissingTextureAtlasSprite.info());
        ant.popPush("stitching");
        try {
            ejr8.stitch();
        }
        catch (StitcherException ejs14) {
            final CrashReport l15 = CrashReport.forThrowable((Throwable)ejs14, "Stitching");
            final CrashReportCategory m16 = l15.addCategory("Stitcher");
            m16.setDetail("Sprites", ejs14.getAllSprites().stream().map(a -> String.format("%s[%dx%d]", new Object[] { a.name(), a.width(), a.height() })).collect(Collectors.joining(",")));
            m16.setDetail("Max Texture Size", integer2);
            throw new ReportedException(l15);
        }
        ant.popPush("loading");
        final List<TextureAtlasSprite> list14 = this.getLoadedSprites(acf, ejr8, integer5);
        ant.pop();
        return new Preparations(set6, ejr8.getWidth(), ejr8.getHeight(), integer5, list14);
    }
    
    private Collection<TextureAtlasSprite.Info> getBasicSpriteInfos(final ResourceManager acf, final Set<ResourceLocation> set) {
        final List<CompletableFuture<?>> list4 = (List<CompletableFuture<?>>)Lists.newArrayList();
        final ConcurrentLinkedQueue<TextureAtlasSprite.Info> concurrentLinkedQueue5 = (ConcurrentLinkedQueue<TextureAtlasSprite.Info>)new ConcurrentLinkedQueue();
        for (final ResourceLocation vk7 : set) {
            if (MissingTextureAtlasSprite.getLocation().equals(vk7)) {
                continue;
            }
            list4.add(CompletableFuture.runAsync(() -> {
                final ResourceLocation vk2 = this.getResourceLocation(vk7);
                TextureAtlasSprite.Info a6;
                try (final Resource ace7 = acf.getResource(vk2)) {
                    final PngInfo der9 = new PngInfo(ace7.toString(), ace7.getInputStream());
                    AnimationMetadataSection eku10 = ace7.<AnimationMetadataSection>getMetadata((MetadataSectionSerializer<AnimationMetadataSection>)AnimationMetadataSection.SERIALIZER);
                    if (eku10 == null) {
                        eku10 = AnimationMetadataSection.EMPTY;
                    }
                    final Pair<Integer, Integer> pair11 = eku10.getFrameSize(der9.width, der9.height);
                    a6 = new TextureAtlasSprite.Info(vk7, (int)pair11.getFirst(), (int)pair11.getSecond(), eku10);
                }
                catch (RuntimeException runtimeException7) {
                    TextureAtlas.LOGGER.error("Unable to parse metadata from {} : {}", (Object)vk2, (Object)runtimeException7);
                    return;
                }
                catch (IOException iOException7) {
                    TextureAtlas.LOGGER.error("Using missing texture, unable to load {} : {}", (Object)vk2, (Object)iOException7);
                    return;
                }
                concurrentLinkedQueue5.add((Object)a6);
            }, Util.backgroundExecutor()));
        }
        CompletableFuture.allOf((CompletableFuture[])list4.toArray((Object[])new CompletableFuture[0])).join();
        return (Collection<TextureAtlasSprite.Info>)concurrentLinkedQueue5;
    }
    
    private List<TextureAtlasSprite> getLoadedSprites(final ResourceManager acf, final Stitcher ejr, final int integer) {
        final ConcurrentLinkedQueue<TextureAtlasSprite> concurrentLinkedQueue5 = (ConcurrentLinkedQueue<TextureAtlasSprite>)new ConcurrentLinkedQueue();
        final List<CompletableFuture<?>> list6 = (List<CompletableFuture<?>>)Lists.newArrayList();
        MissingTextureAtlasSprite ejn11;
        final ConcurrentLinkedQueue concurrentLinkedQueue6;
        final List list7;
        ejr.gatherSprites((a, integer6, integer7, integer8, integer9) -> {
            if (a == MissingTextureAtlasSprite.info()) {
                ejn11 = MissingTextureAtlasSprite.newInstance(this, integer, integer6, integer7, integer8, integer9);
                concurrentLinkedQueue6.add(ejn11);
            }
            else {
                list7.add(CompletableFuture.runAsync(() -> {
                    final TextureAtlasSprite eju10 = this.load(acf, a, integer6, integer7, integer, integer8, integer9);
                    if (eju10 != null) {
                        concurrentLinkedQueue6.add((Object)eju10);
                    }
                }, Util.backgroundExecutor()));
            }
            return;
        });
        CompletableFuture.allOf((CompletableFuture[])list6.toArray((Object[])new CompletableFuture[0])).join();
        return (List<TextureAtlasSprite>)Lists.newArrayList((Iterable)concurrentLinkedQueue5);
    }
    
    @Nullable
    private TextureAtlasSprite load(final ResourceManager acf, final TextureAtlasSprite.Info a, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        final ResourceLocation vk9 = this.getResourceLocation(a.name());
        try (final Resource ace10 = acf.getResource(vk9)) {
            final NativeImage deq12 = NativeImage.read(ace10.getInputStream());
            return new TextureAtlasSprite(this, a, integer5, integer3, integer4, integer6, integer7, deq12);
        }
        catch (RuntimeException runtimeException10) {
            TextureAtlas.LOGGER.error("Unable to parse metadata from {}", vk9, runtimeException10);
            return null;
        }
        catch (IOException iOException10) {
            TextureAtlas.LOGGER.error("Using missing texture, unable to load {}", vk9, iOException10);
            return null;
        }
    }
    
    private ResourceLocation getResourceLocation(final ResourceLocation vk) {
        return new ResourceLocation(vk.getNamespace(), String.format("textures/%s%s", new Object[] { vk.getPath(), ".png" }));
    }
    
    public void cycleAnimationFrames() {
        this.bind();
        for (final TextureAtlasSprite eju3 : this.animatedTextures) {
            eju3.cycleFrames();
        }
    }
    
    @Override
    public void tick() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::cycleAnimationFrames);
        }
        else {
            this.cycleAnimationFrames();
        }
    }
    
    public TextureAtlasSprite getSprite(final ResourceLocation vk) {
        final TextureAtlasSprite eju3 = (TextureAtlasSprite)this.texturesByName.get(vk);
        if (eju3 == null) {
            return (TextureAtlasSprite)this.texturesByName.get(MissingTextureAtlasSprite.getLocation());
        }
        return eju3;
    }
    
    public void clearTextureData() {
        for (final TextureAtlasSprite eju3 : this.texturesByName.values()) {
            eju3.close();
        }
        this.texturesByName.clear();
        this.animatedTextures.clear();
    }
    
    public ResourceLocation location() {
        return this.location;
    }
    
    public void updateFilter(final Preparations a) {
        this.setFilter(false, a.mipLevel > 0);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        LOCATION_BLOCKS = InventoryMenu.BLOCK_ATLAS;
        LOCATION_PARTICLES = new ResourceLocation("textures/atlas/particles.png");
    }
    
    public static class Preparations {
        final Set<ResourceLocation> sprites;
        final int width;
        final int height;
        final int mipLevel;
        final List<TextureAtlasSprite> regions;
        
        public Preparations(final Set<ResourceLocation> set, final int integer2, final int integer3, final int integer4, final List<TextureAtlasSprite> list) {
            this.sprites = set;
            this.width = integer2;
            this.height = integer3;
            this.mipLevel = integer4;
            this.regions = list;
        }
    }
}
