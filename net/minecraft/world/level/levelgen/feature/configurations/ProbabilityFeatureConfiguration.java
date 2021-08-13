package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;

public class ProbabilityFeatureConfiguration implements CarverConfiguration, FeatureConfiguration {
    public static final Codec<ProbabilityFeatureConfiguration> CODEC;
    public final float probability;
    
    public ProbabilityFeatureConfiguration(final float float1) {
        this.probability = float1;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.floatRange(0.0f, 1.0f).fieldOf("probability").forGetter(cmh -> cmh.probability)).apply((Applicative)instance, ProbabilityFeatureConfiguration::new));
    }
}
