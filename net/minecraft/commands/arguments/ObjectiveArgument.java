package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class ObjectiveArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_OBJECTIVE_NOT_FOUND;
    private static final DynamicCommandExceptionType ERROR_OBJECTIVE_READ_ONLY;
    public static final DynamicCommandExceptionType ERROR_OBJECTIVE_NAME_TOO_LONG;
    
    public static ObjectiveArgument objective() {
        return new ObjectiveArgument();
    }
    
    public static Objective getObjective(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final String string2 = (String)commandContext.getArgument(string, (Class)String.class);
        final Scoreboard ddk4 = ((CommandSourceStack)commandContext.getSource()).getServer().getScoreboard();
        final Objective ddh5 = ddk4.getObjective(string2);
        if (ddh5 == null) {
            throw ObjectiveArgument.ERROR_OBJECTIVE_NOT_FOUND.create(string2);
        }
        return ddh5;
    }
    
    public static Objective getWritableObjective(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final Objective ddh3 = getObjective(commandContext, string);
        if (ddh3.getCriteria().isReadOnly()) {
            throw ObjectiveArgument.ERROR_OBJECTIVE_READ_ONLY.create(ddh3.getName());
        }
        return ddh3;
    }
    
    public String parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.readUnquotedString();
        if (string3.length() > 16) {
            throw ObjectiveArgument.ERROR_OBJECTIVE_NAME_TOO_LONG.create(16);
        }
        return string3;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof CommandSourceStack) {
            return SharedSuggestionProvider.suggest((Iterable<String>)((CommandSourceStack)commandContext.getSource()).getServer().getScoreboard().getObjectiveNames(), suggestionsBuilder);
        }
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            final SharedSuggestionProvider dd4 = (SharedSuggestionProvider)commandContext.getSource();
            return dd4.customSuggestion((CommandContext<SharedSuggestionProvider>)commandContext, suggestionsBuilder);
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return ObjectiveArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "*", "012" });
        ERROR_OBJECTIVE_NOT_FOUND = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.objective.notFound", new Object[] { object }));
        ERROR_OBJECTIVE_READ_ONLY = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.objective.readonly", new Object[] { object }));
        ERROR_OBJECTIVE_NAME_TOO_LONG = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.scoreboard.objectives.add.longName", new Object[] { object }));
    }
}
