package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;

public enum AddMushroomIslandLayer implements BishopTransformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        if (Layers.isShallowOcean(integer6) && Layers.isShallowOcean(integer5) && Layers.isShallowOcean(integer2) && Layers.isShallowOcean(integer4) && Layers.isShallowOcean(integer3) && cvh.nextRandom(100) == 0) {
            return 14;
        }
        return integer6;
    }
}
