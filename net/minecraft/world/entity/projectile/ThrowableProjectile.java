package net.minecraft.world.entity.projectile;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public abstract class ThrowableProjectile extends Projectile {
    protected ThrowableProjectile(final EntityType<? extends ThrowableProjectile> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    protected ThrowableProjectile(final EntityType<? extends ThrowableProjectile> aqb, final double double2, final double double3, final double double4, final Level bru) {
        this(aqb, bru);
        this.setPos(double2, double3, double4);
    }
    
    protected ThrowableProjectile(final EntityType<? extends ThrowableProjectile> aqb, final LivingEntity aqj, final Level bru) {
        this(aqb, aqj.getX(), aqj.getEyeY() - 0.10000000149011612, aqj.getZ(), bru);
        this.setOwner(aqj);
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
        super.tick();
        final HitResult dci2 = ProjectileUtil.getHitResult(this, (Predicate<Entity>)this::canHitEntity);
        boolean boolean3 = false;
        if (dci2.getType() == HitResult.Type.BLOCK) {
            final BlockPos fx4 = ((BlockHitResult)dci2).getBlockPos();
            final BlockState cee5 = this.level.getBlockState(fx4);
            if (cee5.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(fx4);
                boolean3 = true;
            }
            else if (cee5.is(Blocks.END_GATEWAY)) {
                final BlockEntity ccg6 = this.level.getBlockEntity(fx4);
                if (ccg6 instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    ((TheEndGatewayBlockEntity)ccg6).teleportEntity(this);
                }
                boolean3 = true;
            }
        }
        if (dci2.getType() != HitResult.Type.MISS && !boolean3) {
            this.onHit(dci2);
        }
        this.checkInsideBlocks();
        final Vec3 dck4 = this.getDeltaMovement();
        final double double5 = this.getX() + dck4.x;
        final double double6 = this.getY() + dck4.y;
        final double double7 = this.getZ() + dck4.z;
        this.updateRotation();
        float float14;
        if (this.isInWater()) {
            for (int integer12 = 0; integer12 < 4; ++integer12) {
                final float float13 = 0.25f;
                this.level.addParticle(ParticleTypes.BUBBLE, double5 - dck4.x * 0.25, double6 - dck4.y * 0.25, double7 - dck4.z * 0.25, dck4.x, dck4.y, dck4.z);
            }
            float14 = 0.8f;
        }
        else {
            float14 = 0.99f;
        }
        this.setDeltaMovement(dck4.scale(float14));
        if (!this.isNoGravity()) {
            final Vec3 dck5 = this.getDeltaMovement();
            this.setDeltaMovement(dck5.x, dck5.y - this.getGravity(), dck5.z);
        }
        this.setPos(double5, double6, double7);
    }
    
    protected float getGravity() {
        return 0.03f;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
