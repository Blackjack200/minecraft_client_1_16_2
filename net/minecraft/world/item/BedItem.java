package net.minecraft.world.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class BedItem extends BlockItem {
    public BedItem(final Block bul, final Properties a) {
        super(bul, a);
    }
    
    @Override
    protected boolean placeBlock(final BlockPlaceContext bnv, final BlockState cee) {
        return bnv.getLevel().setBlock(bnv.getClickedPos(), cee, 26);
    }
}
