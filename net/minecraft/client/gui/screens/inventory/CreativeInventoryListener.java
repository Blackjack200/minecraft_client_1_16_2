package net.minecraft.client.gui.screens.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ContainerListener;

public class CreativeInventoryListener implements ContainerListener {
    private final Minecraft minecraft;
    
    public CreativeInventoryListener(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void refreshContainer(final AbstractContainerMenu bhz, final NonNullList<ItemStack> gj) {
    }
    
    public void slotChanged(final AbstractContainerMenu bhz, final int integer, final ItemStack bly) {
        this.minecraft.gameMode.handleCreativeModeItemAdd(bly, integer);
    }
    
    public void setContainerData(final AbstractContainerMenu bhz, final int integer2, final int integer3) {
    }
}
