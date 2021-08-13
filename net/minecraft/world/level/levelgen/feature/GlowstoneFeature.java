package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GlowstoneFeature extends Feature<NoneFeatureConfiguration> {
    public GlowstoneFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        if (!bso.isEmptyBlock(fx)) {
            return false;
        }
        final BlockState cee7 = bso.getBlockState(fx.above());
        if (!cee7.is(Blocks.NETHERRACK) && !cee7.is(Blocks.BASALT) && !cee7.is(Blocks.BLACKSTONE)) {
            return false;
        }
        bso.setBlock(fx, Blocks.GLOWSTONE.defaultBlockState(), 2);
        for (int integer8 = 0; integer8 < 1500; ++integer8) {
            final BlockPos fx2 = fx.offset(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
            if (bso.getBlockState(fx2).isAir()) {
                int integer9 = 0;
                for (final Direction gc14 : Direction.values()) {
                    if (bso.getBlockState(fx2.relative(gc14)).is(Blocks.GLOWSTONE)) {
                        ++integer9;
                    }
                    if (integer9 > 1) {
                        break;
                    }
                }
                if (integer9 == 1) {
                    bso.setBlock(fx2, Blocks.GLOWSTONE.defaultBlockState(), 2);
                }
            }
        }
        return true;
    }
}
