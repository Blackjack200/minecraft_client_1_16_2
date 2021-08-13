package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class FireAspectEnchantment extends Enchantment {
    protected FireAspectEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.WEAPON, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 10 + 20 * (integer - 1);
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return super.getMinCost(integer) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
