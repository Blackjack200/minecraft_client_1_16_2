package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import java.util.Optional;
import net.minecraft.world.level.biome.Biome;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Random;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.player.LocalPlayer;

public class BiomeAmbientSoundsHandler implements AmbientSoundHandler {
    private final LocalPlayer player;
    private final SoundManager soundManager;
    private final BiomeManager biomeManager;
    private final Random random;
    private Object2ObjectArrayMap<Biome, LoopSoundInstance> loopSounds;
    private Optional<AmbientMoodSettings> moodSettings;
    private Optional<AmbientAdditionsSettings> additionsSettings;
    private float moodiness;
    private Biome previousBiome;
    
    public BiomeAmbientSoundsHandler(final LocalPlayer dze, final SoundManager enm, final BiomeManager bsu) {
        this.loopSounds = (Object2ObjectArrayMap<Biome, LoopSoundInstance>)new Object2ObjectArrayMap();
        this.moodSettings = (Optional<AmbientMoodSettings>)Optional.empty();
        this.additionsSettings = (Optional<AmbientAdditionsSettings>)Optional.empty();
        this.random = dze.level.getRandom();
        this.player = dze;
        this.soundManager = enm;
        this.biomeManager = bsu;
    }
    
    public float getMoodiness() {
        return this.moodiness;
    }
    
    public void tick() {
        this.loopSounds.values().removeIf(AbstractTickableSoundInstance::isStopped);
        final Biome bss2 = this.biomeManager.getNoiseBiomeAtPosition(this.player.getX(), this.player.getY(), this.player.getZ());
        if (bss2 != this.previousBiome) {
            this.previousBiome = bss2;
            this.moodSettings = bss2.getAmbientMood();
            this.additionsSettings = bss2.getAmbientAdditions();
            this.loopSounds.values().forEach(LoopSoundInstance::fadeOut);
            bss2.getAmbientLoop().ifPresent(adn -> {
                final LoopSoundInstance loopSoundInstance = (LoopSoundInstance)this.loopSounds.compute(bss2, (bss, a) -> {
                    if (a == null) {
                        a = new LoopSoundInstance(adn);
                        this.soundManager.play(a);
                    }
                    a.fadeIn();
                    return a;
                });
            });
        }
        this.additionsSettings.ifPresent(bsp -> {
            if (this.random.nextDouble() < bsp.getTickChance()) {
                this.soundManager.play(SimpleSoundInstance.forAmbientAddition(bsp.getSoundEvent()));
            }
        });
        this.moodSettings.ifPresent(bsq -> {
            final Level bru3 = this.player.level;
            final int integer4 = bsq.getBlockSearchExtent() * 2 + 1;
            final BlockPos fx5 = new BlockPos(this.player.getX() + this.random.nextInt(integer4) - bsq.getBlockSearchExtent(), this.player.getEyeY() + this.random.nextInt(integer4) - bsq.getBlockSearchExtent(), this.player.getZ() + this.random.nextInt(integer4) - bsq.getBlockSearchExtent());
            final int integer5 = bru3.getBrightness(LightLayer.SKY, fx5);
            if (integer5 > 0) {
                this.moodiness -= integer5 / (float)bru3.getMaxLightLevel() * 0.001f;
            }
            else {
                this.moodiness -= (bru3.getBrightness(LightLayer.BLOCK, fx5) - 1) / (float)bsq.getTickDelay();
            }
            if (this.moodiness >= 1.0f) {
                final double double7 = fx5.getX() + 0.5;
                final double double8 = fx5.getY() + 0.5;
                final double double9 = fx5.getZ() + 0.5;
                final double double10 = double7 - this.player.getX();
                final double double11 = double8 - this.player.getEyeY();
                final double double12 = double9 - this.player.getZ();
                final double double13 = Mth.sqrt(double10 * double10 + double11 * double11 + double12 * double12);
                final double double14 = double13 + bsq.getSoundPositionOffset();
                final SimpleSoundInstance emh23 = SimpleSoundInstance.forAmbientMood(bsq.getSoundEvent(), this.player.getX() + double10 / double13 * double14, this.player.getEyeY() + double11 / double13 * double14, this.player.getZ() + double12 / double13 * double14);
                this.soundManager.play(emh23);
                this.moodiness = 0.0f;
            }
            else {
                this.moodiness = Math.max(this.moodiness, 0.0f);
            }
        });
    }
    
    public static class LoopSoundInstance extends AbstractTickableSoundInstance {
        private int fadeDirection;
        private int fade;
        
        public LoopSoundInstance(final SoundEvent adn) {
            super(adn, SoundSource.AMBIENT);
            this.looping = true;
            this.delay = 0;
            this.volume = 1.0f;
            this.relative = true;
        }
        
        @Override
        public void tick() {
            if (this.fade < 0) {
                this.stop();
            }
            this.fade += this.fadeDirection;
            this.volume = Mth.clamp(this.fade / 40.0f, 0.0f, 1.0f);
        }
        
        public void fadeOut() {
            this.fade = Math.min(this.fade, 40);
            this.fadeDirection = -1;
        }
        
        public void fadeIn() {
            this.fade = Math.max(0, this.fade);
            this.fadeDirection = 1;
        }
    }
}
