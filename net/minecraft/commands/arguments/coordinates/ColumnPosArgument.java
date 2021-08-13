package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import net.minecraft.commands.Commands;
import java.util.Collections;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class ColumnPosArgument implements ArgumentType<Coordinates> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE;
    
    public static ColumnPosArgument columnPos() {
        return new ColumnPosArgument();
    }
    
    public static ColumnPos getColumnPos(final CommandContext<CommandSourceStack> commandContext, final String string) {
        final BlockPos fx3 = ((Coordinates)commandContext.getArgument(string, (Class)Coordinates.class)).getBlockPos((CommandSourceStack)commandContext.getSource());
        return new ColumnPos(fx3.getX(), fx3.getZ());
    }
    
    public Coordinates parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        if (!stringReader.canRead()) {
            throw ColumnPosArgument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        final WorldCoordinate es4 = WorldCoordinate.parseInt(stringReader);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer3);
            throw ColumnPosArgument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es5 = WorldCoordinate.parseInt(stringReader);
        return new WorldCoordinates(es4, new WorldCoordinate(true, 0.0), es5);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            final String string4 = suggestionsBuilder.getRemaining();
            Collection<SharedSuggestionProvider.TextCoordinates> collection5;
            if (!string4.isEmpty() && string4.charAt(0) == '^') {
                collection5 = (Collection<SharedSuggestionProvider.TextCoordinates>)Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
            }
            else {
                collection5 = ((SharedSuggestionProvider)commandContext.getSource()).getRelevantCoordinates();
            }
            return SharedSuggestionProvider.suggest2DCoordinates(string4, collection5, suggestionsBuilder, Commands.createValidator(this::parse));
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return ColumnPosArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0" });
        ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos2d.incomplete"));
    }
}
