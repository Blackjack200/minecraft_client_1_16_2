package net.minecraft.world.item;

import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import java.util.Iterator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.Registry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class TippedArrowItem extends ArrowItem {
    public TippedArrowItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
    }
    
    @Override
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        if (this.allowdedIn(bkp)) {
            for (final Potion bnq5 : Registry.POTION) {
                if (!bnq5.getEffects().isEmpty()) {
                    gj.add(PotionUtils.setPotion(new ItemStack(this), bnq5));
                }
            }
        }
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        PotionUtils.addPotionTooltip(bly, list, 0.125f);
    }
    
    @Override
    public String getDescriptionId(final ItemStack bly) {
        return PotionUtils.getPotion(bly).getName(this.getDescriptionId() + ".effect.");
    }
}
