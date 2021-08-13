package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.ProfilerFiller;

public interface PreparableReloadListener {
    CompletableFuture<Void> reload(final PreparationBarrier a, final ResourceManager acf, final ProfilerFiller ant3, final ProfilerFiller ant4, final Executor executor5, final Executor executor6);
    
    default String getName() {
        return this.getClass().getSimpleName();
    }
    
    public interface PreparationBarrier {
         <T> CompletableFuture<T> wait(final T object);
    }
}
