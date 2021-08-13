package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundContainerClosePacket implements Packet<ServerGamePacketListener> {
    private int containerId;
    
    public ServerboundContainerClosePacket() {
    }
    
    public ServerboundContainerClosePacket(final int integer) {
        this.containerId = integer;
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleContainerClose(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
    }
}
