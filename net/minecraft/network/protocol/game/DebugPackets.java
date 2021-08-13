package net.minecraft.network.protocol.game;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raid;
import java.util.Collection;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.WorldGenLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.network.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;

public class DebugPackets {
    private static final Logger LOGGER;
    
    public static void sendGameTestAddMarker(final ServerLevel aag, final BlockPos fx, final String string, final int integer4, final int integer5) {
        final FriendlyByteBuf nf6 = new FriendlyByteBuf(Unpooled.buffer());
        nf6.writeBlockPos(fx);
        nf6.writeInt(integer4);
        nf6.writeUtf(string);
        nf6.writeInt(integer5);
        sendPacketToAllPlayers(aag, nf6, ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER);
    }
    
    public static void sendGameTestClearPacket(final ServerLevel aag) {
        final FriendlyByteBuf nf2 = new FriendlyByteBuf(Unpooled.buffer());
        sendPacketToAllPlayers(aag, nf2, ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_CLEAR);
    }
    
    public static void sendPoiPacketsForChunk(final ServerLevel aag, final ChunkPos bra) {
    }
    
    public static void sendPoiAddedPacket(final ServerLevel aag, final BlockPos fx) {
        sendVillageSectionsPacket(aag, fx);
    }
    
    public static void sendPoiRemovedPacket(final ServerLevel aag, final BlockPos fx) {
        sendVillageSectionsPacket(aag, fx);
    }
    
    public static void sendPoiTicketCountPacket(final ServerLevel aag, final BlockPos fx) {
        sendVillageSectionsPacket(aag, fx);
    }
    
    private static void sendVillageSectionsPacket(final ServerLevel aag, final BlockPos fx) {
    }
    
    public static void sendPathFindingPacket(final Level bru, final Mob aqk, @Nullable final Path cxa, final float float4) {
    }
    
    public static void sendNeighborsUpdatePacket(final Level bru, final BlockPos fx) {
    }
    
    public static void sendStructurePacket(final WorldGenLevel bso, final StructureStart<?> crs) {
    }
    
    public static void sendGoalSelector(final Level bru, final Mob aqk, final GoalSelector avt) {
        if (!(bru instanceof ServerLevel)) {
            return;
        }
    }
    
    public static void sendRaids(final ServerLevel aag, final Collection<Raid> collection) {
    }
    
    public static void sendEntityBrain(final LivingEntity aqj) {
    }
    
    public static void sendBeeInfo(final Bee azx) {
    }
    
    public static void sendHiveInfo(final BeehiveBlockEntity ccd) {
    }
    
    private static void sendPacketToAllPlayers(final ServerLevel aag, final FriendlyByteBuf nf, final ResourceLocation vk) {
        final Packet<?> oj4 = new ClientboundCustomPayloadPacket(vk, nf);
        for (final Player bft6 : aag.getLevel().players()) {
            ((ServerPlayer)bft6).connection.send(oj4);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
