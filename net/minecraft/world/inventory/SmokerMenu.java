package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.entity.player.Inventory;

public class SmokerMenu extends AbstractFurnaceMenu {
    public SmokerMenu(final int integer, final Inventory bfs) {
        super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.SMOKER, integer, bfs);
    }
    
    public SmokerMenu(final int integer, final Inventory bfs, final Container aok, final ContainerData bii) {
        super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.SMOKER, integer, bfs, aok, bii);
    }
}
