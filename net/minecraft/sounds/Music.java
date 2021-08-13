package net.minecraft.sounds;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class Music {
    public static final Codec<Music> CODEC;
    private final SoundEvent event;
    private final int minDelay;
    private final int maxDelay;
    private final boolean replaceCurrentMusic;
    
    public Music(final SoundEvent adn, final int integer2, final int integer3, final boolean boolean4) {
        this.event = adn;
        this.minDelay = integer2;
        this.maxDelay = integer3;
        this.replaceCurrentMusic = boolean4;
    }
    
    public SoundEvent getEvent() {
        return this.event;
    }
    
    public int getMinDelay() {
        return this.minDelay;
    }
    
    public int getMaxDelay() {
        return this.maxDelay;
    }
    
    public boolean replaceCurrentMusic() {
        return this.replaceCurrentMusic;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(adl -> adl.event), (App)Codec.INT.fieldOf("min_delay").forGetter(adl -> adl.minDelay), (App)Codec.INT.fieldOf("max_delay").forGetter(adl -> adl.maxDelay), (App)Codec.BOOL.fieldOf("replace_current_music").forGetter(adl -> adl.replaceCurrentMusic)).apply((Applicative)instance, Music::new));
    }
}
