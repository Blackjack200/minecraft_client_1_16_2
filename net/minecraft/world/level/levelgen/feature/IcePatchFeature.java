package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import com.mojang.serialization.Codec;

public class IcePatchFeature extends BaseDiskFeature {
    public IcePatchFeature(final Codec<DiskConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, BlockPos fx, final DiskConfiguration clv) {
        while (bso.isEmptyBlock(fx) && fx.getY() > 2) {
            fx = fx.below();
        }
        return bso.getBlockState(fx).is(Blocks.SNOW_BLOCK) && super.place(bso, cfv, random, fx, clv);
    }
}
