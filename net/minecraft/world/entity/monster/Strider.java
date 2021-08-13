package net.minecraft.world.entity.monster;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.entity.Pose;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.DismountHelper;
import com.google.common.collect.Sets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.Items;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.animal.Animal;

public class Strider extends Animal implements ItemSteerable, Saddleable {
    private static final Ingredient FOOD_ITEMS;
    private static final Ingredient TEMPT_ITEMS;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING;
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private final ItemBasedSteering steering;
    private TemptGoal temptGoal;
    private PanicGoal panicGoal;
    
    public Strider(final EntityType<? extends Strider> aqb, final Level bru) {
        super(aqb, bru);
        this.steering = new ItemBasedSteering(this.entityData, Strider.DATA_BOOST_TIME, Strider.DATA_SADDLE_ID);
        this.blocksBuilding = true;
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0f);
    }
    
    public static boolean checkStriderSpawnRules(final EntityType<Strider> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final BlockPos.MutableBlockPos a6 = fx.mutable();
        do {
            a6.move(Direction.UP);
        } while (brv.getFluidState(a6).is(FluidTags.LAVA));
        return brv.getBlockState(a6).isAir();
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Strider.DATA_BOOST_TIME.equals(us) && this.level.isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Strider.DATA_BOOST_TIME, 0);
        this.entityData.<Boolean>define(Strider.DATA_SUFFOCATING, false);
        this.entityData.<Boolean>define(Strider.DATA_SADDLE_ID, false);
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
    
    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }
    
    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }
    
    @Override
    public void equipSaddle(@Nullable final SoundSource adp) {
        this.steering.setSaddle(true);
        if (adp != null) {
            this.level.playSound(null, this, SoundEvents.STRIDER_SADDLE, adp, 0.5f, 1.0f);
        }
    }
    
    protected void registerGoals() {
        this.panicGoal = new PanicGoal(this, 1.65);
        this.goalSelector.addGoal(1, this.panicGoal);
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.temptGoal = new TemptGoal(this, 1.4, false, Strider.TEMPT_ITEMS);
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(4, new StriderGoToLavaGoal(this, 1.5));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0, 60));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Strider.class, 8.0f));
    }
    
    public void setSuffocating(final boolean boolean1) {
        this.entityData.<Boolean>set(Strider.DATA_SUFFOCATING, boolean1);
    }
    
    public boolean isSuffocating() {
        if (this.getVehicle() instanceof Strider) {
            return ((Strider)this.getVehicle()).isSuffocating();
        }
        return this.entityData.<Boolean>get(Strider.DATA_SUFFOCATING);
    }
    
    public boolean canStandOnFluid(final Fluid cut) {
        return cut.is(FluidTags.LAVA);
    }
    
    public double getPassengersRidingOffset() {
        final float float2 = Math.min(0.25f, this.animationSpeed);
        final float float3 = this.animationPosition;
        return this.getBbHeight() - 0.19 + 0.12f * Mth.cos(float3 * 1.5f) * 2.0f * float2;
    }
    
    public boolean canBeControlledByRider() {
        final Entity apx2 = this.getControllingPassenger();
        if (!(apx2 instanceof Player)) {
            return false;
        }
        final Player bft3 = (Player)apx2;
        return bft3.getMainHandItem().getItem() == Items.WARPED_FUNGUS_ON_A_STICK || bft3.getOffhandItem().getItem() == Items.WARPED_FUNGUS_ON_A_STICK;
    }
    
    public boolean checkSpawnObstruction(final LevelReader brw) {
        return brw.isUnobstructed(this);
    }
    
    @Nullable
    public Entity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        return (Entity)this.getPassengers().get(0);
    }
    
    public Vec3 getDismountLocationForPassenger(final LivingEntity aqj) {
        final Vec3[] arr3 = { Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), aqj.yRot), Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), aqj.yRot - 22.5f), Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), aqj.yRot + 22.5f), Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), aqj.yRot - 45.0f), Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), aqj.yRot + 45.0f) };
        final Set<BlockPos> set4 = (Set<BlockPos>)Sets.newLinkedHashSet();
        final double double5 = this.getBoundingBox().maxY;
        final double double6 = this.getBoundingBox().minY - 0.5;
        final BlockPos.MutableBlockPos a9 = new BlockPos.MutableBlockPos();
        for (final Vec3 dck13 : arr3) {
            a9.set(this.getX() + dck13.x, double5, this.getZ() + dck13.z);
            for (double double7 = double5; double7 > double6; --double7) {
                set4.add(a9.immutable());
                a9.move(Direction.DOWN);
            }
        }
        for (final BlockPos fx11 : set4) {
            if (this.level.getFluidState(fx11).is(FluidTags.LAVA)) {
                continue;
            }
            final double double8 = this.level.getBlockFloorHeight(fx11);
            if (!DismountHelper.isBlockFloorValid(double8)) {
                continue;
            }
            final Vec3 dck14 = Vec3.upFromBottomCenterOf(fx11, double8);
            for (final Pose aqu16 : aqj.getDismountPoses()) {
                final AABB dcf17 = aqj.getLocalBoundsForPose(aqu16);
                if (DismountHelper.canDismountTo(this.level, aqj, dcf17.move(dck14))) {
                    aqj.setPose(aqu16);
                    return dck14;
                }
            }
        }
        return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
    }
    
    public void travel(final Vec3 dck) {
        this.setSpeed(this.getMoveSpeed());
        this.travel(this, this.steering, dck);
    }
    
    public float getMoveSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isSuffocating() ? 0.66f : 1.0f);
    }
    
    @Override
    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isSuffocating() ? 0.23f : 0.55f);
    }
    
    @Override
    public void travelWithInput(final Vec3 dck) {
        super.travel(dck);
    }
    
    protected float nextStep() {
        return this.moveDist + 0.6f;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(this.isInLava() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0f, 1.0f);
    }
    
    @Override
    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }
    
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
        this.checkInsideBlocks();
        if (this.isInLava()) {
            this.fallDistance = 0.0f;
            return;
        }
        super.checkFallDamage(double1, boolean2, cee, fx);
    }
    
    public void tick() {
        if (this.isBeingTempted() && this.random.nextInt(140) == 0) {
            this.playSound(SoundEvents.STRIDER_HAPPY, 1.0f, this.getVoicePitch());
        }
        else if (this.isPanicking() && this.random.nextInt(60) == 0) {
            this.playSound(SoundEvents.STRIDER_RETREAT, 1.0f, this.getVoicePitch());
        }
        final BlockState cee2 = this.level.getBlockState(this.blockPosition());
        final BlockState cee3 = this.getBlockStateOn();
        final boolean boolean4 = cee2.is(BlockTags.STRIDER_WARM_BLOCKS) || cee3.is(BlockTags.STRIDER_WARM_BLOCKS) || this.getFluidHeight(FluidTags.LAVA) > 0.0;
        this.setSuffocating(!boolean4);
        super.tick();
        this.floatStrider();
        this.checkInsideBlocks();
    }
    
    private boolean isPanicking() {
        return this.panicGoal != null && this.panicGoal.isRunning();
    }
    
    private boolean isBeingTempted() {
        return this.temptGoal != null && this.temptGoal.isRunning();
    }
    
    protected boolean shouldPassengersInheritMalus() {
        return true;
    }
    
    private void floatStrider() {
        if (this.isInLava()) {
            final CollisionContext dcp2 = CollisionContext.of(this);
            if (!dcp2.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) || this.level.getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(0.0, 0.05, 0.0));
            }
            else {
                this.onGround = true;
            }
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.17499999701976776).add(Attributes.FOLLOW_RANGE, 16.0);
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.isPanicking() || this.isBeingTempted()) {
            return null;
        }
        return SoundEvents.STRIDER_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.STRIDER_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRIDER_DEATH;
    }
    
    protected boolean canAddPassenger(final Entity apx) {
        return this.getPassengers().isEmpty() && !this.isEyeInFluid(FluidTags.LAVA);
    }
    
    public boolean isSensitiveToWater() {
        return true;
    }
    
    public boolean isOnFire() {
        return false;
    }
    
    protected PathNavigation createNavigation(final Level bru) {
        return new StriderPathNavigation(this, bru);
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (brw.getBlockState(fx).getFluidState().is(FluidTags.LAVA)) {
            return 10.0f;
        }
        return this.isInLava() ? Float.NEGATIVE_INFINITY : 0.0f;
    }
    
    @Override
    public Strider getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.STRIDER.create(aag);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return Strider.FOOD_ITEMS.test(bly);
    }
    
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
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
            if (boolean4 && !this.isSilent()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.STRIDER_EAT, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            }
            return aor5;
        }
        final ItemStack bly6 = bft.getItemInHand(aoq);
        if (bly6.getItem() == Items.SADDLE) {
            return bly6.interactLivingEntity(bft, this, aoq);
        }
        return InteractionResult.PASS;
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.6f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (this.isBaby()) {
            return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        }
        if (this.random.nextInt(30) == 0) {
            final Mob aqk7 = EntityType.ZOMBIFIED_PIGLIN.create(bsh.getLevel());
            aqz = this.spawnJockey(bsh, aop, aqk7, new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(this.random), false));
            aqk7.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK));
            this.equipSaddle(null);
        }
        else if (this.random.nextInt(10) == 0) {
            final AgableMob apv7 = EntityType.STRIDER.create(bsh.getLevel());
            apv7.setAge(-24000);
            aqz = this.spawnJockey(bsh, aop, apv7, null);
        }
        else {
            aqz = new AgableMobGroupData(0.5f);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    private SpawnGroupData spawnJockey(final ServerLevelAccessor bsh, final DifficultyInstance aop, final Mob aqk, @Nullable final SpawnGroupData aqz) {
        aqk.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
        aqk.finalizeSpawn(bsh, aop, MobSpawnType.JOCKEY, aqz, null);
        aqk.startRiding(this, true);
        return new AgableMobGroupData(0.0f);
    }
    
    static {
        FOOD_ITEMS = Ingredient.of(Items.WARPED_FUNGUS);
        TEMPT_ITEMS = Ingredient.of(Items.WARPED_FUNGUS, Items.WARPED_FUNGUS_ON_A_STICK);
        DATA_BOOST_TIME = SynchedEntityData.<Integer>defineId(Strider.class, EntityDataSerializers.INT);
        DATA_SUFFOCATING = SynchedEntityData.<Boolean>defineId(Strider.class, EntityDataSerializers.BOOLEAN);
        DATA_SADDLE_ID = SynchedEntityData.<Boolean>defineId(Strider.class, EntityDataSerializers.BOOLEAN);
    }
    
    static class StriderPathNavigation extends GroundPathNavigation {
        StriderPathNavigation(final Strider bea, final Level bru) {
            super(bea, bru);
        }
        
        @Override
        protected PathFinder createPathFinder(final int integer) {
            this.nodeEvaluator = new WalkNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, integer);
        }
        
        @Override
        protected boolean hasValidPathType(final BlockPathTypes cww) {
            return cww == BlockPathTypes.LAVA || cww == BlockPathTypes.DAMAGE_FIRE || cww == BlockPathTypes.DANGER_FIRE || super.hasValidPathType(cww);
        }
        
        @Override
        public boolean isStableDestination(final BlockPos fx) {
            return this.level.getBlockState(fx).is(Blocks.LAVA) || super.isStableDestination(fx);
        }
    }
    
    static class StriderGoToLavaGoal extends MoveToBlockGoal {
        private final Strider strider;
        
        private StriderGoToLavaGoal(final Strider bea, final double double2) {
            super(bea, double2, 8, 2);
            this.strider = bea;
        }
        
        public BlockPos getMoveToTarget() {
            return this.blockPos;
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.strider.isInLava() && this.isValidTarget(this.strider.level, this.blockPos);
        }
        
        @Override
        public boolean canUse() {
            return !this.strider.isInLava() && super.canUse();
        }
        
        @Override
        public boolean shouldRecalculatePath() {
            return this.tryTicks % 20 == 0;
        }
        
        @Override
        protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
            return brw.getBlockState(fx).is(Blocks.LAVA) && brw.getBlockState(fx.above()).isPathfindable(brw, fx, PathComputationType.LAND);
        }
    }
}
