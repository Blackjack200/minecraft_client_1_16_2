package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class SlabBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<SlabType> TYPE;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape TOP_AABB;
    
    public SlabBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue(SlabBlock.TYPE, SlabType.BOTTOM)).<Comparable, Boolean>setValue((Property<Comparable>)SlabBlock.WATERLOGGED, false));
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return cee.<SlabType>getValue(SlabBlock.TYPE) != SlabType.DOUBLE;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SlabBlock.TYPE, SlabBlock.WATERLOGGED);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final SlabType cfj6 = cee.<SlabType>getValue(SlabBlock.TYPE);
        switch (cfj6) {
            case DOUBLE: {
                return Shapes.block();
            }
            case TOP: {
                return SlabBlock.TOP_AABB;
            }
            default: {
                return SlabBlock.BOTTOM_AABB;
            }
        }
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockPos fx3 = bnv.getClickedPos();
        final BlockState cee4 = bnv.getLevel().getBlockState(fx3);
        if (cee4.is(this)) {
            return (((StateHolder<O, BlockState>)cee4).setValue(SlabBlock.TYPE, SlabType.DOUBLE)).<Comparable, Boolean>setValue((Property<Comparable>)SlabBlock.WATERLOGGED, false);
        }
        final FluidState cuu5 = bnv.getLevel().getFluidState(fx3);
        final BlockState cee5 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue(SlabBlock.TYPE, SlabType.BOTTOM)).<Comparable, Boolean>setValue((Property<Comparable>)SlabBlock.WATERLOGGED, cuu5.getType() == Fluids.WATER);
        final Direction gc7 = bnv.getClickedFace();
        if (gc7 == Direction.DOWN || (gc7 != Direction.UP && bnv.getClickLocation().y - fx3.getY() > 0.5)) {
            return ((StateHolder<O, BlockState>)cee5).<SlabType, SlabType>setValue(SlabBlock.TYPE, SlabType.TOP);
        }
        return cee5;
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        final ItemStack bly4 = bnv.getItemInHand();
        final SlabType cfj5 = cee.<SlabType>getValue(SlabBlock.TYPE);
        if (cfj5 == SlabType.DOUBLE || bly4.getItem() != this.asItem()) {
            return false;
        }
        if (!bnv.replacingClickedOnBlock()) {
            return true;
        }
        final boolean boolean6 = bnv.getClickLocation().y - bnv.getClickedPos().getY() > 0.5;
        final Direction gc7 = bnv.getClickedFace();
        if (cfj5 == SlabType.BOTTOM) {
            return gc7 == Direction.UP || (boolean6 && gc7.getAxis().isHorizontal());
        }
        return gc7 == Direction.DOWN || (!boolean6 && gc7.getAxis().isHorizontal());
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)SlabBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Override
    public boolean placeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        return cee.<SlabType>getValue(SlabBlock.TYPE) != SlabType.DOUBLE && super.placeLiquid(brv, fx, cee, cuu);
    }
    
    @Override
    public boolean canPlaceLiquid(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Fluid cut) {
        return cee.<SlabType>getValue(SlabBlock.TYPE) != SlabType.DOUBLE && super.canPlaceLiquid(bqz, fx, cee, cut);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)SlabBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        switch (cxb) {
            case LAND: {
                return false;
            }
            case WATER: {
                return bqz.getFluidState(fx).is(FluidTags.WATER);
            }
            case AIR: {
                return false;
            }
            default: {
                return false;
            }
        }
    }
    
    static {
        TYPE = BlockStateProperties.SLAB_TYPE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        TOP_AABB = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    }
}
