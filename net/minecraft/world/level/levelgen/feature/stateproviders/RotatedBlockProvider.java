package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.Codec;

public class RotatedBlockProvider extends BlockStateProvider {
    public static final Codec<RotatedBlockProvider> CODEC;
    private final Block block;
    
    public RotatedBlockProvider(final Block bul) {
        this.block = bul;
    }
    
    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.ROTATED_BLOCK_PROVIDER;
    }
    
    @Override
    public BlockState getState(final Random random, final BlockPos fx) {
        final Direction.Axis a4 = Direction.Axis.getRandom(random);
        return ((StateHolder<O, BlockState>)this.block.defaultBlockState()).<Direction.Axis, Direction.Axis>setValue(RotatedPillarBlock.AXIS, a4);
    }
    
    static {
        CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).xmap(RotatedBlockProvider::new, cnu -> cnu.block).codec();
    }
}
