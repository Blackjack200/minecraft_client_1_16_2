package net.minecraft.server.level;

import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import it.unimi.dsi.fastutil.longs.LongIterator;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import com.google.common.collect.ImmutableList;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import com.google.common.collect.Sets;
import java.util.concurrent.Executor;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.thread.ProcessorHandle;
import java.util.Set;
import net.minecraft.util.SortedArraySet;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.apache.logging.log4j.Logger;

public abstract class DistanceManager {
    private static final Logger LOGGER;
    private static final int PLAYER_TICKET_LEVEL;
    private final Long2ObjectMap<ObjectSet<ServerPlayer>> playersPerChunk;
    private final Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets;
    private final ChunkTicketTracker ticketTracker;
    private final FixedPlayerDistanceChunkTracker naturalSpawnChunkCounter;
    private final PlayerTicketTracker playerTicketManager;
    private final Set<ChunkHolder> chunksToUpdateFutures;
    private final ChunkTaskPriorityQueueSorter ticketThrottler;
    private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> ticketThrottlerInput;
    private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Release> ticketThrottlerReleaser;
    private final LongSet ticketsToRelease;
    private final Executor mainThreadExecutor;
    private long ticketTickCounter;
    
    protected DistanceManager(final Executor executor1, final Executor executor2) {
        this.playersPerChunk = (Long2ObjectMap<ObjectSet<ServerPlayer>>)new Long2ObjectOpenHashMap();
        this.tickets = (Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>>)new Long2ObjectOpenHashMap();
        this.ticketTracker = new ChunkTicketTracker();
        this.naturalSpawnChunkCounter = new FixedPlayerDistanceChunkTracker(8);
        this.playerTicketManager = new PlayerTicketTracker(33);
        this.chunksToUpdateFutures = (Set<ChunkHolder>)Sets.newHashSet();
        this.ticketsToRelease = (LongSet)new LongOpenHashSet();
        final ProcessorHandle<Runnable> aoa4 = ProcessorHandle.<Runnable>of("player ticket throttler", (java.util.function.Consumer<Runnable>)executor2::execute);
        final ChunkTaskPriorityQueueSorter zu5 = new ChunkTaskPriorityQueueSorter((List<ProcessorHandle<?>>)ImmutableList.of(aoa4), executor1, 4);
        this.ticketThrottler = zu5;
        this.ticketThrottlerInput = zu5.<Runnable>getProcessor(aoa4, true);
        this.ticketThrottlerReleaser = zu5.getReleaseProcessor(aoa4);
        this.mainThreadExecutor = executor2;
    }
    
    protected void purgeStaleTickets() {
        ++this.ticketTickCounter;
        final ObjectIterator<Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>>> objectIterator2 = (ObjectIterator<Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>>>)this.tickets.long2ObjectEntrySet().fastIterator();
        while (objectIterator2.hasNext()) {
            final Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>> entry3 = (Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>>)objectIterator2.next();
            if (((SortedArraySet)entry3.getValue()).removeIf(aak -> aak.timedOut(this.ticketTickCounter))) {
                this.ticketTracker.update(entry3.getLongKey(), getTicketLevelAt((SortedArraySet<Ticket<?>>)entry3.getValue()), false);
            }
            if (((SortedArraySet)entry3.getValue()).isEmpty()) {
                objectIterator2.remove();
            }
        }
    }
    
    private static int getTicketLevelAt(final SortedArraySet<Ticket<?>> afn) {
        return afn.isEmpty() ? (ChunkMap.MAX_CHUNK_DISTANCE + 1) : afn.first().getTicketLevel();
    }
    
    protected abstract boolean isChunkToRemove(final long long1);
    
    @Nullable
    protected abstract ChunkHolder getChunk(final long long1);
    
    @Nullable
    protected abstract ChunkHolder updateChunkScheduling(final long long1, final int integer2, @Nullable final ChunkHolder zr, final int integer4);
    
    public boolean runAllUpdates(final ChunkMap zs) {
        this.naturalSpawnChunkCounter.runAllUpdates();
        this.playerTicketManager.runAllUpdates();
        final int integer3 = Integer.MAX_VALUE - this.ticketTracker.runDistanceUpdates(Integer.MAX_VALUE);
        final boolean boolean4 = integer3 != 0;
        if (boolean4) {}
        if (!this.chunksToUpdateFutures.isEmpty()) {
            this.chunksToUpdateFutures.forEach(zr -> zr.updateFutures(zs));
            this.chunksToUpdateFutures.clear();
            return true;
        }
        if (!this.ticketsToRelease.isEmpty()) {
            final LongIterator longIterator5 = this.ticketsToRelease.iterator();
            while (longIterator5.hasNext()) {
                final long long6 = longIterator5.nextLong();
                if (this.getTickets(long6).stream().anyMatch(aak -> aak.getType() == TicketType.PLAYER)) {
                    final ChunkHolder zr8 = zs.getUpdatingChunkIfPresent(long6);
                    if (zr8 == null) {
                        throw new IllegalStateException();
                    }
                    final CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> completableFuture9 = zr8.getEntityTickingChunkFuture();
                    completableFuture9.thenAccept(either -> this.mainThreadExecutor.execute(() -> this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> {}, long6, false))));
                }
            }
            this.ticketsToRelease.clear();
        }
        return boolean4;
    }
    
    private void addTicket(final long long1, final Ticket<?> aak) {
        final SortedArraySet<Ticket<?>> afn5 = this.getTickets(long1);
        final int integer6 = getTicketLevelAt(afn5);
        final Ticket<?> aak2 = afn5.addOrGet(aak);
        aak2.setCreatedTick(this.ticketTickCounter);
        if (aak.getTicketLevel() < integer6) {
            this.ticketTracker.update(long1, aak.getTicketLevel(), true);
        }
    }
    
    private void removeTicket(final long long1, final Ticket<?> aak) {
        final SortedArraySet<Ticket<?>> afn5 = this.getTickets(long1);
        if (afn5.remove(aak)) {}
        if (afn5.isEmpty()) {
            this.tickets.remove(long1);
        }
        this.ticketTracker.update(long1, getTicketLevelAt(afn5), false);
    }
    
    public <T> void addTicket(final TicketType<T> aal, final ChunkPos bra, final int integer, final T object) {
        this.addTicket(bra.toLong(), new Ticket<>(aal, integer, object));
    }
    
    public <T> void removeTicket(final TicketType<T> aal, final ChunkPos bra, final int integer, final T object) {
        final Ticket<T> aak6 = new Ticket<T>(aal, integer, object);
        this.removeTicket(bra.toLong(), aak6);
    }
    
    public <T> void addRegionTicket(final TicketType<T> aal, final ChunkPos bra, final int integer, final T object) {
        this.addTicket(bra.toLong(), new Ticket<>(aal, 33 - integer, object));
    }
    
    public <T> void removeRegionTicket(final TicketType<T> aal, final ChunkPos bra, final int integer, final T object) {
        final Ticket<T> aak6 = new Ticket<T>(aal, 33 - integer, object);
        this.removeTicket(bra.toLong(), aak6);
    }
    
    private SortedArraySet<Ticket<?>> getTickets(final long long1) {
        return (SortedArraySet<Ticket<?>>)this.tickets.computeIfAbsent(long1, long1 -> SortedArraySet.<Comparable>create(4));
    }
    
    protected void updateChunkForced(final ChunkPos bra, final boolean boolean2) {
        final Ticket<ChunkPos> aak4 = new Ticket<ChunkPos>(TicketType.FORCED, 31, bra);
        if (boolean2) {
            this.addTicket(bra.toLong(), aak4);
        }
        else {
            this.removeTicket(bra.toLong(), aak4);
        }
    }
    
    public void addPlayer(final SectionPos gp, final ServerPlayer aah) {
        final long long4 = gp.chunk().toLong();
        ((ObjectSet)this.playersPerChunk.computeIfAbsent(long4, long1 -> new ObjectOpenHashSet())).add(aah);
        this.naturalSpawnChunkCounter.update(long4, 0, true);
        this.playerTicketManager.update(long4, 0, true);
    }
    
    public void removePlayer(final SectionPos gp, final ServerPlayer aah) {
        final long long4 = gp.chunk().toLong();
        final ObjectSet<ServerPlayer> objectSet6 = (ObjectSet<ServerPlayer>)this.playersPerChunk.get(long4);
        objectSet6.remove(aah);
        if (objectSet6.isEmpty()) {
            this.playersPerChunk.remove(long4);
            this.naturalSpawnChunkCounter.update(long4, Integer.MAX_VALUE, false);
            this.playerTicketManager.update(long4, Integer.MAX_VALUE, false);
        }
    }
    
    protected String getTicketDebugString(final long long1) {
        final SortedArraySet<Ticket<?>> afn4 = (SortedArraySet<Ticket<?>>)this.tickets.get(long1);
        String string5;
        if (afn4 == null || afn4.isEmpty()) {
            string5 = "no_ticket";
        }
        else {
            string5 = afn4.first().toString();
        }
        return string5;
    }
    
    protected void updatePlayerTickets(final int integer) {
        this.playerTicketManager.updateViewDistance(integer);
    }
    
    public int getNaturalSpawnChunkCount() {
        this.naturalSpawnChunkCounter.runAllUpdates();
        return this.naturalSpawnChunkCounter.chunks.size();
    }
    
    public boolean hasPlayersNearby(final long long1) {
        this.naturalSpawnChunkCounter.runAllUpdates();
        return this.naturalSpawnChunkCounter.chunks.containsKey(long1);
    }
    
    public String getDebugStatus() {
        return this.ticketThrottler.getDebugStatus();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PLAYER_TICKET_LEVEL = 33 + ChunkStatus.getDistance(ChunkStatus.FULL) - 2;
    }
    
    class FixedPlayerDistanceChunkTracker extends ChunkTracker {
        protected final Long2ByteMap chunks;
        protected final int maxDistance;
        
        protected FixedPlayerDistanceChunkTracker(final int integer) {
            super(integer + 2, 16, 256);
            this.chunks = (Long2ByteMap)new Long2ByteOpenHashMap();
            this.maxDistance = integer;
            this.chunks.defaultReturnValue((byte)(integer + 2));
        }
        
        @Override
        protected int getLevel(final long long1) {
            return this.chunks.get(long1);
        }
        
        @Override
        protected void setLevel(final long long1, final int integer) {
            byte byte5;
            if (integer > this.maxDistance) {
                byte5 = this.chunks.remove(long1);
            }
            else {
                byte5 = this.chunks.put(long1, (byte)integer);
            }
            this.onLevelChange(long1, byte5, integer);
        }
        
        protected void onLevelChange(final long long1, final int integer2, final int integer3) {
        }
        
        @Override
        protected int getLevelFromSource(final long long1) {
            return this.havePlayer(long1) ? 0 : Integer.MAX_VALUE;
        }
        
        private boolean havePlayer(final long long1) {
            final ObjectSet<ServerPlayer> objectSet4 = (ObjectSet<ServerPlayer>)DistanceManager.this.playersPerChunk.get(long1);
            return objectSet4 != null && !objectSet4.isEmpty();
        }
        
        public void runAllUpdates() {
            this.runUpdates(Integer.MAX_VALUE);
        }
    }
    
    class PlayerTicketTracker extends FixedPlayerDistanceChunkTracker {
        private int viewDistance;
        private final Long2IntMap queueLevels;
        private final LongSet toUpdate;
        
        protected PlayerTicketTracker(final int integer) {
            super(integer);
            this.queueLevels = Long2IntMaps.synchronize((Long2IntMap)new Long2IntOpenHashMap());
            this.toUpdate = (LongSet)new LongOpenHashSet();
            this.viewDistance = 0;
            this.queueLevels.defaultReturnValue(integer + 2);
        }
        
        @Override
        protected void onLevelChange(final long long1, final int integer2, final int integer3) {
            this.toUpdate.add(long1);
        }
        
        public void updateViewDistance(final int integer) {
            for (final Long2ByteMap.Entry entry4 : this.chunks.long2ByteEntrySet()) {
                final byte byte5 = entry4.getByteValue();
                final long long6 = entry4.getLongKey();
                this.onLevelChange(long6, byte5, this.haveTicketFor(byte5), byte5 <= integer - 2);
            }
            this.viewDistance = integer;
        }
        
        private void onLevelChange(final long long1, final int integer, final boolean boolean3, final boolean boolean4) {
            if (boolean3 != boolean4) {
                final Ticket<?> aak7 = new Ticket<>(TicketType.PLAYER, DistanceManager.PLAYER_TICKET_LEVEL, new ChunkPos(long1));
                if (boolean4) {
                    DistanceManager.this.ticketThrottlerInput.tell(ChunkTaskPriorityQueueSorter.message(() -> DistanceManager.this.mainThreadExecutor.execute(() -> {
                        if (this.haveTicketFor(this.getLevel(long1))) {
                            DistanceManager.this.addTicket(long1, aak7);
                            DistanceManager.this.ticketsToRelease.add(long1);
                        }
                        else {
                            DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> {}, long1, false));
                        }
                    }), long1, () -> integer));
                }
                else {
                    DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> DistanceManager.this.mainThreadExecutor.execute(() -> DistanceManager.this.removeTicket(long1, aak7)), long1, true));
                }
            }
        }
        
        @Override
        public void runAllUpdates() {
            super.runAllUpdates();
            if (!this.toUpdate.isEmpty()) {
                final LongIterator longIterator2 = this.toUpdate.iterator();
                while (longIterator2.hasNext()) {
                    final long long3 = longIterator2.nextLong();
                    final int integer5 = this.queueLevels.get(long3);
                    final int integer6 = this.getLevel(long3);
                    if (integer5 != integer6) {
                        DistanceManager.this.ticketThrottler.onLevelChange(new ChunkPos(long3), () -> this.queueLevels.get(long3), integer6, integer -> {
                            if (integer >= this.queueLevels.defaultReturnValue()) {
                                this.queueLevels.remove(long3);
                            }
                            else {
                                this.queueLevels.put(long3, integer);
                            }
                        });
                        this.onLevelChange(long3, integer6, this.haveTicketFor(integer5), this.haveTicketFor(integer6));
                    }
                }
                this.toUpdate.clear();
            }
        }
        
        private boolean haveTicketFor(final int integer) {
            return integer <= this.viewDistance - 2;
        }
    }
    
    class ChunkTicketTracker extends ChunkTracker {
        public ChunkTicketTracker() {
            super(ChunkMap.MAX_CHUNK_DISTANCE + 2, 16, 256);
        }
        
        @Override
        protected int getLevelFromSource(final long long1) {
            final SortedArraySet<Ticket<?>> afn4 = (SortedArraySet<Ticket<?>>)DistanceManager.this.tickets.get(long1);
            if (afn4 == null) {
                return Integer.MAX_VALUE;
            }
            if (afn4.isEmpty()) {
                return Integer.MAX_VALUE;
            }
            return afn4.first().getTicketLevel();
        }
        
        @Override
        protected int getLevel(final long long1) {
            if (!DistanceManager.this.isChunkToRemove(long1)) {
                final ChunkHolder zr4 = DistanceManager.this.getChunk(long1);
                if (zr4 != null) {
                    return zr4.getTicketLevel();
                }
            }
            return ChunkMap.MAX_CHUNK_DISTANCE + 1;
        }
        
        @Override
        protected void setLevel(final long long1, final int integer) {
            ChunkHolder zr5 = DistanceManager.this.getChunk(long1);
            final int integer2 = (zr5 == null) ? (ChunkMap.MAX_CHUNK_DISTANCE + 1) : zr5.getTicketLevel();
            if (integer2 == integer) {
                return;
            }
            zr5 = DistanceManager.this.updateChunkScheduling(long1, integer, zr5, integer2);
            if (zr5 != null) {
                DistanceManager.this.chunksToUpdateFutures.add(zr5);
            }
        }
        
        public int runDistanceUpdates(final int integer) {
            return this.runUpdates(integer);
        }
    }
}
