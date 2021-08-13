package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import java.util.BitSet;
import java.util.stream.IntStream;
import net.minecraft.world.level.ChunkPos;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public class CarvingMaskDecorator extends FeatureDecorator<CarvingMaskDecoratorConfiguration> {
    public CarvingMaskDecorator(final Codec<CarvingMaskDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final CarvingMaskDecoratorConfiguration cpi, final BlockPos fx) {
        final ChunkPos bra6 = new ChunkPos(fx);
        final BitSet bitSet7 = cps.getCarvingMask(bra6, cpi.step);
        return (Stream<BlockPos>)IntStream.range(0, bitSet7.length()).filter(integer -> bitSet7.get(integer) && random.nextFloat() < cpi.probability).mapToObj(integer -> {
            final int integer2 = integer & 0xF;
            final int integer3 = integer >> 4 & 0xF;
            final int integer4 = integer >> 8;
            return new BlockPos(bra6.getMinBlockX() + integer2, integer4, bra6.getMinBlockZ() + integer3);
        });
    }
}
