package net.minecraft.world.item;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.EntityType;

public class ItemFrameItem extends HangingEntityItem {
    public ItemFrameItem(final Properties a) {
        super(EntityType.ITEM_FRAME, a);
    }
    
    @Override
    protected boolean mayPlace(final Player bft, final Direction gc, final ItemStack bly, final BlockPos fx) {
        return !Level.isOutsideBuildHeight(fx) && bft.mayUseItemAt(fx, gc, bly);
    }
}
