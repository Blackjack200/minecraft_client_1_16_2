package net.minecraft.world.entity.animal.horse;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class SkeletonHorse extends AbstractHorse {
    private final SkeletonTrapGoal skeletonTrapGoal;
    private boolean isTrap;
    private int trapTime;
    
    public SkeletonHorse(final EntityType<? extends SkeletonHorse> aqb, final Level bru) {
        super(aqb, bru);
        this.skeletonTrapGoal = new SkeletonTrapGoal(this);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractHorse.createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }
    
    @Override
    protected void randomizeAttributes() {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
    }
    
    @Override
    protected void addBehaviourGoals() {
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            return SoundEvents.SKELETON_HORSE_AMBIENT_WATER;
        }
        return SoundEvents.SKELETON_HORSE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.SKELETON_HORSE_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        super.getHurtSound(aph);
        return SoundEvents.SKELETON_HORSE_HURT;
    }
    
    protected SoundEvent getSwimSound() {
        if (this.onGround) {
            if (!this.isVehicle()) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
            ++this.gallopSoundCounter;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
            }
            if (this.gallopSoundCounter <= 5) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
        }
        return SoundEvents.SKELETON_HORSE_SWIM;
    }
    
    protected void playSwimSound(final float float1) {
        if (this.onGround) {
            super.playSwimSound(0.3f);
        }
        else {
            super.playSwimSound(Math.min(0.1f, float1 * 25.0f));
        }
    }
    
    @Override
    protected void playJumpSound() {
        if (this.isInWater()) {
            this.playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4f, 1.0f);
        }
        else {
            super.playJumpSound();
        }
    }
    
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.1875;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isTrap() && this.trapTime++ >= 18000) {
            this.remove();
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("SkeletonTrap", this.isTrap());
        md.putInt("SkeletonTrapTime", this.trapTime);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setTrap(md.getBoolean("SkeletonTrap"));
        this.trapTime = md.getInt("SkeletonTrapTime");
    }
    
    public boolean rideableUnderWater() {
        return true;
    }
    
    protected float getWaterSlowDown() {
        return 0.96f;
    }
    
    public boolean isTrap() {
        return this.isTrap;
    }
    
    public void setTrap(final boolean boolean1) {
        if (boolean1 == this.isTrap) {
            return;
        }
        this.isTrap = boolean1;
        if (boolean1) {
            this.goalSelector.addGoal(1, this.skeletonTrapGoal);
        }
        else {
            this.goalSelector.removeGoal(this.skeletonTrapGoal);
        }
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.SKELETON_HORSE.create(aag);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (!this.isTamed()) {
            return InteractionResult.PASS;
        }
        if (this.isBaby()) {
            return super.mobInteract(bft, aoq);
        }
        if (bft.isSecondaryUseActive()) {
            this.openInventory(bft);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (this.isVehicle()) {
            return super.mobInteract(bft, aoq);
        }
        if (!bly4.isEmpty()) {
            if (bly4.getItem() == Items.SADDLE && !this.isSaddled()) {
                this.openInventory(bft);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            final InteractionResult aor5 = bly4.interactLivingEntity(bft, this, aoq);
            if (aor5.consumesAction()) {
                return aor5;
            }
        }
        this.doPlayerRide(bft);
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
}
