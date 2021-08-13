package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class WitherSkeleton extends AbstractSkeleton {
    public WitherSkeleton(final EntityType<? extends WitherSkeleton> aqb, final Level bru) {
        super(aqb, bru);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0f);
    }
    
    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractPiglin.class, true));
        super.registerGoals();
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }
    
    @Override
    SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        final Entity apx5 = aph.getEntity();
        if (apx5 instanceof Creeper) {
            final Creeper bcz6 = (Creeper)apx5;
            if (bcz6.canDropMobsSkull()) {
                bcz6.increaseDroppedSkulls();
                this.spawnAtLocation(Items.WITHER_SKELETON_SKULL);
            }
        }
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }
    
    protected void populateDefaultEquipmentEnchantments(final DifficultyInstance aop) {
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        final SpawnGroupData aqz2 = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
        this.reassessWeaponGoal();
        return aqz2;
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 2.1f;
    }
    
    public boolean doHurtTarget(final Entity apx) {
        if (!super.doHurtTarget(apx)) {
            return false;
        }
        if (apx instanceof LivingEntity) {
            ((LivingEntity)apx).addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
        }
        return true;
    }
    
    @Override
    protected AbstractArrow getArrow(final ItemStack bly, final float float2) {
        final AbstractArrow bfx4 = super.getArrow(bly, float2);
        bfx4.setSecondsOnFire(100);
        return bfx4;
    }
    
    public boolean canBeAffected(final MobEffectInstance apr) {
        return apr.getEffect() != MobEffects.WITHER && super.canBeAffected(apr);
    }
}
