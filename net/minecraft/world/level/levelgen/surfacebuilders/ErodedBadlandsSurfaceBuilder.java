package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;

public class ErodedBadlandsSurfaceBuilder extends BadlandsSurfaceBuilder {
    private static final BlockState WHITE_TERRACOTTA;
    private static final BlockState ORANGE_TERRACOTTA;
    private static final BlockState TERRACOTTA;
    
    public ErodedBadlandsSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        double double8 = 0.0;
        final double double9 = Math.min(Math.abs(double7), this.pillarNoise.getValue(integer4 * 0.25, integer5 * 0.25, false) * 15.0);
        if (double9 > 0.0) {
            final double double10 = 0.001953125;
            final double double11 = Math.abs(this.pillarRoofNoise.getValue(integer4 * 0.001953125, integer5 * 0.001953125, false));
            double8 = double9 * double9 * 2.5;
            final double double12 = Math.ceil(double11 * 50.0) + 14.0;
            if (double8 > double12) {
                double8 = double12;
            }
            double8 += 64.0;
        }
        final int integer11 = integer4 & 0xF;
        final int integer12 = integer5 & 0xF;
        BlockState cee10 = ErodedBadlandsSurfaceBuilder.WHITE_TERRACOTTA;
        final SurfaceBuilderConfiguration cts23 = bss.getGenerationSettings().getSurfaceBuilderConfig();
        final BlockState cee11 = cts23.getUnderMaterial();
        final BlockState cee12 = cts23.getTopMaterial();
        BlockState cee13 = cee11;
        final int integer13 = (int)(double7 / 3.0 + 3.0 + random.nextDouble() * 0.25);
        final boolean boolean28 = Math.cos(double7 / 3.0 * 3.141592653589793) > 0.0;
        int integer14 = -1;
        boolean boolean29 = false;
        final BlockPos.MutableBlockPos a31 = new BlockPos.MutableBlockPos();
        for (int integer15 = Math.max(integer6, (int)double8 + 1); integer15 >= 0; --integer15) {
            a31.set(integer11, integer15, integer12);
            if (cft.getBlockState(a31).isAir() && integer15 < (int)double8) {
                cft.setBlockState(a31, cee8, false);
            }
            final BlockState cee14 = cft.getBlockState(a31);
            if (cee14.isAir()) {
                integer14 = -1;
            }
            else if (cee14.is(cee8.getBlock())) {
                if (integer14 == -1) {
                    boolean29 = false;
                    if (integer13 <= 0) {
                        cee10 = Blocks.AIR.defaultBlockState();
                        cee13 = cee8;
                    }
                    else if (integer15 >= integer10 - 4 && integer15 <= integer10 + 1) {
                        cee10 = ErodedBadlandsSurfaceBuilder.WHITE_TERRACOTTA;
                        cee13 = cee11;
                    }
                    if (integer15 < integer10 && (cee10 == null || cee10.isAir())) {
                        cee10 = cee9;
                    }
                    integer14 = integer13 + Math.max(0, integer15 - integer10);
                    if (integer15 >= integer10 - 1) {
                        if (integer15 > integer10 + 3 + integer13) {
                            BlockState cee15;
                            if (integer15 < 64 || integer15 > 127) {
                                cee15 = ErodedBadlandsSurfaceBuilder.ORANGE_TERRACOTTA;
                            }
                            else if (boolean28) {
                                cee15 = ErodedBadlandsSurfaceBuilder.TERRACOTTA;
                            }
                            else {
                                cee15 = this.getBand(integer4, integer15, integer5);
                            }
                            cft.setBlockState(a31, cee15, false);
                        }
                        else {
                            cft.setBlockState(a31, cee12, false);
                            boolean29 = true;
                        }
                    }
                    else {
                        cft.setBlockState(a31, cee13, false);
                        final Block bul34 = cee13.getBlock();
                        if (bul34 == Blocks.WHITE_TERRACOTTA || bul34 == Blocks.ORANGE_TERRACOTTA || bul34 == Blocks.MAGENTA_TERRACOTTA || bul34 == Blocks.LIGHT_BLUE_TERRACOTTA || bul34 == Blocks.YELLOW_TERRACOTTA || bul34 == Blocks.LIME_TERRACOTTA || bul34 == Blocks.PINK_TERRACOTTA || bul34 == Blocks.GRAY_TERRACOTTA || bul34 == Blocks.LIGHT_GRAY_TERRACOTTA || bul34 == Blocks.CYAN_TERRACOTTA || bul34 == Blocks.PURPLE_TERRACOTTA || bul34 == Blocks.BLUE_TERRACOTTA || bul34 == Blocks.BROWN_TERRACOTTA || bul34 == Blocks.GREEN_TERRACOTTA || bul34 == Blocks.RED_TERRACOTTA || bul34 == Blocks.BLACK_TERRACOTTA) {
                            cft.setBlockState(a31, ErodedBadlandsSurfaceBuilder.ORANGE_TERRACOTTA, false);
                        }
                    }
                }
                else if (integer14 > 0) {
                    --integer14;
                    if (boolean29) {
                        cft.setBlockState(a31, ErodedBadlandsSurfaceBuilder.ORANGE_TERRACOTTA, false);
                    }
                    else {
                        cft.setBlockState(a31, this.getBand(integer4, integer15, integer5), false);
                    }
                }
            }
        }
    }
    
    static {
        WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
        ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
    }
}
