package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class SimpleStateProvider extends BlockStateProvider {
    public static final Codec<SimpleStateProvider> CODEC;
    private final BlockState state;
    
    public SimpleStateProvider(final BlockState cee) {
        this.state = cee;
    }
    
    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
    }
    
    @Override
    public BlockState getState(final Random random, final BlockPos fx) {
        return this.state;
    }
    
    static {
        CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, cnv -> cnv.state).codec();
    }
}
