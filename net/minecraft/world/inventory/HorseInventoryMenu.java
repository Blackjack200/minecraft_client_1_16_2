package net.minecraft.world.inventory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.Container;

public class HorseInventoryMenu extends AbstractContainerMenu {
    private final Container horseContainer;
    private final AbstractHorse horse;
    
    public HorseInventoryMenu(final int integer, final Inventory bfs, final Container aok, final AbstractHorse bay) {
        super(null, integer);
        this.horseContainer = aok;
        this.horse = bay;
        final int integer2 = 3;
        aok.startOpen(bfs.player);
        final int integer3 = -18;
        this.addSlot(new Slot(aok, 0, 8, 18) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.getItem() == Items.SADDLE && !this.hasItem() && bay.isSaddleable();
            }
            
            @Override
            public boolean isActive() {
                return bay.isSaddleable();
            }
        });
        this.addSlot(new Slot(aok, 1, 8, 36) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bay.isArmor(bly);
            }
            
            @Override
            public boolean isActive() {
                return bay.canWearArmor();
            }
            
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        if (bay instanceof AbstractChestedHorse && ((AbstractChestedHorse)bay).hasChest()) {
            for (int integer4 = 0; integer4 < 3; ++integer4) {
                for (int integer5 = 0; integer5 < ((AbstractChestedHorse)bay).getInventoryColumns(); ++integer5) {
                    this.addSlot(new Slot(aok, 2 + integer5 + integer4 * ((AbstractChestedHorse)bay).getInventoryColumns(), 80 + integer5 * 18, 18 + integer4 * 18));
                }
            }
        }
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            for (int integer5 = 0; integer5 < 9; ++integer5) {
                this.addSlot(new Slot(bfs, integer5 + integer4 * 9 + 9, 8 + integer5 * 18, 102 + integer4 * 18 - 18));
            }
        }
        for (int integer4 = 0; integer4 < 9; ++integer4) {
            this.addSlot(new Slot(bfs, integer4, 8 + integer4 * 18, 142));
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.horseContainer.stillValid(bft) && this.horse.isAlive() && this.horse.distanceTo(bft) < 8.0f;
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            final int integer2 = this.horseContainer.getContainerSize();
            if (integer < integer2) {
                if (!this.moveItemStackTo(bly5, integer2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(1).mayPlace(bly5) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(bly5, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(0).mayPlace(bly5)) {
                if (!this.moveItemStackTo(bly5, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer2 <= 2 || !this.moveItemStackTo(bly5, 2, integer2, false)) {
                final int integer3 = integer2;
                final int integer5;
                final int integer4 = integer5 = integer3 + 27;
                final int integer6 = integer5 + 9;
                if (integer >= integer5 && integer < integer6) {
                    if (!this.moveItemStackTo(bly5, integer3, integer4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (integer >= integer3 && integer < integer4) {
                    if (!this.moveItemStackTo(bly5, integer5, integer6, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(bly5, integer5, integer4, false)) {
                    return ItemStack.EMPTY;
                }
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
        this.horseContainer.stopOpen(bft);
    }
}
