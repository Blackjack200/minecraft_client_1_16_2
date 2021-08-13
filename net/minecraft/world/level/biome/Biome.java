package net.minecraft.world.level.biome;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Function;
import net.minecraft.util.StringRepresentable;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import java.util.Optional;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.util.Mth;
import java.util.Iterator;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.core.SectionPos;
import java.util.Collections;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.Map;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.Logger;

public final class Biome {
    public static final Logger LOGGER;
    public static final Codec<Biome> DIRECT_CODEC;
    public static final Codec<Biome> NETWORK_CODEC;
    public static final Codec<Supplier<Biome>> CODEC;
    public static final Codec<List<Supplier<Biome>>> LIST_CODEC;
    private final Map<Integer, List<StructureFeature<?>>> structuresByStep;
    private static final PerlinSimplexNoise TEMPERATURE_NOISE;
    private static final PerlinSimplexNoise FROZEN_TEMPERATURE_NOISE;
    public static final PerlinSimplexNoise BIOME_INFO_NOISE;
    private final ClimateSettings climateSettings;
    private final BiomeGenerationSettings generationSettings;
    private final MobSpawnSettings mobSettings;
    private final float depth;
    private final float scale;
    private final BiomeCategory biomeCategory;
    private final BiomeSpecialEffects specialEffects;
    private final ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache;
    
    private Biome(final ClimateSettings d, final BiomeCategory b, final float float3, final float float4, final BiomeSpecialEffects bsw, final BiomeGenerationSettings bst, final MobSpawnSettings btd) {
        this.structuresByStep = (Map<Integer, List<StructureFeature<?>>>)Registry.STRUCTURE_FEATURE.stream().collect(Collectors.groupingBy(ckx -> ckx.step().ordinal()));
        this.temperatureCache = (ThreadLocal<Long2FloatLinkedOpenHashMap>)ThreadLocal.withInitial(() -> Util.<Long2FloatLinkedOpenHashMap>make((java.util.function.Supplier<Long2FloatLinkedOpenHashMap>)(() -> {
            final Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap2 = new Long2FloatLinkedOpenHashMap(1024, 0.25f) {
                protected void rehash(final int integer) {
                }
            };
            long2FloatLinkedOpenHashMap2.defaultReturnValue(Float.NaN);
            return long2FloatLinkedOpenHashMap2;
        })));
        this.climateSettings = d;
        this.generationSettings = bst;
        this.mobSettings = btd;
        this.biomeCategory = b;
        this.depth = float3;
        this.scale = float4;
        this.specialEffects = bsw;
    }
    
    public int getSkyColor() {
        return this.specialEffects.getSkyColor();
    }
    
    public MobSpawnSettings getMobSettings() {
        return this.mobSettings;
    }
    
    public Precipitation getPrecipitation() {
        return this.climateSettings.precipitation;
    }
    
    public boolean isHumid() {
        return this.getDownfall() > 0.85f;
    }
    
    private float getHeightAdjustedTemperature(final BlockPos fx) {
        final float float3 = this.climateSettings.temperatureModifier.modifyTemperature(fx, this.getBaseTemperature());
        if (fx.getY() > 64) {
            final float float4 = (float)(Biome.TEMPERATURE_NOISE.getValue(fx.getX() / 8.0f, fx.getZ() / 8.0f, false) * 4.0);
            return float3 - (float4 + fx.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return float3;
    }
    
    public final float getTemperature(final BlockPos fx) {
        final long long3 = fx.asLong();
        final Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap5 = (Long2FloatLinkedOpenHashMap)this.temperatureCache.get();
        final float float6 = long2FloatLinkedOpenHashMap5.get(long3);
        if (!Float.isNaN(float6)) {
            return float6;
        }
        final float float7 = this.getHeightAdjustedTemperature(fx);
        if (long2FloatLinkedOpenHashMap5.size() == 1024) {
            long2FloatLinkedOpenHashMap5.removeFirstFloat();
        }
        long2FloatLinkedOpenHashMap5.put(long3, float7);
        return float7;
    }
    
    public boolean shouldFreeze(final LevelReader brw, final BlockPos fx) {
        return this.shouldFreeze(brw, fx, true);
    }
    
    public boolean shouldFreeze(final LevelReader brw, final BlockPos fx, final boolean boolean3) {
        if (this.getTemperature(fx) >= 0.15f) {
            return false;
        }
        if (fx.getY() >= 0 && fx.getY() < 256 && brw.getBrightness(LightLayer.BLOCK, fx) < 10) {
            final BlockState cee5 = brw.getBlockState(fx);
            final FluidState cuu6 = brw.getFluidState(fx);
            if (cuu6.getType() == Fluids.WATER && cee5.getBlock() instanceof LiquidBlock) {
                if (!boolean3) {
                    return true;
                }
                final boolean boolean4 = brw.isWaterAt(fx.west()) && brw.isWaterAt(fx.east()) && brw.isWaterAt(fx.north()) && brw.isWaterAt(fx.south());
                if (!boolean4) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean shouldSnow(final LevelReader brw, final BlockPos fx) {
        if (this.getTemperature(fx) >= 0.15f) {
            return false;
        }
        if (fx.getY() >= 0 && fx.getY() < 256 && brw.getBrightness(LightLayer.BLOCK, fx) < 10) {
            final BlockState cee4 = brw.getBlockState(fx);
            if (cee4.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(brw, fx)) {
                return true;
            }
        }
        return false;
    }
    
    public BiomeGenerationSettings getGenerationSettings() {
        return this.generationSettings;
    }
    
    public void generate(final StructureFeatureManager bsk, final ChunkGenerator cfv, final WorldGenRegion aam, final long long4, final WorldgenRandom chu, final BlockPos fx) {
        final List<List<Supplier<ConfiguredFeature<?, ?>>>> list9 = this.generationSettings.features();
        for (int integer10 = GenerationStep.Decoration.values().length, integer11 = 0; integer11 < integer10; ++integer11) {
            int integer12 = 0;
            if (bsk.shouldGenerateFeatures()) {
                final List<StructureFeature<?>> list10 = (List<StructureFeature<?>>)this.structuresByStep.getOrDefault(integer11, Collections.emptyList());
                for (final StructureFeature<?> ckx15 : list10) {
                    chu.setFeatureSeed(long4, integer12, integer11);
                    final int integer13 = fx.getX() >> 4;
                    final int integer14 = fx.getZ() >> 4;
                    final int integer15 = integer13 << 4;
                    final int integer16 = integer14 << 4;
                    try {
                        bsk.startsForFeature(SectionPos.of(fx), ckx15).forEach(crs -> crs.placeInChunk(aam, bsk, cfv, chu, new BoundingBox(integer15, integer16, integer15 + 15, integer16 + 15), new ChunkPos(integer13, integer14)));
                    }
                    catch (Exception exception20) {
                        final CrashReport l21 = CrashReport.forThrowable((Throwable)exception20, "Feature placement");
                        l21.addCategory("Feature").setDetail("Id", Registry.STRUCTURE_FEATURE.getKey(ckx15)).setDetail("Description", (CrashReportDetail<String>)(() -> ckx15.toString()));
                        throw new ReportedException(l21);
                    }
                    ++integer12;
                }
            }
            if (list9.size() > integer11) {
                for (final Supplier<ConfiguredFeature<?, ?>> supplier14 : (List)list9.get(integer11)) {
                    final ConfiguredFeature<?, ?> cis15 = supplier14.get();
                    chu.setFeatureSeed(long4, integer12, integer11);
                    try {
                        cis15.place(aam, cfv, chu, fx);
                    }
                    catch (Exception exception21) {
                        final CrashReport l22 = CrashReport.forThrowable((Throwable)exception21, "Feature placement");
                        l22.addCategory("Feature").setDetail("Id", Registry.FEATURE.getKey(cis15.feature)).setDetail("Config", cis15.config).setDetail("Description", (CrashReportDetail<String>)(() -> cis15.feature.toString()));
                        throw new ReportedException(l22);
                    }
                    ++integer12;
                }
            }
        }
    }
    
    public int getFogColor() {
        return this.specialEffects.getFogColor();
    }
    
    public int getGrassColor(final double double1, final double double2) {
        final int integer6 = (int)this.specialEffects.getGrassColorOverride().orElseGet(this::getGrassColorFromTexture);
        return this.specialEffects.getGrassColorModifier().modifyColor(double1, double2, integer6);
    }
    
    private int getGrassColorFromTexture() {
        final double double2 = Mth.clamp(this.climateSettings.temperature, 0.0f, 1.0f);
        final double double3 = Mth.clamp(this.climateSettings.downfall, 0.0f, 1.0f);
        return GrassColor.get(double2, double3);
    }
    
    public int getFoliageColor() {
        return (int)this.specialEffects.getFoliageColorOverride().orElseGet(this::getFoliageColorFromTexture);
    }
    
    private int getFoliageColorFromTexture() {
        final double double2 = Mth.clamp(this.climateSettings.temperature, 0.0f, 1.0f);
        final double double3 = Mth.clamp(this.climateSettings.downfall, 0.0f, 1.0f);
        return FoliageColor.get(double2, double3);
    }
    
    public void buildSurfaceAt(final Random random, final ChunkAccess cft, final int integer3, final int integer4, final int integer5, final double double6, final BlockState cee7, final BlockState cee8, final int integer9, final long long10) {
        final ConfiguredSurfaceBuilder<?> ctd14 = this.generationSettings.getSurfaceBuilder().get();
        ctd14.initNoise(long10);
        ctd14.apply(random, cft, this, integer3, integer4, integer5, double6, cee7, cee8, integer9, long10);
    }
    
    public final float getDepth() {
        return this.depth;
    }
    
    public final float getDownfall() {
        return this.climateSettings.downfall;
    }
    
    public final float getScale() {
        return this.scale;
    }
    
    public final float getBaseTemperature() {
        return this.climateSettings.temperature;
    }
    
    public BiomeSpecialEffects getSpecialEffects() {
        return this.specialEffects;
    }
    
    public final int getWaterColor() {
        return this.specialEffects.getWaterColor();
    }
    
    public final int getWaterFogColor() {
        return this.specialEffects.getWaterFogColor();
    }
    
    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.specialEffects.getAmbientParticleSettings();
    }
    
    public Optional<SoundEvent> getAmbientLoop() {
        return this.specialEffects.getAmbientLoopSoundEvent();
    }
    
    public Optional<AmbientMoodSettings> getAmbientMood() {
        return this.specialEffects.getAmbientMoodSettings();
    }
    
    public Optional<AmbientAdditionsSettings> getAmbientAdditions() {
        return this.specialEffects.getAmbientAdditionsSettings();
    }
    
    public Optional<Music> getBackgroundMusic() {
        return this.specialEffects.getBackgroundMusic();
    }
    
    public final BiomeCategory getBiomeCategory() {
        return this.biomeCategory;
    }
    
    public String toString() {
        final ResourceLocation vk2 = BuiltinRegistries.BIOME.getKey(this);
        return (vk2 == null) ? super.toString() : vk2.toString();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ClimateSettings.CODEC.forGetter(bss -> bss.climateSettings), (App)BiomeCategory.CODEC.fieldOf("category").forGetter(bss -> bss.biomeCategory), (App)Codec.FLOAT.fieldOf("depth").forGetter(bss -> bss.depth), (App)Codec.FLOAT.fieldOf("scale").forGetter(bss -> bss.scale), (App)BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(bss -> bss.specialEffects), (App)BiomeGenerationSettings.CODEC.forGetter(bss -> bss.generationSettings), (App)MobSpawnSettings.CODEC.forGetter(bss -> bss.mobSettings)).apply((Applicative)instance, Biome::new));
        NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ClimateSettings.CODEC.forGetter(bss -> bss.climateSettings), (App)BiomeCategory.CODEC.fieldOf("category").forGetter(bss -> bss.biomeCategory), (App)Codec.FLOAT.fieldOf("depth").forGetter(bss -> bss.depth), (App)Codec.FLOAT.fieldOf("scale").forGetter(bss -> bss.scale), (App)BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(bss -> bss.specialEffects)).apply((Applicative)instance, (d, b, float3, float4, bsw) -> new Biome(d, b, float3, float4, bsw, BiomeGenerationSettings.EMPTY, MobSpawnSettings.EMPTY)));
        CODEC = (Codec)RegistryFileCodec.<Biome>create(Registry.BIOME_REGISTRY, Biome.DIRECT_CODEC);
        LIST_CODEC = RegistryFileCodec.<Biome>homogeneousList(Registry.BIOME_REGISTRY, Biome.DIRECT_CODEC);
        TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(1234L), (List<Integer>)ImmutableList.of(0));
        FROZEN_TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(3456L), (List<Integer>)ImmutableList.of((-2), (-1), 0));
        BIOME_INFO_NOISE = new PerlinSimplexNoise(new WorldgenRandom(2345L), (List<Integer>)ImmutableList.of(0));
    }
    
    public enum BiomeCategory implements StringRepresentable {
        public static final BiomeCategory NONE;
        public static final BiomeCategory TAIGA;
        public static final BiomeCategory EXTREME_HILLS;
        public static final BiomeCategory JUNGLE;
        public static final BiomeCategory MESA;
        public static final BiomeCategory PLAINS;
        public static final BiomeCategory SAVANNA;
        public static final BiomeCategory ICY;
        public static final BiomeCategory THEEND;
        public static final BiomeCategory BEACH;
        public static final BiomeCategory FOREST;
        public static final BiomeCategory OCEAN;
        public static final BiomeCategory DESERT;
        public static final BiomeCategory RIVER;
        public static final BiomeCategory SWAMP;
        public static final BiomeCategory MUSHROOM;
        public static final BiomeCategory NETHER;
        public static final Codec<BiomeCategory> CODEC;
        private static final Map<String, BiomeCategory> BY_NAME;
        private final String name;
        
        private BiomeCategory(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static BiomeCategory byName(final String string) {
            return (BiomeCategory)BiomeCategory.BY_NAME.get(string);
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: ldc             "NONE"
            //     6: iconst_0       
            //     7: ldc             "none"
            //     9: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //    12: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.NONE:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    15: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    18: dup            
            //    19: ldc             "TAIGA"
            //    21: iconst_1       
            //    22: ldc             "taiga"
            //    24: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //    27: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.TAIGA:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    30: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    33: dup            
            //    34: ldc             "EXTREME_HILLS"
            //    36: iconst_2       
            //    37: ldc             "extreme_hills"
            //    39: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //    42: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.EXTREME_HILLS:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    45: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    48: dup            
            //    49: ldc             "JUNGLE"
            //    51: iconst_3       
            //    52: ldc             "jungle"
            //    54: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //    57: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.JUNGLE:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    60: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    63: dup            
            //    64: ldc             "MESA"
            //    66: iconst_4       
            //    67: ldc             "mesa"
            //    69: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //    72: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.MESA:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    75: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    78: dup            
            //    79: ldc             "PLAINS"
            //    81: iconst_5       
            //    82: ldc             "plains"
            //    84: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //    87: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.PLAINS:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    90: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //    93: dup            
            //    94: ldc             "SAVANNA"
            //    96: bipush          6
            //    98: ldc             "savanna"
            //   100: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   103: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.SAVANNA:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   106: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   109: dup            
            //   110: ldc             "ICY"
            //   112: bipush          7
            //   114: ldc             "icy"
            //   116: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   119: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.ICY:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   122: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   125: dup            
            //   126: ldc             "THEEND"
            //   128: bipush          8
            //   130: ldc             "the_end"
            //   132: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   135: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.THEEND:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   138: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   141: dup            
            //   142: ldc             "BEACH"
            //   144: bipush          9
            //   146: ldc             "beach"
            //   148: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   151: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.BEACH:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   154: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   157: dup            
            //   158: ldc             "FOREST"
            //   160: bipush          10
            //   162: ldc             "forest"
            //   164: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   167: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.FOREST:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   170: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   173: dup            
            //   174: ldc             "OCEAN"
            //   176: bipush          11
            //   178: ldc             "ocean"
            //   180: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   183: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.OCEAN:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   186: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   189: dup            
            //   190: ldc             "DESERT"
            //   192: bipush          12
            //   194: ldc             "desert"
            //   196: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   199: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.DESERT:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   202: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   205: dup            
            //   206: ldc             "RIVER"
            //   208: bipush          13
            //   210: ldc             "river"
            //   212: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   215: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.RIVER:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   218: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   221: dup            
            //   222: ldc             "SWAMP"
            //   224: bipush          14
            //   226: ldc             "swamp"
            //   228: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   231: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.SWAMP:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   234: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   237: dup            
            //   238: ldc             "MUSHROOM"
            //   240: bipush          15
            //   242: ldc             "mushroom"
            //   244: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   247: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.MUSHROOM:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   250: new             Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   253: dup            
            //   254: ldc             "NETHER"
            //   256: bipush          16
            //   258: ldc             "nether"
            //   260: invokespecial   net/minecraft/world/level/biome/Biome$BiomeCategory.<init>:(Ljava/lang/String;ILjava/lang/String;)V
            //   263: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.NETHER:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   266: bipush          17
            //   268: anewarray       Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   271: dup            
            //   272: iconst_0       
            //   273: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.NONE:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   276: aastore        
            //   277: dup            
            //   278: iconst_1       
            //   279: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.TAIGA:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   282: aastore        
            //   283: dup            
            //   284: iconst_2       
            //   285: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.EXTREME_HILLS:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   288: aastore        
            //   289: dup            
            //   290: iconst_3       
            //   291: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.JUNGLE:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   294: aastore        
            //   295: dup            
            //   296: iconst_4       
            //   297: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.MESA:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   300: aastore        
            //   301: dup            
            //   302: iconst_5       
            //   303: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.PLAINS:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   306: aastore        
            //   307: dup            
            //   308: bipush          6
            //   310: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.SAVANNA:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   313: aastore        
            //   314: dup            
            //   315: bipush          7
            //   317: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.ICY:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   320: aastore        
            //   321: dup            
            //   322: bipush          8
            //   324: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.THEEND:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   327: aastore        
            //   328: dup            
            //   329: bipush          9
            //   331: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.BEACH:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   334: aastore        
            //   335: dup            
            //   336: bipush          10
            //   338: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.FOREST:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   341: aastore        
            //   342: dup            
            //   343: bipush          11
            //   345: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.OCEAN:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   348: aastore        
            //   349: dup            
            //   350: bipush          12
            //   352: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.DESERT:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   355: aastore        
            //   356: dup            
            //   357: bipush          13
            //   359: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.RIVER:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   362: aastore        
            //   363: dup            
            //   364: bipush          14
            //   366: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.SWAMP:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   369: aastore        
            //   370: dup            
            //   371: bipush          15
            //   373: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.MUSHROOM:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   376: aastore        
            //   377: dup            
            //   378: bipush          16
            //   380: getstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.NETHER:Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   383: aastore        
            //   384: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.$VALUES:[Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   387: invokedynamic   BootstrapMethod #0, get:()Ljava/util/function/Supplier;
            //   392: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
            //   397: invokestatic    net/minecraft/util/StringRepresentable.fromEnum:(Ljava/util/function/Supplier;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;
            //   400: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.CODEC:Lcom/mojang/serialization/Codec;
            //   403: invokestatic    net/minecraft/world/level/biome/Biome$BiomeCategory.values:()[Lnet/minecraft/world/level/biome/Biome$BiomeCategory;
            //   406: invokestatic    java/util/Arrays.stream:([Ljava/lang/Object;)Ljava/util/stream/Stream;
            //   409: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/Function;
            //   414: invokedynamic   BootstrapMethod #3, apply:()Ljava/util/function/Function;
            //   419: invokestatic    java/util/stream/Collectors.toMap:(Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/util/stream/Collector;
            //   422: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
            //   427: checkcast       Ljava/util/Map;
            //   430: putstatic       net/minecraft/world/level/biome/Biome$BiomeCategory.BY_NAME:Ljava/util/Map;
            //   433: return         
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.util.Vector.get(Vector.java:751)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
            //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
            //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
            //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
            //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
            //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
            //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
            //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
            //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
            //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
            //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
            //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
    
    public enum Precipitation implements StringRepresentable {
        NONE("none"), 
        RAIN("rain"), 
        SNOW("snow");
        
        public static final Codec<Precipitation> CODEC;
        private static final Map<String, Precipitation> BY_NAME;
        private final String name;
        
        private Precipitation(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Precipitation byName(final String string) {
            return (Precipitation)Precipitation.BY_NAME.get(string);
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<Precipitation>fromEnum((java.util.function.Supplier<Precipitation[]>)Precipitation::values, (java.util.function.Function<? super String, ? extends Precipitation>)Precipitation::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Precipitation::getName, e -> e));
        }
    }
    
    public enum TemperatureModifier implements StringRepresentable {
        NONE("none") {
            @Override
            public float modifyTemperature(final BlockPos fx, final float float2) {
                return float2;
            }
        }, 
        FROZEN("frozen") {
            @Override
            public float modifyTemperature(final BlockPos fx, final float float2) {
                final double double4 = Biome.FROZEN_TEMPERATURE_NOISE.getValue(fx.getX() * 0.05, fx.getZ() * 0.05, false) * 7.0;
                final double double5 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() * 0.2, fx.getZ() * 0.2, false);
                final double double6 = double4 + double5;
                if (double6 < 0.3) {
                    final double double7 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() * 0.09, fx.getZ() * 0.09, false);
                    if (double7 < 0.8) {
                        return 0.2f;
                    }
                }
                return float2;
            }
        };
        
        private final String name;
        public static final Codec<TemperatureModifier> CODEC;
        private static final Map<String, TemperatureModifier> BY_NAME;
        
        public abstract float modifyTemperature(final BlockPos fx, final float float2);
        
        private TemperatureModifier(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        public static TemperatureModifier byName(final String string) {
            return (TemperatureModifier)TemperatureModifier.BY_NAME.get(string);
        }
        
        static {
            CODEC = StringRepresentable.<TemperatureModifier>fromEnum((java.util.function.Supplier<TemperatureModifier[]>)TemperatureModifier::values, (java.util.function.Function<? super String, ? extends TemperatureModifier>)TemperatureModifier::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(TemperatureModifier::getName, f -> f));
        }
    }
    
    public static class BiomeBuilder {
        @Nullable
        private Precipitation precipitation;
        @Nullable
        private BiomeCategory biomeCategory;
        @Nullable
        private Float depth;
        @Nullable
        private Float scale;
        @Nullable
        private Float temperature;
        private TemperatureModifier temperatureModifier;
        @Nullable
        private Float downfall;
        @Nullable
        private BiomeSpecialEffects specialEffects;
        @Nullable
        private MobSpawnSettings mobSpawnSettings;
        @Nullable
        private BiomeGenerationSettings generationSettings;
        
        public BiomeBuilder() {
            this.temperatureModifier = TemperatureModifier.NONE;
        }
        
        public BiomeBuilder precipitation(final Precipitation e) {
            this.precipitation = e;
            return this;
        }
        
        public BiomeBuilder biomeCategory(final BiomeCategory b) {
            this.biomeCategory = b;
            return this;
        }
        
        public BiomeBuilder depth(final float float1) {
            this.depth = float1;
            return this;
        }
        
        public BiomeBuilder scale(final float float1) {
            this.scale = float1;
            return this;
        }
        
        public BiomeBuilder temperature(final float float1) {
            this.temperature = float1;
            return this;
        }
        
        public BiomeBuilder downfall(final float float1) {
            this.downfall = float1;
            return this;
        }
        
        public BiomeBuilder specialEffects(final BiomeSpecialEffects bsw) {
            this.specialEffects = bsw;
            return this;
        }
        
        public BiomeBuilder mobSpawnSettings(final MobSpawnSettings btd) {
            this.mobSpawnSettings = btd;
            return this;
        }
        
        public BiomeBuilder generationSettings(final BiomeGenerationSettings bst) {
            this.generationSettings = bst;
            return this;
        }
        
        public BiomeBuilder temperatureAdjustment(final TemperatureModifier f) {
            this.temperatureModifier = f;
            return this;
        }
        
        public Biome build() {
            if (this.precipitation == null || this.biomeCategory == null || this.depth == null || this.scale == null || this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null) {
                throw new IllegalStateException(new StringBuilder().append("You are missing parameters to build a proper biome\n").append(this).toString());
            }
            return new Biome(new ClimateSettings(this.precipitation, (float)this.temperature, this.temperatureModifier, (float)this.downfall), this.biomeCategory, this.depth, this.scale, this.specialEffects, this.generationSettings, this.mobSpawnSettings, null);
        }
        
        public String toString() {
            return new StringBuilder().append("BiomeBuilder{\nprecipitation=").append(this.precipitation).append(",\nbiomeCategory=").append(this.biomeCategory).append(",\ndepth=").append(this.depth).append(",\nscale=").append(this.scale).append(",\ntemperature=").append(this.temperature).append(",\ntemperatureModifier=").append(this.temperatureModifier).append(",\ndownfall=").append(this.downfall).append(",\nspecialEffects=").append(this.specialEffects).append(",\nmobSpawnSettings=").append(this.mobSpawnSettings).append(",\ngenerationSettings=").append(this.generationSettings).append(",\n").append('}').toString();
        }
    }
    
    public static class ClimateParameters {
        public static final Codec<ClimateParameters> CODEC;
        private final float temperature;
        private final float humidity;
        private final float altitude;
        private final float weirdness;
        private final float offset;
        
        public ClimateParameters(final float float1, final float float2, final float float3, final float float4, final float float5) {
            this.temperature = float1;
            this.humidity = float2;
            this.altitude = float3;
            this.weirdness = float4;
            this.offset = float5;
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final ClimateParameters c3 = (ClimateParameters)object;
            return Float.compare(c3.temperature, this.temperature) == 0 && Float.compare(c3.humidity, this.humidity) == 0 && Float.compare(c3.altitude, this.altitude) == 0 && Float.compare(c3.weirdness, this.weirdness) == 0;
        }
        
        public int hashCode() {
            int integer2 = (this.temperature != 0.0f) ? Float.floatToIntBits(this.temperature) : 0;
            integer2 = 31 * integer2 + ((this.humidity != 0.0f) ? Float.floatToIntBits(this.humidity) : 0);
            integer2 = 31 * integer2 + ((this.altitude != 0.0f) ? Float.floatToIntBits(this.altitude) : 0);
            integer2 = 31 * integer2 + ((this.weirdness != 0.0f) ? Float.floatToIntBits(this.weirdness) : 0);
            return integer2;
        }
        
        public float fitness(final ClimateParameters c) {
            return (this.temperature - c.temperature) * (this.temperature - c.temperature) + (this.humidity - c.humidity) * (this.humidity - c.humidity) + (this.altitude - c.altitude) * (this.altitude - c.altitude) + (this.weirdness - c.weirdness) * (this.weirdness - c.weirdness) + (this.offset - c.offset) * (this.offset - c.offset);
        }
        
        static {
            CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.floatRange(-2.0f, 2.0f).fieldOf("temperature").forGetter(c -> c.temperature), (App)Codec.floatRange(-2.0f, 2.0f).fieldOf("humidity").forGetter(c -> c.humidity), (App)Codec.floatRange(-2.0f, 2.0f).fieldOf("altitude").forGetter(c -> c.altitude), (App)Codec.floatRange(-2.0f, 2.0f).fieldOf("weirdness").forGetter(c -> c.weirdness), (App)Codec.floatRange(0.0f, 1.0f).fieldOf("offset").forGetter(c -> c.offset)).apply((Applicative)instance, ClimateParameters::new));
        }
    }
    
    static class ClimateSettings {
        public static final MapCodec<ClimateSettings> CODEC;
        private final Precipitation precipitation;
        private final float temperature;
        private final TemperatureModifier temperatureModifier;
        private final float downfall;
        
        private ClimateSettings(final Precipitation e, final float float2, final TemperatureModifier f, final float float4) {
            this.precipitation = e;
            this.temperature = float2;
            this.temperatureModifier = f;
            this.downfall = float4;
        }
        
        static {
            CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group((App)Precipitation.CODEC.fieldOf("precipitation").forGetter(d -> d.precipitation), (App)Codec.FLOAT.fieldOf("temperature").forGetter(d -> d.temperature), (App)TemperatureModifier.CODEC.optionalFieldOf("temperature_modifier", TemperatureModifier.NONE).forGetter(d -> d.temperatureModifier), (App)Codec.FLOAT.fieldOf("downfall").forGetter(d -> d.downfall)).apply((Applicative)instance, ClimateSettings::new));
        }
    }
}
