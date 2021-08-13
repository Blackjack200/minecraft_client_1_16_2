package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundKeepAlivePacket implements Packet<ServerGamePacketListener> {
    private long id;
    
    public ServerboundKeepAlivePacket() {
    }
    
    public ServerboundKeepAlivePacket(final long long1) {
        this.id = long1;
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleKeepAlive(this);
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
