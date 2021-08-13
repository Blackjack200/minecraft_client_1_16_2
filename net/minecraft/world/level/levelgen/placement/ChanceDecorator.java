package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class ChanceDecorator extends SimpleFeatureDecorator<ChanceDecoratorConfiguration> {
    public ChanceDecorator(final Codec<ChanceDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final ChanceDecoratorConfiguration cpk, final BlockPos fx) {
        if (random.nextFloat() < 1.0f / cpk.chance) {
            return (Stream<BlockPos>)Stream.of(fx);
        }
        return (Stream<BlockPos>)Stream.empty();
    }
}
