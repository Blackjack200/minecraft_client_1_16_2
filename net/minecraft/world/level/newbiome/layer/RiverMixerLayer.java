package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;

public enum RiverMixerLayer implements AreaTransformer2, DimensionOffset0Transformer {
    INSTANCE;
    
    public int applyPixel(final Context cvh, final Area cvc2, final Area cvc3, final int integer4, final int integer5) {
        final int integer6 = cvc2.get(this.getParentX(integer4), this.getParentY(integer5));
        final int integer7 = cvc3.get(this.getParentX(integer4), this.getParentY(integer5));
        if (Layers.isOcean(integer6)) {
            return integer6;
        }
        if (integer7 != 7) {
            return integer6;
        }
        if (integer6 == 12) {
            return 11;
        }
        if (integer6 == 14 || integer6 == 15) {
            return 15;
        }
        return integer7 & 0xFF;
    }
}
