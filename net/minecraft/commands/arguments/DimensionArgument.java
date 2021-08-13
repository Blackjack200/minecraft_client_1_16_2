package net.minecraft.commands.arguments;

import java.util.stream.Collectors;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.world.level.Level;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.arguments.ArgumentType;

public class DimensionArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
    
    public ResourceLocation parse(final StringReader stringReader) throws CommandSyntaxException {
        return ResourceLocation.read(stringReader);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            return SharedSuggestionProvider.suggestResource((Stream<ResourceLocation>)((SharedSuggestionProvider)commandContext.getSource()).levels().stream().map(ResourceKey::location), suggestionsBuilder);
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return DimensionArgument.EXAMPLES;
    }
    
    public static DimensionArgument dimension() {
        return new DimensionArgument();
    }
    
    public static ServerLevel getDimension(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final ResourceLocation vk3 = (ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class);
        final ResourceKey<Level> vj4 = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, vk3);
        final ServerLevel aag5 = ((CommandSourceStack)commandContext.getSource()).getServer().getLevel(vj4);
        if (aag5 == null) {
            throw DimensionArgument.ERROR_INVALID_VALUE.create(vk3);
        }
        return aag5;
    }
    
    static {
        EXAMPLES = (Collection)Stream.of((Object[])new ResourceKey[] { Level.OVERWORLD, Level.NETHER }).map(vj -> vj.location().toString()).collect(Collectors.toList());
        ERROR_INVALID_VALUE = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.dimension.invalid", new Object[] { object }));
    }
}
