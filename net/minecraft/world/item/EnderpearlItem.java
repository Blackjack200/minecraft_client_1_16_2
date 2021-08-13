package net.minecraft.world.item;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EnderpearlItem extends Item {
    public EnderpearlItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (EnderpearlItem.random.nextFloat() * 0.4f + 0.8f));
        bft.getCooldowns().addCooldown(this, 20);
        if (!bru.isClientSide) {
            final ThrownEnderpearl bgs6 = new ThrownEnderpearl(bru, bft);
            bgs6.setItem(bly5);
            bgs6.shootFromRotation(bft, bft.xRot, bft.yRot, 0.0f, 1.5f, 1.0f);
            bru.addFreshEntity(bgs6);
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        if (!bft.abilities.instabuild) {
            bly5.shrink(1);
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
}
