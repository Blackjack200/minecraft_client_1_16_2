package net.minecraft.server.packs.resources;

import org.apache.logging.log4j.LogManager;
import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import com.google.common.collect.Maps;
import net.minecraft.util.profiling.ProfilerFiller;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public abstract class SimpleJsonResourceReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {
    private static final Logger LOGGER;
    private static final int PATH_SUFFIX_LENGTH;
    private final Gson gson;
    private final String directory;
    
    public SimpleJsonResourceReloadListener(final Gson gson, final String string) {
        this.gson = gson;
        this.directory = string;
    }
    
    @Override
    protected Map<ResourceLocation, JsonElement> prepare(final ResourceManager acf, final ProfilerFiller ant) {
        final Map<ResourceLocation, JsonElement> map4 = (Map<ResourceLocation, JsonElement>)Maps.newHashMap();
        final int integer5 = this.directory.length() + 1;
        for (final ResourceLocation vk7 : acf.listResources(this.directory, (Predicate<String>)(string -> string.endsWith(".json")))) {
            final String string8 = vk7.getPath();
            final ResourceLocation vk8 = new ResourceLocation(vk7.getNamespace(), string8.substring(integer5, string8.length() - SimpleJsonResourceReloadListener.PATH_SUFFIX_LENGTH));
            try (final Resource ace10 = acf.getResource(vk7);
                 final InputStream inputStream12 = ace10.getInputStream();
                 final Reader reader14 = (Reader)new BufferedReader((Reader)new InputStreamReader(inputStream12, StandardCharsets.UTF_8))) {
                final JsonElement jsonElement16 = GsonHelper.<JsonElement>fromJson(this.gson, reader14, JsonElement.class);
                if (jsonElement16 != null) {
                    final JsonElement jsonElement17 = (JsonElement)map4.put(vk8, jsonElement16);
                    if (jsonElement17 != null) {
                        throw new IllegalStateException(new StringBuilder().append("Duplicate data file ignored with ID ").append(vk8).toString());
                    }
                }
                else {
                    SimpleJsonResourceReloadListener.LOGGER.error("Couldn't load data file {} from {} as it's null or empty", vk8, vk7);
                }
            }
            catch (JsonParseException | IllegalArgumentException | IOException ex2) {
                final Exception ex;
                final Exception exception10 = ex;
                SimpleJsonResourceReloadListener.LOGGER.error("Couldn't parse data file {} from {}", vk8, vk7, exception10);
            }
        }
        return map4;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PATH_SUFFIX_LENGTH = ".json".length();
    }
}
