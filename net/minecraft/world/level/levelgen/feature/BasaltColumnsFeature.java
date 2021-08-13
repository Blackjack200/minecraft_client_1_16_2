package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Blocks;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;

public class BasaltColumnsFeature extends Feature<ColumnFeatureConfiguration> {
    private static final ImmutableList<Block> CANNOT_PLACE_ON;
    
    public BasaltColumnsFeature(final Codec<ColumnFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final ColumnFeatureConfiguration clq) {
        final int integer7 = cfv.getSeaLevel();
        if (!canPlaceAt(bso, integer7, fx.mutable())) {
            return false;
        }
        final int integer8 = clq.height().sample(random);
        final boolean boolean9 = random.nextFloat() < 0.9f;
        final int integer9 = Math.min(integer8, boolean9 ? 5 : 8);
        final int integer10 = boolean9 ? 50 : 15;
        boolean boolean10 = false;
        for (final BlockPos fx2 : BlockPos.randomBetweenClosed(random, integer10, fx.getX() - integer9, fx.getY(), fx.getZ() - integer9, fx.getX() + integer9, fx.getY(), fx.getZ() + integer9)) {
            final int integer11 = integer8 - fx2.distManhattan(fx);
            if (integer11 >= 0) {
                boolean10 |= this.placeColumn(bso, integer7, fx2, integer11, clq.reach().sample(random));
            }
        }
        return boolean10;
    }
    
    private boolean placeColumn(final LevelAccessor brv, final int integer2, final BlockPos fx, final int integer4, final int integer5) {
        boolean boolean7 = false;
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.getX() - integer5, fx.getY(), fx.getZ() - integer5, fx.getX() + integer5, fx.getY(), fx.getZ() + integer5)) {
            final int integer6 = fx2.distManhattan(fx);
            final BlockPos fx3 = isAirOrLavaOcean(brv, integer2, fx2) ? findSurface(brv, integer2, fx2.mutable(), integer6) : findAir(brv, fx2.mutable(), integer6);
            if (fx3 == null) {
                continue;
            }
            int integer7 = integer4 - integer6 / 2;
            final BlockPos.MutableBlockPos a13 = fx3.mutable();
            while (integer7 >= 0) {
                if (isAirOrLavaOcean(brv, integer2, a13)) {
                    this.setBlock(brv, a13, Blocks.BASALT.defaultBlockState());
                    a13.move(Direction.UP);
                    boolean7 = true;
                }
                else {
                    if (!brv.getBlockState(a13).is(Blocks.BASALT)) {
                        break;
                    }
                    a13.move(Direction.UP);
                }
                --integer7;
            }
        }
        return boolean7;
    }
    
    @Nullable
    private static BlockPos findSurface(final LevelAccessor brv, final int integer2, final BlockPos.MutableBlockPos a, int integer4) {
        while (a.getY() > 1 && integer4 > 0) {
            --integer4;
            if (canPlaceAt(brv, integer2, a)) {
                return a;
            }
            a.move(Direction.DOWN);
        }
        return null;
    }
    
    private static boolean canPlaceAt(final LevelAccessor brv, final int integer, final BlockPos.MutableBlockPos a) {
        if (isAirOrLavaOcean(brv, integer, a)) {
            final BlockState cee4 = brv.getBlockState(a.move(Direction.DOWN));
            a.move(Direction.UP);
            return !cee4.isAir() && !BasaltColumnsFeature.CANNOT_PLACE_ON.contains(cee4.getBlock());
        }
        return false;
    }
    
    @Nullable
    private static BlockPos findAir(final LevelAccessor brv, final BlockPos.MutableBlockPos a, int integer) {
        while (a.getY() < brv.getMaxBuildHeight() && integer > 0) {
            --integer;
            final BlockState cee4 = brv.getBlockState(a);
            if (BasaltColumnsFeature.CANNOT_PLACE_ON.contains(cee4.getBlock())) {
                return null;
            }
            if (cee4.isAir()) {
                return a;
            }
            a.move(Direction.UP);
        }
        return null;
    }
    
    private static boolean isAirOrLavaOcean(final LevelAccessor brv, final int integer, final BlockPos fx) {
        final BlockState cee4 = brv.getBlockState(fx);
        return cee4.isAir() || (cee4.is(Blocks.LAVA) && fx.getY() <= integer);
    }
    
    static {
        CANNOT_PLACE_ON = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
    }
}
