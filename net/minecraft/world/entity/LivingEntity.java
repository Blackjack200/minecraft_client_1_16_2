package net.minecraft.world.entity;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.UseAnim;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.BlockUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.phys.AABB;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import com.google.common.collect.Lists;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.entity.animal.FlyingAnimal;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionUtils;
import java.util.ConcurrentModificationException;
import net.minecraft.world.scores.PlayerTeam;
import com.mojang.serialization.DataResult;
import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ArmorItem;
import java.util.Random;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import java.util.function.Consumer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;
import com.google.common.base.Objects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import com.google.common.collect.Maps;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import java.util.Map;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public abstract class LivingEntity extends Entity {
    private static final UUID SPEED_MODIFIER_SPRINTING_UUID;
    private static final UUID SPEED_MODIFIER_SOUL_SPEED_UUID;
    private static final AttributeModifier SPEED_MODIFIER_SPRINTING;
    protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID;
    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;
    private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;
    private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID;
    private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID;
    private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID;
    protected static final EntityDimensions SLEEPING_DIMENSIONS;
    private final AttributeMap attributes;
    private final CombatTracker combatTracker;
    private final Map<MobEffect, MobEffectInstance> activeEffects;
    private final NonNullList<ItemStack> lastHandItemStacks;
    private final NonNullList<ItemStack> lastArmorItemStacks;
    public boolean swinging;
    public InteractionHand swingingArm;
    public int swingTime;
    public int removeArrowTime;
    public int removeStingerTime;
    public int hurtTime;
    public int hurtDuration;
    public float hurtDir;
    public int deathTime;
    public float oAttackAnim;
    public float attackAnim;
    protected int attackStrengthTicker;
    public float animationSpeedOld;
    public float animationSpeed;
    public float animationPosition;
    public final int invulnerableDuration = 20;
    public final float timeOffs;
    public final float rotA;
    public float yBodyRot;
    public float yBodyRotO;
    public float yHeadRot;
    public float yHeadRotO;
    public float flyingSpeed;
    @Nullable
    protected Player lastHurtByPlayer;
    protected int lastHurtByPlayerTime;
    protected boolean dead;
    protected int noActionTime;
    protected float oRun;
    protected float run;
    protected float animStep;
    protected float animStepO;
    protected float rotOffs;
    protected int deathScore;
    protected float lastHurt;
    protected boolean jumping;
    public float xxa;
    public float yya;
    public float zza;
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;
    protected double lyHeadRot;
    protected int lerpHeadSteps;
    private boolean effectsDirty;
    @Nullable
    private LivingEntity lastHurtByMob;
    private int lastHurtByMobTimestamp;
    private LivingEntity lastHurtMob;
    private int lastHurtMobTimestamp;
    private float speed;
    private int noJumpDelay;
    private float absorptionAmount;
    protected ItemStack useItem;
    protected int useItemRemaining;
    protected int fallFlyTicks;
    private BlockPos lastPos;
    private Optional<BlockPos> lastClimbablePos;
    private DamageSource lastDamageSource;
    private long lastDamageStamp;
    protected int autoSpinAttackTicks;
    private float swimAmount;
    private float swimAmountO;
    protected Brain<?> brain;
    
    protected LivingEntity(final EntityType<? extends LivingEntity> aqb, final Level bru) {
        super(aqb, bru);
        this.combatTracker = new CombatTracker(this);
        this.activeEffects = (Map<MobEffect, MobEffectInstance>)Maps.newHashMap();
        this.lastHandItemStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
        this.lastArmorItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
        this.flyingSpeed = 0.02f;
        this.effectsDirty = true;
        this.useItem = ItemStack.EMPTY;
        this.lastClimbablePos = (Optional<BlockPos>)Optional.empty();
        this.attributes = new AttributeMap(DefaultAttributes.getSupplier(aqb));
        this.setHealth(this.getMaxHealth());
        this.blocksBuilding = true;
        this.rotA = (float)((Math.random() + 1.0) * 0.009999999776482582);
        this.reapplyPosition();
        this.timeOffs = (float)Math.random() * 12398.0f;
        this.yRot = (float)(Math.random() * 6.2831854820251465);
        this.yHeadRot = this.yRot;
        this.maxUpStep = 0.6f;
        final NbtOps mo4 = NbtOps.INSTANCE;
        this.brain = this.makeBrain(new Dynamic((DynamicOps)mo4, mo4.createMap((Map)ImmutableMap.of(mo4.createString("memories"), mo4.emptyMap()))));
    }
    
    public Brain<?> getBrain() {
        return this.brain;
    }
    
    protected Brain.Provider<?> brainProvider() {
        return Brain.provider(ImmutableList.of(), (java.util.Collection<? extends SensorType<? extends Sensor<?>>>)ImmutableList.of());
    }
    
    protected Brain<?> makeBrain(final Dynamic<?> dynamic) {
        return this.brainProvider().makeBrain(dynamic);
    }
    
    @Override
    public void kill() {
        this.hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }
    
    public boolean canAttackType(final EntityType<?> aqb) {
        return true;
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<Byte>define(LivingEntity.DATA_LIVING_ENTITY_FLAGS, (Byte)0);
        this.entityData.<Integer>define(LivingEntity.DATA_EFFECT_COLOR_ID, 0);
        this.entityData.<Boolean>define(LivingEntity.DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.<Integer>define(LivingEntity.DATA_ARROW_COUNT_ID, 0);
        this.entityData.<Integer>define(LivingEntity.DATA_STINGER_COUNT_ID, 0);
        this.entityData.<Float>define(LivingEntity.DATA_HEALTH_ID, 1.0f);
        this.entityData.<Optional<BlockPos>>define(LivingEntity.SLEEPING_POS_ID, (Optional<BlockPos>)Optional.empty());
    }
    
    public static AttributeSupplier.Builder createLivingAttributes() {
        return AttributeSupplier.builder().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE).add(Attributes.MOVEMENT_SPEED).add(Attributes.ARMOR).add(Attributes.ARMOR_TOUGHNESS);
    }
    
    @Override
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
        if (!this.isInWater()) {
            this.updateInWaterStateAndDoWaterCurrentPushing();
        }
        if (!this.level.isClientSide && boolean2 && this.fallDistance > 0.0f) {
            this.removeSoulSpeed();
            this.tryAddSoulSpeed();
        }
        if (!this.level.isClientSide && this.fallDistance > 3.0f && boolean2) {
            final float float7 = (float)Mth.ceil(this.fallDistance - 3.0f);
            if (!cee.isAir()) {
                final double double2 = Math.min((double)(0.2f + float7 / 15.0f), 2.5);
                final int integer10 = (int)(150.0 * double2);
                ((ServerLevel)this.level).<BlockParticleOption>sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, cee), this.getX(), this.getY(), this.getZ(), integer10, 0.0, 0.0, 0.0, 0.15000000596046448);
            }
        }
        super.checkFallDamage(double1, boolean2, cee, fx);
    }
    
    public boolean canBreatheUnderwater() {
        return this.getMobType() == MobType.UNDEAD;
    }
    
    public float getSwimAmount(final float float1) {
        return Mth.lerp(float1, this.swimAmountO, this.swimAmount);
    }
    
    @Override
    public void baseTick() {
        this.oAttackAnim = this.attackAnim;
        if (this.firstTick) {
            this.getSleepingPos().ifPresent(this::setPosToBed);
        }
        if (this.canSpawnSoulSpeedParticle()) {
            this.spawnSoulSpeedParticle();
        }
        super.baseTick();
        this.level.getProfiler().push("livingEntityBaseTick");
        final boolean boolean2 = this instanceof Player;
        if (this.isAlive()) {
            if (this.isInWall()) {
                this.hurt(DamageSource.IN_WALL, 1.0f);
            }
            else if (boolean2 && !this.level.getWorldBorder().isWithinBounds(this.getBoundingBox())) {
                final double double3 = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
                if (double3 < 0.0) {
                    final double double4 = this.level.getWorldBorder().getDamagePerBlock();
                    if (double4 > 0.0) {
                        this.hurt(DamageSource.IN_WALL, (float)Math.max(1, Mth.floor(-double3 * double4)));
                    }
                }
            }
        }
        if (this.fireImmune() || this.level.isClientSide) {
            this.clearFire();
        }
        final boolean boolean3 = boolean2 && ((Player)this).abilities.invulnerable;
        if (this.isAlive()) {
            if (this.isEyeInFluid(FluidTags.WATER) && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)) {
                if (!this.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(this) && !boolean3) {
                    this.setAirSupply(this.decreaseAirSupply(this.getAirSupply()));
                    if (this.getAirSupply() == -20) {
                        this.setAirSupply(0);
                        final Vec3 dck4 = this.getDeltaMovement();
                        for (int integer5 = 0; integer5 < 8; ++integer5) {
                            final double double5 = this.random.nextDouble() - this.random.nextDouble();
                            final double double6 = this.random.nextDouble() - this.random.nextDouble();
                            final double double7 = this.random.nextDouble() - this.random.nextDouble();
                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + double5, this.getY() + double6, this.getZ() + double7, dck4.x, dck4.y, dck4.z);
                        }
                        this.hurt(DamageSource.DROWN, 2.0f);
                    }
                }
                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().rideableUnderWater()) {
                    this.stopRiding();
                }
            }
            else if (this.getAirSupply() < this.getMaxAirSupply()) {
                this.setAirSupply(this.increaseAirSupply(this.getAirSupply()));
            }
            if (!this.level.isClientSide) {
                final BlockPos fx4 = this.blockPosition();
                if (!Objects.equal(this.lastPos, fx4)) {
                    this.onChangedBlock(this.lastPos = fx4);
                }
            }
        }
        if (this.isAlive() && this.isInWaterRainOrBubble()) {
            this.clearFire();
        }
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.invulnerableTime > 0 && !(this instanceof ServerPlayer)) {
            --this.invulnerableTime;
        }
        if (this.isDeadOrDying()) {
            this.tickDeath();
        }
        if (this.lastHurtByPlayerTime > 0) {
            --this.lastHurtByPlayerTime;
        }
        else {
            this.lastHurtByPlayer = null;
        }
        if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
            this.lastHurtMob = null;
        }
        if (this.lastHurtByMob != null) {
            if (!this.lastHurtByMob.isAlive()) {
                this.setLastHurtByMob(null);
            }
            else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
                this.setLastHurtByMob(null);
            }
        }
        this.tickEffects();
        this.animStepO = this.animStep;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
        this.level.getProfiler().pop();
    }
    
    public boolean canSpawnSoulSpeedParticle() {
        return this.tickCount % 5 == 0 && this.getDeltaMovement().x != 0.0 && this.getDeltaMovement().z != 0.0 && !this.isSpectator() && EnchantmentHelper.hasSoulSpeed(this) && this.onSoulSpeedBlock();
    }
    
    protected void spawnSoulSpeedParticle() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.level.addParticle(ParticleTypes.SOUL, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + 0.1, this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), dck2.x * -0.2, 0.1, dck2.z * -0.2);
        final float float3 = (this.random.nextFloat() * 0.4f + this.random.nextFloat() > 0.9f) ? 0.6f : 0.0f;
        this.playSound(SoundEvents.SOUL_ESCAPE, float3, 0.6f + this.random.nextFloat() * 0.4f);
    }
    
    protected boolean onSoulSpeedBlock() {
        return this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.SOUL_SPEED_BLOCKS);
    }
    
    @Override
    protected float getBlockSpeedFactor() {
        if (this.onSoulSpeedBlock() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, this) > 0) {
            return 1.0f;
        }
        return super.getBlockSpeedFactor();
    }
    
    protected boolean shouldRemoveSoulSpeed(final BlockState cee) {
        return !cee.isAir() || this.isFallFlying();
    }
    
    protected void removeSoulSpeed() {
        final AttributeInstance are2 = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (are2 == null) {
            return;
        }
        if (are2.getModifier(LivingEntity.SPEED_MODIFIER_SOUL_SPEED_UUID) != null) {
            are2.removeModifier(LivingEntity.SPEED_MODIFIER_SOUL_SPEED_UUID);
        }
    }
    
    protected void tryAddSoulSpeed() {
        if (!this.getBlockStateOn().isAir()) {
            final int integer2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, this);
            if (integer2 > 0 && this.onSoulSpeedBlock()) {
                final AttributeInstance are3 = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (are3 == null) {
                    return;
                }
                are3.addTransientModifier(new AttributeModifier(LivingEntity.SPEED_MODIFIER_SOUL_SPEED_UUID, "Soul speed boost", (double)(0.03f * (1.0f + integer2 * 0.35f)), AttributeModifier.Operation.ADDITION));
                if (this.getRandom().nextFloat() < 0.04f) {
                    final ItemStack bly4 = this.getItemBySlot(EquipmentSlot.FEET);
                    bly4.<LivingEntity>hurtAndBreak(1, this, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.FEET)));
                }
            }
        }
    }
    
    protected void onChangedBlock(final BlockPos fx) {
        final int integer3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, this);
        if (integer3 > 0) {
            FrostWalkerEnchantment.onEntityMoved(this, this.level, fx, integer3);
        }
        if (this.shouldRemoveSoulSpeed(this.getBlockStateOn())) {
            this.removeSoulSpeed();
        }
        this.tryAddSoulSpeed();
    }
    
    public boolean isBaby() {
        return false;
    }
    
    public float getScale() {
        return this.isBaby() ? 0.5f : 1.0f;
    }
    
    protected boolean isAffectedByFluids() {
        return true;
    }
    
    @Override
    public boolean rideableUnderWater() {
        return false;
    }
    
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove();
            for (int integer2 = 0; integer2 < 20; ++integer2) {
                final double double3 = this.random.nextGaussian() * 0.02;
                final double double4 = this.random.nextGaussian() * 0.02;
                final double double5 = this.random.nextGaussian() * 0.02;
                this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), double3, double4, double5);
            }
        }
    }
    
    protected boolean shouldDropExperience() {
        return !this.isBaby();
    }
    
    protected boolean shouldDropLoot() {
        return !this.isBaby();
    }
    
    protected int decreaseAirSupply(final int integer) {
        final int integer2 = EnchantmentHelper.getRespiration(this);
        if (integer2 > 0 && this.random.nextInt(integer2 + 1) > 0) {
            return integer;
        }
        return integer - 1;
    }
    
    protected int increaseAirSupply(final int integer) {
        return Math.min(integer + 4, this.getMaxAirSupply());
    }
    
    protected int getExperienceReward(final Player bft) {
        return 0;
    }
    
    protected boolean isAlwaysExperienceDropper() {
        return false;
    }
    
    public Random getRandom() {
        return this.random;
    }
    
    @Nullable
    public LivingEntity getLastHurtByMob() {
        return this.lastHurtByMob;
    }
    
    public int getLastHurtByMobTimestamp() {
        return this.lastHurtByMobTimestamp;
    }
    
    public void setLastHurtByPlayer(@Nullable final Player bft) {
        this.lastHurtByPlayer = bft;
        this.lastHurtByPlayerTime = this.tickCount;
    }
    
    public void setLastHurtByMob(@Nullable final LivingEntity aqj) {
        this.lastHurtByMob = aqj;
        this.lastHurtByMobTimestamp = this.tickCount;
    }
    
    @Nullable
    public LivingEntity getLastHurtMob() {
        return this.lastHurtMob;
    }
    
    public int getLastHurtMobTimestamp() {
        return this.lastHurtMobTimestamp;
    }
    
    public void setLastHurtMob(final Entity apx) {
        if (apx instanceof LivingEntity) {
            this.lastHurtMob = (LivingEntity)apx;
        }
        else {
            this.lastHurtMob = null;
        }
        this.lastHurtMobTimestamp = this.tickCount;
    }
    
    public int getNoActionTime() {
        return this.noActionTime;
    }
    
    public void setNoActionTime(final int integer) {
        this.noActionTime = integer;
    }
    
    protected void playEquipSound(final ItemStack bly) {
        if (bly.isEmpty()) {
            return;
        }
        SoundEvent adn3 = SoundEvents.ARMOR_EQUIP_GENERIC;
        final Item blu4 = bly.getItem();
        if (blu4 instanceof ArmorItem) {
            adn3 = ((ArmorItem)blu4).getMaterial().getEquipSound();
        }
        else if (blu4 == Items.ELYTRA) {
            adn3 = SoundEvents.ARMOR_EQUIP_ELYTRA;
        }
        this.playSound(adn3, 1.0f, 1.0f);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        md.putFloat("Health", this.getHealth());
        md.putShort("HurtTime", (short)this.hurtTime);
        md.putInt("HurtByTimestamp", this.lastHurtByMobTimestamp);
        md.putShort("DeathTime", (short)this.deathTime);
        md.putFloat("AbsorptionAmount", this.getAbsorptionAmount());
        md.put("Attributes", (net.minecraft.nbt.Tag)this.getAttributes().save());
        if (!this.activeEffects.isEmpty()) {
            final ListTag mj3 = new ListTag();
            for (final MobEffectInstance apr5 : this.activeEffects.values()) {
                mj3.add(apr5.save(new CompoundTag()));
            }
            md.put("ActiveEffects", (net.minecraft.nbt.Tag)mj3);
        }
        md.putBoolean("FallFlying", this.isFallFlying());
        this.getSleepingPos().ifPresent(fx -> {
            md.putInt("SleepingX", fx.getX());
            md.putInt("SleepingY", fx.getY());
            md.putInt("SleepingZ", fx.getZ());
        });
        final DataResult<net.minecraft.nbt.Tag> dataResult3 = this.brain.<net.minecraft.nbt.Tag>serializeStart((com.mojang.serialization.DynamicOps<net.minecraft.nbt.Tag>)NbtOps.INSTANCE);
        dataResult3.resultOrPartial(LivingEntity.LOGGER::error).ifPresent(mt -> md.put("Brain", mt));
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        this.setAbsorptionAmount(md.getFloat("AbsorptionAmount"));
        if (md.contains("Attributes", 9) && this.level != null && !this.level.isClientSide) {
            this.getAttributes().load(md.getList("Attributes", 10));
        }
        if (md.contains("ActiveEffects", 9)) {
            final ListTag mj3 = md.getList("ActiveEffects", 10);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                final CompoundTag md2 = mj3.getCompound(integer4);
                final MobEffectInstance apr6 = MobEffectInstance.load(md2);
                if (apr6 != null) {
                    this.activeEffects.put(apr6.getEffect(), apr6);
                }
            }
        }
        if (md.contains("Health", 99)) {
            this.setHealth(md.getFloat("Health"));
        }
        this.hurtTime = md.getShort("HurtTime");
        this.deathTime = md.getShort("DeathTime");
        this.lastHurtByMobTimestamp = md.getInt("HurtByTimestamp");
        if (md.contains("Team", 8)) {
            final String string3 = md.getString("Team");
            final PlayerTeam ddi4 = this.level.getScoreboard().getPlayerTeam(string3);
            final boolean boolean5 = ddi4 != null && this.level.getScoreboard().addPlayerToTeam(this.getStringUUID(), ddi4);
            if (!boolean5) {
                LivingEntity.LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", string3);
            }
        }
        if (md.getBoolean("FallFlying")) {
            this.setSharedFlag(7, true);
        }
        if (md.contains("SleepingX", 99) && md.contains("SleepingY", 99) && md.contains("SleepingZ", 99)) {
            final BlockPos fx3 = new BlockPos(md.getInt("SleepingX"), md.getInt("SleepingY"), md.getInt("SleepingZ"));
            this.setSleepingPos(fx3);
            this.entityData.<Pose>set(LivingEntity.DATA_POSE, Pose.SLEEPING);
            if (!this.firstTick) {
                this.setPosToBed(fx3);
            }
        }
        if (md.contains("Brain", 10)) {
            this.brain = this.makeBrain(new Dynamic((DynamicOps)NbtOps.INSTANCE, md.get("Brain")));
        }
    }
    
    protected void tickEffects() {
        final Iterator<MobEffect> iterator2 = (Iterator<MobEffect>)this.activeEffects.keySet().iterator();
        try {
            while (iterator2.hasNext()) {
                final MobEffect app3 = (MobEffect)iterator2.next();
                final MobEffectInstance apr4 = (MobEffectInstance)this.activeEffects.get(app3);
                if (!apr4.tick(this, () -> this.onEffectUpdated(apr4, true))) {
                    if (this.level.isClientSide) {
                        continue;
                    }
                    iterator2.remove();
                    this.onEffectRemoved(apr4);
                }
                else {
                    if (apr4.getDuration() % 600 != 0) {
                        continue;
                    }
                    this.onEffectUpdated(apr4, false);
                }
            }
        }
        catch (ConcurrentModificationException ex) {}
        if (this.effectsDirty) {
            if (!this.level.isClientSide) {
                this.updateInvisibilityStatus();
            }
            this.effectsDirty = false;
        }
        final int integer3 = this.entityData.<Integer>get(LivingEntity.DATA_EFFECT_COLOR_ID);
        final boolean boolean4 = this.entityData.<Boolean>get(LivingEntity.DATA_EFFECT_AMBIENCE_ID);
        if (integer3 > 0) {
            boolean boolean5;
            if (this.isInvisible()) {
                boolean5 = (this.random.nextInt(15) == 0);
            }
            else {
                boolean5 = this.random.nextBoolean();
            }
            if (boolean4) {
                boolean5 &= (this.random.nextInt(5) == 0);
            }
            if (boolean5 && integer3 > 0) {
                final double double6 = (integer3 >> 16 & 0xFF) / 255.0;
                final double double7 = (integer3 >> 8 & 0xFF) / 255.0;
                final double double8 = (integer3 >> 0 & 0xFF) / 255.0;
                this.level.addParticle(boolean4 ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), double6, double7, double8);
            }
        }
    }
    
    protected void updateInvisibilityStatus() {
        if (this.activeEffects.isEmpty()) {
            this.removeEffectParticles();
            this.setInvisible(false);
        }
        else {
            final Collection<MobEffectInstance> collection2 = (Collection<MobEffectInstance>)this.activeEffects.values();
            this.entityData.<Boolean>set(LivingEntity.DATA_EFFECT_AMBIENCE_ID, areAllEffectsAmbient(collection2));
            this.entityData.<Integer>set(LivingEntity.DATA_EFFECT_COLOR_ID, PotionUtils.getColor(collection2));
            this.setInvisible(this.hasEffect(MobEffects.INVISIBILITY));
        }
    }
    
    public double getVisibilityPercent(@Nullable final Entity apx) {
        double double3 = 1.0;
        if (this.isDiscrete()) {
            double3 *= 0.8;
        }
        if (this.isInvisible()) {
            float float5 = this.getArmorCoverPercentage();
            if (float5 < 0.1f) {
                float5 = 0.1f;
            }
            double3 *= 0.7 * float5;
        }
        if (apx != null) {
            final ItemStack bly5 = this.getItemBySlot(EquipmentSlot.HEAD);
            final Item blu6 = bly5.getItem();
            final EntityType<?> aqb7 = apx.getType();
            if ((aqb7 == EntityType.SKELETON && blu6 == Items.SKELETON_SKULL) || (aqb7 == EntityType.ZOMBIE && blu6 == Items.ZOMBIE_HEAD) || (aqb7 == EntityType.CREEPER && blu6 == Items.CREEPER_HEAD)) {
                double3 *= 0.5;
            }
        }
        return double3;
    }
    
    public boolean canAttack(final LivingEntity aqj) {
        return true;
    }
    
    public boolean canAttack(final LivingEntity aqj, final TargetingConditions azd) {
        return azd.test(this, aqj);
    }
    
    public static boolean areAllEffectsAmbient(final Collection<MobEffectInstance> collection) {
        for (final MobEffectInstance apr3 : collection) {
            if (!apr3.isAmbient()) {
                return false;
            }
        }
        return true;
    }
    
    protected void removeEffectParticles() {
        this.entityData.<Boolean>set(LivingEntity.DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.<Integer>set(LivingEntity.DATA_EFFECT_COLOR_ID, 0);
    }
    
    public boolean removeAllEffects() {
        if (this.level.isClientSide) {
            return false;
        }
        final Iterator<MobEffectInstance> iterator2 = (Iterator<MobEffectInstance>)this.activeEffects.values().iterator();
        boolean boolean3 = false;
        while (iterator2.hasNext()) {
            this.onEffectRemoved((MobEffectInstance)iterator2.next());
            iterator2.remove();
            boolean3 = true;
        }
        return boolean3;
    }
    
    public Collection<MobEffectInstance> getActiveEffects() {
        return (Collection<MobEffectInstance>)this.activeEffects.values();
    }
    
    public Map<MobEffect, MobEffectInstance> getActiveEffectsMap() {
        return this.activeEffects;
    }
    
    public boolean hasEffect(final MobEffect app) {
        return this.activeEffects.containsKey(app);
    }
    
    @Nullable
    public MobEffectInstance getEffect(final MobEffect app) {
        return (MobEffectInstance)this.activeEffects.get(app);
    }
    
    public boolean addEffect(final MobEffectInstance apr) {
        if (!this.canBeAffected(apr)) {
            return false;
        }
        final MobEffectInstance apr2 = (MobEffectInstance)this.activeEffects.get(apr.getEffect());
        if (apr2 == null) {
            this.activeEffects.put(apr.getEffect(), apr);
            this.onEffectAdded(apr);
            return true;
        }
        if (apr2.update(apr)) {
            this.onEffectUpdated(apr2, true);
            return true;
        }
        return false;
    }
    
    public boolean canBeAffected(final MobEffectInstance apr) {
        if (this.getMobType() == MobType.UNDEAD) {
            final MobEffect app3 = apr.getEffect();
            if (app3 == MobEffects.REGENERATION || app3 == MobEffects.POISON) {
                return false;
            }
        }
        return true;
    }
    
    public void forceAddEffect(final MobEffectInstance apr) {
        if (!this.canBeAffected(apr)) {
            return;
        }
        final MobEffectInstance apr2 = (MobEffectInstance)this.activeEffects.put(apr.getEffect(), apr);
        if (apr2 == null) {
            this.onEffectAdded(apr);
        }
        else {
            this.onEffectUpdated(apr, true);
        }
    }
    
    public boolean isInvertedHealAndHarm() {
        return this.getMobType() == MobType.UNDEAD;
    }
    
    @Nullable
    public MobEffectInstance removeEffectNoUpdate(@Nullable final MobEffect app) {
        return (MobEffectInstance)this.activeEffects.remove(app);
    }
    
    public boolean removeEffect(final MobEffect app) {
        final MobEffectInstance apr3 = this.removeEffectNoUpdate(app);
        if (apr3 != null) {
            this.onEffectRemoved(apr3);
            return true;
        }
        return false;
    }
    
    protected void onEffectAdded(final MobEffectInstance apr) {
        this.effectsDirty = true;
        if (!this.level.isClientSide) {
            apr.getEffect().addAttributeModifiers(this, this.getAttributes(), apr.getAmplifier());
        }
    }
    
    protected void onEffectUpdated(final MobEffectInstance apr, final boolean boolean2) {
        this.effectsDirty = true;
        if (boolean2 && !this.level.isClientSide) {
            final MobEffect app4 = apr.getEffect();
            app4.removeAttributeModifiers(this, this.getAttributes(), apr.getAmplifier());
            app4.addAttributeModifiers(this, this.getAttributes(), apr.getAmplifier());
        }
    }
    
    protected void onEffectRemoved(final MobEffectInstance apr) {
        this.effectsDirty = true;
        if (!this.level.isClientSide) {
            apr.getEffect().removeAttributeModifiers(this, this.getAttributes(), apr.getAmplifier());
        }
    }
    
    public void heal(final float float1) {
        final float float2 = this.getHealth();
        if (float2 > 0.0f) {
            this.setHealth(float2 + float1);
        }
    }
    
    public float getHealth() {
        return this.entityData.<Float>get(LivingEntity.DATA_HEALTH_ID);
    }
    
    public void setHealth(final float float1) {
        this.entityData.<Float>set(LivingEntity.DATA_HEALTH_ID, Mth.clamp(float1, 0.0f, this.getMaxHealth()));
    }
    
    public boolean isDeadOrDying() {
        return this.getHealth() <= 0.0f;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (this.level.isClientSide) {
            return false;
        }
        if (this.isDeadOrDying()) {
            return false;
        }
        if (aph.isFire() && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        }
        if (this.isSleeping() && !this.level.isClientSide) {
            this.stopSleeping();
        }
        this.noActionTime = 0;
        final float float3 = float2;
        if ((aph == DamageSource.ANVIL || aph == DamageSource.FALLING_BLOCK) && !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            this.getItemBySlot(EquipmentSlot.HEAD).<LivingEntity>hurtAndBreak((int)(float2 * 4.0f + this.random.nextFloat() * float2 * 2.0f), this, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.HEAD)));
            float2 *= 0.75f;
        }
        boolean boolean5 = false;
        float float4 = 0.0f;
        if (float2 > 0.0f && this.isDamageSourceBlocked(aph)) {
            this.hurtCurrentlyUsedShield(float2);
            float4 = float2;
            float2 = 0.0f;
            if (!aph.isProjectile()) {
                final Entity apx7 = aph.getDirectEntity();
                if (apx7 instanceof LivingEntity) {
                    this.blockUsingShield((LivingEntity)apx7);
                }
            }
            boolean5 = true;
        }
        this.animationSpeed = 1.5f;
        boolean boolean6 = true;
        if (this.invulnerableTime > 10.0f) {
            if (float2 <= this.lastHurt) {
                return false;
            }
            this.actuallyHurt(aph, float2 - this.lastHurt);
            this.lastHurt = float2;
            boolean6 = false;
        }
        else {
            this.lastHurt = float2;
            this.invulnerableTime = 20;
            this.actuallyHurt(aph, float2);
            this.hurtDuration = 10;
            this.hurtTime = this.hurtDuration;
        }
        this.hurtDir = 0.0f;
        final Entity apx8 = aph.getEntity();
        if (apx8 != null) {
            if (apx8 instanceof LivingEntity) {
                this.setLastHurtByMob((LivingEntity)apx8);
            }
            if (apx8 instanceof Player) {
                this.lastHurtByPlayerTime = 100;
                this.lastHurtByPlayer = (Player)apx8;
            }
            else if (apx8 instanceof Wolf) {
                final Wolf baw9 = (Wolf)apx8;
                if (baw9.isTame()) {
                    this.lastHurtByPlayerTime = 100;
                    final LivingEntity aqj10 = baw9.getOwner();
                    if (aqj10 != null && aqj10.getType() == EntityType.PLAYER) {
                        this.lastHurtByPlayer = (Player)aqj10;
                    }
                    else {
                        this.lastHurtByPlayer = null;
                    }
                }
            }
        }
        if (boolean6) {
            if (boolean5) {
                this.level.broadcastEntityEvent(this, (byte)29);
            }
            else if (aph instanceof EntityDamageSource && ((EntityDamageSource)aph).isThorns()) {
                this.level.broadcastEntityEvent(this, (byte)33);
            }
            else {
                byte byte9;
                if (aph == DamageSource.DROWN) {
                    byte9 = 36;
                }
                else if (aph.isFire()) {
                    byte9 = 37;
                }
                else if (aph == DamageSource.SWEET_BERRY_BUSH) {
                    byte9 = 44;
                }
                else {
                    byte9 = 2;
                }
                this.level.broadcastEntityEvent(this, byte9);
            }
            if (aph != DamageSource.DROWN && (!boolean5 || float2 > 0.0f)) {
                this.markHurt();
            }
            if (apx8 != null) {
                double double9;
                double double10;
                for (double9 = apx8.getX() - this.getX(), double10 = apx8.getZ() - this.getZ(); double9 * double9 + double10 * double10 < 1.0E-4; double9 = (Math.random() - Math.random()) * 0.01, double10 = (Math.random() - Math.random()) * 0.01) {}
                this.hurtDir = (float)(Mth.atan2(double10, double9) * 57.2957763671875 - this.yRot);
                this.knockback(0.4f, double9, double10);
            }
            else {
                this.hurtDir = (float)((int)(Math.random() * 2.0) * 180);
            }
        }
        if (this.isDeadOrDying()) {
            if (!this.checkTotemDeathProtection(aph)) {
                final SoundEvent adn9 = this.getDeathSound();
                if (boolean6 && adn9 != null) {
                    this.playSound(adn9, this.getSoundVolume(), this.getVoicePitch());
                }
                this.die(aph);
            }
        }
        else if (boolean6) {
            this.playHurtSound(aph);
        }
        final boolean boolean7 = !boolean5 || float2 > 0.0f;
        if (boolean7) {
            this.lastDamageSource = aph;
            this.lastDamageStamp = this.level.getGameTime();
        }
        if (this instanceof ServerPlayer) {
            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)this, aph, float3, float2, boolean5);
            if (float4 > 0.0f && float4 < 3.4028235E37f) {
                ((ServerPlayer)this).awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(float4 * 10.0f));
            }
        }
        if (apx8 instanceof ServerPlayer) {
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)apx8, this, aph, float3, float2, boolean5);
        }
        return boolean7;
    }
    
    protected void blockUsingShield(final LivingEntity aqj) {
        aqj.blockedByShield(this);
    }
    
    protected void blockedByShield(final LivingEntity aqj) {
        aqj.knockback(0.5f, aqj.getX() - this.getX(), aqj.getZ() - this.getZ());
    }
    
    private boolean checkTotemDeathProtection(final DamageSource aph) {
        if (aph.isBypassInvul()) {
            return false;
        }
        ItemStack bly3 = null;
        for (final InteractionHand aoq8 : InteractionHand.values()) {
            final ItemStack bly4 = this.getItemInHand(aoq8);
            if (bly4.getItem() == Items.TOTEM_OF_UNDYING) {
                bly3 = bly4.copy();
                bly4.shrink(1);
                break;
            }
        }
        if (bly3 != null) {
            if (this instanceof ServerPlayer) {
                final ServerPlayer aah5 = (ServerPlayer)this;
                aah5.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                CriteriaTriggers.USED_TOTEM.trigger(aah5, bly3);
            }
            this.setHealth(1.0f);
            this.removeAllEffects();
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            this.level.broadcastEntityEvent(this, (byte)35);
        }
        return bly3 != null;
    }
    
    @Nullable
    public DamageSource getLastDamageSource() {
        if (this.level.getGameTime() - this.lastDamageStamp > 40L) {
            this.lastDamageSource = null;
        }
        return this.lastDamageSource;
    }
    
    protected void playHurtSound(final DamageSource aph) {
        final SoundEvent adn3 = this.getHurtSound(aph);
        if (adn3 != null) {
            this.playSound(adn3, this.getSoundVolume(), this.getVoicePitch());
        }
    }
    
    private boolean isDamageSourceBlocked(final DamageSource aph) {
        final Entity apx3 = aph.getDirectEntity();
        boolean boolean4 = false;
        if (apx3 instanceof AbstractArrow) {
            final AbstractArrow bfx5 = (AbstractArrow)apx3;
            if (bfx5.getPierceLevel() > 0) {
                boolean4 = true;
            }
        }
        if (!aph.isBypassArmor() && this.isBlocking() && !boolean4) {
            final Vec3 dck5 = aph.getSourcePosition();
            if (dck5 != null) {
                final Vec3 dck6 = this.getViewVector(1.0f);
                Vec3 dck7 = dck5.vectorTo(this.position()).normalize();
                dck7 = new Vec3(dck7.x, 0.0, dck7.z);
                if (dck7.dot(dck6) < 0.0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void breakItem(final ItemStack bly) {
        if (!bly.isEmpty()) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, this.getSoundSource(), 0.8f, 0.8f + this.level.random.nextFloat() * 0.4f, false);
            }
            this.spawnItemParticles(bly, 5);
        }
    }
    
    public void die(final DamageSource aph) {
        if (this.removed || this.dead) {
            return;
        }
        final Entity apx3 = aph.getEntity();
        final LivingEntity aqj4 = this.getKillCredit();
        if (this.deathScore >= 0 && aqj4 != null) {
            aqj4.awardKillScore(this, this.deathScore, aph);
        }
        if (this.isSleeping()) {
            this.stopSleeping();
        }
        this.dead = true;
        this.getCombatTracker().recheckStatus();
        if (this.level instanceof ServerLevel) {
            if (apx3 != null) {
                apx3.killed((ServerLevel)this.level, this);
            }
            this.dropAllDeathLoot(aph);
            this.createWitherRose(aqj4);
        }
        this.level.broadcastEntityEvent(this, (byte)3);
        this.setPose(Pose.DYING);
    }
    
    protected void createWitherRose(@Nullable final LivingEntity aqj) {
        if (this.level.isClientSide) {
            return;
        }
        boolean boolean3 = false;
        if (aqj instanceof WitherBoss) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                final BlockPos fx4 = this.blockPosition();
                final BlockState cee5 = Blocks.WITHER_ROSE.defaultBlockState();
                if (this.level.getBlockState(fx4).isAir() && cee5.canSurvive(this.level, fx4)) {
                    this.level.setBlock(fx4, cee5, 3);
                    boolean3 = true;
                }
            }
            if (!boolean3) {
                final ItemEntity bcs4 = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.WITHER_ROSE));
                this.level.addFreshEntity(bcs4);
            }
        }
    }
    
    protected void dropAllDeathLoot(final DamageSource aph) {
        final Entity apx3 = aph.getEntity();
        int integer4;
        if (apx3 instanceof Player) {
            integer4 = EnchantmentHelper.getMobLooting((LivingEntity)apx3);
        }
        else {
            integer4 = 0;
        }
        final boolean boolean5 = this.lastHurtByPlayerTime > 0;
        if (this.shouldDropLoot() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.dropFromLootTable(aph, boolean5);
            this.dropCustomDeathLoot(aph, integer4, boolean5);
        }
        this.dropEquipment();
        this.dropExperience();
    }
    
    protected void dropEquipment() {
    }
    
    protected void dropExperience() {
        if (!this.level.isClientSide && (this.isAlwaysExperienceDropper() || (this.lastHurtByPlayerTime > 0 && this.shouldDropExperience() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)))) {
            int integer2 = this.getExperienceReward(this.lastHurtByPlayer);
            while (integer2 > 0) {
                final int integer3 = ExperienceOrb.getExperienceValue(integer2);
                integer2 -= integer3;
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), integer3));
            }
        }
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
    }
    
    public ResourceLocation getLootTable() {
        return this.getType().getDefaultLootTable();
    }
    
    protected void dropFromLootTable(final DamageSource aph, final boolean boolean2) {
        final ResourceLocation vk4 = this.getLootTable();
        final LootTable cyv5 = this.level.getServer().getLootTables().get(vk4);
        final LootContext.Builder a6 = this.createLootContext(boolean2, aph);
        cyv5.getRandomItems(a6.create(LootContextParamSets.ENTITY), (Consumer<ItemStack>)this::spawnAtLocation);
    }
    
    protected LootContext.Builder createLootContext(final boolean boolean1, final DamageSource aph) {
        LootContext.Builder a4 = new LootContext.Builder((ServerLevel)this.level).withRandom(this.random).<Entity>withParameter(LootContextParams.THIS_ENTITY, this).<Vec3>withParameter(LootContextParams.ORIGIN, this.position()).<DamageSource>withParameter(LootContextParams.DAMAGE_SOURCE, aph).<Entity>withOptionalParameter(LootContextParams.KILLER_ENTITY, aph.getEntity()).<Entity>withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, aph.getDirectEntity());
        if (boolean1 && this.lastHurtByPlayer != null) {
            a4 = a4.<Player>withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }
        return a4;
    }
    
    public void knockback(float float1, final double double2, final double double3) {
        float1 *= (float)(1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        if (float1 <= 0.0f) {
            return;
        }
        this.hasImpulse = true;
        final Vec3 dck7 = this.getDeltaMovement();
        final Vec3 dck8 = new Vec3(double2, 0.0, double3).normalize().scale(float1);
        this.setDeltaMovement(dck7.x / 2.0 - dck8.x, this.onGround ? Math.min(0.4, dck7.y / 2.0 + float1) : dck7.y, dck7.z / 2.0 - dck8.z);
    }
    
    @Nullable
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.GENERIC_HURT;
    }
    
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }
    
    protected SoundEvent getFallDamageSound(final int integer) {
        if (integer > 4) {
            return SoundEvents.GENERIC_BIG_FALL;
        }
        return SoundEvents.GENERIC_SMALL_FALL;
    }
    
    protected SoundEvent getDrinkingSound(final ItemStack bly) {
        return bly.getDrinkingSound();
    }
    
    public SoundEvent getEatingSound(final ItemStack bly) {
        return bly.getEatingSound();
    }
    
    @Override
    public void setOnGround(final boolean boolean1) {
        super.setOnGround(boolean1);
        if (boolean1) {
            this.lastClimbablePos = (Optional<BlockPos>)Optional.empty();
        }
    }
    
    public Optional<BlockPos> getLastClimbablePos() {
        return this.lastClimbablePos;
    }
    
    public boolean onClimbable() {
        if (this.isSpectator()) {
            return false;
        }
        final BlockPos fx2 = this.blockPosition();
        final BlockState cee3 = this.getFeetBlockState();
        final Block bul4 = cee3.getBlock();
        if (bul4.is(BlockTags.CLIMBABLE)) {
            this.lastClimbablePos = (Optional<BlockPos>)Optional.of(fx2);
            return true;
        }
        if (bul4 instanceof TrapDoorBlock && this.trapdoorUsableAsLadder(fx2, cee3)) {
            this.lastClimbablePos = (Optional<BlockPos>)Optional.of(fx2);
            return true;
        }
        return false;
    }
    
    public BlockState getFeetBlockState() {
        return this.level.getBlockState(this.blockPosition());
    }
    
    private boolean trapdoorUsableAsLadder(final BlockPos fx, final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN)) {
            final BlockState cee2 = this.level.getBlockState(fx.below());
            if (cee2.is(Blocks.LADDER) && cee2.<Comparable>getValue((Property<Comparable>)LadderBlock.FACING) == cee.<Comparable>getValue((Property<Comparable>)TrapDoorBlock.FACING)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isAlive() {
        return !this.removed && this.getHealth() > 0.0f;
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        final boolean boolean4 = super.causeFallDamage(float1, float2);
        final int integer5 = this.calculateFallDamage(float1, float2);
        if (integer5 > 0) {
            this.playSound(this.getFallDamageSound(integer5), 1.0f, 1.0f);
            this.playBlockFallSound();
            this.hurt(DamageSource.FALL, (float)integer5);
            return true;
        }
        return boolean4;
    }
    
    protected int calculateFallDamage(final float float1, final float float2) {
        final MobEffectInstance apr4 = this.getEffect(MobEffects.JUMP);
        final float float3 = (apr4 == null) ? 0.0f : ((float)(apr4.getAmplifier() + 1));
        return Mth.ceil((float1 - 3.0f - float3) * float2);
    }
    
    protected void playBlockFallSound() {
        if (this.isSilent()) {
            return;
        }
        final int integer2 = Mth.floor(this.getX());
        final int integer3 = Mth.floor(this.getY() - 0.20000000298023224);
        final int integer4 = Mth.floor(this.getZ());
        final BlockState cee5 = this.level.getBlockState(new BlockPos(integer2, integer3, integer4));
        if (!cee5.isAir()) {
            final SoundType cab6 = cee5.getSoundType();
            this.playSound(cab6.getFallSound(), cab6.getVolume() * 0.5f, cab6.getPitch() * 0.75f);
        }
    }
    
    @Override
    public void animateHurt() {
        this.hurtDuration = 10;
        this.hurtTime = this.hurtDuration;
        this.hurtDir = 0.0f;
    }
    
    public int getArmorValue() {
        return Mth.floor(this.getAttributeValue(Attributes.ARMOR));
    }
    
    protected void hurtArmor(final DamageSource aph, final float float2) {
    }
    
    protected void hurtCurrentlyUsedShield(final float float1) {
    }
    
    protected float getDamageAfterArmorAbsorb(final DamageSource aph, float float2) {
        if (!aph.isBypassArmor()) {
            this.hurtArmor(aph, float2);
            float2 = CombatRules.getDamageAfterAbsorb(float2, (float)this.getArmorValue(), (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        }
        return float2;
    }
    
    protected float getDamageAfterMagicAbsorb(final DamageSource aph, float float2) {
        if (aph.isBypassMagic()) {
            return float2;
        }
        if (this.hasEffect(MobEffects.DAMAGE_RESISTANCE) && aph != DamageSource.OUT_OF_WORLD) {
            final int integer4 = (this.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
            final int integer5 = 25 - integer4;
            final float float3 = float2 * integer5;
            final float float4 = float2;
            float2 = Math.max(float3 / 25.0f, 0.0f);
            final float float5 = float4 - float2;
            if (float5 > 0.0f && float5 < 3.4028235E37f) {
                if (this instanceof ServerPlayer) {
                    ((ServerPlayer)this).awardStat(Stats.DAMAGE_RESISTED, Math.round(float5 * 10.0f));
                }
                else if (aph.getEntity() instanceof ServerPlayer) {
                    ((ServerPlayer)aph.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(float5 * 10.0f));
                }
            }
        }
        if (float2 <= 0.0f) {
            return 0.0f;
        }
        final int integer4 = EnchantmentHelper.getDamageProtection(this.getArmorSlots(), aph);
        if (integer4 > 0) {
            float2 = CombatRules.getDamageAfterMagicAbsorb(float2, (float)integer4);
        }
        return float2;
    }
    
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
        if (float4 > 0.0f && float4 < 3.4028235E37f && aph.getEntity() instanceof ServerPlayer) {
            ((ServerPlayer)aph.getEntity()).awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(float4 * 10.0f));
        }
        if (float2 == 0.0f) {
            return;
        }
        final float float5 = this.getHealth();
        this.setHealth(float5 - float2);
        this.getCombatTracker().recordDamage(aph, float5, float2);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - float2);
    }
    
    public CombatTracker getCombatTracker() {
        return this.combatTracker;
    }
    
    @Nullable
    public LivingEntity getKillCredit() {
        if (this.combatTracker.getKiller() != null) {
            return this.combatTracker.getKiller();
        }
        if (this.lastHurtByPlayer != null) {
            return this.lastHurtByPlayer;
        }
        if (this.lastHurtByMob != null) {
            return this.lastHurtByMob;
        }
        return null;
    }
    
    public final float getMaxHealth() {
        return (float)this.getAttributeValue(Attributes.MAX_HEALTH);
    }
    
    public final int getArrowCount() {
        return this.entityData.<Integer>get(LivingEntity.DATA_ARROW_COUNT_ID);
    }
    
    public final void setArrowCount(final int integer) {
        this.entityData.<Integer>set(LivingEntity.DATA_ARROW_COUNT_ID, integer);
    }
    
    public final int getStingerCount() {
        return this.entityData.<Integer>get(LivingEntity.DATA_STINGER_COUNT_ID);
    }
    
    public final void setStingerCount(final int integer) {
        this.entityData.<Integer>set(LivingEntity.DATA_STINGER_COUNT_ID, integer);
    }
    
    private int getCurrentSwingDuration() {
        if (MobEffectUtil.hasDigSpeed(this)) {
            return 6 - (1 + MobEffectUtil.getDigSpeedAmplification(this));
        }
        if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            return 6 + (1 + this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2;
        }
        return 6;
    }
    
    public void swing(final InteractionHand aoq) {
        this.swing(aoq, false);
    }
    
    public void swing(final InteractionHand aoq, final boolean boolean2) {
        if (!this.swinging || this.swingTime >= this.getCurrentSwingDuration() / 2 || this.swingTime < 0) {
            this.swingTime = -1;
            this.swinging = true;
            this.swingingArm = aoq;
            if (this.level instanceof ServerLevel) {
                final ClientboundAnimatePacket os4 = new ClientboundAnimatePacket(this, (aoq == InteractionHand.MAIN_HAND) ? 0 : 3);
                final ServerChunkCache aae5 = ((ServerLevel)this.level).getChunkSource();
                if (boolean2) {
                    aae5.broadcastAndSend(this, os4);
                }
                else {
                    aae5.broadcast(this, os4);
                }
            }
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        switch (byte1) {
            case 2:
            case 33:
            case 36:
            case 37:
            case 44: {
                final boolean boolean3 = byte1 == 33;
                final boolean boolean4 = byte1 == 36;
                final boolean boolean5 = byte1 == 37;
                final boolean boolean6 = byte1 == 44;
                this.animationSpeed = 1.5f;
                this.invulnerableTime = 20;
                this.hurtDuration = 10;
                this.hurtTime = this.hurtDuration;
                this.hurtDir = 0.0f;
                if (boolean3) {
                    this.playSound(SoundEvents.THORNS_HIT, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                }
                DamageSource aph7;
                if (boolean5) {
                    aph7 = DamageSource.ON_FIRE;
                }
                else if (boolean4) {
                    aph7 = DamageSource.DROWN;
                }
                else if (boolean6) {
                    aph7 = DamageSource.SWEET_BERRY_BUSH;
                }
                else {
                    aph7 = DamageSource.GENERIC;
                }
                final SoundEvent adn8 = this.getHurtSound(aph7);
                if (adn8 != null) {
                    this.playSound(adn8, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                }
                this.hurt(DamageSource.GENERIC, 0.0f);
                break;
            }
            case 3: {
                final SoundEvent adn9 = this.getDeathSound();
                if (adn9 != null) {
                    this.playSound(adn9, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                }
                if (!(this instanceof Player)) {
                    this.setHealth(0.0f);
                    this.die(DamageSource.GENERIC);
                    break;
                }
                break;
            }
            case 30: {
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8f, 0.8f + this.level.random.nextFloat() * 0.4f);
                break;
            }
            case 29: {
                this.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.8f + this.level.random.nextFloat() * 0.4f);
                break;
            }
            case 46: {
                final int integer3 = 128;
                for (int integer4 = 0; integer4 < 128; ++integer4) {
                    final double double5 = integer4 / 127.0;
                    final float float7 = (this.random.nextFloat() - 0.5f) * 0.2f;
                    final float float8 = (this.random.nextFloat() - 0.5f) * 0.2f;
                    final float float9 = (this.random.nextFloat() - 0.5f) * 0.2f;
                    final double double6 = Mth.lerp(double5, this.xo, this.getX()) + (this.random.nextDouble() - 0.5) * this.getBbWidth() * 2.0;
                    final double double7 = Mth.lerp(double5, this.yo, this.getY()) + this.random.nextDouble() * this.getBbHeight();
                    final double double8 = Mth.lerp(double5, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5) * this.getBbWidth() * 2.0;
                    this.level.addParticle(ParticleTypes.PORTAL, double6, double7, double8, float7, float8, float9);
                }
                break;
            }
            case 47: {
                this.breakItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
                break;
            }
            case 48: {
                this.breakItem(this.getItemBySlot(EquipmentSlot.OFFHAND));
                break;
            }
            case 49: {
                this.breakItem(this.getItemBySlot(EquipmentSlot.HEAD));
                break;
            }
            case 50: {
                this.breakItem(this.getItemBySlot(EquipmentSlot.CHEST));
                break;
            }
            case 51: {
                this.breakItem(this.getItemBySlot(EquipmentSlot.LEGS));
                break;
            }
            case 52: {
                this.breakItem(this.getItemBySlot(EquipmentSlot.FEET));
                break;
            }
            case 54: {
                HoneyBlock.showJumpParticles(this);
                break;
            }
            case 55: {
                this.swapHandItems();
                break;
            }
            default: {
                super.handleEntityEvent(byte1);
                break;
            }
        }
    }
    
    private void swapHandItems() {
        final ItemStack bly2 = this.getItemBySlot(EquipmentSlot.OFFHAND);
        this.setItemSlot(EquipmentSlot.OFFHAND, this.getItemBySlot(EquipmentSlot.MAINHAND));
        this.setItemSlot(EquipmentSlot.MAINHAND, bly2);
    }
    
    @Override
    protected void outOfWorld() {
        this.hurt(DamageSource.OUT_OF_WORLD, 4.0f);
    }
    
    protected void updateSwingTime() {
        final int integer2 = this.getCurrentSwingDuration();
        if (this.swinging) {
            ++this.swingTime;
            if (this.swingTime >= integer2) {
                this.swingTime = 0;
                this.swinging = false;
            }
        }
        else {
            this.swingTime = 0;
        }
        this.attackAnim = this.swingTime / (float)integer2;
    }
    
    @Nullable
    public AttributeInstance getAttribute(final Attribute ard) {
        return this.getAttributes().getInstance(ard);
    }
    
    public double getAttributeValue(final Attribute ard) {
        return this.getAttributes().getValue(ard);
    }
    
    public double getAttributeBaseValue(final Attribute ard) {
        return this.getAttributes().getBaseValue(ard);
    }
    
    public AttributeMap getAttributes() {
        return this.attributes;
    }
    
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }
    
    public ItemStack getMainHandItem() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }
    
    public ItemStack getOffhandItem() {
        return this.getItemBySlot(EquipmentSlot.OFFHAND);
    }
    
    public boolean isHolding(final Item blu) {
        return this.isHolding((Predicate<Item>)(blu2 -> blu2 == blu));
    }
    
    public boolean isHolding(final Predicate<Item> predicate) {
        return predicate.test(this.getMainHandItem().getItem()) || predicate.test(this.getOffhandItem().getItem());
    }
    
    public ItemStack getItemInHand(final InteractionHand aoq) {
        if (aoq == InteractionHand.MAIN_HAND) {
            return this.getItemBySlot(EquipmentSlot.MAINHAND);
        }
        if (aoq == InteractionHand.OFF_HAND) {
            return this.getItemBySlot(EquipmentSlot.OFFHAND);
        }
        throw new IllegalArgumentException(new StringBuilder().append("Invalid hand ").append(aoq).toString());
    }
    
    public void setItemInHand(final InteractionHand aoq, final ItemStack bly) {
        if (aoq == InteractionHand.MAIN_HAND) {
            this.setItemSlot(EquipmentSlot.MAINHAND, bly);
        }
        else {
            if (aoq != InteractionHand.OFF_HAND) {
                throw new IllegalArgumentException(new StringBuilder().append("Invalid hand ").append(aoq).toString());
            }
            this.setItemSlot(EquipmentSlot.OFFHAND, bly);
        }
    }
    
    public boolean hasItemInSlot(final EquipmentSlot aqc) {
        return !this.getItemBySlot(aqc).isEmpty();
    }
    
    @Override
    public abstract Iterable<ItemStack> getArmorSlots();
    
    public abstract ItemStack getItemBySlot(final EquipmentSlot aqc);
    
    @Override
    public abstract void setItemSlot(final EquipmentSlot aqc, final ItemStack bly);
    
    public float getArmorCoverPercentage() {
        final Iterable<ItemStack> iterable2 = this.getArmorSlots();
        int integer3 = 0;
        int integer4 = 0;
        for (final ItemStack bly6 : iterable2) {
            if (!bly6.isEmpty()) {
                ++integer4;
            }
            ++integer3;
        }
        return (integer3 > 0) ? (integer4 / (float)integer3) : 0.0f;
    }
    
    @Override
    public void setSprinting(final boolean boolean1) {
        super.setSprinting(boolean1);
        final AttributeInstance are3 = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (are3.getModifier(LivingEntity.SPEED_MODIFIER_SPRINTING_UUID) != null) {
            are3.removeModifier(LivingEntity.SPEED_MODIFIER_SPRINTING);
        }
        if (boolean1) {
            are3.addTransientModifier(LivingEntity.SPEED_MODIFIER_SPRINTING);
        }
    }
    
    protected float getSoundVolume() {
        return 1.0f;
    }
    
    protected float getVoicePitch() {
        if (this.isBaby()) {
            return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.5f;
        }
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f;
    }
    
    protected boolean isImmobile() {
        return this.isDeadOrDying();
    }
    
    @Override
    public void push(final Entity apx) {
        if (!this.isSleeping()) {
            super.push(apx);
        }
    }
    
    private void dismountVehicle(final Entity apx) {
        Vec3 dck3;
        if (apx.removed || this.level.getBlockState(apx.blockPosition()).getBlock().is(BlockTags.PORTALS)) {
            dck3 = new Vec3(apx.getX(), apx.getY() + apx.getBbHeight(), apx.getZ());
        }
        else {
            dck3 = apx.getDismountLocationForPassenger(this);
        }
        this.teleportTo(dck3.x, dck3.y, dck3.z);
    }
    
    @Override
    public boolean shouldShowName() {
        return this.isCustomNameVisible();
    }
    
    protected float getJumpPower() {
        return 0.42f * this.getBlockJumpFactor();
    }
    
    protected void jumpFromGround() {
        float float2 = this.getJumpPower();
        if (this.hasEffect(MobEffects.JUMP)) {
            float2 += 0.1f * (this.getEffect(MobEffects.JUMP).getAmplifier() + 1);
        }
        final Vec3 dck3 = this.getDeltaMovement();
        this.setDeltaMovement(dck3.x, float2, dck3.z);
        if (this.isSprinting()) {
            final float float3 = this.yRot * 0.017453292f;
            this.setDeltaMovement(this.getDeltaMovement().add(-Mth.sin(float3) * 0.2f, 0.0, Mth.cos(float3) * 0.2f));
        }
        this.hasImpulse = true;
    }
    
    protected void goDownInWater() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03999999910593033, 0.0));
    }
    
    protected void jumpInLiquid(final Tag<Fluid> aej) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.03999999910593033, 0.0));
    }
    
    protected float getWaterSlowDown() {
        return 0.8f;
    }
    
    public boolean canStandOnFluid(final Fluid cut) {
        return false;
    }
    
    public void travel(final Vec3 dck) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            double gravity = 0.08;
            final boolean isFalling = this.getDeltaMovement().y <= 0.0;
            if (isFalling && this.hasEffect(MobEffects.SLOW_FALLING)) {
                gravity = 0.01;
                this.fallDistance = 0.0f;
            }
            final FluidState cuu6 = this.level.getFluidState(this.blockPosition());
            if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(cuu6.getType())) {
                final double double4 = this.getY();
                float float9 = this.isSprinting() ? 0.9f : this.getWaterSlowDown();
                float float10 = 0.02f;
                float float11 = (float)EnchantmentHelper.getDepthStrider(this);
                if (float11 > 3.0f) {
                    float11 = 3.0f;
                }
                if (!this.onGround) {
                    float11 *= 0.5f;
                }
                if (float11 > 0.0f) {
                    float9 += (0.54600006f - float9) * float11 / 3.0f;
                    float10 += (this.getSpeed() - float10) * float11 / 3.0f;
                }
                if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                    float9 = 0.96f;
                }
                this.moveRelative(float10, dck);
                this.move(MoverType.SELF, this.getDeltaMovement());
                Vec3 dck2 = this.getDeltaMovement();
                if (this.horizontalCollision && this.onClimbable()) {
                    dck2 = new Vec3(dck2.x, 0.2, dck2.z);
                }
                this.setDeltaMovement(dck2.multiply(float9, 0.800000011920929, float9));
                final Vec3 dck3 = this.getFluidFallingAdjustedMovement(gravity, isFalling, this.getDeltaMovement());
                this.setDeltaMovement(dck3);
                if (this.horizontalCollision && this.isFree(dck3.x, dck3.y + 0.6000000238418579 - this.getY() + double4, dck3.z)) {
                    this.setDeltaMovement(dck3.x, 0.30000001192092896, dck3.z);
                }
            }
            else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(cuu6.getType())) {
                final double double4 = this.getY();
                this.moveRelative(0.02f, dck);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.800000011920929, 0.5));
                    final Vec3 dck4 = this.getFluidFallingAdjustedMovement(gravity, isFalling, this.getDeltaMovement());
                    this.setDeltaMovement(dck4);
                }
                else {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
                }
                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, -gravity / 4.0, 0.0));
                }
                final Vec3 dck4 = this.getDeltaMovement();
                if (this.horizontalCollision && this.isFree(dck4.x, dck4.y + 0.6000000238418579 - this.getY() + double4, dck4.z)) {
                    this.setDeltaMovement(dck4.x, 0.30000001192092896, dck4.z);
                }
            }
            else if (this.isFallFlying()) {
                Vec3 dck5 = this.getDeltaMovement();
                if (dck5.y > -0.5) {
                    this.fallDistance = 1.0f;
                }
                final Vec3 dck6 = this.getLookAngle();
                final float float9 = this.xRot * 0.017453292f;
                final double double5 = Math.sqrt(dck6.x * dck6.x + dck6.z * dck6.z);
                final double double6 = Math.sqrt(Entity.getHorizontalDistanceSqr(dck5));
                final double double7 = dck6.length();
                float float12 = Mth.cos(float9);
                float12 *= (float)(float12 * Math.min(1.0, double7 / 0.4));
                dck5 = this.getDeltaMovement().add(0.0, gravity * (-1.0 + float12 * 0.75), 0.0);
                if (dck5.y < 0.0 && double5 > 0.0) {
                    final double double8 = dck5.y * -0.1 * float12;
                    dck5 = dck5.add(dck6.x * double8 / double5, double8, dck6.z * double8 / double5);
                }
                if (float9 < 0.0f && double5 > 0.0) {
                    final double double8 = double6 * -Mth.sin(float9) * 0.04;
                    dck5 = dck5.add(-dck6.x * double8 / double5, double8 * 3.2, -dck6.z * double8 / double5);
                }
                if (double5 > 0.0) {
                    dck5 = dck5.add((dck6.x / double5 * double6 - dck5.x) * 0.1, 0.0, (dck6.z / double5 * double6 - dck5.z) * 0.1);
                }
                this.setDeltaMovement(dck5.multiply(0.9900000095367432, 0.9800000190734863, 0.9900000095367432));
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.horizontalCollision && !this.level.isClientSide) {
                    final double double8 = Math.sqrt(Entity.getHorizontalDistanceSqr(this.getDeltaMovement()));
                    final double double9 = double6 - double8;
                    final float float13 = (float)(double9 * 10.0 - 3.0);
                    if (float13 > 0.0f) {
                        this.playSound(this.getFallDamageSound((int)float13), 1.0f, 1.0f);
                        this.hurt(DamageSource.FLY_INTO_WALL, float13);
                    }
                }
                if (this.onGround && !this.level.isClientSide) {
                    this.setSharedFlag(7, false);
                }
            }
            else {
                final BlockPos fx7 = this.getBlockPosBelowThatAffectsMyMovement();
                final float float14 = this.level.getBlockState(fx7).getBlock().getFriction();
                final float float9 = this.onGround ? (float14 * 0.91f) : 0.91f;
                final Vec3 dck7 = this.handleRelativeFrictionAndCalculateMovement(dck, float14);
                double double10 = dck7.y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    double10 += (0.05 * (this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - dck7.y) * 0.2;
                    this.fallDistance = 0.0f;
                }
                else if (!this.level.isClientSide || this.level.hasChunkAt(fx7)) {
                    if (!this.isNoGravity()) {
                        double10 -= gravity;
                    }
                }
                else if (this.getY() > 0.0) {
                    double10 = -0.1;
                }
                else {
                    double10 = 0.0;
                }
                this.setDeltaMovement(dck7.x * float9, double10 * 0.9800000190734863, dck7.z * float9);
            }
        }
        this.calculateEntityAnimation(this, this instanceof FlyingAnimal);
    }
    
    public void calculateEntityAnimation(final LivingEntity aqj, final boolean boolean2) {
        aqj.animationSpeedOld = aqj.animationSpeed;
        final double double4 = aqj.getX() - aqj.xo;
        final double double5 = boolean2 ? (aqj.getY() - aqj.yo) : 0.0;
        final double double6 = aqj.getZ() - aqj.zo;
        float float10 = Mth.sqrt(double4 * double4 + double5 * double5 + double6 * double6) * 4.0f;
        if (float10 > 1.0f) {
            float10 = 1.0f;
        }
        aqj.animationSpeed += (float10 - aqj.animationSpeed) * 0.4f;
        aqj.animationPosition += aqj.animationSpeed;
    }
    
    public Vec3 handleRelativeFrictionAndCalculateMovement(final Vec3 dck, final float float2) {
        this.moveRelative(this.getFrictionInfluencedSpeed(float2), dck);
        this.setDeltaMovement(this.handleOnClimbable(this.getDeltaMovement()));
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 dck2 = this.getDeltaMovement();
        if ((this.horizontalCollision || this.jumping) && this.onClimbable()) {
            dck2 = new Vec3(dck2.x, 0.2, dck2.z);
        }
        return dck2;
    }
    
    public Vec3 getFluidFallingAdjustedMovement(final double double1, final boolean boolean2, final Vec3 dck) {
        if (!this.isNoGravity() && !this.isSprinting()) {
            double double2;
            if (boolean2 && Math.abs(dck.y - 0.005) >= 0.003 && Math.abs(dck.y - double1 / 16.0) < 0.003) {
                double2 = -0.003;
            }
            else {
                double2 = dck.y - double1 / 16.0;
            }
            return new Vec3(dck.x, double2, dck.z);
        }
        return dck;
    }
    
    private Vec3 handleOnClimbable(Vec3 dck) {
        if (this.onClimbable()) {
            this.fallDistance = 0.0f;
            final float float3 = 0.15f;
            final double double4 = Mth.clamp(dck.x, -0.15000000596046448, 0.15000000596046448);
            final double double5 = Mth.clamp(dck.z, -0.15000000596046448, 0.15000000596046448);
            double double6 = Math.max(dck.y, -0.15000000596046448);
            if (double6 < 0.0 && !this.getFeetBlockState().is(Blocks.SCAFFOLDING) && this.isSuppressingSlidingDownLadder() && this instanceof Player) {
                double6 = 0.0;
            }
            dck = new Vec3(double4, double6, double5);
        }
        return dck;
    }
    
    private float getFrictionInfluencedSpeed(final float float1) {
        if (this.onGround) {
            return this.getSpeed() * (0.21600002f / (float1 * float1 * float1));
        }
        return this.flyingSpeed;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public void setSpeed(final float float1) {
        this.speed = float1;
    }
    
    public boolean doHurtTarget(final Entity apx) {
        this.setLastHurtMob(apx);
        return false;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.updatingUsingItem();
        this.updateSwimAmount();
        if (!this.level.isClientSide) {
            final int integer2 = this.getArrowCount();
            if (integer2 > 0) {
                if (this.removeArrowTime <= 0) {
                    this.removeArrowTime = 20 * (30 - integer2);
                }
                --this.removeArrowTime;
                if (this.removeArrowTime <= 0) {
                    this.setArrowCount(integer2 - 1);
                }
            }
            final int integer3 = this.getStingerCount();
            if (integer3 > 0) {
                if (this.removeStingerTime <= 0) {
                    this.removeStingerTime = 20 * (30 - integer3);
                }
                --this.removeStingerTime;
                if (this.removeStingerTime <= 0) {
                    this.setStingerCount(integer3 - 1);
                }
            }
            this.detectEquipmentUpdates();
            if (this.tickCount % 20 == 0) {
                this.getCombatTracker().recheckStatus();
            }
            if (!this.glowing) {
                final boolean boolean4 = this.hasEffect(MobEffects.GLOWING);
                if (this.getSharedFlag(6) != boolean4) {
                    this.setSharedFlag(6, boolean4);
                }
            }
            if (this.isSleeping() && !this.checkBedExists()) {
                this.stopSleeping();
            }
        }
        this.aiStep();
        final double double2 = this.getX() - this.xo;
        final double double3 = this.getZ() - this.zo;
        final float float6 = (float)(double2 * double2 + double3 * double3);
        float float7 = this.yBodyRot;
        float float8 = 0.0f;
        this.oRun = this.run;
        float float9 = 0.0f;
        if (float6 > 0.0025000002f) {
            float9 = 1.0f;
            float8 = (float)Math.sqrt((double)float6) * 3.0f;
            final float float10 = (float)Mth.atan2(double3, double2) * 57.295776f - 90.0f;
            final float float11 = Mth.abs(Mth.wrapDegrees(this.yRot) - float10);
            if (95.0f < float11 && float11 < 265.0f) {
                float7 = float10 - 180.0f;
            }
            else {
                float7 = float10;
            }
        }
        if (this.attackAnim > 0.0f) {
            float7 = this.yRot;
        }
        if (!this.onGround) {
            float9 = 0.0f;
        }
        this.run += (float9 - this.run) * 0.3f;
        this.level.getProfiler().push("headTurn");
        float8 = this.tickHeadTurn(float7, float8);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("rangeChecks");
        while (this.yRot - this.yRotO < -180.0f) {
            this.yRotO -= 360.0f;
        }
        while (this.yRot - this.yRotO >= 180.0f) {
            this.yRotO += 360.0f;
        }
        while (this.yBodyRot - this.yBodyRotO < -180.0f) {
            this.yBodyRotO -= 360.0f;
        }
        while (this.yBodyRot - this.yBodyRotO >= 180.0f) {
            this.yBodyRotO += 360.0f;
        }
        while (this.xRot - this.xRotO < -180.0f) {
            this.xRotO -= 360.0f;
        }
        while (this.xRot - this.xRotO >= 180.0f) {
            this.xRotO += 360.0f;
        }
        while (this.yHeadRot - this.yHeadRotO < -180.0f) {
            this.yHeadRotO -= 360.0f;
        }
        while (this.yHeadRot - this.yHeadRotO >= 180.0f) {
            this.yHeadRotO += 360.0f;
        }
        this.level.getProfiler().pop();
        this.animStep += float8;
        if (this.isFallFlying()) {
            ++this.fallFlyTicks;
        }
        else {
            this.fallFlyTicks = 0;
        }
        if (this.isSleeping()) {
            this.xRot = 0.0f;
        }
    }
    
    private void detectEquipmentUpdates() {
        final Map<EquipmentSlot, ItemStack> map2 = this.collectEquipmentChanges();
        if (map2 != null) {
            this.handleHandSwap(map2);
            if (!map2.isEmpty()) {
                this.handleEquipmentChanges(map2);
            }
        }
    }
    
    @Nullable
    private Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
        Map<EquipmentSlot, ItemStack> map2 = null;
        for (final EquipmentSlot aqc6 : EquipmentSlot.values()) {
            Label_0172: {
                ItemStack bly7 = null;
                switch (aqc6.getType()) {
                    case HAND: {
                        bly7 = this.getLastHandItem(aqc6);
                        break;
                    }
                    case ARMOR: {
                        bly7 = this.getLastArmorItem(aqc6);
                        break;
                    }
                    default: {
                        break Label_0172;
                    }
                }
                final ItemStack bly8 = this.getItemBySlot(aqc6);
                if (!ItemStack.matches(bly8, bly7)) {
                    if (map2 == null) {
                        map2 = (Map<EquipmentSlot, ItemStack>)Maps.newEnumMap((Class)EquipmentSlot.class);
                    }
                    map2.put(aqc6, bly8);
                    if (!bly7.isEmpty()) {
                        this.getAttributes().removeAttributeModifiers(bly7.getAttributeModifiers(aqc6));
                    }
                    if (!bly8.isEmpty()) {
                        this.getAttributes().addTransientAttributeModifiers(bly8.getAttributeModifiers(aqc6));
                    }
                }
            }
        }
        return map2;
    }
    
    private void handleHandSwap(final Map<EquipmentSlot, ItemStack> map) {
        final ItemStack bly3 = (ItemStack)map.get(EquipmentSlot.MAINHAND);
        final ItemStack bly4 = (ItemStack)map.get(EquipmentSlot.OFFHAND);
        if (bly3 != null && bly4 != null && ItemStack.matches(bly3, this.getLastHandItem(EquipmentSlot.OFFHAND)) && ItemStack.matches(bly4, this.getLastHandItem(EquipmentSlot.MAINHAND))) {
            ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundEntityEventPacket(this, (byte)55));
            map.remove(EquipmentSlot.MAINHAND);
            map.remove(EquipmentSlot.OFFHAND);
            this.setLastHandItem(EquipmentSlot.MAINHAND, bly3.copy());
            this.setLastHandItem(EquipmentSlot.OFFHAND, bly4.copy());
        }
    }
    
    private void handleEquipmentChanges(final Map<EquipmentSlot, ItemStack> map) {
        final List<Pair<EquipmentSlot, ItemStack>> list3 = (List<Pair<EquipmentSlot, ItemStack>>)Lists.newArrayListWithCapacity(map.size());
        map.forEach((aqc, bly) -> {
            final ItemStack bly2 = bly.copy();
            list3.add(Pair.of((Object)aqc, (Object)bly2));
            switch (aqc.getType()) {
                case HAND: {
                    this.setLastHandItem(aqc, bly2);
                    break;
                }
                case ARMOR: {
                    this.setLastArmorItem(aqc, bly2);
                    break;
                }
            }
        });
        ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundSetEquipmentPacket(this.getId(), list3));
    }
    
    private ItemStack getLastArmorItem(final EquipmentSlot aqc) {
        return this.lastArmorItemStacks.get(aqc.getIndex());
    }
    
    private void setLastArmorItem(final EquipmentSlot aqc, final ItemStack bly) {
        this.lastArmorItemStacks.set(aqc.getIndex(), bly);
    }
    
    private ItemStack getLastHandItem(final EquipmentSlot aqc) {
        return this.lastHandItemStacks.get(aqc.getIndex());
    }
    
    private void setLastHandItem(final EquipmentSlot aqc, final ItemStack bly) {
        this.lastHandItemStacks.set(aqc.getIndex(), bly);
    }
    
    protected float tickHeadTurn(final float float1, float float2) {
        final float float3 = Mth.wrapDegrees(float1 - this.yBodyRot);
        this.yBodyRot += float3 * 0.3f;
        float float4 = Mth.wrapDegrees(this.yRot - this.yBodyRot);
        final boolean boolean6 = float4 < -90.0f || float4 >= 90.0f;
        if (float4 < -75.0f) {
            float4 = -75.0f;
        }
        if (float4 >= 75.0f) {
            float4 = 75.0f;
        }
        this.yBodyRot = this.yRot - float4;
        if (float4 * float4 > 2500.0f) {
            this.yBodyRot += float4 * 0.2f;
        }
        if (boolean6) {
            float2 *= -1.0f;
        }
        return float2;
    }
    
    public void aiStep() {
        if (this.noJumpDelay > 0) {
            --this.noJumpDelay;
        }
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }
        if (this.lerpSteps > 0) {
            final double double2 = this.getX() + (this.lerpX - this.getX()) / this.lerpSteps;
            final double double3 = this.getY() + (this.lerpY - this.getY()) / this.lerpSteps;
            final double double4 = this.getZ() + (this.lerpZ - this.getZ()) / this.lerpSteps;
            final double double5 = Mth.wrapDegrees(this.lerpYRot - this.yRot);
            this.yRot += (float)(double5 / this.lerpSteps);
            this.xRot += (float)((this.lerpXRot - this.xRot) / this.lerpSteps);
            --this.lerpSteps;
            this.setPos(double2, double3, double4);
            this.setRot(this.yRot, this.xRot);
        }
        else if (!this.isEffectiveAi()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        }
        if (this.lerpHeadSteps > 0) {
            this.yHeadRot += (float)(Mth.wrapDegrees(this.lyHeadRot - this.yHeadRot) / this.lerpHeadSteps);
            --this.lerpHeadSteps;
        }
        final Vec3 dck2 = this.getDeltaMovement();
        double double6 = dck2.x;
        double double7 = dck2.y;
        double double8 = dck2.z;
        if (Math.abs(dck2.x) < 0.003) {
            double6 = 0.0;
        }
        if (Math.abs(dck2.y) < 0.003) {
            double7 = 0.0;
        }
        if (Math.abs(dck2.z) < 0.003) {
            double8 = 0.0;
        }
        this.setDeltaMovement(double6, double7, double8);
        this.level.getProfiler().push("ai");
        if (this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0f;
            this.zza = 0.0f;
        }
        else if (this.isEffectiveAi()) {
            this.level.getProfiler().push("newAi");
            this.serverAiStep();
            this.level.getProfiler().pop();
        }
        this.level.getProfiler().pop();
        this.level.getProfiler().push("jump");
        if (this.jumping && this.isAffectedByFluids()) {
            double double9;
            if (this.isInLava()) {
                double9 = this.getFluidHeight(FluidTags.LAVA);
            }
            else {
                double9 = this.getFluidHeight(FluidTags.WATER);
            }
            final boolean boolean11 = this.isInWater() && double9 > 0.0;
            final double double10 = this.getFluidJumpThreshold();
            if (boolean11 && (!this.onGround || double9 > double10)) {
                this.jumpInLiquid(FluidTags.WATER);
            }
            else if (this.isInLava() && (!this.onGround || double9 > double10)) {
                this.jumpInLiquid(FluidTags.LAVA);
            }
            else if ((this.onGround || (boolean11 && double9 <= double10)) && this.noJumpDelay == 0) {
                this.jumpFromGround();
                this.noJumpDelay = 10;
            }
        }
        else {
            this.noJumpDelay = 0;
        }
        this.level.getProfiler().pop();
        this.level.getProfiler().push("travel");
        this.xxa *= 0.98f;
        this.zza *= 0.98f;
        this.updateFallFlying();
        final AABB dcf9 = this.getBoundingBox();
        this.travel(new Vec3(this.xxa, this.yya, this.zza));
        this.level.getProfiler().pop();
        this.level.getProfiler().push("push");
        if (this.autoSpinAttackTicks > 0) {
            --this.autoSpinAttackTicks;
            this.checkAutoSpinAttack(dcf9, this.getBoundingBox());
        }
        this.pushEntities();
        this.level.getProfiler().pop();
        if (!this.level.isClientSide && this.isSensitiveToWater() && this.isInWaterRainOrBubble()) {
            this.hurt(DamageSource.DROWN, 1.0f);
        }
    }
    
    public boolean isSensitiveToWater() {
        return false;
    }
    
    private void updateFallFlying() {
        boolean boolean2 = this.getSharedFlag(7);
        if (boolean2 && !this.onGround && !this.isPassenger() && !this.hasEffect(MobEffects.LEVITATION)) {
            final ItemStack bly3 = this.getItemBySlot(EquipmentSlot.CHEST);
            if (bly3.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(bly3)) {
                boolean2 = true;
                if (!this.level.isClientSide && (this.fallFlyTicks + 1) % 20 == 0) {
                    bly3.<LivingEntity>hurtAndBreak(1, this, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.CHEST)));
                }
            }
            else {
                boolean2 = false;
            }
        }
        else {
            boolean2 = false;
        }
        if (!this.level.isClientSide) {
            this.setSharedFlag(7, boolean2);
        }
    }
    
    protected void serverAiStep() {
    }
    
    protected void pushEntities() {
        final List<Entity> list2 = this.level.getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
        if (!list2.isEmpty()) {
            final int integer3 = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            if (integer3 > 0 && list2.size() > integer3 - 1 && this.random.nextInt(4) == 0) {
                int integer4 = 0;
                for (int integer5 = 0; integer5 < list2.size(); ++integer5) {
                    if (!((Entity)list2.get(integer5)).isPassenger()) {
                        ++integer4;
                    }
                }
                if (integer4 > integer3 - 1) {
                    this.hurt(DamageSource.CRAMMING, 6.0f);
                }
            }
            for (int integer4 = 0; integer4 < list2.size(); ++integer4) {
                final Entity apx5 = (Entity)list2.get(integer4);
                this.doPush(apx5);
            }
        }
    }
    
    protected void checkAutoSpinAttack(final AABB dcf1, final AABB dcf2) {
        final AABB dcf3 = dcf1.minmax(dcf2);
        final List<Entity> list5 = this.level.getEntities(this, dcf3);
        if (!list5.isEmpty()) {
            for (int integer6 = 0; integer6 < list5.size(); ++integer6) {
                final Entity apx7 = (Entity)list5.get(integer6);
                if (apx7 instanceof LivingEntity) {
                    this.doAutoAttackOnTouch((LivingEntity)apx7);
                    this.autoSpinAttackTicks = 0;
                    this.setDeltaMovement(this.getDeltaMovement().scale(-0.2));
                    break;
                }
            }
        }
        else if (this.horizontalCollision) {
            this.autoSpinAttackTicks = 0;
        }
        if (!this.level.isClientSide && this.autoSpinAttackTicks <= 0) {
            this.setLivingEntityFlag(4, false);
        }
    }
    
    protected void doPush(final Entity apx) {
        apx.push(this);
    }
    
    protected void doAutoAttackOnTouch(final LivingEntity aqj) {
    }
    
    public void startAutoSpinAttack(final int integer) {
        this.autoSpinAttackTicks = integer;
        if (!this.level.isClientSide) {
            this.setLivingEntityFlag(4, true);
        }
    }
    
    public boolean isAutoSpinAttack() {
        return (this.entityData.<Byte>get(LivingEntity.DATA_LIVING_ENTITY_FLAGS) & 0x4) != 0x0;
    }
    
    @Override
    public void stopRiding() {
        final Entity apx2 = this.getVehicle();
        super.stopRiding();
        if (apx2 != null && apx2 != this.getVehicle() && !this.level.isClientSide) {
            this.dismountVehicle(apx2);
        }
    }
    
    @Override
    public void rideTick() {
        super.rideTick();
        this.oRun = this.run;
        this.run = 0.0f;
        this.fallDistance = 0.0f;
    }
    
    @Override
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        this.lerpX = double1;
        this.lerpY = double2;
        this.lerpZ = double3;
        this.lerpYRot = float4;
        this.lerpXRot = float5;
        this.lerpSteps = integer;
    }
    
    @Override
    public void lerpHeadTo(final float float1, final int integer) {
        this.lyHeadRot = float1;
        this.lerpHeadSteps = integer;
    }
    
    public void setJumping(final boolean boolean1) {
        this.jumping = boolean1;
    }
    
    public void onItemPickup(final ItemEntity bcs) {
        final Player bft3 = (bcs.getThrower() != null) ? this.level.getPlayerByUUID(bcs.getThrower()) : null;
        if (bft3 instanceof ServerPlayer) {
            CriteriaTriggers.ITEM_PICKED_UP_BY_ENTITY.trigger((ServerPlayer)bft3, bcs.getItem(), this);
        }
    }
    
    public void take(final Entity apx, final int integer) {
        if (!apx.removed && !this.level.isClientSide && (apx instanceof ItemEntity || apx instanceof AbstractArrow || apx instanceof ExperienceOrb)) {
            ((ServerLevel)this.level).getChunkSource().broadcast(apx, new ClientboundTakeItemEntityPacket(apx.getId(), this.getId(), integer));
        }
    }
    
    public boolean canSee(final Entity apx) {
        final Vec3 dck3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        final Vec3 dck4 = new Vec3(apx.getX(), apx.getEyeY(), apx.getZ());
        return this.level.clip(new ClipContext(dck3, dck4, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }
    
    @Override
    public float getViewYRot(final float float1) {
        if (float1 == 1.0f) {
            return this.yHeadRot;
        }
        return Mth.lerp(float1, this.yHeadRotO, this.yHeadRot);
    }
    
    public float getAttackAnim(final float float1) {
        float float2 = this.attackAnim - this.oAttackAnim;
        if (float2 < 0.0f) {
            ++float2;
        }
        return this.oAttackAnim + float2 * float1;
    }
    
    public boolean isEffectiveAi() {
        return !this.level.isClientSide;
    }
    
    @Override
    public boolean isPickable() {
        return !this.removed;
    }
    
    @Override
    public boolean isPushable() {
        return this.isAlive() && !this.isSpectator() && !this.onClimbable();
    }
    
    @Override
    protected void markHurt() {
        this.hurtMarked = (this.random.nextDouble() >= this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }
    
    @Override
    public float getYHeadRot() {
        return this.yHeadRot;
    }
    
    @Override
    public void setYHeadRot(final float float1) {
        this.yHeadRot = float1;
    }
    
    @Override
    public void setYBodyRot(final float float1) {
        this.yBodyRot = float1;
    }
    
    @Override
    protected Vec3 getRelativePortalPosition(final Direction.Axis a, final BlockUtil.FoundRectangle a) {
        return resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(a, a));
    }
    
    public static Vec3 resetForwardDirectionOfRelativePortalPosition(final Vec3 dck) {
        return new Vec3(dck.x, dck.y, 0.0);
    }
    
    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }
    
    public void setAbsorptionAmount(float float1) {
        if (float1 < 0.0f) {
            float1 = 0.0f;
        }
        this.absorptionAmount = float1;
    }
    
    public void onEnterCombat() {
    }
    
    public void onLeaveCombat() {
    }
    
    protected void updateEffectVisibility() {
        this.effectsDirty = true;
    }
    
    public abstract HumanoidArm getMainArm();
    
    public boolean isUsingItem() {
        return (this.entityData.<Byte>get(LivingEntity.DATA_LIVING_ENTITY_FLAGS) & 0x1) > 0;
    }
    
    public InteractionHand getUsedItemHand() {
        return ((this.entityData.<Byte>get(LivingEntity.DATA_LIVING_ENTITY_FLAGS) & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }
    
    private void updatingUsingItem() {
        if (this.isUsingItem()) {
            if (ItemStack.isSameIgnoreDurability(this.getItemInHand(this.getUsedItemHand()), this.useItem)) {
                (this.useItem = this.getItemInHand(this.getUsedItemHand())).onUseTick(this.level, this, this.getUseItemRemainingTicks());
                if (this.shouldTriggerItemUseEffects()) {
                    this.triggerItemUseEffects(this.useItem, 5);
                }
                final int useItemRemaining = this.useItemRemaining - 1;
                this.useItemRemaining = useItemRemaining;
                if (useItemRemaining == 0 && !this.level.isClientSide && !this.useItem.useOnRelease()) {
                    this.completeUsingItem();
                }
            }
            else {
                this.stopUsingItem();
            }
        }
    }
    
    private boolean shouldTriggerItemUseEffects() {
        final int integer2 = this.getUseItemRemainingTicks();
        final FoodProperties bhw3 = this.useItem.getItem().getFoodProperties();
        boolean boolean4 = bhw3 != null && bhw3.isFastFood();
        boolean4 |= (integer2 <= this.useItem.getUseDuration() - 7);
        return boolean4 && integer2 % 4 == 0;
    }
    
    private void updateSwimAmount() {
        this.swimAmountO = this.swimAmount;
        if (this.isVisuallySwimming()) {
            this.swimAmount = Math.min(1.0f, this.swimAmount + 0.09f);
        }
        else {
            this.swimAmount = Math.max(0.0f, this.swimAmount - 0.09f);
        }
    }
    
    protected void setLivingEntityFlag(final int integer, final boolean boolean2) {
        int integer2 = this.entityData.<Byte>get(LivingEntity.DATA_LIVING_ENTITY_FLAGS);
        if (boolean2) {
            integer2 |= integer;
        }
        else {
            integer2 &= ~integer;
        }
        this.entityData.<Byte>set(LivingEntity.DATA_LIVING_ENTITY_FLAGS, (byte)integer2);
    }
    
    public void startUsingItem(final InteractionHand aoq) {
        final ItemStack bly3 = this.getItemInHand(aoq);
        if (bly3.isEmpty() || this.isUsingItem()) {
            return;
        }
        this.useItem = bly3;
        this.useItemRemaining = bly3.getUseDuration();
        if (!this.level.isClientSide) {
            this.setLivingEntityFlag(1, true);
            this.setLivingEntityFlag(2, aoq == InteractionHand.OFF_HAND);
        }
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (LivingEntity.SLEEPING_POS_ID.equals(us)) {
            if (this.level.isClientSide) {
                this.getSleepingPos().ifPresent(this::setPosToBed);
            }
        }
        else if (LivingEntity.DATA_LIVING_ENTITY_FLAGS.equals(us) && this.level.isClientSide) {
            if (this.isUsingItem() && this.useItem.isEmpty()) {
                this.useItem = this.getItemInHand(this.getUsedItemHand());
                if (!this.useItem.isEmpty()) {
                    this.useItemRemaining = this.useItem.getUseDuration();
                }
            }
            else if (!this.isUsingItem() && !this.useItem.isEmpty()) {
                this.useItem = ItemStack.EMPTY;
                this.useItemRemaining = 0;
            }
        }
    }
    
    @Override
    public void lookAt(final EntityAnchorArgument.Anchor a, final Vec3 dck) {
        super.lookAt(a, dck);
        this.yHeadRotO = this.yHeadRot;
        this.yBodyRot = this.yHeadRot;
        this.yBodyRotO = this.yBodyRot;
    }
    
    protected void triggerItemUseEffects(final ItemStack bly, final int integer) {
        if (bly.isEmpty() || !this.isUsingItem()) {
            return;
        }
        if (bly.getUseAnimation() == UseAnim.DRINK) {
            this.playSound(this.getDrinkingSound(bly), 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
        }
        if (bly.getUseAnimation() == UseAnim.EAT) {
            this.spawnItemParticles(bly, integer);
            this.playSound(this.getEatingSound(bly), 0.5f + 0.5f * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
    }
    
    private void spawnItemParticles(final ItemStack bly, final int integer) {
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            Vec3 dck5 = new Vec3((this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            dck5 = dck5.xRot(-this.xRot * 0.017453292f);
            dck5 = dck5.yRot(-this.yRot * 0.017453292f);
            final double double6 = -this.random.nextFloat() * 0.6 - 0.3;
            Vec3 dck6 = new Vec3((this.random.nextFloat() - 0.5) * 0.3, double6, 0.6);
            dck6 = dck6.xRot(-this.xRot * 0.017453292f);
            dck6 = dck6.yRot(-this.yRot * 0.017453292f);
            dck6 = dck6.add(this.getX(), this.getEyeY(), this.getZ());
            this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, bly), dck6.x, dck6.y, dck6.z, dck5.x, dck5.y + 0.05, dck5.z);
        }
    }
    
    protected void completeUsingItem() {
        final InteractionHand aoq2 = this.getUsedItemHand();
        if (!this.useItem.equals(this.getItemInHand(aoq2))) {
            this.releaseUsingItem();
            return;
        }
        if (!this.useItem.isEmpty() && this.isUsingItem()) {
            this.triggerItemUseEffects(this.useItem, 16);
            final ItemStack bly3 = this.useItem.finishUsingItem(this.level, this);
            if (bly3 != this.useItem) {
                this.setItemInHand(aoq2, bly3);
            }
            this.stopUsingItem();
        }
    }
    
    public ItemStack getUseItem() {
        return this.useItem;
    }
    
    public int getUseItemRemainingTicks() {
        return this.useItemRemaining;
    }
    
    public int getTicksUsingItem() {
        if (this.isUsingItem()) {
            return this.useItem.getUseDuration() - this.getUseItemRemainingTicks();
        }
        return 0;
    }
    
    public void releaseUsingItem() {
        if (!this.useItem.isEmpty()) {
            this.useItem.releaseUsing(this.level, this, this.getUseItemRemainingTicks());
            if (this.useItem.useOnRelease()) {
                this.updatingUsingItem();
            }
        }
        this.stopUsingItem();
    }
    
    public void stopUsingItem() {
        if (!this.level.isClientSide) {
            this.setLivingEntityFlag(1, false);
        }
        this.useItem = ItemStack.EMPTY;
        this.useItemRemaining = 0;
    }
    
    public boolean isBlocking() {
        if (!this.isUsingItem() || this.useItem.isEmpty()) {
            return false;
        }
        final Item blu2 = this.useItem.getItem();
        return blu2.getUseAnimation(this.useItem) == UseAnim.BLOCK && blu2.getUseDuration(this.useItem) - this.useItemRemaining >= 5;
    }
    
    public boolean isSuppressingSlidingDownLadder() {
        return this.isShiftKeyDown();
    }
    
    public boolean isFallFlying() {
        return this.getSharedFlag(7);
    }
    
    @Override
    public boolean isVisuallySwimming() {
        return super.isVisuallySwimming() || (!this.isFallFlying() && this.getPose() == Pose.FALL_FLYING);
    }
    
    public int getFallFlyingTicks() {
        return this.fallFlyTicks;
    }
    
    public boolean randomTeleport(final double double1, final double double2, final double double3, final boolean boolean4) {
        final double double4 = this.getX();
        final double double5 = this.getY();
        final double double6 = this.getZ();
        double double7 = double2;
        boolean boolean5 = false;
        BlockPos fx18 = new BlockPos(double1, double7, double3);
        final Level bru19 = this.level;
        if (bru19.hasChunkAt(fx18)) {
            boolean boolean6 = false;
            while (!boolean6 && fx18.getY() > 0) {
                final BlockPos fx19 = fx18.below();
                final BlockState cee22 = bru19.getBlockState(fx19);
                if (cee22.getMaterial().blocksMotion()) {
                    boolean6 = true;
                }
                else {
                    --double7;
                    fx18 = fx19;
                }
            }
            if (boolean6) {
                this.teleportTo(double1, double7, double3);
                if (bru19.noCollision(this) && !bru19.containsAnyLiquid(this.getBoundingBox())) {
                    boolean5 = true;
                }
            }
        }
        if (!boolean5) {
            this.teleportTo(double4, double5, double6);
            return false;
        }
        if (boolean4) {
            bru19.broadcastEntityEvent(this, (byte)46);
        }
        if (this instanceof PathfinderMob) {
            ((PathfinderMob)this).getNavigation().stop();
        }
        return true;
    }
    
    public boolean isAffectedByPotions() {
        return true;
    }
    
    public boolean attackable() {
        return true;
    }
    
    public void setRecordPlayingNearby(final BlockPos fx, final boolean boolean2) {
    }
    
    public boolean canTakeItem(final ItemStack bly) {
        return false;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddMobPacket(this);
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return (aqu == Pose.SLEEPING) ? LivingEntity.SLEEPING_DIMENSIONS : super.getDimensions(aqu).scale(this.getScale());
    }
    
    public ImmutableList<Pose> getDismountPoses() {
        return (ImmutableList<Pose>)ImmutableList.of(Pose.STANDING);
    }
    
    public AABB getLocalBoundsForPose(final Pose aqu) {
        final EntityDimensions apy3 = this.getDimensions(aqu);
        return new AABB(-apy3.width / 2.0f, 0.0, -apy3.width / 2.0f, apy3.width / 2.0f, apy3.height, apy3.width / 2.0f);
    }
    
    public Optional<BlockPos> getSleepingPos() {
        return this.entityData.<Optional<BlockPos>>get(LivingEntity.SLEEPING_POS_ID);
    }
    
    public void setSleepingPos(final BlockPos fx) {
        this.entityData.<Optional<BlockPos>>set(LivingEntity.SLEEPING_POS_ID, (Optional<BlockPos>)Optional.of(fx));
    }
    
    public void clearSleepingPos() {
        this.entityData.<Optional<BlockPos>>set(LivingEntity.SLEEPING_POS_ID, (Optional<BlockPos>)Optional.empty());
    }
    
    public boolean isSleeping() {
        return this.getSleepingPos().isPresent();
    }
    
    public void startSleeping(final BlockPos fx) {
        if (this.isPassenger()) {
            this.stopRiding();
        }
        final BlockState cee3 = this.level.getBlockState(fx);
        if (cee3.getBlock() instanceof BedBlock) {
            this.level.setBlock(fx, ((StateHolder<O, BlockState>)cee3).<Comparable, Boolean>setValue((Property<Comparable>)BedBlock.OCCUPIED, true), 3);
        }
        this.setPose(Pose.SLEEPING);
        this.setPosToBed(fx);
        this.setSleepingPos(fx);
        this.setDeltaMovement(Vec3.ZERO);
        this.hasImpulse = true;
    }
    
    private void setPosToBed(final BlockPos fx) {
        this.setPos(fx.getX() + 0.5, fx.getY() + 0.6875, fx.getZ() + 0.5);
    }
    
    private boolean checkBedExists() {
        return (boolean)this.getSleepingPos().map(fx -> this.level.getBlockState(fx).getBlock() instanceof BedBlock).orElse(false);
    }
    
    public void stopSleeping() {
        this.getSleepingPos().filter(this.level::hasChunkAt).ifPresent(fx -> {
            final BlockState cee3 = this.level.getBlockState(fx);
            if (cee3.getBlock() instanceof BedBlock) {
                this.level.setBlock(fx, ((StateHolder<O, BlockState>)cee3).<Comparable, Boolean>setValue((Property<Comparable>)BedBlock.OCCUPIED, false), 3);
                final Vec3 dck4 = (Vec3)BedBlock.findStandUpPosition(this.getType(), this.level, fx, this.yRot).orElseGet(() -> {
                    final BlockPos fx2 = fx.above();
                    return new Vec3(fx2.getX() + 0.5, fx2.getY() + 0.1, fx2.getZ() + 0.5);
                });
                final Vec3 dck5 = Vec3.atBottomCenterOf(fx).subtract(dck4).normalize();
                final float float6 = (float)Mth.wrapDegrees(Mth.atan2(dck5.z, dck5.x) * 57.2957763671875 - 90.0);
                this.setPos(dck4.x, dck4.y, dck4.z);
                this.yRot = float6;
                this.xRot = 0.0f;
            }
        });
        final Vec3 dck2 = this.position();
        this.setPose(Pose.STANDING);
        this.setPos(dck2.x, dck2.y, dck2.z);
        this.clearSleepingPos();
    }
    
    @Nullable
    public Direction getBedOrientation() {
        final BlockPos fx2 = (BlockPos)this.getSleepingPos().orElse(null);
        return (fx2 != null) ? BedBlock.getBedOrientation(this.level, fx2) : null;
    }
    
    @Override
    public boolean isInWall() {
        return !this.isSleeping() && super.isInWall();
    }
    
    @Override
    protected final float getEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return (aqu == Pose.SLEEPING) ? 0.2f : this.getStandingEyeHeight(aqu, apy);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return super.getEyeHeight(aqu, apy);
    }
    
    public ItemStack getProjectile(final ItemStack bly) {
        return ItemStack.EMPTY;
    }
    
    public ItemStack eat(final Level bru, final ItemStack bly) {
        if (bly.isEdible()) {
            bru.playSound(null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(bly), SoundSource.NEUTRAL, 1.0f, 1.0f + (bru.random.nextFloat() - bru.random.nextFloat()) * 0.4f);
            this.addEatEffect(bly, bru, this);
            if (!(this instanceof Player) || !((Player)this).abilities.instabuild) {
                bly.shrink(1);
            }
        }
        return bly;
    }
    
    private void addEatEffect(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        final Item blu5 = bly.getItem();
        if (blu5.isEdible()) {
            final List<Pair<MobEffectInstance, Float>> list6 = blu5.getFoodProperties().getEffects();
            for (final Pair<MobEffectInstance, Float> pair8 : list6) {
                if (!bru.isClientSide && pair8.getFirst() != null && bru.random.nextFloat() < (float)pair8.getSecond()) {
                    aqj.addEffect(new MobEffectInstance((MobEffectInstance)pair8.getFirst()));
                }
            }
        }
    }
    
    private static byte entityEventForEquipmentBreak(final EquipmentSlot aqc) {
        switch (aqc) {
            case MAINHAND: {
                return 47;
            }
            case OFFHAND: {
                return 48;
            }
            case HEAD: {
                return 49;
            }
            case CHEST: {
                return 50;
            }
            case FEET: {
                return 52;
            }
            case LEGS: {
                return 51;
            }
            default: {
                return 47;
            }
        }
    }
    
    public void broadcastBreakEvent(final EquipmentSlot aqc) {
        this.level.broadcastEntityEvent(this, entityEventForEquipmentBreak(aqc));
    }
    
    public void broadcastBreakEvent(final InteractionHand aoq) {
        this.broadcastBreakEvent((aoq == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
    }
    
    @Override
    public AABB getBoundingBoxForCulling() {
        if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.DRAGON_HEAD) {
            final float float2 = 0.5f;
            return this.getBoundingBox().inflate(0.5, 0.5, 0.5);
        }
        return super.getBoundingBoxForCulling();
    }
    
    static {
        SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
        SPEED_MODIFIER_SOUL_SPEED_UUID = UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038");
        SPEED_MODIFIER_SPRINTING = new AttributeModifier(LivingEntity.SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", 0.30000001192092896, AttributeModifier.Operation.MULTIPLY_TOTAL);
        DATA_LIVING_ENTITY_FLAGS = SynchedEntityData.<Byte>defineId(LivingEntity.class, EntityDataSerializers.BYTE);
        DATA_HEALTH_ID = SynchedEntityData.<Float>defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
        DATA_EFFECT_COLOR_ID = SynchedEntityData.<Integer>defineId(LivingEntity.class, EntityDataSerializers.INT);
        DATA_EFFECT_AMBIENCE_ID = SynchedEntityData.<Boolean>defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_ARROW_COUNT_ID = SynchedEntityData.<Integer>defineId(LivingEntity.class, EntityDataSerializers.INT);
        DATA_STINGER_COUNT_ID = SynchedEntityData.<Integer>defineId(LivingEntity.class, EntityDataSerializers.INT);
        SLEEPING_POS_ID = SynchedEntityData.<Optional<BlockPos>>defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        SLEEPING_DIMENSIONS = EntityDimensions.fixed(0.2f, 0.2f);
    }
}
