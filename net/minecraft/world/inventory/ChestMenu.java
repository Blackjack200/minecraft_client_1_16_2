package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class ChestMenu extends AbstractContainerMenu {
    private final Container container;
    private final int containerRows;
    
    private ChestMenu(final MenuType<?> bjb, final int integer2, final Inventory bfs, final int integer4) {
        this(bjb, integer2, bfs, new SimpleContainer(9 * integer4), integer4);
    }
    
    public static ChestMenu oneRow(final int integer, final Inventory bfs) {
        return new ChestMenu(MenuType.GENERIC_9x1, integer, bfs, 1);
    }
    
    public static ChestMenu twoRows(final int integer, final Inventory bfs) {
        return new ChestMenu(MenuType.GENERIC_9x2, integer, bfs, 2);
    }
    
    public static ChestMenu threeRows(final int integer, final Inventory bfs) {
        return new ChestMenu(MenuType.GENERIC_9x3, integer, bfs, 3);
    }
    
    public static ChestMenu fourRows(final int integer, final Inventory bfs) {
        return new ChestMenu(MenuType.GENERIC_9x4, integer, bfs, 4);
    }
    
    public static ChestMenu fiveRows(final int integer, final Inventory bfs) {
        return new ChestMenu(MenuType.GENERIC_9x5, integer, bfs, 5);
    }
    
    public static ChestMenu sixRows(final int integer, final Inventory bfs) {
        return new ChestMenu(MenuType.GENERIC_9x6, integer, bfs, 6);
    }
    
    public static ChestMenu threeRows(final int integer, final Inventory bfs, final Container aok) {
        return new ChestMenu(MenuType.GENERIC_9x3, integer, bfs, aok, 3);
    }
    
    public static ChestMenu sixRows(final int integer, final Inventory bfs, final Container aok) {
        return new ChestMenu(MenuType.GENERIC_9x6, integer, bfs, aok, 6);
    }
    
    public ChestMenu(final MenuType<?> bjb, final int integer2, final Inventory bfs, final Container aok, final int integer5) {
        super(bjb, integer2);
        AbstractContainerMenu.checkContainerSize(aok, integer5 * 9);
        this.container = aok;
        this.containerRows = integer5;
        aok.startOpen(bfs.player);
        final int integer6 = (this.containerRows - 4) * 18;
        for (int integer7 = 0; integer7 < this.containerRows; ++integer7) {
            for (int integer8 = 0; integer8 < 9; ++integer8) {
                this.addSlot(new Slot(aok, integer8 + integer7 * 9, 8 + integer8 * 18, 18 + integer7 * 18));
            }
        }
        for (int integer7 = 0; integer7 < 3; ++integer7) {
            for (int integer8 = 0; integer8 < 9; ++integer8) {
                this.addSlot(new Slot(bfs, integer8 + integer7 * 9 + 9, 8 + integer8 * 18, 103 + integer7 * 18 + integer6));
            }
        }
        for (int integer7 = 0; integer7 < 9; ++integer7) {
            this.addSlot(new Slot(bfs, integer7, 8 + integer7 * 18, 161 + integer6));
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
            if (integer < this.containerRows * 9) {
                if (!this.moveItemStackTo(bly5, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 0, this.containerRows * 9, false)) {
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
    
    public Container getContainer() {
        return this.container;
    }
    
    public int getRowCount() {
        return this.containerRows;
    }
}
