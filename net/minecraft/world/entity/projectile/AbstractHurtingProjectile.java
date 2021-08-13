package net.minecraft.world.entity.projectile;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.HitResult;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public abstract class AbstractHurtingProjectile extends Projectile {
    public double xPower;
    public double yPower;
    public double zPower;
    
    protected AbstractHurtingProjectile(final EntityType<? extends AbstractHurtingProjectile> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public AbstractHurtingProjectile(final EntityType<? extends AbstractHurtingProjectile> aqb, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final Level bru) {
        this(aqb, bru);
        this.moveTo(double2, double3, double4, this.yRot, this.xRot);
        this.reapplyPosition();
        final double double8 = Mth.sqrt(double5 * double5 + double6 * double6 + double7 * double7);
        if (double8 != 0.0) {
            this.xPower = double5 / double8 * 0.1;
            this.yPower = double6 / double8 * 0.1;
            this.zPower = double7 / double8 * 0.1;
        }
    }
    
    public AbstractHurtingProjectile(final EntityType<? extends AbstractHurtingProjectile> aqb, final LivingEntity aqj, final double double3, final double double4, final double double5, final Level bru) {
        this(aqb, aqj.getX(), aqj.getY(), aqj.getZ(), double3, double4, double5, bru);
        this.setOwner(aqj);
        this.setRot(aqj.yRot, aqj.xRot);
    }
    
    @Override
    protected void defineSynchedData() {
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        double double2 = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(double2)) {
            double2 = 4.0;
        }
        double2 *= 64.0;
        return double1 < double2 * double2;
    }
    
    @Override
    public void tick() {
        final Entity apx2 = this.getOwner();
        if (!this.level.isClientSide && ((apx2 != null && apx2.removed) || !this.level.hasChunkAt(this.blockPosition()))) {
            this.remove();
            return;
        }
        super.tick();
        if (this.shouldBurn()) {
            this.setSecondsOnFire(1);
        }
        final HitResult dci3 = ProjectileUtil.getHitResult(this, (Predicate<Entity>)this::canHitEntity);
        if (dci3.getType() != HitResult.Type.MISS) {
            this.onHit(dci3);
        }
        this.checkInsideBlocks();
        final Vec3 dck4 = this.getDeltaMovement();
        final double double5 = this.getX() + dck4.x;
        final double double6 = this.getY() + dck4.y;
        final double double7 = this.getZ() + dck4.z;
        ProjectileUtil.rotateTowardsMovement(this, 0.2f);
        float float11 = this.getInertia();
        if (this.isInWater()) {
            for (int integer12 = 0; integer12 < 4; ++integer12) {
                final float float12 = 0.25f;
                this.level.addParticle(ParticleTypes.BUBBLE, double5 - dck4.x * 0.25, double6 - dck4.y * 0.25, double7 - dck4.z * 0.25, dck4.x, dck4.y, dck4.z);
            }
            float11 = 0.8f;
        }
        this.setDeltaMovement(dck4.add(this.xPower, this.yPower, this.zPower).scale(float11));
        this.level.addParticle(this.getTrailParticle(), double5, double6 + 0.5, double7, 0.0, 0.0, 0.0);
        this.setPos(double5, double6, double7);
    }
    
    @Override
    protected boolean canHitEntity(final Entity apx) {
        return super.canHitEntity(apx) && !apx.noPhysics;
    }
    
    protected boolean shouldBurn() {
        return true;
    }
    
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SMOKE;
    }
    
    protected float getInertia() {
        return 0.95f;
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.put("power", (Tag)this.newDoubleList(this.xPower, this.yPower, this.zPower));
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("power", 9)) {
            final ListTag mj3 = md.getList("power", 6);
            if (mj3.size() == 3) {
                this.xPower = mj3.getDouble(0);
                this.yPower = mj3.getDouble(1);
                this.zPower = mj3.getDouble(2);
            }
        }
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public float getPickRadius() {
        return 1.0f;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        this.markHurt();
        final Entity apx4 = aph.getEntity();
        if (apx4 != null) {
            final Vec3 dck5 = apx4.getLookAngle();
            this.setDeltaMovement(dck5);
            this.xPower = dck5.x * 0.1;
            this.yPower = dck5.y * 0.1;
            this.zPower = dck5.z * 0.1;
            this.setOwner(apx4);
            return true;
        }
        return false;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        final Entity apx2 = this.getOwner();
        final int integer3 = (apx2 == null) ? 0 : apx2.getId();
        return new ClientboundAddEntityPacket(this.getId(), this.getUUID(), this.getX(), this.getY(), this.getZ(), this.xRot, this.yRot, this.getType(), integer3, new Vec3(this.xPower, this.yPower, this.zPower));
    }
}
