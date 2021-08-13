package net.minecraft.world.entity.animal;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ItemSteerable;

public class Pig extends Animal implements ItemSteerable, Saddleable {
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    private static final Ingredient FOOD_ITEMS;
    private final ItemBasedSteering steering;
    
    public Pig(final EntityType<? extends Pig> aqb, final Level bru) {
        super(aqb, bru);
        this.steering = new ItemBasedSteering(this.entityData, Pig.DATA_BOOST_TIME, Pig.DATA_SADDLE_ID);
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, false, Pig.FOOD_ITEMS));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    
    @Nullable
    public Entity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        return (Entity)this.getPassengers().get(0);
    }
    
    public boolean canBeControlledByRider() {
        final Entity apx2 = this.getControllingPassenger();
        if (!(apx2 instanceof Player)) {
            return false;
        }
        final Player bft3 = (Player)apx2;
        return bft3.getMainHandItem().getItem() == Items.CARROT_ON_A_STICK || bft3.getOffhandItem().getItem() == Items.CARROT_ON_A_STICK;
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Pig.DATA_BOOST_TIME.equals(us) && this.level.isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Pig.DATA_SADDLE_ID, false);
        this.entityData.<Integer>define(Pig.DATA_BOOST_TIME, 0);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        this.steering.addAdditionalSaveData(md);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.steering.readAdditionalSaveData(md);
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PIG_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.PIG_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final boolean boolean4 = this.isFood(bft.getItemInHand(aoq));
        if (!boolean4 && this.isSaddled() && !this.isVehicle() && !bft.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                bft.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        final InteractionResult aor5 = super.mobInteract(bft, aoq);
        if (aor5.consumesAction()) {
            return aor5;
        }
        final ItemStack bly6 = bft.getItemInHand(aoq);
        if (bly6.getItem() == Items.SADDLE) {
            return bly6.interactLivingEntity(bft, this, aoq);
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }
    
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }
    
    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }
    
    @Override
    public void equipSaddle(@Nullable final SoundSource adp) {
        this.steering.setSaddle(true);
        if (adp != null) {
            this.level.playSound(null, this, SoundEvents.PIG_SADDLE, adp, 0.5f, 1.0f);
        }
    }
    
    public Vec3 getDismountLocationForPassenger(final LivingEntity aqj) {
        final Direction gc3 = this.getMotionDirection();
        if (gc3.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(aqj);
        }
        final int[][] arr4 = DismountHelper.offsetsForDirection(gc3);
        final BlockPos fx5 = this.blockPosition();
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        for (final Pose aqu8 : aqj.getDismountPoses()) {
            final AABB dcf9 = aqj.getLocalBoundsForPose(aqu8);
            for (final int[] arr5 : arr4) {
                a6.set(fx5.getX() + arr5[0], fx5.getY(), fx5.getZ() + arr5[1]);
                final double double14 = this.level.getBlockFloorHeight(a6);
                if (DismountHelper.isBlockFloorValid(double14)) {
                    final Vec3 dck16 = Vec3.upFromBottomCenterOf(a6, double14);
                    if (DismountHelper.canDismountTo(this.level, aqj, dcf9.move(dck16))) {
                        aqj.setPose(aqu8);
                        return dck16;
                    }
                }
            }
        }
        return super.getDismountLocationForPassenger(aqj);
    }
    
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
        if (aag.getDifficulty() != Difficulty.PEACEFUL) {
            final ZombifiedPiglin bei4 = EntityType.ZOMBIFIED_PIGLIN.create(aag);
            bei4.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            bei4.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            bei4.setNoAi(this.isNoAi());
            bei4.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                bei4.setCustomName(this.getCustomName());
                bei4.setCustomNameVisible(this.isCustomNameVisible());
            }
            bei4.setPersistenceRequired();
            aag.addFreshEntity(bei4);
            this.remove();
        }
        else {
            super.thunderHit(aag, aqi);
        }
    }
    
    public void travel(final Vec3 dck) {
        this.travel(this, this.steering, dck);
    }
    
    @Override
    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225f;
    }
    
    @Override
    public void travelWithInput(final Vec3 dck) {
        super.travel(dck);
    }
    
    @Override
    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }
    
    @Override
    public Pig getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.PIG.create(aag);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return Pig.FOOD_ITEMS.test(bly);
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.6f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        DATA_SADDLE_ID = SynchedEntityData.<Boolean>defineId(Pig.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.<Integer>defineId(Pig.class, EntityDataSerializers.INT);
        FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);
    }
}
