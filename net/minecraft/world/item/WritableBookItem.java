package net.minecraft.world.item;

import net.minecraft.nbt.ListTag;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class WritableBookItem extends Item {
    public WritableBookItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (cee5.is(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(bru3, fx4, cee5, bnx.getItemInHand()) ? InteractionResult.sidedSuccess(bru3.isClientSide) : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        bft.openItemGui(bly5, aoq);
        bft.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
    
    public static boolean makeSureTagIsValid(@Nullable final CompoundTag md) {
        if (md == null) {
            return false;
        }
        if (!md.contains("pages", 9)) {
            return false;
        }
        final ListTag mj2 = md.getList("pages", 8);
        for (int integer3 = 0; integer3 < mj2.size(); ++integer3) {
            final String string4 = mj2.getString(integer3);
            if (string4.length() > 32767) {
                return false;
            }
        }
        return true;
    }
}
