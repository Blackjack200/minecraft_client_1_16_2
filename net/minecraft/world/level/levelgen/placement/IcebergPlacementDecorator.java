package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class IcebergPlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
    public IcebergPlacementDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        final int integer5 = random.nextInt(8) + 4 + fx.getX();
        final int integer6 = random.nextInt(8) + 4 + fx.getZ();
        return (Stream<BlockPos>)Stream.of(new BlockPos(integer5, fx.getY(), integer6));
    }
}
