package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class FireChargeItem extends Item {
    public FireChargeItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        boolean boolean6 = false;
        if (CampfireBlock.canLight(cee5)) {
            this.playSound(bru3, fx4);
            bru3.setBlockAndUpdate(fx4, ((StateHolder<O, BlockState>)cee5).<Comparable, Boolean>setValue((Property<Comparable>)CampfireBlock.LIT, true));
            boolean6 = true;
        }
        else {
            fx4 = fx4.relative(bnx.getClickedFace());
            if (BaseFireBlock.canBePlacedAt(bru3, fx4, bnx.getHorizontalDirection())) {
                this.playSound(bru3, fx4);
                bru3.setBlockAndUpdate(fx4, BaseFireBlock.getState(bru3, fx4));
                boolean6 = true;
            }
        }
        if (boolean6) {
            bnx.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(bru3.isClientSide);
        }
        return InteractionResult.FAIL;
    }
    
    private void playSound(final Level bru, final BlockPos fx) {
        bru.playSound(null, fx, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0f, (FireChargeItem.random.nextFloat() - FireChargeItem.random.nextFloat()) * 0.2f + 1.0f);
    }
}
