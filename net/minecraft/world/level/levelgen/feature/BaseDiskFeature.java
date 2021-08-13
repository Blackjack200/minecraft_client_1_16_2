package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class BaseDiskFeature extends Feature<DiskConfiguration> {
    public BaseDiskFeature(final Codec<DiskConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final DiskConfiguration clv) {
        boolean boolean7 = false;
        for (int integer8 = clv.radius.sample(random), integer9 = fx.getX() - integer8; integer9 <= fx.getX() + integer8; ++integer9) {
            for (int integer10 = fx.getZ() - integer8; integer10 <= fx.getZ() + integer8; ++integer10) {
                final int integer11 = integer9 - fx.getX();
                final int integer12 = integer10 - fx.getZ();
                if (integer11 * integer11 + integer12 * integer12 <= integer8 * integer8) {
                    for (int integer13 = fx.getY() - clv.halfHeight; integer13 <= fx.getY() + clv.halfHeight; ++integer13) {
                        final BlockPos fx2 = new BlockPos(integer9, integer13, integer10);
                        final Block bul15 = bso.getBlockState(fx2).getBlock();
                        for (final BlockState cee17 : clv.targets) {
                            if (cee17.is(bul15)) {
                                bso.setBlock(fx2, clv.state, 2);
                                boolean7 = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return boolean7;
    }
}
