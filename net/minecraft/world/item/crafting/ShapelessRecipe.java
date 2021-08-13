package net.minecraft.world.item.crafting;

import java.util.Iterator;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class ShapelessRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    
    public ShapelessRecipe(final ResourceLocation vk, final String string, final ItemStack bly, final NonNullList<Ingredient> gj) {
        this.id = vk;
        this.group = string;
        this.result = bly;
        this.ingredients = gj;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHAPELESS_RECIPE;
    }
    
    public String getGroup() {
        return this.group;
    }
    
    public ItemStack getResultItem() {
        return this.result;
    }
    
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        final StackedContents bfv4 = new StackedContents();
        int integer5 = 0;
        for (int integer6 = 0; integer6 < bil.getContainerSize(); ++integer6) {
            final ItemStack bly7 = bil.getItem(integer6);
            if (!bly7.isEmpty()) {
                ++integer5;
                bfv4.accountStack(bly7, 1);
            }
        }
        return integer5 == this.ingredients.size() && bfv4.canCraft(this, null);
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        return this.result.copy();
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= this.ingredients.size();
    }
    
    public static class Serializer implements RecipeSerializer<ShapelessRecipe> {
        public ShapelessRecipe fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
            final String string4 = GsonHelper.getAsString(jsonObject, "group", "");
            final NonNullList<Ingredient> gj5 = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (gj5.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (gj5.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            }
            final ItemStack bly6 = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new ShapelessRecipe(vk, string4, bly6, gj5);
        }
        
        private static NonNullList<Ingredient> itemsFromJson(final JsonArray jsonArray) {
            final NonNullList<Ingredient> gj2 = NonNullList.<Ingredient>create();
            for (int integer3 = 0; integer3 < jsonArray.size(); ++integer3) {
                final Ingredient bok4 = Ingredient.fromJson(jsonArray.get(integer3));
                if (!bok4.isEmpty()) {
                    gj2.add(bok4);
                }
            }
            return gj2;
        }
        
        public ShapelessRecipe fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf) {
            final String string4 = nf.readUtf(32767);
            final int integer5 = nf.readVarInt();
            final NonNullList<Ingredient> gj6 = NonNullList.<Ingredient>withSize(integer5, Ingredient.EMPTY);
            for (int integer6 = 0; integer6 < gj6.size(); ++integer6) {
                gj6.set(integer6, Ingredient.fromNetwork(nf));
            }
            final ItemStack bly7 = nf.readItem();
            return new ShapelessRecipe(vk, string4, bly7, gj6);
        }
        
        public void toNetwork(final FriendlyByteBuf nf, final ShapelessRecipe bot) {
            nf.writeUtf(bot.group);
            nf.writeVarInt(bot.ingredients.size());
            for (final Ingredient bok5 : bot.ingredients) {
                bok5.toNetwork(nf);
            }
            nf.writeItem(bot.result);
        }
    }
}
