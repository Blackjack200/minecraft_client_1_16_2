package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import java.util.stream.Stream;
import com.google.common.collect.Iterables;
import net.minecraft.core.Direction;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.CsvOutput;
import java.io.IOException;
import net.minecraft.world.level.NaturalSpawner;
import java.io.Writer;
import net.minecraft.CrashReport;
import net.minecraft.world.entity.MobCategory;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import java.util.Optional;
import java.util.Objects;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.world.level.ForcedChunksSavedData;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.util.Unit;
import net.minecraft.world.level.saveddata.maps.MapIndex;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagContainer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import javax.annotation.Nonnull;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.Util;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.stream.Collectors;
import net.minecraft.world.entity.LivingEntity;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import java.util.function.BooleanSupplier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.DimensionDataStorage;
import com.google.common.collect.Sets;
import net.minecraft.world.level.TickNextTickData;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Registry;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.Supplier;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.LevelStorageSource;
import java.util.concurrent.Executor;
import net.minecraft.world.level.StructureFeatureManager;
import javax.annotation.Nullable;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.BlockEventData;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import java.util.Set;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.ServerTickList;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.server.MinecraftServer;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.apache.logging.log4j.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.Level;

public class ServerLevel extends Level implements WorldGenLevel {
    public static final BlockPos END_SPAWN_POINT;
    private static final Logger LOGGER;
    private final Int2ObjectMap<Entity> entitiesById;
    private final Map<UUID, Entity> entitiesByUuid;
    private final Queue<Entity> toAddAfterTick;
    private final List<ServerPlayer> players;
    private final ServerChunkCache chunkSource;
    boolean tickingEntities;
    private final MinecraftServer server;
    private final ServerLevelData serverLevelData;
    public boolean noSave;
    private boolean allPlayersSleeping;
    private int emptyTime;
    private final PortalForcer portalForcer;
    private final ServerTickList<Block> blockTicks;
    private final ServerTickList<Fluid> liquidTicks;
    private final Set<PathNavigation> navigations;
    protected final Raids raids;
    private final ObjectLinkedOpenHashSet<BlockEventData> blockEvents;
    private boolean handlingTick;
    private final List<CustomSpawner> customSpawners;
    @Nullable
    private final EndDragonFight dragonFight;
    private final StructureFeatureManager structureFeatureManager;
    private final boolean tickTime;
    
    public ServerLevel(final MinecraftServer minecraftServer, final Executor executor, final LevelStorageSource.LevelStorageAccess a, final ServerLevelData cyj, final ResourceKey<Level> vj, final DimensionType cha, final ChunkProgressListener aap, final ChunkGenerator cfv, final boolean boolean9, final long long10, final List<CustomSpawner> list, final boolean boolean12) {
        super(cyj, vj, cha, (Supplier<ProfilerFiller>)minecraftServer::getProfiler, false, boolean9, long10);
        this.entitiesById = (Int2ObjectMap<Entity>)new Int2ObjectLinkedOpenHashMap();
        this.entitiesByUuid = (Map<UUID, Entity>)Maps.newHashMap();
        this.toAddAfterTick = (Queue<Entity>)Queues.newArrayDeque();
        this.players = (List<ServerPlayer>)Lists.newArrayList();
        this.blockTicks = new ServerTickList<Block>(this, (java.util.function.Predicate<Block>)(bul -> bul == null || bul.defaultBlockState().isAir()), (java.util.function.Function<Block, ResourceLocation>)Registry.BLOCK::getKey, (java.util.function.Consumer<TickNextTickData<Block>>)this::tickBlock);
        this.liquidTicks = new ServerTickList<Fluid>(this, (java.util.function.Predicate<Fluid>)(cut -> cut == null || cut == Fluids.EMPTY), (java.util.function.Function<Fluid, ResourceLocation>)Registry.FLUID::getKey, (java.util.function.Consumer<TickNextTickData<Fluid>>)this::tickLiquid);
        this.navigations = (Set<PathNavigation>)Sets.newHashSet();
        this.blockEvents = (ObjectLinkedOpenHashSet<BlockEventData>)new ObjectLinkedOpenHashSet();
        this.tickTime = boolean12;
        this.server = minecraftServer;
        this.customSpawners = list;
        this.serverLevelData = cyj;
        this.chunkSource = new ServerChunkCache(this, a, minecraftServer.getFixerUpper(), minecraftServer.getStructureManager(), executor, cfv, minecraftServer.getPlayerList().getViewDistance(), minecraftServer.forceSynchronousWrites(), aap, (Supplier<DimensionDataStorage>)(() -> minecraftServer.overworld().getDataStorage()));
        this.portalForcer = new PortalForcer(this);
        this.updateSkyBrightness();
        this.prepareWeather();
        this.getWorldBorder().setAbsoluteMaxSize(minecraftServer.getAbsoluteMaxWorldSize());
        this.raids = this.getDataStorage().<Raids>computeIfAbsent((java.util.function.Supplier<Raids>)(() -> new Raids(this)), Raids.getFileId(this.dimensionType()));
        if (!minecraftServer.isSingleplayer()) {
            cyj.setGameType(minecraftServer.getDefaultGameType());
        }
        this.structureFeatureManager = new StructureFeatureManager(this, minecraftServer.getWorldData().worldGenSettings());
        if (this.dimensionType().createDragonFight()) {
            this.dragonFight = new EndDragonFight(this, minecraftServer.getWorldData().worldGenSettings().seed(), minecraftServer.getWorldData().endDragonFightData());
        }
        else {
            this.dragonFight = null;
        }
    }
    
    public void setWeatherParameters(final int integer1, final int integer2, final boolean boolean3, final boolean boolean4) {
        this.serverLevelData.setClearWeatherTime(integer1);
        this.serverLevelData.setRainTime(integer2);
        this.serverLevelData.setThunderTime(integer2);
        this.serverLevelData.setRaining(boolean3);
        this.serverLevelData.setThundering(boolean4);
    }
    
    public Biome getUncachedNoiseBiome(final int integer1, final int integer2, final int integer3) {
        return this.getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(integer1, integer2, integer3);
    }
    
    public StructureFeatureManager structureFeatureManager() {
        return this.structureFeatureManager;
    }
    
    public void tick(final BooleanSupplier booleanSupplier) {
        final ProfilerFiller ant3 = this.getProfiler();
        this.handlingTick = true;
        ant3.push("world border");
        this.getWorldBorder().tick();
        ant3.popPush("weather");
        final boolean boolean4 = this.isRaining();
        if (this.dimensionType().hasSkyLight()) {
            if (this.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
                int integer5 = this.serverLevelData.getClearWeatherTime();
                int integer6 = this.serverLevelData.getThunderTime();
                int integer7 = this.serverLevelData.getRainTime();
                boolean boolean5 = this.levelData.isThundering();
                boolean boolean6 = this.levelData.isRaining();
                if (integer5 > 0) {
                    --integer5;
                    integer6 = (boolean5 ? 0 : 1);
                    integer7 = (boolean6 ? 0 : 1);
                    boolean5 = false;
                    boolean6 = false;
                }
                else {
                    if (integer6 > 0) {
                        if (--integer6 == 0) {
                            boolean5 = !boolean5;
                        }
                    }
                    else if (boolean5) {
                        integer6 = this.random.nextInt(12000) + 3600;
                    }
                    else {
                        integer6 = this.random.nextInt(168000) + 12000;
                    }
                    if (integer7 > 0) {
                        if (--integer7 == 0) {
                            boolean6 = !boolean6;
                        }
                    }
                    else if (boolean6) {
                        integer7 = this.random.nextInt(12000) + 12000;
                    }
                    else {
                        integer7 = this.random.nextInt(168000) + 12000;
                    }
                }
                this.serverLevelData.setThunderTime(integer6);
                this.serverLevelData.setRainTime(integer7);
                this.serverLevelData.setClearWeatherTime(integer5);
                this.serverLevelData.setThundering(boolean5);
                this.serverLevelData.setRaining(boolean6);
            }
            this.oThunderLevel = this.thunderLevel;
            if (this.levelData.isThundering()) {
                this.thunderLevel += (float)0.01;
            }
            else {
                this.thunderLevel -= (float)0.01;
            }
            this.thunderLevel = Mth.clamp(this.thunderLevel, 0.0f, 1.0f);
            this.oRainLevel = this.rainLevel;
            if (this.levelData.isRaining()) {
                this.rainLevel += (float)0.01;
            }
            else {
                this.rainLevel -= (float)0.01;
            }
            this.rainLevel = Mth.clamp(this.rainLevel, 0.0f, 1.0f);
        }
        if (this.oRainLevel != this.rainLevel) {
            this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel), this.dimension());
        }
        if (this.oThunderLevel != this.thunderLevel) {
            this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel), this.dimension());
        }
        if (boolean4 != this.isRaining()) {
            if (boolean4) {
                this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0f));
            }
            else {
                this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0f));
            }
            this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel));
            this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel));
        }
        if (this.allPlayersSleeping && this.players.stream().noneMatch(aah -> !aah.isSpectator() && !aah.isSleepingLongEnough())) {
            this.allPlayersSleeping = false;
            if (this.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                final long long5 = this.levelData.getDayTime() + 24000L;
                this.setDayTime(long5 - long5 % 24000L);
            }
            this.wakeUpAllPlayers();
            if (this.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
                this.stopWeather();
            }
        }
        this.updateSkyBrightness();
        this.tickTime();
        ant3.popPush("chunkSource");
        this.getChunkSource().tick(booleanSupplier);
        ant3.popPush("tickPending");
        if (!this.isDebug()) {
            this.blockTicks.tick();
            this.liquidTicks.tick();
        }
        ant3.popPush("raid");
        this.raids.tick();
        ant3.popPush("blockEvents");
        this.runBlockEvents();
        this.handlingTick = false;
        ant3.popPush("entities");
        final boolean boolean7 = !this.players.isEmpty() || !this.getForcedChunks().isEmpty();
        if (boolean7) {
            this.resetEmptyTime();
        }
        if (boolean7 || this.emptyTime++ < 300) {
            if (this.dragonFight != null) {
                this.dragonFight.tick();
            }
            this.tickingEntities = true;
            final ObjectIterator<Int2ObjectMap.Entry<Entity>> objectIterator6 = (ObjectIterator<Int2ObjectMap.Entry<Entity>>)this.entitiesById.int2ObjectEntrySet().iterator();
            while (objectIterator6.hasNext()) {
                final Int2ObjectMap.Entry<Entity> entry7 = (Int2ObjectMap.Entry<Entity>)objectIterator6.next();
                final Entity apx8 = (Entity)entry7.getValue();
                final Entity apx9 = apx8.getVehicle();
                if (!this.server.isSpawningAnimals() && (apx8 instanceof Animal || apx8 instanceof WaterAnimal)) {
                    apx8.remove();
                }
                if (!this.server.areNpcsEnabled() && apx8 instanceof Npc) {
                    apx8.remove();
                }
                ant3.push("checkDespawn");
                if (!apx8.removed) {
                    apx8.checkDespawn();
                }
                ant3.pop();
                if (apx9 != null) {
                    if (!apx9.removed && apx9.hasPassenger(apx8)) {
                        continue;
                    }
                    apx8.stopRiding();
                }
                ant3.push("tick");
                if (!apx8.removed && !(apx8 instanceof EnderDragonPart)) {
                    this.guardEntityTick((Consumer<Entity>)this::tickNonPassenger, apx8);
                }
                ant3.pop();
                ant3.push("remove");
                if (apx8.removed) {
                    this.removeFromChunk(apx8);
                    objectIterator6.remove();
                    this.onEntityRemoved(apx8);
                }
                ant3.pop();
            }
            this.tickingEntities = false;
            Entity apx10;
            while ((apx10 = (Entity)this.toAddAfterTick.poll()) != null) {
                this.add(apx10);
            }
            this.tickBlockEntities();
        }
        ant3.pop();
    }
    
    protected void tickTime() {
        if (!this.tickTime) {
            return;
        }
        final long long2 = this.levelData.getGameTime() + 1L;
        this.serverLevelData.setGameTime(long2);
        this.serverLevelData.getScheduledEvents().tick(this.server, long2);
        if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            this.setDayTime(this.levelData.getDayTime() + 1L);
        }
    }
    
    public void setDayTime(final long long1) {
        this.serverLevelData.setDayTime(long1);
    }
    
    public void tickCustomSpawners(final boolean boolean1, final boolean boolean2) {
        for (final CustomSpawner brg5 : this.customSpawners) {
            brg5.tick(this, boolean1, boolean2);
        }
    }
    
    private void wakeUpAllPlayers() {
        ((List)this.players.stream().filter(LivingEntity::isSleeping).collect(Collectors.toList())).forEach(aah -> aah.stopSleepInBed(false, false));
    }
    
    public void tickChunk(final LevelChunk cge, final int integer) {
        final ChunkPos bra4 = cge.getPos();
        final boolean boolean5 = this.isRaining();
        final int integer2 = bra4.getMinBlockX();
        final int integer3 = bra4.getMinBlockZ();
        final ProfilerFiller ant8 = this.getProfiler();
        ant8.push("thunder");
        if (boolean5 && this.isThundering() && this.random.nextInt(100000) == 0) {
            final BlockPos fx9 = this.findLightingTargetAround(this.getBlockRandomPos(integer2, 0, integer3, 15));
            if (this.isRainingAt(fx9)) {
                final DifficultyInstance aop10 = this.getCurrentDifficultyAt(fx9);
                final boolean boolean6 = this.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && this.random.nextDouble() < aop10.getEffectiveDifficulty() * 0.01;
                if (boolean6) {
                    final SkeletonHorse bbe12 = EntityType.SKELETON_HORSE.create(this);
                    bbe12.setTrap(true);
                    bbe12.setAge(0);
                    bbe12.setPos(fx9.getX(), fx9.getY(), fx9.getZ());
                    this.addFreshEntity(bbe12);
                }
                final LightningBolt aqi12 = EntityType.LIGHTNING_BOLT.create(this);
                aqi12.moveTo(Vec3.atBottomCenterOf(fx9));
                aqi12.setVisualOnly(boolean6);
                this.addFreshEntity(aqi12);
            }
        }
        ant8.popPush("iceandsnow");
        if (this.random.nextInt(16) == 0) {
            final BlockPos fx9 = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, this.getBlockRandomPos(integer2, 0, integer3, 15));
            final BlockPos fx10 = fx9.below();
            final Biome bss11 = this.getBiome(fx9);
            if (bss11.shouldFreeze(this, fx10)) {
                this.setBlockAndUpdate(fx10, Blocks.ICE.defaultBlockState());
            }
            if (boolean5 && bss11.shouldSnow(this, fx9)) {
                this.setBlockAndUpdate(fx9, Blocks.SNOW.defaultBlockState());
            }
            if (boolean5 && this.getBiome(fx10).getPrecipitation() == Biome.Precipitation.RAIN) {
                this.getBlockState(fx10).getBlock().handleRain(this, fx10);
            }
        }
        ant8.popPush("tickBlocks");
        if (integer > 0) {
            for (final LevelChunkSection cgf12 : cge.getSections()) {
                if (cgf12 != LevelChunk.EMPTY_SECTION && cgf12.isRandomlyTicking()) {
                    final int integer4 = cgf12.bottomBlockY();
                    for (int integer5 = 0; integer5 < integer; ++integer5) {
                        final BlockPos fx11 = this.getBlockRandomPos(integer2, integer4, integer3, 15);
                        ant8.push("randomTick");
                        final BlockState cee16 = cgf12.getBlockState(fx11.getX() - integer2, fx11.getY() - integer4, fx11.getZ() - integer3);
                        if (cee16.isRandomlyTicking()) {
                            cee16.randomTick(this, fx11, this.random);
                        }
                        final FluidState cuu17 = cee16.getFluidState();
                        if (cuu17.isRandomlyTicking()) {
                            cuu17.randomTick(this, fx11, this.random);
                        }
                        ant8.pop();
                    }
                }
            }
        }
        ant8.pop();
    }
    
    protected BlockPos findLightingTargetAround(final BlockPos fx) {
        BlockPos fx2 = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, fx);
        final AABB dcf4 = new AABB(fx2, new BlockPos(fx2.getX(), this.getMaxBuildHeight(), fx2.getZ())).inflate(3.0);
        final List<LivingEntity> list5 = this.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, dcf4, (java.util.function.Predicate<? super LivingEntity>)(aqj -> aqj != null && aqj.isAlive() && this.canSeeSky(aqj.blockPosition())));
        if (!list5.isEmpty()) {
            return ((LivingEntity)list5.get(this.random.nextInt(list5.size()))).blockPosition();
        }
        if (fx2.getY() == -1) {
            fx2 = fx2.above(2);
        }
        return fx2;
    }
    
    public boolean isHandlingTick() {
        return this.handlingTick;
    }
    
    public void updateSleepingPlayerList() {
        this.allPlayersSleeping = false;
        if (!this.players.isEmpty()) {
            int integer2 = 0;
            int integer3 = 0;
            for (final ServerPlayer aah5 : this.players) {
                if (aah5.isSpectator()) {
                    ++integer2;
                }
                else {
                    if (!aah5.isSleeping()) {
                        continue;
                    }
                    ++integer3;
                }
            }
            this.allPlayersSleeping = (integer3 > 0 && integer3 >= this.players.size() - integer2);
        }
    }
    
    @Override
    public ServerScoreboard getScoreboard() {
        return this.server.getScoreboard();
    }
    
    private void stopWeather() {
        this.serverLevelData.setRainTime(0);
        this.serverLevelData.setRaining(false);
        this.serverLevelData.setThunderTime(0);
        this.serverLevelData.setThundering(false);
    }
    
    public void resetEmptyTime() {
        this.emptyTime = 0;
    }
    
    private void tickLiquid(final TickNextTickData<Fluid> bsm) {
        final FluidState cuu3 = this.getFluidState(bsm.pos);
        if (cuu3.getType() == bsm.getType()) {
            cuu3.tick(this, bsm.pos);
        }
    }
    
    private void tickBlock(final TickNextTickData<Block> bsm) {
        final BlockState cee3 = this.getBlockState(bsm.pos);
        if (cee3.is(bsm.getType())) {
            cee3.tick(this, bsm.pos, this.random);
        }
    }
    
    public void tickNonPassenger(final Entity apx) {
        if (!(apx instanceof Player) && !this.getChunkSource().isEntityTickingChunk(apx)) {
            this.updateChunkPos(apx);
            return;
        }
        apx.setPosAndOldPos(apx.getX(), apx.getY(), apx.getZ());
        apx.yRotO = apx.yRot;
        apx.xRotO = apx.xRot;
        if (apx.inChunk) {
            ++apx.tickCount;
            final ProfilerFiller ant3 = this.getProfiler();
            ant3.push((Supplier<String>)(() -> Registry.ENTITY_TYPE.getKey(apx.getType()).toString()));
            ant3.incrementCounter("tickNonPassenger");
            apx.tick();
            ant3.pop();
        }
        this.updateChunkPos(apx);
        if (apx.inChunk) {
            for (final Entity apx2 : apx.getPassengers()) {
                this.tickPassenger(apx, apx2);
            }
        }
    }
    
    public void tickPassenger(final Entity apx1, final Entity apx2) {
        if (apx2.removed || apx2.getVehicle() != apx1) {
            apx2.stopRiding();
            return;
        }
        if (!(apx2 instanceof Player) && !this.getChunkSource().isEntityTickingChunk(apx2)) {
            return;
        }
        apx2.setPosAndOldPos(apx2.getX(), apx2.getY(), apx2.getZ());
        apx2.yRotO = apx2.yRot;
        apx2.xRotO = apx2.xRot;
        if (apx2.inChunk) {
            ++apx2.tickCount;
            final ProfilerFiller ant4 = this.getProfiler();
            ant4.push((Supplier<String>)(() -> Registry.ENTITY_TYPE.getKey(apx2.getType()).toString()));
            ant4.incrementCounter("tickPassenger");
            apx2.rideTick();
            ant4.pop();
        }
        this.updateChunkPos(apx2);
        if (apx2.inChunk) {
            for (final Entity apx3 : apx2.getPassengers()) {
                this.tickPassenger(apx2, apx3);
            }
        }
    }
    
    public void updateChunkPos(final Entity apx) {
        if (!apx.checkAndResetUpdateChunkPos()) {
            return;
        }
        this.getProfiler().push("chunkCheck");
        final int integer3 = Mth.floor(apx.getX() / 16.0);
        final int integer4 = Mth.floor(apx.getY() / 16.0);
        final int integer5 = Mth.floor(apx.getZ() / 16.0);
        if (!apx.inChunk || apx.xChunk != integer3 || apx.yChunk != integer4 || apx.zChunk != integer5) {
            if (apx.inChunk && this.hasChunk(apx.xChunk, apx.zChunk)) {
                this.getChunk(apx.xChunk, apx.zChunk).removeEntity(apx, apx.yChunk);
            }
            if (apx.checkAndResetForcedChunkAdditionFlag() || this.hasChunk(integer3, integer5)) {
                this.getChunk(integer3, integer5).addEntity(apx);
            }
            else {
                if (apx.inChunk) {
                    ServerLevel.LOGGER.warn("Entity {} left loaded chunk area", apx);
                }
                apx.inChunk = false;
            }
        }
        this.getProfiler().pop();
    }
    
    @Override
    public boolean mayInteract(final Player bft, final BlockPos fx) {
        return !this.server.isUnderSpawnProtection(this, fx, bft) && this.getWorldBorder().isWithinBounds(fx);
    }
    
    public void save(@Nullable final ProgressListener afk, final boolean boolean2, final boolean boolean3) {
        final ServerChunkCache aae5 = this.getChunkSource();
        if (boolean3) {
            return;
        }
        if (afk != null) {
            afk.progressStartNoAbort(new TranslatableComponent("menu.savingLevel"));
        }
        this.saveLevelData();
        if (afk != null) {
            afk.progressStage(new TranslatableComponent("menu.savingChunks"));
        }
        aae5.save(boolean2);
    }
    
    private void saveLevelData() {
        if (this.dragonFight != null) {
            this.server.getWorldData().setEndDragonFightData(this.dragonFight.saveData());
        }
        this.getChunkSource().getDataStorage().save();
    }
    
    public List<Entity> getEntities(@Nullable final EntityType<?> aqb, final Predicate<? super Entity> predicate) {
        final List<Entity> list4 = (List<Entity>)Lists.newArrayList();
        final ServerChunkCache aae5 = this.getChunkSource();
        for (final Entity apx7 : this.entitiesById.values()) {
            if ((aqb == null || apx7.getType() == aqb) && aae5.hasChunk(Mth.floor(apx7.getX()) >> 4, Mth.floor(apx7.getZ()) >> 4) && predicate.test(apx7)) {
                list4.add(apx7);
            }
        }
        return list4;
    }
    
    public List<EnderDragon> getDragons() {
        final List<EnderDragon> list2 = (List<EnderDragon>)Lists.newArrayList();
        for (final Entity apx4 : this.entitiesById.values()) {
            if (apx4 instanceof EnderDragon && apx4.isAlive()) {
                list2.add(apx4);
            }
        }
        return list2;
    }
    
    public List<ServerPlayer> getPlayers(final Predicate<? super ServerPlayer> predicate) {
        final List<ServerPlayer> list3 = (List<ServerPlayer>)Lists.newArrayList();
        for (final ServerPlayer aah5 : this.players) {
            if (predicate.test(aah5)) {
                list3.add(aah5);
            }
        }
        return list3;
    }
    
    @Nullable
    public ServerPlayer getRandomPlayer() {
        final List<ServerPlayer> list2 = this.getPlayers(LivingEntity::isAlive);
        if (list2.isEmpty()) {
            return null;
        }
        return (ServerPlayer)list2.get(this.random.nextInt(list2.size()));
    }
    
    public boolean addFreshEntity(final Entity apx) {
        return this.addEntity(apx);
    }
    
    public boolean addWithUUID(final Entity apx) {
        return this.addEntity(apx);
    }
    
    public void addFromAnotherDimension(final Entity apx) {
        final boolean boolean3 = apx.forcedLoading;
        apx.forcedLoading = true;
        this.addWithUUID(apx);
        apx.forcedLoading = boolean3;
        this.updateChunkPos(apx);
    }
    
    public void addDuringCommandTeleport(final ServerPlayer aah) {
        this.addPlayer(aah);
        this.updateChunkPos(aah);
    }
    
    public void addDuringPortalTeleport(final ServerPlayer aah) {
        this.addPlayer(aah);
        this.updateChunkPos(aah);
    }
    
    public void addNewPlayer(final ServerPlayer aah) {
        this.addPlayer(aah);
    }
    
    public void addRespawnedPlayer(final ServerPlayer aah) {
        this.addPlayer(aah);
    }
    
    private void addPlayer(final ServerPlayer aah) {
        final Entity apx3 = (Entity)this.entitiesByUuid.get(aah.getUUID());
        if (apx3 != null) {
            ServerLevel.LOGGER.warn("Force-added player with duplicate UUID {}", aah.getUUID().toString());
            apx3.unRide();
            this.removePlayerImmediately((ServerPlayer)apx3);
        }
        this.players.add(aah);
        this.updateSleepingPlayerList();
        final ChunkAccess cft4 = this.getChunk(Mth.floor(aah.getX() / 16.0), Mth.floor(aah.getZ() / 16.0), ChunkStatus.FULL, true);
        if (cft4 instanceof LevelChunk) {
            cft4.addEntity(aah);
        }
        this.add(aah);
    }
    
    private boolean addEntity(final Entity apx) {
        if (apx.removed) {
            ServerLevel.LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityType.getKey(apx.getType()));
            return false;
        }
        if (this.isUUIDUsed(apx)) {
            return false;
        }
        final ChunkAccess cft3 = this.getChunk(Mth.floor(apx.getX() / 16.0), Mth.floor(apx.getZ() / 16.0), ChunkStatus.FULL, apx.forcedLoading);
        if (!(cft3 instanceof LevelChunk)) {
            return false;
        }
        cft3.addEntity(apx);
        this.add(apx);
        return true;
    }
    
    public boolean loadFromChunk(final Entity apx) {
        if (this.isUUIDUsed(apx)) {
            return false;
        }
        this.add(apx);
        return true;
    }
    
    private boolean isUUIDUsed(final Entity apx) {
        final UUID uUID3 = apx.getUUID();
        final Entity apx2 = this.findAddedOrPendingEntity(uUID3);
        if (apx2 == null) {
            return false;
        }
        ServerLevel.LOGGER.warn("Trying to add entity with duplicated UUID {}. Existing {}#{}, new: {}#{}", uUID3, EntityType.getKey(apx2.getType()), apx2.getId(), EntityType.getKey(apx.getType()), apx.getId());
        return true;
    }
    
    @Nullable
    private Entity findAddedOrPendingEntity(final UUID uUID) {
        final Entity apx3 = (Entity)this.entitiesByUuid.get(uUID);
        if (apx3 != null) {
            return apx3;
        }
        if (this.tickingEntities) {
            for (final Entity apx4 : this.toAddAfterTick) {
                if (apx4.getUUID().equals(uUID)) {
                    return apx4;
                }
            }
        }
        return null;
    }
    
    public boolean tryAddFreshEntityWithPassengers(final Entity apx) {
        if (apx.getSelfAndPassengers().anyMatch(this::isUUIDUsed)) {
            return false;
        }
        this.addFreshEntityWithPassengers(apx);
        return true;
    }
    
    public void unload(final LevelChunk cge) {
        this.blockEntitiesToUnload.addAll(cge.getBlockEntities().values());
        for (final ClassInstanceMultiMap<Entity> aeq6 : cge.getEntitySections()) {
            for (final Entity apx8 : aeq6) {
                if (apx8 instanceof ServerPlayer) {
                    continue;
                }
                if (this.tickingEntities) {
                    throw Util.<IllegalStateException>pauseInIde(new IllegalStateException("Removing entity while ticking!"));
                }
                this.entitiesById.remove(apx8.getId());
                this.onEntityRemoved(apx8);
            }
        }
    }
    
    public void onEntityRemoved(final Entity apx) {
        if (apx instanceof EnderDragon) {
            for (final EnderDragonPart bbm6 : ((EnderDragon)apx).getSubEntities()) {
                bbm6.remove();
            }
        }
        this.entitiesByUuid.remove(apx.getUUID());
        this.getChunkSource().removeEntity(apx);
        if (apx instanceof ServerPlayer) {
            final ServerPlayer aah3 = (ServerPlayer)apx;
            this.players.remove(aah3);
        }
        this.getScoreboard().entityRemoved(apx);
        if (apx instanceof Mob) {
            this.navigations.remove(((Mob)apx).getNavigation());
        }
    }
    
    private void add(final Entity apx) {
        if (this.tickingEntities) {
            this.toAddAfterTick.add(apx);
        }
        else {
            this.entitiesById.put(apx.getId(), apx);
            if (apx instanceof EnderDragon) {
                for (final EnderDragonPart bbm6 : ((EnderDragon)apx).getSubEntities()) {
                    this.entitiesById.put(bbm6.getId(), bbm6);
                }
            }
            this.entitiesByUuid.put(apx.getUUID(), apx);
            this.getChunkSource().addEntity(apx);
            if (apx instanceof Mob) {
                this.navigations.add(((Mob)apx).getNavigation());
            }
        }
    }
    
    public void despawn(final Entity apx) {
        if (this.tickingEntities) {
            throw Util.<IllegalStateException>pauseInIde(new IllegalStateException("Removing entity while ticking!"));
        }
        this.removeFromChunk(apx);
        this.entitiesById.remove(apx.getId());
        this.onEntityRemoved(apx);
    }
    
    private void removeFromChunk(final Entity apx) {
        final ChunkAccess cft3 = this.getChunk(apx.xChunk, apx.zChunk, ChunkStatus.FULL, false);
        if (cft3 instanceof LevelChunk) {
            ((LevelChunk)cft3).removeEntity(apx);
        }
    }
    
    public void removePlayerImmediately(final ServerPlayer aah) {
        aah.remove();
        this.despawn(aah);
        this.updateSleepingPlayerList();
    }
    
    @Override
    public void destroyBlockProgress(final int integer1, final BlockPos fx, final int integer3) {
        for (final ServerPlayer aah6 : this.server.getPlayerList().getPlayers()) {
            if (aah6 != null && aah6.level == this) {
                if (aah6.getId() == integer1) {
                    continue;
                }
                final double double7 = fx.getX() - aah6.getX();
                final double double8 = fx.getY() - aah6.getY();
                final double double9 = fx.getZ() - aah6.getZ();
                if (double7 * double7 + double8 * double8 + double9 * double9 >= 1024.0) {
                    continue;
                }
                aah6.connection.send(new ClientboundBlockDestructionPacket(integer1, fx, integer3));
            }
        }
    }
    
    @Override
    public void playSound(@Nullable final Player bft, final double double2, final double double3, final double double4, final SoundEvent adn, final SoundSource adp, final float float7, final float float8) {
        this.server.getPlayerList().broadcast(bft, double2, double3, double4, (float7 > 1.0f) ? ((double)(16.0f * float7)) : 16.0, this.dimension(), new ClientboundSoundPacket(adn, adp, double2, double3, double4, float7, float8));
    }
    
    @Override
    public void playSound(@Nullable final Player bft, final Entity apx, final SoundEvent adn, final SoundSource adp, final float float5, final float float6) {
        this.server.getPlayerList().broadcast(bft, apx.getX(), apx.getY(), apx.getZ(), (float5 > 1.0f) ? ((double)(16.0f * float5)) : 16.0, this.dimension(), new ClientboundSoundEntityPacket(adn, adp, apx, float5, float6));
    }
    
    @Override
    public void globalLevelEvent(final int integer1, final BlockPos fx, final int integer3) {
        this.server.getPlayerList().broadcastAll(new ClientboundLevelEventPacket(integer1, fx, integer3, true));
    }
    
    public void levelEvent(@Nullable final Player bft, final int integer2, final BlockPos fx, final int integer4) {
        this.server.getPlayerList().broadcast(bft, fx.getX(), fx.getY(), fx.getZ(), 64.0, this.dimension(), new ClientboundLevelEventPacket(integer2, fx, integer4, false));
    }
    
    @Override
    public void sendBlockUpdated(final BlockPos fx, final BlockState cee2, final BlockState cee3, final int integer) {
        this.getChunkSource().blockChanged(fx);
        final VoxelShape dde6 = cee2.getCollisionShape(this, fx);
        final VoxelShape dde7 = cee3.getCollisionShape(this, fx);
        if (!Shapes.joinIsNotEmpty(dde6, dde7, BooleanOp.NOT_SAME)) {
            return;
        }
        for (final PathNavigation ayg9 : this.navigations) {
            if (ayg9.hasDelayedRecomputation()) {
                continue;
            }
            ayg9.recomputePath(fx);
        }
    }
    
    @Override
    public void broadcastEntityEvent(final Entity apx, final byte byte2) {
        this.getChunkSource().broadcastAndSend(apx, new ClientboundEntityEventPacket(apx, byte2));
    }
    
    public ServerChunkCache getChunkSource() {
        return this.chunkSource;
    }
    
    @Override
    public Explosion explode(@Nullable final Entity apx, @Nullable final DamageSource aph, @Nullable final ExplosionDamageCalculator brn, final double double4, final double double5, final double double6, final float float7, final boolean boolean8, final Explosion.BlockInteraction a) {
        final Explosion brm14 = new Explosion(this, apx, aph, brn, double4, double5, double6, float7, boolean8, a);
        brm14.explode();
        brm14.finalizeExplosion(false);
        if (a == Explosion.BlockInteraction.NONE) {
            brm14.clearToBlow();
        }
        for (final ServerPlayer aah16 : this.players) {
            if (aah16.distanceToSqr(double4, double5, double6) < 4096.0) {
                aah16.connection.send(new ClientboundExplodePacket(double4, double5, double6, float7, brm14.getToBlow(), (Vec3)brm14.getHitPlayers().get(aah16)));
            }
        }
        return brm14;
    }
    
    @Override
    public void blockEvent(final BlockPos fx, final Block bul, final int integer3, final int integer4) {
        this.blockEvents.add(new BlockEventData(fx, bul, integer3, integer4));
    }
    
    private void runBlockEvents() {
        while (!this.blockEvents.isEmpty()) {
            final BlockEventData bqy2 = (BlockEventData)this.blockEvents.removeFirst();
            if (this.doBlockEvent(bqy2)) {
                this.server.getPlayerList().broadcast(null, bqy2.getPos().getX(), bqy2.getPos().getY(), bqy2.getPos().getZ(), 64.0, this.dimension(), new ClientboundBlockEventPacket(bqy2.getPos(), bqy2.getBlock(), bqy2.getParamA(), bqy2.getParamB()));
            }
        }
    }
    
    private boolean doBlockEvent(final BlockEventData bqy) {
        final BlockState cee3 = this.getBlockState(bqy.getPos());
        return cee3.is(bqy.getBlock()) && cee3.triggerEvent(this, bqy.getPos(), bqy.getParamA(), bqy.getParamB());
    }
    
    public ServerTickList<Block> getBlockTicks() {
        return this.blockTicks;
    }
    
    public ServerTickList<Fluid> getLiquidTicks() {
        return this.liquidTicks;
    }
    
    @Nonnull
    @Override
    public MinecraftServer getServer() {
        return this.server;
    }
    
    public PortalForcer getPortalForcer() {
        return this.portalForcer;
    }
    
    public StructureManager getStructureManager() {
        return this.server.getStructureManager();
    }
    
    public <T extends ParticleOptions> int sendParticles(final T hf, final double double2, final double double3, final double double4, final int integer, final double double6, final double double7, final double double8, final double double9) {
        final ClientboundLevelParticlesPacket pv18 = new ClientboundLevelParticlesPacket((T)hf, false, double2, double3, double4, (float)double6, (float)double7, (float)double8, (float)double9, integer);
        int integer2 = 0;
        for (int integer3 = 0; integer3 < this.players.size(); ++integer3) {
            final ServerPlayer aah21 = (ServerPlayer)this.players.get(integer3);
            if (this.sendParticles(aah21, false, double2, double3, double4, pv18)) {
                ++integer2;
            }
        }
        return integer2;
    }
    
    public <T extends ParticleOptions> boolean sendParticles(final ServerPlayer aah, final T hf, final boolean boolean3, final double double4, final double double5, final double double6, final int integer, final double double8, final double double9, final double double10, final double double11) {
        final Packet<?> oj20 = new ClientboundLevelParticlesPacket((T)hf, boolean3, double4, double5, double6, (float)double8, (float)double9, (float)double10, (float)double11, integer);
        return this.sendParticles(aah, boolean3, double4, double5, double6, oj20);
    }
    
    private boolean sendParticles(final ServerPlayer aah, final boolean boolean2, final double double3, final double double4, final double double5, final Packet<?> oj) {
        if (aah.getLevel() != this) {
            return false;
        }
        final BlockPos fx11 = aah.blockPosition();
        if (fx11.closerThan(new Vec3(double3, double4, double5), boolean2 ? 512.0 : 32.0)) {
            aah.connection.send(oj);
            return true;
        }
        return false;
    }
    
    @Nullable
    @Override
    public Entity getEntity(final int integer) {
        return (Entity)this.entitiesById.get(integer);
    }
    
    @Nullable
    public Entity getEntity(final UUID uUID) {
        return (Entity)this.entitiesByUuid.get(uUID);
    }
    
    @Nullable
    public BlockPos findNearestMapFeature(final StructureFeature<?> ckx, final BlockPos fx, final int integer, final boolean boolean4) {
        if (!this.server.getWorldData().worldGenSettings().generateFeatures()) {
            return null;
        }
        return this.getChunkSource().getGenerator().findNearestMapFeature(this, ckx, fx, integer, boolean4);
    }
    
    @Nullable
    public BlockPos findNearestBiome(final Biome bss, final BlockPos fx, final int integer3, final int integer4) {
        return this.getChunkSource().getGenerator().getBiomeSource().findBiomeHorizontal(fx.getX(), fx.getY(), fx.getZ(), integer3, integer4, (Predicate<Biome>)(bss2 -> bss2 == bss), this.random, true);
    }
    
    @Override
    public RecipeManager getRecipeManager() {
        return this.server.getRecipeManager();
    }
    
    @Override
    public TagContainer getTagManager() {
        return this.server.getTags();
    }
    
    @Override
    public boolean noSave() {
        return this.noSave;
    }
    
    public RegistryAccess registryAccess() {
        return this.server.registryAccess();
    }
    
    public DimensionDataStorage getDataStorage() {
        return this.getChunkSource().getDataStorage();
    }
    
    @Nullable
    @Override
    public MapItemSavedData getMapData(final String string) {
        return this.getServer().overworld().getDataStorage().<MapItemSavedData>get((java.util.function.Supplier<MapItemSavedData>)(() -> new MapItemSavedData(string)), string);
    }
    
    @Override
    public void setMapData(final MapItemSavedData cxu) {
        this.getServer().overworld().getDataStorage().set(cxu);
    }
    
    @Override
    public int getFreeMapId() {
        return this.getServer().overworld().getDataStorage().<MapIndex>computeIfAbsent((java.util.function.Supplier<MapIndex>)MapIndex::new, "idcounts").getFreeAuxValueForMap();
    }
    
    public void setDefaultSpawnPos(final BlockPos fx, final float float2) {
        final ChunkPos bra4 = new ChunkPos(new BlockPos(this.levelData.getXSpawn(), 0, this.levelData.getZSpawn()));
        this.levelData.setSpawn(fx, float2);
        this.getChunkSource().<Unit>removeRegionTicket(TicketType.START, bra4, 11, Unit.INSTANCE);
        this.getChunkSource().<Unit>addRegionTicket(TicketType.START, new ChunkPos(fx), 11, Unit.INSTANCE);
        this.getServer().getPlayerList().broadcastAll(new ClientboundSetDefaultSpawnPositionPacket(fx, float2));
    }
    
    public BlockPos getSharedSpawnPos() {
        BlockPos fx2 = new BlockPos(this.levelData.getXSpawn(), this.levelData.getYSpawn(), this.levelData.getZSpawn());
        if (!this.getWorldBorder().isWithinBounds(fx2)) {
            fx2 = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return fx2;
    }
    
    public float getSharedSpawnAngle() {
        return this.levelData.getSpawnAngle();
    }
    
    public LongSet getForcedChunks() {
        final ForcedChunksSavedData brp2 = this.getDataStorage().<ForcedChunksSavedData>get((java.util.function.Supplier<ForcedChunksSavedData>)ForcedChunksSavedData::new, "chunks");
        return (LongSet)((brp2 != null) ? LongSets.unmodifiable(brp2.getChunks()) : LongSets.EMPTY_SET);
    }
    
    public boolean setChunkForced(final int integer1, final int integer2, final boolean boolean3) {
        final ForcedChunksSavedData brp5 = this.getDataStorage().<ForcedChunksSavedData>computeIfAbsent((java.util.function.Supplier<ForcedChunksSavedData>)ForcedChunksSavedData::new, "chunks");
        final ChunkPos bra6 = new ChunkPos(integer1, integer2);
        final long long7 = bra6.toLong();
        boolean boolean4;
        if (boolean3) {
            boolean4 = brp5.getChunks().add(long7);
            if (boolean4) {
                this.getChunk(integer1, integer2);
            }
        }
        else {
            boolean4 = brp5.getChunks().remove(long7);
        }
        brp5.setDirty(boolean4);
        if (boolean4) {
            this.getChunkSource().updateChunkForced(bra6, boolean3);
        }
        return boolean4;
    }
    
    public List<ServerPlayer> players() {
        return this.players;
    }
    
    @Override
    public void onBlockStateChange(final BlockPos fx, final BlockState cee2, final BlockState cee3) {
        final Optional<PoiType> optional5 = PoiType.forState(cee2);
        final Optional<PoiType> optional6 = PoiType.forState(cee3);
        if (Objects.equals(optional5, optional6)) {
            return;
        }
        final BlockPos fx2 = fx.immutable();
        optional5.ifPresent(azo -> this.getServer().execute(() -> {
            this.getPoiManager().remove(fx2);
            DebugPackets.sendPoiRemovedPacket(this, fx2);
        }));
        optional6.ifPresent(azo -> this.getServer().execute(() -> {
            this.getPoiManager().add(fx2, azo);
            DebugPackets.sendPoiAddedPacket(this, fx2);
        }));
    }
    
    public PoiManager getPoiManager() {
        return this.getChunkSource().getPoiManager();
    }
    
    public boolean isVillage(final BlockPos fx) {
        return this.isCloseToVillage(fx, 1);
    }
    
    public boolean isVillage(final SectionPos gp) {
        return this.isVillage(gp.center());
    }
    
    public boolean isCloseToVillage(final BlockPos fx, final int integer) {
        return integer <= 6 && this.sectionsToVillage(SectionPos.of(fx)) <= integer;
    }
    
    public int sectionsToVillage(final SectionPos gp) {
        return this.getPoiManager().sectionsToVillage(gp);
    }
    
    public Raids getRaids() {
        return this.raids;
    }
    
    @Nullable
    public Raid getRaidAt(final BlockPos fx) {
        return this.raids.getNearbyRaid(fx, 9216);
    }
    
    public boolean isRaided(final BlockPos fx) {
        return this.getRaidAt(fx) != null;
    }
    
    public void onReputationEvent(final ReputationEventType azi, final Entity apx, final ReputationEventHandler aqw) {
        aqw.onReputationEventFrom(azi, apx);
    }
    
    public void saveDebugReport(final Path path) throws IOException {
        final ChunkMap zs3 = this.getChunkSource().chunkMap;
        try (final Writer writer4 = (Writer)Files.newBufferedWriter(path.resolve("stats.txt"), new OpenOption[0])) {
            writer4.write(String.format("spawning_chunks: %d\n", new Object[] { zs3.getDistanceManager().getNaturalSpawnChunkCount() }));
            final NaturalSpawner.SpawnState d6 = this.getChunkSource().getLastSpawnState();
            if (d6 != null) {
                for (final Object2IntMap.Entry<MobCategory> entry8 : d6.getMobCategoryCounts().object2IntEntrySet()) {
                    writer4.write(String.format("spawn_count.%s: %d\n", new Object[] { ((MobCategory)entry8.getKey()).getName(), entry8.getIntValue() }));
                }
            }
            writer4.write(String.format("entities: %d\n", new Object[] { this.entitiesById.size() }));
            writer4.write(String.format("block_entities: %d\n", new Object[] { this.blockEntityList.size() }));
            writer4.write(String.format("block_ticks: %d\n", new Object[] { this.getBlockTicks().size() }));
            writer4.write(String.format("fluid_ticks: %d\n", new Object[] { this.getLiquidTicks().size() }));
            writer4.write("distance_manager: " + zs3.getDistanceManager().getDebugStatus() + "\n");
            writer4.write(String.format("pending_tasks: %d\n", new Object[] { this.getChunkSource().getPendingTasksCount() }));
        }
        final CrashReport l4 = new CrashReport("Level dump", (Throwable)new Exception("dummy"));
        this.fillReportDetails(l4);
        try (final Writer writer5 = (Writer)Files.newBufferedWriter(path.resolve("example_crash.txt"), new OpenOption[0])) {
            writer5.write(l4.getFriendlyReport());
        }
        final Path path2 = path.resolve("chunks.csv");
        try (final Writer writer6 = (Writer)Files.newBufferedWriter(path2, new OpenOption[0])) {
            zs3.dumpChunks(writer6);
        }
        final Path path3 = path.resolve("entities.csv");
        try (final Writer writer7 = (Writer)Files.newBufferedWriter(path3, new OpenOption[0])) {
            dumpEntities(writer7, (Iterable<Entity>)this.entitiesById.values());
        }
        final Path path4 = path.resolve("block_entities.csv");
        try (final Writer writer8 = (Writer)Files.newBufferedWriter(path4, new OpenOption[0])) {
            this.dumpBlockEntities(writer8);
        }
    }
    
    private static void dumpEntities(final Writer writer, final Iterable<Entity> iterable) throws IOException {
        final CsvOutput aet3 = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("uuid").addColumn("type").addColumn("alive").addColumn("display_name").addColumn("custom_name").build(writer);
        for (final Entity apx5 : iterable) {
            final Component nr6 = apx5.getCustomName();
            final Component nr7 = apx5.getDisplayName();
            aet3.writeRow(apx5.getX(), apx5.getY(), apx5.getZ(), apx5.getUUID(), Registry.ENTITY_TYPE.getKey(apx5.getType()), apx5.isAlive(), nr7.getString(), (nr6 != null) ? nr6.getString() : null);
        }
    }
    
    private void dumpBlockEntities(final Writer writer) throws IOException {
        final CsvOutput aet3 = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("type").build(writer);
        for (final BlockEntity ccg5 : this.blockEntityList) {
            final BlockPos fx6 = ccg5.getBlockPos();
            aet3.writeRow(fx6.getX(), fx6.getY(), fx6.getZ(), Registry.BLOCK_ENTITY_TYPE.getKey(ccg5.getType()));
        }
    }
    
    @VisibleForTesting
    public void clearBlockEvents(final BoundingBox cqx) {
        this.blockEvents.removeIf(bqy -> cqx.isInside(bqy.getPos()));
    }
    
    public void blockUpdated(final BlockPos fx, final Block bul) {
        if (!this.isDebug()) {
            this.updateNeighborsAt(fx, bul);
        }
    }
    
    public float getShade(final Direction gc, final boolean boolean2) {
        return 1.0f;
    }
    
    public Iterable<Entity> getAllEntities() {
        return (Iterable<Entity>)Iterables.unmodifiableIterable((Iterable)this.entitiesById.values());
    }
    
    public String toString() {
        return "ServerLevel[" + this.serverLevelData.getLevelName() + "]";
    }
    
    public boolean isFlat() {
        return this.server.getWorldData().worldGenSettings().isFlatWorld();
    }
    
    @Override
    public long getSeed() {
        return this.server.getWorldData().worldGenSettings().seed();
    }
    
    @Nullable
    public EndDragonFight dragonFight() {
        return this.dragonFight;
    }
    
    @Override
    public Stream<? extends StructureStart<?>> startsForFeature(final SectionPos gp, final StructureFeature<?> ckx) {
        return this.structureFeatureManager().startsForFeature(gp, ckx);
    }
    
    public ServerLevel getLevel() {
        return this;
    }
    
    public static void makeObsidianPlatform(final ServerLevel aag) {
        final BlockPos fx2 = ServerLevel.END_SPAWN_POINT;
        final int integer3 = fx2.getX();
        final int integer4 = fx2.getY() - 2;
        final int integer5 = fx2.getZ();
        BlockPos.betweenClosed(integer3 - 2, integer4 + 1, integer5 - 2, integer3 + 2, integer4 + 3, integer5 + 2).forEach(fx -> aag.setBlockAndUpdate(fx, Blocks.AIR.defaultBlockState()));
        BlockPos.betweenClosed(integer3 - 2, integer4, integer5 - 2, integer3 + 2, integer4, integer5 + 2).forEach(fx -> aag.setBlockAndUpdate(fx, Blocks.OBSIDIAN.defaultBlockState()));
    }
    
    static {
        END_SPAWN_POINT = new BlockPos(100, 50, 0);
        LOGGER = LogManager.getLogger();
    }
}
