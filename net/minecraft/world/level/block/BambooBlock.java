package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooBlock extends Block implements BonemealableBlock {
    protected static final VoxelShape SMALL_SHAPE;
    protected static final VoxelShape LARGE_SHAPE;
    protected static final VoxelShape COLLISION_SHAPE;
    public static final IntegerProperty AGE;
    public static final EnumProperty<BambooLeaves> LEAVES;
    public static final IntegerProperty STAGE;
    
    public BambooBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)BambooBlock.AGE, 0)).setValue(BambooBlock.LEAVES, BambooLeaves.NONE)).<Comparable, Integer>setValue((Property<Comparable>)BambooBlock.STAGE, 0));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BambooBlock.AGE, BambooBlock.LEAVES, BambooBlock.STAGE);
    }
    
    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final VoxelShape dde6 = (cee.<BambooLeaves>getValue(BambooBlock.LEAVES) == BambooLeaves.LARGE) ? BambooBlock.LARGE_SHAPE : BambooBlock.SMALL_SHAPE;
        final Vec3 dck7 = cee.getOffset(bqz, fx);
        return dde6.move(dck7.x, dck7.y, dck7.z);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Vec3 dck6 = cee.getOffset(bqz, fx);
        return BambooBlock.COLLISION_SHAPE.move(dck6.x, dck6.y, dck6.z);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        if (!cuu3.isEmpty()) {
            return null;
        }
        final BlockState cee4 = bnv.getLevel().getBlockState(bnv.getClickedPos().below());
        if (!cee4.is(BlockTags.BAMBOO_PLANTABLE_ON)) {
            return null;
        }
        if (cee4.is(Blocks.BAMBOO_SAPLING)) {
            return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)BambooBlock.AGE, 0);
        }
        if (cee4.is(Blocks.BAMBOO)) {
            final int integer5 = (cee4.<Integer>getValue((Property<Integer>)BambooBlock.AGE) > 0) ? 1 : 0;
            return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)BambooBlock.AGE, integer5);
        }
        final BlockState cee5 = bnv.getLevel().getBlockState(bnv.getClickedPos().above());
        if (cee5.is(Blocks.BAMBOO) || cee5.is(Blocks.BAMBOO_SAPLING)) {
            return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Comparable>setValue((Property<Comparable>)BambooBlock.AGE, (Comparable)cee5.<V>getValue((Property<V>)BambooBlock.AGE));
        }
        return Blocks.BAMBOO_SAPLING.defaultBlockState();
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            aag.destroyBlock(fx, true);
        }
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)BambooBlock.STAGE) == 0;
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)BambooBlock.STAGE) != 0) {
            return;
        }
        if (random.nextInt(3) == 0 && aag.isEmptyBlock(fx.above()) && aag.getRawBrightness(fx.above(), 0) >= 9) {
            final int integer6 = this.getHeightBelowUpToMax(aag, fx) + 1;
            if (integer6 < 16) {
                this.growBamboo(cee, aag, fx, random, integer6);
            }
        }
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return brw.getBlockState(fx.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        if (gc == Direction.UP && cee3.is(Blocks.BAMBOO) && cee3.<Integer>getValue((Property<Integer>)BambooBlock.AGE) > cee1.<Integer>getValue((Property<Integer>)BambooBlock.AGE)) {
            brv.setBlock(fx5, ((StateHolder<O, BlockState>)cee1).<Comparable>cycle((Property<Comparable>)BambooBlock.AGE), 2);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        final int integer6 = this.getHeightAboveUpToMax(bqz, fx);
        final int integer7 = this.getHeightBelowUpToMax(bqz, fx);
        return integer6 + integer7 + 1 < 16 && bqz.getBlockState(fx.above(integer6)).<Integer>getValue((Property<Integer>)BambooBlock.STAGE) != 1;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        int integer6 = this.getHeightAboveUpToMax(aag, fx);
        final int integer7 = this.getHeightBelowUpToMax(aag, fx);
        int integer8 = integer6 + integer7 + 1;
        for (int integer9 = 1 + random.nextInt(2), integer10 = 0; integer10 < integer9; ++integer10) {
            final BlockPos fx2 = fx.above(integer6);
            final BlockState cee2 = aag.getBlockState(fx2);
            if (integer8 >= 16 || cee2.<Integer>getValue((Property<Integer>)BambooBlock.STAGE) == 1 || !aag.isEmptyBlock(fx2.above())) {
                return;
            }
            this.growBamboo(cee2, aag, fx2, random, integer8);
            ++integer6;
            ++integer8;
        }
    }
    
    @Override
    public float getDestroyProgress(final BlockState cee, final Player bft, final BlockGetter bqz, final BlockPos fx) {
        if (bft.getMainHandItem().getItem() instanceof SwordItem) {
            return 1.0f;
        }
        return super.getDestroyProgress(cee, bft, bqz, fx);
    }
    
    protected void growBamboo(final BlockState cee, final Level bru, final BlockPos fx, final Random random, final int integer) {
        final BlockState cee2 = bru.getBlockState(fx.below());
        final BlockPos fx2 = fx.below(2);
        final BlockState cee3 = bru.getBlockState(fx2);
        BambooLeaves cer10 = BambooLeaves.NONE;
        if (integer >= 1) {
            if (!cee2.is(Blocks.BAMBOO) || cee2.<BambooLeaves>getValue(BambooBlock.LEAVES) == BambooLeaves.NONE) {
                cer10 = BambooLeaves.SMALL;
            }
            else if (cee2.is(Blocks.BAMBOO) && cee2.<BambooLeaves>getValue(BambooBlock.LEAVES) != BambooLeaves.NONE) {
                cer10 = BambooLeaves.LARGE;
                if (cee3.is(Blocks.BAMBOO)) {
                    bru.setBlock(fx.below(), ((StateHolder<O, BlockState>)cee2).<BambooLeaves, BambooLeaves>setValue(BambooBlock.LEAVES, BambooLeaves.SMALL), 3);
                    bru.setBlock(fx2, ((StateHolder<O, BlockState>)cee3).<BambooLeaves, BambooLeaves>setValue(BambooBlock.LEAVES, BambooLeaves.NONE), 3);
                }
            }
        }
        final int integer2 = (cee.<Integer>getValue((Property<Integer>)BambooBlock.AGE) == 1 || cee3.is(Blocks.BAMBOO)) ? 1 : 0;
        final int integer3 = ((integer >= 11 && random.nextFloat() < 0.25f) || integer == 15) ? 1 : 0;
        bru.setBlock(fx.above(), ((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)BambooBlock.AGE, integer2)).setValue(BambooBlock.LEAVES, cer10)).<Comparable, Integer>setValue((Property<Comparable>)BambooBlock.STAGE, integer3), 3);
    }
    
    protected int getHeightAboveUpToMax(final BlockGetter bqz, final BlockPos fx) {
        int integer4;
        for (integer4 = 0; integer4 < 16 && bqz.getBlockState(fx.above(integer4 + 1)).is(Blocks.BAMBOO); ++integer4) {}
        return integer4;
    }
    
    protected int getHeightBelowUpToMax(final BlockGetter bqz, final BlockPos fx) {
        int integer4;
        for (integer4 = 0; integer4 < 16 && bqz.getBlockState(fx.below(integer4 + 1)).is(Blocks.BAMBOO); ++integer4) {}
        return integer4;
    }
    
    static {
        SMALL_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
        LARGE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
        COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
        AGE = BlockStateProperties.AGE_1;
        LEAVES = BlockStateProperties.BAMBOO_LEAVES;
        STAGE = BlockStateProperties.STAGE;
    }
}
