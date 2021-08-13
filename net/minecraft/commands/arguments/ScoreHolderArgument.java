package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import com.mojang.brigadier.StringReader;
import java.util.function.Supplier;
import java.util.Collections;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.arguments.ArgumentType;

public class ScoreHolderArgument implements ArgumentType<Result> {
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_SCORE_HOLDERS;
    private static final Collection<String> EXAMPLES;
    private static final SimpleCommandExceptionType ERROR_NO_RESULTS;
    private final boolean multiple;
    
    public ScoreHolderArgument(final boolean boolean1) {
        this.multiple = boolean1;
    }
    
    public static String getName(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return (String)getNames(commandContext, string).iterator().next();
    }
    
    public static Collection<String> getNames(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return getNames(commandContext, string, (Supplier<Collection<String>>)Collections::emptyList);
    }
    
    public static Collection<String> getNamesWithDefaultWildcard(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return getNames(commandContext, string, (Supplier<Collection<String>>)((CommandSourceStack)commandContext.getSource()).getServer().getScoreboard()::getTrackedPlayers);
    }
    
    public static Collection<String> getNames(final CommandContext<CommandSourceStack> commandContext, final String string, final Supplier<Collection<String>> supplier) throws CommandSyntaxException {
        final Collection<String> collection4 = ((Result)commandContext.getArgument(string, (Class)Result.class)).getNames((CommandSourceStack)commandContext.getSource(), supplier);
        if (collection4.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        }
        return collection4;
    }
    
    public static ScoreHolderArgument scoreHolder() {
        return new ScoreHolderArgument(false);
    }
    
    public static ScoreHolderArgument scoreHolders() {
        return new ScoreHolderArgument(true);
    }
    
    public Result parse(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '@') {
            final EntitySelectorParser fd3 = new EntitySelectorParser(stringReader);
            final EntitySelector fc4 = fd3.parse();
            if (!this.multiple && fc4.getMaxResults() > 1) {
                throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
            }
            return new SelectorResult(fc4);
        }
        else {
            final int integer3 = stringReader.getCursor();
            while (stringReader.canRead() && stringReader.peek() != ' ') {
                stringReader.skip();
            }
            final String string4 = stringReader.getString().substring(integer3, stringReader.getCursor());
            if (string4.equals("*")) {
                final Collection<String> collection3;
                return (db, supplier) -> {
                    collection3 = (Collection<String>)supplier.get();
                    if (collection3.isEmpty()) {
                        throw ScoreHolderArgument.ERROR_NO_RESULTS.create();
                    }
                    else {
                        return collection3;
                    }
                };
            }
            final Collection<String> collection4 = (Collection<String>)Collections.singleton(string4);
            return (db, supplier) -> collection4;
        }
    }
    
    public Collection<String> getExamples() {
        return ScoreHolderArgument.EXAMPLES;
    }
    
    static {
        SUGGEST_SCORE_HOLDERS = ((commandContext, suggestionsBuilder) -> {
            final StringReader stringReader3 = new StringReader(suggestionsBuilder.getInput());
            stringReader3.setCursor(suggestionsBuilder.getStart());
            final EntitySelectorParser fd4 = new EntitySelectorParser(stringReader3);
            try {
                fd4.parse();
            }
            catch (CommandSyntaxException ex) {}
            return fd4.fillSuggestions(suggestionsBuilder, (Consumer<SuggestionsBuilder>)(suggestionsBuilder -> SharedSuggestionProvider.suggest((Iterable<String>)((CommandSourceStack)commandContext.getSource()).getOnlinePlayerNames(), suggestionsBuilder)));
        });
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "Player", "0123", "*", "@e" });
        ERROR_NO_RESULTS = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.scoreHolder.empty"));
    }
    
    public static class SelectorResult implements Result {
        private final EntitySelector selector;
        
        public SelectorResult(final EntitySelector fc) {
            this.selector = fc;
        }
        
        public Collection<String> getNames(final CommandSourceStack db, final Supplier<Collection<String>> supplier) throws CommandSyntaxException {
            final List<? extends Entity> list4 = this.selector.findEntities(db);
            if (list4.isEmpty()) {
                throw EntityArgument.NO_ENTITIES_FOUND.create();
            }
            final List<String> list5 = (List<String>)Lists.newArrayList();
            for (final Entity apx7 : list4) {
                list5.add(apx7.getScoreboardName());
            }
            return (Collection<String>)list5;
        }
    }
    
    public static class Serializer implements ArgumentSerializer<ScoreHolderArgument> {
        public void serializeToNetwork(final ScoreHolderArgument dz, final FriendlyByteBuf nf) {
            byte byte4 = 0;
            if (dz.multiple) {
                byte4 |= 0x1;
            }
            nf.writeByte(byte4);
        }
        
        public ScoreHolderArgument deserializeFromNetwork(final FriendlyByteBuf nf) {
            final byte byte3 = nf.readByte();
            final boolean boolean4 = (byte3 & 0x1) != 0x0;
            return new ScoreHolderArgument(boolean4);
        }
        
        public void serializeToJson(final ScoreHolderArgument dz, final JsonObject jsonObject) {
            jsonObject.addProperty("amount", dz.multiple ? "multiple" : "single");
        }
    }
    
    @FunctionalInterface
    public interface Result {
        Collection<String> getNames(final CommandSourceStack db, final Supplier<Collection<String>> supplier) throws CommandSyntaxException;
    }
}
