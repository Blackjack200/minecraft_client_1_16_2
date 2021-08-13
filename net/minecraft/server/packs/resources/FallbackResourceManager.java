package net.minecraft.server.packs.resources;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import org.apache.logging.log4j.LogManager;
import java.util.stream.Stream;
import java.util.Collections;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.google.common.collect.Lists;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PackResources;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements ResourceManager {
    private static final Logger LOGGER;
    protected final List<PackResources> fallbacks;
    private final PackType type;
    private final String namespace;
    
    public FallbackResourceManager(final PackType abi, final String string) {
        this.fallbacks = (List<PackResources>)Lists.newArrayList();
        this.type = abi;
        this.namespace = string;
    }
    
    public void add(final PackResources abh) {
        this.fallbacks.add(abh);
    }
    
    public Set<String> getNamespaces() {
        return (Set<String>)ImmutableSet.of(this.namespace);
    }
    
    public Resource getResource(final ResourceLocation vk) throws IOException {
        this.validateLocation(vk);
        PackResources abh3 = null;
        final ResourceLocation vk2 = getMetadataLocation(vk);
        for (int integer5 = this.fallbacks.size() - 1; integer5 >= 0; --integer5) {
            final PackResources abh4 = (PackResources)this.fallbacks.get(integer5);
            if (abh3 == null && abh4.hasResource(this.type, vk2)) {
                abh3 = abh4;
            }
            if (abh4.hasResource(this.type, vk)) {
                InputStream inputStream7 = null;
                if (abh3 != null) {
                    inputStream7 = this.getWrappedResource(vk2, abh3);
                }
                return new SimpleResource(abh4.getName(), vk, this.getWrappedResource(vk, abh4), inputStream7);
            }
        }
        throw new FileNotFoundException(vk.toString());
    }
    
    public boolean hasResource(final ResourceLocation vk) {
        if (!this.isValidLocation(vk)) {
            return false;
        }
        for (int integer3 = this.fallbacks.size() - 1; integer3 >= 0; --integer3) {
            final PackResources abh4 = (PackResources)this.fallbacks.get(integer3);
            if (abh4.hasResource(this.type, vk)) {
                return true;
            }
        }
        return false;
    }
    
    protected InputStream getWrappedResource(final ResourceLocation vk, final PackResources abh) throws IOException {
        final InputStream inputStream4 = abh.getResource(this.type, vk);
        return (InputStream)(FallbackResourceManager.LOGGER.isDebugEnabled() ? new LeakedResourceWarningInputStream(inputStream4, vk, abh.getName()) : inputStream4);
    }
    
    private void validateLocation(final ResourceLocation vk) throws IOException {
        if (!this.isValidLocation(vk)) {
            throw new IOException(new StringBuilder().append("Invalid relative path to resource: ").append(vk).toString());
        }
    }
    
    private boolean isValidLocation(final ResourceLocation vk) {
        return !vk.getPath().contains("..");
    }
    
    public List<Resource> getResources(final ResourceLocation vk) throws IOException {
        this.validateLocation(vk);
        final List<Resource> list3 = (List<Resource>)Lists.newArrayList();
        final ResourceLocation vk2 = getMetadataLocation(vk);
        for (final PackResources abh6 : this.fallbacks) {
            if (abh6.hasResource(this.type, vk)) {
                final InputStream inputStream7 = abh6.hasResource(this.type, vk2) ? this.getWrappedResource(vk2, abh6) : null;
                list3.add(new SimpleResource(abh6.getName(), vk, this.getWrappedResource(vk, abh6), inputStream7));
            }
        }
        if (list3.isEmpty()) {
            throw new FileNotFoundException(vk.toString());
        }
        return list3;
    }
    
    public Collection<ResourceLocation> listResources(final String string, final Predicate<String> predicate) {
        final List<ResourceLocation> list4 = (List<ResourceLocation>)Lists.newArrayList();
        for (final PackResources abh6 : this.fallbacks) {
            list4.addAll((Collection)abh6.getResources(this.type, this.namespace, string, Integer.MAX_VALUE, predicate));
        }
        Collections.sort((List)list4);
        return (Collection<ResourceLocation>)list4;
    }
    
    public Stream<PackResources> listPacks() {
        return (Stream<PackResources>)this.fallbacks.stream();
    }
    
    static ResourceLocation getMetadataLocation(final ResourceLocation vk) {
        return new ResourceLocation(vk.getNamespace(), vk.getPath() + ".mcmeta");
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class LeakedResourceWarningInputStream extends FilterInputStream {
        private final String message;
        private boolean closed;
        
        public LeakedResourceWarningInputStream(final InputStream inputStream, final ResourceLocation vk, final String string) {
            super(inputStream);
            final ByteArrayOutputStream byteArrayOutputStream5 = new ByteArrayOutputStream();
            new Exception().printStackTrace(new PrintStream((OutputStream)byteArrayOutputStream5));
            this.message = new StringBuilder().append("Leaked resource: '").append(vk).append("' loaded from pack: '").append(string).append("'\n").append(byteArrayOutputStream5).toString();
        }
        
        public void close() throws IOException {
            super.close();
            this.closed = true;
        }
        
        protected void finalize() throws Throwable {
            if (!this.closed) {
                FallbackResourceManager.LOGGER.warn(this.message);
            }
            super.finalize();
        }
    }
}
