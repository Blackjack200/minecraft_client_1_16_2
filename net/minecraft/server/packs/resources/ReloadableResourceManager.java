package net.minecraft.server.packs.resources;

import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.packs.PackResources;
import java.util.List;
import java.util.concurrent.Executor;

public interface ReloadableResourceManager extends ResourceManager, AutoCloseable {
    default CompletableFuture<Unit> reload(final Executor executor1, final Executor executor2, final List<PackResources> list, final CompletableFuture<Unit> completableFuture) {
        return this.createFullReload(executor1, executor2, completableFuture, list).done();
    }
    
    ReloadInstance createFullReload(final Executor executor1, final Executor executor2, final CompletableFuture<Unit> completableFuture, final List<PackResources> list);
    
    void registerReloadListener(final PreparableReloadListener aca);
    
    void close();
}
