package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.server.players.PlayerList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class DeOpCommands {
    private static final SimpleCommandExceptionType ERROR_NOT_OP;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deop").requires(db -> db.hasPermission(3))).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)GameProfileArgument.gameProfile()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getOpNames(), suggestionsBuilder)).executes(commandContext -> deopPlayers((CommandSourceStack)commandContext.getSource(), GameProfileArgument.getGameProfiles((CommandContext<CommandSourceStack>)commandContext, "targets")))));
    }
    
    private static int deopPlayers(final CommandSourceStack db, final Collection<GameProfile> collection) throws CommandSyntaxException {
        final PlayerList acs3 = db.getServer().getPlayerList();
        int integer4 = 0;
        for (final GameProfile gameProfile6 : collection) {
            if (acs3.isOp(gameProfile6)) {
                acs3.deop(gameProfile6);
                ++integer4;
                db.sendSuccess(new TranslatableComponent("commands.deop.success", new Object[] { ((GameProfile)collection.iterator().next()).getName() }), true);
            }
        }
        if (integer4 == 0) {
            throw DeOpCommands.ERROR_NOT_OP.create();
        }
        db.getServer().kickUnlistedPlayers(db);
        return integer4;
    }
    
    static {
        ERROR_NOT_OP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.deop.failed"));
    }
}
