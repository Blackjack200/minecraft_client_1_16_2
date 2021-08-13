package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class BookCloningRecipe extends CustomRecipe {
    public BookCloningRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        int integer4 = 0;
        ItemStack bly5 = ItemStack.EMPTY;
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly6 = bil.getItem(integer5);
            if (!bly6.isEmpty()) {
                if (bly6.getItem() == Items.WRITTEN_BOOK) {
                    if (!bly5.isEmpty()) {
                        return false;
                    }
                    bly5 = bly6;
                }
                else {
                    if (bly6.getItem() != Items.WRITABLE_BOOK) {
                        return false;
                    }
                    ++integer4;
                }
            }
        }
        return !bly5.isEmpty() && bly5.hasTag() && integer4 > 0;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        int integer3 = 0;
        ItemStack bly4 = ItemStack.EMPTY;
        for (int integer4 = 0; integer4 < bil.getContainerSize(); ++integer4) {
            final ItemStack bly5 = bil.getItem(integer4);
            if (!bly5.isEmpty()) {
                if (bly5.getItem() == Items.WRITTEN_BOOK) {
                    if (!bly4.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    bly4 = bly5;
                }
                else {
                    if (bly5.getItem() != Items.WRITABLE_BOOK) {
                        return ItemStack.EMPTY;
                    }
                    ++integer3;
                }
            }
        }
        if (bly4.isEmpty() || !bly4.hasTag() || integer3 < 1 || WrittenBookItem.getGeneration(bly4) >= 2) {
            return ItemStack.EMPTY;
        }
        final ItemStack bly6 = new ItemStack(Items.WRITTEN_BOOK, integer3);
        final CompoundTag md6 = bly4.getTag().copy();
        md6.putInt("generation", WrittenBookItem.getGeneration(bly4) + 1);
        bly6.setTag(md6);
        return bly6;
    }
    
    public NonNullList<ItemStack> getRemainingItems(final CraftingContainer bil) {
        final NonNullList<ItemStack> gj3 = NonNullList.<ItemStack>withSize(bil.getContainerSize(), ItemStack.EMPTY);
        for (int integer4 = 0; integer4 < gj3.size(); ++integer4) {
            final ItemStack bly5 = bil.getItem(integer4);
            if (bly5.getItem().hasCraftingRemainingItem()) {
                gj3.set(integer4, new ItemStack(bly5.getItem().getCraftingRemainingItem()));
            }
            else if (bly5.getItem() instanceof WrittenBookItem) {
                final ItemStack bly6 = bly5.copy();
                bly6.setCount(1);
                gj3.set(integer4, bly6);
                break;
            }
        }
        return gj3;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.BOOK_CLONING;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 >= 3 && integer2 >= 3;
    }
}
