package net.minecraft.world.level.biome;

import net.minecraft.util.LinearCongruentialGenerator;

public enum FuzzyOffsetBiomeZoomer implements BiomeZoomer {
    INSTANCE;
    
    public Biome getBiome(final long long1, final int integer2, final int integer3, final int integer4, final BiomeManager.NoiseBiomeSource a) {
        final int integer5 = integer2 - 2;
        final int integer6 = integer3 - 2;
        final int integer7 = integer4 - 2;
        final int integer8 = integer5 >> 2;
        final int integer9 = integer6 >> 2;
        final int integer10 = integer7 >> 2;
        final double double14 = (integer5 & 0x3) / 4.0;
        final double double15 = (integer6 & 0x3) / 4.0;
        final double double16 = (integer7 & 0x3) / 4.0;
        final double[] arr20 = new double[8];
        for (int integer11 = 0; integer11 < 8; ++integer11) {
            final boolean boolean22 = (integer11 & 0x4) == 0x0;
            final boolean boolean23 = (integer11 & 0x2) == 0x0;
            final boolean boolean24 = (integer11 & 0x1) == 0x0;
            final int integer12 = boolean22 ? integer8 : (integer8 + 1);
            final int integer13 = boolean23 ? integer9 : (integer9 + 1);
            final int integer14 = boolean24 ? integer10 : (integer10 + 1);
            final double double17 = boolean22 ? double14 : (double14 - 1.0);
            final double double18 = boolean23 ? double15 : (double15 - 1.0);
            final double double19 = boolean24 ? double16 : (double16 - 1.0);
            arr20[integer11] = getFiddledDistance(long1, integer12, integer13, integer14, double17, double18, double19);
        }
        int integer11 = 0;
        double double20 = arr20[0];
        for (int integer15 = 1; integer15 < 8; ++integer15) {
            if (double20 > arr20[integer15]) {
                integer11 = integer15;
                double20 = arr20[integer15];
            }
        }
        int integer15 = ((integer11 & 0x4) == 0x0) ? integer8 : (integer8 + 1);
        final int integer12 = ((integer11 & 0x2) == 0x0) ? integer9 : (integer9 + 1);
        final int integer13 = ((integer11 & 0x1) == 0x0) ? integer10 : (integer10 + 1);
        return a.getNoiseBiome(integer15, integer12, integer13);
    }
    
    private static double getFiddledDistance(final long long1, final int integer2, final int integer3, final int integer4, final double double5, final double double6, final double double7) {
        long long2 = long1;
        long2 = LinearCongruentialGenerator.next(long2, integer2);
        long2 = LinearCongruentialGenerator.next(long2, integer3);
        long2 = LinearCongruentialGenerator.next(long2, integer4);
        long2 = LinearCongruentialGenerator.next(long2, integer2);
        long2 = LinearCongruentialGenerator.next(long2, integer3);
        long2 = LinearCongruentialGenerator.next(long2, integer4);
        final double double8 = getFiddle(long2);
        long2 = LinearCongruentialGenerator.next(long2, long1);
        final double double9 = getFiddle(long2);
        long2 = LinearCongruentialGenerator.next(long2, long1);
        final double double10 = getFiddle(long2);
        return sqr(double7 + double10) + sqr(double6 + double9) + sqr(double5 + double8);
    }
    
    private static double getFiddle(final long long1) {
        final double double3 = (int)Math.floorMod(long1 >> 24, 1024L) / 1024.0;
        return (double3 - 0.5) * 0.9;
    }
    
    private static double sqr(final double double1) {
        return double1 * double1;
    }
}
