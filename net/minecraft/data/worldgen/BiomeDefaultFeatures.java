package net.minecraft.data.worldgen;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.biome.BiomeGenerationSettings;

public class BiomeDefaultFeatures {
    public static void addDefaultOverworldLandMesaStructures(final BiomeGenerationSettings.Builder a) {
        a.addStructureStart(StructureFeatures.MINESHAFT_MESA);
        a.addStructureStart(StructureFeatures.STRONGHOLD);
    }
    
    public static void addDefaultOverworldLandStructures(final BiomeGenerationSettings.Builder a) {
        a.addStructureStart(StructureFeatures.MINESHAFT);
        a.addStructureStart(StructureFeatures.STRONGHOLD);
    }
    
    public static void addDefaultOverworldOceanStructures(final BiomeGenerationSettings.Builder a) {
        a.addStructureStart(StructureFeatures.MINESHAFT);
        a.addStructureStart(StructureFeatures.SHIPWRECK);
    }
    
    public static void addDefaultCarvers(final BiomeGenerationSettings.Builder a) {
        a.<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        a.<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
    }
    
    public static void addOceanCarvers(final BiomeGenerationSettings.Builder a) {
        a.<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.OCEAN_CAVE);
        a.<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        a.<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.LIQUID, Carvers.UNDERWATER_CANYON);
        a.<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.LIQUID, Carvers.UNDERWATER_CAVE);
    }
    
    public static void addDefaultLakes(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_WATER);
        a.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
    }
    
    public static void addDesertLakes(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
    }
    
    public static void addDefaultMonsterRoom(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, Features.MONSTER_ROOM);
    }
    
    public static void addDefaultUndergroundVariety(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_DIRT);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GRAVEL);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GRANITE);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_DIORITE);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_ANDESITE);
    }
    
    public static void addDefaultOres(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_COAL);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_IRON);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GOLD);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_REDSTONE);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_DIAMOND);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_LAPIS);
    }
    
    public static void addExtraGold(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GOLD_EXTRA);
    }
    
    public static void addExtraEmeralds(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_EMERALD);
    }
    
    public static void addInfestedStone(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_INFESTED);
    }
    
    public static void addDefaultSoftDisks(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_SAND);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_CLAY);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_GRAVEL);
    }
    
    public static void addSwampClayDisk(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_CLAY);
    }
    
    public static void addMossyStoneBlock(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.FOREST_ROCK);
    }
    
    public static void addFerns(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_LARGE_FERN);
    }
    
    public static void addBerryBushes(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_BERRY_DECORATED);
    }
    
    public static void addSparseBerryBushes(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_BERRY_SPARSE);
    }
    
    public static void addLightBambooVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BAMBOO_LIGHT);
    }
    
    public static void addBambooVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BAMBOO);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BAMBOO_VEGETATION);
    }
    
    public static void addTaigaTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TAIGA_VEGETATION);
    }
    
    public static void addWaterTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_WATER);
    }
    
    public static void addBirchTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_BIRCH);
    }
    
    public static void addOtherBirchTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BIRCH_OTHER);
    }
    
    public static void addTallBirchTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BIRCH_TALL);
    }
    
    public static void addSavannaTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_SAVANNA);
    }
    
    public static void addShatteredSavannaTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_SHATTERED_SAVANNA);
    }
    
    public static void addMountainTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_MOUNTAIN);
    }
    
    public static void addMountainEdgeTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_MOUNTAIN_EDGE);
    }
    
    public static void addJungleTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_JUNGLE);
    }
    
    public static void addJungleEdgeTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_JUNGLE_EDGE);
    }
    
    public static void addBadlandsTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.OAK_BADLANDS);
    }
    
    public static void addSnowyTrees(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRUCE_SNOWY);
    }
    
    public static void addJungleGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_JUNGLE);
    }
    
    public static void addSavannaGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS);
    }
    
    public static void addShatteredSavannaGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_NORMAL);
    }
    
    public static void addSavannaExtraGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_SAVANNA);
    }
    
    public static void addBadlandGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_BADLANDS);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH_BADLANDS);
    }
    
    public static void addForestFlowers(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_VEGETATION);
    }
    
    public static void addForestGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST);
    }
    
    public static void addSwampVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SWAMP_TREE);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_SWAMP);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_NORMAL);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_WATERLILLY);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_SWAMP);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_SWAMP);
    }
    
    public static void addMushroomFieldVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.MUSHROOM_FIELD_VEGETATION);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_TAIGA);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_TAIGA);
    }
    
    public static void addPlainVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PLAIN_VEGETATION);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_PLAIN_DECORATED);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
    }
    
    public static void addDesertVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH_2);
    }
    
    public static void addGiantTaigaVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_TAIGA);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_GIANT);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_GIANT);
    }
    
    public static void addDefaultFlowers(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_DEFAULT);
    }
    
    public static void addWarmFlowers(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_WARM);
    }
    
    public static void addDefaultGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_BADLANDS);
    }
    
    public static void addTaigaGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_TAIGA_2);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_TAIGA);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_TAIGA);
    }
    
    public static void addPlainGrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS_2);
    }
    
    public static void addDefaultMushrooms(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_NORMAL);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_NORMAL);
    }
    
    public static void addDefaultExtraVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
    }
    
    public static void addBadlandExtraVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_BADLANDS);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS_DECORATED);
    }
    
    public static void addJungleExtraVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_MELON);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.VINES);
    }
    
    public static void addDesertExtraVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_DESERT);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS_DESERT);
    }
    
    public static void addSwampExtraVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_SWAMP);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
    }
    
    public static void addDesertExtraDecoration(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.WELL);
    }
    
    public static void addFossilDecoration(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, Features.FOSSIL);
    }
    
    public static void addColdOceanExtraVegetation(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.KELP_COLD);
    }
    
    public static void addDefaultSeagrass(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_SIMPLE);
    }
    
    public static void addLukeWarmKelp(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.KELP_WARM);
    }
    
    public static void addDefaultSprings(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_WATER);
        a.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
    }
    
    public static void addIcebergs(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.ICEBERG_PACKED);
        a.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.ICEBERG_BLUE);
    }
    
    public static void addBlueIce(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.BLUE_ICE);
    }
    
    public static void addSurfaceFreezing(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Features.FREEZE_TOP_LAYER);
    }
    
    public static void addNetherDefaultOres(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_GRAVEL_NETHER);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_BLACKSTONE);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_GOLD_NETHER);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_QUARTZ_NETHER);
        addAncientDebris(a);
    }
    
    public static void addAncientDebris(final BiomeGenerationSettings.Builder a) {
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_DEBRIS_LARGE);
        a.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_DEBRIS_SMALL);
    }
    
    public static void farmAnimals(final MobSpawnSettings.Builder a) {
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PIG, 10, 4, 4));
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 8, 4, 4));
    }
    
    public static void ambientSpawns(final MobSpawnSettings.Builder a) {
        a.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
    }
    
    public static void commonSpawns(final MobSpawnSettings.Builder a) {
        ambientSpawns(a);
        monsters(a, 95, 5, 100);
    }
    
    public static void oceanSpawns(final MobSpawnSettings.Builder a, final int integer2, final int integer3, final int integer4) {
        a.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, integer2, 1, integer3));
        a.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, integer4, 3, 6));
        commonSpawns(a);
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
    }
    
    public static void warmOceanSpawns(final MobSpawnSettings.Builder a, final int integer2, final int integer3) {
        a.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, integer2, integer3, 4));
        a.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
        a.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));
        commonSpawns(a);
    }
    
    public static void plainsSpawns(final MobSpawnSettings.Builder a) {
        farmAnimals(a);
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 5, 2, 6));
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 3));
        commonSpawns(a);
    }
    
    public static void snowySpawns(final MobSpawnSettings.Builder a) {
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 10, 2, 3));
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));
        ambientSpawns(a);
        monsters(a, 95, 5, 20);
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 80, 4, 4));
    }
    
    public static void desertSpawns(final MobSpawnSettings.Builder a) {
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        ambientSpawns(a);
        monsters(a, 19, 1, 100);
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 80, 4, 4));
    }
    
    public static void monsters(final MobSpawnSettings.Builder a, final int integer2, final int integer3, final int integer4) {
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, integer2, 4, 4));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, integer3, 1, 1));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, integer4, 4, 4));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));
    }
    
    public static void mooshroomSpawns(final MobSpawnSettings.Builder a) {
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 8, 4, 8));
        ambientSpawns(a);
    }
    
    public static void baseJungleSpawns(final MobSpawnSettings.Builder a) {
        farmAnimals(a);
        a.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        commonSpawns(a);
    }
    
    public static void endSpawns(final MobSpawnSettings.Builder a) {
        a.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 4, 4));
    }
}
