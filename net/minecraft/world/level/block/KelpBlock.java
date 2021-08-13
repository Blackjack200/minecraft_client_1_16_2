package net.minecraft.world.level.block;

import net.minecraft.world.level.material.Fluids;
import javax.annotation.Nullable;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import java.util.Random;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KelpBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
    protected static final VoxelShape SHAPE;
    
    protected KelpBlock(final Properties c) {
        super(c, Direction.UP, KelpBlock.SHAPE, true, 0.14);
    }
    
    @Override
    protected boolean canGrowInto(final BlockState cee) {
        return cee.is(Blocks.WATER);
    }
    
    @Override
    protected Block getBodyBlock() {
        return Blocks.KELP_PLANT;
    }
    
    @Override
    protected boolean canAttachToBlock(final Block bul) {
        return bul != Blocks.MAGMA_BLOCK;
    }
    
    @Override
    public boolean canPlaceLiquid(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Fluid cut) {
        return false;
    }
    
    @Override
    public boolean placeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        return false;
    }
    
    @Override
    protected int getBlocksToGrowWhenBonemealed(final Random random) {
        return 1;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        if (cuu3.is(FluidTags.WATER) && cuu3.getAmount() == 8) {
            return super.getStateForPlacement(bnv);
        }
        return null;
    }
    
    public FluidState getFluidState(final BlockState cee) {
        return Fluids.WATER.getSource(false);
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
    }
}
