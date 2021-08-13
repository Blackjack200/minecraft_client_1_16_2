package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.client.player.LocalPlayer;

public class UnderwaterAmbientSoundInstances {
    public static class SubSound extends AbstractTickableSoundInstance {
        private final LocalPlayer player;
        
        protected SubSound(final LocalPlayer dze, final SoundEvent adn) {
            super(adn, SoundSource.AMBIENT);
            this.player = dze;
            this.looping = false;
            this.delay = 0;
            this.volume = 1.0f;
            this.priority = true;
            this.relative = true;
        }
        
        @Override
        public void tick() {
            if (this.player.removed || !this.player.isUnderWater()) {
                this.stop();
            }
        }
    }
    
    public static class UnderwaterAmbientSoundInstance extends AbstractTickableSoundInstance {
        private final LocalPlayer player;
        private int fade;
        
        public UnderwaterAmbientSoundInstance(final LocalPlayer dze) {
            super(SoundEvents.AMBIENT_UNDERWATER_LOOP, SoundSource.AMBIENT);
            this.player = dze;
            this.looping = true;
            this.delay = 0;
            this.volume = 1.0f;
            this.priority = true;
            this.relative = true;
        }
        
        @Override
        public void tick() {
            if (this.player.removed || this.fade < 0) {
                this.stop();
                return;
            }
            if (this.player.isUnderWater()) {
                ++this.fade;
            }
            else {
                this.fade -= 2;
            }
            this.fade = Math.min(this.fade, 40);
            this.volume = Math.max(0.0f, Math.min(this.fade / 40.0f, 1.0f));
        }
    }
}
