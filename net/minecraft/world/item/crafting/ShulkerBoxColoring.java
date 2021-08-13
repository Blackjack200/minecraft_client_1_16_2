package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class ShulkerBoxColoring extends CustomRecipe {
    public ShulkerBoxColoring(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        int integer4 = 0;
        int integer5 = 0;
        for (int integer6 = 0; integer6 < bil.getContainerSize(); ++integer6) {
            final ItemStack bly7 = bil.getItem(integer6);
            if (!bly7.isEmpty()) {
                if (Block.byItem(bly7.getItem()) instanceof ShulkerBoxBlock) {
                    ++integer4;
                }
                else {
                    if (!(bly7.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    ++integer5;
                }
                if (integer5 > 1 || integer4 > 1) {
                    return false;
                }
            }
        }
        return integer4 == 1 && integer5 == 1;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        ItemStack bly3 = ItemStack.EMPTY;
        DyeItem bkv4 = (DyeItem)Items.WHITE_DYE;
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly4 = bil.getItem(integer5);
            if (!bly4.isEmpty()) {
                final Item blu7 = bly4.getItem();
                if (Block.byItem(blu7) instanceof ShulkerBoxBlock) {
                    bly3 = bly4;
                }
                else if (blu7 instanceof DyeItem) {
                    bkv4 = (DyeItem)blu7;
                }
            }
        }
        final ItemStack bly5 = ShulkerBoxBlock.getColoredItemStack(bkv4.getDyeColor());
        if (bly3.hasTag()) {
            bly5.setTag(bly3.getTag().copy());
        }
        return bly5;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHULKER_BOX_COLORING;
    }
}
