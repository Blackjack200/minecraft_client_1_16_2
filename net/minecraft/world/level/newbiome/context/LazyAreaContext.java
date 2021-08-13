package net.minecraft.world.level.newbiome.context;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.newbiome.layer.traits.PixelTransformer;
import java.util.Random;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.world.level.newbiome.area.LazyArea;

public class LazyAreaContext implements BigContext<LazyArea> {
    private final Long2IntLinkedOpenHashMap cache;
    private final int maxCache;
    private final ImprovedNoise biomeNoise;
    private final long seed;
    private long rval;
    
    public LazyAreaContext(final int integer, final long long2, final long long3) {
        this.seed = mixSeed(long2, long3);
        this.biomeNoise = new ImprovedNoise(new Random(long2));
        (this.cache = new Long2IntLinkedOpenHashMap(16, 0.25f)).defaultReturnValue(Integer.MIN_VALUE);
        this.maxCache = integer;
    }
    
    public LazyArea createResult(final PixelTransformer cws) {
        return new LazyArea(this.cache, this.maxCache, cws);
    }
    
    public LazyArea createResult(final PixelTransformer cws, final LazyArea cve) {
        return new LazyArea(this.cache, Math.min(1024, cve.getMaxCache() * 4), cws);
    }
    
    public LazyArea createResult(final PixelTransformer cws, final LazyArea cve2, final LazyArea cve3) {
        return new LazyArea(this.cache, Math.min(1024, Math.max(cve2.getMaxCache(), cve3.getMaxCache()) * 4), cws);
    }
    
    public void initRandom(final long long1, final long long2) {
        long long3 = this.seed;
        long3 = LinearCongruentialGenerator.next(long3, long1);
        long3 = LinearCongruentialGenerator.next(long3, long2);
        long3 = LinearCongruentialGenerator.next(long3, long1);
        long3 = LinearCongruentialGenerator.next(long3, long2);
        this.rval = long3;
    }
    
    public int nextRandom(final int integer) {
        final int integer2 = (int)Math.floorMod(this.rval >> 24, (long)integer);
        this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
        return integer2;
    }
    
    public ImprovedNoise getBiomeNoise() {
        return this.biomeNoise;
    }
    
    private static long mixSeed(final long long1, final long long2) {
        long long3 = long2;
        long3 = LinearCongruentialGenerator.next(long3, long2);
        long3 = LinearCongruentialGenerator.next(long3, long2);
        long3 = LinearCongruentialGenerator.next(long3, long2);
        long long4 = long1;
        long4 = LinearCongruentialGenerator.next(long4, long3);
        long4 = LinearCongruentialGenerator.next(long4, long3);
        long4 = LinearCongruentialGenerator.next(long4, long3);
        return long4;
    }
}
