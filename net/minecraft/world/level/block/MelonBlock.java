package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class MelonBlock extends StemGrownBlock {
    protected MelonBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public StemBlock getStem() {
        return (StemBlock)Blocks.MELON_STEM;
    }
    
    @Override
    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock)Blocks.ATTACHED_MELON_STEM;
    }
}
