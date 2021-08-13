package net.minecraft.world.level.levelgen.synth;

import java.util.Random;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class PerlinSimplexNoise implements SurfaceNoise {
    private final SimplexNoise[] noiseLevels;
    private final double highestFreqValueFactor;
    private final double highestFreqInputFactor;
    
    public PerlinSimplexNoise(final WorldgenRandom chu, final IntStream intStream) {
        this(chu, (List<Integer>)intStream.boxed().collect(ImmutableList.toImmutableList()));
    }
    
    public PerlinSimplexNoise(final WorldgenRandom chu, final List<Integer> list) {
        this(chu, (IntSortedSet)new IntRBTreeSet((Collection)list));
    }
    
    private PerlinSimplexNoise(final WorldgenRandom chu, final IntSortedSet intSortedSet) {
        if (intSortedSet.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        }
        final int integer4 = -intSortedSet.firstInt();
        final int integer5 = intSortedSet.lastInt();
        final int integer6 = integer4 + integer5 + 1;
        if (integer6 < 1) {
            throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
        }
        final SimplexNoise cua7 = new SimplexNoise(chu);
        final int integer7 = integer5;
        this.noiseLevels = new SimplexNoise[integer6];
        if (integer7 >= 0 && integer7 < integer6 && intSortedSet.contains(0)) {
            this.noiseLevels[integer7] = cua7;
        }
        for (int integer8 = integer7 + 1; integer8 < integer6; ++integer8) {
            if (integer8 >= 0 && intSortedSet.contains(integer7 - integer8)) {
                this.noiseLevels[integer8] = new SimplexNoise(chu);
            }
            else {
                chu.consumeCount(262);
            }
        }
        if (integer5 > 0) {
            final long long9 = (long)(cua7.getValue(cua7.xo, cua7.yo, cua7.zo) * 9.223372036854776E18);
            final WorldgenRandom chu2 = new WorldgenRandom(long9);
            for (int integer9 = integer7 - 1; integer9 >= 0; --integer9) {
                if (integer9 < integer6 && intSortedSet.contains(integer7 - integer9)) {
                    this.noiseLevels[integer9] = new SimplexNoise(chu2);
                }
                else {
                    chu2.consumeCount(262);
                }
            }
        }
        this.highestFreqInputFactor = Math.pow(2.0, (double)integer5);
        this.highestFreqValueFactor = 1.0 / (Math.pow(2.0, (double)integer6) - 1.0);
    }
    
    public double getValue(final double double1, final double double2, final boolean boolean3) {
        double double3 = 0.0;
        double double4 = this.highestFreqInputFactor;
        double double5 = this.highestFreqValueFactor;
        for (final SimplexNoise cua16 : this.noiseLevels) {
            if (cua16 != null) {
                double3 += cua16.getValue(double1 * double4 + (boolean3 ? cua16.xo : 0.0), double2 * double4 + (boolean3 ? cua16.yo : 0.0)) * double5;
            }
            double4 /= 2.0;
            double5 *= 2.0;
        }
        return double3;
    }
    
    public double getSurfaceNoiseValue(final double double1, final double double2, final double double3, final double double4) {
        return this.getValue(double1, double2, true) * 0.55;
    }
}
