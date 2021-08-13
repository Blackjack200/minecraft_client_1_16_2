package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public abstract class HorizontalDirectionalBlock extends Block {
    public static final DirectionProperty FACING;
    
    protected HorizontalDirectionalBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)HorizontalDirectionalBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)HorizontalDirectionalBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)HorizontalDirectionalBlock.FACING)));
    }
    
    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
