package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.chunk.LevelChunkSection;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.chunk.LightChunkGetter;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.util.thread.ProcessorHandle;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.thread.ProcessorMailbox;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.lighting.LevelLightEngine;

public class ThreadedLevelLightEngine extends LevelLightEngine implements AutoCloseable {
    private static final Logger LOGGER;
    private final ProcessorMailbox<Runnable> taskMailbox;
    private final ObjectList<Pair<TaskType, Runnable>> lightTasks;
    private final ChunkMap chunkMap;
    private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> sorterMailbox;
    private volatile int taskPerBatch;
    private final AtomicBoolean scheduled;
    
    public ThreadedLevelLightEngine(final LightChunkGetter cgg, final ChunkMap zs, final boolean boolean3, final ProcessorMailbox<Runnable> aob, final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> aoa) {
        super(cgg, true, boolean3);
        this.lightTasks = (ObjectList<Pair<TaskType, Runnable>>)new ObjectArrayList();
        this.taskPerBatch = 5;
        this.scheduled = new AtomicBoolean();
        this.chunkMap = zs;
        this.sorterMailbox = aoa;
        this.taskMailbox = aob;
    }
    
    public void close() {
    }
    
    @Override
    public int runUpdates(final int integer, final boolean boolean2, final boolean boolean3) {
        throw Util.<UnsupportedOperationException>pauseInIde(new UnsupportedOperationException("Ran authomatically on a different thread!"));
    }
    
    @Override
    public void onBlockEmissionIncrease(final BlockPos fx, final int integer) {
        throw Util.<UnsupportedOperationException>pauseInIde(new UnsupportedOperationException("Ran authomatically on a different thread!"));
    }
    
    @Override
    public void checkBlock(final BlockPos fx) {
        final BlockPos fx2 = fx.immutable();
        this.addTask(fx.getX() >> 4, fx.getZ() >> 4, TaskType.POST_UPDATE, Util.name(() -> super.checkBlock(fx2), (Supplier<String>)(() -> new StringBuilder().append("checkBlock ").append(fx2).toString())));
    }
    
    protected void updateChunkStatus(final ChunkPos bra) {
        this.addTask(bra.x, bra.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> {
            super.retainData(bra, false);
            super.enableLightSources(bra, false);
            for (int integer3 = -1; integer3 < 17; ++integer3) {
                super.queueSectionData(LightLayer.BLOCK, SectionPos.of(bra, integer3), null, true);
                super.queueSectionData(LightLayer.SKY, SectionPos.of(bra, integer3), null, true);
            }
            for (int integer3 = 0; integer3 < 16; ++integer3) {
                super.updateSectionStatus(SectionPos.of(bra, integer3), true);
            }
        }, (Supplier<String>)(() -> new StringBuilder().append("updateChunkStatus ").append(bra).append(" ").append(true).toString())));
    }
    
    @Override
    public void updateSectionStatus(final SectionPos gp, final boolean boolean2) {
        this.addTask(gp.x(), gp.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.updateSectionStatus(gp, boolean2), (Supplier<String>)(() -> new StringBuilder().append("updateSectionStatus ").append(gp).append(" ").append(boolean2).toString())));
    }
    
    @Override
    public void enableLightSources(final ChunkPos bra, final boolean boolean2) {
        this.addTask(bra.x, bra.z, TaskType.PRE_UPDATE, Util.name(() -> super.enableLightSources(bra, boolean2), (Supplier<String>)(() -> new StringBuilder().append("enableLight ").append(bra).append(" ").append(boolean2).toString())));
    }
    
    @Override
    public void queueSectionData(final LightLayer bsc, final SectionPos gp, @Nullable final DataLayer cfy, final boolean boolean4) {
        this.addTask(gp.x(), gp.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.queueSectionData(bsc, gp, cfy, boolean4), (Supplier<String>)(() -> new StringBuilder().append("queueData ").append(gp).toString())));
    }
    
    private void addTask(final int integer1, final int integer2, final TaskType a, final Runnable runnable) {
        this.addTask(integer1, integer2, this.chunkMap.getChunkQueueLevel(ChunkPos.asLong(integer1, integer2)), a, runnable);
    }
    
    private void addTask(final int integer1, final int integer2, final IntSupplier intSupplier, final TaskType a, final Runnable runnable) {
        this.sorterMailbox.tell(ChunkTaskPriorityQueueSorter.message(() -> {
            this.lightTasks.add(Pair.of((Object)a, (Object)runnable));
            if (this.lightTasks.size() >= this.taskPerBatch) {
                this.runUpdate();
            }
        }, ChunkPos.asLong(integer1, integer2), intSupplier));
    }
    
    @Override
    public void retainData(final ChunkPos bra, final boolean boolean2) {
        this.addTask(bra.x, bra.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.retainData(bra, boolean2), (Supplier<String>)(() -> new StringBuilder().append("retainData ").append(bra).toString())));
    }
    
    public CompletableFuture<ChunkAccess> lightChunk(final ChunkAccess cft, final boolean boolean2) {
        final ChunkPos bra4 = cft.getPos();
        cft.setLightCorrect(false);
        this.addTask(bra4.x, bra4.z, TaskType.PRE_UPDATE, Util.name(() -> {
            final LevelChunkSection[] arr5 = cft.getSections();
            for (int integer6 = 0; integer6 < 16; ++integer6) {
                final LevelChunkSection cgf7 = arr5[integer6];
                if (!LevelChunkSection.isEmpty(cgf7)) {
                    super.updateSectionStatus(SectionPos.of(bra4, integer6), false);
                }
            }
            super.enableLightSources(bra4, true);
            if (!boolean2) {
                cft.getLights().forEach(fx -> super.onBlockEmissionIncrease(fx, cft.getLightEmission(fx)));
            }
            this.chunkMap.releaseLightTicket(bra4);
        }, (Supplier<String>)(() -> new StringBuilder().append("lightChunk ").append(bra4).append(" ").append(boolean2).toString())));
        return (CompletableFuture<ChunkAccess>)CompletableFuture.supplyAsync(() -> {
            cft.setLightCorrect(true);
            super.retainData(bra4, false);
            return cft;
        }, runnable -> this.addTask(bra4.x, bra4.z, TaskType.POST_UPDATE, runnable));
    }
    
    public void tryScheduleUpdate() {
        if ((!this.lightTasks.isEmpty() || super.hasLightWork()) && this.scheduled.compareAndSet(false, true)) {
            this.taskMailbox.tell(() -> {
                this.runUpdate();
                this.scheduled.set(false);
            });
        }
    }
    
    private void runUpdate() {
        int integer2;
        ObjectListIterator<Pair<TaskType, Runnable>> objectListIterator3;
        int integer3;
        Pair<TaskType, Runnable> pair5;
        for (integer2 = Math.min(this.lightTasks.size(), this.taskPerBatch), objectListIterator3 = (ObjectListIterator<Pair<TaskType, Runnable>>)this.lightTasks.iterator(), integer3 = 0; objectListIterator3.hasNext() && integer3 < integer2; ++integer3) {
            pair5 = (Pair<TaskType, Runnable>)objectListIterator3.next();
            if (pair5.getFirst() == TaskType.PRE_UPDATE) {
                ((Runnable)pair5.getSecond()).run();
            }
        }
        objectListIterator3.back(integer3);
        super.runUpdates(Integer.MAX_VALUE, true, true);
        for (integer3 = 0; objectListIterator3.hasNext() && integer3 < integer2; ++integer3) {
            pair5 = (Pair<TaskType, Runnable>)objectListIterator3.next();
            if (pair5.getFirst() == TaskType.POST_UPDATE) {
                ((Runnable)pair5.getSecond()).run();
            }
            objectListIterator3.remove();
        }
    }
    
    public void setTaskPerBatch(final int integer) {
        this.taskPerBatch = integer;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    enum TaskType {
        PRE_UPDATE, 
        POST_UPDATE;
    }
}
