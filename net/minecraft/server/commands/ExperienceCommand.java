package net.minecraft.server.commands;

import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import java.util.function.ToIntFunction;
import java.util.function.BiPredicate;
import java.util.function.BiConsumer;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ExperienceCommand {
    private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralCommandNode<CommandSourceStack> literalCommandNode2 = (LiteralCommandNode<CommandSourceStack>)commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("experience").requires(db -> db.hasPermission(2))).then(Commands.literal("add").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> addExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "amount"), Type.POINTS))).then(Commands.literal("points").executes(commandContext -> addExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "amount"), Type.POINTS)))).then(Commands.literal("levels").executes(commandContext -> addExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "amount"), Type.LEVELS))))))).then(Commands.literal("set").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "amount"), Type.POINTS))).then(Commands.literal("points").executes(commandContext -> setExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "amount"), Type.POINTS)))).then(Commands.literal("levels").executes(commandContext -> setExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "amount"), Type.LEVELS))))))).then(Commands.literal("query").then(((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.player()).then(Commands.literal("points").executes(commandContext -> queryExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayer((CommandContext<CommandSourceStack>)commandContext, "targets"), Type.POINTS)))).then(Commands.literal("levels").executes(commandContext -> queryExperience((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayer((CommandContext<CommandSourceStack>)commandContext, "targets"), Type.LEVELS))))));
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("xp").requires(db -> db.hasPermission(2))).redirect((CommandNode)literalCommandNode2));
    }
    
    private static int queryExperience(final CommandSourceStack db, final ServerPlayer aah, final Type a) {
        final int integer4 = a.query.applyAsInt(aah);
        db.sendSuccess(new TranslatableComponent("commands.experience.query." + a.name, new Object[] { aah.getDisplayName(), integer4 }), false);
        return integer4;
    }
    
    private static int addExperience(final CommandSourceStack db, final Collection<? extends ServerPlayer> collection, final int integer, final Type a) {
        for (final ServerPlayer aah6 : collection) {
            a.add.accept(aah6, integer);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.experience.add." + a.name + ".success.single", new Object[] { integer, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.experience.add." + a.name + ".success.multiple", new Object[] { integer, collection.size() }), true);
        }
        return collection.size();
    }
    
    private static int setExperience(final CommandSourceStack db, final Collection<? extends ServerPlayer> collection, final int integer, final Type a) throws CommandSyntaxException {
        int integer2 = 0;
        for (final ServerPlayer aah7 : collection) {
            if (a.set.test(aah7, integer)) {
                ++integer2;
            }
        }
        if (integer2 == 0) {
            throw ExperienceCommand.ERROR_SET_POINTS_INVALID.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.experience.set." + a.name + ".success.single", new Object[] { integer, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.experience.set." + a.name + ".success.multiple", new Object[] { integer, collection.size() }), true);
        }
        return collection.size();
    }
    
    static {
        ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.experience.set.points.invalid"));
    }
    
    enum Type {
        POINTS("points", (BiConsumer<ServerPlayer, Integer>)Player::giveExperiencePoints, (BiPredicate<ServerPlayer, Integer>)((aah, integer) -> {
            if (integer >= aah.getXpNeededForNextLevel()) {
                return false;
            }
            aah.setExperiencePoints(integer);
            return true;
        }), (ToIntFunction<ServerPlayer>)(aah -> Mth.floor(aah.experienceProgress * aah.getXpNeededForNextLevel()))), 
        LEVELS("levels", (BiConsumer<ServerPlayer, Integer>)ServerPlayer::giveExperienceLevels, (BiPredicate<ServerPlayer, Integer>)((aah, integer) -> {
            aah.setExperienceLevels(integer);
            return true;
        }), (ToIntFunction<ServerPlayer>)(aah -> aah.experienceLevel));
        
        public final BiConsumer<ServerPlayer, Integer> add;
        public final BiPredicate<ServerPlayer, Integer> set;
        public final String name;
        private final ToIntFunction<ServerPlayer> query;
        
        private Type(final String string3, final BiConsumer<ServerPlayer, Integer> biConsumer, final BiPredicate<ServerPlayer, Integer> biPredicate, final ToIntFunction<ServerPlayer> toIntFunction) {
            this.add = biConsumer;
            this.name = string3;
            this.set = biPredicate;
            this.query = toIntFunction;
        }
    }
}
