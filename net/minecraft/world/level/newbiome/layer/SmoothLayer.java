package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public enum SmoothLayer implements CastleTransformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        final boolean boolean8 = integer3 == integer5;
        final boolean boolean9 = integer2 == integer4;
        if (boolean8 != boolean9) {
            return boolean8 ? integer5 : integer2;
        }
        if (boolean8) {
            return (cvh.nextRandom(2) == 0) ? integer5 : integer2;
        }
        return integer6;
    }
}
