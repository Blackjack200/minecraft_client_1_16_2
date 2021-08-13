package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class TridentRiptideEnchantment extends Enchantment {
    public TridentRiptideEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.TRIDENT, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 10 + integer * 7;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        return super.checkCompatibility(bpp) && bpp != Enchantments.LOYALTY && bpp != Enchantments.CHANNELING;
    }
}
