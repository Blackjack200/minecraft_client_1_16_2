package net.minecraft.world.entity.animal;

import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public abstract class AbstractGolem extends PathfinderMob {
    protected AbstractGolem(final EntityType<? extends AbstractGolem> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return false;
    }
}
