package net.minecraft.tags;

import org.apache.logging.log4j.LogManager;
import java.io.InputStream;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.packs.resources.Resource;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class TagLoader<T> {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final int PATH_SUFFIX_LENGTH;
    private final Function<ResourceLocation, Optional<T>> idToValue;
    private final String directory;
    private final String name;
    
    public TagLoader(final Function<ResourceLocation, Optional<T>> function, final String string2, final String string3) {
        this.idToValue = function;
        this.directory = string2;
        this.name = string3;
    }
    
    public CompletableFuture<Map<ResourceLocation, Tag.Builder>> prepare(final ResourceManager acf, final Executor executor) {
        return (CompletableFuture<Map<ResourceLocation, Tag.Builder>>)CompletableFuture.supplyAsync(() -> {
            final Map<ResourceLocation, Tag.Builder> map3 = (Map<ResourceLocation, Tag.Builder>)Maps.newHashMap();
            for (final ResourceLocation vk5 : acf.listResources(this.directory, (Predicate<String>)(string -> string.endsWith(".json")))) {
                final String string6 = vk5.getPath();
                final ResourceLocation vk6 = new ResourceLocation(vk5.getNamespace(), string6.substring(this.directory.length() + 1, string6.length() - TagLoader.PATH_SUFFIX_LENGTH));
                try {
                    for (final Resource ace9 : acf.getResources(vk5)) {
                        try (final InputStream inputStream10 = ace9.getInputStream();
                             final Reader reader12 = (Reader)new BufferedReader((Reader)new InputStreamReader(inputStream10, StandardCharsets.UTF_8))) {
                            final JsonObject jsonObject14 = GsonHelper.<JsonObject>fromJson(TagLoader.GSON, reader12, JsonObject.class);
                            if (jsonObject14 == null) {
                                TagLoader.LOGGER.error("Couldn't load {} tag list {} from {} in data pack {} as it is empty or null", this.name, vk6, vk5, ace9.getSourceName());
                            }
                            else {
                                ((Tag.Builder)map3.computeIfAbsent(vk6, vk -> Tag.Builder.tag())).addFromJson(jsonObject14, ace9.getSourceName());
                            }
                        }
                        catch (IOException ex) {}
                        catch (RuntimeException exception10) {
                            TagLoader.LOGGER.error("Couldn't read {} tag list {} from {} in data pack {}", this.name, vk6, vk5, ace9.getSourceName(), exception10);
                        }
                        finally {
                            IOUtils.closeQuietly((Closeable)ace9);
                        }
                    }
                }
                catch (IOException iOException8) {
                    TagLoader.LOGGER.error("Couldn't read {} tag list {} from {}", this.name, vk6, vk5, iOException8);
                }
            }
            return map3;
        }, executor);
    }
    
    public TagCollection<T> load(final Map<ResourceLocation, Tag.Builder> map) {
        final Map<ResourceLocation, Tag<T>> map2 = (Map<ResourceLocation, Tag<T>>)Maps.newHashMap();
        final Function<ResourceLocation, Tag<T>> function4 = (Function<ResourceLocation, Tag<T>>)map2::get;
        final Function<ResourceLocation, T> function5 = (Function<ResourceLocation, T>)(vk -> ((Optional)this.idToValue.apply(vk)).orElse(null));
        while (!map.isEmpty()) {
            boolean boolean6 = false;
            final Iterator<Map.Entry<ResourceLocation, Tag.Builder>> iterator7 = (Iterator<Map.Entry<ResourceLocation, Tag.Builder>>)map.entrySet().iterator();
            while (iterator7.hasNext()) {
                final Map.Entry<ResourceLocation, Tag.Builder> entry8 = (Map.Entry<ResourceLocation, Tag.Builder>)iterator7.next();
                final Optional<Tag<T>> optional9 = ((Tag.Builder)entry8.getValue()).<T>build(function4, function5);
                if (optional9.isPresent()) {
                    map2.put(entry8.getKey(), optional9.get());
                    iterator7.remove();
                    boolean6 = true;
                }
            }
            if (!boolean6) {
                break;
            }
        }
        map.forEach((vk, a) -> TagLoader.LOGGER.error("Couldn't load {} tag {} as it is missing following references: {}", this.name, vk, a.getUnresolvedEntries((java.util.function.Function<ResourceLocation, Tag<Object>>)function4, (java.util.function.Function<ResourceLocation, Object>)function5).map(Objects::toString).collect(Collectors.joining(","))));
        return TagCollection.<T>of(map2);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new Gson();
        PATH_SUFFIX_LENGTH = ".json".length();
    }
}
