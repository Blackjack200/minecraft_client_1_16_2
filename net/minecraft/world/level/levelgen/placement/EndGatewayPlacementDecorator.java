package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class EndGatewayPlacementDecorator extends FeatureDecorator<NoneDecoratorConfiguration> {
    public EndGatewayPlacementDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        if (random.nextInt(700) == 0) {
            final int integer6 = random.nextInt(16) + fx.getX();
            final int integer7 = random.nextInt(16) + fx.getZ();
            final int integer8 = cps.getHeight(Heightmap.Types.MOTION_BLOCKING, integer6, integer7);
            if (integer8 > 0) {
                final int integer9 = integer8 + 3 + random.nextInt(7);
                return (Stream<BlockPos>)Stream.of(new BlockPos(integer6, integer9, integer7));
            }
        }
        return (Stream<BlockPos>)Stream.empty();
    }
}
