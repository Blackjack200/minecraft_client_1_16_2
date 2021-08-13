package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.ChatFormatting;
import com.mojang.brigadier.arguments.ArgumentType;

public class ColorArgument implements ArgumentType<ChatFormatting> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
    
    private ColorArgument() {
    }
    
    public static ColorArgument color() {
        return new ColorArgument();
    }
    
    public static ChatFormatting getColor(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (ChatFormatting)commandContext.getArgument(string, (Class)ChatFormatting.class);
    }
    
    public ChatFormatting parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.readUnquotedString();
        final ChatFormatting k4 = ChatFormatting.getByName(string3);
        if (k4 == null || k4.isFormat()) {
            throw ColorArgument.ERROR_INVALID_VALUE.create(string3);
        }
        return k4;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest((Iterable<String>)ChatFormatting.getNames(true, false), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return ColorArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "red", "green" });
        ERROR_INVALID_VALUE = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.color.invalid", new Object[] { object }));
    }
}
