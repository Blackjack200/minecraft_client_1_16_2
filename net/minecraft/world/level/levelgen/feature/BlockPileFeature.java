package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import java.util.Iterator;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;

public class BlockPileFeature extends Feature<BlockPileConfiguration> {
    public BlockPileFeature(final Codec<BlockPileConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final BlockPileConfiguration clo) {
        if (fx.getY() < 5) {
            return false;
        }
        final int integer7 = 2 + random.nextInt(2);
        final int integer8 = 2 + random.nextInt(2);
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-integer7, 0, -integer8), fx.offset(integer7, 1, integer8))) {
            final int integer9 = fx.getX() - fx2.getX();
            final int integer10 = fx.getZ() - fx2.getZ();
            if (integer9 * integer9 + integer10 * integer10 <= random.nextFloat() * 10.0f - random.nextFloat() * 6.0f) {
                this.tryPlaceBlock(bso, fx2, random, clo);
            }
            else {
                if (random.nextFloat() >= 0.031) {
                    continue;
                }
                this.tryPlaceBlock(bso, fx2, random, clo);
            }
        }
        return true;
    }
    
    private boolean mayPlaceOn(final LevelAccessor brv, final BlockPos fx, final Random random) {
        final BlockPos fx2 = fx.below();
        final BlockState cee6 = brv.getBlockState(fx2);
        if (cee6.is(Blocks.GRASS_PATH)) {
            return random.nextBoolean();
        }
        return cee6.isFaceSturdy(brv, fx2, Direction.UP);
    }
    
    private void tryPlaceBlock(final LevelAccessor brv, final BlockPos fx, final Random random, final BlockPileConfiguration clo) {
        if (brv.isEmptyBlock(fx) && this.mayPlaceOn(brv, fx, random)) {
            brv.setBlock(fx, clo.stateProvider.getState(random, fx), 4);
        }
    }
}
