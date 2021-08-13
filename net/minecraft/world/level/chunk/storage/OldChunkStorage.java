package net.minecraft.world.level.chunk.storage;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.IdMap;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.chunk.OldDataLayer;
import net.minecraft.nbt.CompoundTag;

public class OldChunkStorage {
    public static OldLevelChunk load(final CompoundTag md) {
        final int integer2 = md.getInt("xPos");
        final int integer3 = md.getInt("zPos");
        final OldLevelChunk a4 = new OldLevelChunk(integer2, integer3);
        a4.blocks = md.getByteArray("Blocks");
        a4.data = new OldDataLayer(md.getByteArray("Data"), 7);
        a4.skyLight = new OldDataLayer(md.getByteArray("SkyLight"), 7);
        a4.blockLight = new OldDataLayer(md.getByteArray("BlockLight"), 7);
        a4.heightmap = md.getByteArray("HeightMap");
        a4.terrainPopulated = md.getBoolean("TerrainPopulated");
        a4.entities = md.getList("Entities", 10);
        a4.blockEntities = md.getList("TileEntities", 10);
        a4.blockTicks = md.getList("TileTicks", 10);
        try {
            a4.lastUpdated = md.getLong("LastUpdate");
        }
        catch (ClassCastException classCastException5) {
            a4.lastUpdated = md.getInt("LastUpdate");
        }
        return a4;
    }
    
    public static void convertToAnvilFormat(final RegistryAccess.RegistryHolder b, final OldLevelChunk a, final CompoundTag md, final BiomeSource bsv) {
        md.putInt("xPos", a.x);
        md.putInt("zPos", a.z);
        md.putLong("LastUpdate", a.lastUpdated);
        final int[] arr5 = new int[a.heightmap.length];
        for (int integer6 = 0; integer6 < a.heightmap.length; ++integer6) {
            arr5[integer6] = a.heightmap[integer6];
        }
        md.putIntArray("HeightMap", arr5);
        md.putBoolean("TerrainPopulated", a.terrainPopulated);
        final ListTag mj6 = new ListTag();
        for (int integer7 = 0; integer7 < 8; ++integer7) {
            boolean boolean8 = true;
            for (int integer8 = 0; integer8 < 16 && boolean8; ++integer8) {
                for (int integer9 = 0; integer9 < 16 && boolean8; ++integer9) {
                    for (int integer10 = 0; integer10 < 16; ++integer10) {
                        final int integer11 = integer8 << 11 | integer10 << 7 | integer9 + (integer7 << 4);
                        final int integer12 = a.blocks[integer11];
                        if (integer12 != 0) {
                            boolean8 = false;
                            break;
                        }
                    }
                }
            }
            if (!boolean8) {
                final byte[] arr6 = new byte[4096];
                final DataLayer cfy10 = new DataLayer();
                final DataLayer cfy11 = new DataLayer();
                final DataLayer cfy12 = new DataLayer();
                for (int integer12 = 0; integer12 < 16; ++integer12) {
                    for (int integer13 = 0; integer13 < 16; ++integer13) {
                        for (int integer14 = 0; integer14 < 16; ++integer14) {
                            final int integer15 = integer12 << 11 | integer14 << 7 | integer13 + (integer7 << 4);
                            final int integer16 = a.blocks[integer15];
                            arr6[integer13 << 8 | integer14 << 4 | integer12] = (byte)(integer16 & 0xFF);
                            cfy10.set(integer12, integer13, integer14, a.data.get(integer12, integer13 + (integer7 << 4), integer14));
                            cfy11.set(integer12, integer13, integer14, a.skyLight.get(integer12, integer13 + (integer7 << 4), integer14));
                            cfy12.set(integer12, integer13, integer14, a.blockLight.get(integer12, integer13 + (integer7 << 4), integer14));
                        }
                    }
                }
                final CompoundTag md2 = new CompoundTag();
                md2.putByte("Y", (byte)(integer7 & 0xFF));
                md2.putByteArray("Blocks", arr6);
                md2.putByteArray("Data", cfy10.getData());
                md2.putByteArray("SkyLight", cfy11.getData());
                md2.putByteArray("BlockLight", cfy12.getData());
                mj6.add(md2);
            }
        }
        md.put("Sections", (Tag)mj6);
        md.putIntArray("Biomes", new ChunkBiomeContainer(b.registryOrThrow(Registry.BIOME_REGISTRY), new ChunkPos(a.x, a.z), bsv).writeBiomes());
        md.put("Entities", (Tag)a.entities);
        md.put("TileEntities", (Tag)a.blockEntities);
        if (a.blockTicks != null) {
            md.put("TileTicks", (Tag)a.blockTicks);
        }
        md.putBoolean("convertedFromAlphaFormat", true);
    }
    
    public static class OldLevelChunk {
        public long lastUpdated;
        public boolean terrainPopulated;
        public byte[] heightmap;
        public OldDataLayer blockLight;
        public OldDataLayer skyLight;
        public OldDataLayer data;
        public byte[] blocks;
        public ListTag entities;
        public ListTag blockEntities;
        public ListTag blockTicks;
        public final int x;
        public final int z;
        
        public OldLevelChunk(final int integer1, final int integer2) {
            this.x = integer1;
            this.z = integer2;
        }
    }
}
