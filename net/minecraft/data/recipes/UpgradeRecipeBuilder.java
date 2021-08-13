package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Consumer;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class UpgradeRecipeBuilder {
    private final Ingredient base;
    private final Ingredient addition;
    private final Item result;
    private final Advancement.Builder advancement;
    private final RecipeSerializer<?> type;
    
    public UpgradeRecipeBuilder(final RecipeSerializer<?> bop, final Ingredient bok2, final Ingredient bok3, final Item blu) {
        this.advancement = Advancement.Builder.advancement();
        this.type = bop;
        this.base = bok2;
        this.addition = bok3;
        this.result = blu;
    }
    
    public static UpgradeRecipeBuilder smithing(final Ingredient bok1, final Ingredient bok2, final Item blu) {
        return new UpgradeRecipeBuilder(RecipeSerializer.SMITHING, bok1, bok2, blu);
    }
    
    public UpgradeRecipeBuilder unlocks(final String string, final CriterionTriggerInstance ag) {
        this.advancement.addCriterion(string, ag);
        return this;
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final String string) {
        this.save(consumer, new ResourceLocation(string));
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final ResourceLocation vk) {
        this.ensureValid(vk);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(vk)).rewards(AdvancementRewards.Builder.recipe(vk)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(vk, this.type, this.base, this.addition, this.result, this.advancement, new ResourceLocation(vk.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + vk.getPath())));
    }
    
    private void ensureValid(final ResourceLocation vk) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("No way of obtaining recipe ").append(vk).toString());
        }
    }
    
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient base;
        private final Ingredient addition;
        private final Item result;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;
        
        public Result(final ResourceLocation vk1, final RecipeSerializer<?> bop, final Ingredient bok3, final Ingredient bok4, final Item blu, final Advancement.Builder a, final ResourceLocation vk7) {
            this.id = vk1;
            this.type = bop;
            this.base = bok3;
            this.addition = bok4;
            this.result = blu;
            this.advancement = a;
            this.advancementId = vk7;
        }
        
        public void serializeRecipeData(final JsonObject jsonObject) {
            jsonObject.add("base", this.base.toJson());
            jsonObject.add("addition", this.addition.toJson());
            final JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            jsonObject.add("result", (JsonElement)jsonObject2);
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
