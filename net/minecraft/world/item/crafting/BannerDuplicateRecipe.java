package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class BannerDuplicateRecipe extends CustomRecipe {
    public BannerDuplicateRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        DyeColor bku4 = null;
        ItemStack bly5 = null;
        ItemStack bly6 = null;
        for (int integer7 = 0; integer7 < bil.getContainerSize(); ++integer7) {
            final ItemStack bly7 = bil.getItem(integer7);
            final Item blu9 = bly7.getItem();
            if (blu9 instanceof BannerItem) {
                final BannerItem bkb10 = (BannerItem)blu9;
                if (bku4 == null) {
                    bku4 = bkb10.getColor();
                }
                else if (bku4 != bkb10.getColor()) {
                    return false;
                }
                final int integer8 = BannerBlockEntity.getPatternCount(bly7);
                if (integer8 > 6) {
                    return false;
                }
                if (integer8 > 0) {
                    if (bly5 != null) {
                        return false;
                    }
                    bly5 = bly7;
                }
                else {
                    if (bly6 != null) {
                        return false;
                    }
                    bly6 = bly7;
                }
            }
        }
        return bly5 != null && bly6 != null;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        for (int integer3 = 0; integer3 < bil.getContainerSize(); ++integer3) {
            final ItemStack bly4 = bil.getItem(integer3);
            if (!bly4.isEmpty()) {
                final int integer4 = BannerBlockEntity.getPatternCount(bly4);
                if (integer4 > 0 && integer4 <= 6) {
                    final ItemStack bly5 = bly4.copy();
                    bly5.setCount(1);
                    return bly5;
                }
            }
        }
        return ItemStack.EMPTY;
    }
    
    public NonNullList<ItemStack> getRemainingItems(final CraftingContainer bil) {
        final NonNullList<ItemStack> gj3 = NonNullList.<ItemStack>withSize(bil.getContainerSize(), ItemStack.EMPTY);
        for (int integer4 = 0; integer4 < gj3.size(); ++integer4) {
            final ItemStack bly5 = bil.getItem(integer4);
            if (!bly5.isEmpty()) {
                if (bly5.getItem().hasCraftingRemainingItem()) {
                    gj3.set(integer4, new ItemStack(bly5.getItem().getCraftingRemainingItem()));
                }
                else if (bly5.hasTag() && BannerBlockEntity.getPatternCount(bly5) > 0) {
                    final ItemStack bly6 = bly5.copy();
                    bly6.setCount(1);
                    gj3.set(integer4, bly6);
                }
            }
        }
        return gj3;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.BANNER_DUPLICATE;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
}
