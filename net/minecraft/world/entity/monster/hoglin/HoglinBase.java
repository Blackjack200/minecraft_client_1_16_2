package net.minecraft.world.entity.monster.hoglin;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;

public interface HoglinBase {
    int getAttackAnimationRemainingTicks();
    
    default boolean hurtAndThrowTarget(final LivingEntity aqj1, final LivingEntity aqj2) {
        final float float4 = (float)aqj1.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float float5;
        if (!aqj1.isBaby() && (int)float4 > 0) {
            float5 = float4 / 2.0f + aqj1.level.random.nextInt((int)float4);
        }
        else {
            float5 = float4;
        }
        final boolean boolean5 = aqj2.hurt(DamageSource.mobAttack(aqj1), float5);
        if (boolean5) {
            aqj1.doEnchantDamageEffects(aqj1, aqj2);
            if (!aqj1.isBaby()) {
                throwTarget(aqj1, aqj2);
            }
        }
        return boolean5;
    }
    
    default void throwTarget(final LivingEntity aqj1, final LivingEntity aqj2) {
        final double double3 = aqj1.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        final double double4 = aqj2.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        final double double5 = double3 - double4;
        if (double5 <= 0.0) {
            return;
        }
        final double double6 = aqj2.getX() - aqj1.getX();
        final double double7 = aqj2.getZ() - aqj1.getZ();
        final float float13 = (float)(aqj1.level.random.nextInt(21) - 10);
        final double double8 = double5 * (aqj1.level.random.nextFloat() * 0.5f + 0.2f);
        final Vec3 dck16 = new Vec3(double6, 0.0, double7).normalize().scale(double8).yRot(float13);
        final double double9 = double5 * aqj1.level.random.nextFloat() * 0.5;
        aqj2.push(dck16.x, double9, dck16.z);
        aqj2.hurtMarked = true;
    }
}
