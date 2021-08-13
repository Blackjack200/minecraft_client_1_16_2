package net.minecraft.core;

import javax.annotation.Nullable;

public interface IdMap<T> extends Iterable<T> {
    int getId(final T object);
    
    @Nullable
    T byId(final int integer);
}
