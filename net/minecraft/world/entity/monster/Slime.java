package net.minecraft.world.entity.monster;

import net.minecraft.world.effect.MobEffects;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.Difficulty;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;

public class Slime extends Mob implements Enemy {
    private static final EntityDataAccessor<Integer> ID_SIZE;
    public float targetSquish;
    public float squish;
    public float oSquish;
    private boolean wasOnGround;
    
    public Slime(final EntityType<? extends Slime> aqb, final Level bru) {
        super(aqb, bru);
        this.moveControl = new SlimeMoveControl(this);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new SlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new SlimeRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)(aqj -> Math.abs(aqj.getY() - this.getY()) <= 4.0)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Slime.ID_SIZE, 1);
    }
    
    protected void setSize(final int integer, final boolean boolean2) {
        this.entityData.<Integer>set(Slime.ID_SIZE, integer);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(integer * integer);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * integer);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(integer);
        if (boolean2) {
            this.setHealth(this.getMaxHealth());
        }
        this.xpReward = integer;
    }
    
    public int getSize() {
        return this.entityData.<Integer>get(Slime.ID_SIZE);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Size", this.getSize() - 1);
        md.putBoolean("wasOnGround", this.wasOnGround);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        int integer3 = md.getInt("Size");
        if (integer3 < 0) {
            integer3 = 0;
        }
        this.setSize(integer3 + 1, false);
        super.readAdditionalSaveData(md);
        this.wasOnGround = md.getBoolean("wasOnGround");
    }
    
    public boolean isTiny() {
        return this.getSize() <= 1;
    }
    
    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SLIME;
    }
    
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getSize() > 0;
    }
    
    @Override
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5f;
        this.oSquish = this.squish;
        super.tick();
        if (this.onGround && !this.wasOnGround) {
            for (int integer2 = this.getSize(), integer3 = 0; integer3 < integer2 * 8; ++integer3) {
                final float float4 = this.random.nextFloat() * 6.2831855f;
                final float float5 = this.random.nextFloat() * 0.5f + 0.5f;
                final float float6 = Mth.sin(float4) * integer2 * 0.5f * float5;
                final float float7 = Mth.cos(float4) * integer2 * 0.5f * float5;
                this.level.addParticle(this.getParticleType(), this.getX() + float6, this.getY(), this.getZ() + float7, 0.0, 0.0, 0.0);
            }
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.targetSquish = -0.5f;
        }
        else if (!this.onGround && this.wasOnGround) {
            this.targetSquish = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.decreaseSquish();
    }
    
    protected void decreaseSquish() {
        this.targetSquish *= 0.6f;
    }
    
    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }
    
    public void refreshDimensions() {
        final double double2 = this.getX();
        final double double3 = this.getY();
        final double double4 = this.getZ();
        super.refreshDimensions();
        this.setPos(double2, double3, double4);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Slime.ID_SIZE.equals(us)) {
            this.refreshDimensions();
            this.yRot = this.yHeadRot;
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.onSyncedDataUpdated(us);
    }
    
    public EntityType<? extends Slime> getType() {
        return super.getType();
    }
    
    public void remove() {
        final int integer2 = this.getSize();
        if (!this.level.isClientSide && integer2 > 1 && this.isDeadOrDying()) {
            final Component nr3 = this.getCustomName();
            final boolean boolean4 = this.isNoAi();
            final float float5 = integer2 / 4.0f;
            final int integer3 = integer2 / 2;
            for (int integer4 = 2 + this.random.nextInt(3), integer5 = 0; integer5 < integer4; ++integer5) {
                final float float6 = (integer5 % 2 - 0.5f) * float5;
                final float float7 = (integer5 / 2 - 0.5f) * float5;
                final Slime bdw11 = (Slime)this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    bdw11.setPersistenceRequired();
                }
                bdw11.setCustomName(nr3);
                bdw11.setNoAi(boolean4);
                bdw11.setInvulnerable(this.isInvulnerable());
                bdw11.setSize(integer3, true);
                bdw11.moveTo(this.getX() + float6, this.getY() + 0.5, this.getZ() + float7, this.random.nextFloat() * 360.0f, 0.0f);
                this.level.addFreshEntity(bdw11);
            }
        }
        super.remove();
    }
    
    @Override
    public void push(final Entity apx) {
        super.push(apx);
        if (apx instanceof IronGolem && this.isDealsDamage()) {
            this.dealDamage((LivingEntity)apx);
        }
    }
    
    public void playerTouch(final Player bft) {
        if (this.isDealsDamage()) {
            this.dealDamage(bft);
        }
    }
    
    protected void dealDamage(final LivingEntity aqj) {
        if (this.isAlive()) {
            final int integer3 = this.getSize();
            if (this.distanceToSqr(aqj) < 0.6 * integer3 * (0.6 * integer3) && this.canSee(aqj) && aqj.hurt(DamageSource.mobAttack(this), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                this.doEnchantDamageEffects(this, aqj);
            }
        }
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.625f * apy.height;
    }
    
    protected boolean isDealsDamage() {
        return !this.isTiny() && this.isEffectiveAi();
    }
    
    protected float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.isTiny()) {
            return SoundEvents.SLIME_HURT_SMALL;
        }
        return SoundEvents.SLIME_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        if (this.isTiny()) {
            return SoundEvents.SLIME_DEATH_SMALL;
        }
        return SoundEvents.SLIME_DEATH;
    }
    
    protected SoundEvent getSquishSound() {
        if (this.isTiny()) {
            return SoundEvents.SLIME_SQUISH_SMALL;
        }
        return SoundEvents.SLIME_SQUISH;
    }
    
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return (this.getSize() == 1) ? this.getType().getDefaultLootTable() : BuiltInLootTables.EMPTY;
    }
    
    public static boolean checkSlimeSpawnRules(final EntityType<Slime> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        if (brv.getDifficulty() != Difficulty.PEACEFUL) {
            if (Objects.equals(brv.getBiomeName(fx), Optional.of((Object)Biomes.SWAMP)) && fx.getY() > 50 && fx.getY() < 70 && random.nextFloat() < 0.5f && random.nextFloat() < brv.getMoonBrightness() && brv.getMaxLocalRawBrightness(fx) <= random.nextInt(8)) {
                return Mob.checkMobSpawnRules(aqb, brv, aqm, fx, random);
            }
            if (!(brv instanceof WorldGenLevel)) {
                return false;
            }
            final ChunkPos bra6 = new ChunkPos(fx);
            final boolean boolean7 = WorldgenRandom.seedSlimeChunk(bra6.x, bra6.z, ((WorldGenLevel)brv).getSeed(), 987234911L).nextInt(10) == 0;
            if (random.nextInt(10) == 0 && boolean7 && fx.getY() < 40) {
                return Mob.checkMobSpawnRules(aqb, brv, aqm, fx, random);
            }
        }
        return false;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f * this.getSize();
    }
    
    @Override
    public int getMaxHeadXRot() {
        return 0;
    }
    
    protected boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }
    
    @Override
    protected void jumpFromGround() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.setDeltaMovement(dck2.x, this.getJumpPower(), dck2.z);
        this.hasImpulse = true;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        int integer7 = this.random.nextInt(3);
        if (integer7 < 2 && this.random.nextFloat() < 0.5f * aop.getSpecialMultiplier()) {
            ++integer7;
        }
        final int integer8 = 1 << integer7;
        this.setSize(integer8, true);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    private float getSoundPitch() {
        final float float2 = this.isTiny() ? 1.4f : 0.8f;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) * float2;
    }
    
    protected SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return super.getDimensions(aqu).scale(0.255f * this.getSize());
    }
    
    static {
        ID_SIZE = SynchedEntityData.<Integer>defineId(Slime.class, EntityDataSerializers.INT);
    }
    
    static class SlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final Slime slime;
        private boolean isAggressive;
        
        public SlimeMoveControl(final Slime bdw) {
            super(bdw);
            this.slime = bdw;
            this.yRot = 180.0f * bdw.yRot / 3.1415927f;
        }
        
        public void setDirection(final float float1, final boolean boolean2) {
            this.yRot = float1;
            this.isAggressive = boolean2;
        }
        
        public void setWantedMovement(final double double1) {
            this.speedModifier = double1;
            this.operation = Operation.MOVE_TO;
        }
        
        @Override
        public void tick() {
            this.mob.yRot = this.rotlerp(this.mob.yRot, this.yRot, 90.0f);
            this.mob.yHeadRot = this.mob.yRot;
            this.mob.yBodyRot = this.mob.yRot;
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0f);
                return;
            }
            this.operation = Operation.WAIT;
            if (this.mob.isOnGround()) {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.slime.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }
                    this.slime.getJumpControl().jump();
                    if (this.slime.doPlayJumpSound()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                    }
                }
                else {
                    this.slime.xxa = 0.0f;
                    this.slime.zza = 0.0f;
                    this.mob.setSpeed(0.0f);
                }
            }
            else {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }
    
    static class SlimeAttackGoal extends Goal {
        private final Slime slime;
        private int growTiredTimer;
        
        public SlimeAttackGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = this.slime.getTarget();
            return aqj2 != null && aqj2.isAlive() && (!(aqj2 instanceof Player) || !((Player)aqj2).abilities.invulnerable) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }
        
        @Override
        public void start() {
            this.growTiredTimer = 300;
            super.start();
        }
        
        @Override
        public boolean canContinueToUse() {
            final LivingEntity aqj2 = this.slime.getTarget();
            return aqj2 != null && aqj2.isAlive() && (!(aqj2 instanceof Player) || !((Player)aqj2).abilities.invulnerable) && --this.growTiredTimer > 0;
        }
        
        @Override
        public void tick() {
            this.slime.lookAt(this.slime.getTarget(), 10.0f, 10.0f);
            ((SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.slime.yRot, this.slime.isDealsDamage());
        }
    }
    
    static class SlimeRandomDirectionGoal extends Goal {
        private final Slime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;
        
        public SlimeRandomDirectionGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return this.slime.getTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }
        
        @Override
        public void tick() {
            final int nextRandomizeTime = this.nextRandomizeTime - 1;
            this.nextRandomizeTime = nextRandomizeTime;
            if (nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
                this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
            }
            ((SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
        }
    }
    
    static class SlimeFloatGoal extends Goal {
        private final Slime slime;
        
        public SlimeFloatGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
            bdw.getNavigation().setCanFloat(true);
        }
        
        @Override
        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }
        
        @Override
        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8f) {
                this.slime.getJumpControl().jump();
            }
            ((SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.2);
        }
    }
    
    static class SlimeKeepOnJumpingGoal extends Goal {
        private final Slime slime;
        
        public SlimeKeepOnJumpingGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return !this.slime.isPassenger();
        }
        
        @Override
        public void tick() {
            ((SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.0);
        }
    }
}
