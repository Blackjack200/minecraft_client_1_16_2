package net.minecraft.world.item;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class NameTagItem extends Item {
    public NameTagItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult interactLivingEntity(final ItemStack bly, final Player bft, final LivingEntity aqj, final InteractionHand aoq) {
        if (bly.hasCustomHoverName() && !(aqj instanceof Player)) {
            if (!bft.level.isClientSide && aqj.isAlive()) {
                aqj.setCustomName(bly.getHoverName());
                if (aqj instanceof Mob) {
                    ((Mob)aqj).setPersistenceRequired();
                }
                bly.shrink(1);
            }
            return InteractionResult.sidedSuccess(bft.level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
