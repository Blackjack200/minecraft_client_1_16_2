package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.structure.BuriedTreasurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class BuriedTreasureFeature extends StructureFeature<ProbabilityFeatureConfiguration> {
    public BuriedTreasureFeature(final Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final ProbabilityFeatureConfiguration cmh) {
        chu.setLargeFeatureWithSalt(long3, integer5, integer6, 10387320);
        return chu.nextFloat() < cmh.probability;
    }
    
    @Override
    public StructureStartFactory<ProbabilityFeatureConfiguration> getStartFactory() {
        return BuriedTreasureStart::new;
    }
    
    public static class BuriedTreasureStart extends StructureStart<ProbabilityFeatureConfiguration> {
        public BuriedTreasureStart(final StructureFeature<ProbabilityFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final ProbabilityFeatureConfiguration cmh) {
            final int integer6 = integer4 * 16;
            final int integer7 = integer5 * 16;
            final BlockPos fx11 = new BlockPos(integer6 + 9, 90, integer7 + 9);
            this.pieces.add(new BuriedTreasurePieces.BuriedTreasurePiece(fx11));
            this.calculateBoundingBox();
        }
        
        @Override
        public BlockPos getLocatePos() {
            return new BlockPos((this.getChunkX() << 4) + 9, 0, (this.getChunkZ() << 4) + 9);
        }
    }
}
