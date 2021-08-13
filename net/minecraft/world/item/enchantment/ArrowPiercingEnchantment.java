package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowPiercingEnchantment extends Enchantment {
    public ArrowPiercingEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.CROSSBOW, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 1 + (integer - 1) * 10;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        return super.checkCompatibility(bpp) && bpp != Enchantments.MULTISHOT;
    }
}
