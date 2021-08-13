package com.mojang.blaze3d.audio;

import net.minecraft.util.Mth;
import org.lwjgl.BufferUtils;
import com.google.common.collect.Lists;
import java.util.List;
import java.nio.FloatBuffer;
import org.lwjgl.PointerBuffer;
import java.nio.Buffer;
import java.nio.IntBuffer;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.stb.STBVorbisAlloc;
import org.lwjgl.stb.STBVorbis;
import java.io.IOException;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import java.nio.ByteBuffer;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import net.minecraft.client.sounds.AudioStream;

public class OggAudioStream implements AudioStream {
    private long handle;
    private final AudioFormat audioFormat;
    private final InputStream input;
    private ByteBuffer buffer;
    
    public OggAudioStream(final InputStream inputStream) throws IOException {
        this.buffer = MemoryUtil.memAlloc(8192);
        this.input = inputStream;
        this.buffer.limit(0);
        try (final MemoryStack memoryStack3 = MemoryStack.stackPush()) {
            final IntBuffer intBuffer5 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer6 = memoryStack3.mallocInt(1);
            while (this.handle == 0L) {
                if (!this.refillFromStream()) {
                    throw new IOException("Failed to find Ogg header");
                }
                final int integer7 = this.buffer.position();
                this.buffer.position(0);
                this.handle = STBVorbis.stb_vorbis_open_pushdata(this.buffer, intBuffer5, intBuffer6, (STBVorbisAlloc)null);
                this.buffer.position(integer7);
                final int integer8 = intBuffer6.get(0);
                if (integer8 == 1) {
                    this.forwardBuffer();
                }
                else {
                    if (integer8 != 0) {
                        throw new IOException(new StringBuilder().append("Failed to read Ogg file ").append(integer8).toString());
                    }
                    continue;
                }
            }
            this.buffer.position(this.buffer.position() + intBuffer5.get(0));
            final STBVorbisInfo sTBVorbisInfo7 = STBVorbisInfo.mallocStack(memoryStack3);
            STBVorbis.stb_vorbis_get_info(this.handle, sTBVorbisInfo7);
            this.audioFormat = new AudioFormat((float)sTBVorbisInfo7.sample_rate(), 16, sTBVorbisInfo7.channels(), true, false);
        }
    }
    
    private boolean refillFromStream() throws IOException {
        final int integer2 = this.buffer.limit();
        final int integer3 = this.buffer.capacity() - integer2;
        if (integer3 == 0) {
            return true;
        }
        final byte[] arr4 = new byte[integer3];
        final int integer4 = this.input.read(arr4);
        if (integer4 == -1) {
            return false;
        }
        final int integer5 = this.buffer.position();
        this.buffer.limit(integer2 + integer4);
        this.buffer.position(integer2);
        this.buffer.put(arr4, 0, integer4);
        this.buffer.position(integer5);
        return true;
    }
    
    private void forwardBuffer() {
        final boolean boolean2 = this.buffer.position() == 0;
        final boolean boolean3 = this.buffer.position() == this.buffer.limit();
        if (boolean3 && !boolean2) {
            this.buffer.position(0);
            this.buffer.limit(0);
        }
        else {
            final ByteBuffer byteBuffer4 = MemoryUtil.memAlloc(boolean2 ? (2 * this.buffer.capacity()) : this.buffer.capacity());
            byteBuffer4.put(this.buffer);
            MemoryUtil.memFree((Buffer)this.buffer);
            byteBuffer4.flip();
            this.buffer = byteBuffer4;
        }
    }
    
    private boolean readFrame(final OutputConcat a) throws IOException {
        if (this.handle == 0L) {
            return false;
        }
        try (final MemoryStack memoryStack3 = MemoryStack.stackPush()) {
            final PointerBuffer pointerBuffer5 = memoryStack3.mallocPointer(1);
            final IntBuffer intBuffer6 = memoryStack3.mallocInt(1);
            final IntBuffer intBuffer7 = memoryStack3.mallocInt(1);
            while (true) {
                final int integer8 = STBVorbis.stb_vorbis_decode_frame_pushdata(this.handle, this.buffer, intBuffer6, pointerBuffer5, intBuffer7);
                this.buffer.position(this.buffer.position() + integer8);
                final int integer9 = STBVorbis.stb_vorbis_get_error(this.handle);
                if (integer9 == 1) {
                    this.forwardBuffer();
                    if (!this.refillFromStream()) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (integer9 != 0) {
                        throw new IOException(new StringBuilder().append("Failed to read Ogg file ").append(integer9).toString());
                    }
                    final int integer10 = intBuffer7.get(0);
                    if (integer10 == 0) {
                        continue;
                    }
                    final int integer11 = intBuffer6.get(0);
                    final PointerBuffer pointerBuffer6 = pointerBuffer5.getPointerBuffer(integer11);
                    if (integer11 == 1) {
                        this.convertMono(pointerBuffer6.getFloatBuffer(0, integer10), a);
                        return true;
                    }
                    if (integer11 == 2) {
                        this.convertStereo(pointerBuffer6.getFloatBuffer(0, integer10), pointerBuffer6.getFloatBuffer(1, integer10), a);
                        return true;
                    }
                    throw new IllegalStateException(new StringBuilder().append("Invalid number of channels: ").append(integer11).toString());
                }
            }
        }
    }
    
    private void convertMono(final FloatBuffer floatBuffer, final OutputConcat a) {
        while (floatBuffer.hasRemaining()) {
            a.put(floatBuffer.get());
        }
    }
    
    private void convertStereo(final FloatBuffer floatBuffer1, final FloatBuffer floatBuffer2, final OutputConcat a) {
        while (floatBuffer1.hasRemaining() && floatBuffer2.hasRemaining()) {
            a.put(floatBuffer1.get());
            a.put(floatBuffer2.get());
        }
    }
    
    public void close() throws IOException {
        if (this.handle != 0L) {
            STBVorbis.stb_vorbis_close(this.handle);
            this.handle = 0L;
        }
        MemoryUtil.memFree((Buffer)this.buffer);
        this.input.close();
    }
    
    public AudioFormat getFormat() {
        return this.audioFormat;
    }
    
    public ByteBuffer read(final int integer) throws IOException {
        final OutputConcat a3 = new OutputConcat(integer + 8192);
        while (this.readFrame(a3) && a3.byteCount < integer) {}
        return a3.get();
    }
    
    public ByteBuffer readAll() throws IOException {
        final OutputConcat a2 = new OutputConcat(16384);
        while (this.readFrame(a2)) {}
        return a2.get();
    }
    
    static class OutputConcat {
        private final List<ByteBuffer> buffers;
        private final int bufferSize;
        private int byteCount;
        private ByteBuffer currentBuffer;
        
        public OutputConcat(final int integer) {
            this.buffers = (List<ByteBuffer>)Lists.newArrayList();
            this.bufferSize = (integer + 1 & 0xFFFFFFFE);
            this.createNewBuffer();
        }
        
        private void createNewBuffer() {
            this.currentBuffer = BufferUtils.createByteBuffer(this.bufferSize);
        }
        
        public void put(final float float1) {
            if (this.currentBuffer.remaining() == 0) {
                this.currentBuffer.flip();
                this.buffers.add(this.currentBuffer);
                this.createNewBuffer();
            }
            final int integer3 = Mth.clamp((int)(float1 * 32767.5f - 0.5f), -32768, 32767);
            this.currentBuffer.putShort((short)integer3);
            this.byteCount += 2;
        }
        
        public ByteBuffer get() {
            this.currentBuffer.flip();
            if (this.buffers.isEmpty()) {
                return this.currentBuffer;
            }
            final ByteBuffer byteBuffer2 = BufferUtils.createByteBuffer(this.byteCount);
            this.buffers.forEach(byteBuffer2::put);
            byteBuffer2.put(this.currentBuffer);
            byteBuffer2.flip();
            return byteBuffer2;
        }
    }
}
