package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowDamageEnchantment extends Enchantment {
    public ArrowDamageEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.BOW, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 1 + (integer - 1) * 10;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
