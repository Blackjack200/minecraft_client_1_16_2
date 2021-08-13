package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class HopperMenu extends AbstractContainerMenu {
    private final Container hopper;
    
    public HopperMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, new SimpleContainer(5));
    }
    
    public HopperMenu(final int integer, final Inventory bfs, final Container aok) {
        super(MenuType.HOPPER, integer);
        AbstractContainerMenu.checkContainerSize(this.hopper = aok, 5);
        aok.startOpen(bfs.player);
        final int integer2 = 51;
        for (int integer3 = 0; integer3 < 5; ++integer3) {
            this.addSlot(new Slot(aok, integer3, 44 + integer3 * 18, 20));
        }
        for (int integer3 = 0; integer3 < 3; ++integer3) {
            for (int integer4 = 0; integer4 < 9; ++integer4) {
                this.addSlot(new Slot(bfs, integer4 + integer3 * 9 + 9, 8 + integer4 * 18, integer3 * 18 + 51));
            }
        }
        for (int integer3 = 0; integer3 < 9; ++integer3) {
            this.addSlot(new Slot(bfs, integer3, 8 + integer3 * 18, 109));
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.hopper.stillValid(bft);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer < this.hopper.getContainerSize()) {
                if (!this.moveItemStackTo(bly5, this.hopper.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 0, this.hopper.getContainerSize(), false)) {
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
        this.hopper.stopOpen(bft);
    }
}
