package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import net.minecraft.server.packs.PackResources;
import java.util.concurrent.CompletionStage;
import net.minecraft.util.profiling.ProfileResults;
import java.lang.management.ThreadMXBean;
import java.util.Comparator;
import java.lang.management.ThreadInfo;
import java.lang.management.ManagementFactory;
import com.google.common.base.Splitter;
import java.io.Writer;
import java.nio.file.OpenOption;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Path;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.tags.TagContainer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.Commands;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.packs.repository.Pack;
import com.google.common.collect.Sets;
import net.minecraft.world.level.DataPackConfig;
import java.util.concurrent.CompletableFuture;
import java.util.Collection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import java.util.UUID;
import java.util.Optional;
import net.minecraft.CrashReportDetail;
import java.util.Set;
import net.minecraft.gametest.framework.GameTestTicker;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.world.level.GameRules;
import java.util.Collections;
import java.util.Arrays;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import com.mojang.authlib.GameProfile;
import java.util.function.BooleanSupplier;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import io.netty.buffer.ByteBuf;
import java.util.Base64;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import io.netty.buffer.ByteBufOutputStream;
import org.apache.commons.lang3.Validate;
import javax.imageio.ImageIO;
import io.netty.buffer.Unpooled;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.profiling.SingleTickProfiler;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TextComponent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import net.minecraft.world.level.storage.LevelResource;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ForcedChunksSavedData;
import net.minecraft.util.Unit;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.data.worldgen.Features;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProgressListener;
import java.io.IOException;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SaveDataDirtyRunnable;
import net.minecraft.world.scores.Scoreboard;
import java.util.function.Supplier;
import net.minecraft.world.scores.ScoreboardSaveData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import com.google.common.collect.Maps;
import net.minecraft.util.profiling.InactiveProfiler;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.concurrent.Executor;
import net.minecraft.util.FrameTimer;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.world.level.storage.CommandStorage;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.security.KeyPair;
import javax.annotation.Nullable;
import java.net.Proxy;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import com.mojang.datafixers.DataFixer;
import java.util.Random;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.ContinuousProfiler;
import java.util.List;
import net.minecraft.world.Snooper;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.LevelSettings;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.SnooperPopulator;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

public abstract class MinecraftServer extends ReentrantBlockableEventLoop<TickTask> implements SnooperPopulator, CommandSource, AutoCloseable {
    private static final Logger LOGGER;
    public static final File USERID_CACHE_FILE;
    public static final LevelSettings DEMO_SETTINGS;
    protected final LevelStorageSource.LevelStorageAccess storageSource;
    protected final PlayerDataStorage playerDataStorage;
    private final Snooper snooper;
    private final List<Runnable> tickables;
    private final ContinuousProfiler continousProfiler;
    private ProfilerFiller profiler;
    private final ServerConnectionListener connection;
    private final ChunkProgressListenerFactory progressListenerFactory;
    private final ServerStatus status;
    private final Random random;
    private final DataFixer fixerUpper;
    private String localIp;
    private int port;
    protected final RegistryAccess.RegistryHolder registryHolder;
    private final Map<ResourceKey<Level>, ServerLevel> levels;
    private PlayerList playerList;
    private volatile boolean running;
    private boolean stopped;
    private int tickCount;
    protected final Proxy proxy;
    private boolean onlineMode;
    private boolean preventProxyConnections;
    private boolean pvp;
    private boolean allowFlight;
    @Nullable
    private String motd;
    private int maxBuildHeight;
    private int playerIdleTimeout;
    public final long[] tickTimes;
    @Nullable
    private KeyPair keyPair;
    @Nullable
    private String singleplayerName;
    private boolean isDemo;
    private String resourcePack;
    private String resourcePackHash;
    private volatile boolean isReady;
    private long lastOverloadWarning;
    private boolean delayProfilerStart;
    private boolean forceGameType;
    private final MinecraftSessionService sessionService;
    private final GameProfileRepository profileRepository;
    private final GameProfileCache profileCache;
    private long lastServerStatus;
    private final Thread serverThread;
    private long nextTickTime;
    private long delayedTasksMaxNextTickTime;
    private boolean mayHaveDelayedTasks;
    private boolean hasWorldScreenshot;
    private final PackRepository packRepository;
    private final ServerScoreboard scoreboard;
    @Nullable
    private CommandStorage commandStorage;
    private final CustomBossEvents customBossEvents;
    private final ServerFunctionManager functionManager;
    private final FrameTimer frameTimer;
    private boolean enforceWhitelist;
    private float averageTickTime;
    private final Executor executor;
    @Nullable
    private String serverId;
    private ServerResources resources;
    private final StructureManager structureManager;
    protected final WorldData worldData;
    
    public static <S extends MinecraftServer> S spin(final Function<Thread, S> function) {
        final AtomicReference<S> atomicReference2 = (AtomicReference<S>)new AtomicReference();
        final Thread thread3 = new Thread(() -> ((MinecraftServer)atomicReference2.get()).runServer(), "Server thread");
        thread3.setUncaughtExceptionHandler((thread, throwable) -> MinecraftServer.LOGGER.error(throwable));
        final S minecraftServer4 = (S)function.apply(thread3);
        atomicReference2.set(minecraftServer4);
        thread3.start();
        return minecraftServer4;
    }
    
    public MinecraftServer(final Thread thread, final RegistryAccess.RegistryHolder b, final LevelStorageSource.LevelStorageAccess a, final WorldData cyk, final PackRepository abu, final Proxy proxy, final DataFixer dataFixer, final ServerResources vz, final MinecraftSessionService minecraftSessionService, final GameProfileRepository gameProfileRepository, final GameProfileCache aco, final ChunkProgressListenerFactory aaq) {
        super("Server");
        this.snooper = new Snooper("server", (SnooperPopulator)this, Util.getMillis());
        this.tickables = (List<Runnable>)Lists.newArrayList();
        this.continousProfiler = new ContinuousProfiler(Util.timeSource, this::getTickCount);
        this.profiler = InactiveProfiler.INSTANCE;
        this.status = new ServerStatus();
        this.random = new Random();
        this.port = -1;
        this.levels = (Map<ResourceKey<Level>, ServerLevel>)Maps.newLinkedHashMap();
        this.running = true;
        this.tickTimes = new long[100];
        this.resourcePack = "";
        this.resourcePackHash = "";
        this.nextTickTime = Util.getMillis();
        this.scoreboard = new ServerScoreboard(this);
        this.customBossEvents = new CustomBossEvents();
        this.frameTimer = new FrameTimer();
        this.registryHolder = b;
        this.worldData = cyk;
        this.proxy = proxy;
        this.packRepository = abu;
        this.resources = vz;
        this.sessionService = minecraftSessionService;
        this.profileRepository = gameProfileRepository;
        this.profileCache = aco;
        this.connection = new ServerConnectionListener(this);
        this.progressListenerFactory = aaq;
        this.storageSource = a;
        this.playerDataStorage = a.createPlayerStorage();
        this.fixerUpper = dataFixer;
        this.functionManager = new ServerFunctionManager(this, vz.getFunctionLibrary());
        this.structureManager = new StructureManager(vz.getResourceManager(), a, dataFixer);
        this.serverThread = thread;
        this.executor = Util.backgroundExecutor();
    }
    
    private void readScoreboard(final DimensionDataStorage cxz) {
        final ScoreboardSaveData ddl3 = cxz.<ScoreboardSaveData>computeIfAbsent((java.util.function.Supplier<ScoreboardSaveData>)ScoreboardSaveData::new, "scoreboard");
        ddl3.setScoreboard(this.getScoreboard());
        this.getScoreboard().addDirtyListener((Runnable)new SaveDataDirtyRunnable(ddl3));
    }
    
    protected abstract boolean initServer() throws IOException;
    
    public static void convertFromRegionFormatIfNeeded(final LevelStorageSource.LevelStorageAccess a) {
        if (a.requiresConversion()) {
            MinecraftServer.LOGGER.info("Converting map!");
            a.convertLevel(new ProgressListener() {
                private long timeStamp = Util.getMillis();
                
                public void progressStartNoAbort(final Component nr) {
                }
                
                public void progressStart(final Component nr) {
                }
                
                public void progressStagePercentage(final int integer) {
                    if (Util.getMillis() - this.timeStamp >= 1000L) {
                        this.timeStamp = Util.getMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", integer);
                    }
                }
                
                public void stop() {
                }
                
                public void progressStage(final Component nr) {
                }
            });
        }
    }
    
    protected void loadLevel() {
        this.detectBundledResources();
        this.worldData.setModdedInfo(this.getServerModName(), this.getModdedStatus().isPresent());
        final ChunkProgressListener aap2 = this.progressListenerFactory.create(11);
        this.createLevels(aap2);
        this.forceDifficulty();
        this.prepareLevels(aap2);
    }
    
    protected void forceDifficulty() {
    }
    
    protected void createLevels(final ChunkProgressListener aap) {
        final ServerLevelData cyj3 = this.worldData.overworldData();
        final WorldGenSettings cht4 = this.worldData.worldGenSettings();
        final boolean boolean5 = cht4.isDebug();
        final long long6 = cht4.seed();
        final long long7 = BiomeManager.obfuscateSeed(long6);
        final List<CustomSpawner> list10 = (List<CustomSpawner>)ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(cyj3));
        final MappedRegistry<LevelStem> gi11 = cht4.dimensions();
        final LevelStem chb13 = gi11.get(LevelStem.OVERWORLD);
        DimensionType cha14;
        ChunkGenerator cfv12;
        if (chb13 == null) {
            cha14 = this.registryHolder.dimensionTypes().getOrThrow(DimensionType.OVERWORLD_LOCATION);
            cfv12 = WorldGenSettings.makeDefaultOverworld(this.registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), this.registryHolder.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), new Random().nextLong());
        }
        else {
            cha14 = chb13.type();
            cfv12 = chb13.generator();
        }
        final ServerLevel aag15 = new ServerLevel(this, this.executor, this.storageSource, cyj3, Level.OVERWORLD, cha14, aap, cfv12, boolean5, long7, list10, true);
        this.levels.put(Level.OVERWORLD, aag15);
        final DimensionDataStorage cxz16 = aag15.getDataStorage();
        this.readScoreboard(cxz16);
        this.commandStorage = new CommandStorage(cxz16);
        final WorldBorder cfr17 = aag15.getWorldBorder();
        cfr17.applySettings(cyj3.getWorldBorder());
        if (!cyj3.isInitialized()) {
            try {
                setInitialSpawn(aag15, cyj3, cht4.generateBonusChest(), boolean5, true);
                cyj3.setInitialized(true);
                if (boolean5) {
                    this.setupDebugLevel(this.worldData);
                }
            }
            catch (Throwable throwable18) {
                final CrashReport l19 = CrashReport.forThrowable(throwable18, "Exception initializing level");
                try {
                    aag15.fillReportDetails(l19);
                }
                catch (Throwable t) {}
                throw new ReportedException(l19);
            }
            cyj3.setInitialized(true);
        }
        this.getPlayerList().setLevel(aag15);
        if (this.worldData.getCustomBossEvents() != null) {
            this.getCustomBossEvents().load(this.worldData.getCustomBossEvents());
        }
        for (final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry19 : gi11.entrySet()) {
            final ResourceKey<LevelStem> vj20 = (ResourceKey<LevelStem>)entry19.getKey();
            if (vj20 == LevelStem.OVERWORLD) {
                continue;
            }
            final ResourceKey<Level> vj21 = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, vj20.location());
            final DimensionType cha15 = ((LevelStem)entry19.getValue()).type();
            final ChunkGenerator cfv13 = ((LevelStem)entry19.getValue()).generator();
            final DerivedLevelData cxy24 = new DerivedLevelData(this.worldData, cyj3);
            final ServerLevel aag16 = new ServerLevel(this, this.executor, this.storageSource, cxy24, vj21, cha15, aap, cfv13, boolean5, long7, (List<CustomSpawner>)ImmutableList.of(), false);
            cfr17.addListener(new BorderChangeListener.DelegateBorderChangeListener(aag16.getWorldBorder()));
            this.levels.put(vj21, aag16);
        }
    }
    
    private static void setInitialSpawn(final ServerLevel aag, final ServerLevelData cyj, final boolean boolean3, final boolean boolean4, final boolean boolean5) {
        final ChunkGenerator cfv6 = aag.getChunkSource().getGenerator();
        if (!boolean5) {
            cyj.setSpawn(BlockPos.ZERO.above(cfv6.getSpawnHeight()), 0.0f);
            return;
        }
        if (boolean4) {
            cyj.setSpawn(BlockPos.ZERO.above(), 0.0f);
            return;
        }
        final BiomeSource bsv7 = cfv6.getBiomeSource();
        final Random random8 = new Random(aag.getSeed());
        final BlockPos fx9 = bsv7.findBiomeHorizontal(0, aag.getSeaLevel(), 0, 256, (Predicate<Biome>)(bss -> bss.getMobSettings().playerSpawnFriendly()), random8);
        final ChunkPos bra10 = (fx9 == null) ? new ChunkPos(0, 0) : new ChunkPos(fx9);
        if (fx9 == null) {
            MinecraftServer.LOGGER.warn("Unable to find spawn biome");
        }
        boolean boolean6 = false;
        for (final Block bul13 : BlockTags.VALID_SPAWN.getValues()) {
            if (bsv7.getSurfaceBlocks().contains(bul13.defaultBlockState())) {
                boolean6 = true;
                break;
            }
        }
        cyj.setSpawn(bra10.getWorldPosition().offset(8, cfv6.getSpawnHeight(), 8), 0.0f);
        int integer12 = 0;
        int integer13 = 0;
        int integer14 = 0;
        int integer15 = -1;
        final int integer16 = 32;
        for (int integer17 = 0; integer17 < 1024; ++integer17) {
            if (integer12 > -16 && integer12 <= 16 && integer13 > -16 && integer13 <= 16) {
                final BlockPos fx10 = PlayerRespawnLogic.getSpawnPosInChunk(aag, new ChunkPos(bra10.x + integer12, bra10.z + integer13), boolean6);
                if (fx10 != null) {
                    cyj.setSpawn(fx10, 0.0f);
                    break;
                }
            }
            if (integer12 == integer13 || (integer12 < 0 && integer12 == -integer13) || (integer12 > 0 && integer12 == 1 - integer13)) {
                final int integer18 = integer14;
                integer14 = -integer15;
                integer15 = integer18;
            }
            integer12 += integer14;
            integer13 += integer15;
        }
        if (boolean3) {
            final ConfiguredFeature<?, ?> cis17 = Features.BONUS_CHEST;
            cis17.place(aag, cfv6, aag.random, new BlockPos(cyj.getXSpawn(), cyj.getYSpawn(), cyj.getZSpawn()));
        }
    }
    
    private void setupDebugLevel(final WorldData cyk) {
        cyk.setDifficulty(Difficulty.PEACEFUL);
        cyk.setDifficultyLocked(true);
        final ServerLevelData cyj3 = cyk.overworldData();
        cyj3.setRaining(false);
        cyj3.setThundering(false);
        cyj3.setClearWeatherTime(1000000000);
        cyj3.setDayTime(6000L);
        cyj3.setGameType(GameType.SPECTATOR);
    }
    
    private void prepareLevels(final ChunkProgressListener aap) {
        final ServerLevel aag3 = this.overworld();
        MinecraftServer.LOGGER.info("Preparing start region for dimension {}", aag3.dimension().location());
        final BlockPos fx4 = aag3.getSharedSpawnPos();
        aap.updateSpawnPos(new ChunkPos(fx4));
        final ServerChunkCache aae5 = aag3.getChunkSource();
        aae5.getLightEngine().setTaskPerBatch(500);
        this.nextTickTime = Util.getMillis();
        aae5.<Unit>addRegionTicket(TicketType.START, new ChunkPos(fx4), 11, Unit.INSTANCE);
        while (aae5.getTickingGenerated() != 441) {
            this.nextTickTime = Util.getMillis() + 10L;
            this.waitUntilNextTick();
        }
        this.nextTickTime = Util.getMillis() + 10L;
        this.waitUntilNextTick();
        for (final ServerLevel aag4 : this.levels.values()) {
            final ForcedChunksSavedData brp8 = aag4.getDataStorage().<ForcedChunksSavedData>get((java.util.function.Supplier<ForcedChunksSavedData>)ForcedChunksSavedData::new, "chunks");
            if (brp8 != null) {
                final LongIterator longIterator9 = brp8.getChunks().iterator();
                while (longIterator9.hasNext()) {
                    final long long10 = longIterator9.nextLong();
                    final ChunkPos bra12 = new ChunkPos(long10);
                    aag4.getChunkSource().updateChunkForced(bra12, true);
                }
            }
        }
        this.nextTickTime = Util.getMillis() + 10L;
        this.waitUntilNextTick();
        aap.stop();
        aae5.getLightEngine().setTaskPerBatch(5);
        this.updateMobSpawningFlags();
    }
    
    protected void detectBundledResources() {
        final File file2 = this.storageSource.getLevelPath(LevelResource.MAP_RESOURCE_FILE).toFile();
        if (file2.isFile()) {
            final String string3 = this.storageSource.getLevelId();
            try {
                this.setResourcePack("level://" + URLEncoder.encode(string3, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException4) {
                MinecraftServer.LOGGER.warn("Something went wrong url encoding {}", string3);
            }
        }
    }
    
    public GameType getDefaultGameType() {
        return this.worldData.getGameType();
    }
    
    public boolean isHardcore() {
        return this.worldData.isHardcore();
    }
    
    public abstract int getOperatorUserPermissionLevel();
    
    public abstract int getFunctionCompilationLevel();
    
    public abstract boolean shouldRconBroadcast();
    
    public boolean saveAllChunks(final boolean boolean1, final boolean boolean2, final boolean boolean3) {
        boolean boolean4 = false;
        for (final ServerLevel aag7 : this.getAllLevels()) {
            if (!boolean1) {
                MinecraftServer.LOGGER.info("Saving chunks for level '{}'/{}", aag7, aag7.dimension().location());
            }
            aag7.save(null, boolean2, aag7.noSave && !boolean3);
            boolean4 = true;
        }
        final ServerLevel aag8 = this.overworld();
        final ServerLevelData cyj7 = this.worldData.overworldData();
        cyj7.setWorldBorder(aag8.getWorldBorder().createSettings());
        this.worldData.setCustomBossEvents(this.getCustomBossEvents().save());
        this.storageSource.saveDataTag(this.registryHolder, this.worldData, this.getPlayerList().getSingleplayerData());
        return boolean4;
    }
    
    public void close() {
        this.stopServer();
    }
    
    protected void stopServer() {
        MinecraftServer.LOGGER.info("Stopping server");
        if (this.getConnection() != null) {
            this.getConnection().stop();
        }
        if (this.playerList != null) {
            MinecraftServer.LOGGER.info("Saving players");
            this.playerList.saveAll();
            this.playerList.removeAll();
        }
        MinecraftServer.LOGGER.info("Saving worlds");
        for (final ServerLevel aag3 : this.getAllLevels()) {
            if (aag3 != null) {
                aag3.noSave = false;
            }
        }
        this.saveAllChunks(false, true, false);
        for (final ServerLevel aag3 : this.getAllLevels()) {
            if (aag3 != null) {
                try {
                    aag3.close();
                }
                catch (IOException iOException4) {
                    MinecraftServer.LOGGER.error("Exception closing the level", (Throwable)iOException4);
                }
            }
        }
        if (this.snooper.isStarted()) {
            this.snooper.interrupt();
        }
        this.resources.close();
        try {
            this.storageSource.close();
        }
        catch (IOException iOException5) {
            MinecraftServer.LOGGER.error("Failed to unlock level {}", this.storageSource.getLevelId(), iOException5);
        }
    }
    
    public String getLocalIp() {
        return this.localIp;
    }
    
    public void setLocalIp(final String string) {
        this.localIp = string;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void halt(final boolean boolean1) {
        this.running = false;
        if (boolean1) {
            try {
                this.serverThread.join();
            }
            catch (InterruptedException interruptedException3) {
                MinecraftServer.LOGGER.error("Error while shutting down", (Throwable)interruptedException3);
            }
        }
    }
    
    protected void runServer() {
        try {
            if (this.initServer()) {
                this.nextTickTime = Util.getMillis();
                this.status.setDescription(new TextComponent(this.motd));
                this.status.setVersion(new ServerStatus.Version(SharedConstants.getCurrentVersion().getName(), SharedConstants.getCurrentVersion().getProtocolVersion()));
                this.updateStatusIcon(this.status);
                while (this.running) {
                    final long long2 = Util.getMillis() - this.nextTickTime;
                    if (long2 > 2000L && this.nextTickTime - this.lastOverloadWarning >= 15000L) {
                        final long long3 = long2 / 50L;
                        MinecraftServer.LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", long2, long3);
                        this.nextTickTime += long3 * 50L;
                        this.lastOverloadWarning = this.nextTickTime;
                    }
                    this.nextTickTime += 50L;
                    final SingleTickProfiler anw4 = SingleTickProfiler.createTickProfiler("Server");
                    this.startProfilerTick(anw4);
                    this.profiler.startTick();
                    this.profiler.push("tick");
                    this.tickServer(this::haveTime);
                    this.profiler.popPush("nextTickWait");
                    this.mayHaveDelayedTasks = true;
                    this.delayedTasksMaxNextTickTime = Math.max(Util.getMillis() + 50L, this.nextTickTime);
                    this.waitUntilNextTick();
                    this.profiler.pop();
                    this.profiler.endTick();
                    this.endProfilerTick(anw4);
                    this.isReady = true;
                }
            }
            else {
                this.onServerCrash(null);
            }
        }
        catch (Throwable throwable2) {
            MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable2);
            CrashReport l3;
            if (throwable2 instanceof ReportedException) {
                l3 = this.fillReport(((ReportedException)throwable2).getReport());
            }
            else {
                l3 = this.fillReport(new CrashReport("Exception in server tick loop", throwable2));
            }
            final File file4 = new File(new File(this.getServerDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
            if (l3.saveToFile(file4)) {
                MinecraftServer.LOGGER.error("This crash report has been saved to: {}", file4.getAbsolutePath());
            }
            else {
                MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
            }
            this.onServerCrash(l3);
            try {
                this.stopped = true;
                this.stopServer();
            }
            catch (Throwable throwable2) {
                MinecraftServer.LOGGER.error("Exception stopping the server", throwable2);
            }
            finally {
                this.onServerExit();
            }
        }
        finally {
            try {
                this.stopped = true;
                this.stopServer();
            }
            catch (Throwable throwable3) {
                MinecraftServer.LOGGER.error("Exception stopping the server", throwable3);
                this.onServerExit();
            }
            finally {
                this.onServerExit();
            }
        }
    }
    
    private boolean haveTime() {
        return this.runningTask() || Util.getMillis() < (this.mayHaveDelayedTasks ? this.delayedTasksMaxNextTickTime : this.nextTickTime);
    }
    
    protected void waitUntilNextTick() {
        this.runAllTasks();
        this.managedBlock(() -> !this.haveTime());
    }
    
    protected TickTask wrapRunnable(final Runnable runnable) {
        return new TickTask(this.tickCount, runnable);
    }
    
    protected boolean shouldRun(final TickTask wb) {
        return wb.getTick() + 3 < this.tickCount || this.haveTime();
    }
    
    public boolean pollTask() {
        final boolean boolean2 = this.pollTaskInternal();
        return this.mayHaveDelayedTasks = boolean2;
    }
    
    private boolean pollTaskInternal() {
        if (super.pollTask()) {
            return true;
        }
        if (this.haveTime()) {
            for (final ServerLevel aag3 : this.getAllLevels()) {
                if (aag3.getChunkSource().pollTask()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    protected void doRunTask(final TickTask wb) {
        this.getProfiler().incrementCounter("runTask");
        super.doRunTask(wb);
    }
    
    private void updateStatusIcon(final ServerStatus un) {
        File file3 = this.getFile("server-icon.png");
        if (!file3.exists()) {
            file3 = this.storageSource.getIconFile();
        }
        if (file3.isFile()) {
            final ByteBuf byteBuf4 = Unpooled.buffer();
            try {
                final BufferedImage bufferedImage5 = ImageIO.read(file3);
                Validate.validState(bufferedImage5.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(bufferedImage5.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write((RenderedImage)bufferedImage5, "PNG", (OutputStream)new ByteBufOutputStream(byteBuf4));
                final ByteBuffer byteBuffer6 = Base64.getEncoder().encode(byteBuf4.nioBuffer());
                un.setFavicon(new StringBuilder().append("data:image/png;base64,").append(StandardCharsets.UTF_8.decode(byteBuffer6)).toString());
            }
            catch (Exception exception5) {
                MinecraftServer.LOGGER.error("Couldn't load server icon", (Throwable)exception5);
            }
            finally {
                byteBuf4.release();
            }
        }
    }
    
    public boolean hasWorldScreenshot() {
        return this.hasWorldScreenshot = (this.hasWorldScreenshot || this.getWorldScreenshotFile().isFile());
    }
    
    public File getWorldScreenshotFile() {
        return this.storageSource.getIconFile();
    }
    
    public File getServerDirectory() {
        return new File(".");
    }
    
    protected void onServerCrash(final CrashReport l) {
    }
    
    protected void onServerExit() {
    }
    
    protected void tickServer(final BooleanSupplier booleanSupplier) {
        final long long3 = Util.getNanos();
        ++this.tickCount;
        this.tickChildren(booleanSupplier);
        if (long3 - this.lastServerStatus >= 5000000000L) {
            this.lastServerStatus = long3;
            this.status.setPlayers(new ServerStatus.Players(this.getMaxPlayers(), this.getPlayerCount()));
            final GameProfile[] arr5 = new GameProfile[Math.min(this.getPlayerCount(), 12)];
            final int integer6 = Mth.nextInt(this.random, 0, this.getPlayerCount() - arr5.length);
            for (int integer7 = 0; integer7 < arr5.length; ++integer7) {
                arr5[integer7] = ((ServerPlayer)this.playerList.getPlayers().get(integer6 + integer7)).getGameProfile();
            }
            Collections.shuffle(Arrays.asList((Object[])arr5));
            this.status.getPlayers().setSample(arr5);
        }
        if (this.tickCount % 6000 == 0) {
            MinecraftServer.LOGGER.debug("Autosave started");
            this.profiler.push("save");
            this.playerList.saveAll();
            this.saveAllChunks(true, false, false);
            this.profiler.pop();
            MinecraftServer.LOGGER.debug("Autosave finished");
        }
        this.profiler.push("snooper");
        if (!this.snooper.isStarted() && this.tickCount > 100) {
            this.snooper.start();
        }
        if (this.tickCount % 6000 == 0) {
            this.snooper.prepare();
        }
        this.profiler.pop();
        this.profiler.push("tallying");
        final long[] tickTimes = this.tickTimes;
        final int n = this.tickCount % 100;
        final long n2 = Util.getNanos() - long3;
        tickTimes[n] = n2;
        final long long4 = n2;
        this.averageTickTime = this.averageTickTime * 0.8f + long4 / 1000000.0f * 0.19999999f;
        final long long5 = Util.getNanos();
        this.frameTimer.logFrameDuration(long5 - long3);
        this.profiler.pop();
    }
    
    protected void tickChildren(final BooleanSupplier booleanSupplier) {
        this.profiler.push("commandFunctions");
        this.getFunctions().tick();
        this.profiler.popPush("levels");
        for (final ServerLevel aag4 : this.getAllLevels()) {
            this.profiler.push((Supplier<String>)(() -> new StringBuilder().append(aag4).append(" ").append(aag4.dimension().location()).toString()));
            if (this.tickCount % 20 == 0) {
                this.profiler.push("timeSync");
                this.playerList.broadcastAll(new ClientboundSetTimePacket(aag4.getGameTime(), aag4.getDayTime(), aag4.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)), aag4.dimension());
                this.profiler.pop();
            }
            this.profiler.push("tick");
            try {
                aag4.tick(booleanSupplier);
            }
            catch (Throwable throwable5) {
                final CrashReport l6 = CrashReport.forThrowable(throwable5, "Exception ticking world");
                aag4.fillReportDetails(l6);
                throw new ReportedException(l6);
            }
            this.profiler.pop();
            this.profiler.pop();
        }
        this.profiler.popPush("connection");
        this.getConnection().tick();
        this.profiler.popPush("players");
        this.playerList.tick();
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            GameTestTicker.singleton.tick();
        }
        this.profiler.popPush("server gui refresh");
        for (int integer3 = 0; integer3 < this.tickables.size(); ++integer3) {
            ((Runnable)this.tickables.get(integer3)).run();
        }
        this.profiler.pop();
    }
    
    public boolean isNetherEnabled() {
        return true;
    }
    
    public void addTickable(final Runnable runnable) {
        this.tickables.add(runnable);
    }
    
    protected void setId(final String string) {
        this.serverId = string;
    }
    
    public boolean isShutdown() {
        return !this.serverThread.isAlive();
    }
    
    public File getFile(final String string) {
        return new File(this.getServerDirectory(), string);
    }
    
    public final ServerLevel overworld() {
        return (ServerLevel)this.levels.get(Level.OVERWORLD);
    }
    
    @Nullable
    public ServerLevel getLevel(final ResourceKey<Level> vj) {
        return (ServerLevel)this.levels.get(vj);
    }
    
    public Set<ResourceKey<Level>> levelKeys() {
        return (Set<ResourceKey<Level>>)this.levels.keySet();
    }
    
    public Iterable<ServerLevel> getAllLevels() {
        return (Iterable<ServerLevel>)this.levels.values();
    }
    
    public String getServerVersion() {
        return SharedConstants.getCurrentVersion().getName();
    }
    
    public int getPlayerCount() {
        return this.playerList.getPlayerCount();
    }
    
    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }
    
    public String[] getPlayerNames() {
        return this.playerList.getPlayerNamesArray();
    }
    
    public String getServerModName() {
        return "vanilla";
    }
    
    public CrashReport fillReport(final CrashReport l) {
        if (this.playerList != null) {
            l.getSystemDetails().setDetail("Player Count", (CrashReportDetail<String>)(() -> new StringBuilder().append(this.playerList.getPlayerCount()).append(" / ").append(this.playerList.getMaxPlayers()).append("; ").append(this.playerList.getPlayers()).toString()));
        }
        l.getSystemDetails().setDetail("Data Packs", (CrashReportDetail<String>)(() -> {
            final StringBuilder stringBuilder2 = new StringBuilder();
            for (final Pack abs4 : this.packRepository.getSelectedPacks()) {
                if (stringBuilder2.length() > 0) {
                    stringBuilder2.append(", ");
                }
                stringBuilder2.append(abs4.getId());
                if (!abs4.getCompatibility().isCompatible()) {
                    stringBuilder2.append(" (incompatible)");
                }
            }
            return stringBuilder2.toString();
        }));
        if (this.serverId != null) {
            l.getSystemDetails().setDetail("Server Id", (CrashReportDetail<String>)(() -> this.serverId));
        }
        return l;
    }
    
    public abstract Optional<String> getModdedStatus();
    
    @Override
    public void sendMessage(final Component nr, final UUID uUID) {
        MinecraftServer.LOGGER.info(nr.getString());
    }
    
    public KeyPair getKeyPair() {
        return this.keyPair;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int integer) {
        this.port = integer;
    }
    
    public String getSingleplayerName() {
        return this.singleplayerName;
    }
    
    public void setSingleplayerName(final String string) {
        this.singleplayerName = string;
    }
    
    public boolean isSingleplayer() {
        return this.singleplayerName != null;
    }
    
    public void setKeyPair(final KeyPair keyPair) {
        this.keyPair = keyPair;
    }
    
    public void setDifficulty(final Difficulty aoo, final boolean boolean2) {
        if (!boolean2 && this.worldData.isDifficultyLocked()) {
            return;
        }
        this.worldData.setDifficulty(this.worldData.isHardcore() ? Difficulty.HARD : aoo);
        this.updateMobSpawningFlags();
        this.getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
    }
    
    public int getScaledTrackingDistance(final int integer) {
        return integer;
    }
    
    private void updateMobSpawningFlags() {
        for (final ServerLevel aag3 : this.getAllLevels()) {
            aag3.setSpawnSettings(this.isSpawningMonsters(), this.isSpawningAnimals());
        }
    }
    
    public void setDifficultyLocked(final boolean boolean1) {
        this.worldData.setDifficultyLocked(boolean1);
        this.getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
    }
    
    private void sendDifficultyUpdate(final ServerPlayer aah) {
        final LevelData cya3 = aah.getLevel().getLevelData();
        aah.connection.send(new ClientboundChangeDifficultyPacket(cya3.getDifficulty(), cya3.isDifficultyLocked()));
    }
    
    protected boolean isSpawningMonsters() {
        return this.worldData.getDifficulty() != Difficulty.PEACEFUL;
    }
    
    public boolean isDemo() {
        return this.isDemo;
    }
    
    public void setDemo(final boolean boolean1) {
        this.isDemo = boolean1;
    }
    
    public String getResourcePack() {
        return this.resourcePack;
    }
    
    public String getResourcePackHash() {
        return this.resourcePackHash;
    }
    
    public void setResourcePack(final String string1, final String string2) {
        this.resourcePack = string1;
        this.resourcePackHash = string2;
    }
    
    @Override
    public void populateSnooper(final Snooper aoz) {
        aoz.setDynamicData("whitelist_enabled", false);
        aoz.setDynamicData("whitelist_count", 0);
        if (this.playerList != null) {
            aoz.setDynamicData("players_current", this.getPlayerCount());
            aoz.setDynamicData("players_max", this.getMaxPlayers());
            aoz.setDynamicData("players_seen", this.playerDataStorage.getSeenPlayers().length);
        }
        aoz.setDynamicData("uses_auth", this.onlineMode);
        aoz.setDynamicData("gui_state", this.hasGui() ? "enabled" : "disabled");
        aoz.setDynamicData("run_time", ((Util.getMillis() - aoz.getStartupTime()) / 60L * 1000L));
        aoz.setDynamicData("avg_tick_ms", (int)(Mth.average(this.tickTimes) * 1.0E-6));
        int integer3 = 0;
        for (final ServerLevel aag5 : this.getAllLevels()) {
            if (aag5 != null) {
                aoz.setDynamicData(new StringBuilder().append("world[").append(integer3).append("][dimension]").toString(), aag5.dimension().location());
                aoz.setDynamicData(new StringBuilder().append("world[").append(integer3).append("][mode]").toString(), this.worldData.getGameType());
                aoz.setDynamicData(new StringBuilder().append("world[").append(integer3).append("][difficulty]").toString(), aag5.getDifficulty());
                aoz.setDynamicData(new StringBuilder().append("world[").append(integer3).append("][hardcore]").toString(), this.worldData.isHardcore());
                aoz.setDynamicData(new StringBuilder().append("world[").append(integer3).append("][height]").toString(), this.maxBuildHeight);
                aoz.setDynamicData(new StringBuilder().append("world[").append(integer3).append("][chunks_loaded]").toString(), aag5.getChunkSource().getLoadedChunksCount());
                ++integer3;
            }
        }
        aoz.setDynamicData("worlds", integer3);
    }
    
    public abstract boolean isDedicatedServer();
    
    public abstract int getRateLimitPacketsPerSecond();
    
    public boolean usesAuthentication() {
        return this.onlineMode;
    }
    
    public void setUsesAuthentication(final boolean boolean1) {
        this.onlineMode = boolean1;
    }
    
    public boolean getPreventProxyConnections() {
        return this.preventProxyConnections;
    }
    
    public void setPreventProxyConnections(final boolean boolean1) {
        this.preventProxyConnections = boolean1;
    }
    
    public boolean isSpawningAnimals() {
        return true;
    }
    
    public boolean areNpcsEnabled() {
        return true;
    }
    
    public abstract boolean isEpollEnabled();
    
    public boolean isPvpAllowed() {
        return this.pvp;
    }
    
    public void setPvpAllowed(final boolean boolean1) {
        this.pvp = boolean1;
    }
    
    public boolean isFlightAllowed() {
        return this.allowFlight;
    }
    
    public void setFlightAllowed(final boolean boolean1) {
        this.allowFlight = boolean1;
    }
    
    public abstract boolean isCommandBlockEnabled();
    
    public String getMotd() {
        return this.motd;
    }
    
    public void setMotd(final String string) {
        this.motd = string;
    }
    
    public int getMaxBuildHeight() {
        return this.maxBuildHeight;
    }
    
    public void setMaxBuildHeight(final int integer) {
        this.maxBuildHeight = integer;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
    
    public PlayerList getPlayerList() {
        return this.playerList;
    }
    
    public void setPlayerList(final PlayerList acs) {
        this.playerList = acs;
    }
    
    public abstract boolean isPublished();
    
    public void setDefaultGameType(final GameType brr) {
        this.worldData.setGameType(brr);
    }
    
    @Nullable
    public ServerConnectionListener getConnection() {
        return this.connection;
    }
    
    public boolean isReady() {
        return this.isReady;
    }
    
    public boolean hasGui() {
        return false;
    }
    
    public abstract boolean publishServer(final GameType brr, final boolean boolean2, final int integer);
    
    public int getTickCount() {
        return this.tickCount;
    }
    
    public Snooper getSnooper() {
        return this.snooper;
    }
    
    public int getSpawnProtectionRadius() {
        return 16;
    }
    
    public boolean isUnderSpawnProtection(final ServerLevel aag, final BlockPos fx, final Player bft) {
        return false;
    }
    
    public void setForceGameType(final boolean boolean1) {
        this.forceGameType = boolean1;
    }
    
    public boolean getForceGameType() {
        return this.forceGameType;
    }
    
    public boolean repliesToStatus() {
        return true;
    }
    
    public int getPlayerIdleTimeout() {
        return this.playerIdleTimeout;
    }
    
    public void setPlayerIdleTimeout(final int integer) {
        this.playerIdleTimeout = integer;
    }
    
    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }
    
    public GameProfileRepository getProfileRepository() {
        return this.profileRepository;
    }
    
    public GameProfileCache getProfileCache() {
        return this.profileCache;
    }
    
    public ServerStatus getStatus() {
        return this.status;
    }
    
    public void invalidateStatus() {
        this.lastServerStatus = 0L;
    }
    
    public int getAbsoluteMaxWorldSize() {
        return 29999984;
    }
    
    public boolean scheduleExecutables() {
        return super.scheduleExecutables() && !this.isStopped();
    }
    
    public Thread getRunningThread() {
        return this.serverThread;
    }
    
    public int getCompressionThreshold() {
        return 256;
    }
    
    public long getNextTickTime() {
        return this.nextTickTime;
    }
    
    public DataFixer getFixerUpper() {
        return this.fixerUpper;
    }
    
    public int getSpawnRadius(@Nullable final ServerLevel aag) {
        if (aag != null) {
            return aag.getGameRules().getInt(GameRules.RULE_SPAWN_RADIUS);
        }
        return 10;
    }
    
    public ServerAdvancementManager getAdvancements() {
        return this.resources.getAdvancements();
    }
    
    public ServerFunctionManager getFunctions() {
        return this.functionManager;
    }
    
    public CompletableFuture<Void> reloadResources(final Collection<String> collection) {
        final CompletableFuture<Void> completableFuture3 = (CompletableFuture<Void>)CompletableFuture.supplyAsync(() -> (ImmutableList)collection.stream().map(this.packRepository::getPack).filter(Objects::nonNull).map(Pack::open).collect(ImmutableList.toImmutableList()), (Executor)this).thenCompose(immutableList -> ServerResources.loadResources((List<PackResources>)immutableList, this.isDedicatedServer() ? Commands.CommandSelection.DEDICATED : Commands.CommandSelection.INTEGRATED, this.getFunctionCompilationLevel(), this.executor, (Executor)this)).thenAcceptAsync(vz -> {
            this.resources.close();
            this.resources = vz;
            this.packRepository.setSelected(collection);
            this.worldData.setDataPackConfig(getSelectedPacks(this.packRepository));
            vz.updateGlobals();
            this.getPlayerList().saveAll();
            this.getPlayerList().reloadResources();
            this.functionManager.replaceLibrary(this.resources.getFunctionLibrary());
            this.structureManager.onResourceManagerReload(this.resources.getResourceManager());
        }, (Executor)this);
        if (this.isSameThread()) {
            this.managedBlock(completableFuture3::isDone);
        }
        return completableFuture3;
    }
    
    public static DataPackConfig configurePackRepository(final PackRepository abu, final DataPackConfig brh, final boolean boolean3) {
        abu.reload();
        if (boolean3) {
            abu.setSelected((Collection<String>)Collections.singleton("vanilla"));
            return new DataPackConfig((List<String>)ImmutableList.of("vanilla"), (List<String>)ImmutableList.of());
        }
        final Set<String> set4 = (Set<String>)Sets.newLinkedHashSet();
        for (final String string6 : brh.getEnabled()) {
            if (abu.isAvailable(string6)) {
                set4.add(string6);
            }
            else {
                MinecraftServer.LOGGER.warn("Missing data pack {}", string6);
            }
        }
        for (final Pack abs6 : abu.getAvailablePacks()) {
            final String string7 = abs6.getId();
            if (!brh.getDisabled().contains(string7) && !set4.contains(string7)) {
                MinecraftServer.LOGGER.info("Found new data pack {}, loading it automatically", string7);
                set4.add(string7);
            }
        }
        if (set4.isEmpty()) {
            MinecraftServer.LOGGER.info("No datapacks selected, forcing vanilla");
            set4.add("vanilla");
        }
        abu.setSelected((Collection<String>)set4);
        return getSelectedPacks(abu);
    }
    
    private static DataPackConfig getSelectedPacks(final PackRepository abu) {
        final Collection<String> collection2 = abu.getSelectedIds();
        final List<String> list3 = (List<String>)ImmutableList.copyOf((Collection)collection2);
        final List<String> list4 = (List<String>)abu.getAvailableIds().stream().filter(string -> !collection2.contains(string)).collect(ImmutableList.toImmutableList());
        return new DataPackConfig(list3, list4);
    }
    
    public void kickUnlistedPlayers(final CommandSourceStack db) {
        if (!this.isEnforceWhitelist()) {
            return;
        }
        final PlayerList acs3 = db.getServer().getPlayerList();
        final UserWhiteList acz4 = acs3.getWhiteList();
        final List<ServerPlayer> list5 = (List<ServerPlayer>)Lists.newArrayList((Iterable)acs3.getPlayers());
        for (final ServerPlayer aah7 : list5) {
            if (!acz4.isWhiteListed(aah7.getGameProfile())) {
                aah7.connection.disconnect(new TranslatableComponent("multiplayer.disconnect.not_whitelisted"));
            }
        }
    }
    
    public PackRepository getPackRepository() {
        return this.packRepository;
    }
    
    public Commands getCommands() {
        return this.resources.getCommands();
    }
    
    public CommandSourceStack createCommandSourceStack() {
        final ServerLevel aag2 = this.overworld();
        return new CommandSourceStack((CommandSource)this, (aag2 == null) ? Vec3.ZERO : Vec3.atLowerCornerOf(aag2.getSharedSpawnPos()), Vec2.ZERO, aag2, 4, "Server", (Component)new TextComponent("Server"), this, (Entity)null);
    }
    
    @Override
    public boolean acceptsSuccess() {
        return true;
    }
    
    @Override
    public boolean acceptsFailure() {
        return true;
    }
    
    public RecipeManager getRecipeManager() {
        return this.resources.getRecipeManager();
    }
    
    public TagContainer getTags() {
        return this.resources.getTags();
    }
    
    public ServerScoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    public CommandStorage getCommandStorage() {
        if (this.commandStorage == null) {
            throw new NullPointerException("Called before server init");
        }
        return this.commandStorage;
    }
    
    public LootTables getLootTables() {
        return this.resources.getLootTables();
    }
    
    public PredicateManager getPredicateManager() {
        return this.resources.getPredicateManager();
    }
    
    public GameRules getGameRules() {
        return this.overworld().getGameRules();
    }
    
    public CustomBossEvents getCustomBossEvents() {
        return this.customBossEvents;
    }
    
    public boolean isEnforceWhitelist() {
        return this.enforceWhitelist;
    }
    
    public void setEnforceWhitelist(final boolean boolean1) {
        this.enforceWhitelist = boolean1;
    }
    
    public float getAverageTickTime() {
        return this.averageTickTime;
    }
    
    public int getProfilePermissions(final GameProfile gameProfile) {
        if (!this.getPlayerList().isOp(gameProfile)) {
            return 0;
        }
        final ServerOpListEntry acu3 = this.getPlayerList().getOps().get(gameProfile);
        if (acu3 != null) {
            return acu3.getLevel();
        }
        if (this.isSingleplayerOwner(gameProfile)) {
            return 4;
        }
        if (this.isSingleplayer()) {
            return this.getPlayerList().isAllowCheatsForAllPlayers() ? 4 : 0;
        }
        return this.getOperatorUserPermissionLevel();
    }
    
    public FrameTimer getFrameTimer() {
        return this.frameTimer;
    }
    
    public ProfilerFiller getProfiler() {
        return this.profiler;
    }
    
    public abstract boolean isSingleplayerOwner(final GameProfile gameProfile);
    
    public void saveDebugReport(final Path path) throws IOException {
        final Path path2 = path.resolve("levels");
        for (final Map.Entry<ResourceKey<Level>, ServerLevel> entry5 : this.levels.entrySet()) {
            final ResourceLocation vk6 = ((ResourceKey)entry5.getKey()).location();
            final Path path3 = path2.resolve(vk6.getNamespace()).resolve(vk6.getPath());
            Files.createDirectories(path3, new FileAttribute[0]);
            ((ServerLevel)entry5.getValue()).saveDebugReport(path3);
        }
        this.dumpGameRules(path.resolve("gamerules.txt"));
        this.dumpClasspath(path.resolve("classpath.txt"));
        this.dumpCrashCategory(path.resolve("example_crash.txt"));
        this.dumpMiscStats(path.resolve("stats.txt"));
        this.dumpThreads(path.resolve("threads.txt"));
    }
    
    private void dumpMiscStats(final Path path) throws IOException {
        try (final Writer writer3 = (Writer)Files.newBufferedWriter(path, new OpenOption[0])) {
            writer3.write(String.format("pending_tasks: %d\n", new Object[] { this.getPendingTasksCount() }));
            writer3.write(String.format("average_tick_time: %f\n", new Object[] { this.getAverageTickTime() }));
            writer3.write(String.format("tick_times: %s\n", new Object[] { Arrays.toString(this.tickTimes) }));
            writer3.write(String.format("queue: %s\n", new Object[] { Util.backgroundExecutor() }));
        }
    }
    
    private void dumpCrashCategory(final Path path) throws IOException {
        final CrashReport l3 = new CrashReport("Server dump", (Throwable)new Exception("dummy"));
        this.fillReport(l3);
        try (final Writer writer4 = (Writer)Files.newBufferedWriter(path, new OpenOption[0])) {
            writer4.write(l3.getFriendlyReport());
        }
    }
    
    private void dumpGameRules(final Path path) throws IOException {
        try (final Writer writer3 = (Writer)Files.newBufferedWriter(path, new OpenOption[0])) {
            final List<String> list5 = (List<String>)Lists.newArrayList();
            final GameRules brq6 = this.getGameRules();
            GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                public <T extends GameRules.Value<T>> void visit(final GameRules.Key<T> e, final GameRules.Type<T> f) {
                    list5.add(String.format("%s=%s\n", new Object[] { e.getId(), brq6.<T>getRule(e).toString() }));
                }
            });
            for (final String string8 : list5) {
                writer3.write(string8);
            }
        }
    }
    
    private void dumpClasspath(final Path path) throws IOException {
        try (final Writer writer3 = (Writer)Files.newBufferedWriter(path, new OpenOption[0])) {
            final String string5 = System.getProperty("java.class.path");
            final String string6 = System.getProperty("path.separator");
            for (final String string7 : Splitter.on(string6).split((CharSequence)string5)) {
                writer3.write(string7);
                writer3.write("\n");
            }
        }
    }
    
    private void dumpThreads(final Path path) throws IOException {
        final ThreadMXBean threadMXBean3 = ManagementFactory.getThreadMXBean();
        final ThreadInfo[] arr4 = threadMXBean3.dumpAllThreads(true, true);
        Arrays.sort((Object[])arr4, Comparator.comparing(ThreadInfo::getThreadName));
        try (final Writer writer5 = (Writer)Files.newBufferedWriter(path, new OpenOption[0])) {
            for (final ThreadInfo threadInfo10 : arr4) {
                writer5.write(threadInfo10.toString());
                writer5.write(10);
            }
        }
    }
    
    private void startProfilerTick(@Nullable final SingleTickProfiler anw) {
        if (this.delayProfilerStart) {
            this.delayProfilerStart = false;
            this.continousProfiler.enable();
        }
        this.profiler = SingleTickProfiler.decorateFiller(this.continousProfiler.getFiller(), anw);
    }
    
    private void endProfilerTick(@Nullable final SingleTickProfiler anw) {
        if (anw != null) {
            anw.endTick();
        }
        this.profiler = this.continousProfiler.getFiller();
    }
    
    public boolean isProfiling() {
        return this.continousProfiler.isEnabled();
    }
    
    public void startProfiling() {
        this.delayProfilerStart = true;
    }
    
    public ProfileResults finishProfiling() {
        final ProfileResults ans2 = this.continousProfiler.getResults();
        this.continousProfiler.disable();
        return ans2;
    }
    
    public Path getWorldPath(final LevelResource cyb) {
        return this.storageSource.getLevelPath(cyb);
    }
    
    public boolean forceSynchronousWrites() {
        return true;
    }
    
    public StructureManager getStructureManager() {
        return this.structureManager;
    }
    
    public WorldData getWorldData() {
        return this.worldData;
    }
    
    public RegistryAccess registryAccess() {
        return this.registryHolder;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        USERID_CACHE_FILE = new File("usercache.json");
        DEMO_SETTINGS = new LevelSettings("Demo World", GameType.SURVIVAL, false, Difficulty.NORMAL, false, new GameRules(), DataPackConfig.DEFAULT);
    }
}
