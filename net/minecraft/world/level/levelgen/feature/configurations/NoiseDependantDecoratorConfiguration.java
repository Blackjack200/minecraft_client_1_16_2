package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class NoiseDependantDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<NoiseDependantDecoratorConfiguration> CODEC;
    public final double noiseLevel;
    public final int belowNoise;
    public final int aboveNoise;
    
    public NoiseDependantDecoratorConfiguration(final double double1, final int integer2, final int integer3) {
        this.noiseLevel = double1;
        this.belowNoise = integer2;
        this.aboveNoise = integer3;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.DOUBLE.fieldOf("noise_level").forGetter(cmc -> cmc.noiseLevel), (App)Codec.INT.fieldOf("below_noise").forGetter(cmc -> cmc.belowNoise), (App)Codec.INT.fieldOf("above_noise").forGetter(cmc -> cmc.aboveNoise)).apply((Applicative)instance, NoiseDependantDecoratorConfiguration::new));
    }
}
