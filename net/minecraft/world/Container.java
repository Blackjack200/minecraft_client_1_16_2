package net.minecraft.world;

import java.util.Set;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface Container extends Clearable {
    int getContainerSize();
    
    boolean isEmpty();
    
    ItemStack getItem(final int integer);
    
    ItemStack removeItem(final int integer1, final int integer2);
    
    ItemStack removeItemNoUpdate(final int integer);
    
    void setItem(final int integer, final ItemStack bly);
    
    default int getMaxStackSize() {
        return 64;
    }
    
    void setChanged();
    
    boolean stillValid(final Player bft);
    
    default void startOpen(final Player bft) {
    }
    
    default void stopOpen(final Player bft) {
    }
    
    default boolean canPlaceItem(final int integer, final ItemStack bly) {
        return true;
    }
    
    default int countItem(final Item blu) {
        int integer3 = 0;
        for (int integer4 = 0; integer4 < this.getContainerSize(); ++integer4) {
            final ItemStack bly5 = this.getItem(integer4);
            if (bly5.getItem().equals(blu)) {
                integer3 += bly5.getCount();
            }
        }
        return integer3;
    }
    
    default boolean hasAnyOf(final Set<Item> set) {
        for (int integer3 = 0; integer3 < this.getContainerSize(); ++integer3) {
            final ItemStack bly4 = this.getItem(integer3);
            if (set.contains(bly4.getItem()) && bly4.getCount() > 0) {
                return true;
            }
        }
        return false;
    }
}
