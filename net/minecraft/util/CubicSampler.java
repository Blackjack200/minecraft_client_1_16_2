package net.minecraft.util;

import javax.annotation.Nonnull;
import net.minecraft.world.phys.Vec3;

public class CubicSampler {
    private static final double[] GAUSSIAN_SAMPLE_KERNEL;
    
    @Nonnull
    public static Vec3 gaussianSampleVec3(final Vec3 dck, final Vec3Fetcher vec3Fetcher) {
        final int integer3 = Mth.floor(dck.x());
        final int integer4 = Mth.floor(dck.y());
        final int integer5 = Mth.floor(dck.z());
        final double double6 = dck.x() - integer3;
        final double double7 = dck.y() - integer4;
        final double double8 = dck.z() - integer5;
        double double9 = 0.0;
        Vec3 dck2 = Vec3.ZERO;
        for (int integer6 = 0; integer6 < 6; ++integer6) {
            final double double10 = Mth.lerp(double6, CubicSampler.GAUSSIAN_SAMPLE_KERNEL[integer6 + 1], CubicSampler.GAUSSIAN_SAMPLE_KERNEL[integer6]);
            final int integer7 = integer3 - 2 + integer6;
            for (int integer8 = 0; integer8 < 6; ++integer8) {
                final double double11 = Mth.lerp(double7, CubicSampler.GAUSSIAN_SAMPLE_KERNEL[integer8 + 1], CubicSampler.GAUSSIAN_SAMPLE_KERNEL[integer8]);
                final int integer9 = integer4 - 2 + integer8;
                for (int integer10 = 0; integer10 < 6; ++integer10) {
                    final double double12 = Mth.lerp(double8, CubicSampler.GAUSSIAN_SAMPLE_KERNEL[integer10 + 1], CubicSampler.GAUSSIAN_SAMPLE_KERNEL[integer10]);
                    final int integer11 = integer5 - 2 + integer10;
                    final double double13 = double10 * double11 * double12;
                    double9 += double13;
                    dck2 = dck2.add(vec3Fetcher.fetch(integer7, integer9, integer11).scale(double13));
                }
            }
        }
        dck2 = dck2.scale(1.0 / double9);
        return dck2;
    }
    
    static {
        GAUSSIAN_SAMPLE_KERNEL = new double[] { 0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0 };
    }
    
    public interface Vec3Fetcher {
        Vec3 fetch(final int integer1, final int integer2, final int integer3);
    }
}
