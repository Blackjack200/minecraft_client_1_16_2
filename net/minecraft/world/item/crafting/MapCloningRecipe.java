package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class MapCloningRecipe extends CustomRecipe {
    public MapCloningRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        int integer4 = 0;
        ItemStack bly5 = ItemStack.EMPTY;
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly6 = bil.getItem(integer5);
            if (!bly6.isEmpty()) {
                if (bly6.getItem() == Items.FILLED_MAP) {
                    if (!bly5.isEmpty()) {
                        return false;
                    }
                    bly5 = bly6;
                }
                else {
                    if (bly6.getItem() != Items.MAP) {
                        return false;
                    }
                    ++integer4;
                }
            }
        }
        return !bly5.isEmpty() && integer4 > 0;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        int integer3 = 0;
        ItemStack bly4 = ItemStack.EMPTY;
        for (int integer4 = 0; integer4 < bil.getContainerSize(); ++integer4) {
            final ItemStack bly5 = bil.getItem(integer4);
            if (!bly5.isEmpty()) {
                if (bly5.getItem() == Items.FILLED_MAP) {
                    if (!bly4.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    bly4 = bly5;
                }
                else {
                    if (bly5.getItem() != Items.MAP) {
                        return ItemStack.EMPTY;
                    }
                    ++integer3;
                }
            }
        }
        if (bly4.isEmpty() || integer3 < 1) {
            return ItemStack.EMPTY;
        }
        final ItemStack bly6 = bly4.copy();
        bly6.setCount(integer3 + 1);
        return bly6;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 >= 3 && integer2 >= 3;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.MAP_CLONING;
    }
}
