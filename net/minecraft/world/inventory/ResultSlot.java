package net.minecraft.world.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class ResultSlot extends Slot {
    private final CraftingContainer craftSlots;
    private final Player player;
    private int removeCount;
    
    public ResultSlot(final Player bft, final CraftingContainer bil, final Container aok, final int integer4, final int integer5, final int integer6) {
        super(aok, integer4, integer5, integer6);
        this.player = bft;
        this.craftSlots = bil;
    }
    
    @Override
    public boolean mayPlace(final ItemStack bly) {
        return false;
    }
    
    @Override
    public ItemStack remove(final int integer) {
        if (this.hasItem()) {
            this.removeCount += Math.min(integer, this.getItem().getCount());
        }
        return super.remove(integer);
    }
    
    @Override
    protected void onQuickCraft(final ItemStack bly, final int integer) {
        this.removeCount += integer;
        this.checkTakeAchievements(bly);
    }
    
    @Override
    protected void onSwapCraft(final int integer) {
        this.removeCount += integer;
    }
    
    @Override
    protected void checkTakeAchievements(final ItemStack bly) {
        if (this.removeCount > 0) {
            bly.onCraftedBy(this.player.level, this.player, this.removeCount);
        }
        if (this.container instanceof RecipeHolder) {
            ((RecipeHolder)this.container).awardUsedRecipes(this.player);
        }
        this.removeCount = 0;
    }
    
    @Override
    public ItemStack onTake(final Player bft, final ItemStack bly) {
        this.checkTakeAchievements(bly);
        final NonNullList<ItemStack> gj4 = bft.level.getRecipeManager().<CraftingContainer, CraftingRecipe>getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, bft.level);
        for (int integer5 = 0; integer5 < gj4.size(); ++integer5) {
            ItemStack bly2 = this.craftSlots.getItem(integer5);
            final ItemStack bly3 = gj4.get(integer5);
            if (!bly2.isEmpty()) {
                this.craftSlots.removeItem(integer5, 1);
                bly2 = this.craftSlots.getItem(integer5);
            }
            if (!bly3.isEmpty()) {
                if (bly2.isEmpty()) {
                    this.craftSlots.setItem(integer5, bly3);
                }
                else if (ItemStack.isSame(bly2, bly3) && ItemStack.tagMatches(bly2, bly3)) {
                    bly3.grow(bly2.getCount());
                    this.craftSlots.setItem(integer5, bly3);
                }
                else if (!this.player.inventory.add(bly3)) {
                    this.player.drop(bly3, false);
                }
            }
        }
        return bly;
    }
}
