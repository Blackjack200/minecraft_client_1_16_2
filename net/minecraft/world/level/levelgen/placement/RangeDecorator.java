package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;

public class RangeDecorator extends SimpleFeatureDecorator<RangeDecoratorConfiguration> {
    public RangeDecorator(final Codec<RangeDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final RangeDecoratorConfiguration cml, final BlockPos fx) {
        final int integer5 = fx.getX();
        final int integer6 = fx.getZ();
        final int integer7 = random.nextInt(cml.maximum - cml.topOffset) + cml.bottomOffset;
        return (Stream<BlockPos>)Stream.of(new BlockPos(integer5, integer7, integer6));
    }
}
