package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;

public class DefaultSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    public DefaultSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        this.apply(random, cft, bss, integer4, integer5, integer6, double7, cee8, cee9, ctr.getTopMaterial(), ctr.getUnderMaterial(), ctr.getUnderwaterMaterial(), integer10);
    }
    
    protected void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final BlockState cee10, final BlockState cee11, final BlockState cee12, final int integer13) {
        BlockState cee13 = cee10;
        BlockState cee14 = cee11;
        final BlockPos.MutableBlockPos a18 = new BlockPos.MutableBlockPos();
        int integer14 = -1;
        final int integer15 = (int)(double7 / 3.0 + 3.0 + random.nextDouble() * 0.25);
        final int integer16 = integer4 & 0xF;
        final int integer17 = integer5 & 0xF;
        for (int integer18 = integer6; integer18 >= 0; --integer18) {
            a18.set(integer16, integer18, integer17);
            final BlockState cee15 = cft.getBlockState(a18);
            if (cee15.isAir()) {
                integer14 = -1;
            }
            else if (cee15.is(cee8.getBlock())) {
                if (integer14 == -1) {
                    if (integer15 <= 0) {
                        cee13 = Blocks.AIR.defaultBlockState();
                        cee14 = cee8;
                    }
                    else if (integer18 >= integer13 - 4 && integer18 <= integer13 + 1) {
                        cee13 = cee10;
                        cee14 = cee11;
                    }
                    if (integer18 < integer13 && (cee13 == null || cee13.isAir())) {
                        if (bss.getTemperature(a18.set(integer4, integer18, integer5)) < 0.15f) {
                            cee13 = Blocks.ICE.defaultBlockState();
                        }
                        else {
                            cee13 = cee9;
                        }
                        a18.set(integer16, integer18, integer17);
                    }
                    integer14 = integer15;
                    if (integer18 >= integer13 - 1) {
                        cft.setBlockState(a18, cee13, false);
                    }
                    else if (integer18 < integer13 - 7 - integer15) {
                        cee13 = Blocks.AIR.defaultBlockState();
                        cee14 = cee8;
                        cft.setBlockState(a18, cee12, false);
                    }
                    else {
                        cft.setBlockState(a18, cee14, false);
                    }
                }
                else if (integer14 > 0) {
                    --integer14;
                    cft.setBlockState(a18, cee14, false);
                    if (integer14 == 0 && cee14.is(Blocks.SAND) && integer15 > 1) {
                        integer14 = random.nextInt(4) + Math.max(0, integer18 - 63);
                        cee14 = (cee14.is(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState());
                    }
                }
            }
        }
    }
}
