package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class ShieldDecorationRecipe extends CustomRecipe {
    public ShieldDecorationRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        ItemStack bly4 = ItemStack.EMPTY;
        ItemStack bly5 = ItemStack.EMPTY;
        for (int integer6 = 0; integer6 < bil.getContainerSize(); ++integer6) {
            final ItemStack bly6 = bil.getItem(integer6);
            if (!bly6.isEmpty()) {
                if (bly6.getItem() instanceof BannerItem) {
                    if (!bly5.isEmpty()) {
                        return false;
                    }
                    bly5 = bly6;
                }
                else {
                    if (bly6.getItem() != Items.SHIELD) {
                        return false;
                    }
                    if (!bly4.isEmpty()) {
                        return false;
                    }
                    if (bly6.getTagElement("BlockEntityTag") != null) {
                        return false;
                    }
                    bly4 = bly6;
                }
            }
        }
        return !bly4.isEmpty() && !bly5.isEmpty();
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        ItemStack bly3 = ItemStack.EMPTY;
        ItemStack bly4 = ItemStack.EMPTY;
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly5 = bil.getItem(integer5);
            if (!bly5.isEmpty()) {
                if (bly5.getItem() instanceof BannerItem) {
                    bly3 = bly5;
                }
                else if (bly5.getItem() == Items.SHIELD) {
                    bly4 = bly5.copy();
                }
            }
        }
        if (bly4.isEmpty()) {
            return bly4;
        }
        final CompoundTag md5 = bly3.getTagElement("BlockEntityTag");
        final CompoundTag md6 = (md5 == null) ? new CompoundTag() : md5.copy();
        md6.putInt("Base", ((BannerItem)bly3.getItem()).getColor().getId());
        bly4.addTagElement("BlockEntityTag", (Tag)md6);
        return bly4;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHIELD_DECORATION;
    }
}
