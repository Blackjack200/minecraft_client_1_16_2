package net.minecraft.world.effect;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import java.util.Iterator;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import java.util.function.Supplier;
import java.util.UUID;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import java.util.Map;

public class MobEffect {
    private final Map<Attribute, AttributeModifier> attributeModifiers;
    private final MobEffectCategory category;
    private final int color;
    @Nullable
    private String descriptionId;
    
    @Nullable
    public static MobEffect byId(final int integer) {
        return Registry.MOB_EFFECT.byId(integer);
    }
    
    public static int getId(final MobEffect app) {
        return Registry.MOB_EFFECT.getId(app);
    }
    
    protected MobEffect(final MobEffectCategory apq, final int integer) {
        this.attributeModifiers = (Map<Attribute, AttributeModifier>)Maps.newHashMap();
        this.category = apq;
        this.color = integer;
    }
    
    public void applyEffectTick(final LivingEntity aqj, final int integer) {
        if (this == MobEffects.REGENERATION) {
            if (aqj.getHealth() < aqj.getMaxHealth()) {
                aqj.heal(1.0f);
            }
        }
        else if (this == MobEffects.POISON) {
            if (aqj.getHealth() > 1.0f) {
                aqj.hurt(DamageSource.MAGIC, 1.0f);
            }
        }
        else if (this == MobEffects.WITHER) {
            aqj.hurt(DamageSource.WITHER, 1.0f);
        }
        else if (this == MobEffects.HUNGER && aqj instanceof Player) {
            ((Player)aqj).causeFoodExhaustion(0.005f * (integer + 1));
        }
        else if (this == MobEffects.SATURATION && aqj instanceof Player) {
            if (!aqj.level.isClientSide) {
                ((Player)aqj).getFoodData().eat(integer + 1, 1.0f);
            }
        }
        else if ((this == MobEffects.HEAL && !aqj.isInvertedHealAndHarm()) || (this == MobEffects.HARM && aqj.isInvertedHealAndHarm())) {
            aqj.heal((float)Math.max(4 << integer, 0));
        }
        else if ((this == MobEffects.HARM && !aqj.isInvertedHealAndHarm()) || (this == MobEffects.HEAL && aqj.isInvertedHealAndHarm())) {
            aqj.hurt(DamageSource.MAGIC, (float)(6 << integer));
        }
    }
    
    public void applyInstantenousEffect(@Nullable final Entity apx1, @Nullable final Entity apx2, final LivingEntity aqj, final int integer, final double double5) {
        if ((this == MobEffects.HEAL && !aqj.isInvertedHealAndHarm()) || (this == MobEffects.HARM && aqj.isInvertedHealAndHarm())) {
            final int integer2 = (int)(double5 * (4 << integer) + 0.5);
            aqj.heal((float)integer2);
        }
        else if ((this == MobEffects.HARM && !aqj.isInvertedHealAndHarm()) || (this == MobEffects.HEAL && aqj.isInvertedHealAndHarm())) {
            final int integer2 = (int)(double5 * (6 << integer) + 0.5);
            if (apx1 == null) {
                aqj.hurt(DamageSource.MAGIC, (float)integer2);
            }
            else {
                aqj.hurt(DamageSource.indirectMagic(apx1, apx2), (float)integer2);
            }
        }
        else {
            this.applyEffectTick(aqj, integer);
        }
    }
    
    public boolean isDurationEffectTick(final int integer1, final int integer2) {
        if (this == MobEffects.REGENERATION) {
            final int integer3 = 50 >> integer2;
            return integer3 <= 0 || integer1 % integer3 == 0;
        }
        if (this == MobEffects.POISON) {
            final int integer3 = 25 >> integer2;
            return integer3 <= 0 || integer1 % integer3 == 0;
        }
        if (this == MobEffects.WITHER) {
            final int integer3 = 40 >> integer2;
            return integer3 <= 0 || integer1 % integer3 == 0;
        }
        return this == MobEffects.HUNGER;
    }
    
    public boolean isInstantenous() {
        return false;
    }
    
    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("effect", Registry.MOB_EFFECT.getKey(this));
        }
        return this.descriptionId;
    }
    
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
    
    public Component getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId());
    }
    
    public MobEffectCategory getCategory() {
        return this.category;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public MobEffect addAttributeModifier(final Attribute ard, final String string, final double double3, final AttributeModifier.Operation a) {
        final AttributeModifier arg7 = new AttributeModifier(UUID.fromString(string), (Supplier<String>)this::getDescriptionId, double3, a);
        this.attributeModifiers.put(ard, arg7);
        return this;
    }
    
    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }
    
    public void removeAttributeModifiers(final LivingEntity aqj, final AttributeMap arf, final int integer) {
        for (final Map.Entry<Attribute, AttributeModifier> entry6 : this.attributeModifiers.entrySet()) {
            final AttributeInstance are7 = arf.getInstance((Attribute)entry6.getKey());
            if (are7 != null) {
                are7.removeModifier((AttributeModifier)entry6.getValue());
            }
        }
    }
    
    public void addAttributeModifiers(final LivingEntity aqj, final AttributeMap arf, final int integer) {
        for (final Map.Entry<Attribute, AttributeModifier> entry6 : this.attributeModifiers.entrySet()) {
            final AttributeInstance are7 = arf.getInstance((Attribute)entry6.getKey());
            if (are7 != null) {
                final AttributeModifier arg8 = (AttributeModifier)entry6.getValue();
                are7.removeModifier(arg8);
                are7.addPermanentModifier(new AttributeModifier(arg8.getId(), this.getDescriptionId() + " " + integer, this.getAttributeModifierValue(integer, arg8), arg8.getOperation()));
            }
        }
    }
    
    public double getAttributeModifierValue(final int integer, final AttributeModifier arg) {
        return arg.getAmount() * (integer + 1);
    }
    
    public boolean isBeneficial() {
        return this.category == MobEffectCategory.BENEFICIAL;
    }
}
