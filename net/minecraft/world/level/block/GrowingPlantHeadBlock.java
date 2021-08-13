package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class GrowingPlantHeadBlock extends GrowingPlantBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    private final double growPerTickProbability;
    
    protected GrowingPlantHeadBlock(final Properties c, final Direction gc, final VoxelShape dde, final boolean boolean4, final double double5) {
        super(c, gc, dde, boolean4);
        this.growPerTickProbability = double5;
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)GrowingPlantHeadBlock.AGE, 0));
    }
    
    @Override
    public BlockState getStateForPlacement(final LevelAccessor brv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)GrowingPlantHeadBlock.AGE, brv.getRandom().nextInt(25));
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)GrowingPlantHeadBlock.AGE) < 25;
    }
    
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)GrowingPlantHeadBlock.AGE) < 25 && random.nextDouble() < this.growPerTickProbability) {
            final BlockPos fx2 = fx.relative(this.growthDirection);
            if (this.canGrowInto(aag.getBlockState(fx2))) {
                aag.setBlockAndUpdate(fx2, ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)GrowingPlantHeadBlock.AGE));
            }
        }
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == this.growthDirection.getOpposite() && !cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        if (gc == this.growthDirection && (cee3.is(this) || cee3.is(this.getBodyBlock()))) {
            return this.getBodyBlock().defaultBlockState();
        }
        if (this.scheduleFluidTicks) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(GrowingPlantHeadBlock.AGE);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return this.canGrowInto(bqz.getBlockState(fx.relative(this.growthDirection)));
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        BlockPos fx2 = fx.relative(this.growthDirection);
        for (int integer7 = Math.min(cee.<Integer>getValue((Property<Integer>)GrowingPlantHeadBlock.AGE) + 1, 25), integer8 = this.getBlocksToGrowWhenBonemealed(random), integer9 = 0; integer9 < integer8 && this.canGrowInto(aag.getBlockState(fx2)); fx2 = fx2.relative(this.growthDirection), integer7 = Math.min(integer7 + 1, 25), ++integer9) {
            aag.setBlockAndUpdate(fx2, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)GrowingPlantHeadBlock.AGE, integer7));
        }
    }
    
    protected abstract int getBlocksToGrowWhenBonemealed(final Random random);
    
    protected abstract boolean canGrowInto(final BlockState cee);
    
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return this;
    }
    
    static {
        AGE = BlockStateProperties.AGE_25;
    }
}
