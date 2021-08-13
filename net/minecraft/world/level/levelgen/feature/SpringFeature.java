package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;

public class SpringFeature extends Feature<SpringConfiguration> {
    public SpringFeature(final Codec<SpringConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final SpringConfiguration cmt) {
        if (!cmt.validBlocks.contains(bso.getBlockState(fx.above()).getBlock())) {
            return false;
        }
        if (cmt.requiresBlockBelow && !cmt.validBlocks.contains(bso.getBlockState(fx.below()).getBlock())) {
            return false;
        }
        final BlockState cee7 = bso.getBlockState(fx);
        if (!cee7.isAir() && !cmt.validBlocks.contains(cee7.getBlock())) {
            return false;
        }
        int integer8 = 0;
        int integer9 = 0;
        if (cmt.validBlocks.contains(bso.getBlockState(fx.west()).getBlock())) {
            ++integer9;
        }
        if (cmt.validBlocks.contains(bso.getBlockState(fx.east()).getBlock())) {
            ++integer9;
        }
        if (cmt.validBlocks.contains(bso.getBlockState(fx.north()).getBlock())) {
            ++integer9;
        }
        if (cmt.validBlocks.contains(bso.getBlockState(fx.south()).getBlock())) {
            ++integer9;
        }
        if (cmt.validBlocks.contains(bso.getBlockState(fx.below()).getBlock())) {
            ++integer9;
        }
        int integer10 = 0;
        if (bso.isEmptyBlock(fx.west())) {
            ++integer10;
        }
        if (bso.isEmptyBlock(fx.east())) {
            ++integer10;
        }
        if (bso.isEmptyBlock(fx.north())) {
            ++integer10;
        }
        if (bso.isEmptyBlock(fx.south())) {
            ++integer10;
        }
        if (bso.isEmptyBlock(fx.below())) {
            ++integer10;
        }
        if (integer9 == cmt.rockCount && integer10 == cmt.holeCount) {
            bso.setBlock(fx, cmt.state.createLegacyBlock(), 2);
            bso.getLiquidTicks().scheduleTick(fx, cmt.state.getType(), 0);
            ++integer8;
        }
        return integer8 > 0;
    }
}
