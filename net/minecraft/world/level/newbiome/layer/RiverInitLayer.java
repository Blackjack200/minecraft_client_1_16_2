package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;

public enum RiverInitLayer implements C0Transformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer) {
        return Layers.isShallowOcean(integer) ? integer : (cvh.nextRandom(299999) + 2);
    }
}
