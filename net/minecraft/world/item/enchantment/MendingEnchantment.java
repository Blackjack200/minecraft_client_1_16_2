package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class MendingEnchantment extends Enchantment {
    public MendingEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.BREAKABLE, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return integer * 25;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 50;
    }
    
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
