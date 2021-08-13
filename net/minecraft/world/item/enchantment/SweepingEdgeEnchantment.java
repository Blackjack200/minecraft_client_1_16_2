package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class SweepingEdgeEnchantment extends Enchantment {
    public SweepingEdgeEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.WEAPON, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 5 + (integer - 1) * 9;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    public static float getSweepingDamageRatio(final int integer) {
        return 1.0f - 1.0f / (integer + 1);
    }
}
