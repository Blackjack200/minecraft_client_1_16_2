package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.core.particles.ParticleOptions;
import com.mojang.brigadier.arguments.ArgumentType;

public class ParticleArgument implements ArgumentType<ParticleOptions> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE;
    
    public static ParticleArgument particle() {
        return new ParticleArgument();
    }
    
    public static ParticleOptions getParticle(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (ParticleOptions)commandContext.getArgument(string, (Class)ParticleOptions.class);
    }
    
    public ParticleOptions parse(final StringReader stringReader) throws CommandSyntaxException {
        return readParticle(stringReader);
    }
    
    public Collection<String> getExamples() {
        return ParticleArgument.EXAMPLES;
    }
    
    public static ParticleOptions readParticle(final StringReader stringReader) throws CommandSyntaxException {
        final ResourceLocation vk2 = ResourceLocation.read(stringReader);
        final ParticleType<?> hg3 = Registry.PARTICLE_TYPE.getOptional(vk2).orElseThrow(() -> ParticleArgument.ERROR_UNKNOWN_PARTICLE.create(vk2));
        return ParticleArgument.<ParticleOptions>readParticle(stringReader, hg3);
    }
    
    private static <T extends ParticleOptions> T readParticle(final StringReader stringReader, final ParticleType<T> hg) throws CommandSyntaxException {
        return hg.getDeserializer().fromCommand(hg, stringReader);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)Registry.PARTICLE_TYPE.keySet(), suggestionsBuilder);
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "foo:bar", "particle with options" });
        ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType(object -> new TranslatableComponent("particle.notFound", new Object[] { object }));
    }
}
