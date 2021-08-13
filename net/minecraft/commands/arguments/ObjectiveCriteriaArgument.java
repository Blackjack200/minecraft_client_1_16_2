package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.stats.Stat;
import java.util.Iterator;
import java.util.List;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.stats.StatType;
import net.minecraft.core.Registry;
import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import com.mojang.brigadier.arguments.ArgumentType;

public class ObjectiveCriteriaArgument implements ArgumentType<ObjectiveCriteria> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
    
    private ObjectiveCriteriaArgument() {
    }
    
    public static ObjectiveCriteriaArgument criteria() {
        return new ObjectiveCriteriaArgument();
    }
    
    public static ObjectiveCriteria getCriteria(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (ObjectiveCriteria)commandContext.getArgument(string, (Class)ObjectiveCriteria.class);
    }
    
    public ObjectiveCriteria parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        while (stringReader.canRead() && stringReader.peek() != ' ') {
            stringReader.skip();
        }
        final String string4 = stringReader.getString().substring(integer3, stringReader.getCursor());
        return (ObjectiveCriteria)ObjectiveCriteria.byName(string4).orElseThrow(() -> {
            stringReader.setCursor(integer3);
            return ObjectiveCriteriaArgument.ERROR_INVALID_VALUE.create(string4);
        });
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        final List<String> list4 = (List<String>)Lists.newArrayList((Iterable)ObjectiveCriteria.CRITERIA_BY_NAME.keySet());
        for (final StatType<?> adx6 : Registry.STAT_TYPE) {
            for (final Object object8 : adx6.getRegistry()) {
                final String string9 = this.getName(adx6, object8);
                list4.add(string9);
            }
        }
        return SharedSuggestionProvider.suggest((Iterable<String>)list4, suggestionsBuilder);
    }
    
    public <T> String getName(final StatType<T> adx, final Object object) {
        return Stat.buildName((StatType<Object>)adx, object);
    }
    
    public Collection<String> getExamples() {
        return ObjectiveCriteriaArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "foo.bar.baz", "minecraft:foo" });
        ERROR_INVALID_VALUE = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.criteria.invalid", new Object[] { object }));
    }
}
