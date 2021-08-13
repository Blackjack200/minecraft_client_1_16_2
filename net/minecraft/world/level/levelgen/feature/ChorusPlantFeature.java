package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ChorusPlantFeature extends Feature<NoneFeatureConfiguration> {
    public ChorusPlantFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        if (bso.isEmptyBlock(fx) && bso.getBlockState(fx.below()).is(Blocks.END_STONE)) {
            ChorusFlowerBlock.generatePlant(bso, fx, random, 8);
            return true;
        }
        return false;
    }
}
