package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;

public class HugeFungusFeature extends Feature<HugeFungusConfiguration> {
    public HugeFungusFeature(final Codec<HugeFungusConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final HugeFungusConfiguration cjn) {
        final Block bul7 = cjn.validBaseState.getBlock();
        BlockPos fx2 = null;
        final Block bul8 = bso.getBlockState(fx.below()).getBlock();
        if (bul8 == bul7) {
            fx2 = fx;
        }
        if (fx2 == null) {
            return false;
        }
        int integer10 = Mth.nextInt(random, 4, 13);
        if (random.nextInt(12) == 0) {
            integer10 *= 2;
        }
        if (!cjn.planted) {
            final int integer11 = cfv.getGenDepth();
            if (fx2.getY() + integer10 + 1 >= integer11) {
                return false;
            }
        }
        final boolean boolean11 = !cjn.planted && random.nextFloat() < 0.06f;
        bso.setBlock(fx, Blocks.AIR.defaultBlockState(), 4);
        this.placeStem(bso, random, cjn, fx2, integer10, boolean11);
        this.placeHat(bso, random, cjn, fx2, integer10, boolean11);
        return true;
    }
    
    private static boolean isReplaceable(final LevelAccessor brv, final BlockPos fx, final boolean boolean3) {
        return brv.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> {
            final Material cux3 = cee.getMaterial();
            return cee.getMaterial().isReplaceable() || (boolean3 && cux3 == Material.PLANT);
        }));
    }
    
    private void placeStem(final LevelAccessor brv, final Random random, final HugeFungusConfiguration cjn, final BlockPos fx, final int integer, final boolean boolean6) {
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        final BlockState cee9 = cjn.stemState;
        for (int integer2 = boolean6 ? 1 : 0, integer3 = -integer2; integer3 <= integer2; ++integer3) {
            for (int integer4 = -integer2; integer4 <= integer2; ++integer4) {
                final boolean boolean7 = boolean6 && Mth.abs(integer3) == integer2 && Mth.abs(integer4) == integer2;
                for (int integer5 = 0; integer5 < integer; ++integer5) {
                    a8.setWithOffset(fx, integer3, integer5, integer4);
                    if (isReplaceable(brv, a8, true)) {
                        if (cjn.planted) {
                            if (!brv.getBlockState(a8.below()).isAir()) {
                                brv.destroyBlock(a8, true);
                            }
                            brv.setBlock(a8, cee9, 3);
                        }
                        else if (boolean7) {
                            if (random.nextFloat() < 0.1f) {
                                this.setBlock(brv, a8, cee9);
                            }
                        }
                        else {
                            this.setBlock(brv, a8, cee9);
                        }
                    }
                }
            }
        }
    }
    
    private void placeHat(final LevelAccessor brv, final Random random, final HugeFungusConfiguration cjn, final BlockPos fx, final int integer, final boolean boolean6) {
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        final boolean boolean7 = cjn.hatState.is(Blocks.NETHER_WART_BLOCK);
        final int integer2 = Math.min(random.nextInt(1 + integer / 3) + 5, integer);
        int integer4;
        for (int integer3 = integer4 = integer - integer2; integer4 <= integer; ++integer4) {
            int integer5 = (integer4 < integer - random.nextInt(3)) ? 2 : 1;
            if (integer2 > 8 && integer4 < integer3 + 4) {
                integer5 = 3;
            }
            if (boolean6) {
                ++integer5;
            }
            for (int integer6 = -integer5; integer6 <= integer5; ++integer6) {
                for (int integer7 = -integer5; integer7 <= integer5; ++integer7) {
                    final boolean boolean8 = integer6 == -integer5 || integer6 == integer5;
                    final boolean boolean9 = integer7 == -integer5 || integer7 == integer5;
                    final boolean boolean10 = !boolean8 && !boolean9 && integer4 != integer;
                    final boolean boolean11 = boolean8 && boolean9;
                    final boolean boolean12 = integer4 < integer3 + 3;
                    a8.setWithOffset(fx, integer6, integer4, integer7);
                    if (isReplaceable(brv, a8, false)) {
                        if (cjn.planted && !brv.getBlockState(a8.below()).isAir()) {
                            brv.destroyBlock(a8, true);
                        }
                        if (boolean12) {
                            if (!boolean10) {
                                this.placeHatDropBlock(brv, random, a8, cjn.hatState, boolean7);
                            }
                        }
                        else if (boolean10) {
                            this.placeHatBlock(brv, random, cjn, a8, 0.1f, 0.2f, boolean7 ? 0.1f : 0.0f);
                        }
                        else if (boolean11) {
                            this.placeHatBlock(brv, random, cjn, a8, 0.01f, 0.7f, boolean7 ? 0.083f : 0.0f);
                        }
                        else {
                            this.placeHatBlock(brv, random, cjn, a8, 5.0E-4f, 0.98f, boolean7 ? 0.07f : 0.0f);
                        }
                    }
                }
            }
        }
    }
    
    private void placeHatBlock(final LevelAccessor brv, final Random random, final HugeFungusConfiguration cjn, final BlockPos.MutableBlockPos a, final float float5, final float float6, final float float7) {
        if (random.nextFloat() < float5) {
            this.setBlock(brv, a, cjn.decorState);
        }
        else if (random.nextFloat() < float6) {
            this.setBlock(brv, a, cjn.hatState);
            if (random.nextFloat() < float7) {
                tryPlaceWeepingVines(a, brv, random);
            }
        }
    }
    
    private void placeHatDropBlock(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee, final boolean boolean5) {
        if (brv.getBlockState(fx.below()).is(cee.getBlock())) {
            this.setBlock(brv, fx, cee);
        }
        else if (random.nextFloat() < 0.15) {
            this.setBlock(brv, fx, cee);
            if (boolean5 && random.nextInt(11) == 0) {
                tryPlaceWeepingVines(fx, brv, random);
            }
        }
    }
    
    private static void tryPlaceWeepingVines(final BlockPos fx, final LevelAccessor brv, final Random random) {
        final BlockPos.MutableBlockPos a4 = fx.mutable().move(Direction.DOWN);
        if (!brv.isEmptyBlock(a4)) {
            return;
        }
        int integer5 = Mth.nextInt(random, 1, 5);
        if (random.nextInt(7) == 0) {
            integer5 *= 2;
        }
        final int integer6 = 23;
        final int integer7 = 25;
        WeepingVinesFeature.placeWeepingVinesColumn(brv, random, a4, integer5, 23, 25);
    }
}
