package net.minecraft.server.packs;

import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import java.util.function.Predicate;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.util.List;
import java.util.Enumeration;
import java.util.Locale;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.zip.ZipFile;
import com.google.common.base.Splitter;

public class FilePackResources extends AbstractPackResources {
    public static final Splitter SPLITTER;
    private ZipFile zipFile;
    
    public FilePackResources(final File file) {
        super(file);
    }
    
    private ZipFile getOrCreateZipFile() throws IOException {
        if (this.zipFile == null) {
            this.zipFile = new ZipFile(this.file);
        }
        return this.zipFile;
    }
    
    @Override
    protected InputStream getResource(final String string) throws IOException {
        final ZipFile zipFile3 = this.getOrCreateZipFile();
        final ZipEntry zipEntry4 = zipFile3.getEntry(string);
        if (zipEntry4 == null) {
            throw new ResourcePackFileNotFoundException(this.file, string);
        }
        return zipFile3.getInputStream(zipEntry4);
    }
    
    public boolean hasResource(final String string) {
        try {
            return this.getOrCreateZipFile().getEntry(string) != null;
        }
        catch (IOException iOException3) {
            return false;
        }
    }
    
    public Set<String> getNamespaces(final PackType abi) {
        ZipFile zipFile3;
        try {
            zipFile3 = this.getOrCreateZipFile();
        }
        catch (IOException iOException4) {
            return (Set<String>)Collections.emptySet();
        }
        final Enumeration<? extends ZipEntry> enumeration4 = zipFile3.entries();
        final Set<String> set5 = (Set<String>)Sets.newHashSet();
        while (enumeration4.hasMoreElements()) {
            final ZipEntry zipEntry6 = (ZipEntry)enumeration4.nextElement();
            final String string7 = zipEntry6.getName();
            if (string7.startsWith(abi.getDirectory() + "/")) {
                final List<String> list8 = (List<String>)Lists.newArrayList(FilePackResources.SPLITTER.split((CharSequence)string7));
                if (list8.size() <= 1) {
                    continue;
                }
                final String string8 = (String)list8.get(1);
                if (string8.equals(string8.toLowerCase(Locale.ROOT))) {
                    set5.add(string8);
                }
                else {
                    this.logWarning(string8);
                }
            }
        }
        return set5;
    }
    
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    
    public void close() {
        if (this.zipFile != null) {
            IOUtils.closeQuietly((Closeable)this.zipFile);
            this.zipFile = null;
        }
    }
    
    public Collection<ResourceLocation> getResources(final PackType abi, final String string2, final String string3, final int integer, final Predicate<String> predicate) {
        ZipFile zipFile7;
        try {
            zipFile7 = this.getOrCreateZipFile();
        }
        catch (IOException iOException8) {
            return (Collection<ResourceLocation>)Collections.emptySet();
        }
        final Enumeration<? extends ZipEntry> enumeration8 = zipFile7.entries();
        final List<ResourceLocation> list9 = (List<ResourceLocation>)Lists.newArrayList();
        final String string4 = abi.getDirectory() + "/" + string2 + "/";
        final String string5 = string4 + string3 + "/";
        while (enumeration8.hasMoreElements()) {
            final ZipEntry zipEntry12 = (ZipEntry)enumeration8.nextElement();
            if (zipEntry12.isDirectory()) {
                continue;
            }
            final String string6 = zipEntry12.getName();
            if (string6.endsWith(".mcmeta")) {
                continue;
            }
            if (!string6.startsWith(string5)) {
                continue;
            }
            final String string7 = string6.substring(string4.length());
            final String[] arr15 = string7.split("/");
            if (arr15.length < integer + 1 || !predicate.test(arr15[arr15.length - 1])) {
                continue;
            }
            list9.add(new ResourceLocation(string2, string7));
        }
        return (Collection<ResourceLocation>)list9;
    }
    
    static {
        SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
    }
}
