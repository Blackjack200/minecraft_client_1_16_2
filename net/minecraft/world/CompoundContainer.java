package net.minecraft.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CompoundContainer implements Container {
    private final Container container1;
    private final Container container2;
    
    public CompoundContainer(Container aok1, Container aok2) {
        if (aok1 == null) {
            aok1 = aok2;
        }
        if (aok2 == null) {
            aok2 = aok1;
        }
        this.container1 = aok1;
        this.container2 = aok2;
    }
    
    public int getContainerSize() {
        return this.container1.getContainerSize() + this.container2.getContainerSize();
    }
    
    public boolean isEmpty() {
        return this.container1.isEmpty() && this.container2.isEmpty();
    }
    
    public boolean contains(final Container aok) {
        return this.container1 == aok || this.container2 == aok;
    }
    
    public ItemStack getItem(final int integer) {
        if (integer >= this.container1.getContainerSize()) {
            return this.container2.getItem(integer - this.container1.getContainerSize());
        }
        return this.container1.getItem(integer);
    }
    
    public ItemStack removeItem(final int integer1, final int integer2) {
        if (integer1 >= this.container1.getContainerSize()) {
            return this.container2.removeItem(integer1 - this.container1.getContainerSize(), integer2);
        }
        return this.container1.removeItem(integer1, integer2);
    }
    
    public ItemStack removeItemNoUpdate(final int integer) {
        if (integer >= this.container1.getContainerSize()) {
            return this.container2.removeItemNoUpdate(integer - this.container1.getContainerSize());
        }
        return this.container1.removeItemNoUpdate(integer);
    }
    
    public void setItem(final int integer, final ItemStack bly) {
        if (integer >= this.container1.getContainerSize()) {
            this.container2.setItem(integer - this.container1.getContainerSize(), bly);
        }
        else {
            this.container1.setItem(integer, bly);
        }
    }
    
    public int getMaxStackSize() {
        return this.container1.getMaxStackSize();
    }
    
    public void setChanged() {
        this.container1.setChanged();
        this.container2.setChanged();
    }
    
    public boolean stillValid(final Player bft) {
        return this.container1.stillValid(bft) && this.container2.stillValid(bft);
    }
    
    public void startOpen(final Player bft) {
        this.container1.startOpen(bft);
        this.container2.startOpen(bft);
    }
    
    public void stopOpen(final Player bft) {
        this.container1.stopOpen(bft);
        this.container2.stopOpen(bft);
    }
    
    public boolean canPlaceItem(final int integer, final ItemStack bly) {
        if (integer >= this.container1.getContainerSize()) {
            return this.container2.canPlaceItem(integer - this.container1.getContainerSize(), bly);
        }
        return this.container1.canPlaceItem(integer, bly);
    }
    
    public void clearContent() {
        this.container1.clearContent();
        this.container2.clearContent();
    }
}
