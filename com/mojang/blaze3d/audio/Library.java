package com.mojang.blaze3d.audio;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL;
import net.minecraft.util.Mth;
import org.lwjgl.openal.ALC10;
import java.nio.IntBuffer;
import org.lwjgl.openal.ALC;
import org.apache.logging.log4j.Logger;

public class Library {
    private static final Logger LOGGER;
    private long device;
    private long context;
    private static final ChannelPool EMPTY;
    private ChannelPool staticChannels;
    private ChannelPool streamingChannels;
    private final Listener listener;
    
    public Library() {
        this.staticChannels = Library.EMPTY;
        this.streamingChannels = Library.EMPTY;
        this.listener = new Listener();
    }
    
    public void init() {
        this.device = tryOpenDevice();
        final ALCCapabilities aLCCapabilities2 = ALC.createCapabilities(this.device);
        if (OpenAlUtil.checkALCError(this.device, "Get capabilities")) {
            throw new IllegalStateException("Failed to get OpenAL capabilities");
        }
        if (!aLCCapabilities2.OpenALC11) {
            throw new IllegalStateException("OpenAL 1.1 not supported");
        }
        ALC10.alcMakeContextCurrent(this.context = ALC10.alcCreateContext(this.device, (IntBuffer)null));
        final int integer3 = this.getChannelCount();
        final int integer4 = Mth.clamp((int)Mth.sqrt((float)integer3), 2, 8);
        final int integer5 = Mth.clamp(integer3 - integer4, 8, 255);
        this.staticChannels = new CountingChannelPool(integer5);
        this.streamingChannels = new CountingChannelPool(integer4);
        final ALCapabilities aLCapabilities6 = AL.createCapabilities(aLCCapabilities2);
        OpenAlUtil.checkALError("Initialization");
        if (!aLCapabilities6.AL_EXT_source_distance_model) {
            throw new IllegalStateException("AL_EXT_source_distance_model is not supported");
        }
        AL10.alEnable(512);
        if (!aLCapabilities6.AL_EXT_LINEAR_DISTANCE) {
            throw new IllegalStateException("AL_EXT_LINEAR_DISTANCE is not supported");
        }
        OpenAlUtil.checkALError("Enable per-source distance models");
        Library.LOGGER.info("OpenAL initialized.");
    }
    
    private int getChannelCount() {
        try (final MemoryStack memoryStack2 = MemoryStack.stackPush()) {
            final int integer4 = ALC10.alcGetInteger(this.device, 4098);
            if (OpenAlUtil.checkALCError(this.device, "Get attributes size")) {
                throw new IllegalStateException("Failed to get OpenAL attributes");
            }
            final IntBuffer intBuffer5 = memoryStack2.mallocInt(integer4);
            ALC10.alcGetIntegerv(this.device, 4099, intBuffer5);
            if (OpenAlUtil.checkALCError(this.device, "Get attributes")) {
                throw new IllegalStateException("Failed to get OpenAL attributes");
            }
            int integer5 = 0;
            while (integer5 < integer4) {
                final int integer6 = intBuffer5.get(integer5++);
                if (integer6 == 0) {
                    break;
                }
                final int integer7 = intBuffer5.get(integer5++);
                if (integer6 == 4112) {
                    return integer7;
                }
            }
        }
        return 30;
    }
    
    private static long tryOpenDevice() {
        for (int integer1 = 0; integer1 < 3; ++integer1) {
            final long long2 = ALC10.alcOpenDevice((ByteBuffer)null);
            if (long2 != 0L && !OpenAlUtil.checkALCError(long2, "Open device")) {
                return long2;
            }
        }
        throw new IllegalStateException("Failed to open OpenAL device");
    }
    
    public void cleanup() {
        this.staticChannels.cleanup();
        this.streamingChannels.cleanup();
        ALC10.alcDestroyContext(this.context);
        if (this.device != 0L) {
            ALC10.alcCloseDevice(this.device);
        }
    }
    
    public Listener getListener() {
        return this.listener;
    }
    
    @Nullable
    public Channel acquireChannel(final Pool c) {
        return ((c == Pool.STREAMING) ? this.streamingChannels : this.staticChannels).acquire();
    }
    
    public void releaseChannel(final Channel ddr) {
        if (!this.staticChannels.release(ddr) && !this.streamingChannels.release(ddr)) {
            throw new IllegalStateException("Tried to release unknown channel");
        }
    }
    
    public String getDebugString() {
        return String.format("Sounds: %d/%d + %d/%d", new Object[] { this.staticChannels.getUsedCount(), this.staticChannels.getMaxCount(), this.streamingChannels.getUsedCount(), this.streamingChannels.getMaxCount() });
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY = new ChannelPool() {
            @Nullable
            public Channel acquire() {
                return null;
            }
            
            public boolean release(final Channel ddr) {
                return false;
            }
            
            public void cleanup() {
            }
            
            public int getMaxCount() {
                return 0;
            }
            
            public int getUsedCount() {
                return 0;
            }
        };
    }
    
    public enum Pool {
        STATIC, 
        STREAMING;
    }
    
    static class CountingChannelPool implements ChannelPool {
        private final int limit;
        private final Set<Channel> activeChannels;
        
        public CountingChannelPool(final int integer) {
            this.activeChannels = (Set<Channel>)Sets.newIdentityHashSet();
            this.limit = integer;
        }
        
        @Nullable
        public Channel acquire() {
            if (this.activeChannels.size() >= this.limit) {
                Library.LOGGER.warn("Maximum sound pool size {} reached", this.limit);
                return null;
            }
            final Channel ddr2 = Channel.create();
            if (ddr2 != null) {
                this.activeChannels.add(ddr2);
            }
            return ddr2;
        }
        
        public boolean release(final Channel ddr) {
            if (!this.activeChannels.remove(ddr)) {
                return false;
            }
            ddr.destroy();
            return true;
        }
        
        public void cleanup() {
            this.activeChannels.forEach(Channel::destroy);
            this.activeChannels.clear();
        }
        
        public int getMaxCount() {
            return this.limit;
        }
        
        public int getUsedCount() {
            return this.activeChannels.size();
        }
    }
    
    interface ChannelPool {
        @Nullable
        Channel acquire();
        
        boolean release(final Channel ddr);
        
        void cleanup();
        
        int getMaxCount();
        
        int getUsedCount();
    }
}
