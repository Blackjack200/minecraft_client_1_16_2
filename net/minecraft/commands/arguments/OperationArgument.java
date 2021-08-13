package net.minecraft.commands.arguments;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import net.minecraft.world.scores.Score;
import net.minecraft.util.Mth;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class OperationArgument implements ArgumentType<Operation> {
    private static final Collection<String> EXAMPLES;
    private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION;
    private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO;
    
    public static OperationArgument operation() {
        return new OperationArgument();
    }
    
    public static Operation getOperation(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return (Operation)commandContext.getArgument(string, (Class)Operation.class);
    }
    
    public Operation parse(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead()) {
            final int integer3 = stringReader.getCursor();
            while (stringReader.canRead() && stringReader.peek() != ' ') {
                stringReader.skip();
            }
            return getOperation(stringReader.getString().substring(integer3, stringReader.getCursor()));
        }
        throw OperationArgument.ERROR_INVALID_OPERATION.create();
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest(new String[] { "=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><" }, suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return OperationArgument.EXAMPLES;
    }
    
    private static Operation getOperation(final String string) throws CommandSyntaxException {
        if (string.equals("><")) {
            final int integer3;
            return (ddj1, ddj2) -> {
                integer3 = ddj1.getScore();
                ddj1.setScore(ddj2.getScore());
                ddj2.setScore(integer3);
                return;
            };
        }
        return getSimpleOperation(string);
    }
    
    private static SimpleOperation getSimpleOperation(final String string) throws CommandSyntaxException {
        switch (string) {
            case "=": {
                return (integer1, integer2) -> integer2;
            }
            case "+=": {
                return (integer1, integer2) -> integer1 + integer2;
            }
            case "-=": {
                return (integer1, integer2) -> integer1 - integer2;
            }
            case "*=": {
                return (integer1, integer2) -> integer1 * integer2;
            }
            case "/=": {
                return (integer1, integer2) -> {
                    if (integer2 == 0) {
                        throw OperationArgument.ERROR_DIVIDE_BY_ZERO.create();
                    }
                    else {
                        return Mth.intFloorDiv(integer1, integer2);
                    }
                };
            }
            case "%=": {
                return (integer1, integer2) -> {
                    if (integer2 == 0) {
                        throw OperationArgument.ERROR_DIVIDE_BY_ZERO.create();
                    }
                    else {
                        return Mth.positiveModulo(integer1, integer2);
                    }
                };
            }
            case "<": {
                return Math::min;
            }
            case ">": {
                return Math::max;
            }
            default: {
                throw OperationArgument.ERROR_INVALID_OPERATION.create();
            }
        }
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "=", ">", "<" });
        ERROR_INVALID_OPERATION = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.operation.invalid"));
        ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.operation.div0"));
    }
    
    @FunctionalInterface
    interface SimpleOperation extends Operation {
        int apply(final int integer1, final int integer2) throws CommandSyntaxException;
        
        default void apply(final Score ddj1, final Score ddj2) throws CommandSyntaxException {
            ddj1.setScore(this.apply(ddj1.getScore(), ddj2.getScore()));
        }
    }
    
    @FunctionalInterface
    public interface Operation {
        void apply(final Score ddj1, final Score ddj2) throws CommandSyntaxException;
    }
}
