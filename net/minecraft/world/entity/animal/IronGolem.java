package net.minecraft.world.entity.animal;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.TimeUtil;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.util.IntRange;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.NeutralMob;

public class IronGolem extends AbstractGolem implements NeutralMob {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private int attackAnimationTick;
    private int offerFlowerTick;
    private static final IntRange PERSISTENT_ANGER_TIME;
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    
    public IronGolem(final EntityType<? extends IronGolem> aqb, final Level bru) {
        super(aqb, bru);
        this.maxUpStep = 1.0f;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0f));
        this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6, false));
        this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6));
        this.goalSelector.addGoal(5, new OfferFlowerGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)this::isAngryAt));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (Predicate<LivingEntity>)(aqj -> aqj instanceof Enemy && !(aqj instanceof Creeper))));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(IronGolem.DATA_FLAGS_ID, (Byte)0);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ATTACK_DAMAGE, 15.0);
    }
    
    protected int decreaseAirSupply(final int integer) {
        return integer;
    }
    
    protected void doPush(final Entity apx) {
        if (apx instanceof Enemy && !(apx instanceof Creeper) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)apx);
        }
        super.doPush(apx);
    }
    
    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }
        if (this.offerFlowerTick > 0) {
            --this.offerFlowerTick;
        }
        if (Entity.getHorizontalDistanceSqr(this.getDeltaMovement()) > 2.500000277905201E-7 && this.random.nextInt(5) == 0) {
            final int integer2 = Mth.floor(this.getX());
            final int integer3 = Mth.floor(this.getY() - 0.20000000298023224);
            final int integer4 = Mth.floor(this.getZ());
            final BlockState cee5 = this.level.getBlockState(new BlockPos(integer2, integer3, integer4));
            if (!cee5.isAir()) {
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, cee5), this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), this.getY() + 0.1, this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), 4.0 * (this.random.nextFloat() - 0.5), 0.5, (this.random.nextFloat() - 0.5) * 4.0);
            }
        }
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }
    
    public boolean canAttackType(final EntityType<?> aqb) {
        return (!this.isPlayerCreated() || aqb != EntityType.PLAYER) && aqb != EntityType.CREEPER && super.canAttackType(aqb);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("PlayerCreated", this.isPlayerCreated());
        this.addPersistentAngerSaveData(md);
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setPlayerCreated(md.getBoolean("PlayerCreated"));
        this.readPersistentAngerSaveData((ServerLevel)this.level, md);
    }
    
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(IronGolem.PERSISTENT_ANGER_TIME.randomValue(this.random));
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
    
    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    
    public boolean doHurtTarget(final Entity apx) {
        this.attackAnimationTick = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        final float float3 = this.getAttackDamage();
        final float float4 = ((int)float3 > 0) ? (float3 / 2.0f + this.random.nextInt((int)float3)) : float3;
        final boolean boolean5 = apx.hurt(DamageSource.mobAttack(this), float4);
        if (boolean5) {
            apx.setDeltaMovement(apx.getDeltaMovement().add(0.0, 0.4000000059604645, 0.0));
            this.doEnchantDamageEffects(this, apx);
        }
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0f, 1.0f);
        return boolean5;
    }
    
    public boolean hurt(final DamageSource aph, final float float2) {
        final Crackiness a4 = this.getCrackiness();
        final boolean boolean5 = super.hurt(aph, float2);
        if (boolean5 && this.getCrackiness() != a4) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0f, 1.0f);
        }
        return boolean5;
    }
    
    public Crackiness getCrackiness() {
        return Crackiness.byFraction(this.getHealth() / this.getMaxHealth());
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 4) {
            this.attackAnimationTick = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0f, 1.0f);
        }
        else if (byte1 == 11) {
            this.offerFlowerTick = 400;
        }
        else if (byte1 == 34) {
            this.offerFlowerTick = 0;
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }
    
    public void offerFlower(final boolean boolean1) {
        if (boolean1) {
            this.offerFlowerTick = 400;
            this.level.broadcastEntityEvent(this, (byte)11);
        }
        else {
            this.offerFlowerTick = 0;
            this.level.broadcastEntityEvent(this, (byte)34);
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.IRON_GOLEM_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }
    
    protected InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        final Item blu5 = bly4.getItem();
        if (blu5 != Items.IRON_INGOT) {
            return InteractionResult.PASS;
        }
        final float float6 = this.getHealth();
        this.heal(25.0f);
        if (this.getHealth() == float6) {
            return InteractionResult.PASS;
        }
        final float float7 = 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f;
        this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0f, float7);
        if (!bft.abilities.instabuild) {
            bly4.shrink(1);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0f, 1.0f);
    }
    
    public int getOfferFlowerTick() {
        return this.offerFlowerTick;
    }
    
    public boolean isPlayerCreated() {
        return (this.entityData.<Byte>get(IronGolem.DATA_FLAGS_ID) & 0x1) != 0x0;
    }
    
    public void setPlayerCreated(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(IronGolem.DATA_FLAGS_ID);
        if (boolean1) {
            this.entityData.<Byte>set(IronGolem.DATA_FLAGS_ID, (byte)(byte3 | 0x1));
        }
        else {
            this.entityData.<Byte>set(IronGolem.DATA_FLAGS_ID, (byte)(byte3 & 0xFFFFFFFE));
        }
    }
    
    public void die(final DamageSource aph) {
        super.die(aph);
    }
    
    public boolean checkSpawnObstruction(final LevelReader brw) {
        final BlockPos fx3 = this.blockPosition();
        final BlockPos fx4 = fx3.below();
        final BlockState cee5 = brw.getBlockState(fx4);
        if (cee5.entityCanStandOn(brw, fx4, this)) {
            for (int integer6 = 1; integer6 < 3; ++integer6) {
                final BlockPos fx5 = fx3.above(integer6);
                final BlockState cee6 = brw.getBlockState(fx5);
                if (!NaturalSpawner.isValidEmptySpawnBlock(brw, fx5, cee6, cee6.getFluidState(), EntityType.IRON_GOLEM)) {
                    return false;
                }
            }
            return NaturalSpawner.isValidEmptySpawnBlock(brw, fx3, brw.getBlockState(fx3), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && brw.isUnobstructed(this);
        }
        return false;
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.875f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(IronGolem.class, EntityDataSerializers.BYTE);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
    
    public enum Crackiness {
        NONE(1.0f), 
        LOW(0.75f), 
        MEDIUM(0.5f), 
        HIGH(0.25f);
        
        private static final List<Crackiness> BY_DAMAGE;
        private final float fraction;
        
        private Crackiness(final float float3) {
            this.fraction = float3;
        }
        
        public static Crackiness byFraction(final float float1) {
            for (final Crackiness a3 : Crackiness.BY_DAMAGE) {
                if (float1 < a3.fraction) {
                    return a3;
                }
            }
            return Crackiness.NONE;
        }
        
        static {
            BY_DAMAGE = (List)Stream.of((Object[])values()).sorted(Comparator.comparingDouble(a -> a.fraction)).collect(ImmutableList.toImmutableList());
        }
    }
}
