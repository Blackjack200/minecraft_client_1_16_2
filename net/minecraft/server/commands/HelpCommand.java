package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Map;
import com.mojang.brigadier.ParseResults;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class HelpCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("help").executes(commandContext -> {
            final Map<CommandNode<CommandSourceStack>, String> map3 = (Map<CommandNode<CommandSourceStack>, String>)commandDispatcher.getSmartUsage((CommandNode)commandDispatcher.getRoot(), commandContext.getSource());
            for (final String string5 : map3.values()) {
                ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TextComponent("/" + string5), false);
            }
            return map3.size();
        })).then(Commands.argument("command", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).executes(commandContext -> {
            final ParseResults<CommandSourceStack> parseResults3 = (ParseResults<CommandSourceStack>)commandDispatcher.parse(StringArgumentType.getString(commandContext, "command"), commandContext.getSource());
            if (parseResults3.getContext().getNodes().isEmpty()) {
                throw HelpCommand.ERROR_FAILED.create();
            }
            final Map<CommandNode<CommandSourceStack>, String> map4 = (Map<CommandNode<CommandSourceStack>, String>)commandDispatcher.getSmartUsage(((ParsedCommandNode)Iterables.getLast((Iterable)parseResults3.getContext().getNodes())).getNode(), commandContext.getSource());
            for (final String string6 : map4.values()) {
                ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TextComponent("/" + parseResults3.getReader().getString() + " " + string6), false);
            }
            return map4.size();
        })));
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.help.failed"));
    }
}
