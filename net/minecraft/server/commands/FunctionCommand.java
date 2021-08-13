package net.minecraft.server.commands;

import net.minecraft.server.ServerFunctionManager;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.commands.CommandFunction;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public class FunctionCommand {
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_FUNCTION;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("function").requires(db -> db.hasPermission(2))).then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)FunctionArgument.functions()).suggests((SuggestionProvider)FunctionCommand.SUGGEST_FUNCTION).executes(commandContext -> runFunction((CommandSourceStack)commandContext.getSource(), FunctionArgument.getFunctions((CommandContext<CommandSourceStack>)commandContext, "name")))));
    }
    
    private static int runFunction(final CommandSourceStack db, final Collection<CommandFunction> collection) {
        int integer3 = 0;
        for (final CommandFunction cy5 : collection) {
            integer3 += db.getServer().getFunctions().execute(cy5, db.withSuppressedOutput().withMaximumPermission(2));
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.function.success.single", new Object[] { integer3, ((CommandFunction)collection.iterator().next()).getId() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.function.success.multiple", new Object[] { integer3, collection.size() }), true);
        }
        return integer3;
    }
    
    static {
        SUGGEST_FUNCTION = ((commandContext, suggestionsBuilder) -> {
            final ServerFunctionManager vx3 = ((CommandSourceStack)commandContext.getSource()).getServer().getFunctions();
            SharedSuggestionProvider.suggestResource(vx3.getTagNames(), suggestionsBuilder, "#");
            return SharedSuggestionProvider.suggestResource(vx3.getFunctionNames(), suggestionsBuilder);
        });
    }
}
