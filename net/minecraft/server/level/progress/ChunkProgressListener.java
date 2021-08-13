package net.minecraft.server.level.progress;

import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.ChunkPos;

public interface ChunkProgressListener {
    void updateSpawnPos(final ChunkPos bra);
    
    void onStatusChange(final ChunkPos bra, @Nullable final ChunkStatus cfx);
    
    void stop();
}
