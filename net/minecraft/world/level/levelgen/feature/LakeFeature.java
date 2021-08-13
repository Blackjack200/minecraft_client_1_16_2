package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LightLayer;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class LakeFeature extends Feature<BlockStateConfiguration> {
    private static final BlockState AIR;
    
    public LakeFeature(final Codec<BlockStateConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, BlockPos fx, final BlockStateConfiguration clp) {
        while (fx.getY() > 5 && bso.isEmptyBlock(fx)) {
            fx = fx.below();
        }
        if (fx.getY() <= 4) {
            return false;
        }
        fx = fx.below(4);
        if (bso.startsForFeature(SectionPos.of(fx), StructureFeature.VILLAGE).findAny().isPresent()) {
            return false;
        }
        final boolean[] arr7 = new boolean[2048];
        for (int integer8 = random.nextInt(4) + 4, integer9 = 0; integer9 < integer8; ++integer9) {
            final double double10 = random.nextDouble() * 6.0 + 3.0;
            final double double11 = random.nextDouble() * 4.0 + 2.0;
            final double double12 = random.nextDouble() * 6.0 + 3.0;
            final double double13 = random.nextDouble() * (16.0 - double10 - 2.0) + 1.0 + double10 / 2.0;
            final double double14 = random.nextDouble() * (8.0 - double11 - 4.0) + 2.0 + double11 / 2.0;
            final double double15 = random.nextDouble() * (16.0 - double12 - 2.0) + 1.0 + double12 / 2.0;
            for (int integer10 = 1; integer10 < 15; ++integer10) {
                for (int integer11 = 1; integer11 < 15; ++integer11) {
                    for (int integer12 = 1; integer12 < 7; ++integer12) {
                        final double double16 = (integer10 - double13) / (double10 / 2.0);
                        final double double17 = (integer12 - double14) / (double11 / 2.0);
                        final double double18 = (integer11 - double15) / (double12 / 2.0);
                        final double double19 = double16 * double16 + double17 * double17 + double18 * double18;
                        if (double19 < 1.0) {
                            arr7[(integer10 * 16 + integer11) * 8 + integer12] = true;
                        }
                    }
                }
            }
        }
        for (int integer9 = 0; integer9 < 16; ++integer9) {
            for (int integer13 = 0; integer13 < 16; ++integer13) {
                for (int integer14 = 0; integer14 < 8; ++integer14) {
                    final boolean boolean12 = !arr7[(integer9 * 16 + integer13) * 8 + integer14] && ((integer9 < 15 && arr7[((integer9 + 1) * 16 + integer13) * 8 + integer14]) || (integer9 > 0 && arr7[((integer9 - 1) * 16 + integer13) * 8 + integer14]) || (integer13 < 15 && arr7[(integer9 * 16 + integer13 + 1) * 8 + integer14]) || (integer13 > 0 && arr7[(integer9 * 16 + (integer13 - 1)) * 8 + integer14]) || (integer14 < 7 && arr7[(integer9 * 16 + integer13) * 8 + integer14 + 1]) || (integer14 > 0 && arr7[(integer9 * 16 + integer13) * 8 + (integer14 - 1)]));
                    if (boolean12) {
                        final Material cux13 = bso.getBlockState(fx.offset(integer9, integer14, integer13)).getMaterial();
                        if (integer14 >= 4 && cux13.isLiquid()) {
                            return false;
                        }
                        if (integer14 < 4 && !cux13.isSolid() && bso.getBlockState(fx.offset(integer9, integer14, integer13)) != clp.state) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int integer9 = 0; integer9 < 16; ++integer9) {
            for (int integer13 = 0; integer13 < 16; ++integer13) {
                for (int integer14 = 0; integer14 < 8; ++integer14) {
                    if (arr7[(integer9 * 16 + integer13) * 8 + integer14]) {
                        bso.setBlock(fx.offset(integer9, integer14, integer13), (integer14 >= 4) ? LakeFeature.AIR : clp.state, 2);
                    }
                }
            }
        }
        for (int integer9 = 0; integer9 < 16; ++integer9) {
            for (int integer13 = 0; integer13 < 16; ++integer13) {
                for (int integer14 = 4; integer14 < 8; ++integer14) {
                    if (arr7[(integer9 * 16 + integer13) * 8 + integer14]) {
                        final BlockPos fx2 = fx.offset(integer9, integer14 - 1, integer13);
                        if (Feature.isDirt(bso.getBlockState(fx2).getBlock()) && bso.getBrightness(LightLayer.SKY, fx.offset(integer9, integer14, integer13)) > 0) {
                            final Biome bss13 = bso.getBiome(fx2);
                            if (bss13.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial().is(Blocks.MYCELIUM)) {
                                bso.setBlock(fx2, Blocks.MYCELIUM.defaultBlockState(), 2);
                            }
                            else {
                                bso.setBlock(fx2, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
        if (clp.state.getMaterial() == Material.LAVA) {
            for (int integer9 = 0; integer9 < 16; ++integer9) {
                for (int integer13 = 0; integer13 < 16; ++integer13) {
                    for (int integer14 = 0; integer14 < 8; ++integer14) {
                        final boolean boolean12 = !arr7[(integer9 * 16 + integer13) * 8 + integer14] && ((integer9 < 15 && arr7[((integer9 + 1) * 16 + integer13) * 8 + integer14]) || (integer9 > 0 && arr7[((integer9 - 1) * 16 + integer13) * 8 + integer14]) || (integer13 < 15 && arr7[(integer9 * 16 + integer13 + 1) * 8 + integer14]) || (integer13 > 0 && arr7[(integer9 * 16 + (integer13 - 1)) * 8 + integer14]) || (integer14 < 7 && arr7[(integer9 * 16 + integer13) * 8 + integer14 + 1]) || (integer14 > 0 && arr7[(integer9 * 16 + integer13) * 8 + (integer14 - 1)]));
                        if (boolean12 && (integer14 < 4 || random.nextInt(2) != 0) && bso.getBlockState(fx.offset(integer9, integer14, integer13)).getMaterial().isSolid()) {
                            bso.setBlock(fx.offset(integer9, integer14, integer13), Blocks.STONE.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
        if (clp.state.getMaterial() == Material.WATER) {
            for (int integer9 = 0; integer9 < 16; ++integer9) {
                for (int integer13 = 0; integer13 < 16; ++integer13) {
                    final int integer14 = 4;
                    final BlockPos fx2 = fx.offset(integer9, 4, integer13);
                    if (bso.getBiome(fx2).shouldFreeze(bso, fx2, false)) {
                        bso.setBlock(fx2, Blocks.ICE.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
    
    static {
        AIR = Blocks.CAVE_AIR.defaultBlockState();
    }
}
