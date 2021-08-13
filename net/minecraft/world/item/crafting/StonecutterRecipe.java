package net.minecraft.world.item.crafting;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class StonecutterRecipe extends SingleItemRecipe {
    public StonecutterRecipe(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly) {
        super(RecipeType.STONECUTTING, RecipeSerializer.STONECUTTER, vk, string, bok, bly);
    }
    
    public boolean matches(final Container aok, final Level bru) {
        return this.ingredient.test(aok.getItem(0));
    }
    
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.STONECUTTER);
    }
}
