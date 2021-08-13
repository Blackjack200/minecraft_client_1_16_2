package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C1Transformer;

public enum RareBiomeSpotLayer implements C1Transformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer) {
        if (cvh.nextRandom(57) == 0 && integer == 1) {
            return 129;
        }
        return integer;
    }
}
