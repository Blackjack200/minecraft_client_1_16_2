package net.minecraft.network.protocol.handshake;

import net.minecraft.network.PacketListener;

public interface ServerHandshakePacketListener extends PacketListener {
    void handleIntention(final ClientIntentionPacket tv);
}
