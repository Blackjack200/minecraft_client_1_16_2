package net.minecraft.server.commands;

import org.apache.logging.log4j.LogManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.commands.CommandSourceStack;
import java.util.Collection;
import org.apache.logging.log4j.Logger;

public class ReloadCommand {
    private static final Logger LOGGER;
    
    public static void reloadPacks(final Collection<String> collection, final CommandSourceStack db) {
        db.getServer().reloadResources(collection).exceptionally(throwable -> {
            ReloadCommand.LOGGER.warn("Failed to execute reload", throwable);
            db.sendFailure(new TranslatableComponent("commands.reload.failure"));
            return null;
        });
    }
    
    private static Collection<String> discoverNewPacks(final PackRepository abu, final WorldData cyk, final Collection<String> collection) {
        abu.reload();
        final Collection<String> collection2 = (Collection<String>)Lists.newArrayList((Iterable)collection);
        final Collection<String> collection3 = (Collection<String>)cyk.getDataPackConfig().getDisabled();
        for (final String string7 : abu.getAvailableIds()) {
            if (!collection3.contains(string7) && !collection2.contains(string7)) {
                collection2.add(string7);
            }
        }
        return collection2;
    }
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reload").requires(db -> db.hasPermission(2))).executes(commandContext -> {
            final CommandSourceStack db2 = (CommandSourceStack)commandContext.getSource();
            final MinecraftServer minecraftServer3 = db2.getServer();
            final PackRepository abu4 = minecraftServer3.getPackRepository();
            final WorldData cyk5 = minecraftServer3.getWorldData();
            final Collection<String> collection6 = abu4.getSelectedIds();
            final Collection<String> collection7 = discoverNewPacks(abu4, cyk5, collection6);
            db2.sendSuccess(new TranslatableComponent("commands.reload.success"), true);
            reloadPacks(collection7, db2);
            return 0;
        }));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
