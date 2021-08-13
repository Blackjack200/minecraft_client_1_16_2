package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class DecoratedDecorator extends FeatureDecorator<DecoratedDecoratorConfiguration> {
    public DecoratedDecorator(final Codec<DecoratedDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final DecoratedDecoratorConfiguration cpr, final BlockPos fx) {
        return (Stream<BlockPos>)cpr.outer().getPositions(cps, random, fx).flatMap(fx -> cpr.inner().getPositions(cps, random, fx));
    }
}
