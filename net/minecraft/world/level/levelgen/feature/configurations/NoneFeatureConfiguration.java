package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;

public class NoneFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<NoneFeatureConfiguration> CODEC;
    public static final NoneFeatureConfiguration INSTANCE;
    
    static {
        CODEC = Codec.unit(() -> NoneFeatureConfiguration.INSTANCE);
        INSTANCE = new NoneFeatureConfiguration();
    }
}
