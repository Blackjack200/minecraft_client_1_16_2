package net.minecraft.world.level.block.grower;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.server.level.ServerLevel;

public abstract class AbstractMegaTreeGrower extends AbstractTreeGrower {
    @Override
    public boolean growTree(final ServerLevel aag, final ChunkGenerator cfv, final BlockPos fx, final BlockState cee, final Random random) {
        for (int integer7 = 0; integer7 >= -1; --integer7) {
            for (int integer8 = 0; integer8 >= -1; --integer8) {
                if (isTwoByTwoSapling(cee, aag, fx, integer7, integer8)) {
                    return this.placeMega(aag, cfv, fx, cee, random, integer7, integer8);
                }
            }
        }
        return super.growTree(aag, cfv, fx, cee, random);
    }
    
    @Nullable
    protected abstract ConfiguredFeature<TreeConfiguration, ?> getConfiguredMegaFeature(final Random random);
    
    public boolean placeMega(final ServerLevel aag, final ChunkGenerator cfv, final BlockPos fx, final BlockState cee, final Random random, final int integer6, final int integer7) {
        final ConfiguredFeature<TreeConfiguration, ?> cis9 = this.getConfiguredMegaFeature(random);
        if (cis9 == null) {
            return false;
        }
        cis9.config.setFromSapling();
        final BlockState cee2 = Blocks.AIR.defaultBlockState();
        aag.setBlock(fx.offset(integer6, 0, integer7), cee2, 4);
        aag.setBlock(fx.offset(integer6 + 1, 0, integer7), cee2, 4);
        aag.setBlock(fx.offset(integer6, 0, integer7 + 1), cee2, 4);
        aag.setBlock(fx.offset(integer6 + 1, 0, integer7 + 1), cee2, 4);
        if (cis9.place(aag, cfv, random, fx.offset(integer6, 0, integer7))) {
            return true;
        }
        aag.setBlock(fx.offset(integer6, 0, integer7), cee, 4);
        aag.setBlock(fx.offset(integer6 + 1, 0, integer7), cee, 4);
        aag.setBlock(fx.offset(integer6, 0, integer7 + 1), cee, 4);
        aag.setBlock(fx.offset(integer6 + 1, 0, integer7 + 1), cee, 4);
        return false;
    }
    
    public static boolean isTwoByTwoSapling(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final int integer4, final int integer5) {
        final Block bul6 = cee.getBlock();
        return bul6 == bqz.getBlockState(fx.offset(integer4, 0, integer5)).getBlock() && bul6 == bqz.getBlockState(fx.offset(integer4 + 1, 0, integer5)).getBlock() && bul6 == bqz.getBlockState(fx.offset(integer4, 0, integer5 + 1)).getBlock() && bul6 == bqz.getBlockState(fx.offset(integer4 + 1, 0, integer5 + 1)).getBlock();
    }
}
