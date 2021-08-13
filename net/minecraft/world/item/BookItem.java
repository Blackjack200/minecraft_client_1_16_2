package net.minecraft.world.item;

public class BookItem extends Item {
    public BookItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isEnchantable(final ItemStack bly) {
        return bly.getCount() == 1;
    }
    
    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
