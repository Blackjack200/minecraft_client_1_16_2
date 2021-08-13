package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.function.Consumer;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Item;

public class SingleItemRecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private final Advancement.Builder advancement;
    private String group;
    private final RecipeSerializer<?> type;
    
    public SingleItemRecipeBuilder(final RecipeSerializer<?> bop, final Ingredient bok, final ItemLike brt, final int integer) {
        this.advancement = Advancement.Builder.advancement();
        this.type = bop;
        this.result = brt.asItem();
        this.ingredient = bok;
        this.count = integer;
    }
    
    public static SingleItemRecipeBuilder stonecutting(final Ingredient bok, final ItemLike brt) {
        return new SingleItemRecipeBuilder(RecipeSerializer.STONECUTTER, bok, brt, 1);
    }
    
    public static SingleItemRecipeBuilder stonecutting(final Ingredient bok, final ItemLike brt, final int integer) {
        return new SingleItemRecipeBuilder(RecipeSerializer.STONECUTTER, bok, brt, integer);
    }
    
    public SingleItemRecipeBuilder unlocks(final String string, final CriterionTriggerInstance ag) {
        this.advancement.addCriterion(string, ag);
        return this;
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final String string) {
        final ResourceLocation vk4 = Registry.ITEM.getKey(this.result);
        if (new ResourceLocation(string).equals(vk4)) {
            throw new IllegalStateException("Single Item Recipe " + string + " should remove its 'save' argument");
        }
        this.save(consumer, new ResourceLocation(string));
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final ResourceLocation vk) {
        this.ensureValid(vk);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(vk)).rewards(AdvancementRewards.Builder.recipe(vk)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(vk, this.type, (this.group == null) ? "" : this.group, this.ingredient, this.result, this.count, this.advancement, new ResourceLocation(vk.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + vk.getPath())));
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
        private final int count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;
        
        public Result(final ResourceLocation vk1, final RecipeSerializer<?> bop, final String string, final Ingredient bok, final Item blu, final int integer, final Advancement.Builder a, final ResourceLocation vk8) {
            this.id = vk1;
            this.type = bop;
            this.group = string;
            this.ingredient = bok;
            this.result = blu;
            this.count = integer;
            this.advancement = a;
            this.advancementId = vk8;
        }
        
        public void serializeRecipeData(final JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }
            jsonObject.add("ingredient", this.ingredient.toJson());
            jsonObject.addProperty("result", Registry.ITEM.getKey(this.result).toString());
            jsonObject.addProperty("count", (Number)this.count);
        }
        
        public ResourceLocation getId() {
            return this.id;
        }
        
        public RecipeSerializer<?> getType() {
            return this.type;
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
