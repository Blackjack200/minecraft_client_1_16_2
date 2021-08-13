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
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class Vec3Argument implements ArgumentType<Coordinates> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE;
    public static final SimpleCommandExceptionType ERROR_MIXED_TYPE;
    private final boolean centerCorrect;
    
    public Vec3Argument(final boolean boolean1) {
        this.centerCorrect = boolean1;
    }
    
    public static Vec3Argument vec3() {
        return new Vec3Argument(true);
    }
    
    public static Vec3Argument vec3(final boolean boolean1) {
        return new Vec3Argument(boolean1);
    }
    
    public static Vec3 getVec3(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Coordinates)commandContext.getArgument(string, (Class)Coordinates.class)).getPosition((CommandSourceStack)commandContext.getSource());
    }
    
    public static Coordinates getCoordinates(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (Coordinates)commandContext.getArgument(string, (Class)Coordinates.class);
    }
    
    public Coordinates parse(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '^') {
            return LocalCoordinates.parse(stringReader);
        }
        return WorldCoordinates.parseDouble(stringReader, this.centerCorrect);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            final String string4 = suggestionsBuilder.getRemaining();
            Collection<SharedSuggestionProvider.TextCoordinates> collection5;
            if (!string4.isEmpty() && string4.charAt(0) == '^') {
                collection5 = (Collection<SharedSuggestionProvider.TextCoordinates>)Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
            }
            else {
                collection5 = ((SharedSuggestionProvider)commandContext.getSource()).getAbsoluteCoordinates();
            }
            return SharedSuggestionProvider.suggestCoordinates(string4, collection5, suggestionsBuilder, Commands.createValidator(this::parse));
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return Vec3Argument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5" });
        ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos3d.incomplete"));
        ERROR_MIXED_TYPE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.mixed"));
    }
}
