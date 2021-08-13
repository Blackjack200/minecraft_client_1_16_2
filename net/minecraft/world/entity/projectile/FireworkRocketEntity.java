package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.Tag;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.function.Predicate;
import net.minecraft.world.entity.MoverType;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.OptionalInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;

public class FireworkRocketEntity extends Projectile implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM;
    private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET;
    private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE;
    private int life;
    private int lifetime;
    private LivingEntity attachedToEntity;
    
    public FireworkRocketEntity(final EntityType<? extends FireworkRocketEntity> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public FireworkRocketEntity(final Level bru, final double double2, final double double3, final double double4, final ItemStack bly) {
        super(EntityType.FIREWORK_ROCKET, bru);
        this.life = 0;
        this.setPos(double2, double3, double4);
        int integer10 = 1;
        if (!bly.isEmpty() && bly.hasTag()) {
            this.entityData.<ItemStack>set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, bly.copy());
            integer10 += bly.getOrCreateTagElement("Fireworks").getByte("Flight");
        }
        this.setDeltaMovement(this.random.nextGaussian() * 0.001, 0.05, this.random.nextGaussian() * 0.001);
        this.lifetime = 10 * integer10 + this.random.nextInt(6) + this.random.nextInt(7);
    }
    
    public FireworkRocketEntity(final Level bru, @Nullable final Entity apx, final double double3, final double double4, final double double5, final ItemStack bly) {
        this(bru, double3, double4, double5, bly);
        this.setOwner(apx);
    }
    
    public FireworkRocketEntity(final Level bru, final ItemStack bly, final LivingEntity aqj) {
        this(bru, aqj, aqj.getX(), aqj.getY(), aqj.getZ(), bly);
        this.entityData.<OptionalInt>set(FireworkRocketEntity.DATA_ATTACHED_TO_TARGET, OptionalInt.of(aqj.getId()));
        this.attachedToEntity = aqj;
    }
    
    public FireworkRocketEntity(final Level bru, final ItemStack bly, final double double3, final double double4, final double double5, final boolean boolean6) {
        this(bru, double3, double4, double5, bly);
        this.entityData.<Boolean>set(FireworkRocketEntity.DATA_SHOT_AT_ANGLE, boolean6);
    }
    
    public FireworkRocketEntity(final Level bru, final ItemStack bly, final Entity apx, final double double4, final double double5, final double double6, final boolean boolean7) {
        this(bru, bly, double4, double5, double6, boolean7);
        this.setOwner(apx);
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<ItemStack>define(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, ItemStack.EMPTY);
        this.entityData.<OptionalInt>define(FireworkRocketEntity.DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
        this.entityData.<Boolean>define(FireworkRocketEntity.DATA_SHOT_AT_ANGLE, false);
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        return double1 < 4096.0 && !this.isAttachedToEntity();
    }
    
    @Override
    public boolean shouldRender(final double double1, final double double2, final double double3) {
        return super.shouldRender(double1, double2, double3) && !this.isAttachedToEntity();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.isAttachedToEntity()) {
            if (this.attachedToEntity == null) {
                this.entityData.<OptionalInt>get(FireworkRocketEntity.DATA_ATTACHED_TO_TARGET).ifPresent(integer -> {
                    final Entity apx3 = this.level.getEntity(integer);
                    if (apx3 instanceof LivingEntity) {
                        this.attachedToEntity = (LivingEntity)apx3;
                    }
                });
            }
            if (this.attachedToEntity != null) {
                if (this.attachedToEntity.isFallFlying()) {
                    final Vec3 dck2 = this.attachedToEntity.getLookAngle();
                    final double double3 = 1.5;
                    final double double4 = 0.1;
                    final Vec3 dck3 = this.attachedToEntity.getDeltaMovement();
                    this.attachedToEntity.setDeltaMovement(dck3.add(dck2.x * 0.1 + (dck2.x * 1.5 - dck3.x) * 0.5, dck2.y * 0.1 + (dck2.y * 1.5 - dck3.y) * 0.5, dck2.z * 0.1 + (dck2.z * 1.5 - dck3.z) * 0.5));
                }
                this.setPos(this.attachedToEntity.getX(), this.attachedToEntity.getY(), this.attachedToEntity.getZ());
                this.setDeltaMovement(this.attachedToEntity.getDeltaMovement());
            }
        }
        else {
            if (!this.isShotAtAngle()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.15, 1.0, 1.15).add(0.0, 0.04, 0.0));
            }
            final Vec3 dck2 = this.getDeltaMovement();
            this.move(MoverType.SELF, dck2);
            this.setDeltaMovement(dck2);
        }
        final HitResult dci2 = ProjectileUtil.getHitResult(this, (Predicate<Entity>)this::canHitEntity);
        if (!this.noPhysics) {
            this.onHit(dci2);
            this.hasImpulse = true;
        }
        this.updateRotation();
        if (this.life == 0 && !this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0f, 1.0f);
        }
        ++this.life;
        if (this.level.isClientSide && this.life % 2 < 2) {
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY() - 0.3, this.getZ(), this.random.nextGaussian() * 0.05, -this.getDeltaMovement().y * 0.5, this.random.nextGaussian() * 0.05);
        }
        if (!this.level.isClientSide && this.life > this.lifetime) {
            this.explode();
        }
    }
    
    private void explode() {
        this.level.broadcastEntityEvent(this, (byte)17);
        this.dealExplosionDamage();
        this.remove();
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        if (this.level.isClientSide) {
            return;
        }
        this.explode();
    }
    
    @Override
    protected void onHitBlock(final BlockHitResult dcg) {
        final BlockPos fx3 = new BlockPos(dcg.getBlockPos());
        this.level.getBlockState(fx3).entityInside(this.level, fx3, this);
        if (!this.level.isClientSide() && this.hasExplosion()) {
            this.explode();
        }
        super.onHitBlock(dcg);
    }
    
    private boolean hasExplosion() {
        final ItemStack bly2 = this.entityData.<ItemStack>get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
        final CompoundTag md3 = bly2.isEmpty() ? null : bly2.getTagElement("Fireworks");
        final ListTag mj4 = (md3 != null) ? md3.getList("Explosions", 10) : null;
        return mj4 != null && !mj4.isEmpty();
    }
    
    private void dealExplosionDamage() {
        float float2 = 0.0f;
        final ItemStack bly3 = this.entityData.<ItemStack>get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
        final CompoundTag md4 = bly3.isEmpty() ? null : bly3.getTagElement("Fireworks");
        final ListTag mj5 = (md4 != null) ? md4.getList("Explosions", 10) : null;
        if (mj5 != null && !mj5.isEmpty()) {
            float2 = 5.0f + mj5.size() * 2;
        }
        if (float2 > 0.0f) {
            if (this.attachedToEntity != null) {
                this.attachedToEntity.hurt(DamageSource.fireworks(this, this.getOwner()), 5.0f + mj5.size() * 2);
            }
            final double double6 = 5.0;
            final Vec3 dck8 = this.position();
            final List<LivingEntity> list9 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.getBoundingBox().inflate(5.0));
            for (final LivingEntity aqj11 : list9) {
                if (aqj11 == this.attachedToEntity) {
                    continue;
                }
                if (this.distanceToSqr(aqj11) > 25.0) {
                    continue;
                }
                boolean boolean12 = false;
                for (int integer13 = 0; integer13 < 2; ++integer13) {
                    final Vec3 dck9 = new Vec3(aqj11.getX(), aqj11.getY(0.5 * integer13), aqj11.getZ());
                    final HitResult dci15 = this.level.clip(new ClipContext(dck8, dck9, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    if (dci15.getType() == HitResult.Type.MISS) {
                        boolean12 = true;
                        break;
                    }
                }
                if (!boolean12) {
                    continue;
                }
                final float float3 = float2 * (float)Math.sqrt((5.0 - this.distanceTo(aqj11)) / 5.0);
                aqj11.hurt(DamageSource.fireworks(this, this.getOwner()), float3);
            }
        }
    }
    
    private boolean isAttachedToEntity() {
        return this.entityData.<OptionalInt>get(FireworkRocketEntity.DATA_ATTACHED_TO_TARGET).isPresent();
    }
    
    public boolean isShotAtAngle() {
        return this.entityData.<Boolean>get(FireworkRocketEntity.DATA_SHOT_AT_ANGLE);
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 17 && this.level.isClientSide) {
            if (!this.hasExplosion()) {
                for (int integer3 = 0; integer3 < this.random.nextInt(3) + 2; ++integer3) {
                    this.level.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, 0.005, this.random.nextGaussian() * 0.05);
                }
            }
            else {
                final ItemStack bly3 = this.entityData.<ItemStack>get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
                final CompoundTag md4 = bly3.isEmpty() ? null : bly3.getTagElement("Fireworks");
                final Vec3 dck5 = this.getDeltaMovement();
                this.level.createFireworks(this.getX(), this.getY(), this.getZ(), dck5.x, dck5.y, dck5.z, md4);
            }
        }
        super.handleEntityEvent(byte1);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Life", this.life);
        md.putInt("LifeTime", this.lifetime);
        final ItemStack bly3 = this.entityData.<ItemStack>get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
        if (!bly3.isEmpty()) {
            md.put("FireworksItem", (Tag)bly3.save(new CompoundTag()));
        }
        md.putBoolean("ShotAtAngle", (boolean)this.entityData.<Boolean>get(FireworkRocketEntity.DATA_SHOT_AT_ANGLE));
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.life = md.getInt("Life");
        this.lifetime = md.getInt("LifeTime");
        final ItemStack bly3 = ItemStack.of(md.getCompound("FireworksItem"));
        if (!bly3.isEmpty()) {
            this.entityData.<ItemStack>set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, bly3);
        }
        if (md.contains("ShotAtAngle")) {
            this.entityData.<Boolean>set(FireworkRocketEntity.DATA_SHOT_AT_ANGLE, md.getBoolean("ShotAtAngle"));
        }
    }
    
    @Override
    public ItemStack getItem() {
        final ItemStack bly2 = this.entityData.<ItemStack>get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
        return bly2.isEmpty() ? new ItemStack(Items.FIREWORK_ROCKET) : bly2;
    }
    
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    static {
        DATA_ID_FIREWORKS_ITEM = SynchedEntityData.<ItemStack>defineId(FireworkRocketEntity.class, EntityDataSerializers.ITEM_STACK);
        DATA_ATTACHED_TO_TARGET = SynchedEntityData.<OptionalInt>defineId(FireworkRocketEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
        DATA_SHOT_AT_ANGLE = SynchedEntityData.<Boolean>defineId(FireworkRocketEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
