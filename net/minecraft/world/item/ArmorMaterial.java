package net.minecraft.world.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;

public interface ArmorMaterial {
    int getDurabilityForSlot(final EquipmentSlot aqc);
    
    int getDefenseForSlot(final EquipmentSlot aqc);
    
    int getEnchantmentValue();
    
    SoundEvent getEquipSound();
    
    Ingredient getRepairIngredient();
    
    String getName();
    
    float getToughness();
    
    float getKnockbackResistance();
}
