package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BaseCoralPlantTypeBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    private static final VoxelShape AABB;
    
    protected BaseCoralPlantTypeBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Boolean>setValue((Property<Comparable>)BaseCoralPlantTypeBlock.WATERLOGGED, true));
    }
    
    protected void tryScheduleDieTick(final BlockState cee, final LevelAccessor brv, final BlockPos fx) {
        if (!scanForWater(cee, brv, fx)) {
            brv.getBlockTicks().scheduleTick(fx, this, 60 + brv.getRandom().nextInt(40));
        }
    }
    
    protected static boolean scanForWater(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        if (cee.<Boolean>getValue((Property<Boolean>)BaseCoralPlantTypeBlock.WATERLOGGED)) {
            return true;
        }
        for (final Direction gc7 : Direction.values()) {
            if (bqz.getFluidState(fx.relative(gc7)).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)BaseCoralPlantTypeBlock.WATERLOGGED, cuu3.is(FluidTags.WATER) && cuu3.getAmount() == 8);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return BaseCoralPlantTypeBlock.AABB;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)BaseCoralPlantTypeBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc == Direction.DOWN && !this.canSurvive(cee1, brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        return brw.getBlockState(fx2).isFaceSturdy(brw, fx2, Direction.UP);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BaseCoralPlantTypeBlock.WATERLOGGED);
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)BaseCoralPlantTypeBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        AABB = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    }
}
