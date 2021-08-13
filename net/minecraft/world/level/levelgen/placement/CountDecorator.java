package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;

public class CountDecorator extends SimpleFeatureDecorator<CountConfiguration> {
    public CountDecorator(final Codec<CountConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final CountConfiguration clr, final BlockPos fx) {
        return (Stream<BlockPos>)IntStream.range(0, clr.count().sample(random)).mapToObj(integer -> fx);
    }
}
