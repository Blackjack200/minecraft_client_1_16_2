package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPingRequestPacket implements Packet<ServerStatusPacketListener> {
    private long time;
    
    public ServerboundPingRequestPacket() {
    }
    
    public ServerboundPingRequestPacket(final long long1) {
        this.time = long1;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.time = nf.readLong();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeLong(this.time);
    }
    
    public void handle(final ServerStatusPacketListener uo) {
        uo.handlePingRequest(this);
    }
    
    public long getTime() {
        return this.time;
    }
}
