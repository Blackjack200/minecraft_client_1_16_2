package net.minecraft.world.level.levelgen.synth;

import net.minecraft.util.Mth;
import java.util.Random;

public class SimplexNoise {
    protected static final int[][] GRADIENT;
    private static final double SQRT_3;
    private static final double F2;
    private static final double G2;
    private final int[] p;
    public final double xo;
    public final double yo;
    public final double zo;
    
    public SimplexNoise(final Random random) {
        this.p = new int[512];
        this.xo = random.nextDouble() * 256.0;
        this.yo = random.nextDouble() * 256.0;
        this.zo = random.nextDouble() * 256.0;
        for (int integer3 = 0; integer3 < 256; ++integer3) {
            this.p[integer3] = integer3;
        }
        for (int integer3 = 0; integer3 < 256; ++integer3) {
            final int integer4 = random.nextInt(256 - integer3);
            final int integer5 = this.p[integer3];
            this.p[integer3] = this.p[integer4 + integer3];
            this.p[integer4 + integer3] = integer5;
        }
    }
    
    private int p(final int integer) {
        return this.p[integer & 0xFF];
    }
    
    protected static double dot(final int[] arr, final double double2, final double double3, final double double4) {
        return arr[0] * double2 + arr[1] * double3 + arr[2] * double4;
    }
    
    private double getCornerNoise3D(final int integer, final double double2, final double double3, final double double4, final double double5) {
        double double6 = double5 - double2 * double2 - double3 * double3 - double4 * double4;
        double double7;
        if (double6 < 0.0) {
            double7 = 0.0;
        }
        else {
            double6 *= double6;
            double7 = double6 * double6 * dot(SimplexNoise.GRADIENT[integer], double2, double3, double4);
        }
        return double7;
    }
    
    public double getValue(final double double1, final double double2) {
        final double double3 = (double1 + double2) * SimplexNoise.F2;
        final int integer8 = Mth.floor(double1 + double3);
        final int integer9 = Mth.floor(double2 + double3);
        final double double4 = (integer8 + integer9) * SimplexNoise.G2;
        final double double5 = integer8 - double4;
        final double double6 = integer9 - double4;
        final double double7 = double1 - double5;
        final double double8 = double2 - double6;
        int integer10;
        int integer11;
        if (double7 > double8) {
            integer10 = 1;
            integer11 = 0;
        }
        else {
            integer10 = 0;
            integer11 = 1;
        }
        final double double9 = double7 - integer10 + SimplexNoise.G2;
        final double double10 = double8 - integer11 + SimplexNoise.G2;
        final double double11 = double7 - 1.0 + 2.0 * SimplexNoise.G2;
        final double double12 = double8 - 1.0 + 2.0 * SimplexNoise.G2;
        final int integer12 = integer8 & 0xFF;
        final int integer13 = integer9 & 0xFF;
        final int integer14 = this.p(integer12 + this.p(integer13)) % 12;
        final int integer15 = this.p(integer12 + integer10 + this.p(integer13 + integer11)) % 12;
        final int integer16 = this.p(integer12 + 1 + this.p(integer13 + 1)) % 12;
        final double double13 = this.getCornerNoise3D(integer14, double7, double8, 0.0, 0.5);
        final double double14 = this.getCornerNoise3D(integer15, double9, double10, 0.0, 0.5);
        final double double15 = this.getCornerNoise3D(integer16, double11, double12, 0.0, 0.5);
        return 70.0 * (double13 + double14 + double15);
    }
    
    public double getValue(final double double1, final double double2, final double double3) {
        final double double4 = 0.3333333333333333;
        final double double5 = (double1 + double2 + double3) * 0.3333333333333333;
        final int integer12 = Mth.floor(double1 + double5);
        final int integer13 = Mth.floor(double2 + double5);
        final int integer14 = Mth.floor(double3 + double5);
        final double double6 = 0.16666666666666666;
        final double double7 = (integer12 + integer13 + integer14) * 0.16666666666666666;
        final double double8 = integer12 - double7;
        final double double9 = integer13 - double7;
        final double double10 = integer14 - double7;
        final double double11 = double1 - double8;
        final double double12 = double2 - double9;
        final double double13 = double3 - double10;
        int integer15;
        int integer16;
        int integer17;
        int integer18;
        int integer19;
        int integer20;
        if (double11 >= double12) {
            if (double12 >= double13) {
                integer15 = 1;
                integer16 = 0;
                integer17 = 0;
                integer18 = 1;
                integer19 = 1;
                integer20 = 0;
            }
            else if (double11 >= double13) {
                integer15 = 1;
                integer16 = 0;
                integer17 = 0;
                integer18 = 1;
                integer19 = 0;
                integer20 = 1;
            }
            else {
                integer15 = 0;
                integer16 = 0;
                integer17 = 1;
                integer18 = 1;
                integer19 = 0;
                integer20 = 1;
            }
        }
        else if (double12 < double13) {
            integer15 = 0;
            integer16 = 0;
            integer17 = 1;
            integer18 = 0;
            integer19 = 1;
            integer20 = 1;
        }
        else if (double11 < double13) {
            integer15 = 0;
            integer16 = 1;
            integer17 = 0;
            integer18 = 0;
            integer19 = 1;
            integer20 = 1;
        }
        else {
            integer15 = 0;
            integer16 = 1;
            integer17 = 0;
            integer18 = 1;
            integer19 = 1;
            integer20 = 0;
        }
        final double double14 = double11 - integer15 + 0.16666666666666666;
        final double double15 = double12 - integer16 + 0.16666666666666666;
        final double double16 = double13 - integer17 + 0.16666666666666666;
        final double double17 = double11 - integer18 + 0.3333333333333333;
        final double double18 = double12 - integer19 + 0.3333333333333333;
        final double double19 = double13 - integer20 + 0.3333333333333333;
        final double double20 = double11 - 1.0 + 0.5;
        final double double21 = double12 - 1.0 + 0.5;
        final double double22 = double13 - 1.0 + 0.5;
        final int integer21 = integer12 & 0xFF;
        final int integer22 = integer13 & 0xFF;
        final int integer23 = integer14 & 0xFF;
        final int integer24 = this.p(integer21 + this.p(integer22 + this.p(integer23))) % 12;
        final int integer25 = this.p(integer21 + integer15 + this.p(integer22 + integer16 + this.p(integer23 + integer17))) % 12;
        final int integer26 = this.p(integer21 + integer18 + this.p(integer22 + integer19 + this.p(integer23 + integer20))) % 12;
        final int integer27 = this.p(integer21 + 1 + this.p(integer22 + 1 + this.p(integer23 + 1))) % 12;
        final double double23 = this.getCornerNoise3D(integer24, double11, double12, double13, 0.6);
        final double double24 = this.getCornerNoise3D(integer25, double14, double15, double16, 0.6);
        final double double25 = this.getCornerNoise3D(integer26, double17, double18, double19, 0.6);
        final double double26 = this.getCornerNoise3D(integer27, double20, double21, double22, 0.6);
        return 32.0 * (double23 + double24 + double25 + double26);
    }
    
    static {
        GRADIENT = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { 0, -1, 1 }, { -1, 1, 0 }, { 0, -1, -1 } };
        SQRT_3 = Math.sqrt(3.0);
        F2 = 0.5 * (SimplexNoise.SQRT_3 - 1.0);
        G2 = (3.0 - SimplexNoise.SQRT_3) / 6.0;
    }
}
