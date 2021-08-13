package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import java.util.List;
import com.mojang.serialization.Codec;

public class SimpleRandomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<SimpleRandomFeatureConfiguration> CODEC;
    public final List<Supplier<ConfiguredFeature<?, ?>>> features;
    
    public SimpleRandomFeatureConfiguration(final List<Supplier<ConfiguredFeature<?, ?>>> list) {
        this.features = list;
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)this.features.stream().flatMap(supplier -> ((ConfiguredFeature)supplier.get()).getFeatures());
    }
    
    static {
        CODEC = ConfiguredFeature.LIST_CODEC.fieldOf("features").xmap(SimpleRandomFeatureConfiguration::new, cmr -> cmr.features).codec();
    }
}
