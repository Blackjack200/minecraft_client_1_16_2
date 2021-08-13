package net.minecraft.world.level.levelgen;

import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import java.util.OptionalLong;
import com.google.gson.JsonElement;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.util.Objects;
import com.google.common.base.MoreObjects;
import java.util.Properties;
import net.minecraft.world.level.Level;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.OverworldBiomeSource;
import java.util.Random;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.DataResult;
import java.util.Optional;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.core.MappedRegistry;
import org.apache.logging.log4j.Logger;
import com.mojang.serialization.Codec;

public class WorldGenSettings {
    public static final Codec<WorldGenSettings> CODEC;
    private static final Logger LOGGER;
    private final long seed;
    private final boolean generateFeatures;
    private final boolean generateBonusChest;
    private final MappedRegistry<LevelStem> dimensions;
    private final Optional<String> legacyCustomOptions;
    
    private DataResult<WorldGenSettings> guardExperimental() {
        final LevelStem chb2 = this.dimensions.get(LevelStem.OVERWORLD);
        if (chb2 == null) {
            return (DataResult<WorldGenSettings>)DataResult.error("Overworld settings missing");
        }
        if (this.stable()) {
            return (DataResult<WorldGenSettings>)DataResult.success(this, Lifecycle.stable());
        }
        return (DataResult<WorldGenSettings>)DataResult.success(this);
    }
    
    private boolean stable() {
        return LevelStem.stable(this.seed, this.dimensions);
    }
    
    public WorldGenSettings(final long long1, final boolean boolean2, final boolean boolean3, final MappedRegistry<LevelStem> gi) {
        this(long1, boolean2, boolean3, gi, (Optional<String>)Optional.empty());
        final LevelStem chb7 = gi.get(LevelStem.OVERWORLD);
        if (chb7 == null) {
            throw new IllegalStateException("Overworld settings missing");
        }
    }
    
    private WorldGenSettings(final long long1, final boolean boolean2, final boolean boolean3, final MappedRegistry<LevelStem> gi, final Optional<String> optional) {
        this.seed = long1;
        this.generateFeatures = boolean2;
        this.generateBonusChest = boolean3;
        this.dimensions = gi;
        this.legacyCustomOptions = optional;
    }
    
    public static WorldGenSettings demoSettings(final RegistryAccess gn) {
        final Registry<Biome> gm2 = gn.registryOrThrow(Registry.BIOME_REGISTRY);
        final int integer3 = "North Carolina".hashCode();
        final Registry<DimensionType> gm3 = gn.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        final Registry<NoiseGeneratorSettings> gm4 = gn.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        return new WorldGenSettings(integer3, true, true, withOverworld(gm3, DimensionType.defaultDimensions(gm3, gm2, gm4, integer3), makeDefaultOverworld(gm2, gm4, integer3)));
    }
    
    public static WorldGenSettings makeDefault(final Registry<DimensionType> gm1, final Registry<Biome> gm2, final Registry<NoiseGeneratorSettings> gm3) {
        final long long4 = new Random().nextLong();
        return new WorldGenSettings(long4, true, false, withOverworld(gm1, DimensionType.defaultDimensions(gm1, gm2, gm3, long4), makeDefaultOverworld(gm2, gm3, long4)));
    }
    
    public static NoiseBasedChunkGenerator makeDefaultOverworld(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
        return new NoiseBasedChunkGenerator(new OverworldBiomeSource(long3, false, false, gm1), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.OVERWORLD)));
    }
    
    public long seed() {
        return this.seed;
    }
    
    public boolean generateFeatures() {
        return this.generateFeatures;
    }
    
    public boolean generateBonusChest() {
        return this.generateBonusChest;
    }
    
    public static MappedRegistry<LevelStem> withOverworld(final Registry<DimensionType> gm, final MappedRegistry<LevelStem> gi, final ChunkGenerator cfv) {
        final LevelStem chb4 = gi.get(LevelStem.OVERWORLD);
        final Supplier<DimensionType> supplier5 = (Supplier<DimensionType>)(() -> (chb4 == null) ? gm.getOrThrow(DimensionType.OVERWORLD_LOCATION) : chb4.type());
        return withOverworld(gi, supplier5, cfv);
    }
    
    public static MappedRegistry<LevelStem> withOverworld(final MappedRegistry<LevelStem> gi, final Supplier<DimensionType> supplier, final ChunkGenerator cfv) {
        final MappedRegistry<LevelStem> gi2 = new MappedRegistry<LevelStem>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
        gi2.<LevelStem>register(LevelStem.OVERWORLD, new LevelStem(supplier, cfv), Lifecycle.stable());
        for (final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry6 : gi.entrySet()) {
            final ResourceKey<LevelStem> vj7 = (ResourceKey<LevelStem>)entry6.getKey();
            if (vj7 == LevelStem.OVERWORLD) {
                continue;
            }
            gi2.register(vj7, entry6.getValue(), gi.lifecycle((LevelStem)entry6.getValue()));
        }
        return gi2;
    }
    
    public MappedRegistry<LevelStem> dimensions() {
        return this.dimensions;
    }
    
    public ChunkGenerator overworld() {
        final LevelStem chb2 = this.dimensions.get(LevelStem.OVERWORLD);
        if (chb2 == null) {
            throw new IllegalStateException("Overworld settings missing");
        }
        return chb2.generator();
    }
    
    public ImmutableSet<ResourceKey<Level>> levels() {
        return (ImmutableSet<ResourceKey<Level>>)this.dimensions().entrySet().stream().map(entry -> ResourceKey.create(Registry.DIMENSION_REGISTRY, ((ResourceKey)entry.getKey()).location())).collect(ImmutableSet.toImmutableSet());
    }
    
    public boolean isDebug() {
        return this.overworld() instanceof DebugLevelSource;
    }
    
    public boolean isFlatWorld() {
        return this.overworld() instanceof FlatLevelSource;
    }
    
    public boolean isOldCustomizedWorld() {
        return this.legacyCustomOptions.isPresent();
    }
    
    public WorldGenSettings withBonusChest() {
        return new WorldGenSettings(this.seed, this.generateFeatures, true, this.dimensions, this.legacyCustomOptions);
    }
    
    public WorldGenSettings withFeaturesToggled() {
        return new WorldGenSettings(this.seed, !this.generateFeatures, this.generateBonusChest, this.dimensions);
    }
    
    public WorldGenSettings withBonusChestToggled() {
        return new WorldGenSettings(this.seed, this.generateFeatures, !this.generateBonusChest, this.dimensions);
    }
    
    public static WorldGenSettings create(final RegistryAccess gn, final Properties properties) {
        final String string3 = (String)MoreObjects.firstNonNull(properties.get("generator-settings"), "");
        properties.put("generator-settings", string3);
        final String string4 = (String)MoreObjects.firstNonNull(properties.get("level-seed"), "");
        properties.put("level-seed", string4);
        final String string5 = (String)properties.get("generate-structures");
        final boolean boolean6 = string5 == null || Boolean.parseBoolean(string5);
        properties.put("generate-structures", Objects.toString((Object)boolean6));
        final String string6 = (String)properties.get("level-type");
        final String string7 = (String)Optional.ofNullable(string6).map(string -> string.toLowerCase(Locale.ROOT)).orElse("default");
        properties.put("level-type", string7);
        long long9 = new Random().nextLong();
        if (!string4.isEmpty()) {
            try {
                final long long10 = Long.parseLong(string4);
                if (long10 != 0L) {
                    long9 = long10;
                }
            }
            catch (NumberFormatException numberFormatException11) {
                long9 = string4.hashCode();
            }
        }
        final Registry<DimensionType> gm11 = gn.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        final Registry<Biome> gm12 = gn.registryOrThrow(Registry.BIOME_REGISTRY);
        final Registry<NoiseGeneratorSettings> gm13 = gn.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        final MappedRegistry<LevelStem> gi14 = DimensionType.defaultDimensions(gm11, gm12, gm13, long9);
        final String s = string7;
        switch (s) {
            case "flat": {
                final JsonObject jsonObject17 = string3.isEmpty() ? new JsonObject() : GsonHelper.parse(string3);
                final Dynamic<JsonElement> dynamic18 = (Dynamic<JsonElement>)new Dynamic((DynamicOps)JsonOps.INSTANCE, jsonObject17);
                return new WorldGenSettings(long9, boolean6, false, withOverworld(gm11, gi14, new FlatLevelSource((FlatLevelGeneratorSettings)FlatLevelGeneratorSettings.CODEC.parse((Dynamic)dynamic18).resultOrPartial(WorldGenSettings.LOGGER::error).orElseGet(() -> FlatLevelGeneratorSettings.getDefault(gm12)))));
            }
            case "debug_all_block_states": {
                return new WorldGenSettings(long9, boolean6, false, withOverworld(gm11, gi14, new DebugLevelSource(gm12)));
            }
            case "amplified": {
                return new WorldGenSettings(long9, boolean6, false, withOverworld(gm11, gi14, new NoiseBasedChunkGenerator(new OverworldBiomeSource(long9, false, false, gm12), long9, (Supplier<NoiseGeneratorSettings>)(() -> gm13.getOrThrow(NoiseGeneratorSettings.AMPLIFIED)))));
            }
            case "largebiomes": {
                return new WorldGenSettings(long9, boolean6, false, withOverworld(gm11, gi14, new NoiseBasedChunkGenerator(new OverworldBiomeSource(long9, false, true, gm12), long9, (Supplier<NoiseGeneratorSettings>)(() -> gm13.getOrThrow(NoiseGeneratorSettings.OVERWORLD)))));
            }
            default: {
                return new WorldGenSettings(long9, boolean6, false, withOverworld(gm11, gi14, makeDefaultOverworld(gm12, gm13, long9)));
            }
        }
    }
    
    public WorldGenSettings withSeed(final boolean boolean1, final OptionalLong optionalLong) {
        final long long5 = optionalLong.orElse(this.seed);
        MappedRegistry<LevelStem> gi7;
        if (optionalLong.isPresent()) {
            gi7 = new MappedRegistry<LevelStem>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
            final long long6 = optionalLong.getAsLong();
            for (final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry11 : this.dimensions.entrySet()) {
                final ResourceKey<LevelStem> vj12 = (ResourceKey<LevelStem>)entry11.getKey();
                gi7.<LevelStem>register(vj12, new LevelStem(((LevelStem)entry11.getValue()).typeSupplier(), ((LevelStem)entry11.getValue()).generator().withSeed(long6)), this.dimensions.lifecycle((LevelStem)entry11.getValue()));
            }
        }
        else {
            gi7 = this.dimensions;
        }
        WorldGenSettings cht4;
        if (this.isDebug()) {
            cht4 = new WorldGenSettings(long5, false, false, gi7);
        }
        else {
            cht4 = new WorldGenSettings(long5, this.generateFeatures(), this.generateBonusChest() && !boolean1, gi7);
        }
        return cht4;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.LONG.fieldOf("seed").stable().forGetter(WorldGenSettings::seed), (App)Codec.BOOL.fieldOf("generate_features").orElse(true).stable().forGetter(WorldGenSettings::generateFeatures), (App)Codec.BOOL.fieldOf("bonus_chest").orElse(false).stable().forGetter(WorldGenSettings::generateBonusChest), (App)MappedRegistry.<LevelStem>dataPackCodec(Registry.LEVEL_STEM_REGISTRY, Lifecycle.stable(), LevelStem.CODEC).xmap(LevelStem::sortMap, Function.identity()).fieldOf("dimensions").forGetter(WorldGenSettings::dimensions), (App)Codec.STRING.optionalFieldOf("legacy_custom_options").stable().forGetter(cht -> cht.legacyCustomOptions)).apply((Applicative)instance, instance.stable(WorldGenSettings::new))).comapFlatMap(WorldGenSettings::guardExperimental, Function.identity());
        LOGGER = LogManager.getLogger();
    }
}
