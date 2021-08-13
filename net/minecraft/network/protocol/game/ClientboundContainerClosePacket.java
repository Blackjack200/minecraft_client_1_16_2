package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundContainerClosePacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    
    public ClientboundContainerClosePacket() {
    }
    
    public ClientboundContainerClosePacket(final int integer) {
        this.containerId = integer;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleContainerClose(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readUnsignedByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
    }
}
