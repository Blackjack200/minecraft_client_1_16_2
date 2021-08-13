package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;

public class SeaPickleFeature extends Feature<CountConfiguration> {
    public SeaPickleFeature(final Codec<CountConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final CountConfiguration clr) {
        int integer7 = 0;
        for (int integer8 = clr.count().sample(random), integer9 = 0; integer9 < integer8; ++integer9) {
            final int integer10 = random.nextInt(8) - random.nextInt(8);
            final int integer11 = random.nextInt(8) - random.nextInt(8);
            final int integer12 = bso.getHeight(Heightmap.Types.OCEAN_FLOOR, fx.getX() + integer10, fx.getZ() + integer11);
            final BlockPos fx2 = new BlockPos(fx.getX() + integer10, integer12, fx.getZ() + integer11);
            final BlockState cee14 = ((StateHolder<O, BlockState>)Blocks.SEA_PICKLE.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)SeaPickleBlock.PICKLES, random.nextInt(4) + 1);
            if (bso.getBlockState(fx2).is(Blocks.WATER) && cee14.canSurvive(bso, fx2)) {
                bso.setBlock(fx2, cee14, 2);
                ++integer7;
            }
        }
        return integer7 > 0;
    }
}
