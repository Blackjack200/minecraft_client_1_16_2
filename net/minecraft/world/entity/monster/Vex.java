package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.ai.control.MoveControl;
import java.util.Random;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Vex extends Monster {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private Mob owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;
    
    public Vex(final EntityType<? extends Vex> aqb, final Level bru) {
        super(aqb, bru);
        this.moveControl = new VexMoveControl(this);
        this.xpReward = 3;
    }
    
    public void move(final MoverType aqo, final Vec3 dck) {
        super.move(aqo, dck);
        this.checkInsideBlocks();
    }
    
    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 1.0f);
        }
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new VexChargeAttackGoal());
        this.goalSelector.addGoal(8, new VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Raider.class }).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new VexCopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.ATTACK_DAMAGE, 4.0);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Vex.DATA_FLAGS_ID, (Byte)0);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("BoundX")) {
            this.boundOrigin = new BlockPos(md.getInt("BoundX"), md.getInt("BoundY"), md.getInt("BoundZ"));
        }
        if (md.contains("LifeTicks")) {
            this.setLimitedLife(md.getInt("LifeTicks"));
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.boundOrigin != null) {
            md.putInt("BoundX", this.boundOrigin.getX());
            md.putInt("BoundY", this.boundOrigin.getY());
            md.putInt("BoundZ", this.boundOrigin.getZ());
        }
        if (this.hasLimitedLife) {
            md.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }
    
    public Mob getOwner() {
        return this.owner;
    }
    
    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }
    
    public void setBoundOrigin(@Nullable final BlockPos fx) {
        this.boundOrigin = fx;
    }
    
    private boolean getVexFlag(final int integer) {
        final int integer2 = this.entityData.<Byte>get(Vex.DATA_FLAGS_ID);
        return (integer2 & integer) != 0x0;
    }
    
    private void setVexFlag(final int integer, final boolean boolean2) {
        int integer2 = this.entityData.<Byte>get(Vex.DATA_FLAGS_ID);
        if (boolean2) {
            integer2 |= integer;
        }
        else {
            integer2 &= ~integer;
        }
        this.entityData.<Byte>set(Vex.DATA_FLAGS_ID, (byte)(integer2 & 0xFF));
    }
    
    public boolean isCharging() {
        return this.getVexFlag(1);
    }
    
    public void setIsCharging(final boolean boolean1) {
        this.setVexFlag(1, boolean1);
    }
    
    public void setOwner(final Mob aqk) {
        this.owner = aqk;
    }
    
    public void setLimitedLife(final int integer) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = integer;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.VEX_HURT;
    }
    
    public float getBrightness() {
        return 1.0f;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.populateDefaultEquipmentSlots(aop);
        this.populateDefaultEquipmentEnchantments(aop);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }
    
    static {
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(Vex.class, EntityDataSerializers.BYTE);
    }
    
    class VexMoveControl extends MoveControl {
        public VexMoveControl(final Vex beb2) {
            super(beb2);
        }
        
        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                return;
            }
            final Vec3 dck2 = new Vec3(this.wantedX - Vex.this.getX(), this.wantedY - Vex.this.getY(), this.wantedZ - Vex.this.getZ());
            final double double3 = dck2.length();
            if (double3 < Vex.this.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().scale(0.5));
            }
            else {
                Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().add(dck2.scale(this.speedModifier * 0.05 / double3)));
                if (Vex.this.getTarget() == null) {
                    final Vec3 dck3 = Vex.this.getDeltaMovement();
                    Vex.this.yRot = -(float)Mth.atan2(dck3.x, dck3.z) * 57.295776f;
                    Vex.this.yBodyRot = Vex.this.yRot;
                }
                else {
                    final double double4 = Vex.this.getTarget().getX() - Vex.this.getX();
                    final double double5 = Vex.this.getTarget().getZ() - Vex.this.getZ();
                    Vex.this.yRot = -(float)Mth.atan2(double4, double5) * 57.295776f;
                    Vex.this.yBodyRot = Vex.this.yRot;
                }
            }
        }
    }
    
    class VexChargeAttackGoal extends Goal {
        public VexChargeAttackGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return Vex.this.getTarget() != null && !Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(7) == 0 && Vex.this.distanceToSqr(Vex.this.getTarget()) > 4.0;
        }
        
        @Override
        public boolean canContinueToUse() {
            return Vex.this.getMoveControl().hasWanted() && Vex.this.isCharging() && Vex.this.getTarget() != null && Vex.this.getTarget().isAlive();
        }
        
        @Override
        public void start() {
            final LivingEntity aqj2 = Vex.this.getTarget();
            final Vec3 dck3 = aqj2.getEyePosition(1.0f);
            Vex.this.moveControl.setWantedPosition(dck3.x, dck3.y, dck3.z, 1.0);
            Vex.this.setIsCharging(true);
            Vex.this.playSound(SoundEvents.VEX_CHARGE, 1.0f, 1.0f);
        }
        
        @Override
        public void stop() {
            Vex.this.setIsCharging(false);
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = Vex.this.getTarget();
            if (Vex.this.getBoundingBox().intersects(aqj2.getBoundingBox())) {
                Vex.this.doHurtTarget(aqj2);
                Vex.this.setIsCharging(false);
            }
            else {
                final double double3 = Vex.this.distanceToSqr(aqj2);
                if (double3 < 9.0) {
                    final Vec3 dck5 = aqj2.getEyePosition(1.0f);
                    Vex.this.moveControl.setWantedPosition(dck5.x, dck5.y, dck5.z, 1.0);
                }
            }
        }
    }
    
    class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return !Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(7) == 0;
        }
        
        @Override
        public boolean canContinueToUse() {
            return false;
        }
        
        @Override
        public void tick() {
            BlockPos fx2 = Vex.this.getBoundOrigin();
            if (fx2 == null) {
                fx2 = Vex.this.blockPosition();
            }
            int integer3 = 0;
            while (integer3 < 3) {
                final BlockPos fx3 = fx2.offset(Vex.this.random.nextInt(15) - 7, Vex.this.random.nextInt(11) - 5, Vex.this.random.nextInt(15) - 7);
                if (Vex.this.level.isEmptyBlock(fx3)) {
                    Vex.this.moveControl.setWantedPosition(fx3.getX() + 0.5, fx3.getY() + 0.5, fx3.getZ() + 0.5, 0.25);
                    if (Vex.this.getTarget() == null) {
                        Vex.this.getLookControl().setLookAt(fx3.getX() + 0.5, fx3.getY() + 0.5, fx3.getZ() + 0.5, 180.0f, 20.0f);
                        break;
                    }
                    break;
                }
                else {
                    ++integer3;
                }
            }
        }
    }
    
    class VexCopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyOwnerTargeting;
        
        public VexCopyOwnerTargetGoal(final PathfinderMob aqr) {
            super(aqr, false);
            this.copyOwnerTargeting = new TargetingConditions().allowUnseeable().ignoreInvisibilityTesting();
        }
        
        @Override
        public boolean canUse() {
            return Vex.this.owner != null && Vex.this.owner.getTarget() != null && this.canAttack(Vex.this.owner.getTarget(), this.copyOwnerTargeting);
        }
        
        @Override
        public void start() {
            Vex.this.setTarget(Vex.this.owner.getTarget());
            super.start();
        }
    }
}
