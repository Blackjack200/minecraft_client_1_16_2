package com.mojang.blaze3d.font;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.stream.IntStream;
import java.nio.Buffer;
import org.lwjgl.system.MemoryUtil;
import javax.annotation.Nullable;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBTruetype;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.lwjgl.stb.STBTTFontinfo;
import java.nio.ByteBuffer;

public class TrueTypeGlyphProvider implements GlyphProvider {
    private final ByteBuffer fontMemory;
    private final STBTTFontinfo font;
    private final float oversample;
    private final IntSet skip;
    private final float shiftX;
    private final float shiftY;
    private final float pointScale;
    private final float ascent;
    
    public TrueTypeGlyphProvider(final ByteBuffer byteBuffer, final STBTTFontinfo sTBTTFontinfo, final float float3, final float float4, final float float5, final float float6, final String string) {
        this.skip = (IntSet)new IntArraySet();
        this.fontMemory = byteBuffer;
        this.font = sTBTTFontinfo;
        this.oversample = float4;
        string.codePoints().forEach(this.skip::add);
        this.shiftX = float5 * float4;
        this.shiftY = float6 * float4;
        this.pointScale = STBTruetype.stbtt_ScaleForPixelHeight(sTBTTFontinfo, float3 * float4);
        try (final MemoryStack memoryStack9 = MemoryStack.stackPush()) {
            final IntBuffer intBuffer11 = memoryStack9.mallocInt(1);
            final IntBuffer intBuffer12 = memoryStack9.mallocInt(1);
            final IntBuffer intBuffer13 = memoryStack9.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(sTBTTFontinfo, intBuffer11, intBuffer12, intBuffer13);
            this.ascent = intBuffer11.get(0) * this.pointScale;
        }
    }
    
    @Nullable
    public Glyph getGlyph(final int integer) {
        if (this.skip.contains(integer)) {
            return null;
        }
        try (final MemoryStack memoryStack3 = MemoryStack.stackPush()) {
            final IntBuffer intBuffer5 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer6 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer7 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer8 = memoryStack3.mallocInt(1);
            final int integer2 = STBTruetype.stbtt_FindGlyphIndex(this.font, integer);
            if (integer2 == 0) {
                return null;
            }
            STBTruetype.stbtt_GetGlyphBitmapBoxSubpixel(this.font, integer2, this.pointScale, this.pointScale, this.shiftX, this.shiftY, intBuffer5, intBuffer6, intBuffer7, intBuffer8);
            final int integer3 = intBuffer7.get(0) - intBuffer5.get(0);
            final int integer4 = intBuffer8.get(0) - intBuffer6.get(0);
            if (integer3 == 0 || integer4 == 0) {
                return null;
            }
            final IntBuffer intBuffer9 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer10 = memoryStack3.mallocInt(1);
            STBTruetype.stbtt_GetGlyphHMetrics(this.font, integer2, intBuffer9, intBuffer10);
            return new Glyph(intBuffer5.get(0), intBuffer7.get(0), -intBuffer6.get(0), -intBuffer8.get(0), intBuffer9.get(0) * this.pointScale, intBuffer10.get(0) * this.pointScale, integer2);
        }
    }
    
    public void close() {
        this.font.free();
        MemoryUtil.memFree((Buffer)this.fontMemory);
    }
    
    public IntSet getSupportedGlyphs() {
        return (IntSet)IntStream.range(0, 65535).filter(integer -> !this.skip.contains(integer)).collect(IntOpenHashSet::new, IntCollection::add, IntCollection::addAll);
    }
    
    class Glyph implements RawGlyph {
        private final int width;
        private final int height;
        private final float bearingX;
        private final float bearingY;
        private final float advance;
        private final int index;
        
        private Glyph(final int integer2, final int integer3, final int integer4, final int integer5, final float float6, final float float7, final int integer8) {
            this.width = integer3 - integer2;
            this.height = integer4 - integer5;
            this.advance = float6 / TrueTypeGlyphProvider.this.oversample;
            this.bearingX = (float7 + integer2 + TrueTypeGlyphProvider.this.shiftX) / TrueTypeGlyphProvider.this.oversample;
            this.bearingY = (TrueTypeGlyphProvider.this.ascent - integer4 + TrueTypeGlyphProvider.this.shiftY) / TrueTypeGlyphProvider.this.oversample;
            this.index = integer8;
        }
        
        public int getPixelWidth() {
            return this.width;
        }
        
        public int getPixelHeight() {
            return this.height;
        }
        
        public float getOversample() {
            return TrueTypeGlyphProvider.this.oversample;
        }
        
        public float getAdvance() {
            return this.advance;
        }
        
        public float getBearingX() {
            return this.bearingX;
        }
        
        public float getBearingY() {
            return this.bearingY;
        }
        
        public void upload(final int integer1, final int integer2) {
            final NativeImage deq4 = new NativeImage(NativeImage.Format.LUMINANCE, this.width, this.height, false);
            deq4.copyFromFont(TrueTypeGlyphProvider.this.font, this.index, this.width, this.height, TrueTypeGlyphProvider.this.pointScale, TrueTypeGlyphProvider.this.pointScale, TrueTypeGlyphProvider.this.shiftX, TrueTypeGlyphProvider.this.shiftY, 0, 0);
            deq4.upload(0, integer1, integer2, 0, 0, this.width, this.height, false, true);
        }
        
        public boolean isColored() {
            return false;
        }
    }
}
