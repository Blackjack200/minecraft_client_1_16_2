package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class EndIslandPlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
    public EndIslandPlacementDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        Stream<BlockPos> stream5 = (Stream<BlockPos>)Stream.empty();
        if (random.nextInt(14) == 0) {
            stream5 = (Stream<BlockPos>)Stream.concat((Stream)stream5, Stream.of(fx.offset(random.nextInt(16), 55 + random.nextInt(16), random.nextInt(16))));
            if (random.nextInt(4) == 0) {
                stream5 = (Stream<BlockPos>)Stream.concat((Stream)stream5, Stream.of(fx.offset(random.nextInt(16), 55 + random.nextInt(16), random.nextInt(16))));
            }
            return stream5;
        }
        return (Stream<BlockPos>)Stream.empty();
    }
}
