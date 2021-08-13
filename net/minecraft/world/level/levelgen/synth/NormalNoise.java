package net.minecraft.world.level.levelgen.synth;

import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class NormalNoise {
    private final double valueFactor;
    private final PerlinNoise first;
    private final PerlinNoise second;
    
    public static NormalNoise create(final WorldgenRandom chu, final int integer, final DoubleList doubleList) {
        return new NormalNoise(chu, integer, doubleList);
    }
    
    private NormalNoise(final WorldgenRandom chu, final int integer, final DoubleList doubleList) {
        this.first = PerlinNoise.create(chu, integer, doubleList);
        this.second = PerlinNoise.create(chu, integer, doubleList);
        int integer2 = Integer.MAX_VALUE;
        int integer3 = Integer.MIN_VALUE;
        final DoubleListIterator doubleListIterator7 = doubleList.iterator();
        while (doubleListIterator7.hasNext()) {
            final int integer4 = doubleListIterator7.nextIndex();
            final double double9 = doubleListIterator7.nextDouble();
            if (double9 != 0.0) {
                integer2 = Math.min(integer2, integer4);
                integer3 = Math.max(integer3, integer4);
            }
        }
        this.valueFactor = 0.16666666666666666 / expectedDeviation(integer3 - integer2);
    }
    
    private static double expectedDeviation(final int integer) {
        return 0.1 * (1.0 + 1.0 / (integer + 1));
    }
    
    public double getValue(final double double1, final double double2, final double double3) {
        final double double4 = double1 * 1.0181268882175227;
        final double double5 = double2 * 1.0181268882175227;
        final double double6 = double3 * 1.0181268882175227;
        return (this.first.getValue(double1, double2, double3) + this.second.getValue(double4, double5, double6)) * this.valueFactor;
    }
}
