package net.minecraft.world.level.block;

import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SoulFireBlock extends BaseFireBlock {
    public SoulFireBlock(final Properties c) {
        super(c, 2.0f);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (this.canSurvive(cee1, brv, fx5)) {
            return this.defaultBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return canSurviveOnBlock(brw.getBlockState(fx.below()).getBlock());
    }
    
    public static boolean canSurviveOnBlock(final Block bul) {
        return bul.is(BlockTags.SOUL_FIRE_BASE_BLOCKS);
    }
    
    @Override
    protected boolean canBurn(final BlockState cee) {
        return true;
    }
}
