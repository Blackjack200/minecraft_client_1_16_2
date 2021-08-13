package net.minecraft.commands.arguments;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiFunction;
import java.util.Map;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class EntityAnchorArgument implements ArgumentType<Anchor> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_INVALID;
    
    public static Anchor getAnchor(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (Anchor)commandContext.getArgument(string, (Class)Anchor.class);
    }
    
    public static EntityAnchorArgument anchor() {
        return new EntityAnchorArgument();
    }
    
    public Anchor parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        final String string4 = stringReader.readUnquotedString();
        final Anchor a5 = Anchor.getByName(string4);
        if (a5 == null) {
            stringReader.setCursor(integer3);
            throw EntityAnchorArgument.ERROR_INVALID.createWithContext((ImmutableStringReader)stringReader, string4);
        }
        return a5;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest((Iterable<String>)Anchor.BY_NAME.keySet(), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return EntityAnchorArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "eyes", "feet" });
        ERROR_INVALID = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.anchor.invalid", new Object[] { object }));
    }
    
    public enum Anchor {
        FEET("feet", (BiFunction<Vec3, Entity, Vec3>)((dck, apx) -> dck)), 
        EYES("eyes", (BiFunction<Vec3, Entity, Vec3>)((dck, apx) -> new Vec3(dck.x, dck.y + apx.getEyeHeight(), dck.z)));
        
        private static final Map<String, Anchor> BY_NAME;
        private final String name;
        private final BiFunction<Vec3, Entity, Vec3> transform;
        
        private Anchor(final String string3, final BiFunction<Vec3, Entity, Vec3> biFunction) {
            this.name = string3;
            this.transform = biFunction;
        }
        
        @Nullable
        public static Anchor getByName(final String string) {
            return (Anchor)Anchor.BY_NAME.get(string);
        }
        
        public Vec3 apply(final Entity apx) {
            return (Vec3)this.transform.apply(apx.position(), apx);
        }
        
        public Vec3 apply(final CommandSourceStack db) {
            final Entity apx3 = db.getEntity();
            if (apx3 == null) {
                return db.getPosition();
            }
            return (Vec3)this.transform.apply(db.getPosition(), apx3);
        }
        
        static {
            BY_NAME = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
                for (final Anchor a5 : values()) {
                    hashMap.put(a5.name, a5);
                }
            }));
        }
    }
}
