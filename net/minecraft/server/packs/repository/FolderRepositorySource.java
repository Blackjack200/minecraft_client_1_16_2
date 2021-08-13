package net.minecraft.server.packs.repository;

import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.io.File;
import java.io.FileFilter;

public class FolderRepositorySource implements RepositorySource {
    private static final FileFilter RESOURCEPACK_FILTER;
    private final File folder;
    private final PackSource packSource;
    
    public FolderRepositorySource(final File file, final PackSource abv) {
        this.folder = file;
        this.packSource = abv;
    }
    
    public void loadPacks(final Consumer<Pack> consumer, final Pack.PackConstructor a) {
        if (!this.folder.isDirectory()) {
            this.folder.mkdirs();
        }
        final File[] arr4 = this.folder.listFiles(FolderRepositorySource.RESOURCEPACK_FILTER);
        if (arr4 == null) {
            return;
        }
        for (final File file8 : arr4) {
            final String string9 = "file/" + file8.getName();
            final Pack abs10 = Pack.create(string9, false, this.createSupplier(file8), a, Pack.Position.TOP, this.packSource);
            if (abs10 != null) {
                consumer.accept(abs10);
            }
        }
    }
    
    private Supplier<PackResources> createSupplier(final File file) {
        if (file.isDirectory()) {
            return (Supplier<PackResources>)(() -> new FolderPackResources(file));
        }
        return (Supplier<PackResources>)(() -> new FilePackResources(file));
    }
    
    static {
        RESOURCEPACK_FILTER = (file -> {
            final boolean boolean2 = file.isFile() && file.getName().endsWith(".zip");
            final boolean boolean3 = file.isDirectory() && new File(file, "pack.mcmeta").isFile();
            return boolean2 || boolean3;
        });
    }
}
