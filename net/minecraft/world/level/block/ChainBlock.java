package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ChainBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape Y_AXIS_AABB;
    protected static final VoxelShape Z_AXIS_AABB;
    protected static final VoxelShape X_AXIS_AABB;
    
    public ChainBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ChainBlock.WATERLOGGED, false)).<Direction.Axis, Direction.Axis>setValue(ChainBlock.AXIS, Direction.Axis.Y));
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction.Axis>getValue(ChainBlock.AXIS)) {
            default: {
                return ChainBlock.X_AXIS_AABB;
            }
            case Z: {
                return ChainBlock.Z_AXIS_AABB;
            }
            case Y: {
                return ChainBlock.Y_AXIS_AABB;
            }
        }
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final boolean boolean4 = cuu3.getType() == Fluids.WATER;
        return ((StateHolder<O, BlockState>)super.getStateForPlacement(bnv)).<Comparable, Boolean>setValue((Property<Comparable>)ChainBlock.WATERLOGGED, boolean4);
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)ChainBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ChainBlock.WATERLOGGED).add(ChainBlock.AXIS);
    }
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)ChainBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        Y_AXIS_AABB = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
        Z_AXIS_AABB = Block.box(6.5, 6.5, 0.0, 9.5, 9.5, 16.0);
        X_AXIS_AABB = Block.box(0.0, 6.5, 6.5, 16.0, 9.5, 9.5);
    }
}
