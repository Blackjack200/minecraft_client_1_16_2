package net.minecraft.server.packs.resources;

import javax.annotation.Nullable;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import java.io.InputStream;
import net.minecraft.resources.ResourceLocation;
import java.io.Closeable;

public interface Resource extends Closeable {
    ResourceLocation getLocation();
    
    InputStream getInputStream();
    
    @Nullable
     <T> T getMetadata(final MetadataSectionSerializer<T> abl);
    
    String getSourceName();
}
