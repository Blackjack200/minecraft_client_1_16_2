package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Style;

public class TeamMsgCommand {
    private static final Style SUGGEST_STYLE;
    private static final SimpleCommandExceptionType ERROR_NOT_ON_TEAM;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralCommandNode<CommandSourceStack> literalCommandNode2 = (LiteralCommandNode<CommandSourceStack>)commandDispatcher.register((LiteralArgumentBuilder)Commands.literal("teammsg").then(Commands.argument("message", (com.mojang.brigadier.arguments.ArgumentType<Object>)MessageArgument.message()).executes(commandContext -> sendMessage((CommandSourceStack)commandContext.getSource(), MessageArgument.getMessage((CommandContext<CommandSourceStack>)commandContext, "message")))));
        commandDispatcher.register((LiteralArgumentBuilder)Commands.literal("tm").redirect((CommandNode)literalCommandNode2));
    }
    
    private static int sendMessage(final CommandSourceStack db, final Component nr) throws CommandSyntaxException {
        final Entity apx3 = db.getEntityOrException();
        final PlayerTeam ddi4 = (PlayerTeam)apx3.getTeam();
        if (ddi4 == null) {
            throw TeamMsgCommand.ERROR_NOT_ON_TEAM.create();
        }
        final Component nr2 = ddi4.getFormattedDisplayName().withStyle(TeamMsgCommand.SUGGEST_STYLE);
        final List<ServerPlayer> list6 = db.getServer().getPlayerList().getPlayers();
        for (final ServerPlayer aah8 : list6) {
            if (aah8 == apx3) {
                aah8.sendMessage(new TranslatableComponent("chat.type.team.sent", new Object[] { nr2, db.getDisplayName(), nr }), apx3.getUUID());
            }
            else {
                if (aah8.getTeam() != ddi4) {
                    continue;
                }
                aah8.sendMessage(new TranslatableComponent("chat.type.team.text", new Object[] { nr2, db.getDisplayName(), nr }), apx3.getUUID());
            }
        }
        return list6.size();
    }
    
    static {
        SUGGEST_STYLE = Style.EMPTY.withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TranslatableComponent("chat.type.team.hover"))).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/teammsg "));
        ERROR_NOT_ON_TEAM = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.teammsg.failed.noteam"));
    }
}
