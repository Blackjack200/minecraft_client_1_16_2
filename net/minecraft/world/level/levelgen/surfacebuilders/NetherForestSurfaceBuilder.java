package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Blocks;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.block.state.BlockState;

public class NetherForestSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    private static final BlockState AIR;
    protected long seed;
    private PerlinNoise decorationNoise;
    
    public NetherForestSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        final int integer11 = integer10;
        final int integer12 = integer4 & 0xF;
        final int integer13 = integer5 & 0xF;
        final double double8 = this.decorationNoise.getValue(integer4 * 0.1, integer10, integer5 * 0.1);
        final boolean boolean21 = double8 > 0.15 + random.nextDouble() * 0.35;
        final double double9 = this.decorationNoise.getValue(integer4 * 0.1, 109.0, integer5 * 0.1);
        final boolean boolean22 = double9 > 0.25 + random.nextDouble() * 0.9;
        final int integer14 = (int)(double7 / 3.0 + 3.0 + random.nextDouble() * 0.25);
        final BlockPos.MutableBlockPos a26 = new BlockPos.MutableBlockPos();
        int integer15 = -1;
        BlockState cee10 = ctr.getUnderMaterial();
        for (int integer16 = 127; integer16 >= 0; --integer16) {
            a26.set(integer12, integer16, integer13);
            BlockState cee11 = ctr.getTopMaterial();
            final BlockState cee12 = cft.getBlockState(a26);
            if (cee12.isAir()) {
                integer15 = -1;
            }
            else if (cee12.is(cee8.getBlock())) {
                if (integer15 == -1) {
                    boolean boolean23 = false;
                    if (integer14 <= 0) {
                        boolean23 = true;
                        cee10 = ctr.getUnderMaterial();
                    }
                    if (boolean21) {
                        cee11 = ctr.getUnderMaterial();
                    }
                    else if (boolean22) {
                        cee11 = ctr.getUnderwaterMaterial();
                    }
                    if (integer16 < integer11 && boolean23) {
                        cee11 = cee9;
                    }
                    integer15 = integer14;
                    if (integer16 >= integer11 - 1) {
                        cft.setBlockState(a26, cee11, false);
                    }
                    else {
                        cft.setBlockState(a26, cee10, false);
                    }
                }
                else if (integer15 > 0) {
                    --integer15;
                    cft.setBlockState(a26, cee10, false);
                }
            }
        }
    }
    
    @Override
    public void initNoise(final long long1) {
        if (this.seed != long1 || this.decorationNoise == null) {
            this.decorationNoise = new PerlinNoise(new WorldgenRandom(long1), (List<Integer>)ImmutableList.of(0));
        }
        this.seed = long1;
    }
    
    static {
        AIR = Blocks.CAVE_AIR.defaultBlockState();
    }
}
