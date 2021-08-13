package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;

public interface Decoratable<R> {
    R decorated(final ConfiguredDecorator<?> cpl);
    
    default R chance(final int integer) {
        return this.decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(integer)));
    }
    
    default R count(final UniformInt aft) {
        return this.decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(aft)));
    }
    
    default R count(final int integer) {
        return this.count(UniformInt.fixed(integer));
    }
    
    default R countRandom(final int integer) {
        return this.count(UniformInt.of(0, integer));
    }
    
    default R range(final int integer) {
        return this.decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(0, 0, integer)));
    }
    
    default R squared() {
        return this.decorated(FeatureDecorator.SQUARE.configured(NoneDecoratorConfiguration.INSTANCE));
    }
}
