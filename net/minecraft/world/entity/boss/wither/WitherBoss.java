package net.minecraft.world.entity.boss.wither;

import java.util.EnumSet;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerBossEvent;
import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.monster.Monster;

public class WitherBoss extends Monster implements PowerableMob, RangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_TARGET_A;
    private static final EntityDataAccessor<Integer> DATA_TARGET_B;
    private static final EntityDataAccessor<Integer> DATA_TARGET_C;
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS;
    private static final EntityDataAccessor<Integer> DATA_ID_INV;
    private final float[] xRotHeads;
    private final float[] yRotHeads;
    private final float[] xRotOHeads;
    private final float[] yRotOHeads;
    private final int[] nextHeadUpdate;
    private final int[] idleHeadUpdates;
    private int destroyBlocksTick;
    private final ServerBossEvent bossEvent;
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;
    private static final TargetingConditions TARGETING_CONDITIONS;
    
    public WitherBoss(final EntityType<? extends WitherBoss> aqb, final Level bru) {
        super(aqb, bru);
        this.xRotHeads = new float[2];
        this.yRotHeads = new float[2];
        this.xRotOHeads = new float[2];
        this.yRotOHeads = new float[2];
        this.nextHeadUpdate = new int[2];
        this.idleHeadUpdates = new int[2];
        this.bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
        this.setHealth(this.getMaxHealth());
        this.getNavigation().setCanFloat(true);
        this.xpReward = 50;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WitherDoNothingGoal());
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 40, 20.0f));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 0, false, false, WitherBoss.LIVING_ENTITY_SELECTOR));
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(WitherBoss.DATA_TARGET_A, 0);
        this.entityData.<Integer>define(WitherBoss.DATA_TARGET_B, 0);
        this.entityData.<Integer>define(WitherBoss.DATA_TARGET_C, 0);
        this.entityData.<Integer>define(WitherBoss.DATA_ID_INV, 0);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Invul", this.getInvulnerableTicks());
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setInvulnerableTicks(md.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }
    
    public void setCustomName(@Nullable final Component nr) {
        super.setCustomName(nr);
        this.bossEvent.setName(this.getDisplayName());
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.WITHER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }
    
    @Override
    public void aiStep() {
        Vec3 dck2 = this.getDeltaMovement().multiply(1.0, 0.6, 1.0);
        if (!this.level.isClientSide && this.getAlternativeTarget(0) > 0) {
            final Entity apx3 = this.level.getEntity(this.getAlternativeTarget(0));
            if (apx3 != null) {
                double double4 = dck2.y;
                if (this.getY() < apx3.getY() || (!this.isPowered() && this.getY() < apx3.getY() + 5.0)) {
                    double4 = Math.max(0.0, double4);
                    double4 += 0.3 - double4 * 0.6000000238418579;
                }
                dck2 = new Vec3(dck2.x, double4, dck2.z);
                final Vec3 dck3 = new Vec3(apx3.getX() - this.getX(), 0.0, apx3.getZ() - this.getZ());
                if (Entity.getHorizontalDistanceSqr(dck3) > 9.0) {
                    final Vec3 dck4 = dck3.normalize();
                    dck2 = dck2.add(dck4.x * 0.3 - dck2.x * 0.6, 0.0, dck4.z * 0.3 - dck2.z * 0.6);
                }
            }
        }
        this.setDeltaMovement(dck2);
        if (Entity.getHorizontalDistanceSqr(dck2) > 0.05) {
            this.yRot = (float)Mth.atan2(dck2.z, dck2.x) * 57.295776f - 90.0f;
        }
        super.aiStep();
        for (int integer3 = 0; integer3 < 2; ++integer3) {
            this.yRotOHeads[integer3] = this.yRotHeads[integer3];
            this.xRotOHeads[integer3] = this.xRotHeads[integer3];
        }
        for (int integer3 = 0; integer3 < 2; ++integer3) {
            final int integer4 = this.getAlternativeTarget(integer3 + 1);
            Entity apx4 = null;
            if (integer4 > 0) {
                apx4 = this.level.getEntity(integer4);
            }
            if (apx4 != null) {
                final double double5 = this.getHeadX(integer3 + 1);
                final double double6 = this.getHeadY(integer3 + 1);
                final double double7 = this.getHeadZ(integer3 + 1);
                final double double8 = apx4.getX() - double5;
                final double double9 = apx4.getEyeY() - double6;
                final double double10 = apx4.getZ() - double7;
                final double double11 = Mth.sqrt(double8 * double8 + double10 * double10);
                final float float20 = (float)(Mth.atan2(double10, double8) * 57.2957763671875) - 90.0f;
                final float float21 = (float)(-(Mth.atan2(double9, double11) * 57.2957763671875));
                this.xRotHeads[integer3] = this.rotlerp(this.xRotHeads[integer3], float21, 40.0f);
                this.yRotHeads[integer3] = this.rotlerp(this.yRotHeads[integer3], float20, 10.0f);
            }
            else {
                this.yRotHeads[integer3] = this.rotlerp(this.yRotHeads[integer3], this.yBodyRot, 10.0f);
            }
        }
        final boolean boolean3 = this.isPowered();
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            final double double12 = this.getHeadX(integer4);
            final double double13 = this.getHeadY(integer4);
            final double double14 = this.getHeadZ(integer4);
            this.level.addParticle(ParticleTypes.SMOKE, double12 + this.random.nextGaussian() * 0.30000001192092896, double13 + this.random.nextGaussian() * 0.30000001192092896, double14 + this.random.nextGaussian() * 0.30000001192092896, 0.0, 0.0, 0.0);
            if (boolean3 && this.level.random.nextInt(4) == 0) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, double12 + this.random.nextGaussian() * 0.30000001192092896, double13 + this.random.nextGaussian() * 0.30000001192092896, double14 + this.random.nextGaussian() * 0.30000001192092896, 0.699999988079071, 0.699999988079071, 0.5);
            }
        }
        if (this.getInvulnerableTicks() > 0) {
            for (int integer4 = 0; integer4 < 3; ++integer4) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + this.random.nextFloat() * 3.3f, this.getZ() + this.random.nextGaussian(), 0.699999988079071, 0.699999988079071, 0.8999999761581421);
            }
        }
    }
    
    protected void customServerAiStep() {
        if (this.getInvulnerableTicks() > 0) {
            final int integer2 = this.getInvulnerableTicks() - 1;
            if (integer2 <= 0) {
                final Explosion.BlockInteraction a3 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
                this.level.explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0f, false, a3);
                if (!this.isSilent()) {
                    this.level.globalLevelEvent(1023, this.blockPosition(), 0);
                }
            }
            this.setInvulnerableTicks(integer2);
            if (this.tickCount % 10 == 0) {
                this.heal(10.0f);
            }
            return;
        }
        super.customServerAiStep();
        for (int integer2 = 1; integer2 < 3; ++integer2) {
            if (this.tickCount >= this.nextHeadUpdate[integer2 - 1]) {
                this.nextHeadUpdate[integer2 - 1] = this.tickCount + 10 + this.random.nextInt(10);
                if ((this.level.getDifficulty() == Difficulty.NORMAL || this.level.getDifficulty() == Difficulty.HARD) && this.idleHeadUpdates[integer2 - 1]++ > 15) {
                    final float float3 = 10.0f;
                    final float float4 = 5.0f;
                    final double double5 = Mth.nextDouble(this.random, this.getX() - 10.0, this.getX() + 10.0);
                    final double double6 = Mth.nextDouble(this.random, this.getY() - 5.0, this.getY() + 5.0);
                    final double double7 = Mth.nextDouble(this.random, this.getZ() - 10.0, this.getZ() + 10.0);
                    this.performRangedAttack(integer2 + 1, double5, double6, double7, true);
                    this.idleHeadUpdates[integer2 - 1] = 0;
                }
                final int integer3 = this.getAlternativeTarget(integer2);
                if (integer3 > 0) {
                    final Entity apx4 = this.level.getEntity(integer3);
                    if (apx4 == null || !apx4.isAlive() || this.distanceToSqr(apx4) > 900.0 || !this.canSee(apx4)) {
                        this.setAlternativeTarget(integer2, 0);
                    }
                    else if (apx4 instanceof Player && ((Player)apx4).abilities.invulnerable) {
                        this.setAlternativeTarget(integer2, 0);
                    }
                    else {
                        this.performRangedAttack(integer2 + 1, (LivingEntity)apx4);
                        this.nextHeadUpdate[integer2 - 1] = this.tickCount + 40 + this.random.nextInt(20);
                        this.idleHeadUpdates[integer2 - 1] = 0;
                    }
                }
                else {
                    final List<LivingEntity> list4 = this.level.<LivingEntity>getNearbyEntities((java.lang.Class<? extends LivingEntity>)LivingEntity.class, WitherBoss.TARGETING_CONDITIONS, (LivingEntity)this, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
                    int integer4 = 0;
                    while (integer4 < 10 && !list4.isEmpty()) {
                        final LivingEntity aqj6 = (LivingEntity)list4.get(this.random.nextInt(list4.size()));
                        if (aqj6 != this && aqj6.isAlive() && this.canSee(aqj6)) {
                            if (!(aqj6 instanceof Player)) {
                                this.setAlternativeTarget(integer2, aqj6.getId());
                                break;
                            }
                            if (!((Player)aqj6).abilities.invulnerable) {
                                this.setAlternativeTarget(integer2, aqj6.getId());
                                break;
                            }
                            break;
                        }
                        else {
                            list4.remove(aqj6);
                            ++integer4;
                        }
                    }
                }
            }
        }
        if (this.getTarget() != null) {
            this.setAlternativeTarget(0, this.getTarget().getId());
        }
        else {
            this.setAlternativeTarget(0, 0);
        }
        if (this.destroyBlocksTick > 0) {
            --this.destroyBlocksTick;
            if (this.destroyBlocksTick == 0 && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                final int integer2 = Mth.floor(this.getY());
                final int integer3 = Mth.floor(this.getX());
                final int integer5 = Mth.floor(this.getZ());
                boolean boolean5 = false;
                for (int integer6 = -1; integer6 <= 1; ++integer6) {
                    for (int integer7 = -1; integer7 <= 1; ++integer7) {
                        for (int integer8 = 0; integer8 <= 3; ++integer8) {
                            final int integer9 = integer3 + integer6;
                            final int integer10 = integer2 + integer8;
                            final int integer11 = integer5 + integer7;
                            final BlockPos fx12 = new BlockPos(integer9, integer10, integer11);
                            final BlockState cee13 = this.level.getBlockState(fx12);
                            if (canDestroy(cee13)) {
                                boolean5 = (this.level.destroyBlock(fx12, true, this) || boolean5);
                            }
                        }
                    }
                }
                if (boolean5) {
                    this.level.levelEvent(null, 1022, this.blockPosition(), 0);
                }
            }
        }
        if (this.tickCount % 20 == 0) {
            this.heal(1.0f);
        }
        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
    }
    
    public static boolean canDestroy(final BlockState cee) {
        return !cee.isAir() && !BlockTags.WITHER_IMMUNE.contains(cee.getBlock());
    }
    
    public void makeInvulnerable() {
        this.setInvulnerableTicks(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }
    
    public void makeStuckInBlock(final BlockState cee, final Vec3 dck) {
    }
    
    public void startSeenByPlayer(final ServerPlayer aah) {
        super.startSeenByPlayer(aah);
        this.bossEvent.addPlayer(aah);
    }
    
    public void stopSeenByPlayer(final ServerPlayer aah) {
        super.stopSeenByPlayer(aah);
        this.bossEvent.removePlayer(aah);
    }
    
    private double getHeadX(final int integer) {
        if (integer <= 0) {
            return this.getX();
        }
        final float float3 = (this.yBodyRot + 180 * (integer - 1)) * 0.017453292f;
        final float float4 = Mth.cos(float3);
        return this.getX() + float4 * 1.3;
    }
    
    private double getHeadY(final int integer) {
        if (integer <= 0) {
            return this.getY() + 3.0;
        }
        return this.getY() + 2.2;
    }
    
    private double getHeadZ(final int integer) {
        if (integer <= 0) {
            return this.getZ();
        }
        final float float3 = (this.yBodyRot + 180 * (integer - 1)) * 0.017453292f;
        final float float4 = Mth.sin(float3);
        return this.getZ() + float4 * 1.3;
    }
    
    private float rotlerp(final float float1, final float float2, final float float3) {
        float float4 = Mth.wrapDegrees(float2 - float1);
        if (float4 > float3) {
            float4 = float3;
        }
        if (float4 < -float3) {
            float4 = -float3;
        }
        return float1 + float4;
    }
    
    private void performRangedAttack(final int integer, final LivingEntity aqj) {
        this.performRangedAttack(integer, aqj.getX(), aqj.getY() + aqj.getEyeHeight() * 0.5, aqj.getZ(), integer == 0 && this.random.nextFloat() < 0.001f);
    }
    
    private void performRangedAttack(final int integer, final double double2, final double double3, final double double4, final boolean boolean5) {
        if (!this.isSilent()) {
            this.level.levelEvent(null, 1024, this.blockPosition(), 0);
        }
        final double double5 = this.getHeadX(integer);
        final double double6 = this.getHeadY(integer);
        final double double7 = this.getHeadZ(integer);
        final double double8 = double2 - double5;
        final double double9 = double3 - double6;
        final double double10 = double4 - double7;
        final WitherSkull bgw22 = new WitherSkull(this.level, this, double8, double9, double10);
        bgw22.setOwner(this);
        if (boolean5) {
            bgw22.setDangerous(true);
        }
        bgw22.setPosRaw(double5, double6, double7);
        this.level.addFreshEntity(bgw22);
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        this.performRangedAttack(0, aqj);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (aph == DamageSource.DROWN || aph.getEntity() instanceof WitherBoss) {
            return false;
        }
        if (this.getInvulnerableTicks() > 0 && aph != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (this.isPowered()) {
            final Entity apx4 = aph.getDirectEntity();
            if (apx4 instanceof AbstractArrow) {
                return false;
            }
        }
        final Entity apx4 = aph.getEntity();
        if (apx4 != null) {
            if (!(apx4 instanceof Player)) {
                if (apx4 instanceof LivingEntity && ((LivingEntity)apx4).getMobType() == this.getMobType()) {
                    return false;
                }
            }
        }
        if (this.destroyBlocksTick <= 0) {
            this.destroyBlocksTick = 20;
        }
        for (int integer5 = 0; integer5 < this.idleHeadUpdates.length; ++integer5) {
            final int[] idleHeadUpdates = this.idleHeadUpdates;
            final int n = integer5;
            idleHeadUpdates[n] += 3;
        }
        return super.hurt(aph, float2);
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        final ItemEntity bcs5 = this.spawnAtLocation(Items.NETHER_STAR);
        if (bcs5 != null) {
            bcs5.setExtendedLifetime();
        }
    }
    
    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.remove();
            return;
        }
        this.noActionTime = 0;
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    public boolean addEffect(final MobEffectInstance apr) {
        return false;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0).add(Attributes.MOVEMENT_SPEED, 0.6000000238418579).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 4.0);
    }
    
    public float getHeadYRot(final int integer) {
        return this.yRotHeads[integer];
    }
    
    public float getHeadXRot(final int integer) {
        return this.xRotHeads[integer];
    }
    
    public int getInvulnerableTicks() {
        return this.entityData.<Integer>get(WitherBoss.DATA_ID_INV);
    }
    
    public void setInvulnerableTicks(final int integer) {
        this.entityData.<Integer>set(WitherBoss.DATA_ID_INV, integer);
    }
    
    public int getAlternativeTarget(final int integer) {
        return this.entityData.<Integer>get((EntityDataAccessor<Integer>)WitherBoss.DATA_TARGETS.get(integer));
    }
    
    public void setAlternativeTarget(final int integer1, final int integer2) {
        this.entityData.<Integer>set((EntityDataAccessor<Integer>)WitherBoss.DATA_TARGETS.get(integer1), integer2);
    }
    
    @Override
    public boolean isPowered() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }
    
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    protected boolean canRide(final Entity apx) {
        return false;
    }
    
    public boolean canChangeDimensions() {
        return false;
    }
    
    public boolean canBeAffected(final MobEffectInstance apr) {
        return apr.getEffect() != MobEffects.WITHER && super.canBeAffected(apr);
    }
    
    static {
        DATA_TARGET_A = SynchedEntityData.<Integer>defineId(WitherBoss.class, EntityDataSerializers.INT);
        DATA_TARGET_B = SynchedEntityData.<Integer>defineId(WitherBoss.class, EntityDataSerializers.INT);
        DATA_TARGET_C = SynchedEntityData.<Integer>defineId(WitherBoss.class, EntityDataSerializers.INT);
        DATA_TARGETS = (List)ImmutableList.of(WitherBoss.DATA_TARGET_A, WitherBoss.DATA_TARGET_B, WitherBoss.DATA_TARGET_C);
        DATA_ID_INV = SynchedEntityData.<Integer>defineId(WitherBoss.class, EntityDataSerializers.INT);
        LIVING_ENTITY_SELECTOR = (aqj -> aqj.getMobType() != MobType.UNDEAD && aqj.attackable());
        TARGETING_CONDITIONS = new TargetingConditions().range(20.0).selector(WitherBoss.LIVING_ENTITY_SELECTOR);
    }
    
    class WitherDoNothingGoal extends Goal {
        public WitherDoNothingGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.JUMP, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return WitherBoss.this.getInvulnerableTicks() > 0;
        }
    }
}
