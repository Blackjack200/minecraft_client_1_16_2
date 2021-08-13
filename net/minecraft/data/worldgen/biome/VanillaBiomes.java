package net.minecraft.data.worldgen.biome;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.util.Mth;

public class VanillaBiomes {
    private static int calculateSkyColor(final float float1) {
        float float2 = float1;
        float2 /= 3.0f;
        float2 = Mth.clamp(float2, -1.0f, 1.0f);
        return Mth.hsvToRgb(0.62222224f - float2 * 0.05f, 0.5f + float2 * 0.1f, 1.0f);
    }
    
    public static Biome giantTreeTaiga(final float float1, final float float2, final float float3, final boolean boolean4) {
        final MobSpawnSettings.Builder a5 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a5);
        a5.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4));
        a5.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        a5.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));
        if (boolean4) {
            BiomeDefaultFeatures.commonSpawns(a5);
        }
        else {
            BiomeDefaultFeatures.ambientSpawns(a5);
            BiomeDefaultFeatures.monsters(a5, 100, 25, 100);
        }
        final BiomeGenerationSettings.Builder a6 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GIANT_TREE_TAIGA);
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a6);
        a6.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a6);
        BiomeDefaultFeatures.addDefaultLakes(a6);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a6);
        BiomeDefaultFeatures.addMossyStoneBlock(a6);
        BiomeDefaultFeatures.addFerns(a6);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a6);
        BiomeDefaultFeatures.addDefaultOres(a6);
        BiomeDefaultFeatures.addDefaultSoftDisks(a6);
        a6.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, boolean4 ? Features.TREES_GIANT_SPRUCE : Features.TREES_GIANT);
        BiomeDefaultFeatures.addDefaultFlowers(a6);
        BiomeDefaultFeatures.addGiantTaigaVegetation(a6);
        BiomeDefaultFeatures.addDefaultMushrooms(a6);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a6);
        BiomeDefaultFeatures.addDefaultSprings(a6);
        BiomeDefaultFeatures.addSparseBerryBushes(a6);
        BiomeDefaultFeatures.addSurfaceFreezing(a6);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.TAIGA).depth(float1).scale(float2).temperature(float3).downfall(0.8f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(float3)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a5.build()).generationSettings(a6.build()).build();
    }
    
    public static Biome birchForestBiome(final float float1, final float float2, final boolean boolean3) {
        final MobSpawnSettings.Builder a4 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a4);
        BiomeDefaultFeatures.commonSpawns(a4);
        final BiomeGenerationSettings.Builder a5 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a5);
        a5.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a5);
        BiomeDefaultFeatures.addDefaultLakes(a5);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a5);
        BiomeDefaultFeatures.addForestFlowers(a5);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a5);
        BiomeDefaultFeatures.addDefaultOres(a5);
        BiomeDefaultFeatures.addDefaultSoftDisks(a5);
        if (boolean3) {
            BiomeDefaultFeatures.addTallBirchTrees(a5);
        }
        else {
            BiomeDefaultFeatures.addBirchTrees(a5);
        }
        BiomeDefaultFeatures.addDefaultFlowers(a5);
        BiomeDefaultFeatures.addForestGrass(a5);
        BiomeDefaultFeatures.addDefaultMushrooms(a5);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a5);
        BiomeDefaultFeatures.addDefaultSprings(a5);
        BiomeDefaultFeatures.addSurfaceFreezing(a5);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.FOREST).depth(float1).scale(float2).temperature(0.6f).downfall(0.6f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.6f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a4.build()).generationSettings(a5.build()).build();
    }
    
    public static Biome jungleBiome() {
        return jungleBiome(0.1f, 0.2f, 40, 2, 3);
    }
    
    public static Biome jungleEdgeBiome() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(a1);
        return baseJungleBiome(0.1f, 0.2f, 0.8f, false, true, false, a1);
    }
    
    public static Biome modifiedJungleEdgeBiome() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(a1);
        return baseJungleBiome(0.2f, 0.4f, 0.8f, false, true, true, a1);
    }
    
    public static Biome modifiedJungleBiome() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(a1);
        a1.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 1, 1)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
        return baseJungleBiome(0.2f, 0.4f, 0.9f, false, false, true, a1);
    }
    
    public static Biome jungleHillsBiome() {
        return jungleBiome(0.45f, 0.3f, 10, 1, 1);
    }
    
    public static Biome bambooJungleBiome() {
        return bambooJungleBiome(0.1f, 0.2f, 40, 2);
    }
    
    public static Biome bambooJungleHillsBiome() {
        return bambooJungleBiome(0.45f, 0.3f, 10, 1);
    }
    
    private static Biome jungleBiome(final float float1, final float float2, final int integer3, final int integer4, final int integer5) {
        final MobSpawnSettings.Builder a6 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(a6);
        a6.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, integer3, 1, integer4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, integer5)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 1, 2));
        a6.setPlayerCanSpawn();
        return baseJungleBiome(float1, float2, 0.9f, false, false, false, a6);
    }
    
    private static Biome bambooJungleBiome(final float float1, final float float2, final int integer3, final int integer4) {
        final MobSpawnSettings.Builder a5 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(a5);
        a5.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, integer3, 1, integer4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 80, 1, 2)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
        return baseJungleBiome(float1, float2, 0.9f, true, false, false, a5);
    }
    
    private static Biome baseJungleBiome(final float float1, final float float2, final float float3, final boolean boolean4, final boolean boolean5, final boolean boolean6, final MobSpawnSettings.Builder a) {
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        if (!boolean5 && !boolean6) {
            a2.addStructureStart(StructureFeatures.JUNGLE_TEMPLE);
        }
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a2);
        a2.addStructureStart(StructureFeatures.RUINED_PORTAL_JUNGLE);
        BiomeDefaultFeatures.addDefaultCarvers(a2);
        BiomeDefaultFeatures.addDefaultLakes(a2);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a2);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a2);
        BiomeDefaultFeatures.addDefaultOres(a2);
        BiomeDefaultFeatures.addDefaultSoftDisks(a2);
        if (boolean4) {
            BiomeDefaultFeatures.addBambooVegetation(a2);
        }
        else {
            if (!boolean5 && !boolean6) {
                BiomeDefaultFeatures.addLightBambooVegetation(a2);
            }
            if (boolean5) {
                BiomeDefaultFeatures.addJungleEdgeTrees(a2);
            }
            else {
                BiomeDefaultFeatures.addJungleTrees(a2);
            }
        }
        BiomeDefaultFeatures.addWarmFlowers(a2);
        BiomeDefaultFeatures.addJungleGrass(a2);
        BiomeDefaultFeatures.addDefaultMushrooms(a2);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a2);
        BiomeDefaultFeatures.addDefaultSprings(a2);
        BiomeDefaultFeatures.addJungleExtraVegetation(a2);
        BiomeDefaultFeatures.addSurfaceFreezing(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.JUNGLE).depth(float1).scale(float2).temperature(0.95f).downfall(float3).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.95f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a.build()).generationSettings(a2.build()).build();
    }
    
    public static Biome mountainBiome(final float float1, final float float2, final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ctd, final boolean boolean4) {
        final MobSpawnSettings.Builder a5 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a5);
        a5.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 5, 4, 6));
        BiomeDefaultFeatures.commonSpawns(a5);
        final BiomeGenerationSettings.Builder a6 = new BiomeGenerationSettings.Builder().surfaceBuilder(ctd);
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a6);
        a6.addStructureStart(StructureFeatures.RUINED_PORTAL_MOUNTAIN);
        BiomeDefaultFeatures.addDefaultCarvers(a6);
        BiomeDefaultFeatures.addDefaultLakes(a6);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a6);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a6);
        BiomeDefaultFeatures.addDefaultOres(a6);
        BiomeDefaultFeatures.addDefaultSoftDisks(a6);
        if (boolean4) {
            BiomeDefaultFeatures.addMountainEdgeTrees(a6);
        }
        else {
            BiomeDefaultFeatures.addMountainTrees(a6);
        }
        BiomeDefaultFeatures.addDefaultFlowers(a6);
        BiomeDefaultFeatures.addDefaultGrass(a6);
        BiomeDefaultFeatures.addDefaultMushrooms(a6);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a6);
        BiomeDefaultFeatures.addDefaultSprings(a6);
        BiomeDefaultFeatures.addExtraEmeralds(a6);
        BiomeDefaultFeatures.addInfestedStone(a6);
        BiomeDefaultFeatures.addSurfaceFreezing(a6);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.EXTREME_HILLS).depth(float1).scale(float2).temperature(0.2f).downfall(0.3f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.2f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a5.build()).generationSettings(a6.build()).build();
    }
    
    public static Biome desertBiome(final float float1, final float float2, final boolean boolean3, final boolean boolean4, final boolean boolean5) {
        final MobSpawnSettings.Builder a6 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.desertSpawns(a6);
        final BiomeGenerationSettings.Builder a7 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.DESERT);
        if (boolean3) {
            a7.addStructureStart(StructureFeatures.VILLAGE_DESERT);
            a7.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
        }
        if (boolean4) {
            a7.addStructureStart(StructureFeatures.DESERT_PYRAMID);
        }
        if (boolean5) {
            BiomeDefaultFeatures.addFossilDecoration(a7);
        }
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a7);
        a7.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);
        BiomeDefaultFeatures.addDefaultCarvers(a7);
        BiomeDefaultFeatures.addDesertLakes(a7);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a7);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a7);
        BiomeDefaultFeatures.addDefaultOres(a7);
        BiomeDefaultFeatures.addDefaultSoftDisks(a7);
        BiomeDefaultFeatures.addDefaultFlowers(a7);
        BiomeDefaultFeatures.addDefaultGrass(a7);
        BiomeDefaultFeatures.addDesertVegetation(a7);
        BiomeDefaultFeatures.addDefaultMushrooms(a7);
        BiomeDefaultFeatures.addDesertExtraVegetation(a7);
        BiomeDefaultFeatures.addDefaultSprings(a7);
        BiomeDefaultFeatures.addDesertExtraDecoration(a7);
        BiomeDefaultFeatures.addSurfaceFreezing(a7);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.DESERT).depth(float1).scale(float2).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a6.build()).generationSettings(a7.build()).build();
    }
    
    public static Biome plainsBiome(final boolean boolean1) {
        final MobSpawnSettings.Builder a2 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(a2);
        if (!boolean1) {
            a2.setPlayerCanSpawn();
        }
        final BiomeGenerationSettings.Builder a3 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        if (!boolean1) {
            a3.addStructureStart(StructureFeatures.VILLAGE_PLAINS).addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
        }
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a3);
        a3.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a3);
        BiomeDefaultFeatures.addDefaultLakes(a3);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a3);
        BiomeDefaultFeatures.addPlainGrass(a3);
        if (boolean1) {
            a3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUNFLOWER);
        }
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a3);
        BiomeDefaultFeatures.addDefaultOres(a3);
        BiomeDefaultFeatures.addDefaultSoftDisks(a3);
        BiomeDefaultFeatures.addPlainVegetation(a3);
        if (boolean1) {
            a3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE);
        }
        BiomeDefaultFeatures.addDefaultMushrooms(a3);
        if (boolean1) {
            a3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
        }
        else {
            BiomeDefaultFeatures.addDefaultExtraVegetation(a3);
        }
        BiomeDefaultFeatures.addDefaultSprings(a3);
        BiomeDefaultFeatures.addSurfaceFreezing(a3);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.PLAINS).depth(0.125f).scale(0.05f).temperature(0.8f).downfall(0.4f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.8f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a2.build()).generationSettings(a3.build()).build();
    }
    
    private static Biome baseEndBiome(final BiomeGenerationSettings.Builder a) {
        final MobSpawnSettings.Builder a2 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.endSpawns(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.THEEND).depth(0.1f).scale(0.2f).temperature(0.5f).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(10518688).skyColor(0).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a2.build()).generationSettings(a.build()).build();
    }
    
    public static Biome endBarrensBiome() {
        final BiomeGenerationSettings.Builder a1 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.END);
        return baseEndBiome(a1);
    }
    
    public static Biome theEndBiome() {
        final BiomeGenerationSettings.Builder a1 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.END).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.END_SPIKE);
        return baseEndBiome(a1);
    }
    
    public static Biome endMidlandsBiome() {
        final BiomeGenerationSettings.Builder a1 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.END).addStructureStart(StructureFeatures.END_CITY);
        return baseEndBiome(a1);
    }
    
    public static Biome endHighlandsBiome() {
        final BiomeGenerationSettings.Builder a1 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.END).addStructureStart(StructureFeatures.END_CITY).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.END_GATEWAY).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CHORUS_PLANT);
        return baseEndBiome(a1);
    }
    
    public static Biome smallEndIslandsBiome() {
        final BiomeGenerationSettings.Builder a1 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.END).addFeature(GenerationStep.Decoration.RAW_GENERATION, Features.END_ISLAND_DECORATED);
        return baseEndBiome(a1);
    }
    
    public static Biome mushroomFieldsBiome(final float float1, final float float2) {
        final MobSpawnSettings.Builder a3 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.mooshroomSpawns(a3);
        final BiomeGenerationSettings.Builder a4 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.MYCELIUM);
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a4);
        a4.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a4);
        BiomeDefaultFeatures.addDefaultLakes(a4);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a4);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a4);
        BiomeDefaultFeatures.addDefaultOres(a4);
        BiomeDefaultFeatures.addDefaultSoftDisks(a4);
        BiomeDefaultFeatures.addMushroomFieldVegetation(a4);
        BiomeDefaultFeatures.addDefaultMushrooms(a4);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a4);
        BiomeDefaultFeatures.addDefaultSprings(a4);
        BiomeDefaultFeatures.addSurfaceFreezing(a4);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.MUSHROOM).depth(float1).scale(float2).temperature(0.9f).downfall(1.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.9f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a3.build()).generationSettings(a4.build()).build();
    }
    
    private static Biome baseSavannaBiome(final float float1, final float float2, final float float3, final boolean boolean4, final boolean boolean5, final MobSpawnSettings.Builder a) {
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(boolean5 ? SurfaceBuilders.SHATTERED_SAVANNA : SurfaceBuilders.GRASS);
        if (!boolean4 && !boolean5) {
            a2.addStructureStart(StructureFeatures.VILLAGE_SAVANNA).addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
        }
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a2);
        a2.addStructureStart(boolean4 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a2);
        BiomeDefaultFeatures.addDefaultLakes(a2);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a2);
        if (!boolean5) {
            BiomeDefaultFeatures.addSavannaGrass(a2);
        }
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a2);
        BiomeDefaultFeatures.addDefaultOres(a2);
        BiomeDefaultFeatures.addDefaultSoftDisks(a2);
        if (boolean5) {
            BiomeDefaultFeatures.addShatteredSavannaTrees(a2);
            BiomeDefaultFeatures.addDefaultFlowers(a2);
            BiomeDefaultFeatures.addShatteredSavannaGrass(a2);
        }
        else {
            BiomeDefaultFeatures.addSavannaTrees(a2);
            BiomeDefaultFeatures.addWarmFlowers(a2);
            BiomeDefaultFeatures.addSavannaExtraGrass(a2);
        }
        BiomeDefaultFeatures.addDefaultMushrooms(a2);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a2);
        BiomeDefaultFeatures.addDefaultSprings(a2);
        BiomeDefaultFeatures.addSurfaceFreezing(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.SAVANNA).depth(float1).scale(float2).temperature(float3).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(float3)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a.build()).generationSettings(a2.build()).build();
    }
    
    public static Biome savannaBiome(final float float1, final float float2, final float float3, final boolean boolean4, final boolean boolean5) {
        final MobSpawnSettings.Builder a6 = savannaMobs();
        return baseSavannaBiome(float1, float2, float3, boolean4, boolean5, a6);
    }
    
    private static MobSpawnSettings.Builder savannaMobs() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a1);
        a1.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 1, 2, 6)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 1));
        BiomeDefaultFeatures.commonSpawns(a1);
        return a1;
    }
    
    public static Biome savanaPlateauBiome() {
        final MobSpawnSettings.Builder a1 = savannaMobs();
        a1.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 8, 4, 4));
        return baseSavannaBiome(1.5f, 0.025f, 1.0f, true, false, a1);
    }
    
    private static Biome baseBadlandsBiome(final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ctd, final float float2, final float float3, final boolean boolean4, final boolean boolean5) {
        final MobSpawnSettings.Builder a6 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(a6);
        final BiomeGenerationSettings.Builder a7 = new BiomeGenerationSettings.Builder().surfaceBuilder(ctd);
        BiomeDefaultFeatures.addDefaultOverworldLandMesaStructures(a7);
        a7.addStructureStart(boolean4 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a7);
        BiomeDefaultFeatures.addDefaultLakes(a7);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a7);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a7);
        BiomeDefaultFeatures.addDefaultOres(a7);
        BiomeDefaultFeatures.addExtraGold(a7);
        BiomeDefaultFeatures.addDefaultSoftDisks(a7);
        if (boolean5) {
            BiomeDefaultFeatures.addBadlandsTrees(a7);
        }
        BiomeDefaultFeatures.addBadlandGrass(a7);
        BiomeDefaultFeatures.addDefaultMushrooms(a7);
        BiomeDefaultFeatures.addBadlandExtraVegetation(a7);
        BiomeDefaultFeatures.addDefaultSprings(a7);
        BiomeDefaultFeatures.addSurfaceFreezing(a7);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.MESA).depth(float2).scale(float3).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0f)).foliageColorOverride(10387789).grassColorOverride(9470285).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a6.build()).generationSettings(a7.build()).build();
    }
    
    public static Biome badlandsBiome(final float float1, final float float2, final boolean boolean3) {
        return baseBadlandsBiome(SurfaceBuilders.BADLANDS, float1, float2, boolean3, false);
    }
    
    public static Biome woodedBadlandsPlateauBiome(final float float1, final float float2) {
        return baseBadlandsBiome(SurfaceBuilders.WOODED_BADLANDS, float1, float2, true, true);
    }
    
    public static Biome erodedBadlandsBiome() {
        return baseBadlandsBiome(SurfaceBuilders.ERODED_BADLANDS, 0.1f, 0.2f, true, false);
    }
    
    private static Biome baseOceanBiome(final MobSpawnSettings.Builder a, final int integer2, final int integer3, final boolean boolean4, final BiomeGenerationSettings.Builder a) {
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.OCEAN).depth(boolean4 ? -1.8f : -1.0f).scale(0.1f).temperature(0.5f).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(integer2).waterFogColor(integer3).fogColor(12638463).skyColor(calculateSkyColor(0.5f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a.build()).generationSettings(a.build()).build();
    }
    
    private static BiomeGenerationSettings.Builder baseOceanGeneration(final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ctd, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        final BiomeGenerationSettings.Builder a5 = new BiomeGenerationSettings.Builder().surfaceBuilder(ctd);
        final ConfiguredStructureFeature<?, ?> cit6 = boolean3 ? StructureFeatures.OCEAN_RUIN_WARM : StructureFeatures.OCEAN_RUIN_COLD;
        if (boolean4) {
            if (boolean2) {
                a5.addStructureStart(StructureFeatures.OCEAN_MONUMENT);
            }
            BiomeDefaultFeatures.addDefaultOverworldOceanStructures(a5);
            a5.addStructureStart(cit6);
        }
        else {
            a5.addStructureStart(cit6);
            if (boolean2) {
                a5.addStructureStart(StructureFeatures.OCEAN_MONUMENT);
            }
            BiomeDefaultFeatures.addDefaultOverworldOceanStructures(a5);
        }
        a5.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
        BiomeDefaultFeatures.addOceanCarvers(a5);
        BiomeDefaultFeatures.addDefaultLakes(a5);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a5);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a5);
        BiomeDefaultFeatures.addDefaultOres(a5);
        BiomeDefaultFeatures.addDefaultSoftDisks(a5);
        BiomeDefaultFeatures.addWaterTrees(a5);
        BiomeDefaultFeatures.addDefaultFlowers(a5);
        BiomeDefaultFeatures.addDefaultGrass(a5);
        BiomeDefaultFeatures.addDefaultMushrooms(a5);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a5);
        BiomeDefaultFeatures.addDefaultSprings(a5);
        return a5;
    }
    
    public static Biome coldOceanBiome(final boolean boolean1) {
        final MobSpawnSettings.Builder a2 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.oceanSpawns(a2, 3, 4, 15);
        a2.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5));
        final boolean boolean2 = !boolean1;
        final BiomeGenerationSettings.Builder a3 = baseOceanGeneration(SurfaceBuilders.GRASS, boolean1, false, boolean2);
        a3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, boolean1 ? Features.SEAGRASS_DEEP_COLD : Features.SEAGRASS_COLD);
        BiomeDefaultFeatures.addDefaultSeagrass(a3);
        BiomeDefaultFeatures.addColdOceanExtraVegetation(a3);
        BiomeDefaultFeatures.addSurfaceFreezing(a3);
        return baseOceanBiome(a2, 4020182, 329011, boolean1, a3);
    }
    
    public static Biome oceanBiome(final boolean boolean1) {
        final MobSpawnSettings.Builder a2 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.oceanSpawns(a2, 1, 4, 10);
        a2.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 1, 2));
        final BiomeGenerationSettings.Builder a3 = baseOceanGeneration(SurfaceBuilders.GRASS, boolean1, false, true);
        a3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, boolean1 ? Features.SEAGRASS_DEEP : Features.SEAGRASS_NORMAL);
        BiomeDefaultFeatures.addDefaultSeagrass(a3);
        BiomeDefaultFeatures.addColdOceanExtraVegetation(a3);
        BiomeDefaultFeatures.addSurfaceFreezing(a3);
        return baseOceanBiome(a2, 4159204, 329011, boolean1, a3);
    }
    
    public static Biome lukeWarmOceanBiome(final boolean boolean1) {
        final MobSpawnSettings.Builder a2 = new MobSpawnSettings.Builder();
        if (boolean1) {
            BiomeDefaultFeatures.oceanSpawns(a2, 8, 4, 8);
        }
        else {
            BiomeDefaultFeatures.oceanSpawns(a2, 10, 2, 15);
        }
        a2.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 5, 1, 3)).addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8)).addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));
        final BiomeGenerationSettings.Builder a3 = baseOceanGeneration(SurfaceBuilders.OCEAN_SAND, boolean1, true, false);
        a3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, boolean1 ? Features.SEAGRASS_DEEP_WARM : Features.SEAGRASS_WARM);
        if (boolean1) {
            BiomeDefaultFeatures.addDefaultSeagrass(a3);
        }
        BiomeDefaultFeatures.addLukeWarmKelp(a3);
        BiomeDefaultFeatures.addSurfaceFreezing(a3);
        return baseOceanBiome(a2, 4566514, 267827, boolean1, a3);
    }
    
    public static Biome warmOceanBiome() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 15, 1, 3));
        BiomeDefaultFeatures.warmOceanSpawns(a1, 10, 4);
        final BiomeGenerationSettings.Builder a2 = baseOceanGeneration(SurfaceBuilders.FULL_SAND, false, true, false).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARM_OCEAN_VEGETATION).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_WARM).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEA_PICKLE);
        BiomeDefaultFeatures.addSurfaceFreezing(a2);
        return baseOceanBiome(a1, 4445678, 270131, false, a2);
    }
    
    public static Biome deepWarmOceanBiome() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.warmOceanSpawns(a1, 5, 1);
        a1.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
        final BiomeGenerationSettings.Builder a2 = baseOceanGeneration(SurfaceBuilders.FULL_SAND, true, true, false).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_DEEP_WARM);
        BiomeDefaultFeatures.addDefaultSeagrass(a2);
        BiomeDefaultFeatures.addSurfaceFreezing(a2);
        return baseOceanBiome(a1, 4445678, 270131, true, a2);
    }
    
    public static Biome frozenOceanBiome(final boolean boolean1) {
        final MobSpawnSettings.Builder a2 = new MobSpawnSettings.Builder().addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));
        BiomeDefaultFeatures.commonSpawns(a2);
        a2.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
        final float float3 = boolean1 ? 0.5f : 0.0f;
        final BiomeGenerationSettings.Builder a3 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.FROZEN_OCEAN);
        a3.addStructureStart(StructureFeatures.OCEAN_RUIN_COLD);
        if (boolean1) {
            a3.addStructureStart(StructureFeatures.OCEAN_MONUMENT);
        }
        BiomeDefaultFeatures.addDefaultOverworldOceanStructures(a3);
        a3.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
        BiomeDefaultFeatures.addOceanCarvers(a3);
        BiomeDefaultFeatures.addDefaultLakes(a3);
        BiomeDefaultFeatures.addIcebergs(a3);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a3);
        BiomeDefaultFeatures.addBlueIce(a3);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a3);
        BiomeDefaultFeatures.addDefaultOres(a3);
        BiomeDefaultFeatures.addDefaultSoftDisks(a3);
        BiomeDefaultFeatures.addWaterTrees(a3);
        BiomeDefaultFeatures.addDefaultFlowers(a3);
        BiomeDefaultFeatures.addDefaultGrass(a3);
        BiomeDefaultFeatures.addDefaultMushrooms(a3);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a3);
        BiomeDefaultFeatures.addDefaultSprings(a3);
        BiomeDefaultFeatures.addSurfaceFreezing(a3);
        return new Biome.BiomeBuilder().precipitation(boolean1 ? Biome.Precipitation.RAIN : Biome.Precipitation.SNOW).biomeCategory(Biome.BiomeCategory.OCEAN).depth(boolean1 ? -1.8f : -1.0f).scale(0.1f).temperature(float3).temperatureAdjustment(Biome.TemperatureModifier.FROZEN).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(3750089).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(float3)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a2.build()).generationSettings(a3.build()).build();
    }
    
    private static Biome baseForestBiome(final float float1, final float float2, final boolean boolean3, final MobSpawnSettings.Builder a) {
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a2);
        a2.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a2);
        BiomeDefaultFeatures.addDefaultLakes(a2);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a2);
        if (boolean3) {
            a2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_VEGETATION_COMMON);
        }
        else {
            BiomeDefaultFeatures.addForestFlowers(a2);
        }
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a2);
        BiomeDefaultFeatures.addDefaultOres(a2);
        BiomeDefaultFeatures.addDefaultSoftDisks(a2);
        if (boolean3) {
            a2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_TREES);
            a2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_FOREST);
            BiomeDefaultFeatures.addDefaultGrass(a2);
        }
        else {
            BiomeDefaultFeatures.addOtherBirchTrees(a2);
            BiomeDefaultFeatures.addDefaultFlowers(a2);
            BiomeDefaultFeatures.addForestGrass(a2);
        }
        BiomeDefaultFeatures.addDefaultMushrooms(a2);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a2);
        BiomeDefaultFeatures.addDefaultSprings(a2);
        BiomeDefaultFeatures.addSurfaceFreezing(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.FOREST).depth(float1).scale(float2).temperature(0.7f).downfall(0.8f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.7f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a.build()).generationSettings(a2.build()).build();
    }
    
    private static MobSpawnSettings.Builder defaultSpawns() {
        final MobSpawnSettings.Builder a1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a1);
        BiomeDefaultFeatures.commonSpawns(a1);
        return a1;
    }
    
    public static Biome forestBiome(final float float1, final float float2) {
        final MobSpawnSettings.Builder a3 = defaultSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4)).setPlayerCanSpawn();
        return baseForestBiome(float1, float2, false, a3);
    }
    
    public static Biome flowerForestBiome() {
        final MobSpawnSettings.Builder a1 = defaultSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        return baseForestBiome(0.1f, 0.4f, true, a1);
    }
    
    public static Biome taigaBiome(final float float1, final float float2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
        final MobSpawnSettings.Builder a7 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a7);
        a7.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));
        if (!boolean3 && !boolean4) {
            a7.setPlayerCanSpawn();
        }
        BiomeDefaultFeatures.commonSpawns(a7);
        final float float3 = boolean3 ? -0.5f : 0.25f;
        final BiomeGenerationSettings.Builder a8 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        if (boolean5) {
            a8.addStructureStart(StructureFeatures.VILLAGE_TAIGA);
            a8.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
        }
        if (boolean6) {
            a8.addStructureStart(StructureFeatures.IGLOO);
        }
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a8);
        a8.addStructureStart(boolean4 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a8);
        BiomeDefaultFeatures.addDefaultLakes(a8);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a8);
        BiomeDefaultFeatures.addFerns(a8);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a8);
        BiomeDefaultFeatures.addDefaultOres(a8);
        BiomeDefaultFeatures.addDefaultSoftDisks(a8);
        BiomeDefaultFeatures.addTaigaTrees(a8);
        BiomeDefaultFeatures.addDefaultFlowers(a8);
        BiomeDefaultFeatures.addTaigaGrass(a8);
        BiomeDefaultFeatures.addDefaultMushrooms(a8);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a8);
        BiomeDefaultFeatures.addDefaultSprings(a8);
        if (boolean3) {
            BiomeDefaultFeatures.addBerryBushes(a8);
        }
        else {
            BiomeDefaultFeatures.addSparseBerryBushes(a8);
        }
        BiomeDefaultFeatures.addSurfaceFreezing(a8);
        return new Biome.BiomeBuilder().precipitation(boolean3 ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.TAIGA).depth(float1).scale(float2).temperature(float3).downfall(boolean3 ? 0.4f : 0.8f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(boolean3 ? 4020182 : 4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(float3)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a7.build()).generationSettings(a8.build()).build();
    }
    
    public static Biome darkForestBiome(final float float1, final float float2, final boolean boolean3) {
        final MobSpawnSettings.Builder a4 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a4);
        BiomeDefaultFeatures.commonSpawns(a4);
        final BiomeGenerationSettings.Builder a5 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        a5.addStructureStart(StructureFeatures.WOODLAND_MANSION);
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a5);
        a5.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a5);
        BiomeDefaultFeatures.addDefaultLakes(a5);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a5);
        a5.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, boolean3 ? Features.DARK_FOREST_VEGETATION_RED : Features.DARK_FOREST_VEGETATION_BROWN);
        BiomeDefaultFeatures.addForestFlowers(a5);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a5);
        BiomeDefaultFeatures.addDefaultOres(a5);
        BiomeDefaultFeatures.addDefaultSoftDisks(a5);
        BiomeDefaultFeatures.addDefaultFlowers(a5);
        BiomeDefaultFeatures.addForestGrass(a5);
        BiomeDefaultFeatures.addDefaultMushrooms(a5);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a5);
        BiomeDefaultFeatures.addDefaultSprings(a5);
        BiomeDefaultFeatures.addSurfaceFreezing(a5);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.FOREST).depth(float1).scale(float2).temperature(0.7f).downfall(0.8f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.7f)).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a4.build()).generationSettings(a5.build()).build();
    }
    
    public static Biome swampBiome(final float float1, final float float2, final boolean boolean3) {
        final MobSpawnSettings.Builder a4 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(a4);
        BiomeDefaultFeatures.commonSpawns(a4);
        a4.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 1, 1, 1));
        final BiomeGenerationSettings.Builder a5 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.SWAMP);
        if (!boolean3) {
            a5.addStructureStart(StructureFeatures.SWAMP_HUT);
        }
        a5.addStructureStart(StructureFeatures.MINESHAFT);
        a5.addStructureStart(StructureFeatures.RUINED_PORTAL_SWAMP);
        BiomeDefaultFeatures.addDefaultCarvers(a5);
        if (!boolean3) {
            BiomeDefaultFeatures.addFossilDecoration(a5);
        }
        BiomeDefaultFeatures.addDefaultLakes(a5);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a5);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a5);
        BiomeDefaultFeatures.addDefaultOres(a5);
        BiomeDefaultFeatures.addSwampClayDisk(a5);
        BiomeDefaultFeatures.addSwampVegetation(a5);
        BiomeDefaultFeatures.addDefaultMushrooms(a5);
        BiomeDefaultFeatures.addSwampExtraVegetation(a5);
        BiomeDefaultFeatures.addDefaultSprings(a5);
        if (boolean3) {
            BiomeDefaultFeatures.addFossilDecoration(a5);
        }
        else {
            a5.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_SWAMP);
        }
        BiomeDefaultFeatures.addSurfaceFreezing(a5);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.SWAMP).depth(float1).scale(float2).temperature(0.8f).downfall(0.9f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(6388580).waterFogColor(2302743).fogColor(12638463).skyColor(calculateSkyColor(0.8f)).foliageColorOverride(6975545).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a4.build()).generationSettings(a5.build()).build();
    }
    
    public static Biome tundraBiome(final float float1, final float float2, final boolean boolean3, final boolean boolean4) {
        final MobSpawnSettings.Builder a5 = new MobSpawnSettings.Builder().creatureGenerationProbability(0.07f);
        BiomeDefaultFeatures.snowySpawns(a5);
        final BiomeGenerationSettings.Builder a6 = new BiomeGenerationSettings.Builder().surfaceBuilder(boolean3 ? SurfaceBuilders.ICE_SPIKES : SurfaceBuilders.GRASS);
        if (!boolean3 && !boolean4) {
            a6.addStructureStart(StructureFeatures.VILLAGE_SNOWY).addStructureStart(StructureFeatures.IGLOO);
        }
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(a6);
        if (!boolean3 && !boolean4) {
            a6.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
        }
        a6.addStructureStart(boolean4 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a6);
        BiomeDefaultFeatures.addDefaultLakes(a6);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a6);
        if (boolean3) {
            a6.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.ICE_SPIKE);
            a6.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.ICE_PATCH);
        }
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a6);
        BiomeDefaultFeatures.addDefaultOres(a6);
        BiomeDefaultFeatures.addDefaultSoftDisks(a6);
        BiomeDefaultFeatures.addSnowyTrees(a6);
        BiomeDefaultFeatures.addDefaultFlowers(a6);
        BiomeDefaultFeatures.addDefaultGrass(a6);
        BiomeDefaultFeatures.addDefaultMushrooms(a6);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a6);
        BiomeDefaultFeatures.addDefaultSprings(a6);
        BiomeDefaultFeatures.addSurfaceFreezing(a6);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.SNOW).biomeCategory(Biome.BiomeCategory.ICY).depth(float1).scale(float2).temperature(0.0f).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.0f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a5.build()).generationSettings(a6.build()).build();
    }
    
    public static Biome riverBiome(final float float1, final float float2, final float float3, final int integer, final boolean boolean5) {
        final MobSpawnSettings.Builder a6 = new MobSpawnSettings.Builder().addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 2, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 5, 1, 5));
        BiomeDefaultFeatures.commonSpawns(a6);
        a6.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, boolean5 ? 1 : 100, 1, 1));
        final BiomeGenerationSettings.Builder a7 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS);
        a7.addStructureStart(StructureFeatures.MINESHAFT);
        a7.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a7);
        BiomeDefaultFeatures.addDefaultLakes(a7);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a7);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a7);
        BiomeDefaultFeatures.addDefaultOres(a7);
        BiomeDefaultFeatures.addDefaultSoftDisks(a7);
        BiomeDefaultFeatures.addWaterTrees(a7);
        BiomeDefaultFeatures.addDefaultFlowers(a7);
        BiomeDefaultFeatures.addDefaultGrass(a7);
        BiomeDefaultFeatures.addDefaultMushrooms(a7);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a7);
        BiomeDefaultFeatures.addDefaultSprings(a7);
        if (!boolean5) {
            a7.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_RIVER);
        }
        BiomeDefaultFeatures.addSurfaceFreezing(a7);
        return new Biome.BiomeBuilder().precipitation(boolean5 ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN).biomeCategory(Biome.BiomeCategory.RIVER).depth(float1).scale(float2).temperature(float3).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(integer).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(float3)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a6.build()).generationSettings(a7.build()).build();
    }
    
    public static Biome beachBiome(final float float1, final float float2, final float float3, final float float4, final int integer, final boolean boolean6, final boolean boolean7) {
        final MobSpawnSettings.Builder a8 = new MobSpawnSettings.Builder();
        if (!boolean7 && !boolean6) {
            a8.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.TURTLE, 5, 2, 5));
        }
        BiomeDefaultFeatures.commonSpawns(a8);
        final BiomeGenerationSettings.Builder a9 = new BiomeGenerationSettings.Builder().surfaceBuilder(boolean7 ? SurfaceBuilders.STONE : SurfaceBuilders.DESERT);
        if (boolean7) {
            BiomeDefaultFeatures.addDefaultOverworldLandStructures(a9);
        }
        else {
            a9.addStructureStart(StructureFeatures.MINESHAFT);
            a9.addStructureStart(StructureFeatures.BURIED_TREASURE);
            a9.addStructureStart(StructureFeatures.SHIPWRECH_BEACHED);
        }
        a9.addStructureStart(boolean7 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(a9);
        BiomeDefaultFeatures.addDefaultLakes(a9);
        BiomeDefaultFeatures.addDefaultMonsterRoom(a9);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(a9);
        BiomeDefaultFeatures.addDefaultOres(a9);
        BiomeDefaultFeatures.addDefaultSoftDisks(a9);
        BiomeDefaultFeatures.addDefaultFlowers(a9);
        BiomeDefaultFeatures.addDefaultGrass(a9);
        BiomeDefaultFeatures.addDefaultMushrooms(a9);
        BiomeDefaultFeatures.addDefaultExtraVegetation(a9);
        BiomeDefaultFeatures.addDefaultSprings(a9);
        BiomeDefaultFeatures.addSurfaceFreezing(a9);
        return new Biome.BiomeBuilder().precipitation(boolean6 ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN).biomeCategory(boolean7 ? Biome.BiomeCategory.NONE : Biome.BiomeCategory.BEACH).depth(float1).scale(float2).temperature(float3).downfall(float4).specialEffects(new BiomeSpecialEffects.Builder().waterColor(integer).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(float3)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(a8.build()).generationSettings(a9.build()).build();
    }
    
    public static Biome theVoidBiome() {
        final BiomeGenerationSettings.Builder a1 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.NOPE);
        a1.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Features.VOID_START_PLATFORM);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NONE).depth(0.1f).scale(0.2f).temperature(0.5f).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(0.5f)).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings(MobSpawnSettings.EMPTY).generationSettings(a1.build()).build();
    }
    
    public static Biome netherWastesBiome() {
        final MobSpawnSettings btd1 = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 15, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.NETHER).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.BASTION_REMNANT).<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
        BiomeDefaultFeatures.addDefaultMushrooms(a2);
        a2.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BROWN_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.RED_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED);
        BiomeDefaultFeatures.addNetherDefaultOres(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NETHER).depth(0.1f).scale(0.2f).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(3344392).skyColor(calculateSkyColor(2.0f)).ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111)).backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_NETHER_WASTES)).build()).mobSpawnSettings(btd1).generationSettings(a2.build()).build();
    }
    
    public static Biome soulSandValleyBiome() {
        final double double1 = 0.7;
        final double double2 = 0.15;
        final MobSpawnSettings btd5 = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 20, 5, 5)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).addMobCharge(EntityType.SKELETON, 0.7, 0.15).addMobCharge(EntityType.GHAST, 0.7, 0.15).addMobCharge(EntityType.ENDERMAN, 0.7, 0.15).addMobCharge(EntityType.STRIDER, 0.7, 0.15).build();
        final BiomeGenerationSettings.Builder a6 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.SOUL_SAND_VALLEY).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.NETHER_FOSSIL).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addStructureStart(StructureFeatures.BASTION_REMNANT).<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA).addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.BASALT_PILLAR).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_CRIMSON_ROOTS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_SOUL_SAND);
        BiomeDefaultFeatures.addNetherDefaultOres(a6);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NETHER).depth(0.1f).scale(0.2f).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(1787717).skyColor(calculateSkyColor(2.0f)).ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.00625f)).ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111)).backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build()).mobSpawnSettings(btd5).generationSettings(a6.build()).build();
    }
    
    public static Biome basaltDeltasBiome() {
        final MobSpawnSettings btd1 = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 40, 1, 1)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 100, 2, 5)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.BASALT_DELTAS).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addStructureStart(StructureFeatures.NETHER_BRIDGE).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.DELTA).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA_DOUBLE).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.SMALL_BASALT_COLUMNS).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.LARGE_BASALT_COLUMNS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BASALT_BLOBS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BLACKSTONE_BLOBS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_DELTA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BROWN_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.RED_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED_DOUBLE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_GOLD_DELTAS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_QUARTZ_DELTAS);
        BiomeDefaultFeatures.addAncientDebris(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NETHER).depth(0.1f).scale(0.2f).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(4341314).fogColor(6840176).skyColor(calculateSkyColor(2.0f)).ambientParticle(new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.118093334f)).ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111)).backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS)).build()).mobSpawnSettings(btd1).generationSettings(a2.build()).build();
    }
    
    public static Biome crimsonForestBiome() {
        final MobSpawnSettings btd1 = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 9, 3, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 5, 3, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.CRIMSON_FOREST).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.BASTION_REMNANT).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
        BiomeDefaultFeatures.addDefaultMushrooms(a2);
        a2.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WEEPING_VINES).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CRIMSON_FUNGI).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CRIMSON_FOREST_VEGETATION);
        BiomeDefaultFeatures.addNetherDefaultOres(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NETHER).depth(0.1f).scale(0.2f).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(3343107).skyColor(calculateSkyColor(2.0f)).ambientParticle(new AmbientParticleSettings(ParticleTypes.CRIMSON_SPORE, 0.025f)).ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111)).backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)).build()).mobSpawnSettings(btd1).generationSettings(a2.build()).build();
    }
    
    public static Biome warpedForestBiome() {
        final MobSpawnSettings btd1 = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).addMobCharge(EntityType.ENDERMAN, 1.0, 0.12).build();
        final BiomeGenerationSettings.Builder a2 = new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.WARPED_FOREST).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.BASTION_REMNANT).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).<ProbabilityFeatureConfiguration>addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
        BiomeDefaultFeatures.addDefaultMushrooms(a2);
        a2.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARPED_FUNGI).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARPED_FOREST_VEGETATION).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.NETHER_SPROUTS).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TWISTING_VINES);
        BiomeDefaultFeatures.addNetherDefaultOres(a2);
        return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NETHER).depth(0.1f).scale(0.2f).temperature(2.0f).downfall(0.0f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(1705242).skyColor(calculateSkyColor(2.0f)).ambientParticle(new AmbientParticleSettings(ParticleTypes.WARPED_SPORE, 0.01428f)).ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111)).backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST)).build()).mobSpawnSettings(btd1).generationSettings(a2.build()).build();
    }
}
