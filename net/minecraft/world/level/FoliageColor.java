package net.minecraft.world.level;

public class FoliageColor {
    private static int[] pixels;
    
    public static void init(final int[] arr) {
        FoliageColor.pixels = arr;
    }
    
    public static int get(final double double1, double double2) {
        double2 *= double1;
        final int integer5 = (int)((1.0 - double1) * 255.0);
        final int integer6 = (int)((1.0 - double2) * 255.0);
        return FoliageColor.pixels[integer6 << 8 | integer5];
    }
    
    public static int getEvergreenColor() {
        return 6396257;
    }
    
    public static int getBirchColor() {
        return 8431445;
    }
    
    public static int getDefaultColor() {
        return 4764952;
    }
    
    static {
        FoliageColor.pixels = new int[65536];
    }
}
