package net.minecraft.world.entity.monster;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.block.Blocks;
import java.time.temporal.TemporalField;
import java.time.temporal.ChronoField;
import java.time.LocalDate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.animal.Chicken;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.Difficulty;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class Zombie extends Monster {
    private static final UUID SPEED_MODIFIER_BABY_UUID;
    private static final AttributeModifier SPEED_MODIFIER_BABY;
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID;
    private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID;
    private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID;
    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
    private final BreakDoorGoal breakDoorGoal;
    private boolean canBreakDoors;
    private int inWaterTime;
    private int conversionTime;
    
    public Zombie(final EntityType<? extends Zombie> aqb, final Level bru) {
        super(aqb, bru);
        this.breakDoorGoal = new BreakDoorGoal(this, Zombie.DOOR_BREAKING_PREDICATE);
    }
    
    public Zombie(final Level bru) {
        this(EntityType.ZOMBIE, bru);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new ZombieAttackTurtleEggGoal(this, 1.0, 3));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }
    
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ARMOR, 2.0).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().<Boolean>define(Zombie.DATA_BABY_ID, false);
        this.getEntityData().<Integer>define(Zombie.DATA_SPECIAL_TYPE_ID, 0);
        this.getEntityData().<Boolean>define(Zombie.DATA_DROWNED_CONVERSION_ID, false);
    }
    
    public boolean isUnderWaterConverting() {
        return this.getEntityData().<Boolean>get(Zombie.DATA_DROWNED_CONVERSION_ID);
    }
    
    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }
    
    public void setCanBreakDoors(final boolean boolean1) {
        if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != boolean1) {
                this.canBreakDoors = boolean1;
                ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(boolean1);
                if (boolean1) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                }
                else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        }
        else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }
    }
    
    protected boolean supportsBreakDoorGoal() {
        return true;
    }
    
    public boolean isBaby() {
        return this.getEntityData().<Boolean>get(Zombie.DATA_BABY_ID);
    }
    
    @Override
    protected int getExperienceReward(final Player bft) {
        if (this.isBaby()) {
            this.xpReward *= (int)2.5f;
        }
        return super.getExperienceReward(bft);
    }
    
    @Override
    public void setBaby(final boolean boolean1) {
        this.getEntityData().<Boolean>set(Zombie.DATA_BABY_ID, boolean1);
        if (this.level != null && !this.level.isClientSide) {
            final AttributeInstance are3 = this.getAttribute(Attributes.MOVEMENT_SPEED);
            are3.removeModifier(Zombie.SPEED_MODIFIER_BABY);
            if (boolean1) {
                are3.addTransientModifier(Zombie.SPEED_MODIFIER_BABY);
            }
        }
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Zombie.DATA_BABY_ID.equals(us)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(us);
    }
    
    protected boolean convertsInWater() {
        return true;
    }
    
    @Override
    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && !this.isNoAi()) {
            if (this.isUnderWaterConverting()) {
                --this.conversionTime;
                if (this.conversionTime < 0) {
                    this.doUnderWaterConversion();
                }
            }
            else if (this.convertsInWater()) {
                if (this.isEyeInFluid(FluidTags.WATER)) {
                    ++this.inWaterTime;
                    if (this.inWaterTime >= 600) {
                        this.startUnderWaterConversion(300);
                    }
                }
                else {
                    this.inWaterTime = -1;
                }
            }
        }
        super.tick();
    }
    
    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean boolean2 = this.isSunSensitive() && this.isSunBurnTick();
            if (boolean2) {
                final ItemStack bly3 = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!bly3.isEmpty()) {
                    if (bly3.isDamageableItem()) {
                        bly3.setDamageValue(bly3.getDamageValue() + this.random.nextInt(2));
                        if (bly3.getDamageValue() >= bly3.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }
                    boolean2 = false;
                }
                if (boolean2) {
                    this.setSecondsOnFire(8);
                }
            }
        }
        super.aiStep();
    }
    
    private void startUnderWaterConversion(final int integer) {
        this.conversionTime = integer;
        this.getEntityData().<Boolean>set(Zombie.DATA_DROWNED_CONVERSION_ID, true);
    }
    
    protected void doUnderWaterConversion() {
        this.convertToZombieType(EntityType.DROWNED);
        if (!this.isSilent()) {
            this.level.levelEvent(null, 1040, this.blockPosition(), 0);
        }
    }
    
    protected void convertToZombieType(final EntityType<? extends Zombie> aqb) {
        final Zombie beg3 = this.<Zombie>convertTo(aqb, true);
        if (beg3 != null) {
            beg3.handleAttributes(beg3.level.getCurrentDifficultyAt(beg3.blockPosition()).getSpecialMultiplier());
            beg3.setCanBreakDoors(beg3.supportsBreakDoorGoal() && this.canBreakDoors());
        }
    }
    
    protected boolean isSunSensitive() {
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (!super.hurt(aph, float2)) {
            return false;
        }
        if (!(this.level instanceof ServerLevel)) {
            return false;
        }
        final ServerLevel aag4 = (ServerLevel)this.level;
        LivingEntity aqj5 = this.getTarget();
        if (aqj5 == null && aph.getEntity() instanceof LivingEntity) {
            aqj5 = (LivingEntity)aph.getEntity();
        }
        if (aqj5 != null && this.level.getDifficulty() == Difficulty.HARD && this.random.nextFloat() < this.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            final int integer6 = Mth.floor(this.getX());
            final int integer7 = Mth.floor(this.getY());
            final int integer8 = Mth.floor(this.getZ());
            final Zombie beg9 = new Zombie(this.level);
            for (int integer9 = 0; integer9 < 50; ++integer9) {
                final int integer10 = integer6 + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                final int integer11 = integer7 + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                final int integer12 = integer8 + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                final BlockPos fx14 = new BlockPos(integer10, integer11, integer12);
                final EntityType<?> aqb15 = beg9.getType();
                final SpawnPlacements.Type c16 = SpawnPlacements.getPlacementType(aqb15);
                if (NaturalSpawner.isSpawnPositionOk(c16, this.level, fx14, aqb15) && SpawnPlacements.checkSpawnRules(aqb15, aag4, MobSpawnType.REINFORCEMENT, fx14, this.level.random)) {
                    beg9.setPos(integer10, integer11, integer12);
                    if (!this.level.hasNearbyAlivePlayer(integer10, integer11, integer12, 7.0) && this.level.isUnobstructed(beg9) && this.level.noCollision(beg9) && !this.level.containsAnyLiquid(beg9.getBoundingBox())) {
                        beg9.setTarget(aqj5);
                        beg9.finalizeSpawn(aag4, this.level.getCurrentDifficultyAt(beg9.blockPosition()), MobSpawnType.REINFORCEMENT, null, null);
                        aag4.addFreshEntityWithPassengers(beg9);
                        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806, AttributeModifier.Operation.ADDITION));
                        beg9.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806, AttributeModifier.Operation.ADDITION));
                        break;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        final boolean boolean3 = super.doHurtTarget(apx);
        if (boolean3) {
            final float float4 = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < float4 * 0.3f) {
                apx.setSecondsOnFire(2 * (int)float4);
            }
        }
        return boolean3;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ZOMBIE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }
    
    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }
    
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        super.populateDefaultEquipmentSlots(aop);
        if (this.random.nextFloat() < ((this.level.getDifficulty() == Difficulty.HARD) ? 0.05f : 0.01f)) {
            final int integer3 = this.random.nextInt(3);
            if (integer3 == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("IsBaby", this.isBaby());
        md.putBoolean("CanBreakDoors", this.canBreakDoors());
        md.putInt("InWaterTime", this.isInWater() ? this.inWaterTime : -1);
        md.putInt("DrownedConversionTime", this.isUnderWaterConverting() ? this.conversionTime : -1);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setBaby(md.getBoolean("IsBaby"));
        this.setCanBreakDoors(md.getBoolean("CanBreakDoors"));
        this.inWaterTime = md.getInt("InWaterTime");
        if (md.contains("DrownedConversionTime", 99) && md.getInt("DrownedConversionTime") > -1) {
            this.startUnderWaterConversion(md.getInt("DrownedConversionTime"));
        }
    }
    
    public void killed(final ServerLevel aag, final LivingEntity aqj) {
        super.killed(aag, aqj);
        if ((aag.getDifficulty() == Difficulty.NORMAL || aag.getDifficulty() == Difficulty.HARD) && aqj instanceof Villager) {
            if (aag.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return;
            }
            final Villager bfg4 = (Villager)aqj;
            final ZombieVillager beh5 = bfg4.<ZombieVillager>convertTo(EntityType.ZOMBIE_VILLAGER, false);
            beh5.finalizeSpawn(aag, aag.getCurrentDifficultyAt(beh5.blockPosition()), MobSpawnType.CONVERSION, new ZombieGroupData(false, true), null);
            beh5.setVillagerData(bfg4.getVillagerData());
            beh5.setGossips((net.minecraft.nbt.Tag)bfg4.getGossips().store((com.mojang.serialization.DynamicOps<Object>)NbtOps.INSTANCE).getValue());
            beh5.setTradeOffers(bfg4.getOffers().createTag());
            beh5.setVillagerXp(bfg4.getVillagerXp());
            if (!this.isSilent()) {
                aag.levelEvent(null, 1026, this.blockPosition(), 0);
            }
        }
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return this.isBaby() ? 0.93f : 1.74f;
    }
    
    @Override
    public boolean canHoldItem(final ItemStack bly) {
        return (bly.getItem() != Items.EGG || !this.isBaby() || !this.isPassenger()) && super.canHoldItem(bly);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        aqz = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        final float float7 = aop.getSpecialMultiplier();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55f * float7);
        if (aqz == null) {
            aqz = new ZombieGroupData(getSpawnAsBabyOdds(bsh.getRandom()), true);
        }
        if (aqz instanceof ZombieGroupData) {
            final ZombieGroupData b8 = (ZombieGroupData)aqz;
            if (b8.isBaby) {
                this.setBaby(true);
                if (b8.canSpawnJockey) {
                    if (bsh.getRandom().nextFloat() < 0.05) {
                        final List<Chicken> list9 = bsh.<Chicken>getEntitiesOfClass((java.lang.Class<? extends Chicken>)Chicken.class, this.getBoundingBox().inflate(5.0, 3.0, 5.0), (java.util.function.Predicate<? super Chicken>)EntitySelector.ENTITY_NOT_BEING_RIDDEN);
                        if (!list9.isEmpty()) {
                            final Chicken azz10 = (Chicken)list9.get(0);
                            azz10.setChickenJockey(true);
                            this.startRiding(azz10);
                        }
                    }
                    else if (bsh.getRandom().nextFloat() < 0.05) {
                        final Chicken azz11 = EntityType.CHICKEN.create(this.level);
                        azz11.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                        azz11.finalizeSpawn(bsh, aop, MobSpawnType.JOCKEY, null, null);
                        azz11.setChickenJockey(true);
                        this.startRiding(azz11);
                        bsh.addFreshEntity(azz11);
                    }
                }
            }
            this.setCanBreakDoors(this.supportsBreakDoorGoal() && this.random.nextFloat() < float7 * 0.1f);
            this.populateDefaultEquipmentSlots(aop);
            this.populateDefaultEquipmentEnchantments(aop);
        }
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            final LocalDate localDate8 = LocalDate.now();
            final int integer9 = localDate8.get((TemporalField)ChronoField.DAY_OF_MONTH);
            final int integer10 = localDate8.get((TemporalField)ChronoField.MONTH_OF_YEAR);
            if (integer10 == 10 && integer9 == 31 && this.random.nextFloat() < 0.25f) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((this.random.nextFloat() < 0.1f) ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0f;
            }
        }
        this.handleAttributes(float7);
        return aqz;
    }
    
    public static boolean getSpawnAsBabyOdds(final Random random) {
        return random.nextFloat() < 0.05f;
    }
    
    protected void handleAttributes(final float float1) {
        this.randomizeReinforcementsChance();
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806, AttributeModifier.Operation.ADDITION));
        final double double3 = this.random.nextDouble() * 1.5 * float1;
        if (double3 > 1.0) {
            this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random zombie-spawn bonus", double3, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (this.random.nextFloat() < float1 * 0.05f) {
            this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25 + 0.5, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0 + 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL));
            this.setCanBreakDoors(this.supportsBreakDoorGoal());
        }
    }
    
    protected void randomizeReinforcementsChance() {
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.random.nextDouble() * 0.10000000149011612);
    }
    
    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0 : -0.45;
    }
    
    @Override
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        final Entity apx5 = aph.getEntity();
        if (apx5 instanceof Creeper) {
            final Creeper bcz6 = (Creeper)apx5;
            if (bcz6.canDropMobsSkull()) {
                final ItemStack bly7 = this.getSkull();
                if (!bly7.isEmpty()) {
                    bcz6.increaseDroppedSkulls();
                    this.spawnAtLocation(bly7);
                }
            }
        }
    }
    
    protected ItemStack getSkull() {
        return new ItemStack(Items.ZOMBIE_HEAD);
    }
    
    static {
        SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
        SPEED_MODIFIER_BABY = new AttributeModifier(Zombie.SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5, AttributeModifier.Operation.MULTIPLY_BASE);
        DATA_BABY_ID = SynchedEntityData.<Boolean>defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
        DATA_SPECIAL_TYPE_ID = SynchedEntityData.<Integer>defineId(Zombie.class, EntityDataSerializers.INT);
        DATA_DROWNED_CONVERSION_ID = SynchedEntityData.<Boolean>defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
        DOOR_BREAKING_PREDICATE = (aoo -> aoo == Difficulty.HARD);
    }
    
    public static class ZombieGroupData implements SpawnGroupData {
        public final boolean isBaby;
        public final boolean canSpawnJockey;
        
        public ZombieGroupData(final boolean boolean1, final boolean boolean2) {
            this.isBaby = boolean1;
            this.canSpawnJockey = boolean2;
        }
    }
    
    class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
        ZombieAttackTurtleEggGoal(final PathfinderMob aqr, final double double3, final int integer) {
            super(Blocks.TURTLE_EGG, aqr, double3, integer);
        }
        
        @Override
        public void playDestroyProgressSound(final LevelAccessor brv, final BlockPos fx) {
            brv.playSound(null, fx, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5f, 0.9f + Zombie.this.random.nextFloat() * 0.2f);
        }
        
        @Override
        public void playBreakSound(final Level bru, final BlockPos fx) {
            bru.playSound(null, fx, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7f, 0.9f + bru.random.nextFloat() * 0.2f);
        }
        
        @Override
        public double acceptedDistance() {
            return 1.14;
        }
    }
}
