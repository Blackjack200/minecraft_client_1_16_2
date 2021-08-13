package net.minecraft.world.level.levelgen;

import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Registry;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
import java.util.Optional;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import java.util.Objects;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public final class NoiseGeneratorSettings {
    public static final Codec<NoiseGeneratorSettings> DIRECT_CODEC;
    public static final Codec<Supplier<NoiseGeneratorSettings>> CODEC;
    private final StructureSettings structureSettings;
    private final NoiseSettings noiseSettings;
    private final BlockState defaultBlock;
    private final BlockState defaultFluid;
    private final int bedrockRoofPosition;
    private final int bedrockFloorPosition;
    private final int seaLevel;
    private final boolean disableMobGeneration;
    public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD;
    public static final ResourceKey<NoiseGeneratorSettings> AMPLIFIED;
    public static final ResourceKey<NoiseGeneratorSettings> NETHER;
    public static final ResourceKey<NoiseGeneratorSettings> END;
    public static final ResourceKey<NoiseGeneratorSettings> CAVES;
    public static final ResourceKey<NoiseGeneratorSettings> FLOATING_ISLANDS;
    private static final NoiseGeneratorSettings BUILTIN_OVERWORLD;
    
    private NoiseGeneratorSettings(final StructureSettings chs, final NoiseSettings cho, final BlockState cee3, final BlockState cee4, final int integer5, final int integer6, final int integer7, final boolean boolean8) {
        this.structureSettings = chs;
        this.noiseSettings = cho;
        this.defaultBlock = cee3;
        this.defaultFluid = cee4;
        this.bedrockRoofPosition = integer5;
        this.bedrockFloorPosition = integer6;
        this.seaLevel = integer7;
        this.disableMobGeneration = boolean8;
    }
    
    public StructureSettings structureSettings() {
        return this.structureSettings;
    }
    
    public NoiseSettings noiseSettings() {
        return this.noiseSettings;
    }
    
    public BlockState getDefaultBlock() {
        return this.defaultBlock;
    }
    
    public BlockState getDefaultFluid() {
        return this.defaultFluid;
    }
    
    public int getBedrockRoofPosition() {
        return this.bedrockRoofPosition;
    }
    
    public int getBedrockFloorPosition() {
        return this.bedrockFloorPosition;
    }
    
    public int seaLevel() {
        return this.seaLevel;
    }
    
    @Deprecated
    protected boolean disableMobGeneration() {
        return this.disableMobGeneration;
    }
    
    public boolean stable(final ResourceKey<NoiseGeneratorSettings> vj) {
        return Objects.equals(this, BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(vj));
    }
    
    private static NoiseGeneratorSettings register(final ResourceKey<NoiseGeneratorSettings> vj, final NoiseGeneratorSettings chm) {
        BuiltinRegistries.<NoiseGeneratorSettings, NoiseGeneratorSettings>register(BuiltinRegistries.NOISE_GENERATOR_SETTINGS, vj.location(), chm);
        return chm;
    }
    
    public static NoiseGeneratorSettings bootstrap() {
        return NoiseGeneratorSettings.BUILTIN_OVERWORLD;
    }
    
    private static NoiseGeneratorSettings end(final StructureSettings chs, final BlockState cee2, final BlockState cee3, final ResourceLocation vk, final boolean boolean5, final boolean boolean6) {
        return new NoiseGeneratorSettings(chs, new NoiseSettings(128, new NoiseSamplingSettings(2.0, 1.0, 80.0, 160.0), new NoiseSlideSettings(-3000, 64, -46), new NoiseSlideSettings(-30, 7, 1), 2, 1, 0.0, 0.0, true, false, boolean6, false), cee2, cee3, -10, -10, 0, boolean5);
    }
    
    private static NoiseGeneratorSettings nether(final StructureSettings chs, final BlockState cee2, final BlockState cee3, final ResourceLocation vk) {
        final Map<StructureFeature<?>, StructureFeatureConfiguration> map5 = (Map<StructureFeature<?>, StructureFeatureConfiguration>)Maps.newHashMap((Map)StructureSettings.DEFAULTS);
        map5.put(StructureFeature.RUINED_PORTAL, new StructureFeatureConfiguration(25, 10, 34222645));
        return new NoiseGeneratorSettings(new StructureSettings((Optional<StrongholdConfiguration>)Optional.ofNullable(chs.stronghold()), map5), new NoiseSettings(128, new NoiseSamplingSettings(1.0, 3.0, 80.0, 60.0), new NoiseSlideSettings(120, 3, 0), new NoiseSlideSettings(320, 4, -1), 1, 2, 0.0, 0.019921875, false, false, false, false), cee2, cee3, 0, 0, 32, false);
    }
    
    private static NoiseGeneratorSettings overworld(final StructureSettings chs, final boolean boolean2, final ResourceLocation vk) {
        final double double4 = 0.9999999814507745;
        return new NoiseGeneratorSettings(chs, new NoiseSettings(256, new NoiseSamplingSettings(0.9999999814507745, 0.9999999814507745, 80.0, 160.0), new NoiseSlideSettings(-10, 3, 0), new NoiseSlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true, true, false, boolean2), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), -10, 0, 63, false);
    }
    
    static {
        DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group((App)StructureSettings.CODEC.fieldOf("structures").forGetter(NoiseGeneratorSettings::structureSettings), (App)NoiseSettings.CODEC.fieldOf("noise").forGetter(NoiseGeneratorSettings::noiseSettings), (App)BlockState.CODEC.fieldOf("default_block").forGetter(NoiseGeneratorSettings::getDefaultBlock), (App)BlockState.CODEC.fieldOf("default_fluid").forGetter(NoiseGeneratorSettings::getDefaultFluid), (App)Codec.intRange(-20, 276).fieldOf("bedrock_roof_position").forGetter(NoiseGeneratorSettings::getBedrockRoofPosition), (App)Codec.intRange(-20, 276).fieldOf("bedrock_floor_position").forGetter(NoiseGeneratorSettings::getBedrockFloorPosition), (App)Codec.intRange(0, 255).fieldOf("sea_level").forGetter(NoiseGeneratorSettings::seaLevel), (App)Codec.BOOL.fieldOf("disable_mob_generation").forGetter(NoiseGeneratorSettings::disableMobGeneration)).apply((Applicative)instance, NoiseGeneratorSettings::new));
        CODEC = (Codec)RegistryFileCodec.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, NoiseGeneratorSettings.DIRECT_CODEC);
        OVERWORLD = ResourceKey.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("overworld"));
        AMPLIFIED = ResourceKey.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("amplified"));
        NETHER = ResourceKey.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("nether"));
        END = ResourceKey.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("end"));
        CAVES = ResourceKey.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("caves"));
        FLOATING_ISLANDS = ResourceKey.<NoiseGeneratorSettings>create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("floating_islands"));
        BUILTIN_OVERWORLD = register(NoiseGeneratorSettings.OVERWORLD, overworld(new StructureSettings(true), false, NoiseGeneratorSettings.OVERWORLD.location()));
        register(NoiseGeneratorSettings.AMPLIFIED, overworld(new StructureSettings(true), true, NoiseGeneratorSettings.AMPLIFIED.location()));
        register(NoiseGeneratorSettings.NETHER, nether(new StructureSettings(false), Blocks.NETHERRACK.defaultBlockState(), Blocks.LAVA.defaultBlockState(), NoiseGeneratorSettings.NETHER.location()));
        register(NoiseGeneratorSettings.END, end(new StructureSettings(false), Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), NoiseGeneratorSettings.END.location(), true, true));
        register(NoiseGeneratorSettings.CAVES, nether(new StructureSettings(true), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), NoiseGeneratorSettings.CAVES.location()));
        register(NoiseGeneratorSettings.FLOATING_ISLANDS, end(new StructureSettings(true), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), NoiseGeneratorSettings.FLOATING_ISLANDS.location(), false, false));
    }
}
