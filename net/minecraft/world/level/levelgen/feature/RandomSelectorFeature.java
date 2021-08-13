package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;

public class RandomSelectorFeature extends Feature<RandomFeatureConfiguration> {
    public RandomSelectorFeature(final Codec<RandomFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final RandomFeatureConfiguration cmj) {
        for (final WeightedConfiguredFeature clg8 : cmj.features) {
            if (random.nextFloat() < clg8.chance) {
                return clg8.place(bso, cfv, random, fx);
            }
        }
        return ((ConfiguredFeature)cmj.defaultFeature.get()).place(bso, cfv, random, fx);
    }
}
