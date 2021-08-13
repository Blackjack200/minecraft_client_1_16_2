package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.BitSet;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class OreFeature extends Feature<OreConfiguration> {
    public OreFeature(final Codec<OreConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final OreConfiguration cmg) {
        final float float7 = random.nextFloat() * 3.1415927f;
        final float float8 = cmg.size / 8.0f;
        final int integer9 = Mth.ceil((cmg.size / 16.0f * 2.0f + 1.0f) / 2.0f);
        final double double10 = fx.getX() + Math.sin((double)float7) * float8;
        final double double11 = fx.getX() - Math.sin((double)float7) * float8;
        final double double12 = fx.getZ() + Math.cos((double)float7) * float8;
        final double double13 = fx.getZ() - Math.cos((double)float7) * float8;
        final int integer10 = 2;
        final double double14 = fx.getY() + random.nextInt(3) - 2;
        final double double15 = fx.getY() + random.nextInt(3) - 2;
        final int integer11 = fx.getX() - Mth.ceil(float8) - integer9;
        final int integer12 = fx.getY() - 2 - integer9;
        final int integer13 = fx.getZ() - Mth.ceil(float8) - integer9;
        final int integer14 = 2 * (Mth.ceil(float8) + integer9);
        final int integer15 = 2 * (2 + integer9);
        for (int integer16 = integer11; integer16 <= integer11 + integer14; ++integer16) {
            for (int integer17 = integer13; integer17 <= integer13 + integer14; ++integer17) {
                if (integer12 <= bso.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, integer16, integer17)) {
                    return this.doPlace(bso, random, cmg, double10, double11, double12, double13, double14, double15, integer11, integer12, integer13, integer14, integer15);
                }
            }
        }
        return false;
    }
    
    protected boolean doPlace(final LevelAccessor brv, final Random random, final OreConfiguration cmg, final double double4, final double double5, final double double6, final double double7, final double double8, final double double9, final int integer10, final int integer11, final int integer12, final int integer13, final int integer14) {
        int integer15 = 0;
        final BitSet bitSet23 = new BitSet(integer13 * integer14 * integer13);
        final BlockPos.MutableBlockPos a24 = new BlockPos.MutableBlockPos();
        final int integer16 = cmg.size;
        final double[] arr26 = new double[integer16 * 4];
        for (int integer17 = 0; integer17 < integer16; ++integer17) {
            final float float28 = integer17 / (float)integer16;
            final double double10 = Mth.lerp(float28, double4, double5);
            final double double11 = Mth.lerp(float28, double8, double9);
            final double double12 = Mth.lerp(float28, double6, double7);
            final double double13 = random.nextDouble() * integer16 / 16.0;
            final double double14 = ((Mth.sin(3.1415927f * float28) + 1.0f) * double13 + 1.0) / 2.0;
            arr26[integer17 * 4 + 0] = double10;
            arr26[integer17 * 4 + 1] = double11;
            arr26[integer17 * 4 + 2] = double12;
            arr26[integer17 * 4 + 3] = double14;
        }
        for (int integer17 = 0; integer17 < integer16 - 1; ++integer17) {
            if (arr26[integer17 * 4 + 3] > 0.0) {
                for (int integer18 = integer17 + 1; integer18 < integer16; ++integer18) {
                    if (arr26[integer18 * 4 + 3] > 0.0) {
                        final double double10 = arr26[integer17 * 4 + 0] - arr26[integer18 * 4 + 0];
                        final double double11 = arr26[integer17 * 4 + 1] - arr26[integer18 * 4 + 1];
                        final double double12 = arr26[integer17 * 4 + 2] - arr26[integer18 * 4 + 2];
                        final double double13 = arr26[integer17 * 4 + 3] - arr26[integer18 * 4 + 3];
                        if (double13 * double13 > double10 * double10 + double11 * double11 + double12 * double12) {
                            if (double13 > 0.0) {
                                arr26[integer18 * 4 + 3] = -1.0;
                            }
                            else {
                                arr26[integer17 * 4 + 3] = -1.0;
                            }
                        }
                    }
                }
            }
        }
        for (int integer17 = 0; integer17 < integer16; ++integer17) {
            final double double15 = arr26[integer17 * 4 + 3];
            if (double15 >= 0.0) {
                final double double16 = arr26[integer17 * 4 + 0];
                final double double17 = arr26[integer17 * 4 + 1];
                final double double18 = arr26[integer17 * 4 + 2];
                final int integer19 = Math.max(Mth.floor(double16 - double15), integer10);
                final int integer20 = Math.max(Mth.floor(double17 - double15), integer11);
                final int integer21 = Math.max(Mth.floor(double18 - double15), integer12);
                final int integer22 = Math.max(Mth.floor(double16 + double15), integer19);
                final int integer23 = Math.max(Mth.floor(double17 + double15), integer20);
                final int integer24 = Math.max(Mth.floor(double18 + double15), integer21);
                for (int integer25 = integer19; integer25 <= integer22; ++integer25) {
                    final double double19 = (integer25 + 0.5 - double16) / double15;
                    if (double19 * double19 < 1.0) {
                        for (int integer26 = integer20; integer26 <= integer23; ++integer26) {
                            final double double20 = (integer26 + 0.5 - double17) / double15;
                            if (double19 * double19 + double20 * double20 < 1.0) {
                                for (int integer27 = integer21; integer27 <= integer24; ++integer27) {
                                    final double double21 = (integer27 + 0.5 - double18) / double15;
                                    if (double19 * double19 + double20 * double20 + double21 * double21 < 1.0) {
                                        final int integer28 = integer25 - integer10 + (integer26 - integer11) * integer13 + (integer27 - integer12) * integer13 * integer14;
                                        if (!bitSet23.get(integer28)) {
                                            bitSet23.set(integer28);
                                            a24.set(integer25, integer26, integer27);
                                            if (cmg.target.test(brv.getBlockState(a24), random)) {
                                                brv.setBlock(a24, cmg.state, 2);
                                                ++integer15;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return integer15 > 0;
    }
}
