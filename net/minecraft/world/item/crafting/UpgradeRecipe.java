package net.minecraft.world.item.crafting;

import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;

public class UpgradeRecipe implements Recipe<Container> {
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final ResourceLocation id;
    
    public UpgradeRecipe(final ResourceLocation vk, final Ingredient bok2, final Ingredient bok3, final ItemStack bly) {
        this.id = vk;
        this.base = bok2;
        this.addition = bok3;
        this.result = bly;
    }
    
    public boolean matches(final Container aok, final Level bru) {
        return this.base.test(aok.getItem(0)) && this.addition.test(aok.getItem(1));
    }
    
    public ItemStack assemble(final Container aok) {
        final ItemStack bly3 = this.result.copy();
        final CompoundTag md4 = aok.getItem(0).getTag();
        if (md4 != null) {
            bly3.setTag(md4.copy());
        }
        return bly3;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    public ItemStack getResultItem() {
        return this.result;
    }
    
    public boolean isAdditionIngredient(final ItemStack bly) {
        return this.addition.test(bly);
    }
    
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.SMITHING_TABLE);
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMITHING;
    }
    
    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }
    
    public static class Serializer implements RecipeSerializer<UpgradeRecipe> {
        public UpgradeRecipe fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
            final Ingredient bok4 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(jsonObject, "base"));
            final Ingredient bok5 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(jsonObject, "addition"));
            final ItemStack bly6 = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new UpgradeRecipe(vk, bok4, bok5, bly6);
        }
        
        public UpgradeRecipe fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf) {
            final Ingredient bok4 = Ingredient.fromNetwork(nf);
            final Ingredient bok5 = Ingredient.fromNetwork(nf);
            final ItemStack bly6 = nf.readItem();
            return new UpgradeRecipe(vk, bok4, bok5, bly6);
        }
        
        public void toNetwork(final FriendlyByteBuf nf, final UpgradeRecipe bpe) {
            bpe.base.toNetwork(nf);
            bpe.addition.toNetwork(nf);
            nf.writeItem(bpe.result);
        }
    }
}
