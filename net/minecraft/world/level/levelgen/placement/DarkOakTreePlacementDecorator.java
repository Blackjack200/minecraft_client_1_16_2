package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class DarkOakTreePlacementDecorator extends EdgeDecorator<NoneDecoratorConfiguration> {
    public DarkOakTreePlacementDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected Heightmap.Types type(final NoneDecoratorConfiguration cmd) {
        return Heightmap.Types.MOTION_BLOCKING;
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final NoneDecoratorConfiguration cmd, final BlockPos fx) {
        return (Stream<BlockPos>)IntStream.range(0, 16).mapToObj(integer -> {
            final int integer2 = integer / 4;
            final int integer3 = integer % 4;
            final int integer4 = integer2 * 4 + 1 + random.nextInt(3) + fx.getX();
            final int integer5 = integer3 * 4 + 1 + random.nextInt(3) + fx.getZ();
            final int integer6 = cps.getHeight(this.type(cmd), integer4, integer5);
            return new BlockPos(integer4, integer6, integer5);
        });
    }
}
