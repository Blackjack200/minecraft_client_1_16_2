package net.minecraft.world.entity.animal.horse;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.AABB;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.Container;
import java.util.Iterator;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.SimpleContainer;
import java.util.UUID;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.animal.Animal;

public abstract class AbstractHorse extends Animal implements ContainerListener, PlayerRideableJumping, Saddleable {
    private static final Predicate<LivingEntity> PARENT_HORSE_SELECTOR;
    private static final TargetingConditions MOMMY_TARGETING;
    private static final Ingredient FOOD_ITEMS;
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS;
    private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID;
    private int eatingCounter;
    private int mouthCounter;
    private int standCounter;
    public int tailCounter;
    public int sprintCounter;
    protected boolean isJumping;
    protected SimpleContainer inventory;
    protected int temper;
    protected float playerJumpPendingScale;
    private boolean allowStandSliding;
    private float eatAnim;
    private float eatAnimO;
    private float standAnim;
    private float standAnimO;
    private float mouthAnim;
    private float mouthAnimO;
    protected boolean canGallop;
    protected int gallopSoundCounter;
    
    protected AbstractHorse(final EntityType<? extends AbstractHorse> aqb, final Level bru) {
        super(aqb, bru);
        this.canGallop = true;
        this.maxUpStep = 1.0f;
        this.createInventory();
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, AbstractHorse.class));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }
    
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(AbstractHorse.DATA_ID_FLAGS, (Byte)0);
        this.entityData.<Optional<UUID>>define(AbstractHorse.DATA_ID_OWNER_UUID, (Optional<UUID>)Optional.empty());
    }
    
    protected boolean getFlag(final int integer) {
        return (this.entityData.<Byte>get(AbstractHorse.DATA_ID_FLAGS) & integer) != 0x0;
    }
    
    protected void setFlag(final int integer, final boolean boolean2) {
        final byte byte4 = this.entityData.<Byte>get(AbstractHorse.DATA_ID_FLAGS);
        if (boolean2) {
            this.entityData.<Byte>set(AbstractHorse.DATA_ID_FLAGS, (byte)(byte4 | integer));
        }
        else {
            this.entityData.<Byte>set(AbstractHorse.DATA_ID_FLAGS, (byte)(byte4 & ~integer));
        }
    }
    
    public boolean isTamed() {
        return this.getFlag(2);
    }
    
    @Nullable
    public UUID getOwnerUUID() {
        return (UUID)this.entityData.<Optional<UUID>>get(AbstractHorse.DATA_ID_OWNER_UUID).orElse(null);
    }
    
    public void setOwnerUUID(@Nullable final UUID uUID) {
        this.entityData.<Optional<UUID>>set(AbstractHorse.DATA_ID_OWNER_UUID, (Optional<UUID>)Optional.ofNullable(uUID));
    }
    
    public boolean isJumping() {
        return this.isJumping;
    }
    
    public void setTamed(final boolean boolean1) {
        this.setFlag(2, boolean1);
    }
    
    public void setIsJumping(final boolean boolean1) {
        this.isJumping = boolean1;
    }
    
    protected void onLeashDistance(final float float1) {
        if (float1 > 6.0f && this.isEating()) {
            this.setEating(false);
        }
    }
    
    public boolean isEating() {
        return this.getFlag(16);
    }
    
    public boolean isStanding() {
        return this.getFlag(32);
    }
    
    public boolean isBred() {
        return this.getFlag(8);
    }
    
    public void setBred(final boolean boolean1) {
        this.setFlag(8, boolean1);
    }
    
    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTamed();
    }
    
    @Override
    public void equipSaddle(@Nullable final SoundSource adp) {
        this.inventory.setItem(0, new ItemStack(Items.SADDLE));
        if (adp != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, adp, 0.5f, 1.0f);
        }
    }
    
    @Override
    public boolean isSaddled() {
        return this.getFlag(4);
    }
    
    public int getTemper() {
        return this.temper;
    }
    
    public void setTemper(final int integer) {
        this.temper = integer;
    }
    
    public int modifyTemper(final int integer) {
        final int integer2 = Mth.clamp(this.getTemper() + integer, 0, this.getMaxTemper());
        this.setTemper(integer2);
        return integer2;
    }
    
    public boolean isPushable() {
        return !this.isVehicle();
    }
    
    private void eating() {
        this.openMouth();
        if (!this.isSilent()) {
            final SoundEvent adn2 = this.getEatingSound();
            if (adn2 != null) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), adn2, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            }
        }
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        if (float1 > 1.0f) {
            this.playSound(SoundEvents.HORSE_LAND, 0.4f, 1.0f);
        }
        final int integer4 = this.calculateFallDamage(float1, float2);
        if (integer4 <= 0) {
            return false;
        }
        this.hurt(DamageSource.FALL, (float)integer4);
        if (this.isVehicle()) {
            for (final Entity apx6 : this.getIndirectPassengers()) {
                apx6.hurt(DamageSource.FALL, (float)integer4);
            }
        }
        this.playBlockFallSound();
        return true;
    }
    
    protected int calculateFallDamage(final float float1, final float float2) {
        return Mth.ceil((float1 * 0.5f - 3.0f) * float2);
    }
    
    protected int getInventorySize() {
        return 2;
    }
    
    protected void createInventory() {
        final SimpleContainer aox2 = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (aox2 != null) {
            aox2.removeListener(this);
            for (int integer3 = Math.min(aox2.getContainerSize(), this.inventory.getContainerSize()), integer4 = 0; integer4 < integer3; ++integer4) {
                final ItemStack bly5 = aox2.getItem(integer4);
                if (!bly5.isEmpty()) {
                    this.inventory.setItem(integer4, bly5.copy());
                }
            }
        }
        this.inventory.addListener(this);
        this.updateContainerEquipment();
    }
    
    protected void updateContainerEquipment() {
        if (this.level.isClientSide) {
            return;
        }
        this.setFlag(4, !this.inventory.getItem(0).isEmpty());
    }
    
    @Override
    public void containerChanged(final Container aok) {
        final boolean boolean3 = this.isSaddled();
        this.updateContainerEquipment();
        if (this.tickCount > 20 && !boolean3 && this.isSaddled()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5f, 1.0f);
        }
    }
    
    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }
    
    @Nullable
    protected SoundEvent getEatingSound() {
        return null;
    }
    
    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }
    
    @Nullable
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.random.nextInt(3) == 0) {
            this.stand();
        }
        return null;
    }
    
    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.random.nextInt(10) == 0 && !this.isImmobile()) {
            this.stand();
        }
        return null;
    }
    
    @Nullable
    protected SoundEvent getAngrySound() {
        this.stand();
        return null;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        if (cee.getMaterial().isLiquid()) {
            return;
        }
        final BlockState cee2 = this.level.getBlockState(fx.above());
        SoundType cab5 = cee.getSoundType();
        if (cee2.is(Blocks.SNOW)) {
            cab5 = cee2.getSoundType();
        }
        if (this.isVehicle() && this.canGallop) {
            ++this.gallopSoundCounter;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                this.playGallopSound(cab5);
            }
            else if (this.gallopSoundCounter <= 5) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, cab5.getVolume() * 0.15f, cab5.getPitch());
            }
        }
        else if (cab5 == SoundType.WOOD) {
            this.playSound(SoundEvents.HORSE_STEP_WOOD, cab5.getVolume() * 0.15f, cab5.getPitch());
        }
        else {
            this.playSound(SoundEvents.HORSE_STEP, cab5.getVolume() * 0.15f, cab5.getPitch());
        }
    }
    
    protected void playGallopSound(final SoundType cab) {
        this.playSound(SoundEvents.HORSE_GALLOP, cab.getVolume() * 0.15f, cab.getPitch());
    }
    
    public static AttributeSupplier.Builder createBaseHorseAttributes() {
        return Mob.createMobAttributes().add(Attributes.JUMP_STRENGTH).add(Attributes.MAX_HEALTH, 53.0).add(Attributes.MOVEMENT_SPEED, 0.22499999403953552);
    }
    
    public int getMaxSpawnClusterSize() {
        return 6;
    }
    
    public int getMaxTemper() {
        return 100;
    }
    
    protected float getSoundVolume() {
        return 0.8f;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }
    
    public void openInventory(final Player bft) {
        if (!this.level.isClientSide && (!this.isVehicle() || this.hasPassenger(bft)) && this.isTamed()) {
            bft.openHorseInventory(this, this.inventory);
        }
    }
    
    public InteractionResult fedFood(final Player bft, final ItemStack bly) {
        final boolean boolean4 = this.handleEating(bft, bly);
        if (!bft.abilities.instabuild) {
            bly.shrink(1);
        }
        if (this.level.isClientSide) {
            return InteractionResult.CONSUME;
        }
        return boolean4 ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
    
    protected boolean handleEating(final Player bft, final ItemStack bly) {
        boolean boolean4 = false;
        float float5 = 0.0f;
        int integer6 = 0;
        int integer7 = 0;
        final Item blu8 = bly.getItem();
        if (blu8 == Items.WHEAT) {
            float5 = 2.0f;
            integer6 = 20;
            integer7 = 3;
        }
        else if (blu8 == Items.SUGAR) {
            float5 = 1.0f;
            integer6 = 30;
            integer7 = 3;
        }
        else if (blu8 == Blocks.HAY_BLOCK.asItem()) {
            float5 = 20.0f;
            integer6 = 180;
        }
        else if (blu8 == Items.APPLE) {
            float5 = 3.0f;
            integer6 = 60;
            integer7 = 3;
        }
        else if (blu8 == Items.GOLDEN_CARROT) {
            float5 = 4.0f;
            integer6 = 60;
            integer7 = 5;
            if (!this.level.isClientSide && this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                boolean4 = true;
                this.setInLove(bft);
            }
        }
        else if (blu8 == Items.GOLDEN_APPLE || blu8 == Items.ENCHANTED_GOLDEN_APPLE) {
            float5 = 10.0f;
            integer6 = 240;
            integer7 = 10;
            if (!this.level.isClientSide && this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                boolean4 = true;
                this.setInLove(bft);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && float5 > 0.0f) {
            this.heal(float5);
            boolean4 = true;
        }
        if (this.isBaby() && integer6 > 0) {
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
            if (!this.level.isClientSide) {
                this.ageUp(integer6);
            }
            boolean4 = true;
        }
        if (integer7 > 0 && (boolean4 || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
            boolean4 = true;
            if (!this.level.isClientSide) {
                this.modifyTemper(integer7);
            }
        }
        if (boolean4) {
            this.eating();
        }
        return boolean4;
    }
    
    protected void doPlayerRide(final Player bft) {
        this.setEating(false);
        this.setStanding(false);
        if (!this.level.isClientSide) {
            bft.yRot = this.yRot;
            bft.xRot = this.xRot;
            bft.startRiding(this);
        }
    }
    
    protected boolean isImmobile() {
        return (super.isImmobile() && this.isVehicle() && this.isSaddled()) || this.isEating() || this.isStanding();
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return AbstractHorse.FOOD_ITEMS.test(bly);
    }
    
    private void moveTail() {
        this.tailCounter = 1;
    }
    
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.inventory == null) {
            return;
        }
        for (int integer2 = 0; integer2 < this.inventory.getContainerSize(); ++integer2) {
            final ItemStack bly3 = this.inventory.getItem(integer2);
            if (!bly3.isEmpty()) {
                if (!EnchantmentHelper.hasVanishingCurse(bly3)) {
                    this.spawnAtLocation(bly3);
                }
            }
        }
    }
    
    @Override
    public void aiStep() {
        if (this.random.nextInt(200) == 0) {
            this.moveTail();
        }
        super.aiStep();
        if (this.level.isClientSide || !this.isAlive()) {
            return;
        }
        if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
            this.heal(1.0f);
        }
        if (this.canEatGrass()) {
            if (!this.isEating() && !this.isVehicle() && this.random.nextInt(300) == 0 && this.level.getBlockState(this.blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
                this.setEating(true);
            }
            if (this.isEating() && ++this.eatingCounter > 50) {
                this.eatingCounter = 0;
                this.setEating(false);
            }
        }
        this.followMommy();
    }
    
    protected void followMommy() {
        if (this.isBred() && this.isBaby() && !this.isEating()) {
            final LivingEntity aqj2 = this.level.<LivingEntity>getNearestEntity((java.lang.Class<? extends LivingEntity>)AbstractHorse.class, AbstractHorse.MOMMY_TARGETING, (LivingEntity)this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(16.0));
            if (aqj2 != null && this.distanceToSqr(aqj2) > 4.0) {
                this.navigation.createPath(aqj2, 0);
            }
        }
    }
    
    public boolean canEatGrass() {
        return true;
    }
    
    public void tick() {
        super.tick();
        if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
            this.mouthCounter = 0;
            this.setFlag(64, false);
        }
        if ((this.isControlledByLocalInstance() || this.isEffectiveAi()) && this.standCounter > 0 && ++this.standCounter > 20) {
            this.standCounter = 0;
            this.setStanding(false);
        }
        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }
        if (this.sprintCounter > 0) {
            ++this.sprintCounter;
            if (this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }
        }
        this.eatAnimO = this.eatAnim;
        if (this.isEating()) {
            this.eatAnim += (1.0f - this.eatAnim) * 0.4f + 0.05f;
            if (this.eatAnim > 1.0f) {
                this.eatAnim = 1.0f;
            }
        }
        else {
            this.eatAnim += (0.0f - this.eatAnim) * 0.4f - 0.05f;
            if (this.eatAnim < 0.0f) {
                this.eatAnim = 0.0f;
            }
        }
        this.standAnimO = this.standAnim;
        if (this.isStanding()) {
            this.eatAnim = 0.0f;
            this.eatAnimO = this.eatAnim;
            this.standAnim += (1.0f - this.standAnim) * 0.4f + 0.05f;
            if (this.standAnim > 1.0f) {
                this.standAnim = 1.0f;
            }
        }
        else {
            this.allowStandSliding = false;
            this.standAnim += (0.8f * this.standAnim * this.standAnim * this.standAnim - this.standAnim) * 0.6f - 0.05f;
            if (this.standAnim < 0.0f) {
                this.standAnim = 0.0f;
            }
        }
        this.mouthAnimO = this.mouthAnim;
        if (this.getFlag(64)) {
            this.mouthAnim += (1.0f - this.mouthAnim) * 0.7f + 0.05f;
            if (this.mouthAnim > 1.0f) {
                this.mouthAnim = 1.0f;
            }
        }
        else {
            this.mouthAnim += (0.0f - this.mouthAnim) * 0.7f - 0.05f;
            if (this.mouthAnim < 0.0f) {
                this.mouthAnim = 0.0f;
            }
        }
    }
    
    private void openMouth() {
        if (!this.level.isClientSide) {
            this.mouthCounter = 1;
            this.setFlag(64, true);
        }
    }
    
    public void setEating(final boolean boolean1) {
        this.setFlag(16, boolean1);
    }
    
    public void setStanding(final boolean boolean1) {
        if (boolean1) {
            this.setEating(false);
        }
        this.setFlag(32, boolean1);
    }
    
    private void stand() {
        if (this.isControlledByLocalInstance() || this.isEffectiveAi()) {
            this.standCounter = 1;
            this.setStanding(true);
        }
    }
    
    public void makeMad() {
        if (!this.isStanding()) {
            this.stand();
            final SoundEvent adn2 = this.getAngrySound();
            if (adn2 != null) {
                this.playSound(adn2, this.getSoundVolume(), this.getVoicePitch());
            }
        }
    }
    
    public boolean tameWithName(final Player bft) {
        this.setOwnerUUID(bft.getUUID());
        this.setTamed(true);
        if (bft instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)bft, this);
        }
        this.level.broadcastEntityEvent(this, (byte)7);
        return true;
    }
    
    public void travel(final Vec3 dck) {
        if (!this.isAlive()) {
            return;
        }
        if (!this.isVehicle() || !this.canBeControlledByRider() || !this.isSaddled()) {
            this.flyingSpeed = 0.02f;
            super.travel(dck);
            return;
        }
        final LivingEntity aqj3 = (LivingEntity)this.getControllingPassenger();
        this.yRot = aqj3.yRot;
        this.yRotO = this.yRot;
        this.xRot = aqj3.xRot * 0.5f;
        this.setRot(this.yRot, this.xRot);
        this.yBodyRot = this.yRot;
        this.yHeadRot = this.yBodyRot;
        float float4 = aqj3.xxa * 0.5f;
        float float5 = aqj3.zza;
        if (float5 <= 0.0f) {
            float5 *= 0.25f;
            this.gallopSoundCounter = 0;
        }
        if (this.onGround && this.playerJumpPendingScale == 0.0f && this.isStanding() && !this.allowStandSliding) {
            float4 = 0.0f;
            float5 = 0.0f;
        }
        if (this.playerJumpPendingScale > 0.0f && !this.isJumping() && this.onGround) {
            final double double6 = this.getCustomJump() * this.playerJumpPendingScale * this.getBlockJumpFactor();
            double double7;
            if (this.hasEffect(MobEffects.JUMP)) {
                double7 = double6 + (this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1f;
            }
            else {
                double7 = double6;
            }
            final Vec3 dck2 = this.getDeltaMovement();
            this.setDeltaMovement(dck2.x, double7, dck2.z);
            this.setIsJumping(true);
            this.hasImpulse = true;
            if (float5 > 0.0f) {
                final float float6 = Mth.sin(this.yRot * 0.017453292f);
                final float float7 = Mth.cos(this.yRot * 0.017453292f);
                this.setDeltaMovement(this.getDeltaMovement().add(-0.4f * float6 * this.playerJumpPendingScale, 0.0, 0.4f * float7 * this.playerJumpPendingScale));
            }
            this.playerJumpPendingScale = 0.0f;
        }
        this.flyingSpeed = this.getSpeed() * 0.1f;
        if (this.isControlledByLocalInstance()) {
            this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            super.travel(new Vec3(float4, dck.y, float5));
        }
        else if (aqj3 instanceof Player) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        if (this.onGround) {
            this.playerJumpPendingScale = 0.0f;
            this.setIsJumping(false);
        }
        this.calculateEntityAnimation(this, false);
    }
    
    protected void playJumpSound() {
        this.playSound(SoundEvents.HORSE_JUMP, 0.4f, 1.0f);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("EatingHaystack", this.isEating());
        md.putBoolean("Bred", this.isBred());
        md.putInt("Temper", this.getTemper());
        md.putBoolean("Tame", this.isTamed());
        if (this.getOwnerUUID() != null) {
            md.putUUID("Owner", this.getOwnerUUID());
        }
        if (!this.inventory.getItem(0).isEmpty()) {
            md.put("SaddleItem", (Tag)this.inventory.getItem(0).save(new CompoundTag()));
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setEating(md.getBoolean("EatingHaystack"));
        this.setBred(md.getBoolean("Bred"));
        this.setTemper(md.getInt("Temper"));
        this.setTamed(md.getBoolean("Tame"));
        UUID uUID3;
        if (md.hasUUID("Owner")) {
            uUID3 = md.getUUID("Owner");
        }
        else {
            final String string4 = md.getString("Owner");
            uUID3 = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string4);
        }
        if (uUID3 != null) {
            this.setOwnerUUID(uUID3);
        }
        if (md.contains("SaddleItem", 10)) {
            final ItemStack bly4 = ItemStack.of(md.getCompound("SaddleItem"));
            if (bly4.getItem() == Items.SADDLE) {
                this.inventory.setItem(0, bly4);
            }
        }
        this.updateContainerEquipment();
    }
    
    @Override
    public boolean canMate(final Animal azw) {
        return false;
    }
    
    protected boolean canParent() {
        return !this.isVehicle() && !this.isPassenger() && this.isTamed() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return null;
    }
    
    protected void setOffspringAttributes(final AgableMob apv, final AbstractHorse bay) {
        final double double4 = this.getAttributeBaseValue(Attributes.MAX_HEALTH) + apv.getAttributeBaseValue(Attributes.MAX_HEALTH) + this.generateRandomMaxHealth();
        bay.getAttribute(Attributes.MAX_HEALTH).setBaseValue(double4 / 3.0);
        final double double5 = this.getAttributeBaseValue(Attributes.JUMP_STRENGTH) + apv.getAttributeBaseValue(Attributes.JUMP_STRENGTH) + this.generateRandomJumpStrength();
        bay.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(double5 / 3.0);
        final double double6 = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + apv.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + this.generateRandomSpeed();
        bay.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(double6 / 3.0);
    }
    
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }
    
    public float getEatAnim(final float float1) {
        return Mth.lerp(float1, this.eatAnimO, this.eatAnim);
    }
    
    public float getStandAnim(final float float1) {
        return Mth.lerp(float1, this.standAnimO, this.standAnim);
    }
    
    public float getMouthAnim(final float float1) {
        return Mth.lerp(float1, this.mouthAnimO, this.mouthAnim);
    }
    
    @Override
    public void onPlayerJump(int integer) {
        if (!this.isSaddled()) {
            return;
        }
        if (integer < 0) {
            integer = 0;
        }
        else {
            this.allowStandSliding = true;
            this.stand();
        }
        if (integer >= 90) {
            this.playerJumpPendingScale = 1.0f;
        }
        else {
            this.playerJumpPendingScale = 0.4f + 0.4f * integer / 90.0f;
        }
    }
    
    @Override
    public boolean canJump() {
        return this.isSaddled();
    }
    
    @Override
    public void handleStartJump(final int integer) {
        this.allowStandSliding = true;
        this.stand();
        this.playJumpSound();
    }
    
    @Override
    public void handleStopJump() {
    }
    
    protected void spawnTamingParticles(final boolean boolean1) {
        final ParticleOptions hf3 = boolean1 ? ParticleTypes.HEART : ParticleTypes.SMOKE;
        for (int integer4 = 0; integer4 < 7; ++integer4) {
            final double double5 = this.random.nextGaussian() * 0.02;
            final double double6 = this.random.nextGaussian() * 0.02;
            final double double7 = this.random.nextGaussian() * 0.02;
            this.level.addParticle(hf3, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), double5, double6, double7);
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 7) {
            this.spawnTamingParticles(true);
        }
        else if (byte1 == 6) {
            this.spawnTamingParticles(false);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public void positionRider(final Entity apx) {
        super.positionRider(apx);
        if (apx instanceof Mob) {
            final Mob aqk3 = (Mob)apx;
            this.yBodyRot = aqk3.yBodyRot;
        }
        if (this.standAnimO > 0.0f) {
            final float float3 = Mth.sin(this.yBodyRot * 0.017453292f);
            final float float4 = Mth.cos(this.yBodyRot * 0.017453292f);
            final float float5 = 0.7f * this.standAnimO;
            final float float6 = 0.15f * this.standAnimO;
            apx.setPos(this.getX() + float5 * float3, this.getY() + this.getPassengersRidingOffset() + apx.getMyRidingOffset() + float6, this.getZ() - float5 * float4);
            if (apx instanceof LivingEntity) {
                ((LivingEntity)apx).yBodyRot = this.yBodyRot;
            }
        }
    }
    
    protected float generateRandomMaxHealth() {
        return 15.0f + this.random.nextInt(8) + this.random.nextInt(9);
    }
    
    protected double generateRandomJumpStrength() {
        return 0.4000000059604645 + this.random.nextDouble() * 0.2 + this.random.nextDouble() * 0.2 + this.random.nextDouble() * 0.2;
    }
    
    protected double generateRandomSpeed() {
        return (0.44999998807907104 + this.random.nextDouble() * 0.3 + this.random.nextDouble() * 0.3 + this.random.nextDouble() * 0.3) * 0.25;
    }
    
    public boolean onClimbable() {
        return false;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.95f;
    }
    
    public boolean canWearArmor() {
        return false;
    }
    
    public boolean isWearingArmor() {
        return !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }
    
    public boolean isArmor(final ItemStack bly) {
        return false;
    }
    
    public boolean setSlot(final int integer, final ItemStack bly) {
        final int integer2 = integer - 400;
        if (integer2 >= 0 && integer2 < 2 && integer2 < this.inventory.getContainerSize()) {
            if (integer2 == 0 && bly.getItem() != Items.SADDLE) {
                return false;
            }
            if (integer2 == 1 && (!this.canWearArmor() || !this.isArmor(bly))) {
                return false;
            }
            this.inventory.setItem(integer2, bly);
            this.updateContainerEquipment();
            return true;
        }
        else {
            final int integer3 = integer - 500 + 2;
            if (integer3 >= 2 && integer3 < this.inventory.getContainerSize()) {
                this.inventory.setItem(integer3, bly);
                return true;
            }
            return false;
        }
    }
    
    @Nullable
    public Entity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        return (Entity)this.getPassengers().get(0);
    }
    
    @Nullable
    private Vec3 getDismountLocationInDirection(final Vec3 dck, final LivingEntity aqj) {
        final double double4 = this.getX() + dck.x;
        final double double5 = this.getBoundingBox().minY;
        final double double6 = this.getZ() + dck.z;
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        for (final Pose aqu12 : aqj.getDismountPoses()) {
            a10.set(double4, double5, double6);
            final double double7 = this.getBoundingBox().maxY + 0.75;
            do {
                final double double8 = this.level.getBlockFloorHeight(a10);
                if (a10.getY() + double8 > double7) {
                    break;
                }
                if (DismountHelper.isBlockFloorValid(double8)) {
                    final AABB dcf17 = aqj.getLocalBoundsForPose(aqu12);
                    final Vec3 dck2 = new Vec3(double4, a10.getY() + double8, double6);
                    if (DismountHelper.canDismountTo(this.level, aqj, dcf17.move(dck2))) {
                        aqj.setPose(aqu12);
                        return dck2;
                    }
                }
                a10.move(Direction.UP);
            } while (a10.getY() < double7);
        }
        return null;
    }
    
    public Vec3 getDismountLocationForPassenger(final LivingEntity aqj) {
        final Vec3 dck3 = Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), this.yRot + ((aqj.getMainArm() == HumanoidArm.RIGHT) ? 90.0f : -90.0f));
        final Vec3 dck4 = this.getDismountLocationInDirection(dck3, aqj);
        if (dck4 != null) {
            return dck4;
        }
        final Vec3 dck5 = Entity.getCollisionHorizontalEscapeVector(this.getBbWidth(), aqj.getBbWidth(), this.yRot + ((aqj.getMainArm() == HumanoidArm.LEFT) ? 90.0f : -90.0f));
        final Vec3 dck6 = this.getDismountLocationInDirection(dck5, aqj);
        if (dck6 != null) {
            return dck6;
        }
        return this.position();
    }
    
    protected void randomizeAttributes() {
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqz == null) {
            aqz = new AgableMobGroupData(0.2f);
        }
        this.randomizeAttributes();
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    static {
        PARENT_HORSE_SELECTOR = (aqj -> aqj instanceof AbstractHorse && ((AbstractHorse)aqj).isBred());
        MOMMY_TARGETING = new TargetingConditions().range(16.0).allowInvulnerable().allowSameTeam().allowUnseeable().selector(AbstractHorse.PARENT_HORSE_SELECTOR);
        FOOD_ITEMS = Ingredient.of(Items.WHEAT, Items.SUGAR, Blocks.HAY_BLOCK.asItem(), Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
        DATA_ID_FLAGS = SynchedEntityData.<Byte>defineId(AbstractHorse.class, EntityDataSerializers.BYTE);
        DATA_ID_OWNER_UUID = SynchedEntityData.<Optional<UUID>>defineId(AbstractHorse.class, EntityDataSerializers.OPTIONAL_UUID);
    }
}
