package net.minecraft.world.level.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class StoneButtonBlock extends ButtonBlock {
    protected StoneButtonBlock(final Properties c) {
        super(false, c);
    }
    
    @Override
    protected SoundEvent getSound(final boolean boolean1) {
        return boolean1 ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
