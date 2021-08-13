package net.minecraft.world.item.crafting;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;

public abstract class SingleItemRecipe implements Recipe<Container> {
    protected final Ingredient ingredient;
    protected final ItemStack result;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final ResourceLocation id;
    protected final String group;
    
    public SingleItemRecipe(final RecipeType<?> boq, final RecipeSerializer<?> bop, final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly) {
        this.type = boq;
        this.serializer = bop;
        this.id = vk;
        this.group = string;
        this.ingredient = bok;
        this.result = bly;
    }
    
    public RecipeType<?> getType() {
        return this.type;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public String getGroup() {
        return this.group;
    }
    
    public ItemStack getResultItem() {
        return this.result;
    }
    
    public NonNullList<Ingredient> getIngredients() {
        final NonNullList<Ingredient> gj2 = NonNullList.<Ingredient>create();
        gj2.add(this.ingredient);
        return gj2;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return true;
    }
    
    public ItemStack assemble(final Container aok) {
        return this.result.copy();
    }
    
    public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<T> {
        final SingleItemMaker<T> factory;
        
        protected Serializer(final SingleItemMaker<T> a) {
            this.factory = a;
        }
        
        public T fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
            final String string4 = GsonHelper.getAsString(jsonObject, "group", "");
            Ingredient bok5;
            if (GsonHelper.isArrayNode(jsonObject, "ingredient")) {
                bok5 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonArray(jsonObject, "ingredient"));
            }
            else {
                bok5 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            }
            final String string5 = GsonHelper.getAsString(jsonObject, "result");
            final int integer7 = GsonHelper.getAsInt(jsonObject, "count");
            final ItemStack bly8 = new ItemStack(Registry.ITEM.get(new ResourceLocation(string5)), integer7);
            return this.factory.create(vk, string4, bok5, bly8);
        }
        
        public T fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf) {
            final String string4 = nf.readUtf(32767);
            final Ingredient bok5 = Ingredient.fromNetwork(nf);
            final ItemStack bly6 = nf.readItem();
            return this.factory.create(vk, string4, bok5, bly6);
        }
        
        public void toNetwork(final FriendlyByteBuf nf, final T boy) {
            nf.writeUtf(boy.group);
            boy.ingredient.toNetwork(nf);
            nf.writeItem(boy.result);
        }
        
        interface SingleItemMaker<T extends SingleItemRecipe> {
            T create(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly);
        }
        
        interface SingleItemMaker<T extends SingleItemRecipe> {
            T create(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly);
        }
    }
    
    public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<T> {
        final SingleItemMaker<T> factory;
        
        protected Serializer(final SingleItemMaker<T> a) {
            this.factory = a;
        }
        
        public T fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
            final String string4 = GsonHelper.getAsString(jsonObject, "group", "");
            Ingredient bok5;
            if (GsonHelper.isArrayNode(jsonObject, "ingredient")) {
                bok5 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonArray(jsonObject, "ingredient"));
            }
            else {
                bok5 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            }
            final String string5 = GsonHelper.getAsString(jsonObject, "result");
            final int integer7 = GsonHelper.getAsInt(jsonObject, "count");
            final ItemStack bly8 = new ItemStack(Registry.ITEM.get(new ResourceLocation(string5)), integer7);
            return this.factory.create(vk, string4, bok5, bly8);
        }
        
        public T fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf) {
            final String string4 = nf.readUtf(32767);
            final Ingredient bok5 = Ingredient.fromNetwork(nf);
            final ItemStack bly6 = nf.readItem();
            return this.factory.create(vk, string4, bok5, bly6);
        }
        
        public void toNetwork(final FriendlyByteBuf nf, final T boy) {
            nf.writeUtf(boy.group);
            boy.ingredient.toNetwork(nf);
            nf.writeItem(boy.result);
        }
        
        interface SingleItemMaker<T extends SingleItemRecipe> {
            T create(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly);
        }
    }
}
