package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import java.util.Random;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.Map;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import com.google.common.collect.Maps;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.Difficulty;
import java.util.function.Predicate;

public class Vindicator extends AbstractIllager {
    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
    private boolean isJohnny;
    
    public Vindicator(final EntityType<? extends Vindicator> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new VindicatorBreakDoorGoal(this));
        this.goalSelector.addGoal(2, new RaiderOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new HoldGroundAttackGoal(this, 10.0f));
        this.goalSelector.addGoal(4, new VindicatorMeleeAttackGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Raider.class }).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new VindicatorJohnnyAttackGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));
    }
    
    @Override
    protected void customServerAiStep() {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            final boolean boolean2 = ((ServerLevel)this.level).isRaided(this.blockPosition());
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(boolean2);
        }
        super.customServerAiStep();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3499999940395355).add(Attributes.FOLLOW_RANGE, 12.0).add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.isJohnny) {
            md.putBoolean("Johnny", true);
        }
    }
    
    @Override
    public IllagerArmPose getArmPose() {
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        if (this.isCelebrating()) {
            return IllagerArmPose.CELEBRATING;
        }
        return IllagerArmPose.CROSSED;
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("Johnny", 99)) {
            this.isJohnny = md.getBoolean("Johnny");
        }
    }
    
    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        final SpawnGroupData aqz2 = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(aop);
        this.populateDefaultEquipmentEnchantments(aop);
        return aqz2;
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }
    }
    
    public boolean isAlliedTo(final Entity apx) {
        return super.isAlliedTo(apx) || (apx instanceof LivingEntity && ((LivingEntity)apx).getMobType() == MobType.ILLAGER && this.getTeam() == null && apx.getTeam() == null);
    }
    
    public void setCustomName(@Nullable final Component nr) {
        super.setCustomName(nr);
        if (!this.isJohnny && nr != null && nr.getString().equals("Johnny")) {
            this.isJohnny = true;
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.VINDICATOR_HURT;
    }
    
    @Override
    public void applyRaidBuffs(final int integer, final boolean boolean2) {
        final ItemStack bly4 = new ItemStack(Items.IRON_AXE);
        final Raid bgy5 = this.getCurrentRaid();
        int integer2 = 1;
        if (integer > bgy5.getNumGroups(Difficulty.NORMAL)) {
            integer2 = 2;
        }
        final boolean boolean3 = this.random.nextFloat() <= bgy5.getEnchantOdds();
        if (boolean3) {
            final Map<Enchantment, Integer> map8 = (Map<Enchantment, Integer>)Maps.newHashMap();
            map8.put(Enchantments.SHARPNESS, integer2);
            EnchantmentHelper.setEnchantments(map8, bly4);
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, bly4);
    }
    
    static {
        DOOR_BREAKING_PREDICATE = (aoo -> aoo == Difficulty.NORMAL || aoo == Difficulty.HARD);
    }
    
    class VindicatorMeleeAttackGoal extends MeleeAttackGoal {
        public VindicatorMeleeAttackGoal(final Vindicator bec2) {
            super(bec2, 1.0, false);
        }
        
        @Override
        protected double getAttackReachSqr(final LivingEntity aqj) {
            if (this.mob.getVehicle() instanceof Ravager) {
                final float float3 = this.mob.getVehicle().getBbWidth() - 0.1f;
                return float3 * 2.0f * (float3 * 2.0f) + aqj.getBbWidth();
            }
            return super.getAttackReachSqr(aqj);
        }
    }
    
    static class VindicatorBreakDoorGoal extends BreakDoorGoal {
        public VindicatorBreakDoorGoal(final Mob aqk) {
            super(aqk, 6, Vindicator.DOOR_BREAKING_PREDICATE);
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canContinueToUse() {
            final Vindicator bec2 = (Vindicator)this.mob;
            return bec2.hasActiveRaid() && super.canContinueToUse();
        }
        
        @Override
        public boolean canUse() {
            final Vindicator bec2 = (Vindicator)this.mob;
            return bec2.hasActiveRaid() && bec2.random.nextInt(10) == 0 && super.canUse();
        }
        
        @Override
        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }
    
    static class VindicatorJohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public VindicatorJohnnyAttackGoal(final Vindicator bec) {
            super(bec, LivingEntity.class, 0, true, true, (Predicate<LivingEntity>)LivingEntity::attackable);
        }
        
        @Override
        public boolean canUse() {
            return ((Vindicator)this.mob).isJohnny && super.canUse();
        }
        
        @Override
        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }
}
