package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;

public interface Recipe<C extends Container> {
    boolean matches(final C aok, final Level bru);
    
    ItemStack assemble(final C aok);
    
    boolean canCraftInDimensions(final int integer1, final int integer2);
    
    ItemStack getResultItem();
    
    default NonNullList<ItemStack> getRemainingItems(final C aok) {
        final NonNullList<ItemStack> gj3 = NonNullList.<ItemStack>withSize(aok.getContainerSize(), ItemStack.EMPTY);
        for (int integer4 = 0; integer4 < gj3.size(); ++integer4) {
            final Item blu5 = aok.getItem(integer4).getItem();
            if (blu5.hasCraftingRemainingItem()) {
                gj3.set(integer4, new ItemStack(blu5.getCraftingRemainingItem()));
            }
        }
        return gj3;
    }
    
    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.<Ingredient>create();
    }
    
    default boolean isSpecial() {
        return false;
    }
    
    default String getGroup() {
        return "";
    }
    
    default ItemStack getToastSymbol() {
        return new ItemStack(Blocks.CRAFTING_TABLE);
    }
    
    ResourceLocation getId();
    
    RecipeSerializer<?> getSerializer();
    
    RecipeType<?> getType();
}
