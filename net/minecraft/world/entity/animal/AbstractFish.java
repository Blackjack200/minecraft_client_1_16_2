package net.minecraft.world.entity.animal;

import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class AbstractFish extends WaterAnimal {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET;
    
    public AbstractFish(final EntityType<? extends AbstractFish> aqb, final Level bru) {
        super(aqb, bru);
        this.moveControl = new FishMoveControl(this);
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.65f;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0);
    }
    
    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }
    
    public static boolean checkFishSpawnRules(final EntityType<? extends AbstractFish> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getBlockState(fx).is(Blocks.WATER) && brv.getBlockState(fx.above()).is(Blocks.WATER);
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return !this.fromBucket() && !this.hasCustomName();
    }
    
    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(AbstractFish.FROM_BUCKET, false);
    }
    
    private boolean fromBucket() {
        return this.entityData.<Boolean>get(AbstractFish.FROM_BUCKET);
    }
    
    public void setFromBucket(final boolean boolean1) {
        this.entityData.<Boolean>set(AbstractFish.FROM_BUCKET, boolean1);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("FromBucket", this.fromBucket());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setFromBucket(md.getBoolean("FromBucket"));
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0f, 1.6, 1.4, (Predicate<LivingEntity>)EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(4, new FishSwimGoal(this));
    }
    
    @Override
    protected PathNavigation createNavigation(final Level bru) {
        return new WaterBoundPathNavigation(this, bru);
    }
    
    @Override
    public void travel(final Vec3 dck) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        }
        else {
            super.travel(dck);
        }
    }
    
    @Override
    public void aiStep() {
        if (!this.isInWater() && this.onGround && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.05f, 0.4000000059604645, (this.random.nextFloat() * 2.0f - 1.0f) * 0.05f));
            this.onGround = false;
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        super.aiStep();
    }
    
    @Override
    protected InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.WATER_BUCKET && this.isAlive()) {
            this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0f, 1.0f);
            bly4.shrink(1);
            final ItemStack bly5 = this.getBucketItemStack();
            this.saveToBucketTag(bly5);
            if (!this.level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)bft, bly5);
            }
            if (bly4.isEmpty()) {
                bft.setItemInHand(aoq, bly5);
            }
            else if (!bft.inventory.add(bly5)) {
                bft.drop(bly5, false);
            }
            this.remove();
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(bft, aoq);
    }
    
    protected void saveToBucketTag(final ItemStack bly) {
        if (this.hasCustomName()) {
            bly.setHoverName(this.getCustomName());
        }
    }
    
    protected abstract ItemStack getBucketItemStack();
    
    protected boolean canRandomSwim() {
        return true;
    }
    
    protected abstract SoundEvent getFlopSound();
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }
    
    @Override
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
    }
    
    static {
        FROM_BUCKET = SynchedEntityData.<Boolean>defineId(AbstractFish.class, EntityDataSerializers.BOOLEAN);
    }
    
    static class FishSwimGoal extends RandomSwimmingGoal {
        private final AbstractFish fish;
        
        public FishSwimGoal(final AbstractFish azt) {
            super(azt, 1.0, 40);
            this.fish = azt;
        }
        
        @Override
        public boolean canUse() {
            return this.fish.canRandomSwim() && super.canUse();
        }
    }
    
    static class FishMoveControl extends MoveControl {
        private final AbstractFish fish;
        
        FishMoveControl(final AbstractFish azt) {
            super(azt);
            this.fish = azt;
        }
        
        @Override
        public void tick() {
            if (this.fish.isEyeInFluid(FluidTags.WATER)) {
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, 0.005, 0.0));
            }
            if (this.operation != Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
                this.fish.setSpeed(0.0f);
                return;
            }
            final float float2 = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.fish.setSpeed(Mth.lerp(0.125f, this.fish.getSpeed(), float2));
            final double double3 = this.wantedX - this.fish.getX();
            final double double4 = this.wantedY - this.fish.getY();
            final double double5 = this.wantedZ - this.fish.getZ();
            if (double4 != 0.0) {
                final double double6 = Mth.sqrt(double3 * double3 + double4 * double4 + double5 * double5);
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, this.fish.getSpeed() * (double4 / double6) * 0.1, 0.0));
            }
            if (double3 != 0.0 || double5 != 0.0) {
                final float float3 = (float)(Mth.atan2(double5, double3) * 57.2957763671875) - 90.0f;
                this.fish.yRot = this.rotlerp(this.fish.yRot, float3, 90.0f);
                this.fish.yBodyRot = this.fish.yRot;
            }
        }
    }
}
