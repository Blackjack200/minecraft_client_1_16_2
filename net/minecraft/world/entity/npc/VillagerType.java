package net.minecraft.world.entity.npc;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.world.level.biome.Biomes;
import java.util.HashMap;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import java.util.Map;

public final class VillagerType {
    public static final VillagerType DESERT;
    public static final VillagerType JUNGLE;
    public static final VillagerType PLAINS;
    public static final VillagerType SAVANNA;
    public static final VillagerType SNOW;
    public static final VillagerType SWAMP;
    public static final VillagerType TAIGA;
    private final String name;
    private static final Map<ResourceKey<Biome>, VillagerType> BY_BIOME;
    
    private VillagerType(final String string) {
        this.name = string;
    }
    
    public String toString() {
        return this.name;
    }
    
    private static VillagerType register(final String string) {
        return Registry.<VillagerType, VillagerType>register(Registry.VILLAGER_TYPE, new ResourceLocation(string), new VillagerType(string));
    }
    
    public static VillagerType byBiome(final Optional<ResourceKey<Biome>> optional) {
        return (VillagerType)optional.flatMap(vj -> Optional.ofNullable(VillagerType.BY_BIOME.get(vj))).orElse(VillagerType.PLAINS);
    }
    
    static {
        DESERT = register("desert");
        JUNGLE = register("jungle");
        PLAINS = register("plains");
        SAVANNA = register("savanna");
        SNOW = register("snow");
        SWAMP = register("swamp");
        TAIGA = register("taiga");
        BY_BIOME = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(Biomes.BADLANDS, VillagerType.DESERT);
            hashMap.put(Biomes.BADLANDS_PLATEAU, VillagerType.DESERT);
            hashMap.put(Biomes.DESERT, VillagerType.DESERT);
            hashMap.put(Biomes.DESERT_HILLS, VillagerType.DESERT);
            hashMap.put(Biomes.DESERT_LAKES, VillagerType.DESERT);
            hashMap.put(Biomes.ERODED_BADLANDS, VillagerType.DESERT);
            hashMap.put(Biomes.MODIFIED_BADLANDS_PLATEAU, VillagerType.DESERT);
            hashMap.put(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, VillagerType.DESERT);
            hashMap.put(Biomes.WOODED_BADLANDS_PLATEAU, VillagerType.DESERT);
            hashMap.put(Biomes.BAMBOO_JUNGLE, VillagerType.JUNGLE);
            hashMap.put(Biomes.BAMBOO_JUNGLE_HILLS, VillagerType.JUNGLE);
            hashMap.put(Biomes.JUNGLE, VillagerType.JUNGLE);
            hashMap.put(Biomes.JUNGLE_EDGE, VillagerType.JUNGLE);
            hashMap.put(Biomes.JUNGLE_HILLS, VillagerType.JUNGLE);
            hashMap.put(Biomes.MODIFIED_JUNGLE, VillagerType.JUNGLE);
            hashMap.put(Biomes.MODIFIED_JUNGLE_EDGE, VillagerType.JUNGLE);
            hashMap.put(Biomes.SAVANNA_PLATEAU, VillagerType.SAVANNA);
            hashMap.put(Biomes.SAVANNA, VillagerType.SAVANNA);
            hashMap.put(Biomes.SHATTERED_SAVANNA, VillagerType.SAVANNA);
            hashMap.put(Biomes.SHATTERED_SAVANNA_PLATEAU, VillagerType.SAVANNA);
            hashMap.put(Biomes.DEEP_FROZEN_OCEAN, VillagerType.SNOW);
            hashMap.put(Biomes.FROZEN_OCEAN, VillagerType.SNOW);
            hashMap.put(Biomes.FROZEN_RIVER, VillagerType.SNOW);
            hashMap.put(Biomes.ICE_SPIKES, VillagerType.SNOW);
            hashMap.put(Biomes.SNOWY_BEACH, VillagerType.SNOW);
            hashMap.put(Biomes.SNOWY_MOUNTAINS, VillagerType.SNOW);
            hashMap.put(Biomes.SNOWY_TAIGA, VillagerType.SNOW);
            hashMap.put(Biomes.SNOWY_TAIGA_HILLS, VillagerType.SNOW);
            hashMap.put(Biomes.SNOWY_TAIGA_MOUNTAINS, VillagerType.SNOW);
            hashMap.put(Biomes.SNOWY_TUNDRA, VillagerType.SNOW);
            hashMap.put(Biomes.SWAMP, VillagerType.SWAMP);
            hashMap.put(Biomes.SWAMP_HILLS, VillagerType.SWAMP);
            hashMap.put(Biomes.GIANT_SPRUCE_TAIGA, VillagerType.TAIGA);
            hashMap.put(Biomes.GIANT_SPRUCE_TAIGA_HILLS, VillagerType.TAIGA);
            hashMap.put(Biomes.GIANT_TREE_TAIGA, VillagerType.TAIGA);
            hashMap.put(Biomes.GIANT_TREE_TAIGA_HILLS, VillagerType.TAIGA);
            hashMap.put(Biomes.GRAVELLY_MOUNTAINS, VillagerType.TAIGA);
            hashMap.put(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, VillagerType.TAIGA);
            hashMap.put(Biomes.MOUNTAIN_EDGE, VillagerType.TAIGA);
            hashMap.put(Biomes.MOUNTAINS, VillagerType.TAIGA);
            hashMap.put(Biomes.TAIGA, VillagerType.TAIGA);
            hashMap.put(Biomes.TAIGA_HILLS, VillagerType.TAIGA);
            hashMap.put(Biomes.TAIGA_MOUNTAINS, VillagerType.TAIGA);
            hashMap.put(Biomes.WOODED_MOUNTAINS, VillagerType.TAIGA);
        }));
    }
}
