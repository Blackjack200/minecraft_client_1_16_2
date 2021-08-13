package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.world.item.enchantment.Enchantment;
import com.mojang.brigadier.arguments.ArgumentType;

public class ItemEnchantmentArgument implements ArgumentType<Enchantment> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENCHANTMENT;
    
    public static ItemEnchantmentArgument enchantment() {
        return new ItemEnchantmentArgument();
    }
    
    public static Enchantment getEnchantment(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (Enchantment)commandContext.getArgument(string, (Class)Enchantment.class);
    }
    
    public Enchantment parse(final StringReader stringReader) throws CommandSyntaxException {
        final ResourceLocation vk3 = ResourceLocation.read(stringReader);
        return (Enchantment)Registry.ENCHANTMENT.getOptional(vk3).orElseThrow(() -> ItemEnchantmentArgument.ERROR_UNKNOWN_ENCHANTMENT.create(vk3));
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)Registry.ENCHANTMENT.keySet(), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return ItemEnchantmentArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "unbreaking", "silk_touch" });
        ERROR_UNKNOWN_ENCHANTMENT = new DynamicCommandExceptionType(object -> new TranslatableComponent("enchantment.unknown", new Object[] { object }));
    }
}
