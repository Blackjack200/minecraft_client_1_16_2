package net.minecraft.client.multiplayer;

import java.util.concurrent.atomic.AtomicReferenceArray;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LightLayer;
import java.util.function.BooleanSupplier;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.chunk.ChunkStatus;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.chunk.ChunkSource;

public class ClientChunkCache extends ChunkSource {
    private static final Logger LOGGER;
    private final LevelChunk emptyChunk;
    private final LevelLightEngine lightEngine;
    private volatile Storage storage;
    private final ClientLevel level;
    
    public ClientChunkCache(final ClientLevel dwl, final int integer) {
        this.level = dwl;
        this.emptyChunk = new EmptyLevelChunk(dwl, new ChunkPos(0, 0));
        this.lightEngine = new LevelLightEngine(this, true, dwl.dimensionType().hasSkyLight());
        this.storage = new Storage(calculateStorageRange(integer));
    }
    
    @Override
    public LevelLightEngine getLightEngine() {
        return this.lightEngine;
    }
    
    private static boolean isValidChunk(@Nullable final LevelChunk cge, final int integer2, final int integer3) {
        if (cge == null) {
            return false;
        }
        final ChunkPos bra4 = cge.getPos();
        return bra4.x == integer2 && bra4.z == integer3;
    }
    
    public void drop(final int integer1, final int integer2) {
        if (!this.storage.inRange(integer1, integer2)) {
            return;
        }
        final int integer3 = this.storage.getIndex(integer1, integer2);
        final LevelChunk cge5 = this.storage.getChunk(integer3);
        if (isValidChunk(cge5, integer1, integer2)) {
            this.storage.replace(integer3, cge5, null);
        }
    }
    
    @Nullable
    @Override
    public LevelChunk getChunk(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4) {
        if (this.storage.inRange(integer1, integer2)) {
            final LevelChunk cge6 = this.storage.getChunk(this.storage.getIndex(integer1, integer2));
            if (isValidChunk(cge6, integer1, integer2)) {
                return cge6;
            }
        }
        if (boolean4) {
            return this.emptyChunk;
        }
        return null;
    }
    
    public BlockGetter getLevel() {
        return this.level;
    }
    
    @Nullable
    public LevelChunk replaceWithPacketData(final int integer1, final int integer2, @Nullable final ChunkBiomeContainer cfu, final FriendlyByteBuf nf, final CompoundTag md, final int integer6, final boolean boolean7) {
        if (!this.storage.inRange(integer1, integer2)) {
            ClientChunkCache.LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", integer1, integer2);
            return null;
        }
        final int integer7 = this.storage.getIndex(integer1, integer2);
        LevelChunk cge10 = (LevelChunk)this.storage.chunks.get(integer7);
        if (boolean7 || !isValidChunk(cge10, integer1, integer2)) {
            if (cfu == null) {
                ClientChunkCache.LOGGER.warn("Ignoring chunk since we don't have complete data: {}, {}", integer1, integer2);
                return null;
            }
            cge10 = new LevelChunk(this.level, new ChunkPos(integer1, integer2), cfu);
            cge10.replaceWithPacketData(cfu, nf, md, integer6);
            this.storage.replace(integer7, cge10);
        }
        else {
            cge10.replaceWithPacketData(cfu, nf, md, integer6);
        }
        final LevelChunkSection[] arr11 = cge10.getSections();
        final LevelLightEngine cul12 = this.getLightEngine();
        cul12.enableLightSources(new ChunkPos(integer1, integer2), true);
        for (int integer8 = 0; integer8 < arr11.length; ++integer8) {
            final LevelChunkSection cgf14 = arr11[integer8];
            cul12.updateSectionStatus(SectionPos.of(integer1, integer8, integer2), LevelChunkSection.isEmpty(cgf14));
        }
        this.level.onChunkLoaded(integer1, integer2);
        return cge10;
    }
    
    public void tick(final BooleanSupplier booleanSupplier) {
    }
    
    public void updateViewCenter(final int integer1, final int integer2) {
        this.storage.viewCenterX = integer1;
        this.storage.viewCenterZ = integer2;
    }
    
    public void updateViewRadius(final int integer) {
        final int integer2 = this.storage.chunkRadius;
        final int integer3 = calculateStorageRange(integer);
        if (integer2 != integer3) {
            final Storage a5 = new Storage(integer3);
            a5.viewCenterX = this.storage.viewCenterX;
            a5.viewCenterZ = this.storage.viewCenterZ;
            for (int integer4 = 0; integer4 < this.storage.chunks.length(); ++integer4) {
                final LevelChunk cge7 = (LevelChunk)this.storage.chunks.get(integer4);
                if (cge7 != null) {
                    final ChunkPos bra8 = cge7.getPos();
                    if (a5.inRange(bra8.x, bra8.z)) {
                        a5.replace(a5.getIndex(bra8.x, bra8.z), cge7);
                    }
                }
            }
            this.storage = a5;
        }
    }
    
    private static int calculateStorageRange(final int integer) {
        return Math.max(2, integer) + 3;
    }
    
    @Override
    public String gatherStats() {
        return new StringBuilder().append("Client Chunk Cache: ").append(this.storage.chunks.length()).append(", ").append(this.getLoadedChunksCount()).toString();
    }
    
    public int getLoadedChunksCount() {
        return this.storage.chunkCount;
    }
    
    public void onLightUpdate(final LightLayer bsc, final SectionPos gp) {
        Minecraft.getInstance().levelRenderer.setSectionDirty(gp.x(), gp.y(), gp.z());
    }
    
    @Override
    public boolean isTickingChunk(final BlockPos fx) {
        return this.hasChunk(fx.getX() >> 4, fx.getZ() >> 4);
    }
    
    @Override
    public boolean isEntityTickingChunk(final ChunkPos bra) {
        return this.hasChunk(bra.x, bra.z);
    }
    
    @Override
    public boolean isEntityTickingChunk(final Entity apx) {
        return this.hasChunk(Mth.floor(apx.getX()) >> 4, Mth.floor(apx.getZ()) >> 4);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    final class Storage {
        private final AtomicReferenceArray<LevelChunk> chunks;
        private final int chunkRadius;
        private final int viewRange;
        private volatile int viewCenterX;
        private volatile int viewCenterZ;
        private int chunkCount;
        
        private Storage(final int integer) {
            this.chunkRadius = integer;
            this.viewRange = integer * 2 + 1;
            this.chunks = (AtomicReferenceArray<LevelChunk>)new AtomicReferenceArray(this.viewRange * this.viewRange);
        }
        
        private int getIndex(final int integer1, final int integer2) {
            return Math.floorMod(integer2, this.viewRange) * this.viewRange + Math.floorMod(integer1, this.viewRange);
        }
        
        protected void replace(final int integer, @Nullable final LevelChunk cge) {
            final LevelChunk cge2 = (LevelChunk)this.chunks.getAndSet(integer, cge);
            if (cge2 != null) {
                --this.chunkCount;
                ClientChunkCache.this.level.unload(cge2);
            }
            if (cge != null) {
                ++this.chunkCount;
            }
        }
        
        protected LevelChunk replace(final int integer, final LevelChunk cge2, @Nullable final LevelChunk cge3) {
            if (this.chunks.compareAndSet(integer, cge2, cge3) && cge3 == null) {
                --this.chunkCount;
            }
            ClientChunkCache.this.level.unload(cge2);
            return cge2;
        }
        
        private boolean inRange(final int integer1, final int integer2) {
            return Math.abs(integer1 - this.viewCenterX) <= this.chunkRadius && Math.abs(integer2 - this.viewCenterZ) <= this.chunkRadius;
        }
        
        @Nullable
        protected LevelChunk getChunk(final int integer) {
            return (LevelChunk)this.chunks.get(integer);
        }
    }
}
