package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.player.LocalPlayer;

public class UnderwaterAmbientSoundHandler implements AmbientSoundHandler {
    private final LocalPlayer player;
    private final SoundManager soundManager;
    private int tickDelay;
    
    public UnderwaterAmbientSoundHandler(final LocalPlayer dze, final SoundManager enm) {
        this.tickDelay = 0;
        this.player = dze;
        this.soundManager = enm;
    }
    
    public void tick() {
        --this.tickDelay;
        if (this.tickDelay <= 0 && this.player.isUnderWater()) {
            final float float2 = this.player.level.random.nextFloat();
            if (float2 < 1.0E-4f) {
                this.tickDelay = 0;
                this.soundManager.play(new UnderwaterAmbientSoundInstances.SubSound(this.player, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE));
            }
            else if (float2 < 0.001f) {
                this.tickDelay = 0;
                this.soundManager.play(new UnderwaterAmbientSoundInstances.SubSound(this.player, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE));
            }
            else if (float2 < 0.01f) {
                this.tickDelay = 0;
                this.soundManager.play(new UnderwaterAmbientSoundInstances.SubSound(this.player, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS));
            }
        }
    }
}
