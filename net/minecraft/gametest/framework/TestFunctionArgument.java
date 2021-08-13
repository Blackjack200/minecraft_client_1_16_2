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
import java.util.Optional;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.TextComponent;
import com.mojang.brigadier.StringReader;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class TestFunctionArgument implements ArgumentType<TestFunction> {
    private static final Collection<String> EXAMPLES;
    
    public TestFunction parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.readUnquotedString();
        final Optional<TestFunction> optional4 = GameTestRegistry.findTestFunction(string3);
        if (optional4.isPresent()) {
            return (TestFunction)optional4.get();
        }
        final Message message5 = (Message)new TextComponent("No such test: " + string3);
        throw new CommandSyntaxException((CommandExceptionType)new SimpleCommandExceptionType(message5), message5);
    }
    
    public static TestFunctionArgument testFunctionArgument() {
        return new TestFunctionArgument();
    }
    
    public static TestFunction getTestFunction(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (TestFunction)commandContext.getArgument(string, (Class)TestFunction.class);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        final Stream<String> stream4 = (Stream<String>)GameTestRegistry.getAllTestFunctions().stream().map(TestFunction::getTestName);
        return SharedSuggestionProvider.suggest(stream4, suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return TestFunctionArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "techtests.piston", "techtests" });
    }
}
