package com.mojang.blaze3d.platform;

import org.lwjgl.stb.STBIWriteCallback;
import java.util.EnumSet;
import org.apache.logging.log4j.LogManager;
import com.google.common.base.Charsets;
import java.util.Base64;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.stb.STBImageWrite;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.io.ByteArrayOutputStream;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Path;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBTTFontinfo;
import java.io.File;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.IntBuffer;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import org.apache.commons.io.IOUtils;
import java.nio.Buffer;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import org.lwjgl.system.MemoryUtil;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public final class NativeImage implements AutoCloseable {
    private static final Logger LOGGER;
    private static final Set<StandardOpenOption> OPEN_OPTIONS;
    private final Format format;
    private final int width;
    private final int height;
    private final boolean useStbFree;
    private long pixels;
    private final long size;
    
    public NativeImage(final int integer1, final int integer2, final boolean boolean3) {
        this(Format.RGBA, integer1, integer2, boolean3);
    }
    
    public NativeImage(final Format a, final int integer2, final int integer3, final boolean boolean4) {
        this.format = a;
        this.width = integer2;
        this.height = integer3;
        this.size = integer2 * (long)integer3 * a.components();
        this.useStbFree = false;
        if (boolean4) {
            this.pixels = MemoryUtil.nmemCalloc(1L, this.size);
        }
        else {
            this.pixels = MemoryUtil.nmemAlloc(this.size);
        }
    }
    
    private NativeImage(final Format a, final int integer2, final int integer3, final boolean boolean4, final long long5) {
        this.format = a;
        this.width = integer2;
        this.height = integer3;
        this.useStbFree = boolean4;
        this.pixels = long5;
        this.size = integer2 * integer3 * a.components();
    }
    
    public String toString() {
        return new StringBuilder().append("NativeImage[").append(this.format).append(" ").append(this.width).append("x").append(this.height).append("@").append(this.pixels).append(this.useStbFree ? "S" : "N").append("]").toString();
    }
    
    public static NativeImage read(final InputStream inputStream) throws IOException {
        return read(Format.RGBA, inputStream);
    }
    
    public static NativeImage read(@Nullable final Format a, final InputStream inputStream) throws IOException {
        ByteBuffer byteBuffer3 = null;
        try {
            byteBuffer3 = TextureUtil.readResource(inputStream);
            byteBuffer3.rewind();
            return read(a, byteBuffer3);
        }
        finally {
            MemoryUtil.memFree((Buffer)byteBuffer3);
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public static NativeImage read(final ByteBuffer byteBuffer) throws IOException {
        return read(Format.RGBA, byteBuffer);
    }
    
    public static NativeImage read(@Nullable final Format a, final ByteBuffer byteBuffer) throws IOException {
        if (a != null && !a.supportedByStb()) {
            throw new UnsupportedOperationException(new StringBuilder().append("Don't know how to read format ").append(a).toString());
        }
        if (MemoryUtil.memAddress(byteBuffer) == 0L) {
            throw new IllegalArgumentException("Invalid buffer");
        }
        try (final MemoryStack memoryStack3 = MemoryStack.stackPush()) {
            final IntBuffer intBuffer5 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer6 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer7 = memoryStack3.mallocInt(1);
            final ByteBuffer byteBuffer2 = STBImage.stbi_load_from_memory(byteBuffer, intBuffer5, intBuffer6, intBuffer7, (a == null) ? 0 : a.components);
            if (byteBuffer2 == null) {
                throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
            }
            return new NativeImage((a == null) ? getStbFormat(intBuffer7.get(0)) : a, intBuffer5.get(0), intBuffer6.get(0), true, MemoryUtil.memAddress(byteBuffer2));
        }
    }
    
    private static void setClamp(final boolean boolean1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (boolean1) {
            GlStateManager._texParameter(3553, 10242, 10496);
            GlStateManager._texParameter(3553, 10243, 10496);
        }
        else {
            GlStateManager._texParameter(3553, 10242, 10497);
            GlStateManager._texParameter(3553, 10243, 10497);
        }
    }
    
    private static void setFilter(final boolean boolean1, final boolean boolean2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (boolean1) {
            GlStateManager._texParameter(3553, 10241, boolean2 ? 9987 : 9729);
            GlStateManager._texParameter(3553, 10240, 9729);
        }
        else {
            GlStateManager._texParameter(3553, 10241, boolean2 ? 9986 : 9728);
            GlStateManager._texParameter(3553, 10240, 9728);
        }
    }
    
    private void checkAllocated() {
        if (this.pixels == 0L) {
            throw new IllegalStateException("Image is not allocated.");
        }
    }
    
    public void close() {
        if (this.pixels != 0L) {
            if (this.useStbFree) {
                STBImage.nstbi_image_free(this.pixels);
            }
            else {
                MemoryUtil.nmemFree(this.pixels);
            }
        }
        this.pixels = 0L;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Format format() {
        return this.format;
    }
    
    public int getPixelRGBA(final int integer1, final int integer2) {
        if (this.format != Format.RGBA) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", new Object[] { this.format }));
        }
        if (integer1 > this.width || integer2 > this.height) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", new Object[] { integer1, integer2, this.width, this.height }));
        }
        this.checkAllocated();
        final long long4 = (integer1 + integer2 * this.width) * 4;
        return MemoryUtil.memGetInt(this.pixels + long4);
    }
    
    public void setPixelRGBA(final int integer1, final int integer2, final int integer3) {
        if (this.format != Format.RGBA) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", new Object[] { this.format }));
        }
        if (integer1 > this.width || integer2 > this.height) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", new Object[] { integer1, integer2, this.width, this.height }));
        }
        this.checkAllocated();
        final long long5 = (integer1 + integer2 * this.width) * 4;
        MemoryUtil.memPutInt(this.pixels + long5, integer3);
    }
    
    public byte getLuminanceOrAlpha(final int integer1, final int integer2) {
        if (!this.format.hasLuminanceOrAlpha()) {
            throw new IllegalArgumentException(String.format("no luminance or alpha in %s", new Object[] { this.format }));
        }
        if (integer1 > this.width || integer2 > this.height) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", new Object[] { integer1, integer2, this.width, this.height }));
        }
        final int integer3 = (integer1 + integer2 * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
        return MemoryUtil.memGetByte(this.pixels + integer3);
    }
    
    @Deprecated
    public int[] makePixelArray() {
        if (this.format != Format.RGBA) {
            throw new UnsupportedOperationException("can only call makePixelArray for RGBA images.");
        }
        this.checkAllocated();
        final int[] arr2 = new int[this.getWidth() * this.getHeight()];
        for (int integer3 = 0; integer3 < this.getHeight(); ++integer3) {
            for (int integer4 = 0; integer4 < this.getWidth(); ++integer4) {
                final int integer5 = this.getPixelRGBA(integer4, integer3);
                final int integer6 = getA(integer5);
                final int integer7 = getB(integer5);
                final int integer8 = getG(integer5);
                final int integer9 = getR(integer5);
                final int integer10 = integer6 << 24 | integer9 << 16 | integer8 << 8 | integer7;
                arr2[integer4 + integer3 * this.getWidth()] = integer10;
            }
        }
        return arr2;
    }
    
    public void upload(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
        this.upload(integer1, integer2, integer3, 0, 0, this.width, this.height, false, boolean4);
    }
    
    public void upload(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final boolean boolean8, final boolean boolean9) {
        this.upload(integer1, integer2, integer3, integer4, integer5, integer6, integer7, false, false, boolean8, boolean9);
    }
    
    public void upload(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final boolean boolean8, final boolean boolean9, final boolean boolean10, final boolean boolean11) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> this._upload(integer1, integer2, integer3, integer4, integer5, integer6, integer7, boolean8, boolean9, boolean10, boolean11));
        }
        else {
            this._upload(integer1, integer2, integer3, integer4, integer5, integer6, integer7, boolean8, boolean9, boolean10, boolean11);
        }
    }
    
    private void _upload(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final boolean boolean8, final boolean boolean9, final boolean boolean10, final boolean boolean11) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.checkAllocated();
        setFilter(boolean8, boolean10);
        setClamp(boolean9);
        if (integer6 == this.getWidth()) {
            GlStateManager._pixelStore(3314, 0);
        }
        else {
            GlStateManager._pixelStore(3314, this.getWidth());
        }
        GlStateManager._pixelStore(3316, integer4);
        GlStateManager._pixelStore(3315, integer5);
        this.format.setUnpackPixelStoreState();
        GlStateManager._texSubImage2D(3553, integer1, integer2, integer3, integer6, integer7, this.format.glFormat(), 5121, this.pixels);
        if (boolean11) {
            this.close();
        }
    }
    
    public void downloadTexture(final int integer, final boolean boolean2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        this.checkAllocated();
        this.format.setPackPixelStoreState();
        GlStateManager._getTexImage(3553, integer, this.format.glFormat(), 5121, this.pixels);
        if (boolean2 && this.format.hasAlpha()) {
            for (int integer2 = 0; integer2 < this.getHeight(); ++integer2) {
                for (int integer3 = 0; integer3 < this.getWidth(); ++integer3) {
                    this.setPixelRGBA(integer3, integer2, this.getPixelRGBA(integer3, integer2) | 255 << this.format.alphaOffset());
                }
            }
        }
    }
    
    public void writeToFile(final File file) throws IOException {
        this.writeToFile(file.toPath());
    }
    
    public void copyFromFont(final STBTTFontinfo sTBTTFontinfo, final int integer2, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8, final int integer9, final int integer10) {
        if (integer9 < 0 || integer9 + integer3 > this.getWidth() || integer10 < 0 || integer10 + integer4 > this.getHeight()) {
            throw new IllegalArgumentException(String.format("Out of bounds: start: (%s, %s) (size: %sx%s); size: %sx%s", new Object[] { integer9, integer10, integer3, integer4, this.getWidth(), this.getHeight() }));
        }
        if (this.format.components() != 1) {
            throw new IllegalArgumentException("Can only write fonts into 1-component images.");
        }
        STBTruetype.nstbtt_MakeGlyphBitmapSubpixel(sTBTTFontinfo.address(), this.pixels + integer9 + integer10 * this.getWidth(), integer3, integer4, this.getWidth(), float5, float6, float7, float8, integer2);
    }
    
    public void writeToFile(final Path path) throws IOException {
        if (!this.format.supportedByStb()) {
            throw new UnsupportedOperationException(new StringBuilder().append("Don't know how to write format ").append(this.format).toString());
        }
        this.checkAllocated();
        try (final WritableByteChannel writableByteChannel3 = (WritableByteChannel)Files.newByteChannel(path, (Set)NativeImage.OPEN_OPTIONS, new FileAttribute[0])) {
            if (!this.writeToChannel(writableByteChannel3)) {
                throw new IOException(new StringBuilder().append("Could not write image to the PNG file \"").append(path.toAbsolutePath()).append("\": ").append(STBImage.stbi_failure_reason()).toString());
            }
        }
    }
    
    public byte[] asByteArray() throws IOException {
        try (final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
             final WritableByteChannel writableByteChannel4 = Channels.newChannel((OutputStream)byteArrayOutputStream2)) {
            if (!this.writeToChannel(writableByteChannel4)) {
                throw new IOException("Could not write image to byte array: " + STBImage.stbi_failure_reason());
            }
            return byteArrayOutputStream2.toByteArray();
        }
    }
    
    private boolean writeToChannel(final WritableByteChannel writableByteChannel) throws IOException {
        final WriteCallback c3 = new WriteCallback(writableByteChannel);
        try {
            final int integer4 = Math.min(this.getHeight(), Integer.MAX_VALUE / this.getWidth() / this.format.components());
            if (integer4 < this.getHeight()) {
                NativeImage.LOGGER.warn("Dropping image height from {} to {} to fit the size into 32-bit signed int", this.getHeight(), integer4);
            }
            if (STBImageWrite.nstbi_write_png_to_func(c3.address(), 0L, this.getWidth(), integer4, this.format.components(), this.pixels, 0) == 0) {
                return false;
            }
            c3.throwIfException();
            return true;
        }
        finally {
            c3.free();
        }
    }
    
    public void copyFrom(final NativeImage deq) {
        if (deq.format() != this.format) {
            throw new UnsupportedOperationException("Image formats don't match.");
        }
        final int integer3 = this.format.components();
        this.checkAllocated();
        deq.checkAllocated();
        if (this.width == deq.width) {
            MemoryUtil.memCopy(deq.pixels, this.pixels, Math.min(this.size, deq.size));
        }
        else {
            final int integer4 = Math.min(this.getWidth(), deq.getWidth());
            for (int integer5 = Math.min(this.getHeight(), deq.getHeight()), integer6 = 0; integer6 < integer5; ++integer6) {
                final int integer7 = integer6 * deq.getWidth() * integer3;
                final int integer8 = integer6 * this.getWidth() * integer3;
                MemoryUtil.memCopy(deq.pixels + integer7, this.pixels + integer8, (long)integer4);
            }
        }
    }
    
    public void fillRect(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
        for (int integer6 = integer2; integer6 < integer2 + integer4; ++integer6) {
            for (int integer7 = integer1; integer7 < integer1 + integer3; ++integer7) {
                this.setPixelRGBA(integer7, integer6, integer5);
            }
        }
    }
    
    public void copyRect(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final boolean boolean7, final boolean boolean8) {
        for (int integer7 = 0; integer7 < integer6; ++integer7) {
            for (int integer8 = 0; integer8 < integer5; ++integer8) {
                final int integer9 = boolean7 ? (integer5 - 1 - integer8) : integer8;
                final int integer10 = boolean8 ? (integer6 - 1 - integer7) : integer7;
                final int integer11 = this.getPixelRGBA(integer1 + integer8, integer2 + integer7);
                this.setPixelRGBA(integer1 + integer3 + integer9, integer2 + integer4 + integer10, integer11);
            }
        }
    }
    
    public void flipY() {
        this.checkAllocated();
        try (final MemoryStack memoryStack2 = MemoryStack.stackPush()) {
            final int integer4 = this.format.components();
            final int integer5 = this.getWidth() * integer4;
            final long long6 = memoryStack2.nmalloc(integer5);
            for (int integer6 = 0; integer6 < this.getHeight() / 2; ++integer6) {
                final int integer7 = integer6 * this.getWidth() * integer4;
                final int integer8 = (this.getHeight() - 1 - integer6) * this.getWidth() * integer4;
                MemoryUtil.memCopy(this.pixels + integer7, long6, (long)integer5);
                MemoryUtil.memCopy(this.pixels + integer8, this.pixels + integer7, (long)integer5);
                MemoryUtil.memCopy(long6, this.pixels + integer8, (long)integer5);
            }
        }
    }
    
    public void resizeSubRectTo(final int integer1, final int integer2, final int integer3, final int integer4, final NativeImage deq) {
        this.checkAllocated();
        if (deq.format() != this.format) {
            throw new UnsupportedOperationException("resizeSubRectTo only works for images of the same format.");
        }
        final int integer5 = this.format.components();
        STBImageResize.nstbir_resize_uint8(this.pixels + (integer1 + integer2 * this.getWidth()) * integer5, integer3, integer4, this.getWidth() * integer5, deq.pixels, deq.getWidth(), deq.getHeight(), 0, integer5);
    }
    
    public void untrack() {
        DebugMemoryUntracker.untrack(this.pixels);
    }
    
    public static NativeImage fromBase64(final String string) throws IOException {
        final byte[] arr2 = Base64.getDecoder().decode(string.replaceAll("\n", "").getBytes(Charsets.UTF_8));
        try (final MemoryStack memoryStack3 = MemoryStack.stackPush()) {
            final ByteBuffer byteBuffer5 = memoryStack3.malloc(arr2.length);
            byteBuffer5.put(arr2);
            byteBuffer5.rewind();
            return read(byteBuffer5);
        }
    }
    
    public static int getA(final int integer) {
        return integer >> 24 & 0xFF;
    }
    
    public static int getR(final int integer) {
        return integer >> 0 & 0xFF;
    }
    
    public static int getG(final int integer) {
        return integer >> 8 & 0xFF;
    }
    
    public static int getB(final int integer) {
        return integer >> 16 & 0xFF;
    }
    
    public static int combine(final int integer1, final int integer2, final int integer3, final int integer4) {
        return (integer1 & 0xFF) << 24 | (integer2 & 0xFF) << 16 | (integer3 & 0xFF) << 8 | (integer4 & 0xFF) << 0;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        OPEN_OPTIONS = (Set)EnumSet.of((Enum)StandardOpenOption.WRITE, (Enum)StandardOpenOption.CREATE, (Enum)StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    static class WriteCallback extends STBIWriteCallback {
        private final WritableByteChannel output;
        @Nullable
        private IOException exception;
        
        private WriteCallback(final WritableByteChannel writableByteChannel) {
            this.output = writableByteChannel;
        }
        
        public void invoke(final long long1, final long long2, final int integer) {
            final ByteBuffer byteBuffer7 = getData(long2, integer);
            try {
                this.output.write(byteBuffer7);
            }
            catch (IOException iOException8) {
                this.exception = iOException8;
            }
        }
        
        public void throwIfException() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
        }
    }
    
    public enum InternalGlFormat {
        RGBA(6408), 
        RGB(6407), 
        LUMINANCE_ALPHA(6410), 
        LUMINANCE(6409), 
        INTENSITY(32841);
        
        private final int glFormat;
        
        private InternalGlFormat(final int integer3) {
            this.glFormat = integer3;
        }
        
        int glFormat() {
            return this.glFormat;
        }
    }
    
    public enum Format {
        RGBA(4, 6408, true, true, true, false, true, 0, 8, 16, 255, 24, true), 
        RGB(3, 6407, true, true, true, false, false, 0, 8, 16, 255, 255, true), 
        LUMINANCE_ALPHA(2, 6410, false, false, false, true, true, 255, 255, 255, 0, 8, true), 
        LUMINANCE(1, 6409, false, false, false, true, false, 0, 0, 0, 0, 255, true);
        
        private final int components;
        private final int glFormat;
        private final boolean hasRed;
        private final boolean hasGreen;
        private final boolean hasBlue;
        private final boolean hasLuminance;
        private final boolean hasAlpha;
        private final int redOffset;
        private final int greenOffset;
        private final int blueOffset;
        private final int luminanceOffset;
        private final int alphaOffset;
        private final boolean supportedByStb;
        
        private Format(final int integer3, final int integer4, final boolean boolean5, final boolean boolean6, final boolean boolean7, final boolean boolean8, final boolean boolean9, final int integer10, final int integer11, final int integer12, final int integer13, final int integer14, final boolean boolean15) {
            this.components = integer3;
            this.glFormat = integer4;
            this.hasRed = boolean5;
            this.hasGreen = boolean6;
            this.hasBlue = boolean7;
            this.hasLuminance = boolean8;
            this.hasAlpha = boolean9;
            this.redOffset = integer10;
            this.greenOffset = integer11;
            this.blueOffset = integer12;
            this.luminanceOffset = integer13;
            this.alphaOffset = integer14;
            this.supportedByStb = boolean15;
        }
        
        public int components() {
            return this.components;
        }
        
        public void setPackPixelStoreState() {
            RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
            GlStateManager._pixelStore(3333, this.components());
        }
        
        public void setUnpackPixelStoreState() {
            RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
            GlStateManager._pixelStore(3317, this.components());
        }
        
        public int glFormat() {
            return this.glFormat;
        }
        
        public boolean hasAlpha() {
            return this.hasAlpha;
        }
        
        public int alphaOffset() {
            return this.alphaOffset;
        }
        
        public boolean hasLuminanceOrAlpha() {
            return this.hasLuminance || this.hasAlpha;
        }
        
        public int luminanceOrAlphaOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
        }
        
        public boolean supportedByStb() {
            return this.supportedByStb;
        }
        
        private static Format getStbFormat(final int integer) {
            switch (integer) {
                case 1: {
                    return Format.LUMINANCE;
                }
                case 2: {
                    return Format.LUMINANCE_ALPHA;
                }
                case 3: {
                    return Format.RGB;
                }
                default: {
                    return Format.RGBA;
                }
            }
        }
    }
}
