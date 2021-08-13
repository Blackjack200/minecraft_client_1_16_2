package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class IronBarsBlock extends CrossCollisionBlock {
    protected IronBarsBlock(final Properties c) {
        super(1.0f, 1.0f, 16.0f, 16.0f, 16.0f, c);
        this.registerDefaultState(((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)IronBarsBlock.NORTH, false)).setValue((Property<Comparable>)IronBarsBlock.EAST, false)).setValue((Property<Comparable>)IronBarsBlock.SOUTH, false)).setValue((Property<Comparable>)IronBarsBlock.WEST, false)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WATERLOGGED, false));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockGetter bqz3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final FluidState cuu5 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final BlockPos fx5 = fx4.north();
        final BlockPos fx6 = fx4.south();
        final BlockPos fx7 = fx4.west();
        final BlockPos fx8 = fx4.east();
        final BlockState cee10 = bqz3.getBlockState(fx5);
        final BlockState cee11 = bqz3.getBlockState(fx6);
        final BlockState cee12 = bqz3.getBlockState(fx7);
        final BlockState cee13 = bqz3.getBlockState(fx8);
        return ((((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, this.attachsTo(cee10, cee10.isFaceSturdy(bqz3, fx5, Direction.SOUTH)))).setValue((Property<Comparable>)IronBarsBlock.SOUTH, this.attachsTo(cee11, cee11.isFaceSturdy(bqz3, fx6, Direction.NORTH)))).setValue((Property<Comparable>)IronBarsBlock.WEST, this.attachsTo(cee12, cee12.isFaceSturdy(bqz3, fx7, Direction.EAST)))).setValue((Property<Comparable>)IronBarsBlock.EAST, this.attachsTo(cee13, cee13.isFaceSturdy(bqz3, fx8, Direction.WEST)))).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WATERLOGGED, cuu5.getType() == Fluids.WATER);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)IronBarsBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc.getAxis().isHorizontal()) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.PROPERTY_BY_DIRECTION.get(gc), this.attachsTo(cee3, cee3.isFaceSturdy(brv, fx6, gc.getOpposite())));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public VoxelShape getVisualShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.empty();
    }
    
    @Override
    public boolean skipRendering(final BlockState cee1, final BlockState cee2, final Direction gc) {
        if (cee2.is(this)) {
            if (!gc.getAxis().isHorizontal()) {
                return true;
            }
            if (cee1.<Boolean>getValue((Property<Boolean>)IronBarsBlock.PROPERTY_BY_DIRECTION.get(gc)) && cee2.<Boolean>getValue((Property<Boolean>)IronBarsBlock.PROPERTY_BY_DIRECTION.get(gc.getOpposite()))) {
                return true;
            }
        }
        return super.skipRendering(cee1, cee2, gc);
    }
    
    public final boolean attachsTo(final BlockState cee, final boolean boolean2) {
        final Block bul4 = cee.getBlock();
        return (!Block.isExceptionForConnection(bul4) && boolean2) || bul4 instanceof IronBarsBlock || bul4.is(BlockTags.WALLS);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(IronBarsBlock.NORTH, IronBarsBlock.EAST, IronBarsBlock.WEST, IronBarsBlock.SOUTH, IronBarsBlock.WATERLOGGED);
    }
}
