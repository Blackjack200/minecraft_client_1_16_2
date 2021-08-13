package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;

public enum AddIslandLayer implements BishopTransformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        if (!Layers.isShallowOcean(integer6) || (Layers.isShallowOcean(integer5) && Layers.isShallowOcean(integer4) && Layers.isShallowOcean(integer2) && Layers.isShallowOcean(integer3))) {
            if (!Layers.isShallowOcean(integer6) && (Layers.isShallowOcean(integer5) || Layers.isShallowOcean(integer2) || Layers.isShallowOcean(integer4) || Layers.isShallowOcean(integer3)) && cvh.nextRandom(5) == 0) {
                if (Layers.isShallowOcean(integer5)) {
                    return (integer6 == 4) ? 4 : integer5;
                }
                if (Layers.isShallowOcean(integer2)) {
                    return (integer6 == 4) ? 4 : integer2;
                }
                if (Layers.isShallowOcean(integer4)) {
                    return (integer6 == 4) ? 4 : integer4;
                }
                if (Layers.isShallowOcean(integer3)) {
                    return (integer6 == 4) ? 4 : integer3;
                }
            }
            return integer6;
        }
        int integer7 = 1;
        int integer8 = 1;
        if (!Layers.isShallowOcean(integer5) && cvh.nextRandom(integer7++) == 0) {
            integer8 = integer5;
        }
        if (!Layers.isShallowOcean(integer4) && cvh.nextRandom(integer7++) == 0) {
            integer8 = integer4;
        }
        if (!Layers.isShallowOcean(integer2) && cvh.nextRandom(integer7++) == 0) {
            integer8 = integer2;
        }
        if (!Layers.isShallowOcean(integer3) && cvh.nextRandom(integer7++) == 0) {
            integer8 = integer3;
        }
        if (cvh.nextRandom(3) == 0) {
            return integer8;
        }
        return (integer8 == 4) ? 4 : integer6;
    }
}
