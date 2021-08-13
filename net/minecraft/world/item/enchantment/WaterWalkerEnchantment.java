package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class WaterWalkerEnchantment extends Enchantment {
    public WaterWalkerEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.ARMOR_FEET, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return integer * 10;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        return super.checkCompatibility(bpp) && bpp != Enchantments.FROST_WALKER;
    }
}
