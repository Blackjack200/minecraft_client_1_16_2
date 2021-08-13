package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import java.io.FileNotFoundException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import java.io.Reader;
import net.minecraft.util.GsonHelper;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import java.io.File;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class AssetIndex {
    protected static final Logger LOGGER;
    private final Map<String, File> rootFiles;
    private final Map<ResourceLocation, File> namespacedFiles;
    
    protected AssetIndex() {
        this.rootFiles = (Map<String, File>)Maps.newHashMap();
        this.namespacedFiles = (Map<ResourceLocation, File>)Maps.newHashMap();
    }
    
    public AssetIndex(final File file, final String string) {
        this.rootFiles = (Map<String, File>)Maps.newHashMap();
        this.namespacedFiles = (Map<ResourceLocation, File>)Maps.newHashMap();
        final File file2 = new File(file, "objects");
        final File file3 = new File(file, "indexes/" + string + ".json");
        BufferedReader bufferedReader6 = null;
        try {
            bufferedReader6 = Files.newReader(file3, StandardCharsets.UTF_8);
            final JsonObject jsonObject7 = GsonHelper.parse((Reader)bufferedReader6);
            final JsonObject jsonObject8 = GsonHelper.getAsJsonObject(jsonObject7, "objects", (JsonObject)null);
            if (jsonObject8 != null) {
                for (final Map.Entry<String, JsonElement> entry10 : jsonObject8.entrySet()) {
                    final JsonObject jsonObject9 = (JsonObject)entry10.getValue();
                    final String string2 = (String)entry10.getKey();
                    final String[] arr13 = string2.split("/", 2);
                    final String string3 = GsonHelper.getAsString(jsonObject9, "hash");
                    final File file4 = new File(file2, string3.substring(0, 2) + "/" + string3);
                    if (arr13.length == 1) {
                        this.rootFiles.put(arr13[0], file4);
                    }
                    else {
                        this.namespacedFiles.put(new ResourceLocation(arr13[0], arr13[1]), file4);
                    }
                }
            }
        }
        catch (JsonParseException jsonParseException7) {
            AssetIndex.LOGGER.error("Unable to parse resource index file: {}", file3);
        }
        catch (FileNotFoundException fileNotFoundException7) {
            AssetIndex.LOGGER.error("Can't find the resource index file: {}", file3);
        }
        finally {
            IOUtils.closeQuietly((Reader)bufferedReader6);
        }
    }
    
    @Nullable
    public File getFile(final ResourceLocation vk) {
        return (File)this.namespacedFiles.get(vk);
    }
    
    @Nullable
    public File getRootFile(final String string) {
        return (File)this.rootFiles.get(string);
    }
    
    public Collection<ResourceLocation> getFiles(final String string1, final String string2, final int integer, final Predicate<String> predicate) {
        return (Collection<ResourceLocation>)this.namespacedFiles.keySet().stream().filter(vk -> {
            final String string3 = vk.getPath();
            return vk.getNamespace().equals(string2) && !string3.endsWith(".mcmeta") && string3.startsWith(string1 + "/") && predicate.test(string3);
        }).collect(Collectors.toList());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
