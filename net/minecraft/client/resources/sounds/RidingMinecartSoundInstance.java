package net.minecraft.client.resources.sounds;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.player.Player;

public class RidingMinecartSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private final AbstractMinecart minecart;
    
    public RidingMinecartSoundInstance(final Player bft, final AbstractMinecart bhi) {
        super(SoundEvents.MINECART_INSIDE, SoundSource.NEUTRAL);
        this.player = bft;
        this.minecart = bhi;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
    }
    
    public boolean canPlaySound() {
        return !this.minecart.isSilent();
    }
    
    public boolean canStartSilent() {
        return true;
    }
    
    @Override
    public void tick() {
        if (this.minecart.removed || !this.player.isPassenger() || this.player.getVehicle() != this.minecart) {
            this.stop();
            return;
        }
        final float float2 = Mth.sqrt(Entity.getHorizontalDistanceSqr(this.minecart.getDeltaMovement()));
        if (float2 >= 0.01) {
            this.volume = 0.0f + Mth.clamp(float2, 0.0f, 1.0f) * 0.75f;
        }
        else {
            this.volume = 0.0f;
        }
    }
}
