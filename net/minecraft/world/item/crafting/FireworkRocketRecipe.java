package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class FireworkRocketRecipe extends CustomRecipe {
    private static final Ingredient PAPER_INGREDIENT;
    private static final Ingredient GUNPOWDER_INGREDIENT;
    private static final Ingredient STAR_INGREDIENT;
    
    public FireworkRocketRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        boolean boolean4 = false;
        int integer5 = 0;
        for (int integer6 = 0; integer6 < bil.getContainerSize(); ++integer6) {
            final ItemStack bly7 = bil.getItem(integer6);
            if (!bly7.isEmpty()) {
                if (FireworkRocketRecipe.PAPER_INGREDIENT.test(bly7)) {
                    if (boolean4) {
                        return false;
                    }
                    boolean4 = true;
                }
                else if (FireworkRocketRecipe.GUNPOWDER_INGREDIENT.test(bly7)) {
                    if (++integer5 > 3) {
                        return false;
                    }
                }
                else if (!FireworkRocketRecipe.STAR_INGREDIENT.test(bly7)) {
                    return false;
                }
            }
        }
        return boolean4 && integer5 >= 1;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        final ItemStack bly3 = new ItemStack(Items.FIREWORK_ROCKET, 3);
        final CompoundTag md4 = bly3.getOrCreateTagElement("Fireworks");
        final ListTag mj5 = new ListTag();
        int integer6 = 0;
        for (int integer7 = 0; integer7 < bil.getContainerSize(); ++integer7) {
            final ItemStack bly4 = bil.getItem(integer7);
            if (!bly4.isEmpty()) {
                if (FireworkRocketRecipe.GUNPOWDER_INGREDIENT.test(bly4)) {
                    ++integer6;
                }
                else if (FireworkRocketRecipe.STAR_INGREDIENT.test(bly4)) {
                    final CompoundTag md5 = bly4.getTagElement("Explosion");
                    if (md5 != null) {
                        mj5.add(md5);
                    }
                }
            }
        }
        md4.putByte("Flight", (byte)integer6);
        if (!mj5.isEmpty()) {
            md4.put("Explosions", (Tag)mj5);
        }
        return bly3;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    @Override
    public ItemStack getResultItem() {
        return new ItemStack(Items.FIREWORK_ROCKET);
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_ROCKET;
    }
    
    static {
        PAPER_INGREDIENT = Ingredient.of(Items.PAPER);
        GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);
        STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);
    }
}
