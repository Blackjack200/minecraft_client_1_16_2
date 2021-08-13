package net.minecraft.world.entity.monster.hoglin;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.LevelReader;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.animal.Animal;

public class Hoglin extends Animal implements Enemy, HoglinBase {
    private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION;
    private int attackAnimationRemainingTicks;
    private int timeInOverworld;
    private boolean cannotBeHunted;
    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Hoglin>>> SENSOR_TYPES;
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES;
    
    public Hoglin(final EntityType<? extends Hoglin> aqb, final Level bru) {
        super(aqb, bru);
        this.timeInOverworld = 0;
        this.cannotBeHunted = false;
        this.xpReward = 5;
    }
    
    public boolean canBeLeashed(final Player bft) {
        return !this.isLeashed();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 6.0);
    }
    
    public boolean doHurtTarget(final Entity apx) {
        if (!(apx instanceof LivingEntity)) {
            return false;
        }
        this.attackAnimationRemainingTicks = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0f, this.getVoicePitch());
        HoglinAi.onHitTarget(this, (LivingEntity)apx);
        return HoglinBase.hurtAndThrowTarget(this, (LivingEntity)apx);
    }
    
    protected void blockedByShield(final LivingEntity aqj) {
        if (this.isAdult()) {
            HoglinBase.throwTarget(this, aqj);
        }
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        final boolean boolean4 = super.hurt(aph, float2);
        if (this.level.isClientSide) {
            return false;
        }
        if (boolean4 && aph.getEntity() instanceof LivingEntity) {
            HoglinAi.wasHurtBy(this, (LivingEntity)aph.getEntity());
        }
        return boolean4;
    }
    
    protected Brain.Provider<Hoglin> brainProvider() {
        return Brain.<Hoglin>provider(Hoglin.MEMORY_TYPES, (java.util.Collection<? extends SensorType<? extends Sensor<? super Hoglin>>>)Hoglin.SENSOR_TYPES);
    }
    
    protected Brain<?> makeBrain(final Dynamic<?> dynamic) {
        return HoglinAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }
    
    public Brain<Hoglin> getBrain() {
        return (Brain<Hoglin>)super.getBrain();
    }
    
    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("hoglinBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        HoglinAi.updateActivity(this);
        if (this.isConverting()) {
            ++this.timeInOverworld;
            if (this.timeInOverworld > 300) {
                this.playSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
                this.finishConversion((ServerLevel)this.level);
            }
        }
        else {
            this.timeInOverworld = 0;
        }
    }
    
    @Override
    public void aiStep() {
        if (this.attackAnimationRemainingTicks > 0) {
            --this.attackAnimationRemainingTicks;
        }
        super.aiStep();
    }
    
    @Override
    protected void ageBoundaryReached() {
        if (this.isBaby()) {
            this.xpReward = 3;
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5);
        }
        else {
            this.xpReward = 5;
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0);
        }
    }
    
    public static boolean checkHoglinSpawnRules(final EntityType<Hoglin> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return !brv.getBlockState(fx.below()).is(Blocks.NETHER_WART_BLOCK);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (bsh.getRandom().nextFloat() < 0.2f) {
            this.setBaby(true);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return !this.isPersistenceRequired();
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (HoglinAi.isPosNearNearestRepellent(this, fx)) {
            return -1.0f;
        }
        if (brw.getBlockState(fx.below()).is(Blocks.CRIMSON_NYLIUM)) {
            return 10.0f;
        }
        return 0.0f;
    }
    
    public double getPassengersRidingOffset() {
        return this.getBbHeight() - (this.isBaby() ? 0.2 : 0.15);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final InteractionResult aor4 = super.mobInteract(bft, aoq);
        if (aor4.consumesAction()) {
            this.setPersistenceRequired();
        }
        return aor4;
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 4) {
            this.attackAnimationRemainingTicks = 10;
            this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0f, this.getVoicePitch());
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    @Override
    public int getAttackAnimationRemainingTicks() {
        return this.attackAnimationRemainingTicks;
    }
    
    protected boolean shouldDropExperience() {
        return true;
    }
    
    @Override
    protected int getExperienceReward(final Player bft) {
        return this.xpReward;
    }
    
    private void finishConversion(final ServerLevel aag) {
        final Zoglin bef3 = this.<Zoglin>convertTo(EntityType.ZOGLIN, true);
        if (bef3 != null) {
            bef3.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return bly.getItem() == Items.CRIMSON_FUNGUS;
    }
    
    public boolean isAdult() {
        return !this.isBaby();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Hoglin.DATA_IMMUNE_TO_ZOMBIFICATION, false);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.isImmuneToZombification()) {
            md.putBoolean("IsImmuneToZombification", true);
        }
        md.putInt("TimeInOverworld", this.timeInOverworld);
        if (this.cannotBeHunted) {
            md.putBoolean("CannotBeHunted", true);
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setImmuneToZombification(md.getBoolean("IsImmuneToZombification"));
        this.timeInOverworld = md.getInt("TimeInOverworld");
        this.setCannotBeHunted(md.getBoolean("CannotBeHunted"));
    }
    
    public void setImmuneToZombification(final boolean boolean1) {
        this.getEntityData().<Boolean>set(Hoglin.DATA_IMMUNE_TO_ZOMBIFICATION, boolean1);
    }
    
    private boolean isImmuneToZombification() {
        return this.getEntityData().<Boolean>get(Hoglin.DATA_IMMUNE_TO_ZOMBIFICATION);
    }
    
    public boolean isConverting() {
        return !this.level.dimensionType().piglinSafe() && !this.isImmuneToZombification() && !this.isNoAi();
    }
    
    private void setCannotBeHunted(final boolean boolean1) {
        this.cannotBeHunted = boolean1;
    }
    
    public boolean canBeHunted() {
        return this.isAdult() && !this.cannotBeHunted;
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Hoglin bej4 = EntityType.HOGLIN.create(aag);
        if (bej4 != null) {
            bej4.setPersistenceRequired();
        }
        return bej4;
    }
    
    @Override
    public boolean canFallInLove() {
        return !HoglinAi.isPacified(this) && super.canFallInLove();
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.level.isClientSide) {
            return null;
        }
        return (SoundEvent)HoglinAi.getSoundForCurrentActivity(this).orElse(null);
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.HOGLIN_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOGLIN_DEATH;
    }
    
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }
    
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.HOGLIN_STEP, 0.15f, 1.0f);
    }
    
    protected void playSound(final SoundEvent adn) {
        this.playSound(adn, this.getSoundVolume(), this.getVoicePitch());
    }
    
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }
    
    static {
        DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.<Boolean>defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, SensorType.HOGLIN_SPECIFIC_SENSOR);
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.AVOID_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PACIFIED });
    }
}
