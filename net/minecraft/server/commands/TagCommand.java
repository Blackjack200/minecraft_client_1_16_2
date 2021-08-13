package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.ComponentUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class TagCommand {
    private static final SimpleCommandExceptionType ERROR_ADD_FAILED;
    private static final SimpleCommandExceptionType ERROR_REMOVE_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tag").requires(db -> db.hasPermission(2))).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).then(Commands.literal("add").then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).executes(commandContext -> addTag((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), StringArgumentType.getString(commandContext, "name")))))).then(Commands.literal("remove").then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest((Iterable<String>)getTags(EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets")), suggestionsBuilder)).executes(commandContext -> removeTag((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), StringArgumentType.getString(commandContext, "name")))))).then(Commands.literal("list").executes(commandContext -> listTags((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"))))));
    }
    
    private static Collection<String> getTags(final Collection<? extends Entity> collection) {
        final Set<String> set2 = (Set<String>)Sets.newHashSet();
        for (final Entity apx4 : collection) {
            set2.addAll((Collection)apx4.getTags());
        }
        return (Collection<String>)set2;
    }
    
    private static int addTag(final CommandSourceStack db, final Collection<? extends Entity> collection, final String string) throws CommandSyntaxException {
        int integer4 = 0;
        for (final Entity apx6 : collection) {
            if (apx6.addTag(string)) {
                ++integer4;
            }
        }
        if (integer4 == 0) {
            throw TagCommand.ERROR_ADD_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.tag.add.success.single", new Object[] { string, ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.tag.add.success.multiple", new Object[] { string, collection.size() }), true);
        }
        return integer4;
    }
    
    private static int removeTag(final CommandSourceStack db, final Collection<? extends Entity> collection, final String string) throws CommandSyntaxException {
        int integer4 = 0;
        for (final Entity apx6 : collection) {
            if (apx6.removeTag(string)) {
                ++integer4;
            }
        }
        if (integer4 == 0) {
            throw TagCommand.ERROR_REMOVE_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.tag.remove.success.single", new Object[] { string, ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.tag.remove.success.multiple", new Object[] { string, collection.size() }), true);
        }
        return integer4;
    }
    
    private static int listTags(final CommandSourceStack db, final Collection<? extends Entity> collection) {
        final Set<String> set3 = (Set<String>)Sets.newHashSet();
        for (final Entity apx5 : collection) {
            set3.addAll((Collection)apx5.getTags());
        }
        if (collection.size() == 1) {
            final Entity apx6 = (Entity)collection.iterator().next();
            if (set3.isEmpty()) {
                db.sendSuccess(new TranslatableComponent("commands.tag.list.single.empty", new Object[] { apx6.getDisplayName() }), false);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.tag.list.single.success", new Object[] { apx6.getDisplayName(), set3.size(), ComponentUtils.formatList((Collection<String>)set3) }), false);
            }
        }
        else if (set3.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.tag.list.multiple.empty", new Object[] { collection.size() }), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.tag.list.multiple.success", new Object[] { collection.size(), set3.size(), ComponentUtils.formatList((Collection<String>)set3) }), false);
        }
        return set3.size();
    }
    
    static {
        ERROR_ADD_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.tag.add.failed"));
        ERROR_REMOVE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.tag.remove.failed"));
    }
}
