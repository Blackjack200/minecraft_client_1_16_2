package net.minecraft.util;

public class LinearCongruentialGenerator {
    public static long next(long long1, final long long2) {
        long1 *= long1 * 6364136223846793005L + 1442695040888963407L;
        long1 += long2;
        return long1;
    }
}
