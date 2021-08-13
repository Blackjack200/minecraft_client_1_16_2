package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.NoiseColumn;
import java.util.Arrays;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class FlatLevelSource extends ChunkGenerator {
    public static final Codec<FlatLevelSource> CODEC;
    private final FlatLevelGeneratorSettings settings;
    
    public FlatLevelSource(final FlatLevelGeneratorSettings cpc) {
        super(new FixedBiomeSource(cpc.getBiomeFromSettings()), new FixedBiomeSource(cpc.getBiome()), cpc.structureSettings(), 0L);
        this.settings = cpc;
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return FlatLevelSource.CODEC;
    }
    
    @Override
    public ChunkGenerator withSeed(final long long1) {
        return this;
    }
    
    public FlatLevelGeneratorSettings settings() {
        return this.settings;
    }
    
    @Override
    public void buildSurfaceAndBedrock(final WorldGenRegion aam, final ChunkAccess cft) {
    }
    
    @Override
    public int getSpawnHeight() {
        final BlockState[] arr2 = this.settings.getLayers();
        for (int integer3 = 0; integer3 < arr2.length; ++integer3) {
            final BlockState cee4 = (arr2[integer3] == null) ? Blocks.AIR.defaultBlockState() : arr2[integer3];
            if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(cee4)) {
                return integer3 - 1;
            }
        }
        return arr2.length;
    }
    
    @Override
    public void fillFromNoise(final LevelAccessor brv, final StructureFeatureManager bsk, final ChunkAccess cft) {
        final BlockState[] arr5 = this.settings.getLayers();
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        final Heightmap chk7 = cft.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        final Heightmap chk8 = cft.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        for (int integer9 = 0; integer9 < arr5.length; ++integer9) {
            final BlockState cee10 = arr5[integer9];
            if (cee10 != null) {
                for (int integer10 = 0; integer10 < 16; ++integer10) {
                    for (int integer11 = 0; integer11 < 16; ++integer11) {
                        cft.setBlockState(a6.set(integer10, integer9, integer11), cee10, false);
                        chk7.update(integer10, integer9, integer11, cee10);
                        chk8.update(integer10, integer9, integer11, cee10);
                    }
                }
            }
        }
    }
    
    @Override
    public int getBaseHeight(final int integer1, final int integer2, final Heightmap.Types a) {
        final BlockState[] arr5 = this.settings.getLayers();
        for (int integer3 = arr5.length - 1; integer3 >= 0; --integer3) {
            final BlockState cee7 = arr5[integer3];
            if (cee7 != null) {
                if (a.isOpaque().test(cee7)) {
                    return integer3 + 1;
                }
            }
        }
        return 0;
    }
    
    @Override
    public BlockGetter getBaseColumn(final int integer1, final int integer2) {
        return new NoiseColumn((BlockState[])Arrays.stream((Object[])this.settings.getLayers()).map(cee -> (cee == null) ? Blocks.AIR.defaultBlockState() : cee).toArray(BlockState[]::new));
    }
    
    static {
        CODEC = FlatLevelGeneratorSettings.CODEC.fieldOf("settings").xmap(FlatLevelSource::new, FlatLevelSource::settings).codec();
    }
}
