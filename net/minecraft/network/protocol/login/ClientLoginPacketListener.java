package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;

public interface ClientLoginPacketListener extends PacketListener {
    void handleHello(final ClientboundHelloPacket ub);
    
    void handleGameProfile(final ClientboundGameProfilePacket ua);
    
    void handleDisconnect(final ClientboundLoginDisconnectPacket ud);
    
    void handleCompression(final ClientboundLoginCompressionPacket uc);
    
    void handleCustomQuery(final ClientboundCustomQueryPacket tz);
}
