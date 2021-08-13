package net.minecraft.commands.arguments.blocks;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Optional;
import net.minecraft.nbt.TagParser;
import com.mojang.brigadier.ImmutableStringReader;
import net.minecraft.core.Registry;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.tags.Tag;
import java.util.Iterator;
import java.util.Locale;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagCollection;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.function.BiFunction;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class BlockStateParser {
    public static final SimpleCommandExceptionType ERROR_NO_TAGS_ALLOWED;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_BLOCK;
    public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_PROPERTY;
    public static final Dynamic2CommandExceptionType ERROR_DUPLICATE_PROPERTY;
    public static final Dynamic3CommandExceptionType ERROR_INVALID_VALUE;
    public static final Dynamic2CommandExceptionType ERROR_EXPECTED_VALUE;
    public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_PROPERTIES;
    private static final BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>> SUGGEST_NOTHING;
    private final StringReader reader;
    private final boolean forTesting;
    private final Map<Property<?>, Comparable<?>> properties;
    private final Map<String, String> vagueProperties;
    private ResourceLocation id;
    private StateDefinition<Block, BlockState> definition;
    private BlockState state;
    @Nullable
    private CompoundTag nbt;
    private ResourceLocation tag;
    private int tagCursor;
    private BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>> suggestions;
    
    public BlockStateParser(final StringReader stringReader, final boolean boolean2) {
        this.properties = (Map<Property<?>, Comparable<?>>)Maps.newHashMap();
        this.vagueProperties = (Map<String, String>)Maps.newHashMap();
        this.id = new ResourceLocation("");
        this.tag = new ResourceLocation("");
        this.suggestions = BlockStateParser.SUGGEST_NOTHING;
        this.reader = stringReader;
        this.forTesting = boolean2;
    }
    
    public Map<Property<?>, Comparable<?>> getProperties() {
        return this.properties;
    }
    
    @Nullable
    public BlockState getState() {
        return this.state;
    }
    
    @Nullable
    public CompoundTag getNbt() {
        return this.nbt;
    }
    
    @Nullable
    public ResourceLocation getTag() {
        return this.tag;
    }
    
    public BlockStateParser parse(final boolean boolean1) throws CommandSyntaxException {
        this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestBlockIdOrTag;
        if (this.reader.canRead() && this.reader.peek() == '#') {
            this.readTag();
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestOpenVaguePropertiesOrNbt;
            if (this.reader.canRead() && this.reader.peek() == '[') {
                this.readVagueProperties();
                this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestOpenNbt;
            }
        }
        else {
            this.readBlock();
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestOpenPropertiesOrNbt;
            if (this.reader.canRead() && this.reader.peek() == '[') {
                this.readProperties();
                this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestOpenNbt;
            }
        }
        if (boolean1 && this.reader.canRead() && this.reader.peek() == '{') {
            this.suggestions = BlockStateParser.SUGGEST_NOTHING;
            this.readNbt();
        }
        return this;
    }
    
    private CompletableFuture<Suggestions> suggestPropertyNameOrEnd(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            suggestionsBuilder.suggest(String.valueOf(']'));
        }
        return this.suggestPropertyName(suggestionsBuilder, aek);
    }
    
    private CompletableFuture<Suggestions> suggestVaguePropertyNameOrEnd(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            suggestionsBuilder.suggest(String.valueOf(']'));
        }
        return this.suggestVaguePropertyName(suggestionsBuilder, aek);
    }
    
    private CompletableFuture<Suggestions> suggestPropertyName(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        final String string4 = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
        for (final Property<?> cfg6 : this.state.getProperties()) {
            if (!this.properties.containsKey(cfg6) && cfg6.getName().startsWith(string4)) {
                suggestionsBuilder.suggest(cfg6.getName() + '=');
            }
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestVaguePropertyName(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        final String string4 = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
        if (this.tag != null && !this.tag.getPath().isEmpty()) {
            final Tag<Block> aej5 = aek.getTag(this.tag);
            if (aej5 != null) {
                for (final Block bul7 : aej5.getValues()) {
                    for (final Property<?> cfg9 : bul7.getStateDefinition().getProperties()) {
                        if (!this.vagueProperties.containsKey(cfg9.getName()) && cfg9.getName().startsWith(string4)) {
                            suggestionsBuilder.suggest(cfg9.getName() + '=');
                        }
                    }
                }
            }
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestOpenNbt(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty() && this.hasBlockEntity(aek)) {
            suggestionsBuilder.suggest(String.valueOf('{'));
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private boolean hasBlockEntity(final TagCollection<Block> aek) {
        if (this.state != null) {
            return this.state.getBlock().isEntityBlock();
        }
        if (this.tag != null) {
            final Tag<Block> aej3 = aek.getTag(this.tag);
            if (aej3 != null) {
                for (final Block bul5 : aej3.getValues()) {
                    if (bul5.isEntityBlock()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private CompletableFuture<Suggestions> suggestEquals(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            suggestionsBuilder.suggest(String.valueOf('='));
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestNextPropertyOrEnd(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            suggestionsBuilder.suggest(String.valueOf(']'));
        }
        if (suggestionsBuilder.getRemaining().isEmpty() && this.properties.size() < this.state.getProperties().size()) {
            suggestionsBuilder.suggest(String.valueOf(','));
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private static <T extends Comparable<T>> SuggestionsBuilder addSuggestions(final SuggestionsBuilder suggestionsBuilder, final Property<T> cfg) {
        for (final T comparable4 : cfg.getPossibleValues()) {
            if (comparable4 instanceof Integer) {
                suggestionsBuilder.suggest((int)comparable4);
            }
            else {
                suggestionsBuilder.suggest(cfg.getName(comparable4));
            }
        }
        return suggestionsBuilder;
    }
    
    private CompletableFuture<Suggestions> suggestVaguePropertyValue(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek, final String string) {
        boolean boolean5 = false;
        if (this.tag != null && !this.tag.getPath().isEmpty()) {
            final Tag<Block> aej6 = aek.getTag(this.tag);
            if (aej6 != null) {
                for (final Block bul8 : aej6.getValues()) {
                    final Property<?> cfg9 = bul8.getStateDefinition().getProperty(string);
                    if (cfg9 != null) {
                        BlockStateParser.addSuggestions(suggestionsBuilder, cfg9);
                    }
                    if (!boolean5) {
                        for (final Property<?> cfg10 : bul8.getStateDefinition().getProperties()) {
                            if (!this.vagueProperties.containsKey(cfg10.getName())) {
                                boolean5 = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (boolean5) {
            suggestionsBuilder.suggest(String.valueOf(','));
        }
        suggestionsBuilder.suggest(String.valueOf(']'));
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestOpenVaguePropertiesOrNbt(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            final Tag<Block> aej4 = aek.getTag(this.tag);
            if (aej4 != null) {
                boolean boolean5 = false;
                boolean boolean6 = false;
                for (final Block bul8 : aej4.getValues()) {
                    boolean5 |= !bul8.getStateDefinition().getProperties().isEmpty();
                    boolean6 |= bul8.isEntityBlock();
                    if (boolean5 && boolean6) {
                        break;
                    }
                }
                if (boolean5) {
                    suggestionsBuilder.suggest(String.valueOf('['));
                }
                if (boolean6) {
                    suggestionsBuilder.suggest(String.valueOf('{'));
                }
            }
        }
        return this.suggestTag(suggestionsBuilder, aek);
    }
    
    private CompletableFuture<Suggestions> suggestOpenPropertiesOrNbt(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (suggestionsBuilder.getRemaining().isEmpty()) {
            if (!this.state.getBlock().getStateDefinition().getProperties().isEmpty()) {
                suggestionsBuilder.suggest(String.valueOf('['));
            }
            if (this.state.getBlock().isEntityBlock()) {
                suggestionsBuilder.suggest(String.valueOf('{'));
            }
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestTag(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)aek.getAvailableTags(), suggestionsBuilder.createOffset(this.tagCursor).add(suggestionsBuilder));
    }
    
    private CompletableFuture<Suggestions> suggestBlockIdOrTag(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        if (this.forTesting) {
            SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)aek.getAvailableTags(), suggestionsBuilder, String.valueOf('#'));
        }
        SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)Registry.BLOCK.keySet(), suggestionsBuilder);
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    public void readBlock() throws CommandSyntaxException {
        final int integer2 = this.reader.getCursor();
        this.id = ResourceLocation.read(this.reader);
        final Block bul3 = (Block)Registry.BLOCK.getOptional(this.id).orElseThrow(() -> {
            this.reader.setCursor(integer2);
            return BlockStateParser.ERROR_UNKNOWN_BLOCK.createWithContext((ImmutableStringReader)this.reader, this.id.toString());
        });
        this.definition = bul3.getStateDefinition();
        this.state = bul3.defaultBlockState();
    }
    
    public void readTag() throws CommandSyntaxException {
        if (!this.forTesting) {
            throw BlockStateParser.ERROR_NO_TAGS_ALLOWED.create();
        }
        this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestTag;
        this.reader.expect('#');
        this.tagCursor = this.reader.getCursor();
        this.tag = ResourceLocation.read(this.reader);
    }
    
    public void readProperties() throws CommandSyntaxException {
        this.reader.skip();
        this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestPropertyNameOrEnd;
        this.reader.skipWhitespace();
        while (this.reader.canRead() && this.reader.peek() != ']') {
            this.reader.skipWhitespace();
            final int integer2 = this.reader.getCursor();
            final String string3 = this.reader.readString();
            final Property<?> cfg4 = this.definition.getProperty(string3);
            if (cfg4 == null) {
                this.reader.setCursor(integer2);
                throw BlockStateParser.ERROR_UNKNOWN_PROPERTY.createWithContext((ImmutableStringReader)this.reader, this.id.toString(), string3);
            }
            if (this.properties.containsKey(cfg4)) {
                this.reader.setCursor(integer2);
                throw BlockStateParser.ERROR_DUPLICATE_PROPERTY.createWithContext((ImmutableStringReader)this.reader, this.id.toString(), string3);
            }
            this.reader.skipWhitespace();
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestEquals;
            if (!this.reader.canRead() || this.reader.peek() != '=') {
                throw BlockStateParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader, this.id.toString(), string3);
            }
            this.reader.skip();
            this.reader.skipWhitespace();
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)((suggestionsBuilder, aek) -> BlockStateParser.<Comparable>addSuggestions(suggestionsBuilder, cfg4).buildFuture());
            final int integer3 = this.reader.getCursor();
            this.setValue(cfg4, this.reader.readString(), integer3);
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestNextPropertyOrEnd;
            this.reader.skipWhitespace();
            if (!this.reader.canRead()) {
                continue;
            }
            if (this.reader.peek() == ',') {
                this.reader.skip();
                this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestPropertyName;
            }
            else {
                if (this.reader.peek() == ']') {
                    break;
                }
                throw BlockStateParser.ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext((ImmutableStringReader)this.reader);
            }
        }
        if (this.reader.canRead()) {
            this.reader.skip();
            return;
        }
        throw BlockStateParser.ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext((ImmutableStringReader)this.reader);
    }
    
    public void readVagueProperties() throws CommandSyntaxException {
        this.reader.skip();
        this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestVaguePropertyNameOrEnd;
        int integer2 = -1;
        this.reader.skipWhitespace();
        while (this.reader.canRead() && this.reader.peek() != ']') {
            this.reader.skipWhitespace();
            final int integer3 = this.reader.getCursor();
            final String string4 = this.reader.readString();
            if (this.vagueProperties.containsKey(string4)) {
                this.reader.setCursor(integer3);
                throw BlockStateParser.ERROR_DUPLICATE_PROPERTY.createWithContext((ImmutableStringReader)this.reader, this.id.toString(), string4);
            }
            this.reader.skipWhitespace();
            if (!this.reader.canRead() || this.reader.peek() != '=') {
                this.reader.setCursor(integer3);
                throw BlockStateParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader, this.id.toString(), string4);
            }
            this.reader.skip();
            this.reader.skipWhitespace();
            this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)((suggestionsBuilder, aek) -> this.suggestVaguePropertyValue(suggestionsBuilder, aek, string4));
            integer2 = this.reader.getCursor();
            final String string5 = this.reader.readString();
            this.vagueProperties.put(string4, string5);
            this.reader.skipWhitespace();
            if (!this.reader.canRead()) {
                continue;
            }
            integer2 = -1;
            if (this.reader.peek() == ',') {
                this.reader.skip();
                this.suggestions = (BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>>)this::suggestVaguePropertyName;
            }
            else {
                if (this.reader.peek() == ']') {
                    break;
                }
                throw BlockStateParser.ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext((ImmutableStringReader)this.reader);
            }
        }
        if (this.reader.canRead()) {
            this.reader.skip();
            return;
        }
        if (integer2 >= 0) {
            this.reader.setCursor(integer2);
        }
        throw BlockStateParser.ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext((ImmutableStringReader)this.reader);
    }
    
    public void readNbt() throws CommandSyntaxException {
        this.nbt = new TagParser(this.reader).readStruct();
    }
    
    private <T extends Comparable<T>> void setValue(final Property<T> cfg, final String string, final int integer) throws CommandSyntaxException {
        final Optional<T> optional5 = cfg.getValue(string);
        if (optional5.isPresent()) {
            this.state = ((StateHolder<O, BlockState>)this.state).<T, Comparable>setValue(cfg, optional5.get());
            this.properties.put(cfg, optional5.get());
            return;
        }
        this.reader.setCursor(integer);
        throw BlockStateParser.ERROR_INVALID_VALUE.createWithContext((ImmutableStringReader)this.reader, this.id.toString(), cfg.getName(), string);
    }
    
    public static String serialize(final BlockState cee) {
        final StringBuilder stringBuilder2 = new StringBuilder(Registry.BLOCK.getKey(cee.getBlock()).toString());
        if (!cee.getProperties().isEmpty()) {
            stringBuilder2.append('[');
            boolean boolean3 = false;
            for (final Map.Entry<Property<?>, Comparable<?>> entry5 : cee.getValues().entrySet()) {
                if (boolean3) {
                    stringBuilder2.append(',');
                }
                BlockStateParser.<Comparable>appendProperty(stringBuilder2, (Property<Comparable>)entry5.getKey(), entry5.getValue());
                boolean3 = true;
            }
            stringBuilder2.append(']');
        }
        return stringBuilder2.toString();
    }
    
    private static <T extends Comparable<T>> void appendProperty(final StringBuilder stringBuilder, final Property<T> cfg, final Comparable<?> comparable) {
        stringBuilder.append(cfg.getName());
        stringBuilder.append('=');
        stringBuilder.append(cfg.getName((T)comparable));
    }
    
    public CompletableFuture<Suggestions> fillSuggestions(final SuggestionsBuilder suggestionsBuilder, final TagCollection<Block> aek) {
        return (CompletableFuture<Suggestions>)this.suggestions.apply(suggestionsBuilder.createOffset(this.reader.getCursor()), aek);
    }
    
    public Map<String, String> getVagueProperties() {
        return this.vagueProperties;
    }
    
    static {
        ERROR_NO_TAGS_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.block.tag.disallowed"));
        ERROR_UNKNOWN_BLOCK = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.block.id.invalid", new Object[] { object }));
        ERROR_UNKNOWN_PROPERTY = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("argument.block.property.unknown", new Object[] { object1, object2 }));
        ERROR_DUPLICATE_PROPERTY = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("argument.block.property.duplicate", new Object[] { object2, object1 }));
        ERROR_INVALID_VALUE = new Dynamic3CommandExceptionType((object1, object2, object3) -> new TranslatableComponent("argument.block.property.invalid", new Object[] { object1, object3, object2 }));
        ERROR_EXPECTED_VALUE = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("argument.block.property.novalue", new Object[] { object1, object2 }));
        ERROR_EXPECTED_END_OF_PROPERTIES = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.block.property.unclosed"));
        SUGGEST_NOTHING = ((suggestionsBuilder, aek) -> suggestionsBuilder.buildFuture());
    }
}
