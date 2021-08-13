package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.MobEffectArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class EffectCommands {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED;
    private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED;
    private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("effect").requires(db -> db.hasPermission(2))).then(((LiteralArgumentBuilder)Commands.literal("clear").executes(commandContext -> clearEffects((CommandSourceStack)commandContext.getSource(), ImmutableList.of(((CommandSourceStack)commandContext.getSource()).getEntityOrException())))).then(((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).executes(commandContext -> clearEffects((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets")))).then(Commands.argument("effect", (com.mojang.brigadier.arguments.ArgumentType<Object>)MobEffectArgument.effect()).executes(commandContext -> clearEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), MobEffectArgument.getEffect((CommandContext<CommandSourceStack>)commandContext, "effect"))))))).then(Commands.literal("give").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).then(((RequiredArgumentBuilder)Commands.argument("effect", (com.mojang.brigadier.arguments.ArgumentType<Object>)MobEffectArgument.effect()).executes(commandContext -> giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), MobEffectArgument.getEffect((CommandContext<CommandSourceStack>)commandContext, "effect"), null, 0, true))).then(((RequiredArgumentBuilder)Commands.argument("seconds", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(1, 1000000)).executes(commandContext -> giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), MobEffectArgument.getEffect((CommandContext<CommandSourceStack>)commandContext, "effect"), IntegerArgumentType.getInteger(commandContext, "seconds"), 0, true))).then(((RequiredArgumentBuilder)Commands.argument("amplifier", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0, 255)).executes(commandContext -> giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), MobEffectArgument.getEffect((CommandContext<CommandSourceStack>)commandContext, "effect"), IntegerArgumentType.getInteger(commandContext, "seconds"), IntegerArgumentType.getInteger(commandContext, "amplifier"), true))).then(Commands.argument("hideParticles", (com.mojang.brigadier.arguments.ArgumentType<Object>)BoolArgumentType.bool()).executes(commandContext -> giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), MobEffectArgument.getEffect((CommandContext<CommandSourceStack>)commandContext, "effect"), IntegerArgumentType.getInteger(commandContext, "seconds"), IntegerArgumentType.getInteger(commandContext, "amplifier"), !BoolArgumentType.getBool(commandContext, "hideParticles"))))))))));
    }
    
    private static int giveEffect(final CommandSourceStack db, final Collection<? extends Entity> collection, final MobEffect app, @Nullable final Integer integer, final int integer, final boolean boolean6) throws CommandSyntaxException {
        int integer2 = 0;
        int integer3;
        if (integer != null) {
            if (app.isInstantenous()) {
                integer3 = integer;
            }
            else {
                integer3 = integer * 20;
            }
        }
        else if (app.isInstantenous()) {
            integer3 = 1;
        }
        else {
            integer3 = 600;
        }
        for (final Entity apx10 : collection) {
            if (apx10 instanceof LivingEntity) {
                final MobEffectInstance apr11 = new MobEffectInstance(app, integer3, integer, false, boolean6);
                if (!((LivingEntity)apx10).addEffect(apr11)) {
                    continue;
                }
                ++integer2;
            }
        }
        if (integer2 == 0) {
            throw EffectCommands.ERROR_GIVE_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.effect.give.success.single", new Object[] { app.getDisplayName(), ((Entity)collection.iterator().next()).getDisplayName(), integer3 / 20 }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.effect.give.success.multiple", new Object[] { app.getDisplayName(), collection.size(), integer3 / 20 }), true);
        }
        return integer2;
    }
    
    private static int clearEffects(final CommandSourceStack db, final Collection<? extends Entity> collection) throws CommandSyntaxException {
        int integer3 = 0;
        for (final Entity apx5 : collection) {
            if (apx5 instanceof LivingEntity && ((LivingEntity)apx5).removeAllEffects()) {
                ++integer3;
            }
        }
        if (integer3 == 0) {
            throw EffectCommands.ERROR_CLEAR_EVERYTHING_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.everything.success.single", new Object[] { ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.everything.success.multiple", new Object[] { collection.size() }), true);
        }
        return integer3;
    }
    
    private static int clearEffect(final CommandSourceStack db, final Collection<? extends Entity> collection, final MobEffect app) throws CommandSyntaxException {
        int integer4 = 0;
        for (final Entity apx6 : collection) {
            if (apx6 instanceof LivingEntity && ((LivingEntity)apx6).removeEffect(app)) {
                ++integer4;
            }
        }
        if (integer4 == 0) {
            throw EffectCommands.ERROR_CLEAR_SPECIFIC_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.specific.success.single", new Object[] { app.getDisplayName(), ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.specific.success.multiple", new Object[] { app.getDisplayName(), collection.size() }), true);
        }
        return integer4;
    }
    
    static {
        ERROR_GIVE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.effect.give.failed"));
        ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.effect.clear.everything.failed"));
        ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.effect.clear.specific.failed"));
    }
}
