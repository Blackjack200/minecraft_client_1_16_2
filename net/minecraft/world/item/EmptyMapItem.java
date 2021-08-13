package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EmptyMapItem extends ComplexItem {
    public EmptyMapItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = MapItem.create(bru, Mth.floor(bft.getX()), Mth.floor(bft.getZ()), (byte)0, true, false);
        final ItemStack bly6 = bft.getItemInHand(aoq);
        if (!bft.abilities.instabuild) {
            bly6.shrink(1);
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        bft.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0f, 1.0f);
        if (bly6.isEmpty()) {
            return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
        }
        if (!bft.inventory.add(bly5.copy())) {
            bft.drop(bly5, false);
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly6, bru.isClientSide());
    }
}
