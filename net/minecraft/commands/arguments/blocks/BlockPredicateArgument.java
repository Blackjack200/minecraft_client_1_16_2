package net.minecraft.commands.arguments.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Iterator;
import net.minecraft.nbt.NbtUtils;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.tags.TagContainer;
import net.minecraft.tags.BlockTags;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class BlockPredicateArgument implements ArgumentType<Result> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
    
    public static BlockPredicateArgument blockPredicate() {
        return new BlockPredicateArgument();
    }
    
    public Result parse(final StringReader stringReader) throws CommandSyntaxException {
        final BlockStateParser ei3 = new BlockStateParser(stringReader, true).parse(true);
        if (ei3.getState() != null) {
            final BlockPredicate a4 = new BlockPredicate(ei3.getState(), (Set<Property<?>>)ei3.getProperties().keySet(), ei3.getNbt());
            return ael -> a4;
        }
        final ResourceLocation vk4 = ei3.getTag();
        final ResourceLocation vk5;
        final Tag<Block> aej4;
        final BlockStateParser blockStateParser;
        return ael -> {
            aej4 = ael.getBlocks().getTag(vk5);
            if (aej4 == null) {
                throw BlockPredicateArgument.ERROR_UNKNOWN_TAG.create(vk5.toString());
            }
            else {
                return (Predicate<BlockInWorld>)new TagPredicate((Tag)aej4, (Map)blockStateParser.getVagueProperties(), blockStateParser.getNbt());
            }
        };
    }
    
    public static Predicate<BlockInWorld> getBlockPredicate(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Result)commandContext.getArgument(string, (Class)Result.class)).create(((CommandSourceStack)commandContext.getSource()).getServer().getTags());
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        final StringReader stringReader4 = new StringReader(suggestionsBuilder.getInput());
        stringReader4.setCursor(suggestionsBuilder.getStart());
        final BlockStateParser ei5 = new BlockStateParser(stringReader4, true);
        try {
            ei5.parse(true);
        }
        catch (CommandSyntaxException ex) {}
        return ei5.fillSuggestions(suggestionsBuilder, BlockTags.getAllTags());
    }
    
    public Collection<String> getExamples() {
        return BlockPredicateArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "stone", "minecraft:stone", "stone[foo=bar]", "#stone", "#stone[foo=bar]{baz=nbt}" });
        ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.block.tag.unknown", new Object[] { object }));
    }
    
    static class BlockPredicate implements Predicate<BlockInWorld> {
        private final BlockState state;
        private final Set<Property<?>> properties;
        @Nullable
        private final CompoundTag nbt;
        
        public BlockPredicate(final BlockState cee, final Set<Property<?>> set, @Nullable final CompoundTag md) {
            this.state = cee;
            this.properties = set;
            this.nbt = md;
        }
        
        public boolean test(final BlockInWorld cei) {
            final BlockState cee3 = cei.getState();
            if (!cee3.is(this.state.getBlock())) {
                return false;
            }
            for (final Property<?> cfg5 : this.properties) {
                if (cee3.getValue(cfg5) != this.state.getValue(cfg5)) {
                    return false;
                }
            }
            if (this.nbt != null) {
                final BlockEntity ccg4 = cei.getEntity();
                return ccg4 != null && NbtUtils.compareNbt(this.nbt, ccg4.save(new CompoundTag()), true);
            }
            return true;
        }
    }
    
    static class TagPredicate implements Predicate<BlockInWorld> {
        private final Tag<Block> tag;
        @Nullable
        private final CompoundTag nbt;
        private final Map<String, String> vagueProperties;
        
        private TagPredicate(final Tag<Block> aej, final Map<String, String> map, @Nullable final CompoundTag md) {
            this.tag = aej;
            this.vagueProperties = map;
            this.nbt = md;
        }
        
        public boolean test(final BlockInWorld cei) {
            final BlockState cee3 = cei.getState();
            if (!cee3.is(this.tag)) {
                return false;
            }
            for (final Map.Entry<String, String> entry5 : this.vagueProperties.entrySet()) {
                final Property<?> cfg6 = cee3.getBlock().getStateDefinition().getProperty((String)entry5.getKey());
                if (cfg6 == null) {
                    return false;
                }
                final Comparable<?> comparable7 = cfg6.getValue((String)entry5.getValue()).orElse(null);
                if (comparable7 == null) {
                    return false;
                }
                if (cee3.getValue(cfg6) != comparable7) {
                    return false;
                }
            }
            if (this.nbt != null) {
                final BlockEntity ccg4 = cei.getEntity();
                return ccg4 != null && NbtUtils.compareNbt(this.nbt, ccg4.save(new CompoundTag()), true);
            }
            return true;
        }
    }
    
    public interface Result {
        Predicate<BlockInWorld> create(final TagContainer ael) throws CommandSyntaxException;
    }
}
