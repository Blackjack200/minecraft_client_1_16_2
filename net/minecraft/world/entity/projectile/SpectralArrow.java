package net.minecraft.world.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class SpectralArrow extends AbstractArrow {
    private int duration;
    
    public SpectralArrow(final EntityType<? extends SpectralArrow> aqb, final Level bru) {
        super(aqb, bru);
        this.duration = 200;
    }
    
    public SpectralArrow(final Level bru, final LivingEntity aqj) {
        super(EntityType.SPECTRAL_ARROW, aqj, bru);
        this.duration = 200;
    }
    
    public SpectralArrow(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.SPECTRAL_ARROW, double2, double3, double4, bru);
        this.duration = 200;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && !this.inGround) {
            this.level.addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }
    
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }
    
    @Override
    protected void doPostHurtEffects(final LivingEntity aqj) {
        super.doPostHurtEffects(aqj);
        final MobEffectInstance apr3 = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
        aqj.addEffect(apr3);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("Duration")) {
            this.duration = md.getInt("Duration");
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Duration", this.duration);
    }
}
