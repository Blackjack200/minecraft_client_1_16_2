package net.minecraft.world.item.crafting;

import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;

public class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    private final Function<ResourceLocation, T> constructor;
    
    public SimpleRecipeSerializer(final Function<ResourceLocation, T> function) {
        this.constructor = function;
    }
    
    public T fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
        return (T)this.constructor.apply(vk);
    }
    
    public T fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf) {
        return (T)this.constructor.apply(vk);
    }
    
    public void toNetwork(final FriendlyByteBuf nf, final T bon) {
    }
}
