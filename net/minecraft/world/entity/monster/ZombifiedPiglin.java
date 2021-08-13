package net.minecraft.world.entity.monster;

import net.minecraft.util.TimeUtil;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Difficulty;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.world.entity.NeutralMob;

public class ZombifiedPiglin extends Zombie implements NeutralMob {
    private static final UUID SPEED_MODIFIER_ATTACKING_UUID;
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING;
    private static final IntRange FIRST_ANGER_SOUND_DELAY;
    private int playFirstAngerSoundIn;
    private static final IntRange PERSISTENT_ANGER_TIME;
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    private static final IntRange ALERT_INTERVAL;
    private int ticksUntilNextAlert;
    
    public ZombifiedPiglin(final EntityType<? extends ZombifiedPiglin> aqb, final Level bru) {
        super(aqb, bru);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0f);
    }
    
    @Override
    public void setPersistentAngerTarget(@Nullable final UUID uUID) {
        this.persistentAngerTarget = uUID;
    }
    
    @Override
    public double getMyRidingOffset() {
        return this.isBaby() ? -0.05 : -0.45;
    }
    
    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, 5.0);
    }
    
    @Override
    protected boolean convertsInWater() {
        return false;
    }
    
    protected void customServerAiStep() {
        final AttributeInstance are2 = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (this.isAngry()) {
            if (!this.isBaby() && !are2.hasModifier(ZombifiedPiglin.SPEED_MODIFIER_ATTACKING)) {
                are2.addTransientModifier(ZombifiedPiglin.SPEED_MODIFIER_ATTACKING);
            }
            this.maybePlayFirstAngerSound();
        }
        else if (are2.hasModifier(ZombifiedPiglin.SPEED_MODIFIER_ATTACKING)) {
            are2.removeModifier(ZombifiedPiglin.SPEED_MODIFIER_ATTACKING);
        }
        this.updatePersistentAnger((ServerLevel)this.level, true);
        if (this.getTarget() != null) {
            this.maybeAlertOthers();
        }
        if (this.isAngry()) {
            this.lastHurtByPlayerTime = this.tickCount;
        }
        super.customServerAiStep();
    }
    
    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            --this.playFirstAngerSoundIn;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }
    }
    
    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) {
            --this.ticksUntilNextAlert;
            return;
        }
        if (this.getSensing().canSee(this.getTarget())) {
            this.alertOthers();
        }
        this.ticksUntilNextAlert = ZombifiedPiglin.ALERT_INTERVAL.randomValue(this.random);
    }
    
    private void alertOthers() {
        final double double2 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        final AABB dcf4 = AABB.unitCubeFromLowerCorner(this.position()).inflate(double2, 10.0, double2);
        this.level.<Entity>getLoadedEntitiesOfClass((java.lang.Class<? extends Entity>)ZombifiedPiglin.class, dcf4).stream().filter(bei -> bei != this).filter(bei -> bei.getTarget() == null).filter(bei -> !bei.isAlliedTo(this.getTarget())).forEach(bei -> bei.setTarget(this.getTarget()));
    }
    
    private void playAngerSound() {
        this.playSound(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, this.getSoundVolume() * 2.0f, this.getVoicePitch() * 1.8f);
    }
    
    @Override
    public void setTarget(@Nullable final LivingEntity aqj) {
        if (this.getTarget() == null && aqj != null) {
            this.playFirstAngerSoundIn = ZombifiedPiglin.FIRST_ANGER_SOUND_DELAY.randomValue(this.random);
            this.ticksUntilNextAlert = ZombifiedPiglin.ALERT_INTERVAL.randomValue(this.random);
        }
        if (aqj instanceof Player) {
            this.setLastHurtByPlayer((Player)aqj);
        }
        super.setTarget(aqj);
    }
    
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ZombifiedPiglin.PERSISTENT_ANGER_TIME.randomValue(this.random));
    }
    
    public static boolean checkZombifiedPiglinSpawnRules(final EntityType<ZombifiedPiglin> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getDifficulty() != Difficulty.PEACEFUL && brv.getBlockState(fx.below()).getBlock() != Blocks.NETHER_WART_BLOCK;
    }
    
    public boolean checkSpawnObstruction(final LevelReader brw) {
        return brw.isUnobstructed(this) && !brw.containsAnyLiquid(this.getBoundingBox());
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        this.addPersistentAngerSaveData(md);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.readPersistentAngerSaveData((ServerLevel)this.level, md);
    }
    
    @Override
    public void setRemainingPersistentAngerTime(final int integer) {
        this.remainingPersistentAngerTime = integer;
    }
    
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return !this.isInvulnerableTo(aph) && super.hurt(aph, float2);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }
    
    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
    
    @Override
    protected void randomizeReinforcementsChance() {
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0);
    }
    
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    
    @Override
    public boolean isPreventingPlayerRest(final Player bft) {
        return this.isAngryAt(bft);
    }
    
    static {
        SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
        SPEED_MODIFIER_ATTACKING = new AttributeModifier(ZombifiedPiglin.SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.05, AttributeModifier.Operation.ADDITION);
        FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
        ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
    }
}
