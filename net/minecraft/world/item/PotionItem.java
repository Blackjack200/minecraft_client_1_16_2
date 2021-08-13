package net.minecraft.world.item;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.Registry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.ItemLike;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class PotionItem extends Item {
    public PotionItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
    }
    
    @Override
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        final Player bft5 = (aqj instanceof Player) ? ((Player)aqj) : null;
        if (bft5 instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)bft5, bly);
        }
        if (!bru.isClientSide) {
            final List<MobEffectInstance> list6 = PotionUtils.getMobEffects(bly);
            for (final MobEffectInstance apr8 : list6) {
                if (apr8.getEffect().isInstantenous()) {
                    apr8.getEffect().applyInstantenousEffect(bft5, bft5, aqj, apr8.getAmplifier(), 1.0);
                }
                else {
                    aqj.addEffect(new MobEffectInstance(apr8));
                }
            }
        }
        if (bft5 != null) {
            bft5.awardStat(Stats.ITEM_USED.get(this));
            if (!bft5.abilities.instabuild) {
                bly.shrink(1);
            }
        }
        if (bft5 == null || !bft5.abilities.instabuild) {
            if (bly.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (bft5 != null) {
                bft5.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
            }
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
    
    @Override
    public String getDescriptionId(final ItemStack bly) {
        return PotionUtils.getPotion(bly).getName(this.getDescriptionId() + ".effect.");
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        PotionUtils.addPotionTooltip(bly, list, 1.0f);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return super.isFoil(bly) || !PotionUtils.getMobEffects(bly).isEmpty();
    }
    
    @Override
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        if (this.allowdedIn(bkp)) {
            for (final Potion bnq5 : Registry.POTION) {
                if (bnq5 != Potions.EMPTY) {
                    gj.add(PotionUtils.setPotion(new ItemStack(this), bnq5));
                }
            }
        }
    }
}
