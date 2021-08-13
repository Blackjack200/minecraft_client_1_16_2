package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nullable;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class DetectorRailBlock extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE;
    public static final BooleanProperty POWERED;
    
    public DetectorRailBlock(final Properties c) {
        super(true, c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)DetectorRailBlock.POWERED, false)).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_SOUTH));
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (bru.isClientSide) {
            return;
        }
        if (cee.<Boolean>getValue((Property<Boolean>)DetectorRailBlock.POWERED)) {
            return;
        }
        this.checkPressed(bru, fx, cee);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)DetectorRailBlock.POWERED)) {
            return;
        }
        this.checkPressed(aag, fx, cee);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Boolean>getValue((Property<Boolean>)DetectorRailBlock.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (!cee.<Boolean>getValue((Property<Boolean>)DetectorRailBlock.POWERED)) {
            return 0;
        }
        return (gc == Direction.UP) ? 15 : 0;
    }
    
    private void checkPressed(final Level bru, final BlockPos fx, final BlockState cee) {
        if (!this.canSurvive(cee, bru, fx)) {
            return;
        }
        final boolean boolean5 = cee.<Boolean>getValue((Property<Boolean>)DetectorRailBlock.POWERED);
        boolean boolean6 = false;
        final List<AbstractMinecart> list7 = this.<AbstractMinecart>getInteractingMinecartOfType(bru, fx, AbstractMinecart.class, null);
        if (!list7.isEmpty()) {
            boolean6 = true;
        }
        if (boolean6 && !boolean5) {
            final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DetectorRailBlock.POWERED, true);
            bru.setBlock(fx, cee2, 3);
            this.updatePowerToConnected(bru, fx, cee2, true);
            bru.updateNeighborsAt(fx, this);
            bru.updateNeighborsAt(fx.below(), this);
            bru.setBlocksDirty(fx, cee, cee2);
        }
        if (!boolean6 && boolean5) {
            final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DetectorRailBlock.POWERED, false);
            bru.setBlock(fx, cee2, 3);
            this.updatePowerToConnected(bru, fx, cee2, false);
            bru.updateNeighborsAt(fx, this);
            bru.updateNeighborsAt(fx.below(), this);
            bru.setBlocksDirty(fx, cee, cee2);
        }
        if (boolean6) {
            bru.getBlockTicks().scheduleTick(fx, this, 20);
        }
        bru.updateNeighbourForOutputSignal(fx, this);
    }
    
    protected void updatePowerToConnected(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        final RailState byy6 = new RailState(bru, fx, cee);
        final List<BlockPos> list7 = byy6.getConnections();
        for (final BlockPos fx2 : list7) {
            final BlockState cee2 = bru.getBlockState(fx2);
            cee2.neighborChanged(bru, fx2, cee2.getBlock(), fx, false);
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        this.checkPressed(bru, fx, this.updateState(cee1, bru, fx, boolean5));
    }
    
    @Override
    public Property<RailShape> getShapeProperty() {
        return DetectorRailBlock.SHAPE;
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        if (cee.<Boolean>getValue((Property<Boolean>)DetectorRailBlock.POWERED)) {
            final List<MinecartCommandBlock> list5 = this.<MinecartCommandBlock>getInteractingMinecartOfType(bru, fx, MinecartCommandBlock.class, null);
            if (!list5.isEmpty()) {
                return ((MinecartCommandBlock)list5.get(0)).getCommandBlock().getSuccessCount();
            }
            final List<AbstractMinecart> list6 = this.<AbstractMinecart>getInteractingMinecartOfType(bru, fx, AbstractMinecart.class, EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!list6.isEmpty()) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)list6.get(0));
            }
        }
        return 0;
    }
    
    protected <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(final Level bru, final BlockPos fx, final Class<T> class3, @Nullable final Predicate<Entity> predicate) {
        return bru.<T>getEntitiesOfClass((java.lang.Class<? extends T>)class3, this.getSearchBB(fx), (java.util.function.Predicate<? super T>)predicate);
    }
    
    private AABB getSearchBB(final BlockPos fx) {
        final double double3 = 0.2;
        return new AABB(fx.getX() + 0.2, fx.getY(), fx.getZ() + 0.2, fx.getX() + 1 - 0.2, fx.getY() + 1 - 0.2, fx.getZ() + 1 - 0.2);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        Label_0424: {
            switch (bzj) {
                case CLOCKWISE_180: {
                    switch (cee.<RailShape>getValue(DetectorRailBlock.SHAPE)) {
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (cee.<RailShape>getValue(DetectorRailBlock.SHAPE)) {
                        case NORTH_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (cee.<RailShape>getValue(DetectorRailBlock.SHAPE)) {
                        case NORTH_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
            }
        }
        return cee;
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        final RailShape cfh4 = cee.<RailShape>getValue(DetectorRailBlock.SHAPE);
        Label_0319: {
            switch (byd) {
                case LEFT_RIGHT: {
                    switch (cfh4) {
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        default: {
                            break Label_0319;
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    switch (cfh4) {
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(DetectorRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        default: {
                            break Label_0319;
                        }
                    }
                    break;
                }
            }
        }
        return super.mirror(cee, byd);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(DetectorRailBlock.SHAPE, DetectorRailBlock.POWERED);
    }
    
    static {
        SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
        POWERED = BlockStateProperties.POWERED;
    }
}
