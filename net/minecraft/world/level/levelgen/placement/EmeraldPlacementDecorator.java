package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class EmeraldPlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
    public EmeraldPlacementDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        final int integer5 = 3 + random.nextInt(6);
        return (Stream<BlockPos>)IntStream.range(0, integer5).mapToObj(integer -> {
            final int integer2 = random.nextInt(16) + fx.getX();
            final int integer3 = random.nextInt(16) + fx.getZ();
            final int integer4 = random.nextInt(28) + 4;
            return new BlockPos(integer2, integer4, integer3);
        });
    }
}
