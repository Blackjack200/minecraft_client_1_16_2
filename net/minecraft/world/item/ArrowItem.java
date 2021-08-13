package net.minecraft.world.item;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ArrowItem extends Item {
    public ArrowItem(final Properties a) {
        super(a);
    }
    
    public AbstractArrow createArrow(final Level bru, final ItemStack bly, final LivingEntity aqj) {
        final Arrow bfz5 = new Arrow(bru, aqj);
        bfz5.setEffectsFromItem(bly);
        return bfz5;
    }
}
