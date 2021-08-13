package net.minecraft.world.level.levelgen.placement.nether;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.placement.SimpleFeatureDecorator;

public class FireDecorator extends SimpleFeatureDecorator<CountConfiguration> {
    public FireDecorator(final Codec<CountConfiguration> codec) {
        super(codec);
    }
    
    public Stream<BlockPos> place(final Random random, final CountConfiguration clr, final BlockPos fx) {
        final List<BlockPos> list5 = (List<BlockPos>)Lists.newArrayList();
        for (int integer6 = 0; integer6 < random.nextInt(random.nextInt(clr.count().sample(random)) + 1) + 1; ++integer6) {
            final int integer7 = random.nextInt(16) + fx.getX();
            final int integer8 = random.nextInt(16) + fx.getZ();
            final int integer9 = random.nextInt(120) + 4;
            list5.add(new BlockPos(integer7, integer9, integer8));
        }
        return (Stream<BlockPos>)list5.stream();
    }
}
