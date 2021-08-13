package net.minecraft.world.item;

public class SimpleFoiledItem extends Item {
    public SimpleFoiledItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
}
