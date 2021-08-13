package net.minecraft.world.level.chunk.storage;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.ExceptionCollector;
import java.io.DataOutputStream;
import java.io.DataOutput;
import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataInput;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import java.io.IOException;
import net.minecraft.world.level.ChunkPos;
import java.io.File;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;

public final class RegionFileStorage implements AutoCloseable {
    private final Long2ObjectLinkedOpenHashMap<RegionFile> regionCache;
    private final File folder;
    private final boolean sync;
    
    RegionFileStorage(final File file, final boolean boolean2) {
        this.regionCache = (Long2ObjectLinkedOpenHashMap<RegionFile>)new Long2ObjectLinkedOpenHashMap();
        this.folder = file;
        this.sync = boolean2;
    }
    
    private RegionFile getRegionFile(final ChunkPos bra) throws IOException {
        final long long3 = ChunkPos.asLong(bra.getRegionX(), bra.getRegionZ());
        final RegionFile cgv5 = (RegionFile)this.regionCache.getAndMoveToFirst(long3);
        if (cgv5 != null) {
            return cgv5;
        }
        if (this.regionCache.size() >= 256) {
            ((RegionFile)this.regionCache.removeLast()).close();
        }
        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
        final File file6 = new File(this.folder, new StringBuilder().append("r.").append(bra.getRegionX()).append(".").append(bra.getRegionZ()).append(".mca").toString());
        final RegionFile cgv6 = new RegionFile(file6, this.folder, this.sync);
        this.regionCache.putAndMoveToFirst(long3, cgv6);
        return cgv6;
    }
    
    @Nullable
    public CompoundTag read(final ChunkPos bra) throws IOException {
        final RegionFile cgv3 = this.getRegionFile(bra);
        try (final DataInputStream dataInputStream4 = cgv3.getChunkDataInputStream(bra)) {
            if (dataInputStream4 == null) {
                return null;
            }
            return NbtIo.read((DataInput)dataInputStream4);
        }
    }
    
    protected void write(final ChunkPos bra, final CompoundTag md) throws IOException {
        final RegionFile cgv4 = this.getRegionFile(bra);
        try (final DataOutputStream dataOutputStream5 = cgv4.getChunkDataOutputStream(bra)) {
            NbtIo.write(md, (DataOutput)dataOutputStream5);
        }
    }
    
    public void close() throws IOException {
        final ExceptionCollector<IOException> aev2 = new ExceptionCollector<IOException>();
        for (final RegionFile cgv4 : this.regionCache.values()) {
            try {
                cgv4.close();
            }
            catch (IOException iOException5) {
                aev2.add(iOException5);
            }
        }
        aev2.throwIfPresent();
    }
    
    public void flush() throws IOException {
        for (final RegionFile cgv3 : this.regionCache.values()) {
            cgv3.flush();
        }
    }
}
