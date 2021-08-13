package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import net.minecraft.world.level.biome.Biome;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class NoiseBasedDecorator extends SimpleFeatureDecorator<NoiseCountFactorDecoratorConfiguration> {
    public NoiseBasedDecorator(final Codec<NoiseCountFactorDecoratorConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final NoiseCountFactorDecoratorConfiguration cqi, final BlockPos fx) {
        final double double5 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() / cqi.noiseFactor, fx.getZ() / cqi.noiseFactor, false);
        final int integer7 = (int)Math.ceil((double5 + cqi.noiseOffset) * cqi.noiseToCountRatio);
        return (Stream<BlockPos>)IntStream.range(0, integer7).mapToObj(integer -> fx);
    }
}
