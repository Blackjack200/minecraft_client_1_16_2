package net.minecraft.recipebook;

import net.minecraft.world.inventory.Slot;
import java.util.Iterator;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.Container;

public class ServerPlaceSmeltingRecipe<C extends Container> extends ServerPlaceRecipe<C> {
    private boolean recipeMatchesPlaced;
    
    public ServerPlaceSmeltingRecipe(final RecipeBookMenu<C> bjg) {
        super(bjg);
    }
    
    @Override
    protected void handleRecipeClicked(final Recipe<C> bon, final boolean boolean2) {
        this.recipeMatchesPlaced = this.menu.recipeMatches(bon);
        final int integer4 = this.stackedContents.getBiggestCraftableStack(bon, null);
        if (this.recipeMatchesPlaced) {
            final ItemStack bly5 = this.menu.getSlot(0).getItem();
            if (bly5.isEmpty() || integer4 <= bly5.getCount()) {
                return;
            }
        }
        final int integer5 = this.getStackSize(boolean2, integer4, this.recipeMatchesPlaced);
        final IntList intList6 = (IntList)new IntArrayList();
        if (!this.stackedContents.canCraft(bon, intList6, integer5)) {
            return;
        }
        if (!this.recipeMatchesPlaced) {
            this.moveItemToInventory(this.menu.getResultSlotIndex());
            this.moveItemToInventory(0);
        }
        this.placeRecipe(integer5, intList6);
    }
    
    @Override
    protected void clearGrid() {
        this.moveItemToInventory(this.menu.getResultSlotIndex());
        super.clearGrid();
    }
    
    protected void placeRecipe(final int integer, final IntList intList) {
        final Iterator<Integer> iterator4 = (Iterator<Integer>)intList.iterator();
        final Slot bjo5 = this.menu.getSlot(0);
        final ItemStack bly6 = StackedContents.fromStackingIndex((int)iterator4.next());
        if (bly6.isEmpty()) {
            return;
        }
        int integer2 = Math.min(bly6.getMaxStackSize(), integer);
        if (this.recipeMatchesPlaced) {
            integer2 -= bjo5.getItem().getCount();
        }
        for (int integer3 = 0; integer3 < integer2; ++integer3) {
            this.moveItemToGrid(bjo5, bly6);
        }
    }
}
