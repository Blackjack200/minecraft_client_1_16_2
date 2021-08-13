package net.minecraft.world.level.chunk.storage;

import java.io.IOException;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.DimensionDataStorage;
import java.util.function.Supplier;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
import com.mojang.datafixers.DataFixer;

public class ChunkStorage implements AutoCloseable {
    private final IOWorker worker;
    protected final DataFixer fixerUpper;
    @Nullable
    private LegacyStructureDataHandler legacyStructureHandler;
    
    public ChunkStorage(final File file, final DataFixer dataFixer, final boolean boolean3) {
        this.fixerUpper = dataFixer;
        this.worker = new IOWorker(file, boolean3, "chunk");
    }
    
    public CompoundTag upgradeChunkTag(final ResourceKey<Level> vj, final Supplier<DimensionDataStorage> supplier, CompoundTag md) {
        final int integer5 = getVersion(md);
        final int integer6 = 1493;
        if (integer5 < 1493) {
            md = NbtUtils.update(this.fixerUpper, DataFixTypes.CHUNK, md, integer5, 1493);
            if (md.getCompound("Level").getBoolean("hasLegacyStructureData")) {
                if (this.legacyStructureHandler == null) {
                    this.legacyStructureHandler = LegacyStructureDataHandler.getLegacyStructureHandler(vj, (DimensionDataStorage)supplier.get());
                }
                md = this.legacyStructureHandler.updateFromLegacy(md);
            }
        }
        md = NbtUtils.update(this.fixerUpper, DataFixTypes.CHUNK, md, Math.max(1493, integer5));
        if (integer5 < SharedConstants.getCurrentVersion().getWorldVersion()) {
            md.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        }
        return md;
    }
    
    public static int getVersion(final CompoundTag md) {
        return md.contains("DataVersion", 99) ? md.getInt("DataVersion") : -1;
    }
    
    @Nullable
    public CompoundTag read(final ChunkPos bra) throws IOException {
        return this.worker.load(bra);
    }
    
    public void write(final ChunkPos bra, final CompoundTag md) {
        this.worker.store(bra, md);
        if (this.legacyStructureHandler != null) {
            this.legacyStructureHandler.removeIndex(bra.toLong());
        }
    }
    
    public void flushWorker() {
        this.worker.synchronize().join();
    }
    
    public void close() throws IOException {
        this.worker.close();
    }
}
