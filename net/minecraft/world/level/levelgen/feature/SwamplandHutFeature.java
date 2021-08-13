package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.SwamplandHutPiece;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SwamplandHutFeature extends StructureFeature<NoneFeatureConfiguration> {
    private static final List<MobSpawnSettings.SpawnerData> SWAMPHUT_ENEMIES;
    private static final List<MobSpawnSettings.SpawnerData> SWAMPHUT_ANIMALS;
    
    public SwamplandHutFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return FeatureStart::new;
    }
    
    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
        return SwamplandHutFeature.SWAMPHUT_ENEMIES;
    }
    
    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialAnimals() {
        return SwamplandHutFeature.SWAMPHUT_ANIMALS;
    }
    
    static {
        SWAMPHUT_ENEMIES = (List)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1));
        SWAMPHUT_ANIMALS = (List)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.CAT, 1, 1, 1));
    }
    
    public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
        public FeatureStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            final SwamplandHutPiece crt9 = new SwamplandHutPiece(this.random, integer4 * 16, integer5 * 16);
            this.pieces.add(crt9);
            this.calculateBoundingBox();
        }
    }
}
