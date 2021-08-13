package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class NoiseCountFactorDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<NoiseCountFactorDecoratorConfiguration> CODEC;
    public final int noiseToCountRatio;
    public final double noiseFactor;
    public final double noiseOffset;
    
    public NoiseCountFactorDecoratorConfiguration(final int integer, final double double2, final double double3) {
        this.noiseToCountRatio = integer;
        this.noiseFactor = double2;
        this.noiseOffset = double3;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("noise_to_count_ratio").forGetter(cqi -> cqi.noiseToCountRatio), (App)Codec.DOUBLE.fieldOf("noise_factor").forGetter(cqi -> cqi.noiseFactor), (App)Codec.DOUBLE.fieldOf("noise_offset").orElse(0.0).forGetter(cqi -> cqi.noiseOffset)).apply((Applicative)instance, NoiseCountFactorDecoratorConfiguration::new));
    }
}
