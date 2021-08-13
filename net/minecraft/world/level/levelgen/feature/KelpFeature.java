package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class KelpFeature extends Feature<NoneFeatureConfiguration> {
    public KelpFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        int integer7 = 0;
        final int integer8 = bso.getHeight(Heightmap.Types.OCEAN_FLOOR, fx.getX(), fx.getZ());
        BlockPos fx2 = new BlockPos(fx.getX(), integer8, fx.getZ());
        if (bso.getBlockState(fx2).is(Blocks.WATER)) {
            final BlockState cee10 = Blocks.KELP.defaultBlockState();
            final BlockState cee11 = Blocks.KELP_PLANT.defaultBlockState();
            for (int integer9 = 1 + random.nextInt(10), integer10 = 0; integer10 <= integer9; ++integer10) {
                if (bso.getBlockState(fx2).is(Blocks.WATER) && bso.getBlockState(fx2.above()).is(Blocks.WATER) && cee11.canSurvive(bso, fx2)) {
                    if (integer10 == integer9) {
                        bso.setBlock(fx2, ((StateHolder<O, BlockState>)cee10).<Comparable, Integer>setValue((Property<Comparable>)KelpBlock.AGE, random.nextInt(4) + 20), 2);
                        ++integer7;
                    }
                    else {
                        bso.setBlock(fx2, cee11, 2);
                    }
                }
                else if (integer10 > 0) {
                    final BlockPos fx3 = fx2.below();
                    if (cee10.canSurvive(bso, fx3) && !bso.getBlockState(fx3.below()).is(Blocks.KELP)) {
                        bso.setBlock(fx3, ((StateHolder<O, BlockState>)cee10).<Comparable, Integer>setValue((Property<Comparable>)KelpBlock.AGE, random.nextInt(4) + 20), 2);
                        ++integer7;
                        break;
                    }
                    break;
                }
                fx2 = fx2.above();
            }
        }
        return integer7 > 0;
    }
}
