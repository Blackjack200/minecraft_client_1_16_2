package net.minecraft.server.packs;

import javax.annotation.Nullable;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import java.util.Set;
import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;
import java.io.InputStream;

public interface PackResources extends AutoCloseable {
    InputStream getRootResource(final String string) throws IOException;
    
    InputStream getResource(final PackType abi, final ResourceLocation vk) throws IOException;
    
    Collection<ResourceLocation> getResources(final PackType abi, final String string2, final String string3, final int integer, final Predicate<String> predicate);
    
    boolean hasResource(final PackType abi, final ResourceLocation vk);
    
    Set<String> getNamespaces(final PackType abi);
    
    @Nullable
     <T> T getMetadataSection(final MetadataSectionSerializer<T> abl) throws IOException;
    
    String getName();
    
    void close();
}
