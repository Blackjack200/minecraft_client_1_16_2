package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class SpreadingSnowyDirtBlock extends SnowyDirtBlock {
    protected SpreadingSnowyDirtBlock(final Properties c) {
        super(c);
    }
    
    private static boolean canBeGrass(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.above();
        final BlockState cee2 = brw.getBlockState(fx2);
        if (cee2.is(Blocks.SNOW) && cee2.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        if (cee2.getFluidState().getAmount() == 8) {
            return false;
        }
        final int integer6 = LayerLightEngine.getLightBlockInto(brw, cee, fx, cee2, fx2, Direction.UP, cee2.getLightBlock(brw, fx2));
        return integer6 < brw.getMaxLightLevel();
    }
    
    private static boolean canPropagate(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.above();
        return canBeGrass(cee, brw, fx) && !brw.getFluidState(fx2).is(FluidTags.WATER);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!canBeGrass(cee, aag, fx)) {
            aag.setBlockAndUpdate(fx, Blocks.DIRT.defaultBlockState());
            return;
        }
        if (aag.getMaxLocalRawBrightness(fx.above()) >= 9) {
            final BlockState cee2 = this.defaultBlockState();
            for (int integer7 = 0; integer7 < 4; ++integer7) {
                final BlockPos fx2 = fx.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if (aag.getBlockState(fx2).is(Blocks.DIRT) && canPropagate(cee2, aag, fx2)) {
                    aag.setBlockAndUpdate(fx2, ((StateHolder<O, BlockState>)cee2).<Comparable, Boolean>setValue((Property<Comparable>)SpreadingSnowyDirtBlock.SNOWY, aag.getBlockState(fx2.above()).is(Blocks.SNOW)));
                }
            }
        }
    }
}
