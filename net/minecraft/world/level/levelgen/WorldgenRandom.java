package net.minecraft.world.level.levelgen;

import java.util.Random;

public class WorldgenRandom extends Random {
    private int count;
    
    public WorldgenRandom() {
    }
    
    public WorldgenRandom(final long long1) {
        super(long1);
    }
    
    public void consumeCount(final int integer) {
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            this.next(1);
        }
    }
    
    protected int next(final int integer) {
        ++this.count;
        return super.next(integer);
    }
    
    public long setBaseChunkSeed(final int integer1, final int integer2) {
        final long long4 = integer1 * 341873128712L + integer2 * 132897987541L;
        this.setSeed(long4);
        return long4;
    }
    
    public long setDecorationSeed(final long long1, final int integer2, final int integer3) {
        this.setSeed(long1);
        final long long2 = this.nextLong() | 0x1L;
        final long long3 = this.nextLong() | 0x1L;
        final long long4 = integer2 * long2 + integer3 * long3 ^ long1;
        this.setSeed(long4);
        return long4;
    }
    
    public long setFeatureSeed(final long long1, final int integer2, final int integer3) {
        final long long2 = long1 + integer2 + 10000 * integer3;
        this.setSeed(long2);
        return long2;
    }
    
    public long setLargeFeatureSeed(final long long1, final int integer2, final int integer3) {
        this.setSeed(long1);
        final long long2 = this.nextLong();
        final long long3 = this.nextLong();
        final long long4 = integer2 * long2 ^ integer3 * long3 ^ long1;
        this.setSeed(long4);
        return long4;
    }
    
    public long setLargeFeatureWithSalt(final long long1, final int integer2, final int integer3, final int integer4) {
        final long long2 = integer2 * 341873128712L + integer3 * 132897987541L + long1 + integer4;
        this.setSeed(long2);
        return long2;
    }
    
    public static Random seedSlimeChunk(final int integer1, final int integer2, final long long3, final long long4) {
        return new Random(long3 + integer1 * integer1 * 4987142 + integer1 * 5947611 + integer2 * integer2 * 4392871L + integer2 * 389711 ^ long4);
    }
}
