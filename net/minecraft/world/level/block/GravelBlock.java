package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GravelBlock extends FallingBlock {
    public GravelBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public int getDustColor(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return -8356741;
    }
}
