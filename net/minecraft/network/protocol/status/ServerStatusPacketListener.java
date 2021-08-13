package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;

public interface ServerStatusPacketListener extends PacketListener {
    void handlePingRequest(final ServerboundPingRequestPacket up);
    
    void handleStatusRequest(final ServerboundStatusRequestPacket uq);
}
