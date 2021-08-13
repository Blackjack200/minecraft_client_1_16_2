package net.minecraft.core;

import java.util.OptionalInt;
import com.mojang.serialization.Lifecycle;
import net.minecraft.resources.ResourceKey;

public abstract class WritableRegistry<T> extends Registry<T> {
    public WritableRegistry(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle) {
        super(vj, lifecycle);
    }
    
    public abstract <V extends T> V registerMapping(final int integer, final ResourceKey<T> vj, final V object, final Lifecycle lifecycle);
    
    public abstract <V extends T> V register(final ResourceKey<T> vj, final V object, final Lifecycle lifecycle);
    
    public abstract <V extends T> V registerOrOverride(final OptionalInt optionalInt, final ResourceKey<T> vj, final V object, final Lifecycle lifecycle);
}
