package net.minecraft.commands.arguments;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import java.util.function.Consumer;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import java.util.Collections;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class GameProfileArgument implements ArgumentType<Result> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER;
    
    public static Collection<GameProfile> getGameProfiles(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Result)commandContext.getArgument(string, (Class)Result.class)).getNames((CommandSourceStack)commandContext.getSource());
    }
    
    public static GameProfileArgument gameProfile() {
        return new GameProfileArgument();
    }
    
    public Result parse(final StringReader stringReader) throws CommandSyntaxException {
        if (!stringReader.canRead() || stringReader.peek() != '@') {
            final int integer3 = stringReader.getCursor();
            while (stringReader.canRead() && stringReader.peek() != ' ') {
                stringReader.skip();
            }
            final String string4 = stringReader.getString().substring(integer3, stringReader.getCursor());
            final GameProfile gameProfile3;
            return db -> {
                gameProfile3 = db.getServer().getProfileCache().get(string4);
                if (gameProfile3 == null) {
                    throw GameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
                }
                else {
                    return (Collection<GameProfile>)Collections.singleton(gameProfile3);
                }
            };
        }
        final EntitySelectorParser fd3 = new EntitySelectorParser(stringReader);
        final EntitySelector fc4 = fd3.parse();
        if (fc4.includesEntities()) {
            throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.create();
        }
        return new SelectorResult(fc4);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            final StringReader stringReader4 = new StringReader(suggestionsBuilder.getInput());
            stringReader4.setCursor(suggestionsBuilder.getStart());
            final EntitySelectorParser fd5 = new EntitySelectorParser(stringReader4);
            try {
                fd5.parse();
            }
            catch (CommandSyntaxException ex) {}
            return fd5.fillSuggestions(suggestionsBuilder, (Consumer<SuggestionsBuilder>)(suggestionsBuilder -> SharedSuggestionProvider.suggest((Iterable<String>)((SharedSuggestionProvider)commandContext.getSource()).getOnlinePlayerNames(), suggestionsBuilder)));
        }
        return (CompletableFuture<Suggestions>)Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return GameProfileArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e" });
        ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.player.unknown"));
    }
    
    public static class SelectorResult implements Result {
        private final EntitySelector selector;
        
        public SelectorResult(final EntitySelector fc) {
            this.selector = fc;
        }
        
        public Collection<GameProfile> getNames(final CommandSourceStack db) throws CommandSyntaxException {
            final List<ServerPlayer> list3 = this.selector.findPlayers(db);
            if (list3.isEmpty()) {
                throw EntityArgument.NO_PLAYERS_FOUND.create();
            }
            final List<GameProfile> list4 = (List<GameProfile>)Lists.newArrayList();
            for (final ServerPlayer aah6 : list3) {
                list4.add(aah6.getGameProfile());
            }
            return (Collection<GameProfile>)list4;
        }
    }
    
    @FunctionalInterface
    public interface Result {
        Collection<GameProfile> getNames(final CommandSourceStack db) throws CommandSyntaxException;
    }
}
