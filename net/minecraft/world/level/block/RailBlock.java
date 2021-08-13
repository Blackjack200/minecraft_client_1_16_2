package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class RailBlock extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE;
    
    protected RailBlock(final Properties c) {
        super(false, c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH));
    }
    
    @Override
    protected void updateState(final BlockState cee, final Level bru, final BlockPos fx, final Block bul) {
        if (bul.defaultBlockState().isSignalSource() && new RailState(bru, fx, cee).countPotentialConnections() == 3) {
            this.updateDir(bru, fx, cee, false);
        }
    }
    
    @Override
    public Property<RailShape> getShapeProperty() {
        return RailBlock.SHAPE;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        Label_0424: {
            switch (bzj) {
                case CLOCKWISE_180: {
                    switch (cee.<RailShape>getValue(RailBlock.SHAPE)) {
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (cee.<RailShape>getValue(RailBlock.SHAPE)) {
                        case NORTH_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        default: {
                            break Label_0424;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (cee.<RailShape>getValue(RailBlock.SHAPE)) {
                        case NORTH_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_EAST);
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
        final RailShape cfh4 = cee.<RailShape>getValue(RailBlock.SHAPE);
        Label_0319: {
            switch (byd) {
                case LEFT_RIGHT: {
                    switch (cfh4) {
                        case ASCENDING_NORTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_EAST);
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
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return ((StateHolder<O, BlockState>)cee).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_WEST);
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
        a.add(RailBlock.SHAPE);
    }
    
    static {
        SHAPE = BlockStateProperties.RAIL_SHAPE;
    }
}
