package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BaseRailBlock extends Block {
    protected static final VoxelShape FLAT_AABB;
    protected static final VoxelShape HALF_BLOCK_AABB;
    private final boolean isStraight;
    
    public static boolean isRail(final Level bru, final BlockPos fx) {
        return isRail(bru.getBlockState(fx));
    }
    
    public static boolean isRail(final BlockState cee) {
        return cee.is(BlockTags.RAILS) && cee.getBlock() instanceof BaseRailBlock;
    }
    
    protected BaseRailBlock(final boolean boolean1, final Properties c) {
        super(c);
        this.isStraight = boolean1;
    }
    
    public boolean isStraight() {
        return this.isStraight;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final RailShape cfh6 = cee.is(this) ? cee.<RailShape>getValue(this.getShapeProperty()) : null;
        if (cfh6 != null && cfh6.isAscending()) {
            return BaseRailBlock.HALF_BLOCK_AABB;
        }
        return BaseRailBlock.FLAT_AABB;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return Block.canSupportRigidBlock(brw, fx.below());
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        this.updateState(cee1, bru, fx, boolean5);
    }
    
    protected BlockState updateState(BlockState cee, final Level bru, final BlockPos fx, final boolean boolean4) {
        cee = this.updateDir(bru, fx, cee, true);
        if (this.isStraight) {
            cee.neighborChanged(bru, fx, this, fx, boolean4);
        }
        return cee;
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.isClientSide || !bru.getBlockState(fx3).is(this)) {
            return;
        }
        final RailShape cfh8 = cee.<RailShape>getValue(this.getShapeProperty());
        if (shouldBeRemoved(fx3, bru, cfh8)) {
            Block.dropResources(cee, bru, fx3);
            bru.removeBlock(fx3, boolean6);
        }
        else {
            this.updateState(cee, bru, fx3, bul);
        }
    }
    
    private static boolean shouldBeRemoved(final BlockPos fx, final Level bru, final RailShape cfh) {
        if (!Block.canSupportRigidBlock(bru, fx.below())) {
            return true;
        }
        switch (cfh) {
            case ASCENDING_EAST: {
                return !Block.canSupportRigidBlock(bru, fx.east());
            }
            case ASCENDING_WEST: {
                return !Block.canSupportRigidBlock(bru, fx.west());
            }
            case ASCENDING_NORTH: {
                return !Block.canSupportRigidBlock(bru, fx.north());
            }
            case ASCENDING_SOUTH: {
                return !Block.canSupportRigidBlock(bru, fx.south());
            }
            default: {
                return false;
            }
        }
    }
    
    protected void updateState(final BlockState cee, final Level bru, final BlockPos fx, final Block bul) {
    }
    
    protected BlockState updateDir(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        if (bru.isClientSide) {
            return cee;
        }
        final RailShape cfh6 = cee.<RailShape>getValue(this.getShapeProperty());
        return new RailState(bru, fx, cee).place(bru.hasNeighborSignal(fx), boolean4, cfh6).getState();
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.NORMAL;
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5) {
            return;
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
        if (cee1.<RailShape>getValue(this.getShapeProperty()).isAscending()) {
            bru.updateNeighborsAt(fx.above(), this);
        }
        if (this.isStraight) {
            bru.updateNeighborsAt(fx, this);
            bru.updateNeighborsAt(fx.below(), this);
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = super.defaultBlockState();
        final Direction gc4 = bnv.getHorizontalDirection();
        final boolean boolean5 = gc4 == Direction.EAST || gc4 == Direction.WEST;
        return ((StateHolder<O, BlockState>)cee3).<Comparable, RailShape>setValue((Property<Comparable>)this.getShapeProperty(), boolean5 ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH);
    }
    
    public abstract Property<RailShape> getShapeProperty();
    
    static {
        FLAT_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        HALF_BLOCK_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
