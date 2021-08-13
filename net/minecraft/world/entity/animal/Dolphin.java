package net.minecraft.world.entity.animal;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.MoverType;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.level.biome.Biomes;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.DolphinLookControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Dolphin extends WaterAnimal {
    private static final EntityDataAccessor<BlockPos> TREASURE_POS;
    private static final EntityDataAccessor<Boolean> GOT_FISH;
    private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL;
    private static final TargetingConditions SWIM_WITH_PLAYER_TARGETING;
    public static final Predicate<ItemEntity> ALLOWED_ITEMS;
    
    public Dolphin(final EntityType<? extends Dolphin> aqb, final Level bru) {
        super(aqb, bru);
        this.moveControl = new DolphinMoveControl(this);
        this.lookControl = new DolphinLookControl(this, 10);
        this.setCanPickUpLoot(true);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setAirSupply(this.getMaxAirSupply());
        this.xRot = 0.0f;
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }
    
    @Override
    protected void handleAirSupply(final int integer) {
    }
    
    public void setTreasurePos(final BlockPos fx) {
        this.entityData.<BlockPos>set(Dolphin.TREASURE_POS, fx);
    }
    
    public BlockPos getTreasurePos() {
        return this.entityData.<BlockPos>get(Dolphin.TREASURE_POS);
    }
    
    public boolean gotFish() {
        return this.entityData.<Boolean>get(Dolphin.GOT_FISH);
    }
    
    public void setGotFish(final boolean boolean1) {
        this.entityData.<Boolean>set(Dolphin.GOT_FISH, boolean1);
    }
    
    public int getMoistnessLevel() {
        return this.entityData.<Integer>get(Dolphin.MOISTNESS_LEVEL);
    }
    
    public void setMoisntessLevel(final int integer) {
        this.entityData.<Integer>set(Dolphin.MOISTNESS_LEVEL, integer);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<BlockPos>define(Dolphin.TREASURE_POS, BlockPos.ZERO);
        this.entityData.<Boolean>define(Dolphin.GOT_FISH, false);
        this.entityData.<Integer>define(Dolphin.MOISTNESS_LEVEL, 2400);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("TreasurePosX", this.getTreasurePos().getX());
        md.putInt("TreasurePosY", this.getTreasurePos().getY());
        md.putInt("TreasurePosZ", this.getTreasurePos().getZ());
        md.putBoolean("GotFish", this.gotFish());
        md.putInt("Moistness", this.getMoistnessLevel());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        final int integer3 = md.getInt("TreasurePosX");
        final int integer4 = md.getInt("TreasurePosY");
        final int integer5 = md.getInt("TreasurePosZ");
        this.setTreasurePos(new BlockPos(integer3, integer4, integer5));
        super.readAdditionalSaveData(md);
        this.setGotFish(md.getBoolean("GotFish"));
        this.setMoisntessLevel(md.getInt("Moistness"));
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new DolphinSwimToTreasureGoal(this));
        this.goalSelector.addGoal(2, new DolphinSwimWithPlayerGoal(this, 4.0));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(5, new DolphinJumpGoal(this, 10));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2000000476837158, true));
        this.goalSelector.addGoal(8, new PlayWithItemsGoal());
        this.goalSelector.addGoal(8, new FollowBoatGoal(this));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0f, 1.0, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Guardian.class }).setAlertOthers(new Class[0]));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 1.2000000476837158).add(Attributes.ATTACK_DAMAGE, 3.0);
    }
    
    @Override
    protected PathNavigation createNavigation(final Level bru) {
        return new WaterBoundPathNavigation(this, bru);
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        final boolean boolean3 = apx.hurt(DamageSource.mobAttack(this), (float)(int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (boolean3) {
            this.doEnchantDamageEffects(this, apx);
            this.playSound(SoundEvents.DOLPHIN_ATTACK, 1.0f, 1.0f);
        }
        return boolean3;
    }
    
    @Override
    public int getMaxAirSupply() {
        return 4800;
    }
    
    @Override
    protected int increaseAirSupply(final int integer) {
        return this.getMaxAirSupply();
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.3f;
    }
    
    @Override
    public int getMaxHeadXRot() {
        return 1;
    }
    
    @Override
    public int getMaxHeadYRot() {
        return 1;
    }
    
    @Override
    protected boolean canRide(final Entity apx) {
        return true;
    }
    
    @Override
    public boolean canTakeItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = Mob.getEquipmentSlotForItem(bly);
        return this.getItemBySlot(aqc3).isEmpty() && aqc3 == EquipmentSlot.MAINHAND && super.canTakeItem(bly);
    }
    
    @Override
    protected void pickUpItem(final ItemEntity bcs) {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            final ItemStack bly3 = bcs.getItem();
            if (this.canHoldItem(bly3)) {
                this.onItemPickup(bcs);
                this.setItemSlot(EquipmentSlot.MAINHAND, bly3);
                this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0f;
                this.take(bcs, bly3.getCount());
                bcs.remove();
            }
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
            return;
        }
        if (this.isInWaterRainOrBubble()) {
            this.setMoisntessLevel(2400);
        }
        else {
            this.setMoisntessLevel(this.getMoistnessLevel() - 1);
            if (this.getMoistnessLevel() <= 0) {
                this.hurt(DamageSource.DRY_OUT, 1.0f);
            }
            if (this.onGround) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.2f, 0.5, (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f));
                this.yRot = this.random.nextFloat() * 360.0f;
                this.onGround = false;
                this.hasImpulse = true;
            }
        }
        if (this.level.isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03) {
            final Vec3 dck2 = this.getViewVector(0.0f);
            final float float3 = Mth.cos(this.yRot * 0.017453292f) * 0.3f;
            final float float4 = Mth.sin(this.yRot * 0.017453292f) * 0.3f;
            final float float5 = 1.2f - this.random.nextFloat() * 0.7f;
            for (int integer6 = 0; integer6 < 2; ++integer6) {
                this.level.addParticle(ParticleTypes.DOLPHIN, this.getX() - dck2.x * float5 + float3, this.getY() - dck2.y, this.getZ() - dck2.z * float5 + float4, 0.0, 0.0, 0.0);
                this.level.addParticle(ParticleTypes.DOLPHIN, this.getX() - dck2.x * float5 - float3, this.getY() - dck2.y, this.getZ() - dck2.z * float5 - float4, 0.0, 0.0, 0.0);
            }
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 38) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    private void addParticlesAroundSelf(final ParticleOptions hf) {
        for (int integer3 = 0; integer3 < 7; ++integer3) {
            final double double4 = this.random.nextGaussian() * 0.01;
            final double double5 = this.random.nextGaussian() * 0.01;
            final double double6 = this.random.nextGaussian() * 0.01;
            this.level.addParticle(hf, this.getRandomX(1.0), this.getRandomY() + 0.2, this.getRandomZ(1.0), double4, double5, double6);
        }
    }
    
    @Override
    protected InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (!bly4.isEmpty() && bly4.getItem().is(ItemTags.FISHES)) {
            if (!this.level.isClientSide) {
                this.playSound(SoundEvents.DOLPHIN_EAT, 1.0f, 1.0f);
            }
            this.setGotFish(true);
            if (!bft.abilities.instabuild) {
                bly4.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(bft, aoq);
    }
    
    public static boolean checkDolphinSpawnRules(final EntityType<Dolphin> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        if (fx.getY() <= 45 || fx.getY() >= brv.getSeaLevel()) {
            return false;
        }
        final Optional<ResourceKey<Biome>> optional6 = brv.getBiomeName(fx);
        return (!Objects.equals(optional6, Optional.of((Object)Biomes.OCEAN)) || !Objects.equals(optional6, Optional.of((Object)Biomes.DEEP_OCEAN))) && brv.getFluidState(fx).is(FluidTags.WATER);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.DOLPHIN_HURT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.DOLPHIN_SPLASH;
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }
    
    protected boolean closeToNextPos() {
        final BlockPos fx2 = this.getNavigation().getTargetPos();
        return fx2 != null && fx2.closerThan(this.position(), 12.0);
    }
    
    @Override
    public void travel(final Vec3 dck) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), dck);
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
    public boolean canBeLeashed(final Player bft) {
        return true;
    }
    
    static {
        TREASURE_POS = SynchedEntityData.<BlockPos>defineId(Dolphin.class, EntityDataSerializers.BLOCK_POS);
        GOT_FISH = SynchedEntityData.<Boolean>defineId(Dolphin.class, EntityDataSerializers.BOOLEAN);
        MOISTNESS_LEVEL = SynchedEntityData.<Integer>defineId(Dolphin.class, EntityDataSerializers.INT);
        SWIM_WITH_PLAYER_TARGETING = new TargetingConditions().range(10.0).allowSameTeam().allowInvulnerable().allowUnseeable();
        ALLOWED_ITEMS = (bcs -> !bcs.hasPickUpDelay() && bcs.isAlive() && bcs.isInWater());
    }
    
    static class DolphinMoveControl extends MoveControl {
        private final Dolphin dolphin;
        
        public DolphinMoveControl(final Dolphin bac) {
            super(bac);
            this.dolphin = bac;
        }
        
        @Override
        public void tick() {
            if (this.dolphin.isInWater()) {
                this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0, 0.005, 0.0));
            }
            if (this.operation != Operation.MOVE_TO || this.dolphin.getNavigation().isDone()) {
                this.dolphin.setSpeed(0.0f);
                this.dolphin.setXxa(0.0f);
                this.dolphin.setYya(0.0f);
                this.dolphin.setZza(0.0f);
                return;
            }
            final double double2 = this.wantedX - this.dolphin.getX();
            final double double3 = this.wantedY - this.dolphin.getY();
            final double double4 = this.wantedZ - this.dolphin.getZ();
            final double double5 = double2 * double2 + double3 * double3 + double4 * double4;
            if (double5 < 2.500000277905201E-7) {
                this.mob.setZza(0.0f);
                return;
            }
            final float float10 = (float)(Mth.atan2(double4, double2) * 57.2957763671875) - 90.0f;
            this.dolphin.yRot = this.rotlerp(this.dolphin.yRot, float10, 10.0f);
            this.dolphin.yBodyRot = this.dolphin.yRot;
            this.dolphin.yHeadRot = this.dolphin.yRot;
            final float float11 = (float)(this.speedModifier * this.dolphin.getAttributeValue(Attributes.MOVEMENT_SPEED));
            if (this.dolphin.isInWater()) {
                this.dolphin.setSpeed(float11 * 0.02f);
                float float12 = -(float)(Mth.atan2(double3, Mth.sqrt(double2 * double2 + double4 * double4)) * 57.2957763671875);
                float12 = Mth.clamp(Mth.wrapDegrees(float12), -85.0f, 85.0f);
                this.dolphin.xRot = this.rotlerp(this.dolphin.xRot, float12, 5.0f);
                final float float13 = Mth.cos(this.dolphin.xRot * 0.017453292f);
                final float float14 = Mth.sin(this.dolphin.xRot * 0.017453292f);
                this.dolphin.zza = float13 * float11;
                this.dolphin.yya = -float14 * float11;
            }
            else {
                this.dolphin.setSpeed(float11 * 0.1f);
            }
        }
    }
    
    class PlayWithItemsGoal extends Goal {
        private int cooldown;
        
        private PlayWithItemsGoal() {
        }
        
        @Override
        public boolean canUse() {
            if (this.cooldown > Dolphin.this.tickCount) {
                return false;
            }
            final List<ItemEntity> list2 = Dolphin.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Dolphin.ALLOWED_ITEMS);
            return !list2.isEmpty() || !Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
        
        @Override
        public void start() {
            final List<ItemEntity> list2 = Dolphin.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Dolphin.ALLOWED_ITEMS);
            if (!list2.isEmpty()) {
                Dolphin.this.getNavigation().moveTo((Entity)list2.get(0), 1.2000000476837158);
                Dolphin.this.playSound(SoundEvents.DOLPHIN_PLAY, 1.0f, 1.0f);
            }
            this.cooldown = 0;
        }
        
        @Override
        public void stop() {
            final ItemStack bly2 = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!bly2.isEmpty()) {
                this.drop(bly2);
                Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.cooldown = Dolphin.this.tickCount + Dolphin.this.random.nextInt(100);
            }
        }
        
        @Override
        public void tick() {
            final List<ItemEntity> list2 = Dolphin.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Dolphin.ALLOWED_ITEMS);
            final ItemStack bly3 = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!bly3.isEmpty()) {
                this.drop(bly3);
                Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
            else if (!list2.isEmpty()) {
                Dolphin.this.getNavigation().moveTo((Entity)list2.get(0), 1.2000000476837158);
            }
        }
        
        private void drop(final ItemStack bly) {
            if (bly.isEmpty()) {
                return;
            }
            final double double3 = Dolphin.this.getEyeY() - 0.30000001192092896;
            final ItemEntity bcs5 = new ItemEntity(Dolphin.this.level, Dolphin.this.getX(), double3, Dolphin.this.getZ(), bly);
            bcs5.setPickUpDelay(40);
            bcs5.setThrower(Dolphin.this.getUUID());
            final float float6 = 0.3f;
            final float float7 = Dolphin.this.random.nextFloat() * 6.2831855f;
            final float float8 = 0.02f * Dolphin.this.random.nextFloat();
            bcs5.setDeltaMovement(0.3f * -Mth.sin(Dolphin.this.yRot * 0.017453292f) * Mth.cos(Dolphin.this.xRot * 0.017453292f) + Mth.cos(float7) * float8, 0.3f * Mth.sin(Dolphin.this.xRot * 0.017453292f) * 1.5f, 0.3f * Mth.cos(Dolphin.this.yRot * 0.017453292f) * Mth.cos(Dolphin.this.xRot * 0.017453292f) + Mth.sin(float7) * float8);
            Dolphin.this.level.addFreshEntity(bcs5);
        }
    }
    
    static class DolphinSwimWithPlayerGoal extends Goal {
        private final Dolphin dolphin;
        private final double speedModifier;
        private Player player;
        
        DolphinSwimWithPlayerGoal(final Dolphin bac, final double double2) {
            this.dolphin = bac;
            this.speedModifier = double2;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            this.player = this.dolphin.level.getNearestPlayer(Dolphin.SWIM_WITH_PLAYER_TARGETING, this.dolphin);
            return this.player != null && this.player.isSwimming() && this.dolphin.getTarget() != this.player;
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr(this.player) < 256.0;
        }
        
        @Override
        public void start() {
            this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100));
        }
        
        @Override
        public void stop() {
            this.player = null;
            this.dolphin.getNavigation().stop();
        }
        
        @Override
        public void tick() {
            this.dolphin.getLookControl().setLookAt(this.player, (float)(this.dolphin.getMaxHeadYRot() + 20), (float)this.dolphin.getMaxHeadXRot());
            if (this.dolphin.distanceToSqr(this.player) < 6.25) {
                this.dolphin.getNavigation().stop();
            }
            else {
                this.dolphin.getNavigation().moveTo(this.player, this.speedModifier);
            }
            if (this.player.isSwimming() && this.player.level.random.nextInt(6) == 0) {
                this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100));
            }
        }
    }
    
    static class DolphinSwimToTreasureGoal extends Goal {
        private final Dolphin dolphin;
        private boolean stuck;
        
        DolphinSwimToTreasureGoal(final Dolphin bac) {
            this.dolphin = bac;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean isInterruptable() {
            return false;
        }
        
        @Override
        public boolean canUse() {
            return this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100;
        }
        
        @Override
        public boolean canContinueToUse() {
            final BlockPos fx2 = this.dolphin.getTreasurePos();
            return !new BlockPos(fx2.getX(), this.dolphin.getY(), fx2.getZ()).closerThan(this.dolphin.position(), 4.0) && !this.stuck && this.dolphin.getAirSupply() >= 100;
        }
        
        @Override
        public void start() {
            if (!(this.dolphin.level instanceof ServerLevel)) {
                return;
            }
            final ServerLevel aag2 = (ServerLevel)this.dolphin.level;
            this.stuck = false;
            this.dolphin.getNavigation().stop();
            final BlockPos fx3 = this.dolphin.blockPosition();
            final StructureFeature<?> ckx4 = ((aag2.random.nextFloat() >= 0.5) ? StructureFeature.OCEAN_RUIN : StructureFeature.SHIPWRECK);
            final BlockPos fx4 = aag2.findNearestMapFeature(ckx4, fx3, 50, false);
            if (fx4 == null) {
                final StructureFeature<?> ckx5 = (ckx4.equals(StructureFeature.OCEAN_RUIN) ? StructureFeature.SHIPWRECK : StructureFeature.OCEAN_RUIN);
                final BlockPos fx5 = aag2.findNearestMapFeature(ckx5, fx3, 50, false);
                if (fx5 == null) {
                    this.stuck = true;
                    return;
                }
                this.dolphin.setTreasurePos(fx5);
            }
            else {
                this.dolphin.setTreasurePos(fx4);
            }
            aag2.broadcastEntityEvent(this.dolphin, (byte)38);
        }
        
        @Override
        public void stop() {
            final BlockPos fx2 = this.dolphin.getTreasurePos();
            if (new BlockPos(fx2.getX(), this.dolphin.getY(), fx2.getZ()).closerThan(this.dolphin.position(), 4.0) || this.stuck) {
                this.dolphin.setGotFish(false);
            }
        }
        
        @Override
        public void tick() {
            final Level bru2 = this.dolphin.level;
            if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
                final Vec3 dck3 = Vec3.atCenterOf(this.dolphin.getTreasurePos());
                Vec3 dck4 = RandomPos.getPosTowards(this.dolphin, 16, 1, dck3, 0.39269909262657166);
                if (dck4 == null) {
                    dck4 = RandomPos.getPosTowards(this.dolphin, 8, 4, dck3);
                }
                if (dck4 != null) {
                    final BlockPos fx5 = new BlockPos(dck4);
                    if (!bru2.getFluidState(fx5).is(FluidTags.WATER) || !bru2.getBlockState(fx5).isPathfindable(bru2, fx5, PathComputationType.WATER)) {
                        dck4 = RandomPos.getPosTowards(this.dolphin, 8, 5, dck3);
                    }
                }
                if (dck4 == null) {
                    this.stuck = true;
                    return;
                }
                this.dolphin.getLookControl().setLookAt(dck4.x, dck4.y, dck4.z, (float)(this.dolphin.getMaxHeadYRot() + 20), (float)this.dolphin.getMaxHeadXRot());
                this.dolphin.getNavigation().moveTo(dck4.x, dck4.y, dck4.z, 1.3);
                if (bru2.random.nextInt(80) == 0) {
                    bru2.broadcastEntityEvent(this.dolphin, (byte)38);
                }
            }
        }
    }
}
