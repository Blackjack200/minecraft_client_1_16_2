package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class SetPlayerIdleTimeoutCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setidletimeout").requires(db -> db.hasPermission(3))).then(Commands.argument("minutes", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setIdleTimeout((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "minutes")))));
    }
    
    private static int setIdleTimeout(final CommandSourceStack db, final int integer) {
        db.getServer().setPlayerIdleTimeout(integer);
        db.sendSuccess(new TranslatableComponent("commands.setidletimeout.success", new Object[] { integer }), true);
        return integer;
    }
}
