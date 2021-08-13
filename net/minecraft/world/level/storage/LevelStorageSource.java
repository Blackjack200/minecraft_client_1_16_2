package net.minecraft.world.level.storage;

import java.util.zip.ZipEntry;
import java.nio.file.Paths;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.BufferedOutputStream;
import java.nio.file.OpenOption;
import net.minecraft.FileUtil;
import java.time.LocalDateTime;
import java.nio.file.FileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.SimpleFileVisitor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.Level;
import com.google.common.collect.Maps;
import java.util.Map;
import java.time.temporal.TemporalField;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatterBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.nbt.NbtIo;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.io.File;
import net.minecraft.util.DirectoryLock;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.List;
import net.minecraft.world.level.DataPackConfig;
import com.mojang.serialization.DataResult;
import java.util.Optional;
import com.google.common.collect.UnmodifiableIterator;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.DataFixers;
import java.io.IOException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import com.mojang.datafixers.DataFixer;
import java.nio.file.Path;
import com.google.common.collect.ImmutableList;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.Logger;

public class LevelStorageSource {
    private static final Logger LOGGER;
    private static final DateTimeFormatter FORMATTER;
    private static final ImmutableList<String> OLD_SETTINGS_KEYS;
    private final Path baseDir;
    private final Path backupDir;
    private final DataFixer fixerUpper;
    
    public LevelStorageSource(final Path path1, final Path path2, final DataFixer dataFixer) {
        this.fixerUpper = dataFixer;
        try {
            Files.createDirectories(Files.exists(path1, new LinkOption[0]) ? path1.toRealPath(new LinkOption[0]) : path1, new FileAttribute[0]);
        }
        catch (IOException iOException5) {
            throw new RuntimeException((Throwable)iOException5);
        }
        this.baseDir = path1;
        this.backupDir = path2;
    }
    
    public static LevelStorageSource createDefault(final Path path) {
        return new LevelStorageSource(path, path.resolve("../backups"), DataFixers.getDataFixer());
    }
    
    private static <T> Pair<WorldGenSettings, Lifecycle> readWorldGenSettings(final Dynamic<T> dynamic, final DataFixer dataFixer, final int integer) {
        Dynamic<T> dynamic2 = (Dynamic<T>)dynamic.get("WorldGenSettings").orElseEmptyMap();
        for (final String string6 : LevelStorageSource.OLD_SETTINGS_KEYS) {
            final Optional<? extends Dynamic<?>> optional7 = dynamic.get(string6).result();
            if (optional7.isPresent()) {
                dynamic2 = (Dynamic<T>)dynamic2.set(string6, (Dynamic)optional7.get());
            }
        }
        final Dynamic<T> dynamic3 = (Dynamic<T>)dataFixer.update(References.WORLD_GEN_SETTINGS, (Dynamic)dynamic2, integer, SharedConstants.getCurrentVersion().getWorldVersion());
        final DataResult<WorldGenSettings> dataResult6 = (DataResult<WorldGenSettings>)WorldGenSettings.CODEC.parse((Dynamic)dynamic3);
        return (Pair<WorldGenSettings, Lifecycle>)Pair.of(dataResult6.resultOrPartial((Consumer)Util.prefix("WorldGenSettings: ", (Consumer<String>)LevelStorageSource.LOGGER::error)).orElseGet(() -> {
            final Registry<DimensionType> gm2 = (Registry<DimensionType>)RegistryLookupCodec.create(Registry.DIMENSION_TYPE_REGISTRY).codec().parse(dynamic3).resultOrPartial((Consumer)Util.prefix("Dimension type registry: ", (Consumer<String>)LevelStorageSource.LOGGER::error)).orElseThrow(() -> new IllegalStateException("Failed to get dimension registry"));
            final Registry<Biome> gm3 = (Registry<Biome>)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).codec().parse(dynamic3).resultOrPartial((Consumer)Util.prefix("Biome registry: ", (Consumer<String>)LevelStorageSource.LOGGER::error)).orElseThrow(() -> new IllegalStateException("Failed to get biome registry"));
            final Registry<NoiseGeneratorSettings> gm4 = (Registry<NoiseGeneratorSettings>)RegistryLookupCodec.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).codec().parse(dynamic3).resultOrPartial((Consumer)Util.prefix("Noise settings registry: ", (Consumer<String>)LevelStorageSource.LOGGER::error)).orElseThrow(() -> new IllegalStateException("Failed to get noise settings registry"));
            return WorldGenSettings.makeDefault(gm2, gm3, gm4);
        }), dataResult6.lifecycle());
    }
    
    private static DataPackConfig readDataPackConfig(final Dynamic<?> dynamic) {
        return (DataPackConfig)DataPackConfig.CODEC.parse((Dynamic)dynamic).resultOrPartial(LevelStorageSource.LOGGER::error).orElse(DataPackConfig.DEFAULT);
    }
    
    public List<LevelSummary> getLevelList() throws LevelStorageException {
        if (!Files.isDirectory(this.baseDir, new LinkOption[0])) {
            throw new LevelStorageException(new TranslatableComponent("selectWorld.load_folder_access").getString());
        }
        final List<LevelSummary> list2 = (List<LevelSummary>)Lists.newArrayList();
        final File[] listFiles;
        final File[] arr3 = listFiles = this.baseDir.toFile().listFiles();
        for (final File file7 : listFiles) {
            Label_0151: {
                if (file7.isDirectory()) {
                    boolean boolean8;
                    try {
                        boolean8 = DirectoryLock.isLocked(file7.toPath());
                    }
                    catch (Exception exception9) {
                        LevelStorageSource.LOGGER.warn("Failed to read {} lock", file7, exception9);
                        break Label_0151;
                    }
                    final LevelSummary cye9 = this.<LevelSummary>readLevelData(file7, this.levelSummaryReader(file7, boolean8));
                    if (cye9 != null) {
                        list2.add(cye9);
                    }
                }
            }
        }
        return list2;
    }
    
    private int getStorageVersion() {
        return 19133;
    }
    
    @Nullable
    private <T> T readLevelData(final File file, final BiFunction<File, DataFixer, T> biFunction) {
        if (!file.exists()) {
            return null;
        }
        File file2 = new File(file, "level.dat");
        if (file2.exists()) {
            final T object5 = (T)biFunction.apply(file2, this.fixerUpper);
            if (object5 != null) {
                return object5;
            }
        }
        file2 = new File(file, "level.dat_old");
        if (file2.exists()) {
            return (T)biFunction.apply(file2, this.fixerUpper);
        }
        return null;
    }
    
    @Nullable
    private static DataPackConfig getDataPacks(final File file, final DataFixer dataFixer) {
        try {
            final CompoundTag md3 = NbtIo.readCompressed(file);
            final CompoundTag md4 = md3.getCompound("Data");
            md4.remove("Player");
            final int integer5 = md4.contains("DataVersion", 99) ? md4.getInt("DataVersion") : -1;
            final Dynamic<Tag> dynamic6 = (Dynamic<Tag>)dataFixer.update(DataFixTypes.LEVEL.getType(), new Dynamic((DynamicOps)NbtOps.INSTANCE, md4), integer5, SharedConstants.getCurrentVersion().getWorldVersion());
            return (DataPackConfig)dynamic6.get("DataPacks").result().map(LevelStorageSource::readDataPackConfig).orElse(DataPackConfig.DEFAULT);
        }
        catch (Exception exception3) {
            LevelStorageSource.LOGGER.error("Exception reading {}", file, exception3);
            return null;
        }
    }
    
    private static BiFunction<File, DataFixer, PrimaryLevelData> getLevelData(final DynamicOps<Tag> dynamicOps, final DataPackConfig brh) {
        return (BiFunction<File, DataFixer, PrimaryLevelData>)((file, dataFixer) -> {
            try {
                final CompoundTag md5 = NbtIo.readCompressed(file);
                final CompoundTag md6 = md5.getCompound("Data");
                final CompoundTag md7 = md6.contains("Player", 10) ? md6.getCompound("Player") : null;
                md6.remove("Player");
                final int integer8 = md6.contains("DataVersion", 99) ? md6.getInt("DataVersion") : -1;
                final Dynamic<Tag> dynamic9 = (Dynamic<Tag>)dataFixer.update(DataFixTypes.LEVEL.getType(), new Dynamic(dynamicOps, md6), integer8, SharedConstants.getCurrentVersion().getWorldVersion());
                final Pair<WorldGenSettings, Lifecycle> pair10 = LevelStorageSource.<Tag>readWorldGenSettings(dynamic9, dataFixer, integer8);
                final LevelVersion cyf11 = LevelVersion.parse(dynamic9);
                final LevelSettings brx12 = LevelSettings.parse(dynamic9, brh);
                return PrimaryLevelData.parse(dynamic9, dataFixer, integer8, md7, brx12, cyf11, (WorldGenSettings)pair10.getFirst(), (Lifecycle)pair10.getSecond());
            }
            catch (Exception exception5) {
                LevelStorageSource.LOGGER.error("Exception reading {}", file, exception5);
                return null;
            }
        });
    }
    
    private BiFunction<File, DataFixer, LevelSummary> levelSummaryReader(final File file, final boolean boolean2) {
        return (BiFunction<File, DataFixer, LevelSummary>)((file3, dataFixer) -> {
            try {
                final CompoundTag md6 = NbtIo.readCompressed(file3);
                final CompoundTag md7 = md6.getCompound("Data");
                md7.remove("Player");
                final int integer8 = md7.contains("DataVersion", 99) ? md7.getInt("DataVersion") : -1;
                final Dynamic<Tag> dynamic9 = (Dynamic<Tag>)dataFixer.update(DataFixTypes.LEVEL.getType(), new Dynamic((DynamicOps)NbtOps.INSTANCE, md7), integer8, SharedConstants.getCurrentVersion().getWorldVersion());
                final LevelVersion cyf10 = LevelVersion.parse(dynamic9);
                final int integer9 = cyf10.levelDataVersion();
                if (integer9 == 19132 || integer9 == 19133) {
                    final boolean boolean3 = integer9 != this.getStorageVersion();
                    final File file4 = new File(file, "icon.png");
                    final DataPackConfig brh14 = (DataPackConfig)dynamic9.get("DataPacks").result().map(LevelStorageSource::readDataPackConfig).orElse(DataPackConfig.DEFAULT);
                    final LevelSettings brx15 = LevelSettings.parse(dynamic9, brh14);
                    return new LevelSummary(brx15, cyf10, file.getName(), boolean3, boolean2, file4);
                }
                return null;
            }
            catch (Exception exception6) {
                LevelStorageSource.LOGGER.error("Exception reading {}", file3, exception6);
                return null;
            }
        });
    }
    
    public boolean isNewLevelIdAcceptable(final String string) {
        try {
            final Path path3 = this.baseDir.resolve(string);
            Files.createDirectory(path3, new FileAttribute[0]);
            Files.deleteIfExists(path3);
            return true;
        }
        catch (IOException iOException3) {
            return false;
        }
    }
    
    public boolean levelExists(final String string) {
        return Files.isDirectory(this.baseDir.resolve(string), new LinkOption[0]);
    }
    
    public Path getBaseDir() {
        return this.baseDir;
    }
    
    public Path getBackupPath() {
        return this.backupDir;
    }
    
    public LevelStorageAccess createAccess(final String string) throws IOException {
        return new LevelStorageAccess(string);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        FORMATTER = new DateTimeFormatterBuilder().appendValue((TemporalField)ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue((TemporalField)ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue((TemporalField)ChronoField.DAY_OF_MONTH, 2).appendLiteral('_').appendValue((TemporalField)ChronoField.HOUR_OF_DAY, 2).appendLiteral('-').appendValue((TemporalField)ChronoField.MINUTE_OF_HOUR, 2).appendLiteral('-').appendValue((TemporalField)ChronoField.SECOND_OF_MINUTE, 2).toFormatter();
        OLD_SETTINGS_KEYS = ImmutableList.of("RandomSeed", "generatorName", "generatorOptions", "generatorVersion", "legacy_custom_options", "MapFeatures", "BonusChest");
    }
    
    public class LevelStorageAccess implements AutoCloseable {
        private final DirectoryLock lock;
        private final Path levelPath;
        private final String levelId;
        private final Map<LevelResource, Path> resources;
        
        public LevelStorageAccess(final String string) throws IOException {
            this.resources = (Map<LevelResource, Path>)Maps.newHashMap();
            this.levelId = string;
            this.levelPath = LevelStorageSource.this.baseDir.resolve(string);
            this.lock = DirectoryLock.create(this.levelPath);
        }
        
        public String getLevelId() {
            return this.levelId;
        }
        
        public Path getLevelPath(final LevelResource cyb) {
            return (Path)this.resources.computeIfAbsent(cyb, cyb -> this.levelPath.resolve(cyb.getId()));
        }
        
        public File getDimensionPath(final ResourceKey<Level> vj) {
            return DimensionType.getStorageFolder(vj, this.levelPath.toFile());
        }
        
        private void checkLock() {
            if (!this.lock.isValid()) {
                throw new IllegalStateException("Lock is no longer valid");
            }
        }
        
        public PlayerDataStorage createPlayerStorage() {
            this.checkLock();
            return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
        }
        
        public boolean requiresConversion() {
            final LevelSummary cye2 = this.getSummary();
            return cye2 != null && cye2.levelVersion().levelDataVersion() != LevelStorageSource.this.getStorageVersion();
        }
        
        public boolean convertLevel(final ProgressListener afk) {
            this.checkLock();
            return McRegionUpgrader.convertLevel(this, afk);
        }
        
        @Nullable
        public LevelSummary getSummary() {
            this.checkLock();
            return (LevelSummary)LevelStorageSource.this.readLevelData(this.levelPath.toFile(), (java.util.function.BiFunction<File, DataFixer, Object>)LevelStorageSource.this.levelSummaryReader(this.levelPath.toFile(), false));
        }
        
        @Nullable
        public WorldData getDataTag(final DynamicOps<Tag> dynamicOps, final DataPackConfig brh) {
            this.checkLock();
            return (WorldData)LevelStorageSource.this.readLevelData(this.levelPath.toFile(), (java.util.function.BiFunction<File, DataFixer, Object>)getLevelData(dynamicOps, brh));
        }
        
        @Nullable
        public DataPackConfig getDataPacks() {
            this.checkLock();
            return (DataPackConfig)LevelStorageSource.this.readLevelData(this.levelPath.toFile(), (java.util.function.BiFunction<File, DataFixer, Object>)((file, dataFixer) -> getDataPacks(file, dataFixer)));
        }
        
        public void saveDataTag(final RegistryAccess gn, final WorldData cyk) {
            this.saveDataTag(gn, cyk, null);
        }
        
        public void saveDataTag(final RegistryAccess gn, final WorldData cyk, @Nullable final CompoundTag md) {
            final File file5 = this.levelPath.toFile();
            final CompoundTag md2 = cyk.createTag(gn, md);
            final CompoundTag md3 = new CompoundTag();
            md3.put("Data", (Tag)md2);
            try {
                final File file6 = File.createTempFile("level", ".dat", file5);
                NbtIo.writeCompressed(md3, file6);
                final File file7 = new File(file5, "level.dat_old");
                final File file8 = new File(file5, "level.dat");
                Util.safeReplaceFile(file8, file6, file7);
            }
            catch (Exception exception8) {
                LevelStorageSource.LOGGER.error("Failed to save level {}", file5, exception8);
            }
        }
        
        public File getIconFile() {
            this.checkLock();
            return this.levelPath.resolve("icon.png").toFile();
        }
        
        public void deleteLevel() throws IOException {
            this.checkLock();
            final Path path2 = this.levelPath.resolve("session.lock");
            int integer3 = 1;
            while (integer3 <= 5) {
                LevelStorageSource.LOGGER.info("Attempt {}...", integer3);
                try {
                    Files.walkFileTree(this.levelPath, (FileVisitor)new SimpleFileVisitor<Path>() {
                        public FileVisitResult visitFile(final Path path, final BasicFileAttributes basicFileAttributes) throws IOException {
                            if (!path.equals(path2)) {
                                LevelStorageSource.LOGGER.debug("Deleting {}", path);
                                Files.delete(path);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                        
                        public FileVisitResult postVisitDirectory(final Path path, final IOException iOException) throws IOException {
                            if (iOException != null) {
                                throw iOException;
                            }
                            if (path.equals(LevelStorageAccess.this.levelPath)) {
                                LevelStorageAccess.this.lock.close();
                                Files.deleteIfExists(path2);
                            }
                            Files.delete(path);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
                catch (IOException iOException4) {
                    if (integer3 < 5) {
                        LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelPath, iOException4);
                        try {
                            Thread.sleep(500L);
                        }
                        catch (InterruptedException ex) {}
                        ++integer3;
                        continue;
                    }
                    throw iOException4;
                }
                break;
            }
        }
        
        public void renameLevel(final String string) throws IOException {
            this.checkLock();
            final File file3 = new File(LevelStorageSource.this.baseDir.toFile(), this.levelId);
            if (!file3.exists()) {
                return;
            }
            final File file4 = new File(file3, "level.dat");
            if (file4.exists()) {
                final CompoundTag md5 = NbtIo.readCompressed(file4);
                final CompoundTag md6 = md5.getCompound("Data");
                md6.putString("LevelName", string);
                NbtIo.writeCompressed(md5, file4);
            }
        }
        
        public long makeWorldBackup() throws IOException {
            this.checkLock();
            final String string2 = LocalDateTime.now().format(LevelStorageSource.FORMATTER) + "_" + this.levelId;
            final Path path3 = LevelStorageSource.this.getBackupPath();
            try {
                Files.createDirectories(Files.exists(path3, new LinkOption[0]) ? path3.toRealPath(new LinkOption[0]) : path3, new FileAttribute[0]);
            }
            catch (IOException iOException4) {
                throw new RuntimeException((Throwable)iOException4);
            }
            final Path path4 = path3.resolve(FileUtil.findAvailableName(path3, string2, ".zip"));
            try (final ZipOutputStream zipOutputStream5 = new ZipOutputStream((OutputStream)new BufferedOutputStream(Files.newOutputStream(path4, new OpenOption[0])))) {
                final Path path5 = Paths.get(this.levelId, new String[0]);
                Files.walkFileTree(this.levelPath, (FileVisitor)new SimpleFileVisitor<Path>() {
                    public FileVisitResult visitFile(final Path path, final BasicFileAttributes basicFileAttributes) throws IOException {
                        if (path.endsWith("session.lock")) {
                            return FileVisitResult.CONTINUE;
                        }
                        final String string4 = path5.resolve(LevelStorageAccess.this.levelPath.relativize(path)).toString().replace('\\', '/');
                        final ZipEntry zipEntry5 = new ZipEntry(string4);
                        zipOutputStream5.putNextEntry(zipEntry5);
                        com.google.common.io.Files.asByteSource(path.toFile()).copyTo((OutputStream)zipOutputStream5);
                        zipOutputStream5.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            return Files.size(path4);
        }
        
        public void close() throws IOException {
            this.lock.close();
        }
    }
}
