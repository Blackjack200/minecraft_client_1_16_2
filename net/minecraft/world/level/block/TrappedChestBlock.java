package net.minecraft.world.level.block;

import net.minecraft.util.Mth;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TrappedChestBlock extends ChestBlock {
    public TrappedChestBlock(final Properties c) {
        super(c, (Supplier<BlockEntityType<? extends ChestBlockEntity>>)(() -> BlockEntityType.TRAPPED_CHEST));
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new TrappedChestBlockEntity();
    }
    
    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }
    
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(bqz, fx), 0, 15);
    }
    
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (gc == Direction.UP) {
            return cee.getSignal(bqz, fx, gc);
        }
        return 0;
    }
}
