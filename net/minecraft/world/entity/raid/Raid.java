package net.minecraft.world.entity.raid;

import java.util.Locale;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import javax.annotation.Nullable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.EntityType;
import java.util.Collection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import java.util.stream.Stream;
import java.util.Comparator;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import java.util.function.Predicate;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.BossEvent;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Optional;
import java.util.Random;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import java.util.UUID;
import java.util.Set;
import java.util.Map;
import net.minecraft.network.chat.Component;

public class Raid {
    private static final Component RAID_NAME_COMPONENT;
    private static final Component VICTORY;
    private static final Component DEFEAT;
    private static final Component RAID_BAR_VICTORY_COMPONENT;
    private static final Component RAID_BAR_DEFEAT_COMPONENT;
    private final Map<Integer, Raider> groupToLeaderMap;
    private final Map<Integer, Set<Raider>> groupRaiderMap;
    private final Set<UUID> heroesOfTheVillage;
    private long ticksActive;
    private BlockPos center;
    private final ServerLevel level;
    private boolean started;
    private final int id;
    private float totalHealth;
    private int badOmenLevel;
    private boolean active;
    private int groupsSpawned;
    private final ServerBossEvent raidEvent;
    private int postRaidTicks;
    private int raidCooldownTicks;
    private final Random random;
    private final int numGroups;
    private RaidStatus status;
    private int celebrationTicks;
    private Optional<BlockPos> waveSpawnPos;
    
    public Raid(final int integer, final ServerLevel aag, final BlockPos fx) {
        this.groupToLeaderMap = (Map<Integer, Raider>)Maps.newHashMap();
        this.groupRaiderMap = (Map<Integer, Set<Raider>>)Maps.newHashMap();
        this.heroesOfTheVillage = (Set<UUID>)Sets.newHashSet();
        this.raidEvent = new ServerBossEvent(Raid.RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
        this.random = new Random();
        this.waveSpawnPos = (Optional<BlockPos>)Optional.empty();
        this.id = integer;
        this.level = aag;
        this.active = true;
        this.raidCooldownTicks = 300;
        this.raidEvent.setPercent(0.0f);
        this.center = fx;
        this.numGroups = this.getNumGroups(aag.getDifficulty());
        this.status = RaidStatus.ONGOING;
    }
    
    public Raid(final ServerLevel aag, final CompoundTag md) {
        this.groupToLeaderMap = (Map<Integer, Raider>)Maps.newHashMap();
        this.groupRaiderMap = (Map<Integer, Set<Raider>>)Maps.newHashMap();
        this.heroesOfTheVillage = (Set<UUID>)Sets.newHashSet();
        this.raidEvent = new ServerBossEvent(Raid.RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
        this.random = new Random();
        this.waveSpawnPos = (Optional<BlockPos>)Optional.empty();
        this.level = aag;
        this.id = md.getInt("Id");
        this.started = md.getBoolean("Started");
        this.active = md.getBoolean("Active");
        this.ticksActive = md.getLong("TicksActive");
        this.badOmenLevel = md.getInt("BadOmenLevel");
        this.groupsSpawned = md.getInt("GroupsSpawned");
        this.raidCooldownTicks = md.getInt("PreRaidTicks");
        this.postRaidTicks = md.getInt("PostRaidTicks");
        this.totalHealth = md.getFloat("TotalHealth");
        this.center = new BlockPos(md.getInt("CX"), md.getInt("CY"), md.getInt("CZ"));
        this.numGroups = md.getInt("NumGroups");
        this.status = getByName(md.getString("Status"));
        this.heroesOfTheVillage.clear();
        if (md.contains("HeroesOfTheVillage", 9)) {
            final ListTag mj4 = md.getList("HeroesOfTheVillage", 11);
            for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
                this.heroesOfTheVillage.add(NbtUtils.loadUUID(mj4.get(integer5)));
            }
        }
    }
    
    public boolean isOver() {
        return this.isVictory() || this.isLoss();
    }
    
    public boolean isBetweenWaves() {
        return this.hasFirstWaveSpawned() && this.getTotalRaidersAlive() == 0 && this.raidCooldownTicks > 0;
    }
    
    public boolean hasFirstWaveSpawned() {
        return this.groupsSpawned > 0;
    }
    
    public boolean isStopped() {
        return this.status == RaidStatus.STOPPED;
    }
    
    public boolean isVictory() {
        return this.status == RaidStatus.VICTORY;
    }
    
    public boolean isLoss() {
        return this.status == RaidStatus.LOSS;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public boolean isStarted() {
        return this.started;
    }
    
    public int getGroupsSpawned() {
        return this.groupsSpawned;
    }
    
    private Predicate<ServerPlayer> validPlayer() {
        return (Predicate<ServerPlayer>)(aah -> {
            final BlockPos fx3 = aah.blockPosition();
            return aah.isAlive() && this.level.getRaidAt(fx3) == this;
        });
    }
    
    private void updatePlayers() {
        final Set<ServerPlayer> set2 = (Set<ServerPlayer>)Sets.newHashSet((Iterable)this.raidEvent.getPlayers());
        final List<ServerPlayer> list3 = this.level.getPlayers(this.validPlayer());
        for (final ServerPlayer aah5 : list3) {
            if (!set2.contains(aah5)) {
                this.raidEvent.addPlayer(aah5);
            }
        }
        for (final ServerPlayer aah5 : set2) {
            if (!list3.contains(aah5)) {
                this.raidEvent.removePlayer(aah5);
            }
        }
    }
    
    public int getMaxBadOmenLevel() {
        return 5;
    }
    
    public int getBadOmenLevel() {
        return this.badOmenLevel;
    }
    
    public void absorbBadOmen(final Player bft) {
        if (bft.hasEffect(MobEffects.BAD_OMEN)) {
            this.badOmenLevel += bft.getEffect(MobEffects.BAD_OMEN).getAmplifier() + 1;
            this.badOmenLevel = Mth.clamp(this.badOmenLevel, 0, this.getMaxBadOmenLevel());
        }
        bft.removeEffect(MobEffects.BAD_OMEN);
    }
    
    public void stop() {
        this.active = false;
        this.raidEvent.removeAllPlayers();
        this.status = RaidStatus.STOPPED;
    }
    
    public void tick() {
        if (this.isStopped()) {
            return;
        }
        if (this.status == RaidStatus.ONGOING) {
            final boolean boolean2 = this.active;
            this.active = this.level.hasChunkAt(this.center);
            if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
                this.stop();
                return;
            }
            if (boolean2 != this.active) {
                this.raidEvent.setVisible(this.active);
            }
            if (!this.active) {
                return;
            }
            if (!this.level.isVillage(this.center)) {
                this.moveRaidCenterToNearbyVillageSection();
            }
            if (!this.level.isVillage(this.center)) {
                if (this.groupsSpawned > 0) {
                    this.status = RaidStatus.LOSS;
                }
                else {
                    this.stop();
                }
            }
            ++this.ticksActive;
            if (this.ticksActive >= 48000L) {
                this.stop();
                return;
            }
            final int integer3 = this.getTotalRaidersAlive();
            if (integer3 == 0 && this.hasMoreWaves()) {
                if (this.raidCooldownTicks > 0) {
                    final boolean boolean3 = this.waveSpawnPos.isPresent();
                    boolean boolean4 = !boolean3 && this.raidCooldownTicks % 5 == 0;
                    if (boolean3 && !this.level.getChunkSource().isEntityTickingChunk(new ChunkPos((BlockPos)this.waveSpawnPos.get()))) {
                        boolean4 = true;
                    }
                    if (boolean4) {
                        int integer4 = 0;
                        if (this.raidCooldownTicks < 100) {
                            integer4 = 1;
                        }
                        else if (this.raidCooldownTicks < 40) {
                            integer4 = 2;
                        }
                        this.waveSpawnPos = this.getValidSpawnPos(integer4);
                    }
                    if (this.raidCooldownTicks == 300 || this.raidCooldownTicks % 20 == 0) {
                        this.updatePlayers();
                    }
                    --this.raidCooldownTicks;
                    this.raidEvent.setPercent(Mth.clamp((300 - this.raidCooldownTicks) / 300.0f, 0.0f, 1.0f));
                }
                else if (this.raidCooldownTicks == 0 && this.groupsSpawned > 0) {
                    this.raidCooldownTicks = 300;
                    this.raidEvent.setName(Raid.RAID_NAME_COMPONENT);
                    return;
                }
            }
            if (this.ticksActive % 20L == 0L) {
                this.updatePlayers();
                this.updateRaiders();
                if (integer3 > 0) {
                    if (integer3 <= 2) {
                        this.raidEvent.setName(Raid.RAID_NAME_COMPONENT.copy().append(" - ").append(new TranslatableComponent("event.minecraft.raid.raiders_remaining", new Object[] { integer3 })));
                    }
                    else {
                        this.raidEvent.setName(Raid.RAID_NAME_COMPONENT);
                    }
                }
                else {
                    this.raidEvent.setName(Raid.RAID_NAME_COMPONENT);
                }
            }
            boolean boolean3 = false;
            int integer5 = 0;
            while (this.shouldSpawnGroup()) {
                final BlockPos fx6 = (BlockPos)(this.waveSpawnPos.isPresent() ? this.waveSpawnPos.get() : this.findRandomSpawnPos(integer5, 20));
                if (fx6 != null) {
                    this.started = true;
                    this.spawnGroup(fx6);
                    if (!boolean3) {
                        this.playSound(fx6);
                        boolean3 = true;
                    }
                }
                else {
                    ++integer5;
                }
                if (integer5 > 3) {
                    this.stop();
                    break;
                }
            }
            if (this.isStarted() && !this.hasMoreWaves() && integer3 == 0) {
                if (this.postRaidTicks < 40) {
                    ++this.postRaidTicks;
                }
                else {
                    this.status = RaidStatus.VICTORY;
                    for (final UUID uUID7 : this.heroesOfTheVillage) {
                        final Entity apx8 = this.level.getEntity(uUID7);
                        if (apx8 instanceof LivingEntity && !apx8.isSpectator()) {
                            final LivingEntity aqj9 = (LivingEntity)apx8;
                            aqj9.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 48000, this.badOmenLevel - 1, false, false, true));
                            if (!(aqj9 instanceof ServerPlayer)) {
                                continue;
                            }
                            final ServerPlayer aah10 = (ServerPlayer)aqj9;
                            aah10.awardStat(Stats.RAID_WIN);
                            CriteriaTriggers.RAID_WIN.trigger(aah10);
                        }
                    }
                }
            }
            this.setDirty();
        }
        else if (this.isOver()) {
            ++this.celebrationTicks;
            if (this.celebrationTicks >= 600) {
                this.stop();
                return;
            }
            if (this.celebrationTicks % 20 == 0) {
                this.updatePlayers();
                this.raidEvent.setVisible(true);
                if (this.isVictory()) {
                    this.raidEvent.setPercent(0.0f);
                    this.raidEvent.setName(Raid.RAID_BAR_VICTORY_COMPONENT);
                }
                else {
                    this.raidEvent.setName(Raid.RAID_BAR_DEFEAT_COMPONENT);
                }
            }
        }
    }
    
    private void moveRaidCenterToNearbyVillageSection() {
        final Stream<SectionPos> stream2 = SectionPos.cube(SectionPos.of(this.center), 2);
        stream2.filter(this.level::isVillage).map(SectionPos::center).min(Comparator.comparingDouble(fx -> fx.distSqr(this.center))).ifPresent(this::setCenter);
    }
    
    private Optional<BlockPos> getValidSpawnPos(final int integer) {
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            final BlockPos fx4 = this.findRandomSpawnPos(integer, 1);
            if (fx4 != null) {
                return (Optional<BlockPos>)Optional.of(fx4);
            }
        }
        return (Optional<BlockPos>)Optional.empty();
    }
    
    private boolean hasMoreWaves() {
        if (this.hasBonusWave()) {
            return !this.hasSpawnedBonusWave();
        }
        return !this.isFinalWave();
    }
    
    private boolean isFinalWave() {
        return this.getGroupsSpawned() == this.numGroups;
    }
    
    private boolean hasBonusWave() {
        return this.badOmenLevel > 1;
    }
    
    private boolean hasSpawnedBonusWave() {
        return this.getGroupsSpawned() > this.numGroups;
    }
    
    private boolean shouldSpawnBonusGroup() {
        return this.isFinalWave() && this.getTotalRaidersAlive() == 0 && this.hasBonusWave();
    }
    
    private void updateRaiders() {
        final Iterator<Set<Raider>> iterator2 = (Iterator<Set<Raider>>)this.groupRaiderMap.values().iterator();
        final Set<Raider> set3 = (Set<Raider>)Sets.newHashSet();
        while (iterator2.hasNext()) {
            final Set<Raider> set4 = (Set<Raider>)iterator2.next();
            for (final Raider bgz6 : set4) {
                final BlockPos fx7 = bgz6.blockPosition();
                if (bgz6.removed || bgz6.level.dimension() != this.level.dimension() || this.center.distSqr(fx7) >= 12544.0) {
                    set3.add(bgz6);
                }
                else {
                    if (bgz6.tickCount <= 600) {
                        continue;
                    }
                    if (this.level.getEntity(bgz6.getUUID()) == null) {
                        set3.add(bgz6);
                    }
                    if (!this.level.isVillage(fx7) && bgz6.getNoActionTime() > 2400) {
                        bgz6.setTicksOutsideRaid(bgz6.getTicksOutsideRaid() + 1);
                    }
                    if (bgz6.getTicksOutsideRaid() < 30) {
                        continue;
                    }
                    set3.add(bgz6);
                }
            }
        }
        for (final Raider bgz7 : set3) {
            this.removeFromRaid(bgz7, true);
        }
    }
    
    private void playSound(final BlockPos fx) {
        final float float3 = 13.0f;
        final int integer4 = 64;
        final Collection<ServerPlayer> collection5 = this.raidEvent.getPlayers();
        for (final ServerPlayer aah7 : this.level.players()) {
            final Vec3 dck8 = aah7.position();
            final Vec3 dck9 = Vec3.atCenterOf(fx);
            final float float4 = Mth.sqrt((dck9.x - dck8.x) * (dck9.x - dck8.x) + (dck9.z - dck8.z) * (dck9.z - dck8.z));
            final double double11 = dck8.x + 13.0f / float4 * (dck9.x - dck8.x);
            final double double12 = dck8.z + 13.0f / float4 * (dck9.z - dck8.z);
            if (float4 <= 64.0f || collection5.contains(aah7)) {
                aah7.connection.send(new ClientboundSoundPacket(SoundEvents.RAID_HORN, SoundSource.NEUTRAL, double11, aah7.getY(), double12, 64.0f, 1.0f));
            }
        }
    }
    
    private void spawnGroup(final BlockPos fx) {
        boolean boolean3 = false;
        final int integer4 = this.groupsSpawned + 1;
        this.totalHealth = 0.0f;
        final DifficultyInstance aop5 = this.level.getCurrentDifficultyAt(fx);
        final boolean boolean4 = this.shouldSpawnBonusGroup();
        for (final RaiderType b10 : RaiderType.VALUES) {
            final int integer5 = this.getDefaultNumSpawns(b10, integer4, boolean4) + this.getPotentialBonusSpawns(b10, this.random, integer4, aop5, boolean4);
            int integer6 = 0;
            for (int integer7 = 0; integer7 < integer5; ++integer7) {
                final Raider bgz14 = b10.entityType.create(this.level);
                if (!boolean3 && bgz14.canBeLeader()) {
                    bgz14.setPatrolLeader(true);
                    this.setLeader(integer4, bgz14);
                    boolean3 = true;
                }
                this.joinRaid(integer4, bgz14, fx, false);
                if (b10.entityType == EntityType.RAVAGER) {
                    Raider bgz15 = null;
                    if (integer4 == this.getNumGroups(Difficulty.NORMAL)) {
                        bgz15 = EntityType.PILLAGER.create(this.level);
                    }
                    else if (integer4 >= this.getNumGroups(Difficulty.HARD)) {
                        if (integer6 == 0) {
                            bgz15 = EntityType.EVOKER.create(this.level);
                        }
                        else {
                            bgz15 = EntityType.VINDICATOR.create(this.level);
                        }
                    }
                    ++integer6;
                    if (bgz15 != null) {
                        this.joinRaid(integer4, bgz15, fx, false);
                        bgz15.moveTo(fx, 0.0f, 0.0f);
                        bgz15.startRiding(bgz14);
                    }
                }
            }
        }
        this.waveSpawnPos = (Optional<BlockPos>)Optional.empty();
        ++this.groupsSpawned;
        this.updateBossbar();
        this.setDirty();
    }
    
    public void joinRaid(final int integer, final Raider bgz, @Nullable final BlockPos fx, final boolean boolean4) {
        final boolean boolean5 = this.addWaveMob(integer, bgz);
        if (boolean5) {
            bgz.setCurrentRaid(this);
            bgz.setWave(integer);
            bgz.setCanJoinRaid(true);
            bgz.setTicksOutsideRaid(0);
            if (!boolean4 && fx != null) {
                bgz.setPos(fx.getX() + 0.5, fx.getY() + 1.0, fx.getZ() + 0.5);
                bgz.finalizeSpawn(this.level, this.level.getCurrentDifficultyAt(fx), MobSpawnType.EVENT, null, null);
                bgz.applyRaidBuffs(integer, false);
                bgz.setOnGround(true);
                this.level.addFreshEntityWithPassengers(bgz);
            }
        }
    }
    
    public void updateBossbar() {
        this.raidEvent.setPercent(Mth.clamp(this.getHealthOfLivingRaiders() / this.totalHealth, 0.0f, 1.0f));
    }
    
    public float getHealthOfLivingRaiders() {
        float float2 = 0.0f;
        for (final Set<Raider> set4 : this.groupRaiderMap.values()) {
            for (final Raider bgz6 : set4) {
                float2 += bgz6.getHealth();
            }
        }
        return float2;
    }
    
    private boolean shouldSpawnGroup() {
        return this.raidCooldownTicks == 0 && (this.groupsSpawned < this.numGroups || this.shouldSpawnBonusGroup()) && this.getTotalRaidersAlive() == 0;
    }
    
    public int getTotalRaidersAlive() {
        return this.groupRaiderMap.values().stream().mapToInt(Set::size).sum();
    }
    
    public void removeFromRaid(final Raider bgz, final boolean boolean2) {
        final Set<Raider> set4 = (Set<Raider>)this.groupRaiderMap.get(bgz.getWave());
        if (set4 != null) {
            final boolean boolean3 = set4.remove(bgz);
            if (boolean3) {
                if (boolean2) {
                    this.totalHealth -= bgz.getHealth();
                }
                bgz.setCurrentRaid(null);
                this.updateBossbar();
                this.setDirty();
            }
        }
    }
    
    private void setDirty() {
        this.level.getRaids().setDirty();
    }
    
    public static ItemStack getLeaderBannerInstance() {
        final ItemStack bly1 = new ItemStack(Items.WHITE_BANNER);
        final CompoundTag md2 = bly1.getOrCreateTagElement("BlockEntityTag");
        final ListTag mj3 = new BannerPattern.Builder().addPattern(BannerPattern.RHOMBUS_MIDDLE, DyeColor.CYAN).addPattern(BannerPattern.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.STRIPE_CENTER, DyeColor.GRAY).addPattern(BannerPattern.BORDER, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.STRIPE_MIDDLE, DyeColor.BLACK).addPattern(BannerPattern.HALF_HORIZONTAL, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.BORDER, DyeColor.BLACK).toListTag();
        md2.put("Patterns", (Tag)mj3);
        bly1.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        bly1.setHoverName(new TranslatableComponent("block.minecraft.ominous_banner").withStyle(ChatFormatting.GOLD));
        return bly1;
    }
    
    @Nullable
    public Raider getLeader(final int integer) {
        return (Raider)this.groupToLeaderMap.get(integer);
    }
    
    @Nullable
    private BlockPos findRandomSpawnPos(final int integer1, final int integer2) {
        final int integer3 = (integer1 == 0) ? 2 : (2 - integer1);
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        for (int integer4 = 0; integer4 < integer2; ++integer4) {
            final float float10 = this.level.random.nextFloat() * 6.2831855f;
            final int integer5 = this.center.getX() + Mth.floor(Mth.cos(float10) * 32.0f * integer3) + this.level.random.nextInt(5);
            final int integer6 = this.center.getZ() + Mth.floor(Mth.sin(float10) * 32.0f * integer3) + this.level.random.nextInt(5);
            final int integer7 = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, integer5, integer6);
            a8.set(integer5, integer7, integer6);
            if (!this.level.isVillage(a8) || integer1 >= 2) {
                if (this.level.hasChunksAt(a8.getX() - 10, a8.getY() - 10, a8.getZ() - 10, a8.getX() + 10, a8.getY() + 10, a8.getZ() + 10)) {
                    if (this.level.getChunkSource().isEntityTickingChunk(new ChunkPos(a8))) {
                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.level, a8, EntityType.RAVAGER) || (this.level.getBlockState(a8.below()).is(Blocks.SNOW) && this.level.getBlockState(a8).isAir())) {
                            return a8;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private boolean addWaveMob(final int integer, final Raider bgz) {
        return this.addWaveMob(integer, bgz, true);
    }
    
    public boolean addWaveMob(final int integer, final Raider bgz, final boolean boolean3) {
        this.groupRaiderMap.computeIfAbsent(integer, integer -> Sets.newHashSet());
        final Set<Raider> set5 = (Set<Raider>)this.groupRaiderMap.get(integer);
        Raider bgz2 = null;
        for (final Raider bgz3 : set5) {
            if (bgz3.getUUID().equals(bgz.getUUID())) {
                bgz2 = bgz3;
                break;
            }
        }
        if (bgz2 != null) {
            set5.remove(bgz2);
            set5.add(bgz);
        }
        set5.add(bgz);
        if (boolean3) {
            this.totalHealth += bgz.getHealth();
        }
        this.updateBossbar();
        this.setDirty();
        return true;
    }
    
    public void setLeader(final int integer, final Raider bgz) {
        this.groupToLeaderMap.put(integer, bgz);
        bgz.setItemSlot(EquipmentSlot.HEAD, getLeaderBannerInstance());
        bgz.setDropChance(EquipmentSlot.HEAD, 2.0f);
    }
    
    public void removeLeader(final int integer) {
        this.groupToLeaderMap.remove(integer);
    }
    
    public BlockPos getCenter() {
        return this.center;
    }
    
    private void setCenter(final BlockPos fx) {
        this.center = fx;
    }
    
    public int getId() {
        return this.id;
    }
    
    private int getDefaultNumSpawns(final RaiderType b, final int integer, final boolean boolean3) {
        return boolean3 ? b.spawnsPerWaveBeforeBonus[this.numGroups] : b.spawnsPerWaveBeforeBonus[integer];
    }
    
    private int getPotentialBonusSpawns(final RaiderType b, final Random random, final int integer, final DifficultyInstance aop, final boolean boolean5) {
        final Difficulty aoo7 = aop.getDifficulty();
        final boolean boolean6 = aoo7 == Difficulty.EASY;
        final boolean boolean7 = aoo7 == Difficulty.NORMAL;
        int integer2 = 0;
        switch (b) {
            case WITCH: {
                if (!boolean6 && integer > 2 && integer != 4) {
                    integer2 = 1;
                    break;
                }
                return 0;
            }
            case PILLAGER:
            case VINDICATOR: {
                if (boolean6) {
                    integer2 = random.nextInt(2);
                    break;
                }
                if (boolean7) {
                    integer2 = 1;
                    break;
                }
                integer2 = 2;
                break;
            }
            case RAVAGER: {
                integer2 = ((!boolean6 && boolean5) ? 1 : 0);
                break;
            }
            default: {
                return 0;
            }
        }
        return (integer2 > 0) ? random.nextInt(integer2 + 1) : 0;
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public CompoundTag save(final CompoundTag md) {
        md.putInt("Id", this.id);
        md.putBoolean("Started", this.started);
        md.putBoolean("Active", this.active);
        md.putLong("TicksActive", this.ticksActive);
        md.putInt("BadOmenLevel", this.badOmenLevel);
        md.putInt("GroupsSpawned", this.groupsSpawned);
        md.putInt("PreRaidTicks", this.raidCooldownTicks);
        md.putInt("PostRaidTicks", this.postRaidTicks);
        md.putFloat("TotalHealth", this.totalHealth);
        md.putInt("NumGroups", this.numGroups);
        md.putString("Status", this.status.getName());
        md.putInt("CX", this.center.getX());
        md.putInt("CY", this.center.getY());
        md.putInt("CZ", this.center.getZ());
        final ListTag mj3 = new ListTag();
        for (final UUID uUID5 : this.heroesOfTheVillage) {
            mj3.add(NbtUtils.createUUID(uUID5));
        }
        md.put("HeroesOfTheVillage", (Tag)mj3);
        return md;
    }
    
    public int getNumGroups(final Difficulty aoo) {
        switch (aoo) {
            case EASY: {
                return 3;
            }
            case NORMAL: {
                return 5;
            }
            case HARD: {
                return 7;
            }
            default: {
                return 0;
            }
        }
    }
    
    public float getEnchantOdds() {
        final int integer2 = this.getBadOmenLevel();
        if (integer2 == 2) {
            return 0.1f;
        }
        if (integer2 == 3) {
            return 0.25f;
        }
        if (integer2 == 4) {
            return 0.5f;
        }
        if (integer2 == 5) {
            return 0.75f;
        }
        return 0.0f;
    }
    
    public void addHeroOfTheVillage(final Entity apx) {
        this.heroesOfTheVillage.add(apx.getUUID());
    }
    
    static {
        RAID_NAME_COMPONENT = new TranslatableComponent("event.minecraft.raid");
        VICTORY = new TranslatableComponent("event.minecraft.raid.victory");
        DEFEAT = new TranslatableComponent("event.minecraft.raid.defeat");
        RAID_BAR_VICTORY_COMPONENT = Raid.RAID_NAME_COMPONENT.copy().append(" - ").append(Raid.VICTORY);
        RAID_BAR_DEFEAT_COMPONENT = Raid.RAID_NAME_COMPONENT.copy().append(" - ").append(Raid.DEFEAT);
    }
    
    enum RaidStatus {
        ONGOING, 
        VICTORY, 
        LOSS, 
        STOPPED;
        
        private static final RaidStatus[] VALUES;
        
        private static RaidStatus getByName(final String string) {
            for (final RaidStatus a5 : RaidStatus.VALUES) {
                if (string.equalsIgnoreCase(a5.name())) {
                    return a5;
                }
            }
            return RaidStatus.ONGOING;
        }
        
        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
        
        static {
            VALUES = values();
        }
    }
    
    enum RaiderType {
        VINDICATOR(EntityType.VINDICATOR, new int[] { 0, 0, 2, 0, 1, 4, 2, 5 }), 
        EVOKER(EntityType.EVOKER, new int[] { 0, 0, 0, 0, 0, 1, 1, 2 }), 
        PILLAGER(EntityType.PILLAGER, new int[] { 0, 4, 3, 3, 4, 4, 4, 2 }), 
        WITCH(EntityType.WITCH, new int[] { 0, 0, 0, 0, 3, 0, 0, 1 }), 
        RAVAGER(EntityType.RAVAGER, new int[] { 0, 0, 0, 1, 0, 1, 0, 2 });
        
        private static final RaiderType[] VALUES;
        private final EntityType<? extends Raider> entityType;
        private final int[] spawnsPerWaveBeforeBonus;
        
        private RaiderType(final EntityType<? extends Raider> aqb, final int[] arr) {
            this.entityType = aqb;
            this.spawnsPerWaveBeforeBonus = arr;
        }
        
        static {
            VALUES = values();
        }
    }
}
