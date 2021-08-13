package net.minecraft.world.level.levelgen.carver;

import net.minecraft.world.level.material.Fluid;
import java.util.Set;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;
import java.util.BitSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import net.minecraft.world.level.material.Fluids;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import com.mojang.serialization.Codec;

public class NetherWorldCarver extends CaveWorldCarver {
    public NetherWorldCarver(final Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec, 128);
        this.replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.BASALT, Blocks.BLACKSTONE });
        this.liquids = (Set<Fluid>)ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
    }
    
    @Override
    protected int getCaveBound() {
        return 10;
    }
    
    @Override
    protected float getThickness(final Random random) {
        return (random.nextFloat() * 2.0f + random.nextFloat()) * 2.0f;
    }
    
    @Override
    protected double getYScale() {
        return 5.0;
    }
    
    @Override
    protected int getCaveY(final Random random) {
        return random.nextInt(this.genHeight);
    }
    
    @Override
    protected boolean carveBlock(final ChunkAccess cft, final Function<BlockPos, Biome> function, final BitSet bitSet, final Random random, final BlockPos.MutableBlockPos a5, final BlockPos.MutableBlockPos a6, final BlockPos.MutableBlockPos a7, final int integer8, final int integer9, final int integer10, final int integer11, final int integer12, final int integer13, final int integer14, final int integer15, final MutableBoolean mutableBoolean) {
        final int integer16 = integer13 | integer15 << 4 | integer14 << 8;
        if (bitSet.get(integer16)) {
            return false;
        }
        bitSet.set(integer16);
        a5.set(integer11, integer14, integer12);
        if (this.canReplaceBlock(cft.getBlockState(a5))) {
            BlockState cee19;
            if (integer14 <= 31) {
                cee19 = NetherWorldCarver.LAVA.createLegacyBlock();
            }
            else {
                cee19 = NetherWorldCarver.CAVE_AIR;
            }
            cft.setBlockState(a5, cee19, false);
            return true;
        }
        return false;
    }
}
