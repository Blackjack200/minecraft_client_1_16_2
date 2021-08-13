package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeepingVines extends GrowingPlantHeadBlock {
    protected static final VoxelShape SHAPE;
    
    public WeepingVines(final Properties c) {
        super(c, Direction.DOWN, WeepingVines.SHAPE, false, 0.1);
    }
    
    @Override
    protected int getBlocksToGrowWhenBonemealed(final Random random) {
        return NetherVines.getBlocksToGrowWhenBonemealed(random);
    }
    
    @Override
    protected Block getBodyBlock() {
        return Blocks.WEEPING_VINES_PLANT;
    }
    
    @Override
    protected boolean canGrowInto(final BlockState cee) {
        return NetherVines.isValidGrowthState(cee);
    }
    
    static {
        SHAPE = Block.box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);
    }
}
