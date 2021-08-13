package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class LakeLavaPlacementDecorator extends FeatureDecorator<ChanceDecoratorConfiguration> {
    public LakeLavaPlacementDecorator(final Codec<ChanceDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final ChanceDecoratorConfiguration cpk, final BlockPos fx) {
        if (random.nextInt(cpk.chance / 10) == 0) {
            final int integer6 = random.nextInt(16) + fx.getX();
            final int integer7 = random.nextInt(16) + fx.getZ();
            final int integer8 = random.nextInt(random.nextInt(cps.getGenDepth() - 8) + 8);
            if (integer8 < cps.getSeaLevel() || random.nextInt(cpk.chance / 8) == 0) {
                return (Stream<BlockPos>)Stream.of(new BlockPos(integer6, integer8, integer7));
            }
        }
        return (Stream<BlockPos>)Stream.empty();
    }
}
