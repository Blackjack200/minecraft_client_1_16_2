package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ChorusFruitItem extends Item {
    public ChorusFruitItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        final ItemStack bly2 = super.finishUsingItem(bly, bru, aqj);
        if (!bru.isClientSide) {
            final double double6 = aqj.getX();
            final double double7 = aqj.getY();
            final double double8 = aqj.getZ();
            for (int integer12 = 0; integer12 < 16; ++integer12) {
                final double double9 = aqj.getX() + (aqj.getRandom().nextDouble() - 0.5) * 16.0;
                final double double10 = Mth.clamp(aqj.getY() + (aqj.getRandom().nextInt(16) - 8), 0.0, bru.getHeight() - 1);
                final double double11 = aqj.getZ() + (aqj.getRandom().nextDouble() - 0.5) * 16.0;
                if (aqj.isPassenger()) {
                    aqj.stopRiding();
                }
                if (aqj.randomTeleport(double9, double10, double11, true)) {
                    final SoundEvent adn19 = (aqj instanceof Fox) ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    bru.playSound(null, double6, double7, double8, adn19, SoundSource.PLAYERS, 1.0f, 1.0f);
                    aqj.playSound(adn19, 1.0f, 1.0f);
                    break;
                }
            }
            if (aqj instanceof Player) {
                ((Player)aqj).getCooldowns().addCooldown(this, 20);
            }
        }
        return bly2;
    }
}
