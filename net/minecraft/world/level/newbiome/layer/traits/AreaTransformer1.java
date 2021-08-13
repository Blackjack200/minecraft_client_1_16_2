package net.minecraft.world.level.newbiome.layer.traits;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.context.BigContext;

public interface AreaTransformer1 extends DimensionTransformer {
    default <R extends Area> AreaFactory<R> run(final BigContext<R> cvg, final AreaFactory<R> cvd) {
        final Area cvc4;
        final Area cvc5;
        return () -> {
            cvc4 = cvd.make();
            return cvg.createResult((integer3, integer4) -> {
                cvg.initRandom(integer3, integer4);
                return this.applyPixel(cvg, cvc5, integer3, integer4);
            }, (R)cvc4);
        };
    }
    
    int applyPixel(final BigContext<?> cvg, final Area cvc, final int integer3, final int integer4);
}
