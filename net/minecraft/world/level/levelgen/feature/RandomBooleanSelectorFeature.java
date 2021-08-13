package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;

public class RandomBooleanSelectorFeature extends Feature<RandomBooleanFeatureConfiguration> {
    public RandomBooleanSelectorFeature(final Codec<RandomBooleanFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final RandomBooleanFeatureConfiguration cmi) {
        final boolean boolean7 = random.nextBoolean();
        if (boolean7) {
            return ((ConfiguredFeature)cmi.featureTrue.get()).place(bso, cfv, random, fx);
        }
        return ((ConfiguredFeature)cmi.featureFalse.get()).place(bso, cfv, random, fx);
    }
}
