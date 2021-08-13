package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;

public interface FinishedRecipe {
    void serializeRecipeData(final JsonObject jsonObject);
    
    default JsonObject serializeRecipe() {
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("type", Registry.RECIPE_SERIALIZER.getKey(this.getType()).toString());
        this.serializeRecipeData(jsonObject2);
        return jsonObject2;
    }
    
    ResourceLocation getId();
    
    RecipeSerializer<?> getType();
    
    @Nullable
    JsonObject serializeAdvancement();
    
    @Nullable
    ResourceLocation getAdvancementId();
}
