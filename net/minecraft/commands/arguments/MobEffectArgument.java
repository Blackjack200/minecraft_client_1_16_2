package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.world.effect.MobEffect;
import com.mojang.brigadier.arguments.ArgumentType;

public class MobEffectArgument implements ArgumentType<MobEffect> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_EFFECT;
    
    public static MobEffectArgument effect() {
        return new MobEffectArgument();
    }
    
    public static MobEffect getEffect(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return (MobEffect)commandContext.getArgument(string, (Class)MobEffect.class);
    }
    
    public MobEffect parse(final StringReader stringReader) throws CommandSyntaxException {
        final ResourceLocation vk3 = ResourceLocation.read(stringReader);
        return (MobEffect)Registry.MOB_EFFECT.getOptional(vk3).orElseThrow(() -> MobEffectArgument.ERROR_UNKNOWN_EFFECT.create(vk3));
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)Registry.MOB_EFFECT.keySet(), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return MobEffectArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "spooky", "effect" });
        ERROR_UNKNOWN_EFFECT = new DynamicCommandExceptionType(object -> new TranslatableComponent("effect.effectNotFound", new Object[] { object }));
    }
}
