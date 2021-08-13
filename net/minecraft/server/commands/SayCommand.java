package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class SayCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("say").requires(db -> db.hasPermission(2))).then(Commands.argument("message", (com.mojang.brigadier.arguments.ArgumentType<Object>)MessageArgument.message()).executes(commandContext -> {
            final Component nr2 = MessageArgument.getMessage((CommandContext<CommandSourceStack>)commandContext, "message");
            final TranslatableComponent of3 = new TranslatableComponent("chat.type.announcement", new Object[] { ((CommandSourceStack)commandContext.getSource()).getDisplayName(), nr2 });
            final Entity apx4 = ((CommandSourceStack)commandContext.getSource()).getEntity();
            if (apx4 != null) {
                ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().broadcastMessage(of3, ChatType.CHAT, apx4.getUUID());
            }
            else {
                ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().broadcastMessage(of3, ChatType.SYSTEM, Util.NIL_UUID);
            }
            return 1;
        })));
    }
}
