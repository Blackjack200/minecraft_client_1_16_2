package net.minecraft.util.worldupdate;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.regex.Matcher;
import net.minecraft.world.level.chunk.storage.RegionFile;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import java.util.List;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.ReportedException;
import java.io.IOException;
import net.minecraft.SharedConstants;
import java.util.function.Supplier;
import net.minecraft.world.level.ChunkPos;
import java.util.ListIterator;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import net.minecraft.network.chat.TranslatableComponent;
import it.unimi.dsi.fastutil.objects.Object2FloatMaps;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenCustomHashMap;
import net.minecraft.Util;
import net.minecraft.world.level.storage.DimensionDataStorage;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import com.mojang.datafixers.DataFixer;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import com.google.common.collect.ImmutableSet;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;

public class WorldUpgrader {
    private static final Logger LOGGER;
    private static final ThreadFactory THREAD_FACTORY;
    private final ImmutableSet<ResourceKey<Level>> levels;
    private final boolean eraseCache;
    private final LevelStorageSource.LevelStorageAccess levelStorage;
    private final Thread thread;
    private final DataFixer dataFixer;
    private volatile boolean running;
    private volatile boolean finished;
    private volatile float progress;
    private volatile int totalChunks;
    private volatile int converted;
    private volatile int skipped;
    private final Object2FloatMap<ResourceKey<Level>> progressMap;
    private volatile Component status;
    private static final Pattern REGEX;
    private final DimensionDataStorage overworldDataStorage;
    
    public WorldUpgrader(final LevelStorageSource.LevelStorageAccess a, final DataFixer dataFixer, final ImmutableSet<ResourceKey<Level>> immutableSet, final boolean boolean4) {
        this.running = true;
        this.progressMap = (Object2FloatMap<ResourceKey<Level>>)Object2FloatMaps.synchronize((Object2FloatMap)new Object2FloatOpenCustomHashMap((Hash.Strategy)Util.identityStrategy()));
        this.status = new TranslatableComponent("optimizeWorld.stage.counting");
        this.levels = immutableSet;
        this.eraseCache = boolean4;
        this.dataFixer = dataFixer;
        this.levelStorage = a;
        this.overworldDataStorage = new DimensionDataStorage(new File(this.levelStorage.getDimensionPath(Level.OVERWORLD), "data"), dataFixer);
        (this.thread = WorldUpgrader.THREAD_FACTORY.newThread(this::work)).setUncaughtExceptionHandler((thread, throwable) -> {
            WorldUpgrader.LOGGER.error("Error upgrading world", throwable);
            this.status = new TranslatableComponent("optimizeWorld.stage.failed");
            this.finished = true;
        });
        this.thread.start();
    }
    
    public void cancel() {
        this.running = false;
        try {
            this.thread.join();
        }
        catch (InterruptedException ex) {}
    }
    
    private void work() {
        this.totalChunks = 0;
        final ImmutableMap.Builder<ResourceKey<Level>, ListIterator<ChunkPos>> builder2 = (ImmutableMap.Builder<ResourceKey<Level>, ListIterator<ChunkPos>>)ImmutableMap.builder();
        for (final ResourceKey<Level> vj4 : this.levels) {
            final List<ChunkPos> list5 = this.getAllChunkPos(vj4);
            builder2.put(vj4, list5.listIterator());
            this.totalChunks += list5.size();
        }
        if (this.totalChunks == 0) {
            this.finished = true;
            return;
        }
        final float float3 = (float)this.totalChunks;
        final ImmutableMap<ResourceKey<Level>, ListIterator<ChunkPos>> immutableMap4 = (ImmutableMap<ResourceKey<Level>, ListIterator<ChunkPos>>)builder2.build();
        final ImmutableMap.Builder<ResourceKey<Level>, ChunkStorage> builder3 = (ImmutableMap.Builder<ResourceKey<Level>, ChunkStorage>)ImmutableMap.builder();
        for (final ResourceKey<Level> vj5 : this.levels) {
            final File file8 = this.levelStorage.getDimensionPath(vj5);
            builder3.put(vj5, new ChunkStorage(new File(file8, "region"), this.dataFixer, true));
        }
        final ImmutableMap<ResourceKey<Level>, ChunkStorage> immutableMap5 = (ImmutableMap<ResourceKey<Level>, ChunkStorage>)builder3.build();
        long long7 = Util.getMillis();
        this.status = new TranslatableComponent("optimizeWorld.stage.upgrading");
        while (this.running) {
            boolean boolean9 = false;
            float float4 = 0.0f;
            for (final ResourceKey<Level> vj6 : this.levels) {
                final ListIterator<ChunkPos> listIterator13 = (ListIterator<ChunkPos>)immutableMap4.get(vj6);
                final ChunkStorage cgr14 = (ChunkStorage)immutableMap5.get(vj6);
                if (listIterator13.hasNext()) {
                    final ChunkPos bra15 = (ChunkPos)listIterator13.next();
                    boolean boolean10 = false;
                    try {
                        final CompoundTag md17 = cgr14.read(bra15);
                        if (md17 != null) {
                            final int integer18 = ChunkStorage.getVersion(md17);
                            final CompoundTag md18 = cgr14.upgradeChunkTag(vj6, (Supplier<DimensionDataStorage>)(() -> this.overworldDataStorage), md17);
                            final CompoundTag md19 = md18.getCompound("Level");
                            final ChunkPos bra16 = new ChunkPos(md19.getInt("xPos"), md19.getInt("zPos"));
                            if (!bra16.equals(bra15)) {
                                WorldUpgrader.LOGGER.warn("Chunk {} has invalid position {}", bra15, bra16);
                            }
                            boolean boolean11 = integer18 < SharedConstants.getCurrentVersion().getWorldVersion();
                            if (this.eraseCache) {
                                boolean11 = (boolean11 || md19.contains("Heightmaps"));
                                md19.remove("Heightmaps");
                                boolean11 = (boolean11 || md19.contains("isLightOn"));
                                md19.remove("isLightOn");
                            }
                            if (boolean11) {
                                cgr14.write(bra15, md18);
                                boolean10 = true;
                            }
                        }
                    }
                    catch (ReportedException u17) {
                        final Throwable throwable18 = u17.getCause();
                        if (!(throwable18 instanceof IOException)) {
                            throw u17;
                        }
                        WorldUpgrader.LOGGER.error("Error upgrading chunk {}", bra15, throwable18);
                    }
                    catch (IOException iOException17) {
                        WorldUpgrader.LOGGER.error("Error upgrading chunk {}", bra15, iOException17);
                    }
                    if (boolean10) {
                        ++this.converted;
                    }
                    else {
                        ++this.skipped;
                    }
                    boolean9 = true;
                }
                final float float5 = listIterator13.nextIndex() / float3;
                this.progressMap.put(vj6, float5);
                float4 += float5;
            }
            this.progress = float4;
            if (!boolean9) {
                this.running = false;
            }
        }
        this.status = new TranslatableComponent("optimizeWorld.stage.finished");
        for (final ChunkStorage cgr15 : immutableMap5.values()) {
            try {
                cgr15.close();
            }
            catch (IOException iOException18) {
                WorldUpgrader.LOGGER.error("Error upgrading chunk", (Throwable)iOException18);
            }
        }
        this.overworldDataStorage.save();
        long7 = Util.getMillis() - long7;
        WorldUpgrader.LOGGER.info("World optimizaton finished after {} ms", long7);
        this.finished = true;
    }
    
    private List<ChunkPos> getAllChunkPos(final ResourceKey<Level> vj) {
        final File file3 = this.levelStorage.getDimensionPath(vj);
        final File file4 = new File(file3, "region");
        final File[] arr5 = file4.listFiles((file, string) -> string.endsWith(".mca"));
        if (arr5 == null) {
            return (List<ChunkPos>)ImmutableList.of();
        }
        final List<ChunkPos> list6 = (List<ChunkPos>)Lists.newArrayList();
        for (final File file5 : arr5) {
            final Matcher matcher11 = WorldUpgrader.REGEX.matcher((CharSequence)file5.getName());
            if (matcher11.matches()) {
                final int integer12 = Integer.parseInt(matcher11.group(1)) << 5;
                final int integer13 = Integer.parseInt(matcher11.group(2)) << 5;
                try (final RegionFile cgv14 = new RegionFile(file5, file4, true)) {
                    for (int integer14 = 0; integer14 < 32; ++integer14) {
                        for (int integer15 = 0; integer15 < 32; ++integer15) {
                            final ChunkPos bra18 = new ChunkPos(integer14 + integer12, integer15 + integer13);
                            if (cgv14.doesChunkExist(bra18)) {
                                list6.add(bra18);
                            }
                        }
                    }
                }
                catch (Throwable t4) {}
            }
        }
        return list6;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public ImmutableSet<ResourceKey<Level>> levels() {
        return this.levels;
    }
    
    public float dimensionProgress(final ResourceKey<Level> vj) {
        return this.progressMap.getFloat(vj);
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    public int getTotalChunks() {
        return this.totalChunks;
    }
    
    public int getConverted() {
        return this.converted;
    }
    
    public int getSkipped() {
        return this.skipped;
    }
    
    public Component getStatus() {
        return this.status;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).build();
        REGEX = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");
    }
}
