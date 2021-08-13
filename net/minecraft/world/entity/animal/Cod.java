package net.minecraft.world.entity.animal;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Cod extends AbstractSchoolingFish {
    public Cod(final EntityType<? extends Cod> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected ItemStack getBucketItemStack() {
        return new ItemStack(Items.COD_BUCKET);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COD_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.COD_HURT;
    }
    
    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }
}
