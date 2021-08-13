package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class StemGrownBlock extends Block {
    public StemGrownBlock(final Properties c) {
        super(c);
    }
    
    public abstract StemBlock getStem();
    
    public abstract AttachedStemBlock getAttachedStem();
}
