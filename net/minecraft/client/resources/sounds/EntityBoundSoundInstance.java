package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

public class EntityBoundSoundInstance extends AbstractTickableSoundInstance {
    private final Entity entity;
    
    public EntityBoundSoundInstance(final SoundEvent adn, final SoundSource adp, final Entity apx) {
        this(adn, adp, 1.0f, 1.0f, apx);
    }
    
    public EntityBoundSoundInstance(final SoundEvent adn, final SoundSource adp, final float float3, final float float4, final Entity apx) {
        super(adn, adp);
        this.volume = float3;
        this.pitch = float4;
        this.entity = apx;
        this.x = (float)this.entity.getX();
        this.y = (float)this.entity.getY();
        this.z = (float)this.entity.getZ();
    }
    
    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }
    
    @Override
    public void tick() {
        if (this.entity.removed) {
            this.stop();
            return;
        }
        this.x = (float)this.entity.getX();
        this.y = (float)this.entity.getY();
        this.z = (float)this.entity.getZ();
    }
}
