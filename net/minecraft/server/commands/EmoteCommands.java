package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class EmoteCommands {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)Commands.literal("me").then(Commands.argument("action", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).executes(commandContext -> {
            final TranslatableComponent of2 = new TranslatableComponent("chat.type.emote", new Object[] { ((CommandSourceStack)commandContext.getSource()).getDisplayName(), StringArgumentType.getString(commandContext, "action") });
            final Entity apx3 = ((CommandSourceStack)commandContext.getSource()).getEntity();
            if (apx3 != null) {
                ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().broadcastMessage(of2, ChatType.CHAT, apx3.getUUID());
            }
            else {
                ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().broadcastMessage(of2, ChatType.SYSTEM, Util.NIL_UUID);
            }
            return 1;
        })));
    }
}
