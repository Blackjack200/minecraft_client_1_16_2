package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C1Transformer;

public enum RareBiomeLargeLayer implements C1Transformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer) {
        if (cvh.nextRandom(10) == 0 && integer == 21) {
            return 168;
        }
        return integer;
    }
}
