package net.minecraft.client.sounds;

public interface Weighted<T> {
    int getWeight();
    
    T getSound();
    
    void preloadIfRequired(final SoundEngine enj);
}
