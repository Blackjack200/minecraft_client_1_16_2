package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;

public abstract class AbstractTickableSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
    private boolean stopped;
    
    protected AbstractTickableSoundInstance(final SoundEvent adn, final SoundSource adp) {
        super(adn, adp);
    }
    
    @Override
    public boolean isStopped() {
        return this.stopped;
    }
    
    protected final void stop() {
        this.stopped = true;
        this.looping = false;
    }
}
