package net.minecraft.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.entity.player.Player;
import java.util.Iterator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.StackedContentsCompatible;

public class SimpleContainer implements Container, StackedContentsCompatible {
    private final int size;
    private final NonNullList<ItemStack> items;
    private List<ContainerListener> listeners;
    
    public SimpleContainer(final int integer) {
        this.size = integer;
        this.items = NonNullList.<ItemStack>withSize(integer, ItemStack.EMPTY);
    }
    
    public SimpleContainer(final ItemStack... arr) {
        this.size = arr.length;
        this.items = NonNullList.<ItemStack>of(ItemStack.EMPTY, arr);
    }
    
    public void addListener(final ContainerListener aom) {
        if (this.listeners == null) {
            this.listeners = (List<ContainerListener>)Lists.newArrayList();
        }
        this.listeners.add(aom);
    }
    
    public void removeListener(final ContainerListener aom) {
        this.listeners.remove(aom);
    }
    
    public ItemStack getItem(final int integer) {
        if (integer < 0 || integer >= this.items.size()) {
            return ItemStack.EMPTY;
        }
        return this.items.get(integer);
    }
    
    public List<ItemStack> removeAllItems() {
        final List<ItemStack> list2 = (List<ItemStack>)this.items.stream().filter(bly -> !bly.isEmpty()).collect(Collectors.toList());
        this.clearContent();
        return list2;
    }
    
    public ItemStack removeItem(final int integer1, final int integer2) {
        final ItemStack bly4 = ContainerHelper.removeItem((List<ItemStack>)this.items, integer1, integer2);
        if (!bly4.isEmpty()) {
            this.setChanged();
        }
        return bly4;
    }
    
    public ItemStack removeItemType(final Item blu, final int integer) {
        final ItemStack bly4 = new ItemStack(blu, 0);
        for (int integer2 = this.size - 1; integer2 >= 0; --integer2) {
            final ItemStack bly5 = this.getItem(integer2);
            if (bly5.getItem().equals(blu)) {
                final int integer3 = integer - bly4.getCount();
                final ItemStack bly6 = bly5.split(integer3);
                bly4.grow(bly6.getCount());
                if (bly4.getCount() == integer) {
                    break;
                }
            }
        }
        if (!bly4.isEmpty()) {
            this.setChanged();
        }
        return bly4;
    }
    
    public ItemStack addItem(final ItemStack bly) {
        final ItemStack bly2 = bly.copy();
        this.moveItemToOccupiedSlotsWithSameType(bly2);
        if (bly2.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.moveItemToEmptySlots(bly2);
        if (bly2.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return bly2;
    }
    
    public boolean canAddItem(final ItemStack bly) {
        boolean boolean3 = false;
        for (final ItemStack bly2 : this.items) {
            if (bly2.isEmpty() || (this.isSameItem(bly2, bly) && bly2.getCount() < bly2.getMaxStackSize())) {
                boolean3 = true;
                break;
            }
        }
        return boolean3;
    }
    
    public ItemStack removeItemNoUpdate(final int integer) {
        final ItemStack bly3 = this.items.get(integer);
        if (bly3.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.items.set(integer, ItemStack.EMPTY);
        return bly3;
    }
    
    public void setItem(final int integer, final ItemStack bly) {
        this.items.set(integer, bly);
        if (!bly.isEmpty() && bly.getCount() > this.getMaxStackSize()) {
            bly.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }
    
    public int getContainerSize() {
        return this.size;
    }
    
    public boolean isEmpty() {
        for (final ItemStack bly3 : this.items) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public void setChanged() {
        if (this.listeners != null) {
            for (final ContainerListener aom3 : this.listeners) {
                aom3.containerChanged(this);
            }
        }
    }
    
    public boolean stillValid(final Player bft) {
        return true;
    }
    
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }
    
    public void fillStackedContents(final StackedContents bfv) {
        for (final ItemStack bly4 : this.items) {
            bfv.accountStack(bly4);
        }
    }
    
    public String toString() {
        return ((List)this.items.stream().filter(bly -> !bly.isEmpty()).collect(Collectors.toList())).toString();
    }
    
    private void moveItemToEmptySlots(final ItemStack bly) {
        for (int integer3 = 0; integer3 < this.size; ++integer3) {
            final ItemStack bly2 = this.getItem(integer3);
            if (bly2.isEmpty()) {
                this.setItem(integer3, bly.copy());
                bly.setCount(0);
                return;
            }
        }
    }
    
    private void moveItemToOccupiedSlotsWithSameType(final ItemStack bly) {
        for (int integer3 = 0; integer3 < this.size; ++integer3) {
            final ItemStack bly2 = this.getItem(integer3);
            if (this.isSameItem(bly2, bly)) {
                this.moveItemsBetweenStacks(bly, bly2);
                if (bly.isEmpty()) {
                    return;
                }
            }
        }
    }
    
    private boolean isSameItem(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getItem() == bly2.getItem() && ItemStack.tagMatches(bly1, bly2);
    }
    
    private void moveItemsBetweenStacks(final ItemStack bly1, final ItemStack bly2) {
        final int integer4 = Math.min(this.getMaxStackSize(), bly2.getMaxStackSize());
        final int integer5 = Math.min(bly1.getCount(), integer4 - bly2.getCount());
        if (integer5 > 0) {
            bly2.grow(integer5);
            bly1.shrink(integer5);
            this.setChanged();
        }
    }
    
    public void fromTag(final ListTag mj) {
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final ItemStack bly4 = ItemStack.of(mj.getCompound(integer3));
            if (!bly4.isEmpty()) {
                this.addItem(bly4);
            }
        }
    }
    
    public ListTag createTag() {
        final ListTag mj2 = new ListTag();
        for (int integer3 = 0; integer3 < this.getContainerSize(); ++integer3) {
            final ItemStack bly4 = this.getItem(integer3);
            if (!bly4.isEmpty()) {
                mj2.add(bly4.save(new CompoundTag()));
            }
        }
        return mj2;
    }
}
