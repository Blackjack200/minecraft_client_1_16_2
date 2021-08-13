package net.minecraft.world.level.newbiome.layer.traits;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.BigContext;

public interface AreaTransformer0 {
    default <R extends Area> AreaFactory<R> run(final BigContext<R> cvg) {
        return () -> cvg.createResult((integer2, integer3) -> {
            cvg.initRandom(integer2, integer3);
            return this.applyPixel(cvg, integer2, integer3);
        });
    }
    
    int applyPixel(final Context cvh, final int integer2, final int integer3);
}
