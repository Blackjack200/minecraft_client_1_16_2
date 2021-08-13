package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.server.players.PlayerList;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Collection;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.function.Function;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class ListPlayersCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list").executes(commandContext -> listPlayers((CommandSourceStack)commandContext.getSource()))).then(Commands.literal("uuids").executes(commandContext -> listPlayersWithUuids((CommandSourceStack)commandContext.getSource()))));
    }
    
    private static int listPlayers(final CommandSourceStack db) {
        return format(db, (Function<ServerPlayer, Component>)Player::getDisplayName);
    }
    
    private static int listPlayersWithUuids(final CommandSourceStack db) {
        return format(db, (Function<ServerPlayer, Component>)(aah -> new TranslatableComponent("commands.list.nameAndId", new Object[] { aah.getName(), aah.getGameProfile().getId() })));
    }
    
    private static int format(final CommandSourceStack db, final Function<ServerPlayer, Component> function) {
        final PlayerList acs3 = db.getServer().getPlayerList();
        final List<ServerPlayer> list4 = acs3.getPlayers();
        final Component nr5 = ComponentUtils.<ServerPlayer>formatList((java.util.Collection<ServerPlayer>)list4, function);
        db.sendSuccess(new TranslatableComponent("commands.list.players", new Object[] { list4.size(), acs3.getMaxPlayers(), nr5 }), false);
        return list4.size();
    }
}
