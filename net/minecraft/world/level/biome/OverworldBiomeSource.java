package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.resources.RegistryLookupCodec;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.newbiome.layer.Layers;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import net.minecraft.world.level.newbiome.layer.Layer;
import com.mojang.serialization.Codec;

public class OverworldBiomeSource extends BiomeSource {
    public static final Codec<OverworldBiomeSource> CODEC;
    private final Layer noiseBiomeLayer;
    private static final List<ResourceKey<Biome>> POSSIBLE_BIOMES;
    private final long seed;
    private final boolean legacyBiomeInitLayer;
    private final boolean largeBiomes;
    private final Registry<Biome> biomes;
    
    public OverworldBiomeSource(final long long1, final boolean boolean2, final boolean boolean3, final Registry<Biome> gm) {
        super((Stream<Supplier<Biome>>)OverworldBiomeSource.POSSIBLE_BIOMES.stream().map(vj -> () -> gm.getOrThrow(vj)));
        this.seed = long1;
        this.legacyBiomeInitLayer = boolean2;
        this.largeBiomes = boolean3;
        this.biomes = gm;
        this.noiseBiomeLayer = Layers.getDefaultLayer(long1, boolean2, boolean3 ? 6 : 4, 4);
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return OverworldBiomeSource.CODEC;
    }
    
    @Override
    public BiomeSource withSeed(final long long1) {
        return new OverworldBiomeSource(long1, this.legacyBiomeInitLayer, this.largeBiomes, this.biomes);
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        return this.noiseBiomeLayer.get(this.biomes, integer1, integer3);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.LONG.fieldOf("seed").stable().forGetter(btg -> btg.seed), (App)Codec.BOOL.optionalFieldOf("legacy_biome_init_layer", false, Lifecycle.stable()).forGetter(btg -> btg.legacyBiomeInitLayer), (App)Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter(btg -> btg.largeBiomes), (App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(btg -> btg.biomes)).apply((Applicative)instance, instance.stable(OverworldBiomeSource::new)));
        POSSIBLE_BIOMES = (List)ImmutableList.of(Biomes.OCEAN, Biomes.PLAINS, Biomes.DESERT, Biomes.MOUNTAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.SWAMP, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_MOUNTAINS, (Object[])new ResourceKey[] { Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.BEACH, Biomes.DESERT_HILLS, Biomes.WOODED_HILLS, Biomes.TAIGA_HILLS, Biomes.MOUNTAIN_EDGE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.DEEP_OCEAN, Biomes.STONE_SHORE, Biomes.SNOWY_BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.WOODED_MOUNTAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.BADLANDS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.BADLANDS_PLATEAU, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT_LAKES, Biomes.GRAVELLY_MOUNTAINS, Biomes.FLOWER_FOREST, Biomes.TAIGA_MOUNTAINS, Biomes.SWAMP_HILLS, Biomes.ICE_SPIKES, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.DARK_FOREST_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_BADLANDS_PLATEAU });
    }
}
