package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.ProfilerFiller;

public abstract class SimplePreparableReloadListener<T> implements PreparableReloadListener {
    public final CompletableFuture<Void> reload(final PreparationBarrier a, final ResourceManager acf, final ProfilerFiller ant3, final ProfilerFiller ant4, final Executor executor5, final Executor executor6) {
        return (CompletableFuture<Void>)CompletableFuture.supplyAsync(() -> this.prepare(acf, ant3), executor5).thenCompose(a::wait).thenAcceptAsync(object -> this.apply(object, acf, ant4), executor6);
    }
    
    protected abstract T prepare(final ResourceManager acf, final ProfilerFiller ant);
    
    protected abstract void apply(final T object, final ResourceManager acf, final ProfilerFiller ant);
}
