package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.HitResult;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.EntityDataAccessor;
import java.util.Random;

public class FishingHook extends Projectile {
    private final Random syncronizedRandom;
    private boolean biting;
    private int outOfWaterTime;
    private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY;
    private static final EntityDataAccessor<Boolean> DATA_BITING;
    private int life;
    private int nibble;
    private int timeUntilLured;
    private int timeUntilHooked;
    private float fishAngle;
    private boolean openWater;
    private Entity hookedIn;
    private FishHookState currentState;
    private final int luck;
    private final int lureSpeed;
    
    private FishingHook(final Level bru, final Player bft, final int integer3, final int integer4) {
        super(EntityType.FISHING_BOBBER, bru);
        this.syncronizedRandom = new Random();
        this.openWater = true;
        this.currentState = FishHookState.FLYING;
        this.noCulling = true;
        this.setOwner(bft);
        bft.fishing = this;
        this.luck = Math.max(0, integer3);
        this.lureSpeed = Math.max(0, integer4);
    }
    
    public FishingHook(final Level bru, final Player bft, final double double3, final double double4, final double double5) {
        this(bru, bft, 0, 0);
        this.setPos(double3, double4, double5);
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
    }
    
    public FishingHook(final Player bft, final Level bru, final int integer3, final int integer4) {
        this(bru, bft, integer3, integer4);
        final float float6 = bft.xRot;
        final float float7 = bft.yRot;
        final float float8 = Mth.cos(-float7 * 0.017453292f - 3.1415927f);
        final float float9 = Mth.sin(-float7 * 0.017453292f - 3.1415927f);
        final float float10 = -Mth.cos(-float6 * 0.017453292f);
        final float float11 = Mth.sin(-float6 * 0.017453292f);
        final double double12 = bft.getX() - float9 * 0.3;
        final double double13 = bft.getEyeY();
        final double double14 = bft.getZ() - float8 * 0.3;
        this.moveTo(double12, double13, double14, float7, float6);
        Vec3 dck18 = new Vec3(-float9, Mth.clamp(-(float11 / float10), -5.0f, 5.0f), -float8);
        final double double15 = dck18.length();
        dck18 = dck18.multiply(0.6 / double15 + 0.5 + this.random.nextGaussian() * 0.0045, 0.6 / double15 + 0.5 + this.random.nextGaussian() * 0.0045, 0.6 / double15 + 0.5 + this.random.nextGaussian() * 0.0045);
        this.setDeltaMovement(dck18);
        this.yRot = (float)(Mth.atan2(dck18.x, dck18.z) * 57.2957763671875);
        this.xRot = (float)(Mth.atan2(dck18.y, Mth.sqrt(Entity.getHorizontalDistanceSqr(dck18))) * 57.2957763671875);
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<Integer>define(FishingHook.DATA_HOOKED_ENTITY, 0);
        this.getEntityData().<Boolean>define(FishingHook.DATA_BITING, false);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (FishingHook.DATA_HOOKED_ENTITY.equals(us)) {
            final int integer3 = this.getEntityData().<Integer>get(FishingHook.DATA_HOOKED_ENTITY);
            this.hookedIn = ((integer3 > 0) ? this.level.getEntity(integer3 - 1) : null);
        }
        if (FishingHook.DATA_BITING.equals(us)) {
            this.biting = this.getEntityData().<Boolean>get(FishingHook.DATA_BITING);
            if (this.biting) {
                this.setDeltaMovement(this.getDeltaMovement().x, -0.4f * Mth.nextFloat(this.syncronizedRandom, 0.6f, 1.0f), this.getDeltaMovement().z);
            }
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        final double double2 = 64.0;
        return double1 < 4096.0;
    }
    
    @Override
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
    }
    
    @Override
    public void tick() {
        this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level.getGameTime());
        super.tick();
        final Player bft2 = this.getPlayerOwner();
        if (bft2 == null) {
            this.remove();
            return;
        }
        if (!this.level.isClientSide && this.shouldStopFishing(bft2)) {
            return;
        }
        if (this.onGround) {
            ++this.life;
            if (this.life >= 1200) {
                this.remove();
                return;
            }
        }
        else {
            this.life = 0;
        }
        float float3 = 0.0f;
        final BlockPos fx4 = this.blockPosition();
        final FluidState cuu5 = this.level.getFluidState(fx4);
        if (cuu5.is(FluidTags.WATER)) {
            float3 = cuu5.getHeight(this.level, fx4);
        }
        final boolean boolean6 = float3 > 0.0f;
        if (this.currentState == FishHookState.FLYING) {
            if (this.hookedIn != null) {
                this.setDeltaMovement(Vec3.ZERO);
                this.currentState = FishHookState.HOOKED_IN_ENTITY;
                return;
            }
            if (boolean6) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.2, 0.3));
                this.currentState = FishHookState.BOBBING;
                return;
            }
            this.checkCollision();
        }
        else {
            if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
                if (this.hookedIn != null) {
                    if (this.hookedIn.removed) {
                        this.hookedIn = null;
                        this.currentState = FishHookState.FLYING;
                    }
                    else {
                        this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8), this.hookedIn.getZ());
                    }
                }
                return;
            }
            if (this.currentState == FishHookState.BOBBING) {
                final Vec3 dck7 = this.getDeltaMovement();
                double double8 = this.getY() + dck7.y - fx4.getY() - float3;
                if (Math.abs(double8) < 0.01) {
                    double8 += Math.signum(double8) * 0.1;
                }
                this.setDeltaMovement(dck7.x * 0.9, dck7.y - double8 * this.random.nextFloat() * 0.2, dck7.z * 0.9);
                if (this.nibble > 0 || this.timeUntilHooked > 0) {
                    this.openWater = (this.openWater && this.outOfWaterTime < 10 && this.calculateOpenWater(fx4));
                }
                else {
                    this.openWater = true;
                }
                if (boolean6) {
                    this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
                    if (this.biting) {
                        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.1 * this.syncronizedRandom.nextFloat() * this.syncronizedRandom.nextFloat(), 0.0));
                    }
                    if (!this.level.isClientSide) {
                        this.catchingFish(fx4);
                    }
                }
                else {
                    this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
                }
            }
        }
        if (!cuu5.is(FluidTags.WATER)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.updateRotation();
        if (this.currentState == FishHookState.FLYING && (this.onGround || this.horizontalCollision)) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        final double double9 = 0.92;
        this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
        this.reapplyPosition();
    }
    
    private boolean shouldStopFishing(final Player bft) {
        final ItemStack bly3 = bft.getMainHandItem();
        final ItemStack bly4 = bft.getOffhandItem();
        final boolean boolean5 = bly3.getItem() == Items.FISHING_ROD;
        final boolean boolean6 = bly4.getItem() == Items.FISHING_ROD;
        if (bft.removed || !bft.isAlive() || (!boolean5 && !boolean6) || this.distanceToSqr(bft) > 1024.0) {
            this.remove();
            return true;
        }
        return false;
    }
    
    private void checkCollision() {
        final HitResult dci2 = ProjectileUtil.getHitResult(this, (Predicate<Entity>)this::canHitEntity);
        this.onHit(dci2);
    }
    
    @Override
    protected boolean canHitEntity(final Entity apx) {
        return super.canHitEntity(apx) || (apx.isAlive() && apx instanceof ItemEntity);
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        if (!this.level.isClientSide) {
            this.hookedIn = dch.getEntity();
            this.setHookedEntity();
        }
    }
    
    @Override
    protected void onHitBlock(final BlockHitResult dcg) {
        super.onHitBlock(dcg);
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(dcg.distanceTo(this)));
    }
    
    private void setHookedEntity() {
        this.getEntityData().<Integer>set(FishingHook.DATA_HOOKED_ENTITY, this.hookedIn.getId() + 1);
    }
    
    private void catchingFish(final BlockPos fx) {
        final ServerLevel aag3 = (ServerLevel)this.level;
        int integer4 = 1;
        final BlockPos fx2 = fx.above();
        if (this.random.nextFloat() < 0.25f && this.level.isRainingAt(fx2)) {
            ++integer4;
        }
        if (this.random.nextFloat() < 0.5f && !this.level.canSeeSky(fx2)) {
            --integer4;
        }
        if (this.nibble > 0) {
            --this.nibble;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                this.getEntityData().<Boolean>set(FishingHook.DATA_BITING, false);
            }
        }
        else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= integer4;
            if (this.timeUntilHooked > 0) {
                this.fishAngle += (float)(this.random.nextGaussian() * 4.0);
                final float float6 = this.fishAngle * 0.017453292f;
                final float float7 = Mth.sin(float6);
                final float float8 = Mth.cos(float6);
                final double double9 = this.getX() + float7 * this.timeUntilHooked * 0.1f;
                final double double10 = Mth.floor(this.getY()) + 1.0f;
                final double double11 = this.getZ() + float8 * this.timeUntilHooked * 0.1f;
                final BlockState cee15 = aag3.getBlockState(new BlockPos(double9, double10 - 1.0, double11));
                if (cee15.is(Blocks.WATER)) {
                    if (this.random.nextFloat() < 0.15f) {
                        aag3.<SimpleParticleType>sendParticles(ParticleTypes.BUBBLE, double9, double10 - 0.10000000149011612, double11, 1, float7, 0.1, float8, 0.0);
                    }
                    final float float9 = float7 * 0.04f;
                    final float float10 = float8 * 0.04f;
                    aag3.<SimpleParticleType>sendParticles(ParticleTypes.FISHING, double9, double10, double11, 0, float10, 0.01, -float9, 1.0);
                    aag3.<SimpleParticleType>sendParticles(ParticleTypes.FISHING, double9, double10, double11, 0, -float10, 0.01, float9, 1.0);
                }
            }
            else {
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
                final double double12 = this.getY() + 0.5;
                aag3.<SimpleParticleType>sendParticles(ParticleTypes.BUBBLE, this.getX(), double12, this.getZ(), (int)(1.0f + this.getBbWidth() * 20.0f), this.getBbWidth(), 0.0, this.getBbWidth(), 0.20000000298023224);
                aag3.<SimpleParticleType>sendParticles(ParticleTypes.FISHING, this.getX(), double12, this.getZ(), (int)(1.0f + this.getBbWidth() * 20.0f), this.getBbWidth(), 0.0, this.getBbWidth(), 0.20000000298023224);
                this.nibble = Mth.nextInt(this.random, 20, 40);
                this.getEntityData().<Boolean>set(FishingHook.DATA_BITING, true);
            }
        }
        else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= integer4;
            float float6 = 0.15f;
            if (this.timeUntilLured < 20) {
                float6 += (float)((20 - this.timeUntilLured) * 0.05);
            }
            else if (this.timeUntilLured < 40) {
                float6 += (float)((40 - this.timeUntilLured) * 0.02);
            }
            else if (this.timeUntilLured < 60) {
                float6 += (float)((60 - this.timeUntilLured) * 0.01);
            }
            if (this.random.nextFloat() < float6) {
                final float float7 = Mth.nextFloat(this.random, 0.0f, 360.0f) * 0.017453292f;
                final float float8 = Mth.nextFloat(this.random, 25.0f, 60.0f);
                final double double9 = this.getX() + Mth.sin(float7) * float8 * 0.1f;
                final double double10 = Mth.floor(this.getY()) + 1.0f;
                final double double11 = this.getZ() + Mth.cos(float7) * float8 * 0.1f;
                final BlockState cee15 = aag3.getBlockState(new BlockPos(double9, double10 - 1.0, double11));
                if (cee15.is(Blocks.WATER)) {
                    aag3.<SimpleParticleType>sendParticles(ParticleTypes.SPLASH, double9, double10, double11, 2 + this.random.nextInt(2), 0.10000000149011612, 0.0, 0.10000000149011612, 0.0);
                }
            }
            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0f, 360.0f);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        }
        else {
            this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
            this.timeUntilLured -= this.lureSpeed * 20 * 5;
        }
    }
    
    private boolean calculateOpenWater(final BlockPos fx) {
        OpenWaterType b3 = OpenWaterType.INVALID;
        for (int integer4 = -1; integer4 <= 2; ++integer4) {
            final OpenWaterType b4 = this.getOpenWaterTypeForArea(fx.offset(-2, integer4, -2), fx.offset(2, integer4, 2));
            switch (b4) {
                case INVALID: {
                    return false;
                }
                case ABOVE_WATER: {
                    if (b3 == OpenWaterType.INVALID) {
                        return false;
                    }
                    break;
                }
                case INSIDE_WATER: {
                    if (b3 == OpenWaterType.ABOVE_WATER) {
                        return false;
                    }
                    break;
                }
            }
            b3 = b4;
        }
        return true;
    }
    
    private OpenWaterType getOpenWaterTypeForArea(final BlockPos fx1, final BlockPos fx2) {
        return (OpenWaterType)BlockPos.betweenClosedStream(fx1, fx2).map(this::getOpenWaterTypeForBlock).reduce((b1, b2) -> (b1 == b2) ? b1 : OpenWaterType.INVALID).orElse(OpenWaterType.INVALID);
    }
    
    private OpenWaterType getOpenWaterTypeForBlock(final BlockPos fx) {
        final BlockState cee3 = this.level.getBlockState(fx);
        if (cee3.isAir() || cee3.is(Blocks.LILY_PAD)) {
            return OpenWaterType.ABOVE_WATER;
        }
        final FluidState cuu4 = cee3.getFluidState();
        if (cuu4.is(FluidTags.WATER) && cuu4.isSource() && cee3.getCollisionShape(this.level, fx).isEmpty()) {
            return OpenWaterType.INSIDE_WATER;
        }
        return OpenWaterType.INVALID;
    }
    
    public boolean isOpenWaterFishing() {
        return this.openWater;
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
    }
    
    public int retrieve(final ItemStack bly) {
        final Player bft3 = this.getPlayerOwner();
        if (this.level.isClientSide || bft3 == null) {
            return 0;
        }
        int integer4 = 0;
        if (this.hookedIn != null) {
            this.bringInHookedEntity();
            CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)bft3, bly, this, (Collection<ItemStack>)Collections.emptyList());
            this.level.broadcastEntityEvent(this, (byte)31);
            integer4 = ((this.hookedIn instanceof ItemEntity) ? 3 : 5);
        }
        else if (this.nibble > 0) {
            final LootContext.Builder a5 = new LootContext.Builder((ServerLevel)this.level).<Vec3>withParameter(LootContextParams.ORIGIN, this.position()).<ItemStack>withParameter(LootContextParams.TOOL, bly).<Entity>withParameter(LootContextParams.THIS_ENTITY, this).withRandom(this.random).withLuck(this.luck + bft3.getLuck());
            final LootTable cyv6 = this.level.getServer().getLootTables().get(BuiltInLootTables.FISHING);
            final List<ItemStack> list7 = cyv6.getRandomItems(a5.create(LootContextParamSets.FISHING));
            CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)bft3, bly, this, (Collection<ItemStack>)list7);
            for (final ItemStack bly2 : list7) {
                final ItemEntity bcs10 = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), bly2);
                final double double11 = bft3.getX() - this.getX();
                final double double12 = bft3.getY() - this.getY();
                final double double13 = bft3.getZ() - this.getZ();
                final double double14 = 0.1;
                bcs10.setDeltaMovement(double11 * 0.1, double12 * 0.1 + Math.sqrt(Math.sqrt(double11 * double11 + double12 * double12 + double13 * double13)) * 0.08, double13 * 0.1);
                this.level.addFreshEntity(bcs10);
                bft3.level.addFreshEntity(new ExperienceOrb(bft3.level, bft3.getX(), bft3.getY() + 0.5, bft3.getZ() + 0.5, this.random.nextInt(6) + 1));
                if (bly2.getItem().is(ItemTags.FISHES)) {
                    bft3.awardStat(Stats.FISH_CAUGHT, 1);
                }
            }
            integer4 = 1;
        }
        if (this.onGround) {
            integer4 = 2;
        }
        this.remove();
        return integer4;
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 31 && this.level.isClientSide && this.hookedIn instanceof Player && ((Player)this.hookedIn).isLocalPlayer()) {
            this.bringInHookedEntity();
        }
        super.handleEntityEvent(byte1);
    }
    
    protected void bringInHookedEntity() {
        final Entity apx2 = this.getOwner();
        if (apx2 == null) {
            return;
        }
        final Vec3 dck3 = new Vec3(apx2.getX() - this.getX(), apx2.getY() - this.getY(), apx2.getZ() - this.getZ()).scale(0.1);
        this.hookedIn.setDeltaMovement(this.hookedIn.getDeltaMovement().add(dck3));
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    public void remove() {
        super.remove();
        final Player bft2 = this.getPlayerOwner();
        if (bft2 != null) {
            bft2.fishing = null;
        }
    }
    
    @Nullable
    public Player getPlayerOwner() {
        final Entity apx2 = this.getOwner();
        return (apx2 instanceof Player) ? ((Player)apx2) : null;
    }
    
    @Nullable
    public Entity getHookedIn() {
        return this.hookedIn;
    }
    
    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        final Entity apx2 = this.getOwner();
        return new ClientboundAddEntityPacket(this, (apx2 == null) ? this.getId() : apx2.getId());
    }
    
    static {
        DATA_HOOKED_ENTITY = SynchedEntityData.<Integer>defineId(FishingHook.class, EntityDataSerializers.INT);
        DATA_BITING = SynchedEntityData.<Boolean>defineId(FishingHook.class, EntityDataSerializers.BOOLEAN);
    }
    
    enum FishHookState {
        FLYING, 
        HOOKED_IN_ENTITY, 
        BOBBING;
    }
    
    enum OpenWaterType {
        ABOVE_WATER, 
        INSIDE_WATER, 
        INVALID;
    }
}
