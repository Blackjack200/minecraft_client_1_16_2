package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public interface SimpleWaterloggedBlock extends BucketPickup, LiquidBlockContainer {
    default boolean canPlaceLiquid(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Fluid cut) {
        return !cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED) && cut == Fluids.WATER;
    }
    
    default boolean placeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        if (!cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED) && cuu.getType() == Fluids.WATER) {
            if (!brv.isClientSide()) {
                brv.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.WATERLOGGED, true), 3);
                brv.getLiquidTicks().scheduleTick(fx, cuu.getType(), cuu.getType().getTickDelay(brv));
            }
            return true;
        }
        return false;
    }
    
    default Fluid takeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED)) {
            brv.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.WATERLOGGED, false), 3);
            return Fluids.WATER;
        }
        return Fluids.EMPTY;
    }
}
