package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BambooBlock;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class BambooFeature extends Feature<ProbabilityFeatureConfiguration> {
    private static final BlockState BAMBOO_TRUNK;
    private static final BlockState BAMBOO_FINAL_LARGE;
    private static final BlockState BAMBOO_TOP_LARGE;
    private static final BlockState BAMBOO_TOP_SMALL;
    
    public BambooFeature(final Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final ProbabilityFeatureConfiguration cmh) {
        int integer7 = 0;
        final BlockPos.MutableBlockPos a8 = fx.mutable();
        final BlockPos.MutableBlockPos a9 = fx.mutable();
        if (bso.isEmptyBlock(a8)) {
            if (Blocks.BAMBOO.defaultBlockState().canSurvive(bso, a8)) {
                final int integer8 = random.nextInt(12) + 5;
                if (random.nextFloat() < cmh.probability) {
                    for (int integer9 = random.nextInt(4) + 1, integer10 = fx.getX() - integer9; integer10 <= fx.getX() + integer9; ++integer10) {
                        for (int integer11 = fx.getZ() - integer9; integer11 <= fx.getZ() + integer9; ++integer11) {
                            final int integer12 = integer10 - fx.getX();
                            final int integer13 = integer11 - fx.getZ();
                            if (integer12 * integer12 + integer13 * integer13 <= integer9 * integer9) {
                                a9.set(integer10, bso.getHeight(Heightmap.Types.WORLD_SURFACE, integer10, integer11) - 1, integer11);
                                if (Feature.isDirt(bso.getBlockState(a9).getBlock())) {
                                    bso.setBlock(a9, Blocks.PODZOL.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
                for (int integer9 = 0; integer9 < integer8 && bso.isEmptyBlock(a8); ++integer9) {
                    bso.setBlock(a8, BambooFeature.BAMBOO_TRUNK, 2);
                    a8.move(Direction.UP, 1);
                }
                if (a8.getY() - fx.getY() >= 3) {
                    bso.setBlock(a8, BambooFeature.BAMBOO_FINAL_LARGE, 2);
                    bso.setBlock(a8.move(Direction.DOWN, 1), BambooFeature.BAMBOO_TOP_LARGE, 2);
                    bso.setBlock(a8.move(Direction.DOWN, 1), BambooFeature.BAMBOO_TOP_SMALL, 2);
                }
            }
            ++integer7;
        }
        return integer7 > 0;
    }
    
    static {
        BAMBOO_TRUNK = ((((StateHolder<O, BlockState>)Blocks.BAMBOO.defaultBlockState()).setValue((Property<Comparable>)BambooBlock.AGE, 1)).setValue(BambooBlock.LEAVES, BambooLeaves.NONE)).<Comparable, Integer>setValue((Property<Comparable>)BambooBlock.STAGE, 0);
        BAMBOO_FINAL_LARGE = (((StateHolder<O, BlockState>)BambooFeature.BAMBOO_TRUNK).setValue(BambooBlock.LEAVES, BambooLeaves.LARGE)).<Comparable, Integer>setValue((Property<Comparable>)BambooBlock.STAGE, 1);
        BAMBOO_TOP_LARGE = ((StateHolder<O, BlockState>)BambooFeature.BAMBOO_TRUNK).<BambooLeaves, BambooLeaves>setValue(BambooBlock.LEAVES, BambooLeaves.LARGE);
        BAMBOO_TOP_SMALL = ((StateHolder<O, BlockState>)BambooFeature.BAMBOO_TRUNK).<BambooLeaves, BambooLeaves>setValue(BambooBlock.LEAVES, BambooLeaves.SMALL);
    }
}
