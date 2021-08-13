package net.minecraft.world.level.levelgen.structure.templatesystem;

import org.apache.logging.log4j.LogManager;
import java.nio.file.InvalidPathException;
import net.minecraft.ResourceLocationException;
import net.minecraft.FileUtil;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import net.minecraft.server.packs.resources.Resource;
import java.io.FileNotFoundException;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.LevelResource;
import com.google.common.collect.Maps;
import net.minecraft.world.level.storage.LevelStorageSource;
import java.nio.file.Path;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class StructureManager {
    private static final Logger LOGGER;
    private final Map<ResourceLocation, StructureTemplate> structureRepository;
    private final DataFixer fixerUpper;
    private ResourceManager resourceManager;
    private final Path generatedDir;
    
    public StructureManager(final ResourceManager acf, final LevelStorageSource.LevelStorageAccess a, final DataFixer dataFixer) {
        this.structureRepository = (Map<ResourceLocation, StructureTemplate>)Maps.newHashMap();
        this.resourceManager = acf;
        this.fixerUpper = dataFixer;
        this.generatedDir = a.getLevelPath(LevelResource.GENERATED_DIR).normalize();
    }
    
    public StructureTemplate getOrCreate(final ResourceLocation vk) {
        StructureTemplate csy3 = this.get(vk);
        if (csy3 == null) {
            csy3 = new StructureTemplate();
            this.structureRepository.put(vk, csy3);
        }
        return csy3;
    }
    
    @Nullable
    public StructureTemplate get(final ResourceLocation vk) {
        return (StructureTemplate)this.structureRepository.computeIfAbsent(vk, vk -> {
            final StructureTemplate csy3 = this.loadFromGenerated(vk);
            return (csy3 != null) ? csy3 : this.loadFromResource(vk);
        });
    }
    
    public void onResourceManagerReload(final ResourceManager acf) {
        this.resourceManager = acf;
        this.structureRepository.clear();
    }
    
    @Nullable
    private StructureTemplate loadFromResource(final ResourceLocation vk) {
        final ResourceLocation vk2 = new ResourceLocation(vk.getNamespace(), "structures/" + vk.getPath() + ".nbt");
        try (final Resource ace4 = this.resourceManager.getResource(vk2)) {
            return this.readStructure(ace4.getInputStream());
        }
        catch (FileNotFoundException fileNotFoundException4) {
            return null;
        }
        catch (Throwable throwable4) {
            StructureManager.LOGGER.error("Couldn't load structure {}: {}", vk, throwable4.toString());
            return null;
        }
    }
    
    @Nullable
    private StructureTemplate loadFromGenerated(final ResourceLocation vk) {
        if (!this.generatedDir.toFile().isDirectory()) {
            return null;
        }
        final Path path3 = this.createAndValidatePathToStructure(vk, ".nbt");
        try (final InputStream inputStream4 = (InputStream)new FileInputStream(path3.toFile())) {
            return this.readStructure(inputStream4);
        }
        catch (FileNotFoundException fileNotFoundException4) {
            return null;
        }
        catch (IOException iOException4) {
            StructureManager.LOGGER.error("Couldn't load structure from {}", path3, iOException4);
            return null;
        }
    }
    
    private StructureTemplate readStructure(final InputStream inputStream) throws IOException {
        final CompoundTag md3 = NbtIo.readCompressed(inputStream);
        return this.readStructure(md3);
    }
    
    public StructureTemplate readStructure(final CompoundTag md) {
        if (!md.contains("DataVersion", 99)) {
            md.putInt("DataVersion", 500);
        }
        final StructureTemplate csy3 = new StructureTemplate();
        csy3.load(NbtUtils.update(this.fixerUpper, DataFixTypes.STRUCTURE, md, md.getInt("DataVersion")));
        return csy3;
    }
    
    public boolean save(final ResourceLocation vk) {
        final StructureTemplate csy3 = (StructureTemplate)this.structureRepository.get(vk);
        if (csy3 == null) {
            return false;
        }
        final Path path4 = this.createAndValidatePathToStructure(vk, ".nbt");
        final Path path5 = path4.getParent();
        if (path5 == null) {
            return false;
        }
        try {
            Files.createDirectories(Files.exists(path5, new LinkOption[0]) ? path5.toRealPath(new LinkOption[0]) : path5, new FileAttribute[0]);
        }
        catch (IOException iOException6) {
            StructureManager.LOGGER.error("Failed to create parent directory: {}", path5);
            return false;
        }
        final CompoundTag md6 = csy3.save(new CompoundTag());
        try (final OutputStream outputStream7 = (OutputStream)new FileOutputStream(path4.toFile())) {
            NbtIo.writeCompressed(md6, outputStream7);
        }
        catch (Throwable throwable7) {
            return false;
        }
        return true;
    }
    
    public Path createPathToStructure(final ResourceLocation vk, final String string) {
        try {
            final Path path4 = this.generatedDir.resolve(vk.getNamespace());
            final Path path5 = path4.resolve("structures");
            return FileUtil.createPathToResource(path5, vk.getPath(), string);
        }
        catch (InvalidPathException invalidPathException4) {
            throw new ResourceLocationException(new StringBuilder().append("Invalid resource path: ").append(vk).toString(), (Throwable)invalidPathException4);
        }
    }
    
    private Path createAndValidatePathToStructure(final ResourceLocation vk, final String string) {
        if (vk.getPath().contains("//")) {
            throw new ResourceLocationException(new StringBuilder().append("Invalid resource path: ").append(vk).toString());
        }
        final Path path4 = this.createPathToStructure(vk, string);
        if (!path4.startsWith(this.generatedDir) || !FileUtil.isPathNormalized(path4) || !FileUtil.isPathPortable(path4)) {
            throw new ResourceLocationException(new StringBuilder().append("Invalid resource path: ").append(path4).toString());
        }
        return path4;
    }
    
    public void remove(final ResourceLocation vk) {
        this.structureRepository.remove(vk);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
