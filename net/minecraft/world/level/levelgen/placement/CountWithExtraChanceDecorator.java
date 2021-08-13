package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class CountWithExtraChanceDecorator extends SimpleFeatureDecorator<FrequencyWithExtraChanceDecoratorConfiguration> {
    public CountWithExtraChanceDecorator(final Codec<FrequencyWithExtraChanceDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final FrequencyWithExtraChanceDecoratorConfiguration cqa, final BlockPos fx) {
        final int integer5 = cqa.count + ((random.nextFloat() < cqa.extraChance) ? cqa.extraCount : 0);
        return (Stream<BlockPos>)IntStream.range(0, integer5).mapToObj(integer -> fx);
    }
}
