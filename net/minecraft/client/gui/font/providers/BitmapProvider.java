package net.minecraft.client.gui.font.providers;

import net.minecraft.server.packs.resources.Resource;
import java.io.IOException;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonArray;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntSet;
import javax.annotation.Nullable;
import com.mojang.blaze3d.font.RawGlyph;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.mojang.blaze3d.platform.NativeImage;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.font.GlyphProvider;

public class BitmapProvider implements GlyphProvider {
    private static final Logger LOGGER;
    private final NativeImage image;
    private final Int2ObjectMap<Glyph> glyphs;
    
    private BitmapProvider(final NativeImage deq, final Int2ObjectMap<Glyph> int2ObjectMap) {
        this.image = deq;
        this.glyphs = int2ObjectMap;
    }
    
    public void close() {
        this.image.close();
    }
    
    @Nullable
    public RawGlyph getGlyph(final int integer) {
        return (RawGlyph)this.glyphs.get(integer);
    }
    
    public IntSet getSupportedGlyphs() {
        return IntSets.unmodifiable(this.glyphs.keySet());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Builder implements GlyphProviderBuilder {
        private final ResourceLocation texture;
        private final List<int[]> chars;
        private final int height;
        private final int ascent;
        
        public Builder(final ResourceLocation vk, final int integer2, final int integer3, final List<int[]> list) {
            this.texture = new ResourceLocation(vk.getNamespace(), "textures/" + vk.getPath());
            this.chars = list;
            this.height = integer2;
            this.ascent = integer3;
        }
        
        public static Builder fromJson(final JsonObject jsonObject) {
            final int integer2 = GsonHelper.getAsInt(jsonObject, "height", 8);
            final int integer3 = GsonHelper.getAsInt(jsonObject, "ascent");
            if (integer3 > integer2) {
                throw new JsonParseException(new StringBuilder().append("Ascent ").append(integer3).append(" higher than height ").append(integer2).toString());
            }
            final List<int[]> list4 = (List<int[]>)Lists.newArrayList();
            final JsonArray jsonArray5 = GsonHelper.getAsJsonArray(jsonObject, "chars");
            for (int integer4 = 0; integer4 < jsonArray5.size(); ++integer4) {
                final String string7 = GsonHelper.convertToString(jsonArray5.get(integer4), new StringBuilder().append("chars[").append(integer4).append("]").toString());
                final int[] arr8 = string7.codePoints().toArray();
                if (integer4 > 0) {
                    final int integer5 = ((int[])list4.get(0)).length;
                    if (arr8.length != integer5) {
                        throw new JsonParseException(new StringBuilder().append("Elements of chars have to be the same length (found: ").append(arr8.length).append(", expected: ").append(integer5).append("), pad with space or \\u0000").toString());
                    }
                }
                list4.add(arr8);
            }
            if (list4.isEmpty() || ((int[])list4.get(0)).length == 0) {
                throw new JsonParseException("Expected to find data in chars, found none.");
            }
            return new Builder(new ResourceLocation(GsonHelper.getAsString(jsonObject, "file")), integer2, integer3, list4);
        }
        
        @Nullable
        public GlyphProvider create(final ResourceManager acf) {
            try (final Resource ace3 = acf.getResource(this.texture)) {
                final NativeImage deq5 = NativeImage.read(NativeImage.Format.RGBA, ace3.getInputStream());
                final int integer6 = deq5.getWidth();
                final int integer7 = deq5.getHeight();
                final int integer8 = integer6 / ((int[])this.chars.get(0)).length;
                final int integer9 = integer7 / this.chars.size();
                final float float10 = this.height / (float)integer9;
                final Int2ObjectMap<Glyph> int2ObjectMap11 = (Int2ObjectMap<Glyph>)new Int2ObjectOpenHashMap();
                for (int integer10 = 0; integer10 < this.chars.size(); ++integer10) {
                    int integer11 = 0;
                    for (final int integer12 : (int[])this.chars.get(integer10)) {
                        final int integer13 = integer11++;
                        if (integer12 != 0) {
                            if (integer12 != 32) {
                                final int integer14 = this.getActualGlyphWidth(deq5, integer8, integer9, integer13, integer10);
                                final Glyph b20 = (Glyph)int2ObjectMap11.put(integer12, new Glyph(float10, deq5, integer13 * integer8, integer10 * integer9, integer8, integer9, (int)(0.5 + integer14 * float10) + 1, this.ascent));
                                if (b20 != null) {
                                    BitmapProvider.LOGGER.warn("Codepoint '{}' declared multiple times in {}", Integer.toHexString(integer12), this.texture);
                                }
                            }
                        }
                    }
                }
                return new BitmapProvider(deq5, int2ObjectMap11, null);
            }
            catch (IOException iOException3) {
                throw new RuntimeException(iOException3.getMessage());
            }
        }
        
        private int getActualGlyphWidth(final NativeImage deq, final int integer2, final int integer3, final int integer4, final int integer5) {
            int integer6;
            for (integer6 = integer2 - 1; integer6 >= 0; --integer6) {
                final int integer7 = integer4 * integer2 + integer6;
                for (int integer8 = 0; integer8 < integer3; ++integer8) {
                    final int integer9 = integer5 * integer3 + integer8;
                    if (deq.getLuminanceOrAlpha(integer7, integer9) != 0) {
                        return integer6 + 1;
                    }
                }
            }
            return integer6 + 1;
        }
    }
    
    static final class Glyph implements RawGlyph {
        private final float scale;
        private final NativeImage image;
        private final int offsetX;
        private final int offsetY;
        private final int width;
        private final int height;
        private final int advance;
        private final int ascent;
        
        private Glyph(final float float1, final NativeImage deq, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
            this.scale = float1;
            this.image = deq;
            this.offsetX = integer3;
            this.offsetY = integer4;
            this.width = integer5;
            this.height = integer6;
            this.advance = integer7;
            this.ascent = integer8;
        }
        
        public float getOversample() {
            return 1.0f / this.scale;
        }
        
        public int getPixelWidth() {
            return this.width;
        }
        
        public int getPixelHeight() {
            return this.height;
        }
        
        public float getAdvance() {
            return (float)this.advance;
        }
        
        public float getBearingY() {
            return super.getBearingY() + 7.0f - this.ascent;
        }
        
        public void upload(final int integer1, final int integer2) {
            this.image.upload(0, integer1, integer2, this.offsetX, this.offsetY, this.width, this.height, false, false);
        }
        
        public boolean isColored() {
            return this.image.format().components() > 1;
        }
    }
}
