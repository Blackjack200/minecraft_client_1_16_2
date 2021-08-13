package net.minecraft.world.entity.projectile;

import net.minecraft.world.damagesource.DamageSource;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class DragonFireball extends AbstractHurtingProjectile {
    public DragonFireball(final EntityType<? extends DragonFireball> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public DragonFireball(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(EntityType.DRAGON_FIREBALL, double2, double3, double4, double5, double6, double7, bru);
    }
    
    public DragonFireball(final Level bru, final LivingEntity aqj, final double double3, final double double4, final double double5) {
        super(EntityType.DRAGON_FIREBALL, aqj, double3, double4, double5, bru);
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        final Entity apx3 = this.getOwner();
        if (dci.getType() == HitResult.Type.ENTITY && ((EntityHitResult)dci).getEntity().is(apx3)) {
            return;
        }
        if (!this.level.isClientSide) {
            final List<LivingEntity> list4 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.getBoundingBox().inflate(4.0, 2.0, 4.0));
            final AreaEffectCloud apw5 = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            if (apx3 instanceof LivingEntity) {
                apw5.setOwner((LivingEntity)apx3);
            }
            apw5.setParticle(ParticleTypes.DRAGON_BREATH);
            apw5.setRadius(3.0f);
            apw5.setDuration(600);
            apw5.setRadiusPerTick((7.0f - apw5.getRadius()) / apw5.getDuration());
            apw5.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
            if (!list4.isEmpty()) {
                for (final LivingEntity aqj7 : list4) {
                    final double double8 = this.distanceToSqr(aqj7);
                    if (double8 < 16.0) {
                        apw5.setPos(aqj7.getX(), aqj7.getY(), aqj7.getZ());
                        break;
                    }
                }
            }
            this.level.levelEvent(2006, this.blockPosition(), this.isSilent() ? -1 : 1);
            this.level.addFreshEntity(apw5);
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
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }
    
    @Override
    protected boolean shouldBurn() {
        return false;
    }
}
