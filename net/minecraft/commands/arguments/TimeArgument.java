package net.minecraft.commands.arguments;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class TimeArgument implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES;
    private static final SimpleCommandExceptionType ERROR_INVALID_UNIT;
    private static final DynamicCommandExceptionType ERROR_INVALID_TICK_COUNT;
    private static final Object2IntMap<String> UNITS;
    
    public static TimeArgument time() {
        return new TimeArgument();
    }
    
    public Integer parse(final StringReader stringReader) throws CommandSyntaxException {
        final float float3 = stringReader.readFloat();
        final String string4 = stringReader.readUnquotedString();
        final int integer5 = TimeArgument.UNITS.getOrDefault(string4, 0);
        if (integer5 == 0) {
            throw TimeArgument.ERROR_INVALID_UNIT.create();
        }
        final int integer6 = Math.round(float3 * integer5);
        if (integer6 < 0) {
            throw TimeArgument.ERROR_INVALID_TICK_COUNT.create(integer6);
        }
        return integer6;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        final StringReader stringReader4 = new StringReader(suggestionsBuilder.getRemaining());
        try {
            stringReader4.readFloat();
        }
        catch (CommandSyntaxException commandSyntaxException5) {
            return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
        }
        return SharedSuggestionProvider.suggest((Iterable<String>)TimeArgument.UNITS.keySet(), suggestionsBuilder.createOffset(suggestionsBuilder.getStart() + stringReader4.getCursor()));
    }
    
    public Collection<String> getExamples() {
        return TimeArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0d", "0s", "0t", "0" });
        ERROR_INVALID_UNIT = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.time.invalid_unit"));
        ERROR_INVALID_TICK_COUNT = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.time.invalid_tick_count", new Object[] { object }));
        (UNITS = (Object2IntMap)new Object2IntOpenHashMap()).put("d", 24000);
        TimeArgument.UNITS.put("s", 20);
        TimeArgument.UNITS.put("t", 1);
        TimeArgument.UNITS.put("", 1);
    }
}
