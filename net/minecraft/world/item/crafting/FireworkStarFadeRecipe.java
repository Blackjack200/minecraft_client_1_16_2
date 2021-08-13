package net.minecraft.world.item.crafting;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class FireworkStarFadeRecipe extends CustomRecipe {
    private static final Ingredient STAR_INGREDIENT;
    
    public FireworkStarFadeRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        boolean boolean4 = false;
        boolean boolean5 = false;
        for (int integer6 = 0; integer6 < bil.getContainerSize(); ++integer6) {
            final ItemStack bly7 = bil.getItem(integer6);
            if (!bly7.isEmpty()) {
                if (bly7.getItem() instanceof DyeItem) {
                    boolean4 = true;
                }
                else {
                    if (!FireworkStarFadeRecipe.STAR_INGREDIENT.test(bly7)) {
                        return false;
                    }
                    if (boolean5) {
                        return false;
                    }
                    boolean5 = true;
                }
            }
        }
        return boolean5 && boolean4;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        final List<Integer> list3 = (List<Integer>)Lists.newArrayList();
        ItemStack bly4 = null;
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly5 = bil.getItem(integer5);
            final Item blu7 = bly5.getItem();
            if (blu7 instanceof DyeItem) {
                list3.add(((DyeItem)blu7).getDyeColor().getFireworkColor());
            }
            else if (FireworkStarFadeRecipe.STAR_INGREDIENT.test(bly5)) {
                bly4 = bly5.copy();
                bly4.setCount(1);
            }
        }
        if (bly4 == null || list3.isEmpty()) {
            return ItemStack.EMPTY;
        }
        bly4.getOrCreateTagElement("Explosion").putIntArray("FadeColors", list3);
        return bly4;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_STAR_FADE;
    }
    
    static {
        STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);
    }
}
