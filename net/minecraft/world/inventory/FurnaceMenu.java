package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.entity.player.Inventory;

public class FurnaceMenu extends AbstractFurnaceMenu {
    public FurnaceMenu(final int integer, final Inventory bfs) {
        super(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, integer, bfs);
    }
    
    public FurnaceMenu(final int integer, final Inventory bfs, final Container aok, final ContainerData bii) {
        super(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, integer, bfs, aok, bii);
    }
}
