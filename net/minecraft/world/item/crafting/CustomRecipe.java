package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public abstract class CustomRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    
    public CustomRecipe(final ResourceLocation vk) {
        this.id = vk;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public boolean isSpecial() {
        return true;
    }
    
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }
}
