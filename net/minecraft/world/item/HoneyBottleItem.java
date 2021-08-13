package net.minecraft.world.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HoneyBottleItem extends Item {
    public HoneyBottleItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        super.finishUsingItem(bly, bru, aqj);
        if (aqj instanceof ServerPlayer) {
            final ServerPlayer aah5 = (ServerPlayer)aqj;
            CriteriaTriggers.CONSUME_ITEM.trigger(aah5, bly);
            aah5.awardStat(Stats.ITEM_USED.get(this));
        }
        if (!bru.isClientSide) {
            aqj.removeEffect(MobEffects.POISON);
        }
        if (bly.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (aqj instanceof Player && !((Player)aqj).abilities.instabuild) {
            final ItemStack bly2 = new ItemStack(Items.GLASS_BOTTLE);
            final Player bft6 = (Player)aqj;
            if (!bft6.inventory.add(bly2)) {
                bft6.drop(bly2, false);
            }
        }
        return bly;
    }
    
    @Override
    public int getUseDuration(final ItemStack bly) {
        return 40;
    }
    
    @Override
    public UseAnim getUseAnimation(final ItemStack bly) {
        return UseAnim.DRINK;
    }
    
    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }
    
    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        return ItemUtils.useDrink(bru, bft, aoq);
    }
}
