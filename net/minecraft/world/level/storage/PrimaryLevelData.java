package net.minecraft.world.level.storage;

import org.apache.logging.log4j.LogManager;
import java.util.Optional;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import net.minecraft.CrashReportCategory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.DataPackConfig;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.RegistryWriteOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.RegistryAccess;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import com.mojang.serialization.Decoder;
import net.minecraft.core.SerializableUUID;
import com.mojang.serialization.DynamicLike;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.timers.TimerCallbacks;
import com.google.common.collect.Sets;
import net.minecraft.SharedConstants;
import java.util.LinkedHashSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.timers.TimerQueue;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.LevelSettings;
import org.apache.logging.log4j.Logger;

public class PrimaryLevelData implements ServerLevelData, WorldData {
    private static final Logger LOGGER;
    private LevelSettings settings;
    private final WorldGenSettings worldGenSettings;
    private final Lifecycle worldGenSettingsLifecycle;
    private int xSpawn;
    private int ySpawn;
    private int zSpawn;
    private float spawnAngle;
    private long gameTime;
    private long dayTime;
    @Nullable
    private final DataFixer fixerUpper;
    private final int playerDataVersion;
    private boolean upgradedPlayerTag;
    @Nullable
    private CompoundTag loadedPlayerTag;
    private final int version;
    private int clearWeatherTime;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;
    private boolean initialized;
    private boolean difficultyLocked;
    private WorldBorder.Settings worldBorder;
    private CompoundTag endDragonFightData;
    @Nullable
    private CompoundTag customBossEvents;
    private int wanderingTraderSpawnDelay;
    private int wanderingTraderSpawnChance;
    @Nullable
    private UUID wanderingTraderId;
    private final Set<String> knownServerBrands;
    private boolean wasModded;
    private final TimerQueue<MinecraftServer> scheduledEvents;
    
    private PrimaryLevelData(@Nullable final DataFixer dataFixer, final int integer2, @Nullable final CompoundTag md3, final boolean boolean4, final int integer5, final int integer6, final int integer7, final float float8, final long long9, final long long10, final int integer11, final int integer12, final int integer13, final boolean boolean14, final int integer15, final boolean boolean16, final boolean boolean17, final boolean boolean18, final WorldBorder.Settings c, final int integer20, final int integer21, @Nullable final UUID uUID, final LinkedHashSet<String> linkedHashSet, final TimerQueue<MinecraftServer> dcc, @Nullable final CompoundTag md25, final CompoundTag md26, final LevelSettings brx, final WorldGenSettings cht, final Lifecycle lifecycle) {
        this.fixerUpper = dataFixer;
        this.wasModded = boolean4;
        this.xSpawn = integer5;
        this.ySpawn = integer6;
        this.zSpawn = integer7;
        this.spawnAngle = float8;
        this.gameTime = long9;
        this.dayTime = long10;
        this.version = integer11;
        this.clearWeatherTime = integer12;
        this.rainTime = integer13;
        this.raining = boolean14;
        this.thunderTime = integer15;
        this.thundering = boolean16;
        this.initialized = boolean17;
        this.difficultyLocked = boolean18;
        this.worldBorder = c;
        this.wanderingTraderSpawnDelay = integer20;
        this.wanderingTraderSpawnChance = integer21;
        this.wanderingTraderId = uUID;
        this.knownServerBrands = (Set<String>)linkedHashSet;
        this.loadedPlayerTag = md3;
        this.playerDataVersion = integer2;
        this.scheduledEvents = dcc;
        this.customBossEvents = md25;
        this.endDragonFightData = md26;
        this.settings = brx;
        this.worldGenSettings = cht;
        this.worldGenSettingsLifecycle = lifecycle;
    }
    
    public PrimaryLevelData(final LevelSettings brx, final WorldGenSettings cht, final Lifecycle lifecycle) {
        this(null, SharedConstants.getCurrentVersion().getWorldVersion(), null, false, 0, 0, 0, 0.0f, 0L, 0L, 19133, 0, 0, false, 0, false, false, false, WorldBorder.DEFAULT_SETTINGS, 0, 0, null, (LinkedHashSet<String>)Sets.newLinkedHashSet(), new TimerQueue<MinecraftServer>(TimerCallbacks.SERVER_CALLBACKS), null, new CompoundTag(), brx.copy(), cht, lifecycle);
    }
    
    public static PrimaryLevelData parse(final Dynamic<Tag> dynamic, final DataFixer dataFixer, final int integer, @Nullable final CompoundTag md, final LevelSettings brx, final LevelVersion cyf, final WorldGenSettings cht, final Lifecycle lifecycle) {
        final long long9 = dynamic.get("Time").asLong(0L);
        final CompoundTag md2 = (CompoundTag)dynamic.get("DragonFight").result().map(Dynamic::getValue).orElseGet(() -> (Tag)dynamic.get("DimensionData").get("1").get("DragonFight").orElseEmptyMap().getValue());
        return new PrimaryLevelData(dataFixer, integer, md, dynamic.get("WasModded").asBoolean(false), dynamic.get("SpawnX").asInt(0), dynamic.get("SpawnY").asInt(0), dynamic.get("SpawnZ").asInt(0), dynamic.get("SpawnAngle").asFloat(0.0f), long9, dynamic.get("DayTime").asLong(long9), cyf.levelDataVersion(), dynamic.get("clearWeatherTime").asInt(0), dynamic.get("rainTime").asInt(0), dynamic.get("raining").asBoolean(false), dynamic.get("thunderTime").asInt(0), dynamic.get("thundering").asBoolean(false), dynamic.get("initialized").asBoolean(true), dynamic.get("DifficultyLocked").asBoolean(false), WorldBorder.Settings.read(dynamic, WorldBorder.DEFAULT_SETTINGS), dynamic.get("WanderingTraderSpawnDelay").asInt(0), dynamic.get("WanderingTraderSpawnChance").asInt(0), (UUID)dynamic.get("WanderingTraderId").read((Decoder)SerializableUUID.CODEC).result().orElse(null), (LinkedHashSet<String>)dynamic.get("ServerBrands").asStream().flatMap(dynamic -> Util.toStream((java.util.Optional<?>)dynamic.asString().result())).collect(Collectors.toCollection(Sets::newLinkedHashSet)), new TimerQueue<MinecraftServer>(TimerCallbacks.SERVER_CALLBACKS, (Stream<Dynamic<Tag>>)dynamic.get("ScheduledEvents").asStream()), (CompoundTag)dynamic.get("CustomBossEvents").orElseEmptyMap().getValue(), md2, brx, cht, lifecycle);
    }
    
    public CompoundTag createTag(final RegistryAccess gn, @Nullable CompoundTag md) {
        this.updatePlayerTag();
        if (md == null) {
            md = this.loadedPlayerTag;
        }
        final CompoundTag md2 = new CompoundTag();
        this.setTagData(gn, md2, md);
        return md2;
    }
    
    private void setTagData(final RegistryAccess gn, final CompoundTag md2, @Nullable final CompoundTag md3) {
        final ListTag mj5 = new ListTag();
        this.knownServerBrands.stream().map(StringTag::valueOf).forEach(mj5::add);
        md2.put("ServerBrands", (Tag)mj5);
        md2.putBoolean("WasModded", this.wasModded);
        final CompoundTag md4 = new CompoundTag();
        md4.putString("Name", SharedConstants.getCurrentVersion().getName());
        md4.putInt("Id", SharedConstants.getCurrentVersion().getWorldVersion());
        md4.putBoolean("Snapshot", !SharedConstants.getCurrentVersion().isStable());
        md2.put("Version", (Tag)md4);
        md2.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        final RegistryWriteOps<Tag> vi7 = RegistryWriteOps.<Tag>create((com.mojang.serialization.DynamicOps<Tag>)NbtOps.INSTANCE, gn);
        WorldGenSettings.CODEC.encodeStart((DynamicOps)vi7, this.worldGenSettings).resultOrPartial((Consumer)Util.prefix("WorldGenSettings: ", (Consumer<String>)PrimaryLevelData.LOGGER::error)).ifPresent(mt -> md2.put("WorldGenSettings", mt));
        md2.putInt("GameType", this.settings.gameType().getId());
        md2.putInt("SpawnX", this.xSpawn);
        md2.putInt("SpawnY", this.ySpawn);
        md2.putInt("SpawnZ", this.zSpawn);
        md2.putFloat("SpawnAngle", this.spawnAngle);
        md2.putLong("Time", this.gameTime);
        md2.putLong("DayTime", this.dayTime);
        md2.putLong("LastPlayed", Util.getEpochMillis());
        md2.putString("LevelName", this.settings.levelName());
        md2.putInt("version", 19133);
        md2.putInt("clearWeatherTime", this.clearWeatherTime);
        md2.putInt("rainTime", this.rainTime);
        md2.putBoolean("raining", this.raining);
        md2.putInt("thunderTime", this.thunderTime);
        md2.putBoolean("thundering", this.thundering);
        md2.putBoolean("hardcore", this.settings.hardcore());
        md2.putBoolean("allowCommands", this.settings.allowCommands());
        md2.putBoolean("initialized", this.initialized);
        this.worldBorder.write(md2);
        md2.putByte("Difficulty", (byte)this.settings.difficulty().getId());
        md2.putBoolean("DifficultyLocked", this.difficultyLocked);
        md2.put("GameRules", (Tag)this.settings.gameRules().createTag());
        md2.put("DragonFight", (Tag)this.endDragonFightData);
        if (md3 != null) {
            md2.put("Player", (Tag)md3);
        }
        DataPackConfig.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.settings.getDataPackConfig()).result().ifPresent(mt -> md2.put("DataPacks", mt));
        if (this.customBossEvents != null) {
            md2.put("CustomBossEvents", (Tag)this.customBossEvents);
        }
        md2.put("ScheduledEvents", (Tag)this.scheduledEvents.store());
        md2.putInt("WanderingTraderSpawnDelay", this.wanderingTraderSpawnDelay);
        md2.putInt("WanderingTraderSpawnChance", this.wanderingTraderSpawnChance);
        if (this.wanderingTraderId != null) {
            md2.putUUID("WanderingTraderId", this.wanderingTraderId);
        }
    }
    
    public int getXSpawn() {
        return this.xSpawn;
    }
    
    public int getYSpawn() {
        return this.ySpawn;
    }
    
    public int getZSpawn() {
        return this.zSpawn;
    }
    
    public float getSpawnAngle() {
        return this.spawnAngle;
    }
    
    public long getGameTime() {
        return this.gameTime;
    }
    
    public long getDayTime() {
        return this.dayTime;
    }
    
    private void updatePlayerTag() {
        if (this.upgradedPlayerTag || this.loadedPlayerTag == null) {
            return;
        }
        if (this.playerDataVersion < SharedConstants.getCurrentVersion().getWorldVersion()) {
            if (this.fixerUpper == null) {
                throw Util.<NullPointerException>pauseInIde(new NullPointerException("Fixer Upper not set inside LevelData, and the player tag is not upgraded."));
            }
            this.loadedPlayerTag = NbtUtils.update(this.fixerUpper, DataFixTypes.PLAYER, this.loadedPlayerTag, this.playerDataVersion);
        }
        this.upgradedPlayerTag = true;
    }
    
    public CompoundTag getLoadedPlayerTag() {
        this.updatePlayerTag();
        return this.loadedPlayerTag;
    }
    
    public void setXSpawn(final int integer) {
        this.xSpawn = integer;
    }
    
    public void setYSpawn(final int integer) {
        this.ySpawn = integer;
    }
    
    public void setZSpawn(final int integer) {
        this.zSpawn = integer;
    }
    
    public void setSpawnAngle(final float float1) {
        this.spawnAngle = float1;
    }
    
    public void setGameTime(final long long1) {
        this.gameTime = long1;
    }
    
    public void setDayTime(final long long1) {
        this.dayTime = long1;
    }
    
    public void setSpawn(final BlockPos fx, final float float2) {
        this.xSpawn = fx.getX();
        this.ySpawn = fx.getY();
        this.zSpawn = fx.getZ();
        this.spawnAngle = float2;
    }
    
    public String getLevelName() {
        return this.settings.levelName();
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public int getClearWeatherTime() {
        return this.clearWeatherTime;
    }
    
    public void setClearWeatherTime(final int integer) {
        this.clearWeatherTime = integer;
    }
    
    public boolean isThundering() {
        return this.thundering;
    }
    
    public void setThundering(final boolean boolean1) {
        this.thundering = boolean1;
    }
    
    public int getThunderTime() {
        return this.thunderTime;
    }
    
    public void setThunderTime(final int integer) {
        this.thunderTime = integer;
    }
    
    public boolean isRaining() {
        return this.raining;
    }
    
    public void setRaining(final boolean boolean1) {
        this.raining = boolean1;
    }
    
    public int getRainTime() {
        return this.rainTime;
    }
    
    public void setRainTime(final int integer) {
        this.rainTime = integer;
    }
    
    public GameType getGameType() {
        return this.settings.gameType();
    }
    
    public void setGameType(final GameType brr) {
        this.settings = this.settings.withGameType(brr);
    }
    
    public boolean isHardcore() {
        return this.settings.hardcore();
    }
    
    public boolean getAllowCommands() {
        return this.settings.allowCommands();
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public void setInitialized(final boolean boolean1) {
        this.initialized = boolean1;
    }
    
    public GameRules getGameRules() {
        return this.settings.gameRules();
    }
    
    public WorldBorder.Settings getWorldBorder() {
        return this.worldBorder;
    }
    
    public void setWorldBorder(final WorldBorder.Settings c) {
        this.worldBorder = c;
    }
    
    public Difficulty getDifficulty() {
        return this.settings.difficulty();
    }
    
    public void setDifficulty(final Difficulty aoo) {
        this.settings = this.settings.withDifficulty(aoo);
    }
    
    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }
    
    public void setDifficultyLocked(final boolean boolean1) {
        this.difficultyLocked = boolean1;
    }
    
    public TimerQueue<MinecraftServer> getScheduledEvents() {
        return this.scheduledEvents;
    }
    
    public void fillCrashReportCategory(final CrashReportCategory m) {
        super.fillCrashReportCategory(m);
        super.fillCrashReportCategory(m);
    }
    
    public WorldGenSettings worldGenSettings() {
        return this.worldGenSettings;
    }
    
    public Lifecycle worldGenSettingsLifecycle() {
        return this.worldGenSettingsLifecycle;
    }
    
    public CompoundTag endDragonFightData() {
        return this.endDragonFightData;
    }
    
    public void setEndDragonFightData(final CompoundTag md) {
        this.endDragonFightData = md;
    }
    
    public DataPackConfig getDataPackConfig() {
        return this.settings.getDataPackConfig();
    }
    
    public void setDataPackConfig(final DataPackConfig brh) {
        this.settings = this.settings.withDataPackConfig(brh);
    }
    
    @Nullable
    public CompoundTag getCustomBossEvents() {
        return this.customBossEvents;
    }
    
    public void setCustomBossEvents(@Nullable final CompoundTag md) {
        this.customBossEvents = md;
    }
    
    public int getWanderingTraderSpawnDelay() {
        return this.wanderingTraderSpawnDelay;
    }
    
    public void setWanderingTraderSpawnDelay(final int integer) {
        this.wanderingTraderSpawnDelay = integer;
    }
    
    public int getWanderingTraderSpawnChance() {
        return this.wanderingTraderSpawnChance;
    }
    
    public void setWanderingTraderSpawnChance(final int integer) {
        this.wanderingTraderSpawnChance = integer;
    }
    
    public void setWanderingTraderId(final UUID uUID) {
        this.wanderingTraderId = uUID;
    }
    
    public void setModdedInfo(final String string, final boolean boolean2) {
        this.knownServerBrands.add(string);
        this.wasModded |= boolean2;
    }
    
    public boolean wasModded() {
        return this.wasModded;
    }
    
    public Set<String> getKnownServerBrands() {
        return (Set<String>)ImmutableSet.copyOf((Collection)this.knownServerBrands);
    }
    
    public ServerLevelData overworldData() {
        return this;
    }
    
    public LevelSettings getLevelSettings() {
        return this.settings.copy();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
