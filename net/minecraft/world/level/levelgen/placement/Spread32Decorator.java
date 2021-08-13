package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class Spread32Decorator extends FeatureDecorator<NoneDecoratorConfiguration> {
    public Spread32Decorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        final int integer6 = random.nextInt(fx.getY() + 32);
        return (Stream<BlockPos>)Stream.of(new BlockPos(fx.getX(), integer6, fx.getZ()));
    }
}
