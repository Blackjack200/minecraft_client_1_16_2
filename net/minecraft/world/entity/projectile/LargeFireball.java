package net.minecraft.world.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class LargeFireball extends Fireball {
    public int explosionPower;
    
    public LargeFireball(final EntityType<? extends LargeFireball> aqb, final Level bru) {
        super(aqb, bru);
        this.explosionPower = 1;
    }
    
    public LargeFireball(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(EntityType.FIREBALL, double2, double3, double4, double5, double6, double7, bru);
        this.explosionPower = 1;
    }
    
    public LargeFireball(final Level bru, final LivingEntity aqj, final double double3, final double double4, final double double5) {
        super(EntityType.FIREBALL, aqj, double3, double4, double5, bru);
        this.explosionPower = 1;
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (!this.level.isClientSide) {
            final boolean boolean3 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            this.level.explode(null, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, boolean3, boolean3 ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
            this.remove();
        }
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        if (this.level.isClientSide) {
            return;
        }
        final Entity apx3 = dch.getEntity();
        final Entity apx4 = this.getOwner();
        apx3.hurt(DamageSource.fireball(this, apx4), 6.0f);
        if (apx4 instanceof LivingEntity) {
            this.doEnchantDamageEffects((LivingEntity)apx4, apx3);
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("ExplosionPower", this.explosionPower);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("ExplosionPower", 99)) {
            this.explosionPower = md.getInt("ExplosionPower");
        }
    }
}
