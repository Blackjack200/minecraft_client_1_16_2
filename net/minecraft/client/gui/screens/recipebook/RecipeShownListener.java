package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.world.item.crafting.Recipe;
import java.util.List;

public interface RecipeShownListener {
    void recipesShown(final List<Recipe<?>> list);
}
