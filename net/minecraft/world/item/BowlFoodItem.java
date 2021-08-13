package net.minecraft.world.item;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BowlFoodItem extends Item {
    public BowlFoodItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        final ItemStack bly2 = super.finishUsingItem(bly, bru, aqj);
        if (aqj instanceof Player && ((Player)aqj).abilities.instabuild) {
            return bly2;
        }
        return new ItemStack(Items.BOWL);
    }
}
