package net.minecraft.world.level.chunk;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.GenerationStep;
import java.util.Set;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.server.level.WorldGenRegion;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.server.level.ServerLevel;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import javax.annotation.Nullable;
import net.minecraft.server.level.ChunkHolder;
import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.EnumSet;

public class ChunkStatus {
    private static final EnumSet<Heightmap.Types> PRE_FEATURES;
    private static final EnumSet<Heightmap.Types> POST_FEATURES;
    private static final LoadingTask PASSTHROUGH_LOAD_TASK;
    public static final ChunkStatus EMPTY;
    public static final ChunkStatus STRUCTURE_STARTS;
    public static final ChunkStatus STRUCTURE_REFERENCES;
    public static final ChunkStatus BIOMES;
    public static final ChunkStatus NOISE;
    public static final ChunkStatus SURFACE;
    public static final ChunkStatus CARVERS;
    public static final ChunkStatus LIQUID_CARVERS;
    public static final ChunkStatus FEATURES;
    public static final ChunkStatus LIGHT;
    public static final ChunkStatus SPAWN;
    public static final ChunkStatus HEIGHTMAPS;
    public static final ChunkStatus FULL;
    private static final List<ChunkStatus> STATUS_BY_RANGE;
    private static final IntList RANGE_BY_STATUS;
    private final String name;
    private final int index;
    private final ChunkStatus parent;
    private final GenerationTask generationTask;
    private final LoadingTask loadingTask;
    private final int range;
    private final ChunkType chunkType;
    private final EnumSet<Heightmap.Types> heightmapsAfter;
    
    private static CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> lightChunk(final ChunkStatus cfx, final ThreadedLevelLightEngine aaj, final ChunkAccess cft) {
        final boolean boolean4 = isLighted(cfx, cft);
        if (!cft.getStatus().isOrAfter(cfx)) {
            ((ProtoChunk)cft).setStatus(cfx);
        }
        return (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)aaj.lightChunk(cft, boolean4).thenApply(Either::left);
    }
    
    private static ChunkStatus registerSimple(final String string, @Nullable final ChunkStatus cfx, final int integer, final EnumSet<Heightmap.Types> enumSet, final ChunkType a, final SimpleGenerationTask d) {
        return register(string, cfx, integer, enumSet, a, d);
    }
    
    private static ChunkStatus register(final String string, @Nullable final ChunkStatus cfx, final int integer, final EnumSet<Heightmap.Types> enumSet, final ChunkType a, final GenerationTask b) {
        return register(string, cfx, integer, enumSet, a, b, ChunkStatus.PASSTHROUGH_LOAD_TASK);
    }
    
    private static ChunkStatus register(final String string, @Nullable final ChunkStatus cfx, final int integer, final EnumSet<Heightmap.Types> enumSet, final ChunkType a, final GenerationTask b, final LoadingTask c) {
        return Registry.<ChunkStatus>register(Registry.CHUNK_STATUS, string, new ChunkStatus(string, cfx, integer, enumSet, a, b, c));
    }
    
    public static List<ChunkStatus> getStatusList() {
        final List<ChunkStatus> list1 = (List<ChunkStatus>)Lists.newArrayList();
        ChunkStatus cfx2;
        for (cfx2 = ChunkStatus.FULL; cfx2.getParent() != cfx2; cfx2 = cfx2.getParent()) {
            list1.add(cfx2);
        }
        list1.add(cfx2);
        Collections.reverse((List)list1);
        return list1;
    }
    
    private static boolean isLighted(final ChunkStatus cfx, final ChunkAccess cft) {
        return cft.getStatus().isOrAfter(cfx) && cft.isLightCorrect();
    }
    
    public static ChunkStatus getStatus(final int integer) {
        if (integer >= ChunkStatus.STATUS_BY_RANGE.size()) {
            return ChunkStatus.EMPTY;
        }
        if (integer < 0) {
            return ChunkStatus.FULL;
        }
        return (ChunkStatus)ChunkStatus.STATUS_BY_RANGE.get(integer);
    }
    
    public static int maxDistance() {
        return ChunkStatus.STATUS_BY_RANGE.size();
    }
    
    public static int getDistance(final ChunkStatus cfx) {
        return ChunkStatus.RANGE_BY_STATUS.getInt(cfx.getIndex());
    }
    
    ChunkStatus(final String string, @Nullable final ChunkStatus cfx, final int integer, final EnumSet<Heightmap.Types> enumSet, final ChunkType a, final GenerationTask b, final LoadingTask c) {
        this.name = string;
        this.parent = ((cfx == null) ? this : cfx);
        this.generationTask = b;
        this.loadingTask = c;
        this.range = integer;
        this.chunkType = a;
        this.heightmapsAfter = enumSet;
        this.index = ((cfx == null) ? 0 : (cfx.getIndex() + 1));
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ChunkStatus getParent() {
        return this.parent;
    }
    
    public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> generate(final ServerLevel aag, final ChunkGenerator cfv, final StructureManager cst, final ThreadedLevelLightEngine aaj, final Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> function, final List<ChunkAccess> list) {
        return this.generationTask.doWork(this, aag, cfv, cst, aaj, function, list, (ChunkAccess)list.get(list.size() / 2));
    }
    
    public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> load(final ServerLevel aag, final StructureManager cst, final ThreadedLevelLightEngine aaj, final Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> function, final ChunkAccess cft) {
        return this.loadingTask.doWork(this, aag, cst, aaj, function, cft);
    }
    
    public int getRange() {
        return this.range;
    }
    
    public ChunkType getChunkType() {
        return this.chunkType;
    }
    
    public static ChunkStatus byName(final String string) {
        return Registry.CHUNK_STATUS.get(ResourceLocation.tryParse(string));
    }
    
    public EnumSet<Heightmap.Types> heightmapsAfter() {
        return this.heightmapsAfter;
    }
    
    public boolean isOrAfter(final ChunkStatus cfx) {
        return this.getIndex() >= cfx.getIndex();
    }
    
    public String toString() {
        return Registry.CHUNK_STATUS.getKey(this).toString();
    }
    
    static {
        PRE_FEATURES = EnumSet.of((Enum)Heightmap.Types.OCEAN_FLOOR_WG, (Enum)Heightmap.Types.WORLD_SURFACE_WG);
        POST_FEATURES = EnumSet.of((Enum)Heightmap.Types.OCEAN_FLOOR, (Enum)Heightmap.Types.WORLD_SURFACE, (Enum)Heightmap.Types.MOTION_BLOCKING, (Enum)Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
        PASSTHROUGH_LOAD_TASK = ((cfx, aag, cst, aaj, function, cft) -> {
            if (cft instanceof ProtoChunk && !cft.getStatus().isOrAfter(cfx)) {
                cft.setStatus(cfx);
            }
            return (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)CompletableFuture.completedFuture(Either.left((Object)cft));
        });
        EMPTY = registerSimple("empty", (ChunkStatus)null, -1, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> {});
        STRUCTURE_STARTS = register("structure_starts", ChunkStatus.EMPTY, 0, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (cfx, aag, cfv, cst, aaj, function, list, cft) -> {
            if (!cft.getStatus().isOrAfter(cfx)) {
                if (aag.getServer().getWorldData().worldGenSettings().generateFeatures()) {
                    cfv.createStructures(aag.registryAccess(), aag.structureFeatureManager(), cft, cst, aag.getSeed());
                }
                if (cft instanceof ProtoChunk) {
                    cft.setStatus(cfx);
                }
            }
            return (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)CompletableFuture.completedFuture(Either.left((Object)cft));
        });
        final WorldGenRegion aam5;
        STRUCTURE_REFERENCES = registerSimple("structure_references", ChunkStatus.STRUCTURE_STARTS, 8, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> {
            aam5 = new WorldGenRegion(aag, list);
            cfv.createReferences(aam5, aag.structureFeatureManager().forWorldGenRegion(aam5), cft);
            return;
        });
        BIOMES = registerSimple("biomes", ChunkStatus.STRUCTURE_REFERENCES, 0, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> cfv.createBiomes(aag.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), cft));
        final WorldGenRegion aam6;
        NOISE = registerSimple("noise", ChunkStatus.BIOMES, 8, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> {
            aam6 = new WorldGenRegion(aag, list);
            cfv.fillFromNoise(aam6, aag.structureFeatureManager().forWorldGenRegion(aam6), cft);
            return;
        });
        SURFACE = registerSimple("surface", ChunkStatus.NOISE, 0, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> cfv.buildSurfaceAndBedrock(new WorldGenRegion(aag, list), cft));
        CARVERS = registerSimple("carvers", ChunkStatus.SURFACE, 0, ChunkStatus.PRE_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> cfv.applyCarvers(aag.getSeed(), aag.getBiomeManager(), cft, GenerationStep.Carving.AIR));
        LIQUID_CARVERS = registerSimple("liquid_carvers", ChunkStatus.CARVERS, 0, ChunkStatus.POST_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> cfv.applyCarvers(aag.getSeed(), aag.getBiomeManager(), cft, GenerationStep.Carving.LIQUID));
        final ProtoChunk cgm9;
        WorldGenRegion aam7;
        FEATURES = register("features", ChunkStatus.LIQUID_CARVERS, 8, ChunkStatus.POST_FEATURES, ChunkType.PROTOCHUNK, (cfx, aag, cfv, cst, aaj, function, list, cft) -> {
            cgm9 = cft;
            cgm9.setLightEngine(aaj);
            if (!cft.getStatus().isOrAfter(cfx)) {
                Heightmap.primeHeightmaps(cft, (Set<Heightmap.Types>)EnumSet.of((Enum)Heightmap.Types.MOTION_BLOCKING, (Enum)Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (Enum)Heightmap.Types.OCEAN_FLOOR, (Enum)Heightmap.Types.WORLD_SURFACE));
                aam7 = new WorldGenRegion(aag, list);
                cfv.applyBiomeDecoration(aam7, aag.structureFeatureManager().forWorldGenRegion(aam7));
                cgm9.setStatus(cfx);
            }
            return (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)CompletableFuture.completedFuture(Either.left((Object)cft));
        });
        LIGHT = register("light", ChunkStatus.FEATURES, 1, ChunkStatus.POST_FEATURES, ChunkType.PROTOCHUNK, (cfx, aag, cfv, cst, aaj, function, list, cft) -> lightChunk(cfx, aaj, cft), (cfx, aag, cst, aaj, function, cft) -> lightChunk(cfx, aaj, cft));
        SPAWN = registerSimple("spawn", ChunkStatus.LIGHT, 0, ChunkStatus.POST_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> cfv.spawnOriginalMobs(new WorldGenRegion(aag, list)));
        HEIGHTMAPS = registerSimple("heightmaps", ChunkStatus.SPAWN, 0, ChunkStatus.POST_FEATURES, ChunkType.PROTOCHUNK, (aag, cfv, list, cft) -> {});
        FULL = register("full", ChunkStatus.HEIGHTMAPS, 0, ChunkStatus.POST_FEATURES, ChunkType.LEVELCHUNK, (cfx, aag, cfv, cst, aaj, function, list, cft) -> (CompletableFuture)function.apply(cft), (cfx, aag, cst, aaj, function, cft) -> (CompletableFuture)function.apply(cft));
        STATUS_BY_RANGE = (List)ImmutableList.of(ChunkStatus.FULL, ChunkStatus.FEATURES, ChunkStatus.LIQUID_CARVERS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS);
        RANGE_BY_STATUS = Util.<IntList>make((IntList)new IntArrayList(getStatusList().size()), (java.util.function.Consumer<IntList>)(intArrayList -> {
            int integer2 = 0;
            for (int integer3 = getStatusList().size() - 1; integer3 >= 0; --integer3) {
                while (integer2 + 1 < ChunkStatus.STATUS_BY_RANGE.size() && integer3 <= ((ChunkStatus)ChunkStatus.STATUS_BY_RANGE.get(integer2 + 1)).getIndex()) {
                    ++integer2;
                }
                intArrayList.add(0, integer2);
            }
        }));
    }
    
    interface SimpleGenerationTask extends GenerationTask {
        default CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(final ChunkStatus cfx, final ServerLevel aag, final ChunkGenerator cfv, final StructureManager cst, final ThreadedLevelLightEngine aaj, final Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> function, final List<ChunkAccess> list, final ChunkAccess cft) {
            if (!cft.getStatus().isOrAfter(cfx)) {
                this.doWork(aag, cfv, list, cft);
                if (cft instanceof ProtoChunk) {
                    ((ProtoChunk)cft).setStatus(cfx);
                }
            }
            return (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)CompletableFuture.completedFuture(Either.left((Object)cft));
        }
        
        void doWork(final ServerLevel aag, final ChunkGenerator cfv, final List<ChunkAccess> list, final ChunkAccess cft);
    }
    
    public enum ChunkType {
        PROTOCHUNK, 
        LEVELCHUNK;
    }
    
    interface GenerationTask {
        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(final ChunkStatus cfx, final ServerLevel aag, final ChunkGenerator cfv, final StructureManager cst, final ThreadedLevelLightEngine aaj, final Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> function, final List<ChunkAccess> list, final ChunkAccess cft);
    }
    
    interface LoadingTask {
        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(final ChunkStatus cfx, final ServerLevel aag, final StructureManager cst, final ThreadedLevelLightEngine aaj, final Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> function, final ChunkAccess cft);
    }
}
