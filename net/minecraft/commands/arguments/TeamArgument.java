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
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class TeamArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_TEAM_NOT_FOUND;
    
    public static TeamArgument team() {
        return new TeamArgument();
    }
    
    public static PlayerTeam getTeam(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final String string2 = (String)commandContext.getArgument(string, (Class)String.class);
        final Scoreboard ddk4 = ((CommandSourceStack)commandContext.getSource()).getServer().getScoreboard();
        final PlayerTeam ddi5 = ddk4.getPlayerTeam(string2);
        if (ddi5 == null) {
            throw TeamArgument.ERROR_TEAM_NOT_FOUND.create(string2);
        }
        return ddi5;
    }
    
    public String parse(final StringReader stringReader) throws CommandSyntaxException {
        return stringReader.readUnquotedString();
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            return SharedSuggestionProvider.suggest((Iterable<String>)((SharedSuggestionProvider)commandContext.getSource()).getAllTeams(), suggestionsBuilder);
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return TeamArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "123" });
        ERROR_TEAM_NOT_FOUND = new DynamicCommandExceptionType(object -> new TranslatableComponent("team.notFound", new Object[] { object }));
    }
}
