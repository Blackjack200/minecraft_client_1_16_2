package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.Scoreboard;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class ScoreboardSlotArgument implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
    
    private ScoreboardSlotArgument() {
    }
    
    public static ScoreboardSlotArgument displaySlot() {
        return new ScoreboardSlotArgument();
    }
    
    public static int getDisplaySlot(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (int)commandContext.getArgument(string, (Class)Integer.class);
    }
    
    public Integer parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.readUnquotedString();
        final int integer4 = Scoreboard.getDisplaySlotByName(string3);
        if (integer4 == -1) {
            throw ScoreboardSlotArgument.ERROR_INVALID_VALUE.create(string3);
        }
        return integer4;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest(Scoreboard.getDisplaySlotNames(), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return ScoreboardSlotArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "sidebar", "foo.bar" });
        ERROR_INVALID_VALUE = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.scoreboardDisplaySlot.invalid", new Object[] { object }));
    }
}
