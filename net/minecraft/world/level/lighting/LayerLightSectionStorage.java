package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.server.level.SectionTracker;

public abstract class LayerLightSectionStorage<M extends DataLayerStorageMap<M>> extends SectionTracker {
    protected static final DataLayer EMPTY_DATA;
    private static final Direction[] DIRECTIONS;
    private final LightLayer layer;
    private final LightChunkGetter chunkSource;
    protected final LongSet dataSectionSet;
    protected final LongSet toMarkNoData;
    protected final LongSet toMarkData;
    protected volatile M visibleSectionData;
    protected final M updatingSectionData;
    protected final LongSet changedSections;
    protected final LongSet sectionsAffectedByLightUpdates;
    protected final Long2ObjectMap<DataLayer> queuedSections;
    private final LongSet untrustedSections;
    private final LongSet columnsToRetainQueuedDataFor;
    private final LongSet toRemove;
    protected volatile boolean hasToRemove;
    
    protected LayerLightSectionStorage(final LightLayer bsc, final LightChunkGetter cgg, final M cuf) {
        super(3, 16, 256);
        this.dataSectionSet = (LongSet)new LongOpenHashSet();
        this.toMarkNoData = (LongSet)new LongOpenHashSet();
        this.toMarkData = (LongSet)new LongOpenHashSet();
        this.changedSections = (LongSet)new LongOpenHashSet();
        this.sectionsAffectedByLightUpdates = (LongSet)new LongOpenHashSet();
        this.queuedSections = (Long2ObjectMap<DataLayer>)Long2ObjectMaps.synchronize((Long2ObjectMap)new Long2ObjectOpenHashMap());
        this.untrustedSections = (LongSet)new LongOpenHashSet();
        this.columnsToRetainQueuedDataFor = (LongSet)new LongOpenHashSet();
        this.toRemove = (LongSet)new LongOpenHashSet();
        this.layer = bsc;
        this.chunkSource = cgg;
        this.updatingSectionData = cuf;
        (this.visibleSectionData = cuf.copy()).disableCache();
    }
    
    protected boolean storingLightForSection(final long long1) {
        return this.getDataLayer(long1, true) != null;
    }
    
    @Nullable
    protected DataLayer getDataLayer(final long long1, final boolean boolean2) {
        return this.getDataLayer(boolean2 ? this.updatingSectionData : this.visibleSectionData, long1);
    }
    
    @Nullable
    protected DataLayer getDataLayer(final M cuf, final long long2) {
        return cuf.getLayer(long2);
    }
    
    @Nullable
    public DataLayer getDataLayerData(final long long1) {
        final DataLayer cfy4 = (DataLayer)this.queuedSections.get(long1);
        if (cfy4 != null) {
            return cfy4;
        }
        return this.getDataLayer(long1, false);
    }
    
    protected abstract int getLightValue(final long long1);
    
    protected int getStoredLevel(final long long1) {
        final long long2 = SectionPos.blockToSection(long1);
        final DataLayer cfy6 = this.getDataLayer(long2, true);
        return cfy6.get(SectionPos.sectionRelative(BlockPos.getX(long1)), SectionPos.sectionRelative(BlockPos.getY(long1)), SectionPos.sectionRelative(BlockPos.getZ(long1)));
    }
    
    protected void setStoredLevel(final long long1, final int integer) {
        final long long2 = SectionPos.blockToSection(long1);
        if (this.changedSections.add(long2)) {
            this.updatingSectionData.copyDataLayer(long2);
        }
        final DataLayer cfy7 = this.getDataLayer(long2, true);
        cfy7.set(SectionPos.sectionRelative(BlockPos.getX(long1)), SectionPos.sectionRelative(BlockPos.getY(long1)), SectionPos.sectionRelative(BlockPos.getZ(long1)), integer);
        for (int integer2 = -1; integer2 <= 1; ++integer2) {
            for (int integer3 = -1; integer3 <= 1; ++integer3) {
                for (int integer4 = -1; integer4 <= 1; ++integer4) {
                    this.sectionsAffectedByLightUpdates.add(SectionPos.blockToSection(BlockPos.offset(long1, integer3, integer4, integer2)));
                }
            }
        }
    }
    
    @Override
    protected int getLevel(final long long1) {
        if (long1 == Long.MAX_VALUE) {
            return 2;
        }
        if (this.dataSectionSet.contains(long1)) {
            return 0;
        }
        if (!this.toRemove.contains(long1) && this.updatingSectionData.hasLayer(long1)) {
            return 1;
        }
        return 2;
    }
    
    @Override
    protected int getLevelFromSource(final long long1) {
        if (this.toMarkNoData.contains(long1)) {
            return 2;
        }
        if (this.dataSectionSet.contains(long1) || this.toMarkData.contains(long1)) {
            return 0;
        }
        return 2;
    }
    
    @Override
    protected void setLevel(final long long1, final int integer) {
        final int integer2 = this.getLevel(long1);
        if (integer2 != 0 && integer == 0) {
            this.dataSectionSet.add(long1);
            this.toMarkData.remove(long1);
        }
        if (integer2 == 0 && integer != 0) {
            this.dataSectionSet.remove(long1);
            this.toMarkNoData.remove(long1);
        }
        if (integer2 >= 2 && integer != 2) {
            if (this.toRemove.contains(long1)) {
                this.toRemove.remove(long1);
            }
            else {
                this.updatingSectionData.setLayer(long1, this.createDataLayer(long1));
                this.changedSections.add(long1);
                this.onNodeAdded(long1);
                for (int integer3 = -1; integer3 <= 1; ++integer3) {
                    for (int integer4 = -1; integer4 <= 1; ++integer4) {
                        for (int integer5 = -1; integer5 <= 1; ++integer5) {
                            this.sectionsAffectedByLightUpdates.add(SectionPos.blockToSection(BlockPos.offset(long1, integer4, integer5, integer3)));
                        }
                    }
                }
            }
        }
        if (integer2 != 2 && integer >= 2) {
            this.toRemove.add(long1);
        }
        this.hasToRemove = !this.toRemove.isEmpty();
    }
    
    protected DataLayer createDataLayer(final long long1) {
        final DataLayer cfy4 = (DataLayer)this.queuedSections.get(long1);
        if (cfy4 != null) {
            return cfy4;
        }
        return new DataLayer();
    }
    
    protected void clearQueuedSectionBlocks(final LayerLightEngine<?, ?> cui, final long long2) {
        if (cui.getQueueSize() < 8192) {
            cui.removeIf(long2 -> SectionPos.blockToSection(long2) == long2);
            return;
        }
        final int integer5 = SectionPos.sectionToBlockCoord(SectionPos.x(long2));
        final int integer6 = SectionPos.sectionToBlockCoord(SectionPos.y(long2));
        final int integer7 = SectionPos.sectionToBlockCoord(SectionPos.z(long2));
        for (int integer8 = 0; integer8 < 16; ++integer8) {
            for (int integer9 = 0; integer9 < 16; ++integer9) {
                for (int integer10 = 0; integer10 < 16; ++integer10) {
                    final long long3 = BlockPos.asLong(integer5 + integer8, integer6 + integer9, integer7 + integer10);
                    cui.removeFromQueue(long3);
                }
            }
        }
    }
    
    protected boolean hasInconsistencies() {
        return this.hasToRemove;
    }
    
    protected void markNewInconsistencies(final LayerLightEngine<M, ?> cui, final boolean boolean2, final boolean boolean3) {
        if (!this.hasInconsistencies() && this.queuedSections.isEmpty()) {
            return;
        }
        for (final long long6 : this.toRemove) {
            this.clearQueuedSectionBlocks(cui, long6);
            final DataLayer cfy8 = (DataLayer)this.queuedSections.remove(long6);
            final DataLayer cfy9 = this.updatingSectionData.removeLayer(long6);
            if (this.columnsToRetainQueuedDataFor.contains(SectionPos.getZeroNode(long6))) {
                if (cfy8 != null) {
                    this.queuedSections.put(long6, cfy8);
                }
                else {
                    if (cfy9 == null) {
                        continue;
                    }
                    this.queuedSections.put(long6, cfy9);
                }
            }
        }
        this.updatingSectionData.clearCache();
        for (final long long6 : this.toRemove) {
            this.onNodeRemoved(long6);
        }
        this.toRemove.clear();
        this.hasToRemove = false;
        for (final Long2ObjectMap.Entry<DataLayer> entry6 : this.queuedSections.long2ObjectEntrySet()) {
            final long long7 = entry6.getLongKey();
            if (!this.storingLightForSection(long7)) {
                continue;
            }
            final DataLayer cfy9 = (DataLayer)entry6.getValue();
            if (this.updatingSectionData.getLayer(long7) == cfy9) {
                continue;
            }
            this.clearQueuedSectionBlocks(cui, long7);
            this.updatingSectionData.setLayer(long7, cfy9);
            this.changedSections.add(long7);
        }
        this.updatingSectionData.clearCache();
        if (!boolean3) {
            for (final long long6 : this.queuedSections.keySet()) {
                this.checkEdgesForSection(cui, long6);
            }
        }
        else {
            for (final long long6 : this.untrustedSections) {
                this.checkEdgesForSection(cui, long6);
            }
        }
        this.untrustedSections.clear();
        final ObjectIterator<Long2ObjectMap.Entry<DataLayer>> objectIterator5 = (ObjectIterator<Long2ObjectMap.Entry<DataLayer>>)this.queuedSections.long2ObjectEntrySet().iterator();
        while (objectIterator5.hasNext()) {
            final Long2ObjectMap.Entry<DataLayer> entry6 = (Long2ObjectMap.Entry<DataLayer>)objectIterator5.next();
            final long long7 = entry6.getLongKey();
            if (this.storingLightForSection(long7)) {
                objectIterator5.remove();
            }
        }
    }
    
    private void checkEdgesForSection(final LayerLightEngine<M, ?> cui, final long long2) {
        if (!this.storingLightForSection(long2)) {
            return;
        }
        final int integer5 = SectionPos.sectionToBlockCoord(SectionPos.x(long2));
        final int integer6 = SectionPos.sectionToBlockCoord(SectionPos.y(long2));
        final int integer7 = SectionPos.sectionToBlockCoord(SectionPos.z(long2));
        for (final Direction gc11 : LayerLightSectionStorage.DIRECTIONS) {
            final long long3 = SectionPos.offset(long2, gc11);
            if (!this.queuedSections.containsKey(long3)) {
                if (this.storingLightForSection(long3)) {
                    for (int integer8 = 0; integer8 < 16; ++integer8) {
                        for (int integer9 = 0; integer9 < 16; ++integer9) {
                            long long4 = 0L;
                            long long5 = 0L;
                            switch (gc11) {
                                case DOWN: {
                                    long4 = BlockPos.asLong(integer5 + integer9, integer6, integer7 + integer8);
                                    long5 = BlockPos.asLong(integer5 + integer9, integer6 - 1, integer7 + integer8);
                                    break;
                                }
                                case UP: {
                                    long4 = BlockPos.asLong(integer5 + integer9, integer6 + 16 - 1, integer7 + integer8);
                                    long5 = BlockPos.asLong(integer5 + integer9, integer6 + 16, integer7 + integer8);
                                    break;
                                }
                                case NORTH: {
                                    long4 = BlockPos.asLong(integer5 + integer8, integer6 + integer9, integer7);
                                    long5 = BlockPos.asLong(integer5 + integer8, integer6 + integer9, integer7 - 1);
                                    break;
                                }
                                case SOUTH: {
                                    long4 = BlockPos.asLong(integer5 + integer8, integer6 + integer9, integer7 + 16 - 1);
                                    long5 = BlockPos.asLong(integer5 + integer8, integer6 + integer9, integer7 + 16);
                                    break;
                                }
                                case WEST: {
                                    long4 = BlockPos.asLong(integer5, integer6 + integer8, integer7 + integer9);
                                    long5 = BlockPos.asLong(integer5 - 1, integer6 + integer8, integer7 + integer9);
                                    break;
                                }
                                default: {
                                    long4 = BlockPos.asLong(integer5 + 16 - 1, integer6 + integer8, integer7 + integer9);
                                    long5 = BlockPos.asLong(integer5 + 16, integer6 + integer8, integer7 + integer9);
                                    break;
                                }
                            }
                            cui.checkEdge(long4, long5, cui.computeLevelFromNeighbor(long4, long5, cui.getLevel(long4)), false);
                            cui.checkEdge(long5, long4, cui.computeLevelFromNeighbor(long5, long4, cui.getLevel(long5)), false);
                        }
                    }
                }
            }
        }
    }
    
    protected void onNodeAdded(final long long1) {
    }
    
    protected void onNodeRemoved(final long long1) {
    }
    
    protected void enableLightSources(final long long1, final boolean boolean2) {
    }
    
    public void retainData(final long long1, final boolean boolean2) {
        if (boolean2) {
            this.columnsToRetainQueuedDataFor.add(long1);
        }
        else {
            this.columnsToRetainQueuedDataFor.remove(long1);
        }
    }
    
    protected void queueSectionData(final long long1, @Nullable final DataLayer cfy, final boolean boolean3) {
        if (cfy != null) {
            this.queuedSections.put(long1, cfy);
            if (!boolean3) {
                this.untrustedSections.add(long1);
            }
        }
        else {
            this.queuedSections.remove(long1);
        }
    }
    
    protected void updateSectionStatus(final long long1, final boolean boolean2) {
        final boolean boolean3 = this.dataSectionSet.contains(long1);
        if (!boolean3 && !boolean2) {
            this.toMarkData.add(long1);
            this.checkEdge(Long.MAX_VALUE, long1, 0, true);
        }
        if (boolean3 && boolean2) {
            this.toMarkNoData.add(long1);
            this.checkEdge(Long.MAX_VALUE, long1, 2, false);
        }
    }
    
    protected void runAllUpdates() {
        if (this.hasWork()) {
            this.runUpdates(Integer.MAX_VALUE);
        }
    }
    
    protected void swapSectionMap() {
        if (!this.changedSections.isEmpty()) {
            final M cuf2 = this.updatingSectionData.copy();
            cuf2.disableCache();
            this.visibleSectionData = cuf2;
            this.changedSections.clear();
        }
        if (!this.sectionsAffectedByLightUpdates.isEmpty()) {
            final LongIterator longIterator2 = this.sectionsAffectedByLightUpdates.iterator();
            while (longIterator2.hasNext()) {
                final long long3 = longIterator2.nextLong();
                this.chunkSource.onLightUpdate(this.layer, SectionPos.of(long3));
            }
            this.sectionsAffectedByLightUpdates.clear();
        }
    }
    
    static {
        EMPTY_DATA = new DataLayer();
        DIRECTIONS = Direction.values();
    }
}
