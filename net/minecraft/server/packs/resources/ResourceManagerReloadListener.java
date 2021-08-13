package net.minecraft.server.packs.resources;

import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.ProfilerFiller;

public interface ResourceManagerReloadListener extends PreparableReloadListener {
    default CompletableFuture<Void> reload(final PreparationBarrier a, final ResourceManager acf, final ProfilerFiller ant3, final ProfilerFiller ant4, final Executor executor5, final Executor executor6) {
        return (CompletableFuture<Void>)a.<Unit>wait(Unit.INSTANCE).thenRunAsync(() -> {
            ant4.startTick();
            ant4.push("listener");
            this.onResourceManagerReload(acf);
            ant4.pop();
            ant4.endTick();
        }, executor6);
    }
    
    void onResourceManagerReload(final ResourceManager acf);
}
