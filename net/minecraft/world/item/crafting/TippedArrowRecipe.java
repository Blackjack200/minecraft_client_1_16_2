package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.Collection;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class TippedArrowRecipe extends CustomRecipe {
    public TippedArrowRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        if (bil.getWidth() != 3 || bil.getHeight() != 3) {
            return false;
        }
        for (int integer4 = 0; integer4 < bil.getWidth(); ++integer4) {
            for (int integer5 = 0; integer5 < bil.getHeight(); ++integer5) {
                final ItemStack bly6 = bil.getItem(integer4 + integer5 * bil.getWidth());
                if (bly6.isEmpty()) {
                    return false;
                }
                final Item blu7 = bly6.getItem();
                if (integer4 == 1 && integer5 == 1) {
                    if (blu7 != Items.LINGERING_POTION) {
                        return false;
                    }
                }
                else if (blu7 != Items.ARROW) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        final ItemStack bly3 = bil.getItem(1 + bil.getWidth());
        if (bly3.getItem() != Items.LINGERING_POTION) {
            return ItemStack.EMPTY;
        }
        final ItemStack bly4 = new ItemStack(Items.TIPPED_ARROW, 8);
        PotionUtils.setPotion(bly4, PotionUtils.getPotion(bly3));
        PotionUtils.setCustomEffects(bly4, (Collection<MobEffectInstance>)PotionUtils.getCustomEffects(bly3));
        return bly4;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 >= 2 && integer2 >= 2;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.TIPPED_ARROW;
    }
}
