package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SeaPickleBlock extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final IntegerProperty PICKLES;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape ONE_AABB;
    protected static final VoxelShape TWO_AABB;
    protected static final VoxelShape THREE_AABB;
    protected static final VoxelShape FOUR_AABB;
    
    protected SeaPickleBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)SeaPickleBlock.PICKLES, 1)).<Comparable, Boolean>setValue((Property<Comparable>)SeaPickleBlock.WATERLOGGED, true));
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = bnv.getLevel().getBlockState(bnv.getClickedPos());
        if (cee3.is(this)) {
            return ((StateHolder<O, BlockState>)cee3).<Comparable, Integer>setValue((Property<Comparable>)SeaPickleBlock.PICKLES, Math.min(4, cee3.<Integer>getValue((Property<Integer>)SeaPickleBlock.PICKLES) + 1));
        }
        final FluidState cuu4 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final boolean boolean5 = cuu4.getType() == Fluids.WATER;
        return ((StateHolder<O, BlockState>)super.getStateForPlacement(bnv)).<Comparable, Boolean>setValue((Property<Comparable>)SeaPickleBlock.WATERLOGGED, boolean5);
    }
    
    public static boolean isDead(final BlockState cee) {
        return !cee.<Boolean>getValue((Property<Boolean>)SeaPickleBlock.WATERLOGGED);
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return !cee.getCollisionShape(bqz, fx).getFaceShape(Direction.UP).isEmpty() || cee.isFaceSturdy(bqz, fx, Direction.UP);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        return this.mayPlaceOn(brw.getBlockState(fx2), brw, fx2);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)SeaPickleBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        return (bnv.getItemInHand().getItem() == this.asItem() && cee.<Integer>getValue((Property<Integer>)SeaPickleBlock.PICKLES) < 4) || super.canBeReplaced(cee, bnv);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Integer>getValue((Property<Integer>)SeaPickleBlock.PICKLES)) {
            default: {
                return SeaPickleBlock.ONE_AABB;
            }
            case 2: {
                return SeaPickleBlock.TWO_AABB;
            }
            case 3: {
                return SeaPickleBlock.THREE_AABB;
            }
            case 4: {
                return SeaPickleBlock.FOUR_AABB;
            }
        }
    }
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)SeaPickleBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SeaPickleBlock.PICKLES, SeaPickleBlock.WATERLOGGED);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return true;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        if (!isDead(cee) && aag.getBlockState(fx.below()).is(BlockTags.CORAL_BLOCKS)) {
            final int integer6 = 5;
            int integer7 = 1;
            final int integer8 = 2;
            int integer9 = 0;
            final int integer10 = fx.getX() - 2;
            int integer11 = 0;
            for (int integer12 = 0; integer12 < 5; ++integer12) {
                for (int integer13 = 0; integer13 < integer7; ++integer13) {
                    for (int integer14 = 2 + fx.getY() - 1, integer15 = integer14 - 2; integer15 < integer14; ++integer15) {
                        final BlockPos fx2 = new BlockPos(integer10 + integer12, integer15, fx.getZ() - integer11 + integer13);
                        if (fx2 != fx) {
                            if (random.nextInt(6) == 0 && aag.getBlockState(fx2).is(Blocks.WATER)) {
                                final BlockState cee2 = aag.getBlockState(fx2.below());
                                if (cee2.is(BlockTags.CORAL_BLOCKS)) {
                                    aag.setBlock(fx2, ((StateHolder<O, BlockState>)Blocks.SEA_PICKLE.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)SeaPickleBlock.PICKLES, random.nextInt(4) + 1), 3);
                                }
                            }
                        }
                    }
                }
                if (integer9 < 2) {
                    integer7 += 2;
                    ++integer11;
                }
                else {
                    integer7 -= 2;
                    --integer11;
                }
                ++integer9;
            }
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SeaPickleBlock.PICKLES, 4), 2);
        }
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        PICKLES = BlockStateProperties.PICKLES;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        ONE_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);
        TWO_AABB = Block.box(3.0, 0.0, 3.0, 13.0, 6.0, 13.0);
        THREE_AABB = Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);
        FOUR_AABB = Block.box(2.0, 0.0, 2.0, 14.0, 7.0, 14.0);
    }
}
