package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;

public interface ClientStatusPacketListener extends PacketListener {
    void handleStatusResponse(final ClientboundStatusResponsePacket um);
    
    void handlePongResponse(final ClientboundPongResponsePacket ul);
}
