package net.minecraft.resources;

import java.util.Optional;
import net.minecraft.core.WritableRegistry;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.RegistryAccess;

public class RegistryWriteOps<T> extends DelegatingOps<T> {
    private final RegistryAccess registryHolder;
    
    public static <T> RegistryWriteOps<T> create(final DynamicOps<T> dynamicOps, final RegistryAccess gn) {
        return new RegistryWriteOps<T>(dynamicOps, gn);
    }
    
    private RegistryWriteOps(final DynamicOps<T> dynamicOps, final RegistryAccess gn) {
        super(dynamicOps);
        this.registryHolder = gn;
    }
    
    protected <E> DataResult<T> encode(final E object1, final T object2, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        final Optional<WritableRegistry<E>> optional6 = this.registryHolder.<E>registry(vj);
        if (optional6.isPresent()) {
            final WritableRegistry<E> gs7 = (WritableRegistry<E>)optional6.get();
            final Optional<ResourceKey<E>> optional7 = gs7.getResourceKey(object1);
            if (optional7.isPresent()) {
                final ResourceKey<E> vj2 = (ResourceKey<E>)optional7.get();
                return (DataResult<T>)ResourceLocation.CODEC.encode(vj2.location(), (DynamicOps)this.delegate, object2);
            }
        }
        return (DataResult<T>)codec.encode(object1, (DynamicOps)this, object2);
    }
}
