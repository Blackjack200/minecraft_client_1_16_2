package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Predicate;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.BlockHitResult;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.damagesource.DamageSource;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.MoverType;
import java.util.Iterator;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import java.util.List;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class AbstractArrow extends Projectile {
    private static final EntityDataAccessor<Byte> ID_FLAGS;
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL;
    @Nullable
    private BlockState lastState;
    protected boolean inGround;
    protected int inGroundTime;
    public Pickup pickup;
    public int shakeTime;
    private int life;
    private double baseDamage;
    private int knockback;
    private SoundEvent soundEvent;
    private IntOpenHashSet piercingIgnoreEntityIds;
    private List<Entity> piercedAndKilledEntities;
    
    protected AbstractArrow(final EntityType<? extends AbstractArrow> aqb, final Level bru) {
        super(aqb, bru);
        this.pickup = Pickup.DISALLOWED;
        this.baseDamage = 2.0;
        this.soundEvent = this.getDefaultHitGroundSoundEvent();
    }
    
    protected AbstractArrow(final EntityType<? extends AbstractArrow> aqb, final double double2, final double double3, final double double4, final Level bru) {
        this(aqb, bru);
        this.setPos(double2, double3, double4);
    }
    
    protected AbstractArrow(final EntityType<? extends AbstractArrow> aqb, final LivingEntity aqj, final Level bru) {
        this(aqb, aqj.getX(), aqj.getEyeY() - 0.10000000149011612, aqj.getZ(), bru);
        this.setOwner(aqj);
        if (aqj instanceof Player) {
            this.pickup = Pickup.ALLOWED;
        }
    }
    
    public void setSoundEvent(final SoundEvent adn) {
        this.soundEvent = adn;
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        double double2 = this.getBoundingBox().getSize() * 10.0;
        if (Double.isNaN(double2)) {
            double2 = 1.0;
        }
        double2 *= 64.0 * getViewScale();
        return double1 < double2 * double2;
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<Byte>define(AbstractArrow.ID_FLAGS, (Byte)0);
        this.entityData.<Byte>define(AbstractArrow.PIERCE_LEVEL, (Byte)0);
    }
    
    @Override
    public void shoot(final double double1, final double double2, final double double3, final float float4, final float float5) {
        super.shoot(double1, double2, double3, float4, float5);
        this.life = 0;
    }
    
    @Override
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        this.setPos(double1, double2, double3);
        this.setRot(float4, float5);
    }
    
    @Override
    public void lerpMotion(final double double1, final double double2, final double double3) {
        super.lerpMotion(double1, double2, double3);
        this.life = 0;
    }
    
    @Override
    public void tick() {
        super.tick();
        final boolean boolean2 = this.isNoPhysics();
        Vec3 dck3 = this.getDeltaMovement();
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            final float float4 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck3));
            this.yRot = (float)(Mth.atan2(dck3.x, dck3.z) * 57.2957763671875);
            this.xRot = (float)(Mth.atan2(dck3.y, float4) * 57.2957763671875);
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
        }
        final BlockPos fx4 = this.blockPosition();
        final BlockState cee5 = this.level.getBlockState(fx4);
        if (!cee5.isAir() && !boolean2) {
            final VoxelShape dde6 = cee5.getCollisionShape(this.level, fx4);
            if (!dde6.isEmpty()) {
                final Vec3 dck4 = this.position();
                for (final AABB dcf9 : dde6.toAabbs()) {
                    if (dcf9.move(fx4).contains(dck4)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.shakeTime > 0) {
            --this.shakeTime;
        }
        if (this.isInWaterOrRain()) {
            this.clearFire();
        }
        if (this.inGround && !boolean2) {
            if (this.lastState != cee5 && this.shouldFall()) {
                this.startFalling();
            }
            else if (!this.level.isClientSide) {
                this.tickDespawn();
            }
            ++this.inGroundTime;
            return;
        }
        this.inGroundTime = 0;
        final Vec3 dck5 = this.position();
        Vec3 dck4 = dck5.add(dck3);
        HitResult dci8 = this.level.clip(new ClipContext(dck5, dck4, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (dci8.getType() != HitResult.Type.MISS) {
            dck4 = dci8.getLocation();
        }
        while (!this.removed) {
            EntityHitResult dch9 = this.findHitEntity(dck5, dck4);
            if (dch9 != null) {
                dci8 = dch9;
            }
            if (dci8 != null && dci8.getType() == HitResult.Type.ENTITY) {
                final Entity apx10 = ((EntityHitResult)dci8).getEntity();
                final Entity apx11 = this.getOwner();
                if (apx10 instanceof Player && apx11 instanceof Player && !((Player)apx11).canHarmPlayer((Player)apx10)) {
                    dci8 = null;
                    dch9 = null;
                }
            }
            if (dci8 != null && !boolean2) {
                this.onHit(dci8);
                this.hasImpulse = true;
            }
            if (dch9 == null) {
                break;
            }
            if (this.getPierceLevel() <= 0) {
                break;
            }
            dci8 = null;
        }
        dck3 = this.getDeltaMovement();
        final double double9 = dck3.x;
        final double double10 = dck3.y;
        final double double11 = dck3.z;
        if (this.isCritArrow()) {
            for (int integer15 = 0; integer15 < 4; ++integer15) {
                this.level.addParticle(ParticleTypes.CRIT, this.getX() + double9 * integer15 / 4.0, this.getY() + double10 * integer15 / 4.0, this.getZ() + double11 * integer15 / 4.0, -double9, -double10 + 0.2, -double11);
            }
        }
        final double double12 = this.getX() + double9;
        final double double13 = this.getY() + double10;
        final double double14 = this.getZ() + double11;
        final float float5 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck3));
        if (boolean2) {
            this.yRot = (float)(Mth.atan2(-double9, -double11) * 57.2957763671875);
        }
        else {
            this.yRot = (float)(Mth.atan2(double9, double11) * 57.2957763671875);
        }
        this.xRot = (float)(Mth.atan2(double10, float5) * 57.2957763671875);
        this.xRot = Projectile.lerpRotation(this.xRotO, this.xRot);
        this.yRot = Projectile.lerpRotation(this.yRotO, this.yRot);
        float float6 = 0.99f;
        final float float7 = 0.05f;
        if (this.isInWater()) {
            for (int integer16 = 0; integer16 < 4; ++integer16) {
                final float float8 = 0.25f;
                this.level.addParticle(ParticleTypes.BUBBLE, double12 - double9 * 0.25, double13 - double10 * 0.25, double14 - double11 * 0.25, double9, double10, double11);
            }
            float6 = this.getWaterInertia();
        }
        this.setDeltaMovement(dck3.scale(float6));
        if (!this.isNoGravity() && !boolean2) {
            final Vec3 dck6 = this.getDeltaMovement();
            this.setDeltaMovement(dck6.x, dck6.y - 0.05000000074505806, dck6.z);
        }
        this.setPos(double12, double13, double14);
        this.checkInsideBlocks();
    }
    
    private boolean shouldFall() {
        return this.inGround && this.level.noCollision(new AABB(this.position(), this.position()).inflate(0.06));
    }
    
    private void startFalling() {
        this.inGround = false;
        final Vec3 dck2 = this.getDeltaMovement();
        this.setDeltaMovement(dck2.multiply(this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f));
        this.life = 0;
    }
    
    @Override
    public void move(final MoverType aqo, final Vec3 dck) {
        super.move(aqo, dck);
        if (aqo != MoverType.SELF && this.shouldFall()) {
            this.startFalling();
        }
    }
    
    protected void tickDespawn() {
        ++this.life;
        if (this.life >= 1200) {
            this.remove();
        }
    }
    
    private void resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear();
        }
        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        final Entity apx3 = dch.getEntity();
        final float float4 = (float)this.getDeltaMovement().length();
        int integer5 = Mth.ceil(Mth.clamp(float4 * this.baseDamage, 0.0, 2.147483647E9));
        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }
            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = (List<Entity>)Lists.newArrayListWithCapacity(5);
            }
            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }
            this.piercingIgnoreEntityIds.add(apx3.getId());
        }
        if (this.isCritArrow()) {
            final long long6 = this.random.nextInt(integer5 / 2 + 2);
            integer5 = (int)Math.min(long6 + integer5, 2147483647L);
        }
        final Entity apx4 = this.getOwner();
        DamageSource aph6;
        if (apx4 == null) {
            aph6 = DamageSource.arrow(this, this);
        }
        else {
            aph6 = DamageSource.arrow(this, apx4);
            if (apx4 instanceof LivingEntity) {
                ((LivingEntity)apx4).setLastHurtMob(apx3);
            }
        }
        final boolean boolean8 = apx3.getType() == EntityType.ENDERMAN;
        final int integer6 = apx3.getRemainingFireTicks();
        if (this.isOnFire() && !boolean8) {
            apx3.setSecondsOnFire(5);
        }
        if (apx3.hurt(aph6, (float)integer5)) {
            if (boolean8) {
                return;
            }
            if (apx3 instanceof LivingEntity) {
                final LivingEntity aqj10 = (LivingEntity)apx3;
                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    aqj10.setArrowCount(aqj10.getArrowCount() + 1);
                }
                if (this.knockback > 0) {
                    final Vec3 dck11 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(this.knockback * 0.6);
                    if (dck11.lengthSqr() > 0.0) {
                        aqj10.push(dck11.x, 0.1, dck11.z);
                    }
                }
                if (!this.level.isClientSide && apx4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(aqj10, apx4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)apx4, aqj10);
                }
                this.doPostHurtEffects(aqj10);
                if (apx4 != null && aqj10 != apx4 && aqj10 instanceof Player && apx4 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)apx4).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0f));
                }
                if (!apx3.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(aqj10);
                }
                if (!this.level.isClientSide && apx4 instanceof ServerPlayer) {
                    final ServerPlayer aah11 = (ServerPlayer)apx4;
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(aah11, (Collection<Entity>)this.piercedAndKilledEntities);
                    }
                    else if (!apx3.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(aah11, (Collection<Entity>)Arrays.asList((Object[])new Entity[] { apx3 }));
                    }
                }
            }
            this.playSound(this.soundEvent, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
            if (this.getPierceLevel() <= 0) {
                this.remove();
            }
        }
        else {
            apx3.setRemainingFireTicks(integer6);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
            this.yRot += 180.0f;
            this.yRotO += 180.0f;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
                if (this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1f);
                }
                this.remove();
            }
        }
    }
    
    @Override
    protected void onHitBlock(final BlockHitResult dcg) {
        this.lastState = this.level.getBlockState(dcg.getBlockPos());
        super.onHitBlock(dcg);
        final Vec3 dck3 = dcg.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(dck3);
        final Vec3 dck4 = dck3.normalize().scale(0.05000000074505806);
        this.setPosRaw(this.getX() - dck4.x, this.getY() - dck4.y, this.getZ() - dck4.z);
        this.playSound(this.getHitGroundSoundEvent(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.setShotFromCrossbow(false);
        this.resetPiercedEntities();
    }
    
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }
    
    protected final SoundEvent getHitGroundSoundEvent() {
        return this.soundEvent;
    }
    
    protected void doPostHurtEffects(final LivingEntity aqj) {
    }
    
    @Nullable
    protected EntityHitResult findHitEntity(final Vec3 dck1, final Vec3 dck2) {
        return ProjectileUtil.getEntityHitResult(this.level, this, dck1, dck2, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), (Predicate<Entity>)this::canHitEntity);
    }
    
    @Override
    protected boolean canHitEntity(final Entity apx) {
        return super.canHitEntity(apx) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(apx.getId()));
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putShort("life", (short)this.life);
        if (this.lastState != null) {
            md.put("inBlockState", (Tag)NbtUtils.writeBlockState(this.lastState));
        }
        md.putByte("shake", (byte)this.shakeTime);
        md.putBoolean("inGround", this.inGround);
        md.putByte("pickup", (byte)this.pickup.ordinal());
        md.putDouble("damage", this.baseDamage);
        md.putBoolean("crit", this.isCritArrow());
        md.putByte("PierceLevel", this.getPierceLevel());
        md.putString("SoundEvent", Registry.SOUND_EVENT.getKey(this.soundEvent).toString());
        md.putBoolean("ShotFromCrossbow", this.shotFromCrossbow());
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.life = md.getShort("life");
        if (md.contains("inBlockState", 10)) {
            this.lastState = NbtUtils.readBlockState(md.getCompound("inBlockState"));
        }
        this.shakeTime = (md.getByte("shake") & 0xFF);
        this.inGround = md.getBoolean("inGround");
        if (md.contains("damage", 99)) {
            this.baseDamage = md.getDouble("damage");
        }
        if (md.contains("pickup", 99)) {
            this.pickup = Pickup.byOrdinal(md.getByte("pickup"));
        }
        else if (md.contains("player", 99)) {
            this.pickup = (md.getBoolean("player") ? Pickup.ALLOWED : Pickup.DISALLOWED);
        }
        this.setCritArrow(md.getBoolean("crit"));
        this.setPierceLevel(md.getByte("PierceLevel"));
        if (md.contains("SoundEvent", 8)) {
            this.soundEvent = (SoundEvent)Registry.SOUND_EVENT.getOptional(new ResourceLocation(md.getString("SoundEvent"))).orElse(this.getDefaultHitGroundSoundEvent());
        }
        this.setShotFromCrossbow(md.getBoolean("ShotFromCrossbow"));
    }
    
    @Override
    public void setOwner(@Nullable final Entity apx) {
        super.setOwner(apx);
        if (apx instanceof Player) {
            this.pickup = (((Player)apx).abilities.instabuild ? Pickup.CREATIVE_ONLY : Pickup.ALLOWED);
        }
    }
    
    @Override
    public void playerTouch(final Player bft) {
        if (this.level.isClientSide || (!this.inGround && !this.isNoPhysics()) || this.shakeTime > 0) {
            return;
        }
        boolean boolean3 = this.pickup == Pickup.ALLOWED || (this.pickup == Pickup.CREATIVE_ONLY && bft.abilities.instabuild) || (this.isNoPhysics() && this.getOwner().getUUID() == bft.getUUID());
        if (this.pickup == Pickup.ALLOWED && !bft.inventory.add(this.getPickupItem())) {
            boolean3 = false;
        }
        if (boolean3) {
            bft.take(this, 1);
            this.remove();
        }
    }
    
    protected abstract ItemStack getPickupItem();
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    public void setBaseDamage(final double double1) {
        this.baseDamage = double1;
    }
    
    public double getBaseDamage() {
        return this.baseDamage;
    }
    
    public void setKnockback(final int integer) {
        this.knockback = integer;
    }
    
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    @Override
    protected float getEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.13f;
    }
    
    public void setCritArrow(final boolean boolean1) {
        this.setFlag(1, boolean1);
    }
    
    public void setPierceLevel(final byte byte1) {
        this.entityData.<Byte>set(AbstractArrow.PIERCE_LEVEL, byte1);
    }
    
    private void setFlag(final int integer, final boolean boolean2) {
        final byte byte4 = this.entityData.<Byte>get(AbstractArrow.ID_FLAGS);
        if (boolean2) {
            this.entityData.<Byte>set(AbstractArrow.ID_FLAGS, (byte)(byte4 | integer));
        }
        else {
            this.entityData.<Byte>set(AbstractArrow.ID_FLAGS, (byte)(byte4 & ~integer));
        }
    }
    
    public boolean isCritArrow() {
        final byte byte2 = this.entityData.<Byte>get(AbstractArrow.ID_FLAGS);
        return (byte2 & 0x1) != 0x0;
    }
    
    public boolean shotFromCrossbow() {
        final byte byte2 = this.entityData.<Byte>get(AbstractArrow.ID_FLAGS);
        return (byte2 & 0x4) != 0x0;
    }
    
    public byte getPierceLevel() {
        return this.entityData.<Byte>get(AbstractArrow.PIERCE_LEVEL);
    }
    
    public void setEnchantmentEffectsFromEntity(final LivingEntity aqj, final float float2) {
        final int integer4 = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, aqj);
        final int integer5 = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, aqj);
        this.setBaseDamage(float2 * 2.0f + (this.random.nextGaussian() * 0.25 + this.level.getDifficulty().getId() * 0.11f));
        if (integer4 > 0) {
            this.setBaseDamage(this.getBaseDamage() + integer4 * 0.5 + 0.5);
        }
        if (integer5 > 0) {
            this.setKnockback(integer5);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, aqj) > 0) {
            this.setSecondsOnFire(100);
        }
    }
    
    protected float getWaterInertia() {
        return 0.6f;
    }
    
    public void setNoPhysics(final boolean boolean1) {
        this.setFlag(2, this.noPhysics = boolean1);
    }
    
    public boolean isNoPhysics() {
        if (!this.level.isClientSide) {
            return this.noPhysics;
        }
        return (this.entityData.<Byte>get(AbstractArrow.ID_FLAGS) & 0x2) != 0x0;
    }
    
    public void setShotFromCrossbow(final boolean boolean1) {
        this.setFlag(4, boolean1);
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        final Entity apx2 = this.getOwner();
        return new ClientboundAddEntityPacket(this, (apx2 == null) ? 0 : apx2.getId());
    }
    
    static {
        ID_FLAGS = SynchedEntityData.<Byte>defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
        PIERCE_LEVEL = SynchedEntityData.<Byte>defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
    }
    
    public enum Pickup {
        DISALLOWED, 
        ALLOWED, 
        CREATIVE_ONLY;
        
        public static Pickup byOrdinal(int integer) {
            if (integer < 0 || integer > values().length) {
                integer = 0;
            }
            return values()[integer];
        }
    }
}
