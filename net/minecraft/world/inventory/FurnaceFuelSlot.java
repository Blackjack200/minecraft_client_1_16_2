package net.minecraft.world.inventory;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;

public class FurnaceFuelSlot extends Slot {
    private final AbstractFurnaceMenu menu;
    
    public FurnaceFuelSlot(final AbstractFurnaceMenu bia, final Container aok, final int integer3, final int integer4, final int integer5) {
        super(aok, integer3, integer4, integer5);
        this.menu = bia;
    }
    
    @Override
    public boolean mayPlace(final ItemStack bly) {
        return this.menu.isFuel(bly) || isBucket(bly);
    }
    
    @Override
    public int getMaxStackSize(final ItemStack bly) {
        return isBucket(bly) ? 1 : super.getMaxStackSize(bly);
    }
    
    public static boolean isBucket(final ItemStack bly) {
        return bly.getItem() == Items.BUCKET;
    }
}
