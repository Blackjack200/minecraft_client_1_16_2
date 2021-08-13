package net.minecraft.world.entity;

import net.minecraft.network.syncher.EntityDataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.protocol.Packet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.level.GameRules;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.material.PushReaction;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.CrashReportDetail;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.BlockUtil;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import java.util.Locale;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.core.Vec3i;
import com.google.common.collect.Iterables;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.tags.FluidTags;
import java.util.Iterator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.LevelReader;
import net.minecraft.util.RewindableStream;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import java.util.Arrays;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.CrashReportCategory;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.scores.Team;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.Sets;
import net.minecraft.util.Mth;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import com.google.common.collect.Lists;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;

public abstract class Entity implements Nameable, CommandSource {
    protected static final Logger LOGGER;
    private static final AtomicInteger ENTITY_COUNTER;
    private static final List<ItemStack> EMPTY_LIST;
    private static final AABB INITIAL_AABB;
    private static double viewScale;
    private final EntityType<?> type;
    private int id;
    public boolean blocksBuilding;
    private final List<Entity> passengers;
    protected int boardingCooldown;
    @Nullable
    private Entity vehicle;
    public boolean forcedLoading;
    public Level level;
    public double xo;
    public double yo;
    public double zo;
    private Vec3 position;
    private BlockPos blockPosition;
    private Vec3 deltaMovement;
    public float yRot;
    public float xRot;
    public float yRotO;
    public float xRotO;
    private AABB bb;
    protected boolean onGround;
    public boolean horizontalCollision;
    public boolean verticalCollision;
    public boolean hurtMarked;
    protected Vec3 stuckSpeedMultiplier;
    public boolean removed;
    public float walkDistO;
    public float walkDist;
    public float moveDist;
    public float fallDistance;
    private float nextStep;
    private float nextFlap;
    public double xOld;
    public double yOld;
    public double zOld;
    public float maxUpStep;
    public boolean noPhysics;
    public float pushthrough;
    protected final Random random;
    public int tickCount;
    private int remainingFireTicks;
    protected boolean wasTouchingWater;
    protected Object2DoubleMap<Tag<Fluid>> fluidHeight;
    protected boolean wasEyeInWater;
    @Nullable
    protected Tag<Fluid> fluidOnEyes;
    public int invulnerableTime;
    protected boolean firstTick;
    protected final SynchedEntityData entityData;
    protected static final EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;
    private static final EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID;
    private static final EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME;
    private static final EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE;
    private static final EntityDataAccessor<Boolean> DATA_SILENT;
    private static final EntityDataAccessor<Boolean> DATA_NO_GRAVITY;
    protected static final EntityDataAccessor<Pose> DATA_POSE;
    public boolean inChunk;
    public int xChunk;
    public int yChunk;
    public int zChunk;
    private boolean movedSinceLastChunkCheck;
    private Vec3 packetCoordinates;
    public boolean noCulling;
    public boolean hasImpulse;
    private int portalCooldown;
    protected boolean isInsidePortal;
    protected int portalTime;
    protected BlockPos portalEntrancePos;
    private boolean invulnerable;
    protected UUID uuid;
    protected String stringUUID;
    protected boolean glowing;
    private final Set<String> tags;
    private boolean forceChunkAddition;
    private final double[] pistonDeltas;
    private long pistonDeltasGameTime;
    private EntityDimensions dimensions;
    private float eyeHeight;
    
    public Entity(final EntityType<?> aqb, final Level bru) {
        this.id = Entity.ENTITY_COUNTER.incrementAndGet();
        this.passengers = (List<Entity>)Lists.newArrayList();
        this.deltaMovement = Vec3.ZERO;
        this.bb = Entity.INITIAL_AABB;
        this.stuckSpeedMultiplier = Vec3.ZERO;
        this.nextStep = 1.0f;
        this.nextFlap = 1.0f;
        this.random = new Random();
        this.remainingFireTicks = -this.getFireImmuneTicks();
        this.fluidHeight = (Object2DoubleMap<Tag<Fluid>>)new Object2DoubleArrayMap(2);
        this.firstTick = true;
        this.uuid = Mth.createInsecureUUID(this.random);
        this.stringUUID = this.uuid.toString();
        this.tags = (Set<String>)Sets.newHashSet();
        this.pistonDeltas = new double[] { 0.0, 0.0, 0.0 };
        this.type = aqb;
        this.level = bru;
        this.dimensions = aqb.getDimensions();
        this.position = Vec3.ZERO;
        this.blockPosition = BlockPos.ZERO;
        this.packetCoordinates = Vec3.ZERO;
        this.setPos(0.0, 0.0, 0.0);
        (this.entityData = new SynchedEntityData(this)).<Byte>define(Entity.DATA_SHARED_FLAGS_ID, (Byte)0);
        this.entityData.<Integer>define(Entity.DATA_AIR_SUPPLY_ID, this.getMaxAirSupply());
        this.entityData.<Boolean>define(Entity.DATA_CUSTOM_NAME_VISIBLE, false);
        this.entityData.<Optional<Component>>define(Entity.DATA_CUSTOM_NAME, (Optional<Component>)Optional.empty());
        this.entityData.<Boolean>define(Entity.DATA_SILENT, false);
        this.entityData.<Boolean>define(Entity.DATA_NO_GRAVITY, false);
        this.entityData.<Pose>define(Entity.DATA_POSE, Pose.STANDING);
        this.defineSynchedData();
        this.eyeHeight = this.getEyeHeight(Pose.STANDING, this.dimensions);
    }
    
    public boolean isColliding(final BlockPos fx, final BlockState cee) {
        final VoxelShape dde4 = cee.getCollisionShape(this.level, fx, CollisionContext.of(this));
        final VoxelShape dde5 = dde4.move(fx.getX(), fx.getY(), fx.getZ());
        return Shapes.joinIsNotEmpty(dde5, Shapes.create(this.getBoundingBox()), BooleanOp.AND);
    }
    
    public int getTeamColor() {
        final Team ddm2 = this.getTeam();
        if (ddm2 != null && ddm2.getColor().getColor() != null) {
            return ddm2.getColor().getColor();
        }
        return 16777215;
    }
    
    public boolean isSpectator() {
        return false;
    }
    
    public final void unRide() {
        if (this.isVehicle()) {
            this.ejectPassengers();
        }
        if (this.isPassenger()) {
            this.stopRiding();
        }
    }
    
    public void setPacketCoordinates(final double double1, final double double2, final double double3) {
        this.setPacketCoordinates(new Vec3(double1, double2, double3));
    }
    
    public void setPacketCoordinates(final Vec3 dck) {
        this.packetCoordinates = dck;
    }
    
    public Vec3 getPacketCoordinates() {
        return this.packetCoordinates;
    }
    
    public EntityType<?> getType() {
        return this.type;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int integer) {
        this.id = integer;
    }
    
    public Set<String> getTags() {
        return this.tags;
    }
    
    public boolean addTag(final String string) {
        return this.tags.size() < 1024 && this.tags.add(string);
    }
    
    public boolean removeTag(final String string) {
        return this.tags.remove(string);
    }
    
    public void kill() {
        this.remove();
    }
    
    protected abstract void defineSynchedData();
    
    public SynchedEntityData getEntityData() {
        return this.entityData;
    }
    
    public boolean equals(final Object object) {
        return object instanceof Entity && ((Entity)object).id == this.id;
    }
    
    public int hashCode() {
        return this.id;
    }
    
    protected void resetPos() {
        if (this.level == null) {
            return;
        }
        for (double double2 = this.getY(); double2 > 0.0 && double2 < 256.0; ++double2) {
            this.setPos(this.getX(), double2, this.getZ());
            if (this.level.noCollision(this)) {
                break;
            }
        }
        this.setDeltaMovement(Vec3.ZERO);
        this.xRot = 0.0f;
    }
    
    public void remove() {
        this.removed = true;
    }
    
    public void setPose(final Pose aqu) {
        this.entityData.<Pose>set(Entity.DATA_POSE, aqu);
    }
    
    public Pose getPose() {
        return this.entityData.<Pose>get(Entity.DATA_POSE);
    }
    
    public boolean closerThan(final Entity apx, final double double2) {
        final double double3 = apx.position.x - this.position.x;
        final double double4 = apx.position.y - this.position.y;
        final double double5 = apx.position.z - this.position.z;
        return double3 * double3 + double4 * double4 + double5 * double5 < double2 * double2;
    }
    
    protected void setRot(final float float1, final float float2) {
        this.yRot = float1 % 360.0f;
        this.xRot = float2 % 360.0f;
    }
    
    public void setPos(final double double1, final double double2, final double double3) {
        this.setPosRaw(double1, double2, double3);
        this.setBoundingBox(this.dimensions.makeBoundingBox(double1, double2, double3));
    }
    
    protected void reapplyPosition() {
        this.setPos(this.position.x, this.position.y, this.position.z);
    }
    
    public void turn(final double double1, final double double2) {
        final double double3 = double2 * 0.15;
        final double double4 = double1 * 0.15;
        this.xRot += (float)double3;
        this.yRot += (float)double4;
        this.xRot = Mth.clamp(this.xRot, -90.0f, 90.0f);
        this.xRotO += (float)double3;
        this.yRotO += (float)double4;
        this.xRotO = Mth.clamp(this.xRotO, -90.0f, 90.0f);
        if (this.vehicle != null) {
            this.vehicle.onPassengerTurned(this);
        }
    }
    
    public void tick() {
        if (!this.level.isClientSide) {
            this.setSharedFlag(6, this.isGlowing());
        }
        this.baseTick();
    }
    
    public void baseTick() {
        this.level.getProfiler().push("entityBaseTick");
        if (this.isPassenger() && this.getVehicle().removed) {
            this.stopRiding();
        }
        if (this.boardingCooldown > 0) {
            --this.boardingCooldown;
        }
        this.walkDistO = this.walkDist;
        this.xRotO = this.xRot;
        this.yRotO = this.yRot;
        this.handleNetherPortal();
        if (this.canSpawnSprintParticle()) {
            this.spawnSprintParticle();
        }
        this.updateInWaterStateAndDoFluidPushing();
        this.updateFluidOnEyes();
        this.updateSwimming();
        if (this.level.isClientSide) {
            this.clearFire();
        }
        else if (this.remainingFireTicks > 0) {
            if (this.fireImmune()) {
                this.setRemainingFireTicks(this.remainingFireTicks - 4);
                if (this.remainingFireTicks < 0) {
                    this.clearFire();
                }
            }
            else {
                if (this.remainingFireTicks % 20 == 0 && !this.isInLava()) {
                    this.hurt(DamageSource.ON_FIRE, 1.0f);
                }
                this.setRemainingFireTicks(this.remainingFireTicks - 1);
            }
        }
        if (this.isInLava()) {
            this.lavaHurt();
            this.fallDistance *= 0.5f;
        }
        if (this.getY() < -64.0) {
            this.outOfWorld();
        }
        if (!this.level.isClientSide) {
            this.setSharedFlag(0, this.remainingFireTicks > 0);
        }
        this.firstTick = false;
        this.level.getProfiler().pop();
    }
    
    public void setPortalCooldown() {
        this.portalCooldown = this.getDimensionChangingDelay();
    }
    
    public boolean isOnPortalCooldown() {
        return this.portalCooldown > 0;
    }
    
    protected void processPortalCooldown() {
        if (this.isOnPortalCooldown()) {
            --this.portalCooldown;
        }
    }
    
    public int getPortalWaitTime() {
        return 0;
    }
    
    protected void lavaHurt() {
        if (this.fireImmune()) {
            return;
        }
        this.setSecondsOnFire(15);
        this.hurt(DamageSource.LAVA, 4.0f);
    }
    
    public void setSecondsOnFire(final int integer) {
        int integer2 = integer * 20;
        if (this instanceof LivingEntity) {
            integer2 = ProtectionEnchantment.getFireAfterDampener((LivingEntity)this, integer2);
        }
        if (this.remainingFireTicks < integer2) {
            this.setRemainingFireTicks(integer2);
        }
    }
    
    public void setRemainingFireTicks(final int integer) {
        this.remainingFireTicks = integer;
    }
    
    public int getRemainingFireTicks() {
        return this.remainingFireTicks;
    }
    
    public void clearFire() {
        this.setRemainingFireTicks(0);
    }
    
    protected void outOfWorld() {
        this.remove();
    }
    
    public boolean isFree(final double double1, final double double2, final double double3) {
        return this.isFree(this.getBoundingBox().move(double1, double2, double3));
    }
    
    private boolean isFree(final AABB dcf) {
        return this.level.noCollision(this, dcf) && !this.level.containsAnyLiquid(dcf);
    }
    
    public void setOnGround(final boolean boolean1) {
        this.onGround = boolean1;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void move(final MoverType aqo, Vec3 dck) {
        if (this.noPhysics) {
            this.setBoundingBox(this.getBoundingBox().move(dck));
            this.setLocationFromBoundingbox();
            return;
        }
        if (aqo == MoverType.PISTON) {
            dck = this.limitPistonMovement(dck);
            if (dck.equals(Vec3.ZERO)) {
                return;
            }
        }
        this.level.getProfiler().push("move");
        if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7) {
            dck = dck.multiply(this.stuckSpeedMultiplier);
            this.stuckSpeedMultiplier = Vec3.ZERO;
            this.setDeltaMovement(Vec3.ZERO);
        }
        dck = this.maybeBackOffFromEdge(dck, aqo);
        final Vec3 dck2 = this.collide(dck);
        if (dck2.lengthSqr() > 1.0E-7) {
            this.setBoundingBox(this.getBoundingBox().move(dck2));
            this.setLocationFromBoundingbox();
        }
        this.level.getProfiler().pop();
        this.level.getProfiler().push("rest");
        this.horizontalCollision = (!Mth.equal(dck.x, dck2.x) || !Mth.equal(dck.z, dck2.z));
        this.verticalCollision = (dck.y != dck2.y);
        this.onGround = (this.verticalCollision && dck.y < 0.0);
        final BlockPos fx5 = this.getOnPos();
        final BlockState cee6 = this.level.getBlockState(fx5);
        this.checkFallDamage(dck2.y, this.onGround, cee6, fx5);
        final Vec3 dck3 = this.getDeltaMovement();
        if (dck.x != dck2.x) {
            this.setDeltaMovement(0.0, dck3.y, dck3.z);
        }
        if (dck.z != dck2.z) {
            this.setDeltaMovement(dck3.x, dck3.y, 0.0);
        }
        final Block bul8 = cee6.getBlock();
        if (dck.y != dck2.y) {
            bul8.updateEntityAfterFallOn(this.level, this);
        }
        if (this.onGround && !this.isSteppingCarefully()) {
            bul8.stepOn(this.level, fx5, this);
        }
        if (this.isMovementNoisy() && !this.isPassenger()) {
            final double double9 = dck2.x;
            double double10 = dck2.y;
            final double double11 = dck2.z;
            if (!bul8.is(BlockTags.CLIMBABLE)) {
                double10 = 0.0;
            }
            this.walkDist += (float)(Mth.sqrt(getHorizontalDistanceSqr(dck2)) * 0.6);
            this.moveDist += (float)(Mth.sqrt(double9 * double9 + double10 * double10 + double11 * double11) * 0.6);
            if (this.moveDist > this.nextStep && !cee6.isAir()) {
                this.nextStep = this.nextStep();
                if (this.isInWater()) {
                    final Entity apx15 = (this.isVehicle() && this.getControllingPassenger() != null) ? this.getControllingPassenger() : this;
                    final float float16 = (apx15 == this) ? 0.35f : 0.4f;
                    final Vec3 dck4 = apx15.getDeltaMovement();
                    float float17 = Mth.sqrt(dck4.x * dck4.x * 0.20000000298023224 + dck4.y * dck4.y + dck4.z * dck4.z * 0.20000000298023224) * float16;
                    if (float17 > 1.0f) {
                        float17 = 1.0f;
                    }
                    this.playSwimSound(float17);
                }
                else {
                    this.playStepSound(fx5, cee6);
                }
            }
            else if (this.moveDist > this.nextFlap && this.makeFlySound() && cee6.isAir()) {
                this.nextFlap = this.playFlySound(this.moveDist);
            }
        }
        try {
            this.checkInsideBlocks();
        }
        catch (Throwable throwable9) {
            final CrashReport l10 = CrashReport.forThrowable(throwable9, "Checking entity block collision");
            final CrashReportCategory m11 = l10.addCategory("Entity being checked for collision");
            this.fillCrashReportCategory(m11);
            throw new ReportedException(l10);
        }
        final float float18 = this.getBlockSpeedFactor();
        this.setDeltaMovement(this.getDeltaMovement().multiply(float18, 1.0, float18));
        if (this.level.getBlockStatesIfLoaded(this.getBoundingBox().deflate(0.001)).noneMatch(cee -> cee.is(BlockTags.FIRE) || cee.is(Blocks.LAVA)) && this.remainingFireTicks <= 0) {
            this.setRemainingFireTicks(-this.getFireImmuneTicks());
        }
        if (this.isInWaterRainOrBubble() && this.isOnFire()) {
            this.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.7f, 1.6f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
            this.setRemainingFireTicks(-this.getFireImmuneTicks());
        }
        this.level.getProfiler().pop();
    }
    
    protected BlockPos getOnPos() {
        final int integer2 = Mth.floor(this.position.x);
        final int integer3 = Mth.floor(this.position.y - 0.20000000298023224);
        final int integer4 = Mth.floor(this.position.z);
        final BlockPos fx5 = new BlockPos(integer2, integer3, integer4);
        if (this.level.getBlockState(fx5).isAir()) {
            final BlockPos fx6 = fx5.below();
            final BlockState cee7 = this.level.getBlockState(fx6);
            final Block bul8 = cee7.getBlock();
            if (bul8.is(BlockTags.FENCES) || bul8.is(BlockTags.WALLS) || bul8 instanceof FenceGateBlock) {
                return fx6;
            }
        }
        return fx5;
    }
    
    protected float getBlockJumpFactor() {
        final float float2 = this.level.getBlockState(this.blockPosition()).getBlock().getJumpFactor();
        final float float3 = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getJumpFactor();
        return (float2 == 1.0) ? float3 : float2;
    }
    
    protected float getBlockSpeedFactor() {
        final Block bul2 = this.level.getBlockState(this.blockPosition()).getBlock();
        final float float3 = bul2.getSpeedFactor();
        if (bul2 == Blocks.WATER || bul2 == Blocks.BUBBLE_COLUMN) {
            return float3;
        }
        return (float3 == 1.0) ? this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getSpeedFactor() : float3;
    }
    
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return new BlockPos(this.position.x, this.getBoundingBox().minY - 0.5000001, this.position.z);
    }
    
    protected Vec3 maybeBackOffFromEdge(final Vec3 dck, final MoverType aqo) {
        return dck;
    }
    
    protected Vec3 limitPistonMovement(final Vec3 dck) {
        if (dck.lengthSqr() <= 1.0E-7) {
            return dck;
        }
        final long long3 = this.level.getGameTime();
        if (long3 != this.pistonDeltasGameTime) {
            Arrays.fill(this.pistonDeltas, 0.0);
            this.pistonDeltasGameTime = long3;
        }
        if (dck.x != 0.0) {
            final double double5 = this.applyPistonMovementRestriction(Direction.Axis.X, dck.x);
            return (Math.abs(double5) <= 9.999999747378752E-6) ? Vec3.ZERO : new Vec3(double5, 0.0, 0.0);
        }
        if (dck.y != 0.0) {
            final double double5 = this.applyPistonMovementRestriction(Direction.Axis.Y, dck.y);
            return (Math.abs(double5) <= 9.999999747378752E-6) ? Vec3.ZERO : new Vec3(0.0, double5, 0.0);
        }
        if (dck.z != 0.0) {
            final double double5 = this.applyPistonMovementRestriction(Direction.Axis.Z, dck.z);
            return (Math.abs(double5) <= 9.999999747378752E-6) ? Vec3.ZERO : new Vec3(0.0, 0.0, double5);
        }
        return Vec3.ZERO;
    }
    
    private double applyPistonMovementRestriction(final Direction.Axis a, double double2) {
        final int integer5 = a.ordinal();
        final double double3 = Mth.clamp(double2 + this.pistonDeltas[integer5], -0.51, 0.51);
        double2 = double3 - this.pistonDeltas[integer5];
        this.pistonDeltas[integer5] = double3;
        return double2;
    }
    
    private Vec3 collide(final Vec3 dck) {
        final AABB dcf3 = this.getBoundingBox();
        final CollisionContext dcp4 = CollisionContext.of(this);
        final VoxelShape dde5 = this.level.getWorldBorder().getCollisionShape();
        final Stream<VoxelShape> stream6 = (Stream<VoxelShape>)(Shapes.joinIsNotEmpty(dde5, Shapes.create(dcf3.deflate(1.0E-7)), BooleanOp.AND) ? Stream.empty() : Stream.of(dde5));
        final Stream<VoxelShape> stream7 = this.level.getEntityCollisions(this, dcf3.expandTowards(dck), (Predicate<Entity>)(apx -> true));
        final RewindableStream<VoxelShape> afl8 = new RewindableStream<VoxelShape>((java.util.stream.Stream<VoxelShape>)Stream.concat((Stream)stream7, (Stream)stream6));
        final Vec3 dck2 = (dck.lengthSqr() == 0.0) ? dck : collideBoundingBoxHeuristically(this, dck, dcf3, this.level, dcp4, afl8);
        final boolean boolean10 = dck.x != dck2.x;
        final boolean boolean11 = dck.y != dck2.y;
        final boolean boolean12 = dck.z != dck2.z;
        final boolean boolean13 = this.onGround || (boolean11 && dck.y < 0.0);
        if (this.maxUpStep > 0.0f && boolean13 && (boolean10 || boolean12)) {
            Vec3 dck3 = collideBoundingBoxHeuristically(this, new Vec3(dck.x, this.maxUpStep, dck.z), dcf3, this.level, dcp4, afl8);
            final Vec3 dck4 = collideBoundingBoxHeuristically(this, new Vec3(0.0, this.maxUpStep, 0.0), dcf3.expandTowards(dck.x, 0.0, dck.z), this.level, dcp4, afl8);
            if (dck4.y < this.maxUpStep) {
                final Vec3 dck5 = collideBoundingBoxHeuristically(this, new Vec3(dck.x, 0.0, dck.z), dcf3.move(dck4), this.level, dcp4, afl8).add(dck4);
                if (getHorizontalDistanceSqr(dck5) > getHorizontalDistanceSqr(dck3)) {
                    dck3 = dck5;
                }
            }
            if (getHorizontalDistanceSqr(dck3) > getHorizontalDistanceSqr(dck2)) {
                return dck3.add(collideBoundingBoxHeuristically(this, new Vec3(0.0, -dck3.y + dck.y, 0.0), dcf3.move(dck3), this.level, dcp4, afl8));
            }
        }
        return dck2;
    }
    
    public static double getHorizontalDistanceSqr(final Vec3 dck) {
        return dck.x * dck.x + dck.z * dck.z;
    }
    
    public static Vec3 collideBoundingBoxHeuristically(@Nullable final Entity apx, final Vec3 dck, final AABB dcf, final Level bru, final CollisionContext dcp, final RewindableStream<VoxelShape> afl) {
        final boolean boolean7 = dck.x == 0.0;
        final boolean boolean8 = dck.y == 0.0;
        final boolean boolean9 = dck.z == 0.0;
        if ((boolean7 && boolean8) || (boolean7 && boolean9) || (boolean8 && boolean9)) {
            return collideBoundingBox(dck, dcf, bru, dcp, afl);
        }
        final RewindableStream<VoxelShape> afl2 = new RewindableStream<VoxelShape>((java.util.stream.Stream<VoxelShape>)Stream.concat((Stream)afl.getStream(), (Stream)bru.getBlockCollisions(apx, dcf.expandTowards(dck))));
        return collideBoundingBoxLegacy(dck, dcf, afl2);
    }
    
    public static Vec3 collideBoundingBoxLegacy(final Vec3 dck, AABB dcf, final RewindableStream<VoxelShape> afl) {
        double double4 = dck.x;
        double double5 = dck.y;
        double double6 = dck.z;
        if (double5 != 0.0) {
            double5 = Shapes.collide(Direction.Axis.Y, dcf, afl.getStream(), double5);
            if (double5 != 0.0) {
                dcf = dcf.move(0.0, double5, 0.0);
            }
        }
        final boolean boolean10 = Math.abs(double4) < Math.abs(double6);
        if (boolean10 && double6 != 0.0) {
            double6 = Shapes.collide(Direction.Axis.Z, dcf, afl.getStream(), double6);
            if (double6 != 0.0) {
                dcf = dcf.move(0.0, 0.0, double6);
            }
        }
        if (double4 != 0.0) {
            double4 = Shapes.collide(Direction.Axis.X, dcf, afl.getStream(), double4);
            if (!boolean10 && double4 != 0.0) {
                dcf = dcf.move(double4, 0.0, 0.0);
            }
        }
        if (!boolean10 && double6 != 0.0) {
            double6 = Shapes.collide(Direction.Axis.Z, dcf, afl.getStream(), double6);
        }
        return new Vec3(double4, double5, double6);
    }
    
    public static Vec3 collideBoundingBox(final Vec3 dck, AABB dcf, final LevelReader brw, final CollisionContext dcp, final RewindableStream<VoxelShape> afl) {
        double double6 = dck.x;
        double double7 = dck.y;
        double double8 = dck.z;
        if (double7 != 0.0) {
            double7 = Shapes.collide(Direction.Axis.Y, dcf, brw, double7, dcp, afl.getStream());
            if (double7 != 0.0) {
                dcf = dcf.move(0.0, double7, 0.0);
            }
        }
        final boolean boolean12 = Math.abs(double6) < Math.abs(double8);
        if (boolean12 && double8 != 0.0) {
            double8 = Shapes.collide(Direction.Axis.Z, dcf, brw, double8, dcp, afl.getStream());
            if (double8 != 0.0) {
                dcf = dcf.move(0.0, 0.0, double8);
            }
        }
        if (double6 != 0.0) {
            double6 = Shapes.collide(Direction.Axis.X, dcf, brw, double6, dcp, afl.getStream());
            if (!boolean12 && double6 != 0.0) {
                dcf = dcf.move(double6, 0.0, 0.0);
            }
        }
        if (!boolean12 && double8 != 0.0) {
            double8 = Shapes.collide(Direction.Axis.Z, dcf, brw, double8, dcp, afl.getStream());
        }
        return new Vec3(double6, double7, double8);
    }
    
    protected float nextStep() {
        return (float)((int)this.moveDist + 1);
    }
    
    public void setLocationFromBoundingbox() {
        final AABB dcf2 = this.getBoundingBox();
        this.setPosRaw((dcf2.minX + dcf2.maxX) / 2.0, dcf2.minY, (dcf2.minZ + dcf2.maxZ) / 2.0);
    }
    
    protected SoundEvent getSwimSound() {
        return SoundEvents.GENERIC_SWIM;
    }
    
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.GENERIC_SPLASH;
    }
    
    protected SoundEvent getSwimHighSpeedSplashSound() {
        return SoundEvents.GENERIC_SPLASH;
    }
    
    protected void checkInsideBlocks() {
        final AABB dcf2 = this.getBoundingBox();
        final BlockPos fx3 = new BlockPos(dcf2.minX + 0.001, dcf2.minY + 0.001, dcf2.minZ + 0.001);
        final BlockPos fx4 = new BlockPos(dcf2.maxX - 0.001, dcf2.maxY - 0.001, dcf2.maxZ - 0.001);
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        if (this.level.hasChunksAt(fx3, fx4)) {
            for (int integer6 = fx3.getX(); integer6 <= fx4.getX(); ++integer6) {
                for (int integer7 = fx3.getY(); integer7 <= fx4.getY(); ++integer7) {
                    for (int integer8 = fx3.getZ(); integer8 <= fx4.getZ(); ++integer8) {
                        a5.set(integer6, integer7, integer8);
                        final BlockState cee9 = this.level.getBlockState(a5);
                        try {
                            cee9.entityInside(this.level, a5, this);
                            this.onInsideBlock(cee9);
                        }
                        catch (Throwable throwable10) {
                            final CrashReport l11 = CrashReport.forThrowable(throwable10, "Colliding entity with block");
                            final CrashReportCategory m12 = l11.addCategory("Block being collided with");
                            CrashReportCategory.populateBlockDetails(m12, a5, cee9);
                            throw new ReportedException(l11);
                        }
                    }
                }
            }
        }
    }
    
    protected void onInsideBlock(final BlockState cee) {
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        if (cee.getMaterial().isLiquid()) {
            return;
        }
        final BlockState cee2 = this.level.getBlockState(fx.above());
        final SoundType cab5 = cee2.is(Blocks.SNOW) ? cee2.getSoundType() : cee.getSoundType();
        this.playSound(cab5.getStepSound(), cab5.getVolume() * 0.15f, cab5.getPitch());
    }
    
    protected void playSwimSound(final float float1) {
        this.playSound(this.getSwimSound(), float1, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
    }
    
    protected float playFlySound(final float float1) {
        return 0.0f;
    }
    
    protected boolean makeFlySound() {
        return false;
    }
    
    public void playSound(final SoundEvent adn, final float float2, final float float3) {
        if (!this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), adn, this.getSoundSource(), float2, float3);
        }
    }
    
    public boolean isSilent() {
        return this.entityData.<Boolean>get(Entity.DATA_SILENT);
    }
    
    public void setSilent(final boolean boolean1) {
        this.entityData.<Boolean>set(Entity.DATA_SILENT, boolean1);
    }
    
    public boolean isNoGravity() {
        return this.entityData.<Boolean>get(Entity.DATA_NO_GRAVITY);
    }
    
    public void setNoGravity(final boolean boolean1) {
        this.entityData.<Boolean>set(Entity.DATA_NO_GRAVITY, boolean1);
    }
    
    protected boolean isMovementNoisy() {
        return true;
    }
    
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
        if (boolean2) {
            if (this.fallDistance > 0.0f) {
                cee.getBlock().fallOn(this.level, fx, this, this.fallDistance);
            }
            this.fallDistance = 0.0f;
        }
        else if (double1 < 0.0) {
            this.fallDistance -= (float)double1;
        }
    }
    
    public boolean fireImmune() {
        return this.getType().fireImmune();
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        if (this.isVehicle()) {
            for (final Entity apx5 : this.getPassengers()) {
                apx5.causeFallDamage(float1, float2);
            }
        }
        return false;
    }
    
    public boolean isInWater() {
        return this.wasTouchingWater;
    }
    
    private boolean isInRain() {
        final BlockPos fx2 = this.blockPosition();
        return this.level.isRainingAt(fx2) || this.level.isRainingAt(new BlockPos(fx2.getX(), this.getBoundingBox().maxY, fx2.getZ()));
    }
    
    private boolean isInBubbleColumn() {
        return this.level.getBlockState(this.blockPosition()).is(Blocks.BUBBLE_COLUMN);
    }
    
    public boolean isInWaterOrRain() {
        return this.isInWater() || this.isInRain();
    }
    
    public boolean isInWaterRainOrBubble() {
        return this.isInWater() || this.isInRain() || this.isInBubbleColumn();
    }
    
    public boolean isInWaterOrBubble() {
        return this.isInWater() || this.isInBubbleColumn();
    }
    
    public boolean isUnderWater() {
        return this.wasEyeInWater && this.isInWater();
    }
    
    public void updateSwimming() {
        if (this.isSwimming()) {
            this.setSwimming(this.isSprinting() && this.isInWater() && !this.isPassenger());
        }
        else {
            this.setSwimming(this.isSprinting() && this.isUnderWater() && !this.isPassenger());
        }
    }
    
    protected boolean updateInWaterStateAndDoFluidPushing() {
        this.fluidHeight.clear();
        this.updateInWaterStateAndDoWaterCurrentPushing();
        final double double2 = this.level.dimensionType().ultraWarm() ? 0.007 : 0.0023333333333333335;
        final boolean boolean4 = this.updateFluidHeightAndDoFluidPushing(FluidTags.LAVA, double2);
        return this.isInWater() || boolean4;
    }
    
    void updateInWaterStateAndDoWaterCurrentPushing() {
        if (this.getVehicle() instanceof Boat) {
            this.wasTouchingWater = false;
        }
        else if (this.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014)) {
            if (!this.wasTouchingWater && !this.firstTick) {
                this.doWaterSplashEffect();
            }
            this.fallDistance = 0.0f;
            this.wasTouchingWater = true;
            this.clearFire();
        }
        else {
            this.wasTouchingWater = false;
        }
    }
    
    private void updateFluidOnEyes() {
        this.wasEyeInWater = this.isEyeInFluid(FluidTags.WATER);
        this.fluidOnEyes = null;
        final double double2 = this.getEyeY() - 0.1111111119389534;
        final Entity apx4 = this.getVehicle();
        if (apx4 instanceof Boat) {
            final Boat bhk5 = (Boat)apx4;
            if (!bhk5.isUnderWater() && bhk5.getBoundingBox().maxY >= double2 && bhk5.getBoundingBox().minY <= double2) {
                return;
            }
        }
        final BlockPos fx5 = new BlockPos(this.getX(), double2, this.getZ());
        final FluidState cuu6 = this.level.getFluidState(fx5);
        for (final Tag<Fluid> aej8 : FluidTags.getWrappers()) {
            if (cuu6.is(aej8)) {
                final double double3 = fx5.getY() + cuu6.getHeight(this.level, fx5);
                if (double3 > double2) {
                    this.fluidOnEyes = aej8;
                }
            }
        }
    }
    
    protected void doWaterSplashEffect() {
        final Entity apx2 = (this.isVehicle() && this.getControllingPassenger() != null) ? this.getControllingPassenger() : this;
        final float float3 = (apx2 == this) ? 0.2f : 0.9f;
        final Vec3 dck4 = apx2.getDeltaMovement();
        float float4 = Mth.sqrt(dck4.x * dck4.x * 0.20000000298023224 + dck4.y * dck4.y + dck4.z * dck4.z * 0.20000000298023224) * float3;
        if (float4 > 1.0f) {
            float4 = 1.0f;
        }
        if (float4 < 0.25) {
            this.playSound(this.getSwimSplashSound(), float4, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
        }
        else {
            this.playSound(this.getSwimHighSpeedSplashSound(), float4, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
        }
        final float float5 = (float)Mth.floor(this.getY());
        for (int integer7 = 0; integer7 < 1.0f + this.dimensions.width * 20.0f; ++integer7) {
            final double double8 = (this.random.nextDouble() * 2.0 - 1.0) * this.dimensions.width;
            final double double9 = (this.random.nextDouble() * 2.0 - 1.0) * this.dimensions.width;
            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + double8, float5 + 1.0f, this.getZ() + double9, dck4.x, dck4.y - this.random.nextDouble() * 0.20000000298023224, dck4.z);
        }
        for (int integer7 = 0; integer7 < 1.0f + this.dimensions.width * 20.0f; ++integer7) {
            final double double8 = (this.random.nextDouble() * 2.0 - 1.0) * this.dimensions.width;
            final double double9 = (this.random.nextDouble() * 2.0 - 1.0) * this.dimensions.width;
            this.level.addParticle(ParticleTypes.SPLASH, this.getX() + double8, float5 + 1.0f, this.getZ() + double9, dck4.x, dck4.y, dck4.z);
        }
    }
    
    protected BlockState getBlockStateOn() {
        return this.level.getBlockState(this.getOnPos());
    }
    
    public boolean canSpawnSprintParticle() {
        return this.isSprinting() && !this.isInWater() && !this.isSpectator() && !this.isCrouching() && !this.isInLava() && this.isAlive();
    }
    
    protected void spawnSprintParticle() {
        final int integer2 = Mth.floor(this.getX());
        final int integer3 = Mth.floor(this.getY() - 0.20000000298023224);
        final int integer4 = Mth.floor(this.getZ());
        final BlockPos fx5 = new BlockPos(integer2, integer3, integer4);
        final BlockState cee6 = this.level.getBlockState(fx5);
        if (cee6.getRenderShape() != RenderShape.INVISIBLE) {
            final Vec3 dck7 = this.getDeltaMovement();
            this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, cee6), this.getX() + (this.random.nextDouble() - 0.5) * this.dimensions.width, this.getY() + 0.1, this.getZ() + (this.random.nextDouble() - 0.5) * this.dimensions.width, dck7.x * -4.0, 1.5, dck7.z * -4.0);
        }
    }
    
    public boolean isEyeInFluid(final Tag<Fluid> aej) {
        return this.fluidOnEyes == aej;
    }
    
    public boolean isInLava() {
        return !this.firstTick && this.fluidHeight.getDouble(FluidTags.LAVA) > 0.0;
    }
    
    public void moveRelative(final float float1, final Vec3 dck) {
        final Vec3 dck2 = getInputVector(dck, float1, this.yRot);
        this.setDeltaMovement(this.getDeltaMovement().add(dck2));
    }
    
    private static Vec3 getInputVector(final Vec3 dck, final float float2, final float float3) {
        final double double4 = dck.lengthSqr();
        if (double4 < 1.0E-7) {
            return Vec3.ZERO;
        }
        final Vec3 dck2 = ((double4 > 1.0) ? dck.normalize() : dck).scale(float2);
        final float float4 = Mth.sin(float3 * 0.017453292f);
        final float float5 = Mth.cos(float3 * 0.017453292f);
        return new Vec3(dck2.x * float5 - dck2.z * float4, dck2.y, dck2.z * float5 + dck2.x * float4);
    }
    
    public float getBrightness() {
        final BlockPos.MutableBlockPos a2 = new BlockPos.MutableBlockPos(this.getX(), 0.0, this.getZ());
        if (this.level.hasChunkAt(a2)) {
            a2.setY(Mth.floor(this.getEyeY()));
            return this.level.getBrightness(a2);
        }
        return 0.0f;
    }
    
    public void setLevel(final Level bru) {
        this.level = bru;
    }
    
    public void absMoveTo(final double double1, final double double2, final double double3, final float float4, final float float5) {
        this.absMoveTo(double1, double2, double3);
        this.yRot = float4 % 360.0f;
        this.xRot = Mth.clamp(float5, -90.0f, 90.0f) % 360.0f;
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }
    
    public void absMoveTo(final double double1, final double double2, final double double3) {
        final double double4 = Mth.clamp(double1, -3.0E7, 3.0E7);
        final double double5 = Mth.clamp(double3, -3.0E7, 3.0E7);
        this.setPos(this.xo = double4, this.yo = double2, this.zo = double5);
    }
    
    public void moveTo(final Vec3 dck) {
        this.moveTo(dck.x, dck.y, dck.z);
    }
    
    public void moveTo(final double double1, final double double2, final double double3) {
        this.moveTo(double1, double2, double3, this.yRot, this.xRot);
    }
    
    public void moveTo(final BlockPos fx, final float float2, final float float3) {
        this.moveTo(fx.getX() + 0.5, fx.getY(), fx.getZ() + 0.5, float2, float3);
    }
    
    public void moveTo(final double double1, final double double2, final double double3, final float float4, final float float5) {
        this.setPosAndOldPos(double1, double2, double3);
        this.yRot = float4;
        this.xRot = float5;
        this.reapplyPosition();
    }
    
    public void setPosAndOldPos(final double double1, final double double2, final double double3) {
        this.setPosRaw(double1, double2, double3);
        this.xo = double1;
        this.yo = double2;
        this.zo = double3;
        this.xOld = double1;
        this.yOld = double2;
        this.zOld = double3;
    }
    
    public float distanceTo(final Entity apx) {
        final float float3 = (float)(this.getX() - apx.getX());
        final float float4 = (float)(this.getY() - apx.getY());
        final float float5 = (float)(this.getZ() - apx.getZ());
        return Mth.sqrt(float3 * float3 + float4 * float4 + float5 * float5);
    }
    
    public double distanceToSqr(final double double1, final double double2, final double double3) {
        final double double4 = this.getX() - double1;
        final double double5 = this.getY() - double2;
        final double double6 = this.getZ() - double3;
        return double4 * double4 + double5 * double5 + double6 * double6;
    }
    
    public double distanceToSqr(final Entity apx) {
        return this.distanceToSqr(apx.position());
    }
    
    public double distanceToSqr(final Vec3 dck) {
        final double double3 = this.getX() - dck.x;
        final double double4 = this.getY() - dck.y;
        final double double5 = this.getZ() - dck.z;
        return double3 * double3 + double4 * double4 + double5 * double5;
    }
    
    public void playerTouch(final Player bft) {
    }
    
    public void push(final Entity apx) {
        if (this.isPassengerOfSameVehicle(apx)) {
            return;
        }
        if (apx.noPhysics || this.noPhysics) {
            return;
        }
        double double3 = apx.getX() - this.getX();
        double double4 = apx.getZ() - this.getZ();
        double double5 = Mth.absMax(double3, double4);
        if (double5 >= 0.009999999776482582) {
            double5 = Mth.sqrt(double5);
            double3 /= double5;
            double4 /= double5;
            double double6 = 1.0 / double5;
            if (double6 > 1.0) {
                double6 = 1.0;
            }
            double3 *= double6;
            double4 *= double6;
            double3 *= 0.05000000074505806;
            double4 *= 0.05000000074505806;
            double3 *= 1.0f - this.pushthrough;
            double4 *= 1.0f - this.pushthrough;
            if (!this.isVehicle()) {
                this.push(-double3, 0.0, -double4);
            }
            if (!apx.isVehicle()) {
                apx.push(double3, 0.0, double4);
            }
        }
    }
    
    public void push(final double double1, final double double2, final double double3) {
        this.setDeltaMovement(this.getDeltaMovement().add(double1, double2, double3));
        this.hasImpulse = true;
    }
    
    protected void markHurt() {
        this.hurtMarked = true;
    }
    
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        this.markHurt();
        return false;
    }
    
    public final Vec3 getViewVector(final float float1) {
        return this.calculateViewVector(this.getViewXRot(float1), this.getViewYRot(float1));
    }
    
    public float getViewXRot(final float float1) {
        if (float1 == 1.0f) {
            return this.xRot;
        }
        return Mth.lerp(float1, this.xRotO, this.xRot);
    }
    
    public float getViewYRot(final float float1) {
        if (float1 == 1.0f) {
            return this.yRot;
        }
        return Mth.lerp(float1, this.yRotO, this.yRot);
    }
    
    protected final Vec3 calculateViewVector(final float float1, final float float2) {
        final float float3 = float1 * 0.017453292f;
        final float float4 = -float2 * 0.017453292f;
        final float float5 = Mth.cos(float4);
        final float float6 = Mth.sin(float4);
        final float float7 = Mth.cos(float3);
        final float float8 = Mth.sin(float3);
        return new Vec3(float6 * float7, -float8, float5 * float7);
    }
    
    public final Vec3 getUpVector(final float float1) {
        return this.calculateUpVector(this.getViewXRot(float1), this.getViewYRot(float1));
    }
    
    protected final Vec3 calculateUpVector(final float float1, final float float2) {
        return this.calculateViewVector(float1 - 90.0f, float2);
    }
    
    public final Vec3 getEyePosition(final float float1) {
        if (float1 == 1.0f) {
            return new Vec3(this.getX(), this.getEyeY(), this.getZ());
        }
        final double double3 = Mth.lerp(float1, this.xo, this.getX());
        final double double4 = Mth.lerp(float1, this.yo, this.getY()) + this.getEyeHeight();
        final double double5 = Mth.lerp(float1, this.zo, this.getZ());
        return new Vec3(double3, double4, double5);
    }
    
    public Vec3 getLightProbePosition(final float float1) {
        return this.getEyePosition(float1);
    }
    
    public final Vec3 getPosition(final float float1) {
        final double double3 = Mth.lerp(float1, this.xo, this.getX());
        final double double4 = Mth.lerp(float1, this.yo, this.getY());
        final double double5 = Mth.lerp(float1, this.zo, this.getZ());
        return new Vec3(double3, double4, double5);
    }
    
    public HitResult pick(final double double1, final float float2, final boolean boolean3) {
        final Vec3 dck6 = this.getEyePosition(float2);
        final Vec3 dck7 = this.getViewVector(float2);
        final Vec3 dck8 = dck6.add(dck7.x * double1, dck7.y * double1, dck7.z * double1);
        return this.level.clip(new ClipContext(dck6, dck8, ClipContext.Block.OUTLINE, boolean3 ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this));
    }
    
    public boolean isPickable() {
        return false;
    }
    
    public boolean isPushable() {
        return false;
    }
    
    public void awardKillScore(final Entity apx, final int integer, final DamageSource aph) {
        if (apx instanceof ServerPlayer) {
            CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((ServerPlayer)apx, this, aph);
        }
    }
    
    public boolean shouldRender(final double double1, final double double2, final double double3) {
        final double double4 = this.getX() - double1;
        final double double5 = this.getY() - double2;
        final double double6 = this.getZ() - double3;
        final double double7 = double4 * double4 + double5 * double5 + double6 * double6;
        return this.shouldRenderAtSqrDistance(double7);
    }
    
    public boolean shouldRenderAtSqrDistance(final double double1) {
        double double2 = this.getBoundingBox().getSize();
        if (Double.isNaN(double2)) {
            double2 = 1.0;
        }
        double2 *= 64.0 * Entity.viewScale;
        return double1 < double2 * double2;
    }
    
    public boolean saveAsPassenger(final CompoundTag md) {
        final String string3 = this.getEncodeId();
        if (this.removed || string3 == null) {
            return false;
        }
        md.putString("id", string3);
        this.saveWithoutId(md);
        return true;
    }
    
    public boolean save(final CompoundTag md) {
        return !this.isPassenger() && this.saveAsPassenger(md);
    }
    
    public CompoundTag saveWithoutId(final CompoundTag md) {
        try {
            if (this.vehicle != null) {
                md.put("Pos", (net.minecraft.nbt.Tag)this.newDoubleList(this.vehicle.getX(), this.getY(), this.vehicle.getZ()));
            }
            else {
                md.put("Pos", (net.minecraft.nbt.Tag)this.newDoubleList(this.getX(), this.getY(), this.getZ()));
            }
            final Vec3 dck3 = this.getDeltaMovement();
            md.put("Motion", (net.minecraft.nbt.Tag)this.newDoubleList(dck3.x, dck3.y, dck3.z));
            md.put("Rotation", (net.minecraft.nbt.Tag)this.newFloatList(this.yRot, this.xRot));
            md.putFloat("FallDistance", this.fallDistance);
            md.putShort("Fire", (short)this.remainingFireTicks);
            md.putShort("Air", (short)this.getAirSupply());
            md.putBoolean("OnGround", this.onGround);
            md.putBoolean("Invulnerable", this.invulnerable);
            md.putInt("PortalCooldown", this.portalCooldown);
            md.putUUID("UUID", this.getUUID());
            final Component nr4 = this.getCustomName();
            if (nr4 != null) {
                md.putString("CustomName", Component.Serializer.toJson(nr4));
            }
            if (this.isCustomNameVisible()) {
                md.putBoolean("CustomNameVisible", this.isCustomNameVisible());
            }
            if (this.isSilent()) {
                md.putBoolean("Silent", this.isSilent());
            }
            if (this.isNoGravity()) {
                md.putBoolean("NoGravity", this.isNoGravity());
            }
            if (this.glowing) {
                md.putBoolean("Glowing", this.glowing);
            }
            if (!this.tags.isEmpty()) {
                final ListTag mj5 = new ListTag();
                for (final String string7 : this.tags) {
                    mj5.add(StringTag.valueOf(string7));
                }
                md.put("Tags", (net.minecraft.nbt.Tag)mj5);
            }
            this.addAdditionalSaveData(md);
            if (this.isVehicle()) {
                final ListTag mj5 = new ListTag();
                for (final Entity apx7 : this.getPassengers()) {
                    final CompoundTag md2 = new CompoundTag();
                    if (apx7.saveAsPassenger(md2)) {
                        mj5.add(md2);
                    }
                }
                if (!mj5.isEmpty()) {
                    md.put("Passengers", (net.minecraft.nbt.Tag)mj5);
                }
            }
        }
        catch (Throwable throwable3) {
            final CrashReport l4 = CrashReport.forThrowable(throwable3, "Saving entity NBT");
            final CrashReportCategory m5 = l4.addCategory("Entity being saved");
            this.fillCrashReportCategory(m5);
            throw new ReportedException(l4);
        }
        return md;
    }
    
    public void load(final CompoundTag md) {
        try {
            final ListTag mj3 = md.getList("Pos", 6);
            final ListTag mj4 = md.getList("Motion", 6);
            final ListTag mj5 = md.getList("Rotation", 5);
            final double double6 = mj4.getDouble(0);
            final double double7 = mj4.getDouble(1);
            final double double8 = mj4.getDouble(2);
            this.setDeltaMovement((Math.abs(double6) > 10.0) ? 0.0 : double6, (Math.abs(double7) > 10.0) ? 0.0 : double7, (Math.abs(double8) > 10.0) ? 0.0 : double8);
            this.setPosAndOldPos(mj3.getDouble(0), mj3.getDouble(1), mj3.getDouble(2));
            this.yRot = mj5.getFloat(0);
            this.xRot = mj5.getFloat(1);
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
            this.setYHeadRot(this.yRot);
            this.setYBodyRot(this.yRot);
            this.fallDistance = md.getFloat("FallDistance");
            this.remainingFireTicks = md.getShort("Fire");
            this.setAirSupply(md.getShort("Air"));
            this.onGround = md.getBoolean("OnGround");
            this.invulnerable = md.getBoolean("Invulnerable");
            this.portalCooldown = md.getInt("PortalCooldown");
            if (md.hasUUID("UUID")) {
                this.uuid = md.getUUID("UUID");
                this.stringUUID = this.uuid.toString();
            }
            if (!Double.isFinite(this.getX()) || !Double.isFinite(this.getY()) || !Double.isFinite(this.getZ())) {
                throw new IllegalStateException("Entity has invalid position");
            }
            if (!Double.isFinite((double)this.yRot) || !Double.isFinite((double)this.xRot)) {
                throw new IllegalStateException("Entity has invalid rotation");
            }
            this.reapplyPosition();
            this.setRot(this.yRot, this.xRot);
            if (md.contains("CustomName", 8)) {
                final String string12 = md.getString("CustomName");
                try {
                    this.setCustomName(Component.Serializer.fromJson(string12));
                }
                catch (Exception exception13) {
                    Entity.LOGGER.warn("Failed to parse entity custom name {}", string12, exception13);
                }
            }
            this.setCustomNameVisible(md.getBoolean("CustomNameVisible"));
            this.setSilent(md.getBoolean("Silent"));
            this.setNoGravity(md.getBoolean("NoGravity"));
            this.setGlowing(md.getBoolean("Glowing"));
            if (md.contains("Tags", 9)) {
                this.tags.clear();
                final ListTag mj6 = md.getList("Tags", 8);
                for (int integer13 = Math.min(mj6.size(), 1024), integer14 = 0; integer14 < integer13; ++integer14) {
                    this.tags.add(mj6.getString(integer14));
                }
            }
            this.readAdditionalSaveData(md);
            if (this.repositionEntityAfterLoad()) {
                this.reapplyPosition();
            }
        }
        catch (Throwable throwable3) {
            final CrashReport l4 = CrashReport.forThrowable(throwable3, "Loading entity NBT");
            final CrashReportCategory m5 = l4.addCategory("Entity being loaded");
            this.fillCrashReportCategory(m5);
            throw new ReportedException(l4);
        }
    }
    
    protected boolean repositionEntityAfterLoad() {
        return true;
    }
    
    @Nullable
    protected final String getEncodeId() {
        final EntityType<?> aqb2 = this.getType();
        final ResourceLocation vk3 = EntityType.getKey(aqb2);
        return (!aqb2.canSerialize() || vk3 == null) ? null : vk3.toString();
    }
    
    protected abstract void readAdditionalSaveData(final CompoundTag md);
    
    protected abstract void addAdditionalSaveData(final CompoundTag md);
    
    protected ListTag newDoubleList(final double... arr) {
        final ListTag mj3 = new ListTag();
        for (final double double7 : arr) {
            mj3.add(DoubleTag.valueOf(double7));
        }
        return mj3;
    }
    
    protected ListTag newFloatList(final float... arr) {
        final ListTag mj3 = new ListTag();
        for (final float float7 : arr) {
            mj3.add(FloatTag.valueOf(float7));
        }
        return mj3;
    }
    
    @Nullable
    public ItemEntity spawnAtLocation(final ItemLike brt) {
        return this.spawnAtLocation(brt, 0);
    }
    
    @Nullable
    public ItemEntity spawnAtLocation(final ItemLike brt, final int integer) {
        return this.spawnAtLocation(new ItemStack(brt), (float)integer);
    }
    
    @Nullable
    public ItemEntity spawnAtLocation(final ItemStack bly) {
        return this.spawnAtLocation(bly, 0.0f);
    }
    
    @Nullable
    public ItemEntity spawnAtLocation(final ItemStack bly, final float float2) {
        if (bly.isEmpty()) {
            return null;
        }
        if (this.level.isClientSide) {
            return null;
        }
        final ItemEntity bcs4 = new ItemEntity(this.level, this.getX(), this.getY() + float2, this.getZ(), bly);
        bcs4.setDefaultPickUpDelay();
        this.level.addFreshEntity(bcs4);
        return bcs4;
    }
    
    public boolean isAlive() {
        return !this.removed;
    }
    
    public boolean isInWall() {
        if (this.noPhysics) {
            return false;
        }
        final float float2 = 0.1f;
        final float float3 = this.dimensions.width * 0.8f;
        final AABB dcf4 = AABB.ofSize(float3, 0.10000000149011612, float3).move(this.getX(), this.getEyeY(), this.getZ());
        return this.level.getBlockCollisions(this, dcf4, (BiPredicate<BlockState, BlockPos>)((cee, fx) -> cee.isSuffocating(this.level, fx))).findAny().isPresent();
    }
    
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        return InteractionResult.PASS;
    }
    
    public boolean canCollideWith(final Entity apx) {
        return apx.canBeCollidedWith() && !this.isPassengerOfSameVehicle(apx);
    }
    
    public boolean canBeCollidedWith() {
        return false;
    }
    
    public void rideTick() {
        this.setDeltaMovement(Vec3.ZERO);
        this.tick();
        if (!this.isPassenger()) {
            return;
        }
        this.getVehicle().positionRider(this);
    }
    
    public void positionRider(final Entity apx) {
        this.positionRider(apx, Entity::setPos);
    }
    
    private void positionRider(final Entity apx, final MoveFunction a) {
        if (!this.hasPassenger(apx)) {
            return;
        }
        final double double4 = this.getY() + this.getPassengersRidingOffset() + apx.getMyRidingOffset();
        a.accept(apx, this.getX(), double4, this.getZ());
    }
    
    public void onPassengerTurned(final Entity apx) {
    }
    
    public double getMyRidingOffset() {
        return 0.0;
    }
    
    public double getPassengersRidingOffset() {
        return this.dimensions.height * 0.75;
    }
    
    public boolean startRiding(final Entity apx) {
        return this.startRiding(apx, false);
    }
    
    public boolean showVehicleHealth() {
        return this instanceof LivingEntity;
    }
    
    public boolean startRiding(final Entity apx, final boolean boolean2) {
        for (Entity apx2 = apx; apx2.vehicle != null; apx2 = apx2.vehicle) {
            if (apx2.vehicle == this) {
                return false;
            }
        }
        if (!boolean2 && (!this.canRide(apx) || !apx.canAddPassenger(this))) {
            return false;
        }
        if (this.isPassenger()) {
            this.stopRiding();
        }
        this.setPose(Pose.STANDING);
        (this.vehicle = apx).addPassenger(this);
        return true;
    }
    
    protected boolean canRide(final Entity apx) {
        return !this.isShiftKeyDown() && this.boardingCooldown <= 0;
    }
    
    protected boolean canEnterPose(final Pose aqu) {
        return this.level.noCollision(this, this.getBoundingBoxForPose(aqu).deflate(1.0E-7));
    }
    
    public void ejectPassengers() {
        for (int integer2 = this.passengers.size() - 1; integer2 >= 0; --integer2) {
            ((Entity)this.passengers.get(integer2)).stopRiding();
        }
    }
    
    public void removeVehicle() {
        if (this.vehicle != null) {
            final Entity apx2 = this.vehicle;
            this.vehicle = null;
            apx2.removePassenger(this);
        }
    }
    
    public void stopRiding() {
        this.removeVehicle();
    }
    
    protected void addPassenger(final Entity apx) {
        if (apx.getVehicle() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        }
        if (!this.level.isClientSide && apx instanceof Player && !(this.getControllingPassenger() instanceof Player)) {
            this.passengers.add(0, apx);
        }
        else {
            this.passengers.add(apx);
        }
    }
    
    protected void removePassenger(final Entity apx) {
        if (apx.getVehicle() == this) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        }
        this.passengers.remove(apx);
        apx.boardingCooldown = 60;
    }
    
    protected boolean canAddPassenger(final Entity apx) {
        return this.getPassengers().size() < 1;
    }
    
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        this.setPos(double1, double2, double3);
        this.setRot(float4, float5);
    }
    
    public void lerpHeadTo(final float float1, final int integer) {
        this.setYHeadRot(float1);
    }
    
    public float getPickRadius() {
        return 0.0f;
    }
    
    public Vec3 getLookAngle() {
        return this.calculateViewVector(this.xRot, this.yRot);
    }
    
    public Vec2 getRotationVector() {
        return new Vec2(this.xRot, this.yRot);
    }
    
    public Vec3 getForward() {
        return Vec3.directionFromRotation(this.getRotationVector());
    }
    
    public void handleInsidePortal(final BlockPos fx) {
        if (this.isOnPortalCooldown()) {
            this.setPortalCooldown();
            return;
        }
        if (!this.level.isClientSide && !fx.equals(this.portalEntrancePos)) {
            this.portalEntrancePos = fx.immutable();
        }
        this.isInsidePortal = true;
    }
    
    protected void handleNetherPortal() {
        if (!(this.level instanceof ServerLevel)) {
            return;
        }
        final int integer2 = this.getPortalWaitTime();
        final ServerLevel aag3 = (ServerLevel)this.level;
        if (this.isInsidePortal) {
            final MinecraftServer minecraftServer4 = aag3.getServer();
            final ResourceKey<Level> vj5 = (this.level.dimension() == Level.NETHER) ? Level.OVERWORLD : Level.NETHER;
            final ServerLevel aag4 = minecraftServer4.getLevel(vj5);
            if (aag4 != null && minecraftServer4.isNetherEnabled() && !this.isPassenger() && this.portalTime++ >= integer2) {
                this.level.getProfiler().push("portal");
                this.portalTime = integer2;
                this.setPortalCooldown();
                this.changeDimension(aag4);
                this.level.getProfiler().pop();
            }
            this.isInsidePortal = false;
        }
        else {
            if (this.portalTime > 0) {
                this.portalTime -= 4;
            }
            if (this.portalTime < 0) {
                this.portalTime = 0;
            }
        }
        this.processPortalCooldown();
    }
    
    public int getDimensionChangingDelay() {
        return 300;
    }
    
    public void lerpMotion(final double double1, final double double2, final double double3) {
        this.setDeltaMovement(double1, double2, double3);
    }
    
    public void handleEntityEvent(final byte byte1) {
        switch (byte1) {
            case 53: {
                HoneyBlock.showSlideParticles(this);
                break;
            }
        }
    }
    
    public void animateHurt() {
    }
    
    public Iterable<ItemStack> getHandSlots() {
        return (Iterable<ItemStack>)Entity.EMPTY_LIST;
    }
    
    public Iterable<ItemStack> getArmorSlots() {
        return (Iterable<ItemStack>)Entity.EMPTY_LIST;
    }
    
    public Iterable<ItemStack> getAllSlots() {
        return (Iterable<ItemStack>)Iterables.concat((Iterable)this.getHandSlots(), (Iterable)this.getArmorSlots());
    }
    
    public void setItemSlot(final EquipmentSlot aqc, final ItemStack bly) {
    }
    
    public boolean isOnFire() {
        final boolean boolean2 = this.level != null && this.level.isClientSide;
        return !this.fireImmune() && (this.remainingFireTicks > 0 || (boolean2 && this.getSharedFlag(0)));
    }
    
    public boolean isPassenger() {
        return this.getVehicle() != null;
    }
    
    public boolean isVehicle() {
        return !this.getPassengers().isEmpty();
    }
    
    public boolean rideableUnderWater() {
        return true;
    }
    
    public void setShiftKeyDown(final boolean boolean1) {
        this.setSharedFlag(1, boolean1);
    }
    
    public boolean isShiftKeyDown() {
        return this.getSharedFlag(1);
    }
    
    public boolean isSteppingCarefully() {
        return this.isShiftKeyDown();
    }
    
    public boolean isSuppressingBounce() {
        return this.isShiftKeyDown();
    }
    
    public boolean isDiscrete() {
        return this.isShiftKeyDown();
    }
    
    public boolean isDescending() {
        return this.isShiftKeyDown();
    }
    
    public boolean isCrouching() {
        return this.getPose() == Pose.CROUCHING;
    }
    
    public boolean isSprinting() {
        return this.getSharedFlag(3);
    }
    
    public void setSprinting(final boolean boolean1) {
        this.setSharedFlag(3, boolean1);
    }
    
    public boolean isSwimming() {
        return this.getSharedFlag(4);
    }
    
    public boolean isVisuallySwimming() {
        return this.getPose() == Pose.SWIMMING;
    }
    
    public boolean isVisuallyCrawling() {
        return this.isVisuallySwimming() && !this.isInWater();
    }
    
    public void setSwimming(final boolean boolean1) {
        this.setSharedFlag(4, boolean1);
    }
    
    public boolean isGlowing() {
        return this.glowing || (this.level.isClientSide && this.getSharedFlag(6));
    }
    
    public void setGlowing(final boolean boolean1) {
        this.glowing = boolean1;
        if (!this.level.isClientSide) {
            this.setSharedFlag(6, this.glowing);
        }
    }
    
    public boolean isInvisible() {
        return this.getSharedFlag(5);
    }
    
    public boolean isInvisibleTo(final Player bft) {
        if (bft.isSpectator()) {
            return false;
        }
        final Team ddm3 = this.getTeam();
        return (ddm3 == null || bft == null || bft.getTeam() != ddm3 || !ddm3.canSeeFriendlyInvisibles()) && this.isInvisible();
    }
    
    @Nullable
    public Team getTeam() {
        return this.level.getScoreboard().getPlayersTeam(this.getScoreboardName());
    }
    
    public boolean isAlliedTo(final Entity apx) {
        return this.isAlliedTo(apx.getTeam());
    }
    
    public boolean isAlliedTo(final Team ddm) {
        return this.getTeam() != null && this.getTeam().isAlliedTo(ddm);
    }
    
    public void setInvisible(final boolean boolean1) {
        this.setSharedFlag(5, boolean1);
    }
    
    protected boolean getSharedFlag(final int integer) {
        return (this.entityData.<Byte>get(Entity.DATA_SHARED_FLAGS_ID) & 1 << integer) != 0x0;
    }
    
    protected void setSharedFlag(final int integer, final boolean boolean2) {
        final byte byte4 = this.entityData.<Byte>get(Entity.DATA_SHARED_FLAGS_ID);
        if (boolean2) {
            this.entityData.<Byte>set(Entity.DATA_SHARED_FLAGS_ID, (byte)(byte4 | 1 << integer));
        }
        else {
            this.entityData.<Byte>set(Entity.DATA_SHARED_FLAGS_ID, (byte)(byte4 & ~(1 << integer)));
        }
    }
    
    public int getMaxAirSupply() {
        return 300;
    }
    
    public int getAirSupply() {
        return this.entityData.<Integer>get(Entity.DATA_AIR_SUPPLY_ID);
    }
    
    public void setAirSupply(final int integer) {
        this.entityData.<Integer>set(Entity.DATA_AIR_SUPPLY_ID, integer);
    }
    
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
        this.setRemainingFireTicks(this.remainingFireTicks + 1);
        if (this.remainingFireTicks == 0) {
            this.setSecondsOnFire(8);
        }
        this.hurt(DamageSource.LIGHTNING_BOLT, 5.0f);
    }
    
    public void onAboveBubbleCol(final boolean boolean1) {
        final Vec3 dck3 = this.getDeltaMovement();
        double double4;
        if (boolean1) {
            double4 = Math.max(-0.9, dck3.y - 0.03);
        }
        else {
            double4 = Math.min(1.8, dck3.y + 0.1);
        }
        this.setDeltaMovement(dck3.x, double4, dck3.z);
    }
    
    public void onInsideBubbleColumn(final boolean boolean1) {
        final Vec3 dck3 = this.getDeltaMovement();
        double double4;
        if (boolean1) {
            double4 = Math.max(-0.3, dck3.y - 0.03);
        }
        else {
            double4 = Math.min(0.7, dck3.y + 0.06);
        }
        this.setDeltaMovement(dck3.x, double4, dck3.z);
        this.fallDistance = 0.0f;
    }
    
    public void killed(final ServerLevel aag, final LivingEntity aqj) {
    }
    
    protected void moveTowardsClosestSpace(final double double1, final double double2, final double double3) {
        final BlockPos fx8 = new BlockPos(double1, double2, double3);
        final Vec3 dck9 = new Vec3(double1 - fx8.getX(), double2 - fx8.getY(), double3 - fx8.getZ());
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        Direction gc11 = Direction.UP;
        double double4 = Double.MAX_VALUE;
        for (final Direction gc12 : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
            a10.setWithOffset(fx8, gc12);
            if (!this.level.getBlockState(a10).isCollisionShapeFullBlock(this.level, a10)) {
                final double double5 = dck9.get(gc12.getAxis());
                final double double6 = (gc12.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0 - double5) : double5;
                if (double6 < double4) {
                    double4 = double6;
                    gc11 = gc12;
                }
            }
        }
        final float float14 = this.random.nextFloat() * 0.2f + 0.1f;
        final float float15 = (float)gc11.getAxisDirection().getStep();
        final Vec3 dck10 = this.getDeltaMovement().scale(0.75);
        if (gc11.getAxis() == Direction.Axis.X) {
            this.setDeltaMovement(float15 * float14, dck10.y, dck10.z);
        }
        else if (gc11.getAxis() == Direction.Axis.Y) {
            this.setDeltaMovement(dck10.x, float15 * float14, dck10.z);
        }
        else if (gc11.getAxis() == Direction.Axis.Z) {
            this.setDeltaMovement(dck10.x, dck10.y, float15 * float14);
        }
    }
    
    public void makeStuckInBlock(final BlockState cee, final Vec3 dck) {
        this.fallDistance = 0.0f;
        this.stuckSpeedMultiplier = dck;
    }
    
    private static Component removeAction(final Component nr) {
        final MutableComponent nx2 = nr.plainCopy().setStyle(nr.getStyle().withClickEvent(null));
        for (final Component nr2 : nr.getSiblings()) {
            nx2.append(removeAction(nr2));
        }
        return nx2;
    }
    
    public Component getName() {
        final Component nr2 = this.getCustomName();
        if (nr2 != null) {
            return removeAction(nr2);
        }
        return this.getTypeName();
    }
    
    protected Component getTypeName() {
        return this.type.getDescription();
    }
    
    public boolean is(final Entity apx) {
        return this == apx;
    }
    
    public float getYHeadRot() {
        return 0.0f;
    }
    
    public void setYHeadRot(final float float1) {
    }
    
    public void setYBodyRot(final float float1) {
    }
    
    public boolean isAttackable() {
        return true;
    }
    
    public boolean skipAttackInteraction(final Entity apx) {
        return false;
    }
    
    public String toString() {
        return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { this.getClass().getSimpleName(), this.getName().getString(), this.id, (this.level == null) ? "~NULL~" : this.level.toString(), this.getX(), this.getY(), this.getZ() });
    }
    
    public boolean isInvulnerableTo(final DamageSource aph) {
        return this.invulnerable && aph != DamageSource.OUT_OF_WORLD && !aph.isCreativePlayer();
    }
    
    public boolean isInvulnerable() {
        return this.invulnerable;
    }
    
    public void setInvulnerable(final boolean boolean1) {
        this.invulnerable = boolean1;
    }
    
    public void copyPosition(final Entity apx) {
        this.moveTo(apx.getX(), apx.getY(), apx.getZ(), apx.yRot, apx.xRot);
    }
    
    public void restoreFrom(final Entity apx) {
        final CompoundTag md3 = apx.saveWithoutId(new CompoundTag());
        md3.remove("Dimension");
        this.load(md3);
        this.portalCooldown = apx.portalCooldown;
        this.portalEntrancePos = apx.portalEntrancePos;
    }
    
    @Nullable
    public Entity changeDimension(final ServerLevel aag) {
        if (!(this.level instanceof ServerLevel) || this.removed) {
            return null;
        }
        this.level.getProfiler().push("changeDimension");
        this.unRide();
        this.level.getProfiler().push("reposition");
        final PortalInfo cxj3 = this.findDimensionEntryPoint(aag);
        if (cxj3 == null) {
            return null;
        }
        this.level.getProfiler().popPush("reloading");
        final Entity apx4 = (Entity)this.getType().create(aag);
        if (apx4 != null) {
            apx4.restoreFrom(this);
            apx4.moveTo(cxj3.pos.x, cxj3.pos.y, cxj3.pos.z, cxj3.yRot, apx4.xRot);
            apx4.setDeltaMovement(cxj3.speed);
            aag.addFromAnotherDimension(apx4);
            if (aag.dimension() == Level.END) {
                ServerLevel.makeObsidianPlatform(aag);
            }
        }
        this.removeAfterChangingDimensions();
        this.level.getProfiler().pop();
        ((ServerLevel)this.level).resetEmptyTime();
        aag.resetEmptyTime();
        this.level.getProfiler().pop();
        return apx4;
    }
    
    protected void removeAfterChangingDimensions() {
        this.removed = true;
    }
    
    @Nullable
    protected PortalInfo findDimensionEntryPoint(final ServerLevel aag) {
        final boolean boolean3 = this.level.dimension() == Level.END && aag.dimension() == Level.OVERWORLD;
        final boolean boolean4 = aag.dimension() == Level.END;
        if (boolean3 || boolean4) {
            BlockPos fx5;
            if (boolean4) {
                fx5 = ServerLevel.END_SPAWN_POINT;
            }
            else {
                fx5 = aag.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, aag.getSharedSpawnPos());
            }
            return new PortalInfo(new Vec3(fx5.getX() + 0.5, fx5.getY(), fx5.getZ() + 0.5), this.getDeltaMovement(), this.yRot, this.xRot);
        }
        final boolean boolean5 = aag.dimension() == Level.NETHER;
        if (this.level.dimension() != Level.NETHER && !boolean5) {
            return null;
        }
        final WorldBorder cfr6 = aag.getWorldBorder();
        final double double7 = Math.max(-2.9999872E7, cfr6.getMinX() + 16.0);
        final double double8 = Math.max(-2.9999872E7, cfr6.getMinZ() + 16.0);
        final double double9 = Math.min(2.9999872E7, cfr6.getMaxX() - 16.0);
        final double double10 = Math.min(2.9999872E7, cfr6.getMaxZ() - 16.0);
        final double double11 = DimensionType.getTeleportationScale(this.level.dimensionType(), aag.dimensionType());
        final BlockPos fx6 = new BlockPos(Mth.clamp(this.getX() * double11, double7, double9), this.getY(), Mth.clamp(this.getZ() * double11, double8, double10));
        return (PortalInfo)this.getExitPortal(aag, fx6, boolean5).map(a -> {
            final BlockState cee6 = this.level.getBlockState(this.portalEntrancePos);
            Direction.Axis a2;
            Vec3 dck5;
            if (cee6.<Direction.Axis>hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
                a2 = cee6.<Direction.Axis>getValue(BlockStateProperties.HORIZONTAL_AXIS);
                final BlockUtil.FoundRectangle a3 = BlockUtil.getLargestRectangleAround(this.portalEntrancePos, a2, 21, Direction.Axis.Y, 21, (Predicate<BlockPos>)(fx -> this.level.getBlockState(fx) == cee6));
                dck5 = this.getRelativePortalPosition(a2, a3);
            }
            else {
                a2 = Direction.Axis.X;
                dck5 = new Vec3(0.5, 0.0, 0.0);
            }
            return PortalShape.createPortalInfo(aag, a, a2, dck5, this.getDimensions(this.getPose()), this.getDeltaMovement(), this.yRot, this.xRot);
        }).orElse(null);
    }
    
    protected Vec3 getRelativePortalPosition(final Direction.Axis a, final BlockUtil.FoundRectangle a) {
        return PortalShape.getRelativePosition(a, a, this.position(), this.getDimensions(this.getPose()));
    }
    
    protected Optional<BlockUtil.FoundRectangle> getExitPortal(final ServerLevel aag, final BlockPos fx, final boolean boolean3) {
        return aag.getPortalForcer().findPortalAround(fx, boolean3);
    }
    
    public boolean canChangeDimensions() {
        return true;
    }
    
    public float getBlockExplosionResistance(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final FluidState cuu, final float float6) {
        return float6;
    }
    
    public boolean shouldBlockExplode(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final float float5) {
        return true;
    }
    
    public int getMaxFallDistance() {
        return 3;
    }
    
    public boolean isIgnoringBlockTriggers() {
        return false;
    }
    
    public void fillCrashReportCategory(final CrashReportCategory m) {
        m.setDetail("Entity Type", (CrashReportDetail<String>)(() -> new StringBuilder().append(EntityType.getKey(this.getType())).append(" (").append(this.getClass().getCanonicalName()).append(")").toString()));
        m.setDetail("Entity ID", this.id);
        m.setDetail("Entity Name", (CrashReportDetail<String>)(() -> this.getName().getString()));
        m.setDetail("Entity's Exact location", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { this.getX(), this.getY(), this.getZ() }));
        m.setDetail("Entity's Block location", CrashReportCategory.formatLocation(Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ())));
        final Vec3 dck3 = this.getDeltaMovement();
        m.setDetail("Entity's Momentum", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { dck3.x, dck3.y, dck3.z }));
        m.setDetail("Entity's Passengers", (CrashReportDetail<String>)(() -> this.getPassengers().toString()));
        m.setDetail("Entity's Vehicle", (CrashReportDetail<String>)(() -> this.getVehicle().toString()));
    }
    
    public boolean displayFireAnimation() {
        return this.isOnFire() && !this.isSpectator();
    }
    
    public void setUUID(final UUID uUID) {
        this.uuid = uUID;
        this.stringUUID = this.uuid.toString();
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public String getStringUUID() {
        return this.stringUUID;
    }
    
    public String getScoreboardName() {
        return this.stringUUID;
    }
    
    public boolean isPushedByFluid() {
        return true;
    }
    
    public static double getViewScale() {
        return Entity.viewScale;
    }
    
    public static void setViewScale(final double double1) {
        Entity.viewScale = double1;
    }
    
    public Component getDisplayName() {
        return PlayerTeam.formatNameForTeam(this.getTeam(), this.getName()).withStyle((UnaryOperator<Style>)(ob -> ob.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID())));
    }
    
    public void setCustomName(@Nullable final Component nr) {
        this.entityData.<Optional<Component>>set(Entity.DATA_CUSTOM_NAME, (Optional<Component>)Optional.ofNullable(nr));
    }
    
    @Nullable
    public Component getCustomName() {
        return (Component)this.entityData.<Optional<Component>>get(Entity.DATA_CUSTOM_NAME).orElse(null);
    }
    
    public boolean hasCustomName() {
        return this.entityData.<Optional<Component>>get(Entity.DATA_CUSTOM_NAME).isPresent();
    }
    
    public void setCustomNameVisible(final boolean boolean1) {
        this.entityData.<Boolean>set(Entity.DATA_CUSTOM_NAME_VISIBLE, boolean1);
    }
    
    public boolean isCustomNameVisible() {
        return this.entityData.<Boolean>get(Entity.DATA_CUSTOM_NAME_VISIBLE);
    }
    
    public final void teleportToWithTicket(final double double1, final double double2, final double double3) {
        if (!(this.level instanceof ServerLevel)) {
            return;
        }
        final ChunkPos bra8 = new ChunkPos(new BlockPos(double1, double2, double3));
        ((ServerLevel)this.level).getChunkSource().<Integer>addRegionTicket(TicketType.POST_TELEPORT, bra8, 0, this.getId());
        this.level.getChunk(bra8.x, bra8.z);
        this.teleportTo(double1, double2, double3);
    }
    
    public void teleportTo(final double double1, final double double2, final double double3) {
        if (!(this.level instanceof ServerLevel)) {
            return;
        }
        final ServerLevel aag8 = (ServerLevel)this.level;
        this.moveTo(double1, double2, double3, this.yRot, this.xRot);
        this.getSelfAndPassengers().forEach(apx -> {
            aag8.updateChunkPos(apx);
            apx.forceChunkAddition = true;
            for (final Entity apx2 : apx.passengers) {
                apx.positionRider(apx2, Entity::moveTo);
            }
        });
    }
    
    public boolean shouldShowName() {
        return this.isCustomNameVisible();
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Entity.DATA_POSE.equals(us)) {
            this.refreshDimensions();
        }
    }
    
    public void refreshDimensions() {
        final EntityDimensions apy2 = this.dimensions;
        final Pose aqu3 = this.getPose();
        final EntityDimensions apy3 = this.getDimensions(aqu3);
        this.dimensions = apy3;
        this.eyeHeight = this.getEyeHeight(aqu3, apy3);
        if (apy3.width < apy2.width) {
            final double double5 = apy3.width / 2.0;
            this.setBoundingBox(new AABB(this.getX() - double5, this.getY(), this.getZ() - double5, this.getX() + double5, this.getY() + apy3.height, this.getZ() + double5));
            return;
        }
        final AABB dcf5 = this.getBoundingBox();
        this.setBoundingBox(new AABB(dcf5.minX, dcf5.minY, dcf5.minZ, dcf5.minX + apy3.width, dcf5.minY + apy3.height, dcf5.minZ + apy3.width));
        if (apy3.width > apy2.width && !this.firstTick && !this.level.isClientSide) {
            final float float6 = apy2.width - apy3.width;
            this.move(MoverType.SELF, new Vec3(float6, 0.0, float6));
        }
    }
    
    public Direction getDirection() {
        return Direction.fromYRot(this.yRot);
    }
    
    public Direction getMotionDirection() {
        return this.getDirection();
    }
    
    protected HoverEvent createHoverEvent() {
        return new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_ENTITY, (T)new HoverEvent.EntityTooltipInfo(this.getType(), this.getUUID(), this.getName()));
    }
    
    public boolean broadcastToPlayer(final ServerPlayer aah) {
        return true;
    }
    
    public AABB getBoundingBox() {
        return this.bb;
    }
    
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox();
    }
    
    protected AABB getBoundingBoxForPose(final Pose aqu) {
        final EntityDimensions apy3 = this.getDimensions(aqu);
        final float float4 = apy3.width / 2.0f;
        final Vec3 dck5 = new Vec3(this.getX() - float4, this.getY(), this.getZ() - float4);
        final Vec3 dck6 = new Vec3(this.getX() + float4, this.getY() + apy3.height, this.getZ() + float4);
        return new AABB(dck5, dck6);
    }
    
    public void setBoundingBox(final AABB dcf) {
        this.bb = dcf;
    }
    
    protected float getEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.85f;
    }
    
    public float getEyeHeight(final Pose aqu) {
        return this.getEyeHeight(aqu, this.getDimensions(aqu));
    }
    
    public final float getEyeHeight() {
        return this.eyeHeight;
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    public boolean setSlot(final int integer, final ItemStack bly) {
        return false;
    }
    
    public void sendMessage(final Component nr, final UUID uUID) {
    }
    
    public Level getCommandSenderWorld() {
        return this.level;
    }
    
    @Nullable
    public MinecraftServer getServer() {
        return this.level.getServer();
    }
    
    public InteractionResult interactAt(final Player bft, final Vec3 dck, final InteractionHand aoq) {
        return InteractionResult.PASS;
    }
    
    public boolean ignoreExplosion() {
        return false;
    }
    
    public void doEnchantDamageEffects(final LivingEntity aqj, final Entity apx) {
        if (apx instanceof LivingEntity) {
            EnchantmentHelper.doPostHurtEffects((LivingEntity)apx, aqj);
        }
        EnchantmentHelper.doPostDamageEffects(aqj, apx);
    }
    
    public void startSeenByPlayer(final ServerPlayer aah) {
    }
    
    public void stopSeenByPlayer(final ServerPlayer aah) {
    }
    
    public float rotate(final Rotation bzj) {
        final float float3 = Mth.wrapDegrees(this.yRot);
        switch (bzj) {
            case CLOCKWISE_180: {
                return float3 + 180.0f;
            }
            case COUNTERCLOCKWISE_90: {
                return float3 + 270.0f;
            }
            case CLOCKWISE_90: {
                return float3 + 90.0f;
            }
            default: {
                return float3;
            }
        }
    }
    
    public float mirror(final Mirror byd) {
        final float float3 = Mth.wrapDegrees(this.yRot);
        switch (byd) {
            case LEFT_RIGHT: {
                return -float3;
            }
            case FRONT_BACK: {
                return 180.0f - float3;
            }
            default: {
                return float3;
            }
        }
    }
    
    public boolean onlyOpCanSetNbt() {
        return false;
    }
    
    public boolean checkAndResetForcedChunkAdditionFlag() {
        final boolean boolean2 = this.forceChunkAddition;
        this.forceChunkAddition = false;
        return boolean2;
    }
    
    public boolean checkAndResetUpdateChunkPos() {
        final boolean boolean2 = this.movedSinceLastChunkCheck;
        this.movedSinceLastChunkCheck = false;
        return boolean2;
    }
    
    @Nullable
    public Entity getControllingPassenger() {
        return null;
    }
    
    public List<Entity> getPassengers() {
        if (this.passengers.isEmpty()) {
            return (List<Entity>)Collections.emptyList();
        }
        return (List<Entity>)Lists.newArrayList((Iterable)this.passengers);
    }
    
    public boolean hasPassenger(final Entity apx) {
        for (final Entity apx2 : this.getPassengers()) {
            if (apx2.equals(apx)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasPassenger(final Class<? extends Entity> class1) {
        for (final Entity apx4 : this.getPassengers()) {
            if (class1.isAssignableFrom(apx4.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    public Collection<Entity> getIndirectPassengers() {
        final Set<Entity> set2 = (Set<Entity>)Sets.newHashSet();
        for (final Entity apx4 : this.getPassengers()) {
            set2.add(apx4);
            apx4.fillIndirectPassengers(false, set2);
        }
        return (Collection<Entity>)set2;
    }
    
    public Stream<Entity> getSelfAndPassengers() {
        return (Stream<Entity>)Stream.concat(Stream.of(this), this.passengers.stream().flatMap(Entity::getSelfAndPassengers));
    }
    
    public boolean hasOnePlayerPassenger() {
        final Set<Entity> set2 = (Set<Entity>)Sets.newHashSet();
        this.fillIndirectPassengers(true, set2);
        return set2.size() == 1;
    }
    
    private void fillIndirectPassengers(final boolean boolean1, final Set<Entity> set) {
        for (final Entity apx5 : this.getPassengers()) {
            if (!boolean1 || ServerPlayer.class.isAssignableFrom(apx5.getClass())) {
                set.add(apx5);
            }
            apx5.fillIndirectPassengers(boolean1, set);
        }
    }
    
    public Entity getRootVehicle() {
        Entity apx2;
        for (apx2 = this; apx2.isPassenger(); apx2 = apx2.getVehicle()) {}
        return apx2;
    }
    
    public boolean isPassengerOfSameVehicle(final Entity apx) {
        return this.getRootVehicle() == apx.getRootVehicle();
    }
    
    public boolean hasIndirectPassenger(final Entity apx) {
        for (final Entity apx2 : this.getPassengers()) {
            if (apx2.equals(apx)) {
                return true;
            }
            if (apx2.hasIndirectPassenger(apx)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isControlledByLocalInstance() {
        final Entity apx2 = this.getControllingPassenger();
        if (apx2 instanceof Player) {
            return ((Player)apx2).isLocalPlayer();
        }
        return !this.level.isClientSide;
    }
    
    protected static Vec3 getCollisionHorizontalEscapeVector(final double double1, final double double2, final float float3) {
        final double double3 = (double1 + double2 + 9.999999747378752E-6) / 2.0;
        final float float4 = -Mth.sin(float3 * 0.017453292f);
        final float float5 = Mth.cos(float3 * 0.017453292f);
        final float float6 = Math.max(Math.abs(float4), Math.abs(float5));
        return new Vec3(float4 * double3 / float6, 0.0, float5 * double3 / float6);
    }
    
    public Vec3 getDismountLocationForPassenger(final LivingEntity aqj) {
        return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
    }
    
    @Nullable
    public Entity getVehicle() {
        return this.vehicle;
    }
    
    public PushReaction getPistonPushReaction() {
        return PushReaction.NORMAL;
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }
    
    protected int getFireImmuneTicks() {
        return 1;
    }
    
    public CommandSourceStack createCommandSourceStack() {
        return new CommandSourceStack(this, this.position(), this.getRotationVector(), (this.level instanceof ServerLevel) ? ((ServerLevel)this.level) : null, this.getPermissionLevel(), this.getName().getString(), this.getDisplayName(), this.level.getServer(), this);
    }
    
    protected int getPermissionLevel() {
        return 0;
    }
    
    public boolean hasPermissions(final int integer) {
        return this.getPermissionLevel() >= integer;
    }
    
    public boolean acceptsSuccess() {
        return this.level.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK);
    }
    
    public boolean acceptsFailure() {
        return true;
    }
    
    public boolean shouldInformAdmins() {
        return true;
    }
    
    public void lookAt(final EntityAnchorArgument.Anchor a, final Vec3 dck) {
        final Vec3 dck2 = a.apply(this);
        final double double5 = dck.x - dck2.x;
        final double double6 = dck.y - dck2.y;
        final double double7 = dck.z - dck2.z;
        final double double8 = Mth.sqrt(double5 * double5 + double7 * double7);
        this.xRot = Mth.wrapDegrees((float)(-(Mth.atan2(double6, double8) * 57.2957763671875)));
        this.setYHeadRot(this.yRot = Mth.wrapDegrees((float)(Mth.atan2(double7, double5) * 57.2957763671875) - 90.0f));
        this.xRotO = this.xRot;
        this.yRotO = this.yRot;
    }
    
    public boolean updateFluidHeightAndDoFluidPushing(final Tag<Fluid> aej, final double double2) {
        final AABB dcf5 = this.getBoundingBox().deflate(0.001);
        final int integer6 = Mth.floor(dcf5.minX);
        final int integer7 = Mth.ceil(dcf5.maxX);
        final int integer8 = Mth.floor(dcf5.minY);
        final int integer9 = Mth.ceil(dcf5.maxY);
        final int integer10 = Mth.floor(dcf5.minZ);
        final int integer11 = Mth.ceil(dcf5.maxZ);
        if (!this.level.hasChunksAt(integer6, integer8, integer10, integer7, integer9, integer11)) {
            return false;
        }
        double double3 = 0.0;
        final boolean boolean14 = this.isPushedByFluid();
        boolean boolean15 = false;
        Vec3 dck16 = Vec3.ZERO;
        int integer12 = 0;
        final BlockPos.MutableBlockPos a18 = new BlockPos.MutableBlockPos();
        for (int integer13 = integer6; integer13 < integer7; ++integer13) {
            for (int integer14 = integer8; integer14 < integer9; ++integer14) {
                for (int integer15 = integer10; integer15 < integer11; ++integer15) {
                    a18.set(integer13, integer14, integer15);
                    final FluidState cuu22 = this.level.getFluidState(a18);
                    if (cuu22.is(aej)) {
                        final double double4 = integer14 + cuu22.getHeight(this.level, a18);
                        if (double4 >= dcf5.minY) {
                            boolean15 = true;
                            double3 = Math.max(double4 - dcf5.minY, double3);
                            if (boolean14) {
                                Vec3 dck17 = cuu22.getFlow(this.level, a18);
                                if (double3 < 0.4) {
                                    dck17 = dck17.scale(double3);
                                }
                                dck16 = dck16.add(dck17);
                                ++integer12;
                            }
                        }
                    }
                }
            }
        }
        if (dck16.length() > 0.0) {
            if (integer12 > 0) {
                dck16 = dck16.scale(1.0 / integer12);
            }
            if (!(this instanceof Player)) {
                dck16 = dck16.normalize();
            }
            final Vec3 dck18 = this.getDeltaMovement();
            dck16 = dck16.scale(double2 * 1.0);
            final double double5 = 0.003;
            if (Math.abs(dck18.x) < 0.003 && Math.abs(dck18.z) < 0.003 && dck16.length() < 0.0045000000000000005) {
                dck16 = dck16.normalize().scale(0.0045000000000000005);
            }
            this.setDeltaMovement(this.getDeltaMovement().add(dck16));
        }
        this.fluidHeight.put(aej, double3);
        return boolean15;
    }
    
    public double getFluidHeight(final Tag<Fluid> aej) {
        return this.fluidHeight.getDouble(aej);
    }
    
    public double getFluidJumpThreshold() {
        return (this.getEyeHeight() < 0.4) ? 0.0 : 0.4;
    }
    
    public final float getBbWidth() {
        return this.dimensions.width;
    }
    
    public final float getBbHeight() {
        return this.dimensions.height;
    }
    
    public abstract Packet<?> getAddEntityPacket();
    
    public EntityDimensions getDimensions(final Pose aqu) {
        return this.type.getDimensions();
    }
    
    public Vec3 position() {
        return this.position;
    }
    
    public BlockPos blockPosition() {
        return this.blockPosition;
    }
    
    public Vec3 getDeltaMovement() {
        return this.deltaMovement;
    }
    
    public void setDeltaMovement(final Vec3 dck) {
        this.deltaMovement = dck;
    }
    
    public void setDeltaMovement(final double double1, final double double2, final double double3) {
        this.setDeltaMovement(new Vec3(double1, double2, double3));
    }
    
    public final double getX() {
        return this.position.x;
    }
    
    public double getX(final double double1) {
        return this.position.x + this.getBbWidth() * double1;
    }
    
    public double getRandomX(final double double1) {
        return this.getX((2.0 * this.random.nextDouble() - 1.0) * double1);
    }
    
    public final double getY() {
        return this.position.y;
    }
    
    public double getY(final double double1) {
        return this.position.y + this.getBbHeight() * double1;
    }
    
    public double getRandomY() {
        return this.getY(this.random.nextDouble());
    }
    
    public double getEyeY() {
        return this.position.y + this.eyeHeight;
    }
    
    public final double getZ() {
        return this.position.z;
    }
    
    public double getZ(final double double1) {
        return this.position.z + this.getBbWidth() * double1;
    }
    
    public double getRandomZ(final double double1) {
        return this.getZ((2.0 * this.random.nextDouble() - 1.0) * double1);
    }
    
    public void setPosRaw(final double double1, final double double2, final double double3) {
        if (this.position.x != double1 || this.position.y != double2 || this.position.z != double3) {
            this.position = new Vec3(double1, double2, double3);
            final int integer8 = Mth.floor(double1);
            final int integer9 = Mth.floor(double2);
            final int integer10 = Mth.floor(double3);
            if (integer8 != this.blockPosition.getX() || integer9 != this.blockPosition.getY() || integer10 != this.blockPosition.getZ()) {
                this.blockPosition = new BlockPos(integer8, integer9, integer10);
            }
            this.movedSinceLastChunkCheck = true;
        }
    }
    
    public void checkDespawn() {
    }
    
    public Vec3 getRopeHoldPosition(final float float1) {
        return this.getPosition(float1).add(0.0, this.eyeHeight * 0.7, 0.0);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ENTITY_COUNTER = new AtomicInteger();
        EMPTY_LIST = Collections.emptyList();
        INITIAL_AABB = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Entity.viewScale = 1.0;
        DATA_SHARED_FLAGS_ID = SynchedEntityData.<Byte>defineId(Entity.class, EntityDataSerializers.BYTE);
        DATA_AIR_SUPPLY_ID = SynchedEntityData.<Integer>defineId(Entity.class, EntityDataSerializers.INT);
        DATA_CUSTOM_NAME = SynchedEntityData.<Optional<Component>>defineId(Entity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
        DATA_CUSTOM_NAME_VISIBLE = SynchedEntityData.<Boolean>defineId(Entity.class, EntityDataSerializers.BOOLEAN);
        DATA_SILENT = SynchedEntityData.<Boolean>defineId(Entity.class, EntityDataSerializers.BOOLEAN);
        DATA_NO_GRAVITY = SynchedEntityData.<Boolean>defineId(Entity.class, EntityDataSerializers.BOOLEAN);
        DATA_POSE = SynchedEntityData.<Pose>defineId(Entity.class, EntityDataSerializers.POSE);
    }
    
    @FunctionalInterface
    public interface MoveFunction {
        void accept(final Entity apx, final double double2, final double double3, final double double4);
    }
}
