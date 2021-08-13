package net.minecraft.world.entity.monster;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Husk extends Zombie {
    public Husk(final EntityType<? extends Husk> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public static boolean checkHuskSpawnRules(final EntityType<Husk> aqb, final ServerLevelAccessor bsh, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return Monster.checkMonsterSpawnRules(aqb, bsh, aqm, fx, random) && (aqm == MobSpawnType.SPAWNER || bsh.canSeeSky(fx));
    }
    
    @Override
    protected boolean isSunSensitive() {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.HUSK_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        final boolean boolean3 = super.doHurtTarget(apx);
        if (boolean3 && this.getMainHandItem().isEmpty() && apx instanceof LivingEntity) {
            final float float4 = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)apx).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)float4));
        }
        return boolean3;
    }
    
    @Override
    protected boolean convertsInWater() {
        return true;
    }
    
    @Override
    protected void doUnderWaterConversion() {
        this.convertToZombieType(EntityType.ZOMBIE);
        if (!this.isSilent()) {
            this.level.levelEvent(null, 1041, this.blockPosition(), 0);
        }
    }
    
    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
