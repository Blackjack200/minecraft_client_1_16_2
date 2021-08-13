package net.minecraft.world.level.block;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AirBlock extends Block {
    protected AirBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.empty();
    }
}
