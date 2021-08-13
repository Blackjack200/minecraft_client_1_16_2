package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Guardian;

public class GuardianAttackSoundInstance extends AbstractTickableSoundInstance {
    private final Guardian guardian;
    
    public GuardianAttackSoundInstance(final Guardian bdj) {
        super(SoundEvents.GUARDIAN_ATTACK, SoundSource.HOSTILE);
        this.guardian = bdj;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
    }
    
    public boolean canPlaySound() {
        return !this.guardian.isSilent();
    }
    
    @Override
    public void tick() {
        if (this.guardian.removed || this.guardian.getTarget() != null) {
            this.stop();
            return;
        }
        this.x = (float)this.guardian.getX();
        this.y = (float)this.guardian.getY();
        this.z = (float)this.guardian.getZ();
        final float float2 = this.guardian.getAttackAnimationScale(0.0f);
        this.volume = 0.0f + 1.0f * float2 * float2;
        this.pitch = 0.7f + 0.5f * float2;
    }
}
