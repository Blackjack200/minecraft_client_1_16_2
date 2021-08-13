package net.minecraft.server.commands;

import net.minecraft.server.players.StoredUserList;
import com.mojang.brigadier.Message;
import net.minecraft.server.level.ServerPlayer;
import java.util.stream.Stream;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.StoredUserEntry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.players.UserWhiteListEntry;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class WhitelistCommand {
    private static final SimpleCommandExceptionType ERROR_ALREADY_ENABLED;
    private static final SimpleCommandExceptionType ERROR_ALREADY_DISABLED;
    private static final SimpleCommandExceptionType ERROR_ALREADY_WHITELISTED;
    private static final SimpleCommandExceptionType ERROR_NOT_WHITELISTED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("whitelist").requires(db -> db.hasPermission(3))).then(Commands.literal("on").executes(commandContext -> enableWhitelist((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("off").executes(commandContext -> disableWhitelist((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("list").executes(commandContext -> showList((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("add").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)GameProfileArgument.gameProfile()).suggests((commandContext, suggestionsBuilder) -> {
            final PlayerList acs3 = ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList();
            return SharedSuggestionProvider.suggest((Stream<String>)acs3.getPlayers().stream().filter(aah -> !acs3.getWhiteList().isWhiteListed(aah.getGameProfile())).map(aah -> aah.getGameProfile().getName()), suggestionsBuilder);
        }).executes(commandContext -> addPlayers((CommandSourceStack)commandContext.getSource(), GameProfileArgument.getGameProfiles((CommandContext<CommandSourceStack>)commandContext, "targets")))))).then(Commands.literal("remove").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)GameProfileArgument.gameProfile()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getWhiteListNames(), suggestionsBuilder)).executes(commandContext -> removePlayers((CommandSourceStack)commandContext.getSource(), GameProfileArgument.getGameProfiles((CommandContext<CommandSourceStack>)commandContext, "targets")))))).then(Commands.literal("reload").executes(commandContext -> reload((CommandSourceStack)commandContext.getSource()))));
    }
    
    private static int reload(final CommandSourceStack db) {
        db.getServer().getPlayerList().reloadWhiteList();
        db.sendSuccess(new TranslatableComponent("commands.whitelist.reloaded"), true);
        db.getServer().kickUnlistedPlayers(db);
        return 1;
    }
    
    private static int addPlayers(final CommandSourceStack db, final Collection<GameProfile> collection) throws CommandSyntaxException {
        final UserWhiteList acz3 = db.getServer().getPlayerList().getWhiteList();
        int integer4 = 0;
        for (final GameProfile gameProfile6 : collection) {
            if (!acz3.isWhiteListed(gameProfile6)) {
                final UserWhiteListEntry ada7 = new UserWhiteListEntry(gameProfile6);
                ((StoredUserList<K, UserWhiteListEntry>)acz3).add(ada7);
                db.sendSuccess(new TranslatableComponent("commands.whitelist.add.success", new Object[] { ComponentUtils.getDisplayName(gameProfile6) }), true);
                ++integer4;
            }
        }
        if (integer4 == 0) {
            throw WhitelistCommand.ERROR_ALREADY_WHITELISTED.create();
        }
        return integer4;
    }
    
    private static int removePlayers(final CommandSourceStack db, final Collection<GameProfile> collection) throws CommandSyntaxException {
        final UserWhiteList acz3 = db.getServer().getPlayerList().getWhiteList();
        int integer4 = 0;
        for (final GameProfile gameProfile6 : collection) {
            if (acz3.isWhiteListed(gameProfile6)) {
                final UserWhiteListEntry ada7 = new UserWhiteListEntry(gameProfile6);
                ((StoredUserList<GameProfile, V>)acz3).remove(ada7);
                db.sendSuccess(new TranslatableComponent("commands.whitelist.remove.success", new Object[] { ComponentUtils.getDisplayName(gameProfile6) }), true);
                ++integer4;
            }
        }
        if (integer4 == 0) {
            throw WhitelistCommand.ERROR_NOT_WHITELISTED.create();
        }
        db.getServer().kickUnlistedPlayers(db);
        return integer4;
    }
    
    private static int enableWhitelist(final CommandSourceStack db) throws CommandSyntaxException {
        final PlayerList acs2 = db.getServer().getPlayerList();
        if (acs2.isUsingWhitelist()) {
            throw WhitelistCommand.ERROR_ALREADY_ENABLED.create();
        }
        acs2.setUsingWhiteList(true);
        db.sendSuccess(new TranslatableComponent("commands.whitelist.enabled"), true);
        db.getServer().kickUnlistedPlayers(db);
        return 1;
    }
    
    private static int disableWhitelist(final CommandSourceStack db) throws CommandSyntaxException {
        final PlayerList acs2 = db.getServer().getPlayerList();
        if (!acs2.isUsingWhitelist()) {
            throw WhitelistCommand.ERROR_ALREADY_DISABLED.create();
        }
        acs2.setUsingWhiteList(false);
        db.sendSuccess(new TranslatableComponent("commands.whitelist.disabled"), true);
        return 1;
    }
    
    private static int showList(final CommandSourceStack db) {
        final String[] arr2 = db.getServer().getPlayerList().getWhiteListNames();
        if (arr2.length == 0) {
            db.sendSuccess(new TranslatableComponent("commands.whitelist.none"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.whitelist.list", new Object[] { arr2.length, String.join(", ", (CharSequence[])arr2) }), false);
        }
        return arr2.length;
    }
    
    static {
        ERROR_ALREADY_ENABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.alreadyOn"));
        ERROR_ALREADY_DISABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.alreadyOff"));
        ERROR_ALREADY_WHITELISTED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.add.failed"));
        ERROR_NOT_WHITELISTED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.remove.failed"));
    }
}
