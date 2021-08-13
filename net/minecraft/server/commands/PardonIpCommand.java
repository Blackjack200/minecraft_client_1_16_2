package net.minecraft.server.commands;

import net.minecraft.server.players.StoredUserList;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.players.IpBanList;
import java.util.regex.Matcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class PardonIpCommand {
    private static final SimpleCommandExceptionType ERROR_INVALID;
    private static final SimpleCommandExceptionType ERROR_NOT_BANNED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon-ip").requires(db -> db.hasPermission(3))).then(Commands.argument("target", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getIpBans().getUserList(), suggestionsBuilder)).executes(commandContext -> unban((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "target")))));
    }
    
    private static int unban(final CommandSourceStack db, final String string) throws CommandSyntaxException {
        final Matcher matcher3 = BanIpCommands.IP_ADDRESS_PATTERN.matcher((CharSequence)string);
        if (!matcher3.matches()) {
            throw PardonIpCommand.ERROR_INVALID.create();
        }
        final IpBanList acp4 = db.getServer().getPlayerList().getIpBans();
        if (!acp4.isBanned(string)) {
            throw PardonIpCommand.ERROR_NOT_BANNED.create();
        }
        ((StoredUserList<String, V>)acp4).remove(string);
        db.sendSuccess(new TranslatableComponent("commands.pardonip.success", new Object[] { string }), true);
        return 1;
    }
    
    static {
        ERROR_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardonip.invalid"));
        ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardonip.failed"));
    }
}
