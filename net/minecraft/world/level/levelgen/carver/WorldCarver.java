package net.minecraft.world.level.levelgen.carver;

import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.mutable.MutableBoolean;
import net.minecraft.util.Mth;
import java.util.Random;
import java.util.BitSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.material.Fluids;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public abstract class WorldCarver<C extends CarverConfiguration> {
    public static final WorldCarver<ProbabilityFeatureConfiguration> CAVE;
    public static final WorldCarver<ProbabilityFeatureConfiguration> NETHER_CAVE;
    public static final WorldCarver<ProbabilityFeatureConfiguration> CANYON;
    public static final WorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CANYON;
    public static final WorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CAVE;
    protected static final BlockState AIR;
    protected static final BlockState CAVE_AIR;
    protected static final FluidState WATER;
    protected static final FluidState LAVA;
    protected Set<Block> replaceableBlocks;
    protected Set<Fluid> liquids;
    private final Codec<ConfiguredWorldCarver<C>> configuredCodec;
    protected final int genHeight;
    
    private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(final String string, final F cid) {
        return Registry.<F>register(Registry.CARVER, string, cid);
    }
    
    public WorldCarver(final Codec<C> codec, final int integer) {
        this.replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.PACKED_ICE });
        this.liquids = (Set<Fluid>)ImmutableSet.of(Fluids.WATER);
        this.genHeight = integer;
        this.configuredCodec = (Codec<ConfiguredWorldCarver<C>>)codec.fieldOf("config").xmap(this::configured, ConfiguredWorldCarver::config).codec();
    }
    
    public ConfiguredWorldCarver<C> configured(final C chw) {
        return new ConfiguredWorldCarver<C>(this, chw);
    }
    
    public Codec<ConfiguredWorldCarver<C>> configuredCodec() {
        return this.configuredCodec;
    }
    
    public int getRange() {
        return 4;
    }
    
    protected boolean carveSphere(final ChunkAccess cft, final Function<BlockPos, Biome> function, final long long3, final int integer4, final int integer5, final int integer6, final double double7, final double double8, final double double9, final double double10, final double double11, final BitSet bitSet) {
        final Random random20 = new Random(long3 + integer5 + integer6);
        final double double12 = integer5 * 16 + 8;
        final double double13 = integer6 * 16 + 8;
        if (double7 < double12 - 16.0 - double10 * 2.0 || double9 < double13 - 16.0 - double10 * 2.0 || double7 > double12 + 16.0 + double10 * 2.0 || double9 > double13 + 16.0 + double10 * 2.0) {
            return false;
        }
        final int integer7 = Math.max(Mth.floor(double7 - double10) - integer5 * 16 - 1, 0);
        final int integer8 = Math.min(Mth.floor(double7 + double10) - integer5 * 16 + 1, 16);
        final int integer9 = Math.max(Mth.floor(double8 - double11) - 1, 1);
        final int integer10 = Math.min(Mth.floor(double8 + double11) + 1, this.genHeight - 8);
        final int integer11 = Math.max(Mth.floor(double9 - double10) - integer6 * 16 - 1, 0);
        final int integer12 = Math.min(Mth.floor(double9 + double10) - integer6 * 16 + 1, 16);
        if (this.hasWater(cft, integer5, integer6, integer7, integer8, integer9, integer10, integer11, integer12)) {
            return false;
        }
        boolean boolean31 = false;
        final BlockPos.MutableBlockPos a32 = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos a33 = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos a34 = new BlockPos.MutableBlockPos();
        for (int integer13 = integer7; integer13 < integer8; ++integer13) {
            final int integer14 = integer13 + integer5 * 16;
            final double double14 = (integer14 + 0.5 - double7) / double10;
            for (int integer15 = integer11; integer15 < integer12; ++integer15) {
                final int integer16 = integer15 + integer6 * 16;
                final double double15 = (integer16 + 0.5 - double9) / double10;
                if (double14 * double14 + double15 * double15 < 1.0) {
                    final MutableBoolean mutableBoolean43 = new MutableBoolean(false);
                    for (int integer17 = integer10; integer17 > integer9; --integer17) {
                        final double double16 = (integer17 - 0.5 - double8) / double11;
                        if (!this.skip(double14, double16, double15, integer17)) {
                            boolean31 |= this.carveBlock(cft, function, bitSet, random20, a32, a33, a34, integer4, integer5, integer6, integer14, integer16, integer13, integer17, integer15, mutableBoolean43);
                        }
                    }
                }
            }
        }
        return boolean31;
    }
    
    protected boolean carveBlock(final ChunkAccess cft, final Function<BlockPos, Biome> function, final BitSet bitSet, final Random random, final BlockPos.MutableBlockPos a5, final BlockPos.MutableBlockPos a6, final BlockPos.MutableBlockPos a7, final int integer8, final int integer9, final int integer10, final int integer11, final int integer12, final int integer13, final int integer14, final int integer15, final MutableBoolean mutableBoolean) {
        final int integer16 = integer13 | integer15 << 4 | integer14 << 8;
        if (bitSet.get(integer16)) {
            return false;
        }
        bitSet.set(integer16);
        a5.set(integer11, integer14, integer12);
        final BlockState cee19 = cft.getBlockState(a5);
        final BlockState cee20 = cft.getBlockState(a6.setWithOffset(a5, Direction.UP));
        if (cee19.is(Blocks.GRASS_BLOCK) || cee19.is(Blocks.MYCELIUM)) {
            mutableBoolean.setTrue();
        }
        if (!this.canReplaceBlock(cee19, cee20)) {
            return false;
        }
        if (integer14 < 11) {
            cft.setBlockState(a5, WorldCarver.LAVA.createLegacyBlock(), false);
        }
        else {
            cft.setBlockState(a5, WorldCarver.CAVE_AIR, false);
            if (mutableBoolean.isTrue()) {
                a7.setWithOffset(a5, Direction.DOWN);
                if (cft.getBlockState(a7).is(Blocks.DIRT)) {
                    cft.setBlockState(a7, ((Biome)function.apply(a5)).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial(), false);
                }
            }
        }
        return true;
    }
    
    public abstract boolean carve(final ChunkAccess cft, final Function<BlockPos, Biome> function, final Random random, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final BitSet bitSet, final C chw);
    
    public abstract boolean isStartChunk(final Random random, final int integer2, final int integer3, final C chw);
    
    protected boolean canReplaceBlock(final BlockState cee) {
        return this.replaceableBlocks.contains(cee.getBlock());
    }
    
    protected boolean canReplaceBlock(final BlockState cee1, final BlockState cee2) {
        return this.canReplaceBlock(cee1) || ((cee1.is(Blocks.SAND) || cee1.is(Blocks.GRAVEL)) && !cee2.getFluidState().is(FluidTags.WATER));
    }
    
    protected boolean hasWater(final ChunkAccess cft, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9) {
        final BlockPos.MutableBlockPos a11 = new BlockPos.MutableBlockPos();
        for (int integer10 = integer4; integer10 < integer5; ++integer10) {
            for (int integer11 = integer8; integer11 < integer9; ++integer11) {
                for (int integer12 = integer6 - 1; integer12 <= integer7 + 1; ++integer12) {
                    if (this.liquids.contains(cft.getFluidState(a11.set(integer10 + integer2 * 16, integer12, integer11 + integer3 * 16)).getType())) {
                        return true;
                    }
                    if (integer12 != integer7 + 1 && !this.isEdge(integer4, integer5, integer8, integer9, integer10, integer11)) {
                        integer12 = integer7;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isEdge(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        return integer5 == integer1 || integer5 == integer2 - 1 || integer6 == integer3 || integer6 == integer4 - 1;
    }
    
    protected boolean canReach(final int integer1, final int integer2, final double double3, final double double4, final int integer5, final int integer6, final float float7) {
        final double double5 = integer1 * 16 + 8;
        final double double6 = integer2 * 16 + 8;
        final double double7 = double3 - double5;
        final double double8 = double4 - double6;
        final double double9 = integer6 - integer5;
        final double double10 = float7 + 2.0f + 16.0f;
        return double7 * double7 + double8 * double8 - double9 * double9 <= double10 * double10;
    }
    
    protected abstract boolean skip(final double double1, final double double2, final double double3, final int integer);
    
    static {
        CAVE = WorldCarver.<CarverConfiguration, CaveWorldCarver>register("cave", new CaveWorldCarver(ProbabilityFeatureConfiguration.CODEC, 256));
        NETHER_CAVE = WorldCarver.<CarverConfiguration, NetherWorldCarver>register("nether_cave", new NetherWorldCarver(ProbabilityFeatureConfiguration.CODEC));
        CANYON = WorldCarver.<CarverConfiguration, CanyonWorldCarver>register("canyon", new CanyonWorldCarver(ProbabilityFeatureConfiguration.CODEC));
        UNDERWATER_CANYON = WorldCarver.<CarverConfiguration, UnderwaterCanyonWorldCarver>register("underwater_canyon", new UnderwaterCanyonWorldCarver(ProbabilityFeatureConfiguration.CODEC));
        UNDERWATER_CAVE = WorldCarver.<CarverConfiguration, UnderwaterCaveWorldCarver>register("underwater_cave", new UnderwaterCaveWorldCarver(ProbabilityFeatureConfiguration.CODEC));
        AIR = Blocks.AIR.defaultBlockState();
        CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
        WATER = Fluids.WATER.defaultFluidState();
        LAVA = Fluids.LAVA.defaultFluidState();
    }
}
