package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import org.apache.logging.log4j.LogManager;
import com.mojang.brigadier.context.CommandContext;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.SharedConstants;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.Mth;
import java.util.Locale;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import javax.annotation.Nullable;
import java.nio.file.spi.FileSystemProvider;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.apache.logging.log4j.Logger;

public class DebugCommand {
    private static final Logger LOGGER;
    private static final SimpleCommandExceptionType ERROR_NOT_RUNNING;
    private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING;
    @Nullable
    private static final FileSystemProvider ZIP_FS_PROVIDER;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debug").requires(db -> db.hasPermission(3))).then(Commands.literal("start").executes(commandContext -> start((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("stop").executes(commandContext -> stop((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("report").executes(commandContext -> report((CommandSourceStack)commandContext.getSource()))));
    }
    
    private static int start(final CommandSourceStack db) throws CommandSyntaxException {
        final MinecraftServer minecraftServer2 = db.getServer();
        if (minecraftServer2.isProfiling()) {
            throw DebugCommand.ERROR_ALREADY_RUNNING.create();
        }
        minecraftServer2.startProfiling();
        db.sendSuccess(new TranslatableComponent("commands.debug.started", new Object[] { "Started the debug profiler. Type '/debug stop' to stop it." }), true);
        return 0;
    }
    
    private static int stop(final CommandSourceStack db) throws CommandSyntaxException {
        final MinecraftServer minecraftServer2 = db.getServer();
        if (!minecraftServer2.isProfiling()) {
            throw DebugCommand.ERROR_NOT_RUNNING.create();
        }
        final ProfileResults ans3 = minecraftServer2.finishProfiling();
        final File file4 = new File(minecraftServer2.getFile("debug"), "profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
        ans3.saveResults(file4);
        final float float5 = ans3.getNanoDuration() / 1.0E9f;
        final float float6 = ans3.getTickDuration() / float5;
        db.sendSuccess(new TranslatableComponent("commands.debug.stopped", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { float5 }), ans3.getTickDuration(), String.format("%.2f", new Object[] { float6 }) }), true);
        return Mth.floor(float6);
    }
    
    private static int report(final CommandSourceStack db) {
        final MinecraftServer minecraftServer2 = db.getServer();
        final String string3 = "debug-report-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        try {
            final Path path5 = minecraftServer2.getFile("debug").toPath();
            Files.createDirectories(path5, new FileAttribute[0]);
            if (SharedConstants.IS_RUNNING_IN_IDE || DebugCommand.ZIP_FS_PROVIDER == null) {
                final Path path6 = path5.resolve(string3);
                minecraftServer2.saveDebugReport(path6);
            }
            else {
                final Path path6 = path5.resolve(string3 + ".zip");
                try (final FileSystem fileSystem6 = DebugCommand.ZIP_FS_PROVIDER.newFileSystem(path6, (Map)ImmutableMap.of("create", "true"))) {
                    minecraftServer2.saveDebugReport(fileSystem6.getPath("/", new String[0]));
                }
            }
            db.sendSuccess(new TranslatableComponent("commands.debug.reportSaved", new Object[] { string3 }), false);
            return 1;
        }
        catch (IOException iOException4) {
            DebugCommand.LOGGER.error("Failed to save debug dump", (Throwable)iOException4);
            db.sendFailure(new TranslatableComponent("commands.debug.reportFailed"));
            return 0;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ERROR_NOT_RUNNING = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.debug.notRunning"));
        ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.debug.alreadyRunning"));
        ZIP_FS_PROVIDER = (FileSystemProvider)FileSystemProvider.installedProviders().stream().filter(fileSystemProvider -> fileSystemProvider.getScheme().equalsIgnoreCase("jar")).findFirst().orElse(null);
    }
}
