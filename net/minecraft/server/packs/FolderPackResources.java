package net.minecraft.server.packs;

import net.minecraft.Util;
import org.apache.logging.log4j.LogManager;
import net.minecraft.ResourceLocationException;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.Locale;
import java.io.FileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import com.google.common.base.CharMatcher;
import org.apache.logging.log4j.Logger;

public class FolderPackResources extends AbstractPackResources {
    private static final Logger LOGGER;
    private static final boolean ON_WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER;
    
    public FolderPackResources(final File file) {
        super(file);
    }
    
    public static boolean validatePath(final File file, final String string) throws IOException {
        String string2 = file.getCanonicalPath();
        if (FolderPackResources.ON_WINDOWS) {
            string2 = FolderPackResources.BACKSLASH_MATCHER.replaceFrom((CharSequence)string2, '/');
        }
        return string2.endsWith(string);
    }
    
    @Override
    protected InputStream getResource(final String string) throws IOException {
        final File file3 = this.getFile(string);
        if (file3 == null) {
            throw new ResourcePackFileNotFoundException(this.file, string);
        }
        return (InputStream)new FileInputStream(file3);
    }
    
    @Override
    protected boolean hasResource(final String string) {
        return this.getFile(string) != null;
    }
    
    @Nullable
    private File getFile(final String string) {
        try {
            final File file3 = new File(this.file, string);
            if (file3.isFile() && validatePath(file3, string)) {
                return file3;
            }
        }
        catch (IOException ex) {}
        return null;
    }
    
    public Set<String> getNamespaces(final PackType abi) {
        final Set<String> set3 = (Set<String>)Sets.newHashSet();
        final File file4 = new File(this.file, abi.getDirectory());
        final File[] arr5 = file4.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
        if (arr5 != null) {
            for (final File file5 : arr5) {
                final String string10 = AbstractPackResources.getRelativePath(file4, file5);
                if (string10.equals(string10.toLowerCase(Locale.ROOT))) {
                    set3.add(string10.substring(0, string10.length() - 1));
                }
                else {
                    this.logWarning(string10);
                }
            }
        }
        return set3;
    }
    
    public void close() {
    }
    
    public Collection<ResourceLocation> getResources(final PackType abi, final String string2, final String string3, final int integer, final Predicate<String> predicate) {
        final File file7 = new File(this.file, abi.getDirectory());
        final List<ResourceLocation> list8 = (List<ResourceLocation>)Lists.newArrayList();
        this.listResources(new File(new File(file7, string2), string3), integer, string2, list8, string3 + "/", predicate);
        return (Collection<ResourceLocation>)list8;
    }
    
    private void listResources(final File file, final int integer, final String string3, final List<ResourceLocation> list, final String string5, final Predicate<String> predicate) {
        final File[] arr8 = file.listFiles();
        if (arr8 != null) {
            for (final File file2 : arr8) {
                if (file2.isDirectory()) {
                    if (integer > 0) {
                        this.listResources(file2, integer - 1, string3, list, string5 + file2.getName() + "/", predicate);
                    }
                }
                else if (!file2.getName().endsWith(".mcmeta") && predicate.test(file2.getName())) {
                    try {
                        list.add(new ResourceLocation(string3, string5 + file2.getName()));
                    }
                    catch (ResourceLocationException v13) {
                        FolderPackResources.LOGGER.error(v13.getMessage());
                    }
                }
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ON_WINDOWS = (Util.getPlatform() == Util.OS.WINDOWS);
        BACKSLASH_MATCHER = CharMatcher.is('\\');
    }
}
