package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class HopperBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty ENABLED;
    private static final VoxelShape TOP;
    private static final VoxelShape FUNNEL;
    private static final VoxelShape CONVEX_BASE;
    private static final VoxelShape BASE;
    private static final VoxelShape DOWN_SHAPE;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape WEST_SHAPE;
    private static final VoxelShape DOWN_INTERACTION_SHAPE;
    private static final VoxelShape EAST_INTERACTION_SHAPE;
    private static final VoxelShape NORTH_INTERACTION_SHAPE;
    private static final VoxelShape SOUTH_INTERACTION_SHAPE;
    private static final VoxelShape WEST_INTERACTION_SHAPE;
    
    public HopperBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)HopperBlock.FACING, Direction.DOWN)).<Comparable, Boolean>setValue((Property<Comparable>)HopperBlock.ENABLED, true));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction>getValue((Property<Direction>)HopperBlock.FACING)) {
            case DOWN: {
                return HopperBlock.DOWN_SHAPE;
            }
            case NORTH: {
                return HopperBlock.NORTH_SHAPE;
            }
            case SOUTH: {
                return HopperBlock.SOUTH_SHAPE;
            }
            case WEST: {
                return HopperBlock.WEST_SHAPE;
            }
            case EAST: {
                return HopperBlock.EAST_SHAPE;
            }
            default: {
                return HopperBlock.BASE;
            }
        }
    }
    
    @Override
    public VoxelShape getInteractionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        switch (cee.<Direction>getValue((Property<Direction>)HopperBlock.FACING)) {
            case DOWN: {
                return HopperBlock.DOWN_INTERACTION_SHAPE;
            }
            case NORTH: {
                return HopperBlock.NORTH_INTERACTION_SHAPE;
            }
            case SOUTH: {
                return HopperBlock.SOUTH_INTERACTION_SHAPE;
            }
            case WEST: {
                return HopperBlock.WEST_INTERACTION_SHAPE;
            }
            case EAST: {
                return HopperBlock.EAST_INTERACTION_SHAPE;
            }
            default: {
                return Hopper.INSIDE;
            }
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Direction gc3 = bnv.getClickedFace().getOpposite();
        return (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)HopperBlock.FACING, (gc3.getAxis() == Direction.Axis.Y) ? Direction.DOWN : gc3)).<Comparable, Boolean>setValue((Property<Comparable>)HopperBlock.ENABLED, true);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new HopperBlockEntity();
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof HopperBlockEntity) {
                ((HopperBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        this.checkPoweredState(bru, fx, cee1);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof HopperBlockEntity) {
            bft.openMenu((MenuProvider)ccg8);
            bft.awardStat(Stats.INSPECT_HOPPER);
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        this.checkPoweredState(bru, fx3, cee);
    }
    
    private void checkPoweredState(final Level bru, final BlockPos fx, final BlockState cee) {
        final boolean boolean5 = !bru.hasNeighborSignal(fx);
        if (boolean5 != cee.<Boolean>getValue((Property<Boolean>)HopperBlock.ENABLED)) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)HopperBlock.ENABLED, boolean5), 4);
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof HopperBlockEntity) {
            Containers.dropContents(bru, fx, (Container)ccg7);
            bru.updateNeighbourForOutputSignal(fx, this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(bru.getBlockEntity(fx));
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)HopperBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)HopperBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)HopperBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(HopperBlock.FACING, HopperBlock.ENABLED);
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        final BlockEntity ccg6 = bru.getBlockEntity(fx);
        if (ccg6 instanceof HopperBlockEntity) {
            ((HopperBlockEntity)ccg6).entityInside(apx);
        }
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = BlockStateProperties.FACING_HOPPER;
        ENABLED = BlockStateProperties.ENABLED;
        TOP = Block.box(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);
        FUNNEL = Block.box(4.0, 4.0, 4.0, 12.0, 10.0, 12.0);
        CONVEX_BASE = Shapes.or(HopperBlock.FUNNEL, HopperBlock.TOP);
        BASE = Shapes.join(HopperBlock.CONVEX_BASE, Hopper.INSIDE, BooleanOp.ONLY_FIRST);
        DOWN_SHAPE = Shapes.or(HopperBlock.BASE, Block.box(6.0, 0.0, 6.0, 10.0, 4.0, 10.0));
        EAST_SHAPE = Shapes.or(HopperBlock.BASE, Block.box(12.0, 4.0, 6.0, 16.0, 8.0, 10.0));
        NORTH_SHAPE = Shapes.or(HopperBlock.BASE, Block.box(6.0, 4.0, 0.0, 10.0, 8.0, 4.0));
        SOUTH_SHAPE = Shapes.or(HopperBlock.BASE, Block.box(6.0, 4.0, 12.0, 10.0, 8.0, 16.0));
        WEST_SHAPE = Shapes.or(HopperBlock.BASE, Block.box(0.0, 4.0, 6.0, 4.0, 8.0, 10.0));
        DOWN_INTERACTION_SHAPE = Hopper.INSIDE;
        EAST_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(12.0, 8.0, 6.0, 16.0, 10.0, 10.0));
        NORTH_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(6.0, 8.0, 0.0, 10.0, 10.0, 4.0));
        SOUTH_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(6.0, 8.0, 12.0, 10.0, 10.0, 16.0));
        WEST_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(0.0, 8.0, 6.0, 4.0, 10.0, 10.0));
    }
}
