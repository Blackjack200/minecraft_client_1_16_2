package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class IceSpikeFeature extends Feature<NoneFeatureConfiguration> {
    public IceSpikeFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, BlockPos fx, final NoneFeatureConfiguration cme) {
        while (bso.isEmptyBlock(fx) && fx.getY() > 2) {
            fx = fx.below();
        }
        if (!bso.getBlockState(fx).is(Blocks.SNOW_BLOCK)) {
            return false;
        }
        fx = fx.above(random.nextInt(4));
        final int integer7 = random.nextInt(4) + 7;
        final int integer8 = integer7 / 4 + random.nextInt(2);
        if (integer8 > 1 && random.nextInt(60) == 0) {
            fx = fx.above(10 + random.nextInt(30));
        }
        for (int integer9 = 0; integer9 < integer7; ++integer9) {
            final float float10 = (1.0f - integer9 / (float)integer7) * integer8;
            for (int integer10 = Mth.ceil(float10), integer11 = -integer10; integer11 <= integer10; ++integer11) {
                final float float11 = Mth.abs(integer11) - 0.25f;
                for (int integer12 = -integer10; integer12 <= integer10; ++integer12) {
                    final float float12 = Mth.abs(integer12) - 0.25f;
                    if ((integer11 == 0 && integer12 == 0) || float11 * float11 + float12 * float12 <= float10 * float10) {
                        if ((integer11 != -integer10 && integer11 != integer10 && integer12 != -integer10 && integer12 != integer10) || random.nextFloat() <= 0.75f) {
                            BlockState cee16 = bso.getBlockState(fx.offset(integer11, integer9, integer12));
                            Block bul17 = cee16.getBlock();
                            if (cee16.isAir() || Feature.isDirt(bul17) || bul17 == Blocks.SNOW_BLOCK || bul17 == Blocks.ICE) {
                                this.setBlock(bso, fx.offset(integer11, integer9, integer12), Blocks.PACKED_ICE.defaultBlockState());
                            }
                            if (integer9 != 0 && integer10 > 1) {
                                cee16 = bso.getBlockState(fx.offset(integer11, -integer9, integer12));
                                bul17 = cee16.getBlock();
                                if (cee16.isAir() || Feature.isDirt(bul17) || bul17 == Blocks.SNOW_BLOCK || bul17 == Blocks.ICE) {
                                    this.setBlock(bso, fx.offset(integer11, -integer9, integer12), Blocks.PACKED_ICE.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
        }
        int integer9 = integer8 - 1;
        if (integer9 < 0) {
            integer9 = 0;
        }
        else if (integer9 > 1) {
            integer9 = 1;
        }
        for (int integer13 = -integer9; integer13 <= integer9; ++integer13) {
            for (int integer10 = -integer9; integer10 <= integer9; ++integer10) {
                BlockPos fx2 = fx.offset(integer13, -1, integer10);
                int integer14 = 50;
                if (Math.abs(integer13) == 1 && Math.abs(integer10) == 1) {
                    integer14 = random.nextInt(5);
                }
                while (fx2.getY() > 50) {
                    final BlockState cee17 = bso.getBlockState(fx2);
                    final Block bul18 = cee17.getBlock();
                    if (!cee17.isAir() && !Feature.isDirt(bul18) && bul18 != Blocks.SNOW_BLOCK && bul18 != Blocks.ICE && bul18 != Blocks.PACKED_ICE) {
                        break;
                    }
                    this.setBlock(bso, fx2, Blocks.PACKED_ICE.defaultBlockState());
                    fx2 = fx2.below();
                    if (--integer14 > 0) {
                        continue;
                    }
                    fx2 = fx2.below(random.nextInt(5) + 1);
                    integer14 = random.nextInt(5);
                }
            }
        }
        return true;
    }
}
