package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class FishingSpeedEnchantment extends Enchantment {
    protected FishingSpeedEnchantment(final Rarity a, final EnchantmentCategory bpq, final EquipmentSlot... arr) {
        super(a, bpq, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 15 + (integer - 1) * 9;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return super.getMinCost(integer) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
