package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class DepthAverageDecorator extends SimpleFeatureDecorator<DepthAverageConfigation> {
    public DepthAverageDecorator(final Codec<DepthAverageConfigation> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final DepthAverageConfigation cpt, final BlockPos fx) {
        final int integer5 = cpt.baseline;
        final int integer6 = cpt.spread;
        final int integer7 = fx.getX();
        final int integer8 = fx.getZ();
        final int integer9 = random.nextInt(integer6) + random.nextInt(integer6) - integer6 + integer5;
        return (Stream<BlockPos>)Stream.of(new BlockPos(integer7, integer9, integer8));
    }
}
