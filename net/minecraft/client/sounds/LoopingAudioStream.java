package net.minecraft.client.sounds;

import java.io.FilterInputStream;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

public class LoopingAudioStream implements AudioStream {
    private final AudioStreamProvider provider;
    private AudioStream stream;
    private final BufferedInputStream bufferedInputStream;
    
    public LoopingAudioStream(final AudioStreamProvider a, final InputStream inputStream) throws IOException {
        this.provider = a;
        (this.bufferedInputStream = new BufferedInputStream(inputStream)).mark(Integer.MAX_VALUE);
        this.stream = a.create((InputStream)new NoCloseBuffer((InputStream)this.bufferedInputStream));
    }
    
    public AudioFormat getFormat() {
        return this.stream.getFormat();
    }
    
    public ByteBuffer read(final int integer) throws IOException {
        ByteBuffer byteBuffer3 = this.stream.read(integer);
        if (!byteBuffer3.hasRemaining()) {
            this.stream.close();
            this.bufferedInputStream.reset();
            this.stream = this.provider.create((InputStream)new NoCloseBuffer((InputStream)this.bufferedInputStream));
            byteBuffer3 = this.stream.read(integer);
        }
        return byteBuffer3;
    }
    
    public void close() throws IOException {
        this.stream.close();
        this.bufferedInputStream.close();
    }
    
    static class NoCloseBuffer extends FilterInputStream {
        private NoCloseBuffer(final InputStream inputStream) {
            super(inputStream);
        }
        
        public void close() {
        }
    }
    
    @FunctionalInterface
    public interface AudioStreamProvider {
        AudioStream create(final InputStream inputStream) throws IOException;
    }
}
