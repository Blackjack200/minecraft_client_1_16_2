package net.minecraft.world.effect;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;

public class AbsoptionMobEffect extends MobEffect {
    protected AbsoptionMobEffect(final MobEffectCategory apq, final int integer) {
        super(apq, integer);
    }
    
    @Override
    public void removeAttributeModifiers(final LivingEntity aqj, final AttributeMap arf, final int integer) {
        aqj.setAbsorptionAmount(aqj.getAbsorptionAmount() - 4 * (integer + 1));
        super.removeAttributeModifiers(aqj, arf, integer);
    }
    
    @Override
    public void addAttributeModifiers(final LivingEntity aqj, final AttributeMap arf, final int integer) {
        aqj.setAbsorptionAmount(aqj.getAbsorptionAmount() + 4 * (integer + 1));
        super.addAttributeModifiers(aqj, arf, integer);
    }
}
