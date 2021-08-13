package net.minecraft.server.level;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.stream.Stream;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import java.util.concurrent.CompletionStage;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.BiConsumer;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.world.level.LightLayer;
import it.unimi.dsi.fastutil.shorts.ShortArraySet;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.world.level.lighting.LevelLightEngine;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.world.level.ChunkPos;
import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.world.level.chunk.ChunkStatus;
import java.util.List;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.chunk.ChunkAccess;
import com.mojang.datafixers.util.Either;

public class ChunkHolder {
    public static final Either<ChunkAccess, ChunkLoadingFailure> UNLOADED_CHUNK;
    public static final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> UNLOADED_CHUNK_FUTURE;
    public static final Either<LevelChunk, ChunkLoadingFailure> UNLOADED_LEVEL_CHUNK;
    private static final CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> UNLOADED_LEVEL_CHUNK_FUTURE;
    private static final List<ChunkStatus> CHUNK_STATUSES;
    private static final FullChunkStatus[] FULL_CHUNK_STATUSES;
    private final AtomicReferenceArray<CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>> futures;
    private volatile CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> fullChunkFuture;
    private volatile CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> tickingChunkFuture;
    private volatile CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> entityTickingChunkFuture;
    private CompletableFuture<ChunkAccess> chunkToSave;
    private int oldTicketLevel;
    private int ticketLevel;
    private int queueLevel;
    private final ChunkPos pos;
    private boolean hasChangedSections;
    private final ShortSet[] changedBlocksPerSection;
    private int blockChangedLightSectionFilter;
    private int skyChangedLightSectionFilter;
    private final LevelLightEngine lightEngine;
    private final LevelChangeListener onLevelChange;
    private final PlayerProvider playerProvider;
    private boolean wasAccessibleSinceLastSave;
    private boolean resendLight;
    
    public ChunkHolder(final ChunkPos bra, final int integer, final LevelLightEngine cul, final LevelChangeListener c, final PlayerProvider d) {
        this.futures = (AtomicReferenceArray<CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>>)new AtomicReferenceArray(ChunkHolder.CHUNK_STATUSES.size());
        this.fullChunkFuture = ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE;
        this.tickingChunkFuture = ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE;
        this.entityTickingChunkFuture = ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE;
        this.chunkToSave = (CompletableFuture<ChunkAccess>)CompletableFuture.completedFuture(null);
        this.changedBlocksPerSection = new ShortSet[16];
        this.pos = bra;
        this.lightEngine = cul;
        this.onLevelChange = c;
        this.playerProvider = d;
        this.oldTicketLevel = ChunkMap.MAX_CHUNK_DISTANCE + 1;
        this.ticketLevel = this.oldTicketLevel;
        this.queueLevel = this.oldTicketLevel;
        this.setTicketLevel(integer);
    }
    
    public CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> getFutureIfPresentUnchecked(final ChunkStatus cfx) {
        final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture3 = (CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>)this.futures.get(cfx.getIndex());
        return (completableFuture3 == null) ? ChunkHolder.UNLOADED_CHUNK_FUTURE : completableFuture3;
    }
    
    public CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> getFutureIfPresent(final ChunkStatus cfx) {
        if (getStatus(this.ticketLevel).isOrAfter(cfx)) {
            return this.getFutureIfPresentUnchecked(cfx);
        }
        return ChunkHolder.UNLOADED_CHUNK_FUTURE;
    }
    
    public CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> getTickingChunkFuture() {
        return this.tickingChunkFuture;
    }
    
    public CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> getEntityTickingChunkFuture() {
        return this.entityTickingChunkFuture;
    }
    
    public CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> getFullChunkFuture() {
        return this.fullChunkFuture;
    }
    
    @Nullable
    public LevelChunk getTickingChunk() {
        final CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> completableFuture2 = this.getTickingChunkFuture();
        final Either<LevelChunk, ChunkLoadingFailure> either3 = (Either<LevelChunk, ChunkLoadingFailure>)completableFuture2.getNow(null);
        if (either3 == null) {
            return null;
        }
        return (LevelChunk)either3.left().orElse(null);
    }
    
    @Nullable
    public ChunkStatus getLastAvailableStatus() {
        for (int integer2 = ChunkHolder.CHUNK_STATUSES.size() - 1; integer2 >= 0; --integer2) {
            final ChunkStatus cfx3 = (ChunkStatus)ChunkHolder.CHUNK_STATUSES.get(integer2);
            final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture4 = this.getFutureIfPresentUnchecked(cfx3);
            if (((Either)completableFuture4.getNow(ChunkHolder.UNLOADED_CHUNK)).left().isPresent()) {
                return cfx3;
            }
        }
        return null;
    }
    
    @Nullable
    public ChunkAccess getLastAvailable() {
        for (int integer2 = ChunkHolder.CHUNK_STATUSES.size() - 1; integer2 >= 0; --integer2) {
            final ChunkStatus cfx3 = (ChunkStatus)ChunkHolder.CHUNK_STATUSES.get(integer2);
            final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture4 = this.getFutureIfPresentUnchecked(cfx3);
            if (!completableFuture4.isCompletedExceptionally()) {
                final Optional<ChunkAccess> optional5 = (Optional<ChunkAccess>)((Either)completableFuture4.getNow(ChunkHolder.UNLOADED_CHUNK)).left();
                if (optional5.isPresent()) {
                    return (ChunkAccess)optional5.get();
                }
            }
        }
        return null;
    }
    
    public CompletableFuture<ChunkAccess> getChunkToSave() {
        return this.chunkToSave;
    }
    
    public void blockChanged(final BlockPos fx) {
        final LevelChunk cge3 = this.getTickingChunk();
        if (cge3 == null) {
            return;
        }
        final byte byte4 = (byte)SectionPos.blockToSectionCoord(fx.getY());
        if (this.changedBlocksPerSection[byte4] == null) {
            this.hasChangedSections = true;
            this.changedBlocksPerSection[byte4] = (ShortSet)new ShortArraySet();
        }
        this.changedBlocksPerSection[byte4].add(SectionPos.sectionRelativePos(fx));
    }
    
    public void sectionLightChanged(final LightLayer bsc, final int integer) {
        final LevelChunk cge4 = this.getTickingChunk();
        if (cge4 == null) {
            return;
        }
        cge4.setUnsaved(true);
        if (bsc == LightLayer.SKY) {
            this.skyChangedLightSectionFilter |= 1 << integer + 1;
        }
        else {
            this.blockChangedLightSectionFilter |= 1 << integer + 1;
        }
    }
    
    public void broadcastChanges(final LevelChunk cge) {
        if (!this.hasChangedSections && this.skyChangedLightSectionFilter == 0 && this.blockChangedLightSectionFilter == 0) {
            return;
        }
        final Level bru3 = cge.getLevel();
        int integer4 = 0;
        for (int integer5 = 0; integer5 < this.changedBlocksPerSection.length; ++integer5) {
            integer4 += ((this.changedBlocksPerSection[integer5] != null) ? this.changedBlocksPerSection[integer5].size() : 0);
        }
        this.resendLight |= (integer4 >= 64);
        if (this.skyChangedLightSectionFilter != 0 || this.blockChangedLightSectionFilter != 0) {
            this.broadcast(new ClientboundLightUpdatePacket(cge.getPos(), this.lightEngine, this.skyChangedLightSectionFilter, this.blockChangedLightSectionFilter, true), !this.resendLight);
            this.skyChangedLightSectionFilter = 0;
            this.blockChangedLightSectionFilter = 0;
        }
        for (int integer5 = 0; integer5 < this.changedBlocksPerSection.length; ++integer5) {
            final ShortSet shortSet6 = this.changedBlocksPerSection[integer5];
            if (shortSet6 != null) {
                final SectionPos gp7 = SectionPos.of(cge.getPos(), integer5);
                if (shortSet6.size() == 1) {
                    final BlockPos fx8 = gp7.relativeToBlockPos(shortSet6.iterator().nextShort());
                    final BlockState cee9 = bru3.getBlockState(fx8);
                    this.broadcast(new ClientboundBlockUpdatePacket(fx8, cee9), false);
                    this.broadcastBlockEntityIfNeeded(bru3, fx8, cee9);
                }
                else {
                    final LevelChunkSection cgf8 = cge.getSections()[gp7.getY()];
                    final ClientboundSectionBlocksUpdatePacket qr9 = new ClientboundSectionBlocksUpdatePacket(gp7, shortSet6, cgf8, this.resendLight);
                    this.broadcast(qr9, false);
                    qr9.runUpdates((BiConsumer<BlockPos, BlockState>)((fx, cee) -> this.broadcastBlockEntityIfNeeded(bru3, fx, cee)));
                }
                this.changedBlocksPerSection[integer5] = null;
            }
        }
        this.hasChangedSections = false;
    }
    
    private void broadcastBlockEntityIfNeeded(final Level bru, final BlockPos fx, final BlockState cee) {
        if (cee.getBlock().isEntityBlock()) {
            this.broadcastBlockEntity(bru, fx);
        }
    }
    
    private void broadcastBlockEntity(final Level bru, final BlockPos fx) {
        final BlockEntity ccg4 = bru.getBlockEntity(fx);
        if (ccg4 != null) {
            final ClientboundBlockEntityDataPacket ow5 = ccg4.getUpdatePacket();
            if (ow5 != null) {
                this.broadcast(ow5, false);
            }
        }
    }
    
    private void broadcast(final Packet<?> oj, final boolean boolean2) {
        this.playerProvider.getPlayers(this.pos, boolean2).forEach(aah -> aah.connection.send(oj));
    }
    
    public CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> getOrScheduleFuture(final ChunkStatus cfx, final ChunkMap zs) {
        final int integer4 = cfx.getIndex();
        final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture5 = (CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>)this.futures.get(integer4);
        if (completableFuture5 != null) {
            final Either<ChunkAccess, ChunkLoadingFailure> either6 = (Either<ChunkAccess, ChunkLoadingFailure>)completableFuture5.getNow(null);
            if (either6 == null || either6.left().isPresent()) {
                return completableFuture5;
            }
        }
        if (getStatus(this.ticketLevel).isOrAfter(cfx)) {
            final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture6 = zs.schedule(this, cfx);
            this.updateChunkToSave(completableFuture6);
            this.futures.set(integer4, completableFuture6);
            return completableFuture6;
        }
        return (completableFuture5 == null) ? ChunkHolder.UNLOADED_CHUNK_FUTURE : completableFuture5;
    }
    
    private void updateChunkToSave(final CompletableFuture<? extends Either<? extends ChunkAccess, ChunkLoadingFailure>> completableFuture) {
        this.chunkToSave = (CompletableFuture<ChunkAccess>)this.chunkToSave.thenCombine((CompletionStage)completableFuture, (cft, either) -> (ChunkAccess)either.map(cft -> cft, a -> cft));
    }
    
    public FullChunkStatus getFullStatus() {
        return getFullChunkStatus(this.ticketLevel);
    }
    
    public ChunkPos getPos() {
        return this.pos;
    }
    
    public int getTicketLevel() {
        return this.ticketLevel;
    }
    
    public int getQueueLevel() {
        return this.queueLevel;
    }
    
    private void setQueueLevel(final int integer) {
        this.queueLevel = integer;
    }
    
    public void setTicketLevel(final int integer) {
        this.ticketLevel = integer;
    }
    
    protected void updateFutures(final ChunkMap zs) {
        final ChunkStatus cfx3 = getStatus(this.oldTicketLevel);
        final ChunkStatus cfx4 = getStatus(this.ticketLevel);
        final boolean boolean5 = this.oldTicketLevel <= ChunkMap.MAX_CHUNK_DISTANCE;
        final boolean boolean6 = this.ticketLevel <= ChunkMap.MAX_CHUNK_DISTANCE;
        final FullChunkStatus b7 = getFullChunkStatus(this.oldTicketLevel);
        final FullChunkStatus b8 = getFullChunkStatus(this.ticketLevel);
        if (boolean5) {
            final Either<ChunkAccess, ChunkLoadingFailure> either9 = (Either<ChunkAccess, ChunkLoadingFailure>)Either.right(new ChunkLoadingFailure() {
                public String toString() {
                    return "Unloaded ticket level " + ChunkHolder.this.pos.toString();
                }
            });
            for (int integer10 = boolean6 ? (cfx4.getIndex() + 1) : 0; integer10 <= cfx3.getIndex(); ++integer10) {
                final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture11 = (CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>)this.futures.get(integer10);
                if (completableFuture11 != null) {
                    completableFuture11.complete(either9);
                }
                else {
                    this.futures.set(integer10, CompletableFuture.completedFuture((Object)either9));
                }
            }
        }
        final boolean boolean7 = b7.isOrAfter(FullChunkStatus.BORDER);
        final boolean boolean8 = b8.isOrAfter(FullChunkStatus.BORDER);
        this.wasAccessibleSinceLastSave |= boolean8;
        if (!boolean7 && boolean8) {
            this.updateChunkToSave(this.fullChunkFuture = zs.unpackTicks(this));
        }
        if (boolean7 && !boolean8) {
            final CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> completableFuture12 = this.fullChunkFuture;
            this.fullChunkFuture = ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE;
            this.updateChunkToSave(completableFuture12.thenApply(either -> either.ifLeft(zs::packTicks)));
        }
        final boolean boolean9 = b7.isOrAfter(FullChunkStatus.TICKING);
        final boolean boolean10 = b8.isOrAfter(FullChunkStatus.TICKING);
        if (!boolean9 && boolean10) {
            this.updateChunkToSave(this.tickingChunkFuture = zs.postProcess(this));
        }
        if (boolean9 && !boolean10) {
            this.tickingChunkFuture.complete(ChunkHolder.UNLOADED_LEVEL_CHUNK);
            this.tickingChunkFuture = ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE;
        }
        final boolean boolean11 = b7.isOrAfter(FullChunkStatus.ENTITY_TICKING);
        final boolean boolean12 = b8.isOrAfter(FullChunkStatus.ENTITY_TICKING);
        if (!boolean11 && boolean12) {
            if (this.entityTickingChunkFuture != ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE) {
                throw Util.<IllegalStateException>pauseInIde(new IllegalStateException());
            }
            this.updateChunkToSave(this.entityTickingChunkFuture = zs.getEntityTickingRangeFuture(this.pos));
        }
        if (boolean11 && !boolean12) {
            this.entityTickingChunkFuture.complete(ChunkHolder.UNLOADED_LEVEL_CHUNK);
            this.entityTickingChunkFuture = ChunkHolder.UNLOADED_LEVEL_CHUNK_FUTURE;
        }
        this.onLevelChange.onLevelChange(this.pos, this::getQueueLevel, this.ticketLevel, this::setQueueLevel);
        this.oldTicketLevel = this.ticketLevel;
    }
    
    public static ChunkStatus getStatus(final int integer) {
        if (integer < 33) {
            return ChunkStatus.FULL;
        }
        return ChunkStatus.getStatus(integer - 33);
    }
    
    public static FullChunkStatus getFullChunkStatus(final int integer) {
        return ChunkHolder.FULL_CHUNK_STATUSES[Mth.clamp(33 - integer + 1, 0, ChunkHolder.FULL_CHUNK_STATUSES.length - 1)];
    }
    
    public boolean wasAccessibleSinceLastSave() {
        return this.wasAccessibleSinceLastSave;
    }
    
    public void refreshAccessibility() {
        this.wasAccessibleSinceLastSave = getFullChunkStatus(this.ticketLevel).isOrAfter(FullChunkStatus.BORDER);
    }
    
    public void replaceProtoChunk(final ImposterProtoChunk cgd) {
        for (int integer3 = 0; integer3 < this.futures.length(); ++integer3) {
            final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture4 = (CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>)this.futures.get(integer3);
            if (completableFuture4 != null) {
                final Optional<ChunkAccess> optional5 = (Optional<ChunkAccess>)((Either)completableFuture4.getNow(ChunkHolder.UNLOADED_CHUNK)).left();
                if (optional5.isPresent()) {
                    if (optional5.get() instanceof ProtoChunk) {
                        this.futures.set(integer3, CompletableFuture.completedFuture((Object)Either.left((Object)cgd)));
                    }
                }
            }
        }
        this.updateChunkToSave(CompletableFuture.completedFuture(Either.left((Object)cgd.getWrapped())));
    }
    
    static {
        UNLOADED_CHUNK = Either.right(ChunkLoadingFailure.UNLOADED);
        UNLOADED_CHUNK_FUTURE = CompletableFuture.completedFuture(ChunkHolder.UNLOADED_CHUNK);
        UNLOADED_LEVEL_CHUNK = Either.right(ChunkLoadingFailure.UNLOADED);
        UNLOADED_LEVEL_CHUNK_FUTURE = CompletableFuture.completedFuture(ChunkHolder.UNLOADED_LEVEL_CHUNK);
        CHUNK_STATUSES = ChunkStatus.getStatusList();
        FULL_CHUNK_STATUSES = FullChunkStatus.values();
    }
    
    public enum FullChunkStatus {
        INACCESSIBLE, 
        BORDER, 
        TICKING, 
        ENTITY_TICKING;
        
        public boolean isOrAfter(final FullChunkStatus b) {
            return this.ordinal() >= b.ordinal();
        }
    }
    
    public interface ChunkLoadingFailure {
        public static final ChunkLoadingFailure UNLOADED = new ChunkLoadingFailure() {
            public String toString() {
                return "UNLOADED";
            }
        };
    }
    
    public interface PlayerProvider {
        Stream<ServerPlayer> getPlayers(final ChunkPos bra, final boolean boolean2);
    }
    
    public interface LevelChangeListener {
        void onLevelChange(final ChunkPos bra, final IntSupplier intSupplier, final int integer, final IntConsumer intConsumer);
    }
}
