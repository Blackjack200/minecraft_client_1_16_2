package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundKeepAlivePacket implements Packet<ClientGamePacketListener> {
    private long id;
    
    public ClientboundKeepAlivePacket() {
    }
    
    public ClientboundKeepAlivePacket(final long long1) {
        this.id = long1;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleKeepAlive(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readLong();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeLong(this.id);
    }
    
    public long getId() {
        return this.id;
    }
}
