package net.minecraft.client.resources.sounds;

import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.player.LocalPlayer;

public class ElytraOnPlayerSoundInstance extends AbstractTickableSoundInstance {
    private final LocalPlayer player;
    private int time;
    
    public ElytraOnPlayerSoundInstance(final LocalPlayer dze) {
        super(SoundEvents.ELYTRA_FLYING, SoundSource.PLAYERS);
        this.player = dze;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1f;
    }
    
    @Override
    public void tick() {
        ++this.time;
        if (this.player.removed || (this.time > 20 && !this.player.isFallFlying())) {
            this.stop();
            return;
        }
        this.x = (float)this.player.getX();
        this.y = (float)this.player.getY();
        this.z = (float)this.player.getZ();
        final float float2 = (float)this.player.getDeltaMovement().lengthSqr();
        if (float2 >= 1.0E-7) {
            this.volume = Mth.clamp(float2 / 4.0f, 0.0f, 1.0f);
        }
        else {
            this.volume = 0.0f;
        }
        if (this.time < 20) {
            this.volume = 0.0f;
        }
        else if (this.time < 40) {
            this.volume *= (float)((this.time - 20) / 20.0);
        }
        final float float3 = 0.8f;
        if (this.volume > 0.8f) {
            this.pitch = 1.0f + (this.volume - 0.8f);
        }
        else {
            this.pitch = 1.0f;
        }
    }
}
