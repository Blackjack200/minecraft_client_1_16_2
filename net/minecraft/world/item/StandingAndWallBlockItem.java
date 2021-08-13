package net.minecraft.world.item;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class StandingAndWallBlockItem extends BlockItem {
    protected final Block wallBlock;
    
    public StandingAndWallBlockItem(final Block bul1, final Block bul2, final Properties a) {
        super(bul1, a);
        this.wallBlock = bul2;
    }
    
    @Nullable
    @Override
    protected BlockState getPlacementState(final BlockPlaceContext bnv) {
        final BlockState cee3 = this.wallBlock.getStateForPlacement(bnv);
        BlockState cee4 = null;
        final LevelReader brw5 = bnv.getLevel();
        final BlockPos fx6 = bnv.getClickedPos();
        for (final Direction gc10 : bnv.getNearestLookingDirections()) {
            if (gc10 != Direction.UP) {
                final BlockState cee5 = (gc10 == Direction.DOWN) ? this.getBlock().getStateForPlacement(bnv) : cee3;
                if (cee5 != null && cee5.canSurvive(brw5, fx6)) {
                    cee4 = cee5;
                    break;
                }
            }
        }
        return (cee4 != null && brw5.isUnobstructed(cee4, fx6, CollisionContext.empty())) ? cee4 : null;
    }
    
    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item blu) {
        super.registerBlocks(map, blu);
        map.put(this.wallBlock, blu);
    }
}
