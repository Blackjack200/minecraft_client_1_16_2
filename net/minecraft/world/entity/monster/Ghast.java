package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.FlyingMob;

public class Ghast extends FlyingMob implements Enemy {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING;
    private int explosionPower;
    
    public Ghast(final EntityType<? extends Ghast> aqb, final Level bru) {
        super(aqb, bru);
        this.explosionPower = 1;
        this.xpReward = 5;
        this.moveControl = new GhastMoveControl(this);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)(aqj -> Math.abs(aqj.getY() - this.getY()) <= 4.0)));
    }
    
    public boolean isCharging() {
        return this.entityData.<Boolean>get(Ghast.DATA_IS_CHARGING);
    }
    
    public void setCharging(final boolean boolean1) {
        this.entityData.<Boolean>set(Ghast.DATA_IS_CHARGING, boolean1);
    }
    
    public int getExplosionPower() {
        return this.explosionPower;
    }
    
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
    
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (aph.getDirectEntity() instanceof LargeFireball && aph.getEntity() instanceof Player) {
            super.hurt(aph, 1000.0f);
            return true;
        }
        return super.hurt(aph, float2);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Ghast.DATA_IS_CHARGING, false);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 100.0);
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.GHAST_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }
    
    protected float getSoundVolume() {
        return 5.0f;
    }
    
    public static boolean checkGhastSpawnRules(final EntityType<Ghast> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(20) == 0 && Mob.checkMobSpawnRules(aqb, brv, aqm, fx, random);
    }
    
    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("ExplosionPower", this.explosionPower);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("ExplosionPower", 99)) {
            this.explosionPower = md.getInt("ExplosionPower");
        }
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 2.6f;
    }
    
    static {
        DATA_IS_CHARGING = SynchedEntityData.<Boolean>defineId(Ghast.class, EntityDataSerializers.BOOLEAN);
    }
    
    static class GhastMoveControl extends MoveControl {
        private final Ghast ghast;
        private int floatDuration;
        
        public GhastMoveControl(final Ghast bdh) {
            super(bdh);
            this.ghast = bdh;
        }
        
        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            if (this.floatDuration-- <= 0) {
                this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                Vec3 dck2 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                final double double3 = dck2.length();
                dck2 = dck2.normalize();
                if (this.canReach(dck2, Mth.ceil(double3))) {
                    this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(dck2.scale(0.1)));
                }
                else {
                    this.operation = Operation.WAIT;
                }
            }
        }
        
        private boolean canReach(final Vec3 dck, final int integer) {
            AABB dcf4 = this.ghast.getBoundingBox();
            for (int integer2 = 1; integer2 < integer; ++integer2) {
                dcf4 = dcf4.move(dck);
                if (!this.ghast.level.noCollision(this.ghast, dcf4)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    static class RandomFloatAroundGoal extends Goal {
        private final Ghast ghast;
        
        public RandomFloatAroundGoal(final Ghast bdh) {
            this.ghast = bdh;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            final MoveControl auy2 = this.ghast.getMoveControl();
            if (!auy2.hasWanted()) {
                return true;
            }
            final double double3 = auy2.getWantedX() - this.ghast.getX();
            final double double4 = auy2.getWantedY() - this.ghast.getY();
            final double double5 = auy2.getWantedZ() - this.ghast.getZ();
            final double double6 = double3 * double3 + double4 * double4 + double5 * double5;
            return double6 < 1.0 || double6 > 3600.0;
        }
        
        @Override
        public boolean canContinueToUse() {
            return false;
        }
        
        @Override
        public void start() {
            final Random random2 = this.ghast.getRandom();
            final double double3 = this.ghast.getX() + (random2.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double double4 = this.ghast.getY() + (random2.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double double5 = this.ghast.getZ() + (random2.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.ghast.getMoveControl().setWantedPosition(double3, double4, double5, 1.0);
        }
    }
    
    static class GhastLookGoal extends Goal {
        private final Ghast ghast;
        
        public GhastLookGoal(final Ghast bdh) {
            this.ghast = bdh;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return true;
        }
        
        @Override
        public void tick() {
            if (this.ghast.getTarget() == null) {
                final Vec3 dck2 = this.ghast.getDeltaMovement();
                this.ghast.yRot = -(float)Mth.atan2(dck2.x, dck2.z) * 57.295776f;
                this.ghast.yBodyRot = this.ghast.yRot;
            }
            else {
                final LivingEntity aqj2 = this.ghast.getTarget();
                final double double3 = 64.0;
                if (aqj2.distanceToSqr(this.ghast) < 4096.0) {
                    final double double4 = aqj2.getX() - this.ghast.getX();
                    final double double5 = aqj2.getZ() - this.ghast.getZ();
                    this.ghast.yRot = -(float)Mth.atan2(double4, double5) * 57.295776f;
                    this.ghast.yBodyRot = this.ghast.yRot;
                }
            }
        }
    }
    
    static class GhastShootFireballGoal extends Goal {
        private final Ghast ghast;
        public int chargeTime;
        
        public GhastShootFireballGoal(final Ghast bdh) {
            this.ghast = bdh;
        }
        
        @Override
        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }
        
        @Override
        public void start() {
            this.chargeTime = 0;
        }
        
        @Override
        public void stop() {
            this.ghast.setCharging(false);
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = this.ghast.getTarget();
            final double double3 = 64.0;
            if (aqj2.distanceToSqr(this.ghast) < 4096.0 && this.ghast.canSee(aqj2)) {
                final Level bru5 = this.ghast.level;
                ++this.chargeTime;
                if (this.chargeTime == 10 && !this.ghast.isSilent()) {
                    bru5.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
                }
                if (this.chargeTime == 20) {
                    final double double4 = 4.0;
                    final Vec3 dck8 = this.ghast.getViewVector(1.0f);
                    final double double5 = aqj2.getX() - (this.ghast.getX() + dck8.x * 4.0);
                    final double double6 = aqj2.getY(0.5) - (0.5 + this.ghast.getY(0.5));
                    final double double7 = aqj2.getZ() - (this.ghast.getZ() + dck8.z * 4.0);
                    if (!this.ghast.isSilent()) {
                        bru5.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
                    }
                    final LargeFireball bgh15 = new LargeFireball(bru5, this.ghast, double5, double6, double7);
                    bgh15.explosionPower = this.ghast.getExplosionPower();
                    bgh15.setPos(this.ghast.getX() + dck8.x * 4.0, this.ghast.getY(0.5) + 0.5, bgh15.getZ() + dck8.z * 4.0);
                    bru5.addFreshEntity(bgh15);
                    this.chargeTime = -40;
                }
            }
            else if (this.chargeTime > 0) {
                --this.chargeTime;
            }
            this.ghast.setCharging(this.chargeTime > 10);
        }
    }
}
