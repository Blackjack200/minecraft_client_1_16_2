package net.minecraft.world.level.levelgen.placement.nether;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import java.util.List;
import net.minecraft.world.level.levelgen.Heightmap;
import com.google.common.collect.Lists;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class CountMultiLayerDecorator extends FeatureDecorator<CountConfiguration> {
    public CountMultiLayerDecorator(final Codec<CountConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final CountConfiguration clr, final BlockPos fx) {
        final List<BlockPos> list6 = (List<BlockPos>)Lists.newArrayList();
        int integer8 = 0;
        boolean boolean7;
        do {
            boolean7 = false;
            for (int integer9 = 0; integer9 < clr.count().sample(random); ++integer9) {
                final int integer10 = random.nextInt(16) + fx.getX();
                final int integer11 = random.nextInt(16) + fx.getZ();
                final int integer12 = cps.getHeight(Heightmap.Types.MOTION_BLOCKING, integer10, integer11);
                final int integer13 = findOnGroundYPosition(cps, integer10, integer12, integer11, integer8);
                if (integer13 != Integer.MAX_VALUE) {
                    list6.add(new BlockPos(integer10, integer13, integer11));
                    boolean7 = true;
                }
            }
            ++integer8;
        } while (boolean7);
        return (Stream<BlockPos>)list6.stream();
    }
    
    private static int findOnGroundYPosition(final DecorationContext cps, final int integer2, final int integer3, final int integer4, final int integer5) {
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos(integer2, integer3, integer4);
        int integer6 = 0;
        BlockState cee8 = cps.getBlockState(a6);
        for (int integer7 = integer3; integer7 >= 1; --integer7) {
            a6.setY(integer7 - 1);
            final BlockState cee9 = cps.getBlockState(a6);
            if (!isEmpty(cee9) && isEmpty(cee8) && !cee9.is(Blocks.BEDROCK)) {
                if (integer6 == integer5) {
                    return a6.getY() + 1;
                }
                ++integer6;
            }
            cee8 = cee9;
        }
        return Integer.MAX_VALUE;
    }
    
    private static boolean isEmpty(final BlockState cee) {
        return cee.isAir() || cee.is(Blocks.WATER) || cee.is(Blocks.LAVA);
    }
}
