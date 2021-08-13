package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import javax.annotation.Nullable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty OPEN;
    public static final EnumProperty<Half> HALF;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape EAST_OPEN_AABB;
    protected static final VoxelShape WEST_OPEN_AABB;
    protected static final VoxelShape SOUTH_OPEN_AABB;
    protected static final VoxelShape NORTH_OPEN_AABB;
    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape TOP_AABB;
    
    protected TrapDoorBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)TrapDoorBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)TrapDoorBlock.OPEN, false)).setValue(TrapDoorBlock.HALF, Half.BOTTOM)).setValue((Property<Comparable>)TrapDoorBlock.POWERED, false)).<Comparable, Boolean>setValue((Property<Comparable>)TrapDoorBlock.WATERLOGGED, false));
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (!cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN)) {
            return (cee.<Half>getValue(TrapDoorBlock.HALF) == Half.TOP) ? TrapDoorBlock.TOP_AABB : TrapDoorBlock.BOTTOM_AABB;
        }
        switch (cee.<Direction>getValue((Property<Direction>)TrapDoorBlock.FACING)) {
            default: {
                return TrapDoorBlock.NORTH_OPEN_AABB;
            }
            case SOUTH: {
                return TrapDoorBlock.SOUTH_OPEN_AABB;
            }
            case WEST: {
                return TrapDoorBlock.WEST_OPEN_AABB;
            }
            case EAST: {
                return TrapDoorBlock.EAST_OPEN_AABB;
            }
        }
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        switch (cxb) {
            case LAND: {
                return cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN);
            }
            case WATER: {
                return cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.WATERLOGGED);
            }
            case AIR: {
                return cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN);
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractionResult use(BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (this.material == Material.METAL) {
            return InteractionResult.PASS;
        }
        cee = ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)TrapDoorBlock.OPEN);
        bru.setBlock(fx, cee, 2);
        if (cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.WATERLOGGED)) {
            bru.getLiquidTicks().scheduleTick(fx, Fluids.WATER, Fluids.WATER.getTickDelay(bru));
        }
        this.playSound(bft, bru, fx, cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN));
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    protected void playSound(@Nullable final Player bft, final Level bru, final BlockPos fx, final boolean boolean4) {
        if (boolean4) {
            final int integer6 = (this.material == Material.METAL) ? 1037 : 1007;
            bru.levelEvent(bft, integer6, fx, 0);
        }
        else {
            final int integer6 = (this.material == Material.METAL) ? 1036 : 1013;
            bru.levelEvent(bft, integer6, fx, 0);
        }
    }
    
    public void neighborChanged(BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.isClientSide) {
            return;
        }
        final boolean boolean7 = bru.hasNeighborSignal(fx3);
        if (boolean7 != cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.POWERED)) {
            if (cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN) != boolean7) {
                cee = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)TrapDoorBlock.OPEN, boolean7);
                this.playSound(null, bru, fx3, boolean7);
            }
            bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)TrapDoorBlock.POWERED, boolean7), 2);
            if (cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.WATERLOGGED)) {
                bru.getLiquidTicks().scheduleTick(fx3, Fluids.WATER, Fluids.WATER.getTickDelay(bru));
            }
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = this.defaultBlockState();
        final FluidState cuu4 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final Direction gc5 = bnv.getClickedFace();
        if (bnv.replacingClickedOnBlock() || !gc5.getAxis().isHorizontal()) {
            cee3 = (((StateHolder<O, BlockState>)cee3).setValue((Property<Comparable>)TrapDoorBlock.FACING, bnv.getHorizontalDirection().getOpposite())).<Comparable, Half>setValue((Property<Comparable>)TrapDoorBlock.HALF, (gc5 == Direction.UP) ? Half.BOTTOM : Half.TOP);
        }
        else {
            cee3 = (((StateHolder<O, BlockState>)cee3).setValue((Property<Comparable>)TrapDoorBlock.FACING, gc5)).<Comparable, Half>setValue((Property<Comparable>)TrapDoorBlock.HALF, (bnv.getClickLocation().y - bnv.getClickedPos().getY() > 0.5) ? Half.TOP : Half.BOTTOM);
        }
        if (bnv.getLevel().hasNeighborSignal(bnv.getClickedPos())) {
            cee3 = (((StateHolder<O, BlockState>)cee3).setValue((Property<Comparable>)TrapDoorBlock.OPEN, true)).<Comparable, Boolean>setValue((Property<Comparable>)TrapDoorBlock.POWERED, true);
        }
        return ((StateHolder<O, BlockState>)cee3).<Comparable, Boolean>setValue((Property<Comparable>)TrapDoorBlock.WATERLOGGED, cuu4.getType() == Fluids.WATER);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(TrapDoorBlock.FACING, TrapDoorBlock.OPEN, TrapDoorBlock.HALF, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
    }
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    static {
        OPEN = BlockStateProperties.OPEN;
        HALF = BlockStateProperties.HALF;
        POWERED = BlockStateProperties.POWERED;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        EAST_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
        WEST_OPEN_AABB = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        SOUTH_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
        NORTH_OPEN_AABB = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
        TOP_AABB = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);
    }
}
