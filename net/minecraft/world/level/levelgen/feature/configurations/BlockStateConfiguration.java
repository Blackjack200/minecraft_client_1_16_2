package net.minecraft.world.level.levelgen.feature.configurations;

import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class BlockStateConfiguration implements FeatureConfiguration {
    public static final Codec<BlockStateConfiguration> CODEC;
    public final BlockState state;
    
    public BlockStateConfiguration(final BlockState cee) {
        this.state = cee;
    }
    
    static {
        CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, clp -> clp.state).codec();
    }
}
