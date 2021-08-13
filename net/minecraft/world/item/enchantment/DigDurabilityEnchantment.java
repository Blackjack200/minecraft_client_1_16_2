package net.minecraft.world.item.enchantment;

import net.minecraft.world.item.ArmorItem;
import java.util.Random;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

public class DigDurabilityEnchantment extends Enchantment {
    protected DigDurabilityEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.BREAKABLE, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 5 + (integer - 1) * 8;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return super.getMinCost(integer) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public boolean canEnchant(final ItemStack bly) {
        return bly.isDamageableItem() || super.canEnchant(bly);
    }
    
    public static boolean shouldIgnoreDurabilityDrop(final ItemStack bly, final int integer, final Random random) {
        return (!(bly.getItem() instanceof ArmorItem) || random.nextFloat() >= 0.6f) && random.nextInt(integer + 1) > 0;
    }
}
