package net.minecraft.server.commands;

import net.minecraft.server.players.StoredUserList;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.server.players.IpBanList;
import net.minecraft.world.entity.Entity;
import java.util.List;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Date;
import net.minecraft.server.players.IpBanListEntry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import java.util.regex.Matcher;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.MessageArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.regex.Pattern;

public class BanIpCommands {
    public static final Pattern IP_ADDRESS_PATTERN;
    private static final SimpleCommandExceptionType ERROR_INVALID_IP;
    private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban-ip").requires(db -> db.hasPermission(3))).then(((RequiredArgumentBuilder)Commands.argument("target", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).executes(commandContext -> banIpOrName((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "target"), null))).then(Commands.argument("reason", (com.mojang.brigadier.arguments.ArgumentType<Object>)MessageArgument.message()).executes(commandContext -> banIpOrName((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "target"), MessageArgument.getMessage((CommandContext<CommandSourceStack>)commandContext, "reason"))))));
    }
    
    private static int banIpOrName(final CommandSourceStack db, final String string, @Nullable final Component nr) throws CommandSyntaxException {
        final Matcher matcher4 = BanIpCommands.IP_ADDRESS_PATTERN.matcher((CharSequence)string);
        if (matcher4.matches()) {
            return banIp(db, string, nr);
        }
        final ServerPlayer aah5 = db.getServer().getPlayerList().getPlayerByName(string);
        if (aah5 != null) {
            return banIp(db, aah5.getIpAddress(), nr);
        }
        throw BanIpCommands.ERROR_INVALID_IP.create();
    }
    
    private static int banIp(final CommandSourceStack db, final String string, @Nullable final Component nr) throws CommandSyntaxException {
        final IpBanList acp4 = db.getServer().getPlayerList().getIpBans();
        if (acp4.isBanned(string)) {
            throw BanIpCommands.ERROR_ALREADY_BANNED.create();
        }
        final List<ServerPlayer> list5 = db.getServer().getPlayerList().getPlayersWithAddress(string);
        final IpBanListEntry acq6 = new IpBanListEntry(string, null, db.getTextName(), null, (nr == null) ? null : nr.getString());
        ((StoredUserList<K, IpBanListEntry>)acp4).add(acq6);
        db.sendSuccess(new TranslatableComponent("commands.banip.success", new Object[] { string, acq6.getReason() }), true);
        if (!list5.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.banip.info", new Object[] { list5.size(), EntitySelector.joinNames(list5) }), true);
        }
        for (final ServerPlayer aah8 : list5) {
            aah8.connection.disconnect(new TranslatableComponent("multiplayer.disconnect.ip_banned"));
        }
        return list5.size();
    }
    
    static {
        IP_ADDRESS_PATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        ERROR_INVALID_IP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.banip.invalid"));
        ERROR_ALREADY_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.banip.failed"));
    }
}
