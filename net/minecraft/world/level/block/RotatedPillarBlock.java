package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class RotatedPillarBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS;
    
    public RotatedPillarBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.defaultBlockState()).<Direction.Axis, Direction.Axis>setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (cee.<Direction.Axis>getValue(RotatedPillarBlock.AXIS)) {
                    case X: {
                        return ((StateHolder<O, BlockState>)cee).<Direction.Axis, Direction.Axis>setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                    }
                    case Z: {
                        return ((StateHolder<O, BlockState>)cee).<Direction.Axis, Direction.Axis>setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
                    }
                    default: {
                        return cee;
                    }
                }
                break;
            }
            default: {
                return cee;
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RotatedPillarBlock.AXIS);
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Direction.Axis, Direction.Axis>setValue(RotatedPillarBlock.AXIS, bnv.getClickedFace().getAxis());
    }
    
    static {
        AXIS = BlockStateProperties.AXIS;
    }
}
