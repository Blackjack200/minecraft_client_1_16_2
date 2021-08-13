package net.minecraft.world.level.chunk.storage;

import org.apache.logging.log4j.LogManager;
import com.mojang.serialization.DataResult;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.SharedConstants;
import com.mojang.serialization.Dynamic;
import java.io.IOException;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.Util;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.SectionPos;
import java.util.function.BooleanSupplier;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.io.File;
import net.minecraft.util.datafix.DataFixTypes;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import java.util.Optional;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.apache.logging.log4j.Logger;

public class SectionStorage<R> implements AutoCloseable {
    private static final Logger LOGGER;
    private final IOWorker worker;
    private final Long2ObjectMap<Optional<R>> storage;
    private final LongLinkedOpenHashSet dirty;
    private final Function<Runnable, Codec<R>> codec;
    private final Function<Runnable, R> factory;
    private final DataFixer fixerUpper;
    private final DataFixTypes type;
    
    public SectionStorage(final File file, final Function<Runnable, Codec<R>> function2, final Function<Runnable, R> function3, final DataFixer dataFixer, final DataFixTypes afx, final boolean boolean6) {
        this.storage = (Long2ObjectMap<Optional<R>>)new Long2ObjectOpenHashMap();
        this.dirty = new LongLinkedOpenHashSet();
        this.codec = function2;
        this.factory = function3;
        this.fixerUpper = dataFixer;
        this.type = afx;
        this.worker = new IOWorker(file, boolean6, file.getName());
    }
    
    protected void tick(final BooleanSupplier booleanSupplier) {
        while (!this.dirty.isEmpty() && booleanSupplier.getAsBoolean()) {
            final ChunkPos bra3 = SectionPos.of(this.dirty.firstLong()).chunk();
            this.writeColumn(bra3);
        }
    }
    
    @Nullable
    protected Optional<R> get(final long long1) {
        return (Optional<R>)this.storage.get(long1);
    }
    
    protected Optional<R> getOrLoad(final long long1) {
        final SectionPos gp4 = SectionPos.of(long1);
        if (this.outsideStoredRange(gp4)) {
            return (Optional<R>)Optional.empty();
        }
        Optional<R> optional5 = this.get(long1);
        if (optional5 != null) {
            return optional5;
        }
        this.readColumn(gp4.chunk());
        optional5 = this.get(long1);
        if (optional5 == null) {
            throw Util.<IllegalStateException>pauseInIde(new IllegalStateException());
        }
        return optional5;
    }
    
    protected boolean outsideStoredRange(final SectionPos gp) {
        return Level.isOutsideBuildHeight(SectionPos.sectionToBlockCoord(gp.y()));
    }
    
    protected R getOrCreate(final long long1) {
        final Optional<R> optional4 = this.getOrLoad(long1);
        if (optional4.isPresent()) {
            return (R)optional4.get();
        }
        final R object5 = (R)this.factory.apply((() -> this.setDirty(long1)));
        this.storage.put(long1, Optional.of((Object)object5));
        return object5;
    }
    
    private void readColumn(final ChunkPos bra) {
        this.<CompoundTag>readColumn(bra, (com.mojang.serialization.DynamicOps<CompoundTag>)NbtOps.INSTANCE, this.tryRead(bra));
    }
    
    @Nullable
    private CompoundTag tryRead(final ChunkPos bra) {
        try {
            return this.worker.load(bra);
        }
        catch (IOException iOException3) {
            SectionStorage.LOGGER.error("Error reading chunk {} data from disk", bra, iOException3);
            return null;
        }
    }
    
    private <T> void readColumn(final ChunkPos bra, final DynamicOps<T> dynamicOps, @Nullable final T object) {
        if (object == null) {
            for (int integer5 = 0; integer5 < 16; ++integer5) {
                this.storage.put(SectionPos.of(bra, integer5).asLong(), Optional.empty());
            }
        }
        else {
            final Dynamic<T> dynamic5 = (Dynamic<T>)new Dynamic((DynamicOps)dynamicOps, object);
            final int integer6 = getVersion(dynamic5);
            final int integer7 = SharedConstants.getCurrentVersion().getWorldVersion();
            final boolean boolean8 = integer6 != integer7;
            final Dynamic<T> dynamic6 = (Dynamic<T>)this.fixerUpper.update(this.type.getType(), (Dynamic)dynamic5, integer6, integer7);
            final OptionalDynamic<T> optionalDynamic10 = (OptionalDynamic<T>)dynamic6.get("Sections");
            for (int integer8 = 0; integer8 < 16; ++integer8) {
                final long long12 = SectionPos.of(bra, integer8).asLong();
                final Optional<R> optional14 = (Optional<R>)optionalDynamic10.get(Integer.toString(integer8)).result().flatMap(dynamic -> ((Codec)this.codec.apply((() -> this.setDirty(long12)))).parse(dynamic).resultOrPartial(SectionStorage.LOGGER::error));
                this.storage.put(long12, optional14);
                optional14.ifPresent(object -> {
                    this.onSectionLoad(long12);
                    if (boolean8) {
                        this.setDirty(long12);
                    }
                });
            }
        }
    }
    
    private void writeColumn(final ChunkPos bra) {
        final Dynamic<Tag> dynamic3 = this.<Tag>writeColumn(bra, (com.mojang.serialization.DynamicOps<Tag>)NbtOps.INSTANCE);
        final Tag mt4 = (Tag)dynamic3.getValue();
        if (mt4 instanceof CompoundTag) {
            this.worker.store(bra, (CompoundTag)mt4);
        }
        else {
            SectionStorage.LOGGER.error("Expected compound tag, got {}", mt4);
        }
    }
    
    private <T> Dynamic<T> writeColumn(final ChunkPos bra, final DynamicOps<T> dynamicOps) {
        final Map<T, T> map4 = (Map<T, T>)Maps.newHashMap();
        for (int integer5 = 0; integer5 < 16; ++integer5) {
            final long long6 = SectionPos.of(bra, integer5).asLong();
            this.dirty.remove(long6);
            final Optional<R> optional8 = (Optional<R>)this.storage.get(long6);
            if (optional8 != null) {
                if (optional8.isPresent()) {
                    final DataResult<T> dataResult9 = (DataResult<T>)((Codec)this.codec.apply((() -> this.setDirty(long6)))).encodeStart((DynamicOps)dynamicOps, optional8.get());
                    final String string10 = Integer.toString(integer5);
                    dataResult9.resultOrPartial(SectionStorage.LOGGER::error).ifPresent(object -> map4.put(dynamicOps.createString(string10), object));
                }
            }
        }
        return (Dynamic<T>)new Dynamic((DynamicOps)dynamicOps, dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps.createString("Sections"), dynamicOps.createMap((Map)map4), dynamicOps.createString("DataVersion"), dynamicOps.createInt(SharedConstants.getCurrentVersion().getWorldVersion()))));
    }
    
    protected void onSectionLoad(final long long1) {
    }
    
    protected void setDirty(final long long1) {
        final Optional<R> optional4 = (Optional<R>)this.storage.get(long1);
        if (optional4 == null || !optional4.isPresent()) {
            SectionStorage.LOGGER.warn("No data for position: {}", SectionPos.of(long1));
            return;
        }
        this.dirty.add(long1);
    }
    
    private static int getVersion(final Dynamic<?> dynamic) {
        return dynamic.get("DataVersion").asInt(1945);
    }
    
    public void flush(final ChunkPos bra) {
        if (!this.dirty.isEmpty()) {
            for (int integer3 = 0; integer3 < 16; ++integer3) {
                final long long4 = SectionPos.of(bra, integer3).asLong();
                if (this.dirty.contains(long4)) {
                    this.writeColumn(bra);
                    return;
                }
            }
        }
    }
    
    public void close() throws IOException {
        this.worker.close();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
