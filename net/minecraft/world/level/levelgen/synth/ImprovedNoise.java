package net.minecraft.world.level.levelgen.synth;

import net.minecraft.util.Mth;
import java.util.Random;

public final class ImprovedNoise {
    private final byte[] p;
    public final double xo;
    public final double yo;
    public final double zo;
    
    public ImprovedNoise(final Random random) {
        this.xo = random.nextDouble() * 256.0;
        this.yo = random.nextDouble() * 256.0;
        this.zo = random.nextDouble() * 256.0;
        this.p = new byte[256];
        for (int integer3 = 0; integer3 < 256; ++integer3) {
            this.p[integer3] = (byte)integer3;
        }
        for (int integer3 = 0; integer3 < 256; ++integer3) {
            final int integer4 = random.nextInt(256 - integer3);
            final byte byte5 = this.p[integer3];
            this.p[integer3] = this.p[integer3 + integer4];
            this.p[integer3 + integer4] = byte5;
        }
    }
    
    public double noise(final double double1, final double double2, final double double3, final double double4, final double double5) {
        final double double6 = double1 + this.xo;
        final double double7 = double2 + this.yo;
        final double double8 = double3 + this.zo;
        final int integer18 = Mth.floor(double6);
        final int integer19 = Mth.floor(double7);
        final int integer20 = Mth.floor(double8);
        final double double9 = double6 - integer18;
        final double double10 = double7 - integer19;
        final double double11 = double8 - integer20;
        final double double12 = Mth.smoothstep(double9);
        final double double13 = Mth.smoothstep(double10);
        final double double14 = Mth.smoothstep(double11);
        double double16;
        if (double4 != 0.0) {
            final double double15 = Math.min(double5, double10);
            double16 = Mth.floor(double15 / double4) * double4;
        }
        else {
            double16 = 0.0;
        }
        return this.sampleAndLerp(integer18, integer19, integer20, double9, double10 - double16, double11, double12, double13, double14);
    }
    
    private static double gradDot(final int integer, final double double2, final double double3, final double double4) {
        final int integer2 = integer & 0xF;
        return SimplexNoise.dot(SimplexNoise.GRADIENT[integer2], double2, double3, double4);
    }
    
    private int p(final int integer) {
        return this.p[integer & 0xFF] & 0xFF;
    }
    
    public double sampleAndLerp(final int integer1, final int integer2, final int integer3, final double double4, final double double5, final double double6, final double double7, final double double8, final double double9) {
        final int integer4 = this.p(integer1) + integer2;
        final int integer5 = this.p(integer4) + integer3;
        final int integer6 = this.p(integer4 + 1) + integer3;
        final int integer7 = this.p(integer1 + 1) + integer2;
        final int integer8 = this.p(integer7) + integer3;
        final int integer9 = this.p(integer7 + 1) + integer3;
        final double double10 = gradDot(this.p(integer5), double4, double5, double6);
        final double double11 = gradDot(this.p(integer8), double4 - 1.0, double5, double6);
        final double double12 = gradDot(this.p(integer6), double4, double5 - 1.0, double6);
        final double double13 = gradDot(this.p(integer9), double4 - 1.0, double5 - 1.0, double6);
        final double double14 = gradDot(this.p(integer5 + 1), double4, double5, double6 - 1.0);
        final double double15 = gradDot(this.p(integer8 + 1), double4 - 1.0, double5, double6 - 1.0);
        final double double16 = gradDot(this.p(integer6 + 1), double4, double5 - 1.0, double6 - 1.0);
        final double double17 = gradDot(this.p(integer9 + 1), double4 - 1.0, double5 - 1.0, double6 - 1.0);
        return Mth.lerp3(double7, double8, double9, double10, double11, double12, double13, double14, double15, double16, double17);
    }
}
