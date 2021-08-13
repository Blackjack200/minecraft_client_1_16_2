package net.minecraft.world.entity.projectile;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Snowball extends ThrowableItemProjectile {
    public Snowball(final EntityType<? extends Snowball> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public Snowball(final Level bru, final LivingEntity aqj) {
        super(EntityType.SNOWBALL, aqj, bru);
    }
    
    public Snowball(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.SNOWBALL, double2, double3, double4, bru);
    }
    
    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }
    
    private ParticleOptions getParticle() {
        final ItemStack bly2 = this.getItemRaw();
        return bly2.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, bly2);
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 3) {
            final ParticleOptions hf3 = this.getParticle();
            for (int integer4 = 0; integer4 < 8; ++integer4) {
                this.level.addParticle(hf3, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        final Entity apx3 = dch.getEntity();
        final int integer4 = (apx3 instanceof Blaze) ? 3 : 0;
        apx3.hurt(DamageSource.thrown(this, this.getOwner()), (float)integer4);
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }
    }
}
