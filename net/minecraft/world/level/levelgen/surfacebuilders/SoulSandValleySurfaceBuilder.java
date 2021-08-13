package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Blocks;
import com.mojang.serialization.Codec;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.BlockState;

public class SoulSandValleySurfaceBuilder extends NetherCappedSurfaceBuilder {
    private static final BlockState SOUL_SAND;
    private static final BlockState SOUL_SOIL;
    private static final BlockState GRAVEL;
    private static final ImmutableList<BlockState> BLOCK_STATES;
    
    public SoulSandValleySurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected ImmutableList<BlockState> getFloorBlockStates() {
        return SoulSandValleySurfaceBuilder.BLOCK_STATES;
    }
    
    @Override
    protected ImmutableList<BlockState> getCeilingBlockStates() {
        return SoulSandValleySurfaceBuilder.BLOCK_STATES;
    }
    
    @Override
    protected BlockState getPatchBlockState() {
        return SoulSandValleySurfaceBuilder.GRAVEL;
    }
    
    static {
        SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
        SOUL_SOIL = Blocks.SOUL_SOIL.defaultBlockState();
        GRAVEL = Blocks.GRAVEL.defaultBlockState();
        BLOCK_STATES = ImmutableList.of(SoulSandValleySurfaceBuilder.SOUL_SAND, SoulSandValleySurfaceBuilder.SOUL_SOIL);
    }
}
