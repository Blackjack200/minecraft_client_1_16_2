package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.OceanMonumentPieces;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class OceanMonumentFeature extends StructureFeature<NoneFeatureConfiguration> {
    private static final List<MobSpawnSettings.SpawnerData> MONUMENT_ENEMIES;
    
    public OceanMonumentFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean linearSeparation() {
        return false;
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final NoneFeatureConfiguration cme) {
        final Set<Biome> set12 = bsv.getBiomesWithin(integer5 * 16 + 9, cfv.getSeaLevel(), integer6 * 16 + 9, 16);
        for (final Biome bss2 : set12) {
            if (!bss2.getGenerationSettings().isValidStart(this)) {
                return false;
            }
        }
        final Set<Biome> set13 = bsv.getBiomesWithin(integer5 * 16 + 9, cfv.getSeaLevel(), integer6 * 16 + 9, 29);
        for (final Biome bss3 : set13) {
            if (bss3.getBiomeCategory() != Biome.BiomeCategory.OCEAN && bss3.getBiomeCategory() != Biome.BiomeCategory.RIVER) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return OceanMonumentStart::new;
    }
    
    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
        return OceanMonumentFeature.MONUMENT_ENEMIES;
    }
    
    static {
        MONUMENT_ENEMIES = (List)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.GUARDIAN, 1, 2, 4));
    }
    
    public static class OceanMonumentStart extends StructureStart<NoneFeatureConfiguration> {
        private boolean isCreated;
        
        public OceanMonumentStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            this.generatePieces(integer4, integer5);
        }
        
        private void generatePieces(final int integer1, final int integer2) {
            final int integer3 = integer1 * 16 - 29;
            final int integer4 = integer2 * 16 - 29;
            final Direction gc6 = Direction.Plane.HORIZONTAL.getRandomDirection(this.random);
            this.pieces.add(new OceanMonumentPieces.MonumentBuilding(this.random, integer3, integer4, gc6));
            this.calculateBoundingBox();
            this.isCreated = true;
        }
        
        @Override
        public void placeInChunk(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra) {
            if (!this.isCreated) {
                this.pieces.clear();
                this.generatePieces(this.getChunkX(), this.getChunkZ());
            }
            super.placeInChunk(bso, bsk, cfv, random, cqx, bra);
        }
    }
}
