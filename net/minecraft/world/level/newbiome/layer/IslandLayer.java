package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;

public enum IslandLayer implements AreaTransformer0 {
    INSTANCE;
    
    public int applyPixel(final Context cvh, final int integer2, final int integer3) {
        return ((integer2 == 0 && integer3 == 0) || cvh.nextRandom(10) == 0) ? 1 : 0;
    }
}
