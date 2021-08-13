package net.minecraft.world.item;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.function.Consumer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FishingRodItem extends Item implements Vanishable {
    public FishingRodItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (bft.fishing != null) {
            if (!bru.isClientSide) {
                final int integer6 = bft.fishing.retrieve(bly5);
                bly5.<Player>hurtAndBreak(integer6, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
            }
            bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0f, 0.4f / (FishingRodItem.random.nextFloat() * 0.4f + 0.8f));
        }
        else {
            bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (FishingRodItem.random.nextFloat() * 0.4f + 0.8f));
            if (!bru.isClientSide) {
                final int integer6 = EnchantmentHelper.getFishingSpeedBonus(bly5);
                final int integer7 = EnchantmentHelper.getFishingLuckBonus(bly5);
                bru.addFreshEntity(new FishingHook(bft, bru, integer7, integer6));
            }
            bft.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
    
    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
