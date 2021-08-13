package net.minecraft.client.sounds;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.Minecraft;
import java.util.Random;

public class MusicManager {
    private final Random random;
    private final Minecraft minecraft;
    @Nullable
    private SoundInstance currentMusic;
    private int nextSongDelay;
    
    public MusicManager(final Minecraft djw) {
        this.random = new Random();
        this.nextSongDelay = 100;
        this.minecraft = djw;
    }
    
    public void tick() {
        final Music adl2 = this.minecraft.getSituationalMusic();
        if (this.currentMusic != null) {
            if (!adl2.getEvent().getLocation().equals(this.currentMusic.getLocation()) && adl2.replaceCurrentMusic()) {
                this.minecraft.getSoundManager().stop(this.currentMusic);
                this.nextSongDelay = Mth.nextInt(this.random, 0, adl2.getMinDelay() / 2);
            }
            if (!this.minecraft.getSoundManager().isActive(this.currentMusic)) {
                this.currentMusic = null;
                this.nextSongDelay = Math.min(this.nextSongDelay, Mth.nextInt(this.random, adl2.getMinDelay(), adl2.getMaxDelay()));
            }
        }
        this.nextSongDelay = Math.min(this.nextSongDelay, adl2.getMaxDelay());
        if (this.currentMusic == null && this.nextSongDelay-- <= 0) {
            this.startPlaying(adl2);
        }
    }
    
    public void startPlaying(final Music adl) {
        this.currentMusic = SimpleSoundInstance.forMusic(adl.getEvent());
        if (this.currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
            this.minecraft.getSoundManager().play(this.currentMusic);
        }
        this.nextSongDelay = Integer.MAX_VALUE;
    }
    
    public void stopPlaying() {
        if (this.currentMusic != null) {
            this.minecraft.getSoundManager().stop(this.currentMusic);
            this.currentMusic = null;
        }
        this.nextSongDelay += 100;
    }
    
    public boolean isPlayingMusic(final Music adl) {
        return this.currentMusic != null && adl.getEvent().getLocation().equals(this.currentMusic.getLocation());
    }
}
