package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.arguments.ComponentArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class TellRawCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw").requires(db -> db.hasPermission(2))).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("message", (com.mojang.brigadier.arguments.ArgumentType<Object>)ComponentArgument.textComponent()).executes(commandContext -> {
            int integer2 = 0;
            for (final ServerPlayer aah4 : EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets")) {
                aah4.sendMessage(ComponentUtils.updateForEntity((CommandSourceStack)commandContext.getSource(), ComponentArgument.getComponent((CommandContext<CommandSourceStack>)commandContext, "message"), aah4, 0), Util.NIL_UUID);
                ++integer2;
            }
            return integer2;
        }))));
    }
}
