package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TwistingVinesPlant extends GrowingPlantBodyBlock {
    public static final VoxelShape SHAPE;
    
    public TwistingVinesPlant(final Properties c) {
        super(c, Direction.UP, TwistingVinesPlant.SHAPE, false);
    }
    
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)Blocks.TWISTING_VINES;
    }
    
    static {
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    }
}
