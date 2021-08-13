package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Bee;

public class BeeFlyingSoundInstance extends BeeSoundInstance {
    public BeeFlyingSoundInstance(final Bee azx) {
        super(azx, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL);
    }
    
    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new BeeAggressiveSoundInstance(this.bee);
    }
    
    @Override
    protected boolean shouldSwitchSounds() {
        return this.bee.isAngry();
    }
}
