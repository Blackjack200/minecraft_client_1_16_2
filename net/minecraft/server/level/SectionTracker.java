package net.minecraft.server.level;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;

public abstract class SectionTracker extends DynamicGraphMinFixedPoint {
    protected SectionTracker(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected boolean isSource(final long long1) {
        return long1 == Long.MAX_VALUE;
    }
    
    @Override
    protected void checkNeighborsAfterUpdate(final long long1, final int integer, final boolean boolean3) {
        for (int integer2 = -1; integer2 <= 1; ++integer2) {
            for (int integer3 = -1; integer3 <= 1; ++integer3) {
                for (int integer4 = -1; integer4 <= 1; ++integer4) {
                    final long long2 = SectionPos.offset(long1, integer2, integer3, integer4);
                    if (long2 != long1) {
                        this.checkNeighbor(long1, long2, integer, boolean3);
                    }
                }
            }
        }
    }
    
    @Override
    protected int getComputedLevel(final long long1, final long long2, final int integer) {
        int integer2 = integer;
        for (int integer3 = -1; integer3 <= 1; ++integer3) {
            for (int integer4 = -1; integer4 <= 1; ++integer4) {
                for (int integer5 = -1; integer5 <= 1; ++integer5) {
                    long long3 = SectionPos.offset(long1, integer3, integer4, integer5);
                    if (long3 == long1) {
                        long3 = Long.MAX_VALUE;
                    }
                    if (long3 != long2) {
                        final int integer6 = this.computeLevelFromNeighbor(long3, long1, this.getLevel(long3));
                        if (integer2 > integer6) {
                            integer2 = integer6;
                        }
                        if (integer2 == 0) {
                            return integer2;
                        }
                    }
                }
            }
        }
        return integer2;
    }
    
    @Override
    protected int computeLevelFromNeighbor(final long long1, final long long2, final int integer) {
        if (long1 == Long.MAX_VALUE) {
            return this.getLevelFromSource(long2);
        }
        return integer + 1;
    }
    
    protected abstract int getLevelFromSource(final long long1);
    
    public void update(final long long1, final int integer, final boolean boolean3) {
        this.checkEdge(Long.MAX_VALUE, long1, integer, boolean3);
    }
}
