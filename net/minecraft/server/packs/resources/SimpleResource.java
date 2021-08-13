package net.minecraft.server.packs.resources;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import net.minecraft.util.GsonHelper;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import java.io.InputStream;
import net.minecraft.resources.ResourceLocation;

public class SimpleResource implements Resource {
    private final String sourceName;
    private final ResourceLocation location;
    private final InputStream resourceStream;
    private final InputStream metadataStream;
    private boolean triedMetadata;
    private JsonObject metadata;
    
    public SimpleResource(final String string, final ResourceLocation vk, final InputStream inputStream3, @Nullable final InputStream inputStream4) {
        this.sourceName = string;
        this.location = vk;
        this.resourceStream = inputStream3;
        this.metadataStream = inputStream4;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    public InputStream getInputStream() {
        return this.resourceStream;
    }
    
    public boolean hasMetadata() {
        return this.metadataStream != null;
    }
    
    @Nullable
    public <T> T getMetadata(final MetadataSectionSerializer<T> abl) {
        if (!this.hasMetadata()) {
            return null;
        }
        if (this.metadata == null && !this.triedMetadata) {
            this.triedMetadata = true;
            BufferedReader bufferedReader3 = null;
            try {
                bufferedReader3 = new BufferedReader((Reader)new InputStreamReader(this.metadataStream, StandardCharsets.UTF_8));
                this.metadata = GsonHelper.parse((Reader)bufferedReader3);
            }
            finally {
                IOUtils.closeQuietly((Reader)bufferedReader3);
            }
        }
        if (this.metadata == null) {
            return null;
        }
        final String string3 = abl.getMetadataSectionName();
        return this.metadata.has(string3) ? abl.fromJson(GsonHelper.getAsJsonObject(this.metadata, string3)) : null;
    }
    
    public String getSourceName() {
        return this.sourceName;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleResource)) {
            return false;
        }
        final SimpleResource acl3 = (SimpleResource)object;
        Label_0054: {
            if (this.location != null) {
                if (this.location.equals(acl3.location)) {
                    break Label_0054;
                }
            }
            else if (acl3.location == null) {
                break Label_0054;
            }
            return false;
        }
        if (this.sourceName != null) {
            if (this.sourceName.equals(acl3.sourceName)) {
                return true;
            }
        }
        else if (acl3.sourceName == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int integer2 = (this.sourceName != null) ? this.sourceName.hashCode() : 0;
        integer2 = 31 * integer2 + ((this.location != null) ? this.location.hashCode() : 0);
        return integer2;
    }
    
    public void close() throws IOException {
        this.resourceStream.close();
        if (this.metadataStream != null) {
            this.metadataStream.close();
        }
    }
}
