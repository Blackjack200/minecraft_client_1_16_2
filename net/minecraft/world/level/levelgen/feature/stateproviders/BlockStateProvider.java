package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public abstract class BlockStateProvider {
    public static final Codec<BlockStateProvider> CODEC;
    
    protected abstract BlockStateProviderType<?> type();
    
    public abstract BlockState getState(final Random random, final BlockPos fx);
    
    static {
        CODEC = Registry.BLOCKSTATE_PROVIDER_TYPES.dispatch(BlockStateProvider::type, BlockStateProviderType::codec);
    }
}
