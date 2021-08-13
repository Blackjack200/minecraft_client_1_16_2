package net.minecraft.world.inventory;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.recipebook.ServerPlaceSmeltingRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;

public abstract class AbstractFurnaceMenu extends RecipeBookMenu<Container> {
    private final Container container;
    private final ContainerData data;
    protected final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType recipeBookType;
    
    protected AbstractFurnaceMenu(final MenuType<?> bjb, final RecipeType<? extends AbstractCookingRecipe> boq, final RecipeBookType bjh, final int integer, final Inventory bfs) {
        this(bjb, boq, bjh, integer, bfs, new SimpleContainer(3), new SimpleContainerData(4));
    }
    
    protected AbstractFurnaceMenu(final MenuType<?> bjb, final RecipeType<? extends AbstractCookingRecipe> boq, final RecipeBookType bjh, final int integer, final Inventory bfs, final Container aok, final ContainerData bii) {
        super(bjb, integer);
        this.recipeType = boq;
        this.recipeBookType = bjh;
        AbstractContainerMenu.checkContainerSize(aok, 3);
        AbstractContainerMenu.checkContainerDataCount(bii, 4);
        this.container = aok;
        this.data = bii;
        this.level = bfs.player.level;
        this.addSlot(new Slot(aok, 0, 56, 17));
        this.addSlot(new FurnaceFuelSlot(this, aok, 1, 56, 53));
        this.addSlot(new FurnaceResultSlot(bfs.player, aok, 2, 116, 35));
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 8 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 8 + integer2 * 18, 142));
        }
        this.addDataSlots(bii);
    }
    
    @Override
    public void fillCraftSlotsStackedContents(final StackedContents bfv) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(bfv);
        }
    }
    
    @Override
    public void clearCraftingContent() {
        this.container.clearContent();
    }
    
    @Override
    public void handlePlacement(final boolean boolean1, final Recipe<?> bon, final ServerPlayer aah) {
        new ServerPlaceSmeltingRecipe<>(this).recipeClicked(aah, bon, boolean1);
    }
    
    @Override
    public boolean recipeMatches(final Recipe<? super Container> bon) {
        return bon.matches(this.container, this.level);
    }
    
    @Override
    public int getResultSlotIndex() {
        return 2;
    }
    
    @Override
    public int getGridWidth() {
        return 1;
    }
    
    @Override
    public int getGridHeight() {
        return 1;
    }
    
    @Override
    public int getSize() {
        return 3;
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
            if (integer == 2) {
                if (!this.moveItemStackTo(bly5, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (integer == 1 || integer == 0) {
                if (!this.moveItemStackTo(bly5, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.canSmelt(bly5)) {
                if (!this.moveItemStackTo(bly5, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.isFuel(bly5)) {
                if (!this.moveItemStackTo(bly5, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 3 && integer < 30) {
                if (!this.moveItemStackTo(bly5, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 30 && integer < 39 && !this.moveItemStackTo(bly5, 3, 30, false)) {
                return ItemStack.EMPTY;
            }
            if (bly5.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            else {
                bjo5.setChanged();
            }
            if (bly5.getCount() == bly4.getCount()) {
                return ItemStack.EMPTY;
            }
            bjo5.onTake(bft, bly5);
        }
        return bly4;
    }
    
    protected boolean canSmelt(final ItemStack bly) {
        return this.level.getRecipeManager().<SimpleContainer, Recipe>getRecipeFor((RecipeType<Recipe>)this.recipeType, new SimpleContainer(new ItemStack[] { bly }), this.level).isPresent();
    }
    
    protected boolean isFuel(final ItemStack bly) {
        return AbstractFurnaceBlockEntity.isFuel(bly);
    }
    
    public int getBurnProgress() {
        final int integer2 = this.data.get(2);
        final int integer3 = this.data.get(3);
        if (integer3 == 0 || integer2 == 0) {
            return 0;
        }
        return integer2 * 24 / integer3;
    }
    
    public int getLitProgress() {
        int integer2 = this.data.get(1);
        if (integer2 == 0) {
            integer2 = 200;
        }
        return this.data.get(0) * 13 / integer2;
    }
    
    public boolean isLit() {
        return this.data.get(0) > 0;
    }
    
    @Override
    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }
}
