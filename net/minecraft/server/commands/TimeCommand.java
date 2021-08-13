package net.minecraft.server.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class TimeCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("time").requires(db -> db.hasPermission(2))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set").then(Commands.literal("day").executes(commandContext -> setTime((CommandSourceStack)commandContext.getSource(), 1000)))).then(Commands.literal("noon").executes(commandContext -> setTime((CommandSourceStack)commandContext.getSource(), 6000)))).then(Commands.literal("night").executes(commandContext -> setTime((CommandSourceStack)commandContext.getSource(), 13000)))).then(Commands.literal("midnight").executes(commandContext -> setTime((CommandSourceStack)commandContext.getSource(), 18000)))).then(Commands.argument("time", (com.mojang.brigadier.arguments.ArgumentType<Object>)TimeArgument.time()).executes(commandContext -> setTime((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "time")))))).then(Commands.literal("add").then(Commands.argument("time", (com.mojang.brigadier.arguments.ArgumentType<Object>)TimeArgument.time()).executes(commandContext -> addTime((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "time")))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("query").then(Commands.literal("daytime").executes(commandContext -> queryTime((CommandSourceStack)commandContext.getSource(), getDayTime(((CommandSourceStack)commandContext.getSource()).getLevel()))))).then(Commands.literal("gametime").executes(commandContext -> queryTime((CommandSourceStack)commandContext.getSource(), (int)(((CommandSourceStack)commandContext.getSource()).getLevel().getGameTime() % 2147483647L))))).then(Commands.literal("day").executes(commandContext -> queryTime((CommandSourceStack)commandContext.getSource(), (int)(((CommandSourceStack)commandContext.getSource()).getLevel().getDayTime() / 24000L % 2147483647L))))));
    }
    
    private static int getDayTime(final ServerLevel aag) {
        return (int)(aag.getDayTime() % 24000L);
    }
    
    private static int queryTime(final CommandSourceStack db, final int integer) {
        db.sendSuccess(new TranslatableComponent("commands.time.query", new Object[] { integer }), false);
        return integer;
    }
    
    public static int setTime(final CommandSourceStack db, final int integer) {
        for (final ServerLevel aag4 : db.getServer().getAllLevels()) {
            aag4.setDayTime(integer);
        }
        db.sendSuccess(new TranslatableComponent("commands.time.set", new Object[] { integer }), true);
        return getDayTime(db.getLevel());
    }
    
    public static int addTime(final CommandSourceStack db, final int integer) {
        for (final ServerLevel aag4 : db.getServer().getAllLevels()) {
            aag4.setDayTime(aag4.getDayTime() + integer);
        }
        final int integer2 = getDayTime(db.getLevel());
        db.sendSuccess(new TranslatableComponent("commands.time.set", new Object[] { integer2 }), true);
        return integer2;
    }
}
