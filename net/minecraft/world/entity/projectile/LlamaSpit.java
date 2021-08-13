package net.minecraft.world.entity.projectile;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Predicate;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class LlamaSpit extends Projectile {
    public LlamaSpit(final EntityType<? extends LlamaSpit> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public LlamaSpit(final Level bru, final Llama bbb) {
        this(EntityType.LLAMA_SPIT, bru);
        super.setOwner(bbb);
        this.setPos(bbb.getX() - (bbb.getBbWidth() + 1.0f) * 0.5 * Mth.sin(bbb.yBodyRot * 0.017453292f), bbb.getEyeY() - 0.10000000149011612, bbb.getZ() + (bbb.getBbWidth() + 1.0f) * 0.5 * Mth.cos(bbb.yBodyRot * 0.017453292f));
    }
    
    public LlamaSpit(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        this(EntityType.LLAMA_SPIT, bru);
        this.setPos(double2, double3, double4);
        for (int integer15 = 0; integer15 < 7; ++integer15) {
            final double double8 = 0.4 + 0.1 * integer15;
            bru.addParticle(ParticleTypes.SPIT, double2, double3, double4, double5 * double8, double6, double7 * double8);
        }
        this.setDeltaMovement(double5, double6, double7);
    }
    
    @Override
    public void tick() {
        super.tick();
        final Vec3 dck2 = this.getDeltaMovement();
        final HitResult dci3 = ProjectileUtil.getHitResult(this, (Predicate<Entity>)this::canHitEntity);
        if (dci3 != null) {
            this.onHit(dci3);
        }
        final double double4 = this.getX() + dck2.x;
        final double double5 = this.getY() + dck2.y;
        final double double6 = this.getZ() + dck2.z;
        this.updateRotation();
        final float float10 = 0.99f;
        final float float11 = 0.06f;
        if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.remove();
            return;
        }
        if (this.isInWaterOrBubble()) {
            this.remove();
            return;
        }
        this.setDeltaMovement(dck2.scale(0.9900000095367432));
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.05999999865889549, 0.0));
        }
        this.setPos(double4, double5, double6);
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        final Entity apx3 = this.getOwner();
        if (apx3 instanceof LivingEntity) {
            dch.getEntity().hurt(DamageSource.indirectMobAttack(this, (LivingEntity)apx3).setProjectile(), 1.0f);
        }
    }
    
    @Override
    protected void onHitBlock(final BlockHitResult dcg) {
        super.onHitBlock(dcg);
        if (!this.level.isClientSide) {
            this.remove();
        }
    }
    
    @Override
    protected void defineSynchedData() {
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
