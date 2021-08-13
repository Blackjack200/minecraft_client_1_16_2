package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public enum AddDeepOceanLayer implements CastleTransformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        if (Layers.isShallowOcean(integer6)) {
            int integer7 = 0;
            if (Layers.isShallowOcean(integer2)) {
                ++integer7;
            }
            if (Layers.isShallowOcean(integer3)) {
                ++integer7;
            }
            if (Layers.isShallowOcean(integer5)) {
                ++integer7;
            }
            if (Layers.isShallowOcean(integer4)) {
                ++integer7;
            }
            if (integer7 > 3) {
                if (integer6 == 44) {
                    return 47;
                }
                if (integer6 == 45) {
                    return 48;
                }
                if (integer6 == 0) {
                    return 24;
                }
                if (integer6 == 46) {
                    return 49;
                }
                if (integer6 == 10) {
                    return 50;
                }
                return 24;
            }
        }
        return integer6;
    }
}
