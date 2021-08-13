package net.minecraft.world.item;

import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SpectralArrowItem extends ArrowItem {
    public SpectralArrowItem(final Properties a) {
        super(a);
    }
    
    @Override
    public AbstractArrow createArrow(final Level bru, final ItemStack bly, final LivingEntity aqj) {
        return new SpectralArrow(bru, aqj);
    }
}
