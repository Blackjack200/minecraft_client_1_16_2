package net.minecraft.world.level.levelgen.surfacebuilders;

import java.util.Arrays;
import java.util.List;
import com.google.common.collect.ImmutableList;
import java.util.stream.IntStream;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.block.state.BlockState;

public class BadlandsSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    private static final BlockState WHITE_TERRACOTTA;
    private static final BlockState ORANGE_TERRACOTTA;
    private static final BlockState TERRACOTTA;
    private static final BlockState YELLOW_TERRACOTTA;
    private static final BlockState BROWN_TERRACOTTA;
    private static final BlockState RED_TERRACOTTA;
    private static final BlockState LIGHT_GRAY_TERRACOTTA;
    protected BlockState[] clayBands;
    protected long seed;
    protected PerlinSimplexNoise pillarNoise;
    protected PerlinSimplexNoise pillarRoofNoise;
    protected PerlinSimplexNoise clayBandsOffsetNoise;
    
    public BadlandsSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        final int integer11 = integer4 & 0xF;
        final int integer12 = integer5 & 0xF;
        BlockState cee10 = BadlandsSurfaceBuilder.WHITE_TERRACOTTA;
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
                            cee10 = BadlandsSurfaceBuilder.WHITE_TERRACOTTA;
                            cee13 = cee11;
                        }
                        if (integer16 < integer10 && (cee10 == null || cee10.isAir())) {
                            cee10 = cee9;
                        }
                        integer14 = integer13 + Math.max(0, integer16 - integer10);
                        if (integer16 >= integer10 - 1) {
                            if (integer16 > integer10 + 3 + integer13) {
                                BlockState cee15;
                                if (integer16 < 64 || integer16 > 127) {
                                    cee15 = BadlandsSurfaceBuilder.ORANGE_TERRACOTTA;
                                }
                                else if (boolean24) {
                                    cee15 = BadlandsSurfaceBuilder.TERRACOTTA;
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
                            final Block bul31 = cee13.getBlock();
                            if (bul31 == Blocks.WHITE_TERRACOTTA || bul31 == Blocks.ORANGE_TERRACOTTA || bul31 == Blocks.MAGENTA_TERRACOTTA || bul31 == Blocks.LIGHT_BLUE_TERRACOTTA || bul31 == Blocks.YELLOW_TERRACOTTA || bul31 == Blocks.LIME_TERRACOTTA || bul31 == Blocks.PINK_TERRACOTTA || bul31 == Blocks.GRAY_TERRACOTTA || bul31 == Blocks.LIGHT_GRAY_TERRACOTTA || bul31 == Blocks.CYAN_TERRACOTTA || bul31 == Blocks.PURPLE_TERRACOTTA || bul31 == Blocks.BLUE_TERRACOTTA || bul31 == Blocks.BROWN_TERRACOTTA || bul31 == Blocks.GREEN_TERRACOTTA || bul31 == Blocks.RED_TERRACOTTA || bul31 == Blocks.BLACK_TERRACOTTA) {
                                cft.setBlockState(a28, BadlandsSurfaceBuilder.ORANGE_TERRACOTTA, false);
                            }
                        }
                    }
                    else if (integer14 > 0) {
                        --integer14;
                        if (boolean25) {
                            cft.setBlockState(a28, BadlandsSurfaceBuilder.ORANGE_TERRACOTTA, false);
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
    
    @Override
    public void initNoise(final long long1) {
        if (this.seed != long1 || this.clayBands == null) {
            this.generateBands(long1);
        }
        if (this.seed != long1 || this.pillarNoise == null || this.pillarRoofNoise == null) {
            final WorldgenRandom chu4 = new WorldgenRandom(long1);
            this.pillarNoise = new PerlinSimplexNoise(chu4, IntStream.rangeClosed(-3, 0));
            this.pillarRoofNoise = new PerlinSimplexNoise(chu4, (List<Integer>)ImmutableList.of(0));
        }
        this.seed = long1;
    }
    
    protected void generateBands(final long long1) {
        Arrays.fill((Object[])(this.clayBands = new BlockState[64]), BadlandsSurfaceBuilder.TERRACOTTA);
        final WorldgenRandom chu4 = new WorldgenRandom(long1);
        this.clayBandsOffsetNoise = new PerlinSimplexNoise(chu4, (List<Integer>)ImmutableList.of(0));
        for (int integer5 = 0; integer5 < 64; ++integer5) {
            integer5 += chu4.nextInt(5) + 1;
            if (integer5 < 64) {
                this.clayBands[integer5] = BadlandsSurfaceBuilder.ORANGE_TERRACOTTA;
            }
        }
        for (int integer5 = chu4.nextInt(4) + 2, integer6 = 0; integer6 < integer5; ++integer6) {
            for (int integer7 = chu4.nextInt(3) + 1, integer8 = chu4.nextInt(64), integer9 = 0; integer8 + integer9 < 64 && integer9 < integer7; ++integer9) {
                this.clayBands[integer8 + integer9] = BadlandsSurfaceBuilder.YELLOW_TERRACOTTA;
            }
        }
        for (int integer6 = chu4.nextInt(4) + 2, integer7 = 0; integer7 < integer6; ++integer7) {
            for (int integer8 = chu4.nextInt(3) + 2, integer9 = chu4.nextInt(64), integer10 = 0; integer9 + integer10 < 64 && integer10 < integer8; ++integer10) {
                this.clayBands[integer9 + integer10] = BadlandsSurfaceBuilder.BROWN_TERRACOTTA;
            }
        }
        for (int integer7 = chu4.nextInt(4) + 2, integer8 = 0; integer8 < integer7; ++integer8) {
            for (int integer9 = chu4.nextInt(3) + 1, integer10 = chu4.nextInt(64), integer11 = 0; integer10 + integer11 < 64 && integer11 < integer9; ++integer11) {
                this.clayBands[integer10 + integer11] = BadlandsSurfaceBuilder.RED_TERRACOTTA;
            }
        }
        int integer8 = chu4.nextInt(3) + 3;
        int integer9 = 0;
        for (int integer10 = 0; integer10 < integer8; ++integer10) {
            final int integer11 = 1;
            integer9 += chu4.nextInt(16) + 4;
            for (int integer12 = 0; integer9 + integer12 < 64 && integer12 < 1; ++integer12) {
                this.clayBands[integer9 + integer12] = BadlandsSurfaceBuilder.WHITE_TERRACOTTA;
                if (integer9 + integer12 > 1 && chu4.nextBoolean()) {
                    this.clayBands[integer9 + integer12 - 1] = BadlandsSurfaceBuilder.LIGHT_GRAY_TERRACOTTA;
                }
                if (integer9 + integer12 < 63 && chu4.nextBoolean()) {
                    this.clayBands[integer9 + integer12 + 1] = BadlandsSurfaceBuilder.LIGHT_GRAY_TERRACOTTA;
                }
            }
        }
    }
    
    protected BlockState getBand(final int integer1, final int integer2, final int integer3) {
        final int integer4 = (int)Math.round(this.clayBandsOffsetNoise.getValue(integer1 / 512.0, integer3 / 512.0, false) * 2.0);
        return this.clayBands[(integer2 + integer4 + 64) % 64];
    }
    
    static {
        WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
        ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
        YELLOW_TERRACOTTA = Blocks.YELLOW_TERRACOTTA.defaultBlockState();
        BROWN_TERRACOTTA = Blocks.BROWN_TERRACOTTA.defaultBlockState();
        RED_TERRACOTTA = Blocks.RED_TERRACOTTA.defaultBlockState();
        LIGHT_GRAY_TERRACOTTA = Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();
    }
}
