package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.stream.Collector;
import net.minecraft.Util;
import net.minecraft.world.level.block.state.StateDefinition;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.Shapes;
import com.google.common.collect.ImmutableMap;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class VineBlock extends Block {
    public static final BooleanProperty UP;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    private static final VoxelShape UP_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private final Map<BlockState, VoxelShape> shapesCache;
    
    public VineBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)VineBlock.UP, false)).setValue((Property<Comparable>)VineBlock.NORTH, false)).setValue((Property<Comparable>)VineBlock.EAST, false)).setValue((Property<Comparable>)VineBlock.SOUTH, false)).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.WEST, false));
        this.shapesCache = (Map<BlockState, VoxelShape>)ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), VineBlock::calculateShape)));
    }
    
    private static VoxelShape calculateShape(final BlockState cee) {
        VoxelShape dde2 = Shapes.empty();
        if (cee.<Boolean>getValue((Property<Boolean>)VineBlock.UP)) {
            dde2 = VineBlock.UP_AABB;
        }
        if (cee.<Boolean>getValue((Property<Boolean>)VineBlock.NORTH)) {
            dde2 = Shapes.or(dde2, VineBlock.NORTH_AABB);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)VineBlock.SOUTH)) {
            dde2 = Shapes.or(dde2, VineBlock.SOUTH_AABB);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)VineBlock.EAST)) {
            dde2 = Shapes.or(dde2, VineBlock.EAST_AABB);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)VineBlock.WEST)) {
            dde2 = Shapes.or(dde2, VineBlock.WEST_AABB);
        }
        return dde2;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)this.shapesCache.get(cee);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return this.hasFaces(this.getUpdatedState(cee, brw, fx));
    }
    
    private boolean hasFaces(final BlockState cee) {
        return this.countFaces(cee) > 0;
    }
    
    private int countFaces(final BlockState cee) {
        int integer3 = 0;
        for (final BooleanProperty cev5 : VineBlock.PROPERTY_BY_DIRECTION.values()) {
            if (cee.<Boolean>getValue((Property<Boolean>)cev5)) {
                ++integer3;
            }
        }
        return integer3;
    }
    
    private boolean canSupportAtFace(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (gc == Direction.DOWN) {
            return false;
        }
        final BlockPos fx2 = fx.relative(gc);
        if (isAcceptableNeighbour(bqz, fx2, gc)) {
            return true;
        }
        if (gc.getAxis() != Direction.Axis.Y) {
            final BooleanProperty cev6 = (BooleanProperty)VineBlock.PROPERTY_BY_DIRECTION.get(gc);
            final BlockState cee7 = bqz.getBlockState(fx.above());
            return cee7.is(this) && cee7.<Boolean>getValue((Property<Boolean>)cev6);
        }
        return false;
    }
    
    public static boolean isAcceptableNeighbour(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        final BlockState cee4 = bqz.getBlockState(fx);
        return Block.isFaceFull(cee4.getCollisionShape(bqz, fx), gc.getOpposite());
    }
    
    private BlockState getUpdatedState(BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        final BlockPos fx2 = fx.above();
        if (cee.<Boolean>getValue((Property<Boolean>)VineBlock.UP)) {
            cee = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.UP, isAcceptableNeighbour(bqz, fx2, Direction.DOWN));
        }
        BlockState cee2 = null;
        for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
            final BooleanProperty cev9 = getPropertyForFace(gc8);
            if (cee.<Boolean>getValue((Property<Boolean>)cev9)) {
                boolean boolean10 = this.canSupportAtFace(bqz, fx, gc8);
                if (!boolean10) {
                    if (cee2 == null) {
                        cee2 = bqz.getBlockState(fx2);
                    }
                    boolean10 = (cee2.is(this) && cee2.<Boolean>getValue((Property<Boolean>)cev9));
                }
                cee = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)cev9, boolean10);
            }
        }
        return cee;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN) {
            return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        }
        final BlockState cee4 = this.getUpdatedState(cee1, brv, fx5);
        if (!this.hasFaces(cee4)) {
            return Blocks.AIR.defaultBlockState();
        }
        return cee4;
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.random.nextInt(4) != 0) {
            return;
        }
        final Direction gc6 = Direction.getRandom(random);
        final BlockPos fx2 = fx.above();
        if (!gc6.getAxis().isHorizontal() || cee.<Boolean>getValue((Property<Boolean>)getPropertyForFace(gc6))) {
            if (gc6 == Direction.UP && fx.getY() < 255) {
                if (this.canSupportAtFace(aag, fx, gc6)) {
                    aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.UP, true), 2);
                    return;
                }
                if (aag.isEmptyBlock(fx2)) {
                    if (!this.canSpread(aag, fx)) {
                        return;
                    }
                    BlockState cee2 = cee;
                    for (final Direction gc7 : Direction.Plane.HORIZONTAL) {
                        if (random.nextBoolean() || !isAcceptableNeighbour(aag, fx2.relative(gc7), Direction.UP)) {
                            cee2 = ((StateHolder<O, BlockState>)cee2).<Comparable, Boolean>setValue((Property<Comparable>)getPropertyForFace(gc7), false);
                        }
                    }
                    if (this.hasHorizontalConnection(cee2)) {
                        aag.setBlock(fx2, cee2, 2);
                    }
                    return;
                }
            }
            if (fx.getY() > 0) {
                final BlockPos fx3 = fx.below();
                final BlockState cee3 = aag.getBlockState(fx3);
                if (cee3.isAir() || cee3.is(this)) {
                    final BlockState cee4 = cee3.isAir() ? this.defaultBlockState() : cee3;
                    final BlockState cee5 = this.copyRandomFaces(cee, cee4, random);
                    if (cee4 != cee5 && this.hasHorizontalConnection(cee5)) {
                        aag.setBlock(fx3, cee5, 2);
                    }
                }
            }
            return;
        }
        if (!this.canSpread(aag, fx)) {
            return;
        }
        final BlockPos fx3 = fx.relative(gc6);
        final BlockState cee3 = aag.getBlockState(fx3);
        if (cee3.isAir()) {
            final Direction gc7 = gc6.getClockWise();
            final Direction gc8 = gc6.getCounterClockWise();
            final boolean boolean12 = cee.<Boolean>getValue((Property<Boolean>)getPropertyForFace(gc7));
            final boolean boolean13 = cee.<Boolean>getValue((Property<Boolean>)getPropertyForFace(gc8));
            final BlockPos fx4 = fx3.relative(gc7);
            final BlockPos fx5 = fx3.relative(gc8);
            if (boolean12 && isAcceptableNeighbour(aag, fx4, gc7)) {
                aag.setBlock(fx3, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)getPropertyForFace(gc7), true), 2);
            }
            else if (boolean13 && isAcceptableNeighbour(aag, fx5, gc8)) {
                aag.setBlock(fx3, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)getPropertyForFace(gc8), true), 2);
            }
            else {
                final Direction gc9 = gc6.getOpposite();
                if (boolean12 && aag.isEmptyBlock(fx4) && isAcceptableNeighbour(aag, fx.relative(gc7), gc9)) {
                    aag.setBlock(fx4, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)getPropertyForFace(gc9), true), 2);
                }
                else if (boolean13 && aag.isEmptyBlock(fx5) && isAcceptableNeighbour(aag, fx.relative(gc8), gc9)) {
                    aag.setBlock(fx5, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)getPropertyForFace(gc9), true), 2);
                }
                else if (aag.random.nextFloat() < 0.05 && isAcceptableNeighbour(aag, fx3.above(), Direction.UP)) {
                    aag.setBlock(fx3, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.UP, true), 2);
                }
            }
        }
        else if (isAcceptableNeighbour(aag, fx3, gc6)) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)getPropertyForFace(gc6), true), 2);
        }
    }
    
    private BlockState copyRandomFaces(final BlockState cee1, BlockState cee2, final Random random) {
        for (final Direction gc6 : Direction.Plane.HORIZONTAL) {
            if (random.nextBoolean()) {
                final BooleanProperty cev7 = getPropertyForFace(gc6);
                if (!cee1.<Boolean>getValue((Property<Boolean>)cev7)) {
                    continue;
                }
                cee2 = ((StateHolder<O, BlockState>)cee2).<Comparable, Boolean>setValue((Property<Comparable>)cev7, true);
            }
        }
        return cee2;
    }
    
    private boolean hasHorizontalConnection(final BlockState cee) {
        return cee.<Boolean>getValue((Property<Boolean>)VineBlock.NORTH) || cee.<Boolean>getValue((Property<Boolean>)VineBlock.EAST) || cee.<Boolean>getValue((Property<Boolean>)VineBlock.SOUTH) || cee.<Boolean>getValue((Property<Boolean>)VineBlock.WEST);
    }
    
    private boolean canSpread(final BlockGetter bqz, final BlockPos fx) {
        final int integer4 = 4;
        final Iterable<BlockPos> iterable5 = BlockPos.betweenClosed(fx.getX() - 4, fx.getY() - 1, fx.getZ() - 4, fx.getX() + 4, fx.getY() + 1, fx.getZ() + 4);
        int integer5 = 5;
        for (final BlockPos fx2 : iterable5) {
            if (bqz.getBlockState(fx2).is(this) && --integer5 <= 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        final BlockState cee2 = bnv.getLevel().getBlockState(bnv.getClickedPos());
        if (cee2.is(this)) {
            return this.countFaces(cee2) < VineBlock.PROPERTY_BY_DIRECTION.size();
        }
        return super.canBeReplaced(cee, bnv);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = bnv.getLevel().getBlockState(bnv.getClickedPos());
        final boolean boolean4 = cee3.is(this);
        final BlockState cee4 = boolean4 ? cee3 : this.defaultBlockState();
        for (final Direction gc9 : bnv.getNearestLookingDirections()) {
            if (gc9 != Direction.DOWN) {
                final BooleanProperty cev10 = getPropertyForFace(gc9);
                final boolean boolean5 = boolean4 && cee3.<Boolean>getValue((Property<Boolean>)cev10);
                if (!boolean5 && this.canSupportAtFace(bnv.getLevel(), bnv.getClickedPos(), gc9)) {
                    return ((StateHolder<O, BlockState>)cee4).<Comparable, Boolean>setValue((Property<Comparable>)cev10, true);
                }
            }
        }
        return boolean4 ? cee4 : null;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(VineBlock.UP, VineBlock.NORTH, VineBlock.EAST, VineBlock.SOUTH, VineBlock.WEST);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case CLOCKWISE_180: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)VineBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.SOUTH))).setValue((Property<Comparable>)VineBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.WEST))).setValue((Property<Comparable>)VineBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.NORTH))).<Comparable, Comparable>setValue((Property<Comparable>)VineBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)VineBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.EAST))).setValue((Property<Comparable>)VineBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.SOUTH))).setValue((Property<Comparable>)VineBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.WEST))).<Comparable, Comparable>setValue((Property<Comparable>)VineBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.NORTH));
            }
            case CLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)VineBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.WEST))).setValue((Property<Comparable>)VineBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.NORTH))).setValue((Property<Comparable>)VineBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.EAST))).<Comparable, Comparable>setValue((Property<Comparable>)VineBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.SOUTH));
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
                return (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)VineBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.SOUTH))).<Comparable, Comparable>setValue((Property<Comparable>)VineBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)VineBlock.NORTH));
            }
            case FRONT_BACK: {
                return (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)VineBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.WEST))).<Comparable, Comparable>setValue((Property<Comparable>)VineBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)VineBlock.EAST));
            }
            default: {
                return super.mirror(cee, byd);
            }
        }
    }
    
    public static BooleanProperty getPropertyForFace(final Direction gc) {
        return (BooleanProperty)VineBlock.PROPERTY_BY_DIRECTION.get(gc);
    }
    
    static {
        UP = PipeBlock.UP;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        PROPERTY_BY_DIRECTION = (Map)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(entry -> entry.getKey() != Direction.DOWN).collect((Collector)Util.toMap());
        UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
        EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    }
}
