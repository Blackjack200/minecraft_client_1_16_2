package net.minecraft.world.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ItemUtils {
    public static InteractionResultHolder<ItemStack> useDrink(final Level bru, final Player bft, final InteractionHand aoq) {
        bft.startUsingItem(aoq);
        return InteractionResultHolder.<ItemStack>consume(bft.getItemInHand(aoq));
    }
    
    public static ItemStack createFilledResult(final ItemStack bly1, final Player bft, final ItemStack bly3, final boolean boolean4) {
        final boolean boolean5 = bft.abilities.instabuild;
        if (boolean4 && boolean5) {
            if (!bft.inventory.contains(bly3)) {
                bft.inventory.add(bly3);
            }
            return bly1;
        }
        if (!boolean5) {
            bly1.shrink(1);
        }
        if (bly1.isEmpty()) {
            return bly3;
        }
        if (!bft.inventory.add(bly3)) {
            bft.drop(bly3, false);
        }
        return bly1;
    }
    
    public static ItemStack createFilledResult(final ItemStack bly1, final Player bft, final ItemStack bly3) {
        return createFilledResult(bly1, bft, bly3, true);
    }
}
