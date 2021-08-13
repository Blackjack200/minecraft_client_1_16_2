package net.minecraft.client.gui.font.providers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import javax.annotation.Nullable;
import com.mojang.blaze3d.font.RawGlyph;
import net.minecraft.server.packs.resources.Resource;
import java.util.Arrays;
import java.io.IOException;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.font.GlyphProvider;

public class LegacyUnicodeBitmapsProvider implements GlyphProvider {
    private static final Logger LOGGER;
    private final ResourceManager resourceManager;
    private final byte[] sizes;
    private final String texturePattern;
    private final Map<ResourceLocation, NativeImage> textures;
    
    public LegacyUnicodeBitmapsProvider(final ResourceManager acf, final byte[] arr, final String string) {
        this.textures = (Map<ResourceLocation, NativeImage>)Maps.newHashMap();
        this.resourceManager = acf;
        this.sizes = arr;
        this.texturePattern = string;
        for (int integer5 = 0; integer5 < 256; ++integer5) {
            final int integer6 = integer5 * 256;
            final ResourceLocation vk7 = this.getSheetLocation(integer6);
            try (final Resource ace8 = this.resourceManager.getResource(vk7);
                 final NativeImage deq10 = NativeImage.read(NativeImage.Format.RGBA, ace8.getInputStream())) {
                if (deq10.getWidth() == 256 && deq10.getHeight() == 256) {
                    for (int integer7 = 0; integer7 < 256; ++integer7) {
                        final byte byte13 = arr[integer6 + integer7];
                        if (byte13 != 0 && getLeft(byte13) > getRight(byte13)) {
                            arr[integer6 + integer7] = 0;
                        }
                    }
                    continue;
                }
            }
            catch (IOException ex) {}
            Arrays.fill(arr, integer6, integer6 + 256, (byte)0);
        }
    }
    
    public void close() {
        this.textures.values().forEach(NativeImage::close);
    }
    
    private ResourceLocation getSheetLocation(final int integer) {
        final ResourceLocation vk3 = new ResourceLocation(String.format(this.texturePattern, new Object[] { String.format("%02x", new Object[] { integer / 256 }) }));
        return new ResourceLocation(vk3.getNamespace(), "textures/" + vk3.getPath());
    }
    
    @Nullable
    public RawGlyph getGlyph(final int integer) {
        if (integer < 0 || integer > 65535) {
            return null;
        }
        final byte byte3 = this.sizes[integer];
        if (byte3 != 0) {
            final NativeImage deq4 = (NativeImage)this.textures.computeIfAbsent(this.getSheetLocation(integer), this::loadTexture);
            if (deq4 != null) {
                final int integer2 = getLeft(byte3);
                return new Glyph(integer % 16 * 16 + integer2, (integer & 0xFF) / 16 * 16, getRight(byte3) - integer2, 16, deq4);
            }
        }
        return null;
    }
    
    public IntSet getSupportedGlyphs() {
        final IntSet intSet2 = (IntSet)new IntOpenHashSet();
        for (int integer3 = 0; integer3 < 65535; ++integer3) {
            if (this.sizes[integer3] != 0) {
                intSet2.add(integer3);
            }
        }
        return intSet2;
    }
    
    @Nullable
    private NativeImage loadTexture(final ResourceLocation vk) {
        try (final Resource ace3 = this.resourceManager.getResource(vk)) {
            return NativeImage.read(NativeImage.Format.RGBA, ace3.getInputStream());
        }
        catch (IOException iOException3) {
            LegacyUnicodeBitmapsProvider.LOGGER.error("Couldn't load texture {}", vk, iOException3);
            return null;
        }
    }
    
    private static int getLeft(final byte byte1) {
        return byte1 >> 4 & 0xF;
    }
    
    private static int getRight(final byte byte1) {
        return (byte1 & 0xF) + 1;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Builder implements GlyphProviderBuilder {
        private final ResourceLocation metadata;
        private final String texturePattern;
        
        public Builder(final ResourceLocation vk, final String string) {
            this.metadata = vk;
            this.texturePattern = string;
        }
        
        public static GlyphProviderBuilder fromJson(final JsonObject jsonObject) {
            return new Builder(new ResourceLocation(GsonHelper.getAsString(jsonObject, "sizes")), GsonHelper.getAsString(jsonObject, "template"));
        }
        
        @Nullable
        public GlyphProvider create(final ResourceManager acf) {
            try (final Resource ace3 = Minecraft.getInstance().getResourceManager().getResource(this.metadata)) {
                final byte[] arr5 = new byte[65536];
                ace3.getInputStream().read(arr5);
                return new LegacyUnicodeBitmapsProvider(acf, arr5, this.texturePattern);
            }
            catch (IOException iOException3) {
                LegacyUnicodeBitmapsProvider.LOGGER.error("Cannot load {}, unicode glyphs will not render correctly", this.metadata);
                return null;
            }
        }
    }
    
    static class Glyph implements RawGlyph {
        private final int width;
        private final int height;
        private final int sourceX;
        private final int sourceY;
        private final NativeImage source;
        
        private Glyph(final int integer1, final int integer2, final int integer3, final int integer4, final NativeImage deq) {
            this.width = integer3;
            this.height = integer4;
            this.sourceX = integer1;
            this.sourceY = integer2;
            this.source = deq;
        }
        
        public float getOversample() {
            return 2.0f;
        }
        
        public int getPixelWidth() {
            return this.width;
        }
        
        public int getPixelHeight() {
            return this.height;
        }
        
        public float getAdvance() {
            return (float)(this.width / 2 + 1);
        }
        
        public void upload(final int integer1, final int integer2) {
            this.source.upload(0, integer1, integer2, this.sourceX, this.sourceY, this.width, this.height, false, false);
        }
        
        public boolean isColored() {
            return this.source.format().components() > 1;
        }
        
        public float getShadowOffset() {
            return 0.5f;
        }
        
        public float getBoldOffset() {
            return 0.5f;
        }
    }
}
