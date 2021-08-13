package net.minecraft.world.entity.npc;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import java.util.stream.Collectors;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.gossip.GossipType;
import java.util.function.Predicate;
import net.minecraft.server.MinecraftServer;
import java.util.Optional;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.ListTag;
import com.mojang.serialization.DataResult;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import java.util.Iterator;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.server.level.ServerLevel;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.function.BiPredicate;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import java.util.Set;
import net.minecraft.world.item.Item;
import java.util.Map;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ReputationEventHandler;

public class Villager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA;
    public static final Map<Item, Integer> FOOD_POINTS;
    private static final Set<Item> WANTED_ITEMS;
    private int updateMerchantTimer;
    private boolean increaseProfessionLevelOnUpdate;
    @Nullable
    private Player lastTradedPlayer;
    private byte foodLevel;
    private final GossipContainer gossips;
    private long lastGossipTime;
    private long lastGossipDecayTime;
    private int villagerXp;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;
    private boolean assignProfessionWhenSpawned;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    private static final ImmutableList<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<Villager, PoiType>> POI_MEMORIES;
    
    public Villager(final EntityType<? extends Villager> aqb, final Level bru) {
        this(aqb, bru, VillagerType.PLAINS);
    }
    
    public Villager(final EntityType<? extends Villager> aqb, final Level bru, final VillagerType bfl) {
        super(aqb, bru);
        this.gossips = new GossipContainer();
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setCanPickUpLoot(true);
        this.setVillagerData(this.getVillagerData().setType(bfl).setProfession(VillagerProfession.NONE));
    }
    
    public Brain<Villager> getBrain() {
        return (Brain<Villager>)super.getBrain();
    }
    
    protected Brain.Provider<Villager> brainProvider() {
        return Brain.<Villager>provider(Villager.MEMORY_TYPES, (java.util.Collection<? extends SensorType<? extends Sensor<? super Villager>>>)Villager.SENSOR_TYPES);
    }
    
    protected Brain<?> makeBrain(final Dynamic<?> dynamic) {
        final Brain<Villager> arc3 = this.brainProvider().makeBrain(dynamic);
        this.registerBrainGoals(arc3);
        return arc3;
    }
    
    public void refreshBrain(final ServerLevel aag) {
        final Brain<Villager> arc3 = this.getBrain();
        arc3.stopAll(aag, this);
        this.brain = arc3.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }
    
    private void registerBrainGoals(final Brain<Villager> arc) {
        final VillagerProfession bfj3 = this.getVillagerData().getProfession();
        if (this.isBaby()) {
            arc.setSchedule(Schedule.VILLAGER_BABY);
            arc.addActivity(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5f));
        }
        else {
            arc.setSchedule(Schedule.VILLAGER_DEFAULT);
            arc.addActivityWithConditions(Activity.WORK, VillagerGoalPackages.getWorkPackage(bfj3, 0.5f), (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)ImmutableSet.of(Pair.of((Object)MemoryModuleType.JOB_SITE, (Object)MemoryStatus.VALUE_PRESENT)));
        }
        arc.addActivity(Activity.CORE, VillagerGoalPackages.getCorePackage(bfj3, 0.5f));
        arc.addActivityWithConditions(Activity.MEET, VillagerGoalPackages.getMeetPackage(bfj3, 0.5f), (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)ImmutableSet.of(Pair.of((Object)MemoryModuleType.MEETING_POINT, (Object)MemoryStatus.VALUE_PRESENT)));
        arc.addActivity(Activity.REST, VillagerGoalPackages.getRestPackage(bfj3, 0.5f));
        arc.addActivity(Activity.IDLE, VillagerGoalPackages.getIdlePackage(bfj3, 0.5f));
        arc.addActivity(Activity.PANIC, VillagerGoalPackages.getPanicPackage(bfj3, 0.5f));
        arc.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(bfj3, 0.5f));
        arc.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage(bfj3, 0.5f));
        arc.addActivity(Activity.HIDE, VillagerGoalPackages.getHidePackage(bfj3, 0.5f));
        arc.setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
        arc.setDefaultActivity(Activity.IDLE);
        arc.setActiveActivityIfPossible(Activity.IDLE);
        arc.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }
    
    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (this.level instanceof ServerLevel) {
            this.refreshBrain((ServerLevel)this.level);
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 48.0);
    }
    
    public boolean assignProfessionWhenSpawned() {
        return this.assignProfessionWhenSpawned;
    }
    
    protected void customServerAiStep() {
        this.level.getProfiler().push("villagerBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        if (this.assignProfessionWhenSpawned) {
            this.assignProfessionWhenSpawned = false;
        }
        if (!this.isTrading() && this.updateMerchantTimer > 0) {
            --this.updateMerchantTimer;
            if (this.updateMerchantTimer <= 0) {
                if (this.increaseProfessionLevelOnUpdate) {
                    this.increaseMerchantCareer();
                    this.increaseProfessionLevelOnUpdate = false;
                }
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
            }
        }
        if (this.lastTradedPlayer != null && this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).onReputationEvent(ReputationEventType.TRADE, this.lastTradedPlayer, this);
            this.level.broadcastEntityEvent(this, (byte)14);
            this.lastTradedPlayer = null;
        }
        if (!this.isNoAi() && this.random.nextInt(100) == 0) {
            final Raid bgy2 = ((ServerLevel)this.level).getRaidAt(this.blockPosition());
            if (bgy2 != null && bgy2.isActive() && !bgy2.isOver()) {
                this.level.broadcastEntityEvent(this, (byte)42);
            }
        }
        if (this.getVillagerData().getProfession() == VillagerProfession.NONE && this.isTrading()) {
            this.stopTrading();
        }
        super.customServerAiStep();
    }
    
    public void tick() {
        super.tick();
        if (this.getUnhappyCounter() > 0) {
            this.setUnhappyCounter(this.getUnhappyCounter() - 1);
        }
        this.maybeDecayGossip();
    }
    
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.VILLAGER_SPAWN_EGG || !this.isAlive() || this.isTrading() || this.isSleeping()) {
            return super.mobInteract(bft, aoq);
        }
        if (this.isBaby()) {
            this.setUnhappy();
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        final boolean boolean5 = this.getOffers().isEmpty();
        if (aoq == InteractionHand.MAIN_HAND) {
            if (boolean5 && !this.level.isClientSide) {
                this.setUnhappy();
            }
            bft.awardStat(Stats.TALKED_TO_VILLAGER);
        }
        if (boolean5) {
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (!this.level.isClientSide && !this.offers.isEmpty()) {
            this.startTrading(bft);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
    
    private void setUnhappy() {
        this.setUnhappyCounter(40);
        if (!this.level.isClientSide()) {
            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
        }
    }
    
    private void startTrading(final Player bft) {
        this.updateSpecialPrices(bft);
        this.setTradingPlayer(bft);
        this.openTradingScreen(bft, this.getDisplayName(), this.getVillagerData().getLevel());
    }
    
    @Override
    public void setTradingPlayer(@Nullable final Player bft) {
        final boolean boolean3 = this.getTradingPlayer() != null && bft == null;
        super.setTradingPlayer(bft);
        if (boolean3) {
            this.stopTrading();
        }
    }
    
    @Override
    protected void stopTrading() {
        super.stopTrading();
        this.resetSpecialPrices();
    }
    
    private void resetSpecialPrices() {
        for (final MerchantOffer bqs3 : this.getOffers()) {
            bqs3.resetSpecialPriceDiff();
        }
    }
    
    @Override
    public boolean canRestock() {
        return true;
    }
    
    public void restock() {
        this.updateDemand();
        for (final MerchantOffer bqs3 : this.getOffers()) {
            bqs3.resetUses();
        }
        this.lastRestockGameTime = this.level.getGameTime();
        ++this.numberOfRestocksToday;
    }
    
    private boolean needsToRestock() {
        for (final MerchantOffer bqs3 : this.getOffers()) {
            if (bqs3.needsRestock()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean allowedToRestock() {
        return this.numberOfRestocksToday == 0 || (this.numberOfRestocksToday < 2 && this.level.getGameTime() > this.lastRestockGameTime + 2400L);
    }
    
    public boolean shouldRestock() {
        final long long2 = this.lastRestockGameTime + 12000L;
        final long long3 = this.level.getGameTime();
        boolean boolean6 = long3 > long2;
        final long long4 = this.level.getDayTime();
        if (this.lastRestockCheckDayTime > 0L) {
            final long long5 = this.lastRestockCheckDayTime / 24000L;
            final long long6 = long4 / 24000L;
            boolean6 |= (long6 > long5);
        }
        this.lastRestockCheckDayTime = long4;
        if (boolean6) {
            this.lastRestockGameTime = long3;
            this.resetNumberOfRestocks();
        }
        return this.allowedToRestock() && this.needsToRestock();
    }
    
    private void catchUpDemand() {
        final int integer2 = 2 - this.numberOfRestocksToday;
        if (integer2 > 0) {
            for (final MerchantOffer bqs4 : this.getOffers()) {
                bqs4.resetUses();
            }
        }
        for (int integer3 = 0; integer3 < integer2; ++integer3) {
            this.updateDemand();
        }
    }
    
    private void updateDemand() {
        for (final MerchantOffer bqs3 : this.getOffers()) {
            bqs3.updateDemand();
        }
    }
    
    private void updateSpecialPrices(final Player bft) {
        final int integer3 = this.getPlayerReputation(bft);
        if (integer3 != 0) {
            for (final MerchantOffer bqs5 : this.getOffers()) {
                bqs5.addToSpecialPriceDiff(-Mth.floor(integer3 * bqs5.getPriceMultiplier()));
            }
        }
        if (bft.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            final MobEffectInstance apr4 = bft.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
            final int integer4 = apr4.getAmplifier();
            for (final MerchantOffer bqs6 : this.getOffers()) {
                final double double8 = 0.3 + 0.0625 * integer4;
                final int integer5 = (int)Math.floor(double8 * bqs6.getBaseCostA().getCount());
                bqs6.addToSpecialPriceDiff(-Math.max(integer5, 1));
            }
        }
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<VillagerData>define(Villager.DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        VillagerData.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.getVillagerData()).resultOrPartial(Villager.LOGGER::error).ifPresent(mt -> md.put("VillagerData", mt));
        md.putByte("FoodLevel", this.foodLevel);
        md.put("Gossips", (Tag)this.gossips.store((com.mojang.serialization.DynamicOps<Object>)NbtOps.INSTANCE).getValue());
        md.putInt("Xp", this.villagerXp);
        md.putLong("LastRestock", this.lastRestockGameTime);
        md.putLong("LastGossipDecay", this.lastGossipDecayTime);
        md.putInt("RestocksToday", this.numberOfRestocksToday);
        if (this.assignProfessionWhenSpawned) {
            md.putBoolean("AssignProfessionWhenSpawned", true);
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("VillagerData", 10)) {
            final DataResult<VillagerData> dataResult3 = (DataResult<VillagerData>)VillagerData.CODEC.parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, md.get("VillagerData")));
            dataResult3.resultOrPartial(Villager.LOGGER::error).ifPresent(this::setVillagerData);
        }
        if (md.contains("Offers", 10)) {
            this.offers = new MerchantOffers(md.getCompound("Offers"));
        }
        if (md.contains("FoodLevel", 1)) {
            this.foodLevel = md.getByte("FoodLevel");
        }
        final ListTag mj3 = md.getList("Gossips", 10);
        this.gossips.update(new Dynamic((DynamicOps)NbtOps.INSTANCE, mj3));
        if (md.contains("Xp", 3)) {
            this.villagerXp = md.getInt("Xp");
        }
        this.lastRestockGameTime = md.getLong("LastRestock");
        this.lastGossipDecayTime = md.getLong("LastGossipDecay");
        this.setCanPickUpLoot(true);
        if (this.level instanceof ServerLevel) {
            this.refreshBrain((ServerLevel)this.level);
        }
        this.numberOfRestocksToday = md.getInt("RestocksToday");
        if (md.contains("AssignProfessionWhenSpawned")) {
            this.assignProfessionWhenSpawned = md.getBoolean("AssignProfessionWhenSpawned");
        }
    }
    
    public boolean removeWhenFarAway(final double double1) {
        return false;
    }
    
    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        }
        if (this.isTrading()) {
            return SoundEvents.VILLAGER_TRADE;
        }
        return SoundEvents.VILLAGER_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.VILLAGER_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
    
    public void playWorkSound() {
        final SoundEvent adn2 = this.getVillagerData().getProfession().getWorkSound();
        if (adn2 != null) {
            this.playSound(adn2, this.getSoundVolume(), this.getVoicePitch());
        }
    }
    
    public void setVillagerData(final VillagerData bfh) {
        final VillagerData bfh2 = this.getVillagerData();
        if (bfh2.getProfession() != bfh.getProfession()) {
            this.offers = null;
        }
        this.entityData.<VillagerData>set(Villager.DATA_VILLAGER_DATA, bfh);
    }
    
    @Override
    public VillagerData getVillagerData() {
        return this.entityData.<VillagerData>get(Villager.DATA_VILLAGER_DATA);
    }
    
    @Override
    protected void rewardTradeXp(final MerchantOffer bqs) {
        int integer3 = 3 + this.random.nextInt(4);
        this.villagerXp += bqs.getXp();
        this.lastTradedPlayer = this.getTradingPlayer();
        if (this.shouldIncreaseLevel()) {
            this.updateMerchantTimer = 40;
            this.increaseProfessionLevelOnUpdate = true;
            integer3 += 5;
        }
        if (bqs.shouldRewardExp()) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5, this.getZ(), integer3));
        }
    }
    
    public void setLastHurtByMob(@Nullable final LivingEntity aqj) {
        if (aqj != null && this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).onReputationEvent(ReputationEventType.VILLAGER_HURT, aqj, this);
            if (this.isAlive() && aqj instanceof Player) {
                this.level.broadcastEntityEvent(this, (byte)13);
            }
        }
        super.setLastHurtByMob(aqj);
    }
    
    @Override
    public void die(final DamageSource aph) {
        Villager.LOGGER.info("Villager {} died, message: '{}'", this, aph.getLocalizedDeathMessage(this).getString());
        final Entity apx3 = aph.getEntity();
        if (apx3 != null) {
            this.tellWitnessesThatIWasMurdered(apx3);
        }
        this.releaseAllPois();
        super.die(aph);
    }
    
    private void releaseAllPois() {
        this.releasePoi(MemoryModuleType.HOME);
        this.releasePoi(MemoryModuleType.JOB_SITE);
        this.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
        this.releasePoi(MemoryModuleType.MEETING_POINT);
    }
    
    private void tellWitnessesThatIWasMurdered(final Entity apx) {
        if (!(this.level instanceof ServerLevel)) {
            return;
        }
        final Optional<List<LivingEntity>> optional3 = this.brain.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
        if (!optional3.isPresent()) {
            return;
        }
        final ServerLevel aag4 = (ServerLevel)this.level;
        ((List)optional3.get()).stream().filter(aqj -> aqj instanceof ReputationEventHandler).forEach(aqj -> aag4.onReputationEvent(ReputationEventType.VILLAGER_KILLED, apx, (ReputationEventHandler)aqj));
    }
    
    public void releasePoi(final MemoryModuleType<GlobalPos> aya) {
        if (!(this.level instanceof ServerLevel)) {
            return;
        }
        final MinecraftServer minecraftServer3 = ((ServerLevel)this.level).getServer();
        this.brain.<GlobalPos>getMemory(aya).ifPresent(gf -> {
            final ServerLevel aag5 = minecraftServer3.getLevel(gf.dimension());
            if (aag5 == null) {
                return;
            }
            final PoiManager azl6 = aag5.getPoiManager();
            final Optional<PoiType> optional7 = azl6.getType(gf.pos());
            final BiPredicate<Villager, PoiType> biPredicate8 = (BiPredicate<Villager, PoiType>)Villager.POI_MEMORIES.get(aya);
            if (optional7.isPresent() && biPredicate8.test(this, optional7.get())) {
                azl6.release(gf.pos());
                DebugPackets.sendPoiTicketCountPacket(aag5, gf.pos());
            }
        });
    }
    
    @Override
    public boolean canBreed() {
        return this.foodLevel + this.countFoodPointsInInventory() >= 12 && this.getAge() == 0;
    }
    
    private boolean hungry() {
        return this.foodLevel < 12;
    }
    
    private void eatUntilFull() {
        if (!this.hungry() || this.countFoodPointsInInventory() == 0) {
            return;
        }
        for (int integer2 = 0; integer2 < this.getInventory().getContainerSize(); ++integer2) {
            final ItemStack bly3 = this.getInventory().getItem(integer2);
            if (!bly3.isEmpty()) {
                final Integer integer3 = (Integer)Villager.FOOD_POINTS.get(bly3.getItem());
                if (integer3 != null) {
                    int integer5;
                    for (int integer4 = integer5 = bly3.getCount(); integer5 > 0; --integer5) {
                        this.foodLevel += (byte)integer3;
                        this.getInventory().removeItem(integer2, 1);
                        if (!this.hungry()) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    public int getPlayerReputation(final Player bft) {
        return this.gossips.getReputation(bft.getUUID(), (Predicate<GossipType>)(axx -> true));
    }
    
    private void digestFood(final int integer) {
        this.foodLevel -= (byte)integer;
    }
    
    public void eatAndDigestFood() {
        this.eatUntilFull();
        this.digestFood(12);
    }
    
    public void setOffers(final MerchantOffers bqt) {
        this.offers = bqt;
    }
    
    private boolean shouldIncreaseLevel() {
        final int integer2 = this.getVillagerData().getLevel();
        return VillagerData.canLevelUp(integer2) && this.villagerXp >= VillagerData.getMaxXpPerLevel(integer2);
    }
    
    private void increaseMerchantCareer() {
        this.setVillagerData(this.getVillagerData().setLevel(this.getVillagerData().getLevel() + 1));
        this.updateTrades();
    }
    
    protected Component getTypeName() {
        return new TranslatableComponent(this.getType().getDescriptionId() + '.' + Registry.VILLAGER_PROFESSION.getKey(this.getVillagerData().getProfession()).getPath());
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 12) {
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        }
        else if (byte1 == 13) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        }
        else if (byte1 == 14) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        }
        else if (byte1 == 42) {
            this.addParticlesAroundSelf(ParticleTypes.SPLASH);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqm == MobSpawnType.BREEDING) {
            this.setVillagerData(this.getVillagerData().setProfession(VillagerProfession.NONE));
        }
        if (aqm == MobSpawnType.COMMAND || aqm == MobSpawnType.SPAWN_EGG || aqm == MobSpawnType.SPAWNER || aqm == MobSpawnType.DISPENSER) {
            this.setVillagerData(this.getVillagerData().setType(VillagerType.byBiome(bsh.getBiomeName(this.blockPosition()))));
        }
        if (aqm == MobSpawnType.STRUCTURE) {
            this.assignProfessionWhenSpawned = true;
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    public Villager getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final double double5 = this.random.nextDouble();
        VillagerType bfl4;
        if (double5 < 0.5) {
            bfl4 = VillagerType.byBiome(aag.getBiomeName(this.blockPosition()));
        }
        else if (double5 < 0.75) {
            bfl4 = this.getVillagerData().getType();
        }
        else {
            bfl4 = ((Villager)apv).getVillagerData().getType();
        }
        final Villager bfg7 = new Villager(EntityType.VILLAGER, aag, bfl4);
        bfg7.finalizeSpawn(aag, aag.getCurrentDifficultyAt(bfg7.blockPosition()), MobSpawnType.BREEDING, null, null);
        return bfg7;
    }
    
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
        if (aag.getDifficulty() != Difficulty.PEACEFUL) {
            Villager.LOGGER.info("Villager {} was struck by lightning {}.", this, aqi);
            final Witch bed4 = EntityType.WITCH.create(aag);
            bed4.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            bed4.finalizeSpawn(aag, aag.getCurrentDifficultyAt(bed4.blockPosition()), MobSpawnType.CONVERSION, null, null);
            bed4.setNoAi(this.isNoAi());
            if (this.hasCustomName()) {
                bed4.setCustomName(this.getCustomName());
                bed4.setCustomNameVisible(this.isCustomNameVisible());
            }
            bed4.setPersistenceRequired();
            aag.addFreshEntityWithPassengers(bed4);
            this.releaseAllPois();
            this.remove();
        }
        else {
            super.thunderHit(aag, aqi);
        }
    }
    
    protected void pickUpItem(final ItemEntity bcs) {
        final ItemStack bly3 = bcs.getItem();
        if (this.wantsToPickUp(bly3)) {
            final SimpleContainer aox4 = this.getInventory();
            final boolean boolean5 = aox4.canAddItem(bly3);
            if (!boolean5) {
                return;
            }
            this.onItemPickup(bcs);
            this.take(bcs, bly3.getCount());
            final ItemStack bly4 = aox4.addItem(bly3);
            if (bly4.isEmpty()) {
                bcs.remove();
            }
            else {
                bly3.setCount(bly4.getCount());
            }
        }
    }
    
    public boolean wantsToPickUp(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        return (Villager.WANTED_ITEMS.contains(blu3) || this.getVillagerData().getProfession().getRequestedItems().contains(blu3)) && this.getInventory().canAddItem(bly);
    }
    
    public boolean hasExcessFood() {
        return this.countFoodPointsInInventory() >= 24;
    }
    
    public boolean wantsMoreFood() {
        return this.countFoodPointsInInventory() < 12;
    }
    
    private int countFoodPointsInInventory() {
        final SimpleContainer aox2 = this.getInventory();
        return Villager.FOOD_POINTS.entrySet().stream().mapToInt(entry -> aox2.countItem((Item)entry.getKey()) * (int)entry.getValue()).sum();
    }
    
    public boolean hasFarmSeeds() {
        return this.getInventory().hasAnyOf((Set<Item>)ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS));
    }
    
    @Override
    protected void updateTrades() {
        final VillagerData bfh2 = this.getVillagerData();
        final Int2ObjectMap<VillagerTrades.ItemListing[]> int2ObjectMap3 = (Int2ObjectMap<VillagerTrades.ItemListing[]>)VillagerTrades.TRADES.get(bfh2.getProfession());
        if (int2ObjectMap3 == null || int2ObjectMap3.isEmpty()) {
            return;
        }
        final VillagerTrades.ItemListing[] arr4 = (VillagerTrades.ItemListing[])int2ObjectMap3.get(bfh2.getLevel());
        if (arr4 == null) {
            return;
        }
        final MerchantOffers bqt5 = this.getOffers();
        this.addOffersFromItemListings(bqt5, arr4, 2);
    }
    
    public void gossip(final ServerLevel aag, final Villager bfg, final long long3) {
        if ((long3 >= this.lastGossipTime && long3 < this.lastGossipTime + 1200L) || (long3 >= bfg.lastGossipTime && long3 < bfg.lastGossipTime + 1200L)) {
            return;
        }
        this.gossips.transferFrom(bfg.gossips, this.random, 10);
        this.lastGossipTime = long3;
        this.spawnGolemIfNeeded(aag, bfg.lastGossipTime = long3, 5);
    }
    
    private void maybeDecayGossip() {
        final long long2 = this.level.getGameTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = long2;
            return;
        }
        if (long2 < this.lastGossipDecayTime + 24000L) {
            return;
        }
        this.gossips.decay();
        this.lastGossipDecayTime = long2;
    }
    
    public void spawnGolemIfNeeded(final ServerLevel aag, final long long2, final int integer) {
        if (!this.wantsToSpawnGolem(long2)) {
            return;
        }
        final AABB dcf6 = this.getBoundingBox().inflate(10.0, 10.0, 10.0);
        final List<Villager> list7 = aag.<Villager>getEntitiesOfClass((java.lang.Class<? extends Villager>)Villager.class, dcf6);
        final List<Villager> list8 = (List<Villager>)list7.stream().filter(bfg -> bfg.wantsToSpawnGolem(long2)).limit(5L).collect(Collectors.toList());
        if (list8.size() < integer) {
            return;
        }
        final IronGolem baf9 = this.trySpawnGolem(aag);
        if (baf9 == null) {
            return;
        }
        list7.forEach(GolemSensor::golemDetected);
    }
    
    public boolean wantsToSpawnGolem(final long long1) {
        return this.golemSpawnConditionsMet(this.level.getGameTime()) && !this.brain.hasMemoryValue(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
    }
    
    @Nullable
    private IronGolem trySpawnGolem(final ServerLevel aag) {
        final BlockPos fx3 = this.blockPosition();
        for (int integer4 = 0; integer4 < 10; ++integer4) {
            final double double5 = aag.random.nextInt(16) - 8;
            final double double6 = aag.random.nextInt(16) - 8;
            final BlockPos fx4 = this.findSpawnPositionForGolemInColumn(fx3, double5, double6);
            if (fx4 != null) {
                final IronGolem baf10 = EntityType.IRON_GOLEM.create(aag, null, null, null, fx4, MobSpawnType.MOB_SUMMONED, false, false);
                if (baf10 != null) {
                    if (baf10.checkSpawnRules(aag, MobSpawnType.MOB_SUMMONED) && baf10.checkSpawnObstruction(aag)) {
                        aag.addFreshEntityWithPassengers(baf10);
                        return baf10;
                    }
                    baf10.remove();
                }
            }
        }
        return null;
    }
    
    @Nullable
    private BlockPos findSpawnPositionForGolemInColumn(final BlockPos fx, final double double2, final double double3) {
        final int integer7 = 6;
        BlockPos fx2 = fx.offset(double2, 6.0, double3);
        BlockState cee9 = this.level.getBlockState(fx2);
        for (int integer8 = 6; integer8 >= -6; --integer8) {
            final BlockPos fx3 = fx2;
            final BlockState cee10 = cee9;
            fx2 = fx3.below();
            cee9 = this.level.getBlockState(fx2);
            if ((cee10.isAir() || cee10.getMaterial().isLiquid()) && cee9.getMaterial().isSolidBlocking()) {
                return fx3;
            }
        }
        return null;
    }
    
    @Override
    public void onReputationEventFrom(final ReputationEventType azi, final Entity apx) {
        if (azi == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
            this.gossips.add(apx.getUUID(), GossipType.MAJOR_POSITIVE, 20);
            this.gossips.add(apx.getUUID(), GossipType.MINOR_POSITIVE, 25);
        }
        else if (azi == ReputationEventType.TRADE) {
            this.gossips.add(apx.getUUID(), GossipType.TRADING, 2);
        }
        else if (azi == ReputationEventType.VILLAGER_HURT) {
            this.gossips.add(apx.getUUID(), GossipType.MINOR_NEGATIVE, 25);
        }
        else if (azi == ReputationEventType.VILLAGER_KILLED) {
            this.gossips.add(apx.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
        }
    }
    
    @Override
    public int getVillagerXp() {
        return this.villagerXp;
    }
    
    public void setVillagerXp(final int integer) {
        this.villagerXp = integer;
    }
    
    private void resetNumberOfRestocks() {
        this.catchUpDemand();
        this.numberOfRestocksToday = 0;
    }
    
    public GossipContainer getGossips() {
        return this.gossips;
    }
    
    public void setGossips(final Tag mt) {
        this.gossips.update(new Dynamic((DynamicOps)NbtOps.INSTANCE, mt));
    }
    
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }
    
    public void startSleeping(final BlockPos fx) {
        super.startSleeping(fx);
        this.brain.<Long>setMemory(MemoryModuleType.LAST_SLEPT, this.level.getGameTime());
        this.brain.<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        this.brain.<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
    
    public void stopSleeping() {
        super.stopSleeping();
        this.brain.<Long>setMemory(MemoryModuleType.LAST_WOKEN, this.level.getGameTime());
    }
    
    private boolean golemSpawnConditionsMet(final long long1) {
        final Optional<Long> optional4 = this.brain.<Long>getMemory(MemoryModuleType.LAST_SLEPT);
        return optional4.isPresent() && long1 - (long)optional4.get() < 24000L;
    }
    
    static {
        DATA_VILLAGER_DATA = SynchedEntityData.<VillagerData>defineId(Villager.class, EntityDataSerializers.VILLAGER_DATA);
        FOOD_POINTS = (Map)ImmutableMap.of(Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
        WANTED_ITEMS = (Set)ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, (Object[])new Item[] { Items.BEETROOT_SEEDS });
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, (Object[])new MemoryModuleType[] { MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY });
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);
        POI_MEMORIES = (Map)ImmutableMap.of(MemoryModuleType.HOME, ((bfg, azo) -> azo == PoiType.HOME), MemoryModuleType.JOB_SITE, ((bfg, azo) -> bfg.getVillagerData().getProfession().getJobPoiType() == azo), MemoryModuleType.POTENTIAL_JOB_SITE, ((bfg, azo) -> PoiType.ALL_JOBS.test((Object)azo)), MemoryModuleType.MEETING_POINT, ((bfg, azo) -> azo == PoiType.MEETING));
    }
}
