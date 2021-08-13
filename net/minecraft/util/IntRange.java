package net.minecraft.util;

import java.util.Random;

public class IntRange {
    private final int minInclusive;
    private final int maxInclusive;
    
    public IntRange(final int integer1, final int integer2) {
        if (integer2 < integer1) {
            throw new IllegalArgumentException(new StringBuilder().append("max must be >= minInclusive! Given minInclusive: ").append(integer1).append(", Given max: ").append(integer2).toString());
        }
        this.minInclusive = integer1;
        this.maxInclusive = integer2;
    }
    
    public static IntRange of(final int integer1, final int integer2) {
        return new IntRange(integer1, integer2);
    }
    
    public int randomValue(final Random random) {
        if (this.minInclusive == this.maxInclusive) {
            return this.minInclusive;
        }
        return random.nextInt(this.maxInclusive - this.minInclusive + 1) + this.minInclusive;
    }
    
    public int getMinInclusive() {
        return this.minInclusive;
    }
    
    public int getMaxInclusive() {
        return this.maxInclusive;
    }
    
    public String toString() {
        return new StringBuilder().append("IntRange[").append(this.minInclusive).append("-").append(this.maxInclusive).append("]").toString();
    }
}
