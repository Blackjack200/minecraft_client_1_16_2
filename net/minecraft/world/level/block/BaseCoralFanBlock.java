package net.minecraft.world.level.block;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseCoralFanBlock extends BaseCoralPlantTypeBlock {
    private static final VoxelShape AABB;
    
    protected BaseCoralFanBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return BaseCoralFanBlock.AABB;
    }
    
    static {
        AABB = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    }
}
