package net.minecraft.world.entity.vehicle;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import java.util.List;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.WaterAnimal;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.BlockUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public class Boat extends Entity {
    private static final EntityDataAccessor<Integer> DATA_ID_HURT;
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR;
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE;
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE;
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT;
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT;
    private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME;
    private final float[] paddlePositions;
    private float invFriction;
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private double waterLevel;
    private float landFriction;
    private Status status;
    private Status oldStatus;
    private double lastYd;
    private boolean isAboveBubbleColumn;
    private boolean bubbleColumnDirectionIsDown;
    private float bubbleMultiplier;
    private float bubbleAngle;
    private float bubbleAngleO;
    
    public Boat(final EntityType<? extends Boat> aqb, final Level bru) {
        super(aqb, bru);
        this.paddlePositions = new float[2];
        this.blocksBuilding = true;
    }
    
    public Boat(final Level bru, final double double2, final double double3, final double double4) {
        this(EntityType.BOAT, bru);
        this.setPos(double2, double3, double4);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = double2;
        this.yo = double3;
        this.zo = double4;
    }
    
    @Override
    protected float getEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height;
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<Integer>define(Boat.DATA_ID_HURT, 0);
        this.entityData.<Integer>define(Boat.DATA_ID_HURTDIR, 1);
        this.entityData.<Float>define(Boat.DATA_ID_DAMAGE, 0.0f);
        this.entityData.<Integer>define(Boat.DATA_ID_TYPE, Type.OAK.ordinal());
        this.entityData.<Boolean>define(Boat.DATA_ID_PADDLE_LEFT, false);
        this.entityData.<Boolean>define(Boat.DATA_ID_PADDLE_RIGHT, false);
        this.entityData.<Integer>define(Boat.DATA_ID_BUBBLE_TIME, 0);
    }
    
    @Override
    public boolean canCollideWith(final Entity apx) {
        return canVehicleCollide(this, apx);
    }
    
    public static boolean canVehicleCollide(final Entity apx1, final Entity apx2) {
        return (apx2.canBeCollidedWith() || apx2.isPushable()) && !apx1.isPassengerOfSameVehicle(apx2);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean isPushable() {
        return true;
    }
    
    @Override
    protected Vec3 getRelativePortalPosition(final Direction.Axis a, final BlockUtil.FoundRectangle a) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(a, a));
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return -0.1;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (this.level.isClientSide || this.removed) {
            return true;
        }
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() + float2 * 10.0f);
        this.markHurt();
        final boolean boolean4 = aph.getEntity() instanceof Player && ((Player)aph.getEntity()).abilities.instabuild;
        if (boolean4 || this.getDamage() > 40.0f) {
            if (!boolean4 && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.spawnAtLocation(this.getDropItem());
            }
            this.remove();
        }
        return true;
    }
    
    @Override
    public void onAboveBubbleCol(final boolean boolean1) {
        if (!this.level.isClientSide) {
            this.isAboveBubbleColumn = true;
            this.bubbleColumnDirectionIsDown = boolean1;
            if (this.getBubbleTime() == 0) {
                this.setBubbleTime(60);
            }
        }
        this.level.addParticle(ParticleTypes.SPLASH, this.getX() + this.random.nextFloat(), this.getY() + 0.7, this.getZ() + this.random.nextFloat(), 0.0, 0.0, 0.0);
        if (this.random.nextInt(20) == 0) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), this.getSwimSplashSound(), this.getSoundSource(), 1.0f, 0.8f + 0.4f * this.random.nextFloat(), false);
        }
    }
    
    @Override
    public void push(final Entity apx) {
        if (apx instanceof Boat) {
            if (apx.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.push(apx);
            }
        }
        else if (apx.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.push(apx);
        }
    }
    
    public Item getDropItem() {
        switch (this.getBoatType()) {
            default: {
                return Items.OAK_BOAT;
            }
            case SPRUCE: {
                return Items.SPRUCE_BOAT;
            }
            case BIRCH: {
                return Items.BIRCH_BOAT;
            }
            case JUNGLE: {
                return Items.JUNGLE_BOAT;
            }
            case ACACIA: {
                return Items.ACACIA_BOAT;
            }
            case DARK_OAK: {
                return Items.DARK_OAK_BOAT;
            }
        }
    }
    
    @Override
    public void animateHurt() {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0f);
    }
    
    @Override
    public boolean isPickable() {
        return !this.removed;
    }
    
    @Override
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        this.lerpX = double1;
        this.lerpY = double2;
        this.lerpZ = double3;
        this.lerpYRot = float4;
        this.lerpXRot = float5;
        this.lerpSteps = 10;
    }
    
    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }
    
    @Override
    public void tick() {
        this.oldStatus = this.status;
        this.status = this.getStatus();
        if (this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER) {
            ++this.outOfControlTicks;
        }
        else {
            this.outOfControlTicks = 0.0f;
        }
        if (!this.level.isClientSide && this.outOfControlTicks >= 60.0f) {
            this.ejectPassengers();
        }
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }
        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f);
        }
        super.tick();
        this.tickLerp();
        if (this.isControlledByLocalInstance()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player)) {
                this.setPaddleState(false, false);
            }
            this.floatBoat();
            if (this.level.isClientSide) {
                this.controlBoat();
                this.level.sendPacketToServer(new ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        else {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.tickBubbleColumn();
        for (int integer2 = 0; integer2 <= 1; ++integer2) {
            if (this.getPaddleState(integer2)) {
                if (!this.isSilent() && this.paddlePositions[integer2] % 6.2831855f <= 0.7853981852531433 && (this.paddlePositions[integer2] + 0.39269909262657166) % 6.2831854820251465 >= 0.7853981852531433) {
                    final SoundEvent adn3 = this.getPaddleSound();
                    if (adn3 != null) {
                        final Vec3 dck4 = this.getViewVector(1.0f);
                        final double double5 = (integer2 == 1) ? (-dck4.z) : dck4.z;
                        final double double6 = (integer2 == 1) ? dck4.x : (-dck4.x);
                        this.level.playSound(null, this.getX() + double5, this.getY(), this.getZ() + double6, adn3, this.getSoundSource(), 1.0f, 0.8f + 0.4f * this.random.nextFloat());
                    }
                }
                final float[] paddlePositions = this.paddlePositions;
                final int n = integer2;
                paddlePositions[n] += 0.39269909262657166;
            }
            else {
                this.paddlePositions[integer2] = 0.0f;
            }
        }
        this.checkInsideBlocks();
        final List<Entity> list2 = this.level.getEntities(this, this.getBoundingBox().inflate(0.20000000298023224, -0.009999999776482582, 0.20000000298023224), EntitySelector.pushableBy(this));
        if (!list2.isEmpty()) {
            final boolean boolean3 = !this.level.isClientSide && !(this.getControllingPassenger() instanceof Player);
            for (int integer3 = 0; integer3 < list2.size(); ++integer3) {
                final Entity apx5 = (Entity)list2.get(integer3);
                if (!apx5.hasPassenger(this)) {
                    if (boolean3 && this.getPassengers().size() < 2 && !apx5.isPassenger() && apx5.getBbWidth() < this.getBbWidth() && apx5 instanceof LivingEntity && !(apx5 instanceof WaterAnimal) && !(apx5 instanceof Player)) {
                        apx5.startRiding(this);
                    }
                    else {
                        this.push(apx5);
                    }
                }
            }
        }
    }
    
    private void tickBubbleColumn() {
        if (this.level.isClientSide) {
            final int integer2 = this.getBubbleTime();
            if (integer2 > 0) {
                this.bubbleMultiplier += 0.05f;
            }
            else {
                this.bubbleMultiplier -= 0.1f;
            }
            this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0f, 1.0f);
            this.bubbleAngleO = this.bubbleAngle;
            this.bubbleAngle = 10.0f * (float)Math.sin((double)(0.5f * this.level.getGameTime())) * this.bubbleMultiplier;
        }
        else {
            if (!this.isAboveBubbleColumn) {
                this.setBubbleTime(0);
            }
            int integer2 = this.getBubbleTime();
            if (integer2 > 0) {
                --integer2;
                this.setBubbleTime(integer2);
                final int integer3 = 60 - integer2 - 1;
                if (integer3 > 0 && integer2 == 0) {
                    this.setBubbleTime(0);
                    final Vec3 dck4 = this.getDeltaMovement();
                    if (this.bubbleColumnDirectionIsDown) {
                        this.setDeltaMovement(dck4.add(0.0, -0.7, 0.0));
                        this.ejectPassengers();
                    }
                    else {
                        this.setDeltaMovement(dck4.x, this.hasPassenger(Player.class) ? 2.7 : 0.6, dck4.z);
                    }
                }
                this.isAboveBubbleColumn = false;
            }
        }
    }
    
    @Nullable
    protected SoundEvent getPaddleSound() {
        switch (this.getStatus()) {
            case IN_WATER:
            case UNDER_WATER:
            case UNDER_FLOWING_WATER: {
                return SoundEvents.BOAT_PADDLE_WATER;
            }
            case ON_LAND: {
                return SoundEvents.BOAT_PADDLE_LAND;
            }
            default: {
                return null;
            }
        }
    }
    
    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }
        if (this.lerpSteps <= 0) {
            return;
        }
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
    
    public void setPaddleState(final boolean boolean1, final boolean boolean2) {
        this.entityData.<Boolean>set(Boat.DATA_ID_PADDLE_LEFT, boolean1);
        this.entityData.<Boolean>set(Boat.DATA_ID_PADDLE_RIGHT, boolean2);
    }
    
    public float getRowingTime(final int integer, final float float2) {
        if (this.getPaddleState(integer)) {
            return (float)Mth.clampedLerp(this.paddlePositions[integer] - 0.39269909262657166, this.paddlePositions[integer], float2);
        }
        return 0.0f;
    }
    
    private Status getStatus() {
        final Status a2 = this.isUnderwater();
        if (a2 != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return a2;
        }
        if (this.checkInWater()) {
            return Status.IN_WATER;
        }
        final float float3 = this.getGroundFriction();
        if (float3 > 0.0f) {
            this.landFriction = float3;
            return Status.ON_LAND;
        }
        return Status.IN_AIR;
    }
    
    public float getWaterLevelAbove() {
        final AABB dcf2 = this.getBoundingBox();
        final int integer3 = Mth.floor(dcf2.minX);
        final int integer4 = Mth.ceil(dcf2.maxX);
        final int integer5 = Mth.floor(dcf2.maxY);
        final int integer6 = Mth.ceil(dcf2.maxY - this.lastYd);
        final int integer7 = Mth.floor(dcf2.minZ);
        final int integer8 = Mth.ceil(dcf2.maxZ);
        final BlockPos.MutableBlockPos a9 = new BlockPos.MutableBlockPos();
        int integer9 = integer5;
    Label_0196_Outer:
        while (integer9 < integer6) {
            float float11 = 0.0f;
            int integer10 = integer3;
        Label_0196:
            while (true) {
                while (integer10 < integer4) {
                    for (int integer11 = integer7; integer11 < integer8; ++integer11) {
                        a9.set(integer10, integer9, integer11);
                        final FluidState cuu14 = this.level.getFluidState(a9);
                        if (cuu14.is(FluidTags.WATER)) {
                            float11 = Math.max(float11, cuu14.getHeight(this.level, a9));
                        }
                        if (float11 >= 1.0f) {
                            break Label_0196;
                        }
                    }
                    ++integer10;
                    continue Label_0196_Outer;
                    ++integer9;
                    continue Label_0196_Outer;
                }
                if (float11 < 1.0f) {
                    return a9.getY() + float11;
                }
                continue Label_0196;
            }
        }
        return (float)(integer6 + 1);
    }
    
    public float getGroundFriction() {
        final AABB dcf2 = this.getBoundingBox();
        final AABB dcf3 = new AABB(dcf2.minX, dcf2.minY - 0.001, dcf2.minZ, dcf2.maxX, dcf2.minY, dcf2.maxZ);
        final int integer4 = Mth.floor(dcf3.minX) - 1;
        final int integer5 = Mth.ceil(dcf3.maxX) + 1;
        final int integer6 = Mth.floor(dcf3.minY) - 1;
        final int integer7 = Mth.ceil(dcf3.maxY) + 1;
        final int integer8 = Mth.floor(dcf3.minZ) - 1;
        final int integer9 = Mth.ceil(dcf3.maxZ) + 1;
        final VoxelShape dde10 = Shapes.create(dcf3);
        float float11 = 0.0f;
        int integer10 = 0;
        final BlockPos.MutableBlockPos a13 = new BlockPos.MutableBlockPos();
        for (int integer11 = integer4; integer11 < integer5; ++integer11) {
            for (int integer12 = integer8; integer12 < integer9; ++integer12) {
                final int integer13 = ((integer11 == integer4 || integer11 == integer5 - 1) + (integer12 == integer8 || integer12 == integer9 - 1)) ? 1 : 0;
                if (integer13 != 2) {
                    for (int integer14 = integer6; integer14 < integer7; ++integer14) {
                        if (integer13 > 0) {
                            if (integer14 == integer6) {
                                continue;
                            }
                            if (integer14 == integer7 - 1) {
                                continue;
                            }
                        }
                        a13.set(integer11, integer14, integer12);
                        final BlockState cee18 = this.level.getBlockState(a13);
                        if (!(cee18.getBlock() instanceof WaterlilyBlock)) {
                            if (Shapes.joinIsNotEmpty(cee18.getCollisionShape(this.level, a13).move(integer11, integer14, integer12), dde10, BooleanOp.AND)) {
                                float11 += cee18.getBlock().getFriction();
                                ++integer10;
                            }
                        }
                    }
                }
            }
        }
        return float11 / integer10;
    }
    
    private boolean checkInWater() {
        final AABB dcf2 = this.getBoundingBox();
        final int integer3 = Mth.floor(dcf2.minX);
        final int integer4 = Mth.ceil(dcf2.maxX);
        final int integer5 = Mth.floor(dcf2.minY);
        final int integer6 = Mth.ceil(dcf2.minY + 0.001);
        final int integer7 = Mth.floor(dcf2.minZ);
        final int integer8 = Mth.ceil(dcf2.maxZ);
        boolean boolean9 = false;
        this.waterLevel = Double.MIN_VALUE;
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        for (int integer9 = integer3; integer9 < integer4; ++integer9) {
            for (int integer10 = integer5; integer10 < integer6; ++integer10) {
                for (int integer11 = integer7; integer11 < integer8; ++integer11) {
                    a10.set(integer9, integer10, integer11);
                    final FluidState cuu14 = this.level.getFluidState(a10);
                    if (cuu14.is(FluidTags.WATER)) {
                        final float float15 = integer10 + cuu14.getHeight(this.level, a10);
                        this.waterLevel = Math.max((double)float15, this.waterLevel);
                        boolean9 |= (dcf2.minY < float15);
                    }
                }
            }
        }
        return boolean9;
    }
    
    @Nullable
    private Status isUnderwater() {
        final AABB dcf2 = this.getBoundingBox();
        final double double3 = dcf2.maxY + 0.001;
        final int integer5 = Mth.floor(dcf2.minX);
        final int integer6 = Mth.ceil(dcf2.maxX);
        final int integer7 = Mth.floor(dcf2.maxY);
        final int integer8 = Mth.ceil(double3);
        final int integer9 = Mth.floor(dcf2.minZ);
        final int integer10 = Mth.ceil(dcf2.maxZ);
        boolean boolean11 = false;
        final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
        for (int integer11 = integer5; integer11 < integer6; ++integer11) {
            for (int integer12 = integer7; integer12 < integer8; ++integer12) {
                for (int integer13 = integer9; integer13 < integer10; ++integer13) {
                    a12.set(integer11, integer12, integer13);
                    final FluidState cuu16 = this.level.getFluidState(a12);
                    if (cuu16.is(FluidTags.WATER) && double3 < a12.getY() + cuu16.getHeight(this.level, a12)) {
                        if (!cuu16.isSource()) {
                            return Status.UNDER_FLOWING_WATER;
                        }
                        boolean11 = true;
                    }
                }
            }
        }
        return boolean11 ? Status.UNDER_WATER : null;
    }
    
    private void floatBoat() {
        final double double2 = -0.03999999910593033;
        double double3 = this.isNoGravity() ? 0.0 : -0.03999999910593033;
        double double4 = 0.0;
        this.invFriction = 0.05f;
        if (this.oldStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
            this.waterLevel = this.getY(1.0);
            this.setPos(this.getX(), this.getWaterLevelAbove() - this.getBbHeight() + 0.101, this.getZ());
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            this.lastYd = 0.0;
            this.status = Status.IN_WATER;
        }
        else {
            if (this.status == Status.IN_WATER) {
                double4 = (this.waterLevel - this.getY()) / this.getBbHeight();
                this.invFriction = 0.9f;
            }
            else if (this.status == Status.UNDER_FLOWING_WATER) {
                double3 = -7.0E-4;
                this.invFriction = 0.9f;
            }
            else if (this.status == Status.UNDER_WATER) {
                double4 = 0.009999999776482582;
                this.invFriction = 0.45f;
            }
            else if (this.status == Status.IN_AIR) {
                this.invFriction = 0.9f;
            }
            else if (this.status == Status.ON_LAND) {
                this.invFriction = this.landFriction;
                if (this.getControllingPassenger() instanceof Player) {
                    this.landFriction /= 2.0f;
                }
            }
            final Vec3 dck8 = this.getDeltaMovement();
            this.setDeltaMovement(dck8.x * this.invFriction, dck8.y + double3, dck8.z * this.invFriction);
            this.deltaRotation *= this.invFriction;
            if (double4 > 0.0) {
                final Vec3 dck9 = this.getDeltaMovement();
                this.setDeltaMovement(dck9.x, (dck9.y + double4 * 0.06153846016296973) * 0.75, dck9.z);
            }
        }
    }
    
    private void controlBoat() {
        if (!this.isVehicle()) {
            return;
        }
        float float2 = 0.0f;
        if (this.inputLeft) {
            --this.deltaRotation;
        }
        if (this.inputRight) {
            ++this.deltaRotation;
        }
        if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
            float2 += 0.005f;
        }
        this.yRot += this.deltaRotation;
        if (this.inputUp) {
            float2 += 0.04f;
        }
        if (this.inputDown) {
            float2 -= 0.005f;
        }
        this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.yRot * 0.017453292f) * float2, 0.0, Mth.cos(this.yRot * 0.017453292f) * float2));
        this.setPaddleState((this.inputRight && !this.inputLeft) || this.inputUp, (this.inputLeft && !this.inputRight) || this.inputUp);
    }
    
    @Override
    public void positionRider(final Entity apx) {
        if (!this.hasPassenger(apx)) {
            return;
        }
        float float3 = 0.0f;
        final float float4 = (float)((this.removed ? 0.009999999776482582 : this.getPassengersRidingOffset()) + apx.getMyRidingOffset());
        if (this.getPassengers().size() > 1) {
            final int integer5 = this.getPassengers().indexOf(apx);
            if (integer5 == 0) {
                float3 = 0.2f;
            }
            else {
                float3 = -0.6f;
            }
            if (apx instanceof Animal) {
                float3 += (float)0.2;
            }
        }
        final Vec3 dck5 = new Vec3(float3, 0.0, 0.0).yRot(-this.yRot * 0.017453292f - 1.5707964f);
        apx.setPos(this.getX() + dck5.x, this.getY() + float4, this.getZ() + dck5.z);
        apx.yRot += this.deltaRotation;
        apx.setYHeadRot(apx.getYHeadRot() + this.deltaRotation);
        this.clampRotation(apx);
        if (apx instanceof Animal && this.getPassengers().size() > 1) {
            final int integer6 = (apx.getId() % 2 == 0) ? 90 : 270;
            apx.setYBodyRot(((Animal)apx).yBodyRot + integer6);
            apx.setYHeadRot(apx.getYHeadRot() + integer6);
        }
    }
    
    @Override
    public Vec3 getDismountLocationForPassenger(final LivingEntity aqj) {
        final Vec3 dck3 = Entity.getCollisionHorizontalEscapeVector(this.getBbWidth() * Mth.SQRT_OF_TWO, aqj.getBbWidth(), this.yRot);
        final double double4 = this.getX() + dck3.x;
        final double double5 = this.getZ() + dck3.z;
        final BlockPos fx8 = new BlockPos(double4, this.getBoundingBox().maxY, double5);
        final BlockPos fx9 = fx8.below();
        if (!this.level.isWaterAt(fx9)) {
            final double double6 = fx8.getY() + this.level.getBlockFloorHeight(fx8);
            final double double7 = fx8.getY() + this.level.getBlockFloorHeight(fx9);
            for (final Pose aqu15 : aqj.getDismountPoses()) {
                final Vec3 dck4 = DismountHelper.findDismountLocation(this.level, double4, double6, double5, aqj, aqu15);
                if (dck4 != null) {
                    aqj.setPose(aqu15);
                    return dck4;
                }
                final Vec3 dck5 = DismountHelper.findDismountLocation(this.level, double4, double7, double5, aqj, aqu15);
                if (dck5 != null) {
                    aqj.setPose(aqu15);
                    return dck5;
                }
            }
        }
        return super.getDismountLocationForPassenger(aqj);
    }
    
    protected void clampRotation(final Entity apx) {
        apx.setYBodyRot(this.yRot);
        final float float3 = Mth.wrapDegrees(apx.yRot - this.yRot);
        final float float4 = Mth.clamp(float3, -105.0f, 105.0f);
        apx.yRotO += float4 - float3;
        apx.setYHeadRot(apx.yRot += float4 - float3);
    }
    
    @Override
    public void onPassengerTurned(final Entity apx) {
        this.clampRotation(apx);
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        md.putString("Type", this.getBoatType().getName());
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        if (md.contains("Type", 8)) {
            this.setType(Type.byName(md.getString("Type")));
        }
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        if (bft.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (this.outOfControlTicks >= 60.0f) {
            return InteractionResult.PASS;
        }
        if (!this.level.isClientSide) {
            return bft.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
        this.lastYd = this.getDeltaMovement().y;
        if (this.isPassenger()) {
            return;
        }
        if (boolean2) {
            if (this.fallDistance > 3.0f) {
                if (this.status != Status.ON_LAND) {
                    this.fallDistance = 0.0f;
                    return;
                }
                this.causeFallDamage(this.fallDistance, 1.0f);
                if (!this.level.isClientSide && !this.removed) {
                    this.remove();
                    if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                        for (int integer7 = 0; integer7 < 3; ++integer7) {
                            this.spawnAtLocation(this.getBoatType().getPlanks());
                        }
                        for (int integer7 = 0; integer7 < 2; ++integer7) {
                            this.spawnAtLocation(Items.STICK);
                        }
                    }
                }
            }
            this.fallDistance = 0.0f;
        }
        else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && double1 < 0.0) {
            this.fallDistance -= (float)double1;
        }
    }
    
    public boolean getPaddleState(final int integer) {
        return this.entityData.<Boolean>get((integer == 0) ? Boat.DATA_ID_PADDLE_LEFT : Boat.DATA_ID_PADDLE_RIGHT) && this.getControllingPassenger() != null;
    }
    
    public void setDamage(final float float1) {
        this.entityData.<Float>set(Boat.DATA_ID_DAMAGE, float1);
    }
    
    public float getDamage() {
        return this.entityData.<Float>get(Boat.DATA_ID_DAMAGE);
    }
    
    public void setHurtTime(final int integer) {
        this.entityData.<Integer>set(Boat.DATA_ID_HURT, integer);
    }
    
    public int getHurtTime() {
        return this.entityData.<Integer>get(Boat.DATA_ID_HURT);
    }
    
    private void setBubbleTime(final int integer) {
        this.entityData.<Integer>set(Boat.DATA_ID_BUBBLE_TIME, integer);
    }
    
    private int getBubbleTime() {
        return this.entityData.<Integer>get(Boat.DATA_ID_BUBBLE_TIME);
    }
    
    public float getBubbleAngle(final float float1) {
        return Mth.lerp(float1, this.bubbleAngleO, this.bubbleAngle);
    }
    
    public void setHurtDir(final int integer) {
        this.entityData.<Integer>set(Boat.DATA_ID_HURTDIR, integer);
    }
    
    public int getHurtDir() {
        return this.entityData.<Integer>get(Boat.DATA_ID_HURTDIR);
    }
    
    public void setType(final Type b) {
        this.entityData.<Integer>set(Boat.DATA_ID_TYPE, b.ordinal());
    }
    
    public Type getBoatType() {
        return Type.byId(this.entityData.<Integer>get(Boat.DATA_ID_TYPE));
    }
    
    @Override
    protected boolean canAddPassenger(final Entity apx) {
        return this.getPassengers().size() < 2 && !this.isEyeInFluid(FluidTags.WATER);
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        final List<Entity> list2 = this.getPassengers();
        return list2.isEmpty() ? null : ((Entity)list2.get(0));
    }
    
    public void setInput(final boolean boolean1, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        this.inputLeft = boolean1;
        this.inputRight = boolean2;
        this.inputUp = boolean3;
        this.inputDown = boolean4;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    @Override
    public boolean isUnderWater() {
        return this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER;
    }
    
    static {
        DATA_ID_HURT = SynchedEntityData.<Integer>defineId(Boat.class, EntityDataSerializers.INT);
        DATA_ID_HURTDIR = SynchedEntityData.<Integer>defineId(Boat.class, EntityDataSerializers.INT);
        DATA_ID_DAMAGE = SynchedEntityData.<Float>defineId(Boat.class, EntityDataSerializers.FLOAT);
        DATA_ID_TYPE = SynchedEntityData.<Integer>defineId(Boat.class, EntityDataSerializers.INT);
        DATA_ID_PADDLE_LEFT = SynchedEntityData.<Boolean>defineId(Boat.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_PADDLE_RIGHT = SynchedEntityData.<Boolean>defineId(Boat.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_BUBBLE_TIME = SynchedEntityData.<Integer>defineId(Boat.class, EntityDataSerializers.INT);
    }
    
    public enum Status {
        IN_WATER, 
        UNDER_WATER, 
        UNDER_FLOWING_WATER, 
        ON_LAND, 
        IN_AIR;
    }
    
    public enum Type {
        OAK(Blocks.OAK_PLANKS, "oak"), 
        SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"), 
        BIRCH(Blocks.BIRCH_PLANKS, "birch"), 
        JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"), 
        ACACIA(Blocks.ACACIA_PLANKS, "acacia"), 
        DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak");
        
        private final String name;
        private final Block planks;
        
        private Type(final Block bul, final String string4) {
            this.name = string4;
            this.planks = bul;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Block getPlanks() {
            return this.planks;
        }
        
        public String toString() {
            return this.name;
        }
        
        public static Type byId(int integer) {
            final Type[] arr2 = values();
            if (integer < 0 || integer >= arr2.length) {
                integer = 0;
            }
            return arr2[integer];
        }
        
        public static Type byName(final String string) {
            final Type[] arr2 = values();
            for (int integer3 = 0; integer3 < arr2.length; ++integer3) {
                if (arr2[integer3].getName().equals(string)) {
                    return arr2[integer3];
                }
            }
            return arr2[0];
        }
    }
}
