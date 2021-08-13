package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowInfiniteEnchantment extends Enchantment {
    public ArrowInfiniteEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.BOW, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 20;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        return !(bpp instanceof MendingEnchantment) && super.checkCompatibility(bpp);
    }
}
