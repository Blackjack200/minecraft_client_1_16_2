package net.minecraft.world.level.newbiome.layer.traits;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.context.BigContext;

public interface AreaTransformer2 extends DimensionTransformer {
    default <R extends Area> AreaFactory<R> run(final BigContext<R> cvg, final AreaFactory<R> cvd2, final AreaFactory<R> cvd3) {
        final Area cvc5;
        final Area cvc6;
        final Area cvc7;
        final Area cvc8;
        return () -> {
            cvc5 = cvd2.make();
            cvc6 = cvd3.make();
            return cvg.createResult((integer4, integer5) -> {
                cvg.initRandom(integer4, integer5);
                return this.applyPixel(cvg, cvc7, cvc8, integer4, integer5);
            }, (R)cvc5, (R)cvc6);
        };
    }
    
    int applyPixel(final Context cvh, final Area cvc2, final Area cvc3, final int integer4, final int integer5);
}
