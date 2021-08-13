package net.minecraft.world.level;

public class GrassColor {
    private static int[] pixels;
    
    public static void init(final int[] arr) {
        GrassColor.pixels = arr;
    }
    
    public static int get(final double double1, double double2) {
        double2 *= double1;
        final int integer5 = (int)((1.0 - double1) * 255.0);
        final int integer6 = (int)((1.0 - double2) * 255.0);
        final int integer7 = integer6 << 8 | integer5;
        if (integer7 > GrassColor.pixels.length) {
            return -65281;
        }
        return GrassColor.pixels[integer7];
    }
    
    static {
        GrassColor.pixels = new int[65536];
    }
}
