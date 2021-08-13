package net.minecraft.world.item.enchantment;

import net.minecraft.util.WeighedRandom;

public class EnchantmentInstance extends WeighedRandom.WeighedRandomItem {
    public final Enchantment enchantment;
    public final int level;
    
    public EnchantmentInstance(final Enchantment bpp, final int integer) {
        super(bpp.getRarity().getWeight());
        this.enchantment = bpp;
        this.level = integer;
    }
}
