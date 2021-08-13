package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class RangeDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<RangeDecoratorConfiguration> CODEC;
    public final int bottomOffset;
    public final int topOffset;
    public final int maximum;
    
    public RangeDecoratorConfiguration(final int integer1, final int integer2, final int integer3) {
        this.bottomOffset = integer1;
        this.topOffset = integer2;
        this.maximum = integer3;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("bottom_offset").orElse(0).forGetter(cml -> cml.bottomOffset), (App)Codec.INT.fieldOf("top_offset").orElse(0).forGetter(cml -> cml.topOffset), (App)Codec.INT.fieldOf("maximum").orElse(0).forGetter(cml -> cml.maximum)).apply((Applicative)instance, RangeDecoratorConfiguration::new));
    }
}
