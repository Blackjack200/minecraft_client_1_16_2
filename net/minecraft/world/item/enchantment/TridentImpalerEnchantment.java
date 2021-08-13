package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.EquipmentSlot;

public class TridentImpalerEnchantment extends Enchantment {
    public TridentImpalerEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.TRIDENT, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return 1 + (integer - 1) * 8;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 20;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float getDamageBonus(final int integer, final MobType aqn) {
        if (aqn == MobType.WATER) {
            return integer * 2.5f;
        }
        return 0.0f;
    }
}
