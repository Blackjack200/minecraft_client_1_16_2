package net.minecraft.server.packs;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.resources.ResourceLocation;
import java.io.File;
import org.apache.logging.log4j.Logger;

public abstract class AbstractPackResources implements PackResources {
    private static final Logger LOGGER;
    protected final File file;
    
    public AbstractPackResources(final File file) {
        this.file = file;
    }
    
    private static String getPathFromLocation(final PackType abi, final ResourceLocation vk) {
        return String.format("%s/%s/%s", new Object[] { abi.getDirectory(), vk.getNamespace(), vk.getPath() });
    }
    
    protected static String getRelativePath(final File file1, final File file2) {
        return file1.toURI().relativize(file2.toURI()).getPath();
    }
    
    public InputStream getResource(final PackType abi, final ResourceLocation vk) throws IOException {
        return this.getResource(getPathFromLocation(abi, vk));
    }
    
    public boolean hasResource(final PackType abi, final ResourceLocation vk) {
        return this.hasResource(getPathFromLocation(abi, vk));
    }
    
    protected abstract InputStream getResource(final String string) throws IOException;
    
    public InputStream getRootResource(final String string) throws IOException {
        if (string.contains("/") || string.contains("\\")) {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
        return this.getResource(string);
    }
    
    protected abstract boolean hasResource(final String string);
    
    protected void logWarning(final String string) {
        AbstractPackResources.LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", string, this.file);
    }
    
    @Nullable
    public <T> T getMetadataSection(final MetadataSectionSerializer<T> abl) throws IOException {
        try (final InputStream inputStream3 = this.getResource("pack.mcmeta")) {
            return AbstractPackResources.<T>getMetadataFromStream(abl, inputStream3);
        }
    }
    
    @Nullable
    public static <T> T getMetadataFromStream(final MetadataSectionSerializer<T> abl, final InputStream inputStream) {
        JsonObject jsonObject3;
        try (final BufferedReader bufferedReader4 = new BufferedReader((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            jsonObject3 = GsonHelper.parse((Reader)bufferedReader4);
        }
        catch (IOException | JsonParseException ex2) {
            final Exception ex;
            final Exception exception4 = ex;
            AbstractPackResources.LOGGER.error("Couldn't load {} metadata", abl.getMetadataSectionName(), exception4);
            return null;
        }
        if (!jsonObject3.has(abl.getMetadataSectionName())) {
            return null;
        }
        try {
            return abl.fromJson(GsonHelper.getAsJsonObject(jsonObject3, abl.getMetadataSectionName()));
        }
        catch (JsonParseException jsonParseException4) {
            AbstractPackResources.LOGGER.error("Couldn't load {} metadata", abl.getMetadataSectionName(), jsonParseException4);
            return null;
        }
    }
    
    public String getName() {
        return this.file.getName();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
