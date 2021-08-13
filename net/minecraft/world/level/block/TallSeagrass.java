package net.minecraft.world.level.block;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class TallSeagrass extends DoublePlantBlock implements LiquidBlockContainer {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    protected static final VoxelShape SHAPE;
    
    public TallSeagrass(final Properties c) {
        super(c);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return TallSeagrass.SHAPE;
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.isFaceSturdy(bqz, fx, Direction.UP) && !cee.is(Blocks.MAGMA_BLOCK);
    }
    
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack(Blocks.SEAGRASS);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = super.getStateForPlacement(bnv);
        if (cee3 != null) {
            final FluidState cuu4 = bnv.getLevel().getFluidState(bnv.getClickedPos().above());
            if (cuu4.is(FluidTags.WATER) && cuu4.getAmount() == 8) {
                return cee3;
            }
        }
        return null;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        if (cee.<DoubleBlockHalf>getValue(TallSeagrass.HALF) == DoubleBlockHalf.UPPER) {
            final BlockState cee2 = brw.getBlockState(fx.below());
            return cee2.is(this) && cee2.<DoubleBlockHalf>getValue(TallSeagrass.HALF) == DoubleBlockHalf.LOWER;
        }
        final FluidState cuu5 = brw.getFluidState(fx);
        return super.canSurvive(cee, brw, fx) && cuu5.is(FluidTags.WATER) && cuu5.getAmount() == 8;
    }
    
    public FluidState getFluidState(final BlockState cee) {
        return Fluids.WATER.getSource(false);
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
        HALF = DoublePlantBlock.HALF;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }
}
