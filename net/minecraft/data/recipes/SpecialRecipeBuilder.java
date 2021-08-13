package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class SpecialRecipeBuilder {
    private final SimpleRecipeSerializer<?> serializer;
    
    public SpecialRecipeBuilder(final SimpleRecipeSerializer<?> box) {
        this.serializer = box;
    }
    
    public static SpecialRecipeBuilder special(final SimpleRecipeSerializer<?> box) {
        return new SpecialRecipeBuilder(box);
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final String string) {
        consumer.accept(new FinishedRecipe() {
            public void serializeRecipeData(final JsonObject jsonObject) {
            }
            
            public RecipeSerializer<?> getType() {
                return SpecialRecipeBuilder.this.serializer;
            }
            
            public ResourceLocation getId() {
                return new ResourceLocation(string);
            }
            
            @Nullable
            public JsonObject serializeAdvancement() {
                return null;
            }
            
            public ResourceLocation getAdvancementId() {
                return new ResourceLocation("");
            }
        });
    }
}
