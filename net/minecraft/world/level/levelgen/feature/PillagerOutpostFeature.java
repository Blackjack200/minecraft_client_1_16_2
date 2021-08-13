package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;

public class PillagerOutpostFeature extends JigsawFeature {
    private static final List<MobSpawnSettings.SpawnerData> OUTPOST_ENEMIES;
    
    public PillagerOutpostFeature(final Codec<JigsawConfiguration> codec) {
        super(codec, 0, true, true);
    }
    
    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
        return PillagerOutpostFeature.OUTPOST_ENEMIES;
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final JigsawConfiguration clz) {
        final int integer7 = integer5 >> 4;
        final int integer8 = integer6 >> 4;
        chu.setSeed((long)(integer7 ^ integer8 << 4) ^ long3);
        chu.nextInt();
        return chu.nextInt(5) == 0 && !this.isNearVillage(cfv, long3, chu, integer5, integer6);
    }
    
    private boolean isNearVillage(final ChunkGenerator cfv, final long long2, final WorldgenRandom chu, final int integer4, final int integer5) {
        final StructureFeatureConfiguration cmv8 = cfv.getSettings().getConfig(StructureFeature.VILLAGE);
        if (cmv8 == null) {
            return false;
        }
        for (int integer6 = integer4 - 10; integer6 <= integer4 + 10; ++integer6) {
            for (int integer7 = integer5 - 10; integer7 <= integer5 + 10; ++integer7) {
                final ChunkPos bra11 = StructureFeature.VILLAGE.getPotentialFeatureChunk(cmv8, long2, chu, integer6, integer7);
                if (integer6 == bra11.x && integer7 == bra11.z) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        OUTPOST_ENEMIES = (List)ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, 1, 1, 1));
    }
}
