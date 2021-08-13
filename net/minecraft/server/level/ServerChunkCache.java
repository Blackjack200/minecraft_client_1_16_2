package net.minecraft.server.level;

import java.util.concurrent.CompletionStage;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import com.google.common.annotations.VisibleForTesting;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.LevelData;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.world.level.GameRules;
import java.util.function.BooleanSupplier;
import java.io.IOException;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import java.util.Optional;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.Util;
import java.util.Arrays;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.util.profiling.ProfilerFiller;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.ChunkPos;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.util.thread.BlockableEventLoop;
import java.io.File;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;
import net.minecraft.server.level.progress.ChunkProgressListener;
import java.util.concurrent.Executor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import com.mojang.datafixers.DataFixer;
import net.minecraft.world.level.storage.LevelStorageSource;
import javax.annotation.Nullable;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import java.util.List;
import net.minecraft.world.level.chunk.ChunkSource;

public class ServerChunkCache extends ChunkSource {
    private static final List<ChunkStatus> CHUNK_STATUSES;
    private final DistanceManager distanceManager;
    private final ChunkGenerator generator;
    private final ServerLevel level;
    private final Thread mainThread;
    private final ThreadedLevelLightEngine lightEngine;
    private final MainThreadExecutor mainThreadProcessor;
    public final ChunkMap chunkMap;
    private final DimensionDataStorage dataStorage;
    private long lastInhabitedUpdate;
    private boolean spawnEnemies;
    private boolean spawnFriendlies;
    private final long[] lastChunkPos;
    private final ChunkStatus[] lastChunkStatus;
    private final ChunkAccess[] lastChunk;
    @Nullable
    private NaturalSpawner.SpawnState lastSpawnState;
    
    public ServerChunkCache(final ServerLevel aag, final LevelStorageSource.LevelStorageAccess a, final DataFixer dataFixer, final StructureManager cst, final Executor executor, final ChunkGenerator cfv, final int integer, final boolean boolean8, final ChunkProgressListener aap, final Supplier<DimensionDataStorage> supplier) {
        this.spawnEnemies = true;
        this.spawnFriendlies = true;
        this.lastChunkPos = new long[4];
        this.lastChunkStatus = new ChunkStatus[4];
        this.lastChunk = new ChunkAccess[4];
        this.level = aag;
        this.mainThreadProcessor = new MainThreadExecutor((Level)aag);
        this.generator = cfv;
        this.mainThread = Thread.currentThread();
        final File file12 = a.getDimensionPath(aag.dimension());
        final File file13 = new File(file12, "data");
        file13.mkdirs();
        this.dataStorage = new DimensionDataStorage(file13, dataFixer);
        this.chunkMap = new ChunkMap(aag, a, dataFixer, cst, executor, this.mainThreadProcessor, this, this.getGenerator(), aap, supplier, integer, boolean8);
        this.lightEngine = this.chunkMap.getLightEngine();
        this.distanceManager = this.chunkMap.getDistanceManager();
        this.clearCache();
    }
    
    @Override
    public ThreadedLevelLightEngine getLightEngine() {
        return this.lightEngine;
    }
    
    @Nullable
    private ChunkHolder getVisibleChunkIfPresent(final long long1) {
        return this.chunkMap.getVisibleChunkIfPresent(long1);
    }
    
    public int getTickingGenerated() {
        return this.chunkMap.getTickingGenerated();
    }
    
    private void storeInCache(final long long1, final ChunkAccess cft, final ChunkStatus cfx) {
        for (int integer6 = 3; integer6 > 0; --integer6) {
            this.lastChunkPos[integer6] = this.lastChunkPos[integer6 - 1];
            this.lastChunkStatus[integer6] = this.lastChunkStatus[integer6 - 1];
            this.lastChunk[integer6] = this.lastChunk[integer6 - 1];
        }
        this.lastChunkPos[0] = long1;
        this.lastChunkStatus[0] = cfx;
        this.lastChunk[0] = cft;
    }
    
    @Nullable
    @Override
    public ChunkAccess getChunk(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4) {
        if (Thread.currentThread() != this.mainThread) {
            return (ChunkAccess)CompletableFuture.supplyAsync(() -> this.getChunk(integer1, integer2, cfx, boolean4), (Executor)this.mainThreadProcessor).join();
        }
        final ProfilerFiller ant6 = this.level.getProfiler();
        ant6.incrementCounter("getChunk");
        final long long7 = ChunkPos.asLong(integer1, integer2);
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            if (long7 == this.lastChunkPos[integer3] && cfx == this.lastChunkStatus[integer3]) {
                final ChunkAccess cft10 = this.lastChunk[integer3];
                if (cft10 != null || !boolean4) {
                    return cft10;
                }
            }
        }
        ant6.incrementCounter("getChunkCacheMiss");
        final CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> completableFuture9 = this.getChunkFutureMainThread(integer1, integer2, cfx, boolean4);
        this.mainThreadProcessor.managedBlock(completableFuture9::isDone);
        final ChunkAccess cft10 = (ChunkAccess)((Either)completableFuture9.join()).map(cft -> cft, a -> {
            if (boolean4) {
                throw Util.<IllegalStateException>pauseInIde(new IllegalStateException(new StringBuilder().append("Chunk not there when requested: ").append(a).toString()));
            }
            return null;
        });
        this.storeInCache(long7, cft10, cfx);
        return cft10;
    }
    
    @Nullable
    @Override
    public LevelChunk getChunkNow(final int integer1, final int integer2) {
        if (Thread.currentThread() != this.mainThread) {
            return null;
        }
        this.level.getProfiler().incrementCounter("getChunkNow");
        final long long4 = ChunkPos.asLong(integer1, integer2);
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            if (long4 == this.lastChunkPos[integer3] && this.lastChunkStatus[integer3] == ChunkStatus.FULL) {
                final ChunkAccess cft7 = this.lastChunk[integer3];
                return (cft7 instanceof LevelChunk) ? ((LevelChunk)cft7) : null;
            }
        }
        final ChunkHolder zr6 = this.getVisibleChunkIfPresent(long4);
        if (zr6 == null) {
            return null;
        }
        final Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure> either7 = (Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>)zr6.getFutureIfPresent(ChunkStatus.FULL).getNow(null);
        if (either7 == null) {
            return null;
        }
        final ChunkAccess cft8 = (ChunkAccess)either7.left().orElse(null);
        if (cft8 != null) {
            this.storeInCache(long4, cft8, ChunkStatus.FULL);
            if (cft8 instanceof LevelChunk) {
                return (LevelChunk)cft8;
            }
        }
        return null;
    }
    
    private void clearCache() {
        Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
        Arrays.fill((Object[])this.lastChunkStatus, null);
        Arrays.fill((Object[])this.lastChunk, null);
    }
    
    public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getChunkFuture(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4) {
        final boolean boolean5 = Thread.currentThread() == this.mainThread;
        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> completableFuture7;
        if (boolean5) {
            completableFuture7 = this.getChunkFutureMainThread(integer1, integer2, cfx, boolean4);
            this.mainThreadProcessor.managedBlock(completableFuture7::isDone);
        }
        else {
            completableFuture7 = (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)CompletableFuture.supplyAsync(() -> this.getChunkFutureMainThread(integer1, integer2, cfx, boolean4), (Executor)this.mainThreadProcessor).thenCompose(completableFuture -> completableFuture);
        }
        return completableFuture7;
    }
    
    private CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getChunkFutureMainThread(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4) {
        final ChunkPos bra6 = new ChunkPos(integer1, integer2);
        final long long7 = bra6.toLong();
        final int integer3 = 33 + ChunkStatus.getDistance(cfx);
        ChunkHolder zr10 = this.getVisibleChunkIfPresent(long7);
        if (boolean4) {
            this.distanceManager.<ChunkPos>addTicket(TicketType.UNKNOWN, bra6, integer3, bra6);
            if (this.chunkAbsent(zr10, integer3)) {
                final ProfilerFiller ant11 = this.level.getProfiler();
                ant11.push("chunkLoad");
                this.runDistanceManagerUpdates();
                zr10 = this.getVisibleChunkIfPresent(long7);
                ant11.pop();
                if (this.chunkAbsent(zr10, integer3)) {
                    throw Util.<IllegalStateException>pauseInIde(new IllegalStateException("No chunk holder after ticket has been added"));
                }
            }
        }
        if (this.chunkAbsent(zr10, integer3)) {
            return ChunkHolder.UNLOADED_CHUNK_FUTURE;
        }
        return zr10.getOrScheduleFuture(cfx, this.chunkMap);
    }
    
    private boolean chunkAbsent(@Nullable final ChunkHolder zr, final int integer) {
        return zr == null || zr.getTicketLevel() > integer;
    }
    
    @Override
    public boolean hasChunk(final int integer1, final int integer2) {
        final ChunkHolder zr4 = this.getVisibleChunkIfPresent(new ChunkPos(integer1, integer2).toLong());
        final int integer3 = 33 + ChunkStatus.getDistance(ChunkStatus.FULL);
        return !this.chunkAbsent(zr4, integer3);
    }
    
    @Override
    public BlockGetter getChunkForLighting(final int integer1, final int integer2) {
        final long long4 = ChunkPos.asLong(integer1, integer2);
        final ChunkHolder zr6 = this.getVisibleChunkIfPresent(long4);
        if (zr6 == null) {
            return null;
        }
        int integer3 = ServerChunkCache.CHUNK_STATUSES.size() - 1;
        while (true) {
            final ChunkStatus cfx8 = (ChunkStatus)ServerChunkCache.CHUNK_STATUSES.get(integer3);
            final Optional<ChunkAccess> optional9 = (Optional<ChunkAccess>)((Either)zr6.getFutureIfPresentUnchecked(cfx8).getNow(ChunkHolder.UNLOADED_CHUNK)).left();
            if (optional9.isPresent()) {
                return (BlockGetter)optional9.get();
            }
            if (cfx8 == ChunkStatus.LIGHT.getParent()) {
                return null;
            }
            --integer3;
        }
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public boolean pollTask() {
        return this.mainThreadProcessor.pollTask();
    }
    
    private boolean runDistanceManagerUpdates() {
        final boolean boolean2 = this.distanceManager.runAllUpdates(this.chunkMap);
        final boolean boolean3 = this.chunkMap.promoteChunkMap();
        if (boolean2 || boolean3) {
            this.clearCache();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isEntityTickingChunk(final Entity apx) {
        final long long3 = ChunkPos.asLong(Mth.floor(apx.getX()) >> 4, Mth.floor(apx.getZ()) >> 4);
        return this.checkChunkFuture(long3, (Function<ChunkHolder, CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>>>)ChunkHolder::getEntityTickingChunkFuture);
    }
    
    @Override
    public boolean isEntityTickingChunk(final ChunkPos bra) {
        return this.checkChunkFuture(bra.toLong(), (Function<ChunkHolder, CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>>>)ChunkHolder::getEntityTickingChunkFuture);
    }
    
    @Override
    public boolean isTickingChunk(final BlockPos fx) {
        final long long3 = ChunkPos.asLong(fx.getX() >> 4, fx.getZ() >> 4);
        return this.checkChunkFuture(long3, (Function<ChunkHolder, CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>>>)ChunkHolder::getTickingChunkFuture);
    }
    
    private boolean checkChunkFuture(final long long1, final Function<ChunkHolder, CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>>> function) {
        final ChunkHolder zr5 = this.getVisibleChunkIfPresent(long1);
        if (zr5 == null) {
            return false;
        }
        final Either<LevelChunk, ChunkHolder.ChunkLoadingFailure> either6 = (Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>)((CompletableFuture)function.apply(zr5)).getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK);
        return either6.left().isPresent();
    }
    
    public void save(final boolean boolean1) {
        this.runDistanceManagerUpdates();
        this.chunkMap.saveAllChunks(boolean1);
    }
    
    @Override
    public void close() throws IOException {
        this.save(true);
        this.lightEngine.close();
        this.chunkMap.close();
    }
    
    public void tick(final BooleanSupplier booleanSupplier) {
        this.level.getProfiler().push("purge");
        this.distanceManager.purgeStaleTickets();
        this.runDistanceManagerUpdates();
        this.level.getProfiler().popPush("chunks");
        this.tickChunks();
        this.level.getProfiler().popPush("unload");
        this.chunkMap.tick(booleanSupplier);
        this.level.getProfiler().pop();
        this.clearCache();
    }
    
    private void tickChunks() {
        final long long2 = this.level.getGameTime();
        final long long3 = long2 - this.lastInhabitedUpdate;
        this.lastInhabitedUpdate = long2;
        final LevelData cya6 = this.level.getLevelData();
        final boolean boolean7 = this.level.isDebug();
        final boolean boolean8 = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
        if (!boolean7) {
            this.level.getProfiler().push("pollingChunks");
            final int integer9 = this.level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
            final boolean boolean9 = cya6.getGameTime() % 400L == 0L;
            this.level.getProfiler().push("naturalSpawnCount");
            final int integer10 = this.distanceManager.getNaturalSpawnChunkCount();
            final NaturalSpawner.SpawnState d12 = NaturalSpawner.createState(integer10, this.level.getAllEntities(), this::getFullChunk);
            this.lastSpawnState = d12;
            this.level.getProfiler().pop();
            final List<ChunkHolder> list13 = (List<ChunkHolder>)Lists.newArrayList((Iterable)this.chunkMap.getChunks());
            Collections.shuffle((List)list13);
            list13.forEach(zr -> {
                final Optional<LevelChunk> optional9 = (Optional<LevelChunk>)((Either)zr.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left();
                if (!optional9.isPresent()) {
                    return;
                }
                this.level.getProfiler().push("broadcast");
                zr.broadcastChanges((LevelChunk)optional9.get());
                this.level.getProfiler().pop();
                final Optional<LevelChunk> optional10 = (Optional<LevelChunk>)((Either)zr.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left();
                if (!optional10.isPresent()) {
                    return;
                }
                final LevelChunk cge11 = (LevelChunk)optional10.get();
                final ChunkPos bra12 = zr.getPos();
                if (this.chunkMap.noPlayersCloseForSpawning(bra12)) {
                    return;
                }
                cge11.setInhabitedTime(cge11.getInhabitedTime() + long3);
                if (boolean8 && (this.spawnEnemies || this.spawnFriendlies) && this.level.getWorldBorder().isWithinBounds(cge11.getPos())) {
                    NaturalSpawner.spawnForChunk(this.level, cge11, d12, this.spawnFriendlies, this.spawnEnemies, boolean9);
                }
                this.level.tickChunk(cge11, integer9);
            });
            this.level.getProfiler().push("customSpawners");
            if (boolean8) {
                this.level.tickCustomSpawners(this.spawnEnemies, this.spawnFriendlies);
            }
            this.level.getProfiler().pop();
            this.level.getProfiler().pop();
        }
        this.chunkMap.tick();
    }
    
    private void getFullChunk(final long long1, final Consumer<LevelChunk> consumer) {
        final ChunkHolder zr5 = this.getVisibleChunkIfPresent(long1);
        if (zr5 != null) {
            ((Either)zr5.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left().ifPresent((Consumer)consumer);
        }
    }
    
    @Override
    public String gatherStats() {
        return new StringBuilder().append("ServerChunkCache: ").append(this.getLoadedChunksCount()).toString();
    }
    
    @VisibleForTesting
    public int getPendingTasksCount() {
        return this.mainThreadProcessor.getPendingTasksCount();
    }
    
    public ChunkGenerator getGenerator() {
        return this.generator;
    }
    
    public int getLoadedChunksCount() {
        return this.chunkMap.size();
    }
    
    public void blockChanged(final BlockPos fx) {
        final int integer3 = fx.getX() >> 4;
        final int integer4 = fx.getZ() >> 4;
        final ChunkHolder zr5 = this.getVisibleChunkIfPresent(ChunkPos.asLong(integer3, integer4));
        if (zr5 != null) {
            zr5.blockChanged(fx);
        }
    }
    
    public void onLightUpdate(final LightLayer bsc, final SectionPos gp) {
        this.mainThreadProcessor.execute(() -> {
            final ChunkHolder zr4 = this.getVisibleChunkIfPresent(gp.chunk().toLong());
            if (zr4 != null) {
                zr4.sectionLightChanged(bsc, gp.y());
            }
        });
    }
    
    public <T> void addRegionTicket(final TicketType<T> aal, final ChunkPos bra, final int integer, final T object) {
        this.distanceManager.<T>addRegionTicket(aal, bra, integer, object);
    }
    
    public <T> void removeRegionTicket(final TicketType<T> aal, final ChunkPos bra, final int integer, final T object) {
        this.distanceManager.<T>removeRegionTicket(aal, bra, integer, object);
    }
    
    @Override
    public void updateChunkForced(final ChunkPos bra, final boolean boolean2) {
        this.distanceManager.updateChunkForced(bra, boolean2);
    }
    
    public void move(final ServerPlayer aah) {
        this.chunkMap.move(aah);
    }
    
    public void removeEntity(final Entity apx) {
        this.chunkMap.removeEntity(apx);
    }
    
    public void addEntity(final Entity apx) {
        this.chunkMap.addEntity(apx);
    }
    
    public void broadcastAndSend(final Entity apx, final Packet<?> oj) {
        this.chunkMap.broadcastAndSend(apx, oj);
    }
    
    public void broadcast(final Entity apx, final Packet<?> oj) {
        this.chunkMap.broadcast(apx, oj);
    }
    
    public void setViewDistance(final int integer) {
        this.chunkMap.setViewDistance(integer);
    }
    
    @Override
    public void setSpawnSettings(final boolean boolean1, final boolean boolean2) {
        this.spawnEnemies = boolean1;
        this.spawnFriendlies = boolean2;
    }
    
    public String getChunkDebugData(final ChunkPos bra) {
        return this.chunkMap.getChunkDebugData(bra);
    }
    
    public DimensionDataStorage getDataStorage() {
        return this.dataStorage;
    }
    
    public PoiManager getPoiManager() {
        return this.chunkMap.getPoiManager();
    }
    
    @Nullable
    public NaturalSpawner.SpawnState getLastSpawnState() {
        return this.lastSpawnState;
    }
    
    static {
        CHUNK_STATUSES = ChunkStatus.getStatusList();
    }
    
    final class MainThreadExecutor extends BlockableEventLoop<Runnable> {
        private MainThreadExecutor(final Level bru) {
            super(new StringBuilder().append("Chunk source main thread executor for ").append(bru.dimension().location()).toString());
        }
        
        @Override
        protected Runnable wrapRunnable(final Runnable runnable) {
            return runnable;
        }
        
        @Override
        protected boolean shouldRun(final Runnable runnable) {
            return true;
        }
        
        @Override
        protected boolean scheduleExecutables() {
            return true;
        }
        
        @Override
        protected Thread getRunningThread() {
            return ServerChunkCache.this.mainThread;
        }
        
        @Override
        protected void doRunTask(final Runnable runnable) {
            ServerChunkCache.this.level.getProfiler().incrementCounter("runTask");
            super.doRunTask(runnable);
        }
        
        @Override
        protected boolean pollTask() {
            if (ServerChunkCache.this.runDistanceManagerUpdates()) {
                return true;
            }
            ServerChunkCache.this.lightEngine.tryScheduleUpdate();
            return super.pollTask();
        }
    }
}
