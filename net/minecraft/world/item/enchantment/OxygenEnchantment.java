package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class OxygenEnchantment extends Enchantment {
    public OxygenEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.ARMOR_HEAD, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 10 * integer;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 30;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
