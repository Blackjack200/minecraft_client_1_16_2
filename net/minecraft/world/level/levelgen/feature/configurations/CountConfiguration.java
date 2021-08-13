package net.minecraft.world.level.levelgen.feature.configurations;

import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public class CountConfiguration implements DecoratorConfiguration, FeatureConfiguration {
    public static final Codec<CountConfiguration> CODEC;
    private final UniformInt count;
    
    public CountConfiguration(final int integer) {
        this.count = UniformInt.fixed(integer);
    }
    
    public CountConfiguration(final UniformInt aft) {
        this.count = aft;
    }
    
    public UniformInt count() {
        return this.count;
    }
    
    static {
        CODEC = UniformInt.codec(-10, 128, 128).fieldOf("count").xmap(CountConfiguration::new, CountConfiguration::count).codec();
    }
}
