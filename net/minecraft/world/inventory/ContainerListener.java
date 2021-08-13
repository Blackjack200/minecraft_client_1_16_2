package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public interface ContainerListener {
    void refreshContainer(final AbstractContainerMenu bhz, final NonNullList<ItemStack> gj);
    
    void slotChanged(final AbstractContainerMenu bhz, final int integer, final ItemStack bly);
    
    void setContainerData(final AbstractContainerMenu bhz, final int integer2, final int integer3);
}
