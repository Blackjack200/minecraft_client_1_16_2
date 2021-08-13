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

public class WeatherCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("weather").requires(db -> db.hasPermission(2))).then(((LiteralArgumentBuilder)Commands.literal("clear").executes(commandContext -> setClear((CommandSourceStack)commandContext.getSource(), 6000))).then(Commands.argument("duration", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0, 1000000)).executes(commandContext -> setClear((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "duration") * 20))))).then(((LiteralArgumentBuilder)Commands.literal("rain").executes(commandContext -> setRain((CommandSourceStack)commandContext.getSource(), 6000))).then(Commands.argument("duration", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0, 1000000)).executes(commandContext -> setRain((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "duration") * 20))))).then(((LiteralArgumentBuilder)Commands.literal("thunder").executes(commandContext -> setThunder((CommandSourceStack)commandContext.getSource(), 6000))).then(Commands.argument("duration", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0, 1000000)).executes(commandContext -> setThunder((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "duration") * 20)))));
    }
    
    private static int setClear(final CommandSourceStack db, final int integer) {
        db.getLevel().setWeatherParameters(integer, 0, false, false);
        db.sendSuccess(new TranslatableComponent("commands.weather.set.clear"), true);
        return integer;
    }
    
    private static int setRain(final CommandSourceStack db, final int integer) {
        db.getLevel().setWeatherParameters(0, integer, true, false);
        db.sendSuccess(new TranslatableComponent("commands.weather.set.rain"), true);
        return integer;
    }
    
    private static int setThunder(final CommandSourceStack db, final int integer) {
        db.getLevel().setWeatherParameters(0, integer, true, true);
        db.sendSuccess(new TranslatableComponent("commands.weather.set.thunder"), true);
        return integer;
    }
}
