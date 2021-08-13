package net.minecraft.world.level.storage;

import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.storage.OldChunkStorage;
import java.io.IOException;
import java.io.DataInput;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.OverworldBiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.DataPackConfig;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.RegistryAccess;
import java.io.File;
import java.util.Collection;
import net.minecraft.world.level.Level;
import com.google.common.collect.Lists;
import net.minecraft.util.ProgressListener;
import org.apache.logging.log4j.Logger;

public class McRegionUpgrader {
    private static final Logger LOGGER;
    
    static boolean convertLevel(final LevelStorageSource.LevelStorageAccess a, final ProgressListener afk) {
        afk.progressStagePercentage(0);
        final List<File> list3 = (List<File>)Lists.newArrayList();
        final List<File> list4 = (List<File>)Lists.newArrayList();
        final List<File> list5 = (List<File>)Lists.newArrayList();
        final File file6 = a.getDimensionPath(Level.OVERWORLD);
        final File file7 = a.getDimensionPath(Level.NETHER);
        final File file8 = a.getDimensionPath(Level.END);
        McRegionUpgrader.LOGGER.info("Scanning folders...");
        addRegionFiles(file6, (Collection<File>)list3);
        if (file7.exists()) {
            addRegionFiles(file7, (Collection<File>)list4);
        }
        if (file8.exists()) {
            addRegionFiles(file8, (Collection<File>)list5);
        }
        final int integer9 = list3.size() + list4.size() + list5.size();
        McRegionUpgrader.LOGGER.info("Total conversion count is {}", integer9);
        final RegistryAccess.RegistryHolder b10 = RegistryAccess.builtin();
        final RegistryReadOps<Tag> vh11 = RegistryReadOps.<Tag>create((com.mojang.serialization.DynamicOps<Tag>)NbtOps.INSTANCE, ResourceManager.Empty.INSTANCE, b10);
        final WorldData cyk12 = a.getDataTag((DynamicOps<Tag>)vh11, DataPackConfig.DEFAULT);
        final long long13 = (cyk12 != null) ? cyk12.worldGenSettings().seed() : 0L;
        final Registry<Biome> gm16 = b10.registryOrThrow(Registry.BIOME_REGISTRY);
        BiomeSource bsv15;
        if (cyk12 != null && cyk12.worldGenSettings().isFlatWorld()) {
            bsv15 = new FixedBiomeSource(gm16.getOrThrow(Biomes.PLAINS));
        }
        else {
            bsv15 = new OverworldBiomeSource(long13, false, false, gm16);
        }
        convertRegions(b10, new File(file6, "region"), (Iterable<File>)list3, bsv15, 0, integer9, afk);
        convertRegions(b10, new File(file7, "region"), (Iterable<File>)list4, new FixedBiomeSource(gm16.getOrThrow(Biomes.NETHER_WASTES)), list3.size(), integer9, afk);
        convertRegions(b10, new File(file8, "region"), (Iterable<File>)list5, new FixedBiomeSource(gm16.getOrThrow(Biomes.THE_END)), list3.size() + list4.size(), integer9, afk);
        makeMcrLevelDatBackup(a);
        a.saveDataTag(b10, cyk12);
        return true;
    }
    
    private static void makeMcrLevelDatBackup(final LevelStorageSource.LevelStorageAccess a) {
        final File file2 = a.getLevelPath(LevelResource.LEVEL_DATA_FILE).toFile();
        if (!file2.exists()) {
            McRegionUpgrader.LOGGER.warn("Unable to create level.dat_mcr backup");
            return;
        }
        final File file3 = new File(file2.getParent(), "level.dat_mcr");
        if (!file2.renameTo(file3)) {
            McRegionUpgrader.LOGGER.warn("Unable to create level.dat_mcr backup");
        }
    }
    
    private static void convertRegions(final RegistryAccess.RegistryHolder b, final File file, final Iterable<File> iterable, final BiomeSource bsv, int integer5, final int integer6, final ProgressListener afk) {
        for (final File file2 : iterable) {
            convertRegion(b, file, file2, bsv, integer5, integer6, afk);
            ++integer5;
            final int integer7 = (int)Math.round(100.0 * integer5 / integer6);
            afk.progressStagePercentage(integer7);
        }
    }
    
    private static void convertRegion(final RegistryAccess.RegistryHolder b, final File file2, final File file3, final BiomeSource bsv, final int integer5, final int integer6, final ProgressListener afk) {
        final String string8 = file3.getName();
        try (final RegionFile cgv9 = new RegionFile(file3, file2, true);
             final RegionFile cgv10 = new RegionFile(new File(file2, string8.substring(0, string8.length() - ".mcr".length()) + ".mca"), file2, true)) {
            for (int integer7 = 0; integer7 < 32; ++integer7) {
                for (int integer8 = 0; integer8 < 32; ++integer8) {
                    final ChunkPos bra15 = new ChunkPos(integer7, integer8);
                    if (cgv9.hasChunk(bra15) && !cgv10.hasChunk(bra15)) {
                        CompoundTag md16;
                        try (final DataInputStream dataInputStream17 = cgv9.getChunkDataInputStream(bra15)) {
                            if (dataInputStream17 == null) {
                                McRegionUpgrader.LOGGER.warn("Failed to fetch input stream for chunk {}", bra15);
                                continue;
                            }
                            md16 = NbtIo.read((DataInput)dataInputStream17);
                        }
                        catch (IOException iOException17) {
                            McRegionUpgrader.LOGGER.warn("Failed to read data for chunk {}", bra15, iOException17);
                            continue;
                        }
                        final CompoundTag md17 = md16.getCompound("Level");
                        final OldChunkStorage.OldLevelChunk a18 = OldChunkStorage.load(md17);
                        final CompoundTag md18 = new CompoundTag();
                        final CompoundTag md19 = new CompoundTag();
                        md18.put("Level", (Tag)md19);
                        OldChunkStorage.convertToAnvilFormat(b, a18, md19, bsv);
                        try (final DataOutputStream dataOutputStream21 = cgv10.getChunkDataOutputStream(bra15)) {
                            NbtIo.write(md18, (DataOutput)dataOutputStream21);
                        }
                    }
                }
                int integer8 = (int)Math.round(100.0 * (integer5 * 1024) / (integer6 * 1024));
                final int integer9 = (int)Math.round(100.0 * ((integer7 + 1) * 32 + integer5 * 1024) / (integer6 * 1024));
                if (integer9 > integer8) {
                    afk.progressStagePercentage(integer9);
                }
            }
        }
        catch (IOException iOException18) {
            McRegionUpgrader.LOGGER.error("Failed to upgrade region file {}", file3, iOException18);
        }
    }
    
    private static void addRegionFiles(final File file, final Collection<File> collection) {
        final File file2 = new File(file, "region");
        final File[] arr4 = file2.listFiles((file, string) -> string.endsWith(".mcr"));
        if (arr4 != null) {
            Collections.addAll((Collection)collection, (Object[])arr4);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
