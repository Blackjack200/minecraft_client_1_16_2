package net.minecraft.world.level.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WoodButtonBlock extends ButtonBlock {
    protected WoodButtonBlock(final Properties c) {
        super(true, c);
    }
    
    @Override
    protected SoundEvent getSound(final boolean boolean1) {
        return boolean1 ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
    }
}
