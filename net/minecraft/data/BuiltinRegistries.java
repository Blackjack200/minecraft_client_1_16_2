package net.minecraft.data;

import net.minecraft.data.worldgen.Pools;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.core.MappedRegistry;
import com.mojang.serialization.Lifecycle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class BuiltinRegistries {
    protected static final Logger LOGGER;
    private static final Map<ResourceLocation, Supplier<?>> LOADERS;
    private static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY;
    public static final Registry<? extends Registry<?>> REGISTRY;
    public static final Registry<ConfiguredSurfaceBuilder<?>> CONFIGURED_SURFACE_BUILDER;
    public static final Registry<ConfiguredWorldCarver<?>> CONFIGURED_CARVER;
    public static final Registry<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE;
    public static final Registry<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE_FEATURE;
    public static final Registry<StructureProcessorList> PROCESSOR_LIST;
    public static final Registry<StructureTemplatePool> TEMPLATE_POOL;
    public static final Registry<Biome> BIOME;
    public static final Registry<NoiseGeneratorSettings> NOISE_GENERATOR_SETTINGS;
    
    private static <T> Registry<T> registerSimple(final ResourceKey<? extends Registry<T>> vj, final Supplier<T> supplier) {
        return BuiltinRegistries.<T>registerSimple(vj, Lifecycle.stable(), supplier);
    }
    
    private static <T> Registry<T> registerSimple(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle, final Supplier<T> supplier) {
        return BuiltinRegistries.<T, MappedRegistry<T>>internalRegister(vj, new MappedRegistry<T>(vj, lifecycle), supplier, lifecycle);
    }
    
    private static <T, R extends WritableRegistry<T>> R internalRegister(final ResourceKey<? extends Registry<T>> vj, final R gs, final Supplier<T> supplier, final Lifecycle lifecycle) {
        final ResourceLocation vk5 = vj.location();
        BuiltinRegistries.LOADERS.put(vk5, supplier);
        final WritableRegistry<R> gs2 = (WritableRegistry<R>)BuiltinRegistries.WRITABLE_REGISTRY;
        return gs2.<R>register((ResourceKey<R>)vj, gs, lifecycle);
    }
    
    public static <T> T register(final Registry<? super T> gm, final String string, final T object) {
        return BuiltinRegistries.register(gm, new ResourceLocation(string), object);
    }
    
    public static <V, T extends V> T register(final Registry<V> gm, final ResourceLocation vk, final T object) {
        return ((WritableRegistry)gm).<T>register(ResourceKey.create(gm.key(), vk), object, Lifecycle.stable());
    }
    
    public static <V, T extends V> T registerMapping(final Registry<V> gm, final int integer, final ResourceKey<V> vj, final T object) {
        return ((WritableRegistry)gm).<T>registerMapping(integer, vj, object, Lifecycle.stable());
    }
    
    public static void bootstrap() {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        LOADERS = (Map)Maps.newLinkedHashMap();
        WRITABLE_REGISTRY = new MappedRegistry<WritableRegistry<?>>(ResourceKey.createRegistryKey(new ResourceLocation("root")), Lifecycle.experimental());
        REGISTRY = BuiltinRegistries.WRITABLE_REGISTRY;
        CONFIGURED_SURFACE_BUILDER = BuiltinRegistries.<ConfiguredSurfaceBuilder<?>>registerSimple(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, (java.util.function.Supplier<ConfiguredSurfaceBuilder<?>>)(() -> SurfaceBuilders.NOPE));
        CONFIGURED_CARVER = BuiltinRegistries.<ConfiguredWorldCarver<?>>registerSimple(Registry.CONFIGURED_CARVER_REGISTRY, (java.util.function.Supplier<ConfiguredWorldCarver<?>>)(() -> Carvers.CAVE));
        CONFIGURED_FEATURE = BuiltinRegistries.<ConfiguredFeature<?, ?>>registerSimple(Registry.CONFIGURED_FEATURE_REGISTRY, (java.util.function.Supplier<ConfiguredFeature<?, ?>>)(() -> Features.OAK));
        CONFIGURED_STRUCTURE_FEATURE = BuiltinRegistries.<ConfiguredStructureFeature<?, ?>>registerSimple(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, (java.util.function.Supplier<ConfiguredStructureFeature<?, ?>>)(() -> StructureFeatures.MINESHAFT));
        PROCESSOR_LIST = BuiltinRegistries.<StructureProcessorList>registerSimple(Registry.PROCESSOR_LIST_REGISTRY, (java.util.function.Supplier<StructureProcessorList>)(() -> ProcessorLists.ZOMBIE_PLAINS));
        TEMPLATE_POOL = BuiltinRegistries.<StructureTemplatePool>registerSimple(Registry.TEMPLATE_POOL_REGISTRY, (java.util.function.Supplier<StructureTemplatePool>)Pools::bootstrap);
        BIOME = BuiltinRegistries.<Biome>registerSimple(Registry.BIOME_REGISTRY, (java.util.function.Supplier<Biome>)(() -> Biomes.PLAINS));
        NOISE_GENERATOR_SETTINGS = BuiltinRegistries.<NoiseGeneratorSettings>registerSimple(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, (java.util.function.Supplier<NoiseGeneratorSettings>)NoiseGeneratorSettings::bootstrap);
        BuiltinRegistries.LOADERS.forEach((vk, supplier) -> {
            if (supplier.get() == null) {
                BuiltinRegistries.LOGGER.error("Unable to bootstrap registry '{}'", vk);
            }
        });
        Registry.<WritableRegistry<?>>checkRegistry(BuiltinRegistries.WRITABLE_REGISTRY);
    }
}
