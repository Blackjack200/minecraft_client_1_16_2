package net.minecraft.util;

public class TimeUtil {
    public static IntRange rangeOfSeconds(final int integer1, final int integer2) {
        return new IntRange(integer1 * 20, integer2 * 20);
    }
}
