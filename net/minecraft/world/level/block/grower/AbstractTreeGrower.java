package net.minecraft.world.level.block.grower;

import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.Random;

public abstract class AbstractTreeGrower {
    @Nullable
    protected abstract ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(final Random random, final boolean boolean2);
    
    public boolean growTree(final ServerLevel aag, final ChunkGenerator cfv, final BlockPos fx, final BlockState cee, final Random random) {
        final ConfiguredFeature<TreeConfiguration, ?> cis7 = this.getConfiguredFeature(random, this.hasFlowers(aag, fx));
        if (cis7 == null) {
            return false;
        }
        aag.setBlock(fx, Blocks.AIR.defaultBlockState(), 4);
        cis7.config.setFromSapling();
        if (cis7.place(aag, cfv, random, fx)) {
            return true;
        }
        aag.setBlock(fx, cee, 4);
        return false;
    }
    
    private boolean hasFlowers(final LevelAccessor brv, final BlockPos fx) {
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.below().north(2).west(2), fx.above().south(2).east(2))) {
            if (brv.getBlockState(fx2).is(BlockTags.FLOWERS)) {
                return true;
            }
        }
        return false;
    }
}
