package net.minecraft.resources;

import java.util.stream.Collectors;
import com.mojang.serialization.Encoder;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.io.Reader;
import net.minecraft.server.packs.resources.Resource;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonIOException;
import java.io.IOException;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import java.util.OptionalInt;
import com.mojang.serialization.Decoder;
import com.google.common.base.Suppliers;
import java.util.Iterator;
import java.util.Collection;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import java.util.Optional;
import net.minecraft.core.WritableRegistry;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.util.IdentityHashMap;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.serialization.DynamicOps;
import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import org.apache.logging.log4j.Logger;

public class RegistryReadOps<T> extends DelegatingOps<T> {
    private static final Logger LOGGER;
    private final ResourceAccess resources;
    private final RegistryAccess.RegistryHolder registryHolder;
    private final Map<ResourceKey<? extends Registry<?>>, ReadCache<?>> readCache;
    private final RegistryReadOps<JsonElement> jsonOps;
    
    public static <T> RegistryReadOps<T> create(final DynamicOps<T> dynamicOps, final ResourceManager acf, final RegistryAccess.RegistryHolder b) {
        return RegistryReadOps.<T>create(dynamicOps, ResourceAccess.forResourceManager(acf), b);
    }
    
    public static <T> RegistryReadOps<T> create(final DynamicOps<T> dynamicOps, final ResourceAccess b, final RegistryAccess.RegistryHolder b) {
        final RegistryReadOps<T> vh4 = new RegistryReadOps<T>(dynamicOps, b, b, (IdentityHashMap<ResourceKey<? extends Registry<?>>, ReadCache<?>>)Maps.newIdentityHashMap());
        RegistryAccess.load(b, vh4);
        return vh4;
    }
    
    private RegistryReadOps(final DynamicOps<T> dynamicOps, final ResourceAccess b, final RegistryAccess.RegistryHolder b, final IdentityHashMap<ResourceKey<? extends Registry<?>>, ReadCache<?>> identityHashMap) {
        super(dynamicOps);
        this.resources = b;
        this.registryHolder = b;
        this.readCache = (Map<ResourceKey<? extends Registry<?>>, ReadCache<?>>)identityHashMap;
        this.jsonOps = ((dynamicOps == JsonOps.INSTANCE) ? this : new RegistryReadOps<JsonElement>((com.mojang.serialization.DynamicOps<Object>)JsonOps.INSTANCE, b, b, identityHashMap));
    }
    
    protected <E> DataResult<Pair<Supplier<E>, T>> decodeElement(final T object, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec, final boolean boolean4) {
        final Optional<WritableRegistry<E>> optional6 = this.registryHolder.<E>registry(vj);
        if (!optional6.isPresent()) {
            return (DataResult<Pair<Supplier<E>, T>>)DataResult.error(new StringBuilder().append("Unknown registry: ").append(vj).toString());
        }
        final WritableRegistry<E> gs7 = (WritableRegistry<E>)optional6.get();
        final DataResult<Pair<ResourceLocation, T>> dataResult8 = (DataResult<Pair<ResourceLocation, T>>)ResourceLocation.CODEC.decode((DynamicOps)this.delegate, object);
        if (dataResult8.result().isPresent()) {
            final Pair<ResourceLocation, T> pair9 = (Pair<ResourceLocation, T>)dataResult8.result().get();
            final ResourceLocation vk10 = (ResourceLocation)pair9.getFirst();
            return (DataResult<Pair<Supplier<E>, T>>)this.<E>readAndRegisterElement(vj, gs7, codec, vk10).map(supplier -> Pair.of(supplier, pair9.getSecond()));
        }
        if (!boolean4) {
            return (DataResult<Pair<Supplier<E>, T>>)DataResult.error("Inline definitions not allowed here");
        }
        return (DataResult<Pair<Supplier<E>, T>>)codec.decode((DynamicOps)this, object).map(pair -> pair.mapFirst(object -> () -> object));
    }
    
    public <E> DataResult<MappedRegistry<E>> decodeElements(final MappedRegistry<E> gi, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        final Collection<ResourceLocation> collection5 = this.resources.listResources(vj);
        DataResult<MappedRegistry<E>> dataResult6 = (DataResult<MappedRegistry<E>>)DataResult.success(gi, Lifecycle.stable());
        final String string7 = vj.location().getPath() + "/";
        for (final ResourceLocation vk9 : collection5) {
            final String string8 = vk9.getPath();
            if (!string8.endsWith(".json")) {
                RegistryReadOps.LOGGER.warn("Skipping resource {} since it is not a json file", vk9);
            }
            else if (!string8.startsWith(string7)) {
                RegistryReadOps.LOGGER.warn("Skipping resource {} since it does not have a registry name prefix", vk9);
            }
            else {
                final String string9 = string8.substring(string7.length(), string8.length() - ".json".length());
                final ResourceLocation vk10 = new ResourceLocation(vk9.getNamespace(), string9);
                dataResult6 = (DataResult<MappedRegistry<E>>)dataResult6.flatMap(gi -> this.readAndRegisterElement(vj, gi, (com.mojang.serialization.Codec<Object>)codec, vk10).map(supplier -> gi));
            }
        }
        return (DataResult<MappedRegistry<E>>)dataResult6.setPartial(gi);
    }
    
    private <E> DataResult<Supplier<E>> readAndRegisterElement(final ResourceKey<? extends Registry<E>> vj, final WritableRegistry<E> gs, final Codec<E> codec, final ResourceLocation vk) {
        final ResourceKey<E> vj2 = ResourceKey.<E>create(vj, vk);
        final ReadCache<E> a7 = this.<E>readCache(vj);
        final DataResult<Supplier<E>> dataResult8 = (DataResult<Supplier<E>>)((ReadCache<Object>)a7).values.get(vj2);
        if (dataResult8 != null) {
            return dataResult8;
        }
        final Supplier<E> supplier9 = (Supplier<E>)Suppliers.memoize(() -> {
            final Object object3 = gs.get(vj2);
            if (object3 == null) {
                throw new RuntimeException(new StringBuilder().append("Error during recursive registry parsing, element resolved too early: ").append(vj2).toString());
            }
            return object3;
        });
        ((ReadCache<Object>)a7).values.put(vj2, DataResult.success((Object)supplier9));
        final DataResult<Pair<E, OptionalInt>> dataResult9 = this.resources.<E>parseElement((DynamicOps<JsonElement>)this.jsonOps, vj, vj2, (com.mojang.serialization.Decoder<E>)codec);
        final Optional<Pair<E, OptionalInt>> optional11 = (Optional<Pair<E, OptionalInt>>)dataResult9.result();
        if (optional11.isPresent()) {
            final Pair<E, OptionalInt> pair12 = (Pair<E, OptionalInt>)optional11.get();
            gs.registerOrOverride((OptionalInt)pair12.getSecond(), vj2, pair12.getFirst(), dataResult9.lifecycle());
        }
        DataResult<Supplier<E>> dataResult10;
        if (!optional11.isPresent() && gs.get(vj2) != null) {
            dataResult10 = (DataResult<Supplier<E>>)DataResult.success((() -> gs.get(vj2)), Lifecycle.stable());
        }
        else {
            dataResult10 = (DataResult<Supplier<E>>)dataResult9.map(pair -> () -> gs.get(vj2));
        }
        ((ReadCache<Object>)a7).values.put(vj2, dataResult10);
        return dataResult10;
    }
    
    private <E> ReadCache<E> readCache(final ResourceKey<? extends Registry<E>> vj) {
        return (ReadCache<E>)this.readCache.computeIfAbsent(vj, vj -> new ReadCache());
    }
    
    protected <E> DataResult<Registry<E>> registry(final ResourceKey<? extends Registry<E>> vj) {
        return (DataResult<Registry<E>>)this.registryHolder.registry(vj).map(gs -> DataResult.success(gs, gs.elementsLifecycle())).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown registry: ").append(vj).toString()));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static final class ReadCache<E> {
        private final Map<ResourceKey<E>, DataResult<Supplier<E>>> values;
        
        private ReadCache() {
            this.values = (Map<ResourceKey<E>, DataResult<Supplier<E>>>)Maps.newIdentityHashMap();
        }
    }
    
    public interface ResourceAccess {
        Collection<ResourceLocation> listResources(final ResourceKey<? extends Registry<?>> vj);
        
         <E> DataResult<Pair<E, OptionalInt>> parseElement(final DynamicOps<JsonElement> dynamicOps, final ResourceKey<? extends Registry<E>> vj2, final ResourceKey<E> vj3, final Decoder<E> decoder);
        
        default ResourceAccess forResourceManager(final ResourceManager acf) {
            return new ResourceAccess() {
                public Collection<ResourceLocation> listResources(final ResourceKey<? extends Registry<?>> vj) {
                    return acf.listResources(vj.location().getPath(), (Predicate<String>)(string -> string.endsWith(".json")));
                }
                
                public <E> DataResult<Pair<E, OptionalInt>> parseElement(final DynamicOps<JsonElement> dynamicOps, final ResourceKey<? extends Registry<E>> vj2, final ResourceKey<E> vj3, final Decoder<E> decoder) {
                    final ResourceLocation vk6 = vj3.location();
                    final ResourceLocation vk7 = new ResourceLocation(vk6.getNamespace(), vj2.location().getPath() + "/" + vk6.getPath() + ".json");
                    try (final Resource ace8 = acf.getResource(vk7);
                         final Reader reader10 = (Reader)new InputStreamReader(ace8.getInputStream(), StandardCharsets.UTF_8)) {
                        final JsonParser jsonParser12 = new JsonParser();
                        final JsonElement jsonElement13 = jsonParser12.parse(reader10);
                        return (DataResult<Pair<E, OptionalInt>>)decoder.parse((DynamicOps)dynamicOps, jsonElement13).map(object -> Pair.of(object, OptionalInt.empty()));
                    }
                    catch (IOException | JsonIOException | JsonSyntaxException ex2) {
                        final Exception ex;
                        final Exception exception8 = ex;
                        return (DataResult<Pair<E, OptionalInt>>)DataResult.error(new StringBuilder().append("Failed to parse ").append(vk7).append(" file: ").append(exception8.getMessage()).toString());
                    }
                }
                
                public String toString() {
                    return new StringBuilder().append("ResourceAccess[").append(acf).append("]").toString();
                }
            };
        }
        
        public static final class MemoryMap implements ResourceAccess {
            private final Map<ResourceKey<?>, JsonElement> data;
            private final Object2IntMap<ResourceKey<?>> ids;
            private final Map<ResourceKey<?>, Lifecycle> lifecycles;
            
            public MemoryMap() {
                this.data = (Map<ResourceKey<?>, JsonElement>)Maps.newIdentityHashMap();
                this.ids = (Object2IntMap<ResourceKey<?>>)new Object2IntOpenCustomHashMap((Hash.Strategy)Util.identityStrategy());
                this.lifecycles = (Map<ResourceKey<?>, Lifecycle>)Maps.newIdentityHashMap();
            }
            
            public <E> void add(final RegistryAccess.RegistryHolder b, final ResourceKey<E> vj, final Encoder<E> encoder, final int integer, final E object, final Lifecycle lifecycle) {
                final DataResult<JsonElement> dataResult8 = (DataResult<JsonElement>)encoder.encodeStart((DynamicOps)RegistryWriteOps.create((com.mojang.serialization.DynamicOps<Object>)JsonOps.INSTANCE, b), object);
                final Optional<DataResult.PartialResult<JsonElement>> optional9 = (Optional<DataResult.PartialResult<JsonElement>>)dataResult8.error();
                if (optional9.isPresent()) {
                    RegistryReadOps.LOGGER.error("Error adding element: {}", ((DataResult.PartialResult)optional9.get()).message());
                    return;
                }
                this.data.put(vj, dataResult8.result().get());
                this.ids.put(vj, integer);
                this.lifecycles.put(vj, lifecycle);
            }
            
            public Collection<ResourceLocation> listResources(final ResourceKey<? extends Registry<?>> vj) {
                return (Collection<ResourceLocation>)this.data.keySet().stream().filter(vj2 -> vj2.isFor(vj)).map(vj2 -> new ResourceLocation(vj2.location().getNamespace(), vj.location().getPath() + "/" + vj2.location().getPath() + ".json")).collect(Collectors.toList());
            }
            
            public <E> DataResult<Pair<E, OptionalInt>> parseElement(final DynamicOps<JsonElement> dynamicOps, final ResourceKey<? extends Registry<E>> vj2, final ResourceKey<E> vj3, final Decoder<E> decoder) {
                final JsonElement jsonElement6 = (JsonElement)this.data.get(vj3);
                if (jsonElement6 == null) {
                    return (DataResult<Pair<E, OptionalInt>>)DataResult.error(new StringBuilder().append("Unknown element: ").append(vj3).toString());
                }
                return (DataResult<Pair<E, OptionalInt>>)decoder.parse((DynamicOps)dynamicOps, jsonElement6).setLifecycle((Lifecycle)this.lifecycles.get(vj3)).map(object -> Pair.of(object, OptionalInt.of(this.ids.getInt((Object)vj3))));
            }
        }
    }
}
