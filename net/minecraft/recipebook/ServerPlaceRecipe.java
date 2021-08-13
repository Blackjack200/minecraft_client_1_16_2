package net.minecraft.recipebook;

import org.apache.logging.log4j.LogManager;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.inventory.Slot;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import it.unimi.dsi.fastutil.ints.IntList;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.Container;

public class ServerPlaceRecipe<C extends Container> implements PlaceRecipe<Integer> {
    protected static final Logger LOGGER;
    protected final StackedContents stackedContents;
    protected Inventory inventory;
    protected RecipeBookMenu<C> menu;
    
    public ServerPlaceRecipe(final RecipeBookMenu<C> bjg) {
        this.stackedContents = new StackedContents();
        this.menu = bjg;
    }
    
    public void recipeClicked(final ServerPlayer aah, @Nullable final Recipe<C> bon, final boolean boolean3) {
        if (bon == null || !aah.getRecipeBook().contains(bon)) {
            return;
        }
        this.inventory = aah.inventory;
        if (!this.testClearGrid() && !aah.isCreative()) {
            return;
        }
        this.stackedContents.clear();
        aah.inventory.fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        if (this.stackedContents.canCraft(bon, null)) {
            this.handleRecipeClicked(bon, boolean3);
        }
        else {
            this.clearGrid();
            aah.connection.send(new ClientboundPlaceGhostRecipePacket(aah.containerMenu.containerId, bon));
        }
        aah.inventory.setChanged();
    }
    
    protected void clearGrid() {
        for (int integer2 = 0; integer2 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++integer2) {
            if (integer2 == this.menu.getResultSlotIndex()) {
                if (this.menu instanceof CraftingMenu) {
                    continue;
                }
                if (this.menu instanceof InventoryMenu) {
                    continue;
                }
            }
            this.moveItemToInventory(integer2);
        }
        this.menu.clearCraftingContent();
    }
    
    protected void moveItemToInventory(final int integer) {
        final ItemStack bly3 = this.menu.getSlot(integer).getItem();
        if (bly3.isEmpty()) {
            return;
        }
        while (bly3.getCount() > 0) {
            int integer2 = this.inventory.getSlotWithRemainingSpace(bly3);
            if (integer2 == -1) {
                integer2 = this.inventory.getFreeSlot();
            }
            final ItemStack bly4 = bly3.copy();
            bly4.setCount(1);
            if (!this.inventory.add(integer2, bly4)) {
                ServerPlaceRecipe.LOGGER.error("Can't find any space for item in the inventory");
            }
            this.menu.getSlot(integer).remove(1);
        }
    }
    
    protected void handleRecipeClicked(final Recipe<C> bon, final boolean boolean2) {
        final boolean boolean3 = this.menu.recipeMatches(bon);
        final int integer5 = this.stackedContents.getBiggestCraftableStack(bon, null);
        if (boolean3) {
            for (int integer6 = 0; integer6 < this.menu.getGridHeight() * this.menu.getGridWidth() + 1; ++integer6) {
                if (integer6 != this.menu.getResultSlotIndex()) {
                    final ItemStack bly7 = this.menu.getSlot(integer6).getItem();
                    if (!bly7.isEmpty() && Math.min(integer5, bly7.getMaxStackSize()) < bly7.getCount() + 1) {
                        return;
                    }
                }
            }
        }
        int integer6 = this.getStackSize(boolean2, integer5, boolean3);
        final IntList intList7 = (IntList)new IntArrayList();
        if (this.stackedContents.canCraft(bon, intList7, integer6)) {
            int integer7 = integer6;
            for (final int integer8 : intList7) {
                final int integer9 = StackedContents.fromStackingIndex(integer8).getMaxStackSize();
                if (integer9 < integer7) {
                    integer7 = integer9;
                }
            }
            integer6 = integer7;
            if (this.stackedContents.canCraft(bon, intList7, integer6)) {
                this.clearGrid();
                this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), bon, (java.util.Iterator<Integer>)intList7.iterator(), integer6);
            }
        }
    }
    
    public void addItemToSlot(final Iterator<Integer> iterator, final int integer2, final int integer3, final int integer4, final int integer5) {
        final Slot bjo7 = this.menu.getSlot(integer2);
        final ItemStack bly8 = StackedContents.fromStackingIndex((int)iterator.next());
        if (!bly8.isEmpty()) {
            for (int integer6 = 0; integer6 < integer3; ++integer6) {
                this.moveItemToGrid(bjo7, bly8);
            }
        }
    }
    
    protected int getStackSize(final boolean boolean1, final int integer, final boolean boolean3) {
        int integer2 = 1;
        if (boolean1) {
            integer2 = integer;
        }
        else if (boolean3) {
            integer2 = 64;
            for (int integer3 = 0; integer3 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++integer3) {
                if (integer3 != this.menu.getResultSlotIndex()) {
                    final ItemStack bly7 = this.menu.getSlot(integer3).getItem();
                    if (!bly7.isEmpty() && integer2 > bly7.getCount()) {
                        integer2 = bly7.getCount();
                    }
                }
            }
            if (integer2 < 64) {
                ++integer2;
            }
        }
        return integer2;
    }
    
    protected void moveItemToGrid(final Slot bjo, final ItemStack bly) {
        final int integer4 = this.inventory.findSlotMatchingUnusedItem(bly);
        if (integer4 == -1) {
            return;
        }
        final ItemStack bly2 = this.inventory.getItem(integer4).copy();
        if (bly2.isEmpty()) {
            return;
        }
        if (bly2.getCount() > 1) {
            this.inventory.removeItem(integer4, 1);
        }
        else {
            this.inventory.removeItemNoUpdate(integer4);
        }
        bly2.setCount(1);
        if (bjo.getItem().isEmpty()) {
            bjo.set(bly2);
        }
        else {
            bjo.getItem().grow(1);
        }
    }
    
    private boolean testClearGrid() {
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayList();
        final int integer3 = this.getAmountOfFreeSlotsInInventory();
        for (int integer4 = 0; integer4 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++integer4) {
            if (integer4 != this.menu.getResultSlotIndex()) {
                final ItemStack bly5 = this.menu.getSlot(integer4).getItem().copy();
                if (!bly5.isEmpty()) {
                    final int integer5 = this.inventory.getSlotWithRemainingSpace(bly5);
                    if (integer5 == -1 && list2.size() <= integer3) {
                        for (final ItemStack bly6 : list2) {
                            if (bly6.sameItem(bly5) && bly6.getCount() != bly6.getMaxStackSize() && bly6.getCount() + bly5.getCount() <= bly6.getMaxStackSize()) {
                                bly6.grow(bly5.getCount());
                                bly5.setCount(0);
                                break;
                            }
                        }
                        if (!bly5.isEmpty()) {
                            if (list2.size() >= integer3) {
                                return false;
                            }
                            list2.add(bly5);
                        }
                    }
                    else if (integer5 == -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private int getAmountOfFreeSlotsInInventory() {
        int integer2 = 0;
        for (final ItemStack bly4 : this.inventory.items) {
            if (bly4.isEmpty()) {
                ++integer2;
            }
        }
        return integer2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
