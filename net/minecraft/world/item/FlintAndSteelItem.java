package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class FlintAndSteelItem extends Item {
    public FlintAndSteelItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Player bft3 = bnx.getPlayer();
        final Level bru4 = bnx.getLevel();
        final BlockPos fx5 = bnx.getClickedPos();
        final BlockState cee6 = bru4.getBlockState(fx5);
        if (CampfireBlock.canLight(cee6)) {
            bru4.playSound(bft3, fx5, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, FlintAndSteelItem.random.nextFloat() * 0.4f + 0.8f);
            bru4.setBlock(fx5, ((StateHolder<O, BlockState>)cee6).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.LIT, true), 11);
            if (bft3 != null) {
                bnx.getItemInHand().<Player>hurtAndBreak(1, bft3, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(bnx.getHand())));
            }
            return InteractionResult.sidedSuccess(bru4.isClientSide());
        }
        final BlockPos fx6 = fx5.relative(bnx.getClickedFace());
        if (BaseFireBlock.canBePlacedAt(bru4, fx6, bnx.getHorizontalDirection())) {
            bru4.playSound(bft3, fx6, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, FlintAndSteelItem.random.nextFloat() * 0.4f + 0.8f);
            final BlockState cee7 = BaseFireBlock.getState(bru4, fx6);
            bru4.setBlock(fx6, cee7, 11);
            final ItemStack bly9 = bnx.getItemInHand();
            if (bft3 instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)bft3, fx6, bly9);
                bly9.<Player>hurtAndBreak(1, bft3, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(bnx.getHand())));
            }
            return InteractionResult.sidedSuccess(bru4.isClientSide());
        }
        return InteractionResult.FAIL;
    }
}
