package net.minecraft.world.level.chunk;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Predicate;
import net.minecraft.core.IdMapper;

public class GlobalPalette<T> implements Palette<T> {
    private final IdMapper<T> registry;
    private final T defaultValue;
    
    public GlobalPalette(final IdMapper<T> gh, final T object) {
        this.registry = gh;
        this.defaultValue = object;
    }
    
    public int idFor(final T object) {
        final int integer3 = this.registry.getId(object);
        return (integer3 == -1) ? 0 : integer3;
    }
    
    public boolean maybeHas(final Predicate<T> predicate) {
        return true;
    }
    
    public T valueFor(final int integer) {
        final T object3 = this.registry.byId(integer);
        return (object3 == null) ? this.defaultValue : object3;
    }
    
    public void read(final FriendlyByteBuf nf) {
    }
    
    public void write(final FriendlyByteBuf nf) {
    }
    
    public int getSerializedSize() {
        return FriendlyByteBuf.getVarIntSize(0);
    }
    
    public void read(final ListTag mj) {
    }
}
