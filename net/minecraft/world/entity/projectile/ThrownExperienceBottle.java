package net.minecraft.world.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ThrownExperienceBottle extends ThrowableItemProjectile {
    public ThrownExperienceBottle(final EntityType<? extends ThrownExperienceBottle> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public ThrownExperienceBottle(final Level bru, final LivingEntity aqj) {
        super(EntityType.EXPERIENCE_BOTTLE, aqj, bru);
    }
    
    public ThrownExperienceBottle(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.EXPERIENCE_BOTTLE, double2, double3, double4, bru);
    }
    
    @Override
    protected Item getDefaultItem() {
        return Items.EXPERIENCE_BOTTLE;
    }
    
    @Override
    protected float getGravity() {
        return 0.07f;
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (!this.level.isClientSide) {
            this.level.levelEvent(2002, this.blockPosition(), PotionUtils.getColor(Potions.WATER));
            int integer3 = 3 + this.level.random.nextInt(5) + this.level.random.nextInt(5);
            while (integer3 > 0) {
                final int integer4 = ExperienceOrb.getExperienceValue(integer3);
                integer3 -= integer4;
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), integer4));
            }
            this.remove();
        }
    }
}
