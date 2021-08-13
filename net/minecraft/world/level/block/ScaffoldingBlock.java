package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ScaffoldingBlock extends Block implements SimpleWaterloggedBlock {
    private static final VoxelShape STABLE_SHAPE;
    private static final VoxelShape UNSTABLE_SHAPE;
    private static final VoxelShape UNSTABLE_SHAPE_BOTTOM;
    private static final VoxelShape BELOW_BLOCK;
    public static final IntegerProperty DISTANCE;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty BOTTOM;
    
    protected ScaffoldingBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ScaffoldingBlock.DISTANCE, 7)).setValue((Property<Comparable>)ScaffoldingBlock.WATERLOGGED, false)).<Comparable, Boolean>setValue((Property<Comparable>)ScaffoldingBlock.BOTTOM, false));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ScaffoldingBlock.DISTANCE, ScaffoldingBlock.WATERLOGGED, ScaffoldingBlock.BOTTOM);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (!dcp.isHoldingItem(cee.getBlock().asItem())) {
            return cee.<Boolean>getValue((Property<Boolean>)ScaffoldingBlock.BOTTOM) ? ScaffoldingBlock.UNSTABLE_SHAPE : ScaffoldingBlock.STABLE_SHAPE;
        }
        return Shapes.block();
    }
    
    @Override
    public VoxelShape getInteractionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return Shapes.block();
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        return bnv.getItemInHand().getItem() == this.asItem();
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockPos fx3 = bnv.getClickedPos();
        final Level bru4 = bnv.getLevel();
        final int integer5 = getDistance(bru4, fx3);
        return ((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)ScaffoldingBlock.WATERLOGGED, bru4.getFluidState(fx3).getType() == Fluids.WATER)).setValue((Property<Comparable>)ScaffoldingBlock.DISTANCE, integer5)).<Comparable, Boolean>setValue((Property<Comparable>)ScaffoldingBlock.BOTTOM, this.isBottom(bru4, fx3, integer5));
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (!bru.isClientSide) {
            bru.getBlockTicks().scheduleTick(fx, this, 1);
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)ScaffoldingBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (!brv.isClientSide()) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return cee1;
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final int integer6 = getDistance(aag, fx);
        final BlockState cee2 = (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)ScaffoldingBlock.DISTANCE, integer6)).<Comparable, Boolean>setValue((Property<Comparable>)ScaffoldingBlock.BOTTOM, this.isBottom(aag, fx, integer6));
        if (cee2.<Integer>getValue((Property<Integer>)ScaffoldingBlock.DISTANCE) == 7) {
            if (cee.<Integer>getValue((Property<Integer>)ScaffoldingBlock.DISTANCE) == 7) {
                aag.addFreshEntity(new FallingBlockEntity(aag, fx.getX() + 0.5, fx.getY(), fx.getZ() + 0.5, ((StateHolder<O, BlockState>)cee2).<Comparable, Boolean>setValue((Property<Comparable>)ScaffoldingBlock.WATERLOGGED, false)));
            }
            else {
                aag.destroyBlock(fx, true);
            }
        }
        else if (cee != cee2) {
            aag.setBlock(fx, cee2, 3);
        }
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return getDistance(brw, fx) < 7;
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (dcp.isAbove(Shapes.block(), fx, true) && !dcp.isDescending()) {
            return ScaffoldingBlock.STABLE_SHAPE;
        }
        if (cee.<Integer>getValue((Property<Integer>)ScaffoldingBlock.DISTANCE) != 0 && cee.<Boolean>getValue((Property<Boolean>)ScaffoldingBlock.BOTTOM) && dcp.isAbove(ScaffoldingBlock.BELOW_BLOCK, fx, true)) {
            return ScaffoldingBlock.UNSTABLE_SHAPE_BOTTOM;
        }
        return Shapes.empty();
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)ScaffoldingBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    private boolean isBottom(final BlockGetter bqz, final BlockPos fx, final int integer) {
        return integer > 0 && !bqz.getBlockState(fx.below()).is(this);
    }
    
    public static int getDistance(final BlockGetter bqz, final BlockPos fx) {
        final BlockPos.MutableBlockPos a3 = fx.mutable().move(Direction.DOWN);
        final BlockState cee4 = bqz.getBlockState(a3);
        int integer5 = 7;
        if (cee4.is(Blocks.SCAFFOLDING)) {
            integer5 = cee4.<Integer>getValue((Property<Integer>)ScaffoldingBlock.DISTANCE);
        }
        else if (cee4.isFaceSturdy(bqz, a3, Direction.UP)) {
            return 0;
        }
        for (final Direction gc7 : Direction.Plane.HORIZONTAL) {
            final BlockState cee5 = bqz.getBlockState(a3.setWithOffset(fx, gc7));
            if (!cee5.is(Blocks.SCAFFOLDING)) {
                continue;
            }
            integer5 = Math.min(integer5, cee5.<Integer>getValue((Property<Integer>)ScaffoldingBlock.DISTANCE) + 1);
            if (integer5 == 1) {
                break;
            }
        }
        return integer5;
    }
    
    static {
        UNSTABLE_SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        BELOW_BLOCK = Shapes.block().move(0.0, -1.0, 0.0);
        DISTANCE = BlockStateProperties.STABILITY_DISTANCE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        BOTTOM = BlockStateProperties.BOTTOM;
        final VoxelShape dde1 = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        final VoxelShape dde2 = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 2.0);
        final VoxelShape dde3 = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 2.0);
        final VoxelShape dde4 = Block.box(0.0, 0.0, 14.0, 2.0, 16.0, 16.0);
        final VoxelShape dde5 = Block.box(14.0, 0.0, 14.0, 16.0, 16.0, 16.0);
        STABLE_SHAPE = Shapes.or(dde1, dde2, dde3, dde4, dde5);
        final VoxelShape dde6 = Block.box(0.0, 0.0, 0.0, 2.0, 2.0, 16.0);
        final VoxelShape dde7 = Block.box(14.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        final VoxelShape dde8 = Block.box(0.0, 0.0, 14.0, 16.0, 2.0, 16.0);
        final VoxelShape dde9 = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 2.0);
        UNSTABLE_SHAPE = Shapes.or(ScaffoldingBlock.UNSTABLE_SHAPE_BOTTOM, ScaffoldingBlock.STABLE_SHAPE, dde7, dde6, dde9, dde8);
    }
}
