package net.minecraft.world.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.Difficulty;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.Map;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.EquipmentSlot;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.SimpleContainer;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Pillager extends AbstractIllager implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW;
    private final SimpleContainer inventory;
    
    public Pillager(final EntityType<? extends Pillager> aqb, final Level bru) {
        super(aqb, bru);
        this.inventory = new SimpleContainer(5);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new HoldGroundAttackGoal(this, 10.0f));
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0, 8.0f));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 15.0f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Raider.class }).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3499999940395355).add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Pillager.IS_CHARGING_CROSSBOW, false);
    }
    
    public boolean canFireProjectileWeapon(final ProjectileWeaponItem bml) {
        return bml == Items.CROSSBOW;
    }
    
    public boolean isChargingCrossbow() {
        return this.entityData.<Boolean>get(Pillager.IS_CHARGING_CROSSBOW);
    }
    
    @Override
    public void setChargingCrossbow(final boolean boolean1) {
        this.entityData.<Boolean>set(Pillager.IS_CHARGING_CROSSBOW, boolean1);
    }
    
    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        final ListTag mj3 = new ListTag();
        for (int integer4 = 0; integer4 < this.inventory.getContainerSize(); ++integer4) {
            final ItemStack bly5 = this.inventory.getItem(integer4);
            if (!bly5.isEmpty()) {
                mj3.add(bly5.save(new CompoundTag()));
            }
        }
        md.put("Inventory", (Tag)mj3);
    }
    
    @Override
    public IllagerArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return IllagerArmPose.CROSSBOW_CHARGE;
        }
        if (this.isHolding(Items.CROSSBOW)) {
            return IllagerArmPose.CROSSBOW_HOLD;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        return IllagerArmPose.NEUTRAL;
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        final ListTag mj3 = md.getList("Inventory", 10);
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            final ItemStack bly5 = ItemStack.of(mj3.getCompound(integer4));
            if (!bly5.isEmpty()) {
                this.inventory.addItem(bly5);
            }
        }
        this.setCanPickUpLoot(true);
    }
    
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        final BlockState cee4 = brw.getBlockState(fx.below());
        if (cee4.is(Blocks.GRASS_BLOCK) || cee4.is(Blocks.SAND)) {
            return 10.0f;
        }
        return 0.5f - brw.getBrightness(fx);
    }
    
    public int getMaxSpawnClusterSize() {
        return 1;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.populateDefaultEquipmentSlots(aop);
        this.populateDefaultEquipmentEnchantments(aop);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }
    
    protected void enchantSpawnedWeapon(final float float1) {
        super.enchantSpawnedWeapon(float1);
        if (this.random.nextInt(300) == 0) {
            final ItemStack bly3 = this.getMainHandItem();
            if (bly3.getItem() == Items.CROSSBOW) {
                final Map<Enchantment, Integer> map4 = EnchantmentHelper.getEnchantments(bly3);
                map4.putIfAbsent(Enchantments.PIERCING, 1);
                EnchantmentHelper.setEnchantments(map4, bly3);
                this.setItemSlot(EquipmentSlot.MAINHAND, bly3);
            }
        }
    }
    
    public boolean isAlliedTo(final Entity apx) {
        return super.isAlliedTo(apx) || (apx instanceof LivingEntity && ((LivingEntity)apx).getMobType() == MobType.ILLAGER && this.getTeam() == null && apx.getTeam() == null);
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PILLAGER_HURT;
    }
    
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        this.performCrossbowAttack(this, 1.6f);
    }
    
    @Override
    public void shootCrossbowProjectile(final LivingEntity aqj, final ItemStack bly, final Projectile bgj, final float float4) {
        this.shootCrossbowProjectile(this, aqj, bgj, float4, 1.6f);
    }
    
    @Override
    protected void pickUpItem(final ItemEntity bcs) {
        final ItemStack bly3 = bcs.getItem();
        if (bly3.getItem() instanceof BannerItem) {
            super.pickUpItem(bcs);
        }
        else {
            final Item blu4 = bly3.getItem();
            if (this.wantsItem(blu4)) {
                this.onItemPickup(bcs);
                final ItemStack bly4 = this.inventory.addItem(bly3);
                if (bly4.isEmpty()) {
                    bcs.remove();
                }
                else {
                    bly3.setCount(bly4.getCount());
                }
            }
        }
    }
    
    private boolean wantsItem(final Item blu) {
        return this.hasActiveRaid() && blu == Items.WHITE_BANNER;
    }
    
    public boolean setSlot(final int integer, final ItemStack bly) {
        if (super.setSlot(integer, bly)) {
            return true;
        }
        final int integer2 = integer - 300;
        if (integer2 >= 0 && integer2 < this.inventory.getContainerSize()) {
            this.inventory.setItem(integer2, bly);
            return true;
        }
        return false;
    }
    
    @Override
    public void applyRaidBuffs(final int integer, final boolean boolean2) {
        final Raid bgy4 = this.getCurrentRaid();
        final boolean boolean3 = this.random.nextFloat() <= bgy4.getEnchantOdds();
        if (boolean3) {
            final ItemStack bly6 = new ItemStack(Items.CROSSBOW);
            final Map<Enchantment, Integer> map7 = (Map<Enchantment, Integer>)Maps.newHashMap();
            if (integer > bgy4.getNumGroups(Difficulty.NORMAL)) {
                map7.put(Enchantments.QUICK_CHARGE, 2);
            }
            else if (integer > bgy4.getNumGroups(Difficulty.EASY)) {
                map7.put(Enchantments.QUICK_CHARGE, 1);
            }
            map7.put(Enchantments.MULTISHOT, 1);
            EnchantmentHelper.setEnchantments(map7, bly6);
            this.setItemSlot(EquipmentSlot.MAINHAND, bly6);
        }
    }
    
    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }
    
    static {
        IS_CHARGING_CROSSBOW = SynchedEntityData.<Boolean>defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
    }
}
