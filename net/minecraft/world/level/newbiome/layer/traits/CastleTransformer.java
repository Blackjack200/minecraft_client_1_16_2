package net.minecraft.world.level.newbiome.layer.traits;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.Context;

public interface CastleTransformer extends AreaTransformer1, DimensionOffset1Transformer {
    int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6);
    
    default int applyPixel(final BigContext<?> cvg, final Area cvc, final int integer3, final int integer4) {
        return this.apply(cvg, cvc.get(this.getParentX(integer3 + 1), this.getParentY(integer4 + 0)), cvc.get(this.getParentX(integer3 + 2), this.getParentY(integer4 + 1)), cvc.get(this.getParentX(integer3 + 1), this.getParentY(integer4 + 2)), cvc.get(this.getParentX(integer3 + 0), this.getParentY(integer4 + 1)), cvc.get(this.getParentX(integer3 + 1), this.getParentY(integer4 + 1)));
    }
}
