package net.minecraft.world.level.lighting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.DataLayer;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public class BlockLightSectionStorage extends LayerLightSectionStorage<BlockDataLayerStorageMap> {
    protected BlockLightSectionStorage(final LightChunkGetter cgg) {
        super(LightLayer.BLOCK, cgg, new BlockDataLayerStorageMap((Long2ObjectOpenHashMap<DataLayer>)new Long2ObjectOpenHashMap()));
    }
    
    @Override
    protected int getLightValue(final long long1) {
        final long long2 = SectionPos.blockToSection(long1);
        final DataLayer cfy6 = this.getDataLayer(long2, false);
        if (cfy6 == null) {
            return 0;
        }
        return cfy6.get(SectionPos.sectionRelative(BlockPos.getX(long1)), SectionPos.sectionRelative(BlockPos.getY(long1)), SectionPos.sectionRelative(BlockPos.getZ(long1)));
    }
    
    public static final class BlockDataLayerStorageMap extends DataLayerStorageMap<BlockDataLayerStorageMap> {
        public BlockDataLayerStorageMap(final Long2ObjectOpenHashMap<DataLayer> long2ObjectOpenHashMap) {
            super(long2ObjectOpenHashMap);
        }
        
        @Override
        public BlockDataLayerStorageMap copy() {
            return new BlockDataLayerStorageMap((Long2ObjectOpenHashMap<DataLayer>)this.map.clone());
        }
    }
}
