package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.stream.IntStream;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class StairBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<Half> HALF;
    public static final EnumProperty<StairsShape> SHAPE;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape TOP_AABB;
    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape OCTET_NNN;
    protected static final VoxelShape OCTET_NNP;
    protected static final VoxelShape OCTET_NPN;
    protected static final VoxelShape OCTET_NPP;
    protected static final VoxelShape OCTET_PNN;
    protected static final VoxelShape OCTET_PNP;
    protected static final VoxelShape OCTET_PPN;
    protected static final VoxelShape OCTET_PPP;
    protected static final VoxelShape[] TOP_SHAPES;
    protected static final VoxelShape[] BOTTOM_SHAPES;
    private static final int[] SHAPE_BY_STATE;
    private final Block base;
    private final BlockState baseState;
    
    private static VoxelShape[] makeShapes(final VoxelShape dde1, final VoxelShape dde2, final VoxelShape dde3, final VoxelShape dde4, final VoxelShape dde5) {
        return (VoxelShape[])IntStream.range(0, 16).mapToObj(integer -> makeStairShape(integer, dde1, dde2, dde3, dde4, dde5)).toArray(VoxelShape[]::new);
    }
    
    private static VoxelShape makeStairShape(final int integer, final VoxelShape dde2, final VoxelShape dde3, final VoxelShape dde4, final VoxelShape dde5, final VoxelShape dde6) {
        VoxelShape dde7 = dde2;
        if ((integer & 0x1) != 0x0) {
            dde7 = Shapes.or(dde7, dde3);
        }
        if ((integer & 0x2) != 0x0) {
            dde7 = Shapes.or(dde7, dde4);
        }
        if ((integer & 0x4) != 0x0) {
            dde7 = Shapes.or(dde7, dde5);
        }
        if ((integer & 0x8) != 0x0) {
            dde7 = Shapes.or(dde7, dde6);
        }
        return dde7;
    }
    
    protected StairBlock(final BlockState cee, final Properties c) {
        super(c);
        this.registerDefaultState((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)StairBlock.FACING, Direction.NORTH)).setValue(StairBlock.HALF, Half.BOTTOM)).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT)).<Comparable, Boolean>setValue((Property<Comparable>)StairBlock.WATERLOGGED, false));
        this.base = cee.getBlock();
        this.baseState = cee;
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return ((cee.<Half>getValue(StairBlock.HALF) == Half.TOP) ? StairBlock.TOP_SHAPES : StairBlock.BOTTOM_SHAPES)[StairBlock.SHAPE_BY_STATE[this.getShapeIndex(cee)]];
    }
    
    private int getShapeIndex(final BlockState cee) {
        return cee.<StairsShape>getValue(StairBlock.SHAPE).ordinal() * 4 + cee.<Direction>getValue((Property<Direction>)StairBlock.FACING).get2DDataValue();
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        this.base.animateTick(cee, bru, fx, random);
    }
    
    @Override
    public void attack(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        this.baseState.attack(bru, fx, bft);
    }
    
    @Override
    public void destroy(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        this.base.destroy(brv, fx, cee);
    }
    
    @Override
    public float getExplosionResistance() {
        return this.base.getExplosionResistance();
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee1.getBlock())) {
            return;
        }
        this.baseState.neighborChanged(bru, fx, Blocks.AIR, fx, false);
        this.base.onPlace(this.baseState, bru, fx, cee4, false);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        this.baseState.onRemove(bru, fx, cee4, boolean5);
    }
    
    @Override
    public void stepOn(final Level bru, final BlockPos fx, final Entity apx) {
        this.base.stepOn(bru, fx, apx);
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return this.base.isRandomlyTicking(cee);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.base.randomTick(cee, aag, fx, random);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.base.tick(cee, aag, fx, random);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        return this.baseState.use(bru, bft, aoq, dcg);
    }
    
    @Override
    public void wasExploded(final Level bru, final BlockPos fx, final Explosion brm) {
        this.base.wasExploded(bru, fx, brm);
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Direction gc3 = bnv.getClickedFace();
        final BlockPos fx4 = bnv.getClickedPos();
        final FluidState cuu5 = bnv.getLevel().getFluidState(fx4);
        final BlockState cee6 = ((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)StairBlock.FACING, bnv.getHorizontalDirection())).setValue((Property<Comparable>)StairBlock.HALF, (gc3 == Direction.DOWN || (gc3 != Direction.UP && bnv.getClickLocation().y - fx4.getY() > 0.5)) ? Half.TOP : Half.BOTTOM)).<Comparable, Boolean>setValue((Property<Comparable>)StairBlock.WATERLOGGED, cuu5.getType() == Fluids.WATER);
        return ((StateHolder<O, BlockState>)cee6).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, getStairsShape(cee6, bnv.getLevel(), fx4));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)StairBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc.getAxis().isHorizontal()) {
            return ((StateHolder<O, BlockState>)cee1).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, getStairsShape(cee1, brv, fx5));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    private static StairsShape getStairsShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        final Direction gc4 = cee.<Direction>getValue((Property<Direction>)StairBlock.FACING);
        final BlockState cee2 = bqz.getBlockState(fx.relative(gc4));
        if (isStairs(cee2) && cee.<Half>getValue(StairBlock.HALF) == cee2.<Half>getValue(StairBlock.HALF)) {
            final Direction gc5 = cee2.<Direction>getValue((Property<Direction>)StairBlock.FACING);
            if (gc5.getAxis() != cee.<Direction>getValue((Property<Direction>)StairBlock.FACING).getAxis() && canTakeShape(cee, bqz, fx, gc5.getOpposite())) {
                if (gc5 == gc4.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }
                return StairsShape.OUTER_RIGHT;
            }
        }
        final BlockState cee3 = bqz.getBlockState(fx.relative(gc4.getOpposite()));
        if (isStairs(cee3) && cee.<Half>getValue(StairBlock.HALF) == cee3.<Half>getValue(StairBlock.HALF)) {
            final Direction gc6 = cee3.<Direction>getValue((Property<Direction>)StairBlock.FACING);
            if (gc6.getAxis() != cee.<Direction>getValue((Property<Direction>)StairBlock.FACING).getAxis() && canTakeShape(cee, bqz, fx, gc6)) {
                if (gc6 == gc4.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }
                return StairsShape.INNER_RIGHT;
            }
        }
        return StairsShape.STRAIGHT;
    }
    
    private static boolean canTakeShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        final BlockState cee2 = bqz.getBlockState(fx.relative(gc));
        return !isStairs(cee2) || cee2.<Comparable>getValue((Property<Comparable>)StairBlock.FACING) != cee.<Comparable>getValue((Property<Comparable>)StairBlock.FACING) || cee2.<Half>getValue(StairBlock.HALF) != cee.<Half>getValue(StairBlock.HALF);
    }
    
    public static boolean isStairs(final BlockState cee) {
        return cee.getBlock() instanceof StairBlock;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)StairBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        final Direction gc4 = cee.<Direction>getValue((Property<Direction>)StairBlock.FACING);
        final StairsShape cfk5 = cee.<StairsShape>getValue(StairBlock.SHAPE);
        Label_0335: {
            switch (byd) {
                case LEFT_RIGHT: {
                    if (gc4.getAxis() != Direction.Axis.Z) {
                        break;
                    }
                    switch (cfk5) {
                        case INNER_LEFT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.INNER_RIGHT);
                        }
                        case INNER_RIGHT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.INNER_LEFT);
                        }
                        case OUTER_LEFT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT);
                        }
                        case OUTER_RIGHT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT);
                        }
                        default: {
                            return cee.rotate(Rotation.CLOCKWISE_180);
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    if (gc4.getAxis() != Direction.Axis.X) {
                        break;
                    }
                    switch (cfk5) {
                        case INNER_LEFT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.INNER_LEFT);
                        }
                        case INNER_RIGHT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.INNER_RIGHT);
                        }
                        case OUTER_LEFT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT);
                        }
                        case OUTER_RIGHT: {
                            return ((StateHolder<O, BlockState>)cee.rotate(Rotation.CLOCKWISE_180)).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT);
                        }
                        case STRAIGHT: {
                            return cee.rotate(Rotation.CLOCKWISE_180);
                        }
                        default: {
                            break Label_0335;
                        }
                    }
                    break;
                }
            }
        }
        return super.mirror(cee, byd);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(StairBlock.FACING, StairBlock.HALF, StairBlock.SHAPE, StairBlock.WATERLOGGED);
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)StairBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        HALF = BlockStateProperties.HALF;
        SHAPE = BlockStateProperties.STAIRS_SHAPE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TOP_AABB = SlabBlock.TOP_AABB;
        BOTTOM_AABB = SlabBlock.BOTTOM_AABB;
        OCTET_NNN = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
        OCTET_NNP = Block.box(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
        OCTET_NPN = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
        OCTET_NPP = Block.box(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
        OCTET_PNN = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
        OCTET_PNP = Block.box(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
        OCTET_PPN = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
        OCTET_PPP = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);
        TOP_SHAPES = makeShapes(StairBlock.TOP_AABB, StairBlock.OCTET_NNN, StairBlock.OCTET_PNN, StairBlock.OCTET_NNP, StairBlock.OCTET_PNP);
        BOTTOM_SHAPES = makeShapes(StairBlock.BOTTOM_AABB, StairBlock.OCTET_NPN, StairBlock.OCTET_PPN, StairBlock.OCTET_NPP, StairBlock.OCTET_PPP);
        SHAPE_BY_STATE = new int[] { 12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8 };
    }
}
