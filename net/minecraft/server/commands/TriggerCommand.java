package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.scores.Score;
import java.util.Iterator;
import net.minecraft.world.scores.Scoreboard;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.Objective;
import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ObjectiveArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class TriggerCommand {
    private static final SimpleCommandExceptionType ERROR_NOT_PRIMED;
    private static final SimpleCommandExceptionType ERROR_INVALID_OBJECTIVE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)Commands.literal("trigger").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()).suggests((commandContext, suggestionsBuilder) -> suggestObjectives((CommandSourceStack)commandContext.getSource(), suggestionsBuilder)).executes(commandContext -> simpleTrigger((CommandSourceStack)commandContext.getSource(), getScore(((CommandSourceStack)commandContext.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)commandContext, "objective"))))).then(Commands.literal("add").then(Commands.argument("value", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> addValue((CommandSourceStack)commandContext.getSource(), getScore(((CommandSourceStack)commandContext.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)commandContext, "objective")), IntegerArgumentType.getInteger(commandContext, "value")))))).then(Commands.literal("set").then(Commands.argument("value", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> setValue((CommandSourceStack)commandContext.getSource(), getScore(((CommandSourceStack)commandContext.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)commandContext, "objective")), IntegerArgumentType.getInteger(commandContext, "value")))))));
    }
    
    public static CompletableFuture<Suggestions> suggestObjectives(final CommandSourceStack db, final SuggestionsBuilder suggestionsBuilder) {
        final Entity apx3 = db.getEntity();
        final List<String> list4 = (List<String>)Lists.newArrayList();
        if (apx3 != null) {
            final Scoreboard ddk5 = db.getServer().getScoreboard();
            final String string6 = apx3.getScoreboardName();
            for (final Objective ddh8 : ddk5.getObjectives()) {
                if (ddh8.getCriteria() == ObjectiveCriteria.TRIGGER && ddk5.hasPlayerScore(string6, ddh8)) {
                    final Score ddj9 = ddk5.getOrCreatePlayerScore(string6, ddh8);
                    if (ddj9.isLocked()) {
                        continue;
                    }
                    list4.add(ddh8.getName());
                }
            }
        }
        return SharedSuggestionProvider.suggest((Iterable<String>)list4, suggestionsBuilder);
    }
    
    private static int addValue(final CommandSourceStack db, final Score ddj, final int integer) {
        ddj.add(integer);
        db.sendSuccess(new TranslatableComponent("commands.trigger.add.success", new Object[] { ddj.getObjective().getFormattedDisplayName(), integer }), true);
        return ddj.getScore();
    }
    
    private static int setValue(final CommandSourceStack db, final Score ddj, final int integer) {
        ddj.setScore(integer);
        db.sendSuccess(new TranslatableComponent("commands.trigger.set.success", new Object[] { ddj.getObjective().getFormattedDisplayName(), integer }), true);
        return integer;
    }
    
    private static int simpleTrigger(final CommandSourceStack db, final Score ddj) {
        ddj.add(1);
        db.sendSuccess(new TranslatableComponent("commands.trigger.simple.success", new Object[] { ddj.getObjective().getFormattedDisplayName() }), true);
        return ddj.getScore();
    }
    
    private static Score getScore(final ServerPlayer aah, final Objective ddh) throws CommandSyntaxException {
        if (ddh.getCriteria() != ObjectiveCriteria.TRIGGER) {
            throw TriggerCommand.ERROR_INVALID_OBJECTIVE.create();
        }
        final Scoreboard ddk3 = aah.getScoreboard();
        final String string4 = aah.getScoreboardName();
        if (!ddk3.hasPlayerScore(string4, ddh)) {
            throw TriggerCommand.ERROR_NOT_PRIMED.create();
        }
        final Score ddj5 = ddk3.getOrCreatePlayerScore(string4, ddh);
        if (ddj5.isLocked()) {
            throw TriggerCommand.ERROR_NOT_PRIMED.create();
        }
        ddj5.setLocked(true);
        return ddj5;
    }
    
    static {
        ERROR_NOT_PRIMED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.trigger.failed.unprimed"));
        ERROR_INVALID_OBJECTIVE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.trigger.failed.invalid"));
    }
}
