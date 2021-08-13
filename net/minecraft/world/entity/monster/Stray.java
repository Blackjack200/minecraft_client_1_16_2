package net.minecraft.world.entity.monster;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Stray extends AbstractSkeleton {
    public Stray(final EntityType<? extends Stray> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public static boolean checkStraySpawnRules(final EntityType<Stray> aqb, final ServerLevelAccessor bsh, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return Monster.checkMonsterSpawnRules(aqb, bsh, aqm, fx, random) && (aqm == MobSpawnType.SPAWNER || bsh.canSeeSky(fx));
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.STRAY_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }
    
    @Override
    SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }
    
    @Override
    protected AbstractArrow getArrow(final ItemStack bly, final float float2) {
        final AbstractArrow bfx4 = super.getArrow(bly, float2);
        if (bfx4 instanceof Arrow) {
            ((Arrow)bfx4).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        }
        return bfx4;
    }
}
