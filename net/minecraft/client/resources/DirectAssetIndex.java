package net.minecraft.client.resources;

import java.nio.file.LinkOption;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.util.Collections;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import java.io.File;

public class DirectAssetIndex extends AssetIndex {
    private final File assetsDirectory;
    
    public DirectAssetIndex(final File file) {
        this.assetsDirectory = file;
    }
    
    @Override
    public File getFile(final ResourceLocation vk) {
        return new File(this.assetsDirectory, vk.toString().replace(':', '/'));
    }
    
    @Override
    public File getRootFile(final String string) {
        return new File(this.assetsDirectory, string);
    }
    
    @Override
    public Collection<ResourceLocation> getFiles(final String string1, final String string2, final int integer, final Predicate<String> predicate) {
        final Path path6 = this.assetsDirectory.toPath().resolve(string2);
        try (final Stream<Path> stream7 = (Stream<Path>)Files.walk(path6.resolve(string1), integer, new FileVisitOption[0])) {
            return (Collection<ResourceLocation>)stream7.filter(path -> Files.isRegularFile(path, new LinkOption[0])).filter(path -> !path.endsWith(".mcmeta")).filter(path -> predicate.test(path.getFileName().toString())).map(path3 -> new ResourceLocation(string2, path6.relativize(path3).toString().replaceAll("\\\\", "/"))).collect(Collectors.toList());
        }
        catch (NoSuchFileException ex) {}
        catch (IOException iOException7) {
            DirectAssetIndex.LOGGER.warn("Unable to getFiles on {}", string1, iOException7);
        }
        return (Collection<ResourceLocation>)Collections.emptyList();
    }
}
