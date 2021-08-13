package net.minecraft.world.level.block;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractChestBlock<E extends BlockEntity> extends BaseEntityBlock {
    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
    
    protected AbstractChestBlock(final Properties c, final Supplier<BlockEntityType<? extends E>> supplier) {
        super(c);
        this.blockEntityType = supplier;
    }
    
    public abstract DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(final BlockState cee, final Level bru, final BlockPos fx, final boolean boolean4);
}
