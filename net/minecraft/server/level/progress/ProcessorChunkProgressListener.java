package net.minecraft.server.level.progress;

import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.ChunkPos;
import java.util.concurrent.Executor;
import net.minecraft.util.thread.ProcessorMailbox;

public class ProcessorChunkProgressListener implements ChunkProgressListener {
    private final ChunkProgressListener delegate;
    private final ProcessorMailbox<Runnable> mailbox;
    
    public ProcessorChunkProgressListener(final ChunkProgressListener aap, final Executor executor) {
        this.delegate = aap;
        this.mailbox = ProcessorMailbox.create(executor, "progressListener");
    }
    
    public void updateSpawnPos(final ChunkPos bra) {
        this.mailbox.tell(() -> this.delegate.updateSpawnPos(bra));
    }
    
    public void onStatusChange(final ChunkPos bra, @Nullable final ChunkStatus cfx) {
        this.mailbox.tell(() -> this.delegate.onStatusChange(bra, cfx));
    }
    
    public void stop() {
        this.mailbox.tell(this.delegate::stop);
    }
}
