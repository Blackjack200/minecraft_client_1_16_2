package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class HalfTransparentBlock extends Block {
    protected HalfTransparentBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean skipRendering(final BlockState cee1, final BlockState cee2, final Direction gc) {
        return cee2.is(this) || super.skipRendering(cee1, cee2, gc);
    }
}
