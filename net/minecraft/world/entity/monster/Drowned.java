package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import java.util.EnumSet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelAccessor;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;

public class Drowned extends Zombie implements RangedAttackMob {
    private boolean searchingForLand;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    
    public Drowned(final EntityType<? extends Drowned> aqb, final Level bru) {
        super(aqb, bru);
        this.maxUpStep = 1.0f;
        this.moveControl = new DrownedMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.waterNavigation = new WaterBoundPathNavigation(this, bru);
        this.groundNavigation = new GroundPathNavigation(this, bru);
    }
    
    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new DrownedGoToWaterGoal(this, 1.0));
        this.goalSelector.addGoal(2, new DrownedTridentAttackGoal(this, 1.0, 40, 10.0f));
        this.goalSelector.addGoal(2, new DrownedAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new DrownedGoToBeachGoal(this, 1.0));
        this.goalSelector.addGoal(6, new DrownedSwimUpGoal(this, 1.0, this.level.getSeaLevel()));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Drowned.class }).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)this::okTarget));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        aqz = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        if (this.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && this.random.nextFloat() < 0.03f) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.NAUTILUS_SHELL));
            this.handDropChances[EquipmentSlot.OFFHAND.getIndex()] = 2.0f;
        }
        return aqz;
    }
    
    public static boolean checkDrownedSpawnRules(final EntityType<Drowned> aqb, final ServerLevelAccessor bsh, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final Optional<ResourceKey<Biome>> optional6 = bsh.getBiomeName(fx);
        final boolean boolean7 = bsh.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(bsh, fx, random) && (aqm == MobSpawnType.SPAWNER || bsh.getFluidState(fx).is(FluidTags.WATER));
        if (Objects.equals(optional6, Optional.of((Object)Biomes.RIVER)) || Objects.equals(optional6, Optional.of((Object)Biomes.FROZEN_RIVER))) {
            return random.nextInt(15) == 0 && boolean7;
        }
        return random.nextInt(40) == 0 && isDeepEnoughToSpawn(bsh, fx) && boolean7;
    }
    
    private static boolean isDeepEnoughToSpawn(final LevelAccessor brv, final BlockPos fx) {
        return fx.getY() < brv.getSeaLevel() - 5;
    }
    
    @Override
    protected boolean supportsBreakDoorGoal() {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isInWater()) {
            return SoundEvents.DROWNED_AMBIENT_WATER;
        }
        return SoundEvents.DROWNED_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.isInWater()) {
            return SoundEvents.DROWNED_HURT_WATER;
        }
        return SoundEvents.DROWNED_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        if (this.isInWater()) {
            return SoundEvents.DROWNED_DEATH_WATER;
        }
        return SoundEvents.DROWNED_DEATH;
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.DROWNED_STEP;
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.DROWNED_SWIM;
    }
    
    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        if (this.random.nextFloat() > 0.9) {
            final int integer3 = this.random.nextInt(16);
            if (integer3 < 10) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            }
            else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
        }
    }
    
    protected boolean canReplaceCurrentItem(final ItemStack bly1, final ItemStack bly2) {
        if (bly2.getItem() == Items.NAUTILUS_SHELL) {
            return false;
        }
        if (bly2.getItem() == Items.TRIDENT) {
            return bly1.getItem() == Items.TRIDENT && bly1.getDamageValue() < bly2.getDamageValue();
        }
        return bly1.getItem() == Items.TRIDENT || super.canReplaceCurrentItem(bly1, bly2);
    }
    
    @Override
    protected boolean convertsInWater() {
        return false;
    }
    
    public boolean checkSpawnObstruction(final LevelReader brw) {
        return brw.isUnobstructed(this);
    }
    
    public boolean okTarget(@Nullable final LivingEntity aqj) {
        return aqj != null && (!this.level.isDay() || aqj.isInWater());
    }
    
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }
    
    private boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        }
        final LivingEntity aqj2 = this.getTarget();
        return aqj2 != null && aqj2.isInWater();
    }
    
    public void travel(final Vec3 dck) {
        if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.01f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        else {
            super.travel(dck);
        }
    }
    
    public void updateSwimming() {
        if (!this.level.isClientSide) {
            if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            }
            else {
                this.navigation = this.groundNavigation;
                this.setSwimming(false);
            }
        }
    }
    
    protected boolean closeToNextPos() {
        final Path cxa2 = this.getNavigation().getPath();
        if (cxa2 != null) {
            final BlockPos fx3 = cxa2.getTarget();
            if (fx3 != null) {
                final double double4 = this.distanceToSqr(fx3.getX(), fx3.getY(), fx3.getZ());
                if (double4 < 4.0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        final ThrownTrident bgv4 = new ThrownTrident(this.level, this, new ItemStack(Items.TRIDENT));
        final double double5 = aqj.getX() - this.getX();
        final double double6 = aqj.getY(0.3333333333333333) - bgv4.getY();
        final double double7 = aqj.getZ() - this.getZ();
        final double double8 = Mth.sqrt(double5 * double5 + double7 * double7);
        bgv4.shoot(double5, double6 + double8 * 0.20000000298023224, double7, 1.6f, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level.addFreshEntity(bgv4);
    }
    
    public void setSearchingForLand(final boolean boolean1) {
        this.searchingForLand = boolean1;
    }
    
    static class DrownedTridentAttackGoal extends RangedAttackGoal {
        private final Drowned drowned;
        
        public DrownedTridentAttackGoal(final RangedAttackMob bdr, final double double2, final int integer, final float float4) {
            super(bdr, double2, integer, float4);
            this.drowned = (Drowned)bdr;
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.drowned.getMainHandItem().getItem() == Items.TRIDENT;
        }
        
        @Override
        public void start() {
            super.start();
            this.drowned.setAggressive(true);
            this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
        }
        
        @Override
        public void stop() {
            super.stop();
            this.drowned.stopUsingItem();
            this.drowned.setAggressive(false);
        }
    }
    
    static class DrownedSwimUpGoal extends Goal {
        private final Drowned drowned;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;
        
        public DrownedSwimUpGoal(final Drowned bdb, final double double2, final int integer) {
            this.drowned = bdb;
            this.speedModifier = double2;
            this.seaLevel = integer;
        }
        
        @Override
        public boolean canUse() {
            return !this.drowned.level.isDay() && this.drowned.isInWater() && this.drowned.getY() < this.seaLevel - 2;
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }
        
        @Override
        public void tick() {
            if (this.drowned.getY() < this.seaLevel - 1 && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
                final Vec3 dck2 = RandomPos.getPosTowards(this.drowned, 4, 8, new Vec3(this.drowned.getX(), this.seaLevel - 1, this.drowned.getZ()));
                if (dck2 == null) {
                    this.stuck = true;
                    return;
                }
                this.drowned.getNavigation().moveTo(dck2.x, dck2.y, dck2.z, this.speedModifier);
            }
        }
        
        @Override
        public void start() {
            this.drowned.setSearchingForLand(true);
            this.stuck = false;
        }
        
        @Override
        public void stop() {
            this.drowned.setSearchingForLand(false);
        }
    }
    
    static class DrownedGoToBeachGoal extends MoveToBlockGoal {
        private final Drowned drowned;
        
        public DrownedGoToBeachGoal(final Drowned bdb, final double double2) {
            super(bdb, double2, 8, 2);
            this.drowned = bdb;
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && !this.drowned.level.isDay() && this.drowned.isInWater() && this.drowned.getY() >= this.drowned.level.getSeaLevel() - 3;
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
        
        @Override
        protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
            final BlockPos fx2 = fx.above();
            return brw.isEmptyBlock(fx2) && brw.isEmptyBlock(fx2.above()) && brw.getBlockState(fx).entityCanStandOn(brw, fx, this.drowned);
        }
        
        @Override
        public void start() {
            this.drowned.setSearchingForLand(false);
            this.drowned.navigation = this.drowned.groundNavigation;
            super.start();
        }
        
        @Override
        public void stop() {
            super.stop();
        }
    }
    
    static class DrownedGoToWaterGoal extends Goal {
        private final PathfinderMob mob;
        private double wantedX;
        private double wantedY;
        private double wantedZ;
        private final double speedModifier;
        private final Level level;
        
        public DrownedGoToWaterGoal(final PathfinderMob aqr, final double double2) {
            this.mob = aqr;
            this.speedModifier = double2;
            this.level = aqr.level;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            if (!this.level.isDay()) {
                return false;
            }
            if (this.mob.isInWater()) {
                return false;
            }
            final Vec3 dck2 = this.getWaterPos();
            if (dck2 == null) {
                return false;
            }
            this.wantedX = dck2.x;
            this.wantedY = dck2.y;
            this.wantedZ = dck2.z;
            return true;
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone();
        }
        
        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }
        
        @Nullable
        private Vec3 getWaterPos() {
            final Random random2 = this.mob.getRandom();
            final BlockPos fx3 = this.mob.blockPosition();
            for (int integer4 = 0; integer4 < 10; ++integer4) {
                final BlockPos fx4 = fx3.offset(random2.nextInt(20) - 10, 2 - random2.nextInt(8), random2.nextInt(20) - 10);
                if (this.level.getBlockState(fx4).is(Blocks.WATER)) {
                    return Vec3.atBottomCenterOf(fx4);
                }
            }
            return null;
        }
    }
    
    static class DrownedAttackGoal extends ZombieAttackGoal {
        private final Drowned drowned;
        
        public DrownedAttackGoal(final Drowned bdb, final double double2, final boolean boolean3) {
            super(bdb, double2, boolean3);
            this.drowned = bdb;
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.drowned.okTarget(this.drowned.getTarget());
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget());
        }
    }
    
    static class DrownedMoveControl extends MoveControl {
        private final Drowned drowned;
        
        public DrownedMoveControl(final Drowned bdb) {
            super(bdb);
            this.drowned = bdb;
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = this.drowned.getTarget();
            if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
                if ((aqj2 != null && aqj2.getY() > this.drowned.getY()) || this.drowned.searchingForLand) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, 0.002, 0.0));
                }
                if (this.operation != Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
                    this.drowned.setSpeed(0.0f);
                    return;
                }
                final double double3 = this.wantedX - this.drowned.getX();
                double double4 = this.wantedY - this.drowned.getY();
                final double double5 = this.wantedZ - this.drowned.getZ();
                final double double6 = Mth.sqrt(double3 * double3 + double4 * double4 + double5 * double5);
                double4 /= double6;
                final float float11 = (float)(Mth.atan2(double5, double3) * 57.2957763671875) - 90.0f;
                this.drowned.yRot = this.rotlerp(this.drowned.yRot, float11, 90.0f);
                this.drowned.yBodyRot = this.drowned.yRot;
                final float float12 = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                final float float13 = Mth.lerp(0.125f, this.drowned.getSpeed(), float12);
                this.drowned.setSpeed(float13);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(float13 * double3 * 0.005, float13 * double4 * 0.1, float13 * double5 * 0.005));
            }
            else {
                if (!this.drowned.onGround) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, -0.008, 0.0));
                }
                super.tick();
            }
        }
    }
}
