package net.minecraft.world.level.levelgen.carver;

import com.mojang.serialization.Codec;

public class NoneCarverConfiguration implements CarverConfiguration {
    public static final Codec<NoneCarverConfiguration> CODEC;
    public static final NoneCarverConfiguration INSTANCE;
    
    static {
        CODEC = Codec.unit(() -> NoneCarverConfiguration.INSTANCE);
        INSTANCE = new NoneCarverConfiguration();
    }
}
