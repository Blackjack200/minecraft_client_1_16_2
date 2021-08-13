package net.minecraft.world.level.block;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class AbstractGlassBlock extends HalfTransparentBlock {
    protected AbstractGlassBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getVisualShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.empty();
    }
    
    @Override
    public float getShadeBrightness(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return 1.0f;
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return true;
    }
}
