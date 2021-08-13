package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import java.awt.GraphicsEnvironment;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import com.google.common.collect.ImmutableSet;
import java.util.function.BooleanSupplier;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.storage.WorldData;
import java.util.concurrent.CompletableFuture;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.nio.file.Path;
import joptsimple.OptionSet;
import net.minecraft.DefaultUncaughtExceptionHandler;
import java.util.function.Function;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.storage.PrimaryLevelData;
import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.storage.LevelStorageSource;
import java.util.Optional;
import net.minecraft.server.players.GameProfileCache;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import java.net.Proxy;
import java.io.File;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import java.nio.file.Paths;
import net.minecraft.core.RegistryAccess;
import net.minecraft.Util;
import net.minecraft.CrashReport;
import java.io.OutputStream;
import joptsimple.OptionSpec;
import joptsimple.OptionParser;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER;
    
    public static void main(final String[] arr) {
        final OptionParser optionParser2 = new OptionParser();
        final OptionSpec<Void> optionSpec3 = (OptionSpec<Void>)optionParser2.accepts("nogui");
        final OptionSpec<Void> optionSpec4 = (OptionSpec<Void>)optionParser2.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
        final OptionSpec<Void> optionSpec5 = (OptionSpec<Void>)optionParser2.accepts("demo");
        final OptionSpec<Void> optionSpec6 = (OptionSpec<Void>)optionParser2.accepts("bonusChest");
        final OptionSpec<Void> optionSpec7 = (OptionSpec<Void>)optionParser2.accepts("forceUpgrade");
        final OptionSpec<Void> optionSpec8 = (OptionSpec<Void>)optionParser2.accepts("eraseCache");
        final OptionSpec<Void> optionSpec9 = (OptionSpec<Void>)optionParser2.accepts("safeMode", "Loads level with vanilla datapack only");
        final OptionSpec<Void> optionSpec10 = (OptionSpec<Void>)optionParser2.accepts("help").forHelp();
        final OptionSpec<String> optionSpec11 = (OptionSpec<String>)optionParser2.accepts("singleplayer").withRequiredArg();
        final OptionSpec<String> optionSpec12 = (OptionSpec<String>)optionParser2.accepts("universe").withRequiredArg().defaultsTo(".", (Object[])new String[0]);
        final OptionSpec<String> optionSpec13 = (OptionSpec<String>)optionParser2.accepts("world").withRequiredArg();
        final OptionSpec<Integer> optionSpec14 = (OptionSpec<Integer>)optionParser2.accepts("port").withRequiredArg().ofType((Class)Integer.class).defaultsTo((-1), (Object[])new Integer[0]);
        final OptionSpec<String> optionSpec15 = (OptionSpec<String>)optionParser2.accepts("serverId").withRequiredArg();
        final OptionSpec<String> optionSpec16 = (OptionSpec<String>)optionParser2.nonOptions();
        try {
            final OptionSet optionSet17 = optionParser2.parse(arr);
            if (optionSet17.has((OptionSpec)optionSpec10)) {
                optionParser2.printHelpOn((OutputStream)System.err);
                return;
            }
            CrashReport.preload();
            Bootstrap.bootStrap();
            Bootstrap.validate();
            Util.startTimerHackThread();
            final RegistryAccess.RegistryHolder b18 = RegistryAccess.builtin();
            final Path path19 = Paths.get("server.properties", new String[0]);
            final DedicatedServerSettings zi20 = new DedicatedServerSettings(b18, path19);
            zi20.forceSave();
            final Path path20 = Paths.get("eula.txt", new String[0]);
            final Eula vr22 = new Eula(path20);
            if (optionSet17.has((OptionSpec)optionSpec4)) {
                Main.LOGGER.info("Initialized '{}' and '{}'", path19.toAbsolutePath(), path20.toAbsolutePath());
                return;
            }
            if (!vr22.hasAgreedToEULA()) {
                Main.LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
                return;
            }
            final File file23 = new File((String)optionSet17.valueOf((OptionSpec)optionSpec12));
            final YggdrasilAuthenticationService yggdrasilAuthenticationService24 = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            final MinecraftSessionService minecraftSessionService25 = yggdrasilAuthenticationService24.createMinecraftSessionService();
            final GameProfileRepository gameProfileRepository26 = yggdrasilAuthenticationService24.createProfileRepository();
            final GameProfileCache aco27 = new GameProfileCache(gameProfileRepository26, new File(file23, MinecraftServer.USERID_CACHE_FILE.getName()));
            final String string28 = (String)Optional.ofNullable(optionSet17.valueOf((OptionSpec)optionSpec13)).orElse(zi20.getProperties().levelName);
            final LevelStorageSource cyd29 = LevelStorageSource.createDefault(file23.toPath());
            final LevelStorageSource.LevelStorageAccess a30 = cyd29.createAccess(string28);
            MinecraftServer.convertFromRegionFormatIfNeeded(a30);
            final DataPackConfig brh31 = a30.getDataPacks();
            final boolean boolean32 = optionSet17.has((OptionSpec)optionSpec9);
            if (boolean32) {
                Main.LOGGER.warn("Safe mode active, only vanilla datapack will be loaded");
            }
            final PackRepository abu33 = new PackRepository(new RepositorySource[] { new ServerPacksSource(), new FolderRepositorySource(a30.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD) });
            final DataPackConfig brh32 = MinecraftServer.configurePackRepository(abu33, (brh31 == null) ? DataPackConfig.DEFAULT : brh31, boolean32);
            final CompletableFuture<ServerResources> completableFuture35 = ServerResources.loadResources(abu33.openAllSelected(), Commands.CommandSelection.DEDICATED, zi20.getProperties().functionPermissionLevel, Util.backgroundExecutor(), Runnable::run);
            ServerResources vz36;
            try {
                vz36 = (ServerResources)completableFuture35.get();
            }
            catch (Exception exception37) {
                Main.LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", (Throwable)exception37);
                abu33.close();
                return;
            }
            vz36.updateGlobals();
            final RegistryReadOps<Tag> vh37 = RegistryReadOps.<Tag>create((com.mojang.serialization.DynamicOps<Tag>)NbtOps.INSTANCE, vz36.getResourceManager(), b18);
            WorldData cyk38 = a30.getDataTag((DynamicOps<Tag>)vh37, brh32);
            if (cyk38 == null) {
                LevelSettings brx39;
                WorldGenSettings cht40;
                if (optionSet17.has((OptionSpec)optionSpec5)) {
                    brx39 = MinecraftServer.DEMO_SETTINGS;
                    cht40 = WorldGenSettings.demoSettings(b18);
                }
                else {
                    final DedicatedServerProperties zh41 = zi20.getProperties();
                    brx39 = new LevelSettings(zh41.levelName, zh41.gamemode, zh41.hardcore, zh41.difficulty, false, new GameRules(), brh32);
                    cht40 = (optionSet17.has((OptionSpec)optionSpec6) ? zh41.worldGenSettings.withBonusChest() : zh41.worldGenSettings);
                }
                cyk38 = new PrimaryLevelData(brx39, cht40, Lifecycle.stable());
            }
            if (optionSet17.has((OptionSpec)optionSpec7)) {
                forceUpgrade(a30, DataFixers.getDataFixer(), optionSet17.has((OptionSpec)optionSpec8), () -> true, cyk38.worldGenSettings().levels());
            }
            a30.saveDataTag(b18, cyk38);
            final WorldData cyk39 = cyk38;
            final DedicatedServer zg40 = MinecraftServer.<DedicatedServer>spin((java.util.function.Function<Thread, DedicatedServer>)(thread -> {
                final DedicatedServer zg18 = new DedicatedServer(thread, b18, a30, abu33, vz36, cyk39, zi20, DataFixers.getDataFixer(), minecraftSessionService25, gameProfileRepository26, aco27, LoggerChunkProgressListener::new);
                zg18.setSingleplayerName((String)optionSet17.valueOf(optionSpec11));
                zg18.setPort((int)optionSet17.valueOf(optionSpec14));
                zg18.setDemo(optionSet17.has(optionSpec5));
                zg18.setId((String)optionSet17.valueOf(optionSpec15));
                final boolean boolean19 = !optionSet17.has(optionSpec3) && !optionSet17.valuesOf(optionSpec16).contains("nogui");
                if (boolean19 && !GraphicsEnvironment.isHeadless()) {
                    zg18.showGui();
                }
                return zg18;
            }));
            final Thread thread41 = new Thread("Server Shutdown Thread") {
                public void run() {
                    zg40.halt(true);
                }
            };
            thread41.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(Main.LOGGER));
            Runtime.getRuntime().addShutdownHook(thread41);
        }
        catch (Exception exception38) {
            Main.LOGGER.fatal("Failed to start the minecraft server", (Throwable)exception38);
        }
    }
    
    private static void forceUpgrade(final LevelStorageSource.LevelStorageAccess a, final DataFixer dataFixer, final boolean boolean3, final BooleanSupplier booleanSupplier, final ImmutableSet<ResourceKey<Level>> immutableSet) {
        Main.LOGGER.info("Forcing world upgrade!");
        final WorldUpgrader aof6 = new WorldUpgrader(a, dataFixer, immutableSet, boolean3);
        Component nr7 = null;
        while (!aof6.isFinished()) {
            final Component nr8 = aof6.getStatus();
            if (nr7 != nr8) {
                nr7 = nr8;
                Main.LOGGER.info(aof6.getStatus().getString());
            }
            final int integer9 = aof6.getTotalChunks();
            if (integer9 > 0) {
                final int integer10 = aof6.getConverted() + aof6.getSkipped();
                Main.LOGGER.info("{}% completed ({} / {} chunks)...", Mth.floor(integer10 / (float)integer9 * 100.0f), integer10, integer9);
            }
            if (!booleanSupplier.getAsBoolean()) {
                aof6.cancel();
            }
            else {
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {}
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
