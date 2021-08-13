package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.commands.arguments.selector.EntitySelector;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.sounds.SoundSource;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class StopSoundCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final RequiredArgumentBuilder<CommandSourceStack, EntitySelector> requiredArgumentBuilder2 = (RequiredArgumentBuilder<CommandSourceStack, EntitySelector>)((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> stopSound((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), null, null))).then(Commands.literal("*").then(Commands.argument("sound", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)SuggestionProviders.AVAILABLE_SOUNDS).executes(commandContext -> stopSound((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), null, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "sound")))));
        for (final SoundSource adp6 : SoundSource.values()) {
            requiredArgumentBuilder2.then(((LiteralArgumentBuilder)Commands.literal(adp6.getName()).executes(commandContext -> stopSound((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), adp6, null))).then(Commands.argument("sound", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)SuggestionProviders.AVAILABLE_SOUNDS).executes(commandContext -> stopSound((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), adp6, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "sound")))));
        }
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stopsound").requires(db -> db.hasPermission(2))).then((ArgumentBuilder)requiredArgumentBuilder2));
    }
    
    private static int stopSound(final CommandSourceStack db, final Collection<ServerPlayer> collection, @Nullable final SoundSource adp, @Nullable final ResourceLocation vk) {
        final ClientboundStopSoundPacket ro5 = new ClientboundStopSoundPacket(vk, adp);
        for (final ServerPlayer aah7 : collection) {
            aah7.connection.send(ro5);
        }
        if (adp != null) {
            if (vk != null) {
                db.sendSuccess(new TranslatableComponent("commands.stopsound.success.source.sound", new Object[] { vk, adp.getName() }), true);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.stopsound.success.source.any", new Object[] { adp.getName() }), true);
            }
        }
        else if (vk != null) {
            db.sendSuccess(new TranslatableComponent("commands.stopsound.success.sourceless.sound", new Object[] { vk }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.stopsound.success.sourceless.any"), true);
        }
        return collection.size();
    }
}
