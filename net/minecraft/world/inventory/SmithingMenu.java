package net.minecraft.world.inventory;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.BlockPos;
import java.util.function.BiConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.entity.player.Inventory;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;

public class SmithingMenu extends ItemCombinerMenu {
    private final Level level;
    @Nullable
    private UpgradeRecipe selectedRecipe;
    private final List<UpgradeRecipe> recipes;
    
    public SmithingMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public SmithingMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.SMITHING, integer, bfs, bij);
        this.level = bfs.player.level;
        this.recipes = this.level.getRecipeManager().<Container, UpgradeRecipe>getAllRecipesFor(RecipeType.SMITHING);
    }
    
    @Override
    protected boolean isValidBlock(final BlockState cee) {
        return cee.is(Blocks.SMITHING_TABLE);
    }
    
    @Override
    protected boolean mayPickup(final Player bft, final boolean boolean2) {
        return this.selectedRecipe != null && this.selectedRecipe.matches(this.inputSlots, this.level);
    }
    
    @Override
    protected ItemStack onTake(final Player bft, final ItemStack bly) {
        bly.onCraftedBy(bft.level, bft, bly.getCount());
        this.resultSlots.awardUsedRecipes(bft);
        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> bru.levelEvent(1044, fx, 0)));
        return bly;
    }
    
    private void shrinkStackInSlot(final int integer) {
        final ItemStack bly3 = this.inputSlots.getItem(integer);
        bly3.shrink(1);
        this.inputSlots.setItem(integer, bly3);
    }
    
    @Override
    public void createResult() {
        final List<UpgradeRecipe> list2 = this.level.getRecipeManager().<Container, UpgradeRecipe>getRecipesFor(RecipeType.SMITHING, this.inputSlots, this.level);
        if (list2.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
        else {
            this.selectedRecipe = (UpgradeRecipe)list2.get(0);
            final ItemStack bly3 = this.selectedRecipe.assemble(this.inputSlots);
            this.resultSlots.setRecipeUsed(this.selectedRecipe);
            this.resultSlots.setItem(0, bly3);
        }
    }
    
    @Override
    protected boolean shouldQuickMoveToAdditionalSlot(final ItemStack bly) {
        return this.recipes.stream().anyMatch(bpe -> bpe.isAdditionIngredient(bly));
    }
    
    @Override
    public boolean canTakeItemForPickAll(final ItemStack bly, final Slot bjo) {
        return bjo.container != this.resultSlots && super.canTakeItemForPickAll(bly, bjo);
    }
}
