package net.minecraft.world.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.alchemy.Potion;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.world.entity.raid.Raider;

public class Witch extends Raider implements RangedAttackMob {
    private static final UUID SPEED_MODIFIER_DRINKING_UUID;
    private static final AttributeModifier SPEED_MODIFIER_DRINKING;
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM;
    private int usingTime;
    private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;
    private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
    
    public Witch(final EntityType<? extends Witch> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.healRaidersGoal = new NearestHealableRaiderTargetGoal<Raider>(this, Raider.class, true, (Predicate<LivingEntity>)(aqj -> aqj != null && this.hasActiveRaid() && aqj.getType() != EntityType.WITCH));
        this.attackPlayersGoal = new NearestAttackableWitchTargetGoal<Player>(this, Player.class, 10, true, false, null);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 60, 10.0f));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Raider.class }));
        this.targetSelector.addGoal(2, this.healRaidersGoal);
        this.targetSelector.addGoal(3, this.attackPlayersGoal);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().<Boolean>define(Witch.DATA_USING_ITEM, false);
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.WITCH_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }
    
    public void setUsingItem(final boolean boolean1) {
        this.getEntityData().<Boolean>set(Witch.DATA_USING_ITEM, boolean1);
    }
    
    public boolean isDrinkingPotion() {
        return this.getEntityData().<Boolean>get(Witch.DATA_USING_ITEM);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 26.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    
    @Override
    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive()) {
            this.healRaidersGoal.decrementCooldown();
            if (this.healRaidersGoal.getCooldown() <= 0) {
                this.attackPlayersGoal.setCanAttack(true);
            }
            else {
                this.attackPlayersGoal.setCanAttack(false);
            }
            if (this.isDrinkingPotion()) {
                if (this.usingTime-- <= 0) {
                    this.setUsingItem(false);
                    final ItemStack bly2 = this.getMainHandItem();
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    if (bly2.getItem() == Items.POTION) {
                        final List<MobEffectInstance> list3 = PotionUtils.getMobEffects(bly2);
                        if (list3 != null) {
                            for (final MobEffectInstance apr5 : list3) {
                                this.addEffect(new MobEffectInstance(apr5));
                            }
                        }
                    }
                    this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(Witch.SPEED_MODIFIER_DRINKING);
                }
            }
            else {
                Potion bnq2 = null;
                if (this.random.nextFloat() < 0.15f && this.isEyeInFluid(FluidTags.WATER) && !this.hasEffect(MobEffects.WATER_BREATHING)) {
                    bnq2 = Potions.WATER_BREATHING;
                }
                else if (this.random.nextFloat() < 0.15f && (this.isOnFire() || (this.getLastDamageSource() != null && this.getLastDamageSource().isFire())) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    bnq2 = Potions.FIRE_RESISTANCE;
                }
                else if (this.random.nextFloat() < 0.05f && this.getHealth() < this.getMaxHealth()) {
                    bnq2 = Potions.HEALING;
                }
                else if (this.random.nextFloat() < 0.5f && this.getTarget() != null && !this.hasEffect(MobEffects.MOVEMENT_SPEED) && this.getTarget().distanceToSqr(this) > 121.0) {
                    bnq2 = Potions.SWIFTNESS;
                }
                if (bnq2 != null) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), bnq2));
                    this.usingTime = this.getMainHandItem().getUseDuration();
                    this.setUsingItem(true);
                    if (!this.isSilent()) {
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0f, 0.8f + this.random.nextFloat() * 0.4f);
                    }
                    final AttributeInstance are3 = this.getAttribute(Attributes.MOVEMENT_SPEED);
                    are3.removeModifier(Witch.SPEED_MODIFIER_DRINKING);
                    are3.addTransientModifier(Witch.SPEED_MODIFIER_DRINKING);
                }
            }
            if (this.random.nextFloat() < 7.5E-4f) {
                this.level.broadcastEntityEvent(this, (byte)15);
            }
        }
        super.aiStep();
    }
    
    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.WITCH_CELEBRATE;
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 15) {
            for (int integer3 = 0; integer3 < this.random.nextInt(35) + 10; ++integer3) {
                this.level.addParticle(ParticleTypes.WITCH, this.getX() + this.random.nextGaussian() * 0.12999999523162842, this.getBoundingBox().maxY + 0.5 + this.random.nextGaussian() * 0.12999999523162842, this.getZ() + this.random.nextGaussian() * 0.12999999523162842, 0.0, 0.0, 0.0);
            }
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    protected float getDamageAfterMagicAbsorb(final DamageSource aph, float float2) {
        float2 = super.getDamageAfterMagicAbsorb(aph, float2);
        if (aph.getEntity() == this) {
            float2 = 0.0f;
        }
        if (aph.isMagic()) {
            float2 *= (float)0.15;
        }
        return float2;
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        if (this.isDrinkingPotion()) {
            return;
        }
        final Vec3 dck4 = aqj.getDeltaMovement();
        final double double5 = aqj.getX() + dck4.x - this.getX();
        final double double6 = aqj.getEyeY() - 1.100000023841858 - this.getY();
        final double double7 = aqj.getZ() + dck4.z - this.getZ();
        final float float3 = Mth.sqrt(double5 * double5 + double7 * double7);
        Potion bnq12 = Potions.HARMING;
        if (aqj instanceof Raider) {
            if (aqj.getHealth() <= 4.0f) {
                bnq12 = Potions.HEALING;
            }
            else {
                bnq12 = Potions.REGENERATION;
            }
            this.setTarget(null);
        }
        else if (float3 >= 8.0f && !aqj.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            bnq12 = Potions.SLOWNESS;
        }
        else if (aqj.getHealth() >= 8.0f && !aqj.hasEffect(MobEffects.POISON)) {
            bnq12 = Potions.POISON;
        }
        else if (float3 <= 3.0f && !aqj.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25f) {
            bnq12 = Potions.WEAKNESS;
        }
        final ThrownPotion bgu13 = new ThrownPotion(this.level, this);
        bgu13.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), bnq12));
        final ThrownPotion thrownPotion = bgu13;
        thrownPotion.xRot += 20.0f;
        bgu13.shoot(double5, double6 + float3 * 0.2f, double7, 0.75f, 8.0f);
        if (!this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0f, 0.8f + this.random.nextFloat() * 0.4f);
        }
        this.level.addFreshEntity(bgu13);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 1.62f;
    }
    
    @Override
    public void applyRaidBuffs(final int integer, final boolean boolean2) {
    }
    
    @Override
    public boolean canBeLeader() {
        return false;
    }
    
    static {
        SPEED_MODIFIER_DRINKING_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
        SPEED_MODIFIER_DRINKING = new AttributeModifier(Witch.SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25, AttributeModifier.Operation.ADDITION);
        DATA_USING_ITEM = SynchedEntityData.<Boolean>defineId(Witch.class, EntityDataSerializers.BOOLEAN);
    }
}
