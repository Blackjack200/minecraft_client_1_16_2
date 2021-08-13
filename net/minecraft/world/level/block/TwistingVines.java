package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TwistingVines extends GrowingPlantHeadBlock {
    public static final VoxelShape SHAPE;
    
    public TwistingVines(final Properties c) {
        super(c, Direction.UP, TwistingVines.SHAPE, false, 0.1);
    }
    
    @Override
    protected int getBlocksToGrowWhenBonemealed(final Random random) {
        return NetherVines.getBlocksToGrowWhenBonemealed(random);
    }
    
    @Override
    protected Block getBodyBlock() {
        return Blocks.TWISTING_VINES_PLANT;
    }
    
    @Override
    protected boolean canGrowInto(final BlockState cee) {
        return NetherVines.isValidGrowthState(cee);
    }
    
    static {
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 15.0, 12.0);
    }
}
