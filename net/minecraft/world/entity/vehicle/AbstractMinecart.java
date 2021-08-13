package net.minecraft.world.entity.vehicle;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.EnumMap;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.MoverType;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.animal.IronGolem;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.function.Supplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.BlockUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.Vec3i;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.properties.RailShape;
import java.util.Map;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Pose;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public abstract class AbstractMinecart extends Entity {
    private static final EntityDataAccessor<Integer> DATA_ID_HURT;
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR;
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE;
    private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_BLOCK;
    private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET;
    private static final EntityDataAccessor<Boolean> DATA_ID_CUSTOM_DISPLAY;
    private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS;
    private boolean flipped;
    private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS;
    private int lSteps;
    private double lx;
    private double ly;
    private double lz;
    private double lyr;
    private double lxr;
    private double lxd;
    private double lyd;
    private double lzd;
    
    protected AbstractMinecart(final EntityType<?> aqb, final Level bru) {
        super(aqb, bru);
        this.blocksBuilding = true;
    }
    
    protected AbstractMinecart(final EntityType<?> aqb, final Level bru, final double double3, final double double4, final double double5) {
        this(aqb, bru);
        this.setPos(double3, double4, double5);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = double3;
        this.yo = double4;
        this.zo = double5;
    }
    
    public static AbstractMinecart createMinecart(final Level bru, final double double2, final double double3, final double double4, final Type a) {
        if (a == Type.CHEST) {
            return new MinecartChest(bru, double2, double3, double4);
        }
        if (a == Type.FURNACE) {
            return new MinecartFurnace(bru, double2, double3, double4);
        }
        if (a == Type.TNT) {
            return new MinecartTNT(bru, double2, double3, double4);
        }
        if (a == Type.SPAWNER) {
            return new MinecartSpawner(bru, double2, double3, double4);
        }
        if (a == Type.HOPPER) {
            return new MinecartHopper(bru, double2, double3, double4);
        }
        if (a == Type.COMMAND_BLOCK) {
            return new MinecartCommandBlock(bru, double2, double3, double4);
        }
        return new Minecart(bru, double2, double3, double4);
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<Integer>define(AbstractMinecart.DATA_ID_HURT, 0);
        this.entityData.<Integer>define(AbstractMinecart.DATA_ID_HURTDIR, 1);
        this.entityData.<Float>define(AbstractMinecart.DATA_ID_DAMAGE, 0.0f);
        this.entityData.<Integer>define(AbstractMinecart.DATA_ID_DISPLAY_BLOCK, Block.getId(Blocks.AIR.defaultBlockState()));
        this.entityData.<Integer>define(AbstractMinecart.DATA_ID_DISPLAY_OFFSET, 6);
        this.entityData.<Boolean>define(AbstractMinecart.DATA_ID_CUSTOM_DISPLAY, false);
    }
    
    @Override
    public boolean canCollideWith(final Entity apx) {
        return Boat.canVehicleCollide(this, apx);
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
        return 0.0;
    }
    
    @Override
    public Vec3 getDismountLocationForPassenger(final LivingEntity aqj) {
        final Direction gc3 = this.getMotionDirection();
        if (gc3.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(aqj);
        }
        final int[][] arr4 = DismountHelper.offsetsForDirection(gc3);
        final BlockPos fx5 = this.blockPosition();
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        final ImmutableList<Pose> immutableList7 = aqj.getDismountPoses();
        for (final Pose aqu9 : immutableList7) {
            final EntityDimensions apy10 = aqj.getDimensions(aqu9);
            final float float11 = Math.min(apy10.width, 1.0f) / 2.0f;
            for (final int integer13 : (ImmutableList)AbstractMinecart.POSE_DISMOUNT_HEIGHTS.get(aqu9)) {
                for (final int[] arr5 : arr4) {
                    a6.set(fx5.getX() + arr5[0], fx5.getY() + integer13, fx5.getZ() + arr5[1]);
                    final double double18 = this.level.getBlockFloorHeight(DismountHelper.nonClimbableShape(this.level, a6), (Supplier<VoxelShape>)(() -> DismountHelper.nonClimbableShape(this.level, a6.below())));
                    if (DismountHelper.isBlockFloorValid(double18)) {
                        final AABB dcf20 = new AABB(-float11, 0.0, -float11, float11, apy10.height, float11);
                        final Vec3 dck21 = Vec3.upFromBottomCenterOf(a6, double18);
                        if (DismountHelper.canDismountTo(this.level, aqj, dcf20.move(dck21))) {
                            aqj.setPose(aqu9);
                            return dck21;
                        }
                    }
                }
            }
        }
        final double double19 = this.getBoundingBox().maxY;
        a6.set(fx5.getX(), double19, fx5.getZ());
        for (final Pose aqu10 : immutableList7) {
            final double double20 = aqj.getDimensions(aqu10).height;
            final int integer14 = Mth.ceil(double19 - a6.getY() + double20);
            final double double21 = DismountHelper.findCeilingFrom(a6, integer14, (Function<BlockPos, VoxelShape>)(fx -> this.level.getBlockState(fx).getCollisionShape(this.level, fx)));
            if (double19 + double20 <= double21) {
                aqj.setPose(aqu10);
                break;
            }
        }
        return super.getDismountLocationForPassenger(aqj);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.level.isClientSide || this.removed) {
            return true;
        }
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.markHurt();
        this.setDamage(this.getDamage() + float2 * 10.0f);
        final boolean boolean4 = aph.getEntity() instanceof Player && ((Player)aph.getEntity()).abilities.instabuild;
        if (boolean4 || this.getDamage() > 40.0f) {
            this.ejectPassengers();
            if (!boolean4 || this.hasCustomName()) {
                this.destroy(aph);
            }
            else {
                this.remove();
            }
        }
        return true;
    }
    
    @Override
    protected float getBlockSpeedFactor() {
        final BlockState cee2 = this.level.getBlockState(this.blockPosition());
        if (cee2.is(BlockTags.RAILS)) {
            return 1.0f;
        }
        return super.getBlockSpeedFactor();
    }
    
    public void destroy(final DamageSource aph) {
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            final ItemStack bly3 = new ItemStack(Items.MINECART);
            if (this.hasCustomName()) {
                bly3.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(bly3);
        }
    }
    
    @Override
    public void animateHurt() {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0f);
    }
    
    @Override
    public boolean isPickable() {
        return !this.removed;
    }
    
    private static Pair<Vec3i, Vec3i> exits(final RailShape cfh) {
        return (Pair<Vec3i, Vec3i>)AbstractMinecart.EXITS.get(cfh);
    }
    
    @Override
    public Direction getMotionDirection() {
        return this.flipped ? this.getDirection().getOpposite().getClockWise() : this.getDirection().getClockWise();
    }
    
    @Override
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }
        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f);
        }
        if (this.getY() < -64.0) {
            this.outOfWorld();
        }
        this.handleNetherPortal();
        if (this.level.isClientSide) {
            if (this.lSteps > 0) {
                final double double2 = this.getX() + (this.lx - this.getX()) / this.lSteps;
                final double double3 = this.getY() + (this.ly - this.getY()) / this.lSteps;
                final double double4 = this.getZ() + (this.lz - this.getZ()) / this.lSteps;
                final double double5 = Mth.wrapDegrees(this.lyr - this.yRot);
                this.yRot += (float)(double5 / this.lSteps);
                this.xRot += (float)((this.lxr - this.xRot) / this.lSteps);
                --this.lSteps;
                this.setPos(double2, double3, double4);
                this.setRot(this.yRot, this.xRot);
            }
            else {
                this.reapplyPosition();
                this.setRot(this.yRot, this.xRot);
            }
            return;
        }
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        final int integer2 = Mth.floor(this.getX());
        int integer3 = Mth.floor(this.getY());
        final int integer4 = Mth.floor(this.getZ());
        if (this.level.getBlockState(new BlockPos(integer2, integer3 - 1, integer4)).is(BlockTags.RAILS)) {
            --integer3;
        }
        final BlockPos fx5 = new BlockPos(integer2, integer3, integer4);
        final BlockState cee6 = this.level.getBlockState(fx5);
        if (BaseRailBlock.isRail(cee6)) {
            this.moveAlongTrack(fx5, cee6);
            if (cee6.is(Blocks.ACTIVATOR_RAIL)) {
                this.activateMinecart(integer2, integer3, integer4, cee6.<Boolean>getValue((Property<Boolean>)PoweredRailBlock.POWERED));
            }
        }
        else {
            this.comeOffTrack();
        }
        this.checkInsideBlocks();
        this.xRot = 0.0f;
        final double double6 = this.xo - this.getX();
        final double double7 = this.zo - this.getZ();
        if (double6 * double6 + double7 * double7 > 0.001) {
            this.yRot = (float)(Mth.atan2(double7, double6) * 180.0 / 3.141592653589793);
            if (this.flipped) {
                this.yRot += 180.0f;
            }
        }
        final double double8 = Mth.wrapDegrees(this.yRot - this.yRotO);
        if (double8 < -170.0 || double8 >= 170.0) {
            this.yRot += 180.0f;
            this.flipped = !this.flipped;
        }
        this.setRot(this.yRot, this.xRot);
        if (this.getMinecartType() == Type.RIDEABLE && Entity.getHorizontalDistanceSqr(this.getDeltaMovement()) > 0.01) {
            final List<Entity> list13 = this.level.getEntities(this, this.getBoundingBox().inflate(0.20000000298023224, 0.0, 0.20000000298023224), EntitySelector.pushableBy(this));
            if (!list13.isEmpty()) {
                for (int integer5 = 0; integer5 < list13.size(); ++integer5) {
                    final Entity apx15 = (Entity)list13.get(integer5);
                    if (apx15 instanceof Player || apx15 instanceof IronGolem || apx15 instanceof AbstractMinecart || this.isVehicle() || apx15.isPassenger()) {
                        apx15.push(this);
                    }
                    else {
                        apx15.startRiding(this);
                    }
                }
            }
        }
        else {
            for (final Entity apx16 : this.level.getEntities(this, this.getBoundingBox().inflate(0.20000000298023224, 0.0, 0.20000000298023224))) {
                if (!this.hasPassenger(apx16) && apx16.isPushable() && apx16 instanceof AbstractMinecart) {
                    apx16.push(this);
                }
            }
        }
        this.updateInWaterStateAndDoFluidPushing();
        if (this.isInLava()) {
            this.lavaHurt();
            this.fallDistance *= 0.5f;
        }
        this.firstTick = false;
    }
    
    protected double getMaxSpeed() {
        return 0.4;
    }
    
    public void activateMinecart(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
    }
    
    protected void comeOffTrack() {
        final double double2 = this.getMaxSpeed();
        final Vec3 dck4 = this.getDeltaMovement();
        this.setDeltaMovement(Mth.clamp(dck4.x, -double2, double2), dck4.y, Mth.clamp(dck4.z, -double2, double2));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (!this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95));
        }
    }
    
    protected void moveAlongTrack(final BlockPos fx, final BlockState cee) {
        this.fallDistance = 0.0f;
        double double4 = this.getX();
        double double5 = this.getY();
        double double6 = this.getZ();
        final Vec3 dck10 = this.getPos(double4, double5, double6);
        double5 = fx.getY();
        boolean boolean11 = false;
        boolean boolean12 = false;
        final BaseRailBlock bud13 = (BaseRailBlock)cee.getBlock();
        if (bud13 == Blocks.POWERED_RAIL) {
            boolean11 = cee.<Boolean>getValue((Property<Boolean>)PoweredRailBlock.POWERED);
            boolean12 = !boolean11;
        }
        final double double7 = 0.0078125;
        Vec3 dck11 = this.getDeltaMovement();
        final RailShape cfh17 = cee.<RailShape>getValue(bud13.getShapeProperty());
        switch (cfh17) {
            case ASCENDING_EAST: {
                this.setDeltaMovement(dck11.add(-0.0078125, 0.0, 0.0));
                ++double5;
                break;
            }
            case ASCENDING_WEST: {
                this.setDeltaMovement(dck11.add(0.0078125, 0.0, 0.0));
                ++double5;
                break;
            }
            case ASCENDING_NORTH: {
                this.setDeltaMovement(dck11.add(0.0, 0.0, 0.0078125));
                ++double5;
                break;
            }
            case ASCENDING_SOUTH: {
                this.setDeltaMovement(dck11.add(0.0, 0.0, -0.0078125));
                ++double5;
                break;
            }
        }
        dck11 = this.getDeltaMovement();
        final Pair<Vec3i, Vec3i> pair18 = exits(cfh17);
        final Vec3i gr19 = (Vec3i)pair18.getFirst();
        final Vec3i gr20 = (Vec3i)pair18.getSecond();
        double double8 = gr20.getX() - gr19.getX();
        double double9 = gr20.getZ() - gr19.getZ();
        final double double10 = Math.sqrt(double8 * double8 + double9 * double9);
        final double double11 = dck11.x * double8 + dck11.z * double9;
        if (double11 < 0.0) {
            double8 = -double8;
            double9 = -double9;
        }
        final double double12 = Math.min(2.0, Math.sqrt(Entity.getHorizontalDistanceSqr(dck11)));
        dck11 = new Vec3(double12 * double8 / double10, dck11.y, double12 * double9 / double10);
        this.setDeltaMovement(dck11);
        final Entity apx31 = this.getPassengers().isEmpty() ? null : ((Entity)this.getPassengers().get(0));
        if (apx31 instanceof Player) {
            final Vec3 dck12 = apx31.getDeltaMovement();
            final double double13 = Entity.getHorizontalDistanceSqr(dck12);
            final double double14 = Entity.getHorizontalDistanceSqr(this.getDeltaMovement());
            if (double13 > 1.0E-4 && double14 < 0.01) {
                this.setDeltaMovement(this.getDeltaMovement().add(dck12.x * 0.1, 0.0, dck12.z * 0.1));
                boolean12 = false;
            }
        }
        if (boolean12) {
            final double double15 = Math.sqrt(Entity.getHorizontalDistanceSqr(this.getDeltaMovement()));
            if (double15 < 0.03) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            else {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.0, 0.5));
            }
        }
        final double double15 = fx.getX() + 0.5 + gr19.getX() * 0.5;
        final double double16 = fx.getZ() + 0.5 + gr19.getZ() * 0.5;
        final double double17 = fx.getX() + 0.5 + gr20.getX() * 0.5;
        final double double18 = fx.getZ() + 0.5 + gr20.getZ() * 0.5;
        double8 = double17 - double15;
        double9 = double18 - double16;
        double double19;
        if (double8 == 0.0) {
            double19 = double6 - fx.getZ();
        }
        else if (double9 == 0.0) {
            double19 = double4 - fx.getX();
        }
        else {
            final double double20 = double4 - double15;
            final double double21 = double6 - double16;
            double19 = (double20 * double8 + double21 * double9) * 2.0;
        }
        double4 = double15 + double8 * double19;
        double6 = double16 + double9 * double19;
        this.setPos(double4, double5, double6);
        final double double20 = this.isVehicle() ? 0.75 : 1.0;
        final double double21 = this.getMaxSpeed();
        dck11 = this.getDeltaMovement();
        this.move(MoverType.SELF, new Vec3(Mth.clamp(double20 * dck11.x, -double21, double21), 0.0, Mth.clamp(double20 * dck11.z, -double21, double21)));
        if (gr19.getY() != 0 && Mth.floor(this.getX()) - fx.getX() == gr19.getX() && Mth.floor(this.getZ()) - fx.getZ() == gr19.getZ()) {
            this.setPos(this.getX(), this.getY() + gr19.getY(), this.getZ());
        }
        else if (gr20.getY() != 0 && Mth.floor(this.getX()) - fx.getX() == gr20.getX() && Mth.floor(this.getZ()) - fx.getZ() == gr20.getZ()) {
            this.setPos(this.getX(), this.getY() + gr20.getY(), this.getZ());
        }
        this.applyNaturalSlowdown();
        final Vec3 dck13 = this.getPos(this.getX(), this.getY(), this.getZ());
        if (dck13 != null && dck10 != null) {
            final double double22 = (dck10.y - dck13.y) * 0.05;
            final Vec3 dck14 = this.getDeltaMovement();
            final double double23 = Math.sqrt(Entity.getHorizontalDistanceSqr(dck14));
            if (double23 > 0.0) {
                this.setDeltaMovement(dck14.multiply((double23 + double22) / double23, 1.0, (double23 + double22) / double23));
            }
            this.setPos(this.getX(), dck13.y, this.getZ());
        }
        final int integer47 = Mth.floor(this.getX());
        final int integer48 = Mth.floor(this.getZ());
        if (integer47 != fx.getX() || integer48 != fx.getZ()) {
            final Vec3 dck14 = this.getDeltaMovement();
            final double double23 = Math.sqrt(Entity.getHorizontalDistanceSqr(dck14));
            this.setDeltaMovement(double23 * (integer47 - fx.getX()), dck14.y, double23 * (integer48 - fx.getZ()));
        }
        if (boolean11) {
            final Vec3 dck14 = this.getDeltaMovement();
            final double double23 = Math.sqrt(Entity.getHorizontalDistanceSqr(dck14));
            if (double23 > 0.01) {
                final double double24 = 0.06;
                this.setDeltaMovement(dck14.add(dck14.x / double23 * 0.06, 0.0, dck14.z / double23 * 0.06));
            }
            else {
                final Vec3 dck15 = this.getDeltaMovement();
                double double25 = dck15.x;
                double double26 = dck15.z;
                if (cfh17 == RailShape.EAST_WEST) {
                    if (this.isRedstoneConductor(fx.west())) {
                        double25 = 0.02;
                    }
                    else if (this.isRedstoneConductor(fx.east())) {
                        double25 = -0.02;
                    }
                }
                else {
                    if (cfh17 != RailShape.NORTH_SOUTH) {
                        return;
                    }
                    if (this.isRedstoneConductor(fx.north())) {
                        double26 = 0.02;
                    }
                    else if (this.isRedstoneConductor(fx.south())) {
                        double26 = -0.02;
                    }
                }
                this.setDeltaMovement(double25, dck15.y, double26);
            }
        }
    }
    
    private boolean isRedstoneConductor(final BlockPos fx) {
        return this.level.getBlockState(fx).isRedstoneConductor(this.level, fx);
    }
    
    protected void applyNaturalSlowdown() {
        final double double2 = this.isVehicle() ? 0.997 : 0.96;
        this.setDeltaMovement(this.getDeltaMovement().multiply(double2, 0.0, double2));
    }
    
    @Nullable
    public Vec3 getPosOffs(double double1, double double2, double double3, final double double4) {
        final int integer10 = Mth.floor(double1);
        int integer11 = Mth.floor(double2);
        final int integer12 = Mth.floor(double3);
        if (this.level.getBlockState(new BlockPos(integer10, integer11 - 1, integer12)).is(BlockTags.RAILS)) {
            --integer11;
        }
        final BlockState cee13 = this.level.getBlockState(new BlockPos(integer10, integer11, integer12));
        if (BaseRailBlock.isRail(cee13)) {
            final RailShape cfh14 = cee13.<RailShape>getValue(((BaseRailBlock)cee13.getBlock()).getShapeProperty());
            double2 = integer11;
            if (cfh14.isAscending()) {
                double2 = integer11 + 1;
            }
            final Pair<Vec3i, Vec3i> pair15 = exits(cfh14);
            final Vec3i gr16 = (Vec3i)pair15.getFirst();
            final Vec3i gr17 = (Vec3i)pair15.getSecond();
            double double5 = gr17.getX() - gr16.getX();
            double double6 = gr17.getZ() - gr16.getZ();
            final double double7 = Math.sqrt(double5 * double5 + double6 * double6);
            double5 /= double7;
            double6 /= double7;
            double1 += double5 * double4;
            double3 += double6 * double4;
            if (gr16.getY() != 0 && Mth.floor(double1) - integer10 == gr16.getX() && Mth.floor(double3) - integer12 == gr16.getZ()) {
                double2 += gr16.getY();
            }
            else if (gr17.getY() != 0 && Mth.floor(double1) - integer10 == gr17.getX() && Mth.floor(double3) - integer12 == gr17.getZ()) {
                double2 += gr17.getY();
            }
            return this.getPos(double1, double2, double3);
        }
        return null;
    }
    
    @Nullable
    public Vec3 getPos(double double1, double double2, double double3) {
        final int integer8 = Mth.floor(double1);
        int integer9 = Mth.floor(double2);
        final int integer10 = Mth.floor(double3);
        if (this.level.getBlockState(new BlockPos(integer8, integer9 - 1, integer10)).is(BlockTags.RAILS)) {
            --integer9;
        }
        final BlockState cee11 = this.level.getBlockState(new BlockPos(integer8, integer9, integer10));
        if (BaseRailBlock.isRail(cee11)) {
            final RailShape cfh12 = cee11.<RailShape>getValue(((BaseRailBlock)cee11.getBlock()).getShapeProperty());
            final Pair<Vec3i, Vec3i> pair13 = exits(cfh12);
            final Vec3i gr14 = (Vec3i)pair13.getFirst();
            final Vec3i gr15 = (Vec3i)pair13.getSecond();
            final double double4 = integer8 + 0.5 + gr14.getX() * 0.5;
            final double double5 = integer9 + 0.0625 + gr14.getY() * 0.5;
            final double double6 = integer10 + 0.5 + gr14.getZ() * 0.5;
            final double double7 = integer8 + 0.5 + gr15.getX() * 0.5;
            final double double8 = integer9 + 0.0625 + gr15.getY() * 0.5;
            final double double9 = integer10 + 0.5 + gr15.getZ() * 0.5;
            final double double10 = double7 - double4;
            final double double11 = (double8 - double5) * 2.0;
            final double double12 = double9 - double6;
            double double13;
            if (double10 == 0.0) {
                double13 = double3 - integer10;
            }
            else if (double12 == 0.0) {
                double13 = double1 - integer8;
            }
            else {
                final double double14 = double1 - double4;
                final double double15 = double3 - double6;
                double13 = (double14 * double10 + double15 * double12) * 2.0;
            }
            double1 = double4 + double10 * double13;
            double2 = double5 + double11 * double13;
            double3 = double6 + double12 * double13;
            if (double11 < 0.0) {
                ++double2;
            }
            else if (double11 > 0.0) {
                double2 += 0.5;
            }
            return new Vec3(double1, double2, double3);
        }
        return null;
    }
    
    @Override
    public AABB getBoundingBoxForCulling() {
        final AABB dcf2 = this.getBoundingBox();
        if (this.hasCustomDisplay()) {
            return dcf2.inflate(Math.abs(this.getDisplayOffset()) / 16.0);
        }
        return dcf2;
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        if (md.getBoolean("CustomDisplayTile")) {
            this.setDisplayBlockState(NbtUtils.readBlockState(md.getCompound("DisplayState")));
            this.setDisplayOffset(md.getInt("DisplayOffset"));
        }
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        if (this.hasCustomDisplay()) {
            md.putBoolean("CustomDisplayTile", true);
            md.put("DisplayState", (net.minecraft.nbt.Tag)NbtUtils.writeBlockState(this.getDisplayBlockState()));
            md.putInt("DisplayOffset", this.getDisplayOffset());
        }
    }
    
    @Override
    public void push(final Entity apx) {
        if (this.level.isClientSide) {
            return;
        }
        if (apx.noPhysics || this.noPhysics) {
            return;
        }
        if (this.hasPassenger(apx)) {
            return;
        }
        double double3 = apx.getX() - this.getX();
        double double4 = apx.getZ() - this.getZ();
        double double5 = double3 * double3 + double4 * double4;
        if (double5 >= 9.999999747378752E-5) {
            double5 = Mth.sqrt(double5);
            double3 /= double5;
            double4 /= double5;
            double double6 = 1.0 / double5;
            if (double6 > 1.0) {
                double6 = 1.0;
            }
            double3 *= double6;
            double4 *= double6;
            double3 *= 0.10000000149011612;
            double4 *= 0.10000000149011612;
            double3 *= 1.0f - this.pushthrough;
            double4 *= 1.0f - this.pushthrough;
            double3 *= 0.5;
            double4 *= 0.5;
            if (apx instanceof AbstractMinecart) {
                final double double7 = apx.getX() - this.getX();
                final double double8 = apx.getZ() - this.getZ();
                final Vec3 dck15 = new Vec3(double7, 0.0, double8).normalize();
                final Vec3 dck16 = new Vec3(Mth.cos(this.yRot * 0.017453292f), 0.0, Mth.sin(this.yRot * 0.017453292f)).normalize();
                final double double9 = Math.abs(dck15.dot(dck16));
                if (double9 < 0.800000011920929) {
                    return;
                }
                final Vec3 dck17 = this.getDeltaMovement();
                final Vec3 dck18 = apx.getDeltaMovement();
                if (((AbstractMinecart)apx).getMinecartType() == Type.FURNACE && this.getMinecartType() != Type.FURNACE) {
                    this.setDeltaMovement(dck17.multiply(0.2, 1.0, 0.2));
                    this.push(dck18.x - double3, 0.0, dck18.z - double4);
                    apx.setDeltaMovement(dck18.multiply(0.95, 1.0, 0.95));
                }
                else if (((AbstractMinecart)apx).getMinecartType() != Type.FURNACE && this.getMinecartType() == Type.FURNACE) {
                    apx.setDeltaMovement(dck18.multiply(0.2, 1.0, 0.2));
                    apx.push(dck17.x + double3, 0.0, dck17.z + double4);
                    this.setDeltaMovement(dck17.multiply(0.95, 1.0, 0.95));
                }
                else {
                    final double double10 = (dck18.x + dck17.x) / 2.0;
                    final double double11 = (dck18.z + dck17.z) / 2.0;
                    this.setDeltaMovement(dck17.multiply(0.2, 1.0, 0.2));
                    this.push(double10 - double3, 0.0, double11 - double4);
                    apx.setDeltaMovement(dck18.multiply(0.2, 1.0, 0.2));
                    apx.push(double10 + double3, 0.0, double11 + double4);
                }
            }
            else {
                this.push(-double3, 0.0, -double4);
                apx.push(double3 / 4.0, 0.0, double4 / 4.0);
            }
        }
    }
    
    @Override
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        this.lx = double1;
        this.ly = double2;
        this.lz = double3;
        this.lyr = float4;
        this.lxr = float5;
        this.lSteps = integer + 2;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }
    
    @Override
    public void lerpMotion(final double double1, final double double2, final double double3) {
        this.lxd = double1;
        this.lyd = double2;
        this.lzd = double3;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }
    
    public void setDamage(final float float1) {
        this.entityData.<Float>set(AbstractMinecart.DATA_ID_DAMAGE, float1);
    }
    
    public float getDamage() {
        return this.entityData.<Float>get(AbstractMinecart.DATA_ID_DAMAGE);
    }
    
    public void setHurtTime(final int integer) {
        this.entityData.<Integer>set(AbstractMinecart.DATA_ID_HURT, integer);
    }
    
    public int getHurtTime() {
        return this.entityData.<Integer>get(AbstractMinecart.DATA_ID_HURT);
    }
    
    public void setHurtDir(final int integer) {
        this.entityData.<Integer>set(AbstractMinecart.DATA_ID_HURTDIR, integer);
    }
    
    public int getHurtDir() {
        return this.entityData.<Integer>get(AbstractMinecart.DATA_ID_HURTDIR);
    }
    
    public abstract Type getMinecartType();
    
    public BlockState getDisplayBlockState() {
        if (!this.hasCustomDisplay()) {
            return this.getDefaultDisplayBlockState();
        }
        return Block.stateById(this.getEntityData().<Integer>get(AbstractMinecart.DATA_ID_DISPLAY_BLOCK));
    }
    
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.AIR.defaultBlockState();
    }
    
    public int getDisplayOffset() {
        if (!this.hasCustomDisplay()) {
            return this.getDefaultDisplayOffset();
        }
        return this.getEntityData().<Integer>get(AbstractMinecart.DATA_ID_DISPLAY_OFFSET);
    }
    
    public int getDefaultDisplayOffset() {
        return 6;
    }
    
    public void setDisplayBlockState(final BlockState cee) {
        this.getEntityData().<Integer>set(AbstractMinecart.DATA_ID_DISPLAY_BLOCK, Block.getId(cee));
        this.setCustomDisplay(true);
    }
    
    public void setDisplayOffset(final int integer) {
        this.getEntityData().<Integer>set(AbstractMinecart.DATA_ID_DISPLAY_OFFSET, integer);
        this.setCustomDisplay(true);
    }
    
    public boolean hasCustomDisplay() {
        return this.getEntityData().<Boolean>get(AbstractMinecart.DATA_ID_CUSTOM_DISPLAY);
    }
    
    public void setCustomDisplay(final boolean boolean1) {
        this.getEntityData().<Boolean>set(AbstractMinecart.DATA_ID_CUSTOM_DISPLAY, boolean1);
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    static {
        DATA_ID_HURT = SynchedEntityData.<Integer>defineId(AbstractMinecart.class, EntityDataSerializers.INT);
        DATA_ID_HURTDIR = SynchedEntityData.<Integer>defineId(AbstractMinecart.class, EntityDataSerializers.INT);
        DATA_ID_DAMAGE = SynchedEntityData.<Float>defineId(AbstractMinecart.class, EntityDataSerializers.FLOAT);
        DATA_ID_DISPLAY_BLOCK = SynchedEntityData.<Integer>defineId(AbstractMinecart.class, EntityDataSerializers.INT);
        DATA_ID_DISPLAY_OFFSET = SynchedEntityData.<Integer>defineId(AbstractMinecart.class, EntityDataSerializers.INT);
        DATA_ID_CUSTOM_DISPLAY = SynchedEntityData.<Boolean>defineId(AbstractMinecart.class, EntityDataSerializers.BOOLEAN);
        POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, ImmutableList.of((Object)0, (Object)1, (Object)(-1)), Pose.CROUCHING, ImmutableList.of((Object)0, (Object)1, (Object)(-1)), Pose.SWIMMING, ImmutableList.of((Object)0, (Object)1));
        EXITS = Util.<Map>make((Map)Maps.newEnumMap((Class)RailShape.class), (java.util.function.Consumer<Map>)(enumMap -> {
            final Vec3i gr2 = Direction.WEST.getNormal();
            final Vec3i gr3 = Direction.EAST.getNormal();
            final Vec3i gr4 = Direction.NORTH.getNormal();
            final Vec3i gr5 = Direction.SOUTH.getNormal();
            final Vec3i gr6 = gr2.below();
            final Vec3i gr7 = gr3.below();
            final Vec3i gr8 = gr4.below();
            final Vec3i gr9 = gr5.below();
            enumMap.put((Enum)RailShape.NORTH_SOUTH, Pair.of((Object)gr4, (Object)gr5));
            enumMap.put((Enum)RailShape.EAST_WEST, Pair.of((Object)gr2, (Object)gr3));
            enumMap.put((Enum)RailShape.ASCENDING_EAST, Pair.of((Object)gr6, (Object)gr3));
            enumMap.put((Enum)RailShape.ASCENDING_WEST, Pair.of((Object)gr2, (Object)gr7));
            enumMap.put((Enum)RailShape.ASCENDING_NORTH, Pair.of((Object)gr4, (Object)gr9));
            enumMap.put((Enum)RailShape.ASCENDING_SOUTH, Pair.of((Object)gr8, (Object)gr5));
            enumMap.put((Enum)RailShape.SOUTH_EAST, Pair.of((Object)gr5, (Object)gr3));
            enumMap.put((Enum)RailShape.SOUTH_WEST, Pair.of((Object)gr5, (Object)gr2));
            enumMap.put((Enum)RailShape.NORTH_WEST, Pair.of((Object)gr4, (Object)gr2));
            enumMap.put((Enum)RailShape.NORTH_EAST, Pair.of((Object)gr4, (Object)gr3));
        }));
    }
    
    public enum Type {
        RIDEABLE, 
        CHEST, 
        FURNACE, 
        TNT, 
        SPAWNER, 
        HOPPER, 
        COMMAND_BLOCK;
    }
}
