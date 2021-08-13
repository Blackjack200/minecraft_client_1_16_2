package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Blocks;
import com.mojang.serialization.Codec;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.BlockState;

public class BasaltDeltasSurfaceBuilder extends NetherCappedSurfaceBuilder {
    private static final BlockState BASALT;
    private static final BlockState BLACKSTONE;
    private static final BlockState GRAVEL;
    private static final ImmutableList<BlockState> FLOOR_BLOCK_STATES;
    private static final ImmutableList<BlockState> CEILING_BLOCK_STATES;
    
    public BasaltDeltasSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected ImmutableList<BlockState> getFloorBlockStates() {
        return BasaltDeltasSurfaceBuilder.FLOOR_BLOCK_STATES;
    }
    
    @Override
    protected ImmutableList<BlockState> getCeilingBlockStates() {
        return BasaltDeltasSurfaceBuilder.CEILING_BLOCK_STATES;
    }
    
    @Override
    protected BlockState getPatchBlockState() {
        return BasaltDeltasSurfaceBuilder.GRAVEL;
    }
    
    static {
        BASALT = Blocks.BASALT.defaultBlockState();
        BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
        GRAVEL = Blocks.GRAVEL.defaultBlockState();
        FLOOR_BLOCK_STATES = ImmutableList.of(BasaltDeltasSurfaceBuilder.BASALT, BasaltDeltasSurfaceBuilder.BLACKSTONE);
        CEILING_BLOCK_STATES = ImmutableList.of(BasaltDeltasSurfaceBuilder.BASALT);
    }
}
