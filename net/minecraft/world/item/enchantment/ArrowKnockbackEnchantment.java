package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowKnockbackEnchantment extends Enchantment {
    public ArrowKnockbackEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.BOW, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 12 + (integer - 1) * 20;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
