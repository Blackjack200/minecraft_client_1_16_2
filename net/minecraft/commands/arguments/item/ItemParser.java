package net.minecraft.commands.arguments.item;

import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.ImmutableStringReader;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.nbt.TagParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Registry;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagCollection;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.function.BiFunction;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ItemParser {
    public static final SimpleCommandExceptionType ERROR_NO_TAGS_ALLOWED;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM;
    private static final BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>> SUGGEST_NOTHING;
    private final StringReader reader;
    private final boolean forTesting;
    private final Map<Property<?>, Comparable<?>> properties;
    private Item item;
    @Nullable
    private CompoundTag nbt;
    private ResourceLocation tag;
    private int tagCursor;
    private BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>> suggestions;
    
    public ItemParser(final StringReader stringReader, final boolean boolean2) {
        this.properties = (Map<Property<?>, Comparable<?>>)Maps.newHashMap();
        this.tag = new ResourceLocation("");
        this.suggestions = ItemParser.SUGGEST_NOTHING;
        this.reader = stringReader;
        this.forTesting = boolean2;
    }
    
    public Item getItem() {
        return this.item;
    }
    
    @Nullable
    public CompoundTag getNbt() {
        return this.nbt;
    }
    
    public ResourceLocation getTag() {
        return this.tag;
    }
    
    public void readItem() throws CommandSyntaxException {
        final int integer2 = this.reader.getCursor();
        final ResourceLocation vk3 = ResourceLocation.read(this.reader);
        this.item = (Item)Registry.ITEM.getOptional(vk3).orElseThrow(() -> {
            this.reader.setCursor(integer2);
            return ItemParser.ERROR_UNKNOWN_ITEM.createWithContext((ImmutableStringReader)this.reader, vk3.toString());
        });
    }
    
    public void readTag() throws CommandSyntaxException {
        if (!this.forTesting) {
            throw ItemParser.ERROR_NO_TAGS_ALLOWED.create();
        }
        this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>>)this::suggestTag;
        this.reader.expect('#');
        this.tagCursor = this.reader.getCursor();
        this.tag = ResourceLocation.read(this.reader);
    }
    
    public void readNbt() throws CommandSyntaxException {
        this.nbt = new TagParser(this.reader).readStruct();
    }
    
    public ItemParser parse() throws CommandSyntaxException {
        this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>>)this::suggestItemIdOrTag;
        if (this.reader.canRead() && this.reader.peek() == '#') {
            this.readTag();
        }
        else {
            this.readItem();
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>>)this::suggestOpenNbt;
        }
        if (this.reader.canRead() && this.reader.peek() == '{') {
            this.suggestions = ItemParser.SUGGEST_NOTHING;
            this.readNbt();
        }
        return this;
    }
    
    private CompletableFuture<Suggestions> suggestOpenNbt(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Item> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            suggestionsBuilder.suggest(String.valueOf('{'));
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestTag(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Item> aek) {
        return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)aek.getAvailableTags(), suggestionsBuilder.createOffset(this.tagCursor));
    }
    
    private CompletableFuture<Suggestions> suggestItemIdOrTag(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Item> aek) {
        if (this.forTesting) {
            SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)aek.getAvailableTags(), suggestionsBuilder, String.valueOf('#'));
        }
        return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)Registry.ITEM.keySet(), suggestionsBuilder);
    }
    
    public CompletableFuture<Suggestions> fillSuggestions(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Item> aek) {
        return (CompletableFuture<Suggestions>)this.suggestions.apply(suggestionsBuilder.createOffset(this.reader.getCursor()), aek);
    }
    
    static {
        ERROR_NO_TAGS_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.item.tag.disallowed"));
        ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.item.id.invalid", new Object[] { object }));
        SUGGEST_NOTHING = ((suggestionsBuilder, aek) -> suggestionsBuilder.buildFuture());
    }
}
