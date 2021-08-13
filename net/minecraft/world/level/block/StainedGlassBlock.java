package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;

public class StainedGlassBlock extends AbstractGlassBlock implements BeaconBeamBlock {
    private final DyeColor color;
    
    public StainedGlassBlock(final DyeColor bku, final Properties c) {
        super(c);
        this.color = bku;
    }
    
    @Override
    public DyeColor getColor() {
        return this.color;
    }
}
