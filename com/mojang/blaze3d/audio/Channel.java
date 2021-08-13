package com.mojang.blaze3d.audio;

import org.apache.logging.log4j.LogManager;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import net.minecraft.world.phys.Vec3;
import java.io.IOException;
import org.lwjgl.openal.AL10;
import javax.annotation.Nullable;
import net.minecraft.client.sounds.AudioStream;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.Logger;

public class Channel {
    private static final Logger LOGGER;
    private final int source;
    private final AtomicBoolean initialized;
    private int streamingBufferSize;
    @Nullable
    private AudioStream stream;
    
    @Nullable
    static Channel create() {
        final int[] arr1 = { 0 };
        AL10.alGenSources(arr1);
        if (OpenAlUtil.checkALError("Allocate new source")) {
            return null;
        }
        return new Channel(arr1[0]);
    }
    
    private Channel(final int integer) {
        this.initialized = new AtomicBoolean(true);
        this.streamingBufferSize = 16384;
        this.source = integer;
    }
    
    public void destroy() {
        if (this.initialized.compareAndSet(true, false)) {
            AL10.alSourceStop(this.source);
            OpenAlUtil.checkALError("Stop");
            if (this.stream != null) {
                try {
                    this.stream.close();
                }
                catch (IOException iOException2) {
                    Channel.LOGGER.error("Failed to close audio stream", (Throwable)iOException2);
                }
                this.removeProcessedBuffers();
                this.stream = null;
            }
            AL10.alDeleteSources(new int[] { this.source });
            OpenAlUtil.checkALError("Cleanup");
        }
    }
    
    public void play() {
        AL10.alSourcePlay(this.source);
    }
    
    private int getState() {
        if (!this.initialized.get()) {
            return 4116;
        }
        return AL10.alGetSourcei(this.source, 4112);
    }
    
    public void pause() {
        if (this.getState() == 4114) {
            AL10.alSourcePause(this.source);
        }
    }
    
    public void unpause() {
        if (this.getState() == 4115) {
            AL10.alSourcePlay(this.source);
        }
    }
    
    public void stop() {
        if (this.initialized.get()) {
            AL10.alSourceStop(this.source);
            OpenAlUtil.checkALError("Stop");
        }
    }
    
    public boolean stopped() {
        return this.getState() == 4116;
    }
    
    public void setSelfPosition(final Vec3 dck) {
        AL10.alSourcefv(this.source, 4100, new float[] { (float)dck.x, (float)dck.y, (float)dck.z });
    }
    
    public void setPitch(final float float1) {
        AL10.alSourcef(this.source, 4099, float1);
    }
    
    public void setLooping(final boolean boolean1) {
        AL10.alSourcei(this.source, 4103, (int)(boolean1 ? 1 : 0));
    }
    
    public void setVolume(final float float1) {
        AL10.alSourcef(this.source, 4106, float1);
    }
    
    public void disableAttenuation() {
        AL10.alSourcei(this.source, 53248, 0);
    }
    
    public void linearAttenuation(final float float1) {
        AL10.alSourcei(this.source, 53248, 53251);
        AL10.alSourcef(this.source, 4131, float1);
        AL10.alSourcef(this.source, 4129, 1.0f);
        AL10.alSourcef(this.source, 4128, 0.0f);
    }
    
    public void setRelative(final boolean boolean1) {
        AL10.alSourcei(this.source, 514, (int)(boolean1 ? 1 : 0));
    }
    
    public void attachStaticBuffer(final SoundBuffer ddw) {
        ddw.getAlBuffer().ifPresent(integer -> AL10.alSourcei(this.source, 4105, integer));
    }
    
    public void attachBufferStream(final AudioStream ene) {
        this.stream = ene;
        final AudioFormat audioFormat3 = ene.getFormat();
        this.streamingBufferSize = calculateBufferSize(audioFormat3, 1);
        this.pumpBuffers(4);
    }
    
    private static int calculateBufferSize(final AudioFormat audioFormat, final int integer) {
        return (int)(integer * audioFormat.getSampleSizeInBits() / 8.0f * audioFormat.getChannels() * audioFormat.getSampleRate());
    }
    
    private void pumpBuffers(final int integer) {
        if (this.stream != null) {
            try {
                for (int integer2 = 0; integer2 < integer; ++integer2) {
                    final ByteBuffer byteBuffer4 = this.stream.read(this.streamingBufferSize);
                    if (byteBuffer4 != null) {
                        new SoundBuffer(byteBuffer4, this.stream.getFormat()).releaseAlBuffer().ifPresent(integer -> AL10.alSourceQueueBuffers(this.source, new int[] { integer }));
                    }
                }
            }
            catch (IOException iOException3) {
                Channel.LOGGER.error("Failed to read from audio stream", (Throwable)iOException3);
            }
        }
    }
    
    public void updateStream() {
        if (this.stream != null) {
            final int integer2 = this.removeProcessedBuffers();
            this.pumpBuffers(integer2);
        }
    }
    
    private int removeProcessedBuffers() {
        final int integer2 = AL10.alGetSourcei(this.source, 4118);
        if (integer2 > 0) {
            final int[] arr3 = new int[integer2];
            AL10.alSourceUnqueueBuffers(this.source, arr3);
            OpenAlUtil.checkALError("Unqueue buffers");
            AL10.alDeleteBuffers(arr3);
            OpenAlUtil.checkALError("Remove processed buffers");
        }
        return integer2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
