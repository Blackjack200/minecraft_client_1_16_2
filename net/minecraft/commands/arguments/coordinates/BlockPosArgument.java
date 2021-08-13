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
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class BlockPosArgument implements ArgumentType<Coordinates> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_LOADED;
    public static final SimpleCommandExceptionType ERROR_OUT_OF_WORLD;
    
    public static BlockPosArgument blockPos() {
        return new BlockPosArgument();
    }
    
    public static BlockPos getLoadedBlockPos(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final BlockPos fx3 = ((Coordinates)commandContext.getArgument(string, (Class)Coordinates.class)).getBlockPos((CommandSourceStack)commandContext.getSource());
        if (!((CommandSourceStack)commandContext.getSource()).getLevel().hasChunkAt(fx3)) {
            throw BlockPosArgument.ERROR_NOT_LOADED.create();
        }
        ((CommandSourceStack)commandContext.getSource()).getLevel();
        if (!Level.isInWorldBounds(fx3)) {
            throw BlockPosArgument.ERROR_OUT_OF_WORLD.create();
        }
        return fx3;
    }
    
    public static BlockPos getOrLoadBlockPos(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Coordinates)commandContext.getArgument(string, (Class)Coordinates.class)).getBlockPos((CommandSourceStack)commandContext.getSource());
    }
    
    public Coordinates parse(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '^') {
            return LocalCoordinates.parse(stringReader);
        }
        return WorldCoordinates.parseInt(stringReader);
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
            return SharedSuggestionProvider.suggestCoordinates(string4, collection5, suggestionsBuilder, Commands.createValidator(this::parse));
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return BlockPosArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5" });
        ERROR_NOT_LOADED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.unloaded"));
        ERROR_OUT_OF_WORLD = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.outofworld"));
    }
}
