package net.minecraft.world.level.chunk;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface Palette<T> {
    int idFor(final T object);
    
    boolean maybeHas(final Predicate<T> predicate);
    
    @Nullable
    T valueFor(final int integer);
    
    void read(final FriendlyByteBuf nf);
    
    void write(final FriendlyByteBuf nf);
    
    int getSerializedSize();
    
    void read(final ListTag mj);
}
