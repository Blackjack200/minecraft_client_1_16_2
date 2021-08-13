package net.minecraft.world.level.levelgen.feature.configurations;

import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class BlockPileConfiguration implements FeatureConfiguration {
    public static final Codec<BlockPileConfiguration> CODEC;
    public final BlockStateProvider stateProvider;
    
    public BlockPileConfiguration(final BlockStateProvider cnq) {
        this.stateProvider = cnq;
    }
    
    static {
        CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, clo -> clo.stateProvider).codec();
    }
}
