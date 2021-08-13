package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StandingSignBlock extends SignBlock {
    public static final IntegerProperty ROTATION;
    
    public StandingSignBlock(final Properties c, final WoodType cfn) {
        super(c, cfn);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)StandingSignBlock.ROTATION, 0)).<Comparable, Boolean>setValue((Property<Comparable>)StandingSignBlock.WATERLOGGED, false));
    }
    
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return brw.getBlockState(fx.below()).getMaterial().isSolid();
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        return (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)StandingSignBlock.ROTATION, Mth.floor((180.0f + bnv.getRotation()) * 16.0f / 360.0f + 0.5) & 0xF)).<Comparable, Boolean>setValue((Property<Comparable>)StandingSignBlock.WATERLOGGED, cuu3.getType() == Fluids.WATER);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN && !this.canSurvive(cee1, brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)StandingSignBlock.ROTATION, bzj.rotate(cee.<Integer>getValue((Property<Integer>)StandingSignBlock.ROTATION), 16));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)StandingSignBlock.ROTATION, byd.mirror(cee.<Integer>getValue((Property<Integer>)StandingSignBlock.ROTATION), 16));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(StandingSignBlock.ROTATION, StandingSignBlock.WATERLOGGED);
    }
    
    static {
        ROTATION = BlockStateProperties.ROTATION_16;
    }
}
