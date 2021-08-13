package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SandBlock extends FallingBlock {
    private final int dustColor;
    
    public SandBlock(final int integer, final Properties c) {
        super(c);
        this.dustColor = integer;
    }
    
    @Override
    public int getDustColor(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return this.dustColor;
    }
}
