package net.minecraft.world.item;

import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public class SignItem extends StandingAndWallBlockItem {
    public SignItem(final Properties a, final Block bul2, final Block bul3) {
        super(bul2, bul3, a);
    }
    
    @Override
    protected boolean updateCustomBlockEntityTag(final BlockPos fx, final Level bru, @Nullable final Player bft, final ItemStack bly, final BlockState cee) {
        final boolean boolean7 = super.updateCustomBlockEntityTag(fx, bru, bft, bly, cee);
        if (!bru.isClientSide && !boolean7 && bft != null) {
            bft.openTextEdit((SignBlockEntity)bru.getBlockEntity(fx));
        }
        return boolean7;
    }
}
