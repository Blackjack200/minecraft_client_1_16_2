package net.minecraft.commands.arguments;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.HashMap;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import java.util.Map;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class SlotArgument implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT;
    private static final Map<String, Integer> SLOTS;
    
    public static SlotArgument slot() {
        return new SlotArgument();
    }
    
    public static int getSlot(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (int)commandContext.getArgument(string, (Class)Integer.class);
    }
    
    public Integer parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.readUnquotedString();
        if (!SlotArgument.SLOTS.containsKey(string3)) {
            throw SlotArgument.ERROR_UNKNOWN_SLOT.create(string3);
        }
        return (Integer)SlotArgument.SLOTS.get(string3);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest((Iterable<String>)SlotArgument.SLOTS.keySet(), suggestionsBuilder);
    }
    
    public Collection<String> getExamples() {
        return SlotArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "container.5", "12", "weapon" });
        ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(object -> new TranslatableComponent("slot.unknown", new Object[] { object }));
        SLOTS = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            for (int integer2 = 0; integer2 < 54; ++integer2) {
                hashMap.put(new StringBuilder().append("container.").append(integer2).toString(), integer2);
            }
            for (int integer2 = 0; integer2 < 9; ++integer2) {
                hashMap.put(new StringBuilder().append("hotbar.").append(integer2).toString(), integer2);
            }
            for (int integer2 = 0; integer2 < 27; ++integer2) {
                hashMap.put(new StringBuilder().append("inventory.").append(integer2).toString(), (9 + integer2));
            }
            for (int integer2 = 0; integer2 < 27; ++integer2) {
                hashMap.put(new StringBuilder().append("enderchest.").append(integer2).toString(), (200 + integer2));
            }
            for (int integer2 = 0; integer2 < 8; ++integer2) {
                hashMap.put(new StringBuilder().append("villager.").append(integer2).toString(), (300 + integer2));
            }
            for (int integer2 = 0; integer2 < 15; ++integer2) {
                hashMap.put(new StringBuilder().append("horse.").append(integer2).toString(), (500 + integer2));
            }
            hashMap.put("weapon", 98);
            hashMap.put("weapon.mainhand", 98);
            hashMap.put("weapon.offhand", 99);
            hashMap.put("armor.head", (100 + EquipmentSlot.HEAD.getIndex()));
            hashMap.put("armor.chest", (100 + EquipmentSlot.CHEST.getIndex()));
            hashMap.put("armor.legs", (100 + EquipmentSlot.LEGS.getIndex()));
            hashMap.put("armor.feet", (100 + EquipmentSlot.FEET.getIndex()));
            hashMap.put("horse.saddle", 400);
            hashMap.put("horse.armor", 401);
            hashMap.put("horse.chest", 499);
        }));
    }
}
