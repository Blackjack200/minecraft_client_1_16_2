package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import java.util.Iterator;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SugarCaneBlock extends Block {
    public static final IntegerProperty AGE;
    protected static final VoxelShape SHAPE;
    
    protected SugarCaneBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)SugarCaneBlock.AGE, 0));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SugarCaneBlock.SHAPE;
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            aag.destroyBlock(fx, true);
        }
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.isEmptyBlock(fx.above())) {
            int integer6;
            for (integer6 = 1; aag.getBlockState(fx.below(integer6)).is(this); ++integer6) {}
            if (integer6 < 3) {
                final int integer7 = cee.<Integer>getValue((Property<Integer>)SugarCaneBlock.AGE);
                if (integer7 == 15) {
                    aag.setBlockAndUpdate(fx.above(), this.defaultBlockState());
                    aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SugarCaneBlock.AGE, 0), 4);
                }
                else {
                    aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SugarCaneBlock.AGE, integer7 + 1), 4);
                }
            }
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.below());
        if (cee2.getBlock() == this) {
            return true;
        }
        if (cee2.is(Blocks.GRASS_BLOCK) || cee2.is(Blocks.DIRT) || cee2.is(Blocks.COARSE_DIRT) || cee2.is(Blocks.PODZOL) || cee2.is(Blocks.SAND) || cee2.is(Blocks.RED_SAND)) {
            final BlockPos fx2 = fx.below();
            for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
                final BlockState cee3 = brw.getBlockState(fx2.relative(gc8));
                final FluidState cuu10 = brw.getFluidState(fx2.relative(gc8));
                if (cuu10.is(FluidTags.WATER) || cee3.is(Blocks.FROSTED_ICE)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SugarCaneBlock.AGE);
    }
    
    static {
        AGE = BlockStateProperties.AGE_15;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }
}
