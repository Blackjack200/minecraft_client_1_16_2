package net.minecraft.client.resources.sounds;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class MinecartSoundInstance extends AbstractTickableSoundInstance {
    private final AbstractMinecart minecart;
    private float pitch;
    
    public MinecartSoundInstance(final AbstractMinecart bhi) {
        super(SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL);
        this.pitch = 0.0f;
        this.minecart = bhi;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
        this.x = (float)bhi.getX();
        this.y = (float)bhi.getY();
        this.z = (float)bhi.getZ();
    }
    
    public boolean canPlaySound() {
        return !this.minecart.isSilent();
    }
    
    public boolean canStartSilent() {
        return true;
    }
    
    @Override
    public void tick() {
        if (this.minecart.removed) {
            this.stop();
            return;
        }
        this.x = (float)this.minecart.getX();
        this.y = (float)this.minecart.getY();
        this.z = (float)this.minecart.getZ();
        final float float2 = Mth.sqrt(Entity.getHorizontalDistanceSqr(this.minecart.getDeltaMovement()));
        if (float2 >= 0.01) {
            this.pitch = Mth.clamp(this.pitch + 0.0025f, 0.0f, 1.0f);
            this.volume = Mth.lerp(Mth.clamp(float2, 0.0f, 0.5f), 0.0f, 0.7f);
        }
        else {
            this.pitch = 0.0f;
            this.volume = 0.0f;
        }
    }
}
