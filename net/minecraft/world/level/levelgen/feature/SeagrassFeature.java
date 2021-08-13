package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.TallSeagrass;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class SeagrassFeature extends Feature<ProbabilityFeatureConfiguration> {
    public SeagrassFeature(final Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final ProbabilityFeatureConfiguration cmh) {
        boolean boolean7 = false;
        final int integer8 = random.nextInt(8) - random.nextInt(8);
        final int integer9 = random.nextInt(8) - random.nextInt(8);
        final int integer10 = bso.getHeight(Heightmap.Types.OCEAN_FLOOR, fx.getX() + integer8, fx.getZ() + integer9);
        final BlockPos fx2 = new BlockPos(fx.getX() + integer8, integer10, fx.getZ() + integer9);
        if (bso.getBlockState(fx2).is(Blocks.WATER)) {
            final boolean boolean8 = random.nextDouble() < cmh.probability;
            final BlockState cee13 = boolean8 ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
            if (cee13.canSurvive(bso, fx2)) {
                if (boolean8) {
                    final BlockState cee14 = ((StateHolder<O, BlockState>)cee13).<DoubleBlockHalf, DoubleBlockHalf>setValue(TallSeagrass.HALF, DoubleBlockHalf.UPPER);
                    final BlockPos fx3 = fx2.above();
                    if (bso.getBlockState(fx3).is(Blocks.WATER)) {
                        bso.setBlock(fx2, cee13, 2);
                        bso.setBlock(fx3, cee14, 2);
                    }
                }
                else {
                    bso.setBlock(fx2, cee13, 2);
                }
                boolean7 = true;
            }
        }
        return boolean7;
    }
}
