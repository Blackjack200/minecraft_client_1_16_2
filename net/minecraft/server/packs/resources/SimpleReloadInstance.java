package net.minecraft.server.packs.resources;

import java.util.Iterator;
import net.minecraft.Util;
import java.util.concurrent.CompletionStage;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.InactiveProfiler;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.List;
import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;

public class SimpleReloadInstance<S> implements ReloadInstance {
    protected final ResourceManager resourceManager;
    protected final CompletableFuture<Unit> allPreparations;
    protected final CompletableFuture<List<S>> allDone;
    private final Set<PreparableReloadListener> preparingListeners;
    private final int listenerCount;
    private int startedReloads;
    private int finishedReloads;
    private final AtomicInteger startedTaskCounter;
    private final AtomicInteger doneTaskCounter;
    
    public static SimpleReloadInstance<Void> of(final ResourceManager acf, final List<PreparableReloadListener> list, final Executor executor3, final Executor executor4, final CompletableFuture<Unit> completableFuture) {
        return new SimpleReloadInstance<Void>(executor3, executor4, acf, list, (a, acf, aca, executor5, executor6) -> aca.reload(a, acf, InactiveProfiler.INSTANCE, InactiveProfiler.INSTANCE, executor3, executor6), completableFuture);
    }
    
    protected SimpleReloadInstance(final Executor executor1, final Executor executor2, final ResourceManager acf, final List<PreparableReloadListener> list, final StateFactory<S> a, final CompletableFuture<Unit> completableFuture) {
        this.allPreparations = (CompletableFuture<Unit>)new CompletableFuture();
        this.startedTaskCounter = new AtomicInteger();
        this.doneTaskCounter = new AtomicInteger();
        this.resourceManager = acf;
        this.listenerCount = list.size();
        this.startedTaskCounter.incrementAndGet();
        completableFuture.thenRun(this.doneTaskCounter::incrementAndGet);
        final List<CompletableFuture<S>> list2 = (List<CompletableFuture<S>>)Lists.newArrayList();
        CompletableFuture<?> completableFuture2 = completableFuture;
        this.preparingListeners = (Set<PreparableReloadListener>)Sets.newHashSet((Iterable)list);
        for (final PreparableReloadListener aca11 : list) {
            final CompletableFuture<?> completableFuture3 = completableFuture2;
            final CompletableFuture<S> completableFuture4 = a.create(new PreparableReloadListener.PreparationBarrier() {
                public <T> CompletableFuture<T> wait(final T object) {
                    executor2.execute(() -> {
                        SimpleReloadInstance.this.preparingListeners.remove(aca);
                        if (SimpleReloadInstance.this.preparingListeners.isEmpty()) {
                            SimpleReloadInstance.this.allPreparations.complete(Unit.INSTANCE);
                        }
                    });
                    return (CompletableFuture<T>)SimpleReloadInstance.this.allPreparations.thenCombine((CompletionStage)completableFuture3, (afu, object3) -> object);
                }
            }, acf, aca11, runnable -> {
                this.startedTaskCounter.incrementAndGet();
                executor1.execute(() -> {
                    runnable.run();
                    this.doneTaskCounter.incrementAndGet();
                });
            }, runnable -> {
                ++this.startedReloads;
                executor2.execute(() -> {
                    runnable.run();
                    ++this.finishedReloads;
                });
            });
            list2.add(completableFuture4);
            completableFuture2 = completableFuture4;
        }
        this.allDone = Util.<S>sequence((java.util.List<? extends java.util.concurrent.CompletableFuture<? extends S>>)list2);
    }
    
    public CompletableFuture<Unit> done() {
        return (CompletableFuture<Unit>)this.allDone.thenApply(list -> Unit.INSTANCE);
    }
    
    public float getActualProgress() {
        final int integer2 = this.listenerCount - this.preparingListeners.size();
        final float float3 = (float)(this.doneTaskCounter.get() * 2 + this.finishedReloads * 2 + integer2 * 1);
        final float float4 = (float)(this.startedTaskCounter.get() * 2 + this.startedReloads * 2 + this.listenerCount * 1);
        return float3 / float4;
    }
    
    public boolean isApplying() {
        return this.allPreparations.isDone();
    }
    
    public boolean isDone() {
        return this.allDone.isDone();
    }
    
    public void checkExceptions() {
        if (this.allDone.isCompletedExceptionally()) {
            this.allDone.join();
        }
    }
    
    public interface StateFactory<S> {
        CompletableFuture<S> create(final PreparableReloadListener.PreparationBarrier a, final ResourceManager acf, final PreparableReloadListener aca, final Executor executor4, final Executor executor5);
    }
}
