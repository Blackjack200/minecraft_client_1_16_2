package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class DoorBlock extends Block {
    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE;
    public static final BooleanProperty POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    
    protected DoorBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)DoorBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)DoorBlock.OPEN, false)).setValue(DoorBlock.HINGE, DoorHingeSide.LEFT)).setValue((Property<Comparable>)DoorBlock.POWERED, false)).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)DoorBlock.FACING);
        final boolean boolean7 = !cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN);
        final boolean boolean8 = cee.<DoorHingeSide>getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
        switch (gc6) {
            default: {
                return boolean7 ? DoorBlock.EAST_AABB : (boolean8 ? DoorBlock.NORTH_AABB : DoorBlock.SOUTH_AABB);
            }
            case SOUTH: {
                return boolean7 ? DoorBlock.SOUTH_AABB : (boolean8 ? DoorBlock.EAST_AABB : DoorBlock.WEST_AABB);
            }
            case WEST: {
                return boolean7 ? DoorBlock.WEST_AABB : (boolean8 ? DoorBlock.SOUTH_AABB : DoorBlock.NORTH_AABB);
            }
            case NORTH: {
                return boolean7 ? DoorBlock.NORTH_AABB : (boolean8 ? DoorBlock.WEST_AABB : DoorBlock.EAST_AABB);
            }
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final DoubleBlockHalf cfa8 = cee1.<DoubleBlockHalf>getValue(DoorBlock.HALF);
        if (gc.getAxis() == Direction.Axis.Y && cfa8 == DoubleBlockHalf.LOWER == (gc == Direction.UP)) {
            if (cee3.is(this) && cee3.<DoubleBlockHalf>getValue(DoorBlock.HALF) != cfa8) {
                return (((((StateHolder<O, BlockState>)cee1).setValue((Property<Comparable>)DoorBlock.FACING, (Comparable)cee3.<V>getValue((Property<V>)DoorBlock.FACING))).setValue((Property<Comparable>)DoorBlock.OPEN, (Comparable)cee3.<V>getValue((Property<V>)DoorBlock.OPEN))).setValue(DoorBlock.HINGE, (Comparable)cee3.<V>getValue((Property<V>)DoorBlock.HINGE))).<Comparable, Comparable>setValue((Property<Comparable>)DoorBlock.POWERED, (Comparable)cee3.<V>getValue((Property<V>)DoorBlock.POWERED));
            }
            return Blocks.AIR.defaultBlockState();
        }
        else {
            if (cfa8 == DoubleBlockHalf.LOWER && gc == Direction.DOWN && !cee1.canSurvive(brv, fx5)) {
                return Blocks.AIR.defaultBlockState();
            }
            return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        }
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide && bft.isCreative()) {
            DoublePlantBlock.preventCreativeDropFromBottomPart(bru, fx, cee, bft);
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        switch (cxb) {
            case LAND: {
                return cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN);
            }
            case WATER: {
                return false;
            }
            case AIR: {
                return cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN);
            }
            default: {
                return false;
            }
        }
    }
    
    private int getCloseSound() {
        return (this.material == Material.METAL) ? 1011 : 1012;
    }
    
    private int getOpenSound() {
        return (this.material == Material.METAL) ? 1005 : 1006;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockPos fx3 = bnv.getClickedPos();
        if (fx3.getY() < 255 && bnv.getLevel().getBlockState(fx3.above()).canBeReplaced(bnv)) {
            final Level bru4 = bnv.getLevel();
            final boolean boolean5 = bru4.hasNeighborSignal(fx3) || bru4.hasNeighborSignal(fx3.above());
            return ((((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)DoorBlock.FACING, bnv.getHorizontalDirection())).setValue(DoorBlock.HINGE, this.getHinge(bnv))).setValue((Property<Comparable>)DoorBlock.POWERED, boolean5)).setValue((Property<Comparable>)DoorBlock.OPEN, boolean5)).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        bru.setBlock(fx.above(), ((StateHolder<O, BlockState>)cee).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3);
    }
    
    private DoorHingeSide getHinge(final BlockPlaceContext bnv) {
        final BlockGetter bqz3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final Direction gc5 = bnv.getHorizontalDirection();
        final BlockPos fx5 = fx4.above();
        final Direction gc6 = gc5.getCounterClockWise();
        final BlockPos fx6 = fx4.relative(gc6);
        final BlockState cee9 = bqz3.getBlockState(fx6);
        final BlockPos fx7 = fx5.relative(gc6);
        final BlockState cee10 = bqz3.getBlockState(fx7);
        final Direction gc7 = gc5.getClockWise();
        final BlockPos fx8 = fx4.relative(gc7);
        final BlockState cee11 = bqz3.getBlockState(fx8);
        final BlockPos fx9 = fx5.relative(gc7);
        final BlockState cee12 = bqz3.getBlockState(fx9);
        final int integer17 = (cee9.isCollisionShapeFullBlock(bqz3, fx6) ? -1 : 0) + (cee10.isCollisionShapeFullBlock(bqz3, fx7) ? -1 : 0) + (cee11.isCollisionShapeFullBlock(bqz3, fx8) ? 1 : 0) + (cee12.isCollisionShapeFullBlock(bqz3, fx9) ? 1 : 0);
        final boolean boolean18 = cee9.is(this) && cee9.<DoubleBlockHalf>getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
        final boolean boolean19 = cee11.is(this) && cee11.<DoubleBlockHalf>getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
        if ((boolean18 && !boolean19) || integer17 > 0) {
            return DoorHingeSide.RIGHT;
        }
        if ((boolean19 && !boolean18) || integer17 < 0) {
            return DoorHingeSide.LEFT;
        }
        final int integer18 = gc5.getStepX();
        final int integer19 = gc5.getStepZ();
        final Vec3 dck22 = bnv.getClickLocation();
        final double double23 = dck22.x - fx4.getX();
        final double double24 = dck22.z - fx4.getZ();
        return ((integer18 < 0 && double24 < 0.5) || (integer18 > 0 && double24 > 0.5) || (integer19 < 0 && double23 > 0.5) || (integer19 > 0 && double23 < 0.5)) ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
    }
    
    @Override
    public InteractionResult use(BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (this.material == Material.METAL) {
            return InteractionResult.PASS;
        }
        cee = ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)DoorBlock.OPEN);
        bru.setBlock(fx, cee, 10);
        bru.levelEvent(bft, ((boolean)cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN)) ? this.getOpenSound() : this.getCloseSound(), fx, 0);
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    public boolean isOpen(final BlockState cee) {
        return cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN);
    }
    
    public void setOpen(final Level bru, final BlockState cee, final BlockPos fx, final boolean boolean4) {
        if (!cee.is(this) || cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN) == boolean4) {
            return;
        }
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DoorBlock.OPEN, boolean4), 10);
        this.playSound(bru, fx, boolean4);
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        final boolean boolean7 = bru.hasNeighborSignal(fx3) || bru.hasNeighborSignal(fx3.relative((cee.<DoubleBlockHalf>getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) ? Direction.UP : Direction.DOWN));
        if (bul != this && boolean7 != cee.<Boolean>getValue((Property<Boolean>)DoorBlock.POWERED)) {
            if (boolean7 != cee.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN)) {
                this.playSound(bru, fx3, boolean7);
            }
            bru.setBlock(fx3, (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)DoorBlock.POWERED, boolean7)).<Comparable, Boolean>setValue((Property<Comparable>)DoorBlock.OPEN, boolean7), 2);
        }
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        final BlockState cee2 = brw.getBlockState(fx2);
        if (cee.<DoubleBlockHalf>getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            return cee2.isFaceSturdy(brw, fx2, Direction.UP);
        }
        return cee2.is(this);
    }
    
    private void playSound(final Level bru, final BlockPos fx, final boolean boolean3) {
        bru.levelEvent(null, boolean3 ? this.getOpenSound() : this.getCloseSound(), fx, 0);
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.DESTROY;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)DoorBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)DoorBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        if (byd == Mirror.NONE) {
            return cee;
        }
        return ((StateHolder<O, BlockState>)cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)DoorBlock.FACING)))).<DoorHingeSide>cycle(DoorBlock.HINGE);
    }
    
    @Override
    public long getSeed(final BlockState cee, final BlockPos fx) {
        return Mth.getSeed(fx.getX(), fx.below((cee.<DoubleBlockHalf>getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) ? 1 : 0).getY(), fx.getZ());
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(DoorBlock.HALF, DoorBlock.FACING, DoorBlock.OPEN, DoorBlock.HINGE, DoorBlock.POWERED);
    }
    
    public static boolean isWoodenDoor(final Level bru, final BlockPos fx) {
        return isWoodenDoor(bru.getBlockState(fx));
    }
    
    public static boolean isWoodenDoor(final BlockState cee) {
        return cee.getBlock() instanceof DoorBlock && (cee.getMaterial() == Material.WOOD || cee.getMaterial() == Material.NETHER_WOOD);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        OPEN = BlockStateProperties.OPEN;
        HINGE = BlockStateProperties.DOOR_HINGE;
        POWERED = BlockStateProperties.POWERED;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
        NORTH_AABB = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
        WEST_AABB = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        EAST_AABB = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    }
}
