package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.Blocks;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryLookupCodec;
import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import net.minecraft.util.Mth;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class DebugLevelSource extends ChunkGenerator {
    public static final Codec<DebugLevelSource> CODEC;
    private static final List<BlockState> ALL_BLOCKS;
    private static final int GRID_WIDTH;
    private static final int GRID_HEIGHT;
    protected static final BlockState AIR;
    protected static final BlockState BARRIER;
    private final Registry<Biome> biomes;
    
    public DebugLevelSource(final Registry<Biome> gm) {
        super(new FixedBiomeSource(gm.getOrThrow(Biomes.PLAINS)), new StructureSettings(false));
        this.biomes = gm;
    }
    
    public Registry<Biome> biomes() {
        return this.biomes;
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return DebugLevelSource.CODEC;
    }
    
    @Override
    public ChunkGenerator withSeed(final long long1) {
        return this;
    }
    
    @Override
    public void buildSurfaceAndBedrock(final WorldGenRegion aam, final ChunkAccess cft) {
    }
    
    @Override
    public void applyCarvers(final long long1, final BiomeManager bsu, final ChunkAccess cft, final GenerationStep.Carving a) {
    }
    
    @Override
    public void applyBiomeDecoration(final WorldGenRegion aam, final StructureFeatureManager bsk) {
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        final int integer5 = aam.getCenterX();
        final int integer6 = aam.getCenterZ();
        for (int integer7 = 0; integer7 < 16; ++integer7) {
            for (int integer8 = 0; integer8 < 16; ++integer8) {
                final int integer9 = (integer5 << 4) + integer7;
                final int integer10 = (integer6 << 4) + integer8;
                aam.setBlock(a4.set(integer9, 60, integer10), DebugLevelSource.BARRIER, 2);
                final BlockState cee11 = getBlockStateFor(integer9, integer10);
                if (cee11 != null) {
                    aam.setBlock(a4.set(integer9, 70, integer10), cee11, 2);
                }
            }
        }
    }
    
    @Override
    public void fillFromNoise(final LevelAccessor brv, final StructureFeatureManager bsk, final ChunkAccess cft) {
    }
    
    @Override
    public int getBaseHeight(final int integer1, final int integer2, final Heightmap.Types a) {
        return 0;
    }
    
    @Override
    public BlockGetter getBaseColumn(final int integer1, final int integer2) {
        return new NoiseColumn(new BlockState[0]);
    }
    
    public static BlockState getBlockStateFor(int integer1, int integer2) {
        BlockState cee3 = DebugLevelSource.AIR;
        if (integer1 > 0 && integer2 > 0 && integer1 % 2 != 0 && integer2 % 2 != 0) {
            integer1 /= 2;
            integer2 /= 2;
            if (integer1 <= DebugLevelSource.GRID_WIDTH && integer2 <= DebugLevelSource.GRID_HEIGHT) {
                final int integer3 = Mth.abs(integer1 * DebugLevelSource.GRID_WIDTH + integer2);
                if (integer3 < DebugLevelSource.ALL_BLOCKS.size()) {
                    cee3 = (BlockState)DebugLevelSource.ALL_BLOCKS.get(integer3);
                }
            }
        }
        return cee3;
    }
    
    static {
        CODEC = RegistryLookupCodec.create(Registry.BIOME_REGISTRY).xmap(DebugLevelSource::new, DebugLevelSource::biomes).stable().codec();
        ALL_BLOCKS = (List)StreamSupport.stream(Registry.BLOCK.spliterator(), false).flatMap(bul -> bul.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toList());
        GRID_WIDTH = Mth.ceil(Mth.sqrt((float)DebugLevelSource.ALL_BLOCKS.size()));
        GRID_HEIGHT = Mth.ceil(DebugLevelSource.ALL_BLOCKS.size() / (float)DebugLevelSource.GRID_WIDTH);
        AIR = Blocks.AIR.defaultBlockState();
        BARRIER = Blocks.BARRIER.defaultBlockState();
    }
}
