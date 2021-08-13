package net.minecraft.world.level;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.Blocks;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class NoiseColumn implements BlockGetter {
    private final BlockState[] column;
    
    public NoiseColumn(final BlockState[] arr) {
        this.column = arr;
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        return null;
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        final int integer3 = fx.getY();
        if (integer3 < 0 || integer3 >= this.column.length) {
            return Blocks.AIR.defaultBlockState();
        }
        return this.column[integer3];
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        return this.getBlockState(fx).getFluidState();
    }
}
