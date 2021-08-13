package net.minecraft.client.resources.sounds;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Bee;

public abstract class BeeSoundInstance extends AbstractTickableSoundInstance {
    protected final Bee bee;
    private boolean hasSwitched;
    
    public BeeSoundInstance(final Bee azx, final SoundEvent adn, final SoundSource adp) {
        super(adn, adp);
        this.bee = azx;
        this.x = (float)azx.getX();
        this.y = (float)azx.getY();
        this.z = (float)azx.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
    }
    
    @Override
    public void tick() {
        final boolean boolean2 = this.shouldSwitchSounds();
        if (boolean2 && !this.isStopped()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(this.getAlternativeSoundInstance());
            this.hasSwitched = true;
        }
        if (this.bee.removed || this.hasSwitched) {
            this.stop();
            return;
        }
        this.x = (float)this.bee.getX();
        this.y = (float)this.bee.getY();
        this.z = (float)this.bee.getZ();
        final float float3 = Mth.sqrt(Entity.getHorizontalDistanceSqr(this.bee.getDeltaMovement()));
        if (float3 >= 0.01) {
            this.pitch = Mth.lerp(Mth.clamp(float3, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
            this.volume = Mth.lerp(Mth.clamp(float3, 0.0f, 0.5f), 0.0f, 1.2f);
        }
        else {
            this.pitch = 0.0f;
            this.volume = 0.0f;
        }
    }
    
    private float getMinPitch() {
        if (this.bee.isBaby()) {
            return 1.1f;
        }
        return 0.7f;
    }
    
    private float getMaxPitch() {
        if (this.bee.isBaby()) {
            return 1.5f;
        }
        return 1.1f;
    }
    
    public boolean canStartSilent() {
        return true;
    }
    
    public boolean canPlaySound() {
        return !this.bee.isSilent();
    }
    
    protected abstract AbstractTickableSoundInstance getAlternativeSoundInstance();
    
    protected abstract boolean shouldSwitchSounds();
}
