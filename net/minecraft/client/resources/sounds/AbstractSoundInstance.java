package net.minecraft.client.resources.sounds;

import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public abstract class AbstractSoundInstance implements SoundInstance {
    protected Sound sound;
    protected final SoundSource source;
    protected final ResourceLocation location;
    protected float volume;
    protected float pitch;
    protected double x;
    protected double y;
    protected double z;
    protected boolean looping;
    protected int delay;
    protected Attenuation attenuation;
    protected boolean priority;
    protected boolean relative;
    
    protected AbstractSoundInstance(final SoundEvent adn, final SoundSource adp) {
        this(adn.getLocation(), adp);
    }
    
    protected AbstractSoundInstance(final ResourceLocation vk, final SoundSource adp) {
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.attenuation = Attenuation.LINEAR;
        this.location = vk;
        this.source = adp;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    public WeighedSoundEvents resolve(final SoundManager enm) {
        final WeighedSoundEvents enn3 = enm.getSoundEvent(this.location);
        if (enn3 == null) {
            this.sound = SoundManager.EMPTY_SOUND;
        }
        else {
            this.sound = enn3.getSound();
        }
        return enn3;
    }
    
    public Sound getSound() {
        return this.sound;
    }
    
    public SoundSource getSource() {
        return this.source;
    }
    
    public boolean isLooping() {
        return this.looping;
    }
    
    public int getDelay() {
        return this.delay;
    }
    
    public float getVolume() {
        return this.volume * this.sound.getVolume();
    }
    
    public float getPitch() {
        return this.pitch * this.sound.getPitch();
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public Attenuation getAttenuation() {
        return this.attenuation;
    }
    
    public boolean isRelative() {
        return this.relative;
    }
    
    public String toString() {
        return new StringBuilder().append("SoundInstance[").append(this.location).append("]").toString();
    }
}
