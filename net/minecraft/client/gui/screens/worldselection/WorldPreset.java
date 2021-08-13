package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.world.level.biome.OverworldBiomeSource;
import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.CreateBuffetWorldScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.biome.Biomes;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import java.util.Optional;
import java.util.Map;
import java.util.List;

public abstract class WorldPreset {
    public static final WorldPreset NORMAL;
    private static final WorldPreset FLAT;
    private static final WorldPreset LARGE_BIOMES;
    public static final WorldPreset AMPLIFIED;
    private static final WorldPreset SINGLE_BIOME_SURFACE;
    private static final WorldPreset SINGLE_BIOME_CAVES;
    private static final WorldPreset SINGLE_BIOME_FLOATING_ISLANDS;
    private static final WorldPreset DEBUG;
    protected static final List<WorldPreset> PRESETS;
    protected static final Map<Optional<WorldPreset>, PresetEditor> EDITORS;
    private final Component description;
    
    private WorldPreset(final String string) {
        this.description = new TranslatableComponent("generator." + string);
    }
    
    private static WorldGenSettings fromBuffetSettings(final RegistryAccess gn, final WorldGenSettings cht, final WorldPreset dsd, final Biome bss) {
        final BiomeSource bsv5 = new FixedBiomeSource(bss);
        final Registry<DimensionType> gm7 = gn.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        final Registry<NoiseGeneratorSettings> gm8 = gn.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        Supplier<NoiseGeneratorSettings> supplier6;
        if (dsd == WorldPreset.SINGLE_BIOME_CAVES) {
            supplier6 = (Supplier<NoiseGeneratorSettings>)(() -> gm8.getOrThrow(NoiseGeneratorSettings.CAVES));
        }
        else if (dsd == WorldPreset.SINGLE_BIOME_FLOATING_ISLANDS) {
            supplier6 = (Supplier<NoiseGeneratorSettings>)(() -> gm8.getOrThrow(NoiseGeneratorSettings.FLOATING_ISLANDS));
        }
        else {
            supplier6 = (Supplier<NoiseGeneratorSettings>)(() -> gm8.getOrThrow(NoiseGeneratorSettings.OVERWORLD));
        }
        return new WorldGenSettings(cht.seed(), cht.generateFeatures(), cht.generateBonusChest(), WorldGenSettings.withOverworld(gm7, cht.dimensions(), new NoiseBasedChunkGenerator(bsv5, cht.seed(), supplier6)));
    }
    
    private static Biome parseBuffetSettings(final RegistryAccess gn, final WorldGenSettings cht) {
        return (Biome)cht.overworld().getBiomeSource().possibleBiomes().stream().findFirst().orElse(gn.registryOrThrow(Registry.BIOME_REGISTRY).getOrThrow(Biomes.PLAINS));
    }
    
    public static Optional<WorldPreset> of(final WorldGenSettings cht) {
        final ChunkGenerator cfv2 = cht.overworld();
        if (cfv2 instanceof FlatLevelSource) {
            return (Optional<WorldPreset>)Optional.of(WorldPreset.FLAT);
        }
        if (cfv2 instanceof DebugLevelSource) {
            return (Optional<WorldPreset>)Optional.of(WorldPreset.DEBUG);
        }
        return (Optional<WorldPreset>)Optional.empty();
    }
    
    public Component description() {
        return this.description;
    }
    
    public WorldGenSettings create(final RegistryAccess.RegistryHolder b, final long long2, final boolean boolean3, final boolean boolean4) {
        final Registry<Biome> gm7 = b.registryOrThrow(Registry.BIOME_REGISTRY);
        final Registry<DimensionType> gm8 = b.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        final Registry<NoiseGeneratorSettings> gm9 = b.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        return new WorldGenSettings(long2, boolean3, boolean4, WorldGenSettings.withOverworld(gm8, DimensionType.defaultDimensions(gm8, gm7, gm9, long2), this.generator(gm7, gm9, long2)));
    }
    
    protected abstract ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3);
    
    static {
        NORMAL = new WorldPreset("default") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new NoiseBasedChunkGenerator(new OverworldBiomeSource(long3, false, false, gm1), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.OVERWORLD)));
            }
        };
        FLAT = new WorldPreset("flat") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new FlatLevelSource(FlatLevelGeneratorSettings.getDefault(gm1));
            }
        };
        LARGE_BIOMES = new WorldPreset("large_biomes") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new NoiseBasedChunkGenerator(new OverworldBiomeSource(long3, false, true, gm1), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.OVERWORLD)));
            }
        };
        AMPLIFIED = new WorldPreset("amplified") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new NoiseBasedChunkGenerator(new OverworldBiomeSource(long3, false, false, gm1), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.AMPLIFIED)));
            }
        };
        SINGLE_BIOME_SURFACE = new WorldPreset("single_biome_surface") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new NoiseBasedChunkGenerator(new FixedBiomeSource(gm1.getOrThrow(Biomes.PLAINS)), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.OVERWORLD)));
            }
        };
        SINGLE_BIOME_CAVES = new WorldPreset("single_biome_caves") {
            @Override
            public WorldGenSettings create(final RegistryAccess.RegistryHolder b, final long long2, final boolean boolean3, final boolean boolean4) {
                final Registry<Biome> gm7 = b.registryOrThrow(Registry.BIOME_REGISTRY);
                final Registry<DimensionType> gm8 = b.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
                final Registry<NoiseGeneratorSettings> gm9 = b.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
                return new WorldGenSettings(long2, boolean3, boolean4, WorldGenSettings.withOverworld(DimensionType.defaultDimensions(gm8, gm7, gm9, long2), (Supplier<DimensionType>)(() -> gm8.getOrThrow(DimensionType.OVERWORLD_CAVES_LOCATION)), this.generator(gm7, gm9, long2)));
            }
            
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new NoiseBasedChunkGenerator(new FixedBiomeSource(gm1.getOrThrow(Biomes.PLAINS)), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.CAVES)));
            }
        };
        SINGLE_BIOME_FLOATING_ISLANDS = new WorldPreset("single_biome_floating_islands") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new NoiseBasedChunkGenerator(new FixedBiomeSource(gm1.getOrThrow(Biomes.PLAINS)), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.FLOATING_ISLANDS)));
            }
        };
        DEBUG = new WorldPreset("debug_all_block_states") {
            @Override
            protected ChunkGenerator generator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
                return new DebugLevelSource(gm1);
            }
        };
        PRESETS = (List)Lists.newArrayList((Object[])new WorldPreset[] { WorldPreset.NORMAL, WorldPreset.FLAT, WorldPreset.LARGE_BIOMES, WorldPreset.AMPLIFIED, WorldPreset.SINGLE_BIOME_SURFACE, WorldPreset.SINGLE_BIOME_CAVES, WorldPreset.SINGLE_BIOME_FLOATING_ISLANDS, WorldPreset.DEBUG });
        final ChunkGenerator cfv3;
        final Screen screen;
        EDITORS = (Map)ImmutableMap.of(Optional.of((Object)WorldPreset.FLAT), (drx, cht) -> {
            cfv3 = cht.overworld();
            new CreateFlatWorldScreen(drx, (Consumer<FlatLevelGeneratorSettings>)(cpc -> drx.worldGenSettingsComponent.updateSettings(new WorldGenSettings(cht.seed(), cht.generateFeatures(), cht.generateBonusChest(), WorldGenSettings.withOverworld(drx.worldGenSettingsComponent.registryHolder().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY), cht.dimensions(), new FlatLevelSource(cpc))))), (cfv3 instanceof FlatLevelSource) ? ((FlatLevelSource)cfv3).settings() : FlatLevelGeneratorSettings.getDefault(drx.worldGenSettingsComponent.registryHolder().registryOrThrow(Registry.BIOME_REGISTRY)));
            return screen;
        }, Optional.of((Object)WorldPreset.SINGLE_BIOME_SURFACE), (drx, cht) -> new CreateBuffetWorldScreen(drx, drx.worldGenSettingsComponent.registryHolder(), (Consumer<Biome>)(bss -> drx.worldGenSettingsComponent.updateSettings(fromBuffetSettings(drx.worldGenSettingsComponent.registryHolder(), cht, WorldPreset.SINGLE_BIOME_SURFACE, bss))), parseBuffetSettings(drx.worldGenSettingsComponent.registryHolder(), cht)), Optional.of((Object)WorldPreset.SINGLE_BIOME_CAVES), (drx, cht) -> new CreateBuffetWorldScreen(drx, drx.worldGenSettingsComponent.registryHolder(), (Consumer<Biome>)(bss -> drx.worldGenSettingsComponent.updateSettings(fromBuffetSettings(drx.worldGenSettingsComponent.registryHolder(), cht, WorldPreset.SINGLE_BIOME_CAVES, bss))), parseBuffetSettings(drx.worldGenSettingsComponent.registryHolder(), cht)), Optional.of((Object)WorldPreset.SINGLE_BIOME_FLOATING_ISLANDS), (drx, cht) -> new CreateBuffetWorldScreen(drx, drx.worldGenSettingsComponent.registryHolder(), (Consumer<Biome>)(bss -> drx.worldGenSettingsComponent.updateSettings(fromBuffetSettings(drx.worldGenSettingsComponent.registryHolder(), cht, WorldPreset.SINGLE_BIOME_FLOATING_ISLANDS, bss))), parseBuffetSettings(drx.worldGenSettingsComponent.registryHolder(), cht)));
    }
    
    public interface PresetEditor {
        Screen createEditScreen(final CreateWorldScreen drx, final WorldGenSettings cht);
    }
}
