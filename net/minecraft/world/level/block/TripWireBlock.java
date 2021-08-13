package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.Vec3i;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TripWireBlock extends Block {
    public static final BooleanProperty POWERED;
    public static final BooleanProperty ATTACHED;
    public static final BooleanProperty DISARMED;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    protected static final VoxelShape AABB;
    protected static final VoxelShape NOT_ATTACHED_AABB;
    private final TripWireHookBlock hook;
    
    public TripWireBlock(final TripWireHookBlock cbb, final Properties c) {
        super(c);
        this.registerDefaultState(((((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)TripWireBlock.POWERED, false)).setValue((Property<Comparable>)TripWireBlock.ATTACHED, false)).setValue((Property<Comparable>)TripWireBlock.DISARMED, false)).setValue((Property<Comparable>)TripWireBlock.NORTH, false)).setValue((Property<Comparable>)TripWireBlock.EAST, false)).setValue((Property<Comparable>)TripWireBlock.SOUTH, false)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.WEST, false));
        this.hook = cbb;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return cee.<Boolean>getValue((Property<Boolean>)TripWireBlock.ATTACHED) ? TripWireBlock.AABB : TripWireBlock.NOT_ATTACHED_AABB;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockGetter bqz3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        return (((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)TripWireBlock.NORTH, this.shouldConnectTo(bqz3.getBlockState(fx4.north()), Direction.NORTH))).setValue((Property<Comparable>)TripWireBlock.EAST, this.shouldConnectTo(bqz3.getBlockState(fx4.east()), Direction.EAST))).setValue((Property<Comparable>)TripWireBlock.SOUTH, this.shouldConnectTo(bqz3.getBlockState(fx4.south()), Direction.SOUTH))).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.WEST, this.shouldConnectTo(bqz3.getBlockState(fx4.west()), Direction.WEST));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getAxis().isHorizontal()) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.PROPERTY_BY_DIRECTION.get(gc), this.shouldConnectTo(cee3, gc));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        this.updateSource(bru, fx, cee1);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        this.updateSource(bru, fx, ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.POWERED, true));
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide && !bft.getMainHandItem().isEmpty() && bft.getMainHandItem().getItem() == Items.SHEARS) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.DISARMED, true), 4);
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    private void updateSource(final Level bru, final BlockPos fx, final BlockState cee) {
        for (final Direction gc8 : new Direction[] { Direction.SOUTH, Direction.WEST }) {
            int integer9 = 1;
            while (integer9 < 42) {
                final BlockPos fx2 = fx.relative(gc8, integer9);
                final BlockState cee2 = bru.getBlockState(fx2);
                if (cee2.is(this.hook)) {
                    if (cee2.<Comparable>getValue((Property<Comparable>)TripWireHookBlock.FACING) == gc8.getOpposite()) {
                        this.hook.calculateState(bru, fx2, cee2, false, true, integer9, cee);
                        break;
                    }
                    break;
                }
                else {
                    if (!cee2.is(this)) {
                        break;
                    }
                    ++integer9;
                }
            }
        }
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (bru.isClientSide) {
            return;
        }
        if (cee.<Boolean>getValue((Property<Boolean>)TripWireBlock.POWERED)) {
            return;
        }
        this.checkPressed(bru, fx);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!aag.getBlockState(fx).<Boolean>getValue((Property<Boolean>)TripWireBlock.POWERED)) {
            return;
        }
        this.checkPressed(aag, fx);
    }
    
    private void checkPressed(final Level bru, final BlockPos fx) {
        BlockState cee4 = bru.getBlockState(fx);
        final boolean boolean5 = cee4.<Boolean>getValue((Property<Boolean>)TripWireBlock.POWERED);
        boolean boolean6 = false;
        final List<? extends Entity> list7 = bru.getEntities(null, cee4.getShape(bru, fx).bounds().move(fx));
        if (!list7.isEmpty()) {
            for (final Entity apx9 : list7) {
                if (!apx9.isIgnoringBlockTriggers()) {
                    boolean6 = true;
                    break;
                }
            }
        }
        if (boolean6 != boolean5) {
            cee4 = ((StateHolder<O, BlockState>)cee4).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.POWERED, boolean6);
            bru.setBlock(fx, cee4, 3);
            this.updateSource(bru, fx, cee4);
        }
        if (boolean6) {
            bru.getBlockTicks().scheduleTick(new BlockPos(fx), this, 10);
        }
    }
    
    public boolean shouldConnectTo(final BlockState cee, final Direction gc) {
        final Block bul4 = cee.getBlock();
        if (bul4 == this.hook) {
            return cee.<Comparable>getValue((Property<Comparable>)TripWireHookBlock.FACING) == gc.getOpposite();
        }
        return bul4 == this;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case CLOCKWISE_180: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)TripWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.SOUTH))).setValue((Property<Comparable>)TripWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.WEST))).setValue((Property<Comparable>)TripWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.NORTH))).<Comparable, Comparable>setValue((Property<Comparable>)TripWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)TripWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.EAST))).setValue((Property<Comparable>)TripWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.SOUTH))).setValue((Property<Comparable>)TripWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.WEST))).<Comparable, Comparable>setValue((Property<Comparable>)TripWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.NORTH));
            }
            case CLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)TripWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.WEST))).setValue((Property<Comparable>)TripWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.NORTH))).setValue((Property<Comparable>)TripWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.EAST))).<Comparable, Comparable>setValue((Property<Comparable>)TripWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.SOUTH));
            }
            default: {
                return cee;
            }
        }
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        switch (byd) {
            case LEFT_RIGHT: {
                return (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)TripWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.SOUTH))).<Comparable, Comparable>setValue((Property<Comparable>)TripWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.NORTH));
            }
            case FRONT_BACK: {
                return (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)TripWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.WEST))).<Comparable, Comparable>setValue((Property<Comparable>)TripWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)TripWireBlock.EAST));
            }
            default: {
                return super.mirror(cee, byd);
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(TripWireBlock.POWERED, TripWireBlock.ATTACHED, TripWireBlock.DISARMED, TripWireBlock.NORTH, TripWireBlock.EAST, TripWireBlock.WEST, TripWireBlock.SOUTH);
    }
    
    static {
        POWERED = BlockStateProperties.POWERED;
        ATTACHED = BlockStateProperties.ATTACHED;
        DISARMED = BlockStateProperties.DISARMED;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        PROPERTY_BY_DIRECTION = CrossCollisionBlock.PROPERTY_BY_DIRECTION;
        AABB = Block.box(0.0, 1.0, 0.0, 16.0, 2.5, 16.0);
        NOT_ATTACHED_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
