package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class BindingCurseEnchantment extends Enchantment {
    public BindingCurseEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.WEARABLE, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 25;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    
    @Override
    public boolean isCurse() {
        return true;
    }
}
