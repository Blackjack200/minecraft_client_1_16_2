package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.util.TimeUtil;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.level.biome.Biomes;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.util.IntRange;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.NeutralMob;

public class PolarBear extends Animal implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private static final IntRange PERSISTENT_ANGER_TIME;
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    
    public PolarBear(final EntityType<? extends PolarBear> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.POLAR_BEAR.create(aag);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return false;
    }
    
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PolarBearMeleeAttackGoal());
        this.goalSelector.addGoal(1, new PolarBearPanicGoal());
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new PolarBearHurtByTargetGoal());
        this.targetSelector.addGoal(2, new PolarBearAttackPlayersGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, null));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 6.0);
    }
    
    public static boolean checkPolarBearSpawnRules(final EntityType<PolarBear> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final Optional<ResourceKey<Biome>> optional6 = brv.getBiomeName(fx);
        if (Objects.equals(optional6, Optional.of((Object)Biomes.FROZEN_OCEAN)) || Objects.equals(optional6, Optional.of((Object)Biomes.DEEP_FROZEN_OCEAN))) {
            return brv.getRawBrightness(fx, 0) > 8 && brv.getBlockState(fx.below()).is(Blocks.ICE);
        }
        return Animal.checkAnimalSpawnRules(aqb, brv, aqm, fx, random);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.readPersistentAngerSaveData((ServerLevel)this.level, md);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        this.addPersistentAngerSaveData(md);
    }
    
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PolarBear.PERSISTENT_ANGER_TIME.randomValue(this.random));
    }
    
    @Override
    public void setRemainingPersistentAngerTime(final int integer) {
        this.remainingPersistentAngerTime = integer;
    }
    
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }
    
    @Override
    public void setPersistentAngerTarget(@Nullable final UUID uUID) {
        this.persistentAngerTarget = uUID;
    }
    
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.isBaby()) {
            return SoundEvents.POLAR_BEAR_AMBIENT_BABY;
        }
        return SoundEvents.POLAR_BEAR_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.POLAR_BEAR_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15f, 1.0f);
    }
    
    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEvents.POLAR_BEAR_WARNING, 1.0f, this.getVoicePitch());
            this.warningSoundTicks = 40;
        }
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(PolarBear.DATA_STANDING_ID, false);
    }
    
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.refreshDimensions();
            }
            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0f, 0.0f, 6.0f);
            }
            else {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0f, 0.0f, 6.0f);
            }
        }
        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }
    
    public EntityDimensions getDimensions(final Pose aqu) {
        if (this.clientSideStandAnimation > 0.0f) {
            final float float3 = this.clientSideStandAnimation / 6.0f;
            final float float4 = 1.0f + float3;
            return super.getDimensions(aqu).scale(1.0f, float4);
        }
        return super.getDimensions(aqu);
    }
    
    public boolean doHurtTarget(final Entity apx) {
        final boolean boolean3 = apx.hurt(DamageSource.mobAttack(this), (float)(int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (boolean3) {
            this.doEnchantDamageEffects(this, apx);
        }
        return boolean3;
    }
    
    public boolean isStanding() {
        return this.entityData.<Boolean>get(PolarBear.DATA_STANDING_ID);
    }
    
    public void setStanding(final boolean boolean1) {
        this.entityData.<Boolean>set(PolarBear.DATA_STANDING_ID, boolean1);
    }
    
    public float getStandingAnimationScale(final float float1) {
        return Mth.lerp(float1, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0f;
    }
    
    protected float getWaterSlowDown() {
        return 0.98f;
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqz == null) {
            aqz = new AgableMobGroupData(1.0f);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    static {
        DATA_STANDING_ID = SynchedEntityData.<Boolean>defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
    
    class PolarBearHurtByTargetGoal extends HurtByTargetGoal {
        public PolarBearHurtByTargetGoal() {
            super(PolarBear.this, new Class[0]);
        }
        
        @Override
        public void start() {
            super.start();
            if (PolarBear.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }
        }
        
        @Override
        protected void alertOther(final Mob aqk, final LivingEntity aqj) {
            if (aqk instanceof PolarBear && !aqk.isBaby()) {
                super.alertOther(aqk, aqj);
            }
        }
    }
    
    class PolarBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public PolarBearAttackPlayersGoal() {
            super(PolarBear.this, Player.class, 20, true, true, null);
        }
        
        @Override
        public boolean canUse() {
            if (PolarBear.this.isBaby()) {
                return false;
            }
            if (super.canUse()) {
                final List<PolarBear> list2 = PolarBear.this.level.<PolarBear>getEntitiesOfClass((java.lang.Class<? extends PolarBear>)PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0, 4.0, 8.0));
                for (final PolarBear bal4 : list2) {
                    if (bal4.isBaby()) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5;
        }
    }
    
    class PolarBearMeleeAttackGoal extends MeleeAttackGoal {
        public PolarBearMeleeAttackGoal() {
            super(PolarBear.this, 1.25, true);
        }
        
        @Override
        protected void checkAndPerformAttack(final LivingEntity aqj, final double double2) {
            final double double3 = this.getAttackReachSqr(aqj);
            if (double2 <= double3 && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(aqj);
                PolarBear.this.setStanding(false);
            }
            else if (double2 <= double3 * 2.0) {
                if (this.isTimeToAttack()) {
                    PolarBear.this.setStanding(false);
                    this.resetAttackCooldown();
                }
                if (this.getTicksUntilNextAttack() <= 10) {
                    PolarBear.this.setStanding(true);
                    PolarBear.this.playWarningSound();
                }
            }
            else {
                this.resetAttackCooldown();
                PolarBear.this.setStanding(false);
            }
        }
        
        @Override
        public void stop() {
            PolarBear.this.setStanding(false);
            super.stop();
        }
        
        @Override
        protected double getAttackReachSqr(final LivingEntity aqj) {
            return 4.0f + aqj.getBbWidth();
        }
    }
    
    class PolarBearPanicGoal extends PanicGoal {
        public PolarBearPanicGoal() {
            super(PolarBear.this, 2.0);
        }
        
        @Override
        public boolean canUse() {
            return (PolarBear.this.isBaby() || PolarBear.this.isOnFire()) && super.canUse();
        }
    }
}
