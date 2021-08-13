package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.Difficulty;
import java.util.Random;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Guardian extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_ID_MOVING;
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    private float clientSideTailAnimation;
    private float clientSideTailAnimationO;
    private float clientSideTailAnimationSpeed;
    private float clientSideSpikesAnimation;
    private float clientSideSpikesAnimationO;
    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected RandomStrollGoal randomStrollGoal;
    
    public Guardian(final EntityType<? extends Guardian> aqb, final Level bru) {
        super(aqb, bru);
        this.xpReward = 10;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.moveControl = new GuardianMoveControl(this);
        this.clientSideTailAnimation = this.random.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }
    
    @Override
    protected void registerGoals() {
        final MoveTowardsRestrictionGoal awh2 = new MoveTowardsRestrictionGoal(this, 1.0);
        this.randomStrollGoal = new RandomStrollGoal(this, 1.0, 80);
        this.goalSelector.addGoal(4, new GuardianAttackGoal(this));
        this.goalSelector.addGoal(5, awh2);
        this.goalSelector.addGoal(7, this.randomStrollGoal);
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0f, 0.01f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.randomStrollGoal.setFlags((EnumSet<Goal.Flag>)EnumSet.of((Enum)Goal.Flag.MOVE, (Enum)Goal.Flag.LOOK));
        awh2.setFlags((EnumSet<Goal.Flag>)EnumSet.of((Enum)Goal.Flag.MOVE, (Enum)Goal.Flag.LOOK));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (Predicate<LivingEntity>)new GuardianAttackSelector(this)));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MAX_HEALTH, 30.0);
    }
    
    @Override
    protected PathNavigation createNavigation(final Level bru) {
        return new WaterBoundPathNavigation(this, bru);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Guardian.DATA_ID_MOVING, false);
        this.entityData.<Integer>define(Guardian.DATA_ID_ATTACK_TARGET, 0);
    }
    
    public boolean canBreatheUnderwater() {
        return true;
    }
    
    public MobType getMobType() {
        return MobType.WATER;
    }
    
    public boolean isMoving() {
        return this.entityData.<Boolean>get(Guardian.DATA_ID_MOVING);
    }
    
    private void setMoving(final boolean boolean1) {
        this.entityData.<Boolean>set(Guardian.DATA_ID_MOVING, boolean1);
    }
    
    public int getAttackDuration() {
        return 80;
    }
    
    private void setActiveAttackTarget(final int integer) {
        this.entityData.<Integer>set(Guardian.DATA_ID_ATTACK_TARGET, integer);
    }
    
    public boolean hasActiveAttackTarget() {
        return this.entityData.<Integer>get(Guardian.DATA_ID_ATTACK_TARGET) != 0;
    }
    
    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        }
        if (!this.level.isClientSide) {
            return this.getTarget();
        }
        if (this.clientSideCachedAttackTarget != null) {
            return this.clientSideCachedAttackTarget;
        }
        final Entity apx2 = this.level.getEntity(this.entityData.<Integer>get(Guardian.DATA_ID_ATTACK_TARGET));
        if (apx2 instanceof LivingEntity) {
            return this.clientSideCachedAttackTarget = (LivingEntity)apx2;
        }
        return null;
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (Guardian.DATA_ID_ATTACK_TARGET.equals(us)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND;
    }
    
    protected boolean isMovementNoisy() {
        return false;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.5f;
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (brw.getFluidState(fx).is(FluidTags.WATER)) {
            return 10.0f + brw.getBrightness(fx) - 0.5f;
        }
        return super.getWalkTargetValue(fx, brw);
    }
    
    @Override
    public void aiStep() {
        if (this.isAlive()) {
            if (this.level.isClientSide) {
                this.clientSideTailAnimationO = this.clientSideTailAnimation;
                if (!this.isInWater()) {
                    this.clientSideTailAnimationSpeed = 2.0f;
                    final Vec3 dck2 = this.getDeltaMovement();
                    if (dck2.y > 0.0 && this.clientSideTouchedGround && !this.isSilent()) {
                        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(), this.getSoundSource(), 1.0f, 1.0f, false);
                    }
                    this.clientSideTouchedGround = (dck2.y < 0.0 && this.level.loadedAndEntityCanStandOn(this.blockPosition().below(), this));
                }
                else if (this.isMoving()) {
                    if (this.clientSideTailAnimationSpeed < 0.5f) {
                        this.clientSideTailAnimationSpeed = 4.0f;
                    }
                    else {
                        this.clientSideTailAnimationSpeed += (0.5f - this.clientSideTailAnimationSpeed) * 0.1f;
                    }
                }
                else {
                    this.clientSideTailAnimationSpeed += (0.125f - this.clientSideTailAnimationSpeed) * 0.2f;
                }
                this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
                this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
                if (!this.isInWaterOrBubble()) {
                    this.clientSideSpikesAnimation = this.random.nextFloat();
                }
                else if (this.isMoving()) {
                    this.clientSideSpikesAnimation += (0.0f - this.clientSideSpikesAnimation) * 0.25f;
                }
                else {
                    this.clientSideSpikesAnimation += (1.0f - this.clientSideSpikesAnimation) * 0.06f;
                }
                if (this.isMoving() && this.isInWater()) {
                    final Vec3 dck2 = this.getViewVector(0.0f);
                    for (int integer3 = 0; integer3 < 2; ++integer3) {
                        this.level.addParticle(ParticleTypes.BUBBLE, this.getRandomX(0.5) - dck2.x * 1.5, this.getRandomY() - dck2.y * 1.5, this.getRandomZ(0.5) - dck2.z * 1.5, 0.0, 0.0, 0.0);
                    }
                }
                if (this.hasActiveAttackTarget()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }
                    final LivingEntity aqj2 = this.getActiveAttackTarget();
                    if (aqj2 != null) {
                        this.getLookControl().setLookAt(aqj2, 90.0f, 90.0f);
                        this.getLookControl().tick();
                        final double double3 = this.getAttackAnimationScale(0.0f);
                        double double4 = aqj2.getX() - this.getX();
                        double double5 = aqj2.getY(0.5) - this.getEyeY();
                        double double6 = aqj2.getZ() - this.getZ();
                        final double double7 = Math.sqrt(double4 * double4 + double5 * double5 + double6 * double6);
                        double4 /= double7;
                        double5 /= double7;
                        double6 /= double7;
                        double double8 = this.random.nextDouble();
                        while (double8 < double7) {
                            double8 += 1.8 - double3 + this.random.nextDouble() * (1.7 - double3);
                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + double4 * double8, this.getEyeY() + double5 * double8, this.getZ() + double6 * double8, 0.0, 0.0, 0.0);
                        }
                    }
                }
            }
            if (this.isInWaterOrBubble()) {
                this.setAirSupply(300);
            }
            else if (this.onGround) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.4f, 0.5, (this.random.nextFloat() * 2.0f - 1.0f) * 0.4f));
                this.yRot = this.random.nextFloat() * 360.0f;
                this.onGround = false;
                this.hasImpulse = true;
            }
            if (this.hasActiveAttackTarget()) {
                this.yRot = this.yHeadRot;
            }
        }
        super.aiStep();
    }
    
    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }
    
    public float getTailAnimation(final float float1) {
        return Mth.lerp(float1, this.clientSideTailAnimationO, this.clientSideTailAnimation);
    }
    
    public float getSpikesAnimation(final float float1) {
        return Mth.lerp(float1, this.clientSideSpikesAnimationO, this.clientSideSpikesAnimation);
    }
    
    public float getAttackAnimationScale(final float float1) {
        return (this.clientSideAttackTime + float1) / this.getAttackDuration();
    }
    
    @Override
    public boolean checkSpawnObstruction(final LevelReader brw) {
        return brw.isUnobstructed(this);
    }
    
    public static boolean checkGuardianSpawnRules(final EntityType<? extends Guardian> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return (random.nextInt(20) == 0 || !brv.canSeeSkyFromBelowWater(fx)) && brv.getDifficulty() != Difficulty.PEACEFUL && (aqm == MobSpawnType.SPAWNER || brv.getFluidState(fx).is(FluidTags.WATER));
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (!this.isMoving() && !aph.isMagic() && aph.getDirectEntity() instanceof LivingEntity) {
            final LivingEntity aqj4 = (LivingEntity)aph.getDirectEntity();
            if (!aph.isExplosion()) {
                aqj4.hurt(DamageSource.thorns(this), 2.0f);
            }
        }
        if (this.randomStrollGoal != null) {
            this.randomStrollGoal.trigger();
        }
        return super.hurt(aph, float2);
    }
    
    @Override
    public int getMaxHeadXRot() {
        return 180;
    }
    
    public void travel(final Vec3 dck) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.1f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (!this.isMoving() && this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        }
        else {
            super.travel(dck);
        }
    }
    
    static {
        DATA_ID_MOVING = SynchedEntityData.<Boolean>defineId(Guardian.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_ATTACK_TARGET = SynchedEntityData.<Integer>defineId(Guardian.class, EntityDataSerializers.INT);
    }
    
    static class GuardianAttackSelector implements Predicate<LivingEntity> {
        private final Guardian guardian;
        
        public GuardianAttackSelector(final Guardian bdj) {
            this.guardian = bdj;
        }
        
        public boolean test(@Nullable final LivingEntity aqj) {
            return (aqj instanceof Player || aqj instanceof Squid) && aqj.distanceToSqr(this.guardian) > 9.0;
        }
    }
    
    static class GuardianAttackGoal extends Goal {
        private final Guardian guardian;
        private int attackTime;
        private final boolean elder;
        
        public GuardianAttackGoal(final Guardian bdj) {
            this.guardian = bdj;
            this.elder = (bdj instanceof ElderGuardian);
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = this.guardian.getTarget();
            return aqj2 != null && aqj2.isAlive();
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.elder || this.guardian.distanceToSqr(this.guardian.getTarget()) > 9.0);
        }
        
        @Override
        public void start() {
            this.attackTime = -10;
            this.guardian.getNavigation().stop();
            this.guardian.getLookControl().setLookAt(this.guardian.getTarget(), 90.0f, 90.0f);
            this.guardian.hasImpulse = true;
        }
        
        @Override
        public void stop() {
            this.guardian.setActiveAttackTarget(0);
            this.guardian.setTarget(null);
            this.guardian.randomStrollGoal.trigger();
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = this.guardian.getTarget();
            this.guardian.getNavigation().stop();
            this.guardian.getLookControl().setLookAt(aqj2, 90.0f, 90.0f);
            if (!this.guardian.canSee(aqj2)) {
                this.guardian.setTarget(null);
                return;
            }
            ++this.attackTime;
            if (this.attackTime == 0) {
                this.guardian.setActiveAttackTarget(this.guardian.getTarget().getId());
                if (!this.guardian.isSilent()) {
                    this.guardian.level.broadcastEntityEvent(this.guardian, (byte)21);
                }
            }
            else if (this.attackTime >= this.guardian.getAttackDuration()) {
                float float3 = 1.0f;
                if (this.guardian.level.getDifficulty() == Difficulty.HARD) {
                    float3 += 2.0f;
                }
                if (this.elder) {
                    float3 += 2.0f;
                }
                aqj2.hurt(DamageSource.indirectMagic(this.guardian, this.guardian), float3);
                aqj2.hurt(DamageSource.mobAttack(this.guardian), (float)this.guardian.getAttributeValue(Attributes.ATTACK_DAMAGE));
                this.guardian.setTarget(null);
            }
            super.tick();
        }
    }
    
    static class GuardianMoveControl extends MoveControl {
        private final Guardian guardian;
        
        public GuardianMoveControl(final Guardian bdj) {
            super(bdj);
            this.guardian = bdj;
        }
        
        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO || this.guardian.getNavigation().isDone()) {
                this.guardian.setSpeed(0.0f);
                this.guardian.setMoving(false);
                return;
            }
            final Vec3 dck2 = new Vec3(this.wantedX - this.guardian.getX(), this.wantedY - this.guardian.getY(), this.wantedZ - this.guardian.getZ());
            final double double3 = dck2.length();
            final double double4 = dck2.x / double3;
            final double double5 = dck2.y / double3;
            final double double6 = dck2.z / double3;
            final float float11 = (float)(Mth.atan2(dck2.z, dck2.x) * 57.2957763671875) - 90.0f;
            this.guardian.yRot = this.rotlerp(this.guardian.yRot, float11, 90.0f);
            this.guardian.yBodyRot = this.guardian.yRot;
            final float float12 = (float)(this.speedModifier * this.guardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
            final float float13 = Mth.lerp(0.125f, this.guardian.getSpeed(), float12);
            this.guardian.setSpeed(float13);
            final double double7 = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.5) * 0.05;
            final double double8 = Math.cos((double)(this.guardian.yRot * 0.017453292f));
            final double double9 = Math.sin((double)(this.guardian.yRot * 0.017453292f));
            final double double10 = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.75) * 0.05;
            this.guardian.setDeltaMovement(this.guardian.getDeltaMovement().add(double7 * double8, double10 * (double9 + double8) * 0.25 + float13 * double5 * 0.1, double7 * double9));
            final LookControl auw22 = this.guardian.getLookControl();
            final double double11 = this.guardian.getX() + double4 * 2.0;
            final double double12 = this.guardian.getEyeY() + double5 / double3;
            final double double13 = this.guardian.getZ() + double6 * 2.0;
            double double14 = auw22.getWantedX();
            double double15 = auw22.getWantedY();
            double double16 = auw22.getWantedZ();
            if (!auw22.isHasWanted()) {
                double14 = double11;
                double15 = double12;
                double16 = double13;
            }
            this.guardian.getLookControl().setLookAt(Mth.lerp(0.125, double14, double11), Mth.lerp(0.125, double15, double12), Mth.lerp(0.125, double16, double13), 10.0f, 40.0f);
            this.guardian.setMoving(true);
        }
    }
}
