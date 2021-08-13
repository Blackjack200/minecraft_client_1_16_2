package net.minecraft.world.effect;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;

public class HealthBoostMobEffect extends MobEffect {
    public HealthBoostMobEffect(final MobEffectCategory apq, final int integer) {
        super(apq, integer);
    }
    
    @Override
    public void removeAttributeModifiers(final LivingEntity aqj, final AttributeMap arf, final int integer) {
        super.removeAttributeModifiers(aqj, arf, integer);
        if (aqj.getHealth() > aqj.getMaxHealth()) {
            aqj.setHealth(aqj.getMaxHealth());
        }
    }
}
