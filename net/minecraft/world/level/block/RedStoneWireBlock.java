package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.DustParticleOptions;
import java.util.Random;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import java.util.Iterator;
import net.minecraft.world.phys.shapes.Shapes;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class RedStoneWireBlock extends Block {
    public static final EnumProperty<RedstoneSide> NORTH;
    public static final EnumProperty<RedstoneSide> EAST;
    public static final EnumProperty<RedstoneSide> SOUTH;
    public static final EnumProperty<RedstoneSide> WEST;
    public static final IntegerProperty POWER;
    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION;
    private static final VoxelShape SHAPE_DOT;
    private static final Map<Direction, VoxelShape> SHAPES_FLOOR;
    private static final Map<Direction, VoxelShape> SHAPES_UP;
    private final Map<BlockState, VoxelShape> SHAPES_CACHE;
    private static final Vector3f[] COLORS;
    private final BlockState crossState;
    private boolean shouldSignal;
    
    public RedStoneWireBlock(final Properties c) {
        super(c);
        this.SHAPES_CACHE = (Map<BlockState, VoxelShape>)Maps.newHashMap();
        this.shouldSignal = true;
        this.registerDefaultState(((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue(RedStoneWireBlock.NORTH, RedstoneSide.NONE)).setValue(RedStoneWireBlock.EAST, RedstoneSide.NONE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.NONE)).setValue(RedStoneWireBlock.WEST, RedstoneSide.NONE)).<Comparable, Integer>setValue((Property<Comparable>)RedStoneWireBlock.POWER, 0));
        this.crossState = (((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE);
        for (final BlockState cee4 : this.getStateDefinition().getPossibleStates()) {
            if (cee4.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER) == 0) {
                this.SHAPES_CACHE.put(cee4, this.calculateShape(cee4));
            }
        }
    }
    
    private VoxelShape calculateShape(final BlockState cee) {
        VoxelShape dde3 = RedStoneWireBlock.SHAPE_DOT;
        for (final Direction gc5 : Direction.Plane.HORIZONTAL) {
            final RedstoneSide cfi6 = cee.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc5));
            if (cfi6 == RedstoneSide.SIDE) {
                dde3 = Shapes.or(dde3, (VoxelShape)RedStoneWireBlock.SHAPES_FLOOR.get(gc5));
            }
            else {
                if (cfi6 != RedstoneSide.UP) {
                    continue;
                }
                dde3 = Shapes.or(dde3, (VoxelShape)RedStoneWireBlock.SHAPES_UP.get(gc5));
            }
        }
        return dde3;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)this.SHAPES_CACHE.get(((StateHolder<O, Object>)cee).<Comparable, Integer>setValue((Property<Comparable>)RedStoneWireBlock.POWER, 0));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return this.getConnectionState(bnv.getLevel(), this.crossState, bnv.getClickedPos());
    }
    
    private BlockState getConnectionState(final BlockGetter bqz, BlockState cee, final BlockPos fx) {
        final boolean boolean5 = isDot(cee);
        cee = this.getMissingConnections(bqz, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Comparable>setValue((Property<Comparable>)RedStoneWireBlock.POWER, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.POWER)), fx);
        if (boolean5 && isDot(cee)) {
            return cee;
        }
        final boolean boolean6 = cee.<RedstoneSide>getValue(RedStoneWireBlock.NORTH).isConnected();
        final boolean boolean7 = cee.<RedstoneSide>getValue(RedStoneWireBlock.SOUTH).isConnected();
        final boolean boolean8 = cee.<RedstoneSide>getValue(RedStoneWireBlock.EAST).isConnected();
        final boolean boolean9 = cee.<RedstoneSide>getValue(RedStoneWireBlock.WEST).isConnected();
        final boolean boolean10 = !boolean6 && !boolean7;
        final boolean boolean11 = !boolean8 && !boolean9;
        if (!boolean9 && boolean10) {
            cee = ((StateHolder<O, BlockState>)cee).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE);
        }
        if (!boolean8 && boolean10) {
            cee = ((StateHolder<O, BlockState>)cee).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE);
        }
        if (!boolean6 && boolean11) {
            cee = ((StateHolder<O, BlockState>)cee).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE);
        }
        if (!boolean7 && boolean11) {
            cee = ((StateHolder<O, BlockState>)cee).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE);
        }
        return cee;
    }
    
    private BlockState getMissingConnections(final BlockGetter bqz, BlockState cee, final BlockPos fx) {
        final boolean boolean5 = !bqz.getBlockState(fx.above()).isRedstoneConductor(bqz, fx);
        for (final Direction gc7 : Direction.Plane.HORIZONTAL) {
            if (!cee.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc7)).isConnected()) {
                final RedstoneSide cfi8 = this.getConnectingSide(bqz, fx, gc7, boolean5);
                cee = ((StateHolder<O, BlockState>)cee).<Comparable, RedstoneSide>setValue((Property<Comparable>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc7), cfi8);
            }
        }
        return cee;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN) {
            return cee1;
        }
        if (gc == Direction.UP) {
            return this.getConnectionState(brv, cee1, fx5);
        }
        final RedstoneSide cfi8 = this.getConnectingSide(brv, fx5, gc);
        if (cfi8.isConnected() == cee1.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc)).isConnected() && !isCross(cee1)) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, RedstoneSide>setValue((Property<Comparable>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc), cfi8);
        }
        return this.getConnectionState(brv, (((StateHolder<O, BlockState>)this.crossState).setValue((Property<Comparable>)RedStoneWireBlock.POWER, (Comparable)cee1.<V>getValue((Property<V>)RedStoneWireBlock.POWER))).<Comparable, RedstoneSide>setValue((Property<Comparable>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc), cfi8), fx5);
    }
    
    private static boolean isCross(final BlockState cee) {
        return cee.<RedstoneSide>getValue(RedStoneWireBlock.NORTH).isConnected() && cee.<RedstoneSide>getValue(RedStoneWireBlock.SOUTH).isConnected() && cee.<RedstoneSide>getValue(RedStoneWireBlock.EAST).isConnected() && cee.<RedstoneSide>getValue(RedStoneWireBlock.WEST).isConnected();
    }
    
    private static boolean isDot(final BlockState cee) {
        return !cee.<RedstoneSide>getValue(RedStoneWireBlock.NORTH).isConnected() && !cee.<RedstoneSide>getValue(RedStoneWireBlock.SOUTH).isConnected() && !cee.<RedstoneSide>getValue(RedStoneWireBlock.EAST).isConnected() && !cee.<RedstoneSide>getValue(RedStoneWireBlock.WEST).isConnected();
    }
    
    @Override
    public void updateIndirectNeighbourShapes(final BlockState cee, final LevelAccessor brv, final BlockPos fx, final int integer4, final int integer5) {
        final BlockPos.MutableBlockPos a7 = new BlockPos.MutableBlockPos();
        for (final Direction gc9 : Direction.Plane.HORIZONTAL) {
            final RedstoneSide cfi10 = cee.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc9));
            if (cfi10 != RedstoneSide.NONE && !brv.getBlockState(a7.setWithOffset(fx, gc9)).is(this)) {
                a7.move(Direction.DOWN);
                final BlockState cee2 = brv.getBlockState(a7);
                if (!cee2.is(Blocks.OBSERVER)) {
                    final BlockPos fx2 = a7.relative(gc9.getOpposite());
                    final BlockState cee3 = cee2.updateShape(gc9.getOpposite(), brv.getBlockState(fx2), brv, a7, fx2);
                    Block.updateOrDestroy(cee2, cee3, brv, a7, integer4, integer5);
                }
                a7.setWithOffset(fx, gc9).move(Direction.UP);
                final BlockState cee4 = brv.getBlockState(a7);
                if (cee4.is(Blocks.OBSERVER)) {
                    continue;
                }
                final BlockPos fx3 = a7.relative(gc9.getOpposite());
                final BlockState cee5 = cee4.updateShape(gc9.getOpposite(), brv.getBlockState(fx3), brv, a7, fx3);
                Block.updateOrDestroy(cee4, cee5, brv, a7, integer4, integer5);
            }
        }
    }
    
    private RedstoneSide getConnectingSide(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return this.getConnectingSide(bqz, fx, gc, !bqz.getBlockState(fx.above()).isRedstoneConductor(bqz, fx));
    }
    
    private RedstoneSide getConnectingSide(final BlockGetter bqz, final BlockPos fx, final Direction gc, final boolean boolean4) {
        final BlockPos fx2 = fx.relative(gc);
        final BlockState cee7 = bqz.getBlockState(fx2);
        if (boolean4) {
            final boolean boolean5 = this.canSurviveOn(bqz, fx2, cee7);
            if (boolean5 && shouldConnectTo(bqz.getBlockState(fx2.above()))) {
                if (cee7.isFaceSturdy(bqz, fx2, gc.getOpposite())) {
                    return RedstoneSide.UP;
                }
                return RedstoneSide.SIDE;
            }
        }
        if (shouldConnectTo(cee7, gc) || (!cee7.isRedstoneConductor(bqz, fx2) && shouldConnectTo(bqz.getBlockState(fx2.below())))) {
            return RedstoneSide.SIDE;
        }
        return RedstoneSide.NONE;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        final BlockState cee2 = brw.getBlockState(fx2);
        return this.canSurviveOn(brw, fx2, cee2);
    }
    
    private boolean canSurviveOn(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return cee.isFaceSturdy(bqz, fx, Direction.UP) || cee.is(Blocks.HOPPER);
    }
    
    private void updatePowerStrength(final Level bru, final BlockPos fx, final BlockState cee) {
        final int integer5 = this.calculateTargetStrength(bru, fx);
        if (cee.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER) != integer5) {
            if (bru.getBlockState(fx) == cee) {
                bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)RedStoneWireBlock.POWER, integer5), 2);
            }
            final Set<BlockPos> set6 = (Set<BlockPos>)Sets.newHashSet();
            set6.add(fx);
            for (final Direction gc10 : Direction.values()) {
                set6.add(fx.relative(gc10));
            }
            for (final BlockPos fx2 : set6) {
                bru.updateNeighborsAt(fx2, this);
            }
        }
    }
    
    private int calculateTargetStrength(final Level bru, final BlockPos fx) {
        this.shouldSignal = false;
        final int integer4 = bru.getBestNeighborSignal(fx);
        this.shouldSignal = true;
        int integer5 = 0;
        if (integer4 < 15) {
            for (final Direction gc7 : Direction.Plane.HORIZONTAL) {
                final BlockPos fx2 = fx.relative(gc7);
                final BlockState cee9 = bru.getBlockState(fx2);
                integer5 = Math.max(integer5, this.getWireSignal(cee9));
                final BlockPos fx3 = fx.above();
                if (cee9.isRedstoneConductor(bru, fx2) && !bru.getBlockState(fx3).isRedstoneConductor(bru, fx3)) {
                    integer5 = Math.max(integer5, this.getWireSignal(bru.getBlockState(fx2.above())));
                }
                else {
                    if (cee9.isRedstoneConductor(bru, fx2)) {
                        continue;
                    }
                    integer5 = Math.max(integer5, this.getWireSignal(bru.getBlockState(fx2.below())));
                }
            }
        }
        return Math.max(integer4, integer5 - 1);
    }
    
    private int getWireSignal(final BlockState cee) {
        return cee.is(this) ? cee.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER) : 0;
    }
    
    private void checkCornerChangeAt(final Level bru, final BlockPos fx) {
        if (!bru.getBlockState(fx).is(this)) {
            return;
        }
        bru.updateNeighborsAt(fx, this);
        for (final Direction gc7 : Direction.values()) {
            bru.updateNeighborsAt(fx.relative(gc7), this);
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock()) || bru.isClientSide) {
            return;
        }
        this.updatePowerStrength(bru, fx, cee1);
        for (final Direction gc8 : Direction.Plane.VERTICAL) {
            bru.updateNeighborsAt(fx.relative(gc8), this);
        }
        this.updateNeighborsOfNeighboringWires(bru, fx);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
        if (bru.isClientSide) {
            return;
        }
        for (final Direction gc10 : Direction.values()) {
            bru.updateNeighborsAt(fx.relative(gc10), this);
        }
        this.updatePowerStrength(bru, fx, cee1);
        this.updateNeighborsOfNeighboringWires(bru, fx);
    }
    
    private void updateNeighborsOfNeighboringWires(final Level bru, final BlockPos fx) {
        for (final Direction gc5 : Direction.Plane.HORIZONTAL) {
            this.checkCornerChangeAt(bru, fx.relative(gc5));
        }
        for (final Direction gc5 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc5);
            if (bru.getBlockState(fx2).isRedstoneConductor(bru, fx2)) {
                this.checkCornerChangeAt(bru, fx2.above());
            }
            else {
                this.checkCornerChangeAt(bru, fx2.below());
            }
        }
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.isClientSide) {
            return;
        }
        if (cee.canSurvive(bru, fx3)) {
            this.updatePowerStrength(bru, fx3, cee);
        }
        else {
            Block.dropResources(cee, bru, fx3);
            bru.removeBlock(fx3, false);
        }
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (!this.shouldSignal) {
            return 0;
        }
        return cee.getSignal(bqz, fx, gc);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (!this.shouldSignal || gc == Direction.DOWN) {
            return 0;
        }
        final int integer6 = cee.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER);
        if (integer6 == 0) {
            return 0;
        }
        if (gc == Direction.UP || this.getConnectionState(bqz, cee, fx).<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc.getOpposite())).isConnected()) {
            return integer6;
        }
        return 0;
    }
    
    protected static boolean shouldConnectTo(final BlockState cee) {
        return shouldConnectTo(cee, null);
    }
    
    protected static boolean shouldConnectTo(final BlockState cee, @Nullable final Direction gc) {
        if (cee.is(Blocks.REDSTONE_WIRE)) {
            return true;
        }
        if (cee.is(Blocks.REPEATER)) {
            final Direction gc2 = cee.<Direction>getValue((Property<Direction>)RepeaterBlock.FACING);
            return gc2 == gc || gc2.getOpposite() == gc;
        }
        if (cee.is(Blocks.OBSERVER)) {
            return gc == cee.<Comparable>getValue((Property<Comparable>)ObserverBlock.FACING);
        }
        return cee.isSignalSource() && gc != null;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return this.shouldSignal;
    }
    
    public static int getColorForPower(final int integer) {
        final Vector3f g2 = RedStoneWireBlock.COLORS[integer];
        return Mth.color(g2.x(), g2.y(), g2.z());
    }
    
    private void spawnParticlesAlongLine(final Level bru, final Random random, final BlockPos fx, final Vector3f g, final Direction gc5, final Direction gc6, final float float7, final float float8) {
        final float float9 = float8 - float7;
        if (random.nextFloat() >= 0.2f * float9) {
            return;
        }
        final float float10 = 0.4375f;
        final float float11 = float7 + float9 * random.nextFloat();
        final double double13 = 0.5 + 0.4375f * gc5.getStepX() + float11 * gc6.getStepX();
        final double double14 = 0.5 + 0.4375f * gc5.getStepY() + float11 * gc6.getStepY();
        final double double15 = 0.5 + 0.4375f * gc5.getStepZ() + float11 * gc6.getStepZ();
        bru.addParticle(new DustParticleOptions(g.x(), g.y(), g.z(), 1.0f), fx.getX() + double13, fx.getY() + double14, fx.getZ() + double15, 0.0, 0.0, 0.0);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final int integer6 = cee.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER);
        if (integer6 == 0) {
            return;
        }
        for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
            final RedstoneSide cfi9 = cee.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc8));
            switch (cfi9) {
                case UP: {
                    this.spawnParticlesAlongLine(bru, random, fx, RedStoneWireBlock.COLORS[integer6], gc8, Direction.UP, -0.5f, 0.5f);
                }
                case SIDE: {
                    this.spawnParticlesAlongLine(bru, random, fx, RedStoneWireBlock.COLORS[integer6], Direction.DOWN, gc8, 0.0f, 0.5f);
                    continue;
                }
                default: {
                    this.spawnParticlesAlongLine(bru, random, fx, RedStoneWireBlock.COLORS[integer6], Direction.DOWN, gc8, 0.0f, 0.3f);
                    continue;
                }
            }
        }
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case CLOCKWISE_180: {
                return (((((StateHolder<O, BlockState>)cee).setValue(RedStoneWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.SOUTH))).setValue(RedStoneWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.WEST))).setValue(RedStoneWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.NORTH))).<RedstoneSide, Comparable>setValue(RedStoneWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue(RedStoneWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.EAST))).setValue(RedStoneWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.SOUTH))).setValue(RedStoneWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.WEST))).<RedstoneSide, Comparable>setValue(RedStoneWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.NORTH));
            }
            case CLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue(RedStoneWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.WEST))).setValue(RedStoneWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.NORTH))).setValue(RedStoneWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.EAST))).<RedstoneSide, Comparable>setValue(RedStoneWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.SOUTH));
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
                return (((StateHolder<O, BlockState>)cee).setValue(RedStoneWireBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.SOUTH))).<RedstoneSide, Comparable>setValue(RedStoneWireBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.NORTH));
            }
            case FRONT_BACK: {
                return (((StateHolder<O, BlockState>)cee).setValue(RedStoneWireBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.WEST))).<RedstoneSide, Comparable>setValue(RedStoneWireBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.EAST));
            }
            default: {
                return super.mirror(cee, byd);
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RedStoneWireBlock.NORTH, RedStoneWireBlock.EAST, RedStoneWireBlock.SOUTH, RedStoneWireBlock.WEST, RedStoneWireBlock.POWER);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (!bft.abilities.mayBuild) {
            return InteractionResult.PASS;
        }
        if (isCross(cee) || isDot(cee)) {
            BlockState cee2 = isCross(cee) ? this.defaultBlockState() : this.crossState;
            cee2 = ((StateHolder<O, BlockState>)cee2).<Comparable, Comparable>setValue((Property<Comparable>)RedStoneWireBlock.POWER, (Comparable)cee.<V>getValue((Property<V>)RedStoneWireBlock.POWER));
            cee2 = this.getConnectionState(bru, cee2, fx);
            if (cee2 != cee) {
                bru.setBlock(fx, cee2, 3);
                this.updatesOnShapeChange(bru, fx, cee, cee2);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    private void updatesOnShapeChange(final Level bru, final BlockPos fx, final BlockState cee3, final BlockState cee4) {
        for (final Direction gc7 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc7);
            if (cee3.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc7)).isConnected() != cee4.<RedstoneSide>getValue((Property<RedstoneSide>)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(gc7)).isConnected() && bru.getBlockState(fx2).isRedstoneConductor(bru, fx2)) {
                bru.updateNeighborsAtExceptFromFacing(fx2, cee4.getBlock(), gc7.getOpposite());
            }
        }
    }
    
    static {
        NORTH = BlockStateProperties.NORTH_REDSTONE;
        EAST = BlockStateProperties.EAST_REDSTONE;
        SOUTH = BlockStateProperties.SOUTH_REDSTONE;
        WEST = BlockStateProperties.WEST_REDSTONE;
        POWER = BlockStateProperties.POWER;
        PROPERTY_BY_DIRECTION = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, RedStoneWireBlock.NORTH, Direction.EAST, RedStoneWireBlock.EAST, Direction.SOUTH, RedStoneWireBlock.SOUTH, Direction.WEST, RedStoneWireBlock.WEST));
        SHAPE_DOT = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
        SHAPES_FLOOR = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, Block.box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0), Direction.SOUTH, Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0), Direction.EAST, Block.box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0), Direction.WEST, Block.box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)));
        SHAPES_UP = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, Shapes.or((VoxelShape)RedStoneWireBlock.SHAPES_FLOOR.get((Object)Direction.NORTH), Block.box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)), Direction.SOUTH, Shapes.or((VoxelShape)RedStoneWireBlock.SHAPES_FLOOR.get((Object)Direction.SOUTH), Block.box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)), Direction.EAST, Shapes.or((VoxelShape)RedStoneWireBlock.SHAPES_FLOOR.get((Object)Direction.EAST), Block.box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)), Direction.WEST, Shapes.or((VoxelShape)RedStoneWireBlock.SHAPES_FLOOR.get((Object)Direction.WEST), Block.box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0))));
        COLORS = new Vector3f[16];
        for (int integer1 = 0; integer1 <= 15; ++integer1) {
            final float float2 = integer1 / 15.0f;
            final float float3 = float2 * 0.6f + ((float2 > 0.0f) ? 0.4f : 0.3f);
            final float float4 = Mth.clamp(float2 * float2 * 0.7f - 0.5f, 0.0f, 1.0f);
            final float float5 = Mth.clamp(float2 * float2 * 0.6f - 0.7f, 0.0f, 1.0f);
            RedStoneWireBlock.COLORS[integer1] = new Vector3f(float3, float4, float5);
        }
    }
}
