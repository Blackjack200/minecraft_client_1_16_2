package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

public abstract class AbstractCookingRecipe implements Recipe<Container> {
    protected final RecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;
    
    public AbstractCookingRecipe(final RecipeType<?> boq, final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly, final float float6, final int integer) {
        this.type = boq;
        this.id = vk;
        this.group = string;
        this.ingredient = bok;
        this.result = bly;
        this.experience = float6;
        this.cookingTime = integer;
    }
    
    public boolean matches(final Container aok, final Level bru) {
        return this.ingredient.test(aok.getItem(0));
    }
    
    public ItemStack assemble(final Container aok) {
        return this.result.copy();
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return true;
    }
    
    public NonNullList<Ingredient> getIngredients() {
        final NonNullList<Ingredient> gj2 = NonNullList.<Ingredient>create();
        gj2.add(this.ingredient);
        return gj2;
    }
    
    public float getExperience() {
        return this.experience;
    }
    
    public ItemStack getResultItem() {
        return this.result;
    }
    
    public String getGroup() {
        return this.group;
    }
    
    public int getCookingTime() {
        return this.cookingTime;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public RecipeType<?> getType() {
        return this.type;
    }
}
