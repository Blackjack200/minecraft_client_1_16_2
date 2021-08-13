package net.minecraft.network;

import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;

public class RateKickingConnection extends Connection {
    private static final Logger LOGGER;
    private static final Component EXCEED_REASON;
    private final int rateLimitPacketsPerSecond;
    
    public RateKickingConnection(final int integer) {
        super(PacketFlow.SERVERBOUND);
        this.rateLimitPacketsPerSecond = integer;
    }
    
    @Override
    protected void tickSecond() {
        super.tickSecond();
        final float float2 = this.getAverageReceivedPackets();
        if (float2 > this.rateLimitPacketsPerSecond) {
            RateKickingConnection.LOGGER.warn("Player exceeded rate-limit (sent {} packets per second)", float2);
            this.send(new ClientboundDisconnectPacket(RateKickingConnection.EXCEED_REASON), (future -> this.disconnect(RateKickingConnection.EXCEED_REASON)));
            this.setReadOnly();
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EXCEED_REASON = new TranslatableComponent("disconnect.exceeded_packet_rate");
    }
}
