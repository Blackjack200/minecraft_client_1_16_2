package net.minecraft.world.level.chunk;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Function;
import net.minecraft.core.IdMapper;

public class LinearPalette<T> implements Palette<T> {
    private final IdMapper<T> registry;
    private final T[] values;
    private final PaletteResize<T> resizeHandler;
    private final Function<CompoundTag, T> reader;
    private final int bits;
    private int size;
    
    public LinearPalette(final IdMapper<T> gh, final int integer, final PaletteResize<T> cgk, final Function<CompoundTag, T> function) {
        this.registry = gh;
        this.values = (T[])new Object[1 << integer];
        this.bits = integer;
        this.resizeHandler = cgk;
        this.reader = function;
    }
    
    public int idFor(final T object) {
        for (int integer3 = 0; integer3 < this.size; ++integer3) {
            if (this.values[integer3] == object) {
                return integer3;
            }
        }
        int integer3 = this.size;
        if (integer3 < this.values.length) {
            this.values[integer3] = object;
            ++this.size;
            return integer3;
        }
        return this.resizeHandler.onResize(this.bits + 1, object);
    }
    
    public boolean maybeHas(final Predicate<T> predicate) {
        for (int integer3 = 0; integer3 < this.size; ++integer3) {
            if (predicate.test(this.values[integer3])) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public T valueFor(final int integer) {
        if (integer >= 0 && integer < this.size) {
            return this.values[integer];
        }
        return null;
    }
    
    public void read(final FriendlyByteBuf nf) {
        this.size = nf.readVarInt();
        for (int integer3 = 0; integer3 < this.size; ++integer3) {
            this.values[integer3] = this.registry.byId(nf.readVarInt());
        }
    }
    
    public void write(final FriendlyByteBuf nf) {
        nf.writeVarInt(this.size);
        for (int integer3 = 0; integer3 < this.size; ++integer3) {
            nf.writeVarInt(this.registry.getId(this.values[integer3]));
        }
    }
    
    public int getSerializedSize() {
        int integer2 = FriendlyByteBuf.getVarIntSize(this.getSize());
        for (int integer3 = 0; integer3 < this.getSize(); ++integer3) {
            integer2 += FriendlyByteBuf.getVarIntSize(this.registry.getId(this.values[integer3]));
        }
        return integer2;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void read(final ListTag mj) {
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            this.values[integer3] = (T)this.reader.apply(mj.getCompound(integer3));
        }
        this.size = mj.size();
    }
}
