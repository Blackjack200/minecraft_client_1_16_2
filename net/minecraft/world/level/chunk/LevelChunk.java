package net.minecraft.world.level.chunk;

import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkTickList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import net.minecraft.world.level.LevelAccessor;
import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import java.util.Collections;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Collection;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Iterator;
import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import com.google.common.collect.Maps;
import net.minecraft.world.level.EmptyTickList;
import net.minecraft.world.level.ChunkPos;
import java.util.function.Consumer;
import net.minecraft.server.level.ChunkHolder;
import java.util.function.Supplier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.TickList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class LevelChunk implements ChunkAccess {
    private static final Logger LOGGER;
    @Nullable
    public static final LevelChunkSection EMPTY_SECTION;
    private final LevelChunkSection[] sections;
    private ChunkBiomeContainer biomes;
    private final Map<BlockPos, CompoundTag> pendingBlockEntities;
    private boolean loaded;
    private final Level level;
    private final Map<Heightmap.Types, Heightmap> heightmaps;
    private final UpgradeData upgradeData;
    private final Map<BlockPos, BlockEntity> blockEntities;
    private final ClassInstanceMultiMap<Entity>[] entitySections;
    private final Map<StructureFeature<?>, StructureStart<?>> structureStarts;
    private final Map<StructureFeature<?>, LongSet> structuresRefences;
    private final ShortList[] postProcessing;
    private TickList<Block> blockTicks;
    private TickList<Fluid> liquidTicks;
    private boolean lastSaveHadEntities;
    private long lastSaveTime;
    private volatile boolean unsaved;
    private long inhabitedTime;
    @Nullable
    private Supplier<ChunkHolder.FullChunkStatus> fullStatus;
    @Nullable
    private Consumer<LevelChunk> postLoad;
    private final ChunkPos chunkPos;
    private volatile boolean isLightCorrect;
    
    public LevelChunk(final Level bru, final ChunkPos bra, final ChunkBiomeContainer cfu) {
        this(bru, bra, cfu, UpgradeData.EMPTY, EmptyTickList.empty(), EmptyTickList.empty(), 0L, null, null);
    }
    
    public LevelChunk(final Level bru, final ChunkPos bra, final ChunkBiomeContainer cfu, final UpgradeData cgo, final TickList<Block> bsl5, final TickList<Fluid> bsl6, final long long7, @Nullable final LevelChunkSection[] arr, @Nullable final Consumer<LevelChunk> consumer) {
        this.sections = new LevelChunkSection[16];
        this.pendingBlockEntities = (Map<BlockPos, CompoundTag>)Maps.newHashMap();
        this.heightmaps = (Map<Heightmap.Types, Heightmap>)Maps.newEnumMap((Class)Heightmap.Types.class);
        this.blockEntities = (Map<BlockPos, BlockEntity>)Maps.newHashMap();
        this.structureStarts = (Map<StructureFeature<?>, StructureStart<?>>)Maps.newHashMap();
        this.structuresRefences = (Map<StructureFeature<?>, LongSet>)Maps.newHashMap();
        this.postProcessing = new ShortList[16];
        this.entitySections = new ClassInstanceMultiMap[16];
        this.level = bru;
        this.chunkPos = bra;
        this.upgradeData = cgo;
        for (final Heightmap.Types a15 : Heightmap.Types.values()) {
            if (ChunkStatus.FULL.heightmapsAfter().contains(a15)) {
                this.heightmaps.put(a15, new Heightmap(this, a15));
            }
        }
        for (int integer12 = 0; integer12 < this.entitySections.length; ++integer12) {
            this.entitySections[integer12] = new ClassInstanceMultiMap<Entity>(Entity.class);
        }
        this.biomes = cfu;
        this.blockTicks = bsl5;
        this.liquidTicks = bsl6;
        this.inhabitedTime = long7;
        this.postLoad = consumer;
        if (arr != null) {
            if (this.sections.length == arr.length) {
                System.arraycopy(arr, 0, this.sections, 0, this.sections.length);
            }
            else {
                LevelChunk.LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", arr.length, this.sections.length);
            }
        }
    }
    
    public LevelChunk(final Level bru, final ProtoChunk cgm) {
        this(bru, cgm.getPos(), cgm.getBiomes(), cgm.getUpgradeData(), cgm.getBlockTicks(), cgm.getLiquidTicks(), cgm.getInhabitedTime(), cgm.getSections(), null);
        for (final CompoundTag md5 : cgm.getEntities()) {
            EntityType.loadEntityRecursive(md5, bru, (Function<Entity, Entity>)(apx -> {
                this.addEntity(apx);
                return apx;
            }));
        }
        for (final BlockEntity ccg5 : cgm.getBlockEntities().values()) {
            this.addBlockEntity(ccg5);
        }
        this.pendingBlockEntities.putAll((Map)cgm.getBlockEntityNbts());
        for (int integer4 = 0; integer4 < cgm.getPostProcessing().length; ++integer4) {
            this.postProcessing[integer4] = cgm.getPostProcessing()[integer4];
        }
        this.setAllStarts(cgm.getAllStarts());
        this.setAllReferences(cgm.getAllReferences());
        for (final Map.Entry<Heightmap.Types, Heightmap> entry5 : cgm.getHeightmaps()) {
            if (ChunkStatus.FULL.heightmapsAfter().contains(entry5.getKey())) {
                this.getOrCreateHeightmapUnprimed((Heightmap.Types)entry5.getKey()).setRawData(((Heightmap)entry5.getValue()).getRawData());
            }
        }
        this.setLightCorrect(cgm.isLightCorrect());
        this.unsaved = true;
    }
    
    public Heightmap getOrCreateHeightmapUnprimed(final Heightmap.Types a) {
        return (Heightmap)this.heightmaps.computeIfAbsent(a, a -> new Heightmap(this, a));
    }
    
    public Set<BlockPos> getBlockEntitiesPos() {
        final Set<BlockPos> set2 = (Set<BlockPos>)Sets.newHashSet((Iterable)this.pendingBlockEntities.keySet());
        set2.addAll((Collection)this.blockEntities.keySet());
        return set2;
    }
    
    public LevelChunkSection[] getSections() {
        return this.sections;
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        final int integer3 = fx.getX();
        final int integer4 = fx.getY();
        final int integer5 = fx.getZ();
        if (this.level.isDebug()) {
            BlockState cee6 = null;
            if (integer4 == 60) {
                cee6 = Blocks.BARRIER.defaultBlockState();
            }
            if (integer4 == 70) {
                cee6 = DebugLevelSource.getBlockStateFor(integer3, integer5);
            }
            return (cee6 == null) ? Blocks.AIR.defaultBlockState() : cee6;
        }
        try {
            if (integer4 >= 0 && integer4 >> 4 < this.sections.length) {
                final LevelChunkSection cgf6 = this.sections[integer4 >> 4];
                if (!LevelChunkSection.isEmpty(cgf6)) {
                    return cgf6.getBlockState(integer3 & 0xF, integer4 & 0xF, integer5 & 0xF);
                }
            }
            return Blocks.AIR.defaultBlockState();
        }
        catch (Throwable throwable6) {
            final CrashReport l7 = CrashReport.forThrowable(throwable6, "Getting block state");
            final CrashReportCategory m8 = l7.addCategory("Block being got");
            m8.setDetail("Location", (CrashReportDetail<String>)(() -> CrashReportCategory.formatLocation(integer3, integer4, integer5)));
            throw new ReportedException(l7);
        }
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        return this.getFluidState(fx.getX(), fx.getY(), fx.getZ());
    }
    
    public FluidState getFluidState(final int integer1, final int integer2, final int integer3) {
        try {
            if (integer2 >= 0 && integer2 >> 4 < this.sections.length) {
                final LevelChunkSection cgf5 = this.sections[integer2 >> 4];
                if (!LevelChunkSection.isEmpty(cgf5)) {
                    return cgf5.getFluidState(integer1 & 0xF, integer2 & 0xF, integer3 & 0xF);
                }
            }
            return Fluids.EMPTY.defaultFluidState();
        }
        catch (Throwable throwable5) {
            final CrashReport l6 = CrashReport.forThrowable(throwable5, "Getting fluid state");
            final CrashReportCategory m7 = l6.addCategory("Block being got");
            m7.setDetail("Location", (CrashReportDetail<String>)(() -> CrashReportCategory.formatLocation(integer1, integer2, integer3)));
            throw new ReportedException(l6);
        }
    }
    
    @Nullable
    public BlockState setBlockState(final BlockPos fx, final BlockState cee, final boolean boolean3) {
        final int integer5 = fx.getX() & 0xF;
        final int integer6 = fx.getY();
        final int integer7 = fx.getZ() & 0xF;
        LevelChunkSection cgf8 = this.sections[integer6 >> 4];
        if (cgf8 == LevelChunk.EMPTY_SECTION) {
            if (cee.isAir()) {
                return null;
            }
            cgf8 = new LevelChunkSection(integer6 >> 4 << 4);
            this.sections[integer6 >> 4] = cgf8;
        }
        final boolean boolean4 = cgf8.isEmpty();
        final BlockState cee2 = cgf8.setBlockState(integer5, integer6 & 0xF, integer7, cee);
        if (cee2 == cee) {
            return null;
        }
        final Block bul11 = cee.getBlock();
        final Block bul12 = cee2.getBlock();
        ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING)).update(integer5, integer6, integer7, cee);
        ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)).update(integer5, integer6, integer7, cee);
        ((Heightmap)this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR)).update(integer5, integer6, integer7, cee);
        ((Heightmap)this.heightmaps.get(Heightmap.Types.WORLD_SURFACE)).update(integer5, integer6, integer7, cee);
        final boolean boolean5 = cgf8.isEmpty();
        if (boolean4 != boolean5) {
            this.level.getChunkSource().getLightEngine().updateSectionStatus(fx, boolean5);
        }
        if (!this.level.isClientSide) {
            cee2.onRemove(this.level, fx, cee, boolean3);
        }
        else if (bul12 != bul11 && bul12 instanceof EntityBlock) {
            this.level.removeBlockEntity(fx);
        }
        if (!cgf8.getBlockState(integer5, integer6 & 0xF, integer7).is(bul11)) {
            return null;
        }
        if (bul12 instanceof EntityBlock) {
            final BlockEntity ccg14 = this.getBlockEntity(fx, EntityCreationType.CHECK);
            if (ccg14 != null) {
                ccg14.clearCache();
            }
        }
        if (!this.level.isClientSide) {
            cee.onPlace(this.level, fx, cee2, boolean3);
        }
        if (bul11 instanceof EntityBlock) {
            BlockEntity ccg14 = this.getBlockEntity(fx, EntityCreationType.CHECK);
            if (ccg14 == null) {
                ccg14 = ((EntityBlock)bul11).newBlockEntity(this.level);
                this.level.setBlockEntity(fx, ccg14);
            }
            else {
                ccg14.clearCache();
            }
        }
        this.unsaved = true;
        return cee2;
    }
    
    @Nullable
    public LevelLightEngine getLightEngine() {
        return this.level.getChunkSource().getLightEngine();
    }
    
    public void addEntity(final Entity apx) {
        this.lastSaveHadEntities = true;
        final int integer3 = Mth.floor(apx.getX() / 16.0);
        final int integer4 = Mth.floor(apx.getZ() / 16.0);
        if (integer3 != this.chunkPos.x || integer4 != this.chunkPos.z) {
            LevelChunk.LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", integer3, integer4, this.chunkPos.x, this.chunkPos.z, apx);
            apx.removed = true;
        }
        int integer5 = Mth.floor(apx.getY() / 16.0);
        if (integer5 < 0) {
            integer5 = 0;
        }
        if (integer5 >= this.entitySections.length) {
            integer5 = this.entitySections.length - 1;
        }
        apx.inChunk = true;
        apx.xChunk = this.chunkPos.x;
        apx.yChunk = integer5;
        apx.zChunk = this.chunkPos.z;
        this.entitySections[integer5].add(apx);
    }
    
    public void setHeightmap(final Heightmap.Types a, final long[] arr) {
        ((Heightmap)this.heightmaps.get(a)).setRawData(arr);
    }
    
    public void removeEntity(final Entity apx) {
        this.removeEntity(apx, apx.yChunk);
    }
    
    public void removeEntity(final Entity apx, int integer) {
        if (integer < 0) {
            integer = 0;
        }
        if (integer >= this.entitySections.length) {
            integer = this.entitySections.length - 1;
        }
        this.entitySections[integer].remove(apx);
    }
    
    public int getHeight(final Heightmap.Types a, final int integer2, final int integer3) {
        return ((Heightmap)this.heightmaps.get(a)).getFirstAvailable(integer2 & 0xF, integer3 & 0xF) - 1;
    }
    
    @Nullable
    private BlockEntity createBlockEntity(final BlockPos fx) {
        final BlockState cee3 = this.getBlockState(fx);
        final Block bul4 = cee3.getBlock();
        if (!bul4.isEntityBlock()) {
            return null;
        }
        return ((EntityBlock)bul4).newBlockEntity(this.level);
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        return this.getBlockEntity(fx, EntityCreationType.CHECK);
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx, final EntityCreationType a) {
        BlockEntity ccg4 = (BlockEntity)this.blockEntities.get(fx);
        if (ccg4 == null) {
            final CompoundTag md5 = (CompoundTag)this.pendingBlockEntities.remove(fx);
            if (md5 != null) {
                final BlockEntity ccg5 = this.promotePendingBlockEntity(fx, md5);
                if (ccg5 != null) {
                    return ccg5;
                }
            }
        }
        if (ccg4 == null) {
            if (a == EntityCreationType.IMMEDIATE) {
                ccg4 = this.createBlockEntity(fx);
                this.level.setBlockEntity(fx, ccg4);
            }
        }
        else if (ccg4.isRemoved()) {
            this.blockEntities.remove(fx);
            return null;
        }
        return ccg4;
    }
    
    public void addBlockEntity(final BlockEntity ccg) {
        this.setBlockEntity(ccg.getBlockPos(), ccg);
        if (this.loaded || this.level.isClientSide()) {
            this.level.setBlockEntity(ccg.getBlockPos(), ccg);
        }
    }
    
    public void setBlockEntity(final BlockPos fx, final BlockEntity ccg) {
        if (!(this.getBlockState(fx).getBlock() instanceof EntityBlock)) {
            return;
        }
        ccg.setLevelAndPosition(this.level, fx);
        ccg.clearRemoved();
        final BlockEntity ccg2 = (BlockEntity)this.blockEntities.put(fx.immutable(), ccg);
        if (ccg2 != null && ccg2 != ccg) {
            ccg2.setRemoved();
        }
    }
    
    public void setBlockEntityNbt(final CompoundTag md) {
        this.pendingBlockEntities.put(new BlockPos(md.getInt("x"), md.getInt("y"), md.getInt("z")), md);
    }
    
    @Nullable
    public CompoundTag getBlockEntityNbtForSaving(final BlockPos fx) {
        final BlockEntity ccg3 = this.getBlockEntity(fx);
        if (ccg3 != null && !ccg3.isRemoved()) {
            final CompoundTag md4 = ccg3.save(new CompoundTag());
            md4.putBoolean("keepPacked", false);
            return md4;
        }
        CompoundTag md4 = (CompoundTag)this.pendingBlockEntities.get(fx);
        if (md4 != null) {
            md4 = md4.copy();
            md4.putBoolean("keepPacked", true);
        }
        return md4;
    }
    
    public void removeBlockEntity(final BlockPos fx) {
        if (this.loaded || this.level.isClientSide()) {
            final BlockEntity ccg3 = (BlockEntity)this.blockEntities.remove(fx);
            if (ccg3 != null) {
                ccg3.setRemoved();
            }
        }
    }
    
    public void runPostLoad() {
        if (this.postLoad != null) {
            this.postLoad.accept(this);
            this.postLoad = null;
        }
    }
    
    public void markUnsaved() {
        this.unsaved = true;
    }
    
    public void getEntities(@Nullable final Entity apx, final AABB dcf, final List<Entity> list, @Nullable final Predicate<? super Entity> predicate) {
        int integer6 = Mth.floor((dcf.minY - 2.0) / 16.0);
        int integer7 = Mth.floor((dcf.maxY + 2.0) / 16.0);
        integer6 = Mth.clamp(integer6, 0, this.entitySections.length - 1);
        integer7 = Mth.clamp(integer7, 0, this.entitySections.length - 1);
        for (int integer8 = integer6; integer8 <= integer7; ++integer8) {
            final ClassInstanceMultiMap<Entity> aeq9 = this.entitySections[integer8];
            final List<Entity> list2 = aeq9.getAllInstances();
            for (int integer9 = list2.size(), integer10 = 0; integer10 < integer9; ++integer10) {
                final Entity apx2 = (Entity)list2.get(integer10);
                if (apx2.getBoundingBox().intersects(dcf) && apx2 != apx) {
                    if (predicate == null || predicate.test(apx2)) {
                        list.add(apx2);
                    }
                    if (apx2 instanceof EnderDragon) {
                        for (final EnderDragonPart bbm17 : ((EnderDragon)apx2).getSubEntities()) {
                            if (bbm17 != apx && bbm17.getBoundingBox().intersects(dcf) && (predicate == null || predicate.test(bbm17))) {
                                list.add(bbm17);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public <T extends Entity> void getEntities(@Nullable final EntityType<?> aqb, final AABB dcf, final List<? super T> list, final Predicate<? super T> predicate) {
        int integer6 = Mth.floor((dcf.minY - 2.0) / 16.0);
        int integer7 = Mth.floor((dcf.maxY + 2.0) / 16.0);
        integer6 = Mth.clamp(integer6, 0, this.entitySections.length - 1);
        integer7 = Mth.clamp(integer7, 0, this.entitySections.length - 1);
        for (int integer8 = integer6; integer8 <= integer7; ++integer8) {
            for (final Entity apx10 : this.entitySections[integer8].<Entity>find(Entity.class)) {
                if (aqb != null && apx10.getType() != aqb) {
                    continue;
                }
                final T apx11 = (T)apx10;
                if (!apx10.getBoundingBox().intersects(dcf) || !predicate.test(apx11)) {
                    continue;
                }
                list.add(apx11);
            }
        }
    }
    
    public <T extends Entity> void getEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, final List<T> list, @Nullable final Predicate<? super T> predicate) {
        int integer6 = Mth.floor((dcf.minY - 2.0) / 16.0);
        int integer7 = Mth.floor((dcf.maxY + 2.0) / 16.0);
        integer6 = Mth.clamp(integer6, 0, this.entitySections.length - 1);
        integer7 = Mth.clamp(integer7, 0, this.entitySections.length - 1);
        for (int integer8 = integer6; integer8 <= integer7; ++integer8) {
            for (final T apx10 : this.entitySections[integer8].find(class1)) {
                if (apx10.getBoundingBox().intersects(dcf) && (predicate == null || predicate.test(apx10))) {
                    list.add(apx10);
                }
            }
        }
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    public ChunkPos getPos() {
        return this.chunkPos;
    }
    
    public void replaceWithPacketData(@Nullable final ChunkBiomeContainer cfu, final FriendlyByteBuf nf, final CompoundTag md, final int integer) {
        final boolean boolean6 = cfu != null;
        final Predicate<BlockPos> predicate7 = (Predicate<BlockPos>)(boolean6 ? (fx -> true) : (fx -> (integer & 1 << (fx.getY() >> 4)) != 0x0));
        Sets.newHashSet((Iterable)this.blockEntities.keySet()).stream().filter((Predicate)predicate7).forEach(this.level::removeBlockEntity);
        for (int integer2 = 0; integer2 < this.sections.length; ++integer2) {
            LevelChunkSection cgf9 = this.sections[integer2];
            if ((integer & 1 << integer2) == 0x0) {
                if (boolean6 && cgf9 != LevelChunk.EMPTY_SECTION) {
                    this.sections[integer2] = LevelChunk.EMPTY_SECTION;
                }
            }
            else {
                if (cgf9 == LevelChunk.EMPTY_SECTION) {
                    cgf9 = new LevelChunkSection(integer2 << 4);
                    this.sections[integer2] = cgf9;
                }
                cgf9.read(nf);
            }
        }
        if (cfu != null) {
            this.biomes = cfu;
        }
        for (final Heightmap.Types a11 : Heightmap.Types.values()) {
            final String string12 = a11.getSerializationKey();
            if (md.contains(string12, 12)) {
                this.setHeightmap(a11, md.getLongArray(string12));
            }
        }
        for (final BlockEntity ccg9 : this.blockEntities.values()) {
            ccg9.clearCache();
        }
    }
    
    public ChunkBiomeContainer getBiomes() {
        return this.biomes;
    }
    
    public void setLoaded(final boolean boolean1) {
        this.loaded = boolean1;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
        return (Collection<Map.Entry<Heightmap.Types, Heightmap>>)Collections.unmodifiableSet(this.heightmaps.entrySet());
    }
    
    public Map<BlockPos, BlockEntity> getBlockEntities() {
        return this.blockEntities;
    }
    
    public ClassInstanceMultiMap<Entity>[] getEntitySections() {
        return this.entitySections;
    }
    
    public CompoundTag getBlockEntityNbt(final BlockPos fx) {
        return (CompoundTag)this.pendingBlockEntities.get(fx);
    }
    
    public Stream<BlockPos> getLights() {
        return (Stream<BlockPos>)StreamSupport.stream(BlockPos.betweenClosed(this.chunkPos.getMinBlockX(), 0, this.chunkPos.getMinBlockZ(), this.chunkPos.getMaxBlockX(), 255, this.chunkPos.getMaxBlockZ()).spliterator(), false).filter(fx -> this.getBlockState(fx).getLightEmission() != 0);
    }
    
    public TickList<Block> getBlockTicks() {
        return this.blockTicks;
    }
    
    public TickList<Fluid> getLiquidTicks() {
        return this.liquidTicks;
    }
    
    public void setUnsaved(final boolean boolean1) {
        this.unsaved = boolean1;
    }
    
    public boolean isUnsaved() {
        return this.unsaved || (this.lastSaveHadEntities && this.level.getGameTime() != this.lastSaveTime);
    }
    
    public void setLastSaveHadEntities(final boolean boolean1) {
        this.lastSaveHadEntities = boolean1;
    }
    
    public void setLastSaveTime(final long long1) {
        this.lastSaveTime = long1;
    }
    
    @Nullable
    public StructureStart<?> getStartForFeature(final StructureFeature<?> ckx) {
        return this.structureStarts.get(ckx);
    }
    
    public void setStartForFeature(final StructureFeature<?> ckx, final StructureStart<?> crs) {
        this.structureStarts.put(ckx, crs);
    }
    
    public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
        return this.structureStarts;
    }
    
    public void setAllStarts(final Map<StructureFeature<?>, StructureStart<?>> map) {
        this.structureStarts.clear();
        this.structureStarts.putAll((Map)map);
    }
    
    public LongSet getReferencesForFeature(final StructureFeature<?> ckx) {
        return (LongSet)this.structuresRefences.computeIfAbsent(ckx, ckx -> new LongOpenHashSet());
    }
    
    public void addReferenceForFeature(final StructureFeature<?> ckx, final long long2) {
        ((LongSet)this.structuresRefences.computeIfAbsent(ckx, ckx -> new LongOpenHashSet())).add(long2);
    }
    
    public Map<StructureFeature<?>, LongSet> getAllReferences() {
        return this.structuresRefences;
    }
    
    public void setAllReferences(final Map<StructureFeature<?>, LongSet> map) {
        this.structuresRefences.clear();
        this.structuresRefences.putAll((Map)map);
    }
    
    public long getInhabitedTime() {
        return this.inhabitedTime;
    }
    
    public void setInhabitedTime(final long long1) {
        this.inhabitedTime = long1;
    }
    
    public void postProcessGeneration() {
        final ChunkPos bra2 = this.getPos();
        for (int integer3 = 0; integer3 < this.postProcessing.length; ++integer3) {
            if (this.postProcessing[integer3] != null) {
                for (final Short short5 : this.postProcessing[integer3]) {
                    final BlockPos fx6 = ProtoChunk.unpackOffsetCoordinates(short5, integer3, bra2);
                    final BlockState cee7 = this.getBlockState(fx6);
                    final BlockState cee8 = Block.updateFromNeighbourShapes(cee7, this.level, fx6);
                    this.level.setBlock(fx6, cee8, 20);
                }
                this.postProcessing[integer3].clear();
            }
        }
        this.unpackTicks();
        for (final BlockPos fx7 : Sets.newHashSet((Iterable)this.pendingBlockEntities.keySet())) {
            this.getBlockEntity(fx7);
        }
        this.pendingBlockEntities.clear();
        this.upgradeData.upgrade(this);
    }
    
    @Nullable
    private BlockEntity promotePendingBlockEntity(final BlockPos fx, final CompoundTag md) {
        final BlockState cee5 = this.getBlockState(fx);
        BlockEntity ccg4;
        if ("DUMMY".equals(md.getString("id"))) {
            final Block bul6 = cee5.getBlock();
            if (bul6 instanceof EntityBlock) {
                ccg4 = ((EntityBlock)bul6).newBlockEntity(this.level);
            }
            else {
                ccg4 = null;
                LevelChunk.LOGGER.warn("Tried to load a DUMMY block entity @ {} but found not block entity block {} at location", fx, cee5);
            }
        }
        else {
            ccg4 = BlockEntity.loadStatic(cee5, md);
        }
        if (ccg4 != null) {
            ccg4.setLevelAndPosition(this.level, fx);
            this.addBlockEntity(ccg4);
        }
        else {
            LevelChunk.LOGGER.warn("Tried to load a block entity for block {} but failed at location {}", cee5, fx);
        }
        return ccg4;
    }
    
    public UpgradeData getUpgradeData() {
        return this.upgradeData;
    }
    
    public ShortList[] getPostProcessing() {
        return this.postProcessing;
    }
    
    public void unpackTicks() {
        if (this.blockTicks instanceof ProtoTickList) {
            ((ProtoTickList)this.blockTicks).copyOut(this.level.getBlockTicks(), fx -> this.getBlockState(fx).getBlock());
            this.blockTicks = EmptyTickList.empty();
        }
        else if (this.blockTicks instanceof ChunkTickList) {
            ((ChunkTickList)this.blockTicks).copyOut(this.level.getBlockTicks());
            this.blockTicks = EmptyTickList.empty();
        }
        if (this.liquidTicks instanceof ProtoTickList) {
            ((ProtoTickList)this.liquidTicks).copyOut(this.level.getLiquidTicks(), fx -> this.getFluidState(fx).getType());
            this.liquidTicks = EmptyTickList.empty();
        }
        else if (this.liquidTicks instanceof ChunkTickList) {
            ((ChunkTickList)this.liquidTicks).copyOut(this.level.getLiquidTicks());
            this.liquidTicks = EmptyTickList.empty();
        }
    }
    
    public void packTicks(final ServerLevel aag) {
        if (this.blockTicks == EmptyTickList.empty()) {
            this.blockTicks = new ChunkTickList<Block>((java.util.function.Function<Block, ResourceLocation>)Registry.BLOCK::getKey, aag.getBlockTicks().fetchTicksInChunk(this.chunkPos, true, false), aag.getGameTime());
            this.setUnsaved(true);
        }
        if (this.liquidTicks == EmptyTickList.empty()) {
            this.liquidTicks = new ChunkTickList<Fluid>((java.util.function.Function<Fluid, ResourceLocation>)Registry.FLUID::getKey, aag.getLiquidTicks().fetchTicksInChunk(this.chunkPos, true, false), aag.getGameTime());
            this.setUnsaved(true);
        }
    }
    
    public ChunkStatus getStatus() {
        return ChunkStatus.FULL;
    }
    
    public ChunkHolder.FullChunkStatus getFullStatus() {
        if (this.fullStatus == null) {
            return ChunkHolder.FullChunkStatus.BORDER;
        }
        return (ChunkHolder.FullChunkStatus)this.fullStatus.get();
    }
    
    public void setFullStatus(final Supplier<ChunkHolder.FullChunkStatus> supplier) {
        this.fullStatus = supplier;
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
        EMPTY_SECTION = null;
    }
    
    public enum EntityCreationType {
        IMMEDIATE, 
        QUEUED, 
        CHECK;
    }
}
