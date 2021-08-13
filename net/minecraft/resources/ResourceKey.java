package net.minecraft.resources;

import java.util.Collections;
import com.google.common.collect.Maps;
import java.util.function.Function;
import net.minecraft.core.Registry;
import java.util.Map;

public class ResourceKey<T> {
    private static final Map<String, ResourceKey<?>> VALUES;
    private final ResourceLocation registryName;
    private final ResourceLocation location;
    
    public static <T> ResourceKey<T> create(final ResourceKey<? extends Registry<T>> vj, final ResourceLocation vk) {
        return ResourceKey.<T>create(vj.location, vk);
    }
    
    public static <T> ResourceKey<Registry<T>> createRegistryKey(final ResourceLocation vk) {
        return ResourceKey.<Registry<T>>create(Registry.ROOT_REGISTRY_NAME, vk);
    }
    
    private static <T> ResourceKey<T> create(final ResourceLocation vk1, final ResourceLocation vk2) {
        final String string3 = new StringBuilder().append(vk1).append(":").append(vk2).toString().intern();
        return (ResourceKey<T>)ResourceKey.VALUES.computeIfAbsent(string3, string -> new ResourceKey(vk1, vk2));
    }
    
    private ResourceKey(final ResourceLocation vk1, final ResourceLocation vk2) {
        this.registryName = vk1;
        this.location = vk2;
    }
    
    public String toString() {
        return new StringBuilder().append("ResourceKey[").append(this.registryName).append(" / ").append(this.location).append(']').toString();
    }
    
    public boolean isFor(final ResourceKey<? extends Registry<?>> vj) {
        return this.registryName.equals(vj.location());
    }
    
    public ResourceLocation location() {
        return this.location;
    }
    
    public static <T> Function<ResourceLocation, ResourceKey<T>> elementKey(final ResourceKey<? extends Registry<T>> vj) {
        return (Function<ResourceLocation, ResourceKey<T>>)(vk -> ResourceKey.create(vj, vk));
    }
    
    static {
        VALUES = Collections.synchronizedMap((Map)Maps.newIdentityHashMap());
    }
}
