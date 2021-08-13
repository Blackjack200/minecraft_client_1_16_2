package net.minecraft.world.level.block.state;

import java.util.function.Function;
import net.minecraft.core.Registry;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.Codec;

public class BlockState extends BlockBehaviour.BlockStateBase {
    public static final Codec<BlockState> CODEC;
    
    public BlockState(final Block bul, final ImmutableMap<Property<?>, Comparable<?>> immutableMap, final MapCodec<BlockState> mapCodec) {
        super(bul, immutableMap, mapCodec);
    }
    
    @Override
    protected BlockState asState() {
        return this;
    }
    
    static {
        CODEC = StateHolder.<Object, StateHolder>codec((com.mojang.serialization.Codec<Object>)Registry.BLOCK, (java.util.function.Function<Object, StateHolder>)Block::defaultBlockState).stable();
    }
}
