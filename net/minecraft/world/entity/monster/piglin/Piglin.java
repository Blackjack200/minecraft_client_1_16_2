package net.minecraft.world.entity.monster.piglin;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.CrossbowAttackMob;

public class Piglin extends AbstractPiglin implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID;
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW;
    private static final EntityDataAccessor<Boolean> DATA_IS_DANCING;
    private static final UUID SPEED_MODIFIER_BABY_UUID;
    private static final AttributeModifier SPEED_MODIFIER_BABY;
    private final SimpleContainer inventory;
    private boolean cannotHunt;
    protected static final ImmutableList<SensorType<? extends Sensor<? super Piglin>>> SENSOR_TYPES;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    
    public Piglin(final EntityType<? extends AbstractPiglin> aqb, final Level bru) {
        super(aqb, bru);
        this.inventory = new SimpleContainer(8);
        this.cannotHunt = false;
        this.xpReward = 5;
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.isBaby()) {
            md.putBoolean("IsBaby", true);
        }
        if (this.cannotHunt) {
            md.putBoolean("CannotHunt", true);
        }
        md.put("Inventory", (Tag)this.inventory.createTag());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setBaby(md.getBoolean("IsBaby"));
        this.setCannotHunt(md.getBoolean("CannotHunt"));
        this.inventory.fromTag(md.getList("Inventory", 10));
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
    }
    
    protected ItemStack addToInventory(final ItemStack bly) {
        return this.inventory.addItem(bly);
    }
    
    protected boolean canAddToInventory(final ItemStack bly) {
        return this.inventory.canAddItem(bly);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Piglin.DATA_BABY_ID, false);
        this.entityData.<Boolean>define(Piglin.DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.<Boolean>define(Piglin.DATA_IS_DANCING, false);
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (Piglin.DATA_BABY_ID.equals(us)) {
            this.refreshDimensions();
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3499999940395355).add(Attributes.ATTACK_DAMAGE, 5.0);
    }
    
    public static boolean checkPiglinSpawnRules(final EntityType<Piglin> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return !brv.getBlockState(fx.below()).is(Blocks.NETHER_WART_BLOCK);
    }
    
    @Nullable
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqm != MobSpawnType.STRUCTURE) {
            if (bsh.getRandom().nextFloat() < 0.2f) {
                this.setBaby(true);
            }
            else if (this.isAdult()) {
                this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
            }
        }
        PiglinAi.initMemories(this);
        this.populateDefaultEquipmentSlots(aop);
        this.populateDefaultEquipmentEnchantments(aop);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }
    
    public boolean removeWhenFarAway(final double double1) {
        return !this.isPersistenceRequired();
    }
    
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        if (this.isAdult()) {
            this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
            this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
        }
    }
    
    private void maybeWearArmor(final EquipmentSlot aqc, final ItemStack bly) {
        if (this.level.random.nextFloat() < 0.1f) {
            this.setItemSlot(aqc, bly);
        }
    }
    
    protected Brain.Provider<Piglin> brainProvider() {
        return Brain.<Piglin>provider(Piglin.MEMORY_TYPES, (java.util.Collection<? extends SensorType<? extends Sensor<? super Piglin>>>)Piglin.SENSOR_TYPES);
    }
    
    protected Brain<?> makeBrain(final Dynamic<?> dynamic) {
        return PiglinAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }
    
    public Brain<Piglin> getBrain() {
        return (Brain<Piglin>)super.getBrain();
    }
    
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final InteractionResult aor4 = super.mobInteract(bft, aoq);
        if (aor4.consumesAction()) {
            return aor4;
        }
        if (this.level.isClientSide) {
            final boolean boolean5 = PiglinAi.canAdmire(this, bft.getItemInHand(aoq)) && this.getArmPose() != PiglinArmPose.ADMIRING_ITEM;
            return boolean5 ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return PiglinAi.mobInteract(this, bft, aoq);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return this.isBaby() ? 0.93f : 1.74f;
    }
    
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.92;
    }
    
    public void setBaby(final boolean boolean1) {
        this.getEntityData().<Boolean>set(Piglin.DATA_BABY_ID, boolean1);
        if (!this.level.isClientSide) {
            final AttributeInstance are3 = this.getAttribute(Attributes.MOVEMENT_SPEED);
            are3.removeModifier(Piglin.SPEED_MODIFIER_BABY);
            if (boolean1) {
                are3.addTransientModifier(Piglin.SPEED_MODIFIER_BABY);
            }
        }
    }
    
    public boolean isBaby() {
        return this.getEntityData().<Boolean>get(Piglin.DATA_BABY_ID);
    }
    
    private void setCannotHunt(final boolean boolean1) {
        this.cannotHunt = boolean1;
    }
    
    @Override
    protected boolean canHunt() {
        return !this.cannotHunt;
    }
    
    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("piglinBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        PiglinAi.updateActivity(this);
        super.customServerAiStep();
    }
    
    protected int getExperienceReward(final Player bft) {
        return this.xpReward;
    }
    
    @Override
    protected void finishConversion(final ServerLevel aag) {
        PiglinAi.cancelAdmiring(this);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        super.finishConversion(aag);
    }
    
    private ItemStack createSpawnWeapon() {
        if (this.random.nextFloat() < 0.5) {
            return new ItemStack(Items.CROSSBOW);
        }
        return new ItemStack(Items.GOLDEN_SWORD);
    }
    
    private boolean isChargingCrossbow() {
        return this.entityData.<Boolean>get(Piglin.DATA_IS_CHARGING_CROSSBOW);
    }
    
    @Override
    public void setChargingCrossbow(final boolean boolean1) {
        this.entityData.<Boolean>set(Piglin.DATA_IS_CHARGING_CROSSBOW, boolean1);
    }
    
    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }
    
    @Override
    public PiglinArmPose getArmPose() {
        if (this.isDancing()) {
            return PiglinArmPose.DANCING;
        }
        if (PiglinAi.isLovedItem(this.getOffhandItem().getItem())) {
            return PiglinArmPose.ADMIRING_ITEM;
        }
        if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        }
        if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        }
        if (this.isAggressive() && this.isHolding(Items.CROSSBOW)) {
            return PiglinArmPose.CROSSBOW_HOLD;
        }
        return PiglinArmPose.DEFAULT;
    }
    
    public boolean isDancing() {
        return this.entityData.<Boolean>get(Piglin.DATA_IS_DANCING);
    }
    
    public void setDancing(final boolean boolean1) {
        this.entityData.<Boolean>set(Piglin.DATA_IS_DANCING, boolean1);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        final boolean boolean4 = super.hurt(aph, float2);
        if (this.level.isClientSide) {
            return false;
        }
        if (boolean4 && aph.getEntity() instanceof LivingEntity) {
            PiglinAi.wasHurtBy(this, (LivingEntity)aph.getEntity());
        }
        return boolean4;
    }
    
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        this.performCrossbowAttack(this, 1.6f);
    }
    
    @Override
    public void shootCrossbowProjectile(final LivingEntity aqj, final ItemStack bly, final Projectile bgj, final float float4) {
        this.shootCrossbowProjectile(this, aqj, bgj, float4, 1.6f);
    }
    
    public boolean canFireProjectileWeapon(final ProjectileWeaponItem bml) {
        return bml == Items.CROSSBOW;
    }
    
    protected void holdInMainHand(final ItemStack bly) {
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, bly);
    }
    
    protected void holdInOffHand(final ItemStack bly) {
        if (bly.getItem() == PiglinAi.BARTERING_ITEM) {
            this.setItemSlot(EquipmentSlot.OFFHAND, bly);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        }
        else {
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, bly);
        }
    }
    
    public boolean wantsToPickUp(final ItemStack bly) {
        return this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.canPickUpLoot() && PiglinAi.wantsToPickup(this, bly);
    }
    
    protected boolean canReplaceCurrentItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = Mob.getEquipmentSlotForItem(bly);
        final ItemStack bly2 = this.getItemBySlot(aqc3);
        return this.canReplaceCurrentItem(bly, bly2);
    }
    
    protected boolean canReplaceCurrentItem(final ItemStack bly1, final ItemStack bly2) {
        if (EnchantmentHelper.hasBindingCurse(bly2)) {
            return false;
        }
        final boolean boolean4 = PiglinAi.isLovedItem(bly1.getItem()) || bly1.getItem() == Items.CROSSBOW;
        final boolean boolean5 = PiglinAi.isLovedItem(bly2.getItem()) || bly2.getItem() == Items.CROSSBOW;
        return (boolean4 && !boolean5) || ((boolean4 || !boolean5) && (!this.isAdult() || bly1.getItem() == Items.CROSSBOW || bly2.getItem() != Items.CROSSBOW) && super.canReplaceCurrentItem(bly1, bly2));
    }
    
    protected void pickUpItem(final ItemEntity bcs) {
        this.onItemPickup(bcs);
        PiglinAi.pickUpItem(this, bcs);
    }
    
    public boolean startRiding(Entity apx, final boolean boolean2) {
        if (this.isBaby() && apx.getType() == EntityType.HOGLIN) {
            apx = this.getTopPassenger(apx, 3);
        }
        return super.startRiding(apx, boolean2);
    }
    
    private Entity getTopPassenger(final Entity apx, final int integer) {
        final List<Entity> list4 = apx.getPassengers();
        if (integer == 1 || list4.isEmpty()) {
            return apx;
        }
        return this.getTopPassenger((Entity)list4.get(0), integer - 1);
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.level.isClientSide) {
            return null;
        }
        return (SoundEvent)PiglinAi.getSoundForCurrentActivity(this).orElse(null);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PIGLIN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15f, 1.0f);
    }
    
    protected void playSound(final SoundEvent adn) {
        this.playSound(adn, this.getSoundVolume(), this.getVoicePitch());
    }
    
    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }
    
    static {
        DATA_BABY_ID = SynchedEntityData.<Boolean>defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.<Boolean>defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_DANCING = SynchedEntityData.<Boolean>defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
        SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
        SPEED_MODIFIER_BABY = new AttributeModifier(Piglin.SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.20000000298023224, AttributeModifier.Operation.MULTIPLY_BASE);
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, (Object[])new MemoryModuleType[] { MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT });
    }
}
