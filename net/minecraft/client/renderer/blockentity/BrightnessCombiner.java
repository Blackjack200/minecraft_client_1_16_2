package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.LevelRenderer;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BrightnessCombiner<S extends BlockEntity> implements DoubleBlockCombiner.Combiner<S, Int2IntFunction> {
    public Int2IntFunction acceptDouble(final S ccg1, final S ccg2) {
        return integer -> {
            final int integer2 = LevelRenderer.getLightColor(ccg1.getLevel(), ccg1.getBlockPos());
            final int integer3 = LevelRenderer.getLightColor(ccg2.getLevel(), ccg2.getBlockPos());
            final int integer4 = LightTexture.block(integer2);
            final int integer5 = LightTexture.block(integer3);
            final int integer6 = LightTexture.sky(integer2);
            final int integer7 = LightTexture.sky(integer3);
            return LightTexture.pack(Math.max(integer4, integer5), Math.max(integer6, integer7));
        };
    }
    
    public Int2IntFunction acceptSingle(final S ccg) {
        return integer -> integer;
    }
    
    public Int2IntFunction acceptNone() {
        return integer -> integer;
    }
}
