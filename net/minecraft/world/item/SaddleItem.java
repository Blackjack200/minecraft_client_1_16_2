package net.minecraft.world.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SaddleItem extends Item {
    public SaddleItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult interactLivingEntity(final ItemStack bly, final Player bft, final LivingEntity aqj, final InteractionHand aoq) {
        if (aqj instanceof Saddleable && aqj.isAlive()) {
            final Saddleable aqx6 = (Saddleable)aqj;
            if (!aqx6.isSaddled() && aqx6.isSaddleable()) {
                if (!bft.level.isClientSide) {
                    aqx6.equipSaddle(SoundSource.NEUTRAL);
                    bly.shrink(1);
                }
                return InteractionResult.sidedSuccess(bft.level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
}
