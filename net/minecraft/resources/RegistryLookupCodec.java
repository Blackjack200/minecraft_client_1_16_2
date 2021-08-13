package net.minecraft.resources;

import java.util.stream.Stream;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Registry;
import com.mojang.serialization.MapCodec;

public final class RegistryLookupCodec<E> extends MapCodec<Registry<E>> {
    private final ResourceKey<? extends Registry<E>> registryKey;
    
    public static <E> RegistryLookupCodec<E> create(final ResourceKey<? extends Registry<E>> vj) {
        return new RegistryLookupCodec<E>(vj);
    }
    
    private RegistryLookupCodec(final ResourceKey<? extends Registry<E>> vj) {
        this.registryKey = vj;
    }
    
    public <T> RecordBuilder<T> encode(final Registry<E> gm, final DynamicOps<T> dynamicOps, final RecordBuilder<T> recordBuilder) {
        return recordBuilder;
    }
    
    public <T> DataResult<Registry<E>> decode(final DynamicOps<T> dynamicOps, final MapLike<T> mapLike) {
        if (dynamicOps instanceof RegistryReadOps) {
            return ((RegistryReadOps)dynamicOps).<E>registry(this.registryKey);
        }
        return (DataResult<Registry<E>>)DataResult.error("Not a registry ops");
    }
    
    public String toString() {
        return new StringBuilder().append("RegistryLookupCodec[").append(this.registryKey).append("]").toString();
    }
    
    public <T> Stream<T> keys(final DynamicOps<T> dynamicOps) {
        return (Stream<T>)Stream.empty();
    }
}
