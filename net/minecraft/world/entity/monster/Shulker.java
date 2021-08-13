package net.minecraft.world.entity.monster;

import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.Difficulty;
import java.util.EnumSet;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.ShulkerSharedHelper;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.world.entity.animal.AbstractGolem;

public class Shulker extends AbstractGolem implements Enemy {
    private static final UUID COVERED_ARMOR_MODIFIER_UUID;
    private static final AttributeModifier COVERED_ARMOR_MODIFIER;
    protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID;
    protected static final EntityDataAccessor<Optional<BlockPos>> DATA_ATTACH_POS_ID;
    protected static final EntityDataAccessor<Byte> DATA_PEEK_ID;
    protected static final EntityDataAccessor<Byte> DATA_COLOR_ID;
    private float currentPeekAmountO;
    private float currentPeekAmount;
    private BlockPos oldAttachPosition;
    private int clientSideTeleportInterpolation;
    
    public Shulker(final EntityType<? extends Shulker> aqb, final Level bru) {
        super(aqb, bru);
        this.oldAttachPosition = null;
        this.xpReward = 5;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new ShulkerAttackGoal());
        this.goalSelector.addGoal(7, new ShulkerPeekGoal());
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new ShulkerNearestAttackGoal(this));
        this.targetSelector.addGoal(3, new ShulkerDefenseAttackGoal(this));
    }
    
    protected boolean isMovementNoisy() {
        return false;
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHULKER_AMBIENT;
    }
    
    public void playAmbientSound() {
        if (!this.isClosed()) {
            super.playAmbientSound();
        }
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHULKER_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.isClosed()) {
            return SoundEvents.SHULKER_HURT_CLOSED;
        }
        return SoundEvents.SHULKER_HURT;
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Direction>define(Shulker.DATA_ATTACH_FACE_ID, Direction.DOWN);
        this.entityData.<Optional<BlockPos>>define(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.empty());
        this.entityData.<Byte>define(Shulker.DATA_PEEK_ID, (Byte)0);
        this.entityData.<Byte>define(Shulker.DATA_COLOR_ID, (Byte)16);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0);
    }
    
    protected BodyRotationControl createBodyControl() {
        return new ShulkerBodyRotationControl(this);
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.entityData.<Direction>set(Shulker.DATA_ATTACH_FACE_ID, Direction.from3DDataValue(md.getByte("AttachFace")));
        this.entityData.<Byte>set(Shulker.DATA_PEEK_ID, md.getByte("Peek"));
        this.entityData.<Byte>set(Shulker.DATA_COLOR_ID, md.getByte("Color"));
        if (md.contains("APX")) {
            final int integer3 = md.getInt("APX");
            final int integer4 = md.getInt("APY");
            final int integer5 = md.getInt("APZ");
            this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.of(new BlockPos(integer3, integer4, integer5)));
        }
        else {
            this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.empty());
        }
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putByte("AttachFace", (byte)this.entityData.<Direction>get(Shulker.DATA_ATTACH_FACE_ID).get3DDataValue());
        md.putByte("Peek", (byte)this.entityData.<Byte>get(Shulker.DATA_PEEK_ID));
        md.putByte("Color", (byte)this.entityData.<Byte>get(Shulker.DATA_COLOR_ID));
        final BlockPos fx3 = this.getAttachPosition();
        if (fx3 != null) {
            md.putInt("APX", fx3.getX());
            md.putInt("APY", fx3.getY());
            md.putInt("APZ", fx3.getZ());
        }
    }
    
    public void tick() {
        super.tick();
        BlockPos fx2 = (BlockPos)this.entityData.<Optional<BlockPos>>get(Shulker.DATA_ATTACH_POS_ID).orElse(null);
        if (fx2 == null && !this.level.isClientSide) {
            fx2 = this.blockPosition();
            this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.of(fx2));
        }
        if (this.isPassenger()) {
            fx2 = null;
            final float float3 = this.getVehicle().yRot;
            this.yRot = float3;
            this.yBodyRot = float3;
            this.yBodyRotO = float3;
            this.clientSideTeleportInterpolation = 0;
        }
        else if (!this.level.isClientSide) {
            final BlockState cee3 = this.level.getBlockState(fx2);
            if (!cee3.isAir()) {
                if (cee3.is(Blocks.MOVING_PISTON)) {
                    final Direction gc4 = cee3.<Direction>getValue((Property<Direction>)PistonBaseBlock.FACING);
                    if (this.level.isEmptyBlock(fx2.relative(gc4))) {
                        fx2 = fx2.relative(gc4);
                        this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.of(fx2));
                    }
                    else {
                        this.teleportSomewhere();
                    }
                }
                else if (cee3.is(Blocks.PISTON_HEAD)) {
                    final Direction gc4 = cee3.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING);
                    if (this.level.isEmptyBlock(fx2.relative(gc4))) {
                        fx2 = fx2.relative(gc4);
                        this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.of(fx2));
                    }
                    else {
                        this.teleportSomewhere();
                    }
                }
                else {
                    this.teleportSomewhere();
                }
            }
            final Direction gc4 = this.getAttachFace();
            if (!this.canAttachOnBlockFace(fx2, gc4)) {
                final Direction gc5 = this.findAttachableFace(fx2);
                if (gc5 != null) {
                    this.entityData.<Direction>set(Shulker.DATA_ATTACH_FACE_ID, gc5);
                }
                else {
                    this.teleportSomewhere();
                }
            }
        }
        final float float3 = this.getRawPeekAmount() * 0.01f;
        this.currentPeekAmountO = this.currentPeekAmount;
        if (this.currentPeekAmount > float3) {
            this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05f, float3, 1.0f);
        }
        else if (this.currentPeekAmount < float3) {
            this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05f, 0.0f, float3);
        }
        if (fx2 != null) {
            if (this.level.isClientSide) {
                if (this.clientSideTeleportInterpolation > 0 && this.oldAttachPosition != null) {
                    --this.clientSideTeleportInterpolation;
                }
                else {
                    this.oldAttachPosition = fx2;
                }
            }
            this.setPosAndOldPos(fx2.getX() + 0.5, fx2.getY(), fx2.getZ() + 0.5);
            final double double4 = 0.5 - Mth.sin((0.5f + this.currentPeekAmount) * 3.1415927f) * 0.5;
            final double double5 = 0.5 - Mth.sin((0.5f + this.currentPeekAmountO) * 3.1415927f) * 0.5;
            final Direction gc6 = this.getAttachFace().getOpposite();
            this.setBoundingBox(new AABB(this.getX() - 0.5, this.getY(), this.getZ() - 0.5, this.getX() + 0.5, this.getY() + 1.0, this.getZ() + 0.5).expandTowards(gc6.getStepX() * double4, gc6.getStepY() * double4, gc6.getStepZ() * double4));
            final double double6 = double4 - double5;
            if (double6 > 0.0) {
                final List<Entity> list11 = this.level.getEntities(this, this.getBoundingBox());
                if (!list11.isEmpty()) {
                    for (final Entity apx13 : list11) {
                        if (!(apx13 instanceof Shulker) && !apx13.noPhysics) {
                            apx13.move(MoverType.SHULKER, new Vec3(double6 * gc6.getStepX(), double6 * gc6.getStepY(), double6 * gc6.getStepZ()));
                        }
                    }
                }
            }
        }
    }
    
    public void move(final MoverType aqo, final Vec3 dck) {
        if (aqo == MoverType.SHULKER_BOX) {
            this.teleportSomewhere();
        }
        else {
            super.move(aqo, dck);
        }
    }
    
    public void setPos(final double double1, final double double2, final double double3) {
        super.setPos(double1, double2, double3);
        if (this.entityData == null || this.tickCount == 0) {
            return;
        }
        final Optional<BlockPos> optional8 = this.entityData.<Optional<BlockPos>>get(Shulker.DATA_ATTACH_POS_ID);
        final Optional<BlockPos> optional9 = (Optional<BlockPos>)Optional.of(new BlockPos(double1, double2, double3));
        if (!optional9.equals(optional8)) {
            this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, optional9);
            this.entityData.<Byte>set(Shulker.DATA_PEEK_ID, (Byte)0);
            this.hasImpulse = true;
        }
    }
    
    @Nullable
    protected Direction findAttachableFace(final BlockPos fx) {
        for (final Direction gc6 : Direction.values()) {
            if (this.canAttachOnBlockFace(fx, gc6)) {
                return gc6;
            }
        }
        return null;
    }
    
    private boolean canAttachOnBlockFace(final BlockPos fx, final Direction gc) {
        return this.level.loadedAndEntityCanStandOnFace(fx.relative(gc), this, gc.getOpposite()) && this.level.noCollision(this, ShulkerSharedHelper.openBoundingBox(fx, gc.getOpposite()));
    }
    
    protected boolean teleportSomewhere() {
        if (this.isNoAi() || !this.isAlive()) {
            return true;
        }
        final BlockPos fx2 = this.blockPosition();
        for (int integer3 = 0; integer3 < 5; ++integer3) {
            final BlockPos fx3 = fx2.offset(8 - this.random.nextInt(17), 8 - this.random.nextInt(17), 8 - this.random.nextInt(17));
            if (fx3.getY() > 0 && this.level.isEmptyBlock(fx3) && this.level.getWorldBorder().isWithinBounds(fx3) && this.level.noCollision(this, new AABB(fx3))) {
                final Direction gc5 = this.findAttachableFace(fx3);
                if (gc5 != null) {
                    this.entityData.<Direction>set(Shulker.DATA_ATTACH_FACE_ID, gc5);
                    this.playSound(SoundEvents.SHULKER_TELEPORT, 1.0f, 1.0f);
                    this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.of(fx3));
                    this.entityData.<Byte>set(Shulker.DATA_PEEK_ID, (Byte)0);
                    this.setTarget(null);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void aiStep() {
        super.aiStep();
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.isNoAi()) {
            this.yBodyRotO = 0.0f;
            this.yBodyRot = 0.0f;
        }
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Shulker.DATA_ATTACH_POS_ID.equals(us) && this.level.isClientSide && !this.isPassenger()) {
            final BlockPos fx3 = this.getAttachPosition();
            if (fx3 != null) {
                if (this.oldAttachPosition == null) {
                    this.oldAttachPosition = fx3;
                }
                else {
                    this.clientSideTeleportInterpolation = 6;
                }
                this.setPosAndOldPos(fx3.getX() + 0.5, fx3.getY(), fx3.getZ() + 0.5);
            }
        }
        super.onSyncedDataUpdated(us);
    }
    
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        this.lerpSteps = 0;
    }
    
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isClosed()) {
            final Entity apx4 = aph.getDirectEntity();
            if (apx4 instanceof AbstractArrow) {
                return false;
            }
        }
        if (super.hurt(aph, float2)) {
            if (this.getHealth() < this.getMaxHealth() * 0.5 && this.random.nextInt(4) == 0) {
                this.teleportSomewhere();
            }
            return true;
        }
        return false;
    }
    
    private boolean isClosed() {
        return this.getRawPeekAmount() == 0;
    }
    
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }
    
    public Direction getAttachFace() {
        return this.entityData.<Direction>get(Shulker.DATA_ATTACH_FACE_ID);
    }
    
    @Nullable
    public BlockPos getAttachPosition() {
        return (BlockPos)this.entityData.<Optional<BlockPos>>get(Shulker.DATA_ATTACH_POS_ID).orElse(null);
    }
    
    public void setAttachPosition(@Nullable final BlockPos fx) {
        this.entityData.<Optional<BlockPos>>set(Shulker.DATA_ATTACH_POS_ID, (Optional<BlockPos>)Optional.ofNullable(fx));
    }
    
    public int getRawPeekAmount() {
        return this.entityData.<Byte>get(Shulker.DATA_PEEK_ID);
    }
    
    public void setRawPeekAmount(final int integer) {
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(Shulker.COVERED_ARMOR_MODIFIER);
            if (integer == 0) {
                this.getAttribute(Attributes.ARMOR).addPermanentModifier(Shulker.COVERED_ARMOR_MODIFIER);
                this.playSound(SoundEvents.SHULKER_CLOSE, 1.0f, 1.0f);
            }
            else {
                this.playSound(SoundEvents.SHULKER_OPEN, 1.0f, 1.0f);
            }
        }
        this.entityData.<Byte>set(Shulker.DATA_PEEK_ID, (byte)integer);
    }
    
    public float getClientPeekAmount(final float float1) {
        return Mth.lerp(float1, this.currentPeekAmountO, this.currentPeekAmount);
    }
    
    public int getClientSideTeleportInterpolation() {
        return this.clientSideTeleportInterpolation;
    }
    
    public BlockPos getOldAttachPosition() {
        return this.oldAttachPosition;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.5f;
    }
    
    public int getMaxHeadXRot() {
        return 180;
    }
    
    public int getMaxHeadYRot() {
        return 180;
    }
    
    public void push(final Entity apx) {
    }
    
    public float getPickRadius() {
        return 0.0f;
    }
    
    public boolean hasValidInterpolationPositions() {
        return this.oldAttachPosition != null && this.getAttachPosition() != null;
    }
    
    @Nullable
    public DyeColor getColor() {
        final Byte byte2 = this.entityData.<Byte>get(Shulker.DATA_COLOR_ID);
        if (byte2 == 16 || byte2 > 15) {
            return null;
        }
        return DyeColor.byId(byte2);
    }
    
    static {
        COVERED_ARMOR_MODIFIER_UUID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
        COVERED_ARMOR_MODIFIER = new AttributeModifier(Shulker.COVERED_ARMOR_MODIFIER_UUID, "Covered armor bonus", 20.0, AttributeModifier.Operation.ADDITION);
        DATA_ATTACH_FACE_ID = SynchedEntityData.<Direction>defineId(Shulker.class, EntityDataSerializers.DIRECTION);
        DATA_ATTACH_POS_ID = SynchedEntityData.<Optional<BlockPos>>defineId(Shulker.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        DATA_PEEK_ID = SynchedEntityData.<Byte>defineId(Shulker.class, EntityDataSerializers.BYTE);
        DATA_COLOR_ID = SynchedEntityData.<Byte>defineId(Shulker.class, EntityDataSerializers.BYTE);
    }
    
    class ShulkerBodyRotationControl extends BodyRotationControl {
        public ShulkerBodyRotationControl(final Mob aqk) {
            super(aqk);
        }
        
        @Override
        public void clientTick() {
        }
    }
    
    class ShulkerPeekGoal extends Goal {
        private int peekTime;
        
        private ShulkerPeekGoal() {
        }
        
        @Override
        public boolean canUse() {
            return Shulker.this.getTarget() == null && Shulker.this.random.nextInt(40) == 0;
        }
        
        @Override
        public boolean canContinueToUse() {
            return Shulker.this.getTarget() == null && this.peekTime > 0;
        }
        
        @Override
        public void start() {
            this.peekTime = 20 * (1 + Shulker.this.random.nextInt(3));
            Shulker.this.setRawPeekAmount(30);
        }
        
        @Override
        public void stop() {
            if (Shulker.this.getTarget() == null) {
                Shulker.this.setRawPeekAmount(0);
            }
        }
        
        @Override
        public void tick() {
            --this.peekTime;
        }
    }
    
    class ShulkerAttackGoal extends Goal {
        private int attackTime;
        
        public ShulkerAttackGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = Shulker.this.getTarget();
            return aqj2 != null && aqj2.isAlive() && Shulker.this.level.getDifficulty() != Difficulty.PEACEFUL;
        }
        
        @Override
        public void start() {
            this.attackTime = 20;
            Shulker.this.setRawPeekAmount(100);
        }
        
        @Override
        public void stop() {
            Shulker.this.setRawPeekAmount(0);
        }
        
        @Override
        public void tick() {
            if (Shulker.this.level.getDifficulty() == Difficulty.PEACEFUL) {
                return;
            }
            --this.attackTime;
            final LivingEntity aqj2 = Shulker.this.getTarget();
            Shulker.this.getLookControl().setLookAt(aqj2, 180.0f, 180.0f);
            final double double3 = Shulker.this.distanceToSqr(aqj2);
            if (double3 < 400.0) {
                if (this.attackTime <= 0) {
                    this.attackTime = 20 + Shulker.this.random.nextInt(10) * 20 / 2;
                    Shulker.this.level.addFreshEntity(new ShulkerBullet(Shulker.this.level, Shulker.this, aqj2, Shulker.this.getAttachFace().getAxis()));
                    Shulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0f, (Shulker.this.random.nextFloat() - Shulker.this.random.nextFloat()) * 0.2f + 1.0f);
                }
            }
            else {
                Shulker.this.setTarget(null);
            }
            super.tick();
        }
    }
    
    class ShulkerNearestAttackGoal extends NearestAttackableTargetGoal<Player> {
        public ShulkerNearestAttackGoal(final Shulker bdt2) {
            super(bdt2, Player.class, true);
        }
        
        @Override
        public boolean canUse() {
            return Shulker.this.level.getDifficulty() != Difficulty.PEACEFUL && super.canUse();
        }
        
        @Override
        protected AABB getTargetSearchArea(final double double1) {
            final Direction gc4 = ((Shulker)this.mob).getAttachFace();
            if (gc4.getAxis() == Direction.Axis.X) {
                return this.mob.getBoundingBox().inflate(4.0, double1, double1);
            }
            if (gc4.getAxis() == Direction.Axis.Z) {
                return this.mob.getBoundingBox().inflate(double1, double1, 4.0);
            }
            return this.mob.getBoundingBox().inflate(double1, 4.0, double1);
        }
    }
    
    static class ShulkerDefenseAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public ShulkerDefenseAttackGoal(final Shulker bdt) {
            super(bdt, LivingEntity.class, 10, true, false, (Predicate<LivingEntity>)(aqj -> aqj instanceof Enemy));
        }
        
        @Override
        public boolean canUse() {
            return this.mob.getTeam() != null && super.canUse();
        }
        
        @Override
        protected AABB getTargetSearchArea(final double double1) {
            final Direction gc4 = ((Shulker)this.mob).getAttachFace();
            if (gc4.getAxis() == Direction.Axis.X) {
                return this.mob.getBoundingBox().inflate(4.0, double1, double1);
            }
            if (gc4.getAxis() == Direction.Axis.Z) {
                return this.mob.getBoundingBox().inflate(double1, double1, 4.0);
            }
            return this.mob.getBoundingBox().inflate(double1, 4.0, double1);
        }
    }
}
