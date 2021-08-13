package net.minecraft.world.level.levelgen.feature.featuresize;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public class FeatureSizeType<P extends FeatureSize> {
    public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE;
    public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE;
    private final Codec<P> codec;
    
    private static <P extends FeatureSize> FeatureSizeType<P> register(final String string, final Codec<P> codec) {
        return Registry.<FeatureSizeType<P>>register(Registry.FEATURE_SIZE_TYPES, string, new FeatureSizeType<P>(codec));
    }
    
    private FeatureSizeType(final Codec<P> codec) {
        this.codec = codec;
    }
    
    public Codec<P> codec() {
        return this.codec;
    }
    
    static {
        TWO_LAYERS_FEATURE_SIZE = FeatureSizeType.<TwoLayersFeatureSize>register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);
        THREE_LAYERS_FEATURE_SIZE = FeatureSizeType.<ThreeLayersFeatureSize>register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);
    }
}
