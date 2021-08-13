package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class BlockBlobFeature extends Feature<BlockStateConfiguration> {
    public BlockBlobFeature(final Codec<BlockStateConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, BlockPos fx, final BlockStateConfiguration clp) {
        while (fx.getY() > 3) {
            if (!bso.isEmptyBlock(fx.below())) {
                final Block bul7 = bso.getBlockState(fx.below()).getBlock();
                if (Feature.isDirt(bul7)) {
                    break;
                }
                if (Feature.isStone(bul7)) {
                    break;
                }
            }
            fx = fx.below();
        }
        if (fx.getY() <= 3) {
            return false;
        }
        for (int integer7 = 0; integer7 < 3; ++integer7) {
            final int integer8 = random.nextInt(2);
            final int integer9 = random.nextInt(2);
            final int integer10 = random.nextInt(2);
            final float float11 = (integer8 + integer9 + integer10) * 0.333f + 0.5f;
            for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-integer8, -integer9, -integer10), fx.offset(integer8, integer9, integer10))) {
                if (fx2.distSqr(fx) <= float11 * float11) {
                    bso.setBlock(fx2, clp.state, 4);
                }
            }
            fx = fx.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
        }
        return true;
    }
}
