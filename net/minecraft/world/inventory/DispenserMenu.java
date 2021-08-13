package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class DispenserMenu extends AbstractContainerMenu {
    private final Container dispenser;
    
    public DispenserMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, new SimpleContainer(9));
    }
    
    public DispenserMenu(final int integer, final Inventory bfs, final Container aok) {
        super(MenuType.GENERIC_3x3, integer);
        AbstractContainerMenu.checkContainerSize(aok, 9);
        (this.dispenser = aok).startOpen(bfs.player);
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 3; ++integer3) {
                this.addSlot(new Slot(aok, integer3 + integer2 * 3, 62 + integer3 * 18, 17 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 8 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 8 + integer2 * 18, 142));
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.dispenser.stillValid(bft);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer < 9) {
                if (!this.moveItemStackTo(bly5, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
            if (bly5.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            else {
                bjo5.setChanged();
            }
            if (bly5.getCount() == bly4.getCount()) {
                return ItemStack.EMPTY;
            }
            bjo5.onTake(bft, bly5);
        }
        return bly4;
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.dispenser.stopOpen(bft);
    }
}
