package net.minecraft.world.entity.animal;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.GameRules;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.control.MoveControl;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.minecraft.world.level.biome.Biomes;
import java.util.Arrays;
import java.util.Map;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.ListTag;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.function.Predicate;
import java.util.UUID;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Fox extends Animal {
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_0;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_1;
    private static final Predicate<ItemEntity> ALLOWED_ITEMS;
    private static final Predicate<Entity> TRUSTED_TARGET_SELECTOR;
    private static final Predicate<Entity> STALKABLE_PREY;
    private static final Predicate<Entity> AVOID_PLAYERS;
    private Goal landTargetGoal;
    private Goal turtleEggTargetGoal;
    private Goal fishTargetGoal;
    private float interestedAngle;
    private float interestedAngleO;
    private float crouchAmount;
    private float crouchAmountO;
    private int ticksSinceEaten;
    
    public Fox(final EntityType<? extends Fox> aqb, final Level bru) {
        super(aqb, bru);
        this.lookControl = new FoxLookControl();
        this.moveControl = new FoxMoveControl();
        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0f);
        this.setCanPickUpLoot(true);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Optional<UUID>>define(Fox.DATA_TRUSTED_ID_0, (Optional<UUID>)Optional.empty());
        this.entityData.<Optional<UUID>>define(Fox.DATA_TRUSTED_ID_1, (Optional<UUID>)Optional.empty());
        this.entityData.<Integer>define(Fox.DATA_TYPE_ID, 0);
        this.entityData.<Byte>define(Fox.DATA_FLAGS_ID, (Byte)0);
    }
    
    @Override
    protected void registerGoals() {
        this.landTargetGoal = new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, (Predicate<LivingEntity>)(aqj -> aqj instanceof Chicken || aqj instanceof Rabbit));
        this.turtleEggTargetGoal = new NearestAttackableTargetGoal<>(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR);
        this.fishTargetGoal = new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, (Predicate<LivingEntity>)(aqj -> aqj instanceof AbstractSchoolingFish));
        this.goalSelector.addGoal(0, new FoxFloatGoal());
        this.goalSelector.addGoal(1, new FaceplantGoal());
        this.goalSelector.addGoal(2, new FoxPanicGoal(2.2));
        this.goalSelector.addGoal(3, new FoxBreedGoal(1.0));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16.0f, 1.6, 1.4, (Predicate<LivingEntity>)(aqj -> Fox.AVOID_PLAYERS.test(aqj) && !this.trusts(aqj.getUUID()) && !this.isDefending())));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Wolf.class, 8.0f, 1.6, 1.4, (Predicate<LivingEntity>)(aqj -> !((Wolf)aqj).isTame() && !this.isDefending())));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PolarBear.class, 8.0f, 1.6, 1.4, (Predicate<LivingEntity>)(aqj -> !this.isDefending())));
        this.goalSelector.addGoal(5, new StalkPreyGoal());
        this.goalSelector.addGoal(6, new FoxPounceGoal());
        this.goalSelector.addGoal(6, new SeekShelterGoal(1.25));
        this.goalSelector.addGoal(7, new FoxMeleeAttackGoal(1.2000000476837158, true));
        this.goalSelector.addGoal(7, new SleepGoal());
        this.goalSelector.addGoal(8, new FoxFollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(9, new FoxStrollThroughVillageGoal(32, 200));
        this.goalSelector.addGoal(10, new FoxEatBerriesGoal(1.2000000476837158, 12, 2));
        this.goalSelector.addGoal(10, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(11, new FoxSearchForItemsGoal());
        this.goalSelector.addGoal(12, new FoxLookAtPlayerGoal(this, Player.class, 24.0f));
        this.goalSelector.addGoal(13, new PerchAndSearchGoal());
        this.targetSelector.addGoal(3, new DefendTrustedTargetGoal(LivingEntity.class, false, false, (Predicate<LivingEntity>)(aqj -> Fox.TRUSTED_TARGET_SELECTOR.test(aqj) && !this.trusts(aqj.getUUID()))));
    }
    
    @Override
    public SoundEvent getEatingSound(final ItemStack bly) {
        return SoundEvents.FOX_EAT;
    }
    
    @Override
    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive() && this.isEffectiveAi()) {
            ++this.ticksSinceEaten;
            final ItemStack bly2 = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (this.canEat(bly2)) {
                if (this.ticksSinceEaten > 600) {
                    final ItemStack bly3 = bly2.finishUsingItem(this.level, this);
                    if (!bly3.isEmpty()) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, bly3);
                    }
                    this.ticksSinceEaten = 0;
                }
                else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1f) {
                    this.playSound(this.getEatingSound(bly2), 1.0f, 1.0f);
                    this.level.broadcastEntityEvent(this, (byte)45);
                }
            }
            final LivingEntity aqj3 = this.getTarget();
            if (aqj3 == null || !aqj3.isAlive()) {
                this.setIsCrouching(false);
                this.setIsInterested(false);
            }
        }
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0f;
            this.zza = 0.0f;
        }
        super.aiStep();
        if (this.isDefending() && this.random.nextFloat() < 0.05f) {
            this.playSound(SoundEvents.FOX_AGGRO, 1.0f, 1.0f);
        }
    }
    
    @Override
    protected boolean isImmobile() {
        return this.isDeadOrDying();
    }
    
    private boolean canEat(final ItemStack bly) {
        return bly.getItem().isEdible() && this.getTarget() == null && this.onGround && !this.isSleeping();
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        if (this.random.nextFloat() < 0.2f) {
            final float float3 = this.random.nextFloat();
            ItemStack bly4;
            if (float3 < 0.05f) {
                bly4 = new ItemStack(Items.EMERALD);
            }
            else if (float3 < 0.2f) {
                bly4 = new ItemStack(Items.EGG);
            }
            else if (float3 < 0.4f) {
                bly4 = (this.random.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE));
            }
            else if (float3 < 0.6f) {
                bly4 = new ItemStack(Items.WHEAT);
            }
            else if (float3 < 0.8f) {
                bly4 = new ItemStack(Items.LEATHER);
            }
            else {
                bly4 = new ItemStack(Items.FEATHER);
            }
            this.setItemSlot(EquipmentSlot.MAINHAND, bly4);
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 45) {
            final ItemStack bly3 = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!bly3.isEmpty()) {
                for (int integer4 = 0; integer4 < 8; ++integer4) {
                    final Vec3 dck5 = new Vec3((this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0).xRot(-this.xRot * 0.017453292f).yRot(-this.yRot * 0.017453292f);
                    this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, bly3), this.getX() + this.getLookAngle().x / 2.0, this.getY(), this.getZ() + this.getLookAngle().z / 2.0, dck5.x, dck5.y + 0.05, dck5.z);
                }
            }
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    
    @Override
    public Fox getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Fox bae4 = EntityType.FOX.create(aag);
        bae4.setFoxType(this.random.nextBoolean() ? this.getFoxType() : ((Fox)apv).getFoxType());
        return bae4;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        final Optional<ResourceKey<Biome>> optional7 = bsh.getBiomeName(this.blockPosition());
        Type v8 = Type.byBiome(optional7);
        boolean boolean9 = false;
        if (aqz instanceof FoxGroupData) {
            v8 = ((FoxGroupData)aqz).type;
            if (((FoxGroupData)aqz).getGroupSize() >= 2) {
                boolean9 = true;
            }
        }
        else {
            aqz = new FoxGroupData(v8);
        }
        this.setFoxType(v8);
        if (boolean9) {
            this.setAge(-24000);
        }
        if (bsh instanceof ServerLevel) {
            this.setTargetGoals();
        }
        this.populateDefaultEquipmentSlots(aop);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    private void setTargetGoals() {
        if (this.getFoxType() == Type.RED) {
            this.targetSelector.addGoal(4, this.landTargetGoal);
            this.targetSelector.addGoal(4, this.turtleEggTargetGoal);
            this.targetSelector.addGoal(6, this.fishTargetGoal);
        }
        else {
            this.targetSelector.addGoal(4, this.fishTargetGoal);
            this.targetSelector.addGoal(6, this.landTargetGoal);
            this.targetSelector.addGoal(6, this.turtleEggTargetGoal);
        }
    }
    
    @Override
    protected void usePlayerItem(final Player bft, final ItemStack bly) {
        if (this.isFood(bly)) {
            this.playSound(this.getEatingSound(bly), 1.0f, 1.0f);
        }
        super.usePlayerItem(bft, bly);
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        if (this.isBaby()) {
            return apy.height * 0.85f;
        }
        return 0.4f;
    }
    
    public Type getFoxType() {
        return Type.byId(this.entityData.<Integer>get(Fox.DATA_TYPE_ID));
    }
    
    private void setFoxType(final Type v) {
        this.entityData.<Integer>set(Fox.DATA_TYPE_ID, v.getId());
    }
    
    private List<UUID> getTrustedUUIDs() {
        final List<UUID> list2 = (List<UUID>)Lists.newArrayList();
        list2.add(this.entityData.<Optional<UUID>>get(Fox.DATA_TRUSTED_ID_0).orElse(null));
        list2.add(this.entityData.<Optional<UUID>>get(Fox.DATA_TRUSTED_ID_1).orElse(null));
        return list2;
    }
    
    private void addTrustedUUID(@Nullable final UUID uUID) {
        if (this.entityData.<Optional<UUID>>get(Fox.DATA_TRUSTED_ID_0).isPresent()) {
            this.entityData.<Optional<UUID>>set(Fox.DATA_TRUSTED_ID_1, (Optional<UUID>)Optional.ofNullable(uUID));
        }
        else {
            this.entityData.<Optional<UUID>>set(Fox.DATA_TRUSTED_ID_0, (Optional<UUID>)Optional.ofNullable(uUID));
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        final List<UUID> list3 = this.getTrustedUUIDs();
        final ListTag mj4 = new ListTag();
        for (final UUID uUID6 : list3) {
            if (uUID6 != null) {
                mj4.add(NbtUtils.createUUID(uUID6));
            }
        }
        md.put("Trusted", (Tag)mj4);
        md.putBoolean("Sleeping", this.isSleeping());
        md.putString("Type", this.getFoxType().getName());
        md.putBoolean("Sitting", this.isSitting());
        md.putBoolean("Crouching", this.isCrouching());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        final ListTag mj3 = md.getList("Trusted", 11);
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            this.addTrustedUUID(NbtUtils.loadUUID(mj3.get(integer4)));
        }
        this.setSleeping(md.getBoolean("Sleeping"));
        this.setFoxType(Type.byName(md.getString("Type")));
        this.setSitting(md.getBoolean("Sitting"));
        this.setIsCrouching(md.getBoolean("Crouching"));
        if (this.level instanceof ServerLevel) {
            this.setTargetGoals();
        }
    }
    
    public boolean isSitting() {
        return this.getFlag(1);
    }
    
    public void setSitting(final boolean boolean1) {
        this.setFlag(1, boolean1);
    }
    
    public boolean isFaceplanted() {
        return this.getFlag(64);
    }
    
    private void setFaceplanted(final boolean boolean1) {
        this.setFlag(64, boolean1);
    }
    
    private boolean isDefending() {
        return this.getFlag(128);
    }
    
    private void setDefending(final boolean boolean1) {
        this.setFlag(128, boolean1);
    }
    
    @Override
    public boolean isSleeping() {
        return this.getFlag(32);
    }
    
    private void setSleeping(final boolean boolean1) {
        this.setFlag(32, boolean1);
    }
    
    private void setFlag(final int integer, final boolean boolean2) {
        if (boolean2) {
            this.entityData.<Byte>set(Fox.DATA_FLAGS_ID, (byte)(this.entityData.<Byte>get(Fox.DATA_FLAGS_ID) | integer));
        }
        else {
            this.entityData.<Byte>set(Fox.DATA_FLAGS_ID, (byte)(this.entityData.<Byte>get(Fox.DATA_FLAGS_ID) & ~integer));
        }
    }
    
    private boolean getFlag(final int integer) {
        return (this.entityData.<Byte>get(Fox.DATA_FLAGS_ID) & integer) != 0x0;
    }
    
    @Override
    public boolean canTakeItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = Mob.getEquipmentSlotForItem(bly);
        return this.getItemBySlot(aqc3).isEmpty() && aqc3 == EquipmentSlot.MAINHAND && super.canTakeItem(bly);
    }
    
    @Override
    public boolean canHoldItem(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        final ItemStack bly2 = this.getItemBySlot(EquipmentSlot.MAINHAND);
        return bly2.isEmpty() || (this.ticksSinceEaten > 0 && blu3.isEdible() && !bly2.getItem().isEdible());
    }
    
    private void spitOutItem(final ItemStack bly) {
        if (bly.isEmpty() || this.level.isClientSide) {
            return;
        }
        final ItemEntity bcs3 = new ItemEntity(this.level, this.getX() + this.getLookAngle().x, this.getY() + 1.0, this.getZ() + this.getLookAngle().z, bly);
        bcs3.setPickUpDelay(40);
        bcs3.setThrower(this.getUUID());
        this.playSound(SoundEvents.FOX_SPIT, 1.0f, 1.0f);
        this.level.addFreshEntity(bcs3);
    }
    
    private void dropItemStack(final ItemStack bly) {
        final ItemEntity bcs3 = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), bly);
        this.level.addFreshEntity(bcs3);
    }
    
    @Override
    protected void pickUpItem(final ItemEntity bcs) {
        final ItemStack bly3 = bcs.getItem();
        if (this.canHoldItem(bly3)) {
            final int integer4 = bly3.getCount();
            if (integer4 > 1) {
                this.dropItemStack(bly3.split(integer4 - 1));
            }
            this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
            this.onItemPickup(bcs);
            this.setItemSlot(EquipmentSlot.MAINHAND, bly3.split(1));
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0f;
            this.take(bcs, bly3.getCount());
            bcs.remove();
            this.ticksSinceEaten = 0;
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            final boolean boolean2 = this.isInWater();
            if (boolean2 || this.getTarget() != null || this.level.isThundering()) {
                this.wakeUp();
            }
            if (boolean2 || this.isSleeping()) {
                this.setSitting(false);
            }
            if (this.isFaceplanted() && this.level.random.nextFloat() < 0.2f) {
                final BlockPos fx3 = this.blockPosition();
                final BlockState cee4 = this.level.getBlockState(fx3);
                this.level.levelEvent(2001, fx3, Block.getId(cee4));
            }
        }
        this.interestedAngleO = this.interestedAngle;
        if (this.isInterested()) {
            this.interestedAngle += (1.0f - this.interestedAngle) * 0.4f;
        }
        else {
            this.interestedAngle += (0.0f - this.interestedAngle) * 0.4f;
        }
        this.crouchAmountO = this.crouchAmount;
        if (this.isCrouching()) {
            this.crouchAmount += 0.2f;
            if (this.crouchAmount > 3.0f) {
                this.crouchAmount = 3.0f;
            }
        }
        else {
            this.crouchAmount = 0.0f;
        }
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return bly.getItem() == Items.SWEET_BERRIES;
    }
    
    @Override
    protected void onOffspringSpawnedFromEgg(final Player bft, final Mob aqk) {
        ((Fox)aqk).addTrustedUUID(bft.getUUID());
    }
    
    public boolean isPouncing() {
        return this.getFlag(16);
    }
    
    public void setIsPouncing(final boolean boolean1) {
        this.setFlag(16, boolean1);
    }
    
    public boolean isFullyCrouched() {
        return this.crouchAmount == 3.0f;
    }
    
    public void setIsCrouching(final boolean boolean1) {
        this.setFlag(4, boolean1);
    }
    
    @Override
    public boolean isCrouching() {
        return this.getFlag(4);
    }
    
    public void setIsInterested(final boolean boolean1) {
        this.setFlag(8, boolean1);
    }
    
    public boolean isInterested() {
        return this.getFlag(8);
    }
    
    public float getHeadRollAngle(final float float1) {
        return Mth.lerp(float1, this.interestedAngleO, this.interestedAngle) * 0.11f * 3.1415927f;
    }
    
    public float getCrouchAmount(final float float1) {
        return Mth.lerp(float1, this.crouchAmountO, this.crouchAmount);
    }
    
    @Override
    public void setTarget(@Nullable final LivingEntity aqj) {
        if (this.isDefending() && aqj == null) {
            this.setDefending(false);
        }
        super.setTarget(aqj);
    }
    
    @Override
    protected int calculateFallDamage(final float float1, final float float2) {
        return Mth.ceil((float1 - 5.0f) * float2);
    }
    
    private void wakeUp() {
        this.setSleeping(false);
    }
    
    private void clearStates() {
        this.setIsInterested(false);
        this.setIsCrouching(false);
        this.setSitting(false);
        this.setSleeping(false);
        this.setDefending(false);
        this.setFaceplanted(false);
    }
    
    private boolean canMove() {
        return !this.isSleeping() && !this.isSitting() && !this.isFaceplanted();
    }
    
    @Override
    public void playAmbientSound() {
        final SoundEvent adn2 = this.getAmbientSound();
        if (adn2 == SoundEvents.FOX_SCREECH) {
            this.playSound(adn2, 2.0f, this.getVoicePitch());
        }
        else {
            super.playAmbientSound();
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundEvents.FOX_SLEEP;
        }
        if (!this.level.isDay() && this.random.nextFloat() < 0.1f) {
            final List<Player> list2 = this.level.<Player>getEntitiesOfClass((java.lang.Class<? extends Player>)Player.class, this.getBoundingBox().inflate(16.0, 16.0, 16.0), (java.util.function.Predicate<? super Player>)EntitySelector.NO_SPECTATORS);
            if (list2.isEmpty()) {
                return SoundEvents.FOX_SCREECH;
            }
        }
        return SoundEvents.FOX_AMBIENT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.FOX_HURT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }
    
    private boolean trusts(final UUID uUID) {
        return this.getTrustedUUIDs().contains(uUID);
    }
    
    @Override
    protected void dropAllDeathLoot(final DamageSource aph) {
        final ItemStack bly3 = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!bly3.isEmpty()) {
            this.spawnAtLocation(bly3);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        super.dropAllDeathLoot(aph);
    }
    
    public static boolean isPathClear(final Fox bae, final LivingEntity aqj) {
        final double double3 = aqj.getZ() - bae.getZ();
        final double double4 = aqj.getX() - bae.getX();
        final double double5 = double3 / double4;
        final int integer9 = 6;
        for (int integer10 = 0; integer10 < 6; ++integer10) {
            final double double6 = (double5 == 0.0) ? 0.0 : (double3 * (integer10 / 6.0f));
            final double double7 = (double5 == 0.0) ? (double4 * (integer10 / 6.0f)) : (double6 / double5);
            for (int integer11 = 1; integer11 < 4; ++integer11) {
                if (!bae.level.getBlockState(new BlockPos(bae.getX() + double7, bae.getY() + integer11, bae.getZ() + double6)).getMaterial().isReplaceable()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.55f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        DATA_TYPE_ID = SynchedEntityData.<Integer>defineId(Fox.class, EntityDataSerializers.INT);
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(Fox.class, EntityDataSerializers.BYTE);
        DATA_TRUSTED_ID_0 = SynchedEntityData.<Optional<UUID>>defineId(Fox.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_TRUSTED_ID_1 = SynchedEntityData.<Optional<UUID>>defineId(Fox.class, EntityDataSerializers.OPTIONAL_UUID);
        ALLOWED_ITEMS = (bcs -> !bcs.hasPickUpDelay() && bcs.isAlive());
        TRUSTED_TARGET_SELECTOR = (apx -> {
            if (apx instanceof LivingEntity) {
                final LivingEntity aqj2 = (LivingEntity)apx;
                return aqj2.getLastHurtMob() != null && aqj2.getLastHurtMobTimestamp() < aqj2.tickCount + 600;
            }
            return false;
        });
        STALKABLE_PREY = (apx -> apx instanceof Chicken || apx instanceof Rabbit);
        AVOID_PLAYERS = (apx -> !apx.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(apx));
    }
    
    public enum Type {
        RED(0, "red", new ResourceKey[] { Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.TAIGA_MOUNTAINS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.GIANT_SPRUCE_TAIGA_HILLS }), 
        SNOW(1, "snow", new ResourceKey[] { Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS });
        
        private static final Type[] BY_ID;
        private static final Map<String, Type> BY_NAME;
        private final int id;
        private final String name;
        private final List<ResourceKey<Biome>> biomes;
        
        private Type(final int integer3, final String string4, final ResourceKey<Biome>[] arr) {
            this.id = integer3;
            this.name = string4;
            this.biomes = (List<ResourceKey<Biome>>)Arrays.asList((Object[])arr);
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getId() {
            return this.id;
        }
        
        public static Type byName(final String string) {
            return (Type)Type.BY_NAME.getOrDefault(string, Type.RED);
        }
        
        public static Type byId(int integer) {
            if (integer < 0 || integer > Type.BY_ID.length) {
                integer = 0;
            }
            return Type.BY_ID[integer];
        }
        
        public static Type byBiome(final Optional<ResourceKey<Biome>> optional) {
            return (optional.isPresent() && Type.SNOW.biomes.contains(optional.get())) ? Type.SNOW : Type.RED;
        }
        
        static {
            BY_ID = (Type[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(Type::getId)).toArray(Type[]::new);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Type::getName, v -> v));
        }
    }
    
    class FoxSearchForItemsGoal extends Goal {
        public FoxSearchForItemsGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            if (!Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                return false;
            }
            if (Fox.this.getTarget() != null || Fox.this.getLastHurtByMob() != null) {
                return false;
            }
            if (!Fox.this.canMove()) {
                return false;
            }
            if (Fox.this.getRandom().nextInt(10) != 0) {
                return false;
            }
            final List<ItemEntity> list2 = Fox.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Fox.ALLOWED_ITEMS);
            return !list2.isEmpty() && Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
        
        @Override
        public void tick() {
            final List<ItemEntity> list2 = Fox.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Fox.ALLOWED_ITEMS);
            final ItemStack bly3 = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (bly3.isEmpty() && !list2.isEmpty()) {
                Fox.this.getNavigation().moveTo((Entity)list2.get(0), 1.2000000476837158);
            }
        }
        
        @Override
        public void start() {
            final List<ItemEntity> list2 = Fox.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Fox.ALLOWED_ITEMS);
            if (!list2.isEmpty()) {
                Fox.this.getNavigation().moveTo((Entity)list2.get(0), 1.2000000476837158);
            }
        }
    }
    
    class FoxMoveControl extends MoveControl {
        public FoxMoveControl() {
            super(Fox.this);
        }
        
        @Override
        public void tick() {
            if (Fox.this.canMove()) {
                super.tick();
            }
        }
    }
    
    class StalkPreyGoal extends Goal {
        public StalkPreyGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            if (Fox.this.isSleeping()) {
                return false;
            }
            final LivingEntity aqj2 = Fox.this.getTarget();
            return aqj2 != null && aqj2.isAlive() && Fox.STALKABLE_PREY.test(aqj2) && Fox.this.distanceToSqr(aqj2) > 36.0 && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.jumping;
        }
        
        @Override
        public void start() {
            Fox.this.setSitting(false);
            Fox.this.setFaceplanted(false);
        }
        
        @Override
        public void stop() {
            final LivingEntity aqj2 = Fox.this.getTarget();
            if (aqj2 != null && Fox.isPathClear(Fox.this, aqj2)) {
                Fox.this.setIsInterested(true);
                Fox.this.setIsCrouching(true);
                Fox.this.getNavigation().stop();
                Fox.this.getLookControl().setLookAt(aqj2, (float)Fox.this.getMaxHeadYRot(), (float)Fox.this.getMaxHeadXRot());
            }
            else {
                Fox.this.setIsInterested(false);
                Fox.this.setIsCrouching(false);
            }
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = Fox.this.getTarget();
            Fox.this.getLookControl().setLookAt(aqj2, (float)Fox.this.getMaxHeadYRot(), (float)Fox.this.getMaxHeadXRot());
            if (Fox.this.distanceToSqr(aqj2) <= 36.0) {
                Fox.this.setIsInterested(true);
                Fox.this.setIsCrouching(true);
                Fox.this.getNavigation().stop();
            }
            else {
                Fox.this.getNavigation().moveTo(aqj2, 1.5);
            }
        }
    }
    
    class FoxMeleeAttackGoal extends MeleeAttackGoal {
        public FoxMeleeAttackGoal(final double double2, final boolean boolean3) {
            super(Fox.this, double2, boolean3);
        }
        
        @Override
        protected void checkAndPerformAttack(final LivingEntity aqj, final double double2) {
            final double double3 = this.getAttackReachSqr(aqj);
            if (double2 <= double3 && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(aqj);
                Fox.this.playSound(SoundEvents.FOX_BITE, 1.0f, 1.0f);
            }
        }
        
        @Override
        public void start() {
            Fox.this.setIsInterested(false);
            super.start();
        }
        
        @Override
        public boolean canUse() {
            return !Fox.this.isSitting() && !Fox.this.isSleeping() && !Fox.this.isCrouching() && !Fox.this.isFaceplanted() && super.canUse();
        }
    }
    
    class FoxBreedGoal extends BreedGoal {
        public FoxBreedGoal(final double double2) {
            super(Fox.this, double2);
        }
        
        @Override
        public void start() {
            ((Fox)this.animal).clearStates();
            ((Fox)this.partner).clearStates();
            super.start();
        }
        
        @Override
        protected void breed() {
            final ServerLevel aag2 = (ServerLevel)this.level;
            final Fox bae3 = (Fox)this.animal.getBreedOffspring(aag2, this.partner);
            if (bae3 == null) {
                return;
            }
            final ServerPlayer aah4 = this.animal.getLoveCause();
            final ServerPlayer aah5 = this.partner.getLoveCause();
            ServerPlayer aah6;
            if ((aah6 = aah4) != null) {
                bae3.addTrustedUUID(aah4.getUUID());
            }
            else {
                aah6 = aah5;
            }
            if (aah5 != null && aah4 != aah5) {
                bae3.addTrustedUUID(aah5.getUUID());
            }
            if (aah6 != null) {
                aah6.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(aah6, this.animal, this.partner, bae3);
            }
            this.animal.setAge(6000);
            this.partner.setAge(6000);
            this.animal.resetLove();
            this.partner.resetLove();
            bae3.setAge(-24000);
            bae3.moveTo(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0f, 0.0f);
            aag2.addFreshEntityWithPassengers(bae3);
            this.level.broadcastEntityEvent(this.animal, (byte)18);
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1));
            }
        }
    }
    
    class DefendTrustedTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
        @Nullable
        private LivingEntity trustedLastHurtBy;
        private LivingEntity trustedLastHurt;
        private int timestamp;
        
        public DefendTrustedTargetGoal(final Class<LivingEntity> class2, final boolean boolean3, final boolean boolean4, @Nullable final Predicate<LivingEntity> predicate) {
            super(Fox.this, class2, 10, boolean3, boolean4, predicate);
        }
        
        @Override
        public boolean canUse() {
            if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
                return false;
            }
            for (final UUID uUID3 : Fox.this.getTrustedUUIDs()) {
                if (uUID3 != null) {
                    if (!(Fox.this.level instanceof ServerLevel)) {
                        continue;
                    }
                    final Entity apx4 = ((ServerLevel)Fox.this.level).getEntity(uUID3);
                    if (!(apx4 instanceof LivingEntity)) {
                        continue;
                    }
                    final LivingEntity aqj5 = (LivingEntity)apx4;
                    this.trustedLastHurt = aqj5;
                    this.trustedLastHurtBy = aqj5.getLastHurtByMob();
                    final int integer6 = aqj5.getLastHurtByMobTimestamp();
                    return integer6 != this.timestamp && this.canAttack(this.trustedLastHurtBy, this.targetConditions);
                }
            }
            return false;
        }
        
        @Override
        public void start() {
            this.setTarget(this.trustedLastHurtBy);
            this.target = this.trustedLastHurtBy;
            if (this.trustedLastHurt != null) {
                this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp();
            }
            Fox.this.playSound(SoundEvents.FOX_AGGRO, 1.0f, 1.0f);
            Fox.this.setDefending(true);
            Fox.this.wakeUp();
            super.start();
        }
    }
    
    class SeekShelterGoal extends FleeSunGoal {
        private int interval;
        
        public SeekShelterGoal(final double double2) {
            super(Fox.this, double2);
            this.interval = 100;
        }
        
        @Override
        public boolean canUse() {
            if (Fox.this.isSleeping() || this.mob.getTarget() != null) {
                return false;
            }
            if (Fox.this.level.isThundering()) {
                return true;
            }
            if (this.interval > 0) {
                --this.interval;
                return false;
            }
            this.interval = 100;
            final BlockPos fx2 = this.mob.blockPosition();
            return Fox.this.level.isDay() && Fox.this.level.canSeeSky(fx2) && !((ServerLevel)Fox.this.level).isVillage(fx2) && this.setWantedPos();
        }
        
        @Override
        public void start() {
            Fox.this.clearStates();
            super.start();
        }
    }
    
    public class FoxAlertableEntitiesSelector implements Predicate<LivingEntity> {
        public boolean test(final LivingEntity aqj) {
            if (aqj instanceof Fox) {
                return false;
            }
            if (aqj instanceof Chicken || aqj instanceof Rabbit || aqj instanceof Monster) {
                return true;
            }
            if (aqj instanceof TamableAnimal) {
                return !((TamableAnimal)aqj).isTame();
            }
            return (!(aqj instanceof Player) || (!aqj.isSpectator() && !((Player)aqj).isCreative())) && !Fox.this.trusts(aqj.getUUID()) && !aqj.isSleeping() && !aqj.isDiscrete();
        }
    }
    
    abstract class FoxBehaviorGoal extends Goal {
        private final TargetingConditions alertableTargeting;
        
        private FoxBehaviorGoal() {
            this.alertableTargeting = new TargetingConditions().range(12.0).allowUnseeable().selector((Predicate<LivingEntity>)new FoxAlertableEntitiesSelector());
        }
        
        protected boolean hasShelter() {
            final BlockPos fx2 = new BlockPos(Fox.this.getX(), Fox.this.getBoundingBox().maxY, Fox.this.getZ());
            return !Fox.this.level.canSeeSky(fx2) && Fox.this.getWalkTargetValue(fx2) >= 0.0f;
        }
        
        protected boolean alertable() {
            return !Fox.this.level.<LivingEntity>getNearbyEntities((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.alertableTargeting, (LivingEntity)Fox.this, Fox.this.getBoundingBox().inflate(12.0, 6.0, 12.0)).isEmpty();
        }
    }
    
    class SleepGoal extends FoxBehaviorGoal {
        private int countdown;
        
        public SleepGoal() {
            this.countdown = Fox.this.random.nextInt(140);
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK, (Enum)Flag.JUMP));
        }
        
        @Override
        public boolean canUse() {
            return Fox.this.xxa == 0.0f && Fox.this.yya == 0.0f && Fox.this.zza == 0.0f && (this.canSleep() || Fox.this.isSleeping());
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.canSleep();
        }
        
        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            }
            return Fox.this.level.isDay() && this.hasShelter() && !this.alertable();
        }
        
        @Override
        public void stop() {
            this.countdown = Fox.this.random.nextInt(140);
            Fox.this.clearStates();
        }
        
        @Override
        public void start() {
            Fox.this.setSitting(false);
            Fox.this.setIsCrouching(false);
            Fox.this.setIsInterested(false);
            Fox.this.setJumping(false);
            Fox.this.setSleeping(true);
            Fox.this.getNavigation().stop();
            Fox.this.getMoveControl().setWantedPosition(Fox.this.getX(), Fox.this.getY(), Fox.this.getZ(), 0.0);
        }
    }
    
    class PerchAndSearchGoal extends FoxBehaviorGoal {
        private double relX;
        private double relZ;
        private int lookTime;
        private int looksRemaining;
        
        public PerchAndSearchGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return Fox.this.getLastHurtByMob() == null && Fox.this.getRandom().nextFloat() < 0.02f && !Fox.this.isSleeping() && Fox.this.getTarget() == null && Fox.this.getNavigation().isDone() && !this.alertable() && !Fox.this.isPouncing() && !Fox.this.isCrouching();
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.looksRemaining > 0;
        }
        
        @Override
        public void start() {
            this.resetLook();
            this.looksRemaining = 2 + Fox.this.getRandom().nextInt(3);
            Fox.this.setSitting(true);
            Fox.this.getNavigation().stop();
        }
        
        @Override
        public void stop() {
            Fox.this.setSitting(false);
        }
        
        @Override
        public void tick() {
            --this.lookTime;
            if (this.lookTime <= 0) {
                --this.looksRemaining;
                this.resetLook();
            }
            Fox.this.getLookControl().setLookAt(Fox.this.getX() + this.relX, Fox.this.getEyeY(), Fox.this.getZ() + this.relZ, (float)Fox.this.getMaxHeadYRot(), (float)Fox.this.getMaxHeadXRot());
        }
        
        private void resetLook() {
            final double double2 = 6.283185307179586 * Fox.this.getRandom().nextDouble();
            this.relX = Math.cos(double2);
            this.relZ = Math.sin(double2);
            this.lookTime = 80 + Fox.this.getRandom().nextInt(20);
        }
    }
    
    public class FoxEatBerriesGoal extends MoveToBlockGoal {
        protected int ticksWaited;
        
        public FoxEatBerriesGoal(final double double2, final int integer3, final int integer4) {
            super(Fox.this, double2, integer3, integer4);
        }
        
        @Override
        public double acceptedDistance() {
            return 2.0;
        }
        
        @Override
        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }
        
        @Override
        protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
            final BlockState cee4 = brw.getBlockState(fx);
            return cee4.is(Blocks.SWEET_BERRY_BUSH) && cee4.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) >= 2;
        }
        
        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                }
                else {
                    ++this.ticksWaited;
                }
            }
            else if (!this.isReachedTarget() && Fox.this.random.nextFloat() < 0.05f) {
                Fox.this.playSound(SoundEvents.FOX_SNIFF, 1.0f, 1.0f);
            }
            super.tick();
        }
        
        protected void onReachedTarget() {
            if (!Fox.this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return;
            }
            final BlockState cee2 = Fox.this.level.getBlockState(this.blockPos);
            if (!cee2.is(Blocks.SWEET_BERRY_BUSH)) {
                return;
            }
            final int integer3 = cee2.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE);
            ((StateHolder<Object, Object>)cee2).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, 1);
            int integer4 = 1 + Fox.this.level.random.nextInt(2) + ((integer3 == 3) ? 1 : 0);
            final ItemStack bly5 = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (bly5.isEmpty()) {
                Fox.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.SWEET_BERRIES));
                --integer4;
            }
            if (integer4 > 0) {
                Block.popResource(Fox.this.level, this.blockPos, new ItemStack(Items.SWEET_BERRIES, integer4));
            }
            Fox.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0f, 1.0f);
            Fox.this.level.setBlock(this.blockPos, ((StateHolder<O, BlockState>)cee2).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, 1), 2);
        }
        
        @Override
        public boolean canUse() {
            return !Fox.this.isSleeping() && super.canUse();
        }
        
        @Override
        public void start() {
            this.ticksWaited = 0;
            Fox.this.setSitting(false);
            super.start();
        }
    }
    
    public static class FoxGroupData extends AgableMobGroupData {
        public final Type type;
        
        public FoxGroupData(final Type v) {
            super(false);
            this.type = v;
        }
    }
    
    class FaceplantGoal extends Goal {
        int countdown;
        
        public FaceplantGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK, (Enum)Flag.JUMP, (Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return Fox.this.isFaceplanted();
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.canUse() && this.countdown > 0;
        }
        
        @Override
        public void start() {
            this.countdown = 40;
        }
        
        @Override
        public void stop() {
            Fox.this.setFaceplanted(false);
        }
        
        @Override
        public void tick() {
            --this.countdown;
        }
    }
    
    class FoxPanicGoal extends PanicGoal {
        public FoxPanicGoal(final double double2) {
            super(Fox.this, double2);
        }
        
        @Override
        public boolean canUse() {
            return !Fox.this.isDefending() && super.canUse();
        }
    }
    
    class FoxStrollThroughVillageGoal extends StrollThroughVillageGoal {
        public FoxStrollThroughVillageGoal(final int integer2, final int integer3) {
            super(Fox.this, integer3);
        }
        
        @Override
        public void start() {
            Fox.this.clearStates();
            super.start();
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.canFoxMove();
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.canFoxMove();
        }
        
        private boolean canFoxMove() {
            return !Fox.this.isSleeping() && !Fox.this.isSitting() && !Fox.this.isDefending() && Fox.this.getTarget() == null;
        }
    }
    
    class FoxFloatGoal extends FloatGoal {
        public FoxFloatGoal() {
            super(Fox.this);
        }
        
        @Override
        public void start() {
            super.start();
            Fox.this.clearStates();
        }
        
        @Override
        public boolean canUse() {
            return (Fox.this.isInWater() && Fox.this.getFluidHeight(FluidTags.WATER) > 0.25) || Fox.this.isInLava();
        }
    }
    
    public class FoxPounceGoal extends JumpGoal {
        @Override
        public boolean canUse() {
            if (!Fox.this.isFullyCrouched()) {
                return false;
            }
            final LivingEntity aqj2 = Fox.this.getTarget();
            if (aqj2 == null || !aqj2.isAlive()) {
                return false;
            }
            if (aqj2.getMotionDirection() != aqj2.getDirection()) {
                return false;
            }
            final boolean boolean3 = Fox.isPathClear(Fox.this, aqj2);
            if (!boolean3) {
                Fox.this.getNavigation().createPath(aqj2, 0);
                Fox.this.setIsCrouching(false);
                Fox.this.setIsInterested(false);
            }
            return boolean3;
        }
        
        @Override
        public boolean canContinueToUse() {
            final LivingEntity aqj2 = Fox.this.getTarget();
            if (aqj2 == null || !aqj2.isAlive()) {
                return false;
            }
            final double double3 = Fox.this.getDeltaMovement().y;
            return (double3 * double3 >= 0.05000000074505806 || Math.abs(Fox.this.xRot) >= 15.0f || !Fox.this.onGround) && !Fox.this.isFaceplanted();
        }
        
        @Override
        public boolean isInterruptable() {
            return false;
        }
        
        @Override
        public void start() {
            Fox.this.setJumping(true);
            Fox.this.setIsPouncing(true);
            Fox.this.setIsInterested(false);
            final LivingEntity aqj2 = Fox.this.getTarget();
            Fox.this.getLookControl().setLookAt(aqj2, 60.0f, 30.0f);
            final Vec3 dck3 = new Vec3(aqj2.getX() - Fox.this.getX(), aqj2.getY() - Fox.this.getY(), aqj2.getZ() - Fox.this.getZ()).normalize();
            Fox.this.setDeltaMovement(Fox.this.getDeltaMovement().add(dck3.x * 0.8, 0.9, dck3.z * 0.8));
            Fox.this.getNavigation().stop();
        }
        
        @Override
        public void stop() {
            Fox.this.setIsCrouching(false);
            Fox.this.crouchAmount = 0.0f;
            Fox.this.crouchAmountO = 0.0f;
            Fox.this.setIsInterested(false);
            Fox.this.setIsPouncing(false);
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = Fox.this.getTarget();
            if (aqj2 != null) {
                Fox.this.getLookControl().setLookAt(aqj2, 60.0f, 30.0f);
            }
            if (!Fox.this.isFaceplanted()) {
                final Vec3 dck3 = Fox.this.getDeltaMovement();
                if (dck3.y * dck3.y < 0.029999999329447746 && Fox.this.xRot != 0.0f) {
                    Fox.this.xRot = Mth.rotlerp(Fox.this.xRot, 0.0f, 0.2f);
                }
                else {
                    final double double4 = Math.sqrt(Entity.getHorizontalDistanceSqr(dck3));
                    final double double5 = Math.signum(-dck3.y) * Math.acos(double4 / dck3.length()) * 57.2957763671875;
                    Fox.this.xRot = (float)double5;
                }
            }
            if (aqj2 != null && Fox.this.distanceTo(aqj2) <= 2.0f) {
                Fox.this.doHurtTarget(aqj2);
            }
            else if (Fox.this.xRot > 0.0f && Fox.this.onGround && (float)Fox.this.getDeltaMovement().y != 0.0f && Fox.this.level.getBlockState(Fox.this.blockPosition()).is(Blocks.SNOW)) {
                Fox.this.xRot = 60.0f;
                Fox.this.setTarget(null);
                Fox.this.setFaceplanted(true);
            }
        }
    }
    
    public class FoxLookControl extends LookControl {
        public FoxLookControl() {
            super(Fox.this);
        }
        
        @Override
        public void tick() {
            if (!Fox.this.isSleeping()) {
                super.tick();
            }
        }
        
        @Override
        protected boolean resetXRotOnTick() {
            return !Fox.this.isPouncing() && !Fox.this.isCrouching() && (!Fox.this.isInterested() & !Fox.this.isFaceplanted());
        }
    }
    
    class FoxFollowParentGoal extends FollowParentGoal {
        private final Fox fox;
        
        public FoxFollowParentGoal(final Fox bae2, final double double3) {
            super(bae2, double3);
            this.fox = bae2;
        }
        
        @Override
        public boolean canUse() {
            return !this.fox.isDefending() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.fox.isDefending() && super.canContinueToUse();
        }
        
        @Override
        public void start() {
            this.fox.clearStates();
            super.start();
        }
    }
    
    class FoxLookAtPlayerGoal extends LookAtPlayerGoal {
        public FoxLookAtPlayerGoal(final Mob aqk, final Class<? extends LivingEntity> class3, final float float4) {
            super(aqk, class3, float4);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested();
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested();
        }
    }
}
