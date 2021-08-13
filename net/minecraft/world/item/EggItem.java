package net.minecraft.world.item;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EggItem extends Item {
    public EggItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5f, 0.4f / (EggItem.random.nextFloat() * 0.4f + 0.8f));
        if (!bru.isClientSide) {
            final ThrownEgg bgr6 = new ThrownEgg(bru, bft);
            bgr6.setItem(bly5);
            bgr6.shootFromRotation(bft, bft.xRot, bft.yRot, 0.0f, 1.5f, 1.0f);
            bru.addFreshEntity(bgr6);
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        if (!bft.abilities.instabuild) {
            bly5.shrink(1);
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
}
