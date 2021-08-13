package net.minecraft.world.level.chunk;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Function;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.core.IdMapper;

public class HashMapPalette<T> implements Palette<T> {
    private final IdMapper<T> registry;
    private final CrudeIncrementalIntIdentityHashBiMap<T> values;
    private final PaletteResize<T> resizeHandler;
    private final Function<CompoundTag, T> reader;
    private final Function<T, CompoundTag> writer;
    private final int bits;
    
    public HashMapPalette(final IdMapper<T> gh, final int integer, final PaletteResize<T> cgk, final Function<CompoundTag, T> function4, final Function<T, CompoundTag> function5) {
        this.registry = gh;
        this.bits = integer;
        this.resizeHandler = cgk;
        this.reader = function4;
        this.writer = function5;
        this.values = new CrudeIncrementalIntIdentityHashBiMap<T>(1 << integer);
    }
    
    public int idFor(final T object) {
        int integer3 = this.values.getId(object);
        if (integer3 == -1) {
            integer3 = this.values.add(object);
            if (integer3 >= 1 << this.bits) {
                integer3 = this.resizeHandler.onResize(this.bits + 1, object);
            }
        }
        return integer3;
    }
    
    public boolean maybeHas(final Predicate<T> predicate) {
        for (int integer3 = 0; integer3 < this.getSize(); ++integer3) {
            if (predicate.test(this.values.byId(integer3))) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public T valueFor(final int integer) {
        return this.values.byId(integer);
    }
    
    public void read(final FriendlyByteBuf nf) {
        this.values.clear();
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            this.values.add(this.registry.byId(nf.readVarInt()));
        }
    }
    
    public void write(final FriendlyByteBuf nf) {
        final int integer3 = this.getSize();
        nf.writeVarInt(integer3);
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            nf.writeVarInt(this.registry.getId(this.values.byId(integer4)));
        }
    }
    
    public int getSerializedSize() {
        int integer2 = FriendlyByteBuf.getVarIntSize(this.getSize());
        for (int integer3 = 0; integer3 < this.getSize(); ++integer3) {
            integer2 += FriendlyByteBuf.getVarIntSize(this.registry.getId(this.values.byId(integer3)));
        }
        return integer2;
    }
    
    public int getSize() {
        return this.values.size();
    }
    
    public void read(final ListTag mj) {
        this.values.clear();
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            this.values.add((T)this.reader.apply(mj.getCompound(integer3)));
        }
    }
    
    public void write(final ListTag mj) {
        for (int integer3 = 0; integer3 < this.getSize(); ++integer3) {
            mj.add(this.writer.apply(this.values.byId(integer3)));
        }
    }
}
