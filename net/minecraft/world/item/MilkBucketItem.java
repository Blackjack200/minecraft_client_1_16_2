package net.minecraft.world.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.player.Player;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MilkBucketItem extends Item {
    public MilkBucketItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        if (aqj instanceof ServerPlayer) {
            final ServerPlayer aah5 = (ServerPlayer)aqj;
            CriteriaTriggers.CONSUME_ITEM.trigger(aah5, bly);
            aah5.awardStat(Stats.ITEM_USED.get(this));
        }
        if (aqj instanceof Player && !((Player)aqj).abilities.instabuild) {
            bly.shrink(1);
        }
        if (!bru.isClientSide) {
            aqj.removeAllEffects();
        }
        if (bly.isEmpty()) {
            return new ItemStack(Items.BUCKET);
        }
        return bly;
    }
    
    @Override
    public int getUseDuration(final ItemStack bly) {
        return 32;
    }
    
    @Override
    public UseAnim getUseAnimation(final ItemStack bly) {
        return UseAnim.DRINK;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        return ItemUtils.useDrink(bru, bft, aoq);
    }
}
