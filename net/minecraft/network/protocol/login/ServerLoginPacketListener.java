package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;

public interface ServerLoginPacketListener extends PacketListener {
    void handleHello(final ServerboundHelloPacket ug);
    
    void handleKey(final ServerboundKeyPacket uh);
    
    void handleCustomQueryPacket(final ServerboundCustomQueryPacket uf);
}
