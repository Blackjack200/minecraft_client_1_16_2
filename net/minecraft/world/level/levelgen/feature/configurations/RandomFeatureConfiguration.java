package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.feature.WeightedConfiguredFeature;
import java.util.List;
import com.mojang.serialization.Codec;

public class RandomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<RandomFeatureConfiguration> CODEC;
    public final List<WeightedConfiguredFeature> features;
    public final Supplier<ConfiguredFeature<?, ?>> defaultFeature;
    
    public RandomFeatureConfiguration(final List<WeightedConfiguredFeature> list, final ConfiguredFeature<?, ?> cis) {
        this(list, (Supplier<ConfiguredFeature<?, ?>>)(() -> cis));
    }
    
    private RandomFeatureConfiguration(final List<WeightedConfiguredFeature> list, final Supplier<ConfiguredFeature<?, ?>> supplier) {
        this.features = list;
        this.defaultFeature = supplier;
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)Stream.concat(this.features.stream().flatMap(clg -> ((ConfiguredFeature)clg.feature.get()).getFeatures()), ((ConfiguredFeature)this.defaultFeature.get()).getFeatures());
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.apply2(RandomFeatureConfiguration::new, (App)WeightedConfiguredFeature.CODEC.listOf().fieldOf("features").forGetter(cmj -> cmj.features), (App)ConfiguredFeature.CODEC.fieldOf("default").forGetter(cmj -> cmj.defaultFeature)));
    }
}
