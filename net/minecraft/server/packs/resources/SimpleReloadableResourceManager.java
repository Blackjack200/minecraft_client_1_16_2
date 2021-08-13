package net.minecraft.server.packs.resources;

import org.apache.logging.log4j.LogManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.util.Supplier;
import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.Collections;
import java.util.Collection;
import java.util.function.Predicate;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PackResources;
import java.util.Set;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements ReloadableResourceManager {
    private static final Logger LOGGER;
    private final Map<String, FallbackResourceManager> namespacedPacks;
    private final List<PreparableReloadListener> listeners;
    private final List<PreparableReloadListener> recentlyRegistered;
    private final Set<String> namespaces;
    private final List<PackResources> packs;
    private final PackType type;
    
    public SimpleReloadableResourceManager(final PackType abi) {
        this.namespacedPacks = (Map<String, FallbackResourceManager>)Maps.newHashMap();
        this.listeners = (List<PreparableReloadListener>)Lists.newArrayList();
        this.recentlyRegistered = (List<PreparableReloadListener>)Lists.newArrayList();
        this.namespaces = (Set<String>)Sets.newLinkedHashSet();
        this.packs = (List<PackResources>)Lists.newArrayList();
        this.type = abi;
    }
    
    public void add(final PackResources abh) {
        this.packs.add(abh);
        for (final String string4 : abh.getNamespaces(this.type)) {
            this.namespaces.add(string4);
            FallbackResourceManager abz5 = (FallbackResourceManager)this.namespacedPacks.get(string4);
            if (abz5 == null) {
                abz5 = new FallbackResourceManager(this.type, string4);
                this.namespacedPacks.put(string4, abz5);
            }
            abz5.add(abh);
        }
    }
    
    public Set<String> getNamespaces() {
        return this.namespaces;
    }
    
    public Resource getResource(final ResourceLocation vk) throws IOException {
        final ResourceManager acf3 = (ResourceManager)this.namespacedPacks.get(vk.getNamespace());
        if (acf3 != null) {
            return acf3.getResource(vk);
        }
        throw new FileNotFoundException(vk.toString());
    }
    
    public boolean hasResource(final ResourceLocation vk) {
        final ResourceManager acf3 = (ResourceManager)this.namespacedPacks.get(vk.getNamespace());
        return acf3 != null && acf3.hasResource(vk);
    }
    
    public List<Resource> getResources(final ResourceLocation vk) throws IOException {
        final ResourceManager acf3 = (ResourceManager)this.namespacedPacks.get(vk.getNamespace());
        if (acf3 != null) {
            return acf3.getResources(vk);
        }
        throw new FileNotFoundException(vk.toString());
    }
    
    public Collection<ResourceLocation> listResources(final String string, final Predicate<String> predicate) {
        final Set<ResourceLocation> set4 = (Set<ResourceLocation>)Sets.newHashSet();
        for (final FallbackResourceManager abz6 : this.namespacedPacks.values()) {
            set4.addAll((Collection)abz6.listResources(string, predicate));
        }
        final List<ResourceLocation> list5 = (List<ResourceLocation>)Lists.newArrayList((Iterable)set4);
        Collections.sort((List)list5);
        return (Collection<ResourceLocation>)list5;
    }
    
    private void clear() {
        this.namespacedPacks.clear();
        this.namespaces.clear();
        this.packs.forEach(PackResources::close);
        this.packs.clear();
    }
    
    public void close() {
        this.clear();
    }
    
    public void registerReloadListener(final PreparableReloadListener aca) {
        this.listeners.add(aca);
        this.recentlyRegistered.add(aca);
    }
    
    protected ReloadInstance createReload(final Executor executor1, final Executor executor2, final List<PreparableReloadListener> list, final CompletableFuture<Unit> completableFuture) {
        ReloadInstance acc6;
        if (SimpleReloadableResourceManager.LOGGER.isDebugEnabled()) {
            acc6 = new ProfiledReloadInstance(this, (List<PreparableReloadListener>)Lists.newArrayList((Iterable)list), executor1, executor2, completableFuture);
        }
        else {
            acc6 = SimpleReloadInstance.of(this, (List<PreparableReloadListener>)Lists.newArrayList((Iterable)list), executor1, executor2, completableFuture);
        }
        this.recentlyRegistered.clear();
        return acc6;
    }
    
    public ReloadInstance createFullReload(final Executor executor1, final Executor executor2, final CompletableFuture<Unit> completableFuture, final List<PackResources> list) {
        this.clear();
        SimpleReloadableResourceManager.LOGGER.info("Reloading ResourceManager: {}", new Supplier[] { () -> list.stream().map(PackResources::getName).collect(Collectors.joining(", ")) });
        for (final PackResources abh7 : list) {
            try {
                this.add(abh7);
            }
            catch (Exception exception8) {
                SimpleReloadableResourceManager.LOGGER.error("Failed to add resource pack {}", abh7.getName(), exception8);
                return new FailingReloadInstance(new ResourcePackLoadingFailure(abh7, (Throwable)exception8));
            }
        }
        return this.createReload(executor1, executor2, this.listeners, completableFuture);
    }
    
    public Stream<PackResources> listPacks() {
        return (Stream<PackResources>)this.packs.stream();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class ResourcePackLoadingFailure extends RuntimeException {
        private final PackResources pack;
        
        public ResourcePackLoadingFailure(final PackResources abh, final Throwable throwable) {
            super(abh.getName(), throwable);
            this.pack = abh;
        }
        
        public PackResources getPack() {
            return this.pack;
        }
    }
    
    static class FailingReloadInstance implements ReloadInstance {
        private final ResourcePackLoadingFailure exception;
        private final CompletableFuture<Unit> failedFuture;
        
        public FailingReloadInstance(final ResourcePackLoadingFailure b) {
            this.exception = b;
            (this.failedFuture = (CompletableFuture<Unit>)new CompletableFuture()).completeExceptionally((Throwable)b);
        }
        
        public CompletableFuture<Unit> done() {
            return this.failedFuture;
        }
        
        public float getActualProgress() {
            return 0.0f;
        }
        
        public boolean isApplying() {
            return false;
        }
        
        public boolean isDone() {
            return true;
        }
        
        public void checkExceptions() {
            throw this.exception;
        }
    }
}
