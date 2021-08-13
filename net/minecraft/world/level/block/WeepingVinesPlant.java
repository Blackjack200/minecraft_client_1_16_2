package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeepingVinesPlant extends GrowingPlantBodyBlock {
    public static final VoxelShape SHAPE;
    
    public WeepingVinesPlant(final Properties c) {
        super(c, Direction.DOWN, WeepingVinesPlant.SHAPE, false);
    }
    
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)Blocks.WEEPING_VINES;
    }
    
    static {
        SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }
}
