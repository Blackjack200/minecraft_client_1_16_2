package net.minecraft.world.level.levelgen.feature.configurations;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.stream.Stream;

public interface FeatureConfiguration {
    public static final NoneFeatureConfiguration NONE = NoneFeatureConfiguration.INSTANCE;
    
    default Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)Stream.empty();
    }
}
