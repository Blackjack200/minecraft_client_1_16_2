package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;

public class GiantTreeTaigaSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    public GiantTreeTaigaSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        if (double7 > 1.75) {
            SurfaceBuilder.DEFAULT.apply(random, cft, bss, integer4, integer5, integer6, double7, cee8, cee9, integer10, long11, SurfaceBuilder.CONFIG_COARSE_DIRT);
        }
        else if (double7 > -0.95) {
            SurfaceBuilder.DEFAULT.apply(random, cft, bss, integer4, integer5, integer6, double7, cee8, cee9, integer10, long11, SurfaceBuilder.CONFIG_PODZOL);
        }
        else {
            SurfaceBuilder.DEFAULT.apply(random, cft, bss, integer4, integer5, integer6, double7, cee8, cee9, integer10, long11, SurfaceBuilder.CONFIG_GRASS);
        }
    }
}
