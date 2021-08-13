package net.minecraft.world.level.levelgen;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.chunk.LevelChunkSection;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.LevelAccessor;
import java.util.Iterator;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.NoiseColumn;
import java.util.function.Predicate;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceKey;
import java.util.Random;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import java.util.stream.IntStream;
import net.minecraft.world.level.biome.BiomeSource;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraft.world.level.levelgen.synth.SurfaceNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;

public final class NoiseBasedChunkGenerator extends ChunkGenerator {
    public static final Codec<NoiseBasedChunkGenerator> CODEC;
    private static final float[] BEARD_KERNEL;
    private static final float[] BIOME_WEIGHTS;
    private static final BlockState AIR;
    private final int chunkHeight;
    private final int chunkWidth;
    private final int chunkCountX;
    private final int chunkCountY;
    private final int chunkCountZ;
    protected final WorldgenRandom random;
    private final PerlinNoise minLimitPerlinNoise;
    private final PerlinNoise maxLimitPerlinNoise;
    private final PerlinNoise mainPerlinNoise;
    private final SurfaceNoise surfaceNoise;
    private final PerlinNoise depthNoise;
    @Nullable
    private final SimplexNoise islandNoise;
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    private final long seed;
    protected final Supplier<NoiseGeneratorSettings> settings;
    private final int height;
    
    public NoiseBasedChunkGenerator(final BiomeSource bsv, final long long2, final Supplier<NoiseGeneratorSettings> supplier) {
        this(bsv, bsv, long2, supplier);
    }
    
    private NoiseBasedChunkGenerator(final BiomeSource bsv1, final BiomeSource bsv2, final long long3, final Supplier<NoiseGeneratorSettings> supplier) {
        super(bsv1, bsv2, ((NoiseGeneratorSettings)supplier.get()).structureSettings(), long3);
        this.seed = long3;
        final NoiseGeneratorSettings chm7 = (NoiseGeneratorSettings)supplier.get();
        this.settings = supplier;
        final NoiseSettings cho8 = chm7.noiseSettings();
        this.height = cho8.height();
        this.chunkHeight = cho8.noiseSizeVertical() * 4;
        this.chunkWidth = cho8.noiseSizeHorizontal() * 4;
        this.defaultBlock = chm7.getDefaultBlock();
        this.defaultFluid = chm7.getDefaultFluid();
        this.chunkCountX = 16 / this.chunkWidth;
        this.chunkCountY = cho8.height() / this.chunkHeight;
        this.chunkCountZ = 16 / this.chunkWidth;
        this.random = new WorldgenRandom(long3);
        this.minLimitPerlinNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
        this.maxLimitPerlinNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
        this.mainPerlinNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-7, 0));
        this.surfaceNoise = (cho8.useSimplexSurfaceNoise() ? new PerlinSimplexNoise(this.random, IntStream.rangeClosed(-3, 0)) : new PerlinNoise(this.random, IntStream.rangeClosed(-3, 0)));
        this.random.consumeCount(2620);
        this.depthNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
        if (cho8.islandNoiseOverride()) {
            final WorldgenRandom chu9 = new WorldgenRandom(long3);
            chu9.consumeCount(17292);
            this.islandNoise = new SimplexNoise(chu9);
        }
        else {
            this.islandNoise = null;
        }
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return NoiseBasedChunkGenerator.CODEC;
    }
    
    @Override
    public ChunkGenerator withSeed(final long long1) {
        return new NoiseBasedChunkGenerator(this.biomeSource.withSeed(long1), long1, this.settings);
    }
    
    public boolean stable(final long long1, final ResourceKey<NoiseGeneratorSettings> vj) {
        return this.seed == long1 && ((NoiseGeneratorSettings)this.settings.get()).stable(vj);
    }
    
    private double sampleAndClampNoise(final int integer1, final int integer2, final int integer3, final double double4, final double double5, final double double6, final double double7) {
        double double8 = 0.0;
        double double9 = 0.0;
        double double10 = 0.0;
        final boolean boolean19 = true;
        double double11 = 1.0;
        for (int integer4 = 0; integer4 < 16; ++integer4) {
            final double double12 = PerlinNoise.wrap(integer1 * double4 * double11);
            final double double13 = PerlinNoise.wrap(integer2 * double5 * double11);
            final double double14 = PerlinNoise.wrap(integer3 * double4 * double11);
            final double double15 = double5 * double11;
            final ImprovedNoise ctw31 = this.minLimitPerlinNoise.getOctaveNoise(integer4);
            if (ctw31 != null) {
                double8 += ctw31.noise(double12, double13, double14, double15, integer2 * double15) / double11;
            }
            final ImprovedNoise ctw32 = this.maxLimitPerlinNoise.getOctaveNoise(integer4);
            if (ctw32 != null) {
                double9 += ctw32.noise(double12, double13, double14, double15, integer2 * double15) / double11;
            }
            if (integer4 < 8) {
                final ImprovedNoise ctw33 = this.mainPerlinNoise.getOctaveNoise(integer4);
                if (ctw33 != null) {
                    double10 += ctw33.noise(PerlinNoise.wrap(integer1 * double6 * double11), PerlinNoise.wrap(integer2 * double7 * double11), PerlinNoise.wrap(integer3 * double6 * double11), double7 * double11, integer2 * double7 * double11) / double11;
                }
            }
            double11 /= 2.0;
        }
        return Mth.clampedLerp(double8 / 512.0, double9 / 512.0, (double10 / 10.0 + 1.0) / 2.0);
    }
    
    private double[] makeAndFillNoiseColumn(final int integer1, final int integer2) {
        final double[] arr4 = new double[this.chunkCountY + 1];
        this.fillNoiseColumn(arr4, integer1, integer2);
        return arr4;
    }
    
    private void fillNoiseColumn(final double[] arr, final int integer2, final int integer3) {
        final NoiseSettings cho9 = ((NoiseGeneratorSettings)this.settings.get()).noiseSettings();
        double double5;
        double double6;
        if (this.islandNoise != null) {
            double5 = TheEndBiomeSource.getHeightValue(this.islandNoise, integer2, integer3) - 8.0f;
            if (double5 > 0.0) {
                double6 = 0.25;
            }
            else {
                double6 = 1.0;
            }
        }
        else {
            float float10 = 0.0f;
            float float11 = 0.0f;
            float float12 = 0.0f;
            final int integer4 = 2;
            final int integer5 = this.getSeaLevel();
            final float float13 = this.biomeSource.getNoiseBiome(integer2, integer5, integer3).getDepth();
            for (int integer6 = -2; integer6 <= 2; ++integer6) {
                for (int integer7 = -2; integer7 <= 2; ++integer7) {
                    final Biome bss18 = this.biomeSource.getNoiseBiome(integer2 + integer6, integer5, integer3 + integer7);
                    final float float14 = bss18.getDepth();
                    final float float15 = bss18.getScale();
                    float float16;
                    float float17;
                    if (cho9.isAmplified() && float14 > 0.0f) {
                        float16 = 1.0f + float14 * 2.0f;
                        float17 = 1.0f + float15 * 4.0f;
                    }
                    else {
                        float16 = float14;
                        float17 = float15;
                    }
                    final float float18 = (float14 > float13) ? 0.5f : 1.0f;
                    final float float19 = float18 * NoiseBasedChunkGenerator.BIOME_WEIGHTS[integer6 + 2 + (integer7 + 2) * 5] / (float16 + 2.0f);
                    float10 += float17 * float19;
                    float11 += float16 * float19;
                    float12 += float19;
                }
            }
            final float float20 = float11 / float12;
            final float float21 = float10 / float12;
            final double double7 = float20 * 0.5f - 0.125f;
            final double double8 = float21 * 0.9f + 0.1f;
            double5 = double7 * 0.265625;
            double6 = 96.0 / double8;
        }
        final double double9 = 684.412 * cho9.noiseSamplingSettings().xzScale();
        final double double10 = 684.412 * cho9.noiseSamplingSettings().yScale();
        final double double11 = double9 / cho9.noiseSamplingSettings().xzFactor();
        final double double12 = double10 / cho9.noiseSamplingSettings().yFactor();
        final double double7 = cho9.topSlideSettings().target();
        final double double8 = cho9.topSlideSettings().size();
        final double double13 = cho9.topSlideSettings().offset();
        final double double14 = cho9.bottomSlideSettings().target();
        final double double15 = cho9.bottomSlideSettings().size();
        final double double16 = cho9.bottomSlideSettings().offset();
        final double double17 = cho9.randomDensityOffset() ? this.getRandomDensity(integer2, integer3) : 0.0;
        final double double18 = cho9.densityFactor();
        final double double19 = cho9.densityOffset();
        for (int integer8 = 0; integer8 <= this.chunkCountY; ++integer8) {
            double double20 = this.sampleAndClampNoise(integer2, integer8, integer3, double9, double10, double11, double12);
            final double double21 = 1.0 - integer8 * 2.0 / this.chunkCountY + double17;
            final double double22 = double21 * double18 + double19;
            final double double23 = (double22 + double5) * double6;
            if (double23 > 0.0) {
                double20 += double23 * 4.0;
            }
            else {
                double20 += double23;
            }
            if (double8 > 0.0) {
                final double double24 = (this.chunkCountY - integer8 - double13) / double8;
                double20 = Mth.clampedLerp(double7, double20, double24);
            }
            if (double15 > 0.0) {
                final double double24 = (integer8 - double16) / double15;
                double20 = Mth.clampedLerp(double14, double20, double24);
            }
            arr[integer8] = double20;
        }
    }
    
    private double getRandomDensity(final int integer1, final int integer2) {
        final double double4 = this.depthNoise.getValue(integer1 * 200, 10.0, integer2 * 200, 1.0, 0.0, true);
        double double5;
        if (double4 < 0.0) {
            double5 = -double4 * 0.3;
        }
        else {
            double5 = double4;
        }
        final double double6 = double5 * 24.575625 - 2.0;
        if (double6 < 0.0) {
            return double6 * 0.009486607142857142;
        }
        return Math.min(double6, 1.0) * 0.006640625;
    }
    
    @Override
    public int getBaseHeight(final int integer1, final int integer2, final Heightmap.Types a) {
        return this.iterateNoiseColumn(integer1, integer2, null, a.isOpaque());
    }
    
    @Override
    public BlockGetter getBaseColumn(final int integer1, final int integer2) {
        final BlockState[] arr4 = new BlockState[this.chunkCountY * this.chunkHeight];
        this.iterateNoiseColumn(integer1, integer2, arr4, null);
        return new NoiseColumn(arr4);
    }
    
    private int iterateNoiseColumn(final int integer1, final int integer2, @Nullable final BlockState[] arr, @Nullable final Predicate<BlockState> predicate) {
        final int integer3 = Math.floorDiv(integer1, this.chunkWidth);
        final int integer4 = Math.floorDiv(integer2, this.chunkWidth);
        final int integer5 = Math.floorMod(integer1, this.chunkWidth);
        final int integer6 = Math.floorMod(integer2, this.chunkWidth);
        final double double10 = integer5 / (double)this.chunkWidth;
        final double double11 = integer6 / (double)this.chunkWidth;
        final double[][] arr2 = { this.makeAndFillNoiseColumn(integer3, integer4), this.makeAndFillNoiseColumn(integer3, integer4 + 1), this.makeAndFillNoiseColumn(integer3 + 1, integer4), this.makeAndFillNoiseColumn(integer3 + 1, integer4 + 1) };
        for (int integer7 = this.chunkCountY - 1; integer7 >= 0; --integer7) {
            final double double12 = arr2[0][integer7];
            final double double13 = arr2[1][integer7];
            final double double14 = arr2[2][integer7];
            final double double15 = arr2[3][integer7];
            final double double16 = arr2[0][integer7 + 1];
            final double double17 = arr2[1][integer7 + 1];
            final double double18 = arr2[2][integer7 + 1];
            final double double19 = arr2[3][integer7 + 1];
            for (int integer8 = this.chunkHeight - 1; integer8 >= 0; --integer8) {
                final double double20 = integer8 / (double)this.chunkHeight;
                final double double21 = Mth.lerp3(double20, double10, double11, double12, double16, double14, double18, double13, double17, double15, double19);
                final int integer9 = integer7 * this.chunkHeight + integer8;
                final BlockState cee38 = this.generateBaseState(double21, integer9);
                if (arr != null) {
                    arr[integer9] = cee38;
                }
                if (predicate != null && predicate.test(cee38)) {
                    return integer9 + 1;
                }
            }
        }
        return 0;
    }
    
    protected BlockState generateBaseState(final double double1, final int integer) {
        BlockState cee5;
        if (double1 > 0.0) {
            cee5 = this.defaultBlock;
        }
        else if (integer < this.getSeaLevel()) {
            cee5 = this.defaultFluid;
        }
        else {
            cee5 = NoiseBasedChunkGenerator.AIR;
        }
        return cee5;
    }
    
    @Override
    public void buildSurfaceAndBedrock(final WorldGenRegion aam, final ChunkAccess cft) {
        final ChunkPos bra4 = cft.getPos();
        final int integer5 = bra4.x;
        final int integer6 = bra4.z;
        final WorldgenRandom chu7 = new WorldgenRandom();
        chu7.setBaseChunkSeed(integer5, integer6);
        final ChunkPos bra5 = cft.getPos();
        final int integer7 = bra5.getMinBlockX();
        final int integer8 = bra5.getMinBlockZ();
        final double double11 = 0.0625;
        final BlockPos.MutableBlockPos a13 = new BlockPos.MutableBlockPos();
        for (int integer9 = 0; integer9 < 16; ++integer9) {
            for (int integer10 = 0; integer10 < 16; ++integer10) {
                final int integer11 = integer7 + integer9;
                final int integer12 = integer8 + integer10;
                final int integer13 = cft.getHeight(Heightmap.Types.WORLD_SURFACE_WG, integer9, integer10) + 1;
                final double double12 = this.surfaceNoise.getSurfaceNoiseValue(integer11 * 0.0625, integer12 * 0.0625, 0.0625, integer9 * 0.0625) * 15.0;
                aam.getBiome(a13.set(integer7 + integer9, integer13, integer8 + integer10)).buildSurfaceAt(chu7, cft, integer11, integer12, integer13, double12, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), aam.getSeed());
            }
        }
        this.setBedrock(cft, chu7);
    }
    
    private void setBedrock(final ChunkAccess cft, final Random random) {
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        final int integer5 = cft.getPos().getMinBlockX();
        final int integer6 = cft.getPos().getMinBlockZ();
        final NoiseGeneratorSettings chm7 = (NoiseGeneratorSettings)this.settings.get();
        final int integer7 = chm7.getBedrockFloorPosition();
        final int integer8 = this.height - 1 - chm7.getBedrockRoofPosition();
        final int integer9 = 5;
        final boolean boolean11 = integer8 + 4 >= 0 && integer8 < this.height;
        final boolean boolean12 = integer7 + 4 >= 0 && integer7 < this.height;
        if (!boolean11 && !boolean12) {
            return;
        }
        for (final BlockPos fx14 : BlockPos.betweenClosed(integer5, 0, integer6, integer5 + 15, 0, integer6 + 15)) {
            if (boolean11) {
                for (int integer10 = 0; integer10 < 5; ++integer10) {
                    if (integer10 <= random.nextInt(5)) {
                        cft.setBlockState(a4.set(fx14.getX(), integer8 - integer10, fx14.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
                    }
                }
            }
            if (boolean12) {
                for (int integer10 = 4; integer10 >= 0; --integer10) {
                    if (integer10 <= random.nextInt(5)) {
                        cft.setBlockState(a4.set(fx14.getX(), integer7 + integer10, fx14.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
                    }
                }
            }
        }
    }
    
    @Override
    public void fillFromNoise(final LevelAccessor brv, final StructureFeatureManager bsk, final ChunkAccess cft) {
        final ObjectList<StructurePiece> objectList5 = (ObjectList<StructurePiece>)new ObjectArrayList(10);
        final ObjectList<JigsawJunction> objectList6 = (ObjectList<JigsawJunction>)new ObjectArrayList(32);
        final ChunkPos bra7 = cft.getPos();
        final int integer8 = bra7.x;
        final int integer9 = bra7.z;
        final int integer10 = integer8 << 4;
        final int integer11 = integer9 << 4;
        for (final StructureFeature<?> ckx13 : StructureFeature.NOISE_AFFECTING_FEATURES) {
            bsk.startsForFeature(SectionPos.of(bra7, 0), ckx13).forEach(crs -> {
                for (final StructurePiece crr8 : crs.getPieces()) {
                    if (!crr8.isCloseToChunk(bra7, 12)) {
                        continue;
                    }
                    if (crr8 instanceof PoolElementStructurePiece) {
                        final PoolElementStructurePiece crl9 = (PoolElementStructurePiece)crr8;
                        final StructureTemplatePool.Projection a10 = crl9.getElement().getProjection();
                        if (a10 == StructureTemplatePool.Projection.RIGID) {
                            objectList5.add(crl9);
                        }
                        for (final JigsawJunction coa12 : crl9.getJunctions()) {
                            final int integer5 = coa12.getSourceX();
                            final int integer6 = coa12.getSourceZ();
                            if (integer5 > integer10 - 12 && integer6 > integer11 - 12 && integer5 < integer10 + 15 + 12) {
                                if (integer6 >= integer11 + 15 + 12) {
                                    continue;
                                }
                                objectList6.add(coa12);
                            }
                        }
                    }
                    else {
                        objectList5.add(crr8);
                    }
                }
            });
        }
        final double[][][] arr12 = new double[2][this.chunkCountZ + 1][this.chunkCountY + 1];
        for (int integer12 = 0; integer12 < this.chunkCountZ + 1; ++integer12) {
            this.fillNoiseColumn(arr12[0][integer12] = new double[this.chunkCountY + 1], integer8 * this.chunkCountX, integer9 * this.chunkCountZ + integer12);
            arr12[1][integer12] = new double[this.chunkCountY + 1];
        }
        final ProtoChunk cgm13 = (ProtoChunk)cft;
        final Heightmap chk14 = cgm13.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        final Heightmap chk15 = cgm13.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        final BlockPos.MutableBlockPos a16 = new BlockPos.MutableBlockPos();
        final ObjectListIterator<StructurePiece> objectListIterator17 = (ObjectListIterator<StructurePiece>)objectList5.iterator();
        final ObjectListIterator<JigsawJunction> objectListIterator18 = (ObjectListIterator<JigsawJunction>)objectList6.iterator();
        for (int integer13 = 0; integer13 < this.chunkCountX; ++integer13) {
            for (int integer14 = 0; integer14 < this.chunkCountZ + 1; ++integer14) {
                this.fillNoiseColumn(arr12[1][integer14], integer8 * this.chunkCountX + integer13 + 1, integer9 * this.chunkCountZ + integer14);
            }
            for (int integer14 = 0; integer14 < this.chunkCountZ; ++integer14) {
                LevelChunkSection cgf21 = cgm13.getOrCreateSection(15);
                cgf21.acquire();
                for (int integer15 = this.chunkCountY - 1; integer15 >= 0; --integer15) {
                    final double double23 = arr12[0][integer14][integer15];
                    final double double24 = arr12[0][integer14 + 1][integer15];
                    final double double25 = arr12[1][integer14][integer15];
                    final double double26 = arr12[1][integer14 + 1][integer15];
                    final double double27 = arr12[0][integer14][integer15 + 1];
                    final double double28 = arr12[0][integer14 + 1][integer15 + 1];
                    final double double29 = arr12[1][integer14][integer15 + 1];
                    final double double30 = arr12[1][integer14 + 1][integer15 + 1];
                    for (int integer16 = this.chunkHeight - 1; integer16 >= 0; --integer16) {
                        final int integer17 = integer15 * this.chunkHeight + integer16;
                        final int integer18 = integer17 & 0xF;
                        final int integer19 = integer17 >> 4;
                        if (cgf21.bottomBlockY() >> 4 != integer19) {
                            cgf21.release();
                            cgf21 = cgm13.getOrCreateSection(integer19);
                            cgf21.acquire();
                        }
                        final double double31 = integer16 / (double)this.chunkHeight;
                        final double double32 = Mth.lerp(double31, double23, double27);
                        final double double33 = Mth.lerp(double31, double25, double29);
                        final double double34 = Mth.lerp(double31, double24, double28);
                        final double double35 = Mth.lerp(double31, double26, double30);
                        for (int integer20 = 0; integer20 < this.chunkWidth; ++integer20) {
                            final int integer21 = integer10 + integer13 * this.chunkWidth + integer20;
                            final int integer22 = integer21 & 0xF;
                            final double double36 = integer20 / (double)this.chunkWidth;
                            final double double37 = Mth.lerp(double36, double32, double33);
                            final double double38 = Mth.lerp(double36, double34, double35);
                            for (int integer23 = 0; integer23 < this.chunkWidth; ++integer23) {
                                final int integer24 = integer11 + integer14 * this.chunkWidth + integer23;
                                final int integer25 = integer24 & 0xF;
                                final double double39 = integer23 / (double)this.chunkWidth;
                                final double double40 = Mth.lerp(double39, double37, double38);
                                double double41 = Mth.clamp(double40 / 200.0, -1.0, 1.0);
                                double41 = double41 / 2.0 - double41 * double41 * double41 / 24.0;
                                while (objectListIterator17.hasNext()) {
                                    final StructurePiece crr71 = (StructurePiece)objectListIterator17.next();
                                    final BoundingBox cqx72 = crr71.getBoundingBox();
                                    final int integer26 = Math.max(0, Math.max(cqx72.x0 - integer21, integer21 - cqx72.x1));
                                    final int integer27 = integer17 - (cqx72.y0 + ((crr71 instanceof PoolElementStructurePiece) ? ((PoolElementStructurePiece)crr71).getGroundLevelDelta() : 0));
                                    final int integer28 = Math.max(0, Math.max(cqx72.z0 - integer24, integer24 - cqx72.z1));
                                    double41 += getContribution(integer26, integer27, integer28) * 0.8;
                                }
                                objectListIterator17.back(objectList5.size());
                                while (objectListIterator18.hasNext()) {
                                    final JigsawJunction coa71 = (JigsawJunction)objectListIterator18.next();
                                    final int integer29 = integer21 - coa71.getSourceX();
                                    final int integer26 = integer17 - coa71.getSourceGroundY();
                                    final int integer27 = integer24 - coa71.getSourceZ();
                                    double41 += getContribution(integer29, integer26, integer27) * 0.4;
                                }
                                objectListIterator18.back(objectList6.size());
                                final BlockState cee71 = this.generateBaseState(double41, integer17);
                                if (cee71 != NoiseBasedChunkGenerator.AIR) {
                                    if (cee71.getLightEmission() != 0) {
                                        a16.set(integer21, integer17, integer24);
                                        cgm13.addLight(a16);
                                    }
                                    cgf21.setBlockState(integer22, integer18, integer25, cee71, false);
                                    chk14.update(integer22, integer17, integer25, cee71);
                                    chk15.update(integer22, integer17, integer25, cee71);
                                }
                            }
                        }
                    }
                }
                cgf21.release();
            }
            final double[][] arr13 = arr12[0];
            arr12[0] = arr12[1];
            arr12[1] = arr13;
        }
    }
    
    private static double getContribution(final int integer1, final int integer2, final int integer3) {
        final int integer4 = integer1 + 12;
        final int integer5 = integer2 + 12;
        final int integer6 = integer3 + 12;
        if (integer4 < 0 || integer4 >= 24) {
            return 0.0;
        }
        if (integer5 < 0 || integer5 >= 24) {
            return 0.0;
        }
        if (integer6 < 0 || integer6 >= 24) {
            return 0.0;
        }
        return NoiseBasedChunkGenerator.BEARD_KERNEL[integer6 * 24 * 24 + integer4 * 24 + integer5];
    }
    
    private static double computeContribution(final int integer1, final int integer2, final int integer3) {
        final double double4 = integer1 * integer1 + integer3 * integer3;
        final double double5 = integer2 + 0.5;
        final double double6 = double5 * double5;
        final double double7 = Math.pow(2.718281828459045, -(double6 / 16.0 + double4 / 16.0));
        final double double8 = -double5 * Mth.fastInvSqrt(double6 / 2.0 + double4 / 2.0) / 2.0;
        return double8 * double7;
    }
    
    @Override
    public int getGenDepth() {
        return this.height;
    }
    
    @Override
    public int getSeaLevel() {
        return ((NoiseGeneratorSettings)this.settings.get()).seaLevel();
    }
    
    @Override
    public List<MobSpawnSettings.SpawnerData> getMobsAt(final Biome bss, final StructureFeatureManager bsk, final MobCategory aql, final BlockPos fx) {
        if (bsk.getStructureAt(fx, true, StructureFeature.SWAMP_HUT).isValid()) {
            if (aql == MobCategory.MONSTER) {
                return StructureFeature.SWAMP_HUT.getSpecialEnemies();
            }
            if (aql == MobCategory.CREATURE) {
                return StructureFeature.SWAMP_HUT.getSpecialAnimals();
            }
        }
        if (aql == MobCategory.MONSTER) {
            if (bsk.getStructureAt(fx, false, StructureFeature.PILLAGER_OUTPOST).isValid()) {
                return StructureFeature.PILLAGER_OUTPOST.getSpecialEnemies();
            }
            if (bsk.getStructureAt(fx, false, StructureFeature.OCEAN_MONUMENT).isValid()) {
                return StructureFeature.OCEAN_MONUMENT.getSpecialEnemies();
            }
            if (bsk.getStructureAt(fx, true, StructureFeature.NETHER_BRIDGE).isValid()) {
                return StructureFeature.NETHER_BRIDGE.getSpecialEnemies();
            }
        }
        return super.getMobsAt(bss, bsk, aql, fx);
    }
    
    @Override
    public void spawnOriginalMobs(final WorldGenRegion aam) {
        if (((NoiseGeneratorSettings)this.settings.get()).disableMobGeneration()) {
            return;
        }
        final int integer3 = aam.getCenterX();
        final int integer4 = aam.getCenterZ();
        final Biome bss5 = aam.getBiome(new ChunkPos(integer3, integer4).getWorldPosition());
        final WorldgenRandom chu6 = new WorldgenRandom();
        chu6.setDecorationSeed(aam.getSeed(), integer3 << 4, integer4 << 4);
        NaturalSpawner.spawnMobsForChunkGeneration(aam, bss5, integer3, integer4, chu6);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BiomeSource.CODEC.fieldOf("biome_source").forGetter(chl -> chl.biomeSource), (App)Codec.LONG.fieldOf("seed").stable().forGetter(chl -> chl.seed), (App)NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(chl -> chl.settings)).apply((Applicative)instance, instance.stable(NoiseBasedChunkGenerator::new)));
        BEARD_KERNEL = Util.<float[]>make(new float[13824], (java.util.function.Consumer<float[]>)(arr -> {
            for (int integer2 = 0; integer2 < 24; ++integer2) {
                for (int integer3 = 0; integer3 < 24; ++integer3) {
                    for (int integer4 = 0; integer4 < 24; ++integer4) {
                        arr[integer2 * 24 * 24 + integer3 * 24 + integer4] = (float)computeContribution(integer3 - 12, integer4 - 12, integer2 - 12);
                    }
                }
            }
        }));
        BIOME_WEIGHTS = Util.<float[]>make(new float[25], (java.util.function.Consumer<float[]>)(arr -> {
            for (int integer2 = -2; integer2 <= 2; ++integer2) {
                for (int integer3 = -2; integer3 <= 2; ++integer3) {
                    final float float4 = 10.0f / Mth.sqrt(integer2 * integer2 + integer3 * integer3 + 0.2f);
                    arr[integer2 + 2 + (integer3 + 2) * 5] = float4;
                }
            }
        }));
        AIR = Blocks.AIR.defaultBlockState();
    }
}
