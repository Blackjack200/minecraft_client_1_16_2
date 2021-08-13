package net.minecraft.client.renderer.chunk;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import com.google.common.primitives.Doubles;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CancellationException;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.RenderShape;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.world.level.chunk.ChunkStatus;
import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.stream.Collectors;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import java.util.Map;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Set;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.Minecraft;
import net.minecraft.CrashReport;
import java.util.concurrent.CompletionStage;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import com.google.common.collect.Queues;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import java.util.concurrent.Executor;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import java.util.Queue;
import java.util.PriorityQueue;
import org.apache.logging.log4j.Logger;

public class ChunkRenderDispatcher {
    private static final Logger LOGGER;
    private final PriorityQueue<RenderChunk.ChunkCompileTask> toBatch;
    private final Queue<ChunkBufferBuilderPack> freeBuffers;
    private final Queue<Runnable> toUpload;
    private volatile int toBatchCount;
    private volatile int freeBufferCount;
    private final ChunkBufferBuilderPack fixedBuffers;
    private final ProcessorMailbox<Runnable> mailbox;
    private final Executor executor;
    private Level level;
    private final LevelRenderer renderer;
    private Vec3 camera;
    
    public ChunkRenderDispatcher(final Level bru, final LevelRenderer dzw, final Executor executor, final boolean boolean4, final ChunkBufferBuilderPack dzl) {
        this.toBatch = (PriorityQueue<RenderChunk.ChunkCompileTask>)Queues.newPriorityQueue();
        this.toUpload = (Queue<Runnable>)Queues.newConcurrentLinkedQueue();
        this.camera = Vec3.ZERO;
        this.level = bru;
        this.renderer = dzw;
        final int integer7 = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3) / (RenderType.chunkBufferLayers().stream().mapToInt(RenderType::bufferSize).sum() * 4) - 1);
        final int integer8 = Runtime.getRuntime().availableProcessors();
        final int integer9 = boolean4 ? integer8 : Math.min(integer8, 4);
        final int integer10 = Math.max(1, Math.min(integer9, integer7));
        this.fixedBuffers = dzl;
        final List<ChunkBufferBuilderPack> list11 = (List<ChunkBufferBuilderPack>)Lists.newArrayListWithExpectedSize(integer10);
        try {
            for (int integer11 = 0; integer11 < integer10; ++integer11) {
                list11.add(new ChunkBufferBuilderPack());
            }
        }
        catch (OutOfMemoryError outOfMemoryError12) {
            ChunkRenderDispatcher.LOGGER.warn("Allocated only {}/{} buffers", list11.size(), integer10);
            for (int integer12 = Math.min(list11.size() * 2 / 3, list11.size() - 1), integer13 = 0; integer13 < integer12; ++integer13) {
                list11.remove(list11.size() - 1);
            }
            System.gc();
        }
        this.freeBuffers = (Queue<ChunkBufferBuilderPack>)Queues.newArrayDeque((Iterable)list11);
        this.freeBufferCount = this.freeBuffers.size();
        this.executor = executor;
        (this.mailbox = ProcessorMailbox.create(executor, "Chunk Renderer")).tell(this::runTask);
    }
    
    public void setLevel(final Level bru) {
        this.level = bru;
    }
    
    private void runTask() {
        if (this.freeBuffers.isEmpty()) {
            return;
        }
        final RenderChunk.ChunkCompileTask a2 = (RenderChunk.ChunkCompileTask)this.toBatch.poll();
        if (a2 == null) {
            return;
        }
        final ChunkBufferBuilderPack dzl3 = (ChunkBufferBuilderPack)this.freeBuffers.poll();
        this.toBatchCount = this.toBatch.size();
        this.freeBufferCount = this.freeBuffers.size();
        CompletableFuture.runAsync(() -> {}, this.executor).thenCompose(void3 -> a2.doTask(dzl3)).whenComplete((a, throwable) -> {
            if (throwable != null) {
                final CrashReport l5 = CrashReport.forThrowable(throwable, "Batching chunks");
                Minecraft.getInstance().delayCrash(Minecraft.getInstance().fillReport(l5));
                return;
            }
            this.mailbox.tell(() -> {
                if (a == ChunkTaskResult.SUCCESSFUL) {
                    dzl3.clearAll();
                }
                else {
                    dzl3.discardAll();
                }
                this.freeBuffers.add(dzl3);
                this.freeBufferCount = this.freeBuffers.size();
                this.runTask();
            });
        });
    }
    
    public String getStats() {
        return String.format("pC: %03d, pU: %02d, aB: %02d", new Object[] { this.toBatchCount, this.toUpload.size(), this.freeBufferCount });
    }
    
    public void setCamera(final Vec3 dck) {
        this.camera = dck;
    }
    
    public Vec3 getCameraPosition() {
        return this.camera;
    }
    
    public boolean uploadAllPendingUploads() {
        boolean boolean2 = false;
        Runnable runnable3;
        while ((runnable3 = (Runnable)this.toUpload.poll()) != null) {
            runnable3.run();
            boolean2 = true;
        }
        return boolean2;
    }
    
    public void rebuildChunkSync(final RenderChunk c) {
        c.compileSync();
    }
    
    public void blockUntilClear() {
        this.clearBatchQueue();
    }
    
    public void schedule(final RenderChunk.ChunkCompileTask a) {
        this.mailbox.tell(() -> {
            this.toBatch.offer(a);
            this.toBatchCount = this.toBatch.size();
            this.runTask();
        });
    }
    
    public CompletableFuture<Void> uploadChunkLayer(final BufferBuilder dfe, final VertexBuffer dfm) {
        return (CompletableFuture<Void>)CompletableFuture.runAsync(() -> {}, this.toUpload::add).thenCompose(void3 -> this.doUploadChunkLayer(dfe, dfm));
    }
    
    private CompletableFuture<Void> doUploadChunkLayer(final BufferBuilder dfe, final VertexBuffer dfm) {
        return dfm.uploadLater(dfe);
    }
    
    private void clearBatchQueue() {
        while (!this.toBatch.isEmpty()) {
            final RenderChunk.ChunkCompileTask a2 = (RenderChunk.ChunkCompileTask)this.toBatch.poll();
            if (a2 != null) {
                a2.cancel();
            }
        }
        this.toBatchCount = 0;
    }
    
    public boolean isQueueEmpty() {
        return this.toBatchCount == 0 && this.toUpload.isEmpty();
    }
    
    public void dispose() {
        this.clearBatchQueue();
        this.mailbox.close();
        this.freeBuffers.clear();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public class RenderChunk {
        public final AtomicReference<CompiledChunk> compiled;
        @Nullable
        private RebuildTask lastRebuildTask;
        @Nullable
        private ResortTransparencyTask lastResortTransparencyTask;
        private final Set<BlockEntity> globalBlockEntities;
        private final Map<RenderType, VertexBuffer> buffers;
        public AABB bb;
        private int lastFrame;
        private boolean dirty;
        private final BlockPos.MutableBlockPos origin;
        private final BlockPos.MutableBlockPos[] relativeOrigins;
        private boolean playerChanged;
        
        public RenderChunk() {
            this.compiled = (AtomicReference<CompiledChunk>)new AtomicReference(CompiledChunk.UNCOMPILED);
            this.globalBlockEntities = (Set<BlockEntity>)Sets.newHashSet();
            this.buffers = (Map<RenderType, VertexBuffer>)RenderType.chunkBufferLayers().stream().collect(Collectors.toMap(eag -> eag, eag -> new VertexBuffer(DefaultVertexFormat.BLOCK)));
            this.lastFrame = -1;
            this.dirty = true;
            this.origin = new BlockPos.MutableBlockPos(-1, -1, -1);
            this.relativeOrigins = Util.<BlockPos.MutableBlockPos[]>make(new BlockPos.MutableBlockPos[6], (java.util.function.Consumer<BlockPos.MutableBlockPos[]>)(arr -> {
                for (int integer2 = 0; integer2 < arr.length; ++integer2) {
                    arr[integer2] = new BlockPos.MutableBlockPos();
                }
            }));
        }
        
        private boolean doesChunkExistAt(final BlockPos fx) {
            return ChunkRenderDispatcher.this.level.getChunk(fx.getX() >> 4, fx.getZ() >> 4, ChunkStatus.FULL, false) != null;
        }
        
        public boolean hasAllNeighbors() {
            final int integer2 = 24;
            return this.getDistToPlayerSqr() <= 576.0 || (this.doesChunkExistAt(this.relativeOrigins[Direction.WEST.ordinal()]) && this.doesChunkExistAt(this.relativeOrigins[Direction.NORTH.ordinal()]) && this.doesChunkExistAt(this.relativeOrigins[Direction.EAST.ordinal()]) && this.doesChunkExistAt(this.relativeOrigins[Direction.SOUTH.ordinal()]));
        }
        
        public boolean setFrame(final int integer) {
            if (this.lastFrame == integer) {
                return false;
            }
            this.lastFrame = integer;
            return true;
        }
        
        public VertexBuffer getBuffer(final RenderType eag) {
            return (VertexBuffer)this.buffers.get(eag);
        }
        
        public void setOrigin(final int integer1, final int integer2, final int integer3) {
            if (integer1 == this.origin.getX() && integer2 == this.origin.getY() && integer3 == this.origin.getZ()) {
                return;
            }
            this.reset();
            this.origin.set(integer1, integer2, integer3);
            this.bb = new AABB(integer1, integer2, integer3, integer1 + 16, integer2 + 16, integer3 + 16);
            for (final Direction gc8 : Direction.values()) {
                this.relativeOrigins[gc8.ordinal()].set(this.origin).move(gc8, 16);
            }
        }
        
        protected double getDistToPlayerSqr() {
            final Camera djh2 = Minecraft.getInstance().gameRenderer.getMainCamera();
            final double double3 = this.bb.minX + 8.0 - djh2.getPosition().x;
            final double double4 = this.bb.minY + 8.0 - djh2.getPosition().y;
            final double double5 = this.bb.minZ + 8.0 - djh2.getPosition().z;
            return double3 * double3 + double4 * double4 + double5 * double5;
        }
        
        private void beginLayer(final BufferBuilder dfe) {
            dfe.begin(7, DefaultVertexFormat.BLOCK);
        }
        
        public CompiledChunk getCompiledChunk() {
            return (CompiledChunk)this.compiled.get();
        }
        
        private void reset() {
            this.cancelTasks();
            this.compiled.set(CompiledChunk.UNCOMPILED);
            this.dirty = true;
        }
        
        public void releaseBuffers() {
            this.reset();
            this.buffers.values().forEach(VertexBuffer::close);
        }
        
        public BlockPos getOrigin() {
            return this.origin;
        }
        
        public void setDirty(final boolean boolean1) {
            final boolean boolean2 = this.dirty;
            this.dirty = true;
            this.playerChanged = (boolean1 | (boolean2 && this.playerChanged));
        }
        
        public void setNotDirty() {
            this.dirty = false;
            this.playerChanged = false;
        }
        
        public boolean isDirty() {
            return this.dirty;
        }
        
        public boolean isDirtyFromPlayer() {
            return this.dirty && this.playerChanged;
        }
        
        public BlockPos getRelativeOrigin(final Direction gc) {
            return this.relativeOrigins[gc.ordinal()];
        }
        
        public boolean resortTransparency(final RenderType eag, final ChunkRenderDispatcher ecm) {
            final CompiledChunk b4 = this.getCompiledChunk();
            if (this.lastResortTransparencyTask != null) {
                this.lastResortTransparencyTask.cancel();
            }
            if (!b4.hasLayer.contains(eag)) {
                return false;
            }
            ecm.schedule(this.lastResortTransparencyTask = new ResortTransparencyTask(this.getDistToPlayerSqr(), b4));
            return true;
        }
        
        protected void cancelTasks() {
            if (this.lastRebuildTask != null) {
                this.lastRebuildTask.cancel();
                this.lastRebuildTask = null;
            }
            if (this.lastResortTransparencyTask != null) {
                this.lastResortTransparencyTask.cancel();
                this.lastResortTransparencyTask = null;
            }
        }
        
        public ChunkCompileTask createCompileTask() {
            this.cancelTasks();
            final BlockPos fx2 = this.origin.immutable();
            final int integer3 = 1;
            final RenderChunkRegion ecn4 = RenderChunkRegion.createIfNotEmpty(ChunkRenderDispatcher.this.level, fx2.offset(-1, -1, -1), fx2.offset(16, 16, 16), 1);
            return this.lastRebuildTask = new RebuildTask(this.getDistToPlayerSqr(), ecn4);
        }
        
        public void rebuildChunkAsync(final ChunkRenderDispatcher ecm) {
            final ChunkCompileTask a3 = this.createCompileTask();
            ecm.schedule(a3);
        }
        
        private void updateGlobalBlockEntities(final Set<BlockEntity> set) {
            final Set<BlockEntity> set2 = (Set<BlockEntity>)Sets.newHashSet((Iterable)set);
            final Set<BlockEntity> set3 = (Set<BlockEntity>)Sets.newHashSet((Iterable)this.globalBlockEntities);
            set2.removeAll((Collection)this.globalBlockEntities);
            set3.removeAll((Collection)set);
            this.globalBlockEntities.clear();
            this.globalBlockEntities.addAll((Collection)set);
            ChunkRenderDispatcher.this.renderer.updateGlobalBlockEntities((Collection<BlockEntity>)set3, (Collection<BlockEntity>)set2);
        }
        
        public void compileSync() {
            final ChunkCompileTask a2 = this.createCompileTask();
            a2.doTask(ChunkRenderDispatcher.this.fixedBuffers);
        }
        
        class RebuildTask extends ChunkCompileTask {
            @Nullable
            protected RenderChunkRegion region;
            
            public RebuildTask(final double double2, @Nullable final RenderChunkRegion ecn) {
                super(double2);
                this.region = ecn;
            }
            
            @Override
            public CompletableFuture<ChunkTaskResult> doTask(final ChunkBufferBuilderPack dzl) {
                if (this.isCancelled.get()) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                if (!RenderChunk.this.hasAllNeighbors()) {
                    this.region = null;
                    RenderChunk.this.setDirty(false);
                    this.isCancelled.set(true);
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                if (this.isCancelled.get()) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                final Vec3 dck3 = ChunkRenderDispatcher.this.getCameraPosition();
                final float float4 = (float)dck3.x;
                final float float5 = (float)dck3.y;
                final float float6 = (float)dck3.z;
                final CompiledChunk b7 = new CompiledChunk();
                final Set<BlockEntity> set8 = this.compile(float4, float5, float6, b7, dzl);
                RenderChunk.this.updateGlobalBlockEntities(set8);
                if (this.isCancelled.get()) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                final List<CompletableFuture<Void>> list9 = (List<CompletableFuture<Void>>)Lists.newArrayList();
                b7.hasLayer.forEach(eag -> list9.add(ChunkRenderDispatcher.this.uploadChunkLayer(dzl.builder(eag), RenderChunk.this.getBuffer(eag))));
                return (CompletableFuture<ChunkTaskResult>)Util.sequence((java.util.List<? extends java.util.concurrent.CompletableFuture<?>>)list9).handle((list, throwable) -> {
                    if (throwable != null && !(throwable instanceof CancellationException) && !(throwable instanceof InterruptedException)) {
                        Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Rendering chunk"));
                    }
                    if (this.isCancelled.get()) {
                        return ChunkTaskResult.CANCELLED;
                    }
                    RenderChunk.this.compiled.set(b7);
                    return ChunkTaskResult.SUCCESSFUL;
                });
            }
            
            private Set<BlockEntity> compile(final float float1, final float float2, final float float3, final CompiledChunk b, final ChunkBufferBuilderPack dzl) {
                final int integer7 = 1;
                final BlockPos fx8 = RenderChunk.this.origin.immutable();
                final BlockPos fx9 = fx8.offset(15, 15, 15);
                final VisGraph eco10 = new VisGraph();
                final Set<BlockEntity> set11 = (Set<BlockEntity>)Sets.newHashSet();
                final RenderChunkRegion ecn12 = this.region;
                this.region = null;
                final PoseStack dfj13 = new PoseStack();
                if (ecn12 != null) {
                    ModelBlockRenderer.enableCaching();
                    final Random random14 = new Random();
                    final BlockRenderDispatcher eap15 = Minecraft.getInstance().getBlockRenderer();
                    for (final BlockPos fx10 : BlockPos.betweenClosed(fx8, fx9)) {
                        final BlockState cee18 = ecn12.getBlockState(fx10);
                        final Block bul19 = cee18.getBlock();
                        if (cee18.isSolidRender(ecn12, fx10)) {
                            eco10.setOpaque(fx10);
                        }
                        if (bul19.isEntityBlock()) {
                            final BlockEntity ccg20 = ecn12.getBlockEntity(fx10, LevelChunk.EntityCreationType.CHECK);
                            if (ccg20 != null) {
                                this.<BlockEntity>handleBlockEntity(b, set11, ccg20);
                            }
                        }
                        final FluidState cuu20 = ecn12.getFluidState(fx10);
                        if (!cuu20.isEmpty()) {
                            final RenderType eag21 = ItemBlockRenderTypes.getRenderLayer(cuu20);
                            final BufferBuilder dfe22 = dzl.builder(eag21);
                            if (b.hasLayer.add(eag21)) {
                                RenderChunk.this.beginLayer(dfe22);
                            }
                            if (eap15.renderLiquid(fx10, ecn12, dfe22, cuu20)) {
                                b.isCompletelyEmpty = false;
                                b.hasBlocks.add(eag21);
                            }
                        }
                        if (cee18.getRenderShape() != RenderShape.INVISIBLE) {
                            final RenderType eag21 = ItemBlockRenderTypes.getChunkRenderType(cee18);
                            final BufferBuilder dfe22 = dzl.builder(eag21);
                            if (b.hasLayer.add(eag21)) {
                                RenderChunk.this.beginLayer(dfe22);
                            }
                            dfj13.pushPose();
                            dfj13.translate(fx10.getX() & 0xF, fx10.getY() & 0xF, fx10.getZ() & 0xF);
                            if (eap15.renderBatched(cee18, fx10, ecn12, dfj13, dfe22, true, random14)) {
                                b.isCompletelyEmpty = false;
                                b.hasBlocks.add(eag21);
                            }
                            dfj13.popPose();
                        }
                    }
                    if (b.hasBlocks.contains(RenderType.translucent())) {
                        final BufferBuilder dfe23 = dzl.builder(RenderType.translucent());
                        dfe23.sortQuads(float1 - fx8.getX(), float2 - fx8.getY(), float3 - fx8.getZ());
                        b.transparencyState = dfe23.getState();
                    }
                    b.hasLayer.stream().map(dzl::builder).forEach(BufferBuilder::end);
                    ModelBlockRenderer.clearCache();
                }
                b.visibilitySet = eco10.resolve();
                return set11;
            }
            
            private <E extends BlockEntity> void handleBlockEntity(final CompiledChunk b, final Set<BlockEntity> set, final E ccg) {
                final BlockEntityRenderer<E> ebw5 = BlockEntityRenderDispatcher.instance.<E>getRenderer(ccg);
                if (ebw5 != null) {
                    b.renderableBlockEntities.add(ccg);
                    if (ebw5.shouldRenderOffScreen(ccg)) {
                        set.add(ccg);
                    }
                }
            }
            
            @Override
            public void cancel() {
                this.region = null;
                if (this.isCancelled.compareAndSet(false, true)) {
                    RenderChunk.this.setDirty(false);
                }
            }
        }
        
        class ResortTransparencyTask extends ChunkCompileTask {
            private final CompiledChunk compiledChunk;
            
            public ResortTransparencyTask(final double double2, final CompiledChunk b) {
                super(double2);
                this.compiledChunk = b;
            }
            
            @Override
            public CompletableFuture<ChunkTaskResult> doTask(final ChunkBufferBuilderPack dzl) {
                if (this.isCancelled.get()) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                if (!RenderChunk.this.hasAllNeighbors()) {
                    this.isCancelled.set(true);
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                if (this.isCancelled.get()) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                final Vec3 dck3 = ChunkRenderDispatcher.this.getCameraPosition();
                final float float4 = (float)dck3.x;
                final float float5 = (float)dck3.y;
                final float float6 = (float)dck3.z;
                final BufferBuilder.State b7 = this.compiledChunk.transparencyState;
                if (b7 == null || !this.compiledChunk.hasBlocks.contains(RenderType.translucent())) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                final BufferBuilder dfe8 = dzl.builder(RenderType.translucent());
                RenderChunk.this.beginLayer(dfe8);
                dfe8.restoreState(b7);
                dfe8.sortQuads(float4 - RenderChunk.this.origin.getX(), float5 - RenderChunk.this.origin.getY(), float6 - RenderChunk.this.origin.getZ());
                this.compiledChunk.transparencyState = dfe8.getState();
                dfe8.end();
                if (this.isCancelled.get()) {
                    return (CompletableFuture<ChunkTaskResult>)CompletableFuture.completedFuture(ChunkTaskResult.CANCELLED);
                }
                final CompletableFuture<ChunkTaskResult> completableFuture9 = (CompletableFuture<ChunkTaskResult>)ChunkRenderDispatcher.this.uploadChunkLayer(dzl.builder(RenderType.translucent()), RenderChunk.this.getBuffer(RenderType.translucent())).thenApply(void1 -> ChunkTaskResult.CANCELLED);
                return (CompletableFuture<ChunkTaskResult>)completableFuture9.handle((a, throwable) -> {
                    if (throwable != null && !(throwable instanceof CancellationException) && !(throwable instanceof InterruptedException)) {
                        Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Rendering chunk"));
                    }
                    return this.isCancelled.get() ? ChunkTaskResult.CANCELLED : ChunkTaskResult.SUCCESSFUL;
                });
            }
            
            @Override
            public void cancel() {
                this.isCancelled.set(true);
            }
        }
        
        abstract class ChunkCompileTask implements Comparable<ChunkCompileTask> {
            protected final double distAtCreation;
            protected final AtomicBoolean isCancelled;
            
            public ChunkCompileTask(final double double2) {
                this.isCancelled = new AtomicBoolean(false);
                this.distAtCreation = double2;
            }
            
            public abstract CompletableFuture<ChunkTaskResult> doTask(final ChunkBufferBuilderPack dzl);
            
            public abstract void cancel();
            
            public int compareTo(final ChunkCompileTask a) {
                return Doubles.compare(this.distAtCreation, a.distAtCreation);
            }
        }
    }
    
    enum ChunkTaskResult {
        SUCCESSFUL, 
        CANCELLED;
    }
    
    public static class CompiledChunk {
        public static final CompiledChunk UNCOMPILED;
        private final Set<RenderType> hasBlocks;
        private final Set<RenderType> hasLayer;
        private boolean isCompletelyEmpty;
        private final List<BlockEntity> renderableBlockEntities;
        private VisibilitySet visibilitySet;
        @Nullable
        private BufferBuilder.State transparencyState;
        
        public CompiledChunk() {
            this.hasBlocks = (Set<RenderType>)new ObjectArraySet();
            this.hasLayer = (Set<RenderType>)new ObjectArraySet();
            this.isCompletelyEmpty = true;
            this.renderableBlockEntities = (List<BlockEntity>)Lists.newArrayList();
            this.visibilitySet = new VisibilitySet();
        }
        
        public boolean hasNoRenderableLayers() {
            return this.isCompletelyEmpty;
        }
        
        public boolean isEmpty(final RenderType eag) {
            return !this.hasBlocks.contains(eag);
        }
        
        public List<BlockEntity> getRenderableBlockEntities() {
            return this.renderableBlockEntities;
        }
        
        public boolean facesCanSeeEachother(final Direction gc1, final Direction gc2) {
            return this.visibilitySet.visibilityBetween(gc1, gc2);
        }
        
        static {
            UNCOMPILED = new CompiledChunk() {
                @Override
                public boolean facesCanSeeEachother(final Direction gc1, final Direction gc2) {
                    return false;
                }
            };
        }
    }
}
