package net.minecraft.client.sounds;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Iterator;
import com.mojang.blaze3d.audio.Channel;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import com.google.common.collect.Sets;
import java.util.concurrent.Executor;
import com.mojang.blaze3d.audio.Library;
import java.util.Set;

public class ChannelAccess {
    private final Set<ChannelHandle> channels;
    private final Library library;
    private final Executor executor;
    
    public ChannelAccess(final Library dds, final Executor executor) {
        this.channels = (Set<ChannelHandle>)Sets.newIdentityHashSet();
        this.library = dds;
        this.executor = executor;
    }
    
    public CompletableFuture<ChannelHandle> createHandle(final Library.Pool c) {
        final CompletableFuture<ChannelHandle> completableFuture3 = (CompletableFuture<ChannelHandle>)new CompletableFuture();
        this.executor.execute(() -> {
            final Channel ddr4 = this.library.acquireChannel(c);
            if (ddr4 != null) {
                final ChannelHandle a5 = new ChannelHandle(ddr4);
                this.channels.add(a5);
                completableFuture3.complete(a5);
            }
            else {
                completableFuture3.complete(null);
            }
        });
        return completableFuture3;
    }
    
    public void executeOnChannels(final Consumer<Stream<Channel>> consumer) {
        this.executor.execute(() -> consumer.accept(this.channels.stream().map(a -> a.channel).filter(Objects::nonNull)));
    }
    
    public void scheduleTick() {
        this.executor.execute(() -> {
            final Iterator<ChannelHandle> iterator2 = (Iterator<ChannelHandle>)this.channels.iterator();
            while (iterator2.hasNext()) {
                final ChannelHandle a3 = (ChannelHandle)iterator2.next();
                a3.channel.updateStream();
                if (a3.channel.stopped()) {
                    a3.release();
                    iterator2.remove();
                }
            }
        });
    }
    
    public void clear() {
        this.channels.forEach(ChannelHandle::release);
        this.channels.clear();
    }
    
    public class ChannelHandle {
        @Nullable
        private Channel channel;
        private boolean stopped;
        
        public boolean isStopped() {
            return this.stopped;
        }
        
        public ChannelHandle(final Channel ddr) {
            this.channel = ddr;
        }
        
        public void execute(final Consumer<Channel> consumer) {
            ChannelAccess.this.executor.execute(() -> {
                if (this.channel != null) {
                    consumer.accept(this.channel);
                }
            });
        }
        
        public void release() {
            this.stopped = true;
            ChannelAccess.this.library.releaseChannel(this.channel);
            this.channel = null;
        }
    }
}
