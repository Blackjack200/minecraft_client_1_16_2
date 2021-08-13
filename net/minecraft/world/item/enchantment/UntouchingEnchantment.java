package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class UntouchingEnchantment extends Enchantment {
    protected UntouchingEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.DIGGER, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 15;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return super.getMinCost(integer) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        return super.checkCompatibility(bpp) && bpp != Enchantments.BLOCK_FORTUNE;
    }
}
