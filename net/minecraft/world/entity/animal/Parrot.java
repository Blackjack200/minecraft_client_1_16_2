package net.minecraft.world.entity.animal;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Items;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.HashMap;
import net.minecraft.sounds.SoundSource;
import com.google.common.collect.Lists;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.Mob;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Parrot extends ShoulderRidingEntity implements FlyingAnimal {
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID;
    private static final Predicate<Mob> NOT_PARROT_PREDICATE;
    private static final Item POISONOUS_FOOD;
    private static final Set<Item> TAME_FOOD;
    private static final Map<EntityType<?>, SoundEvent> MOB_SOUND_MAP;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping;
    private boolean partyParrot;
    private BlockPos jukebox;
    
    public Parrot(final EntityType<? extends Parrot> aqb, final Level bru) {
        super(aqb, bru);
        this.flapping = 1.0f;
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0f);
    }
    
    @Nullable
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setVariant(this.random.nextInt(5));
        if (aqz == null) {
            aqz = new AgableMobGroupData(false);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public boolean isBaby() {
        return false;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0, 5.0f, 1.0f, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LandOnOwnersShoulderGoal(this));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0, 3.0f, 7.0f));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.FLYING_SPEED, 0.4000000059604645).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }
    
    protected PathNavigation createNavigation(final Level bru) {
        final FlyingPathNavigation aye3 = new FlyingPathNavigation(this, bru);
        aye3.setCanOpenDoors(false);
        aye3.setCanFloat(true);
        aye3.setCanPassDoors(true);
        return aye3;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.6f;
    }
    
    public void aiStep() {
        if (this.jukebox == null || !this.jukebox.closerThan(this.position(), 3.46) || !this.level.getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.partyParrot = false;
            this.jukebox = null;
        }
        if (this.level.random.nextInt(400) == 0) {
            imitateNearbyMobs(this.level, this);
        }
        super.aiStep();
        this.calculateFlapping();
    }
    
    public void setRecordPlayingNearby(final BlockPos fx, final boolean boolean2) {
        this.jukebox = fx;
        this.partyParrot = boolean2;
    }
    
    public boolean isPartyParrot() {
        return this.partyParrot;
    }
    
    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(((this.onGround || this.isPassenger()) ? -1 : 4) * 0.3);
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0f, 1.0f);
        if (!this.onGround && this.flapping < 1.0f) {
            this.flapping = 1.0f;
        }
        this.flapping *= (float)0.9;
        final Vec3 dck2 = this.getDeltaMovement();
        if (!this.onGround && dck2.y < 0.0) {
            this.setDeltaMovement(dck2.multiply(1.0, 0.6, 1.0));
        }
        this.flap += this.flapping * 2.0f;
    }
    
    public static boolean imitateNearbyMobs(final Level bru, final Entity apx) {
        if (!apx.isAlive() || apx.isSilent() || bru.random.nextInt(2) != 0) {
            return false;
        }
        final List<Mob> list3 = bru.<Mob>getEntitiesOfClass((java.lang.Class<? extends Mob>)Mob.class, apx.getBoundingBox().inflate(20.0), (java.util.function.Predicate<? super Mob>)Parrot.NOT_PARROT_PREDICATE);
        if (!list3.isEmpty()) {
            final Mob aqk4 = (Mob)list3.get(bru.random.nextInt(list3.size()));
            if (!aqk4.isSilent()) {
                final SoundEvent adn5 = getImitatedSound(aqk4.getType());
                bru.playSound(null, apx.getX(), apx.getY(), apx.getZ(), adn5, apx.getSoundSource(), 0.7f, getPitch(bru.random));
                return true;
            }
        }
        return false;
    }
    
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (!this.isTame() && Parrot.TAME_FOOD.contains(bly4.getItem())) {
            if (!bft.abilities.instabuild) {
                bly4.shrink(1);
            }
            if (!this.isSilent()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            }
            if (!this.level.isClientSide) {
                if (this.random.nextInt(10) == 0) {
                    this.tame(bft);
                    this.level.broadcastEntityEvent(this, (byte)7);
                }
                else {
                    this.level.broadcastEntityEvent(this, (byte)6);
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (bly4.getItem() == Parrot.POISONOUS_FOOD) {
            if (!bft.abilities.instabuild) {
                bly4.shrink(1);
            }
            this.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
            if (bft.isCreative() || !this.isInvulnerable()) {
                this.hurt(DamageSource.playerAttack(bft), Float.MAX_VALUE);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (!this.isFlying() && this.isTame() && this.isOwnedBy(bft)) {
            if (!this.level.isClientSide) {
                this.setOrderedToSit(!this.isOrderedToSit());
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(bft, aoq);
    }
    
    public boolean isFood(final ItemStack bly) {
        return false;
    }
    
    public static boolean checkParrotSpawnRules(final EntityType<Parrot> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final BlockState cee6 = brv.getBlockState(fx.below());
        return (cee6.is(BlockTags.LEAVES) || cee6.is(Blocks.GRASS_BLOCK) || cee6.is(BlockTags.LOGS) || cee6.is(Blocks.AIR)) && brv.getRawBrightness(fx, 0) > 8;
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
    }
    
    public boolean canMate(final Animal azw) {
        return false;
    }
    
    @Nullable
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return null;
    }
    
    public boolean doHurtTarget(final Entity apx) {
        return apx.hurt(DamageSource.mobAttack(this), 3.0f);
    }
    
    @Nullable
    public SoundEvent getAmbientSound() {
        return getAmbient(this.level, this.level.random);
    }
    
    public static SoundEvent getAmbient(final Level bru, final Random random) {
        if (bru.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(1000) == 0) {
            final List<EntityType<?>> list3 = (List<EntityType<?>>)Lists.newArrayList((Iterable)Parrot.MOB_SOUND_MAP.keySet());
            return getImitatedSound(list3.get(random.nextInt(list3.size())));
        }
        return SoundEvents.PARROT_AMBIENT;
    }
    
    private static SoundEvent getImitatedSound(final EntityType<?> aqb) {
        return (SoundEvent)Parrot.MOB_SOUND_MAP.getOrDefault(aqb, SoundEvents.PARROT_AMBIENT);
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PARROT_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15f, 1.0f);
    }
    
    protected float playFlySound(final float float1) {
        this.playSound(SoundEvents.PARROT_FLY, 0.15f, 1.0f);
        return float1 + this.flapSpeed / 2.0f;
    }
    
    protected boolean makeFlySound() {
        return true;
    }
    
    protected float getVoicePitch() {
        return getPitch(this.random);
    }
    
    public static float getPitch(final Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f;
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }
    
    public boolean isPushable() {
        return true;
    }
    
    protected void doPush(final Entity apx) {
        if (apx instanceof Player) {
            return;
        }
        super.doPush(apx);
    }
    
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        this.setOrderedToSit(false);
        return super.hurt(aph, float2);
    }
    
    public int getVariant() {
        return Mth.clamp(this.entityData.<Integer>get(Parrot.DATA_VARIANT_ID), 0, 4);
    }
    
    public void setVariant(final int integer) {
        this.entityData.<Integer>set(Parrot.DATA_VARIANT_ID, integer);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Parrot.DATA_VARIANT_ID, 0);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Variant", this.getVariant());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setVariant(md.getInt("Variant"));
    }
    
    public boolean isFlying() {
        return !this.onGround;
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        DATA_VARIANT_ID = SynchedEntityData.<Integer>defineId(Parrot.class, EntityDataSerializers.INT);
        NOT_PARROT_PREDICATE = (Predicate)new Predicate<Mob>() {
            public boolean test(@Nullable final Mob aqk) {
                return aqk != null && Parrot.MOB_SOUND_MAP.containsKey(aqk.getType());
            }
        };
        POISONOUS_FOOD = Items.COOKIE;
        TAME_FOOD = (Set)Sets.newHashSet((Object[])new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
        MOB_SOUND_MAP = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(EntityType.BLAZE, SoundEvents.PARROT_IMITATE_BLAZE);
            hashMap.put(EntityType.CAVE_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
            hashMap.put(EntityType.CREEPER, SoundEvents.PARROT_IMITATE_CREEPER);
            hashMap.put(EntityType.DROWNED, SoundEvents.PARROT_IMITATE_DROWNED);
            hashMap.put(EntityType.ELDER_GUARDIAN, SoundEvents.PARROT_IMITATE_ELDER_GUARDIAN);
            hashMap.put(EntityType.ENDER_DRAGON, SoundEvents.PARROT_IMITATE_ENDER_DRAGON);
            hashMap.put(EntityType.ENDERMITE, SoundEvents.PARROT_IMITATE_ENDERMITE);
            hashMap.put(EntityType.EVOKER, SoundEvents.PARROT_IMITATE_EVOKER);
            hashMap.put(EntityType.GHAST, SoundEvents.PARROT_IMITATE_GHAST);
            hashMap.put(EntityType.GUARDIAN, SoundEvents.PARROT_IMITATE_GUARDIAN);
            hashMap.put(EntityType.HOGLIN, SoundEvents.PARROT_IMITATE_HOGLIN);
            hashMap.put(EntityType.HUSK, SoundEvents.PARROT_IMITATE_HUSK);
            hashMap.put(EntityType.ILLUSIONER, SoundEvents.PARROT_IMITATE_ILLUSIONER);
            hashMap.put(EntityType.MAGMA_CUBE, SoundEvents.PARROT_IMITATE_MAGMA_CUBE);
            hashMap.put(EntityType.PHANTOM, SoundEvents.PARROT_IMITATE_PHANTOM);
            hashMap.put(EntityType.PIGLIN, SoundEvents.PARROT_IMITATE_PIGLIN);
            hashMap.put(EntityType.PIGLIN_BRUTE, SoundEvents.PARROT_IMITATE_PIGLIN_BRUTE);
            hashMap.put(EntityType.PILLAGER, SoundEvents.PARROT_IMITATE_PILLAGER);
            hashMap.put(EntityType.RAVAGER, SoundEvents.PARROT_IMITATE_RAVAGER);
            hashMap.put(EntityType.SHULKER, SoundEvents.PARROT_IMITATE_SHULKER);
            hashMap.put(EntityType.SILVERFISH, SoundEvents.PARROT_IMITATE_SILVERFISH);
            hashMap.put(EntityType.SKELETON, SoundEvents.PARROT_IMITATE_SKELETON);
            hashMap.put(EntityType.SLIME, SoundEvents.PARROT_IMITATE_SLIME);
            hashMap.put(EntityType.SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
            hashMap.put(EntityType.STRAY, SoundEvents.PARROT_IMITATE_STRAY);
            hashMap.put(EntityType.VEX, SoundEvents.PARROT_IMITATE_VEX);
            hashMap.put(EntityType.VINDICATOR, SoundEvents.PARROT_IMITATE_VINDICATOR);
            hashMap.put(EntityType.WITCH, SoundEvents.PARROT_IMITATE_WITCH);
            hashMap.put(EntityType.WITHER, SoundEvents.PARROT_IMITATE_WITHER);
            hashMap.put(EntityType.WITHER_SKELETON, SoundEvents.PARROT_IMITATE_WITHER_SKELETON);
            hashMap.put(EntityType.ZOGLIN, SoundEvents.PARROT_IMITATE_ZOGLIN);
            hashMap.put(EntityType.ZOMBIE, SoundEvents.PARROT_IMITATE_ZOMBIE);
            hashMap.put(EntityType.ZOMBIE_VILLAGER, SoundEvents.PARROT_IMITATE_ZOMBIE_VILLAGER);
        }));
    }
}
