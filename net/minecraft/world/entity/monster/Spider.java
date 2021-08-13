package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import java.util.Random;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Spider extends Monster {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    
    public Spider(final EntityType<? extends Spider> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(4, new SpiderAttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new SpiderTargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(3, new SpiderTargetGoal<>(this, IronGolem.class));
    }
    
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.5f;
    }
    
    @Override
    protected PathNavigation createNavigation(final Level bru) {
        return new WallClimberNavigation(this, bru);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Spider.DATA_FLAGS_ID, (Byte)0);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.SPIDER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }
    
    public boolean onClimbable() {
        return this.isClimbing();
    }
    
    public void makeStuckInBlock(final BlockState cee, final Vec3 dck) {
        if (!cee.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(cee, dck);
        }
    }
    
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }
    
    public boolean canBeAffected(final MobEffectInstance apr) {
        return apr.getEffect() != MobEffects.POISON && super.canBeAffected(apr);
    }
    
    public boolean isClimbing() {
        return (this.entityData.<Byte>get(Spider.DATA_FLAGS_ID) & 0x1) != 0x0;
    }
    
    public void setClimbing(final boolean boolean1) {
        byte byte3 = this.entityData.<Byte>get(Spider.DATA_FLAGS_ID);
        if (boolean1) {
            byte3 |= 0x1;
        }
        else {
            byte3 &= 0xFFFFFFFE;
        }
        this.entityData.<Byte>set(Spider.DATA_FLAGS_ID, byte3);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        aqz = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        if (bsh.getRandom().nextInt(100) == 0) {
            final Skeleton bdv7 = EntityType.SKELETON.create(this.level);
            bdv7.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            bdv7.finalizeSpawn(bsh, aop, aqm, null, null);
            bdv7.startRiding(this);
        }
        if (aqz == null) {
            aqz = new SpiderEffectsGroupData();
            if (bsh.getDifficulty() == Difficulty.HARD && bsh.getRandom().nextFloat() < 0.1f * aop.getSpecialMultiplier()) {
                ((SpiderEffectsGroupData)aqz).setRandomEffect(bsh.getRandom());
            }
        }
        if (aqz instanceof SpiderEffectsGroupData) {
            final MobEffect app7 = ((SpiderEffectsGroupData)aqz).effect;
            if (app7 != null) {
                this.addEffect(new MobEffectInstance(app7, Integer.MAX_VALUE));
            }
        }
        return aqz;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.65f;
    }
    
    static {
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(Spider.class, EntityDataSerializers.BYTE);
    }
    
    public static class SpiderEffectsGroupData implements SpawnGroupData {
        public MobEffect effect;
        
        public void setRandomEffect(final Random random) {
            final int integer3 = random.nextInt(5);
            if (integer3 <= 1) {
                this.effect = MobEffects.MOVEMENT_SPEED;
            }
            else if (integer3 <= 2) {
                this.effect = MobEffects.DAMAGE_BOOST;
            }
            else if (integer3 <= 3) {
                this.effect = MobEffects.REGENERATION;
            }
            else if (integer3 <= 4) {
                this.effect = MobEffects.INVISIBILITY;
            }
        }
    }
    
    static class SpiderAttackGoal extends MeleeAttackGoal {
        public SpiderAttackGoal(final Spider bdy) {
            super(bdy, 1.0, true);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }
        
        @Override
        public boolean canContinueToUse() {
            final float float2 = this.mob.getBrightness();
            if (float2 >= 0.5f && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            }
            return super.canContinueToUse();
        }
        
        @Override
        protected double getAttackReachSqr(final LivingEntity aqj) {
            return 4.0f + aqj.getBbWidth();
        }
    }
    
    static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public SpiderTargetGoal(final Spider bdy, final Class<T> class2) {
            super(bdy, class2, true);
        }
        
        @Override
        public boolean canUse() {
            final float float2 = this.mob.getBrightness();
            return float2 < 0.5f && super.canUse();
        }
    }
}
