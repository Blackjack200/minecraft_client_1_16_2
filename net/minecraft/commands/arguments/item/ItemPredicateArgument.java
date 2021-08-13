package net.minecraft.commands.arguments.item;

import net.minecraft.nbt.NbtUtils;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.tags.ItemTags;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class ItemPredicateArgument implements ArgumentType<Result> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
    
    public static ItemPredicateArgument itemPredicate() {
        return new ItemPredicateArgument();
    }
    
    public Result parse(final StringReader stringReader) throws CommandSyntaxException {
        final ItemParser ey3 = new ItemParser(stringReader, true).parse();
        if (ey3.getItem() != null) {
            final ItemPredicate a4 = new ItemPredicate(ey3.getItem(), ey3.getNbt());
            return commandContext -> a4;
        }
        final ResourceLocation vk4 = ey3.getTag();
        final ResourceLocation vk5;
        final Tag<Item> aej4;
        final ItemParser itemParser;
        return commandContext -> {
            aej4 = ((CommandSourceStack)commandContext.getSource()).getServer().getTags().getItems().getTag(vk5);
            if (aej4 == null) {
                throw ItemPredicateArgument.ERROR_UNKNOWN_TAG.create(vk5.toString());
            }
            else {
                return (Predicate<ItemStack>)new TagPredicate(aej4, itemParser.getNbt());
            }
        };
    }
    
    public static Predicate<ItemStack> getItemPredicate(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Result)commandContext.getArgument(string, (Class)Result.class)).create(commandContext);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        final StringReader stringReader4 = new StringReader(suggestionsBuilder.getInput());
        stringReader4.setCursor(suggestionsBuilder.getStart());
        final ItemParser ey5 = new ItemParser(stringReader4, true);
        try {
            ey5.parse();
        }
        catch (CommandSyntaxException ex) {}
        return ey5.fillSuggestions(suggestionsBuilder, ItemTags.getAllTags());
    }
    
    public Collection<String> getExamples() {
        return ItemPredicateArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "stick", "minecraft:stick", "#stick", "#stick{foo=bar}" });
        ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.item.tag.unknown", new Object[] { object }));
    }
    
    static class ItemPredicate implements Predicate<ItemStack> {
        private final Item item;
        @Nullable
        private final CompoundTag nbt;
        
        public ItemPredicate(final Item blu, @Nullable final CompoundTag md) {
            this.item = blu;
            this.nbt = md;
        }
        
        public boolean test(final ItemStack bly) {
            return bly.getItem() == this.item && NbtUtils.compareNbt(this.nbt, bly.getTag(), true);
        }
    }
    
    static class TagPredicate implements Predicate<ItemStack> {
        private final Tag<Item> tag;
        @Nullable
        private final CompoundTag nbt;
        
        public TagPredicate(final Tag<Item> aej, @Nullable final CompoundTag md) {
            this.tag = aej;
            this.nbt = md;
        }
        
        public boolean test(final ItemStack bly) {
            return this.tag.contains(bly.getItem()) && NbtUtils.compareNbt(this.nbt, bly.getTag(), true);
        }
    }
    
    public interface Result {
        Predicate<ItemStack> create(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }
}
