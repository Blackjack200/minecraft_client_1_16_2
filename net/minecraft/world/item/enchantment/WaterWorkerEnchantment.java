package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class WaterWorkerEnchantment extends Enchantment {
    public WaterWorkerEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.ARMOR_HEAD, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 1;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 40;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
