package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C1Transformer;

public enum AddSnowLayer implements C1Transformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer) {
        if (Layers.isShallowOcean(integer)) {
            return integer;
        }
        final int integer2 = cvh.nextRandom(6);
        if (integer2 == 0) {
            return 4;
        }
        if (integer2 == 1) {
            return 3;
        }
        return 1;
    }
}
