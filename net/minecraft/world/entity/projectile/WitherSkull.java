package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;

public class WitherSkull extends AbstractHurtingProjectile {
    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS;
    
    public WitherSkull(final EntityType<? extends WitherSkull> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public WitherSkull(final Level bru, final LivingEntity aqj, final double double3, final double double4, final double double5) {
        super(EntityType.WITHER_SKULL, aqj, double3, double4, double5, bru);
    }
    
    public WitherSkull(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(EntityType.WITHER_SKULL, double2, double3, double4, double5, double6, double7, bru);
    }
    
    @Override
    protected float getInertia() {
        return this.isDangerous() ? 0.73f : super.getInertia();
    }
    
    @Override
    public boolean isOnFire() {
        return false;
    }
    
    @Override
    public float getBlockExplosionResistance(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final FluidState cuu, final float float6) {
        if (this.isDangerous() && WitherBoss.canDestroy(cee)) {
            return Math.min(0.8f, float6);
        }
        return float6;
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        if (this.level.isClientSide) {
            return;
        }
        final Entity apx3 = dch.getEntity();
        final Entity apx4 = this.getOwner();
        boolean boolean5;
        if (apx4 instanceof LivingEntity) {
            final LivingEntity aqj6 = (LivingEntity)apx4;
            boolean5 = apx3.hurt(DamageSource.witherSkull(this, aqj6), 8.0f);
            if (boolean5) {
                if (apx3.isAlive()) {
                    this.doEnchantDamageEffects(aqj6, apx3);
                }
                else {
                    aqj6.heal(5.0f);
                }
            }
        }
        else {
            boolean5 = apx3.hurt(DamageSource.MAGIC, 5.0f);
        }
        if (boolean5 && apx3 instanceof LivingEntity) {
            int integer6 = 0;
            if (this.level.getDifficulty() == Difficulty.NORMAL) {
                integer6 = 10;
            }
            else if (this.level.getDifficulty() == Difficulty.HARD) {
                integer6 = 40;
            }
            if (integer6 > 0) {
                ((LivingEntity)apx3).addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * integer6, 1));
            }
        }
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (!this.level.isClientSide) {
            final Explosion.BlockInteraction a3 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, a3);
            this.remove();
        }
    }
    
    @Override
    public boolean isPickable() {
        return false;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<Boolean>define(WitherSkull.DATA_DANGEROUS, false);
    }
    
    public boolean isDangerous() {
        return this.entityData.<Boolean>get(WitherSkull.DATA_DANGEROUS);
    }
    
    public void setDangerous(final boolean boolean1) {
        this.entityData.<Boolean>set(WitherSkull.DATA_DANGEROUS, boolean1);
    }
    
    @Override
    protected boolean shouldBurn() {
        return false;
    }
    
    static {
        DATA_DANGEROUS = SynchedEntityData.<Boolean>defineId(WitherSkull.class, EntityDataSerializers.BOOLEAN);
    }
}
