package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import com.mojang.serialization.Codec;

public class WeightedStateProvider extends BlockStateProvider {
    public static final Codec<WeightedStateProvider> CODEC;
    private final WeightedList<BlockState> weightedList;
    
    private static DataResult<WeightedStateProvider> create(final WeightedList<BlockState> aum) {
        if (aum.isEmpty()) {
            return (DataResult<WeightedStateProvider>)DataResult.error("WeightedStateProvider with no states");
        }
        return (DataResult<WeightedStateProvider>)DataResult.success(new WeightedStateProvider(aum));
    }
    
    private WeightedStateProvider(final WeightedList<BlockState> aum) {
        this.weightedList = aum;
    }
    
    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
    }
    
    public WeightedStateProvider() {
        this(new WeightedList<BlockState>());
    }
    
    public WeightedStateProvider add(final BlockState cee, final int integer) {
        this.weightedList.add(cee, integer);
        return this;
    }
    
    @Override
    public BlockState getState(final Random random, final BlockPos fx) {
        return this.weightedList.getOne(random);
    }
    
    static {
        CODEC = WeightedList.<BlockState>codec(BlockState.CODEC).comapFlatMap(WeightedStateProvider::create, cnw -> cnw.weightedList).fieldOf("entries").codec();
    }
}
