package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import java.util.Collections;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Set;
import java.util.EnumSet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class TeleportCommand {
    private static final SimpleCommandExceptionType INVALID_POSITION;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralCommandNode<CommandSourceStack> literalCommandNode2 = (LiteralCommandNode<CommandSourceStack>)commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("teleport").requires(db -> db.hasPermission(2))).then(((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("location", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).executes(commandContext -> teleportToPos((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates((CommandContext<CommandSourceStack>)commandContext, "location"), null, null))).then(Commands.argument("rotation", (com.mojang.brigadier.arguments.ArgumentType<Object>)RotationArgument.rotation()).executes(commandContext -> teleportToPos((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates((CommandContext<CommandSourceStack>)commandContext, "location"), RotationArgument.getRotation((CommandContext<CommandSourceStack>)commandContext, "rotation"), null)))).then(((LiteralArgumentBuilder)Commands.literal("facing").then(Commands.literal("entity").then(((RequiredArgumentBuilder)Commands.argument("facingEntity", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entity()).executes(commandContext -> teleportToPos((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates((CommandContext<CommandSourceStack>)commandContext, "location"), null, new LookAt(EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "facingEntity"), EntityAnchorArgument.Anchor.FEET)))).then(Commands.argument("facingAnchor", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityAnchorArgument.anchor()).executes(commandContext -> teleportToPos((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates((CommandContext<CommandSourceStack>)commandContext, "location"), null, new LookAt(EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "facingEntity"), EntityAnchorArgument.getAnchor((CommandContext<CommandSourceStack>)commandContext, "facingAnchor")))))))).then(Commands.argument("facingLocation", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).executes(commandContext -> teleportToPos((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ((CommandSourceStack)commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates((CommandContext<CommandSourceStack>)commandContext, "location"), null, new LookAt(Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "facingLocation")))))))).then(Commands.argument("destination", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entity()).executes(commandContext -> teleportToEntity((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "destination")))))).then(Commands.argument("location", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).executes(commandContext -> teleportToPos((CommandSourceStack)commandContext.getSource(), Collections.singleton(((CommandSourceStack)commandContext.getSource()).getEntityOrException()), ((CommandSourceStack)commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates((CommandContext<CommandSourceStack>)commandContext, "location"), WorldCoordinates.current(), null)))).then(Commands.argument("destination", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entity()).executes(commandContext -> teleportToEntity((CommandSourceStack)commandContext.getSource(), Collections.singleton(((CommandSourceStack)commandContext.getSource()).getEntityOrException()), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "destination")))));
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tp").requires(db -> db.hasPermission(2))).redirect((CommandNode)literalCommandNode2));
    }
    
    private static int teleportToEntity(final CommandSourceStack db, final Collection<? extends Entity> collection, final Entity apx) throws CommandSyntaxException {
        for (final Entity apx2 : collection) {
            performTeleport(db, apx2, (ServerLevel)apx.level, apx.getX(), apx.getY(), apx.getZ(), (Set<ClientboundPlayerPositionPacket.RelativeArgument>)EnumSet.noneOf((Class)ClientboundPlayerPositionPacket.RelativeArgument.class), apx.yRot, apx.xRot, null);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.teleport.success.entity.single", new Object[] { ((Entity)collection.iterator().next()).getDisplayName(), apx.getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.teleport.success.entity.multiple", new Object[] { collection.size(), apx.getDisplayName() }), true);
        }
        return collection.size();
    }
    
    private static int teleportToPos(final CommandSourceStack db, final Collection<? extends Entity> collection, final ServerLevel aag, final Coordinates em4, @Nullable final Coordinates em5, @Nullable final LookAt a) throws CommandSyntaxException {
        final Vec3 dck7 = em4.getPosition(db);
        final Vec2 dcj8 = (em5 == null) ? null : em5.getRotation(db);
        final Set<ClientboundPlayerPositionPacket.RelativeArgument> set9 = (Set<ClientboundPlayerPositionPacket.RelativeArgument>)EnumSet.noneOf((Class)ClientboundPlayerPositionPacket.RelativeArgument.class);
        if (em4.isXRelative()) {
            set9.add(ClientboundPlayerPositionPacket.RelativeArgument.X);
        }
        if (em4.isYRelative()) {
            set9.add(ClientboundPlayerPositionPacket.RelativeArgument.Y);
        }
        if (em4.isZRelative()) {
            set9.add(ClientboundPlayerPositionPacket.RelativeArgument.Z);
        }
        if (em5 == null) {
            set9.add(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT);
            set9.add(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT);
        }
        else {
            if (em5.isXRelative()) {
                set9.add(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT);
            }
            if (em5.isYRelative()) {
                set9.add(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT);
            }
        }
        for (final Entity apx11 : collection) {
            if (em5 == null) {
                performTeleport(db, apx11, aag, dck7.x, dck7.y, dck7.z, set9, apx11.yRot, apx11.xRot, a);
            }
            else {
                performTeleport(db, apx11, aag, dck7.x, dck7.y, dck7.z, set9, dcj8.y, dcj8.x, a);
            }
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.teleport.success.location.single", new Object[] { ((Entity)collection.iterator().next()).getDisplayName(), dck7.x, dck7.y, dck7.z }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.teleport.success.location.multiple", new Object[] { collection.size(), dck7.x, dck7.y, dck7.z }), true);
        }
        return collection.size();
    }
    
    private static void performTeleport(final CommandSourceStack db, Entity apx, final ServerLevel aag, final double double4, final double double5, final double double6, final Set<ClientboundPlayerPositionPacket.RelativeArgument> set, final float float8, final float float9, @Nullable final LookAt a) throws CommandSyntaxException {
        final BlockPos fx14 = new BlockPos(double4, double5, double6);
        if (!Level.isInSpawnableBounds(fx14)) {
            throw TeleportCommand.INVALID_POSITION.create();
        }
        if (apx instanceof ServerPlayer) {
            final ChunkPos bra15 = new ChunkPos(new BlockPos(double4, double5, double6));
            aag.getChunkSource().<Integer>addRegionTicket(TicketType.POST_TELEPORT, bra15, 1, apx.getId());
            apx.stopRiding();
            if (((ServerPlayer)apx).isSleeping()) {
                ((ServerPlayer)apx).stopSleepInBed(true, true);
            }
            if (aag == apx.level) {
                ((ServerPlayer)apx).connection.teleport(double4, double5, double6, float8, float9, set);
            }
            else {
                ((ServerPlayer)apx).teleportTo(aag, double4, double5, double6, float8, float9);
            }
            apx.setYHeadRot(float8);
        }
        else {
            final float float10 = Mth.wrapDegrees(float8);
            float float11 = Mth.wrapDegrees(float9);
            float11 = Mth.clamp(float11, -90.0f, 90.0f);
            if (aag == apx.level) {
                apx.moveTo(double4, double5, double6, float10, float11);
                apx.setYHeadRot(float10);
            }
            else {
                apx.unRide();
                final Entity apx2 = apx;
                apx = (Entity)apx2.getType().create(aag);
                if (apx == null) {
                    return;
                }
                apx.restoreFrom(apx2);
                apx.moveTo(double4, double5, double6, float10, float11);
                apx.setYHeadRot(float10);
                aag.addFromAnotherDimension(apx);
                apx2.removed = true;
            }
        }
        if (a != null) {
            a.perform(db, apx);
        }
        if (!(apx instanceof LivingEntity) || !((LivingEntity)apx).isFallFlying()) {
            apx.setDeltaMovement(apx.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            apx.setOnGround(true);
        }
        if (apx instanceof PathfinderMob) {
            ((PathfinderMob)apx).getNavigation().stop();
        }
    }
    
    static {
        INVALID_POSITION = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.teleport.invalidPosition"));
    }
    
    static class LookAt {
        private final Vec3 position;
        private final Entity entity;
        private final EntityAnchorArgument.Anchor anchor;
        
        public LookAt(final Entity apx, final EntityAnchorArgument.Anchor a) {
            this.entity = apx;
            this.anchor = a;
            this.position = a.apply(apx);
        }
        
        public LookAt(final Vec3 dck) {
            this.entity = null;
            this.position = dck;
            this.anchor = null;
        }
        
        public void perform(final CommandSourceStack db, final Entity apx) {
            if (this.entity != null) {
                if (apx instanceof ServerPlayer) {
                    ((ServerPlayer)apx).lookAt(db.getAnchor(), this.entity, this.anchor);
                }
                else {
                    apx.lookAt(db.getAnchor(), this.position);
                }
            }
            else {
                apx.lookAt(db.getAnchor(), this.position);
            }
        }
    }
}
