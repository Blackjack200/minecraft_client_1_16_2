package net.minecraft.world.item;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ThrowablePotionItem extends PotionItem {
    public ThrowablePotionItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (!bru.isClientSide) {
            final ThrownPotion bgu6 = new ThrownPotion(bru, bft);
            bgu6.setItem(bly5);
            bgu6.shootFromRotation(bft, bft.xRot, bft.yRot, -20.0f, 0.5f, 1.0f);
            bru.addFreshEntity(bgu6);
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        if (!bft.abilities.instabuild) {
            bly5.shrink(1);
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
}
