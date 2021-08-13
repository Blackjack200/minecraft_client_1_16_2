package net.minecraft.world.item.crafting;

import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class SimpleCookingSerializer<T extends AbstractCookingRecipe> implements RecipeSerializer<T> {
    private final int defaultCookingTime;
    private final CookieBaker<T> factory;
    
    public SimpleCookingSerializer(final CookieBaker<T> a, final int integer) {
        this.defaultCookingTime = integer;
        this.factory = a;
    }
    
    public T fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
        final String string4 = GsonHelper.getAsString(jsonObject, "group", "");
        final JsonElement jsonElement5 = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient") : GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
        final Ingredient bok6 = Ingredient.fromJson(jsonElement5);
        final String string5 = GsonHelper.getAsString(jsonObject, "result");
        final ResourceLocation vk2 = new ResourceLocation(string5);
        final ItemStack bly9 = new ItemStack((ItemLike)Registry.ITEM.getOptional(vk2).orElseThrow(() -> new IllegalStateException("Item: " + string5 + " does not exist")));
        final float float10 = GsonHelper.getAsFloat(jsonObject, "experience", 0.0f);
        final int integer11 = GsonHelper.getAsInt(jsonObject, "cookingtime", this.defaultCookingTime);
        return this.factory.create(vk, string4, bok6, bly9, float10, integer11);
    }
    
    public T fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf) {
        final String string4 = nf.readUtf(32767);
        final Ingredient bok5 = Ingredient.fromNetwork(nf);
        final ItemStack bly6 = nf.readItem();
        final float float7 = nf.readFloat();
        final int integer8 = nf.readVarInt();
        return this.factory.create(vk, string4, bok5, bly6, float7, integer8);
    }
    
    public void toNetwork(final FriendlyByteBuf nf, final T bnz) {
        nf.writeUtf(bnz.group);
        bnz.ingredient.toNetwork(nf);
        nf.writeItem(bnz.result);
        nf.writeFloat(bnz.experience);
        nf.writeVarInt(bnz.cookingTime);
    }
    
    interface CookieBaker<T extends AbstractCookingRecipe> {
        T create(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly, final float float5, final int integer);
    }
}
