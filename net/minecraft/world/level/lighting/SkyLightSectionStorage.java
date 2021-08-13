package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.world.level.chunk.DataLayer;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.Direction;

public class SkyLightSectionStorage extends LayerLightSectionStorage<SkyDataLayerStorageMap> {
    private static final Direction[] HORIZONTALS;
    private final LongSet sectionsWithSources;
    private final LongSet sectionsToAddSourcesTo;
    private final LongSet sectionsToRemoveSourcesFrom;
    private final LongSet columnsWithSkySources;
    private volatile boolean hasSourceInconsistencies;
    
    protected SkyLightSectionStorage(final LightChunkGetter cgg) {
        super(LightLayer.SKY, cgg, new SkyDataLayerStorageMap((Long2ObjectOpenHashMap<DataLayer>)new Long2ObjectOpenHashMap(), new Long2IntOpenHashMap(), Integer.MAX_VALUE));
        this.sectionsWithSources = (LongSet)new LongOpenHashSet();
        this.sectionsToAddSourcesTo = (LongSet)new LongOpenHashSet();
        this.sectionsToRemoveSourcesFrom = (LongSet)new LongOpenHashSet();
        this.columnsWithSkySources = (LongSet)new LongOpenHashSet();
    }
    
    @Override
    protected int getLightValue(long long1) {
        long long2 = SectionPos.blockToSection(long1);
        int integer6 = SectionPos.y(long2);
        final SkyDataLayerStorageMap a7 = (SkyDataLayerStorageMap)this.visibleSectionData;
        final int integer7 = a7.topSections.get(SectionPos.getZeroNode(long2));
        if (integer7 == a7.currentLowestY || integer6 >= integer7) {
            return 15;
        }
        DataLayer cfy9 = this.getDataLayer(a7, long2);
        if (cfy9 == null) {
            long1 = BlockPos.getFlatIndex(long1);
            while (cfy9 == null) {
                long2 = SectionPos.offset(long2, Direction.UP);
                if (++integer6 >= integer7) {
                    return 15;
                }
                long1 = BlockPos.offset(long1, 0, 16, 0);
                cfy9 = this.getDataLayer(a7, long2);
            }
        }
        return cfy9.get(SectionPos.sectionRelative(BlockPos.getX(long1)), SectionPos.sectionRelative(BlockPos.getY(long1)), SectionPos.sectionRelative(BlockPos.getZ(long1)));
    }
    
    @Override
    protected void onNodeAdded(final long long1) {
        final int integer4 = SectionPos.y(long1);
        if (((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY > integer4) {
            ((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY = integer4;
            ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.defaultReturnValue(((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY);
        }
        final long long2 = SectionPos.getZeroNode(long1);
        final int integer5 = ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.get(long2);
        if (integer5 < integer4 + 1) {
            ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.put(long2, integer4 + 1);
            if (this.columnsWithSkySources.contains(long2)) {
                this.queueAddSource(long1);
                if (integer5 > ((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY) {
                    final long long3 = SectionPos.asLong(SectionPos.x(long1), integer5 - 1, SectionPos.z(long1));
                    this.queueRemoveSource(long3);
                }
                this.recheckInconsistencyFlag();
            }
        }
    }
    
    private void queueRemoveSource(final long long1) {
        this.sectionsToRemoveSourcesFrom.add(long1);
        this.sectionsToAddSourcesTo.remove(long1);
    }
    
    private void queueAddSource(final long long1) {
        this.sectionsToAddSourcesTo.add(long1);
        this.sectionsToRemoveSourcesFrom.remove(long1);
    }
    
    private void recheckInconsistencyFlag() {
        this.hasSourceInconsistencies = (!this.sectionsToAddSourcesTo.isEmpty() || !this.sectionsToRemoveSourcesFrom.isEmpty());
    }
    
    @Override
    protected void onNodeRemoved(final long long1) {
        final long long2 = SectionPos.getZeroNode(long1);
        final boolean boolean6 = this.columnsWithSkySources.contains(long2);
        if (boolean6) {
            this.queueRemoveSource(long1);
        }
        int integer7 = SectionPos.y(long1);
        if (((SkyDataLayerStorageMap)this.updatingSectionData).topSections.get(long2) == integer7 + 1) {
            long long3;
            for (long3 = long1; !this.storingLightForSection(long3) && this.hasSectionsBelow(integer7); --integer7, long3 = SectionPos.offset(long3, Direction.DOWN)) {}
            if (this.storingLightForSection(long3)) {
                ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.put(long2, integer7 + 1);
                if (boolean6) {
                    this.queueAddSource(long3);
                }
            }
            else {
                ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.remove(long2);
            }
        }
        if (boolean6) {
            this.recheckInconsistencyFlag();
        }
    }
    
    @Override
    protected void enableLightSources(final long long1, final boolean boolean2) {
        this.runAllUpdates();
        if (boolean2 && this.columnsWithSkySources.add(long1)) {
            final int integer5 = ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.get(long1);
            if (integer5 != ((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY) {
                final long long2 = SectionPos.asLong(SectionPos.x(long1), integer5 - 1, SectionPos.z(long1));
                this.queueAddSource(long2);
                this.recheckInconsistencyFlag();
            }
        }
        else if (!boolean2) {
            this.columnsWithSkySources.remove(long1);
        }
    }
    
    @Override
    protected boolean hasInconsistencies() {
        return super.hasInconsistencies() || this.hasSourceInconsistencies;
    }
    
    @Override
    protected DataLayer createDataLayer(final long long1) {
        final DataLayer cfy4 = (DataLayer)this.queuedSections.get(long1);
        if (cfy4 != null) {
            return cfy4;
        }
        long long2 = SectionPos.offset(long1, Direction.UP);
        final int integer7 = ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.get(SectionPos.getZeroNode(long1));
        if (integer7 == ((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY || SectionPos.y(long2) >= integer7) {
            return new DataLayer();
        }
        DataLayer cfy5;
        while ((cfy5 = this.getDataLayer(long2, true)) == null) {
            long2 = SectionPos.offset(long2, Direction.UP);
        }
        return new DataLayer(new FlatDataLayer(cfy5, 0).getData());
    }
    
    @Override
    protected void markNewInconsistencies(final LayerLightEngine<SkyDataLayerStorageMap, ?> cui, final boolean boolean2, final boolean boolean3) {
        super.markNewInconsistencies(cui, boolean2, boolean3);
        if (!boolean2) {
            return;
        }
        if (!this.sectionsToAddSourcesTo.isEmpty()) {
            for (final long long6 : this.sectionsToAddSourcesTo) {
                final int integer8 = this.getLevel(long6);
                if (integer8 == 2) {
                    continue;
                }
                if (this.sectionsToRemoveSourcesFrom.contains(long6) || !this.sectionsWithSources.add(long6)) {
                    continue;
                }
                if (integer8 == 1) {
                    this.clearQueuedSectionBlocks(cui, long6);
                    if (this.changedSections.add(long6)) {
                        ((SkyDataLayerStorageMap)this.updatingSectionData).copyDataLayer(long6);
                    }
                    Arrays.fill(this.getDataLayer(long6, true).getData(), (byte)(-1));
                    final int integer9 = SectionPos.sectionToBlockCoord(SectionPos.x(long6));
                    final int integer10 = SectionPos.sectionToBlockCoord(SectionPos.y(long6));
                    final int integer11 = SectionPos.sectionToBlockCoord(SectionPos.z(long6));
                    for (final Direction gc15 : SkyLightSectionStorage.HORIZONTALS) {
                        final long long7 = SectionPos.offset(long6, gc15);
                        if (this.sectionsToRemoveSourcesFrom.contains(long7) || (!this.sectionsWithSources.contains(long7) && !this.sectionsToAddSourcesTo.contains(long7))) {
                            if (this.storingLightForSection(long7)) {
                                for (int integer12 = 0; integer12 < 16; ++integer12) {
                                    for (int integer13 = 0; integer13 < 16; ++integer13) {
                                        long long8 = 0L;
                                        long long9 = 0L;
                                        switch (gc15) {
                                            case NORTH: {
                                                long8 = BlockPos.asLong(integer9 + integer12, integer10 + integer13, integer11);
                                                long9 = BlockPos.asLong(integer9 + integer12, integer10 + integer13, integer11 - 1);
                                                break;
                                            }
                                            case SOUTH: {
                                                long8 = BlockPos.asLong(integer9 + integer12, integer10 + integer13, integer11 + 16 - 1);
                                                long9 = BlockPos.asLong(integer9 + integer12, integer10 + integer13, integer11 + 16);
                                                break;
                                            }
                                            case WEST: {
                                                long8 = BlockPos.asLong(integer9, integer10 + integer12, integer11 + integer13);
                                                long9 = BlockPos.asLong(integer9 - 1, integer10 + integer12, integer11 + integer13);
                                                break;
                                            }
                                            default: {
                                                long8 = BlockPos.asLong(integer9 + 16 - 1, integer10 + integer12, integer11 + integer13);
                                                long9 = BlockPos.asLong(integer9 + 16, integer10 + integer12, integer11 + integer13);
                                                break;
                                            }
                                        }
                                        cui.checkEdge(long8, long9, cui.computeLevelFromNeighbor(long8, long9, 0), true);
                                    }
                                }
                            }
                        }
                    }
                    for (int integer14 = 0; integer14 < 16; ++integer14) {
                        for (int integer15 = 0; integer15 < 16; ++integer15) {
                            final long long10 = BlockPos.asLong(SectionPos.sectionToBlockCoord(SectionPos.x(long6)) + integer14, SectionPos.sectionToBlockCoord(SectionPos.y(long6)), SectionPos.sectionToBlockCoord(SectionPos.z(long6)) + integer15);
                            final long long7 = BlockPos.asLong(SectionPos.sectionToBlockCoord(SectionPos.x(long6)) + integer14, SectionPos.sectionToBlockCoord(SectionPos.y(long6)) - 1, SectionPos.sectionToBlockCoord(SectionPos.z(long6)) + integer15);
                            cui.checkEdge(long10, long7, cui.computeLevelFromNeighbor(long10, long7, 0), true);
                        }
                    }
                }
                else {
                    for (int integer9 = 0; integer9 < 16; ++integer9) {
                        for (int integer10 = 0; integer10 < 16; ++integer10) {
                            final long long11 = BlockPos.asLong(SectionPos.sectionToBlockCoord(SectionPos.x(long6)) + integer9, SectionPos.sectionToBlockCoord(SectionPos.y(long6)) + 16 - 1, SectionPos.sectionToBlockCoord(SectionPos.z(long6)) + integer10);
                            cui.checkEdge(Long.MAX_VALUE, long11, 0, true);
                        }
                    }
                }
            }
        }
        this.sectionsToAddSourcesTo.clear();
        if (!this.sectionsToRemoveSourcesFrom.isEmpty()) {
            for (final long long6 : this.sectionsToRemoveSourcesFrom) {
                if (this.sectionsWithSources.remove(long6)) {
                    if (!this.storingLightForSection(long6)) {
                        continue;
                    }
                    for (int integer8 = 0; integer8 < 16; ++integer8) {
                        for (int integer9 = 0; integer9 < 16; ++integer9) {
                            final long long12 = BlockPos.asLong(SectionPos.sectionToBlockCoord(SectionPos.x(long6)) + integer8, SectionPos.sectionToBlockCoord(SectionPos.y(long6)) + 16 - 1, SectionPos.sectionToBlockCoord(SectionPos.z(long6)) + integer9);
                            cui.checkEdge(Long.MAX_VALUE, long12, 15, false);
                        }
                    }
                }
            }
        }
        this.sectionsToRemoveSourcesFrom.clear();
        this.hasSourceInconsistencies = false;
    }
    
    protected boolean hasSectionsBelow(final int integer) {
        return integer >= ((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY;
    }
    
    protected boolean hasLightSource(final long long1) {
        final int integer4 = BlockPos.getY(long1);
        if ((integer4 & 0xF) != 0xF) {
            return false;
        }
        final long long2 = SectionPos.blockToSection(long1);
        final long long3 = SectionPos.getZeroNode(long2);
        if (!this.columnsWithSkySources.contains(long3)) {
            return false;
        }
        final int integer5 = ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.get(long3);
        return SectionPos.sectionToBlockCoord(integer5) == integer4 + 16;
    }
    
    protected boolean isAboveData(final long long1) {
        final long long2 = SectionPos.getZeroNode(long1);
        final int integer6 = ((SkyDataLayerStorageMap)this.updatingSectionData).topSections.get(long2);
        return integer6 == ((SkyDataLayerStorageMap)this.updatingSectionData).currentLowestY || SectionPos.y(long1) >= integer6;
    }
    
    protected boolean lightOnInSection(final long long1) {
        final long long2 = SectionPos.getZeroNode(long1);
        return this.columnsWithSkySources.contains(long2);
    }
    
    static {
        HORIZONTALS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
    }
    
    public static final class SkyDataLayerStorageMap extends DataLayerStorageMap<SkyDataLayerStorageMap> {
        private int currentLowestY;
        private final Long2IntOpenHashMap topSections;
        
        public SkyDataLayerStorageMap(final Long2ObjectOpenHashMap<DataLayer> long2ObjectOpenHashMap, final Long2IntOpenHashMap long2IntOpenHashMap, final int integer) {
            super(long2ObjectOpenHashMap);
            (this.topSections = long2IntOpenHashMap).defaultReturnValue(integer);
            this.currentLowestY = integer;
        }
        
        @Override
        public SkyDataLayerStorageMap copy() {
            return new SkyDataLayerStorageMap((Long2ObjectOpenHashMap<DataLayer>)this.map.clone(), this.topSections.clone(), this.currentLowestY);
        }
    }
}
