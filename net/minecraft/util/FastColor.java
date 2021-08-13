package net.minecraft.util;

public class FastColor {
    public static class ARGB32 {
        public static int alpha(final int integer) {
            return integer >>> 24;
        }
        
        public static int red(final int integer) {
            return integer >> 16 & 0xFF;
        }
        
        public static int green(final int integer) {
            return integer >> 8 & 0xFF;
        }
        
        public static int blue(final int integer) {
            return integer & 0xFF;
        }
        
        public static int color(final int integer1, final int integer2, final int integer3, final int integer4) {
            return integer1 << 24 | integer2 << 16 | integer3 << 8 | integer4;
        }
        
        public static int multiply(final int integer1, final int integer2) {
            return color(alpha(integer1) * alpha(integer2) / 255, red(integer1) * red(integer2) / 255, green(integer1) * green(integer2) / 255, blue(integer1) * blue(integer2) / 255);
        }
    }
}
