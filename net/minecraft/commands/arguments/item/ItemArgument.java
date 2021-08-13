package net.minecraft.commands.arguments.item;

import java.util.Arrays;
import net.minecraft.tags.ItemTags;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class ItemArgument implements ArgumentType<ItemInput> {
    private static final Collection<String> EXAMPLES;
    
    public static ItemArgument item() {
        return new ItemArgument();
    }
    
    public ItemInput parse(final StringReader stringReader) throws CommandSyntaxException {
        final ItemParser ey3 = new ItemParser(stringReader, false).parse();
        return new ItemInput(ey3.getItem(), ey3.getNbt());
    }
    
    public static <S> ItemInput getItem(final CommandContext<S> commandContext, final String string) {
        return (ItemInput)commandContext.getArgument(string, (Class)ItemInput.class);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        final StringReader stringReader4 = new StringReader(suggestionsBuilder.getInput());
        stringReader4.setCursor(suggestionsBuilder.getStart());
        final ItemParser ey5 = new ItemParser(stringReader4, false);
        try {
            ey5.parse();
        }
        catch (CommandSyntaxException ex) {}
        return ey5.fillSuggestions(suggestionsBuilder, ItemTags.getAllTags());
    }
    
    public Collection<String> getExamples() {
        return ItemArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "stick", "minecraft:stick", "stick{foo=bar}" });
    }
}
