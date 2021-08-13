package net.minecraft.world.level.biome;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Function3;
import java.util.Map;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import java.util.Objects;
import net.minecraft.data.worldgen.biome.Biomes;
import java.util.Comparator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import java.util.Optional;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class MultiNoiseBiomeSource extends BiomeSource {
    private static final NoiseParameters DEFAULT_NOISE_PARAMETERS;
    public static final MapCodec<MultiNoiseBiomeSource> DIRECT_CODEC;
    public static final Codec<MultiNoiseBiomeSource> CODEC;
    private final NoiseParameters temperatureParams;
    private final NoiseParameters humidityParams;
    private final NoiseParameters altitudeParams;
    private final NoiseParameters weirdnessParams;
    private final NormalNoise temperatureNoise;
    private final NormalNoise humidityNoise;
    private final NormalNoise altitudeNoise;
    private final NormalNoise weirdnessNoise;
    private final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> parameters;
    private final boolean useY;
    private final long seed;
    private final Optional<Pair<Registry<Biome>, Preset>> preset;
    
    private MultiNoiseBiomeSource(final long long1, final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, final Optional<Pair<Registry<Biome>, Preset>> optional) {
        this(long1, list, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, optional);
    }
    
    private MultiNoiseBiomeSource(final long long1, final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, final NoiseParameters a3, final NoiseParameters a4, final NoiseParameters a5, final NoiseParameters a6) {
        this(long1, list, a3, a4, a5, a6, (Optional<Pair<Registry<Biome>, Preset>>)Optional.empty());
    }
    
    private MultiNoiseBiomeSource(final long long1, final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, final NoiseParameters a3, final NoiseParameters a4, final NoiseParameters a5, final NoiseParameters a6, final Optional<Pair<Registry<Biome>, Preset>> optional) {
        super((Stream<Supplier<Biome>>)list.stream().map(Pair::getSecond));
        this.seed = long1;
        this.preset = optional;
        this.temperatureParams = a3;
        this.humidityParams = a4;
        this.altitudeParams = a5;
        this.weirdnessParams = a6;
        this.temperatureNoise = NormalNoise.create(new WorldgenRandom(long1), a3.firstOctave(), a3.amplitudes());
        this.humidityNoise = NormalNoise.create(new WorldgenRandom(long1 + 1L), a4.firstOctave(), a4.amplitudes());
        this.altitudeNoise = NormalNoise.create(new WorldgenRandom(long1 + 2L), a5.firstOctave(), a5.amplitudes());
        this.weirdnessNoise = NormalNoise.create(new WorldgenRandom(long1 + 3L), a6.firstOctave(), a6.amplitudes());
        this.parameters = list;
        this.useY = false;
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return MultiNoiseBiomeSource.CODEC;
    }
    
    @Override
    public BiomeSource withSeed(final long long1) {
        return new MultiNoiseBiomeSource(long1, this.parameters, this.temperatureParams, this.humidityParams, this.altitudeParams, this.weirdnessParams, this.preset);
    }
    
    private Optional<PresetInstance> preset() {
        return (Optional<PresetInstance>)this.preset.map(pair -> new PresetInstance((Preset)pair.getSecond(), (Registry)pair.getFirst(), this.seed));
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        final int integer4 = this.useY ? integer2 : 0;
        final Biome.ClimateParameters c6 = new Biome.ClimateParameters((float)this.temperatureNoise.getValue(integer1, integer4, integer3), (float)this.humidityNoise.getValue(integer1, integer4, integer3), (float)this.altitudeNoise.getValue(integer1, integer4, integer3), (float)this.weirdnessNoise.getValue(integer1, integer4, integer3), 0.0f);
        return (Biome)this.parameters.stream().min(Comparator.comparing(pair -> ((Biome.ClimateParameters)pair.getFirst()).fitness(c6))).map(Pair::getSecond).map(Supplier::get).orElse(Biomes.THE_VOID);
    }
    
    public boolean stable(final long long1) {
        return this.seed == long1 && this.preset.isPresent() && Objects.equals(((Pair)this.preset.get()).getSecond(), Preset.NETHER);
    }
    
    static {
        DEFAULT_NOISE_PARAMETERS = new NoiseParameters(-7, (List<Double>)ImmutableList.of(1.0, 1.0));
        DIRECT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group((App)Codec.LONG.fieldOf("seed").forGetter(bte -> bte.seed), (App)RecordCodecBuilder.create(instance -> instance.group((App)Biome.ClimateParameters.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), (App)Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)).apply((Applicative)instance, Pair::of)).listOf().fieldOf("biomes").forGetter(bte -> bte.parameters), (App)NoiseParameters.CODEC.fieldOf("temperature_noise").forGetter(bte -> bte.temperatureParams), (App)NoiseParameters.CODEC.fieldOf("humidity_noise").forGetter(bte -> bte.humidityParams), (App)NoiseParameters.CODEC.fieldOf("altitude_noise").forGetter(bte -> bte.altitudeParams), (App)NoiseParameters.CODEC.fieldOf("weirdness_noise").forGetter(bte -> bte.weirdnessParams)).apply((Applicative)instance, MultiNoiseBiomeSource::new));
        CODEC = Codec.mapEither((MapCodec)PresetInstance.CODEC, (MapCodec)MultiNoiseBiomeSource.DIRECT_CODEC).xmap(either -> (MultiNoiseBiomeSource)either.map(PresetInstance::biomeSource, Function.identity()), bte -> (Either)bte.preset().map(Either::left).orElseGet(() -> Either.right(bte))).codec();
    }
    
    static class NoiseParameters {
        private final int firstOctave;
        private final DoubleList amplitudes;
        public static final Codec<NoiseParameters> CODEC;
        
        public NoiseParameters(final int integer, final List<Double> list) {
            this.firstOctave = integer;
            this.amplitudes = (DoubleList)new DoubleArrayList((Collection)list);
        }
        
        public int firstOctave() {
            return this.firstOctave;
        }
        
        public DoubleList amplitudes() {
            return this.amplitudes;
        }
        
        static {
            CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave), (App)Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes)).apply((Applicative)instance, NoiseParameters::new));
        }
    }
    
    static final class PresetInstance {
        public static final MapCodec<PresetInstance> CODEC;
        private final Preset preset;
        private final Registry<Biome> biomes;
        private final long seed;
        
        private PresetInstance(final Preset b, final Registry<Biome> gm, final long long3) {
            this.preset = b;
            this.biomes = gm;
            this.seed = long3;
        }
        
        public Preset preset() {
            return this.preset;
        }
        
        public Registry<Biome> biomes() {
            return this.biomes;
        }
        
        public long seed() {
            return this.seed;
        }
        
        public MultiNoiseBiomeSource biomeSource() {
            return this.preset.biomeSource(this.biomes, this.seed);
        }
        
        static {
            CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group((App)ResourceLocation.CODEC.flatXmap(vk -> (DataResult)Optional.ofNullable(Preset.BY_NAME.get(vk)).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown preset: ").append(vk).toString())), b -> DataResult.success(b.name)).fieldOf("preset").stable().forGetter(PresetInstance::preset), (App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(PresetInstance::biomes), (App)Codec.LONG.fieldOf("seed").stable().forGetter(PresetInstance::seed)).apply((Applicative)instance, instance.stable(PresetInstance::new)));
        }
    }
    
    public static class Preset {
        private static final Map<ResourceLocation, Preset> BY_NAME;
        public static final Preset NETHER;
        private final ResourceLocation name;
        private final Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource> biomeSource;
        
        public Preset(final ResourceLocation vk, final Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource> function3) {
            this.name = vk;
            this.biomeSource = function3;
            Preset.BY_NAME.put(vk, this);
        }
        
        public MultiNoiseBiomeSource biomeSource(final Registry<Biome> gm, final long long2) {
            return (MultiNoiseBiomeSource)this.biomeSource.apply(this, gm, long2);
        }
        
        static {
            BY_NAME = (Map)Maps.newHashMap();
            NETHER = new Preset(new ResourceLocation("nether"), (Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource>)((b, gm, long3) -> new MultiNoiseBiomeSource(long3, (List)ImmutableList.of(Pair.of((Object)new Biome.ClimateParameters(0.0f, 0.0f, 0.0f, 0.0f, 0.0f), (Object)(() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.NETHER_WASTES))), Pair.of((Object)new Biome.ClimateParameters(0.0f, -0.5f, 0.0f, 0.0f, 0.0f), (Object)(() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.SOUL_SAND_VALLEY))), Pair.of((Object)new Biome.ClimateParameters(0.4f, 0.0f, 0.0f, 0.0f, 0.0f), (Object)(() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.CRIMSON_FOREST))), Pair.of((Object)new Biome.ClimateParameters(0.0f, 0.5f, 0.0f, 0.0f, 0.375f), (Object)(() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.WARPED_FOREST))), Pair.of((Object)new Biome.ClimateParameters(-0.5f, 0.0f, 0.0f, 0.0f, 0.175f), (Object)(() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.BASALT_DELTAS)))), Optional.of(Pair.of((Object)gm, (Object)b)), null)));
        }
    }
}
