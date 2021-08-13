package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Container;

public class LecternMenu extends AbstractContainerMenu {
    private final Container lectern;
    private final ContainerData lecternData;
    
    public LecternMenu(final int integer) {
        this(integer, new SimpleContainer(1), new SimpleContainerData(1));
    }
    
    public LecternMenu(final int integer, final Container aok, final ContainerData bii) {
        super(MenuType.LECTERN, integer);
        AbstractContainerMenu.checkContainerSize(aok, 1);
        AbstractContainerMenu.checkContainerDataCount(bii, 1);
        this.lectern = aok;
        this.lecternData = bii;
        this.addSlot(new Slot(aok, 0, 0, 0) {
            @Override
            public void setChanged() {
                super.setChanged();
                LecternMenu.this.slotsChanged(this.container);
            }
        });
        this.addDataSlots(bii);
    }
    
    @Override
    public boolean clickMenuButton(final Player bft, final int integer) {
        if (integer >= 100) {
            final int integer2 = integer - 100;
            this.setData(0, integer2);
            return true;
        }
        switch (integer) {
            case 2: {
                final int integer2 = this.lecternData.get(0);
                this.setData(0, integer2 + 1);
                return true;
            }
            case 1: {
                final int integer2 = this.lecternData.get(0);
                this.setData(0, integer2 - 1);
                return true;
            }
            case 3: {
                if (!bft.mayBuild()) {
                    return false;
                }
                final ItemStack bly4 = this.lectern.removeItemNoUpdate(0);
                this.lectern.setChanged();
                if (!bft.inventory.add(bly4)) {
                    bft.drop(bly4, false);
                }
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void setData(final int integer1, final int integer2) {
        super.setData(integer1, integer2);
        this.broadcastChanges();
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.lectern.stillValid(bft);
    }
    
    public ItemStack getBook() {
        return this.lectern.getItem(0);
    }
    
    public int getPage() {
        return this.lecternData.get(0);
    }
}
