package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import java.util.stream.Stream;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.function.Function;
import net.minecraft.network.chat.ComponentUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.server.packs.repository.PackRepository;
import java.util.stream.Collectors;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.Lists;
import net.minecraft.server.packs.repository.Pack;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class DataPackCommand {
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_PACK;
    private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_ENABLED;
    private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_DISABLED;
    private static final SuggestionProvider<CommandSourceStack> SELECTED_PACKS;
    private static final SuggestionProvider<CommandSourceStack> UNSELECTED_PACKS;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("datapack").requires(db -> db.hasPermission(2))).then(Commands.literal("enable").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).suggests((SuggestionProvider)DataPackCommand.UNSELECTED_PACKS).executes(commandContext -> enablePack((CommandSourceStack)commandContext.getSource(), getPack((CommandContext<CommandSourceStack>)commandContext, "name", true), (list, abs) -> abs.getDefaultPosition().<Pack>insert(list, abs, (java.util.function.Function<Pack, Pack>)(abs -> abs), false)))).then(Commands.literal("after").then(Commands.argument("existing", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).suggests((SuggestionProvider)DataPackCommand.SELECTED_PACKS).executes(commandContext -> enablePack((CommandSourceStack)commandContext.getSource(), getPack((CommandContext<CommandSourceStack>)commandContext, "name", true), (list, abs) -> list.add(list.indexOf(getPack((CommandContext<CommandSourceStack>)commandContext, "existing", false)) + 1, abs)))))).then(Commands.literal("before").then(Commands.argument("existing", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).suggests((SuggestionProvider)DataPackCommand.SELECTED_PACKS).executes(commandContext -> enablePack((CommandSourceStack)commandContext.getSource(), getPack((CommandContext<CommandSourceStack>)commandContext, "name", true), (list, abs) -> list.add(list.indexOf(getPack((CommandContext<CommandSourceStack>)commandContext, "existing", false)), abs)))))).then(Commands.literal("last").executes(commandContext -> enablePack((CommandSourceStack)commandContext.getSource(), getPack((CommandContext<CommandSourceStack>)commandContext, "name", true), List::add)))).then(Commands.literal("first").executes(commandContext -> enablePack((CommandSourceStack)commandContext.getSource(), getPack((CommandContext<CommandSourceStack>)commandContext, "name", true), (list, abs) -> list.add(0, abs))))))).then(Commands.literal("disable").then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).suggests((SuggestionProvider)DataPackCommand.SELECTED_PACKS).executes(commandContext -> disablePack((CommandSourceStack)commandContext.getSource(), getPack((CommandContext<CommandSourceStack>)commandContext, "name", false)))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list").executes(commandContext -> listPacks((CommandSourceStack)commandContext.getSource()))).then(Commands.literal("available").executes(commandContext -> listAvailablePacks((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("enabled").executes(commandContext -> listEnabledPacks((CommandSourceStack)commandContext.getSource())))));
    }
    
    private static int enablePack(final CommandSourceStack db, final Pack abs, final Inserter a) throws CommandSyntaxException {
        final PackRepository abu4 = db.getServer().getPackRepository();
        final List<Pack> list5 = (List<Pack>)Lists.newArrayList((Iterable)abu4.getSelectedPacks());
        a.apply(list5, abs);
        db.sendSuccess(new TranslatableComponent("commands.datapack.modify.enable", new Object[] { abs.getChatLink(true) }), true);
        ReloadCommand.reloadPacks((Collection<String>)list5.stream().map(Pack::getId).collect(Collectors.toList()), db);
        return list5.size();
    }
    
    private static int disablePack(final CommandSourceStack db, final Pack abs) {
        final PackRepository abu3 = db.getServer().getPackRepository();
        final List<Pack> list4 = (List<Pack>)Lists.newArrayList((Iterable)abu3.getSelectedPacks());
        list4.remove(abs);
        db.sendSuccess(new TranslatableComponent("commands.datapack.modify.disable", new Object[] { abs.getChatLink(true) }), true);
        ReloadCommand.reloadPacks((Collection<String>)list4.stream().map(Pack::getId).collect(Collectors.toList()), db);
        return list4.size();
    }
    
    private static int listPacks(final CommandSourceStack db) {
        return listEnabledPacks(db) + listAvailablePacks(db);
    }
    
    private static int listAvailablePacks(final CommandSourceStack db) {
        final PackRepository abu2 = db.getServer().getPackRepository();
        abu2.reload();
        final Collection<? extends Pack> collection3 = abu2.getSelectedPacks();
        final Collection<? extends Pack> collection4 = abu2.getAvailablePacks();
        final List<Pack> list5 = (List<Pack>)collection4.stream().filter(abs -> !collection3.contains(abs)).collect(Collectors.toList());
        if (list5.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.datapack.list.available.none"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.datapack.list.available.success", new Object[] { list5.size(), ComponentUtils.formatList((java.util.Collection<Object>)list5, (java.util.function.Function<Object, Component>)(abs -> abs.getChatLink(false))) }), false);
        }
        return list5.size();
    }
    
    private static int listEnabledPacks(final CommandSourceStack db) {
        final PackRepository abu2 = db.getServer().getPackRepository();
        abu2.reload();
        final Collection<? extends Pack> collection3 = abu2.getSelectedPacks();
        if (collection3.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.datapack.list.enabled.none"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.datapack.list.enabled.success", new Object[] { collection3.size(), ComponentUtils.formatList(collection3, (java.util.function.Function<? extends Pack, Component>)(abs -> abs.getChatLink(true))) }), false);
        }
        return collection3.size();
    }
    
    private static Pack getPack(final CommandContext<CommandSourceStack> commandContext, final String string, final boolean boolean3) throws CommandSyntaxException {
        final String string2 = StringArgumentType.getString((CommandContext)commandContext, string);
        final PackRepository abu5 = ((CommandSourceStack)commandContext.getSource()).getServer().getPackRepository();
        final Pack abs6 = abu5.getPack(string2);
        if (abs6 == null) {
            throw DataPackCommand.ERROR_UNKNOWN_PACK.create(string2);
        }
        final boolean boolean4 = abu5.getSelectedPacks().contains(abs6);
        if (boolean3 && boolean4) {
            throw DataPackCommand.ERROR_PACK_ALREADY_ENABLED.create(string2);
        }
        if (!boolean3 && !boolean4) {
            throw DataPackCommand.ERROR_PACK_ALREADY_DISABLED.create(string2);
        }
        return abs6;
    }
    
    static {
        ERROR_UNKNOWN_PACK = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.datapack.unknown", new Object[] { object }));
        ERROR_PACK_ALREADY_ENABLED = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.datapack.enable.failed", new Object[] { object }));
        ERROR_PACK_ALREADY_DISABLED = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.datapack.disable.failed", new Object[] { object }));
        SELECTED_PACKS = ((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest((Stream<String>)((CommandSourceStack)commandContext.getSource()).getServer().getPackRepository().getSelectedIds().stream().map(StringArgumentType::escapeIfRequired), suggestionsBuilder));
        UNSELECTED_PACKS = ((commandContext, suggestionsBuilder) -> {
            final PackRepository abu3 = ((CommandSourceStack)commandContext.getSource()).getServer().getPackRepository();
            final Collection<String> collection4 = abu3.getSelectedIds();
            return SharedSuggestionProvider.suggest((Stream<String>)abu3.getAvailableIds().stream().filter(string -> !collection4.contains(string)).map(StringArgumentType::escapeIfRequired), suggestionsBuilder);
        });
    }
    
    interface Inserter {
        void apply(final List<Pack> list, final Pack abs) throws CommandSyntaxException;
    }
}
