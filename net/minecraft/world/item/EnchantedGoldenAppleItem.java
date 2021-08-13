package net.minecraft.world.item;

public class EnchantedGoldenAppleItem extends Item {
    public EnchantedGoldenAppleItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
}
