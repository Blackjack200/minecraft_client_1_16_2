package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Seagrass extends BushBlock implements BonemealableBlock, LiquidBlockContainer {
    protected static final VoxelShape SHAPE;
    
    protected Seagrass(final Properties c) {
        super(c);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Seagrass.SHAPE;
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.isFaceSturdy(bqz, fx, Direction.UP) && !cee.is(Blocks.MAGMA_BLOCK);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        if (cuu3.is(FluidTags.WATER) && cuu3.getAmount() == 8) {
            return super.getStateForPlacement(bnv);
        }
        return null;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final BlockState cee4 = super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        if (!cee4.isAir()) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return cee4;
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return true;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    public FluidState getFluidState(final BlockState cee) {
        return Fluids.WATER.getSource(false);
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        final BlockState cee2 = Blocks.TALL_SEAGRASS.defaultBlockState();
        final BlockState cee3 = ((StateHolder<O, BlockState>)cee2).<DoubleBlockHalf, DoubleBlockHalf>setValue(TallSeagrass.HALF, DoubleBlockHalf.UPPER);
        final BlockPos fx2 = fx.above();
        if (aag.getBlockState(fx2).is(Blocks.WATER)) {
            aag.setBlock(fx, cee2, 2);
            aag.setBlock(fx2, cee3, 2);
        }
    }
    
    @Override
    public boolean canPlaceLiquid(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Fluid cut) {
        return false;
    }
    
    @Override
    public boolean placeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        return false;
    }
    
    static {
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    }
}
