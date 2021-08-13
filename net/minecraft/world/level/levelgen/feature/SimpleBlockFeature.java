package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class SimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
    public SimpleBlockFeature(final Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final SimpleBlockConfiguration cmq) {
        if (cmq.placeOn.contains(bso.getBlockState(fx.below())) && cmq.placeIn.contains(bso.getBlockState(fx)) && cmq.placeUnder.contains(bso.getBlockState(fx.above()))) {
            bso.setBlock(fx, cmq.toPlace, 2);
            return true;
        }
        return false;
    }
}
