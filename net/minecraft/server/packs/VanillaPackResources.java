package net.minecraft.server.packs;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.nio.file.FileSystemNotFoundException;
import java.util.Collections;
import java.nio.file.FileSystems;
import java.util.HashMap;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import java.io.File;
import javax.annotation.Nullable;
import java.util.stream.Stream;
import java.nio.file.FileVisitOption;
import java.net.URI;
import java.util.Enumeration;
import java.nio.file.NoSuchFileException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.net.URL;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.function.Predicate;
import java.io.FileNotFoundException;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.InputStream;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.nio.file.FileSystem;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;

public class VanillaPackResources implements PackResources {
    public static Path generatedDir;
    private static final Logger LOGGER;
    public static Class<?> clientObject;
    private static final Map<PackType, FileSystem> JAR_FILESYSTEM_BY_TYPE;
    public final Set<String> namespaces;
    
    public VanillaPackResources(final String... arr) {
        this.namespaces = (Set<String>)ImmutableSet.copyOf((Object[])arr);
    }
    
    public InputStream getRootResource(final String string) throws IOException {
        if (string.contains("/") || string.contains("\\")) {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
        if (VanillaPackResources.generatedDir != null) {
            final Path path3 = VanillaPackResources.generatedDir.resolve(string);
            if (Files.exists(path3, new LinkOption[0])) {
                return Files.newInputStream(path3, new OpenOption[0]);
            }
        }
        return this.getResourceAsStream(string);
    }
    
    public InputStream getResource(final PackType abi, final ResourceLocation vk) throws IOException {
        final InputStream inputStream4 = this.getResourceAsStream(abi, vk);
        if (inputStream4 != null) {
            return inputStream4;
        }
        throw new FileNotFoundException(vk.getPath());
    }
    
    public Collection<ResourceLocation> getResources(final PackType abi, final String string2, final String string3, final int integer, final Predicate<String> predicate) {
        final Set<ResourceLocation> set7 = (Set<ResourceLocation>)Sets.newHashSet();
        if (VanillaPackResources.generatedDir != null) {
            try {
                getResources((Collection<ResourceLocation>)set7, integer, string2, VanillaPackResources.generatedDir.resolve(abi.getDirectory()), string3, predicate);
            }
            catch (IOException ex2) {}
            if (abi == PackType.CLIENT_RESOURCES) {
                Enumeration<URL> enumeration8 = null;
                try {
                    enumeration8 = (Enumeration<URL>)VanillaPackResources.clientObject.getClassLoader().getResources(abi.getDirectory() + "/");
                }
                catch (IOException ex3) {}
                while (enumeration8 != null && enumeration8.hasMoreElements()) {
                    try {
                        final URI uRI9 = ((URL)enumeration8.nextElement()).toURI();
                        if (!"file".equals(uRI9.getScheme())) {
                            continue;
                        }
                        getResources((Collection<ResourceLocation>)set7, integer, string2, Paths.get(uRI9), string3, predicate);
                    }
                    catch (URISyntaxException | IOException ex4) {}
                }
            }
        }
        try {
            final URL uRL8 = VanillaPackResources.class.getResource("/" + abi.getDirectory() + "/.mcassetsroot");
            if (uRL8 == null) {
                VanillaPackResources.LOGGER.error("Couldn't find .mcassetsroot, cannot load vanilla resources");
                return (Collection<ResourceLocation>)set7;
            }
            final URI uRI9 = uRL8.toURI();
            if ("file".equals(uRI9.getScheme())) {
                final URL uRL9 = new URL(uRL8.toString().substring(0, uRL8.toString().length() - ".mcassetsroot".length()));
                final Path path11 = Paths.get(uRL9.toURI());
                getResources((Collection<ResourceLocation>)set7, integer, string2, path11, string3, predicate);
            }
            else if ("jar".equals(uRI9.getScheme())) {
                final Path path12 = ((FileSystem)VanillaPackResources.JAR_FILESYSTEM_BY_TYPE.get(abi)).getPath("/" + abi.getDirectory(), new String[0]);
                getResources((Collection<ResourceLocation>)set7, integer, "minecraft", path12, string3, predicate);
            }
            else {
                VanillaPackResources.LOGGER.error("Unsupported scheme {} trying to list vanilla resources (NYI?)", uRI9);
            }
        }
        catch (FileNotFoundException | NoSuchFileException ex5) {}
        catch (URISyntaxException | IOException ex6) {
            final Exception ex;
            final Exception exception8 = ex;
            VanillaPackResources.LOGGER.error("Couldn't get a list of all vanilla resources", (Throwable)exception8);
        }
        return (Collection<ResourceLocation>)set7;
    }
    
    private static void getResources(final Collection<ResourceLocation> collection, final int integer, final String string3, final Path path, final String string5, final Predicate<String> predicate) throws IOException {
        final Path path2 = path.resolve(string3);
        try (final Stream<Path> stream8 = (Stream<Path>)Files.walk(path2.resolve(string5), integer, new FileVisitOption[0])) {
            stream8.filter(path -> !path.endsWith(".mcmeta") && Files.isRegularFile(path, new LinkOption[0]) && predicate.test(path.getFileName().toString())).map(path3 -> new ResourceLocation(string3, path2.relativize(path3).toString().replaceAll("\\\\", "/"))).forEach(collection::add);
        }
    }
    
    @Nullable
    protected InputStream getResourceAsStream(final PackType abi, final ResourceLocation vk) {
        final String string4 = createPath(abi, vk);
        if (VanillaPackResources.generatedDir != null) {
            final Path path5 = VanillaPackResources.generatedDir.resolve(abi.getDirectory() + "/" + vk.getNamespace() + "/" + vk.getPath());
            if (Files.exists(path5, new LinkOption[0])) {
                try {
                    return Files.newInputStream(path5, new OpenOption[0]);
                }
                catch (IOException ex) {}
            }
        }
        try {
            final URL uRL5 = VanillaPackResources.class.getResource(string4);
            if (isResourceUrlValid(string4, uRL5)) {
                return uRL5.openStream();
            }
        }
        catch (IOException iOException5) {
            return VanillaPackResources.class.getResourceAsStream(string4);
        }
        return null;
    }
    
    private static String createPath(final PackType abi, final ResourceLocation vk) {
        return "/" + abi.getDirectory() + "/" + vk.getNamespace() + "/" + vk.getPath();
    }
    
    private static boolean isResourceUrlValid(final String string, @Nullable final URL uRL) throws IOException {
        return uRL != null && (uRL.getProtocol().equals("jar") || FolderPackResources.validatePath(new File(uRL.getFile()), string));
    }
    
    @Nullable
    protected InputStream getResourceAsStream(final String string) {
        return VanillaPackResources.class.getResourceAsStream("/" + string);
    }
    
    public boolean hasResource(final PackType abi, final ResourceLocation vk) {
        final String string4 = createPath(abi, vk);
        if (VanillaPackResources.generatedDir != null) {
            final Path path5 = VanillaPackResources.generatedDir.resolve(abi.getDirectory() + "/" + vk.getNamespace() + "/" + vk.getPath());
            if (Files.exists(path5, new LinkOption[0])) {
                return true;
            }
        }
        try {
            final URL uRL5 = VanillaPackResources.class.getResource(string4);
            return isResourceUrlValid(string4, uRL5);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public Set<String> getNamespaces(final PackType abi) {
        return this.namespaces;
    }
    
    @Nullable
    public <T> T getMetadataSection(final MetadataSectionSerializer<T> abl) throws IOException {
        try (final InputStream inputStream3 = this.getRootResource("pack.mcmeta")) {
            return AbstractPackResources.<T>getMetadataFromStream(abl, inputStream3);
        }
        catch (RuntimeException | FileNotFoundException ex2) {
            final Exception ex;
            final Exception exception3 = ex;
            return null;
        }
    }
    
    public String getName() {
        return "Default";
    }
    
    public void close() {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        JAR_FILESYSTEM_BY_TYPE = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            synchronized (VanillaPackResources.class) {
                for (final PackType abi6 : PackType.values()) {
                    final URL uRL7 = VanillaPackResources.class.getResource("/" + abi6.getDirectory() + "/.mcassetsroot");
                    try {
                        final URI uRI8 = uRL7.toURI();
                        if ("jar".equals(uRI8.getScheme())) {
                            FileSystem fileSystem9;
                            try {
                                fileSystem9 = FileSystems.getFileSystem(uRI8);
                            }
                            catch (FileSystemNotFoundException fileSystemNotFoundException10) {
                                fileSystem9 = FileSystems.newFileSystem(uRI8, Collections.emptyMap());
                            }
                            hashMap.put(abi6, fileSystem9);
                        }
                    }
                    catch (URISyntaxException | IOException ex2) {
                        final Exception ex;
                        final Exception exception8 = ex;
                        VanillaPackResources.LOGGER.error("Couldn't get a list of all vanilla resources", (Throwable)exception8);
                    }
                }
            }
        }));
    }
}
