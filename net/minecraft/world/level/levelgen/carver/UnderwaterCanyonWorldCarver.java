package net.minecraft.world.level.levelgen.carver;

import java.util.Set;
import org.apache.commons.lang3.mutable.MutableBoolean;
import java.util.Random;
import java.util.BitSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.chunk.ChunkAccess;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import com.mojang.serialization.Codec;

public class UnderwaterCanyonWorldCarver extends CanyonWorldCarver {
    public UnderwaterCanyonWorldCarver(final Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
        this.replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.SAND, Blocks.GRAVEL, Blocks.WATER, Blocks.LAVA, Blocks.OBSIDIAN, Blocks.AIR, Blocks.CAVE_AIR });
    }
    
    @Override
    protected boolean hasWater(final ChunkAccess cft, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9) {
        return false;
    }
    
    @Override
    protected boolean carveBlock(final ChunkAccess cft, final Function<BlockPos, Biome> function, final BitSet bitSet, final Random random, final BlockPos.MutableBlockPos a5, final BlockPos.MutableBlockPos a6, final BlockPos.MutableBlockPos a7, final int integer8, final int integer9, final int integer10, final int integer11, final int integer12, final int integer13, final int integer14, final int integer15, final MutableBoolean mutableBoolean) {
        return UnderwaterCaveWorldCarver.carveBlock(this, cft, bitSet, random, a5, integer8, integer9, integer10, integer11, integer12, integer13, integer14, integer15);
    }
}
