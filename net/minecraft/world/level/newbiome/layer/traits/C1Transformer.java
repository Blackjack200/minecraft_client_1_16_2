package net.minecraft.world.level.newbiome.layer.traits;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.Context;

public interface C1Transformer extends AreaTransformer1, DimensionOffset1Transformer {
    int apply(final Context cvh, final int integer);
    
    default int applyPixel(final BigContext<?> cvg, final Area cvc, final int integer3, final int integer4) {
        final int integer5 = cvc.get(this.getParentX(integer3 + 1), this.getParentY(integer4 + 1));
        return this.apply(cvg, integer5);
    }
}
