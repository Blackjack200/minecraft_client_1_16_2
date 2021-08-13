package net.minecraft.world.level.levelgen.placement.nether;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.placement.SimpleFeatureDecorator;

public class GlowstoneDecorator extends SimpleFeatureDecorator<CountConfiguration> {
    public GlowstoneDecorator(final Codec<CountConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final CountConfiguration clr, final BlockPos fx) {
        return (Stream<BlockPos>)IntStream.range(0, random.nextInt(random.nextInt(clr.count().sample(random)) + 1)).mapToObj(integer -> {
            final int integer2 = random.nextInt(16) + fx.getX();
            final int integer3 = random.nextInt(16) + fx.getZ();
            final int integer4 = random.nextInt(120) + 4;
            return new BlockPos(integer2, integer4, integer3);
        });
    }
}
