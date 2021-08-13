package net.minecraft.world.level.levelgen.structure;

import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.HashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.ListTag;
import java.util.function.Supplier;
import java.io.IOException;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.nbt.Tag;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.Locale;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.ChunkPos;
import java.util.Iterator;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.DimensionDataStorage;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.Map;

public class LegacyStructureDataHandler {
    private static final Map<String, String> CURRENT_TO_LEGACY_MAP;
    private static final Map<String, String> LEGACY_TO_CURRENT_MAP;
    private final boolean hasLegacyData;
    private final Map<String, Long2ObjectMap<CompoundTag>> dataMap;
    private final Map<String, StructureFeatureIndexSavedData> indexMap;
    private final List<String> legacyKeys;
    private final List<String> currentKeys;
    
    public LegacyStructureDataHandler(@Nullable final DimensionDataStorage cxz, final List<String> list2, final List<String> list3) {
        this.dataMap = (Map<String, Long2ObjectMap<CompoundTag>>)Maps.newHashMap();
        this.indexMap = (Map<String, StructureFeatureIndexSavedData>)Maps.newHashMap();
        this.legacyKeys = list2;
        this.currentKeys = list3;
        this.populateCaches(cxz);
        boolean boolean5 = false;
        for (final String string7 : this.currentKeys) {
            boolean5 |= (this.dataMap.get(string7) != null);
        }
        this.hasLegacyData = boolean5;
    }
    
    public void removeIndex(final long long1) {
        for (final String string5 : this.legacyKeys) {
            final StructureFeatureIndexSavedData crq6 = (StructureFeatureIndexSavedData)this.indexMap.get(string5);
            if (crq6 != null && crq6.hasUnhandledIndex(long1)) {
                crq6.removeIndex(long1);
                crq6.setDirty();
            }
        }
    }
    
    public CompoundTag updateFromLegacy(CompoundTag md) {
        final CompoundTag md2 = md.getCompound("Level");
        final ChunkPos bra4 = new ChunkPos(md2.getInt("xPos"), md2.getInt("zPos"));
        if (this.isUnhandledStructureStart(bra4.x, bra4.z)) {
            md = this.updateStructureStart(md, bra4);
        }
        final CompoundTag md3 = md2.getCompound("Structures");
        final CompoundTag md4 = md3.getCompound("References");
        for (final String string8 : this.currentKeys) {
            final StructureFeature<?> ckx9 = StructureFeature.STRUCTURES_REGISTRY.get(string8.toLowerCase(Locale.ROOT));
            if (!md4.contains(string8, 12)) {
                if (ckx9 == null) {
                    continue;
                }
                final int integer10 = 8;
                final LongList longList11 = (LongList)new LongArrayList();
                for (int integer11 = bra4.x - 8; integer11 <= bra4.x + 8; ++integer11) {
                    for (int integer12 = bra4.z - 8; integer12 <= bra4.z + 8; ++integer12) {
                        if (this.hasLegacyStart(integer11, integer12, string8)) {
                            longList11.add(ChunkPos.asLong(integer11, integer12));
                        }
                    }
                }
                md4.putLongArray(string8, (List<Long>)longList11);
            }
        }
        md3.put("References", (Tag)md4);
        md2.put("Structures", (Tag)md3);
        md.put("Level", (Tag)md2);
        return md;
    }
    
    private boolean hasLegacyStart(final int integer1, final int integer2, final String string) {
        return this.hasLegacyData && (this.dataMap.get(string) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(LegacyStructureDataHandler.CURRENT_TO_LEGACY_MAP.get(string))).hasStartIndex(ChunkPos.asLong(integer1, integer2)));
    }
    
    private boolean isUnhandledStructureStart(final int integer1, final int integer2) {
        if (!this.hasLegacyData) {
            return false;
        }
        for (final String string5 : this.currentKeys) {
            if (this.dataMap.get(string5) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(LegacyStructureDataHandler.CURRENT_TO_LEGACY_MAP.get(string5))).hasUnhandledIndex(ChunkPos.asLong(integer1, integer2))) {
                return true;
            }
        }
        return false;
    }
    
    private CompoundTag updateStructureStart(final CompoundTag md, final ChunkPos bra) {
        final CompoundTag md2 = md.getCompound("Level");
        final CompoundTag md3 = md2.getCompound("Structures");
        final CompoundTag md4 = md3.getCompound("Starts");
        for (final String string8 : this.currentKeys) {
            final Long2ObjectMap<CompoundTag> long2ObjectMap9 = (Long2ObjectMap<CompoundTag>)this.dataMap.get(string8);
            if (long2ObjectMap9 == null) {
                continue;
            }
            final long long10 = bra.toLong();
            if (!((StructureFeatureIndexSavedData)this.indexMap.get(LegacyStructureDataHandler.CURRENT_TO_LEGACY_MAP.get(string8))).hasUnhandledIndex(long10)) {
                continue;
            }
            final CompoundTag md5 = (CompoundTag)long2ObjectMap9.get(long10);
            if (md5 == null) {
                continue;
            }
            md4.put(string8, md5);
        }
        md3.put("Starts", (Tag)md4);
        md2.put("Structures", (Tag)md3);
        md.put("Level", (Tag)md2);
        return md;
    }
    
    private void populateCaches(@Nullable final DimensionDataStorage cxz) {
        if (cxz == null) {
            return;
        }
        for (final String string4 : this.legacyKeys) {
            CompoundTag md5 = new CompoundTag();
            try {
                md5 = cxz.readTagFromDisk(string4, 1493).getCompound("data").getCompound("Features");
                if (md5.isEmpty()) {
                    continue;
                }
            }
            catch (IOException ex) {}
            for (final String string5 : md5.getAllKeys()) {
                final CompoundTag md6 = md5.getCompound(string5);
                final long long9 = ChunkPos.asLong(md6.getInt("ChunkX"), md6.getInt("ChunkZ"));
                final ListTag mj11 = md6.getList("Children", 10);
                if (!mj11.isEmpty()) {
                    final String string6 = mj11.getCompound(0).getString("id");
                    final String string7 = (String)LegacyStructureDataHandler.LEGACY_TO_CURRENT_MAP.get(string6);
                    if (string7 != null) {
                        md6.putString("id", string7);
                    }
                }
                final String string6 = md6.getString("id");
                ((Long2ObjectMap)this.dataMap.computeIfAbsent(string6, string -> new Long2ObjectOpenHashMap())).put(long9, md6);
            }
            final String string8 = string4 + "_index";
            final StructureFeatureIndexSavedData crq7 = cxz.<StructureFeatureIndexSavedData>computeIfAbsent((java.util.function.Supplier<StructureFeatureIndexSavedData>)(() -> new StructureFeatureIndexSavedData(string8)), string8);
            if (crq7.getAll().isEmpty()) {
                final StructureFeatureIndexSavedData crq8 = new StructureFeatureIndexSavedData(string8);
                this.indexMap.put(string4, crq8);
                for (final String string9 : md5.getAllKeys()) {
                    final CompoundTag md7 = md5.getCompound(string9);
                    crq8.addIndex(ChunkPos.asLong(md7.getInt("ChunkX"), md7.getInt("ChunkZ")));
                }
                crq8.setDirty();
            }
            else {
                this.indexMap.put(string4, crq7);
            }
        }
    }
    
    public static LegacyStructureDataHandler getLegacyStructureHandler(final ResourceKey<Level> vj, @Nullable final DimensionDataStorage cxz) {
        if (vj == Level.OVERWORLD) {
            return new LegacyStructureDataHandler(cxz, (List<String>)ImmutableList.of("Monument", "Stronghold", "Village", "Mineshaft", "Temple", "Mansion"), (List<String>)ImmutableList.of("Village", "Mineshaft", "Mansion", "Igloo", "Desert_Pyramid", "Jungle_Pyramid", "Swamp_Hut", "Stronghold", "Monument"));
        }
        if (vj == Level.NETHER) {
            final List<String> list3 = (List<String>)ImmutableList.of("Fortress");
            return new LegacyStructureDataHandler(cxz, list3, list3);
        }
        if (vj == Level.END) {
            final List<String> list3 = (List<String>)ImmutableList.of("EndCity");
            return new LegacyStructureDataHandler(cxz, list3, list3);
        }
        throw new RuntimeException(String.format("Unknown dimension type : %s", new Object[] { vj }));
    }
    
    static {
        CURRENT_TO_LEGACY_MAP = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("Village", "Village");
            hashMap.put("Mineshaft", "Mineshaft");
            hashMap.put("Mansion", "Mansion");
            hashMap.put("Igloo", "Temple");
            hashMap.put("Desert_Pyramid", "Temple");
            hashMap.put("Jungle_Pyramid", "Temple");
            hashMap.put("Swamp_Hut", "Temple");
            hashMap.put("Stronghold", "Stronghold");
            hashMap.put("Monument", "Monument");
            hashMap.put("Fortress", "Fortress");
            hashMap.put("EndCity", "EndCity");
        }));
        LEGACY_TO_CURRENT_MAP = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("Iglu", "Igloo");
            hashMap.put("TeDP", "Desert_Pyramid");
            hashMap.put("TeJP", "Jungle_Pyramid");
            hashMap.put("TeSH", "Swamp_Hut");
        }));
    }
}
