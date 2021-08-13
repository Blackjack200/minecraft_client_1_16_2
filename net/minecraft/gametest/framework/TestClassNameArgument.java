package net.minecraft.gametest.framework;

import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.TextComponent;
import com.mojang.brigadier.StringReader;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class TestClassNameArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES;
    
    public String parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.readUnquotedString();
        if (GameTestRegistry.isTestClass(string3)) {
            return string3;
        }
        final Message message4 = (Message)new TextComponent("No such test class: " + string3);
        throw new CommandSyntaxException((CommandExceptionType)new SimpleCommandExceptionType(message4), message4);
    }
    
    public static TestClassNameArgument testClassName() {
        return new TestClassNameArgument();
    }
    
    public static String getTestClassName(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (String)commandContext.getArgument(string, (Class)String.class);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest((Stream<String>)GameTestRegistry.getAllTestClassNames().stream(), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return TestClassNameArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "techtests", "mobtests" });
    }
}
