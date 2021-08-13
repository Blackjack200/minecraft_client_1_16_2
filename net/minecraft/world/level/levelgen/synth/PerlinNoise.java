package net.minecraft.world.level.levelgen.synth;

import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import java.util.Random;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public class PerlinNoise implements SurfaceNoise {
    private final ImprovedNoise[] noiseLevels;
    private final DoubleList amplitudes;
    private final double lowestFreqValueFactor;
    private final double lowestFreqInputFactor;
    
    public PerlinNoise(final WorldgenRandom chu, final IntStream intStream) {
        this(chu, (List<Integer>)intStream.boxed().collect(ImmutableList.toImmutableList()));
    }
    
    public PerlinNoise(final WorldgenRandom chu, final List<Integer> list) {
        this(chu, (IntSortedSet)new IntRBTreeSet((Collection)list));
    }
    
    public static PerlinNoise create(final WorldgenRandom chu, final int integer, final DoubleList doubleList) {
        return new PerlinNoise(chu, (Pair<Integer, DoubleList>)Pair.of(integer, doubleList));
    }
    
    private static Pair<Integer, DoubleList> makeAmplitudes(final IntSortedSet intSortedSet) {
        if (intSortedSet.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        }
        final int integer2 = -intSortedSet.firstInt();
        final int integer3 = intSortedSet.lastInt();
        final int integer4 = integer2 + integer3 + 1;
        if (integer4 < 1) {
            throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
        }
        final DoubleList doubleList5 = (DoubleList)new DoubleArrayList(new double[integer4]);
        final IntBidirectionalIterator intBidirectionalIterator6 = intSortedSet.iterator();
        while (intBidirectionalIterator6.hasNext()) {
            final int integer5 = intBidirectionalIterator6.nextInt();
            doubleList5.set(integer5 + integer2, 1.0);
        }
        return (Pair<Integer, DoubleList>)Pair.of((-integer2), doubleList5);
    }
    
    private PerlinNoise(final WorldgenRandom chu, final IntSortedSet intSortedSet) {
        this(chu, makeAmplitudes(intSortedSet));
    }
    
    private PerlinNoise(final WorldgenRandom chu, final Pair<Integer, DoubleList> pair) {
        final int integer4 = (int)pair.getFirst();
        this.amplitudes = (DoubleList)pair.getSecond();
        final ImprovedNoise ctw5 = new ImprovedNoise(chu);
        final int integer5 = this.amplitudes.size();
        final int integer6 = -integer4;
        this.noiseLevels = new ImprovedNoise[integer5];
        if (integer6 >= 0 && integer6 < integer5) {
            final double double8 = this.amplitudes.getDouble(integer6);
            if (double8 != 0.0) {
                this.noiseLevels[integer6] = ctw5;
            }
        }
        for (int integer7 = integer6 - 1; integer7 >= 0; --integer7) {
            if (integer7 < integer5) {
                final double double9 = this.amplitudes.getDouble(integer7);
                if (double9 != 0.0) {
                    this.noiseLevels[integer7] = new ImprovedNoise(chu);
                }
                else {
                    chu.consumeCount(262);
                }
            }
            else {
                chu.consumeCount(262);
            }
        }
        if (integer6 < integer5 - 1) {
            final long long8 = (long)(ctw5.noise(0.0, 0.0, 0.0, 0.0, 0.0) * 9.223372036854776E18);
            final WorldgenRandom chu2 = new WorldgenRandom(long8);
            for (int integer8 = integer6 + 1; integer8 < integer5; ++integer8) {
                if (integer8 >= 0) {
                    final double double10 = this.amplitudes.getDouble(integer8);
                    if (double10 != 0.0) {
                        this.noiseLevels[integer8] = new ImprovedNoise(chu2);
                    }
                    else {
                        chu2.consumeCount(262);
                    }
                }
                else {
                    chu2.consumeCount(262);
                }
            }
        }
        this.lowestFreqInputFactor = Math.pow(2.0, (double)(-integer6));
        this.lowestFreqValueFactor = Math.pow(2.0, (double)(integer5 - 1)) / (Math.pow(2.0, (double)integer5) - 1.0);
    }
    
    public double getValue(final double double1, final double double2, final double double3) {
        return this.getValue(double1, double2, double3, 0.0, 0.0, false);
    }
    
    public double getValue(final double double1, final double double2, final double double3, final double double4, final double double5, final boolean boolean6) {
        double double6 = 0.0;
        double double7 = this.lowestFreqInputFactor;
        double double8 = this.lowestFreqValueFactor;
        for (int integer19 = 0; integer19 < this.noiseLevels.length; ++integer19) {
            final ImprovedNoise ctw20 = this.noiseLevels[integer19];
            if (ctw20 != null) {
                double6 += this.amplitudes.getDouble(integer19) * ctw20.noise(wrap(double1 * double7), boolean6 ? (-ctw20.yo) : wrap(double2 * double7), wrap(double3 * double7), double4 * double7, double5 * double7) * double8;
            }
            double7 *= 2.0;
            double8 /= 2.0;
        }
        return double6;
    }
    
    @Nullable
    public ImprovedNoise getOctaveNoise(final int integer) {
        return this.noiseLevels[this.noiseLevels.length - 1 - integer];
    }
    
    public static double wrap(final double double1) {
        return double1 - Mth.lfloor(double1 / 3.3554432E7 + 0.5) * 3.3554432E7;
    }
    
    public double getSurfaceNoiseValue(final double double1, final double double2, final double double3, final double double4) {
        return this.getValue(double1, double2, 0.0, double3, double4, false);
    }
}
