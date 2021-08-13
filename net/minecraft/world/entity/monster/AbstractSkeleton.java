package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.Blocks;
import java.time.temporal.TemporalField;
import java.time.temporal.ChronoField;
import java.time.LocalDate;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;

public abstract class AbstractSkeleton extends Monster implements RangedAttackMob {
    private final RangedBowAttackGoal<AbstractSkeleton> bowGoal;
    private final MeleeAttackGoal meleeGoal;
    
    protected AbstractSkeleton(final EntityType<? extends AbstractSkeleton> aqb, final Level bru) {
        super(aqb, bru);
        this.bowGoal = new RangedBowAttackGoal<AbstractSkeleton>(this, 1.0, 20, 15.0f);
        this.meleeGoal = new MeleeAttackGoal((PathfinderMob)this, 1.2, false) {
            @Override
            public void stop() {
                super.stop();
                AbstractSkeleton.this.setAggressive(false);
            }
            
            @Override
            public void start() {
                super.start();
                AbstractSkeleton.this.setAggressive(true);
            }
        };
        this.reassessWeaponGoal();
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0f, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }
    
    abstract SoundEvent getStepSound();
    
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    @Override
    public void aiStep() {
        boolean boolean2 = this.isSunBurnTick();
        if (boolean2) {
            final ItemStack bly3 = this.getItemBySlot(EquipmentSlot.HEAD);
            if (!bly3.isEmpty()) {
                if (bly3.isDamageableItem()) {
                    bly3.setDamageValue(bly3.getDamageValue() + this.random.nextInt(2));
                    if (bly3.getDamageValue() >= bly3.getMaxDamage()) {
                        this.broadcastBreakEvent(EquipmentSlot.HEAD);
                        this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }
                boolean2 = false;
            }
            if (boolean2) {
                this.setSecondsOnFire(8);
            }
        }
        super.aiStep();
    }
    
    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof PathfinderMob) {
            final PathfinderMob aqr2 = (PathfinderMob)this.getVehicle();
            this.yBodyRot = aqr2.yBodyRot;
        }
    }
    
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        super.populateDefaultEquipmentSlots(aop);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }
    
    @Nullable
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        aqz = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        this.populateDefaultEquipmentSlots(aop);
        this.populateDefaultEquipmentEnchantments(aop);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55f * aop.getSpecialMultiplier());
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            final LocalDate localDate7 = LocalDate.now();
            final int integer8 = localDate7.get((TemporalField)ChronoField.DAY_OF_MONTH);
            final int integer9 = localDate7.get((TemporalField)ChronoField.MONTH_OF_YEAR);
            if (integer9 == 10 && integer8 == 31 && this.random.nextFloat() < 0.25f) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((this.random.nextFloat() < 0.1f) ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0f;
            }
        }
        return aqz;
    }
    
    public void reassessWeaponGoal() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        this.goalSelector.removeGoal(this.meleeGoal);
        this.goalSelector.removeGoal(this.bowGoal);
        final ItemStack bly2 = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        if (bly2.getItem() == Items.BOW) {
            int integer3 = 20;
            if (this.level.getDifficulty() != Difficulty.HARD) {
                integer3 = 40;
            }
            this.bowGoal.setMinAttackInterval(integer3);
            this.goalSelector.addGoal(4, this.bowGoal);
        }
        else {
            this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        final ItemStack bly4 = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        final AbstractArrow bfx5 = this.getArrow(bly4, float2);
        final double double6 = aqj.getX() - this.getX();
        final double double7 = aqj.getY(0.3333333333333333) - bfx5.getY();
        final double double8 = aqj.getZ() - this.getZ();
        final double double9 = Mth.sqrt(double6 * double6 + double8 * double8);
        bfx5.shoot(double6, double7 + double9 * 0.20000000298023224, double8, 1.6f, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level.addFreshEntity(bfx5);
    }
    
    protected AbstractArrow getArrow(final ItemStack bly, final float float2) {
        return ProjectileUtil.getMobArrow(this, bly, float2);
    }
    
    public boolean canFireProjectileWeapon(final ProjectileWeaponItem bml) {
        return bml == Items.BOW;
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.reassessWeaponGoal();
    }
    
    public void setItemSlot(final EquipmentSlot aqc, final ItemStack bly) {
        super.setItemSlot(aqc, bly);
        if (!this.level.isClientSide) {
            this.reassessWeaponGoal();
        }
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 1.74f;
    }
    
    public double getMyRidingOffset() {
        return -0.6;
    }
}
