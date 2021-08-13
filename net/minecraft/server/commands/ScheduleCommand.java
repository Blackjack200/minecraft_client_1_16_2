package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.world.level.timers.FunctionCallback;
import net.minecraft.world.level.timers.TimerCallback;
import net.minecraft.world.level.timers.FunctionTagCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.timers.TimerQueue;
import net.minecraft.tags.Tag;
import net.minecraft.commands.CommandFunction;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Pair;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.TimeArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ScheduleCommand {
    private static final SimpleCommandExceptionType ERROR_SAME_TICK;
    private static final DynamicCommandExceptionType ERROR_CANT_REMOVE;
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_SCHEDULE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("schedule").requires(db -> db.hasPermission(2))).then(Commands.literal("function").then(Commands.argument("function", (com.mojang.brigadier.arguments.ArgumentType<Object>)FunctionArgument.functions()).suggests((SuggestionProvider)FunctionCommand.SUGGEST_FUNCTION).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("time", (com.mojang.brigadier.arguments.ArgumentType<Object>)TimeArgument.time()).executes(commandContext -> schedule((CommandSourceStack)commandContext.getSource(), FunctionArgument.getFunctionOrTag((CommandContext<CommandSourceStack>)commandContext, "function"), IntegerArgumentType.getInteger(commandContext, "time"), true))).then(Commands.literal("append").executes(commandContext -> schedule((CommandSourceStack)commandContext.getSource(), FunctionArgument.getFunctionOrTag((CommandContext<CommandSourceStack>)commandContext, "function"), IntegerArgumentType.getInteger(commandContext, "time"), false)))).then(Commands.literal("replace").executes(commandContext -> schedule((CommandSourceStack)commandContext.getSource(), FunctionArgument.getFunctionOrTag((CommandContext<CommandSourceStack>)commandContext, "function"), IntegerArgumentType.getInteger(commandContext, "time"), true))))))).then(Commands.literal("clear").then(Commands.argument("function", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).suggests((SuggestionProvider)ScheduleCommand.SUGGEST_SCHEDULE).executes(commandContext -> remove((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "function"))))));
    }
    
    private static int schedule(final CommandSourceStack db, final Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> pair, final int integer, final boolean boolean4) throws CommandSyntaxException {
        if (integer == 0) {
            throw ScheduleCommand.ERROR_SAME_TICK.create();
        }
        final long long5 = db.getLevel().getGameTime() + integer;
        final ResourceLocation vk7 = (ResourceLocation)pair.getFirst();
        final TimerQueue<MinecraftServer> dcc8 = db.getServer().getWorldData().overworldData().getScheduledEvents();
        ((Either)pair.getSecond()).ifLeft(cy -> {
            final String string9 = vk7.toString();
            if (boolean4) {
                dcc8.remove(string9);
            }
            dcc8.schedule(string9, long5, new FunctionCallback(vk7));
            db.sendSuccess(new TranslatableComponent("commands.schedule.created.function", new Object[] { vk7, integer, long5 }), true);
        }).ifRight(aej -> {
            final String string9 = "#" + vk7.toString();
            if (boolean4) {
                dcc8.remove(string9);
            }
            dcc8.schedule(string9, long5, new FunctionTagCallback(vk7));
            db.sendSuccess(new TranslatableComponent("commands.schedule.created.tag", new Object[] { vk7, integer, long5 }), true);
        });
        return (int)Math.floorMod(long5, 2147483647L);
    }
    
    private static int remove(final CommandSourceStack db, final String string) throws CommandSyntaxException {
        final int integer3 = db.getServer().getWorldData().overworldData().getScheduledEvents().remove(string);
        if (integer3 == 0) {
            throw ScheduleCommand.ERROR_CANT_REMOVE.create(string);
        }
        db.sendSuccess(new TranslatableComponent("commands.schedule.cleared.success", new Object[] { integer3, string }), true);
        return integer3;
    }
    
    static {
        ERROR_SAME_TICK = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.schedule.same_tick"));
        ERROR_CANT_REMOVE = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.schedule.cleared.failure", new Object[] { object }));
        SUGGEST_SCHEDULE = ((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest((Iterable<String>)((CommandSourceStack)commandContext.getSource()).getServer().getWorldData().overworldData().getScheduledEvents().getEventsIds(), suggestionsBuilder));
    }
}
