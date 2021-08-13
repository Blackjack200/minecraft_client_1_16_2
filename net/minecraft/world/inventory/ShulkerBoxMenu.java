package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class ShulkerBoxMenu extends AbstractContainerMenu {
    private final Container container;
    
    public ShulkerBoxMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, new SimpleContainer(27));
    }
    
    public ShulkerBoxMenu(final int integer, final Inventory bfs, final Container aok) {
        super(MenuType.SHULKER_BOX, integer);
        AbstractContainerMenu.checkContainerSize(aok, 27);
        (this.container = aok).startOpen(bfs.player);
        final int integer2 = 3;
        final int integer3 = 9;
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            for (int integer5 = 0; integer5 < 9; ++integer5) {
                this.addSlot(new ShulkerBoxSlot(aok, integer5 + integer4 * 9, 8 + integer5 * 18, 18 + integer4 * 18));
            }
        }
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            for (int integer5 = 0; integer5 < 9; ++integer5) {
                this.addSlot(new Slot(bfs, integer5 + integer4 * 9 + 9, 8 + integer5 * 18, 84 + integer4 * 18));
            }
        }
        for (int integer4 = 0; integer4 < 9; ++integer4) {
            this.addSlot(new Slot(bfs, integer4, 8 + integer4 * 18, 142));
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.container.stillValid(bft);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(bly5, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (bly5.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            else {
                bjo5.setChanged();
            }
        }
        return bly4;
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.container.stopOpen(bft);
    }
}
