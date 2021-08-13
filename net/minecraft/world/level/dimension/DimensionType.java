package net.minecraft.world.level.dimension;

import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.FuzzyOffsetConstantColumnBiomeZoomer;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import java.io.File;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.WritableRegistry;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import java.util.Optional;
import net.minecraft.world.level.Level;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.FuzzyOffsetBiomeZoomer;
import net.minecraft.world.level.biome.BiomeZoomer;
import java.util.OptionalLong;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public class DimensionType {
    public static final ResourceLocation OVERWORLD_EFFECTS;
    public static final ResourceLocation NETHER_EFFECTS;
    public static final ResourceLocation END_EFFECTS;
    public static final Codec<DimensionType> DIRECT_CODEC;
    public static final float[] MOON_BRIGHTNESS_PER_PHASE;
    public static final ResourceKey<DimensionType> OVERWORLD_LOCATION;
    public static final ResourceKey<DimensionType> NETHER_LOCATION;
    public static final ResourceKey<DimensionType> END_LOCATION;
    protected static final DimensionType DEFAULT_OVERWORLD;
    protected static final DimensionType DEFAULT_NETHER;
    protected static final DimensionType DEFAULT_END;
    public static final ResourceKey<DimensionType> OVERWORLD_CAVES_LOCATION;
    protected static final DimensionType DEFAULT_OVERWORLD_CAVES;
    public static final Codec<Supplier<DimensionType>> CODEC;
    private final OptionalLong fixedTime;
    private final boolean hasSkylight;
    private final boolean hasCeiling;
    private final boolean ultraWarm;
    private final boolean natural;
    private final double coordinateScale;
    private final boolean createDragonFight;
    private final boolean piglinSafe;
    private final boolean bedWorks;
    private final boolean respawnAnchorWorks;
    private final boolean hasRaids;
    private final int logicalHeight;
    private final BiomeZoomer biomeZoomer;
    private final ResourceLocation infiniburn;
    private final ResourceLocation effectsLocation;
    private final float ambientLight;
    private final transient float[] brightnessRamp;
    
    protected DimensionType(final OptionalLong optionalLong, final boolean boolean2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final double double6, final boolean boolean7, final boolean boolean8, final boolean boolean9, final boolean boolean10, final int integer, final ResourceLocation vk12, final ResourceLocation vk13, final float float14) {
        this(optionalLong, boolean2, boolean3, boolean4, boolean5, double6, false, boolean7, boolean8, boolean9, boolean10, integer, FuzzyOffsetBiomeZoomer.INSTANCE, vk12, vk13, float14);
    }
    
    protected DimensionType(final OptionalLong optionalLong, final boolean boolean2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final double double6, final boolean boolean7, final boolean boolean8, final boolean boolean9, final boolean boolean10, final boolean boolean11, final int integer, final BiomeZoomer bsx, final ResourceLocation vk14, final ResourceLocation vk15, final float float16) {
        this.fixedTime = optionalLong;
        this.hasSkylight = boolean2;
        this.hasCeiling = boolean3;
        this.ultraWarm = boolean4;
        this.natural = boolean5;
        this.coordinateScale = double6;
        this.createDragonFight = boolean7;
        this.piglinSafe = boolean8;
        this.bedWorks = boolean9;
        this.respawnAnchorWorks = boolean10;
        this.hasRaids = boolean11;
        this.logicalHeight = integer;
        this.biomeZoomer = bsx;
        this.infiniburn = vk14;
        this.effectsLocation = vk15;
        this.ambientLight = float16;
        this.brightnessRamp = fillBrightnessRamp(float16);
    }
    
    private static float[] fillBrightnessRamp(final float float1) {
        final float[] arr2 = new float[16];
        for (int integer3 = 0; integer3 <= 15; ++integer3) {
            final float float2 = integer3 / 15.0f;
            final float float3 = float2 / (4.0f - 3.0f * float2);
            arr2[integer3] = Mth.lerp(float1, float3, 1.0f);
        }
        return arr2;
    }
    
    @Deprecated
    public static DataResult<ResourceKey<Level>> parseLegacy(final Dynamic<?> dynamic) {
        final Optional<Number> optional2 = (Optional<Number>)dynamic.asNumber().result();
        if (optional2.isPresent()) {
            final int integer3 = ((Number)optional2.get()).intValue();
            if (integer3 == -1) {
                return (DataResult<ResourceKey<Level>>)DataResult.success(Level.NETHER);
            }
            if (integer3 == 0) {
                return (DataResult<ResourceKey<Level>>)DataResult.success(Level.OVERWORLD);
            }
            if (integer3 == 1) {
                return (DataResult<ResourceKey<Level>>)DataResult.success(Level.END);
            }
        }
        return (DataResult<ResourceKey<Level>>)Level.RESOURCE_KEY_CODEC.parse((Dynamic)dynamic);
    }
    
    public static RegistryAccess.RegistryHolder registerBuiltin(final RegistryAccess.RegistryHolder b) {
        final WritableRegistry<DimensionType> gs2 = b.<DimensionType>registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        gs2.<DimensionType>register(DimensionType.OVERWORLD_LOCATION, DimensionType.DEFAULT_OVERWORLD, Lifecycle.stable());
        gs2.<DimensionType>register(DimensionType.OVERWORLD_CAVES_LOCATION, DimensionType.DEFAULT_OVERWORLD_CAVES, Lifecycle.stable());
        gs2.<DimensionType>register(DimensionType.NETHER_LOCATION, DimensionType.DEFAULT_NETHER, Lifecycle.stable());
        gs2.<DimensionType>register(DimensionType.END_LOCATION, DimensionType.DEFAULT_END, Lifecycle.stable());
        return b;
    }
    
    private static ChunkGenerator defaultEndGenerator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
        return new NoiseBasedChunkGenerator(new TheEndBiomeSource(gm1, long3), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.END)));
    }
    
    private static ChunkGenerator defaultNetherGenerator(final Registry<Biome> gm1, final Registry<NoiseGeneratorSettings> gm2, final long long3) {
        return new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.Preset.NETHER.biomeSource(gm1, long3), long3, (Supplier<NoiseGeneratorSettings>)(() -> gm2.getOrThrow(NoiseGeneratorSettings.NETHER)));
    }
    
    public static MappedRegistry<LevelStem> defaultDimensions(final Registry<DimensionType> gm1, final Registry<Biome> gm2, final Registry<NoiseGeneratorSettings> gm3, final long long4) {
        final MappedRegistry<LevelStem> gi6 = new MappedRegistry<LevelStem>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
        gi6.<LevelStem>register(LevelStem.NETHER, new LevelStem((Supplier<DimensionType>)(() -> gm1.getOrThrow(DimensionType.NETHER_LOCATION)), defaultNetherGenerator(gm2, gm3, long4)), Lifecycle.stable());
        gi6.<LevelStem>register(LevelStem.END, new LevelStem((Supplier<DimensionType>)(() -> gm1.getOrThrow(DimensionType.END_LOCATION)), defaultEndGenerator(gm2, gm3, long4)), Lifecycle.stable());
        return gi6;
    }
    
    public static double getTeleportationScale(final DimensionType cha1, final DimensionType cha2) {
        final double double3 = cha1.coordinateScale();
        final double double4 = cha2.coordinateScale();
        return double3 / double4;
    }
    
    @Deprecated
    public String getFileSuffix() {
        if (this.equalTo(DimensionType.DEFAULT_END)) {
            return "_end";
        }
        return "";
    }
    
    public static File getStorageFolder(final ResourceKey<Level> vj, final File file) {
        if (vj == Level.OVERWORLD) {
            return file;
        }
        if (vj == Level.END) {
            return new File(file, "DIM1");
        }
        if (vj == Level.NETHER) {
            return new File(file, "DIM-1");
        }
        return new File(file, "dimensions/" + vj.location().getNamespace() + "/" + vj.location().getPath());
    }
    
    public boolean hasSkyLight() {
        return this.hasSkylight;
    }
    
    public boolean hasCeiling() {
        return this.hasCeiling;
    }
    
    public boolean ultraWarm() {
        return this.ultraWarm;
    }
    
    public boolean natural() {
        return this.natural;
    }
    
    public double coordinateScale() {
        return this.coordinateScale;
    }
    
    public boolean piglinSafe() {
        return this.piglinSafe;
    }
    
    public boolean bedWorks() {
        return this.bedWorks;
    }
    
    public boolean respawnAnchorWorks() {
        return this.respawnAnchorWorks;
    }
    
    public boolean hasRaids() {
        return this.hasRaids;
    }
    
    public int logicalHeight() {
        return this.logicalHeight;
    }
    
    public boolean createDragonFight() {
        return this.createDragonFight;
    }
    
    public BiomeZoomer getBiomeZoomer() {
        return this.biomeZoomer;
    }
    
    public boolean hasFixedTime() {
        return this.fixedTime.isPresent();
    }
    
    public float timeOfDay(final long long1) {
        final double double4 = Mth.frac(this.fixedTime.orElse(long1) / 24000.0 - 0.25);
        final double double5 = 0.5 - Math.cos(double4 * 3.141592653589793) / 2.0;
        return (float)(double4 * 2.0 + double5) / 3.0f;
    }
    
    public int moonPhase(final long long1) {
        return (int)(long1 / 24000L % 8L + 8L) % 8;
    }
    
    public float brightness(final int integer) {
        return this.brightnessRamp[integer];
    }
    
    public Tag<Block> infiniburn() {
        final Tag<Block> aej2 = BlockTags.getAllTags().getTag(this.infiniburn);
        return (aej2 != null) ? aej2 : BlockTags.INFINIBURN_OVERWORLD;
    }
    
    public ResourceLocation effectsLocation() {
        return this.effectsLocation;
    }
    
    public boolean equalTo(final DimensionType cha) {
        return this == cha || (this.hasSkylight == cha.hasSkylight && this.hasCeiling == cha.hasCeiling && this.ultraWarm == cha.ultraWarm && this.natural == cha.natural && this.coordinateScale == cha.coordinateScale && this.createDragonFight == cha.createDragonFight && this.piglinSafe == cha.piglinSafe && this.bedWorks == cha.bedWorks && this.respawnAnchorWorks == cha.respawnAnchorWorks && this.hasRaids == cha.hasRaids && this.logicalHeight == cha.logicalHeight && Float.compare(cha.ambientLight, this.ambientLight) == 0 && this.fixedTime.equals(cha.fixedTime) && this.biomeZoomer.equals(cha.biomeZoomer) && this.infiniburn.equals(cha.infiniburn) && this.effectsLocation.equals(cha.effectsLocation));
    }
    
    static {
        OVERWORLD_EFFECTS = new ResourceLocation("overworld");
        NETHER_EFFECTS = new ResourceLocation("the_nether");
        END_EFFECTS = new ResourceLocation("the_end");
        DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.LONG.optionalFieldOf("fixed_time").xmap(optional -> (OptionalLong)optional.map(OptionalLong::of).orElseGet(OptionalLong::empty), optionalLong -> optionalLong.isPresent() ? Optional.of(optionalLong.getAsLong()) : Optional.empty()).forGetter(cha -> cha.fixedTime), (App)Codec.BOOL.fieldOf("has_skylight").forGetter(DimensionType::hasSkyLight), (App)Codec.BOOL.fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling), (App)Codec.BOOL.fieldOf("ultrawarm").forGetter(DimensionType::ultraWarm), (App)Codec.BOOL.fieldOf("natural").forGetter(DimensionType::natural), (App)Codec.doubleRange(9.999999747378752E-6, 3.0E7).fieldOf("coordinate_scale").forGetter(DimensionType::coordinateScale), (App)Codec.BOOL.fieldOf("piglin_safe").forGetter(DimensionType::piglinSafe), (App)Codec.BOOL.fieldOf("bed_works").forGetter(DimensionType::bedWorks), (App)Codec.BOOL.fieldOf("respawn_anchor_works").forGetter(DimensionType::respawnAnchorWorks), (App)Codec.BOOL.fieldOf("has_raids").forGetter(DimensionType::hasRaids), (App)Codec.intRange(0, 256).fieldOf("logical_height").forGetter(DimensionType::logicalHeight), (App)ResourceLocation.CODEC.fieldOf("infiniburn").forGetter(cha -> cha.infiniburn), (App)ResourceLocation.CODEC.fieldOf("effects").orElse(DimensionType.OVERWORLD_EFFECTS).forGetter(cha -> cha.effectsLocation), (App)Codec.FLOAT.fieldOf("ambient_light").forGetter(cha -> cha.ambientLight)).apply((Applicative)instance, DimensionType::new));
        MOON_BRIGHTNESS_PER_PHASE = new float[] { 1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f };
        OVERWORLD_LOCATION = ResourceKey.<DimensionType>create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("overworld"));
        NETHER_LOCATION = ResourceKey.<DimensionType>create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("the_nether"));
        END_LOCATION = ResourceKey.<DimensionType>create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("the_end"));
        DEFAULT_OVERWORLD = new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, false, false, true, false, true, 256, FuzzyOffsetConstantColumnBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), DimensionType.OVERWORLD_EFFECTS, 0.0f);
        DEFAULT_NETHER = new DimensionType(OptionalLong.of(18000L), false, true, true, false, 8.0, false, true, false, true, false, 128, FuzzyOffsetBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_NETHER.getName(), DimensionType.NETHER_EFFECTS, 0.1f);
        DEFAULT_END = new DimensionType(OptionalLong.of(6000L), false, false, false, false, 1.0, true, false, false, false, true, 256, FuzzyOffsetBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_END.getName(), DimensionType.END_EFFECTS, 0.0f);
        OVERWORLD_CAVES_LOCATION = ResourceKey.<DimensionType>create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("overworld_caves"));
        DEFAULT_OVERWORLD_CAVES = new DimensionType(OptionalLong.empty(), true, true, false, true, 1.0, false, false, true, false, true, 256, FuzzyOffsetConstantColumnBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), DimensionType.OVERWORLD_EFFECTS, 0.0f);
        CODEC = (Codec)RegistryFileCodec.<DimensionType>create(Registry.DIMENSION_TYPE_REGISTRY, DimensionType.DIRECT_CODEC);
    }
}
