package net.minecraft.network.protocol.handshake;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.SharedConstants;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;

public class ClientIntentionPacket implements Packet<ServerHandshakePacketListener> {
    private int protocolVersion;
    private String hostName;
    private int port;
    private ConnectionProtocol intention;
    
    public ClientIntentionPacket() {
    }
    
    public ClientIntentionPacket(final String string, final int integer, final ConnectionProtocol ne) {
        this.protocolVersion = SharedConstants.getCurrentVersion().getProtocolVersion();
        this.hostName = string;
        this.port = integer;
        this.intention = ne;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.protocolVersion = nf.readVarInt();
        this.hostName = nf.readUtf(255);
        this.port = nf.readUnsignedShort();
        this.intention = ConnectionProtocol.getById(nf.readVarInt());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.protocolVersion);
        nf.writeUtf(this.hostName);
        nf.writeShort(this.port);
        nf.writeVarInt(this.intention.getId());
    }
    
    public void handle(final ServerHandshakePacketListener tw) {
        tw.handleIntention(this);
    }
    
    public ConnectionProtocol getIntention() {
        return this.intention;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
}
