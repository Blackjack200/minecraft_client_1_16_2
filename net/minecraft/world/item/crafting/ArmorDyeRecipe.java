package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import java.util.List;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class ArmorDyeRecipe extends CustomRecipe {
    public ArmorDyeRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        ItemStack bly4 = ItemStack.EMPTY;
        final List<ItemStack> list5 = (List<ItemStack>)Lists.newArrayList();
        for (int integer6 = 0; integer6 < bil.getContainerSize(); ++integer6) {
            final ItemStack bly5 = bil.getItem(integer6);
            if (!bly5.isEmpty()) {
                if (bly5.getItem() instanceof DyeableLeatherItem) {
                    if (!bly4.isEmpty()) {
                        return false;
                    }
                    bly4 = bly5;
                }
                else {
                    if (!(bly5.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    list5.add(bly5);
                }
            }
        }
        return !bly4.isEmpty() && !list5.isEmpty();
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        final List<DyeItem> list3 = (List<DyeItem>)Lists.newArrayList();
        ItemStack bly4 = ItemStack.EMPTY;
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly5 = bil.getItem(integer5);
            if (!bly5.isEmpty()) {
                final Item blu7 = bly5.getItem();
                if (blu7 instanceof DyeableLeatherItem) {
                    if (!bly4.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    bly4 = bly5.copy();
                }
                else {
                    if (!(blu7 instanceof DyeItem)) {
                        return ItemStack.EMPTY;
                    }
                    list3.add(blu7);
                }
            }
        }
        if (bly4.isEmpty() || list3.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return DyeableLeatherItem.dyeArmor(bly4, list3);
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.ARMOR_DYE;
    }
}
