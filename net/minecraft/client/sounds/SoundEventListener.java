package net.minecraft.client.sounds;

import net.minecraft.client.resources.sounds.SoundInstance;

public interface SoundEventListener {
    void onPlaySound(final SoundInstance eml, final WeighedSoundEvents enn);
}
