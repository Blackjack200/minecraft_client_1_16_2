package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class DecoratedDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<DecoratedDecoratorConfiguration> CODEC;
    private final ConfiguredDecorator<?> outer;
    private final ConfiguredDecorator<?> inner;
    
    public DecoratedDecoratorConfiguration(final ConfiguredDecorator<?> cpl1, final ConfiguredDecorator<?> cpl2) {
        this.outer = cpl1;
        this.inner = cpl2;
    }
    
    public ConfiguredDecorator<?> outer() {
        return this.outer;
    }
    
    public ConfiguredDecorator<?> inner() {
        return this.inner;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ConfiguredDecorator.CODEC.fieldOf("outer").forGetter(DecoratedDecoratorConfiguration::outer), (App)ConfiguredDecorator.CODEC.fieldOf("inner").forGetter(DecoratedDecoratorConfiguration::inner)).apply((Applicative)instance, DecoratedDecoratorConfiguration::new));
    }
}
