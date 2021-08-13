package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;

public class WoodedBadlandsSurfaceBuilder extends BadlandsSurfaceBuilder {
    private static final BlockState WHITE_TERRACOTTA;
    private static final BlockState ORANGE_TERRACOTTA;
    private static final BlockState TERRACOTTA;
    
    public WoodedBadlandsSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        final int integer11 = integer4 & 0xF;
        final int integer12 = integer5 & 0xF;
        BlockState cee10 = WoodedBadlandsSurfaceBuilder.WHITE_TERRACOTTA;
        final SurfaceBuilderConfiguration cts19 = bss.getGenerationSettings().getSurfaceBuilderConfig();
        final BlockState cee11 = cts19.getUnderMaterial();
        final BlockState cee12 = cts19.getTopMaterial();
        BlockState cee13 = cee11;
        final int integer13 = (int)(double7 / 3.0 + 3.0 + random.nextDouble() * 0.25);
        final boolean boolean24 = Math.cos(double7 / 3.0 * 3.141592653589793) > 0.0;
        int integer14 = -1;
        boolean boolean25 = false;
        int integer15 = 0;
        final BlockPos.MutableBlockPos a28 = new BlockPos.MutableBlockPos();
        for (int integer16 = integer6; integer16 >= 0; --integer16) {
            if (integer15 < 15) {
                a28.set(integer11, integer16, integer12);
                final BlockState cee14 = cft.getBlockState(a28);
                if (cee14.isAir()) {
                    integer14 = -1;
                }
                else if (cee14.is(cee8.getBlock())) {
                    if (integer14 == -1) {
                        boolean25 = false;
                        if (integer13 <= 0) {
                            cee10 = Blocks.AIR.defaultBlockState();
                            cee13 = cee8;
                        }
                        else if (integer16 >= integer10 - 4 && integer16 <= integer10 + 1) {
                            cee10 = WoodedBadlandsSurfaceBuilder.WHITE_TERRACOTTA;
                            cee13 = cee11;
                        }
                        if (integer16 < integer10 && (cee10 == null || cee10.isAir())) {
                            cee10 = cee9;
                        }
                        integer14 = integer13 + Math.max(0, integer16 - integer10);
                        if (integer16 >= integer10 - 1) {
                            if (integer16 > 86 + integer13 * 2) {
                                if (boolean24) {
                                    cft.setBlockState(a28, Blocks.COARSE_DIRT.defaultBlockState(), false);
                                }
                                else {
                                    cft.setBlockState(a28, Blocks.GRASS_BLOCK.defaultBlockState(), false);
                                }
                            }
                            else if (integer16 > integer10 + 3 + integer13) {
                                BlockState cee15;
                                if (integer16 < 64 || integer16 > 127) {
                                    cee15 = WoodedBadlandsSurfaceBuilder.ORANGE_TERRACOTTA;
                                }
                                else if (boolean24) {
                                    cee15 = WoodedBadlandsSurfaceBuilder.TERRACOTTA;
                                }
                                else {
                                    cee15 = this.getBand(integer4, integer16, integer5);
                                }
                                cft.setBlockState(a28, cee15, false);
                            }
                            else {
                                cft.setBlockState(a28, cee12, false);
                                boolean25 = true;
                            }
                        }
                        else {
                            cft.setBlockState(a28, cee13, false);
                            if (cee13 == WoodedBadlandsSurfaceBuilder.WHITE_TERRACOTTA) {
                                cft.setBlockState(a28, WoodedBadlandsSurfaceBuilder.ORANGE_TERRACOTTA, false);
                            }
                        }
                    }
                    else if (integer14 > 0) {
                        --integer14;
                        if (boolean25) {
                            cft.setBlockState(a28, WoodedBadlandsSurfaceBuilder.ORANGE_TERRACOTTA, false);
                        }
                        else {
                            cft.setBlockState(a28, this.getBand(integer4, integer16, integer5), false);
                        }
                    }
                    ++integer15;
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
