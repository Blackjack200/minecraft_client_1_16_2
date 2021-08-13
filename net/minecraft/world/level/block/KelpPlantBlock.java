package net.minecraft.world.level.block;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class KelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
    protected KelpPlantBlock(final Properties c) {
        super(c, Direction.UP, Shapes.block(), true);
    }
    
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)Blocks.KELP;
    }
    
    public FluidState getFluidState(final BlockState cee) {
        return Fluids.WATER.getSource(false);
    }
    
    @Override
    public boolean canPlaceLiquid(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Fluid cut) {
        return false;
    }
    
    @Override
    public boolean placeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        return false;
    }
}
