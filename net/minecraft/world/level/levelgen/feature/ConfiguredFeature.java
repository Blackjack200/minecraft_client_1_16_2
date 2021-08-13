package net.minecraft.world.level.levelgen.feature;

import org.apache.logging.log4j.LogManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Registry;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.Decoratable;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ConfiguredFeature<FC extends FeatureConfiguration, F extends Feature<FC>> implements Decoratable<ConfiguredFeature<?, ?>> {
    public static final Codec<ConfiguredFeature<?, ?>> DIRECT_CODEC;
    public static final Codec<Supplier<ConfiguredFeature<?, ?>>> CODEC;
    public static final Codec<List<Supplier<ConfiguredFeature<?, ?>>>> LIST_CODEC;
    public static final Logger LOGGER;
    public final F feature;
    public final FC config;
    
    public ConfiguredFeature(final F cji, final FC clx) {
        this.feature = cji;
        this.config = clx;
    }
    
    public F feature() {
        return this.feature;
    }
    
    public FC config() {
        return this.config;
    }
    
    public ConfiguredFeature<?, ?> decorated(final ConfiguredDecorator<?> cpl) {
        return Feature.DECORATED.configured(new DecoratedFeatureConfiguration((Supplier<ConfiguredFeature<?, ?>>)(() -> this), cpl));
    }
    
    public WeightedConfiguredFeature weighted(final float float1) {
        return new WeightedConfiguredFeature(this, float1);
    }
    
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx) {
        return this.feature.place(bso, cfv, random, fx, this.config);
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)Stream.concat(Stream.of(this), (Stream)this.config.getFeatures());
    }
    
    static {
        DIRECT_CODEC = Registry.FEATURE.dispatch(cis -> cis.feature, Feature::configuredCodec);
        CODEC = (Codec)RegistryFileCodec.<ConfiguredFeature<?, ?>>create(Registry.CONFIGURED_FEATURE_REGISTRY, ConfiguredFeature.DIRECT_CODEC);
        LIST_CODEC = RegistryFileCodec.<ConfiguredFeature<?, ?>>homogeneousList(Registry.CONFIGURED_FEATURE_REGISTRY, ConfiguredFeature.DIRECT_CODEC);
        LOGGER = LogManager.getLogger();
    }
}
