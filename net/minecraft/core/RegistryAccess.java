package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.stream.Collectors;
import java.util.function.Function;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import java.util.function.Supplier;
import net.minecraft.Util;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.biome.Biome;
import com.mojang.serialization.DataResult;
import net.minecraft.data.BuiltinRegistries;
import com.mojang.serialization.Encoder;
import java.util.Iterator;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.RegistryReadOps;
import com.mojang.serialization.Codec;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public abstract class RegistryAccess {
    private static final Logger LOGGER;
    private static final Map<ResourceKey<? extends Registry<?>>, RegistryData<?>> REGISTRIES;
    private static final RegistryHolder BUILTIN;
    
    public abstract <E> Optional<WritableRegistry<E>> registry(final ResourceKey<? extends Registry<E>> vj);
    
    public <E> WritableRegistry<E> registryOrThrow(final ResourceKey<? extends Registry<E>> vj) {
        return (WritableRegistry<E>)this.registry(vj).orElseThrow(() -> new IllegalStateException(new StringBuilder().append("Missing registry: ").append(vj).toString()));
    }
    
    public Registry<DimensionType> dimensionTypes() {
        return this.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
    }
    
    private static <E> void put(final ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>> builder, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        builder.put(vj, new RegistryData(vj, (com.mojang.serialization.Codec<Object>)codec, null));
    }
    
    private static <E> void put(final ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>> builder, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec3, final Codec<E> codec4) {
        builder.put(vj, new RegistryData(vj, (com.mojang.serialization.Codec<Object>)codec3, (com.mojang.serialization.Codec<Object>)codec4));
    }
    
    public static RegistryHolder builtin() {
        final RegistryHolder b1 = new RegistryHolder();
        final RegistryReadOps.ResourceAccess.MemoryMap a2 = new RegistryReadOps.ResourceAccess.MemoryMap();
        for (final RegistryData<?> a3 : RegistryAccess.REGISTRIES.values()) {
            RegistryAccess.addBuiltinElements(b1, a2, a3);
        }
        RegistryReadOps.create((com.mojang.serialization.DynamicOps<Object>)JsonOps.INSTANCE, a2, b1);
        return b1;
    }
    
    private static <E> void addBuiltinElements(final RegistryHolder b, final RegistryReadOps.ResourceAccess.MemoryMap a, final RegistryData<E> a) {
        final ResourceKey<? extends Registry<E>> vj4 = a.key();
        final boolean boolean5 = !vj4.equals(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY) && !vj4.equals(Registry.DIMENSION_TYPE_REGISTRY);
        final Registry<E> gm6 = RegistryAccess.BUILTIN.registryOrThrow(vj4);
        final WritableRegistry<E> gs7 = b.<E>registryOrThrow(vj4);
        for (final Map.Entry<ResourceKey<E>, E> entry9 : gm6.entrySet()) {
            final E object10 = (E)entry9.getValue();
            if (boolean5) {
                a.<E>add(RegistryAccess.BUILTIN, (ResourceKey<E>)entry9.getKey(), (com.mojang.serialization.Encoder<E>)a.codec(), gm6.getId(object10), object10, gm6.lifecycle(object10));
            }
            else {
                gs7.<E>registerMapping(gm6.getId(object10), (ResourceKey<E>)entry9.getKey(), object10, gm6.lifecycle(object10));
            }
        }
    }
    
    private static <R extends Registry<?>> void copyBuiltin(final RegistryHolder b, final ResourceKey<R> vj) {
        final Registry<R> gm3 = (Registry<R>)BuiltinRegistries.REGISTRY;
        final Registry<?> gm4 = gm3.get(vj);
        if (gm4 == null) {
            throw new IllegalStateException(new StringBuilder().append("Missing builtin registry: ").append(vj).toString());
        }
        RegistryAccess.copy(b, gm4);
    }
    
    private static <E> void copy(final RegistryHolder b, final Registry<E> gm) {
        final WritableRegistry<E> gs3 = (WritableRegistry<E>)b.registry(gm.key()).orElseThrow(() -> new IllegalStateException(new StringBuilder().append("Missing registry: ").append(gm.key()).toString()));
        for (final Map.Entry<ResourceKey<E>, E> entry5 : gm.entrySet()) {
            final E object6 = (E)entry5.getValue();
            gs3.<E>registerMapping(gm.getId(object6), (ResourceKey<E>)entry5.getKey(), object6, gm.lifecycle(object6));
        }
    }
    
    public static void load(final RegistryHolder b, final RegistryReadOps<?> vh) {
        for (final RegistryData<?> a4 : RegistryAccess.REGISTRIES.values()) {
            RegistryAccess.readRegistry(vh, b, a4);
        }
    }
    
    private static <E> void readRegistry(final RegistryReadOps<?> vh, final RegistryHolder b, final RegistryData<E> a) {
        final ResourceKey<? extends Registry<E>> vj4 = a.key();
        final MappedRegistry<E> gi5 = (MappedRegistry<E>)Optional.ofNullable(b.registries.get(vj4)).map(gi -> gi).orElseThrow(() -> new IllegalStateException(new StringBuilder().append("Missing registry: ").append(vj4).toString()));
        final DataResult<MappedRegistry<E>> dataResult6 = vh.<E>decodeElements(gi5, a.key(), a.codec());
        dataResult6.error().ifPresent(partialResult -> RegistryAccess.LOGGER.error("Error loading registry data: {}", partialResult.message()));
    }
    
    static {
        LOGGER = LogManager.getLogger();
        REGISTRIES = Util.<Map>make((java.util.function.Supplier<Map>)(() -> {
            final ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>> builder1 = (ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryData<?>>)ImmutableMap.builder();
            RegistryAccess.<DimensionType>put(builder1, Registry.DIMENSION_TYPE_REGISTRY, DimensionType.DIRECT_CODEC, DimensionType.DIRECT_CODEC);
            RegistryAccess.<Biome>put(builder1, Registry.BIOME_REGISTRY, Biome.DIRECT_CODEC, Biome.NETWORK_CODEC);
            RegistryAccess.<ConfiguredSurfaceBuilder<?>>put(builder1, Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, ConfiguredSurfaceBuilder.DIRECT_CODEC);
            RegistryAccess.<ConfiguredWorldCarver<?>>put(builder1, Registry.CONFIGURED_CARVER_REGISTRY, ConfiguredWorldCarver.DIRECT_CODEC);
            RegistryAccess.<ConfiguredFeature<?, ?>>put(builder1, Registry.CONFIGURED_FEATURE_REGISTRY, ConfiguredFeature.DIRECT_CODEC);
            RegistryAccess.<ConfiguredStructureFeature<?, ?>>put(builder1, Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ConfiguredStructureFeature.DIRECT_CODEC);
            RegistryAccess.<StructureProcessorList>put(builder1, Registry.PROCESSOR_LIST_REGISTRY, StructureProcessorType.DIRECT_CODEC);
            RegistryAccess.<StructureTemplatePool>put(builder1, Registry.TEMPLATE_POOL_REGISTRY, StructureTemplatePool.DIRECT_CODEC);
            RegistryAccess.<NoiseGeneratorSettings>put(builder1, Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, NoiseGeneratorSettings.DIRECT_CODEC);
            return builder1.build();
        }));
        BUILTIN = Util.<RegistryHolder>make((java.util.function.Supplier<RegistryHolder>)(() -> {
            final RegistryHolder b1 = new RegistryHolder();
            DimensionType.registerBuiltin(b1);
            RegistryAccess.REGISTRIES.keySet().stream().filter(vj -> !vj.equals(Registry.DIMENSION_TYPE_REGISTRY)).forEach(vj -> RegistryAccess.<Registry>copyBuiltin(b1, (ResourceKey<Registry>)vj));
            return b1;
        }));
    }
    
    static final class RegistryData<E> {
        private final ResourceKey<? extends Registry<E>> key;
        private final Codec<E> codec;
        @Nullable
        private final Codec<E> networkCodec;
        
        public RegistryData(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec2, @Nullable final Codec<E> codec3) {
            this.key = vj;
            this.codec = codec2;
            this.networkCodec = codec3;
        }
        
        public ResourceKey<? extends Registry<E>> key() {
            return this.key;
        }
        
        public Codec<E> codec() {
            return this.codec;
        }
        
        @Nullable
        public Codec<E> networkCodec() {
            return this.networkCodec;
        }
        
        public boolean sendToClient() {
            return this.networkCodec != null;
        }
    }
    
    public static final class RegistryHolder extends RegistryAccess {
        public static final Codec<RegistryHolder> NETWORK_CODEC;
        private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>> registries;
        
        private static <E> Codec<RegistryHolder> makeNetworkCodec() {
            final Codec<ResourceKey<? extends Registry<E>>> codec1 = (Codec<ResourceKey<? extends Registry<E>>>)ResourceLocation.CODEC.xmap(ResourceKey::createRegistryKey, ResourceKey::location);
            final Codec<MappedRegistry<E>> codec2 = (Codec<MappedRegistry<E>>)codec1.partialDispatch("type", gi -> DataResult.success(gi.key()), vj -> RegistryHolder.getNetworkCodec(vj).map(codec -> MappedRegistry.networkCodec(vj, Lifecycle.experimental(), (com.mojang.serialization.Codec<Object>)codec)));
            final UnboundedMapCodec<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>> unboundedMapCodec3 = Codec.unboundedMap((Codec)codec1, (Codec)codec2);
            return RegistryHolder.captureMap(unboundedMapCodec3);
        }
        
        private static <K extends ResourceKey<? extends Registry<?>>, V extends MappedRegistry<?>> Codec<RegistryHolder> captureMap(final UnboundedMapCodec<K, V> unboundedMapCodec) {
            return (Codec<RegistryHolder>)unboundedMapCodec.xmap(RegistryHolder::new, b -> (ImmutableMap)b.registries.entrySet().stream().filter(entry -> ((RegistryData)RegistryAccess.REGISTRIES.get(entry.getKey())).sendToClient()).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        
        private static <E> DataResult<? extends Codec<E>> getNetworkCodec(final ResourceKey<? extends Registry<E>> vj) {
            return Optional.ofNullable(RegistryAccess.REGISTRIES.get(vj)).map(a -> a.networkCodec()).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown or not serializable registry: ").append(vj).toString()));
        }
        
        public RegistryHolder() {
            this(RegistryAccess.REGISTRIES.keySet().stream().collect(Collectors.toMap(Function.identity(), RegistryHolder::createRegistry)));
        }
        
        private RegistryHolder(final Map<? extends ResourceKey<? extends Registry<?>>, ? extends MappedRegistry<?>> map) {
            this.registries = map;
        }
        
        private static <E> MappedRegistry<?> createRegistry(final ResourceKey<? extends Registry<?>> vj) {
            return new MappedRegistry<>(vj, Lifecycle.stable());
        }
        
        @Override
        public <E> Optional<WritableRegistry<E>> registry(final ResourceKey<? extends Registry<E>> vj) {
            return (Optional<WritableRegistry<E>>)Optional.ofNullable(this.registries.get(vj)).map(gi -> gi);
        }
        
        static {
            NETWORK_CODEC = RegistryHolder.makeNetworkCodec();
        }
    }
}
