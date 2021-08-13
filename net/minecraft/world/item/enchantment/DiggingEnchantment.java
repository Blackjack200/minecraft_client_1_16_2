package net.minecraft.world.item.enchantment;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

public class DiggingEnchantment extends Enchantment {
    protected DiggingEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.DIGGER, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 1 + 10 * (integer - 1);
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return super.getMinCost(integer) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public boolean canEnchant(final ItemStack bly) {
        return bly.getItem() == Items.SHEARS || super.canEnchant(bly);
    }
}
