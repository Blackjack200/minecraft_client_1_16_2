package net.minecraft.world.level.biome;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import com.mojang.serialization.Codec;

public class AmbientAdditionsSettings {
    public static final Codec<AmbientAdditionsSettings> CODEC;
    private SoundEvent soundEvent;
    private double tickChance;
    
    public AmbientAdditionsSettings(final SoundEvent adn, final double double2) {
        this.soundEvent = adn;
        this.tickChance = double2;
    }
    
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
    
    public double getTickChance() {
        return this.tickChance;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(bsp -> bsp.soundEvent), (App)Codec.DOUBLE.fieldOf("tick_chance").forGetter(bsp -> bsp.tickChance)).apply((Applicative)instance, AmbientAdditionsSettings::new));
    }
}
