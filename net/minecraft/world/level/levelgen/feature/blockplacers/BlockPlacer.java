package net.minecraft.world.level.levelgen.feature.blockplacers;

import net.minecraft.core.Registry;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.mojang.serialization.Codec;

public abstract class BlockPlacer {
    public static final Codec<BlockPlacer> CODEC;
    
    public abstract void place(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final Random random);
    
    protected abstract BlockPlacerType<?> type();
    
    static {
        CODEC = Registry.BLOCK_PLACER_TYPES.dispatch(BlockPlacer::type, BlockPlacerType::codec);
    }
}
