package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.stats.Stats;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BellBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<BellAttachType> ATTACHMENT;
    public static final BooleanProperty POWERED;
    private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE;
    private static final VoxelShape EAST_WEST_FLOOR_SHAPE;
    private static final VoxelShape BELL_TOP_SHAPE;
    private static final VoxelShape BELL_BOTTOM_SHAPE;
    private static final VoxelShape BELL_SHAPE;
    private static final VoxelShape NORTH_SOUTH_BETWEEN;
    private static final VoxelShape EAST_WEST_BETWEEN;
    private static final VoxelShape TO_WEST;
    private static final VoxelShape TO_EAST;
    private static final VoxelShape TO_NORTH;
    private static final VoxelShape TO_SOUTH;
    private static final VoxelShape CEILING_SHAPE;
    
    public BellBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)BellBlock.FACING, Direction.NORTH)).setValue(BellBlock.ATTACHMENT, BellAttachType.FLOOR)).<Comparable, Boolean>setValue((Property<Comparable>)BellBlock.POWERED, false));
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        final boolean boolean7 = bru.hasNeighborSignal(fx3);
        if (boolean7 != cee.<Boolean>getValue((Property<Boolean>)BellBlock.POWERED)) {
            if (boolean7) {
                this.attemptToRing(bru, fx3, null);
            }
            bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)BellBlock.POWERED, boolean7), 3);
        }
    }
    
    @Override
    public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
        final Entity apx6 = bgj.getOwner();
        final Player bft7 = (apx6 instanceof Player) ? ((Player)apx6) : null;
        this.onHit(bru, cee, dcg, bft7, true);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        return this.onHit(bru, cee, dcg, bft, true) ? InteractionResult.sidedSuccess(bru.isClientSide) : InteractionResult.PASS;
    }
    
    public boolean onHit(final Level bru, final BlockState cee, final BlockHitResult dcg, @Nullable final Player bft, final boolean boolean5) {
        final Direction gc7 = dcg.getDirection();
        final BlockPos fx8 = dcg.getBlockPos();
        final boolean boolean6 = !boolean5 || this.isProperHit(cee, gc7, dcg.getLocation().y - fx8.getY());
        if (boolean6) {
            final boolean boolean7 = this.attemptToRing(bru, fx8, gc7);
            if (boolean7 && bft != null) {
                bft.awardStat(Stats.BELL_RING);
            }
            return true;
        }
        return false;
    }
    
    private boolean isProperHit(final BlockState cee, final Direction gc, final double double3) {
        if (gc.getAxis() == Direction.Axis.Y || double3 > 0.8123999834060669) {
            return false;
        }
        final Direction gc2 = cee.<Direction>getValue((Property<Direction>)BellBlock.FACING);
        final BellAttachType cet7 = cee.<BellAttachType>getValue(BellBlock.ATTACHMENT);
        switch (cet7) {
            case FLOOR: {
                return gc2.getAxis() == gc.getAxis();
            }
            case SINGLE_WALL:
            case DOUBLE_WALL: {
                return gc2.getAxis() != gc.getAxis();
            }
            case CEILING: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean attemptToRing(final Level bru, final BlockPos fx, @Nullable Direction gc) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (!bru.isClientSide && ccg5 instanceof BellBlockEntity) {
            if (gc == null) {
                gc = bru.getBlockState(fx).<Direction>getValue((Property<Direction>)BellBlock.FACING);
            }
            ((BellBlockEntity)ccg5).onHit(gc);
            bru.playSound(null, fx, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0f, 1.0f);
            return true;
        }
        return false;
    }
    
    private VoxelShape getVoxelShape(final BlockState cee) {
        final Direction gc3 = cee.<Direction>getValue((Property<Direction>)BellBlock.FACING);
        final BellAttachType cet4 = cee.<BellAttachType>getValue(BellBlock.ATTACHMENT);
        if (cet4 == BellAttachType.FLOOR) {
            if (gc3 == Direction.NORTH || gc3 == Direction.SOUTH) {
                return BellBlock.NORTH_SOUTH_FLOOR_SHAPE;
            }
            return BellBlock.EAST_WEST_FLOOR_SHAPE;
        }
        else {
            if (cet4 == BellAttachType.CEILING) {
                return BellBlock.CEILING_SHAPE;
            }
            if (cet4 == BellAttachType.DOUBLE_WALL) {
                if (gc3 == Direction.NORTH || gc3 == Direction.SOUTH) {
                    return BellBlock.NORTH_SOUTH_BETWEEN;
                }
                return BellBlock.EAST_WEST_BETWEEN;
            }
            else {
                if (gc3 == Direction.NORTH) {
                    return BellBlock.TO_NORTH;
                }
                if (gc3 == Direction.SOUTH) {
                    return BellBlock.TO_SOUTH;
                }
                if (gc3 == Direction.EAST) {
                    return BellBlock.TO_EAST;
                }
                return BellBlock.TO_WEST;
            }
        }
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.getVoxelShape(cee);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.getVoxelShape(cee);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Direction gc4 = bnv.getClickedFace();
        final BlockPos fx5 = bnv.getClickedPos();
        final Level bru6 = bnv.getLevel();
        final Direction.Axis a7 = gc4.getAxis();
        if (a7 == Direction.Axis.Y) {
            final BlockState cee3 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)BellBlock.ATTACHMENT, (gc4 == Direction.DOWN) ? BellAttachType.CEILING : BellAttachType.FLOOR)).<Comparable, Direction>setValue((Property<Comparable>)BellBlock.FACING, bnv.getHorizontalDirection());
            if (cee3.canSurvive(bnv.getLevel(), fx5)) {
                return cee3;
            }
        }
        else {
            final boolean boolean8 = (a7 == Direction.Axis.X && bru6.getBlockState(fx5.west()).isFaceSturdy(bru6, fx5.west(), Direction.EAST) && bru6.getBlockState(fx5.east()).isFaceSturdy(bru6, fx5.east(), Direction.WEST)) || (a7 == Direction.Axis.Z && bru6.getBlockState(fx5.north()).isFaceSturdy(bru6, fx5.north(), Direction.SOUTH) && bru6.getBlockState(fx5.south()).isFaceSturdy(bru6, fx5.south(), Direction.NORTH));
            BlockState cee3 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)BellBlock.FACING, gc4.getOpposite())).<Comparable, BellAttachType>setValue((Property<Comparable>)BellBlock.ATTACHMENT, boolean8 ? BellAttachType.DOUBLE_WALL : BellAttachType.SINGLE_WALL);
            if (cee3.canSurvive(bnv.getLevel(), bnv.getClickedPos())) {
                return cee3;
            }
            final boolean boolean9 = bru6.getBlockState(fx5.below()).isFaceSturdy(bru6, fx5.below(), Direction.UP);
            cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, BellAttachType>setValue((Property<Comparable>)BellBlock.ATTACHMENT, boolean9 ? BellAttachType.FLOOR : BellAttachType.CEILING);
            if (cee3.canSurvive(bnv.getLevel(), bnv.getClickedPos())) {
                return cee3;
            }
        }
        return null;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final BellAttachType cet8 = cee1.<BellAttachType>getValue(BellBlock.ATTACHMENT);
        final Direction gc2 = getConnectedDirection(cee1).getOpposite();
        if (gc2 == gc && !cee1.canSurvive(brv, fx5) && cet8 != BellAttachType.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        }
        if (gc.getAxis() == cee1.<Direction>getValue((Property<Direction>)BellBlock.FACING).getAxis()) {
            if (cet8 == BellAttachType.DOUBLE_WALL && !cee3.isFaceSturdy(brv, fx6, gc)) {
                return (((StateHolder<O, BlockState>)cee1).setValue(BellBlock.ATTACHMENT, BellAttachType.SINGLE_WALL)).<Comparable, Direction>setValue((Property<Comparable>)BellBlock.FACING, gc.getOpposite());
            }
            if (cet8 == BellAttachType.SINGLE_WALL && gc2.getOpposite() == gc && cee3.isFaceSturdy(brv, fx6, cee1.<Direction>getValue((Property<Direction>)BellBlock.FACING))) {
                return ((StateHolder<O, BlockState>)cee1).<BellAttachType, BellAttachType>setValue(BellBlock.ATTACHMENT, BellAttachType.DOUBLE_WALL);
            }
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final Direction gc5 = getConnectedDirection(cee).getOpposite();
        if (gc5 == Direction.UP) {
            return Block.canSupportCenter(brw, fx.above(), Direction.DOWN);
        }
        return FaceAttachedHorizontalDirectionalBlock.canAttach(brw, fx, gc5);
    }
    
    private static Direction getConnectedDirection(final BlockState cee) {
        switch (cee.<BellAttachType>getValue(BellBlock.ATTACHMENT)) {
            case CEILING: {
                return Direction.DOWN;
            }
            case FLOOR: {
                return Direction.UP;
            }
            default: {
                return cee.<Direction>getValue((Property<Direction>)BellBlock.FACING).getOpposite();
            }
        }
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.DESTROY;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BellBlock.FACING, BellBlock.ATTACHMENT, BellBlock.POWERED);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new BellBlockEntity();
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
        POWERED = BlockStateProperties.POWERED;
        NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);
        EAST_WEST_FLOOR_SHAPE = Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0);
        BELL_TOP_SHAPE = Block.box(5.0, 6.0, 5.0, 11.0, 13.0, 11.0);
        BELL_BOTTOM_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 6.0, 12.0);
        BELL_SHAPE = Shapes.or(BellBlock.BELL_BOTTOM_SHAPE, BellBlock.BELL_TOP_SHAPE);
        NORTH_SOUTH_BETWEEN = Shapes.or(BellBlock.BELL_SHAPE, Block.box(7.0, 13.0, 0.0, 9.0, 15.0, 16.0));
        EAST_WEST_BETWEEN = Shapes.or(BellBlock.BELL_SHAPE, Block.box(0.0, 13.0, 7.0, 16.0, 15.0, 9.0));
        TO_WEST = Shapes.or(BellBlock.BELL_SHAPE, Block.box(0.0, 13.0, 7.0, 13.0, 15.0, 9.0));
        TO_EAST = Shapes.or(BellBlock.BELL_SHAPE, Block.box(3.0, 13.0, 7.0, 16.0, 15.0, 9.0));
        TO_NORTH = Shapes.or(BellBlock.BELL_SHAPE, Block.box(7.0, 13.0, 0.0, 9.0, 15.0, 13.0));
        TO_SOUTH = Shapes.or(BellBlock.BELL_SHAPE, Block.box(7.0, 13.0, 3.0, 9.0, 15.0, 16.0));
        CEILING_SHAPE = Shapes.or(BellBlock.BELL_SHAPE, Block.box(7.0, 13.0, 7.0, 9.0, 16.0, 9.0));
    }
}
