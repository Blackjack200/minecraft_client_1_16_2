package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ChorusPlantBlock extends PipeBlock {
    protected ChorusPlantBlock(final Properties c) {
        super(0.3125f, c);
        this.registerDefaultState((((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ChorusPlantBlock.NORTH, false)).setValue((Property<Comparable>)ChorusPlantBlock.EAST, false)).setValue((Property<Comparable>)ChorusPlantBlock.SOUTH, false)).setValue((Property<Comparable>)ChorusPlantBlock.WEST, false)).setValue((Property<Comparable>)ChorusPlantBlock.UP, false)).<Comparable, Boolean>setValue((Property<Comparable>)ChorusPlantBlock.DOWN, false));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return this.getStateForPlacement(bnv.getLevel(), bnv.getClickedPos());
    }
    
    public BlockState getStateForPlacement(final BlockGetter bqz, final BlockPos fx) {
        final Block bul4 = bqz.getBlockState(fx.below()).getBlock();
        final Block bul5 = bqz.getBlockState(fx.above()).getBlock();
        final Block bul6 = bqz.getBlockState(fx.north()).getBlock();
        final Block bul7 = bqz.getBlockState(fx.east()).getBlock();
        final Block bul8 = bqz.getBlockState(fx.south()).getBlock();
        final Block bul9 = bqz.getBlockState(fx.west()).getBlock();
        return (((((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)ChorusPlantBlock.DOWN, bul4 == this || bul4 == Blocks.CHORUS_FLOWER || bul4 == Blocks.END_STONE)).setValue((Property<Comparable>)ChorusPlantBlock.UP, bul5 == this || bul5 == Blocks.CHORUS_FLOWER)).setValue((Property<Comparable>)ChorusPlantBlock.NORTH, bul6 == this || bul6 == Blocks.CHORUS_FLOWER)).setValue((Property<Comparable>)ChorusPlantBlock.EAST, bul7 == this || bul7 == Blocks.CHORUS_FLOWER)).setValue((Property<Comparable>)ChorusPlantBlock.SOUTH, bul8 == this || bul8 == Blocks.CHORUS_FLOWER)).<Comparable, Boolean>setValue((Property<Comparable>)ChorusPlantBlock.WEST, bul9 == this || bul9 == Blocks.CHORUS_FLOWER);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
            return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        }
        final boolean boolean8 = cee3.getBlock() == this || cee3.is(Blocks.CHORUS_FLOWER) || (gc == Direction.DOWN && cee3.is(Blocks.END_STONE));
        return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)ChorusPlantBlock.PROPERTY_BY_DIRECTION.get(gc), boolean8);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            aag.destroyBlock(fx, true);
        }
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.below());
        final boolean boolean6 = !brw.getBlockState(fx.above()).isAir() && !cee2.isAir();
        for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc8);
            final Block bul10 = brw.getBlockState(fx2).getBlock();
            if (bul10 == this) {
                if (boolean6) {
                    return false;
                }
                final Block bul11 = brw.getBlockState(fx2.below()).getBlock();
                if (bul11 == this || bul11 == Blocks.END_STONE) {
                    return true;
                }
                continue;
            }
        }
        final Block bul12 = cee2.getBlock();
        return bul12 == this || bul12 == Blocks.END_STONE;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ChorusPlantBlock.NORTH, ChorusPlantBlock.EAST, ChorusPlantBlock.SOUTH, ChorusPlantBlock.WEST, ChorusPlantBlock.UP, ChorusPlantBlock.DOWN);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
}
