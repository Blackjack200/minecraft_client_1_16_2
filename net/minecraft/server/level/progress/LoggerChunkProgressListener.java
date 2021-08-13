package net.minecraft.server.level.progress;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Logger;

public class LoggerChunkProgressListener implements ChunkProgressListener {
    private static final Logger LOGGER;
    private final int maxCount;
    private int count;
    private long startTime;
    private long nextTickTime;
    
    public LoggerChunkProgressListener(final int integer) {
        this.nextTickTime = Long.MAX_VALUE;
        final int integer2 = integer * 2 + 1;
        this.maxCount = integer2 * integer2;
    }
    
    public void updateSpawnPos(final ChunkPos bra) {
        this.nextTickTime = Util.getMillis();
        this.startTime = this.nextTickTime;
    }
    
    public void onStatusChange(final ChunkPos bra, @Nullable final ChunkStatus cfx) {
        if (cfx == ChunkStatus.FULL) {
            ++this.count;
        }
        final int integer4 = this.getProgress();
        if (Util.getMillis() > this.nextTickTime) {
            this.nextTickTime += 500L;
            LoggerChunkProgressListener.LOGGER.info(new TranslatableComponent("menu.preparingSpawn", new Object[] { Mth.clamp(integer4, 0, 100) }).getString());
        }
    }
    
    public void stop() {
        LoggerChunkProgressListener.LOGGER.info("Time elapsed: {} ms", (Util.getMillis() - this.startTime));
        this.nextTickTime = Long.MAX_VALUE;
    }
    
    public int getProgress() {
        return Mth.floor(this.count * 100.0f / this.maxCount);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
