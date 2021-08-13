package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import java.util.Iterator;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;

public class MapExtendingRecipe extends ShapedRecipe {
    public MapExtendingRecipe(final ResourceLocation vk) {
        super(vk, "", 3, 3, NonNullList.<Ingredient>of(Ingredient.EMPTY, Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.FILLED_MAP), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER)), new ItemStack(Items.MAP));
    }
    
    @Override
    public boolean matches(final CraftingContainer bil, final Level bru) {
        if (!super.matches(bil, bru)) {
            return false;
        }
        ItemStack bly4 = ItemStack.EMPTY;
        for (int integer5 = 0; integer5 < bil.getContainerSize() && bly4.isEmpty(); ++integer5) {
            final ItemStack bly5 = bil.getItem(integer5);
            if (bly5.getItem() == Items.FILLED_MAP) {
                bly4 = bly5;
            }
        }
        if (bly4.isEmpty()) {
            return false;
        }
        final MapItemSavedData cxu5 = MapItem.getOrCreateSavedData(bly4, bru);
        return cxu5 != null && !this.isExplorationMap(cxu5) && cxu5.scale < 4;
    }
    
    private boolean isExplorationMap(final MapItemSavedData cxu) {
        if (cxu.decorations != null) {
            for (final MapDecoration cxr4 : cxu.decorations.values()) {
                if (cxr4.getType() == MapDecoration.Type.MANSION || cxr4.getType() == MapDecoration.Type.MONUMENT) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public ItemStack assemble(final CraftingContainer bil) {
        ItemStack bly3 = ItemStack.EMPTY;
        for (int integer4 = 0; integer4 < bil.getContainerSize() && bly3.isEmpty(); ++integer4) {
            final ItemStack bly4 = bil.getItem(integer4);
            if (bly4.getItem() == Items.FILLED_MAP) {
                bly3 = bly4;
            }
        }
        bly3 = bly3.copy();
        bly3.setCount(1);
        bly3.getOrCreateTag().putInt("map_scale_direction", 1);
        return bly3;
    }
    
    public boolean isSpecial() {
        return true;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.MAP_EXTENDING;
    }
}
