package net.minecraft.world.entity.animal.horse;

import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Donkey extends AbstractChestedHorse {
    public Donkey(final EntityType<? extends Donkey> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.DONKEY_AMBIENT;
    }
    
    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.DONKEY_ANGRY;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.DONKEY_DEATH;
    }
    
    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.DONKEY_EAT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        super.getHurtSound(aph);
        return SoundEvents.DONKEY_HURT;
    }
    
    @Override
    public boolean canMate(final Animal azw) {
        return azw != this && (azw instanceof Donkey || azw instanceof Horse) && this.canParent() && ((AbstractHorse)azw).canParent();
    }
    
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final EntityType<? extends AbstractHorse> aqb4 = (apv instanceof Horse) ? EntityType.MULE : EntityType.DONKEY;
        final AbstractHorse bay5 = (AbstractHorse)aqb4.create(aag);
        this.setOffspringAttributes(apv, bay5);
        return bay5;
    }
}
