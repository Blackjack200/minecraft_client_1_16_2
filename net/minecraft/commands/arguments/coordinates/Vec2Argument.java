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
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class Vec2Argument implements ArgumentType<Coordinates> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE;
    private final boolean centerCorrect;
    
    public Vec2Argument(final boolean boolean1) {
        this.centerCorrect = boolean1;
    }
    
    public static Vec2Argument vec2() {
        return new Vec2Argument(true);
    }
    
    public static Vec2 getVec2(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final Vec3 dck3 = ((Coordinates)commandContext.getArgument(string, (Class)Coordinates.class)).getPosition((CommandSourceStack)commandContext.getSource());
        return new Vec2((float)dck3.x, (float)dck3.z);
    }
    
    public Coordinates parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        if (!stringReader.canRead()) {
            throw Vec2Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        final WorldCoordinate es4 = WorldCoordinate.parseDouble(stringReader, this.centerCorrect);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer3);
            throw Vec2Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es5 = WorldCoordinate.parseDouble(stringReader, this.centerCorrect);
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
                collection5 = ((SharedSuggestionProvider)commandContext.getSource()).getAbsoluteCoordinates();
            }
            return SharedSuggestionProvider.suggest2DCoordinates(string4, collection5, suggestionsBuilder, Commands.createValidator(this::parse));
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return Vec2Argument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0 0", "~ ~", "0.1 -0.5", "~1 ~-2" });
        ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos2d.incomplete"));
    }
}
