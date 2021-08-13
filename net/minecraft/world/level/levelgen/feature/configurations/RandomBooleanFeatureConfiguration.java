package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class RandomBooleanFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<RandomBooleanFeatureConfiguration> CODEC;
    public final Supplier<ConfiguredFeature<?, ?>> featureTrue;
    public final Supplier<ConfiguredFeature<?, ?>> featureFalse;
    
    public RandomBooleanFeatureConfiguration(final Supplier<ConfiguredFeature<?, ?>> supplier1, final Supplier<ConfiguredFeature<?, ?>> supplier2) {
        this.featureTrue = supplier1;
        this.featureFalse = supplier2;
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)Stream.concat(((ConfiguredFeature)this.featureTrue.get()).getFeatures(), ((ConfiguredFeature)this.featureFalse.get()).getFeatures());
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ConfiguredFeature.CODEC.fieldOf("feature_true").forGetter(cmi -> cmi.featureTrue), (App)ConfiguredFeature.CODEC.fieldOf("feature_false").forGetter(cmi -> cmi.featureFalse)).apply((Applicative)instance, RandomBooleanFeatureConfiguration::new));
    }
}
