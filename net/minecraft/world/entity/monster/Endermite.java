package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.MobType;
import java.util.Random;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Endermite extends Monster {
    private int life;
    private boolean playerSpawned;
    
    public Endermite(final EntityType<? extends Endermite> aqb, final Level bru) {
        super(aqb, bru);
        this.xpReward = 3;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.13f;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMITE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ENDERMITE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMITE_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.ENDERMITE_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.life = md.getInt("Lifetime");
        this.playerSpawned = md.getBoolean("PlayerSpawned");
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Lifetime", this.life);
        md.putBoolean("PlayerSpawned", this.playerSpawned);
    }
    
    @Override
    public void tick() {
        this.yBodyRot = this.yRot;
        super.tick();
    }
    
    public void setYBodyRot(final float float1) {
        super.setYBodyRot(this.yRot = float1);
    }
    
    public double getMyRidingOffset() {
        return 0.1;
    }
    
    public boolean isPlayerSpawned() {
        return this.playerSpawned;
    }
    
    public void setPlayerSpawned(final boolean boolean1) {
        this.playerSpawned = boolean1;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level.isClientSide) {
            for (int integer2 = 0; integer2 < 2; ++integer2) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
            }
        }
        else {
            if (!this.isPersistenceRequired()) {
                ++this.life;
            }
            if (this.life >= 2400) {
                this.remove();
            }
        }
    }
    
    public static boolean checkEndermiteSpawnRules(final EntityType<Endermite> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        if (Monster.checkAnyLightMonsterSpawnRules(aqb, brv, aqm, fx, random)) {
            final Player bft6 = brv.getNearestPlayer(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 5.0, true);
            return bft6 == null;
        }
        return false;
    }
    
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }
}
