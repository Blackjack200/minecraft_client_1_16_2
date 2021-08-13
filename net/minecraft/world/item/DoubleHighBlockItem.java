package net.minecraft.world.item;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class DoubleHighBlockItem extends BlockItem {
    public DoubleHighBlockItem(final Block bul, final Properties a) {
        super(bul, a);
    }
    
    @Override
    protected boolean placeBlock(final BlockPlaceContext bnv, final BlockState cee) {
        bnv.getLevel().setBlock(bnv.getClickedPos().above(), Blocks.AIR.defaultBlockState(), 27);
        return super.placeBlock(bnv, cee);
    }
}
