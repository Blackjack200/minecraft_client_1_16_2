package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;

public enum OceanLayer implements AreaTransformer0 {
    INSTANCE;
    
    public int applyPixel(final Context cvh, final int integer2, final int integer3) {
        final ImprovedNoise ctw5 = cvh.getBiomeNoise();
        final double double6 = ctw5.noise(integer2 / 8.0, integer3 / 8.0, 0.0, 0.0, 0.0);
        if (double6 > 0.4) {
            return 44;
        }
        if (double6 > 0.2) {
            return 45;
        }
        if (double6 < -0.4) {
            return 10;
        }
        if (double6 < -0.2) {
            return 46;
        }
        return 0;
    }
}
