package net.minecraft.server.packs.resources;

import com.google.common.collect.ImmutableList;
import java.io.FileNotFoundException;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.packs.PackResources;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.List;
import java.io.IOException;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;

public interface ResourceManager {
    Set<String> getNamespaces();
    
    Resource getResource(final ResourceLocation vk) throws IOException;
    
    boolean hasResource(final ResourceLocation vk);
    
    List<Resource> getResources(final ResourceLocation vk) throws IOException;
    
    Collection<ResourceLocation> listResources(final String string, final Predicate<String> predicate);
    
    Stream<PackResources> listPacks();
    
    public enum Empty implements ResourceManager {
        INSTANCE;
        
        public Set<String> getNamespaces() {
            return (Set<String>)ImmutableSet.of();
        }
        
        public Resource getResource(final ResourceLocation vk) throws IOException {
            throw new FileNotFoundException(vk.toString());
        }
        
        public boolean hasResource(final ResourceLocation vk) {
            return false;
        }
        
        public List<Resource> getResources(final ResourceLocation vk) {
            return (List<Resource>)ImmutableList.of();
        }
        
        public Collection<ResourceLocation> listResources(final String string, final Predicate<String> predicate) {
            return (Collection<ResourceLocation>)ImmutableSet.of();
        }
        
        public Stream<PackResources> listPacks() {
            return (Stream<PackResources>)Stream.of((Object[])new PackResources[0]);
        }
    }
}
