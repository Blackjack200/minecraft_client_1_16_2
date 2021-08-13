package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import net.minecraft.world.level.biome.Biome;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;

public class CountNoiseDecorator extends FeatureDecorator<NoiseDependantDecoratorConfiguration> {
    public CountNoiseDecorator(final Codec<NoiseDependantDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final NoiseDependantDecoratorConfiguration cmc, final BlockPos fx) {
        final double double6 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() / 200.0, fx.getZ() / 200.0, false);
        final int integer8 = (double6 < cmc.noiseLevel) ? cmc.belowNoise : cmc.aboveNoise;
        return (Stream<BlockPos>)IntStream.range(0, integer8).mapToObj(integer -> fx);
    }
}
