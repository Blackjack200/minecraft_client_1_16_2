package net.minecraft.world.level.biome;

import net.minecraft.sounds.SoundEvents;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import com.mojang.serialization.Codec;

public class AmbientMoodSettings {
    public static final Codec<AmbientMoodSettings> CODEC;
    public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS;
    private SoundEvent soundEvent;
    private int tickDelay;
    private int blockSearchExtent;
    private double soundPositionOffset;
    
    public AmbientMoodSettings(final SoundEvent adn, final int integer2, final int integer3, final double double4) {
        this.soundEvent = adn;
        this.tickDelay = integer2;
        this.blockSearchExtent = integer3;
        this.soundPositionOffset = double4;
    }
    
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
    
    public int getTickDelay() {
        return this.tickDelay;
    }
    
    public int getBlockSearchExtent() {
        return this.blockSearchExtent;
    }
    
    public double getSoundPositionOffset() {
        return this.soundPositionOffset;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(bsq -> bsq.soundEvent), (App)Codec.INT.fieldOf("tick_delay").forGetter(bsq -> bsq.tickDelay), (App)Codec.INT.fieldOf("block_search_extent").forGetter(bsq -> bsq.blockSearchExtent), (App)Codec.DOUBLE.fieldOf("offset").forGetter(bsq -> bsq.soundPositionOffset)).apply((Applicative)instance, AmbientMoodSettings::new));
        LEGACY_CAVE_SETTINGS = new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0);
    }
}
