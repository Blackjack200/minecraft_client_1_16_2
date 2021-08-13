package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundPongResponsePacket implements Packet<ClientStatusPacketListener> {
    private long time;
    
    public ClientboundPongResponsePacket() {
    }
    
    public ClientboundPongResponsePacket(final long long1) {
        this.time = long1;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.time = nf.readLong();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeLong(this.time);
    }
    
    public void handle(final ClientStatusPacketListener uk) {
        uk.handlePongResponse(this);
    }
}
