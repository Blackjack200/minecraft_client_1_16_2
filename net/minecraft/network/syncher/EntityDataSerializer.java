package net.minecraft.network.syncher;

import net.minecraft.network.FriendlyByteBuf;

public interface EntityDataSerializer<T> {
    void write(final FriendlyByteBuf nf, final T object);
    
    T read(final FriendlyByteBuf nf);
    
    default EntityDataAccessor<T> createAccessor(final int integer) {
        return new EntityDataAccessor<T>(integer, this);
    }
    
    T copy(final T object);
}
