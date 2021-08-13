package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.util.Mth;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Blaze extends Monster {
    private float allowedHeightOffset;
    private int nextHeightOffsetChangeTick;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    
    public Blaze(final EntityType<? extends Blaze> aqb, final Level bru) {
        super(aqb, bru);
        this.allowedHeightOffset = 0.5f;
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0f);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0f);
        this.xpReward = 10;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new BlazeAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.FOLLOW_RANGE, 48.0);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Blaze.DATA_FLAGS_ID, (Byte)0);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.BLAZE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }
    
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    public void aiStep() {
        if (!this.onGround && this.getDeltaMovement().y < 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }
        if (this.level.isClientSide) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.level.playLocalSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, SoundEvents.BLAZE_BURN, this.getSoundSource(), 1.0f + this.random.nextFloat(), this.random.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int integer2 = 0; integer2 < 2; ++integer2) {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
        }
        super.aiStep();
    }
    
    public boolean isSensitiveToWater() {
        return true;
    }
    
    @Override
    protected void customServerAiStep() {
        --this.nextHeightOffsetChangeTick;
        if (this.nextHeightOffsetChangeTick <= 0) {
            this.nextHeightOffsetChangeTick = 100;
            this.allowedHeightOffset = 0.5f + (float)this.random.nextGaussian() * 3.0f;
        }
        final LivingEntity aqj2 = this.getTarget();
        if (aqj2 != null && aqj2.getEyeY() > this.getEyeY() + this.allowedHeightOffset && this.canAttack(aqj2)) {
            final Vec3 dck3 = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, (0.30000001192092896 - dck3.y) * 0.30000001192092896, 0.0));
            this.hasImpulse = true;
        }
        super.customServerAiStep();
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    public boolean isOnFire() {
        return this.isCharged();
    }
    
    private boolean isCharged() {
        return (this.entityData.<Byte>get(Blaze.DATA_FLAGS_ID) & 0x1) != 0x0;
    }
    
    private void setCharged(final boolean boolean1) {
        byte byte3 = this.entityData.<Byte>get(Blaze.DATA_FLAGS_ID);
        if (boolean1) {
            byte3 |= 0x1;
        }
        else {
            byte3 &= 0xFFFFFFFE;
        }
        this.entityData.<Byte>set(Blaze.DATA_FLAGS_ID, byte3);
    }
    
    static {
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(Blaze.class, EntityDataSerializers.BYTE);
    }
    
    static class BlazeAttackGoal extends Goal {
        private final Blaze blaze;
        private int attackStep;
        private int attackTime;
        private int lastSeen;
        
        public BlazeAttackGoal(final Blaze bcx) {
            this.blaze = bcx;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = this.blaze.getTarget();
            return aqj2 != null && aqj2.isAlive() && this.blaze.canAttack(aqj2);
        }
        
        @Override
        public void start() {
            this.attackStep = 0;
        }
        
        @Override
        public void stop() {
            this.blaze.setCharged(false);
            this.lastSeen = 0;
        }
        
        @Override
        public void tick() {
            --this.attackTime;
            final LivingEntity aqj2 = this.blaze.getTarget();
            if (aqj2 == null) {
                return;
            }
            final boolean boolean3 = this.blaze.getSensing().canSee(aqj2);
            if (boolean3) {
                this.lastSeen = 0;
            }
            else {
                ++this.lastSeen;
            }
            final double double4 = this.blaze.distanceToSqr(aqj2);
            if (double4 < 4.0) {
                if (!boolean3) {
                    return;
                }
                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.blaze.doHurtTarget(aqj2);
                }
                this.blaze.getMoveControl().setWantedPosition(aqj2.getX(), aqj2.getY(), aqj2.getZ(), 1.0);
            }
            else if (double4 < this.getFollowDistance() * this.getFollowDistance() && boolean3) {
                final double double5 = aqj2.getX() - this.blaze.getX();
                final double double6 = aqj2.getY(0.5) - this.blaze.getY(0.5);
                final double double7 = aqj2.getZ() - this.blaze.getZ();
                if (this.attackTime <= 0) {
                    ++this.attackStep;
                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                        this.blaze.setCharged(true);
                    }
                    else if (this.attackStep <= 4) {
                        this.attackTime = 6;
                    }
                    else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                        this.blaze.setCharged(false);
                    }
                    if (this.attackStep > 1) {
                        final float float12 = Mth.sqrt(Mth.sqrt(double4)) * 0.5f;
                        if (!this.blaze.isSilent()) {
                            this.blaze.level.levelEvent(null, 1018, this.blaze.blockPosition(), 0);
                        }
                        for (int integer13 = 0; integer13 < 1; ++integer13) {
                            final SmallFireball bgm14 = new SmallFireball(this.blaze.level, this.blaze, double5 + this.blaze.getRandom().nextGaussian() * float12, double6, double7 + this.blaze.getRandom().nextGaussian() * float12);
                            bgm14.setPos(bgm14.getX(), this.blaze.getY(0.5) + 0.5, bgm14.getZ());
                            this.blaze.level.addFreshEntity(bgm14);
                        }
                    }
                }
                this.blaze.getLookControl().setLookAt(aqj2, 10.0f, 10.0f);
            }
            else if (this.lastSeen < 5) {
                this.blaze.getMoveControl().setWantedPosition(aqj2.getX(), aqj2.getY(), aqj2.getZ(), 1.0);
            }
            super.tick();
        }
        
        private double getFollowDistance() {
            return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
