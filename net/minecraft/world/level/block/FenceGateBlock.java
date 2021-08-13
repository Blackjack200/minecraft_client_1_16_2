package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FenceGateBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPEN;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty IN_WALL;
    protected static final VoxelShape Z_SHAPE;
    protected static final VoxelShape X_SHAPE;
    protected static final VoxelShape Z_SHAPE_LOW;
    protected static final VoxelShape X_SHAPE_LOW;
    protected static final VoxelShape Z_COLLISION_SHAPE;
    protected static final VoxelShape X_COLLISION_SHAPE;
    protected static final VoxelShape Z_OCCLUSION_SHAPE;
    protected static final VoxelShape X_OCCLUSION_SHAPE;
    protected static final VoxelShape Z_OCCLUSION_SHAPE_LOW;
    protected static final VoxelShape X_OCCLUSION_SHAPE_LOW;
    
    public FenceGateBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)FenceGateBlock.OPEN, false)).setValue((Property<Comparable>)FenceGateBlock.POWERED, false)).<Comparable, Boolean>setValue((Property<Comparable>)FenceGateBlock.IN_WALL, false));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.IN_WALL)) {
            return (cee.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getAxis() == Direction.Axis.X) ? FenceGateBlock.X_SHAPE_LOW : FenceGateBlock.Z_SHAPE_LOW;
        }
        return (cee.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getAxis() == Direction.Axis.X) ? FenceGateBlock.X_SHAPE : FenceGateBlock.Z_SHAPE;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final Direction.Axis a8 = gc.getAxis();
        if (cee1.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getClockWise().getAxis() == a8) {
            final boolean boolean9 = this.isWall(cee3) || this.isWall(brv.getBlockState(fx5.relative(gc.getOpposite())));
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)FenceGateBlock.IN_WALL, boolean9);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN)) {
            return Shapes.empty();
        }
        return (cee.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getAxis() == Direction.Axis.Z) ? FenceGateBlock.Z_COLLISION_SHAPE : FenceGateBlock.X_COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getOcclusionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        if (cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.IN_WALL)) {
            return (cee.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getAxis() == Direction.Axis.X) ? FenceGateBlock.X_OCCLUSION_SHAPE_LOW : FenceGateBlock.Z_OCCLUSION_SHAPE_LOW;
        }
        return (cee.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getAxis() == Direction.Axis.X) ? FenceGateBlock.X_OCCLUSION_SHAPE : FenceGateBlock.Z_OCCLUSION_SHAPE;
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        switch (cxb) {
            case LAND: {
                return cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN);
            }
            case WATER: {
                return false;
            }
            case AIR: {
                return cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN);
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Level bru3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final boolean boolean5 = bru3.hasNeighborSignal(fx4);
        final Direction gc6 = bnv.getHorizontalDirection();
        final Direction.Axis a7 = gc6.getAxis();
        final boolean boolean6 = (a7 == Direction.Axis.Z && (this.isWall(bru3.getBlockState(fx4.west())) || this.isWall(bru3.getBlockState(fx4.east())))) || (a7 == Direction.Axis.X && (this.isWall(bru3.getBlockState(fx4.north())) || this.isWall(bru3.getBlockState(fx4.south()))));
        return (((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)FenceGateBlock.FACING, gc6)).setValue((Property<Comparable>)FenceGateBlock.OPEN, boolean5)).setValue((Property<Comparable>)FenceGateBlock.POWERED, boolean5)).<Comparable, Boolean>setValue((Property<Comparable>)FenceGateBlock.IN_WALL, boolean6);
    }
    
    private boolean isWall(final BlockState cee) {
        return cee.getBlock().is(BlockTags.WALLS);
    }
    
    @Override
    public InteractionResult use(BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN)) {
            cee = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)FenceGateBlock.OPEN, false);
            bru.setBlock(fx, cee, 10);
        }
        else {
            final Direction gc8 = bft.getDirection();
            if (cee.<Comparable>getValue((Property<Comparable>)FenceGateBlock.FACING) == gc8.getOpposite()) {
                cee = ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)FenceGateBlock.FACING, gc8);
            }
            cee = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)FenceGateBlock.OPEN, true);
            bru.setBlock(fx, cee, 10);
        }
        bru.levelEvent(bft, ((boolean)cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN)) ? 1008 : 1014, fx, 0);
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.isClientSide) {
            return;
        }
        final boolean boolean7 = bru.hasNeighborSignal(fx3);
        if (cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.POWERED) != boolean7) {
            bru.setBlock(fx3, (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)FenceGateBlock.POWERED, boolean7)).<Comparable, Boolean>setValue((Property<Comparable>)FenceGateBlock.OPEN, boolean7), 2);
            if (cee.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN) != boolean7) {
                bru.levelEvent(null, boolean7 ? 1008 : 1014, fx3, 0);
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(FenceGateBlock.FACING, FenceGateBlock.OPEN, FenceGateBlock.POWERED, FenceGateBlock.IN_WALL);
    }
    
    public static boolean connectsToDirection(final BlockState cee, final Direction gc) {
        return cee.<Direction>getValue((Property<Direction>)FenceGateBlock.FACING).getAxis() == gc.getClockWise().getAxis();
    }
    
    static {
        OPEN = BlockStateProperties.OPEN;
        POWERED = BlockStateProperties.POWERED;
        IN_WALL = BlockStateProperties.IN_WALL;
        Z_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
        X_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
        Z_SHAPE_LOW = Block.box(0.0, 0.0, 6.0, 16.0, 13.0, 10.0);
        X_SHAPE_LOW = Block.box(6.0, 0.0, 0.0, 10.0, 13.0, 16.0);
        Z_COLLISION_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 24.0, 10.0);
        X_COLLISION_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 24.0, 16.0);
        Z_OCCLUSION_SHAPE = Shapes.or(Block.box(0.0, 5.0, 7.0, 2.0, 16.0, 9.0), Block.box(14.0, 5.0, 7.0, 16.0, 16.0, 9.0));
        X_OCCLUSION_SHAPE = Shapes.or(Block.box(7.0, 5.0, 0.0, 9.0, 16.0, 2.0), Block.box(7.0, 5.0, 14.0, 9.0, 16.0, 16.0));
        Z_OCCLUSION_SHAPE_LOW = Shapes.or(Block.box(0.0, 2.0, 7.0, 2.0, 13.0, 9.0), Block.box(14.0, 2.0, 7.0, 16.0, 13.0, 9.0));
        X_OCCLUSION_SHAPE_LOW = Shapes.or(Block.box(7.0, 2.0, 0.0, 9.0, 13.0, 2.0), Block.box(7.0, 2.0, 14.0, 9.0, 13.0, 16.0));
    }
}
