package net.minecraft.world.level.chunk;

import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.world.level.TickList;
import java.util.Collections;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.EnumSet;
import net.minecraft.world.level.BlockGetter;
import java.util.Iterator;
import java.util.stream.Stream;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.function.Predicate;
import java.util.BitSet;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;
import net.minecraft.world.level.lighting.LevelLightEngine;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Logger;

public class ProtoChunk implements ChunkAccess {
    private static final Logger LOGGER;
    private final ChunkPos chunkPos;
    private volatile boolean isDirty;
    @Nullable
    private ChunkBiomeContainer biomes;
    @Nullable
    private volatile LevelLightEngine lightEngine;
    private final Map<Heightmap.Types, Heightmap> heightmaps;
    private volatile ChunkStatus status;
    private final Map<BlockPos, BlockEntity> blockEntities;
    private final Map<BlockPos, CompoundTag> blockEntityNbts;
    private final LevelChunkSection[] sections;
    private final List<CompoundTag> entities;
    private final List<BlockPos> lights;
    private final ShortList[] postProcessing;
    private final Map<StructureFeature<?>, StructureStart<?>> structureStarts;
    private final Map<StructureFeature<?>, LongSet> structuresRefences;
    private final UpgradeData upgradeData;
    private final ProtoTickList<Block> blockTicks;
    private final ProtoTickList<Fluid> liquidTicks;
    private long inhabitedTime;
    private final Map<GenerationStep.Carving, BitSet> carvingMasks;
    private volatile boolean isLightCorrect;
    
    public ProtoChunk(final ChunkPos bra, final UpgradeData cgo) {
        this(bra, cgo, null, new ProtoTickList<Block>((java.util.function.Predicate<Block>)(bul -> bul == null || bul.defaultBlockState().isAir()), bra), new ProtoTickList<Fluid>((java.util.function.Predicate<Fluid>)(cut -> cut == null || cut == Fluids.EMPTY), bra));
    }
    
    public ProtoChunk(final ChunkPos bra, final UpgradeData cgo, @Nullable final LevelChunkSection[] arr, final ProtoTickList<Block> cgn4, final ProtoTickList<Fluid> cgn5) {
        this.heightmaps = (Map<Heightmap.Types, Heightmap>)Maps.newEnumMap((Class)Heightmap.Types.class);
        this.status = ChunkStatus.EMPTY;
        this.blockEntities = (Map<BlockPos, BlockEntity>)Maps.newHashMap();
        this.blockEntityNbts = (Map<BlockPos, CompoundTag>)Maps.newHashMap();
        this.sections = new LevelChunkSection[16];
        this.entities = (List<CompoundTag>)Lists.newArrayList();
        this.lights = (List<BlockPos>)Lists.newArrayList();
        this.postProcessing = new ShortList[16];
        this.structureStarts = (Map<StructureFeature<?>, StructureStart<?>>)Maps.newHashMap();
        this.structuresRefences = (Map<StructureFeature<?>, LongSet>)Maps.newHashMap();
        this.carvingMasks = (Map<GenerationStep.Carving, BitSet>)new Object2ObjectArrayMap();
        this.chunkPos = bra;
        this.upgradeData = cgo;
        this.blockTicks = cgn4;
        this.liquidTicks = cgn5;
        if (arr != null) {
            if (this.sections.length == arr.length) {
                System.arraycopy(arr, 0, this.sections, 0, this.sections.length);
            }
            else {
                ProtoChunk.LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", arr.length, this.sections.length);
            }
        }
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        final int integer3 = fx.getY();
        if (Level.isOutsideBuildHeight(integer3)) {
            return Blocks.VOID_AIR.defaultBlockState();
        }
        final LevelChunkSection cgf4 = this.getSections()[integer3 >> 4];
        if (LevelChunkSection.isEmpty(cgf4)) {
            return Blocks.AIR.defaultBlockState();
        }
        return cgf4.getBlockState(fx.getX() & 0xF, integer3 & 0xF, fx.getZ() & 0xF);
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        final int integer3 = fx.getY();
        if (Level.isOutsideBuildHeight(integer3)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        final LevelChunkSection cgf4 = this.getSections()[integer3 >> 4];
        if (LevelChunkSection.isEmpty(cgf4)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        return cgf4.getFluidState(fx.getX() & 0xF, integer3 & 0xF, fx.getZ() & 0xF);
    }
    
    public Stream<BlockPos> getLights() {
        return (Stream<BlockPos>)this.lights.stream();
    }
    
    public ShortList[] getPackedLights() {
        final ShortList[] arr2 = new ShortList[16];
        for (final BlockPos fx4 : this.lights) {
            ChunkAccess.getOrCreateOffsetList(arr2, fx4.getY() >> 4).add(packOffsetCoordinates(fx4));
        }
        return arr2;
    }
    
    public void addLight(final short short1, final int integer) {
        this.addLight(unpackOffsetCoordinates(short1, integer, this.chunkPos));
    }
    
    public void addLight(final BlockPos fx) {
        this.lights.add(fx.immutable());
    }
    
    @Nullable
    public BlockState setBlockState(final BlockPos fx, final BlockState cee, final boolean boolean3) {
        final int integer5 = fx.getX();
        final int integer6 = fx.getY();
        final int integer7 = fx.getZ();
        if (integer6 < 0 || integer6 >= 256) {
            return Blocks.VOID_AIR.defaultBlockState();
        }
        if (this.sections[integer6 >> 4] == LevelChunk.EMPTY_SECTION && cee.is(Blocks.AIR)) {
            return cee;
        }
        if (cee.getLightEmission() > 0) {
            this.lights.add(new BlockPos((integer5 & 0xF) + this.getPos().getMinBlockX(), integer6, (integer7 & 0xF) + this.getPos().getMinBlockZ()));
        }
        final LevelChunkSection cgf8 = this.getOrCreateSection(integer6 >> 4);
        final BlockState cee2 = cgf8.setBlockState(integer5 & 0xF, integer6 & 0xF, integer7 & 0xF, cee);
        if (this.status.isOrAfter(ChunkStatus.FEATURES) && cee != cee2 && (cee.getLightBlock(this, fx) != cee2.getLightBlock(this, fx) || cee.getLightEmission() != cee2.getLightEmission() || cee.useShapeForLightOcclusion() || cee2.useShapeForLightOcclusion())) {
            final LevelLightEngine cul10 = this.getLightEngine();
            cul10.checkBlock(fx);
        }
        final EnumSet<Heightmap.Types> enumSet10 = this.getStatus().heightmapsAfter();
        EnumSet<Heightmap.Types> enumSet11 = null;
        for (final Heightmap.Types a13 : enumSet10) {
            final Heightmap chk14 = (Heightmap)this.heightmaps.get(a13);
            if (chk14 == null) {
                if (enumSet11 == null) {
                    enumSet11 = (EnumSet<Heightmap.Types>)EnumSet.noneOf((Class)Heightmap.Types.class);
                }
                enumSet11.add(a13);
            }
        }
        if (enumSet11 != null) {
            Heightmap.primeHeightmaps(this, (Set<Heightmap.Types>)enumSet11);
        }
        for (final Heightmap.Types a13 : enumSet10) {
            ((Heightmap)this.heightmaps.get(a13)).update(integer5 & 0xF, integer6, integer7 & 0xF, cee);
        }
        return cee2;
    }
    
    public LevelChunkSection getOrCreateSection(final int integer) {
        if (this.sections[integer] == LevelChunk.EMPTY_SECTION) {
            this.sections[integer] = new LevelChunkSection(integer << 4);
        }
        return this.sections[integer];
    }
    
    public void setBlockEntity(final BlockPos fx, final BlockEntity ccg) {
        ccg.setPosition(fx);
        this.blockEntities.put(fx, ccg);
    }
    
    public Set<BlockPos> getBlockEntitiesPos() {
        final Set<BlockPos> set2 = (Set<BlockPos>)Sets.newHashSet((Iterable)this.blockEntityNbts.keySet());
        set2.addAll((Collection)this.blockEntities.keySet());
        return set2;
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        return (BlockEntity)this.blockEntities.get(fx);
    }
    
    public Map<BlockPos, BlockEntity> getBlockEntities() {
        return this.blockEntities;
    }
    
    public void addEntity(final CompoundTag md) {
        this.entities.add(md);
    }
    
    public void addEntity(final Entity apx) {
        if (apx.isPassenger()) {
            return;
        }
        final CompoundTag md3 = new CompoundTag();
        apx.save(md3);
        this.addEntity(md3);
    }
    
    public List<CompoundTag> getEntities() {
        return this.entities;
    }
    
    public void setBiomes(final ChunkBiomeContainer cfu) {
        this.biomes = cfu;
    }
    
    @Nullable
    public ChunkBiomeContainer getBiomes() {
        return this.biomes;
    }
    
    public void setUnsaved(final boolean boolean1) {
        this.isDirty = boolean1;
    }
    
    public boolean isUnsaved() {
        return this.isDirty;
    }
    
    public ChunkStatus getStatus() {
        return this.status;
    }
    
    public void setStatus(final ChunkStatus cfx) {
        this.status = cfx;
        this.setUnsaved(true);
    }
    
    public LevelChunkSection[] getSections() {
        return this.sections;
    }
    
    @Nullable
    public LevelLightEngine getLightEngine() {
        return this.lightEngine;
    }
    
    public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
        return (Collection<Map.Entry<Heightmap.Types, Heightmap>>)Collections.unmodifiableSet(this.heightmaps.entrySet());
    }
    
    public void setHeightmap(final Heightmap.Types a, final long[] arr) {
        this.getOrCreateHeightmapUnprimed(a).setRawData(arr);
    }
    
    public Heightmap getOrCreateHeightmapUnprimed(final Heightmap.Types a) {
        return (Heightmap)this.heightmaps.computeIfAbsent(a, a -> new Heightmap(this, a));
    }
    
    public int getHeight(final Heightmap.Types a, final int integer2, final int integer3) {
        Heightmap chk5 = (Heightmap)this.heightmaps.get(a);
        if (chk5 == null) {
            Heightmap.primeHeightmaps(this, (Set<Heightmap.Types>)EnumSet.of((Enum)a));
            chk5 = (Heightmap)this.heightmaps.get(a);
        }
        return chk5.getFirstAvailable(integer2 & 0xF, integer3 & 0xF) - 1;
    }
    
    public ChunkPos getPos() {
        return this.chunkPos;
    }
    
    public void setLastSaveTime(final long long1) {
    }
    
    @Nullable
    public StructureStart<?> getStartForFeature(final StructureFeature<?> ckx) {
        return this.structureStarts.get(ckx);
    }
    
    public void setStartForFeature(final StructureFeature<?> ckx, final StructureStart<?> crs) {
        this.structureStarts.put(ckx, crs);
        this.isDirty = true;
    }
    
    public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
        return (Map<StructureFeature<?>, StructureStart<?>>)Collections.unmodifiableMap((Map)this.structureStarts);
    }
    
    public void setAllStarts(final Map<StructureFeature<?>, StructureStart<?>> map) {
        this.structureStarts.clear();
        this.structureStarts.putAll((Map)map);
        this.isDirty = true;
    }
    
    public LongSet getReferencesForFeature(final StructureFeature<?> ckx) {
        return (LongSet)this.structuresRefences.computeIfAbsent(ckx, ckx -> new LongOpenHashSet());
    }
    
    public void addReferenceForFeature(final StructureFeature<?> ckx, final long long2) {
        ((LongSet)this.structuresRefences.computeIfAbsent(ckx, ckx -> new LongOpenHashSet())).add(long2);
        this.isDirty = true;
    }
    
    public Map<StructureFeature<?>, LongSet> getAllReferences() {
        return (Map<StructureFeature<?>, LongSet>)Collections.unmodifiableMap((Map)this.structuresRefences);
    }
    
    public void setAllReferences(final Map<StructureFeature<?>, LongSet> map) {
        this.structuresRefences.clear();
        this.structuresRefences.putAll((Map)map);
        this.isDirty = true;
    }
    
    public static short packOffsetCoordinates(final BlockPos fx) {
        final int integer2 = fx.getX();
        final int integer3 = fx.getY();
        final int integer4 = fx.getZ();
        final int integer5 = integer2 & 0xF;
        final int integer6 = integer3 & 0xF;
        final int integer7 = integer4 & 0xF;
        return (short)(integer5 | integer6 << 4 | integer7 << 8);
    }
    
    public static BlockPos unpackOffsetCoordinates(final short short1, final int integer, final ChunkPos bra) {
        final int integer2 = (short1 & 0xF) + (bra.x << 4);
        final int integer3 = (short1 >>> 4 & 0xF) + (integer << 4);
        final int integer4 = (short1 >>> 8 & 0xF) + (bra.z << 4);
        return new BlockPos(integer2, integer3, integer4);
    }
    
    public void markPosForPostprocessing(final BlockPos fx) {
        if (!Level.isOutsideBuildHeight(fx)) {
            ChunkAccess.getOrCreateOffsetList(this.postProcessing, fx.getY() >> 4).add(packOffsetCoordinates(fx));
        }
    }
    
    public ShortList[] getPostProcessing() {
        return this.postProcessing;
    }
    
    public void addPackedPostProcess(final short short1, final int integer) {
        ChunkAccess.getOrCreateOffsetList(this.postProcessing, integer).add(short1);
    }
    
    public ProtoTickList<Block> getBlockTicks() {
        return this.blockTicks;
    }
    
    public ProtoTickList<Fluid> getLiquidTicks() {
        return this.liquidTicks;
    }
    
    public UpgradeData getUpgradeData() {
        return this.upgradeData;
    }
    
    public void setInhabitedTime(final long long1) {
        this.inhabitedTime = long1;
    }
    
    public long getInhabitedTime() {
        return this.inhabitedTime;
    }
    
    public void setBlockEntityNbt(final CompoundTag md) {
        this.blockEntityNbts.put(new BlockPos(md.getInt("x"), md.getInt("y"), md.getInt("z")), md);
    }
    
    public Map<BlockPos, CompoundTag> getBlockEntityNbts() {
        return (Map<BlockPos, CompoundTag>)Collections.unmodifiableMap((Map)this.blockEntityNbts);
    }
    
    public CompoundTag getBlockEntityNbt(final BlockPos fx) {
        return (CompoundTag)this.blockEntityNbts.get(fx);
    }
    
    @Nullable
    public CompoundTag getBlockEntityNbtForSaving(final BlockPos fx) {
        final BlockEntity ccg3 = this.getBlockEntity(fx);
        if (ccg3 != null) {
            return ccg3.save(new CompoundTag());
        }
        return (CompoundTag)this.blockEntityNbts.get(fx);
    }
    
    public void removeBlockEntity(final BlockPos fx) {
        this.blockEntities.remove(fx);
        this.blockEntityNbts.remove(fx);
    }
    
    @Nullable
    public BitSet getCarvingMask(final GenerationStep.Carving a) {
        return (BitSet)this.carvingMasks.get(a);
    }
    
    public BitSet getOrCreateCarvingMask(final GenerationStep.Carving a) {
        return (BitSet)this.carvingMasks.computeIfAbsent(a, a -> new BitSet(65536));
    }
    
    public void setCarvingMask(final GenerationStep.Carving a, final BitSet bitSet) {
        this.carvingMasks.put(a, bitSet);
    }
    
    public void setLightEngine(final LevelLightEngine cul) {
        this.lightEngine = cul;
    }
    
    public boolean isLightCorrect() {
        return this.isLightCorrect;
    }
    
    public void setLightCorrect(final boolean boolean1) {
        this.isLightCorrect = boolean1;
        this.setUnsaved(true);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
