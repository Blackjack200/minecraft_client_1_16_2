package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FenceBlock extends CrossCollisionBlock {
    private final VoxelShape[] occlusionByIndex;
    
    public FenceBlock(final Properties c) {
        super(2.0f, 2.0f, 16.0f, 16.0f, 24.0f, c);
        this.registerDefaultState(((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)FenceBlock.NORTH, false)).setValue((Property<Comparable>)FenceBlock.EAST, false)).setValue((Property<Comparable>)FenceBlock.SOUTH, false)).setValue((Property<Comparable>)FenceBlock.WEST, false)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WATERLOGGED, false));
        this.occlusionByIndex = this.makeShapes(2.0f, 1.0f, 16.0f, 6.0f, 15.0f);
    }
    
    @Override
    public VoxelShape getOcclusionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return this.occlusionByIndex[this.getAABBIndex(cee)];
    }
    
    @Override
    public VoxelShape getVisualShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.getShape(cee, bqz, fx, dcp);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    public boolean connectsTo(final BlockState cee, final boolean boolean2, final Direction gc) {
        final Block bul5 = cee.getBlock();
        final boolean boolean3 = this.isSameFence(bul5);
        final boolean boolean4 = bul5 instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(cee, gc);
        return (!Block.isExceptionForConnection(bul5) && boolean2) || boolean3 || boolean4;
    }
    
    private boolean isSameFence(final Block bul) {
        return bul.is(BlockTags.FENCES) && bul.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (!bru.isClientSide) {
            return LeadItem.bindPlayerMobs(bft, bru, fx);
        }
        final ItemStack bly8 = bft.getItemInHand(aoq);
        if (bly8.getItem() == Items.LEAD) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockGetter bqz3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final FluidState cuu5 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final BlockPos fx5 = fx4.north();
        final BlockPos fx6 = fx4.east();
        final BlockPos fx7 = fx4.south();
        final BlockPos fx8 = fx4.west();
        final BlockState cee10 = bqz3.getBlockState(fx5);
        final BlockState cee11 = bqz3.getBlockState(fx6);
        final BlockState cee12 = bqz3.getBlockState(fx7);
        final BlockState cee13 = bqz3.getBlockState(fx8);
        return ((((((StateHolder<O, BlockState>)super.getStateForPlacement(bnv)).setValue((Property<Comparable>)FenceBlock.NORTH, this.connectsTo(cee10, cee10.isFaceSturdy(bqz3, fx5, Direction.SOUTH), Direction.SOUTH))).setValue((Property<Comparable>)FenceBlock.EAST, this.connectsTo(cee11, cee11.isFaceSturdy(bqz3, fx6, Direction.WEST), Direction.WEST))).setValue((Property<Comparable>)FenceBlock.SOUTH, this.connectsTo(cee12, cee12.isFaceSturdy(bqz3, fx7, Direction.NORTH), Direction.NORTH))).setValue((Property<Comparable>)FenceBlock.WEST, this.connectsTo(cee13, cee13.isFaceSturdy(bqz3, fx8, Direction.EAST), Direction.EAST))).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WATERLOGGED, cuu5.getType() == Fluids.WATER);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)FenceBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc.getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.PROPERTY_BY_DIRECTION.get(gc), this.connectsTo(cee3, cee3.isFaceSturdy(brv, fx6, gc.getOpposite()), gc.getOpposite()));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(FenceBlock.NORTH, FenceBlock.EAST, FenceBlock.WEST, FenceBlock.SOUTH, FenceBlock.WATERLOGGED);
    }
}
