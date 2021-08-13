package net.minecraft.world.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobType;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.entity.Mob;
import java.util.function.Function;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.schedule.Activity;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;

public class Zoglin extends Monster implements Enemy, HoglinBase {
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID;
    private int attackAnimationRemainingTicks;
    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Zoglin>>> SENSOR_TYPES;
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES;
    
    public Zoglin(final EntityType<? extends Zoglin> aqb, final Level bru) {
        super(aqb, bru);
        this.xpReward = 5;
    }
    
    protected Brain.Provider<Zoglin> brainProvider() {
        return Brain.<Zoglin>provider(Zoglin.MEMORY_TYPES, (java.util.Collection<? extends SensorType<? extends Sensor<? super Zoglin>>>)Zoglin.SENSOR_TYPES);
    }
    
    protected Brain<?> makeBrain(final Dynamic<?> dynamic) {
        final Brain<Zoglin> arc3 = this.brainProvider().makeBrain(dynamic);
        initCoreActivity(arc3);
        initIdleActivity(arc3);
        initFightActivity(arc3);
        arc3.setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
        arc3.setDefaultActivity(Activity.IDLE);
        arc3.useDefaultActivity();
        return arc3;
    }
    
    private static void initCoreActivity(final Brain<Zoglin> arc) {
        arc.addActivity(Activity.CORE, 0, (com.google.common.collect.ImmutableList<? extends Behavior<? super Zoglin>>)ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }
    
    private static void initIdleActivity(final Brain<Zoglin> arc) {
        arc.addActivity(Activity.IDLE, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Zoglin>>)ImmutableList.of(new StartAttacking((java.util.function.Function<Mob, Optional<? extends LivingEntity>>)Zoglin::findNearestValidAttackTarget), new RunSometimes(new SetEntityLookTarget(8.0f), IntRange.of(30, 60)), new RunOne((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of((Object)Pair.of((Object)new RandomStroll(0.4f), (Object)2), (Object)Pair.of((Object)new SetWalkTargetFromLookTarget(0.4f, 3), (Object)2), (Object)Pair.of((Object)new DoNothing(30, 60), (Object)1)))));
    }
    
    private static void initFightActivity(final Brain<Zoglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Zoglin>>)ImmutableList.of(new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0f), new RunIf((java.util.function.Predicate<LivingEntity>)Zoglin::isAdult, new MeleeAttack(40)), new RunIf((java.util.function.Predicate<LivingEntity>)Zoglin::isBaby, new MeleeAttack(15)), new StopAttackingIfTargetInvalid()), MemoryModuleType.ATTACK_TARGET);
    }
    
    private Optional<? extends LivingEntity> findNearestValidAttackTarget() {
        return ((List)this.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())).stream().filter(Zoglin::isTargetable).findFirst();
    }
    
    private static boolean isTargetable(final LivingEntity aqj) {
        final EntityType<?> aqb2 = aqj.getType();
        return aqb2 != EntityType.ZOGLIN && aqb2 != EntityType.CREEPER && EntitySelector.ATTACK_ALLOWED.test(aqj);
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Zoglin.DATA_BABY_ID, false);
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (Zoglin.DATA_BABY_ID.equals(us)) {
            this.refreshDimensions();
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 6.0);
    }
    
    public boolean isAdult() {
        return !this.isBaby();
    }
    
    public boolean doHurtTarget(final Entity apx) {
        if (!(apx instanceof LivingEntity)) {
            return false;
        }
        this.attackAnimationRemainingTicks = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        this.playSound(SoundEvents.ZOGLIN_ATTACK, 1.0f, this.getVoicePitch());
        return HoglinBase.hurtAndThrowTarget(this, (LivingEntity)apx);
    }
    
    public boolean canBeLeashed(final Player bft) {
        return !this.isLeashed();
    }
    
    protected void blockedByShield(final LivingEntity aqj) {
        if (!this.isBaby()) {
            HoglinBase.throwTarget(this, aqj);
        }
    }
    
    public double getPassengersRidingOffset() {
        return this.getBbHeight() - (this.isBaby() ? 0.2 : 0.15);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        final boolean boolean4 = super.hurt(aph, float2);
        if (this.level.isClientSide) {
            return false;
        }
        if (!boolean4 || !(aph.getEntity() instanceof LivingEntity)) {
            return boolean4;
        }
        final LivingEntity aqj5 = (LivingEntity)aph.getEntity();
        if (EntitySelector.ATTACK_ALLOWED.test(aqj5) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(this, aqj5, 4.0)) {
            this.setAttackTarget(aqj5);
        }
        return boolean4;
    }
    
    private void setAttackTarget(final LivingEntity aqj) {
        this.brain.<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        this.brain.<LivingEntity>setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, aqj, 200L);
    }
    
    public Brain<Zoglin> getBrain() {
        return (Brain<Zoglin>)super.getBrain();
    }
    
    protected void updateActivity() {
        final Activity bhc2 = (Activity)this.brain.getActiveNonCoreActivity().orElse(null);
        this.brain.setActiveActivityToFirstValid((List<Activity>)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        final Activity bhc3 = (Activity)this.brain.getActiveNonCoreActivity().orElse(null);
        if (bhc3 == Activity.FIGHT && bhc2 != Activity.FIGHT) {
            this.playAngrySound();
        }
        this.setAggressive(this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
    
    protected void customServerAiStep() {
        this.level.getProfiler().push("zoglinBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        this.updateActivity();
    }
    
    public void setBaby(final boolean boolean1) {
        this.getEntityData().<Boolean>set(Zoglin.DATA_BABY_ID, boolean1);
        if (!this.level.isClientSide && boolean1) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5);
        }
    }
    
    public boolean isBaby() {
        return this.getEntityData().<Boolean>get(Zoglin.DATA_BABY_ID);
    }
    
    @Override
    public void aiStep() {
        if (this.attackAnimationRemainingTicks > 0) {
            --this.attackAnimationRemainingTicks;
        }
        super.aiStep();
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 4) {
            this.attackAnimationRemainingTicks = 10;
            this.playSound(SoundEvents.ZOGLIN_ATTACK, 1.0f, this.getVoicePitch());
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    @Override
    public int getAttackAnimationRemainingTicks() {
        return this.attackAnimationRemainingTicks;
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.level.isClientSide) {
            return null;
        }
        if (this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return SoundEvents.ZOGLIN_ANGRY;
        }
        return SoundEvents.ZOGLIN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ZOGLIN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOGLIN_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.ZOGLIN_STEP, 0.15f, 1.0f);
    }
    
    protected void playAngrySound() {
        this.playSound(SoundEvents.ZOGLIN_ANGRY, 1.0f, this.getVoicePitch());
    }
    
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }
    
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.isBaby()) {
            md.putBoolean("IsBaby", true);
        }
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.getBoolean("IsBaby")) {
            this.setBaby(true);
        }
    }
    
    static {
        DATA_BABY_ID = SynchedEntityData.<Boolean>defineId(Zoglin.class, EntityDataSerializers.BOOLEAN);
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
    }
}
