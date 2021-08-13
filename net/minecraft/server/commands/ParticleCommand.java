package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ParticleArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ParticleCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("particle").requires(db -> db.hasPermission(2))).then(((RequiredArgumentBuilder)Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)ParticleArgument.particle()).executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), ((CommandSourceStack)commandContext.getSource()).getPosition(), Vec3.ZERO, 0.0f, 0, false, (Collection<ServerPlayer>)((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getPlayers()))).then(((RequiredArgumentBuilder)Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), Vec3.ZERO, 0.0f, 0, false, (Collection<ServerPlayer>)((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getPlayers()))).then(Commands.argument("delta", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3(false)).then(Commands.argument("speed", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(0.0f)).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("count", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), false, (Collection<ServerPlayer>)((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getPlayers()))).then(((LiteralArgumentBuilder)Commands.literal("force").executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), true, (Collection<ServerPlayer>)((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getPlayers()))).then(Commands.argument("viewers", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), true, EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "viewers")))))).then(((LiteralArgumentBuilder)Commands.literal("normal").executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), false, (Collection<ServerPlayer>)((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getPlayers()))).then(Commands.argument("viewers", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> sendParticles((CommandSourceStack)commandContext.getSource(), ParticleArgument.getParticle((CommandContext<CommandSourceStack>)commandContext, "name"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), false, EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "viewers")))))))))));
    }
    
    private static int sendParticles(final CommandSourceStack db, final ParticleOptions hf, final Vec3 dck3, final Vec3 dck4, final float float5, final int integer, final boolean boolean7, final Collection<ServerPlayer> collection) throws CommandSyntaxException {
        int integer2 = 0;
        for (final ServerPlayer aah11 : collection) {
            if (db.getLevel().<ParticleOptions>sendParticles(aah11, hf, boolean7, dck3.x, dck3.y, dck3.z, integer, dck4.x, dck4.y, dck4.z, float5)) {
                ++integer2;
            }
        }
        if (integer2 == 0) {
            throw ParticleCommand.ERROR_FAILED.create();
        }
        db.sendSuccess(new TranslatableComponent("commands.particle.success", new Object[] { Registry.PARTICLE_TYPE.getKey(hf.getType()).toString() }), true);
        return integer2;
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.particle.failed"));
    }
}
