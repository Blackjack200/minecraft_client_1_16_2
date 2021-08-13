package net.minecraft.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Either;
import java.util.List;
import net.minecraft.core.Registry;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public final class RegistryFileCodec<E> implements Codec<Supplier<E>> {
    private final ResourceKey<? extends Registry<E>> registryKey;
    private final Codec<E> elementCodec;
    private final boolean allowInline;
    
    public static <E> RegistryFileCodec<E> create(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        return RegistryFileCodec.<E>create(vj, codec, true);
    }
    
    public static <E> Codec<List<Supplier<E>>> homogeneousList(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        return (Codec<List<Supplier<E>>>)Codec.either(RegistryFileCodec.<E>create(vj, codec, false).listOf(), codec.xmap(object -> () -> object, Supplier::get).listOf()).xmap(either -> (List)either.map(list -> list, list -> list), Either::left);
    }
    
    private static <E> RegistryFileCodec<E> create(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec, final boolean boolean3) {
        return new RegistryFileCodec<E>(vj, codec, boolean3);
    }
    
    private RegistryFileCodec(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec, final boolean boolean3) {
        this.registryKey = vj;
        this.elementCodec = codec;
        this.allowInline = boolean3;
    }
    
    public <T> DataResult<T> encode(final Supplier<E> supplier, final DynamicOps<T> dynamicOps, final T object) {
        if (dynamicOps instanceof RegistryWriteOps) {
            return ((RegistryWriteOps)dynamicOps).encode(supplier.get(), object, this.registryKey, this.elementCodec);
        }
        return (DataResult<T>)this.elementCodec.encode(supplier.get(), (DynamicOps)dynamicOps, object);
    }
    
    public <T> DataResult<Pair<Supplier<E>, T>> decode(final DynamicOps<T> dynamicOps, final T object) {
        if (dynamicOps instanceof RegistryReadOps) {
            return ((RegistryReadOps)dynamicOps).<E>decodeElement(object, this.registryKey, this.elementCodec, this.allowInline);
        }
        return (DataResult<Pair<Supplier<E>, T>>)this.elementCodec.decode((DynamicOps)dynamicOps, object).map(pair -> pair.mapFirst(object -> () -> object));
    }
    
    public String toString() {
        return new StringBuilder().append("RegistryFileCodec[").append(this.registryKey).append(" ").append(this.elementCodec).append("]").toString();
    }
}
