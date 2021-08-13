package net.minecraft.world.level.block;

import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RootsBlock extends BushBlock {
    protected static final VoxelShape SHAPE;
    
    protected RootsBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return RootsBlock.SHAPE;
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.is(BlockTags.NYLIUM) || cee.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(cee, bqz, fx);
    }
    
    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
    
    static {
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    }
}
