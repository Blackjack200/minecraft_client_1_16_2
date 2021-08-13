package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class StopCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stop").requires(db -> db.hasPermission(4))).executes(commandContext -> {
            ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.stop.stopping"), true);
            ((CommandSourceStack)commandContext.getSource()).getServer().halt(false);
            return 1;
        }));
    }
}
