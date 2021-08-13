package net.minecraft.world.level.chunk;

import java.util.Arrays;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.function.Predicate;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.util.BitStorage;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Function;
import net.minecraft.core.IdMapper;

public class PalettedContainer<T> implements PaletteResize<T> {
    private final Palette<T> globalPalette;
    private final PaletteResize<T> dummyPaletteResize;
    private final IdMapper<T> registry;
    private final Function<CompoundTag, T> reader;
    private final Function<T, CompoundTag> writer;
    private final T defaultValue;
    protected BitStorage storage;
    private Palette<T> palette;
    private int bits;
    private final ReentrantLock lock;
    
    public void acquire() {
        if (this.lock.isLocked() && !this.lock.isHeldByCurrentThread()) {
            final String string2 = (String)Thread.getAllStackTraces().keySet().stream().filter(Objects::nonNull).map(thread -> thread.getName() + ": \n\tat " + (String)Arrays.stream((Object[])thread.getStackTrace()).map(Object::toString).collect(Collectors.joining("\n\tat "))).collect(Collectors.joining("\n"));
            final CrashReport l3 = new CrashReport("Writing into PalettedContainer from multiple threads", (Throwable)new IllegalStateException());
            final CrashReportCategory m4 = l3.addCategory("Thread dumps");
            m4.setDetail("Thread dumps", string2);
            throw new ReportedException(l3);
        }
        this.lock.lock();
    }
    
    public void release() {
        this.lock.unlock();
    }
    
    public PalettedContainer(final Palette<T> cgj, final IdMapper<T> gh, final Function<CompoundTag, T> function3, final Function<T, CompoundTag> function4, final T object) {
        this.dummyPaletteResize = ((integer, object) -> 0);
        this.lock = new ReentrantLock();
        this.globalPalette = cgj;
        this.registry = gh;
        this.reader = function3;
        this.writer = function4;
        this.defaultValue = object;
        this.setBits(4);
    }
    
    private static int getIndex(final int integer1, final int integer2, final int integer3) {
        return integer2 << 8 | integer3 << 4 | integer1;
    }
    
    private void setBits(final int integer) {
        if (integer == this.bits) {
            return;
        }
        this.bits = integer;
        if (this.bits <= 4) {
            this.bits = 4;
            this.palette = new LinearPalette<T>(this.registry, this.bits, this, this.reader);
        }
        else if (this.bits < 9) {
            this.palette = new HashMapPalette<T>(this.registry, this.bits, this, this.reader, this.writer);
        }
        else {
            this.palette = this.globalPalette;
            this.bits = Mth.ceillog2(this.registry.size());
        }
        this.palette.idFor(this.defaultValue);
        this.storage = new BitStorage(this.bits, 4096);
    }
    
    public int onResize(final int integer, final T object) {
        this.acquire();
        final BitStorage aep4 = this.storage;
        final Palette<T> cgj5 = this.palette;
        this.setBits(integer);
        for (int integer2 = 0; integer2 < aep4.getSize(); ++integer2) {
            final T object2 = cgj5.valueFor(aep4.get(integer2));
            if (object2 != null) {
                this.set(integer2, object2);
            }
        }
        int integer2 = this.palette.idFor(object);
        this.release();
        return integer2;
    }
    
    public T getAndSet(final int integer1, final int integer2, final int integer3, final T object) {
        this.acquire();
        final T object2 = this.getAndSet(getIndex(integer1, integer2, integer3), object);
        this.release();
        return object2;
    }
    
    public T getAndSetUnchecked(final int integer1, final int integer2, final int integer3, final T object) {
        return this.getAndSet(getIndex(integer1, integer2, integer3), object);
    }
    
    protected T getAndSet(final int integer, final T object) {
        final int integer2 = this.palette.idFor(object);
        final int integer3 = this.storage.getAndSet(integer, integer2);
        final T object2 = this.palette.valueFor(integer3);
        return (object2 == null) ? this.defaultValue : object2;
    }
    
    protected void set(final int integer, final T object) {
        final int integer2 = this.palette.idFor(object);
        this.storage.set(integer, integer2);
    }
    
    public T get(final int integer1, final int integer2, final int integer3) {
        return this.get(getIndex(integer1, integer2, integer3));
    }
    
    protected T get(final int integer) {
        final T object3 = this.palette.valueFor(this.storage.get(integer));
        return (object3 == null) ? this.defaultValue : object3;
    }
    
    public void read(final FriendlyByteBuf nf) {
        this.acquire();
        final int integer3 = nf.readByte();
        if (this.bits != integer3) {
            this.setBits(integer3);
        }
        this.palette.read(nf);
        nf.readLongArray(this.storage.getRaw());
        this.release();
    }
    
    public void write(final FriendlyByteBuf nf) {
        this.acquire();
        nf.writeByte(this.bits);
        this.palette.write(nf);
        nf.writeLongArray(this.storage.getRaw());
        this.release();
    }
    
    public void read(final ListTag mj, final long[] arr) {
        this.acquire();
        final int integer4 = Math.max(4, Mth.ceillog2(mj.size()));
        if (integer4 != this.bits) {
            this.setBits(integer4);
        }
        this.palette.read(mj);
        final int integer5 = arr.length * 64 / 4096;
        if (this.palette == this.globalPalette) {
            final Palette<T> cgj6 = new HashMapPalette<T>(this.registry, integer4, this.dummyPaletteResize, this.reader, this.writer);
            cgj6.read(mj);
            final BitStorage aep7 = new BitStorage(integer4, 4096, arr);
            for (int integer6 = 0; integer6 < 4096; ++integer6) {
                this.storage.set(integer6, this.globalPalette.idFor(cgj6.valueFor(aep7.get(integer6))));
            }
        }
        else if (integer5 == this.bits) {
            System.arraycopy(arr, 0, this.storage.getRaw(), 0, arr.length);
        }
        else {
            final BitStorage aep8 = new BitStorage(integer5, 4096, arr);
            for (int integer7 = 0; integer7 < 4096; ++integer7) {
                this.storage.set(integer7, aep8.get(integer7));
            }
        }
        this.release();
    }
    
    public void write(final CompoundTag md, final String string2, final String string3) {
        this.acquire();
        final HashMapPalette<T> cgc5 = new HashMapPalette<T>(this.registry, this.bits, this.dummyPaletteResize, this.reader, this.writer);
        T object6 = this.defaultValue;
        int integer7 = cgc5.idFor(this.defaultValue);
        final int[] arr8 = new int[4096];
        for (int integer8 = 0; integer8 < 4096; ++integer8) {
            final T object7 = this.get(integer8);
            if (object7 != object6) {
                object6 = object7;
                integer7 = cgc5.idFor(object7);
            }
            arr8[integer8] = integer7;
        }
        final ListTag mj9 = new ListTag();
        cgc5.write(mj9);
        md.put(string2, mj9);
        final int integer9 = Math.max(4, Mth.ceillog2(mj9.size()));
        final BitStorage aep11 = new BitStorage(integer9, 4096);
        for (int integer10 = 0; integer10 < arr8.length; ++integer10) {
            aep11.set(integer10, arr8[integer10]);
        }
        md.putLongArray(string3, aep11.getRaw());
        this.release();
    }
    
    public int getSerializedSize() {
        return 1 + this.palette.getSerializedSize() + FriendlyByteBuf.getVarIntSize(this.storage.getSize()) + this.storage.getRaw().length * 8;
    }
    
    public boolean maybeHas(final Predicate<T> predicate) {
        return this.palette.maybeHas(predicate);
    }
    
    public void count(final CountConsumer<T> a) {
        final Int2IntMap int2IntMap3 = (Int2IntMap)new Int2IntOpenHashMap();
        this.storage.getAll(integer -> int2IntMap3.put(integer, int2IntMap3.get(integer) + 1));
        int2IntMap3.int2IntEntrySet().forEach(entry -> a.accept(this.palette.valueFor(entry.getIntKey()), entry.getIntValue()));
    }
    
    @FunctionalInterface
    public interface CountConsumer<T> {
        void accept(final T object, final int integer);
    }
}
