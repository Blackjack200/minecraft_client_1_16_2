package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;

public class SwampSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    public SwampSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        final double double8 = Biome.BIOME_INFO_NOISE.getValue(integer4 * 0.25, integer5 * 0.25, false);
        if (double8 > 0.0) {
            final int integer11 = integer4 & 0xF;
            final int integer12 = integer5 & 0xF;
            final BlockPos.MutableBlockPos a20 = new BlockPos.MutableBlockPos();
            int integer13 = integer6;
            while (integer13 >= 0) {
                a20.set(integer11, integer13, integer12);
                if (!cft.getBlockState(a20).isAir()) {
                    if (integer13 == 62 && !cft.getBlockState(a20).is(cee9.getBlock())) {
                        cft.setBlockState(a20, cee9, false);
                        break;
                    }
                    break;
                }
                else {
                    --integer13;
                }
            }
        }
        SurfaceBuilder.DEFAULT.apply(random, cft, bss, integer4, integer5, integer6, double7, cee8, cee9, integer10, long11, ctr);
    }
}
