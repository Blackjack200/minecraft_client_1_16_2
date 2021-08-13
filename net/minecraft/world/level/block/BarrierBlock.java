package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BarrierBlock extends Block {
    protected BarrierBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return true;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public float getShadeBrightness(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return 1.0f;
    }
}
