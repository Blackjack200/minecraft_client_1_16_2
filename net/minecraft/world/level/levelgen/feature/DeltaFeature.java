package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;

public class DeltaFeature extends Feature<DeltaFeatureConfiguration> {
    private static final ImmutableList<Block> CANNOT_REPLACE;
    private static final Direction[] DIRECTIONS;
    
    public DeltaFeature(final Codec<DeltaFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final DeltaFeatureConfiguration clu) {
        boolean boolean7 = false;
        final boolean boolean8 = random.nextDouble() < 0.9;
        final int integer9 = boolean8 ? clu.rimSize().sample(random) : 0;
        final int integer10 = boolean8 ? clu.rimSize().sample(random) : 0;
        final boolean boolean9 = boolean8 && integer9 != 0 && integer10 != 0;
        final int integer11 = clu.size().sample(random);
        final int integer12 = clu.size().sample(random);
        final int integer13 = Math.max(integer11, integer12);
        for (final BlockPos fx2 : BlockPos.withinManhattan(fx, integer11, 0, integer12)) {
            if (fx2.distManhattan(fx) > integer13) {
                break;
            }
            if (!isClear(bso, fx2, clu)) {
                continue;
            }
            if (boolean9) {
                boolean7 = true;
                this.setBlock(bso, fx2, clu.rim());
            }
            final BlockPos fx3 = fx2.offset(integer9, 0, integer10);
            if (!isClear(bso, fx3, clu)) {
                continue;
            }
            boolean7 = true;
            this.setBlock(bso, fx3, clu.contents());
        }
        return boolean7;
    }
    
    private static boolean isClear(final LevelAccessor brv, final BlockPos fx, final DeltaFeatureConfiguration clu) {
        final BlockState cee4 = brv.getBlockState(fx);
        if (cee4.is(clu.contents().getBlock())) {
            return false;
        }
        if (DeltaFeature.CANNOT_REPLACE.contains(cee4.getBlock())) {
            return false;
        }
        for (final Direction gc8 : DeltaFeature.DIRECTIONS) {
            final boolean boolean9 = brv.getBlockState(fx.relative(gc8)).isAir();
            if ((boolean9 && gc8 != Direction.UP) || (!boolean9 && gc8 == Direction.UP)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        CANNOT_REPLACE = ImmutableList.of(Blocks.BEDROCK, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
        DIRECTIONS = Direction.values();
    }
}
