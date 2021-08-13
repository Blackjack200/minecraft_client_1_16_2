package net.minecraft.world.entity.projectile;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ThrownEnderpearl extends ThrowableItemProjectile {
    public ThrownEnderpearl(final EntityType<? extends ThrownEnderpearl> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public ThrownEnderpearl(final Level bru, final LivingEntity aqj) {
        super(EntityType.ENDER_PEARL, aqj, bru);
    }
    
    public ThrownEnderpearl(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.ENDER_PEARL, double2, double3, double4, bru);
    }
    
    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_PEARL;
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        dch.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0f);
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        final Entity apx3 = this.getOwner();
        for (int integer4 = 0; integer4 < 32; ++integer4) {
            this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0, this.getZ(), this.random.nextGaussian(), 0.0, this.random.nextGaussian());
        }
        if (!this.level.isClientSide && !this.removed) {
            if (apx3 instanceof ServerPlayer) {
                final ServerPlayer aah4 = (ServerPlayer)apx3;
                if (aah4.connection.getConnection().isConnected() && aah4.level == this.level && !aah4.isSleeping()) {
                    if (this.random.nextFloat() < 0.05f && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                        final Endermite bde5 = EntityType.ENDERMITE.create(this.level);
                        bde5.setPlayerSpawned(true);
                        bde5.moveTo(apx3.getX(), apx3.getY(), apx3.getZ(), apx3.yRot, apx3.xRot);
                        this.level.addFreshEntity(bde5);
                    }
                    if (apx3.isPassenger()) {
                        apx3.stopRiding();
                    }
                    apx3.teleportTo(this.getX(), this.getY(), this.getZ());
                    apx3.fallDistance = 0.0f;
                    apx3.hurt(DamageSource.FALL, 5.0f);
                }
            }
            else if (apx3 != null) {
                apx3.teleportTo(this.getX(), this.getY(), this.getZ());
                apx3.fallDistance = 0.0f;
            }
            this.remove();
        }
    }
    
    @Override
    public void tick() {
        final Entity apx2 = this.getOwner();
        if (apx2 instanceof Player && !apx2.isAlive()) {
            this.remove();
        }
        else {
            super.tick();
        }
    }
    
    @Nullable
    public Entity changeDimension(final ServerLevel aag) {
        final Entity apx3 = this.getOwner();
        if (apx3 != null && apx3.level.dimension() != aag.dimension()) {
            this.setOwner(null);
        }
        return super.changeDimension(aag);
    }
}
