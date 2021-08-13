package net.minecraft.data;

import org.apache.logging.log4j.LogManager;
import java.nio.file.LinkOption;
import java.nio.file.FileVisitOption;
import java.util.stream.Stream;
import java.util.Objects;
import javax.annotation.Nullable;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.Collection;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import com.google.common.base.Charsets;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import java.util.Map;
import java.nio.file.Path;
import org.apache.logging.log4j.Logger;

public class HashCache {
    private static final Logger LOGGER;
    private final Path path;
    private final Path cachePath;
    private int hits;
    private final Map<Path, String> oldCache;
    private final Map<Path, String> newCache;
    private final Set<Path> keep;
    
    public HashCache(final Path path, final String string) throws IOException {
        this.oldCache = (Map<Path, String>)Maps.newHashMap();
        this.newCache = (Map<Path, String>)Maps.newHashMap();
        this.keep = (Set<Path>)Sets.newHashSet();
        this.path = path;
        final Path path2 = path.resolve(".cache");
        Files.createDirectories(path2, new FileAttribute[0]);
        this.cachePath = path2.resolve(string);
        this.walkOutputFiles().forEach(path -> {
            final String s = (String)this.oldCache.put(path, "");
        });
        if (Files.isReadable(this.cachePath)) {
            IOUtils.readLines(Files.newInputStream(this.cachePath, new OpenOption[0]), Charsets.UTF_8).forEach(string -> {
                final int integer4 = string.indexOf(32);
                this.oldCache.put(path.resolve(string.substring(integer4 + 1)), string.substring(0, integer4));
            });
        }
    }
    
    public void purgeStaleAndWrite() throws IOException {
        this.removeStale();
        Writer writer2;
        try {
            writer2 = (Writer)Files.newBufferedWriter(this.cachePath, new OpenOption[0]);
        }
        catch (IOException iOException3) {
            HashCache.LOGGER.warn("Unable write cachefile {}: {}", this.cachePath, iOException3.toString());
            return;
        }
        IOUtils.writeLines((Collection)this.newCache.entrySet().stream().map(entry -> (String)entry.getValue() + ' ' + this.path.relativize((Path)entry.getKey())).collect(Collectors.toList()), System.lineSeparator(), writer2);
        writer2.close();
        HashCache.LOGGER.debug("Caching: cache hits: {}, created: {} removed: {}", this.hits, (this.newCache.size() - this.hits), this.oldCache.size());
    }
    
    @Nullable
    public String getHash(final Path path) {
        return (String)this.oldCache.get(path);
    }
    
    public void putNew(final Path path, final String string) {
        this.newCache.put(path, string);
        if (Objects.equals(this.oldCache.remove(path), string)) {
            ++this.hits;
        }
    }
    
    public boolean had(final Path path) {
        return this.oldCache.containsKey(path);
    }
    
    public void keep(final Path path) {
        this.keep.add(path);
    }
    
    private void removeStale() throws IOException {
        this.walkOutputFiles().forEach(path -> {
            if (this.had(path) && !this.keep.contains(path)) {
                try {
                    Files.delete(path);
                }
                catch (IOException iOException3) {
                    HashCache.LOGGER.debug("Unable to delete: {} ({})", path, iOException3.toString());
                }
            }
        });
    }
    
    private Stream<Path> walkOutputFiles() throws IOException {
        return (Stream<Path>)Files.walk(this.path, new FileVisitOption[0]).filter(path -> !Objects.equals(this.cachePath, path) && !Files.isDirectory(path, new LinkOption[0]));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
