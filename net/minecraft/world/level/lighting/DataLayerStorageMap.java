package net.minecraft.world.level.lighting;

import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.chunk.DataLayer;

public abstract class DataLayerStorageMap<M extends DataLayerStorageMap<M>> {
    private final long[] lastSectionKeys;
    private final DataLayer[] lastSections;
    private boolean cacheEnabled;
    protected final Long2ObjectOpenHashMap<DataLayer> map;
    
    protected DataLayerStorageMap(final Long2ObjectOpenHashMap<DataLayer> long2ObjectOpenHashMap) {
        this.lastSectionKeys = new long[2];
        this.lastSections = new DataLayer[2];
        this.map = long2ObjectOpenHashMap;
        this.clearCache();
        this.cacheEnabled = true;
    }
    
    public abstract M copy();
    
    public void copyDataLayer(final long long1) {
        this.map.put(long1, ((DataLayer)this.map.get(long1)).copy());
        this.clearCache();
    }
    
    public boolean hasLayer(final long long1) {
        return this.map.containsKey(long1);
    }
    
    @Nullable
    public DataLayer getLayer(final long long1) {
        if (this.cacheEnabled) {
            for (int integer4 = 0; integer4 < 2; ++integer4) {
                if (long1 == this.lastSectionKeys[integer4]) {
                    return this.lastSections[integer4];
                }
            }
        }
        final DataLayer cfy4 = (DataLayer)this.map.get(long1);
        if (cfy4 != null) {
            if (this.cacheEnabled) {
                for (int integer5 = 1; integer5 > 0; --integer5) {
                    this.lastSectionKeys[integer5] = this.lastSectionKeys[integer5 - 1];
                    this.lastSections[integer5] = this.lastSections[integer5 - 1];
                }
                this.lastSectionKeys[0] = long1;
                this.lastSections[0] = cfy4;
            }
            return cfy4;
        }
        return null;
    }
    
    @Nullable
    public DataLayer removeLayer(final long long1) {
        return (DataLayer)this.map.remove(long1);
    }
    
    public void setLayer(final long long1, final DataLayer cfy) {
        this.map.put(long1, cfy);
    }
    
    public void clearCache() {
        for (int integer2 = 0; integer2 < 2; ++integer2) {
            this.lastSectionKeys[integer2] = Long.MAX_VALUE;
            this.lastSections[integer2] = null;
        }
    }
    
    public void disableCache() {
        this.cacheEnabled = false;
    }
}
