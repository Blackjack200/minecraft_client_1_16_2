package com.mojang.blaze3d.platform;

import java.io.EOFException;
import java.nio.channels.ReadableByteChannel;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryUtil;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.io.FileInputStream;
import java.nio.IntBuffer;
import java.io.IOException;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBIEOFCallbackI;
import org.lwjgl.stb.STBISkipCallbackI;
import org.lwjgl.stb.STBIReadCallbackI;
import org.lwjgl.stb.STBIIOCallbacks;
import org.lwjgl.stb.STBIEOFCallback;
import org.lwjgl.stb.STBISkipCallback;
import org.lwjgl.stb.STBIReadCallback;
import org.lwjgl.system.MemoryStack;
import java.io.InputStream;

public class PngInfo {
    public final int width;
    public final int height;
    
    public PngInfo(final String string, final InputStream inputStream) throws IOException {
        try (final MemoryStack memoryStack4 = MemoryStack.stackPush();
             final StbReader a6 = createCallbacks(inputStream);
             final STBIReadCallback sTBIReadCallback8 = STBIReadCallback.create(a6::read);
             final STBISkipCallback sTBISkipCallback10 = STBISkipCallback.create(a6::skip);
             final STBIEOFCallback sTBIEOFCallback12 = STBIEOFCallback.create(a6::eof)) {
            final STBIIOCallbacks sTBIIOCallbacks14 = STBIIOCallbacks.mallocStack(memoryStack4);
            sTBIIOCallbacks14.read((STBIReadCallbackI)sTBIReadCallback8);
            sTBIIOCallbacks14.skip((STBISkipCallbackI)sTBISkipCallback10);
            sTBIIOCallbacks14.eof((STBIEOFCallbackI)sTBIEOFCallback12);
            final IntBuffer intBuffer15 = memoryStack4.mallocInt(1);
            final IntBuffer intBuffer16 = memoryStack4.mallocInt(1);
            final IntBuffer intBuffer17 = memoryStack4.mallocInt(1);
            if (!STBImage.stbi_info_from_callbacks(sTBIIOCallbacks14, 0L, intBuffer15, intBuffer16, intBuffer17)) {
                throw new IOException("Could not read info from the PNG file " + string + " " + STBImage.stbi_failure_reason());
            }
            this.width = intBuffer15.get(0);
            this.height = intBuffer16.get(0);
        }
    }
    
    private static StbReader createCallbacks(final InputStream inputStream) {
        if (inputStream instanceof FileInputStream) {
            return new StbReaderSeekableByteChannel((SeekableByteChannel)((FileInputStream)inputStream).getChannel());
        }
        return new StbReaderBufferedChannel(Channels.newChannel(inputStream));
    }
    
    abstract static class StbReader implements AutoCloseable {
        protected boolean closed;
        
        private StbReader() {
        }
        
        int read(final long long1, final long long2, final int integer) {
            try {
                return this.read(long2, integer);
            }
            catch (IOException iOException7) {
                this.closed = true;
                return 0;
            }
        }
        
        void skip(final long long1, final int integer) {
            try {
                this.skip(integer);
            }
            catch (IOException iOException5) {
                this.closed = true;
            }
        }
        
        int eof(final long long1) {
            return this.closed ? 1 : 0;
        }
        
        protected abstract int read(final long long1, final int integer) throws IOException;
        
        protected abstract void skip(final int integer) throws IOException;
        
        public abstract void close() throws IOException;
    }
    
    static class StbReaderSeekableByteChannel extends StbReader {
        private final SeekableByteChannel channel;
        
        private StbReaderSeekableByteChannel(final SeekableByteChannel seekableByteChannel) {
            this.channel = seekableByteChannel;
        }
        
        public int read(final long long1, final int integer) throws IOException {
            final ByteBuffer byteBuffer5 = MemoryUtil.memByteBuffer(long1, integer);
            return this.channel.read(byteBuffer5);
        }
        
        public void skip(final int integer) throws IOException {
            this.channel.position(this.channel.position() + integer);
        }
        
        public int eof(final long long1) {
            return (super.eof(long1) != 0 && this.channel.isOpen()) ? 1 : 0;
        }
        
        @Override
        public void close() throws IOException {
            this.channel.close();
        }
    }
    
    static class StbReaderBufferedChannel extends StbReader {
        private final ReadableByteChannel channel;
        private long readBufferAddress;
        private int bufferSize;
        private int read;
        private int consumed;
        
        private StbReaderBufferedChannel(final ReadableByteChannel readableByteChannel) {
            this.readBufferAddress = MemoryUtil.nmemAlloc(128L);
            this.bufferSize = 128;
            this.channel = readableByteChannel;
        }
        
        private void fillReadBuffer(final int integer) throws IOException {
            ByteBuffer byteBuffer3 = MemoryUtil.memByteBuffer(this.readBufferAddress, this.bufferSize);
            if (integer + this.consumed > this.bufferSize) {
                this.bufferSize = integer + this.consumed;
                byteBuffer3 = MemoryUtil.memRealloc(byteBuffer3, this.bufferSize);
                this.readBufferAddress = MemoryUtil.memAddress(byteBuffer3);
            }
            byteBuffer3.position(this.read);
            while (integer + this.consumed > this.read) {
                try {
                    final int integer2 = this.channel.read(byteBuffer3);
                    if (integer2 == -1) {
                        break;
                    }
                    continue;
                }
                finally {
                    this.read = byteBuffer3.position();
                }
            }
        }
        
        public int read(final long long1, int integer) throws IOException {
            this.fillReadBuffer(integer);
            if (integer + this.consumed > this.read) {
                integer = this.read - this.consumed;
            }
            MemoryUtil.memCopy(this.readBufferAddress + this.consumed, long1, (long)integer);
            this.consumed += integer;
            return integer;
        }
        
        public void skip(final int integer) throws IOException {
            if (integer > 0) {
                this.fillReadBuffer(integer);
                if (integer + this.consumed > this.read) {
                    throw new EOFException("Can't skip past the EOF.");
                }
            }
            if (this.consumed + integer < 0) {
                throw new IOException(new StringBuilder().append("Can't seek before the beginning: ").append(this.consumed + integer).toString());
            }
            this.consumed += integer;
        }
        
        @Override
        public void close() throws IOException {
            MemoryUtil.nmemFree(this.readBufferAddress);
            this.channel.close();
        }
    }
}
