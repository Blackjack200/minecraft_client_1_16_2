package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;

public class NetherForestVegetationFeature extends Feature<BlockPileConfiguration> {
    public NetherForestVegetationFeature(final Codec<BlockPileConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final BlockPileConfiguration clo) {
        return place(bso, random, fx, clo, 8, 4);
    }
    
    public static boolean place(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockPileConfiguration clo, final int integer5, final int integer6) {
        final Block bul7 = brv.getBlockState(fx.below()).getBlock();
        if (!bul7.is(BlockTags.NYLIUM)) {
            return false;
        }
        final int integer7 = fx.getY();
        if (integer7 < 1 || integer7 + 1 >= 256) {
            return false;
        }
        int integer8 = 0;
        for (int integer9 = 0; integer9 < integer5 * integer5; ++integer9) {
            final BlockPos fx2 = fx.offset(random.nextInt(integer5) - random.nextInt(integer5), random.nextInt(integer6) - random.nextInt(integer6), random.nextInt(integer5) - random.nextInt(integer5));
            final BlockState cee12 = clo.stateProvider.getState(random, fx2);
            if (brv.isEmptyBlock(fx2) && fx2.getY() > 0 && cee12.canSurvive(brv, fx2)) {
                brv.setBlock(fx2, cee12, 2);
                ++integer8;
            }
        }
        return integer8 > 0;
    }
}
