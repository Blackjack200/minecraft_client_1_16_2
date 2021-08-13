package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.structure.StructurePiece;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.NetherBridgePieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class NetherFortressFeature extends StructureFeature<NoneFeatureConfiguration> {
    private static final List<MobSpawnSettings.SpawnerData> FORTRESS_ENEMIES;
    
    public NetherFortressFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final NoneFeatureConfiguration cme) {
        return chu.nextInt(5) < 2;
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return NetherBridgeStart::new;
    }
    
    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
        return NetherFortressFeature.FORTRESS_ENEMIES;
    }
    
    static {
        FORTRESS_ENEMIES = (List)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 10, 2, 3), new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4), new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 8, 5, 5), new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 2, 5, 5), new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 3, 4, 4));
    }
    
    public static class NetherBridgeStart extends StructureStart<NoneFeatureConfiguration> {
        public NetherBridgeStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            final NetherBridgePieces.StartPiece q9 = new NetherBridgePieces.StartPiece(this.random, (integer4 << 4) + 2, (integer5 << 4) + 2);
            this.pieces.add(q9);
            q9.addChildren(q9, this.pieces, this.random);
            final List<StructurePiece> list10 = q9.pendingChildren;
            while (!list10.isEmpty()) {
                final int integer6 = this.random.nextInt(list10.size());
                final StructurePiece crr12 = (StructurePiece)list10.remove(integer6);
                crr12.addChildren(q9, this.pieces, this.random);
            }
            this.calculateBoundingBox();
            this.moveInsideHeights(this.random, 48, 70);
        }
    }
}
