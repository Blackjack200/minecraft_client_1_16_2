package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PoweredRailBlock extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE;
    public static final BooleanProperty POWERED;
    
    protected PoweredRailBlock(final Properties c) {
        super(true, c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_SOUTH)).<Comparable, Boolean>setValue((Property<Comparable>)PoweredRailBlock.POWERED, false));
    }
    
    protected boolean findPoweredRailSignal(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4, final int integer) {
        if (integer >= 8) {
            return false;
        }
        int integer2 = fx.getX();
        int integer3 = fx.getY();
        int integer4 = fx.getZ();
        boolean boolean5 = true;
        RailShape cfh11 = cee.<RailShape>getValue(PoweredRailBlock.SHAPE);
        switch (cfh11) {
            case NORTH_SOUTH: {
                if (boolean4) {
                    ++integer4;
                    break;
                }
                --integer4;
                break;
            }
            case EAST_WEST: {
                if (boolean4) {
                    --integer2;
                    break;
                }
                ++integer2;
                break;
            }
            case ASCENDING_EAST: {
                if (boolean4) {
                    --integer2;
                }
                else {
                    ++integer2;
                    ++integer3;
                    boolean5 = false;
                }
                cfh11 = RailShape.EAST_WEST;
                break;
            }
            case ASCENDING_WEST: {
                if (boolean4) {
                    --integer2;
                    ++integer3;
                    boolean5 = false;
                }
                else {
                    ++integer2;
                }
                cfh11 = RailShape.EAST_WEST;
                break;
            }
            case ASCENDING_NORTH: {
                if (boolean4) {
                    ++integer4;
                }
                else {
                    --integer4;
                    ++integer3;
                    boolean5 = false;
                }
                cfh11 = RailShape.NORTH_SOUTH;
                break;
            }
            case ASCENDING_SOUTH: {
                if (boolean4) {
                    ++integer4;
                    ++integer3;
                    boolean5 = false;
                }
                else {
                    --integer4;
                }
                cfh11 = RailShape.NORTH_SOUTH;
                break;
            }
        }
        return this.isSameRailWithPower(bru, new BlockPos(integer2, integer3, integer4), boolean4, integer, cfh11) || (boolean5 && this.isSameRailWithPower(bru, new BlockPos(integer2, integer3 - 1, integer4), boolean4, integer, cfh11));
    }
    
    protected boolean isSameRailWithPower(final Level bru, final BlockPos fx, final boolean boolean3, final int integer, final RailShape cfh) {
        final BlockState cee7 = bru.getBlockState(fx);
        if (!cee7.is(this)) {
            return false;
        }
        final RailShape cfh2 = cee7.<RailShape>getValue(PoweredRailBlock.SHAPE);
        return (cfh != RailShape.EAST_WEST || (cfh2 != RailShape.NORTH_SOUTH && cfh2 != RailShape.ASCENDING_NORTH && cfh2 != RailShape.ASCENDING_SOUTH)) && (cfh != RailShape.NORTH_SOUTH || (cfh2 != RailShape.EAST_WEST && cfh2 != RailShape.ASCENDING_EAST && cfh2 != RailShape.ASCENDING_WEST)) && cee7.<Boolean>getValue((Property<Boolean>)PoweredRailBlock.POWERED) && (bru.hasNeighborSignal(fx) || this.findPoweredRailSignal(bru, fx, cee7, boolean3, integer + 1));
    }
    
    @Override
    protected void updateState(final BlockState cee, final Level bru, final BlockPos fx, final Block bul) {
        final boolean boolean6 = cee.<Boolean>getValue((Property<Boolean>)PoweredRailBlock.POWERED);
        final boolean boolean7 = bru.hasNeighborSignal(fx) || this.findPoweredRailSignal(bru, fx, cee, true, 0) || this.findPoweredRailSignal(bru, fx, cee, false, 0);
        if (boolean7 != boolean6) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)PoweredRailBlock.POWERED, boolean7), 3);
            bru.updateNeighborsAt(fx.below(), this);
            if (cee.<RailShape>getValue(PoweredRailBlock.SHAPE).isAscending()) {
                bru.updateNeighborsAt(fx.above(), this);
            }
        }
    }
    
    @Override
    public Property<RailShape> getShapeProperty() {
        return PoweredRailBlock.SHAPE;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        Label_0424: {
            switch (bzj) {
                case CLOCKWISE_180: {
                    switch (cee.<RailShape>getValue(PoweredRailBlock.SHAPE)) {
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (cee.<RailShape>getValue(PoweredRailBlock.SHAPE)) {
                        case NORTH_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (cee.<RailShape>getValue(PoweredRailBlock.SHAPE)) {
                        case NORTH_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_EAST);
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
        final RailShape cfh4 = cee.<RailShape>getValue(PoweredRailBlock.SHAPE);
        Label_0319: {
            switch (byd) {
                case LEFT_RIGHT: {
                    switch (cfh4) {
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_EAST);
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
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(PoweredRailBlock.SHAPE, RailShape.NORTH_WEST);
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
        a.add(PoweredRailBlock.SHAPE, PoweredRailBlock.POWERED);
    }
    
    static {
        SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
        POWERED = BlockStateProperties.POWERED;
    }
}
