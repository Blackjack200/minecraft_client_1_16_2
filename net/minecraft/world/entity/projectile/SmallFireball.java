package net.minecraft.world.entity.projectile;

import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class SmallFireball extends Fireball {
    public SmallFireball(final EntityType<? extends SmallFireball> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public SmallFireball(final Level bru, final LivingEntity aqj, final double double3, final double double4, final double double5) {
        super(EntityType.SMALL_FIREBALL, aqj, double3, double4, double5, bru);
    }
    
    public SmallFireball(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(EntityType.SMALL_FIREBALL, double2, double3, double4, double5, double6, double7, bru);
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        if (this.level.isClientSide) {
            return;
        }
        final Entity apx3 = dch.getEntity();
        if (!apx3.fireImmune()) {
            final Entity apx4 = this.getOwner();
            final int integer5 = apx3.getRemainingFireTicks();
            apx3.setSecondsOnFire(5);
            final boolean boolean6 = apx3.hurt(DamageSource.fireball(this, apx4), 5.0f);
            if (!boolean6) {
                apx3.setRemainingFireTicks(integer5);
            }
            else if (apx4 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)apx4, apx3);
            }
        }
    }
    
    @Override
    protected void onHitBlock(final BlockHitResult dcg) {
        super.onHitBlock(dcg);
        if (this.level.isClientSide) {
            return;
        }
        final Entity apx3 = this.getOwner();
        if (apx3 == null || !(apx3 instanceof Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            final BlockHitResult dcg2 = dcg;
            final BlockPos fx5 = dcg2.getBlockPos().relative(dcg2.getDirection());
            if (this.level.isEmptyBlock(fx5)) {
                this.level.setBlockAndUpdate(fx5, BaseFireBlock.getState(this.level, fx5));
            }
        }
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (!this.level.isClientSide) {
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
}
