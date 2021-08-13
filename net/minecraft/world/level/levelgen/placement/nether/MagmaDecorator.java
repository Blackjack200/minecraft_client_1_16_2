package net.minecraft.world.level.levelgen.placement.nether;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class MagmaDecorator extends FeatureDecorator<NoneDecoratorConfiguration> {
    public MagmaDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        final int integer6 = cps.getSeaLevel();
        final int integer7 = integer6 - 5 + random.nextInt(10);
        return (Stream<BlockPos>)Stream.of(new BlockPos(fx.getX(), integer7, fx.getZ()));
    }
}
