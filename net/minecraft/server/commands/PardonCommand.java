package net.minecraft.server.commands;

import net.minecraft.server.players.StoredUserList;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.server.players.UserBanList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.ComponentUtils;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class PardonCommand {
    private static final SimpleCommandExceptionType ERROR_NOT_BANNED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon").requires(db -> db.hasPermission(3))).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)GameProfileArgument.gameProfile()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getBans().getUserList(), suggestionsBuilder)).executes(commandContext -> pardonPlayers((CommandSourceStack)commandContext.getSource(), GameProfileArgument.getGameProfiles((CommandContext<CommandSourceStack>)commandContext, "targets")))));
    }
    
    private static int pardonPlayers(final CommandSourceStack db, final Collection<GameProfile> collection) throws CommandSyntaxException {
        final UserBanList acx3 = db.getServer().getPlayerList().getBans();
        int integer4 = 0;
        for (final GameProfile gameProfile6 : collection) {
            if (acx3.isBanned(gameProfile6)) {
                ((StoredUserList<GameProfile, V>)acx3).remove(gameProfile6);
                ++integer4;
                db.sendSuccess(new TranslatableComponent("commands.pardon.success", new Object[] { ComponentUtils.getDisplayName(gameProfile6) }), true);
            }
        }
        if (integer4 == 0) {
            throw PardonCommand.ERROR_NOT_BANNED.create();
        }
        return integer4;
    }
    
    static {
        ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardon.failed"));
    }
}
