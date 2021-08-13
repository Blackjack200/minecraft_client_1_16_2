package net.minecraft.server.level;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;

public abstract class ChunkTracker extends DynamicGraphMinFixedPoint {
    protected ChunkTracker(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected boolean isSource(final long long1) {
        return long1 == ChunkPos.INVALID_CHUNK_POS;
    }
    
    @Override
    protected void checkNeighborsAfterUpdate(final long long1, final int integer, final boolean boolean3) {
        final ChunkPos bra6 = new ChunkPos(long1);
        final int integer2 = bra6.x;
        final int integer3 = bra6.z;
        for (int integer4 = -1; integer4 <= 1; ++integer4) {
            for (int integer5 = -1; integer5 <= 1; ++integer5) {
                final long long2 = ChunkPos.asLong(integer2 + integer4, integer3 + integer5);
                if (long2 != long1) {
                    this.checkNeighbor(long1, long2, integer, boolean3);
                }
            }
        }
    }
    
    @Override
    protected int getComputedLevel(final long long1, final long long2, final int integer) {
        int integer2 = integer;
        final ChunkPos bra8 = new ChunkPos(long1);
        final int integer3 = bra8.x;
        final int integer4 = bra8.z;
        for (int integer5 = -1; integer5 <= 1; ++integer5) {
            for (int integer6 = -1; integer6 <= 1; ++integer6) {
                long long3 = ChunkPos.asLong(integer3 + integer5, integer4 + integer6);
                if (long3 == long1) {
                    long3 = ChunkPos.INVALID_CHUNK_POS;
                }
                if (long3 != long2) {
                    final int integer7 = this.computeLevelFromNeighbor(long3, long1, this.getLevel(long3));
                    if (integer2 > integer7) {
                        integer2 = integer7;
                    }
                    if (integer2 == 0) {
                        return integer2;
                    }
                }
            }
        }
        return integer2;
    }
    
    @Override
    protected int computeLevelFromNeighbor(final long long1, final long long2, final int integer) {
        if (long1 == ChunkPos.INVALID_CHUNK_POS) {
            return this.getLevelFromSource(long2);
        }
        return integer + 1;
    }
    
    protected abstract int getLevelFromSource(final long long1);
    
    public void update(final long long1, final int integer, final boolean boolean3) {
        this.checkEdge(ChunkPos.INVALID_CHUNK_POS, long1, integer, boolean3);
    }
}
