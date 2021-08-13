package net.minecraft.world.entity.player;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.advancements.CriteriaTriggers;
import java.util.function.Predicate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ProjectileWeaponItem;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import java.util.Optional;
import net.minecraft.util.Unit;
import com.mojang.datafixers.util.Either;
import java.util.Iterator;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.trading.MerchantOffers;
import java.util.OptionalInt;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import java.util.function.Consumer;
import net.minecraft.world.scores.Team;
import net.minecraft.world.item.AxeItem;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.stats.Stat;
import net.minecraft.world.damagesource.DamageSource;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.Difficulty;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Pose;
import java.util.Map;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;

public abstract class Player extends LivingEntity {
    public static final EntityDimensions STANDING_DIMENSIONS;
    private static final Map<Pose, EntityDimensions> POSES;
    private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID;
    private static final EntityDataAccessor<Integer> DATA_SCORE_ID;
    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION;
    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND;
    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_LEFT;
    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_RIGHT;
    private long timeEntitySatOnShoulder;
    public final Inventory inventory;
    protected PlayerEnderChestContainer enderChestInventory;
    public final InventoryMenu inventoryMenu;
    public AbstractContainerMenu containerMenu;
    protected FoodData foodData;
    protected int jumpTriggerTime;
    public float oBob;
    public float bob;
    public int takeXpDelay;
    public double xCloakO;
    public double yCloakO;
    public double zCloakO;
    public double xCloak;
    public double yCloak;
    public double zCloak;
    private int sleepCounter;
    protected boolean wasUnderwater;
    public final Abilities abilities;
    public int experienceLevel;
    public int totalExperience;
    public float experienceProgress;
    protected int enchantmentSeed;
    protected final float defaultFlySpeed = 0.02f;
    private int lastLevelUpTime;
    private final GameProfile gameProfile;
    private boolean reducedDebugInfo;
    private ItemStack lastItemInMainHand;
    private final ItemCooldowns cooldowns;
    @Nullable
    public FishingHook fishing;
    
    public Player(final Level bru, final BlockPos fx, final float float3, final GameProfile gameProfile) {
        super(EntityType.PLAYER, bru);
        this.inventory = new Inventory(this);
        this.enderChestInventory = new PlayerEnderChestContainer();
        this.foodData = new FoodData();
        this.abilities = new Abilities();
        this.lastItemInMainHand = ItemStack.EMPTY;
        this.cooldowns = this.createItemCooldowns();
        this.setUUID(createPlayerUUID(gameProfile));
        this.gameProfile = gameProfile;
        this.inventoryMenu = new InventoryMenu(this.inventory, !bru.isClientSide, this);
        this.containerMenu = this.inventoryMenu;
        this.moveTo(fx.getX() + 0.5, fx.getY() + 1, fx.getZ() + 0.5, float3, 0.0f);
        this.rotOffs = 180.0f;
    }
    
    public boolean blockActionRestricted(final Level bru, final BlockPos fx, final GameType brr) {
        if (!brr.isBlockPlacingRestricted()) {
            return false;
        }
        if (brr == GameType.SPECTATOR) {
            return true;
        }
        if (this.mayBuild()) {
            return false;
        }
        final ItemStack bly5 = this.getMainHandItem();
        return bly5.isEmpty() || !bly5.hasAdventureModeBreakTagForBlock(bru.getTagManager(), new BlockInWorld(bru, fx, false));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.10000000149011612).add(Attributes.ATTACK_SPEED).add(Attributes.LUCK);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Float>define(Player.DATA_PLAYER_ABSORPTION_ID, 0.0f);
        this.entityData.<Integer>define(Player.DATA_SCORE_ID, 0);
        this.entityData.<Byte>define(Player.DATA_PLAYER_MODE_CUSTOMISATION, (Byte)0);
        this.entityData.<Byte>define(Player.DATA_PLAYER_MAIN_HAND, (Byte)1);
        this.entityData.<CompoundTag>define(Player.DATA_SHOULDER_LEFT, new CompoundTag());
        this.entityData.<CompoundTag>define(Player.DATA_SHOULDER_RIGHT, new CompoundTag());
    }
    
    @Override
    public void tick() {
        this.noPhysics = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }
        if (this.takeXpDelay > 0) {
            --this.takeXpDelay;
        }
        if (this.isSleeping()) {
            ++this.sleepCounter;
            if (this.sleepCounter > 100) {
                this.sleepCounter = 100;
            }
            if (!this.level.isClientSide && this.level.isDay()) {
                this.stopSleepInBed(false, true);
            }
        }
        else if (this.sleepCounter > 0) {
            ++this.sleepCounter;
            if (this.sleepCounter >= 110) {
                this.sleepCounter = 0;
            }
        }
        this.updateIsUnderwater();
        super.tick();
        if (!this.level.isClientSide && this.containerMenu != null && !this.containerMenu.stillValid(this)) {
            this.closeContainer();
            this.containerMenu = this.inventoryMenu;
        }
        this.moveCloak();
        if (!this.level.isClientSide) {
            this.foodData.tick(this);
            this.awardStat(Stats.PLAY_ONE_MINUTE);
            if (this.isAlive()) {
                this.awardStat(Stats.TIME_SINCE_DEATH);
            }
            if (this.isDiscrete()) {
                this.awardStat(Stats.CROUCH_TIME);
            }
            if (!this.isSleeping()) {
                this.awardStat(Stats.TIME_SINCE_REST);
            }
        }
        final int integer2 = 29999999;
        final double double3 = Mth.clamp(this.getX(), -2.9999999E7, 2.9999999E7);
        final double double4 = Mth.clamp(this.getZ(), -2.9999999E7, 2.9999999E7);
        if (double3 != this.getX() || double4 != this.getZ()) {
            this.setPos(double3, this.getY(), double4);
        }
        ++this.attackStrengthTicker;
        final ItemStack bly7 = this.getMainHandItem();
        if (!ItemStack.matches(this.lastItemInMainHand, bly7)) {
            if (!ItemStack.isSameIgnoreDurability(this.lastItemInMainHand, bly7)) {
                this.resetAttackStrengthTicker();
            }
            this.lastItemInMainHand = bly7.copy();
        }
        this.turtleHelmetTick();
        this.cooldowns.tick();
        this.updatePlayerPose();
    }
    
    public boolean isSecondaryUseActive() {
        return this.isShiftKeyDown();
    }
    
    protected boolean wantsToStopRiding() {
        return this.isShiftKeyDown();
    }
    
    protected boolean isStayingOnGroundSurface() {
        return this.isShiftKeyDown();
    }
    
    protected boolean updateIsUnderwater() {
        return this.wasUnderwater = this.isEyeInFluid(FluidTags.WATER);
    }
    
    private void turtleHelmetTick() {
        final ItemStack bly2 = this.getItemBySlot(EquipmentSlot.HEAD);
        if (bly2.getItem() == Items.TURTLE_HELMET && !this.isEyeInFluid(FluidTags.WATER)) {
            this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
        }
    }
    
    protected ItemCooldowns createItemCooldowns() {
        return new ItemCooldowns();
    }
    
    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        final double double2 = this.getX() - this.xCloak;
        final double double3 = this.getY() - this.yCloak;
        final double double4 = this.getZ() - this.zCloak;
        final double double5 = 10.0;
        if (double2 > 10.0) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }
        if (double4 > 10.0) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }
        if (double3 > 10.0) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }
        if (double2 < -10.0) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }
        if (double4 < -10.0) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }
        if (double3 < -10.0) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }
        this.xCloak += double2 * 0.25;
        this.zCloak += double4 * 0.25;
        this.yCloak += double3 * 0.25;
    }
    
    protected void updatePlayerPose() {
        if (!this.canEnterPose(Pose.SWIMMING)) {
            return;
        }
        Pose aqu2;
        if (this.isFallFlying()) {
            aqu2 = Pose.FALL_FLYING;
        }
        else if (this.isSleeping()) {
            aqu2 = Pose.SLEEPING;
        }
        else if (this.isSwimming()) {
            aqu2 = Pose.SWIMMING;
        }
        else if (this.isAutoSpinAttack()) {
            aqu2 = Pose.SPIN_ATTACK;
        }
        else if (this.isShiftKeyDown() && !this.abilities.flying) {
            aqu2 = Pose.CROUCHING;
        }
        else {
            aqu2 = Pose.STANDING;
        }
        Pose aqu3;
        if (this.isSpectator() || this.isPassenger() || this.canEnterPose(aqu2)) {
            aqu3 = aqu2;
        }
        else if (this.canEnterPose(Pose.CROUCHING)) {
            aqu3 = Pose.CROUCHING;
        }
        else {
            aqu3 = Pose.SWIMMING;
        }
        this.setPose(aqu3);
    }
    
    @Override
    public int getPortalWaitTime() {
        return this.abilities.invulnerable ? 1 : 80;
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.PLAYER_SWIM;
    }
    
    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.PLAYER_SPLASH;
    }
    
    @Override
    protected SoundEvent getSwimHighSpeedSplashSound() {
        return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
    }
    
    @Override
    public int getDimensionChangingDelay() {
        return 10;
    }
    
    @Override
    public void playSound(final SoundEvent adn, final float float2, final float float3) {
        this.level.playSound(this, this.getX(), this.getY(), this.getZ(), adn, this.getSoundSource(), float2, float3);
    }
    
    public void playNotifySound(final SoundEvent adn, final SoundSource adp, final float float3, final float float4) {
    }
    
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.PLAYERS;
    }
    
    @Override
    protected int getFireImmuneTicks() {
        return 20;
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 9) {
            this.completeUsingItem();
        }
        else if (byte1 == 23) {
            this.reducedDebugInfo = false;
        }
        else if (byte1 == 22) {
            this.reducedDebugInfo = true;
        }
        else if (byte1 == 43) {
            this.addParticlesAroundSelf(ParticleTypes.CLOUD);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    private void addParticlesAroundSelf(final ParticleOptions hf) {
        for (int integer3 = 0; integer3 < 5; ++integer3) {
            final double double4 = this.random.nextGaussian() * 0.02;
            final double double5 = this.random.nextGaussian() * 0.02;
            final double double6 = this.random.nextGaussian() * 0.02;
            this.level.addParticle(hf, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), double4, double5, double6);
        }
    }
    
    protected void closeContainer() {
        this.containerMenu = this.inventoryMenu;
    }
    
    @Override
    public void rideTick() {
        if (this.wantsToStopRiding() && this.isPassenger()) {
            this.stopRiding();
            this.setShiftKeyDown(false);
            return;
        }
        final double double2 = this.getX();
        final double double3 = this.getY();
        final double double4 = this.getZ();
        super.rideTick();
        this.oBob = this.bob;
        this.bob = 0.0f;
        this.checkRidingStatistics(this.getX() - double2, this.getY() - double3, this.getZ() - double4);
    }
    
    public void resetPos() {
        this.setPose(Pose.STANDING);
        super.resetPos();
        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }
    
    @Override
    protected void serverAiStep() {
        super.serverAiStep();
        this.updateSwingTime();
        this.yHeadRot = this.yRot;
    }
    
    @Override
    public void aiStep() {
        if (this.jumpTriggerTime > 0) {
            --this.jumpTriggerTime;
        }
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            if (this.getHealth() < this.getMaxHealth() && this.tickCount % 20 == 0) {
                this.heal(1.0f);
            }
            if (this.foodData.needsFood() && this.tickCount % 10 == 0) {
                this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1);
            }
        }
        this.inventory.tick();
        this.oBob = this.bob;
        super.aiStep();
        this.flyingSpeed = 0.02f;
        if (this.isSprinting()) {
            this.flyingSpeed += (float)0.005999999865889549;
        }
        this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        float float2;
        if (!this.onGround || this.isDeadOrDying() || this.isSwimming()) {
            float2 = 0.0f;
        }
        else {
            float2 = Math.min(0.1f, Mth.sqrt(Entity.getHorizontalDistanceSqr(this.getDeltaMovement())));
        }
        this.bob += (float2 - this.bob) * 0.4f;
        if (this.getHealth() > 0.0f && !this.isSpectator()) {
            AABB dcf3;
            if (this.isPassenger() && !this.getVehicle().removed) {
                dcf3 = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(1.0, 0.0, 1.0);
            }
            else {
                dcf3 = this.getBoundingBox().inflate(1.0, 0.5, 1.0);
            }
            final List<Entity> list4 = this.level.getEntities(this, dcf3);
            for (int integer5 = 0; integer5 < list4.size(); ++integer5) {
                final Entity apx6 = (Entity)list4.get(integer5);
                if (!apx6.removed) {
                    this.touch(apx6);
                }
            }
        }
        this.playShoulderEntityAmbientSound(this.getShoulderEntityLeft());
        this.playShoulderEntityAmbientSound(this.getShoulderEntityRight());
        if ((!this.level.isClientSide && (this.fallDistance > 0.5f || this.isInWater())) || this.abilities.flying || this.isSleeping()) {
            this.removeEntitiesOnShoulder();
        }
    }
    
    private void playShoulderEntityAmbientSound(@Nullable final CompoundTag md) {
        if (md != null && (!md.contains("Silent") || !md.getBoolean("Silent")) && this.level.random.nextInt(200) == 0) {
            final String string3 = md.getString("id");
            EntityType.byString(string3).filter(aqb -> aqb == EntityType.PARROT).ifPresent(aqb -> {
                if (!Parrot.imitateNearbyMobs(this.level, this)) {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), Parrot.getAmbient(this.level, this.level.random), this.getSoundSource(), 1.0f, Parrot.getPitch(this.level.random));
                }
            });
        }
    }
    
    private void touch(final Entity apx) {
        apx.playerTouch(this);
    }
    
    public int getScore() {
        return this.entityData.<Integer>get(Player.DATA_SCORE_ID);
    }
    
    public void setScore(final int integer) {
        this.entityData.<Integer>set(Player.DATA_SCORE_ID, integer);
    }
    
    public void increaseScore(final int integer) {
        final int integer2 = this.getScore();
        this.entityData.<Integer>set(Player.DATA_SCORE_ID, integer2 + integer);
    }
    
    @Override
    public void die(final DamageSource aph) {
        super.die(aph);
        this.reapplyPosition();
        if (!this.isSpectator()) {
            this.dropAllDeathLoot(aph);
        }
        if (aph != null) {
            this.setDeltaMovement(-Mth.cos((this.hurtDir + this.yRot) * 0.017453292f) * 0.1f, 0.10000000149011612, -Mth.sin((this.hurtDir + this.yRot) * 0.017453292f) * 0.1f);
        }
        else {
            this.setDeltaMovement(0.0, 0.1, 0.0);
        }
        this.awardStat(Stats.DEATHS);
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        this.clearFire();
        this.setSharedFlag(0, false);
    }
    
    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            this.destroyVanishingCursedItems();
            this.inventory.dropAll();
        }
    }
    
    protected void destroyVanishingCursedItems() {
        for (int integer2 = 0; integer2 < this.inventory.getContainerSize(); ++integer2) {
            final ItemStack bly3 = this.inventory.getItem(integer2);
            if (!bly3.isEmpty() && EnchantmentHelper.hasVanishingCurse(bly3)) {
                this.inventory.removeItemNoUpdate(integer2);
            }
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (aph == DamageSource.ON_FIRE) {
            return SoundEvents.PLAYER_HURT_ON_FIRE;
        }
        if (aph == DamageSource.DROWN) {
            return SoundEvents.PLAYER_HURT_DROWN;
        }
        if (aph == DamageSource.SWEET_BERRY_BUSH) {
            return SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH;
        }
        return SoundEvents.PLAYER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }
    
    public boolean drop(final boolean boolean1) {
        return this.drop(this.inventory.removeItem(this.inventory.selected, (boolean1 && !this.inventory.getSelected().isEmpty()) ? this.inventory.getSelected().getCount() : 1), false, true) != null;
    }
    
    @Nullable
    public ItemEntity drop(final ItemStack bly, final boolean boolean2) {
        return this.drop(bly, false, boolean2);
    }
    
    @Nullable
    public ItemEntity drop(final ItemStack bly, final boolean boolean2, final boolean boolean3) {
        if (bly.isEmpty()) {
            return null;
        }
        if (this.level.isClientSide) {
            this.swing(InteractionHand.MAIN_HAND);
        }
        final double double5 = this.getEyeY() - 0.30000001192092896;
        final ItemEntity bcs7 = new ItemEntity(this.level, this.getX(), double5, this.getZ(), bly);
        bcs7.setPickUpDelay(40);
        if (boolean3) {
            bcs7.setThrower(this.getUUID());
        }
        if (boolean2) {
            final float float8 = this.random.nextFloat() * 0.5f;
            final float float9 = this.random.nextFloat() * 6.2831855f;
            bcs7.setDeltaMovement(-Mth.sin(float9) * float8, 0.20000000298023224, Mth.cos(float9) * float8);
        }
        else {
            final float float8 = 0.3f;
            final float float9 = Mth.sin(this.xRot * 0.017453292f);
            final float float10 = Mth.cos(this.xRot * 0.017453292f);
            final float float11 = Mth.sin(this.yRot * 0.017453292f);
            final float float12 = Mth.cos(this.yRot * 0.017453292f);
            final float float13 = this.random.nextFloat() * 6.2831855f;
            final float float14 = 0.02f * this.random.nextFloat();
            bcs7.setDeltaMovement(-float11 * float10 * 0.3f + Math.cos((double)float13) * float14, -float9 * 0.3f + 0.1f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f, float12 * float10 * 0.3f + Math.sin((double)float13) * float14);
        }
        return bcs7;
    }
    
    public float getDestroySpeed(final BlockState cee) {
        float float3 = this.inventory.getDestroySpeed(cee);
        if (float3 > 1.0f) {
            final int integer4 = EnchantmentHelper.getBlockEfficiency(this);
            final ItemStack bly5 = this.getMainHandItem();
            if (integer4 > 0 && !bly5.isEmpty()) {
                float3 += integer4 * integer4 + 1;
            }
        }
        if (MobEffectUtil.hasDigSpeed(this)) {
            float3 *= 1.0f + (MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2f;
        }
        if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float float4 = 0.0f;
            switch (this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0: {
                    float4 = 0.3f;
                    break;
                }
                case 1: {
                    float4 = 0.09f;
                    break;
                }
                case 2: {
                    float4 = 0.0027f;
                    break;
                }
                default: {
                    float4 = 8.1E-4f;
                    break;
                }
            }
            float3 *= float4;
        }
        if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            float3 /= 5.0f;
        }
        if (!this.onGround) {
            float3 /= 5.0f;
        }
        return float3;
    }
    
    public boolean hasCorrectToolForDrops(final BlockState cee) {
        return !cee.requiresCorrectToolForDrops() || this.inventory.getSelected().isCorrectToolForDrops(cee);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setUUID(createPlayerUUID(this.gameProfile));
        final ListTag mj3 = md.getList("Inventory", 10);
        this.inventory.load(mj3);
        this.inventory.selected = md.getInt("SelectedItemSlot");
        this.sleepCounter = md.getShort("SleepTimer");
        this.experienceProgress = md.getFloat("XpP");
        this.experienceLevel = md.getInt("XpLevel");
        this.totalExperience = md.getInt("XpTotal");
        this.enchantmentSeed = md.getInt("XpSeed");
        if (this.enchantmentSeed == 0) {
            this.enchantmentSeed = this.random.nextInt();
        }
        this.setScore(md.getInt("Score"));
        this.foodData.readAdditionalSaveData(md);
        this.abilities.loadSaveData(md);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.abilities.getWalkingSpeed());
        if (md.contains("EnderItems", 9)) {
            this.enderChestInventory.fromTag(md.getList("EnderItems", 10));
        }
        if (md.contains("ShoulderEntityLeft", 10)) {
            this.setShoulderEntityLeft(md.getCompound("ShoulderEntityLeft"));
        }
        if (md.contains("ShoulderEntityRight", 10)) {
            this.setShoulderEntityRight(md.getCompound("ShoulderEntityRight"));
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        md.put("Inventory", (net.minecraft.nbt.Tag)this.inventory.save(new ListTag()));
        md.putInt("SelectedItemSlot", this.inventory.selected);
        md.putShort("SleepTimer", (short)this.sleepCounter);
        md.putFloat("XpP", this.experienceProgress);
        md.putInt("XpLevel", this.experienceLevel);
        md.putInt("XpTotal", this.totalExperience);
        md.putInt("XpSeed", this.enchantmentSeed);
        md.putInt("Score", this.getScore());
        this.foodData.addAdditionalSaveData(md);
        this.abilities.addSaveData(md);
        md.put("EnderItems", (net.minecraft.nbt.Tag)this.enderChestInventory.createTag());
        if (!this.getShoulderEntityLeft().isEmpty()) {
            md.put("ShoulderEntityLeft", (net.minecraft.nbt.Tag)this.getShoulderEntityLeft());
        }
        if (!this.getShoulderEntityRight().isEmpty()) {
            md.put("ShoulderEntityRight", (net.minecraft.nbt.Tag)this.getShoulderEntityRight());
        }
    }
    
    @Override
    public boolean isInvulnerableTo(final DamageSource aph) {
        if (super.isInvulnerableTo(aph)) {
            return true;
        }
        if (aph == DamageSource.DROWN) {
            return !this.level.getGameRules().getBoolean(GameRules.RULE_DROWNING_DAMAGE);
        }
        if (aph == DamageSource.FALL) {
            return !this.level.getGameRules().getBoolean(GameRules.RULE_FALL_DAMAGE);
        }
        return aph.isFire() && !this.level.getGameRules().getBoolean(GameRules.RULE_FIRE_DAMAGE);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (this.abilities.invulnerable && !aph.isBypassInvul()) {
            return false;
        }
        this.noActionTime = 0;
        if (this.isDeadOrDying()) {
            return false;
        }
        this.removeEntitiesOnShoulder();
        if (aph.scalesWithDifficulty()) {
            if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
                float2 = 0.0f;
            }
            if (this.level.getDifficulty() == Difficulty.EASY) {
                float2 = Math.min(float2 / 2.0f + 1.0f, float2);
            }
            if (this.level.getDifficulty() == Difficulty.HARD) {
                float2 = float2 * 3.0f / 2.0f;
            }
        }
        return float2 != 0.0f && super.hurt(aph, float2);
    }
    
    @Override
    protected void blockUsingShield(final LivingEntity aqj) {
        super.blockUsingShield(aqj);
        if (aqj.getMainHandItem().getItem() instanceof AxeItem) {
            this.disableShield(true);
        }
    }
    
    public boolean canHarmPlayer(final Player bft) {
        final Team ddm3 = this.getTeam();
        final Team ddm4 = bft.getTeam();
        return ddm3 == null || !ddm3.isAlliedTo(ddm4) || ddm3.isAllowFriendlyFire();
    }
    
    @Override
    protected void hurtArmor(final DamageSource aph, final float float2) {
        this.inventory.hurtArmor(aph, float2);
    }
    
    @Override
    protected void hurtCurrentlyUsedShield(final float float1) {
        if (this.useItem.getItem() != Items.SHIELD) {
            return;
        }
        if (!this.level.isClientSide) {
            this.awardStat(Stats.ITEM_USED.get(this.useItem.getItem()));
        }
        if (float1 >= 3.0f) {
            final int integer3 = 1 + Mth.floor(float1);
            final InteractionHand aoq4 = this.getUsedItemHand();
            this.useItem.<Player>hurtAndBreak(integer3, this, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq4)));
            if (this.useItem.isEmpty()) {
                if (aoq4 == InteractionHand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                else {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                this.useItem = ItemStack.EMPTY;
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8f, 0.8f + this.level.random.nextFloat() * 0.4f);
            }
        }
    }
    
    @Override
    protected void actuallyHurt(final DamageSource aph, float float2) {
        if (this.isInvulnerableTo(aph)) {
            return;
        }
        float2 = this.getDamageAfterArmorAbsorb(aph, float2);
        final float float3;
        float2 = (float3 = this.getDamageAfterMagicAbsorb(aph, float2));
        float2 = Math.max(float2 - this.getAbsorptionAmount(), 0.0f);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - (float3 - float2));
        final float float4 = float3 - float2;
        if (float4 > 0.0f && float4 < 3.4028235E37f) {
            this.awardStat(Stats.DAMAGE_ABSORBED, Math.round(float4 * 10.0f));
        }
        if (float2 == 0.0f) {
            return;
        }
        this.causeFoodExhaustion(aph.getFoodExhaustion());
        final float float5 = this.getHealth();
        this.setHealth(this.getHealth() - float2);
        this.getCombatTracker().recordDamage(aph, float5, float2);
        if (float2 < 3.4028235E37f) {
            this.awardStat(Stats.DAMAGE_TAKEN, Math.round(float2 * 10.0f));
        }
    }
    
    @Override
    protected boolean onSoulSpeedBlock() {
        return !this.abilities.flying && super.onSoulSpeedBlock();
    }
    
    public void openTextEdit(final SignBlockEntity cdc) {
    }
    
    public void openMinecartCommandBlock(final BaseCommandBlock bqv) {
    }
    
    public void openCommandBlock(final CommandBlockEntity ccl) {
    }
    
    public void openStructureBlock(final StructureBlockEntity cdg) {
    }
    
    public void openJigsawBlock(final JigsawBlockEntity ccw) {
    }
    
    public void openHorseInventory(final AbstractHorse bay, final Container aok) {
    }
    
    public OptionalInt openMenu(@Nullable final MenuProvider aou) {
        return OptionalInt.empty();
    }
    
    public void sendMerchantOffers(final int integer1, final MerchantOffers bqt, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6) {
    }
    
    public void openItemGui(final ItemStack bly, final InteractionHand aoq) {
    }
    
    public InteractionResult interactOn(final Entity apx, final InteractionHand aoq) {
        if (this.isSpectator()) {
            if (apx instanceof MenuProvider) {
                this.openMenu((MenuProvider)apx);
            }
            return InteractionResult.PASS;
        }
        ItemStack bly4 = this.getItemInHand(aoq);
        final ItemStack bly5 = bly4.copy();
        final InteractionResult aor6 = apx.interact(this, aoq);
        if (aor6.consumesAction()) {
            if (this.abilities.instabuild && bly4 == this.getItemInHand(aoq) && bly4.getCount() < bly5.getCount()) {
                bly4.setCount(bly5.getCount());
            }
            return aor6;
        }
        if (!bly4.isEmpty() && apx instanceof LivingEntity) {
            if (this.abilities.instabuild) {
                bly4 = bly5;
            }
            final InteractionResult aor7 = bly4.interactLivingEntity(this, (LivingEntity)apx, aoq);
            if (aor7.consumesAction()) {
                if (bly4.isEmpty() && !this.abilities.instabuild) {
                    this.setItemInHand(aoq, ItemStack.EMPTY);
                }
                return aor7;
            }
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public double getMyRidingOffset() {
        return -0.35;
    }
    
    @Override
    public void removeVehicle() {
        super.removeVehicle();
        this.boardingCooldown = 0;
    }
    
    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isSleeping();
    }
    
    public boolean isAffectedByFluids() {
        return !this.abilities.flying;
    }
    
    @Override
    protected Vec3 maybeBackOffFromEdge(Vec3 dck, final MoverType aqo) {
        if (!this.abilities.flying && (aqo == MoverType.SELF || aqo == MoverType.PLAYER) && this.isStayingOnGroundSurface() && this.isAboveGround()) {
            double double4 = dck.x;
            double double5 = dck.z;
            final double double6 = 0.05;
            while (double4 != 0.0 && this.level.noCollision(this, this.getBoundingBox().move(double4, -this.maxUpStep, 0.0))) {
                if (double4 < 0.05 && double4 >= -0.05) {
                    double4 = 0.0;
                }
                else if (double4 > 0.0) {
                    double4 -= 0.05;
                }
                else {
                    double4 += 0.05;
                }
            }
            while (double5 != 0.0 && this.level.noCollision(this, this.getBoundingBox().move(0.0, -this.maxUpStep, double5))) {
                if (double5 < 0.05 && double5 >= -0.05) {
                    double5 = 0.0;
                }
                else if (double5 > 0.0) {
                    double5 -= 0.05;
                }
                else {
                    double5 += 0.05;
                }
            }
            while (double4 != 0.0 && double5 != 0.0 && this.level.noCollision(this, this.getBoundingBox().move(double4, -this.maxUpStep, double5))) {
                if (double4 < 0.05 && double4 >= -0.05) {
                    double4 = 0.0;
                }
                else if (double4 > 0.0) {
                    double4 -= 0.05;
                }
                else {
                    double4 += 0.05;
                }
                if (double5 < 0.05 && double5 >= -0.05) {
                    double5 = 0.0;
                }
                else if (double5 > 0.0) {
                    double5 -= 0.05;
                }
                else {
                    double5 += 0.05;
                }
            }
            dck = new Vec3(double4, dck.y, double5);
        }
        return dck;
    }
    
    private boolean isAboveGround() {
        return this.onGround || (this.fallDistance < this.maxUpStep && !this.level.noCollision(this, this.getBoundingBox().move(0.0, this.fallDistance - this.maxUpStep, 0.0)));
    }
    
    public void attack(final Entity apx) {
        if (!apx.isAttackable()) {
            return;
        }
        if (apx.skipAttackInteraction(this)) {
            return;
        }
        float float3 = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float float4;
        if (apx instanceof LivingEntity) {
            float4 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)apx).getMobType());
        }
        else {
            float4 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), MobType.UNDEFINED);
        }
        final float float5 = this.getAttackStrengthScale(0.5f);
        float3 *= 0.2f + float5 * float5 * 0.8f;
        float4 *= float5;
        this.resetAttackStrengthTicker();
        if (float3 > 0.0f || float4 > 0.0f) {
            final boolean boolean6 = float5 > 0.9f;
            boolean boolean7 = false;
            int integer8 = 0;
            integer8 += EnchantmentHelper.getKnockbackBonus(this);
            if (this.isSprinting() && boolean6) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, this.getSoundSource(), 1.0f, 1.0f);
                ++integer8;
                boolean7 = true;
            }
            boolean boolean8 = boolean6 && this.fallDistance > 0.0f && !this.onGround && !this.onClimbable() && !this.isInWater() && !this.hasEffect(MobEffects.BLINDNESS) && !this.isPassenger() && apx instanceof LivingEntity;
            boolean8 = (boolean8 && !this.isSprinting());
            if (boolean8) {
                float3 *= 1.5f;
            }
            float3 += float4;
            boolean boolean9 = false;
            final double double11 = this.walkDist - this.walkDistO;
            if (boolean6 && !boolean8 && !boolean7 && this.onGround && double11 < this.getSpeed()) {
                final ItemStack bly13 = this.getItemInHand(InteractionHand.MAIN_HAND);
                if (bly13.getItem() instanceof SwordItem) {
                    boolean9 = true;
                }
            }
            float float6 = 0.0f;
            boolean boolean10 = false;
            final int integer9 = EnchantmentHelper.getFireAspect(this);
            if (apx instanceof LivingEntity) {
                float6 = ((LivingEntity)apx).getHealth();
                if (integer9 > 0 && !apx.isOnFire()) {
                    boolean10 = true;
                    apx.setSecondsOnFire(1);
                }
            }
            final Vec3 dck16 = apx.getDeltaMovement();
            final boolean boolean11 = apx.hurt(DamageSource.playerAttack(this), float3);
            if (boolean11) {
                if (integer8 > 0) {
                    if (apx instanceof LivingEntity) {
                        ((LivingEntity)apx).knockback(integer8 * 0.5f, Mth.sin(this.yRot * 0.017453292f), -Mth.cos(this.yRot * 0.017453292f));
                    }
                    else {
                        apx.push(-Mth.sin(this.yRot * 0.017453292f) * integer8 * 0.5f, 0.1, Mth.cos(this.yRot * 0.017453292f) * integer8 * 0.5f);
                    }
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                    this.setSprinting(false);
                }
                if (boolean9) {
                    final float float7 = 1.0f + EnchantmentHelper.getSweepingDamageRatio(this) * float3;
                    final List<LivingEntity> list19 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, apx.getBoundingBox().inflate(1.0, 0.25, 1.0));
                    for (final LivingEntity aqj21 : list19) {
                        if (aqj21 != this && aqj21 != apx) {
                            if (this.isAlliedTo(aqj21)) {
                                continue;
                            }
                            if (aqj21 instanceof ArmorStand && ((ArmorStand)aqj21).isMarker()) {
                                continue;
                            }
                            if (this.distanceToSqr(aqj21) >= 9.0) {
                                continue;
                            }
                            aqj21.knockback(0.4f, Mth.sin(this.yRot * 0.017453292f), -Mth.cos(this.yRot * 0.017453292f));
                            aqj21.hurt(DamageSource.playerAttack(this), float7);
                        }
                    }
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1.0f, 1.0f);
                    this.sweepAttack();
                }
                if (apx instanceof ServerPlayer && apx.hurtMarked) {
                    ((ServerPlayer)apx).connection.send(new ClientboundSetEntityMotionPacket(apx));
                    apx.hurtMarked = false;
                    apx.setDeltaMovement(dck16);
                }
                if (boolean8) {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 1.0f, 1.0f);
                    this.crit(apx);
                }
                if (!boolean8 && !boolean9) {
                    if (boolean6) {
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0f, 1.0f);
                    }
                    else {
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, this.getSoundSource(), 1.0f, 1.0f);
                    }
                }
                if (float4 > 0.0f) {
                    this.magicCrit(apx);
                }
                this.setLastHurtMob(apx);
                if (apx instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects((LivingEntity)apx, this);
                }
                EnchantmentHelper.doPostDamageEffects(this, apx);
                final ItemStack bly14 = this.getMainHandItem();
                Entity apx2 = apx;
                if (apx instanceof EnderDragonPart) {
                    apx2 = ((EnderDragonPart)apx).parentMob;
                }
                if (!this.level.isClientSide && !bly14.isEmpty() && apx2 instanceof LivingEntity) {
                    bly14.hurtEnemy((LivingEntity)apx2, this);
                    if (bly14.isEmpty()) {
                        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }
                }
                if (apx instanceof LivingEntity) {
                    final float float8 = float6 - ((LivingEntity)apx).getHealth();
                    this.awardStat(Stats.DAMAGE_DEALT, Math.round(float8 * 10.0f));
                    if (integer9 > 0) {
                        apx.setSecondsOnFire(integer9 * 4);
                    }
                    if (this.level instanceof ServerLevel && float8 > 2.0f) {
                        final int integer10 = (int)(float8 * 0.5);
                        ((ServerLevel)this.level).<SimpleParticleType>sendParticles(ParticleTypes.DAMAGE_INDICATOR, apx.getX(), apx.getY(0.5), apx.getZ(), integer10, 0.1, 0.0, 0.1, 0.2);
                    }
                }
                this.causeFoodExhaustion(0.1f);
            }
            else {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.getSoundSource(), 1.0f, 1.0f);
                if (boolean10) {
                    apx.clearFire();
                }
            }
        }
    }
    
    @Override
    protected void doAutoAttackOnTouch(final LivingEntity aqj) {
        this.attack(aqj);
    }
    
    public void disableShield(final boolean boolean1) {
        float float3 = 0.25f + EnchantmentHelper.getBlockEfficiency(this) * 0.05f;
        if (boolean1) {
            float3 += 0.75f;
        }
        if (this.random.nextFloat() < float3) {
            this.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }
    }
    
    public void crit(final Entity apx) {
    }
    
    public void magicCrit(final Entity apx) {
    }
    
    public void sweepAttack() {
        final double double2 = -Mth.sin(this.yRot * 0.017453292f);
        final double double3 = Mth.cos(this.yRot * 0.017453292f);
        if (this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).<SimpleParticleType>sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + double2, this.getY(0.5), this.getZ() + double3, 0, double2, 0.0, double3, 0.0);
        }
    }
    
    public void respawn() {
    }
    
    @Override
    public void remove() {
        super.remove();
        this.inventoryMenu.removed(this);
        if (this.containerMenu != null) {
            this.containerMenu.removed(this);
        }
    }
    
    public boolean isLocalPlayer() {
        return false;
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public Either<BedSleepingProblem, Unit> startSleepInBed(final BlockPos fx) {
        this.startSleeping(fx);
        this.sleepCounter = 0;
        return (Either<BedSleepingProblem, Unit>)Either.right(Unit.INSTANCE);
    }
    
    public void stopSleepInBed(final boolean boolean1, final boolean boolean2) {
        super.stopSleeping();
        if (this.level instanceof ServerLevel && boolean2) {
            ((ServerLevel)this.level).updateSleepingPlayerList();
        }
        this.sleepCounter = (boolean1 ? 0 : 100);
    }
    
    @Override
    public void stopSleeping() {
        this.stopSleepInBed(true, true);
    }
    
    public static Optional<Vec3> findRespawnPositionAndUseSpawnBlock(final ServerLevel aag, final BlockPos fx, final float float3, final boolean boolean4, final boolean boolean5) {
        final BlockState cee6 = aag.getBlockState(fx);
        final Block bul7 = cee6.getBlock();
        if (bul7 instanceof RespawnAnchorBlock && cee6.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) > 0 && RespawnAnchorBlock.canSetSpawn(aag)) {
            final Optional<Vec3> optional8 = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, aag, fx);
            if (!boolean5 && optional8.isPresent()) {
                aag.setBlock(fx, ((StateHolder<O, BlockState>)cee6).<Comparable, Integer>setValue((Property<Comparable>)RespawnAnchorBlock.CHARGE, cee6.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) - 1), 3);
            }
            return optional8;
        }
        if (bul7 instanceof BedBlock && BedBlock.canSetSpawn(aag)) {
            return BedBlock.findStandUpPosition(EntityType.PLAYER, aag, fx, float3);
        }
        if (!boolean4) {
            return (Optional<Vec3>)Optional.empty();
        }
        final boolean boolean6 = bul7.isPossibleToRespawnInThis();
        final boolean boolean7 = aag.getBlockState(fx.above()).getBlock().isPossibleToRespawnInThis();
        if (boolean6 && boolean7) {
            return (Optional<Vec3>)Optional.of(new Vec3(fx.getX() + 0.5, fx.getY() + 0.1, fx.getZ() + 0.5));
        }
        return (Optional<Vec3>)Optional.empty();
    }
    
    public boolean isSleepingLongEnough() {
        return this.isSleeping() && this.sleepCounter >= 100;
    }
    
    public int getSleepTimer() {
        return this.sleepCounter;
    }
    
    public void displayClientMessage(final Component nr, final boolean boolean2) {
    }
    
    public void awardStat(final ResourceLocation vk) {
        this.awardStat(Stats.CUSTOM.get(vk));
    }
    
    public void awardStat(final ResourceLocation vk, final int integer) {
        this.awardStat(Stats.CUSTOM.get(vk), integer);
    }
    
    public void awardStat(final Stat<?> adv) {
        this.awardStat(adv, 1);
    }
    
    public void awardStat(final Stat<?> adv, final int integer) {
    }
    
    public void resetStat(final Stat<?> adv) {
    }
    
    public int awardRecipes(final Collection<Recipe<?>> collection) {
        return 0;
    }
    
    public void awardRecipesByKey(final ResourceLocation[] arr) {
    }
    
    public int resetRecipes(final Collection<Recipe<?>> collection) {
        return 0;
    }
    
    public void jumpFromGround() {
        super.jumpFromGround();
        this.awardStat(Stats.JUMP);
        if (this.isSprinting()) {
            this.causeFoodExhaustion(0.2f);
        }
        else {
            this.causeFoodExhaustion(0.05f);
        }
    }
    
    @Override
    public void travel(final Vec3 dck) {
        final double double3 = this.getX();
        final double double4 = this.getY();
        final double double5 = this.getZ();
        if (this.isSwimming() && !this.isPassenger()) {
            final double double6 = this.getLookAngle().y;
            final double double7 = (double6 < -0.2) ? 0.085 : 0.06;
            if (double6 <= 0.0 || this.jumping || !this.level.getBlockState(new BlockPos(this.getX(), this.getY() + 1.0 - 0.1, this.getZ())).getFluidState().isEmpty()) {
                final Vec3 dck2 = this.getDeltaMovement();
                this.setDeltaMovement(dck2.add(0.0, (double6 - dck2.y) * double7, 0.0));
            }
        }
        if (this.abilities.flying && !this.isPassenger()) {
            final double double6 = this.getDeltaMovement().y;
            final float float11 = this.flyingSpeed;
            this.flyingSpeed = this.abilities.getFlyingSpeed() * (this.isSprinting() ? 2 : 1);
            super.travel(dck);
            final Vec3 dck3 = this.getDeltaMovement();
            this.setDeltaMovement(dck3.x, double6 * 0.6, dck3.z);
            this.flyingSpeed = float11;
            this.fallDistance = 0.0f;
            this.setSharedFlag(7, false);
        }
        else {
            super.travel(dck);
        }
        this.checkMovementStatistics(this.getX() - double3, this.getY() - double4, this.getZ() - double5);
    }
    
    @Override
    public void updateSwimming() {
        if (this.abilities.flying) {
            this.setSwimming(false);
        }
        else {
            super.updateSwimming();
        }
    }
    
    protected boolean freeAt(final BlockPos fx) {
        return !this.level.getBlockState(fx).isSuffocating(this.level, fx);
    }
    
    @Override
    public float getSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
    
    public void checkMovementStatistics(final double double1, final double double2, final double double3) {
        if (this.isPassenger()) {
            return;
        }
        if (this.isSwimming()) {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double2 * double2 + double3 * double3) * 100.0f);
            if (integer8 > 0) {
                this.awardStat(Stats.SWIM_ONE_CM, integer8);
                this.causeFoodExhaustion(0.01f * integer8 * 0.01f);
            }
        }
        else if (this.isEyeInFluid(FluidTags.WATER)) {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double2 * double2 + double3 * double3) * 100.0f);
            if (integer8 > 0) {
                this.awardStat(Stats.WALK_UNDER_WATER_ONE_CM, integer8);
                this.causeFoodExhaustion(0.01f * integer8 * 0.01f);
            }
        }
        else if (this.isInWater()) {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double3 * double3) * 100.0f);
            if (integer8 > 0) {
                this.awardStat(Stats.WALK_ON_WATER_ONE_CM, integer8);
                this.causeFoodExhaustion(0.01f * integer8 * 0.01f);
            }
        }
        else if (this.onClimbable()) {
            if (double2 > 0.0) {
                this.awardStat(Stats.CLIMB_ONE_CM, (int)Math.round(double2 * 100.0));
            }
        }
        else if (this.onGround) {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double3 * double3) * 100.0f);
            if (integer8 > 0) {
                if (this.isSprinting()) {
                    this.awardStat(Stats.SPRINT_ONE_CM, integer8);
                    this.causeFoodExhaustion(0.1f * integer8 * 0.01f);
                }
                else if (this.isCrouching()) {
                    this.awardStat(Stats.CROUCH_ONE_CM, integer8);
                    this.causeFoodExhaustion(0.0f * integer8 * 0.01f);
                }
                else {
                    this.awardStat(Stats.WALK_ONE_CM, integer8);
                    this.causeFoodExhaustion(0.0f * integer8 * 0.01f);
                }
            }
        }
        else if (this.isFallFlying()) {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double2 * double2 + double3 * double3) * 100.0f);
            this.awardStat(Stats.AVIATE_ONE_CM, integer8);
        }
        else {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double3 * double3) * 100.0f);
            if (integer8 > 25) {
                this.awardStat(Stats.FLY_ONE_CM, integer8);
            }
        }
    }
    
    private void checkRidingStatistics(final double double1, final double double2, final double double3) {
        if (this.isPassenger()) {
            final int integer8 = Math.round(Mth.sqrt(double1 * double1 + double2 * double2 + double3 * double3) * 100.0f);
            if (integer8 > 0) {
                final Entity apx9 = this.getVehicle();
                if (apx9 instanceof AbstractMinecart) {
                    this.awardStat(Stats.MINECART_ONE_CM, integer8);
                }
                else if (apx9 instanceof Boat) {
                    this.awardStat(Stats.BOAT_ONE_CM, integer8);
                }
                else if (apx9 instanceof Pig) {
                    this.awardStat(Stats.PIG_ONE_CM, integer8);
                }
                else if (apx9 instanceof AbstractHorse) {
                    this.awardStat(Stats.HORSE_ONE_CM, integer8);
                }
                else if (apx9 instanceof Strider) {
                    this.awardStat(Stats.STRIDER_ONE_CM, integer8);
                }
            }
        }
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        if (this.abilities.mayfly) {
            return false;
        }
        if (float1 >= 2.0f) {
            this.awardStat(Stats.FALL_ONE_CM, (int)Math.round(float1 * 100.0));
        }
        return super.causeFallDamage(float1, float2);
    }
    
    public boolean tryToStartFallFlying() {
        if (!this.onGround && !this.isFallFlying() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION)) {
            final ItemStack bly2 = this.getItemBySlot(EquipmentSlot.CHEST);
            if (bly2.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(bly2)) {
                this.startFallFlying();
                return true;
            }
        }
        return false;
    }
    
    public void startFallFlying() {
        this.setSharedFlag(7, true);
    }
    
    public void stopFallFlying() {
        this.setSharedFlag(7, true);
        this.setSharedFlag(7, false);
    }
    
    @Override
    protected void doWaterSplashEffect() {
        if (!this.isSpectator()) {
            super.doWaterSplashEffect();
        }
    }
    
    @Override
    protected SoundEvent getFallDamageSound(final int integer) {
        if (integer > 4) {
            return SoundEvents.PLAYER_BIG_FALL;
        }
        return SoundEvents.PLAYER_SMALL_FALL;
    }
    
    @Override
    public void killed(final ServerLevel aag, final LivingEntity aqj) {
        this.awardStat(Stats.ENTITY_KILLED.get(aqj.getType()));
    }
    
    @Override
    public void makeStuckInBlock(final BlockState cee, final Vec3 dck) {
        if (!this.abilities.flying) {
            super.makeStuckInBlock(cee, dck);
        }
    }
    
    public void giveExperiencePoints(final int integer) {
        this.increaseScore(integer);
        this.experienceProgress += integer / (float)this.getXpNeededForNextLevel();
        this.totalExperience = Mth.clamp(this.totalExperience + integer, 0, Integer.MAX_VALUE);
        while (this.experienceProgress < 0.0f) {
            final float float3 = this.experienceProgress * this.getXpNeededForNextLevel();
            if (this.experienceLevel > 0) {
                this.giveExperienceLevels(-1);
                this.experienceProgress = 1.0f + float3 / this.getXpNeededForNextLevel();
            }
            else {
                this.giveExperienceLevels(-1);
                this.experienceProgress = 0.0f;
            }
        }
        while (this.experienceProgress >= 1.0f) {
            this.experienceProgress = (this.experienceProgress - 1.0f) * this.getXpNeededForNextLevel();
            this.giveExperienceLevels(1);
            this.experienceProgress /= this.getXpNeededForNextLevel();
        }
    }
    
    public int getEnchantmentSeed() {
        return this.enchantmentSeed;
    }
    
    public void onEnchantmentPerformed(final ItemStack bly, final int integer) {
        this.experienceLevel -= integer;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0f;
            this.totalExperience = 0;
        }
        this.enchantmentSeed = this.random.nextInt();
    }
    
    public void giveExperienceLevels(final int integer) {
        this.experienceLevel += integer;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0f;
            this.totalExperience = 0;
        }
        if (integer > 0 && this.experienceLevel % 5 == 0 && this.lastLevelUpTime < this.tickCount - 100.0f) {
            final float float3 = (this.experienceLevel > 30) ? 1.0f : (this.experienceLevel / 30.0f);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_LEVELUP, this.getSoundSource(), float3 * 0.75f, 1.0f);
            this.lastLevelUpTime = this.tickCount;
        }
    }
    
    public int getXpNeededForNextLevel() {
        if (this.experienceLevel >= 30) {
            return 112 + (this.experienceLevel - 30) * 9;
        }
        if (this.experienceLevel >= 15) {
            return 37 + (this.experienceLevel - 15) * 5;
        }
        return 7 + this.experienceLevel * 2;
    }
    
    public void causeFoodExhaustion(final float float1) {
        if (this.abilities.invulnerable) {
            return;
        }
        if (!this.level.isClientSide) {
            this.foodData.addExhaustion(float1);
        }
    }
    
    public FoodData getFoodData() {
        return this.foodData;
    }
    
    public boolean canEat(final boolean boolean1) {
        return this.abilities.invulnerable || boolean1 || this.foodData.needsFood();
    }
    
    public boolean isHurt() {
        return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
    }
    
    public boolean mayBuild() {
        return this.abilities.mayBuild;
    }
    
    public boolean mayUseItemAt(final BlockPos fx, final Direction gc, final ItemStack bly) {
        if (this.abilities.mayBuild) {
            return true;
        }
        final BlockPos fx2 = fx.relative(gc.getOpposite());
        final BlockInWorld cei6 = new BlockInWorld(this.level, fx2, false);
        return bly.hasAdventureModePlaceTagForBlock(this.level.getTagManager(), cei6);
    }
    
    @Override
    protected int getExperienceReward(final Player bft) {
        if (this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || this.isSpectator()) {
            return 0;
        }
        final int integer3 = this.experienceLevel * 7;
        if (integer3 > 100) {
            return 100;
        }
        return integer3;
    }
    
    @Override
    protected boolean isAlwaysExperienceDropper() {
        return true;
    }
    
    @Override
    public boolean shouldShowName() {
        return true;
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return !this.abilities.flying && (!this.onGround || !this.isDiscrete());
    }
    
    public void onUpdateAbilities() {
    }
    
    public void setGameMode(final GameType brr) {
    }
    
    @Override
    public Component getName() {
        return new TextComponent(this.gameProfile.getName());
    }
    
    public PlayerEnderChestContainer getEnderChestInventory() {
        return this.enderChestInventory;
    }
    
    @Override
    public ItemStack getItemBySlot(final EquipmentSlot aqc) {
        if (aqc == EquipmentSlot.MAINHAND) {
            return this.inventory.getSelected();
        }
        if (aqc == EquipmentSlot.OFFHAND) {
            return this.inventory.offhand.get(0);
        }
        if (aqc.getType() == EquipmentSlot.Type.ARMOR) {
            return this.inventory.armor.get(aqc.getIndex());
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public void setItemSlot(final EquipmentSlot aqc, final ItemStack bly) {
        if (aqc == EquipmentSlot.MAINHAND) {
            this.playEquipSound(bly);
            this.inventory.items.set(this.inventory.selected, bly);
        }
        else if (aqc == EquipmentSlot.OFFHAND) {
            this.playEquipSound(bly);
            this.inventory.offhand.set(0, bly);
        }
        else if (aqc.getType() == EquipmentSlot.Type.ARMOR) {
            this.playEquipSound(bly);
            this.inventory.armor.set(aqc.getIndex(), bly);
        }
    }
    
    public boolean addItem(final ItemStack bly) {
        this.playEquipSound(bly);
        return this.inventory.add(bly);
    }
    
    @Override
    public Iterable<ItemStack> getHandSlots() {
        return (Iterable<ItemStack>)Lists.newArrayList((Object[])new ItemStack[] { this.getMainHandItem(), this.getOffhandItem() });
    }
    
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return (Iterable<ItemStack>)this.inventory.armor;
    }
    
    public boolean setEntityOnShoulder(final CompoundTag md) {
        if (this.isPassenger() || !this.onGround || this.isInWater()) {
            return false;
        }
        if (this.getShoulderEntityLeft().isEmpty()) {
            this.setShoulderEntityLeft(md);
            this.timeEntitySatOnShoulder = this.level.getGameTime();
            return true;
        }
        if (this.getShoulderEntityRight().isEmpty()) {
            this.setShoulderEntityRight(md);
            this.timeEntitySatOnShoulder = this.level.getGameTime();
            return true;
        }
        return false;
    }
    
    protected void removeEntitiesOnShoulder() {
        if (this.timeEntitySatOnShoulder + 20L < this.level.getGameTime()) {
            this.respawnEntityOnShoulder(this.getShoulderEntityLeft());
            this.setShoulderEntityLeft(new CompoundTag());
            this.respawnEntityOnShoulder(this.getShoulderEntityRight());
            this.setShoulderEntityRight(new CompoundTag());
        }
    }
    
    private void respawnEntityOnShoulder(final CompoundTag md) {
        if (!this.level.isClientSide && !md.isEmpty()) {
            EntityType.create(md, this.level).ifPresent(apx -> {
                if (apx instanceof TamableAnimal) {
                    ((TamableAnimal)apx).setOwnerUUID(this.uuid);
                }
                apx.setPos(this.getX(), this.getY() + 0.699999988079071, this.getZ());
                ((ServerLevel)this.level).addWithUUID(apx);
            });
        }
    }
    
    @Override
    public abstract boolean isSpectator();
    
    @Override
    public boolean isSwimming() {
        return !this.abilities.flying && !this.isSpectator() && super.isSwimming();
    }
    
    public abstract boolean isCreative();
    
    @Override
    public boolean isPushedByFluid() {
        return !this.abilities.flying;
    }
    
    public Scoreboard getScoreboard() {
        return this.level.getScoreboard();
    }
    
    @Override
    public Component getDisplayName() {
        final MutableComponent nx2 = PlayerTeam.formatNameForTeam(this.getTeam(), this.getName());
        return this.decorateDisplayNameComponent(nx2);
    }
    
    private MutableComponent decorateDisplayNameComponent(final MutableComponent nx) {
        final String string3 = this.getGameProfile().getName();
        return nx.withStyle((UnaryOperator<Style>)(ob -> ob.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + string3 + " ")).withHoverEvent(this.createHoverEvent()).withInsertion(string3)));
    }
    
    @Override
    public String getScoreboardName() {
        return this.getGameProfile().getName();
    }
    
    public float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        switch (aqu) {
            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK: {
                return 0.4f;
            }
            case CROUCHING: {
                return 1.27f;
            }
            default: {
                return 1.62f;
            }
        }
    }
    
    @Override
    public void setAbsorptionAmount(float float1) {
        if (float1 < 0.0f) {
            float1 = 0.0f;
        }
        this.getEntityData().<Float>set(Player.DATA_PLAYER_ABSORPTION_ID, float1);
    }
    
    @Override
    public float getAbsorptionAmount() {
        return this.getEntityData().<Float>get(Player.DATA_PLAYER_ABSORPTION_ID);
    }
    
    public static UUID createPlayerUUID(final GameProfile gameProfile) {
        UUID uUID2 = gameProfile.getId();
        if (uUID2 == null) {
            uUID2 = createPlayerUUID(gameProfile.getName());
        }
        return uUID2;
    }
    
    public static UUID createPlayerUUID(final String string) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + string).getBytes(StandardCharsets.UTF_8));
    }
    
    public boolean isModelPartShown(final PlayerModelPart bfu) {
        return (this.getEntityData().<Byte>get(Player.DATA_PLAYER_MODE_CUSTOMISATION) & bfu.getMask()) == bfu.getMask();
    }
    
    @Override
    public boolean setSlot(final int integer, final ItemStack bly) {
        if (integer >= 0 && integer < this.inventory.items.size()) {
            this.inventory.setItem(integer, bly);
            return true;
        }
        EquipmentSlot aqc4;
        if (integer == 100 + EquipmentSlot.HEAD.getIndex()) {
            aqc4 = EquipmentSlot.HEAD;
        }
        else if (integer == 100 + EquipmentSlot.CHEST.getIndex()) {
            aqc4 = EquipmentSlot.CHEST;
        }
        else if (integer == 100 + EquipmentSlot.LEGS.getIndex()) {
            aqc4 = EquipmentSlot.LEGS;
        }
        else if (integer == 100 + EquipmentSlot.FEET.getIndex()) {
            aqc4 = EquipmentSlot.FEET;
        }
        else {
            aqc4 = null;
        }
        if (integer == 98) {
            this.setItemSlot(EquipmentSlot.MAINHAND, bly);
            return true;
        }
        if (integer == 99) {
            this.setItemSlot(EquipmentSlot.OFFHAND, bly);
            return true;
        }
        if (aqc4 != null) {
            if (!bly.isEmpty()) {
                if (bly.getItem() instanceof ArmorItem || bly.getItem() instanceof ElytraItem) {
                    if (Mob.getEquipmentSlotForItem(bly) != aqc4) {
                        return false;
                    }
                }
                else if (aqc4 != EquipmentSlot.HEAD) {
                    return false;
                }
            }
            this.inventory.setItem(aqc4.getIndex() + this.inventory.items.size(), bly);
            return true;
        }
        final int integer2 = integer - 200;
        if (integer2 >= 0 && integer2 < this.enderChestInventory.getContainerSize()) {
            this.enderChestInventory.setItem(integer2, bly);
            return true;
        }
        return false;
    }
    
    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }
    
    public void setReducedDebugInfo(final boolean boolean1) {
        this.reducedDebugInfo = boolean1;
    }
    
    @Override
    public void setRemainingFireTicks(final int integer) {
        super.setRemainingFireTicks(this.abilities.invulnerable ? Math.min(integer, 1) : integer);
    }
    
    @Override
    public HumanoidArm getMainArm() {
        return (this.entityData.<Byte>get(Player.DATA_PLAYER_MAIN_HAND) == 0) ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }
    
    public void setMainArm(final HumanoidArm aqf) {
        this.entityData.<Byte>set(Player.DATA_PLAYER_MAIN_HAND, (byte)((aqf != HumanoidArm.LEFT) ? 1 : 0));
    }
    
    public CompoundTag getShoulderEntityLeft() {
        return this.entityData.<CompoundTag>get(Player.DATA_SHOULDER_LEFT);
    }
    
    protected void setShoulderEntityLeft(final CompoundTag md) {
        this.entityData.<CompoundTag>set(Player.DATA_SHOULDER_LEFT, md);
    }
    
    public CompoundTag getShoulderEntityRight() {
        return this.entityData.<CompoundTag>get(Player.DATA_SHOULDER_RIGHT);
    }
    
    protected void setShoulderEntityRight(final CompoundTag md) {
        this.entityData.<CompoundTag>set(Player.DATA_SHOULDER_RIGHT, md);
    }
    
    public float getCurrentItemAttackStrengthDelay() {
        return (float)(1.0 / this.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0);
    }
    
    public float getAttackStrengthScale(final float float1) {
        return Mth.clamp((this.attackStrengthTicker + float1) / this.getCurrentItemAttackStrengthDelay(), 0.0f, 1.0f);
    }
    
    public void resetAttackStrengthTicker() {
        this.attackStrengthTicker = 0;
    }
    
    public ItemCooldowns getCooldowns() {
        return this.cooldowns;
    }
    
    @Override
    protected float getBlockSpeedFactor() {
        return (this.abilities.flying || this.isFallFlying()) ? 1.0f : super.getBlockSpeedFactor();
    }
    
    public float getLuck() {
        return (float)this.getAttributeValue(Attributes.LUCK);
    }
    
    public boolean canUseGameMasterBlocks() {
        return this.abilities.instabuild && this.getPermissionLevel() >= 2;
    }
    
    @Override
    public boolean canTakeItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = Mob.getEquipmentSlotForItem(bly);
        return this.getItemBySlot(aqc3).isEmpty();
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return (EntityDimensions)Player.POSES.getOrDefault(aqu, Player.STANDING_DIMENSIONS);
    }
    
    @Override
    public ImmutableList<Pose> getDismountPoses() {
        return (ImmutableList<Pose>)ImmutableList.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING);
    }
    
    @Override
    public ItemStack getProjectile(final ItemStack bly) {
        if (!(bly.getItem() instanceof ProjectileWeaponItem)) {
            return ItemStack.EMPTY;
        }
        Predicate<ItemStack> predicate3 = ((ProjectileWeaponItem)bly.getItem()).getSupportedHeldProjectiles();
        final ItemStack bly2 = ProjectileWeaponItem.getHeldProjectile(this, predicate3);
        if (!bly2.isEmpty()) {
            return bly2;
        }
        predicate3 = ((ProjectileWeaponItem)bly.getItem()).getAllSupportedProjectiles();
        for (int integer5 = 0; integer5 < this.inventory.getContainerSize(); ++integer5) {
            final ItemStack bly3 = this.inventory.getItem(integer5);
            if (predicate3.test(bly3)) {
                return bly3;
            }
        }
        return this.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack eat(final Level bru, final ItemStack bly) {
        this.getFoodData().eat(bly.getItem(), bly);
        this.awardStat(Stats.ITEM_USED.get(bly.getItem()));
        bru.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5f, bru.random.nextFloat() * 0.1f + 0.9f);
        if (this instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)this, bly);
        }
        return super.eat(bru, bly);
    }
    
    @Override
    protected boolean shouldRemoveSoulSpeed(final BlockState cee) {
        return this.abilities.flying || super.shouldRemoveSoulSpeed(cee);
    }
    
    @Override
    public Vec3 getRopeHoldPosition(final float float1) {
        final double double3 = 0.22 * ((this.getMainArm() == HumanoidArm.RIGHT) ? -1.0 : 1.0);
        final float float2 = Mth.lerp(float1 * 0.5f, this.xRot, this.xRotO) * 0.017453292f;
        final float float3 = Mth.lerp(float1, this.yBodyRotO, this.yBodyRot) * 0.017453292f;
        if (this.isFallFlying() || this.isAutoSpinAttack()) {
            final Vec3 dck7 = this.getViewVector(float1);
            final Vec3 dck8 = this.getDeltaMovement();
            final double double4 = Entity.getHorizontalDistanceSqr(dck8);
            final double double5 = Entity.getHorizontalDistanceSqr(dck7);
            float float4;
            if (double4 > 0.0 && double5 > 0.0) {
                final double double6 = (dck8.x * dck7.x + dck8.z * dck7.z) / Math.sqrt(double4 * double5);
                final double double7 = dck8.x * dck7.z - dck8.z * dck7.x;
                float4 = (float)(Math.signum(double7) * Math.acos(double6));
            }
            else {
                float4 = 0.0f;
            }
            return this.getPosition(float1).add(new Vec3(double3, -0.11, 0.85).zRot(-float4).xRot(-float2).yRot(-float3));
        }
        if (this.isVisuallySwimming()) {
            return this.getPosition(float1).add(new Vec3(double3, 0.2, -0.15).xRot(-float2).yRot(-float3));
        }
        final double double8 = this.getBoundingBox().getYsize() - 1.0;
        final double double4 = this.isCrouching() ? -0.2 : 0.07;
        return this.getPosition(float1).add(new Vec3(double3, double8, double4).yRot(-float3));
    }
    
    static {
        STANDING_DIMENSIONS = EntityDimensions.scalable(0.6f, 1.8f);
        POSES = (Map)ImmutableMap.builder().put(Pose.STANDING, Player.STANDING_DIMENSIONS).put(Pose.SLEEPING, Player.SLEEPING_DIMENSIONS).put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6f, 0.6f)).put(Pose.SWIMMING, EntityDimensions.scalable(0.6f, 0.6f)).put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6f, 0.6f)).put(Pose.CROUCHING, EntityDimensions.scalable(0.6f, 1.5f)).put(Pose.DYING, EntityDimensions.fixed(0.2f, 0.2f)).build();
        DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.<Float>defineId(Player.class, EntityDataSerializers.FLOAT);
        DATA_SCORE_ID = SynchedEntityData.<Integer>defineId(Player.class, EntityDataSerializers.INT);
        DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.<Byte>defineId(Player.class, EntityDataSerializers.BYTE);
        DATA_PLAYER_MAIN_HAND = SynchedEntityData.<Byte>defineId(Player.class, EntityDataSerializers.BYTE);
        DATA_SHOULDER_LEFT = SynchedEntityData.<CompoundTag>defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
        DATA_SHOULDER_RIGHT = SynchedEntityData.<CompoundTag>defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    }
    
    public enum BedSleepingProblem {
        NOT_POSSIBLE_HERE, 
        NOT_POSSIBLE_NOW((Component)new TranslatableComponent("block.minecraft.bed.no_sleep")), 
        TOO_FAR_AWAY((Component)new TranslatableComponent("block.minecraft.bed.too_far_away")), 
        OBSTRUCTED((Component)new TranslatableComponent("block.minecraft.bed.obstructed")), 
        OTHER_PROBLEM, 
        NOT_SAFE((Component)new TranslatableComponent("block.minecraft.bed.not_safe"));
        
        @Nullable
        private final Component message;
        
        private BedSleepingProblem() {
            this.message = null;
        }
        
        private BedSleepingProblem(final Component nr) {
            this.message = nr;
        }
        
        @Nullable
        public Component getMessage() {
            return this.message;
        }
    }
}
