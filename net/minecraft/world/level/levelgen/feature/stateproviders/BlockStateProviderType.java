package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public class BlockStateProviderType<P extends BlockStateProvider> {
    public static final BlockStateProviderType<SimpleStateProvider> SIMPLE_STATE_PROVIDER;
    public static final BlockStateProviderType<WeightedStateProvider> WEIGHTED_STATE_PROVIDER;
    public static final BlockStateProviderType<PlainFlowerProvider> PLAIN_FLOWER_PROVIDER;
    public static final BlockStateProviderType<ForestFlowerProvider> FOREST_FLOWER_PROVIDER;
    public static final BlockStateProviderType<RotatedBlockProvider> ROTATED_BLOCK_PROVIDER;
    private final Codec<P> codec;
    
    private static <P extends BlockStateProvider> BlockStateProviderType<P> register(final String string, final Codec<P> codec) {
        return Registry.<BlockStateProviderType<P>>register(Registry.BLOCKSTATE_PROVIDER_TYPES, string, new BlockStateProviderType<P>(codec));
    }
    
    private BlockStateProviderType(final Codec<P> codec) {
        this.codec = codec;
    }
    
    public Codec<P> codec() {
        return this.codec;
    }
    
    static {
        SIMPLE_STATE_PROVIDER = BlockStateProviderType.<SimpleStateProvider>register("simple_state_provider", SimpleStateProvider.CODEC);
        WEIGHTED_STATE_PROVIDER = BlockStateProviderType.<WeightedStateProvider>register("weighted_state_provider", WeightedStateProvider.CODEC);
        PLAIN_FLOWER_PROVIDER = BlockStateProviderType.<PlainFlowerProvider>register("plain_flower_provider", PlainFlowerProvider.CODEC);
        FOREST_FLOWER_PROVIDER = BlockStateProviderType.<ForestFlowerProvider>register("forest_flower_provider", ForestFlowerProvider.CODEC);
        ROTATED_BLOCK_PROVIDER = BlockStateProviderType.<RotatedBlockProvider>register("rotated_block_provider", RotatedBlockProvider.CODEC);
    }
}
