package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import java.util.Collections;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class RecipeCommand {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED;
    private static final SimpleCommandExceptionType ERROR_TAKE_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("recipe").requires(db -> db.hasPermission(2))).then(Commands.literal("give").then(((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("recipe", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)SuggestionProviders.ALL_RECIPES).executes(commandContext -> giveRecipes((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), (Collection<Recipe<?>>)Collections.singleton(ResourceLocationArgument.getRecipe((CommandContext<CommandSourceStack>)commandContext, "recipe")))))).then(Commands.literal("*").executes(commandContext -> giveRecipes((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getServer().getRecipeManager().getRecipes())))))).then(Commands.literal("take").then(((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("recipe", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)SuggestionProviders.ALL_RECIPES).executes(commandContext -> takeRecipes((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), (Collection<Recipe<?>>)Collections.singleton(ResourceLocationArgument.getRecipe((CommandContext<CommandSourceStack>)commandContext, "recipe")))))).then(Commands.literal("*").executes(commandContext -> takeRecipes((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getServer().getRecipeManager().getRecipes()))))));
    }
    
    private static int giveRecipes(final CommandSourceStack db, final Collection<ServerPlayer> collection2, final Collection<Recipe<?>> collection3) throws CommandSyntaxException {
        int integer4 = 0;
        for (final ServerPlayer aah6 : collection2) {
            integer4 += aah6.awardRecipes(collection3);
        }
        if (integer4 == 0) {
            throw RecipeCommand.ERROR_GIVE_FAILED.create();
        }
        if (collection2.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.recipe.give.success.single", new Object[] { collection3.size(), ((ServerPlayer)collection2.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.recipe.give.success.multiple", new Object[] { collection3.size(), collection2.size() }), true);
        }
        return integer4;
    }
    
    private static int takeRecipes(final CommandSourceStack db, final Collection<ServerPlayer> collection2, final Collection<Recipe<?>> collection3) throws CommandSyntaxException {
        int integer4 = 0;
        for (final ServerPlayer aah6 : collection2) {
            integer4 += aah6.resetRecipes(collection3);
        }
        if (integer4 == 0) {
            throw RecipeCommand.ERROR_TAKE_FAILED.create();
        }
        if (collection2.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.recipe.take.success.single", new Object[] { collection3.size(), ((ServerPlayer)collection2.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.recipe.take.success.multiple", new Object[] { collection3.size(), collection2.size() }), true);
        }
        return integer4;
    }
    
    static {
        ERROR_GIVE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.recipe.give.failed"));
        ERROR_TAKE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.recipe.take.failed"));
    }
}
