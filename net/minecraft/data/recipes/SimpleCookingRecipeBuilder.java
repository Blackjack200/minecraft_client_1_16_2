package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.function.Consumer;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Item;

public class SimpleCookingRecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement;
    private String group;
    private final SimpleCookingSerializer<?> serializer;
    
    private SimpleCookingRecipeBuilder(final ItemLike brt, final Ingredient bok, final float float3, final int integer, final SimpleCookingSerializer<?> bow) {
        this.advancement = Advancement.Builder.advancement();
        this.result = brt.asItem();
        this.ingredient = bok;
        this.experience = float3;
        this.cookingTime = integer;
        this.serializer = bow;
    }
    
    public static SimpleCookingRecipeBuilder cooking(final Ingredient bok, final ItemLike brt, final float float3, final int integer, final SimpleCookingSerializer<?> bow) {
        return new SimpleCookingRecipeBuilder(brt, bok, float3, integer, bow);
    }
    
    public static SimpleCookingRecipeBuilder blasting(final Ingredient bok, final ItemLike brt, final float float3, final int integer) {
        return cooking(bok, brt, float3, integer, RecipeSerializer.BLASTING_RECIPE);
    }
    
    public static SimpleCookingRecipeBuilder smelting(final Ingredient bok, final ItemLike brt, final float float3, final int integer) {
        return cooking(bok, brt, float3, integer, RecipeSerializer.SMELTING_RECIPE);
    }
    
    public SimpleCookingRecipeBuilder unlockedBy(final String string, final CriterionTriggerInstance ag) {
        this.advancement.addCriterion(string, ag);
        return this;
    }
    
    public void save(final Consumer<FinishedRecipe> consumer) {
        this.save(consumer, Registry.ITEM.getKey(this.result));
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final String string) {
        final ResourceLocation vk4 = Registry.ITEM.getKey(this.result);
        final ResourceLocation vk5 = new ResourceLocation(string);
        if (vk5.equals(vk4)) {
            throw new IllegalStateException(new StringBuilder().append("Recipe ").append(vk5).append(" should remove its 'save' argument").toString());
        }
        this.save(consumer, vk5);
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final ResourceLocation vk) {
        this.ensureValid(vk);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(vk)).rewards(AdvancementRewards.Builder.recipe(vk)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(vk, (this.group == null) ? "" : this.group, this.ingredient, this.result, this.experience, this.cookingTime, this.advancement, new ResourceLocation(vk.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + vk.getPath()), this.serializer));
    }
    
    private void ensureValid(final ResourceLocation vk) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("No way of obtaining recipe ").append(vk).toString());
        }
    }
    
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;
        
        public Result(final ResourceLocation vk1, final String string, final Ingredient bok, final Item blu, final float float5, final int integer, final Advancement.Builder a, final ResourceLocation vk8, final RecipeSerializer<? extends AbstractCookingRecipe> bop) {
            this.id = vk1;
            this.group = string;
            this.ingredient = bok;
            this.result = blu;
            this.experience = float5;
            this.cookingTime = integer;
            this.advancement = a;
            this.advancementId = vk8;
            this.serializer = bop;
        }
        
        public void serializeRecipeData(final JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }
            jsonObject.add("ingredient", this.ingredient.toJson());
            jsonObject.addProperty("result", Registry.ITEM.getKey(this.result).toString());
            jsonObject.addProperty("experience", (Number)this.experience);
            jsonObject.addProperty("cookingtime", (Number)this.cookingTime);
        }
        
        public RecipeSerializer<?> getType() {
            return this.serializer;
        }
        
        public ResourceLocation getId() {
            return this.id;
        }
        
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }
        
        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
