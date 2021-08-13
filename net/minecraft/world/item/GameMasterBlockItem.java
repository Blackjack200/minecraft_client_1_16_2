package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class GameMasterBlockItem extends BlockItem {
    public GameMasterBlockItem(final Block bul, final Properties a) {
        super(bul, a);
    }
    
    @Nullable
    @Override
    protected BlockState getPlacementState(final BlockPlaceContext bnv) {
        final Player bft3 = bnv.getPlayer();
        return (bft3 == null || bft3.canUseGameMasterBlocks()) ? super.getPlacementState(bnv) : null;
    }
}
