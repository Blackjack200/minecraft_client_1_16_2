package net.minecraft.data.structures;

import org.apache.logging.log4j.LogManager;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.LinkOption;
import org.apache.commons.io.FileUtils;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.OutputStream;
import net.minecraft.nbt.NbtIo;
import java.io.ByteArrayOutputStream;
import net.minecraft.nbt.TagParser;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import net.minecraft.data.HashCache;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.data.DataGenerator;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;
import java.nio.file.Path;
import net.minecraft.data.DataProvider;

public class SnbtToNbt implements DataProvider {
    @Nullable
    private static final Path dumpSnbtTo;
    private static final Logger LOGGER;
    private final DataGenerator generator;
    private final List<Filter> filters;
    
    public SnbtToNbt(final DataGenerator hl) {
        this.filters = (List<Filter>)Lists.newArrayList();
        this.generator = hl;
    }
    
    public SnbtToNbt addFilter(final Filter a) {
        this.filters.add(a);
        return this;
    }
    
    private CompoundTag applyFilters(final String string, final CompoundTag md) {
        CompoundTag md2 = md;
        for (final Filter a6 : this.filters) {
            md2 = a6.apply(string, md2);
        }
        return md2;
    }
    
    public void run(final HashCache hn) throws IOException {
        final Path path3 = this.generator.getOutputFolder();
        final List<CompletableFuture<TaskResult>> list4 = (List<CompletableFuture<TaskResult>>)Lists.newArrayList();
        for (final Path path4 : this.generator.getInputFolders()) {
            Files.walk(path4, new FileVisitOption[0]).filter(path -> path.toString().endsWith(".snbt")).forEach(path3 -> list4.add(CompletableFuture.supplyAsync(() -> this.readStructure(path3, this.getName(path4, path3)), Util.backgroundExecutor())));
        }
        ((List)Util.sequence((java.util.List<? extends java.util.concurrent.CompletableFuture<?>>)list4).join()).stream().filter(Objects::nonNull).forEach(b -> this.storeStructureIfChanged(hn, b, path3));
    }
    
    public String getName() {
        return "SNBT -> NBT";
    }
    
    private String getName(final Path path1, final Path path2) {
        final String string4 = path1.relativize(path2).toString().replaceAll("\\\\", "/");
        return string4.substring(0, string4.length() - ".snbt".length());
    }
    
    @Nullable
    private TaskResult readStructure(final Path path, final String string) {
        try (final BufferedReader bufferedReader4 = Files.newBufferedReader(path)) {
            final String string2 = IOUtils.toString((Reader)bufferedReader4);
            final CompoundTag md7 = this.applyFilters(string, TagParser.parseTag(string2));
            final ByteArrayOutputStream byteArrayOutputStream8 = new ByteArrayOutputStream();
            NbtIo.writeCompressed(md7, (OutputStream)byteArrayOutputStream8);
            final byte[] arr9 = byteArrayOutputStream8.toByteArray();
            final String string3 = SnbtToNbt.SHA1.hashBytes(arr9).toString();
            String string4;
            if (SnbtToNbt.dumpSnbtTo != null) {
                string4 = md7.getPrettyDisplay("    ", 0).getString() + "\n";
            }
            else {
                string4 = null;
            }
            return new TaskResult(string, arr9, string4, string3);
        }
        catch (CommandSyntaxException commandSyntaxException4) {
            SnbtToNbt.LOGGER.error("Couldn't convert {} from SNBT to NBT at {} as it's invalid SNBT", string, path, commandSyntaxException4);
        }
        catch (IOException iOException4) {
            SnbtToNbt.LOGGER.error("Couldn't convert {} from SNBT to NBT at {}", string, path, iOException4);
        }
        return null;
    }
    
    private void storeStructureIfChanged(final HashCache hn, final TaskResult b, final Path path) {
        if (b.snbtPayload != null) {
            final Path path2 = SnbtToNbt.dumpSnbtTo.resolve(b.name + ".snbt");
            try {
                FileUtils.write(path2.toFile(), (CharSequence)b.snbtPayload, StandardCharsets.UTF_8);
            }
            catch (IOException iOException6) {
                SnbtToNbt.LOGGER.error("Couldn't write structure SNBT {} at {}", b.name, path2, iOException6);
            }
        }
        final Path path2 = path.resolve(b.name + ".nbt");
        try {
            if (!Objects.equals(hn.getHash(path2), b.hash) || !Files.exists(path2, new LinkOption[0])) {
                Files.createDirectories(path2.getParent(), new FileAttribute[0]);
                try (final OutputStream outputStream6 = Files.newOutputStream(path2, new OpenOption[0])) {
                    outputStream6.write(b.payload);
                }
            }
            hn.putNew(path2, b.hash);
        }
        catch (IOException iOException6) {
            SnbtToNbt.LOGGER.error("Couldn't write structure {} at {}", b.name, path2, iOException6);
        }
    }
    
    static {
        dumpSnbtTo = null;
        LOGGER = LogManager.getLogger();
    }
    
    static class TaskResult {
        private final String name;
        private final byte[] payload;
        @Nullable
        private final String snbtPayload;
        private final String hash;
        
        public TaskResult(final String string1, final byte[] arr, @Nullable final String string3, final String string4) {
            this.name = string1;
            this.payload = arr;
            this.snbtPayload = string3;
            this.hash = string4;
        }
    }
    
    @FunctionalInterface
    public interface Filter {
        CompoundTag apply(final String string, final CompoundTag md);
    }
}
