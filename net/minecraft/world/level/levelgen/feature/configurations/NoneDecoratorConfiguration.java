package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;

public class NoneDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<NoneDecoratorConfiguration> CODEC;
    public static final NoneDecoratorConfiguration INSTANCE;
    
    static {
        CODEC = Codec.unit(() -> NoneDecoratorConfiguration.INSTANCE);
        INSTANCE = new NoneDecoratorConfiguration();
    }
}
