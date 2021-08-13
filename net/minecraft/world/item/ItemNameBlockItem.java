package net.minecraft.world.item;

import net.minecraft.world.level.block.Block;

public class ItemNameBlockItem extends BlockItem {
    public ItemNameBlockItem(final Block bul, final Properties a) {
        super(bul, a);
    }
    
    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
