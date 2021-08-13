package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Skeleton extends AbstractSkeleton {
    public Skeleton(final EntityType<? extends Skeleton> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.SKELETON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }
    
    @Override
    SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        final Entity apx5 = aph.getEntity();
        if (apx5 instanceof Creeper) {
            final Creeper bcz6 = (Creeper)apx5;
            if (bcz6.canDropMobsSkull()) {
                bcz6.increaseDroppedSkulls();
                this.spawnAtLocation(Items.SKELETON_SKULL);
            }
        }
    }
}
