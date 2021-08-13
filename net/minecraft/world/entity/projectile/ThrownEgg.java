package net.minecraft.world.entity.projectile;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ThrownEgg extends ThrowableItemProjectile {
    public ThrownEgg(final EntityType<? extends ThrownEgg> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public ThrownEgg(final Level bru, final LivingEntity aqj) {
        super(EntityType.EGG, aqj, bru);
    }
    
    public ThrownEgg(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.EGG, double2, double3, double4, bru);
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 3) {
            final double double3 = 0.08;
            for (int integer5 = 0; integer5 < 8; ++integer5) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5) * 0.08, (this.random.nextFloat() - 0.5) * 0.08, (this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        dch.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0f);
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (!this.level.isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int integer3 = 1;
                if (this.random.nextInt(32) == 0) {
                    integer3 = 4;
                }
                for (int integer4 = 0; integer4 < integer3; ++integer4) {
                    final Chicken azz5 = EntityType.CHICKEN.create(this.level);
                    azz5.setAge(-24000);
                    azz5.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                    this.level.addFreshEntity(azz5);
                }
            }
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }
    }
    
    @Override
    protected Item getDefaultItem() {
        return Items.EGG;
    }
}
