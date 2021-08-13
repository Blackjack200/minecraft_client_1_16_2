package net.minecraft.data.tags;

import java.util.stream.Stream;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedWriter;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import com.google.gson.JsonElement;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;
import java.nio.file.Path;
import java.util.function.Function;
import net.minecraft.tags.SetTag;
import net.minecraft.data.HashCache;
import com.google.common.collect.Maps;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.data.DataProvider;

public abstract class TagsProvider<T> implements DataProvider {
    private static final Logger LOGGER;
    private static final Gson GSON;
    protected final DataGenerator generator;
    protected final Registry<T> registry;
    private final Map<ResourceLocation, Tag.Builder> builders;
    
    protected TagsProvider(final DataGenerator hl, final Registry<T> gm) {
        this.builders = (Map<ResourceLocation, Tag.Builder>)Maps.newLinkedHashMap();
        this.generator = hl;
        this.registry = gm;
    }
    
    protected abstract void addTags();
    
    public void run(final HashCache hn) {
        this.builders.clear();
        this.addTags();
        final Tag<T> aej3 = SetTag.empty();
        final Function<ResourceLocation, Tag<T>> function4 = (Function<ResourceLocation, Tag<T>>)(vk -> this.builders.containsKey(vk) ? aej3 : null);
        final Function<ResourceLocation, T> function5 = (Function<ResourceLocation, T>)(vk -> this.registry.getOptional(vk).orElse(null));
        this.builders.forEach((vk, a) -> {
            final List<Tag.BuilderEntry> list7 = (List<Tag.BuilderEntry>)a.getUnresolvedEntries((java.util.function.Function<ResourceLocation, Tag<Object>>)function4, (java.util.function.Function<ResourceLocation, Object>)function5).collect(Collectors.toList());
            if (!list7.isEmpty()) {
                throw new IllegalArgumentException(String.format("Couldn't define tag %s as it is missing following references: %s", new Object[] { vk, list7.stream().map(Objects::toString).collect(Collectors.joining(",")) }));
            }
            final JsonObject jsonObject8 = a.serializeToJson();
            final Path path9 = this.getPath(vk);
            try {
                final String string10 = TagsProvider.GSON.toJson((JsonElement)jsonObject8);
                final String string11 = TagsProvider.SHA1.hashUnencodedChars((CharSequence)string10).toString();
                if (!Objects.equals(hn.getHash(path9), string11) || !Files.exists(path9, new LinkOption[0])) {
                    Files.createDirectories(path9.getParent(), new FileAttribute[0]);
                    try (final BufferedWriter bufferedWriter12 = Files.newBufferedWriter(path9, new OpenOption[0])) {
                        bufferedWriter12.write(string10);
                    }
                }
                hn.putNew(path9, string11);
            }
            catch (IOException iOException10) {
                TagsProvider.LOGGER.error("Couldn't save tags to {}", path9, iOException10);
            }
        });
    }
    
    protected abstract Path getPath(final ResourceLocation vk);
    
    protected TagAppender<T> tag(final Tag.Named<T> e) {
        final Tag.Builder a3 = this.getOrCreateRawBuilder(e);
        return new TagAppender<T>(a3, (Registry)this.registry, "vanilla");
    }
    
    protected Tag.Builder getOrCreateRawBuilder(final Tag.Named<T> e) {
        return (Tag.Builder)this.builders.computeIfAbsent(e.getName(), vk -> new Tag.Builder());
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
    
    public static class TagAppender<T> {
        private final Tag.Builder builder;
        private final Registry<T> registry;
        private final String source;
        
        private TagAppender(final Tag.Builder a, final Registry<T> gm, final String string) {
            this.builder = a;
            this.registry = gm;
            this.source = string;
        }
        
        public TagAppender<T> add(final T object) {
            this.builder.addElement(this.registry.getKey(object), this.source);
            return this;
        }
        
        public TagAppender<T> addTag(final Tag.Named<T> e) {
            this.builder.addTag(e.getName(), this.source);
            return this;
        }
        
        @SafeVarargs
        public final TagAppender<T> add(final T... arr) {
            Stream.of((Object[])arr).map(this.registry::getKey).forEach(vk -> this.builder.addElement(vk, this.source));
            return this;
        }
    }
}
