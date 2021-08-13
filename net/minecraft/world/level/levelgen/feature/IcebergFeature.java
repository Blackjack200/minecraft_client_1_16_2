package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class IcebergFeature extends Feature<BlockStateConfiguration> {
    public IcebergFeature(final Codec<BlockStateConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, BlockPos fx, final BlockStateConfiguration clp) {
        fx = new BlockPos(fx.getX(), cfv.getSeaLevel(), fx.getZ());
        final boolean boolean7 = random.nextDouble() > 0.7;
        final BlockState cee8 = clp.state;
        final double double9 = random.nextDouble() * 2.0 * 3.141592653589793;
        final int integer11 = 11 - random.nextInt(5);
        final int integer12 = 3 + random.nextInt(3);
        final boolean boolean8 = random.nextDouble() > 0.7;
        final int integer13 = 11;
        int integer14 = boolean8 ? (random.nextInt(6) + 6) : (random.nextInt(15) + 3);
        if (!boolean8 && random.nextDouble() > 0.9) {
            integer14 += random.nextInt(19) + 7;
        }
        final int integer15 = Math.min(integer14 + random.nextInt(11), 18);
        final int integer16 = Math.min(integer14 + random.nextInt(7) - random.nextInt(5), 11);
        final int integer17 = boolean8 ? integer11 : 11;
        for (int integer18 = -integer17; integer18 < integer17; ++integer18) {
            for (int integer19 = -integer17; integer19 < integer17; ++integer19) {
                for (int integer20 = 0; integer20 < integer14; ++integer20) {
                    final int integer21 = boolean8 ? this.heightDependentRadiusEllipse(integer20, integer14, integer16) : this.heightDependentRadiusRound(random, integer20, integer14, integer16);
                    if (boolean8 || integer18 < integer21) {
                        this.generateIcebergBlock(bso, random, fx, integer14, integer18, integer20, integer19, integer21, integer17, boolean8, integer12, double9, boolean7, cee8);
                    }
                }
            }
        }
        this.smooth(bso, fx, integer16, integer14, boolean8, integer11);
        for (int integer18 = -integer17; integer18 < integer17; ++integer18) {
            for (int integer19 = -integer17; integer19 < integer17; ++integer19) {
                for (int integer20 = -1; integer20 > -integer15; --integer20) {
                    final int integer21 = boolean8 ? Mth.ceil(integer17 * (1.0f - (float)Math.pow((double)integer20, 2.0) / (integer15 * 8.0f))) : integer17;
                    final int integer22 = this.heightDependentRadiusSteep(random, -integer20, integer15, integer16);
                    if (integer18 < integer22) {
                        this.generateIcebergBlock(bso, random, fx, integer15, integer18, integer20, integer19, integer22, integer21, boolean8, integer12, double9, boolean7, cee8);
                    }
                }
            }
        }
        final boolean boolean9 = boolean8 ? (random.nextDouble() > 0.1) : (random.nextDouble() > 0.7);
        if (boolean9) {
            this.generateCutOut(random, bso, integer16, integer14, fx, boolean8, integer11, double9, integer12);
        }
        return true;
    }
    
    private void generateCutOut(final Random random, final LevelAccessor brv, final int integer3, final int integer4, final BlockPos fx, final boolean boolean6, final int integer7, final double double8, final int integer9) {
        final int integer10 = random.nextBoolean() ? -1 : 1;
        final int integer11 = random.nextBoolean() ? -1 : 1;
        int integer12 = random.nextInt(Math.max(integer3 / 2 - 2, 1));
        if (random.nextBoolean()) {
            integer12 = integer3 / 2 + 1 - random.nextInt(Math.max(integer3 - integer3 / 2 - 1, 1));
        }
        int integer13 = random.nextInt(Math.max(integer3 / 2 - 2, 1));
        if (random.nextBoolean()) {
            integer13 = integer3 / 2 + 1 - random.nextInt(Math.max(integer3 - integer3 / 2 - 1, 1));
        }
        if (boolean6) {
            integer13 = (integer12 = random.nextInt(Math.max(integer7 - 5, 1)));
        }
        final BlockPos fx2 = new BlockPos(integer10 * integer12, 0, integer11 * integer13);
        final double double9 = boolean6 ? (double8 + 1.5707963267948966) : (random.nextDouble() * 2.0 * 3.141592653589793);
        for (int integer14 = 0; integer14 < integer4 - 3; ++integer14) {
            final int integer15 = this.heightDependentRadiusRound(random, integer14, integer4, integer3);
            this.carve(integer15, integer14, fx, brv, false, double9, fx2, integer7, integer9);
        }
        for (int integer14 = -1; integer14 > -integer4 + random.nextInt(5); --integer14) {
            final int integer15 = this.heightDependentRadiusSteep(random, -integer14, integer4, integer3);
            this.carve(integer15, integer14, fx, brv, true, double9, fx2, integer7, integer9);
        }
    }
    
    private void carve(final int integer1, final int integer2, final BlockPos fx3, final LevelAccessor brv, final boolean boolean5, final double double6, final BlockPos fx7, final int integer8, final int integer9) {
        final int integer10 = integer1 + 1 + integer8 / 3;
        final int integer11 = Math.min(integer1 - 3, 3) + integer9 / 2 - 1;
        for (int integer12 = -integer10; integer12 < integer10; ++integer12) {
            for (int integer13 = -integer10; integer13 < integer10; ++integer13) {
                final double double7 = this.signedDistanceEllipse(integer12, integer13, fx7, integer10, integer11, double6);
                if (double7 < 0.0) {
                    final BlockPos fx8 = fx3.offset(integer12, integer2, integer13);
                    final Block bul19 = brv.getBlockState(fx8).getBlock();
                    if (this.isIcebergBlock(bul19) || bul19 == Blocks.SNOW_BLOCK) {
                        if (boolean5) {
                            this.setBlock(brv, fx8, Blocks.WATER.defaultBlockState());
                        }
                        else {
                            this.setBlock(brv, fx8, Blocks.AIR.defaultBlockState());
                            this.removeFloatingSnowLayer(brv, fx8);
                        }
                    }
                }
            }
        }
    }
    
    private void removeFloatingSnowLayer(final LevelAccessor brv, final BlockPos fx) {
        if (brv.getBlockState(fx.above()).is(Blocks.SNOW)) {
            this.setBlock(brv, fx.above(), Blocks.AIR.defaultBlockState());
        }
    }
    
    private void generateIcebergBlock(final LevelAccessor brv, final Random random, final BlockPos fx, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9, final boolean boolean10, final int integer11, final double double12, final boolean boolean13, final BlockState cee) {
        final double double13 = boolean10 ? this.signedDistanceEllipse(integer5, integer7, BlockPos.ZERO, integer9, this.getEllipseC(integer6, integer4, integer11), double12) : this.signedDistanceCircle(integer5, integer7, BlockPos.ZERO, integer8, random);
        if (double13 < 0.0) {
            final BlockPos fx2 = fx.offset(integer5, integer6, integer7);
            final double double14 = boolean10 ? -0.5 : (-6 - random.nextInt(3));
            if (double13 > double14 && random.nextDouble() > 0.9) {
                return;
            }
            this.setIcebergBlock(fx2, brv, random, integer4 - integer6, integer4, boolean10, boolean13, cee);
        }
    }
    
    private void setIcebergBlock(final BlockPos fx, final LevelAccessor brv, final Random random, final int integer4, final int integer5, final boolean boolean6, final boolean boolean7, final BlockState cee) {
        final BlockState cee2 = brv.getBlockState(fx);
        if (cee2.getMaterial() == Material.AIR || cee2.is(Blocks.SNOW_BLOCK) || cee2.is(Blocks.ICE) || cee2.is(Blocks.WATER)) {
            final boolean boolean8 = !boolean6 || random.nextDouble() > 0.05;
            final int integer6 = boolean6 ? 3 : 2;
            if (boolean7 && !cee2.is(Blocks.WATER) && integer4 <= random.nextInt(Math.max(1, integer5 / integer6)) + integer5 * 0.6 && boolean8) {
                this.setBlock(brv, fx, Blocks.SNOW_BLOCK.defaultBlockState());
            }
            else {
                this.setBlock(brv, fx, cee);
            }
        }
    }
    
    private int getEllipseC(final int integer1, final int integer2, final int integer3) {
        int integer4 = integer3;
        if (integer1 > 0 && integer2 - integer1 <= 3) {
            integer4 -= 4 - (integer2 - integer1);
        }
        return integer4;
    }
    
    private double signedDistanceCircle(final int integer1, final int integer2, final BlockPos fx, final int integer4, final Random random) {
        final float float7 = 10.0f * Mth.clamp(random.nextFloat(), 0.2f, 0.8f) / integer4;
        return float7 + Math.pow((double)(integer1 - fx.getX()), 2.0) + Math.pow((double)(integer2 - fx.getZ()), 2.0) - Math.pow((double)integer4, 2.0);
    }
    
    private double signedDistanceEllipse(final int integer1, final int integer2, final BlockPos fx, final int integer4, final int integer5, final double double6) {
        return Math.pow(((integer1 - fx.getX()) * Math.cos(double6) - (integer2 - fx.getZ()) * Math.sin(double6)) / integer4, 2.0) + Math.pow(((integer1 - fx.getX()) * Math.sin(double6) + (integer2 - fx.getZ()) * Math.cos(double6)) / integer5, 2.0) - 1.0;
    }
    
    private int heightDependentRadiusRound(final Random random, final int integer2, final int integer3, final int integer4) {
        final float float6 = 3.5f - random.nextFloat();
        float float7 = (1.0f - (float)Math.pow((double)integer2, 2.0) / (integer3 * float6)) * integer4;
        if (integer3 > 15 + random.nextInt(5)) {
            final int integer5 = (integer2 < 3 + random.nextInt(6)) ? (integer2 / 2) : integer2;
            float7 = (1.0f - integer5 / (integer3 * float6 * 0.4f)) * integer4;
        }
        return Mth.ceil(float7 / 2.0f);
    }
    
    private int heightDependentRadiusEllipse(final int integer1, final int integer2, final int integer3) {
        final float float5 = 1.0f;
        final float float6 = (1.0f - (float)Math.pow((double)integer1, 2.0) / (integer2 * 1.0f)) * integer3;
        return Mth.ceil(float6 / 2.0f);
    }
    
    private int heightDependentRadiusSteep(final Random random, final int integer2, final int integer3, final int integer4) {
        final float float6 = 1.0f + random.nextFloat() / 2.0f;
        final float float7 = (1.0f - integer2 / (integer3 * float6)) * integer4;
        return Mth.ceil(float7 / 2.0f);
    }
    
    private boolean isIcebergBlock(final Block bul) {
        return bul == Blocks.PACKED_ICE || bul == Blocks.SNOW_BLOCK || bul == Blocks.BLUE_ICE;
    }
    
    private boolean belowIsAir(final BlockGetter bqz, final BlockPos fx) {
        return bqz.getBlockState(fx.below()).getMaterial() == Material.AIR;
    }
    
    private void smooth(final LevelAccessor brv, final BlockPos fx, final int integer3, final int integer4, final boolean boolean5, final int integer6) {
        for (int integer7 = boolean5 ? integer6 : (integer3 / 2), integer8 = -integer7; integer8 <= integer7; ++integer8) {
            for (int integer9 = -integer7; integer9 <= integer7; ++integer9) {
                for (int integer10 = 0; integer10 <= integer4; ++integer10) {
                    final BlockPos fx2 = fx.offset(integer8, integer10, integer9);
                    final Block bul13 = brv.getBlockState(fx2).getBlock();
                    if (this.isIcebergBlock(bul13) || bul13 == Blocks.SNOW) {
                        if (this.belowIsAir(brv, fx2)) {
                            this.setBlock(brv, fx2, Blocks.AIR.defaultBlockState());
                            this.setBlock(brv, fx2.above(), Blocks.AIR.defaultBlockState());
                        }
                        else if (this.isIcebergBlock(bul13)) {
                            final Block[] arr14 = { brv.getBlockState(fx2.west()).getBlock(), brv.getBlockState(fx2.east()).getBlock(), brv.getBlockState(fx2.north()).getBlock(), brv.getBlockState(fx2.south()).getBlock() };
                            int integer11 = 0;
                            for (final Block bul14 : arr14) {
                                if (!this.isIcebergBlock(bul14)) {
                                    ++integer11;
                                }
                            }
                            if (integer11 >= 3) {
                                this.setBlock(brv, fx2, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }
    }
}
