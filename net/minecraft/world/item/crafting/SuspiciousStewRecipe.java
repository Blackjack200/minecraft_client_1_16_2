package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class SuspiciousStewRecipe extends CustomRecipe {
    public SuspiciousStewRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        boolean boolean4 = false;
        boolean boolean5 = false;
        boolean boolean6 = false;
        boolean boolean7 = false;
        for (int integer8 = 0; integer8 < bil.getContainerSize(); ++integer8) {
            final ItemStack bly9 = bil.getItem(integer8);
            if (!bly9.isEmpty()) {
                if (bly9.getItem() == Blocks.BROWN_MUSHROOM.asItem() && !boolean6) {
                    boolean6 = true;
                }
                else if (bly9.getItem() == Blocks.RED_MUSHROOM.asItem() && !boolean5) {
                    boolean5 = true;
                }
                else if (bly9.getItem().is(ItemTags.SMALL_FLOWERS) && !boolean4) {
                    boolean4 = true;
                }
                else {
                    if (bly9.getItem() != Items.BOWL || boolean7) {
                        return false;
                    }
                    boolean7 = true;
                }
            }
        }
        return boolean4 && boolean6 && boolean5 && boolean7;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        ItemStack bly3 = ItemStack.EMPTY;
        for (int integer4 = 0; integer4 < bil.getContainerSize(); ++integer4) {
            final ItemStack bly4 = bil.getItem(integer4);
            if (!bly4.isEmpty()) {
                if (bly4.getItem().is(ItemTags.SMALL_FLOWERS)) {
                    bly3 = bly4;
                    break;
                }
            }
        }
        final ItemStack bly5 = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        if (bly3.getItem() instanceof BlockItem && ((BlockItem)bly3.getItem()).getBlock() instanceof FlowerBlock) {
            final FlowerBlock bwr5 = (FlowerBlock)((BlockItem)bly3.getItem()).getBlock();
            final MobEffect app6 = bwr5.getSuspiciousStewEffect();
            SuspiciousStewItem.saveMobEffect(bly5, app6, bwr5.getEffectDuration());
        }
        return bly5;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 >= 2 && integer2 >= 2;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SUSPICIOUS_STEW;
    }
}
