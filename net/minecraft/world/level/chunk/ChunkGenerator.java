package net.minecraft.world.level.chunk;

import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.server.level.WorldGenRegion;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import java.util.ListIterator;
import java.util.BitSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
import java.util.function.Predicate;
import java.util.Random;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.biome.Biome;
import com.google.common.collect.Lists;
import net.minecraft.world.level.ChunkPos;
import java.util.List;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.biome.BiomeSource;
import com.mojang.serialization.Codec;

public abstract class ChunkGenerator {
    public static final Codec<ChunkGenerator> CODEC;
    protected final BiomeSource biomeSource;
    protected final BiomeSource runtimeBiomeSource;
    private final StructureSettings settings;
    private final long strongholdSeed;
    private final List<ChunkPos> strongholdPositions;
    
    public ChunkGenerator(final BiomeSource bsv, final StructureSettings chs) {
        this(bsv, bsv, chs, 0L);
    }
    
    public ChunkGenerator(final BiomeSource bsv1, final BiomeSource bsv2, final StructureSettings chs, final long long4) {
        this.strongholdPositions = (List<ChunkPos>)Lists.newArrayList();
        this.biomeSource = bsv1;
        this.runtimeBiomeSource = bsv2;
        this.settings = chs;
        this.strongholdSeed = long4;
    }
    
    private void generateStrongholds() {
        if (!this.strongholdPositions.isEmpty()) {
            return;
        }
        final StrongholdConfiguration cmu2 = this.settings.stronghold();
        if (cmu2 == null || cmu2.count() == 0) {
            return;
        }
        final List<Biome> list3 = (List<Biome>)Lists.newArrayList();
        for (final Biome bss5 : this.biomeSource.possibleBiomes()) {
            if (bss5.getGenerationSettings().isValidStart(StructureFeature.STRONGHOLD)) {
                list3.add(bss5);
            }
        }
        final int integer4 = cmu2.distance();
        final int integer5 = cmu2.count();
        int integer6 = cmu2.spread();
        final Random random7 = new Random();
        random7.setSeed(this.strongholdSeed);
        double double8 = random7.nextDouble() * 3.141592653589793 * 2.0;
        int integer7 = 0;
        int integer8 = 0;
        for (int integer9 = 0; integer9 < integer5; ++integer9) {
            final double double9 = 4 * integer4 + integer4 * integer8 * 6 + (random7.nextDouble() - 0.5) * (integer4 * 2.5);
            int integer10 = (int)Math.round(Math.cos(double8) * double9);
            int integer11 = (int)Math.round(Math.sin(double8) * double9);
            final BlockPos fx17 = this.biomeSource.findBiomeHorizontal((integer10 << 4) + 8, 0, (integer11 << 4) + 8, 112, (Predicate<Biome>)list3::contains, random7);
            if (fx17 != null) {
                integer10 = fx17.getX() >> 4;
                integer11 = fx17.getZ() >> 4;
            }
            this.strongholdPositions.add(new ChunkPos(integer10, integer11));
            double8 += 6.283185307179586 / integer6;
            if (++integer7 == integer6) {
                ++integer8;
                integer7 = 0;
                integer6 += 2 * integer6 / (integer8 + 1);
                integer6 = Math.min(integer6, integer5 - integer9);
                double8 += random7.nextDouble() * 3.141592653589793 * 2.0;
            }
        }
    }
    
    protected abstract Codec<? extends ChunkGenerator> codec();
    
    public abstract ChunkGenerator withSeed(final long long1);
    
    public void createBiomes(final Registry<Biome> gm, final ChunkAccess cft) {
        final ChunkPos bra4 = cft.getPos();
        ((ProtoChunk)cft).setBiomes(new ChunkBiomeContainer(gm, bra4, this.runtimeBiomeSource));
    }
    
    public void applyCarvers(final long long1, final BiomeManager bsu, final ChunkAccess cft, final GenerationStep.Carving a) {
        final BiomeManager bsu2 = bsu.withDifferentSource(this.biomeSource);
        final WorldgenRandom chu8 = new WorldgenRandom();
        final int integer9 = 8;
        final ChunkPos bra10 = cft.getPos();
        final int integer10 = bra10.x;
        final int integer11 = bra10.z;
        final BiomeGenerationSettings bst13 = this.biomeSource.getNoiseBiome(bra10.x << 2, 0, bra10.z << 2).getGenerationSettings();
        final BitSet bitSet14 = ((ProtoChunk)cft).getOrCreateCarvingMask(a);
        for (int integer12 = integer10 - 8; integer12 <= integer10 + 8; ++integer12) {
            for (int integer13 = integer11 - 8; integer13 <= integer11 + 8; ++integer13) {
                final List<Supplier<ConfiguredWorldCarver<?>>> list17 = bst13.getCarvers(a);
                final ListIterator<Supplier<ConfiguredWorldCarver<?>>> listIterator18 = (ListIterator<Supplier<ConfiguredWorldCarver<?>>>)list17.listIterator();
                while (listIterator18.hasNext()) {
                    final int integer14 = listIterator18.nextIndex();
                    final ConfiguredWorldCarver<?> chy20 = ((Supplier)listIterator18.next()).get();
                    chu8.setLargeFeatureSeed(long1 + integer14, integer12, integer13);
                    if (chy20.isStartChunk(chu8, integer12, integer13)) {
                        chy20.carve(cft, (Function<BlockPos, Biome>)bsu2::getBiome, chu8, this.getSeaLevel(), integer12, integer13, integer10, integer11, bitSet14);
                    }
                }
            }
        }
    }
    
    @Nullable
    public BlockPos findNearestMapFeature(final ServerLevel aag, final StructureFeature<?> ckx, final BlockPos fx, final int integer, final boolean boolean5) {
        if (!this.biomeSource.canGenerateStructure(ckx)) {
            return null;
        }
        if (ckx == StructureFeature.STRONGHOLD) {
            this.generateStrongholds();
            BlockPos fx2 = null;
            double double8 = Double.MAX_VALUE;
            final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
            for (final ChunkPos bra12 : this.strongholdPositions) {
                a10.set((bra12.x << 4) + 8, 32, (bra12.z << 4) + 8);
                final double double9 = a10.distSqr(fx);
                if (fx2 == null) {
                    fx2 = new BlockPos(a10);
                    double8 = double9;
                }
                else {
                    if (double9 >= double8) {
                        continue;
                    }
                    fx2 = new BlockPos(a10);
                    double8 = double9;
                }
            }
            return fx2;
        }
        final StructureFeatureConfiguration cmv7 = this.settings.getConfig(ckx);
        if (cmv7 == null) {
            return null;
        }
        return ckx.getNearestGeneratedFeature(aag, aag.structureFeatureManager(), fx, integer, boolean5, aag.getSeed(), cmv7);
    }
    
    public void applyBiomeDecoration(final WorldGenRegion aam, final StructureFeatureManager bsk) {
        final int integer4 = aam.getCenterX();
        final int integer5 = aam.getCenterZ();
        final int integer6 = integer4 * 16;
        final int integer7 = integer5 * 16;
        final BlockPos fx8 = new BlockPos(integer6, 0, integer7);
        final Biome bss9 = this.biomeSource.getNoiseBiome((integer4 << 2) + 2, 2, (integer5 << 2) + 2);
        final WorldgenRandom chu10 = new WorldgenRandom();
        final long long11 = chu10.setDecorationSeed(aam.getSeed(), integer6, integer7);
        try {
            bss9.generate(bsk, this, aam, long11, chu10, fx8);
        }
        catch (Exception exception13) {
            final CrashReport l14 = CrashReport.forThrowable((Throwable)exception13, "Biome decoration");
            l14.addCategory("Generation").setDetail("CenterX", integer4).setDetail("CenterZ", integer5).setDetail("Seed", long11).setDetail("Biome", bss9);
            throw new ReportedException(l14);
        }
    }
    
    public abstract void buildSurfaceAndBedrock(final WorldGenRegion aam, final ChunkAccess cft);
    
    public void spawnOriginalMobs(final WorldGenRegion aam) {
    }
    
    public StructureSettings getSettings() {
        return this.settings;
    }
    
    public int getSpawnHeight() {
        return 64;
    }
    
    public BiomeSource getBiomeSource() {
        return this.runtimeBiomeSource;
    }
    
    public int getGenDepth() {
        return 256;
    }
    
    public List<MobSpawnSettings.SpawnerData> getMobsAt(final Biome bss, final StructureFeatureManager bsk, final MobCategory aql, final BlockPos fx) {
        return bss.getMobSettings().getMobs(aql);
    }
    
    public void createStructures(final RegistryAccess gn, final StructureFeatureManager bsk, final ChunkAccess cft, final StructureManager cst, final long long5) {
        final ChunkPos bra8 = cft.getPos();
        final Biome bss9 = this.biomeSource.getNoiseBiome((bra8.x << 2) + 2, 0, (bra8.z << 2) + 2);
        this.createStructure(StructureFeatures.STRONGHOLD, gn, bsk, cft, cst, long5, bra8, bss9);
        for (final Supplier<ConfiguredStructureFeature<?, ?>> supplier11 : bss9.getGenerationSettings().structures()) {
            this.createStructure(supplier11.get(), gn, bsk, cft, cst, long5, bra8, bss9);
        }
    }
    
    private void createStructure(final ConfiguredStructureFeature<?, ?> cit, final RegistryAccess gn, final StructureFeatureManager bsk, final ChunkAccess cft, final StructureManager cst, final long long6, final ChunkPos bra, final Biome bss) {
        final StructureStart<?> crs11 = bsk.getStartForFeature(SectionPos.of(cft.getPos(), 0), cit.feature, cft);
        final int integer12 = (crs11 != null) ? crs11.getReferences() : 0;
        final StructureFeatureConfiguration cmv13 = this.settings.getConfig(cit.feature);
        if (cmv13 != null) {
            final StructureStart<?> crs12 = cit.generate(gn, this, this.biomeSource, cst, long6, bra, bss, integer12, cmv13);
            bsk.setStartForFeature(SectionPos.of(cft.getPos(), 0), cit.feature, crs12, cft);
        }
    }
    
    public void createReferences(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkAccess cft) {
        final int integer5 = 8;
        final int integer6 = cft.getPos().x;
        final int integer7 = cft.getPos().z;
        final int integer8 = integer6 << 4;
        final int integer9 = integer7 << 4;
        final SectionPos gp10 = SectionPos.of(cft.getPos(), 0);
        for (int integer10 = integer6 - 8; integer10 <= integer6 + 8; ++integer10) {
            for (int integer11 = integer7 - 8; integer11 <= integer7 + 8; ++integer11) {
                final long long13 = ChunkPos.asLong(integer10, integer11);
                for (final StructureStart<?> crs16 : bso.getChunk(integer10, integer11).getAllStarts().values()) {
                    try {
                        if (crs16 == StructureStart.INVALID_START || !crs16.getBoundingBox().intersects(integer8, integer9, integer8 + 15, integer9 + 15)) {
                            continue;
                        }
                        bsk.addReferenceForFeature(gp10, crs16.getFeature(), long13, cft);
                        DebugPackets.sendStructurePacket(bso, crs16);
                    }
                    catch (Exception exception17) {
                        final CrashReport l18 = CrashReport.forThrowable((Throwable)exception17, "Generating structure reference");
                        final CrashReportCategory m19 = l18.addCategory("Structure");
                        m19.setDetail("Id", (CrashReportDetail<String>)(() -> Registry.STRUCTURE_FEATURE.getKey(crs16.getFeature()).toString()));
                        m19.setDetail("Name", (CrashReportDetail<String>)(() -> crs16.getFeature().getFeatureName()));
                        m19.setDetail("Class", (CrashReportDetail<String>)(() -> crs16.getFeature().getClass().getCanonicalName()));
                        throw new ReportedException(l18);
                    }
                }
            }
        }
    }
    
    public abstract void fillFromNoise(final LevelAccessor brv, final StructureFeatureManager bsk, final ChunkAccess cft);
    
    public int getSeaLevel() {
        return 63;
    }
    
    public abstract int getBaseHeight(final int integer1, final int integer2, final Heightmap.Types a);
    
    public abstract BlockGetter getBaseColumn(final int integer1, final int integer2);
    
    public int getFirstFreeHeight(final int integer1, final int integer2, final Heightmap.Types a) {
        return this.getBaseHeight(integer1, integer2, a);
    }
    
    public int getFirstOccupiedHeight(final int integer1, final int integer2, final Heightmap.Types a) {
        return this.getBaseHeight(integer1, integer2, a) - 1;
    }
    
    public boolean hasStronghold(final ChunkPos bra) {
        this.generateStrongholds();
        return this.strongholdPositions.contains(bra);
    }
    
    static {
        Registry.<Codec<NoiseBasedChunkGenerator>>register(Registry.CHUNK_GENERATOR, "noise", NoiseBasedChunkGenerator.CODEC);
        Registry.<Codec<FlatLevelSource>>register(Registry.CHUNK_GENERATOR, "flat", FlatLevelSource.CODEC);
        Registry.<Codec<DebugLevelSource>>register(Registry.CHUNK_GENERATOR, "debug", DebugLevelSource.CODEC);
        CODEC = Registry.CHUNK_GENERATOR.dispatchStable(ChunkGenerator::codec, Function.identity());
    }
}
