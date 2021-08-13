package net.minecraft.world.item;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ExperienceBottleItem extends Item {
    public ExperienceBottleItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (ExperienceBottleItem.random.nextFloat() * 0.4f + 0.8f));
        if (!bru.isClientSide) {
            final ThrownExperienceBottle bgt6 = new ThrownExperienceBottle(bru, bft);
            bgt6.setItem(bly5);
            bgt6.shootFromRotation(bft, bft.xRot, bft.yRot, -20.0f, 0.7f, 1.0f);
            bru.addFreshEntity(bgt6);
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        if (!bft.abilities.instabuild) {
            bly5.shrink(1);
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
}
