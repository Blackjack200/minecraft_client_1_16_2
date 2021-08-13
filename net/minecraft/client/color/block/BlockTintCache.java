package net.minecraft.client.color.block;

import java.util.Arrays;
import net.minecraft.world.level.ChunkPos;
import java.util.function.IntSupplier;
import net.minecraft.core.BlockPos;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;

public class BlockTintCache {
    private final ThreadLocal<LatestCacheInfo> latestChunkOnThread;
    private final Long2ObjectLinkedOpenHashMap<int[]> cache;
    private final ReentrantReadWriteLock lock;
    
    public BlockTintCache() {
        this.latestChunkOnThread = (ThreadLocal<LatestCacheInfo>)ThreadLocal.withInitial(() -> new LatestCacheInfo());
        this.cache = (Long2ObjectLinkedOpenHashMap<int[]>)new Long2ObjectLinkedOpenHashMap(256, 0.25f);
        this.lock = new ReentrantReadWriteLock();
    }
    
    public int getColor(final BlockPos fx, final IntSupplier intSupplier) {
        final int integer4 = fx.getX() >> 4;
        final int integer5 = fx.getZ() >> 4;
        final LatestCacheInfo a6 = (LatestCacheInfo)this.latestChunkOnThread.get();
        if (a6.x != integer4 || a6.z != integer5) {
            a6.x = integer4;
            a6.z = integer5;
            a6.cache = this.findOrCreateChunkCache(integer4, integer5);
        }
        final int integer6 = fx.getX() & 0xF;
        final int integer7 = fx.getZ() & 0xF;
        final int integer8 = integer7 << 4 | integer6;
        final int integer9 = a6.cache[integer8];
        if (integer9 != -1) {
            return integer9;
        }
        final int integer10 = intSupplier.getAsInt();
        return a6.cache[integer8] = integer10;
    }
    
    public void invalidateForChunk(final int integer1, final int integer2) {
        try {
            this.lock.writeLock().lock();
            for (int integer3 = -1; integer3 <= 1; ++integer3) {
                for (int integer4 = -1; integer4 <= 1; ++integer4) {
                    final long long6 = ChunkPos.asLong(integer1 + integer3, integer2 + integer4);
                    this.cache.remove(long6);
                }
            }
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public void invalidateAll() {
        try {
            this.lock.writeLock().lock();
            this.cache.clear();
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    private int[] findOrCreateChunkCache(final int integer1, final int integer2) {
        final long long4 = ChunkPos.asLong(integer1, integer2);
        this.lock.readLock().lock();
        int[] arr6;
        try {
            arr6 = (int[])this.cache.get(long4);
        }
        finally {
            this.lock.readLock().unlock();
        }
        if (arr6 != null) {
            return arr6;
        }
        final int[] arr7 = new int[256];
        Arrays.fill(arr7, -1);
        try {
            this.lock.writeLock().lock();
            if (this.cache.size() >= 256) {
                this.cache.removeFirst();
            }
            this.cache.put(long4, arr7);
        }
        finally {
            this.lock.writeLock().unlock();
        }
        return arr7;
    }
    
    static class LatestCacheInfo {
        public int x;
        public int z;
        public int[] cache;
        
        private LatestCacheInfo() {
            this.x = Integer.MIN_VALUE;
            this.z = Integer.MIN_VALUE;
        }
    }
}
