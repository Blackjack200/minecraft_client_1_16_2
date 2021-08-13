package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DesertWellFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockStatePredicate IS_SAND;
    private final BlockState sandSlab;
    private final BlockState sandstone;
    private final BlockState water;
    
    public DesertWellFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
        this.sandSlab = Blocks.SANDSTONE_SLAB.defaultBlockState();
        this.sandstone = Blocks.SANDSTONE.defaultBlockState();
        this.water = Blocks.WATER.defaultBlockState();
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, BlockPos fx, final NoneFeatureConfiguration cme) {
        for (fx = fx.above(); bso.isEmptyBlock(fx) && fx.getY() > 2; fx = fx.below()) {}
        if (!DesertWellFeature.IS_SAND.test(bso.getBlockState(fx))) {
            return false;
        }
        for (int integer7 = -2; integer7 <= 2; ++integer7) {
            for (int integer8 = -2; integer8 <= 2; ++integer8) {
                if (bso.isEmptyBlock(fx.offset(integer7, -1, integer8)) && bso.isEmptyBlock(fx.offset(integer7, -2, integer8))) {
                    return false;
                }
            }
        }
        for (int integer7 = -1; integer7 <= 0; ++integer7) {
            for (int integer8 = -2; integer8 <= 2; ++integer8) {
                for (int integer9 = -2; integer9 <= 2; ++integer9) {
                    bso.setBlock(fx.offset(integer8, integer7, integer9), this.sandstone, 2);
                }
            }
        }
        bso.setBlock(fx, this.water, 2);
        for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
            bso.setBlock(fx.relative(gc8), this.water, 2);
        }
        for (int integer7 = -2; integer7 <= 2; ++integer7) {
            for (int integer8 = -2; integer8 <= 2; ++integer8) {
                if (integer7 == -2 || integer7 == 2 || integer8 == -2 || integer8 == 2) {
                    bso.setBlock(fx.offset(integer7, 1, integer8), this.sandstone, 2);
                }
            }
        }
        bso.setBlock(fx.offset(2, 1, 0), this.sandSlab, 2);
        bso.setBlock(fx.offset(-2, 1, 0), this.sandSlab, 2);
        bso.setBlock(fx.offset(0, 1, 2), this.sandSlab, 2);
        bso.setBlock(fx.offset(0, 1, -2), this.sandSlab, 2);
        for (int integer7 = -1; integer7 <= 1; ++integer7) {
            for (int integer8 = -1; integer8 <= 1; ++integer8) {
                if (integer7 == 0 && integer8 == 0) {
                    bso.setBlock(fx.offset(integer7, 4, integer8), this.sandstone, 2);
                }
                else {
                    bso.setBlock(fx.offset(integer7, 4, integer8), this.sandSlab, 2);
                }
            }
        }
        for (int integer7 = 1; integer7 <= 3; ++integer7) {
            bso.setBlock(fx.offset(-1, integer7, -1), this.sandstone, 2);
            bso.setBlock(fx.offset(-1, integer7, 1), this.sandstone, 2);
            bso.setBlock(fx.offset(1, integer7, -1), this.sandstone, 2);
            bso.setBlock(fx.offset(1, integer7, 1), this.sandstone, 2);
        }
        return true;
    }
    
    static {
        IS_SAND = BlockStatePredicate.forBlock(Blocks.SAND);
    }
}
